// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.benchmark

import java.io.{BufferedReader, IOException, InputStreamReader}
import java.lang.management.ManagementFactory
import java.nio.charset.StandardCharsets
import java.nio.file.Paths
import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.{ConcurrentLinkedDeque, TimeUnit}
import scala.jdk.CollectionConverters.*

/** The one line a [[BenchmarkWorker]] prints to report how its solve went. */
object WorkerResult {

  val PREFIX = "BENCH_RESULT "

  def format(found: Boolean, millis: Long, tries: Long): String =
    s"$PREFIX${if (found) "solved" else "unsolved"} $millis $tries"

  /** @return the outcome described by the line, or None if it is not a well formed result line */
  def parse(line: String): Option[RunOutcome] =
    if (!line.startsWith(PREFIX)) None
    else
      line.substring(PREFIX.length).trim.split(' ').toList match {
        case "solved" :: millis :: tries :: Nil => toLongs(millis, tries).map { case (m, t) => Solved(m, t) }
        case "unsolved" :: millis :: tries :: Nil => toLongs(millis, tries).map { case (m, t) => Unsolved(m, t) }
        case _ => None
      }

  private def toLongs(a: String, b: String): Option[(Long, Long)] =
    try Some((a.toLong, b.toLong))
    catch {
      case _: NumberFormatException => None
    }
}

/**
  * Runs each benchmark solve in a separate JVM, with the same classpath and JVM options as this one, and kills
  * the process if it exceeds the time limit. Unlike stopping a thread, this reliably ends a solver that is stuck
  * in a long computation, and nothing that one run does can affect the measurements of the next.
  */
object ProcessRunner {

  private val WORKER_CLASS = "com.barrybecker4.puzzle.benchmark.BenchmarkWorker"

  /** Time allowed on top of the limit for JVM start up and building the puzzle (not part of the measured time). */
  private val STARTUP_ALLOWANCE_MILLIS = 20000L
  private val KILL_GRACE_SECONDS = 10L
  private val TAIL_LINES = 10

  def runOnce(benchCase: BenchmarkCase, seed: Long, timeoutMillis: Long): RunOutcome = {
    val process = new ProcessBuilder(command(benchCase, seed).asJava).redirectErrorStream(true).start()
    val resultLine = new AtomicReference[String](null)
    val tail = new ConcurrentLinkedDeque[String]()

    val reader = new Thread(new Runnable {
      override def run(): Unit = {
        val in = new BufferedReader(new InputStreamReader(process.getInputStream, StandardCharsets.UTF_8))
        try {
          var line = in.readLine()
          while (line != null) {
            if (line.startsWith(WorkerResult.PREFIX)) resultLine.set(line)
            else {
              tail.addLast(line)
              while (tail.size > TAIL_LINES) tail.pollFirst()
            }
            line = in.readLine()
          }
        } catch {
          case _: IOException => () // the process was killed
        } finally in.close()
      }
    }, "benchmark-worker-output")
    reader.setDaemon(true)
    reader.start()

    if (process.waitFor(timeoutMillis + STARTUP_ALLOWANCE_MILLIS, TimeUnit.MILLISECONDS)) {
      reader.join(5000L)
      interpret(resultLine.get(), process.exitValue(), tail.asScala.toList, timeoutMillis)
    } else {
      process.destroyForcibly()
      process.waitFor(KILL_GRACE_SECONDS, TimeUnit.SECONDS)
      reader.join(2000L)
      TimedOut(0L)
    }
  }

  /** Turns what a worker left behind into an outcome. A solve that finished, but took longer than the limit, timed out. */
  def interpret(resultLine: String, exitCode: Int, tail: List[String], timeoutMillis: Long): RunOutcome =
    Option(resultLine).flatMap(WorkerResult.parse) match {
      case Some(Solved(millis, tries)) if millis > timeoutMillis => TimedOut(tries)
      case Some(Unsolved(millis, tries)) if millis > timeoutMillis => TimedOut(tries)
      case Some(outcome) => outcome
      case None =>
        val details = if (tail.isEmpty) "" else ": " + tail.mkString(" | ")
        Errored(s"worker exited with code $exitCode without reporting a result$details")
    }

  private def command(benchCase: BenchmarkCase, seed: Long): List[String] = {
    val java = Paths.get(System.getProperty("java.home"), "bin", "java").toString
    // Same JVM options (heap, stack, assertions) as this JVM, but never a debugger or agent port that is already taken.
    val jvmArgs = ManagementFactory.getRuntimeMXBean.getInputArguments.asScala.toList
      .filterNot(arg => arg.startsWith("-agentlib:jdwp") || arg.startsWith("-javaagent"))
    List(java) ++ jvmArgs ++ List(
      "-cp", System.getProperty("java.class.path"),
      WORKER_CLASS, benchCase.puzzle, benchCase.variant, benchCase.algorithm, seed.toString
    )
  }
}
