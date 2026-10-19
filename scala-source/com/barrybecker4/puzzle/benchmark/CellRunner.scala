// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.benchmark

import com.barrybecker4.search.Refreshable

import java.io.{OutputStream, PrintStream}
import java.util.concurrent.atomic.AtomicReference
import scala.collection.mutable.ListBuffer

/** What happened in one run of one algorithm on one puzzle variant. */
sealed trait RunOutcome
final case class Solved(millis: Long, tries: Long) extends RunOutcome
/** Finished within the time limit, but did not find a solution. */
final case class Unsolved(millis: Long, tries: Long) extends RunOutcome
final case class TimedOut(tries: Long) extends RunOutcome
final case class Errored(message: String) extends RunOutcome

/**
  * A [[Refreshable]] that records progress and can be told to abort the solve.
  * Solvers report progress through the puzzle controller, which calls [[refresh]] here, so throwing an
  * InterruptedException from it is the cooperative way to stop a solver that has run out of time.
  */
final class ProbeRefreshable[P, M] extends Refreshable[P, M] {

  @volatile private var cancelled = false
  @volatile private var tries: Long = 0

  def cancel(): Unit = cancelled = true
  def numTries: Long = tries

  override def refresh(state: P, numTries: Long): Unit = {
    tries = numTries
    if (cancelled) throw new InterruptedException("Benchmark time limit reached")
  }

  override def animateTransition(transition: M): P =
    throw new UnsupportedOperationException("Not used when benchmarking")

  override def finalRefresh(path: Option[Seq[M]], state: Option[P], numTries: Long, elapsedMillis: Long): Unit =
    tries = numTries
}

/**
  * A single run that is ready to execute. Everything that is not part of the timed work (building the start
  * position, constructing the solver) has been done already.
  *
  * @param cancel asks the solver to stop at its next progress report
  * @param tries  the solver's current/final reported number of tries
  * @param solve  runs the solver; returns true if it found a solution
  */
final class PreparedRun(val cancel: () => Unit, val tries: () => Long, val solve: () => Boolean)

/** Runs prepared solves with a time limit and aggregates the outcomes. */
object CellRunner {

  /** After this many timeouts or errors in a row the remaining repetitions of a cell are skipped. */
  val MAX_CONSECUTIVE_TIMEOUTS = 2
  private val STOP_GRACE_MILLIS = 5000L

  /**
    * @param isolated true (the default) to run each repetition in its own JVM that is killed on timeout.
    *                 False runs on a thread in this JVM, where a solver that stops reporting progress cannot be stopped
    *                 and keeps running in the background (only meant for tests and quick experiments).
    */
  def runCase(benchCase: BenchmarkCase, repetitions: Int, timeoutSeconds: Int, isolated: Boolean = true): CellResult = {
    val outcomes = ListBuffer[RunOutcome]()
    var consecutiveTimeouts = 0
    var rep = 0
    while (rep < repetitions && consecutiveTimeouts < MAX_CONSECUTIVE_TIMEOUTS) {
      val seed = rep + 1L
      val outcome =
        if (isolated) ProcessRunner.runOnce(benchCase, seed, timeoutSeconds * 1000L)
        else runOnce(benchCase.prepare(seed), timeoutSeconds * 1000L)
      outcomes += outcome
      outcome match {
        case _: TimedOut | _: Errored => consecutiveTimeouts += 1
        case _ => consecutiveTimeouts = 0
      }
      rep += 1
    }
    CellStats.aggregate(benchCase.puzzle, benchCase.variant, benchCase.algorithm, outcomes.toList)
  }

  /**
    * Runs the solve on its own thread in this JVM, and stops it at its next progress report if it exceeds the time limit.
    * A solver that never reports progress cannot be stopped this way; see [[ProcessRunner]] for the reliable way.
    */
  def runOnce(prepared: PreparedRun, timeoutMillis: Long): RunOutcome = {
    val outcome = new AtomicReference[RunOutcome](Errored("The solver did not report a result"))
    val worker = new Thread(new Runnable {
      override def run(): Unit = {
        try {
          // The puzzle controllers print the solution board, which would bury the benchmark's own output.
          val quiet = new PrintStream(OutputStream.nullOutputStream())
          val start = System.nanoTime()
          val found = Console.withOut(quiet)(prepared.solve())
          val millis = (System.nanoTime() - start) / 1000000L
          outcome.set(if (found) Solved(millis, prepared.tries()) else Unsolved(millis, prepared.tries()))
        } catch {
          case _: InterruptedException => outcome.set(TimedOut(prepared.tries()))
          case t: Throwable => outcome.set(Errored(t.toString))
        }
      }
    }, "benchmark-solver")
    worker.setDaemon(true)
    worker.start()
    worker.join(timeoutMillis)
    if (worker.isAlive) {
      prepared.cancel()
      worker.interrupt()
      worker.join(STOP_GRACE_MILLIS)
      if (worker.isAlive) {
        Console.err.println("WARNING: a solver did not stop after the time limit; later timings may be skewed.")
      }
      // Whatever the worker reported while winding down, this run exceeded the time limit.
      TimedOut(prepared.tries())
    } else outcome.get()
  }
}

/** Turns raw run outcomes into the summary stored in the results file. */
object CellStats {

  def aggregate(puzzle: String, variant: String, algorithm: String, outcomes: List[RunOutcome]): CellResult = {
    val solvedRuns = outcomes.collect { case s: Solved => s }
    val millis = solvedRuns.map(_.millis)
    CellResult(
      puzzle = puzzle,
      variant = variant,
      algorithm = algorithm,
      attempts = outcomes.size,
      solved = solvedRuns.size,
      unsolved = outcomes.count(_.isInstanceOf[Unsolved]),
      timedOut = outcomes.count(_.isInstanceOf[TimedOut]),
      errored = outcomes.count(_.isInstanceOf[Errored]),
      medianMillis = median(millis),
      minMillis = millis.minOption,
      maxMillis = millis.maxOption,
      medianTries = median(solvedRuns.map(_.tries)),
      lastError = outcomes.collect { case Errored(message) => message }.lastOption
    )
  }

  def median(values: List[Long]): Option[Long] =
    if (values.isEmpty) None
    else {
      val sorted = values.sorted
      val n = sorted.size
      Some(if (n % 2 == 1) sorted(n / 2) else (sorted(n / 2 - 1) + sorted(n / 2)) / 2)
    }
}
