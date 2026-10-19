// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.benchmark

import com.barrybecker4.puzzle.benchmark.BenchmarkTstUtil.{cell, runRecord}
import org.scalatest.funsuite.AnyFunSuite

class ReportGeneratorSuite extends AnyFunSuite {

  test("times are formatted for their size") {
    assert(ReportGenerator.formatMillis(250) == "250 ms")
    assert(ReportGenerator.formatMillis(1500) == "1.50 s")
    assert(ReportGenerator.formatMillis(65000) == "1m 05s")
    assert(ReportGenerator.formatMillis(125000) == "2m 05s")
  }

  test("a solved cell shows the median time and how many runs solved it") {
    assert(ReportGenerator.cellText(cell("GENETIC_SEARCH", solved = 5, medianMillis = Some(1900)), 60) == "1.90 s (5/5)")
    assert(ReportGenerator.cellText(cell("GENETIC_SEARCH", solved = 3, medianMillis = Some(1900)), 60) == "1.90 s (3/5)")
    assert(ReportGenerator.cellText(cell("GENETIC_SEARCH", solved = 1, attempts = 1, medianMillis = Some(250)), 60) == "250 ms")
  }

  test("a cell that never found a solution says so") {
    assert(ReportGenerator.cellText(cell("SIMULATED_ANNEALING", solved = 0, timedOut = 2, attempts = 2), 60) ==
      "no solution (timed out after 60s)")
    assert(ReportGenerator.cellText(cell("SIMULATED_ANNEALING", solved = 0), 60) == "no solution")
  }

  test("an empty report explains how to record results") {
    val report = ReportGenerator.render(Nil)
    assert(report.contains("No results have been recorded yet"))
    assert(report.contains("./gradlew benchmark"))
  }

  test("a single run has a column for its version and no comparison column") {
    val report = ReportGenerator.render(List(runRecord("2.0.0", List(
      cell("GENETIC_SEARCH", solved = 5, medianMillis = Some(1900)),
      cell("SIMULATED_ANNEALING", solved = 0, timedOut = 2, attempts = 2)
    ))))
    assert(report.contains("## Tantrix"))
    assert(report.contains("| Variant | Algorithm | bb4-optimization 2.0.0 |"))
    assert(report.contains("| 6 tiles | `GENETIC_SEARCH` | 1.90 s (5/5) |"))
    assert(report.contains("| 6 tiles | `SIMULATED_ANNEALING` | no solution (timed out after 60s) |"))
    assert(!report.contains(" vs "))
  }

  test("algorithms added in a newer version are n/a for the older version") {
    val baseline = runRecord("2.0.0", List(cell("GENETIC_SEARCH", solved = 5, medianMillis = Some(2000))))
    val newer = runRecord("2.1-SNAPSHOT", List(
      cell("GENETIC_SEARCH", solved = 5, medianMillis = Some(1000)),
      cell("ITERATED_LOCAL_SEARCH", solved = 5, medianMillis = Some(300))
    ))
    val report = ReportGenerator.render(List(baseline, newer))

    assert(report.contains("bb4-optimization 2.0.0"))
    assert(report.contains("bb4-optimization 2.1-SNAPSHOT"))
    assert(report.contains("2.1-SNAPSHOT vs 2.0.0"))
    val newRow = report.linesIterator.find(_.contains("ITERATED_LOCAL_SEARCH")).get
    assert(newRow.contains(ReportGenerator.NOT_AVAILABLE))
    assert(newRow.contains("300 ms (5/5)"))
    val sharedRow = report.linesIterator.find(_.contains("`GENETIC_SEARCH`")).get
    assert(sharedRow.contains("2.00 s (5/5)"))
    assert(sharedRow.contains("1.00 s (5/5)"))
    assert(sharedRow.contains("2.0x faster"))
  }

  test("new algorithms are listed with the other algorithms of their variant") {
    val baseline = runRecord("2.0.0", List(
      cell("GENETIC_SEARCH", solved = 5, medianMillis = Some(100), variant = "3 tiles"),
      cell("GENETIC_SEARCH", solved = 5, medianMillis = Some(100), variant = "6 tiles")
    ))
    val newer = runRecord("2.1", List(
      cell("GENETIC_SEARCH", solved = 5, medianMillis = Some(100), variant = "3 tiles"),
      cell("ITERATED_LOCAL_SEARCH", solved = 5, medianMillis = Some(100), variant = "3 tiles"),
      cell("GENETIC_SEARCH", solved = 5, medianMillis = Some(100), variant = "6 tiles")
    ))
    val rows = ReportGenerator.render(List(baseline, newer)).linesIterator.filter(_.startsWith("| ")).toList
    val firstSixTiles = rows.indexWhere(_.startsWith("| 6 tiles"))
    val newAlgorithm = rows.indexWhere(_.contains("ITERATED_LOCAL_SEARCH"))
    assert(newAlgorithm >= 0 && newAlgorithm < firstSixTiles)
  }

  test("changes between versions are described") {
    def c(median: Option[Long]) = Some(cell("A", solved = if (median.isDefined) 5 else 0, medianMillis = median))
    assert(ReportGenerator.changeText(c(Some(3000)), c(Some(1000))) == "3.0x faster")
    assert(ReportGenerator.changeText(c(Some(1000)), c(Some(2000))) == "2.0x slower")
    assert(ReportGenerator.changeText(c(Some(1000)), c(Some(1020))) == "about the same")
    assert(ReportGenerator.changeText(c(None), c(Some(1000))) == "now solved")
    assert(ReportGenerator.changeText(c(Some(1000)), c(None)) == "no longer solved")
    assert(ReportGenerator.changeText(c(None), c(None)) == "-")
    assert(ReportGenerator.changeText(None, c(Some(1000))) == "-")
  }

  test("runs on different machines are flagged as not comparable") {
    val other = BenchmarkTstUtil.MACHINE.copy(cpus = 16)
    val report = ReportGenerator.render(List(
      runRecord("2.0.0", List(cell("GENETIC_SEARCH", solved = 5, medianMillis = Some(100)))),
      runRecord("2.1", List(cell("GENETIC_SEARCH", solved = 5, medianMillis = Some(100))), machine = other)
    ))
    assert(report.contains("not directly comparable"))
  }
}
