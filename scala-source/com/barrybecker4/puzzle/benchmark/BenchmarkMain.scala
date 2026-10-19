// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.benchmark

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}
import java.time.Instant

/**
  * Benchmarks every bb4-optimization backed solver on every puzzle variant and records the results under the
  * bb4-optimization version that is on the classpath. Run it with `./gradlew benchmark`.
  *
  * Results are merged into the results file: running against a different library version adds a column to the
  * report, and re-running the same version replaces that version's numbers only.
  */
object BenchmarkMain {

  def main(args: Array[String]): Unit = {
    val status =
      try {
        run(BenchmarkConfig.parse(args.toList))
        0
      } catch {
        case e: IllegalArgumentException =>
          Console.err.println(e.getMessage)
          2
        case e: IllegalStateException =>
          Console.err.println(e.getMessage)
          2
      }
    // Concurrent solvers can leave non-daemon threads behind, so exit explicitly.
    System.exit(status)
  }

  private def run(config: BenchmarkConfig): Unit = {
    val libVersion = OptimizationVersion.resolve(config.libVersion)
    val cases = BenchmarkCases.all(config)
    println(s"Benchmarking bb4-optimization $libVersion: ${cases.size} cells, up to ${config.repetitions} runs each, " +
      s"${config.timeoutSeconds}s limit per run")

    val results = cases.zipWithIndex.map { case (benchCase, i) =>
      val result = CellRunner.runCase(benchCase, config.repetitions, config.timeoutSeconds, isolated = !config.inProcess)
      println(s"[${i + 1}/${cases.size}] ${benchCase.puzzle} | ${benchCase.variant} | ${benchCase.algorithm}: " +
        ReportGenerator.cellText(result, config.timeoutSeconds))
      result
    }

    val rt = Runtime.getRuntime
    val record = RunRecord(
      libName = "bb4-optimization",
      libVersion = libVersion,
      timestamp = Instant.now().toString,
      machine = MachineInfo(
        cpus = rt.availableProcessors(),
        os = System.getProperty("os.name"),
        arch = System.getProperty("os.arch"),
        java = System.getProperty("java.version"),
        maxHeapMb = rt.maxMemory() / (1024L * 1024L)
      ),
      timeoutSeconds = config.timeoutSeconds,
      repetitions = config.repetitions,
      results = results
    )

    val runs = ResultsStore.upsert(ResultsStore.load(config.resultsFile), record)
    ResultsStore.save(config.resultsFile, runs)
    println(s"Recorded results for bb4-optimization $libVersion in ${config.resultsFile}")

    if (config.writeReport) {
      ReportMain.writeReport(runs, config.reportFile)
      println(s"Wrote ${config.reportFile}")
    }
  }
}

/** Regenerates the markdown report from the recorded results without running any benchmarks. */
object ReportMain {

  def main(args: Array[String]): Unit = {
    val config = BenchmarkConfig.parse(args.toList)
    writeReport(ResultsStore.load(config.resultsFile), config.reportFile)
    println(s"Wrote ${config.reportFile}")
  }

  def writeReport(runs: List[RunRecord], reportFile: String): Unit = {
    val path = Paths.get(reportFile)
    val parent = path.toAbsolutePath.getParent
    if (parent != null) Files.createDirectories(parent)
    Files.write(path, ReportGenerator.render(runs).getBytes(StandardCharsets.UTF_8))
  }
}
