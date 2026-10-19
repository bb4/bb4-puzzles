// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.benchmark

/**
  * Aggregated result of repeatedly running one algorithm on one puzzle variant.
  *
  * @param attempts     number of runs actually made (a cell stops early after repeated timeouts)
  * @param solved       runs that found a solution
  * @param unsolved     runs that finished within the time limit without finding a solution
  * @param timedOut     runs that hit the time limit
  * @param errored      runs that threw an unexpected exception
  * @param medianMillis median wall clock time of the solved runs (None if no run solved it)
  * @param medianTries  median of the solver's reported number of tries/iterations for the solved runs
  * @param lastError    description of the exception from the most recent errored run, if any
  */
final case class CellResult(
  puzzle: String,
  variant: String,
  algorithm: String,
  attempts: Int,
  solved: Int,
  unsolved: Int,
  timedOut: Int,
  errored: Int,
  medianMillis: Option[Long],
  minMillis: Option[Long],
  maxMillis: Option[Long],
  medianTries: Option[Long],
  lastError: Option[String] = None
) {
  def wasSolved: Boolean = solved > 0
}

/** Description of the machine a benchmark ran on. Timings are only comparable between runs on the same machine. */
final case class MachineInfo(cpus: Int, os: String, arch: String, java: String, maxHeapMb: Long)

/**
  * Everything recorded by one execution of the benchmark against one version of the optimization library.
  *
  * @param libVersion the bb4-optimization version that was on the classpath, like "2.0.0"
  */
final case class RunRecord(
  libName: String,
  libVersion: String,
  timestamp: String,
  machine: MachineInfo,
  timeoutSeconds: Int,
  repetitions: Int,
  results: List[CellResult]
)
