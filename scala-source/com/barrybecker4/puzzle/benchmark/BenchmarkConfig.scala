// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.benchmark

/**
  * Settings for a benchmark run. Every setting has a default and can be overridden with a `--name=value` argument.
  *
  * @param timeoutSeconds  longest a single run may take before it is counted as "no solution"
  * @param repetitions     runs per puzzle variant and algorithm. The median of the successful runs is reported.
  * @param tantrixSizes    numbers of tiles for the Tantrix variants
  * @param puzzles         which puzzles to run: "tantrix" and/or "redpuzzle"
  * @param libVersion      the bb4-optimization version the person expects to be on the classpath (a safety check)
  * @param resultsFile     where the results are kept (one run per library version)
  * @param reportFile      where the markdown report is written
  * @param writeReport     whether to regenerate the markdown report after the run
  * @param inProcess       run all solves in this JVM instead of one JVM per run. Not recommended: a solver that
  *                        exceeds the time limit cannot be stopped and skews the timings of later runs.
  */
final case class BenchmarkConfig(
  timeoutSeconds: Int = BenchmarkConfig.DEFAULT_TIMEOUT_SECONDS,
  repetitions: Int = BenchmarkConfig.DEFAULT_REPETITIONS,
  tantrixSizes: List[Int] = BenchmarkConfig.DEFAULT_TANTRIX_SIZES,
  puzzles: Set[String] = BenchmarkConfig.ALL_PUZZLES,
  libVersion: Option[String] = None,
  resultsFile: String = BenchmarkConfig.DEFAULT_RESULTS_FILE,
  reportFile: String = BenchmarkConfig.DEFAULT_REPORT_FILE,
  writeReport: Boolean = true,
  inProcess: Boolean = false
)

object BenchmarkConfig {

  val DEFAULT_TIMEOUT_SECONDS = 60
  val DEFAULT_REPETITIONS = 10
  val DEFAULT_TANTRIX_SIZES: List[Int] = List(3, 6, 9, 12, 15)
  val ALL_PUZZLES: Set[String] = Set("tantrix", "redpuzzle")
  val DEFAULT_RESULTS_FILE = "docs/benchmarks.json"
  val DEFAULT_REPORT_FILE = "docs/benchmarks.md"

  val USAGE: String =
    """Options (all optional):
      |  --lib-version=2.0.0           the bb4-optimization version you expect; the run fails if the classpath disagrees
      |  --timeout=60                  seconds a single run may take before it counts as "no solution"
      |  --reps=10                     runs per cell; the median of the successful runs is reported
      |  --tantrix-sizes=3,6,9,12,15   numbers of tiles for the Tantrix variants
      |  --puzzles=tantrix,redpuzzle   which puzzles to run
      |  --results=docs/benchmarks.json
      |  --report=docs/benchmarks.md
      |  --no-report                   do not regenerate the markdown report
      |  --in-process                  run solves in this JVM instead of one JVM per run (a runaway solver cannot be stopped)""".stripMargin

  /** @throws IllegalArgumentException for an unknown or malformed option */
  def parse(args: List[String]): BenchmarkConfig =
    args.foldLeft(BenchmarkConfig())((config, arg) => applyArg(config, arg))

  private def positiveInt(name: String, value: String): Int = {
    val n =
      try value.trim.toInt
      catch {
        case _: NumberFormatException => throw new IllegalArgumentException(s"--$name must be a whole number, got '$value'")
      }
    if (n < 1) throw new IllegalArgumentException(s"--$name must be at least 1, got $n")
    n
  }

  private def applyArg(config: BenchmarkConfig, arg: String): BenchmarkConfig = {
    if (arg == "--no-report") config.copy(writeReport = false)
    else if (arg == "--in-process") config.copy(inProcess = true)
    else if (arg.startsWith("--") && arg.contains("=")) {
      val name = arg.substring(2, arg.indexOf('='))
      val value = arg.substring(arg.indexOf('=') + 1)
      name match {
        case "lib-version" => config.copy(libVersion = Some(value.trim))
        case "timeout" => config.copy(timeoutSeconds = positiveInt(name, value))
        case "reps" => config.copy(repetitions = positiveInt(name, value))
        case "tantrix-sizes" => config.copy(tantrixSizes = value.split(',').toList.map(v => positiveInt(name, v)))
        case "puzzles" =>
          val chosen = value.split(',').toList.map(_.trim.toLowerCase).toSet
          val unknown = chosen.diff(ALL_PUZZLES)
          if (unknown.nonEmpty) {
            throw new IllegalArgumentException(s"Unknown puzzle(s): ${unknown.mkString(", ")}. Choose from ${ALL_PUZZLES.mkString(", ")}")
          }
          config.copy(puzzles = chosen)
        case "results" => config.copy(resultsFile = value)
        case "report" => config.copy(reportFile = value)
        case _ => throw new IllegalArgumentException(s"Unknown option --$name\n$USAGE")
      }
    } else throw new IllegalArgumentException(s"Unrecognized argument '$arg'\n$USAGE")
  }
}
