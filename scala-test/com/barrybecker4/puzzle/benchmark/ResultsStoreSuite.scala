// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.benchmark

import com.barrybecker4.puzzle.benchmark.BenchmarkTstUtil.{cell, runRecord}
import org.scalatest.funsuite.AnyFunSuite

import java.nio.file.Files

class ResultsStoreSuite extends AnyFunSuite {

  private val baseline = runRecord("2.0.0", List(
    cell("GENETIC_SEARCH", solved = 5, medianMillis = Some(1900)),
    cell("SIMULATED_ANNEALING", solved = 0, timedOut = 2, attempts = 2),
    cell("GENETIC_SEARCH", solved = 5, medianMillis = Some(40), puzzle = "Red Puzzle", variant = "9 pieces")
  ))

  test("runs survive a round trip through JSON") {
    val newer = runRecord("2.1-SNAPSHOT", List(cell("ITERATED_LOCAL_SEARCH", solved = 4, medianMillis = Some(70))))
    val runs = List(baseline, newer)
    assert(ResultsStore.fromJson(Json.parse(Json.write(ResultsStore.toJson(runs)))) == runs)
  }

  test("the last error of a crashed cell survives a round trip") {
    val crashed = runRecord("2.0.0", List(
      cell("GENETIC_SEARCH", solved = 0, errored = 5, lastError = Some("java.lang.IndexOutOfBoundsException: boom"))
    ))
    val loaded = ResultsStore.fromJson(Json.parse(Json.write(ResultsStore.toJson(List(crashed)))))
    assert(loaded == List(crashed))
    assert(loaded.head.results.head.lastError.contains("java.lang.IndexOutOfBoundsException: boom"))
  }

  test("results written before errors were recorded can still be read") {
    val json = Json.parse(Json.write(ResultsStore.toJson(List(baseline)))) match {
      case Json.JObj(fields) => fields
      case other => fail(s"Unexpected $other")
    }
    // Remove the lastError field from every cell, as in files written by the first version of the benchmark.
    def withoutLastError(j: Json): Json = j match {
      case Json.JObj(fs) => Json.JObj(fs.filterNot(_._1 == "lastError").map { case (k, v) => (k, withoutLastError(v)) })
      case Json.JArr(items) => Json.JArr(items.map(withoutLastError))
      case other => other
    }
    val old = withoutLastError(Json.JObj(json))
    assert(ResultsStore.fromJson(old) == List(baseline))
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
    val newer = runRecord("2.1-SNAPSHOT", List(cell("GENETIC_SEARCH", solved = 5, medianMillis = Some(1000))))
    assert(ResultsStore.upsert(List(baseline), newer) == List(baseline, newer))
  }

  test("re-running a version replaces only that version") {
    val newer = runRecord("2.1-SNAPSHOT", List(cell("GENETIC_SEARCH", solved = 5, medianMillis = Some(1000))))
    val rerun = runRecord("2.0.0", List(cell("GENETIC_SEARCH", solved = 5, medianMillis = Some(1800))))
    assert(ResultsStore.upsert(List(baseline, newer), rerun) == List(rerun, newer))
  }

  test("a results file without runs is rejected") {
    intercept[IllegalArgumentException] {
      ResultsStore.fromJson(Json.parse("""{"schemaVersion": 1}"""))
    }
  }
}
