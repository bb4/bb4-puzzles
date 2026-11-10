// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.benchmark

import java.net.URLDecoder

/**
  * Determines which bb4-optimization version is actually on the classpath, so that results are never recorded
  * under the wrong column. For example, when a sibling ../bb4-optimization checkout is substituted into the
  * build, the classes come from a directory instead of a versioned jar and the version cannot be determined.
  */
object OptimizationVersion {

  private val JarName = """bb4-optimization-(.+)\.jar""".r

  /** Anchor class in bb4-optimization; referenced by resource path so the IDE does not need that dependency on its classpath. */
  private val LibraryAnchorResource = "com/barrybecker4/optimization/Optimizer.class"

  /** @return the version taken from the jar file name, or None if the classes are not from a versioned jar */
  def detect(): Option[String] =
    Option(OptimizationVersion.getClass.getClassLoader.getResource(LibraryAnchorResource))
      .flatMap(versionFromOptimizationLibraryUrl)

  private def versionFromOptimizationLibraryUrl(url: java.net.URL): Option[String] =
    jarFileName(url).flatMap {
      case JarName(version) => Some(version)
      case _ => None
    }

  private def jarFileName(url: java.net.URL): Option[String] = url.getProtocol match {
    case "jar" =>
      val path = URLDecoder.decode(url.getPath, "UTF-8")
      val bang = path.indexOf("!/")
      if (bang < 0) None
      else {
        val jarPath = path.substring(0, bang)
        Some(jarPath.substring(jarPath.lastIndexOf('/') + 1))
      }
    case _ => None
  }

  /**
    * @param requested the version the person says is on the classpath (--lib-version), if any
    * @return the version to record results under
    * @throws java.lang.IllegalStateException if the requested version disagrees with the detected one,
    *                               or if there is no way to know the version
    */
  def resolve(requested: Option[String]): String =
    (detect(), requested) match {
      case (Some(found), Some(wanted)) if found != wanted =>
        throw new IllegalStateException(
          s"bb4-optimization $found is on the classpath, but --lib-version=$wanted was requested. " +
            "Check the bb4-optimization dependency in build.gradle (and any composite build in settings.gradle).")
      case (Some(found), _) => found
      case (None, Some(wanted)) =>
        println(s"WARNING: could not detect the bb4-optimization version from the classpath; using --lib-version=$wanted")
        wanted
      case (None, None) =>
        throw new IllegalStateException(
          "Could not detect the bb4-optimization version from the classpath (it may be a local source build). " +
            "Pass --lib-version=<version> to say which version the results belong to.")
    }
}
