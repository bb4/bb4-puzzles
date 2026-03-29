package com.barrybecker4.puzzle.rubixcube.ui.render

import com.barrybecker4.puzzle.rubixcube.model.{Cube, CubeMove}
import com.barrybecker4.puzzle.rubixcube.ui.render.CubeCanvasContainer.{AA_SAMPLES, APP_CLASS, BACKGROUND}
import com.jme3.math.ColorRGBA
import com.jme3.system.{AppSettings, JmeCanvasContext}
import org.lwjgl.system.Configuration

import java.awt.Canvas


/**
  * Encapsulates some JMonkeyEngine magic that can be used to create a java Canvas that
  * will render the 3D cube so that we can put it in the CubeViewer JPanel.
  */
object CubeCanvasContainer {
  private val APP_CLASS: String = "com.barrybecker4.puzzle.rubixcube.ui.render.CubeSceneRenderer"
  private val AA_SAMPLES: Int = 5
  private val BACKGROUND: ColorRGBA = new ColorRGBA(0.3f, 0.3f, 0.4f, 1.0f)
}

class CubeCanvasContainer() {
  // On macOS the JME render thread (not thread-0) calls glfwInit. Disable LWJGL's strict thread-0 check so
  // the render thread can proceed. The LwjglAWTGLCanvas path uses JAWT (not a GLFW window) for pixel output.
  Configuration.GLFW_CHECK_THREAD0.set(false)

  private val clazz = Class.forName(APP_CLASS)
  val renderer: CubeSceneRenderer = clazz.getDeclaredConstructor().newInstance().asInstanceOf[CubeSceneRenderer]
  renderer.setBackgroundColor(BACKGROUND)

  renderer.setPauseOnLostFocus(false)
  val settings = new AppSettings(true)
  settings.setRenderer(AppSettings.LWJGL_OPENGL3)
  settings.setSamples(AA_SAMPLES)
  renderer.setSettings(settings)
  renderer.createCanvas()
  // startCanvas() must not run from applet.init() on macOS: LwjglWindow.create() calls run()
  // synchronously on the calling thread, which would block main before the JFrame is shown.

  val context: JmeCanvasContext = renderer.getContext.asInstanceOf[JmeCanvasContext]
  var canvas: Canvas = context.getCanvas

  def startRendering(): Unit =
    renderer.startCanvas()

  def render(cube: Cube, width: Int, height: Int): Unit = {
    canvas.setSize(width, height)
    renderer.updateCube(cube)
  }

  def rotateSlice(cubeMove: CubeMove, doneRotating: () => Unit): Unit = {
    renderer.animateSliceRotation(cubeMove, doneRotating)
  }
}
