// Copyright by Barry G. Becker, 2021. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.rubixcube.ui

import scala.compiletime.uninitialized
import com.barrybecker4.puzzle.common.AlgorithmEnum
import com.barrybecker4.puzzle.common.ui.{NavigationPanel, _}
import com.barrybecker4.puzzle.rubixcube.{Algorithm, RubixCubeController}
import com.barrybecker4.puzzle.rubixcube.model.{Cube, CubeMove}
import com.barrybecker4.search.Refreshable
import com.barrybecker4.ui.util.GUIUtil
import org.lwjgl.glfw.GLFW

import java.awt.event.{WindowAdapter, WindowEvent}
import javax.swing._


/**
  * Rubix Cube - https://en.wikipedia.org/wiki/Rubik%27s_Cube.
  * This program solves the well-known Rubix Cube puzzle.
  * For advanced solving approaches see
  * https://medium.com/@benjamin.botto/implementing-an-optimal-rubiks-cube-solver-using-korf-s-algorithm-bf750b332cf9
  */
object RubixCubePuzzle extends App {
  // Replicates GUIUtil.showApplet but calls setVisible(true) via EDT invokeLater instead of
  // from the main thread.  With -XstartOnFirstThread, calling setVisible from the main thread
  // triggers LWCToolkit.invokeAndWait → EDT waits for macOS thread-0 → thread-0 (main) is
  // blocked in invokeAndWait → deadlock.  Using invokeLater lets the main thread reach the
  // glfwPollEvents() loop below, which pumps the NSRunLoop so the EDT's
  // performSelectorOnMainThread:waitUntilDone:YES selector executes and the window appears.
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
  System.err.println("[D1] calling init()")
  applet.init()
  System.err.println("[D2] init() done")
  frame.setTitle(applet.getName)
  System.err.println("[D3] after setTitle")

  // setVisible on the EDT — this is the critical difference vs GUIUtil.showApplet.
  // The main thread is free to run glfwPollEvents() below, which drains the macOS
  // NSRunLoop so the Cocoa window-show calls dispatched by the EDT complete.
  SwingUtilities.invokeLater(() => {
    System.err.println("[D-EDT] setVisible on EDT")
    frame.setVisible(true)
    frame.toFront()
  })
  System.err.println("[D4] after invokeLater")
  applet.start()
  System.err.println("[D5] after start()")

  System.err.println("[diag] entering glfwPollEvents loop")
  // Keep macOS thread-0 alive, pumping the NSRunLoop.  glfwCreateWindow() in the JME render
  // thread posts to the GCD main queue via dispatch_sync; NSDefaultRunLoopMode drains it.
  while (!Thread.currentThread().isInterrupted) {
    GLFW.glfwPollEvents()
    Thread.sleep(16)
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


