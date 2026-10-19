// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.benchmark

import com.barrybecker4.puzzle.benchmark.Json.*

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Path, Paths}

/**
  * Reads and writes the benchmark results file (docs/benchmarks.json).
  * The file holds one [[RunRecord]] per optimization library version, so adding a new version never
  * disturbs the numbers already recorded for an older one.
  */
object ResultsStore {

  val SCHEMA_VERSION = 1

  /** @return the recorded runs, or an empty list if the file does not exist yet */
  def load(file: String): List[RunRecord] = {
    val path = Paths.get(file)
    if (!Files.exists(path)) Nil
    else fromJson(Json.parse(new String(Files.readAllBytes(path), StandardCharsets.UTF_8)))
  }

  def save(file: String, runs: List[RunRecord]): Unit = {
    val path: Path = Paths.get(file)
    val parent = path.toAbsolutePath.getParent
    if (parent != null) Files.createDirectories(parent)
    Files.write(path, Json.write(toJson(runs)).getBytes(StandardCharsets.UTF_8))
  }

  /** Replace the run for the same library version if there is one (re-running a baseline), otherwise append. */
  def upsert(runs: List[RunRecord], run: RunRecord): List[RunRecord] =
    if (runs.exists(_.libVersion == run.libVersion)) runs.map(r => if (r.libVersion == run.libVersion) run else r)
    else runs :+ run

  def toJson(runs: List[RunRecord]): Json =
    JObj(List(
      "schemaVersion" -> JNum(SCHEMA_VERSION),
      "runs" -> JArr(runs.map(runToJson))
    ))

  def fromJson(json: Json): List[RunRecord] = json match {
    case obj: JObj =>
      obj.get("runs") match {
        case Some(JArr(items)) => items.map(runFromJson)
        case _ => throw new IllegalArgumentException("Benchmark results file has no 'runs' array")
      }
    case _ => throw new IllegalArgumentException("Benchmark results file must contain a JSON object")
  }

  private def optNum(value: Option[Long]): Json = value.map(v => JNum(v.toDouble)).getOrElse(JNull)

  private def runToJson(run: RunRecord): Json =
    JObj(List(
      "libName" -> JStr(run.libName),
      "libVersion" -> JStr(run.libVersion),
      "timestamp" -> JStr(run.timestamp),
      "machine" -> JObj(List(
        "cpus" -> JNum(run.machine.cpus),
        "os" -> JStr(run.machine.os),
        "arch" -> JStr(run.machine.arch),
        "java" -> JStr(run.machine.java),
        "maxHeapMb" -> JNum(run.machine.maxHeapMb.toDouble)
      )),
      "timeoutSeconds" -> JNum(run.timeoutSeconds),
      "repetitions" -> JNum(run.repetitions),
      "results" -> JArr(run.results.map(cellToJson))
    ))

  private def cellToJson(c: CellResult): Json =
    JObj(List(
      "puzzle" -> JStr(c.puzzle),
      "variant" -> JStr(c.variant),
      "algorithm" -> JStr(c.algorithm),
      "attempts" -> JNum(c.attempts),
      "solved" -> JNum(c.solved),
      "unsolved" -> JNum(c.unsolved),
      "timedOut" -> JNum(c.timedOut),
      "errored" -> JNum(c.errored),
      "medianMillis" -> optNum(c.medianMillis),
      "minMillis" -> optNum(c.minMillis),
      "maxMillis" -> optNum(c.maxMillis),
      "medianTries" -> optNum(c.medianTries)
    ))

  private def field(obj: JObj, name: String): Json =
    obj.get(name).getOrElse(throw new IllegalArgumentException(s"Missing field '$name' in benchmark results"))

  private def str(obj: JObj, name: String): String = field(obj, name) match {
    case JStr(s) => s
    case _ => throw new IllegalArgumentException(s"Field '$name' must be a string")
  }

  private def long(obj: JObj, name: String): Long = field(obj, name) match {
    case JNum(n) => n.toLong
    case _ => throw new IllegalArgumentException(s"Field '$name' must be a number")
  }

  private def optLong(obj: JObj, name: String): Option[Long] = obj.get(name) match {
    case Some(JNum(n)) => Some(n.toLong)
    case _ => None
  }

  private def objField(obj: JObj, name: String): JObj = field(obj, name) match {
    case o: JObj => o
    case _ => throw new IllegalArgumentException(s"Field '$name' must be an object")
  }

  private def runFromJson(json: Json): RunRecord = json match {
    case obj: JObj =>
      val machine = objField(obj, "machine")
      val results = field(obj, "results") match {
        case JArr(items) => items.map(cellFromJson)
        case _ => throw new IllegalArgumentException("Field 'results' must be an array")
      }
      RunRecord(
        libName = str(obj, "libName"),
        libVersion = str(obj, "libVersion"),
        timestamp = str(obj, "timestamp"),
        machine = MachineInfo(
          cpus = long(machine, "cpus").toInt,
          os = str(machine, "os"),
          arch = str(machine, "arch"),
          java = str(machine, "java"),
          maxHeapMb = long(machine, "maxHeapMb")
        ),
        timeoutSeconds = long(obj, "timeoutSeconds").toInt,
        repetitions = long(obj, "repetitions").toInt,
        results = results
      )
    case _ => throw new IllegalArgumentException("Each run must be a JSON object")
  }

  private def cellFromJson(json: Json): CellResult = json match {
    case obj: JObj =>
      CellResult(
        puzzle = str(obj, "puzzle"),
        variant = str(obj, "variant"),
        algorithm = str(obj, "algorithm"),
        attempts = long(obj, "attempts").toInt,
        solved = long(obj, "solved").toInt,
        unsolved = long(obj, "unsolved").toInt,
        timedOut = long(obj, "timedOut").toInt,
        errored = long(obj, "errored").toInt,
        medianMillis = optLong(obj, "medianMillis"),
        minMillis = optLong(obj, "minMillis"),
        maxMillis = optLong(obj, "maxMillis"),
        medianTries = optLong(obj, "medianTries")
      )
    case _ => throw new IllegalArgumentException("Each result must be a JSON object")
  }
}
