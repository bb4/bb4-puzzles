// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.benchmark

/** Builders for benchmark results used by several test suites. */
object BenchmarkTstUtil {

  val MACHINE: MachineInfo = MachineInfo(cpus = 8, os = "Linux", arch = "amd64", java = "21", maxHeapMb = 4096)

  def cell(
    algorithm: String,
    solved: Int,
    attempts: Int = 5,
    medianMillis: Option[Long] = None,
    timedOut: Int = 0,
    puzzle: String = "Tantrix",
    variant: String = "6 tiles"
  ): CellResult =
    CellResult(
      puzzle = puzzle,
      variant = variant,
      algorithm = algorithm,
      attempts = attempts,
      solved = solved,
      unsolved = attempts - solved - timedOut,
      timedOut = timedOut,
      errored = 0,
      medianMillis = medianMillis,
      minMillis = medianMillis,
      maxMillis = medianMillis,
      medianTries = medianMillis.map(_ / 10)
    )

  def runRecord(version: String, results: List[CellResult], machine: MachineInfo = MACHINE): RunRecord =
    RunRecord(
      libName = "bb4-optimization",
      libVersion = version,
      timestamp = "2026-10-19T17:00:00Z",
      machine = machine,
      timeoutSeconds = 60,
      repetitions = 5,
      results = results
    )
}
