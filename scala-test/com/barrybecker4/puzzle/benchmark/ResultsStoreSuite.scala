// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.benchmark

import com.barrybecker4.puzzle.benchmark.BenchmarkTstUtil.{cell, run}
import org.scalatest.funsuite.AnyFunSuite

import java.nio.file.Files

class ResultsStoreSuite extends AnyFunSuite {

  private val baseline = run("2.0.0", List(
    cell("GENETIC_SEARCH", solved = 5, medianMillis = Some(1900)),
    cell("SIMULATED_ANNEALING", solved = 0, timedOut = 2, attempts = 2),
    cell("GENETIC_SEARCH", solved = 5, medianMillis = Some(40), puzzle = "Red Puzzle", variant = "9 pieces")
  ))

  test("runs survive a round trip through JSON") {
    val newer = run("2.1-SNAPSHOT", List(cell("ITERATED_LOCAL_SEARCH", solved = 4, medianMillis = Some(70))))
    val runs = List(baseline, newer)
    assert(ResultsStore.fromJson(Json.parse(Json.write(ResultsStore.toJson(runs)))) == runs)
  }

  test("runs survive being saved to and loaded from a file") {
    val dir = Files.createTempDirectory("bench-results")
    val file = dir.resolve("nested").resolve("benchmarks.json").toString
    ResultsStore.save(file, List(baseline))
    assert(ResultsStore.load(file) == List(baseline))
  }

  test("loading a file that does not exist gives no runs") {
    val missing = Files.createTempDirectory("bench-results").resolve("nope.json").toString
    assert(ResultsStore.load(missing).isEmpty)
  }

  test("a new library version is appended and leaves the existing runs alone") {
    val newer = run("2.1-SNAPSHOT", List(cell("GENETIC_SEARCH", solved = 5, medianMillis = Some(1000))))
    assert(ResultsStore.upsert(List(baseline), newer) == List(baseline, newer))
  }

  test("re-running a version replaces only that version") {
    val newer = run("2.1-SNAPSHOT", List(cell("GENETIC_SEARCH", solved = 5, medianMillis = Some(1000))))
    val rerun = run("2.0.0", List(cell("GENETIC_SEARCH", solved = 5, medianMillis = Some(1800))))
    assert(ResultsStore.upsert(List(baseline, newer), rerun) == List(rerun, newer))
  }

  test("a results file without runs is rejected") {
    intercept[IllegalArgumentException] {
      ResultsStore.fromJson(Json.parse("""{"schemaVersion": 1}"""))
    }
  }
}
