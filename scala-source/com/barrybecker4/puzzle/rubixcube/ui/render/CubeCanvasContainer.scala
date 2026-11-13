package com.barrybecker4.puzzle.rubixcube.ui.render

import com.barrybecker4.puzzle.rubixcube.model.{Cube, CubeMove}
import com.barrybecker4.puzzle.rubixcube.ui.render.CubeCanvasContainer.{AA_SAMPLES, APP_CLASS, BACKGROUND, isMacOS}
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

  private def isMacOS: Boolean =
    System.getProperty("os.name", "").toLowerCase.contains("mac")
}

class CubeCanvasContainer() {
  // Soften LWJGL's thread-0 check; JME 3.10 runs the macOS canvas loop via invokeLater on AppKit/EDT.
  Configuration.GLFW_CHECK_THREAD0.set(false)

  private val clazz = Class.forName(APP_CLASS)
  val renderer: CubeSceneRenderer = clazz.getDeclaredConstructor().newInstance().asInstanceOf[CubeSceneRenderer]
  renderer.setBackgroundColor(BACKGROUND)

  renderer.setPauseOnLostFocus(false)
  val settings = new AppSettings(true)
  settings.setRenderer(AppSettings.LWJGL_OPENGL3)
  settings.setUseJoysticks(false)
  settings.setVSync(false)
  settings.setFrameRate(60)
  // macOS + AWT: gamma/sRGB remains flaky on NSOpenGLView with lwjgl3-awt.
  // Stock 3.10 createContext uses a compatibility profile that NSOpenGL rejects — use MacLwjglCanvas.
  if (isMacOS) {
    settings.setGammaCorrection(false)
    settings.setSamples(4)
    settings.setCustomRenderer(classOf[MacLwjglCanvas])
  } else {
    settings.setSamples(AA_SAMPLES)
  }
  renderer.setSettings(settings)
  renderer.createCanvas()
  if (isMacOS) {
    // setCustomRenderer stores "CUSTOM"+className; GL init needs a real LWJGL_* id.
    renderer.getContext.getSettings.setRenderer(AppSettings.LWJGL_OPENGL3)
  }
  // startCanvas() is deferred until the JFrame is showing (see RubixCubePuzzle.main).

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
