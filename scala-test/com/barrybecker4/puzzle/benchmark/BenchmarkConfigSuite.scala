// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.benchmark

import org.scalatest.funsuite.AnyFunSuite

class BenchmarkConfigSuite extends AnyFunSuite {

  test("no arguments gives the defaults") {
    val config = BenchmarkConfig.parse(Nil)
    assert(config.timeoutSeconds == 60)
    assert(config.repetitions == 5)
    assert(config.tantrixSizes == List(3, 6, 9, 12, 15, 20, 24))
    assert(config.puzzles == Set("tantrix", "redpuzzle"))
    assert(config.libVersion.isEmpty)
    assert(config.resultsFile == "docs/benchmarks.json")
    assert(config.reportFile == "docs/benchmarks.md")
    assert(config.writeReport)
    assert(!config.inProcess)
  }

  test("every option can be overridden") {
    val config = BenchmarkConfig.parse(List(
      "--lib-version=2.0.0", "--timeout=30", "--reps=3", "--tantrix-sizes=4,8",
      "--puzzles=tantrix", "--results=out/r.json", "--report=out/r.md", "--no-report", "--in-process"
    ))
    assert(config.libVersion.contains("2.0.0"))
    assert(config.timeoutSeconds == 30)
    assert(config.repetitions == 3)
    assert(config.tantrixSizes == List(4, 8))
    assert(config.puzzles == Set("tantrix"))
    assert(config.resultsFile == "out/r.json")
    assert(config.reportFile == "out/r.md")
    assert(!config.writeReport)
    assert(config.inProcess)
  }

  test("bad options are rejected with a message") {
    List("--timeout=abc", "--reps=0", "--puzzles=chess", "--bogus=1", "positional", "--tantrix-sizes=3,x").foreach { bad =>
      intercept[IllegalArgumentException] {
        BenchmarkConfig.parse(List(bad))
      }
    }
  }
}
