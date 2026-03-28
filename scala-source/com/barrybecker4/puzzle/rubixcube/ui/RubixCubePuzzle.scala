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


/**
  * Rubix Cube - https://en.wikipedia.org/wiki/Rubik%27s_Cube.
  * This program solves the well-known Rubix Cube puzzle.
  * For advanced solving approaches see
  * https://medium.com/@benjamin.botto/implementing-an-optimal-rubiks-cube-solver-using-korf-s-algorithm-bf750b332cf9
  */
object RubixCubePuzzle {

  private def isMacOS: Boolean =
    System.getProperty("os.name", "").toLowerCase.contains("mac")
  // Replicates GUIUtil.showApplet but calls setVisible(true) via EDT invokeLater instead of
  // from the main thread.  With -XstartOnFirstThread, calling setVisible from the main thread
  // triggers LWCToolkit.invokeAndWait → EDT waits for macOS thread-0 → thread-0 (main) is
  // blocked in invokeAndWait → deadlock.  Using invokeLater lets the main thread reach the
  // macOS-only glfwPollEvents() loop below, which pumps the NSRunLoop so the EDT's
  // performSelectorOnMainThread:waitUntilDone:YES selector executes and the window appears.
  def main(args: Array[String]): Unit = {
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


    // setVisible on the EDT — this is the critical difference vs GUIUtil.showApplet.
    // On macOS the main thread is free to run glfwPollEvents() below, which drains the
    // NSRunLoop so the Cocoa window-show calls dispatched by the EDT complete.
    SwingUtilities.invokeLater(() => {

      frame.setVisible(true)
      frame.toFront()
    })

    applet.start()


    // macOS only: keep thread-0 alive and pump GLFW so JME/LWJGL can complete Cocoa work.
    // On Windows/Linux, glfwInit runs later on the jME render thread; calling glfwPollEvents()
    // here fails with "GLFW library is not initialized".  EDT + jME keep the JVM alive.
    if (isMacOS) {
      while (!Thread.currentThread().isInterrupted) {
        org.lwjgl.glfw.GLFW.glfwPollEvents()
        Thread.sleep(16)
      }
    }
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
}


