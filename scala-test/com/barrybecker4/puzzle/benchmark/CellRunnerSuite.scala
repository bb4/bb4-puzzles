// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.benchmark

import org.scalatest.funsuite.AnyFunSuite

class CellRunnerSuite extends AnyFunSuite {

  private def prepared(tries: Long = 0L)(solve: => Boolean): PreparedRun =
    new PreparedRun(() => (), () => tries, () => solve)

  test("a solve that finds a solution is reported with its time and tries") {
    CellRunner.runOnce(prepared(tries = 7)(true), 5000) match {
      case Solved(millis, tries) =>
        assert(millis >= 0)
        assert(tries == 7)
      case other => fail(s"Expected Solved but got $other")
    }
  }

  test("a solve that finishes without a solution is reported as unsolved") {
    assert(CellRunner.runOnce(prepared()(false), 5000).isInstanceOf[Unsolved])
  }

  test("a solve that runs past the time limit is reported as timed out") {
    val outcome = CellRunner.runOnce(prepared(tries = 3) { Thread.sleep(60000); true }, 100)
    assert(outcome == TimedOut(3))
  }

  test("a solve that throws is reported as an error") {
    CellRunner.runOnce(prepared()(throw new IllegalStateException("boom")), 5000) match {
      case Errored(message) => assert(message.contains("boom"))
      case other => fail(s"Expected Errored but got $other")
    }
  }

  test("the solver's console output is suppressed") {
    val captured = new java.io.ByteArrayOutputStream()
    Console.withOut(new java.io.PrintStream(captured)) {
      CellRunner.runOnce(prepared() { println("noisy solver output"); true }, 5000)
    }
    assert(captured.toString.isEmpty)
  }

  test("repetitions stop after consecutive timeouts") {
    var prepareCalls = 0
    val benchCase = BenchmarkCase("P", "v", "A", _ => {
      prepareCalls += 1
      prepared() { Thread.sleep(60000); true }
    })
    val result = CellRunner.runCase(benchCase, repetitions = 5, timeoutSeconds = 1, isolated = false)
    assert(prepareCalls == CellRunner.MAX_CONSECUTIVE_TIMEOUTS)
    assert(result.attempts == CellRunner.MAX_CONSECUTIVE_TIMEOUTS)
    assert(result.timedOut == CellRunner.MAX_CONSECUTIVE_TIMEOUTS)
    assert(!result.wasSolved)
  }

  test("a cell aggregates all its repetitions") {
    val benchCase = BenchmarkCase("P", "v", "A", seed => prepared(tries = seed * 10)(true))
    val result = CellRunner.runCase(benchCase, repetitions = 3, timeoutSeconds = 5, isolated = false)
    assert(result.attempts == 3)
    assert(result.solved == 3)
    assert(result.medianTries.contains(20L))
  }
}
