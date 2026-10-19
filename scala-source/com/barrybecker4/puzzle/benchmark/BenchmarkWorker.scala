// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.benchmark

import java.io.{OutputStream, PrintStream}

/**
  * Runs a single benchmark solve in its own JVM and prints the outcome as one line (see [[WorkerResult]]).
  *
  * Each run gets a fresh JVM so that a solver that is stuck in a long computation can be killed when it
  * exceeds the time limit. A JVM cannot forcibly stop a thread, and a solver left running in the background
  * would steal CPU from every later measurement.
  *
  * Arguments: puzzle, variant, algorithm, seed. It is started by [[ProcessRunner]], not by hand.
  */
object BenchmarkWorker {

  def main(args: Array[String]): Unit = {
    val realOut = System.out
    // A solver that dies on one of its own threads would otherwise leave the main thread waiting until the time
    // limit, and the failure would be reported as a timeout instead of the error it is.
    Thread.setDefaultUncaughtExceptionHandler { (_, error) =>
      realOut.println(WorkerResult.formatError(error))
      realOut.flush()
      error.printStackTrace()
      Runtime.getRuntime.halt(4)
    }
    val status =
      try runWorker(args.toList)
      catch {
        case t: Throwable =>
          realOut.println(WorkerResult.formatError(t))
          t.printStackTrace()
          3
      }
    System.out.flush()
    // Exit explicitly: concurrent solvers can leave non-daemon threads running.
    System.exit(status)
  }

  private def runWorker(args: List[String]): Int = args match {
    case puzzle :: variant :: algorithm :: seed :: Nil =>
      BenchmarkCases.find(puzzle, variant, algorithm) match {
        case Some(benchCase) =>
          val prepared = benchCase.prepare(seed.toLong)
          val realOut = System.out
          // The puzzle controllers print the solution board, which would bury the result line.
          val quiet = new PrintStream(OutputStream.nullOutputStream())
          val start = System.nanoTime()
          val found = Console.withOut(quiet)(prepared.solve())
          val millis = (System.nanoTime() - start) / 1000000L
          realOut.println(WorkerResult.format(found, millis, prepared.tries()))
          0
        case None =>
          Console.err.println(s"Unknown benchmark case: $puzzle | $variant | $algorithm")
          2
      }
    case _ =>
      Console.err.println("Usage: BenchmarkWorker <puzzle> <variant> <algorithm> <seed>")
      2
  }
}
