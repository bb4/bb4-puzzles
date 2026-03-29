// Copyright by Barry G. Becker, 2021. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.rubixcube.ui

import scala.compiletime.uninitialized
import com.barrybecker4.puzzle.common.AlgorithmEnum
import com.barrybecker4.puzzle.common.ui.{NavigationPanel, _}
import com.barrybecker4.puzzle.rubixcube.{Algorithm, RubixCubeController}
import com.barrybecker4.puzzle.rubixcube.model.{Cube, CubeMove}
import com.barrybecker4.search.Refreshable

import java.awt.event.{WindowAdapter, WindowEvent}
import javax.swing._

import org.lwjgl.glfw.GLFW
import org.lwjgl.system.Configuration


/**
  * Rubix Cube - https://en.wikipedia.org/wiki/Rubik%27s_Cube.
  * This program solves the well-known Rubix Cube puzzle.
  * For advanced solving approaches see
  * https://medium.com/@benjamin.botto/implementing-an-optimal-rubiks-cube-solver-using-korf-s-algorithm-bf750b332cf9
  */
object RubixCubePuzzle {

  // With -XstartOnFirstThread, JME / LwjglWindow on macOS runs the game loop on whatever thread
  // calls startCanvas(), so startCanvas() must not run during applet.init().
  //
  // GLFW must be initialized on the main thread before showing the Swing window; otherwise
  // LWCToolkit ↔ AppKit can wedge (invokeLater never runs while the main thread waits).
  // Call glfwInit here, then show the frame from the main thread, then startCanvas on the same
  // thread (JME macOS path enters run() synchronously).
  def main(args: Array[String]): Unit = {
    JmeLwjglVerboseLogging.configureIfEnabled()
    Configuration.GLFW_CHECK_THREAD0.set(false)
    if (!GLFW.glfwInit())
      throw new IllegalStateException("Unable to initialize GLFW")

    JPopupMenu.setDefaultLightWeightPopupEnabled(false)
    val applet = new RubixCubePuzzle(args)
    val frame   = new JFrame()
    frame.addWindowListener(new WindowAdapter {
      override def windowClosing(e: WindowEvent): Unit = System.exit(0)
    })
    frame.setContentPane(applet.getContentPane)
    val screen = java.awt.Toolkit.getDefaultToolkit.getScreenSize
    val h = (screen.getHeight * 2.0 / 3.0).toInt
    val w = Math.min(h * 1.5, screen.getWidth * 2.0 / 3.0).toInt
    frame.setLocation((screen.width - w) / 2, (screen.height - h) / 2)
    frame.setSize(w, h)

    applet.init()

    frame.setTitle(applet.getName)

    frame.setVisible(true)
    frame.toFront()

    applet.start()
    applet.startCubeRendering()
  }
}

case class RubixCubePuzzle(myargs: Array[String])
  extends PuzzleApplet[Cube, CubeMove](myargs) with DoneListener {

  private var navPanel: NavigationPanel = uninitialized

  /** Construct the application */
  def this() = { this(Array[String]()) }

  protected def createViewer = new CubeViewer(this)

  protected def createController(
      viewer: Refreshable[Cube, CubeMove]): RubixCubeController = new RubixCubeController(viewer)

  protected def getAlgorithmValues: Array[AlgorithmEnum[Cube, CubeMove]] =
    AlgorithmEnum.widenArray(Algorithm.values)

  override protected def createTopControls: RubixCubeTopControls =
    RubixCubeTopControls(controller.asInstanceOf[RubixCubeController], getAlgorithmValues)

  override protected def createBottomControls: JPanel = {
    navPanel = new NavigationPanel()
    navPanel
  }

  def done(): Unit = {
    navPanel.setPathNavigator(viewer.asInstanceOf[PathNavigator])
  }

  def startCubeRendering(): Unit =
    viewer.asInstanceOf[CubeViewer].startCubeRendering()
}


