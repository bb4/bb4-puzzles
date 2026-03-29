// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.rubixcube.ui

import org.lwjgl.system.Configuration

import java.util.logging.{ConsoleHandler, Level, Logger}

/**
  * Turns up java.util.logging for JMonkeyEngine and LWJGL, and enables LWJGL's Configuration.DEBUG.
  * Call from [[RubixCubePuzzle.main]] before any LWJGL natives load (i.e. before `applet.init()`).
  *
  * System property `bb4.rubix.jmeVerbose`:
  *   - unset: verbose on macOS only (helps diagnose embedded canvas / GLFW issues)
  *   - `true` / `1` / `yes`: verbose on any OS
  *   - `false` / `0` / `no`: off
  */
object JmeLwjglVerboseLogging {

  val PropertyName: String = "bb4.rubix.jmeVerbose"

  private val MacOs: Boolean =
    System.getProperty("os.name", "").toLowerCase.contains("mac")

  def isVerboseEnabled: Boolean = {
    val raw = Option(System.getProperty(PropertyName)).map(_.trim.toLowerCase).filter(_.nonEmpty)
    raw match
      case Some("false") | Some("0") | Some("no")  => false
      case Some("true") | Some("1") | Some("yes") => true
      case Some(_)                                 => true
      case None                                    => MacOs
  }

  /** JUL level for JME / LWJGL loggers (below this, root/handler levels must allow it). */
  private val JmeLwjglLevel: Level = Level.FINE

  def configureIfEnabled(): Unit =
    if !isVerboseEnabled then ()
    else {
      Configuration.DEBUG.set(true)
      val root = Logger.getLogger("")
      root.setLevel(JmeLwjglLevel)
      var hasConsole = false
      for (h <- root.getHandlers) {
        h.setLevel(JmeLwjglLevel)
        if h.isInstanceOf[ConsoleHandler] then hasConsole = true
      }
      if !hasConsole then {
        val ch = new ConsoleHandler()
        ch.setLevel(JmeLwjglLevel)
        root.addHandler(ch)
      }
      for (name <- Seq("com.jme3", "org.lwjgl", "org.lwjglx")) {
        val lg = Logger.getLogger(name)
        lg.setLevel(JmeLwjglLevel)
      }
      Console.err.println(
        s"[bb4-rubixcube] Verbose JME/LWJGL logging (JUL ${JmeLwjglLevel.getName} + LWJGL Configuration.DEBUG). " +
          s"Disable: -D$PropertyName=false"
      )
    }
}
