// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.benchmark

import org.scalatest.funsuite.AnyFunSuite

class ProcessRunnerSuite extends AnyFunSuite {

  test("worker results survive being formatted and parsed") {
    assert(WorkerResult.parse(WorkerResult.format(found = true, millis = 1234, tries = 56)) == Some(Solved(1234, 56)))
    assert(WorkerResult.parse(WorkerResult.format(found = false, millis = 7, tries = 0)) == Some(Unsolved(7, 0)))
  }

  test("lines that are not well formed results are ignored") {
    List("", "some solver output", "BENCH_RESULT", "BENCH_RESULT solved 12", "BENCH_RESULT maybe 1 2",
      "BENCH_RESULT solved x 2", "BENCH_RESULT solved 1 2 3").foreach { line =>
      assert(WorkerResult.parse(line).isEmpty, line)
    }
  }

  test("a reported result is the outcome") {
    val solved = WorkerResult.format(found = true, millis = 900, tries = 5)
    assert(ProcessRunner.interpret(solved, null, 0, Nil, 60000) == Solved(900, 5))
    val unsolved = WorkerResult.format(found = false, millis = 900, tries = 5)
    assert(ProcessRunner.interpret(unsolved, null, 0, Nil, 60000) == Unsolved(900, 5))
  }

  test("a solve that finished but took longer than the limit timed out") {
    val slow = WorkerResult.format(found = true, millis = 61000, tries = 5)
    assert(ProcessRunner.interpret(slow, null, 0, Nil, 60000) == TimedOut(5))
  }

  test("a worker that reported nothing is an error that includes its last output") {
    ProcessRunner.interpret(null, null, 3, List("Exception in thread main", "  at Foo.bar"), 60000) match {
      case Errored(message) =>
        assert(message.contains("code 3"))
        assert(message.contains("Exception in thread main"))
      case other => fail(s"Expected Errored but got $other")
    }
  }

  test("an error line names the exception and its message") {
    val error = new IndexOutOfBoundsException("0 is out of bounds (empty vector)\nsecond line")
    val line = WorkerResult.formatError(error)
    assert(line == "BENCH_ERROR java.lang.IndexOutOfBoundsException: 0 is out of bounds (empty vector)")
    assert(WorkerResult.parseError(line).contains("java.lang.IndexOutOfBoundsException: 0 is out of bounds (empty vector)"))
    assert(WorkerResult.formatError(new IllegalStateException()) == "BENCH_ERROR java.lang.IllegalStateException")
  }

  test("lines that are not error lines are ignored") {
    assert(WorkerResult.parseError("some solver output").isEmpty)
    assert(WorkerResult.parseError("BENCH_ERROR ").isEmpty)
    assert(WorkerResult.parseError(WorkerResult.format(found = true, millis = 1, tries = 1)).isEmpty)
  }

  test("a worker that reported an error is an error with that cause, not its stack trace") {
    val errorLine = "BENCH_ERROR java.lang.IndexOutOfBoundsException: 0 is out of bounds (empty vector)"
    ProcessRunner.interpret(null, errorLine, 4, List("  at Foo.bar", "  at Foo.baz"), 60000) match {
      case Errored(message) =>
        assert(message == "java.lang.IndexOutOfBoundsException: 0 is out of bounds (empty vector)")
      case other => fail(s"Expected Errored but got $other")
    }
  }

  test("every case can be found by the name the worker is given") {
    val tantrix = BenchmarkCases.tantrixCases(List(3, 6))
    val redPuzzle = BenchmarkCases.redPuzzleCases()
    assert(tantrix.nonEmpty && redPuzzle.nonEmpty)
    (tantrix ++ redPuzzle).foreach { c =>
      val found = BenchmarkCases.find(c.puzzle, c.variant, c.algorithm)
      assert(found.exists(f => f.puzzle == c.puzzle && f.variant == c.variant && f.algorithm == c.algorithm), c)
    }
  }

  test("unknown cases are not found") {
    assert(BenchmarkCases.find("Tantrix", "seven tiles", "GENETIC_SEARCH").isEmpty)
    assert(BenchmarkCases.find("Tantrix", "6 tiles", "NO_SUCH_ALGORITHM").isEmpty)
    assert(BenchmarkCases.find("Chess", "6 tiles", "GENETIC_SEARCH").isEmpty)
  }
}
