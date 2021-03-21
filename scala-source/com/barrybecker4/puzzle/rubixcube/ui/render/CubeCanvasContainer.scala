package com.barrybecker4.puzzle.rubixcube.ui.render

import com.barrybecker4.puzzle.rubixcube.model.{Cube, CubeMove}
import com.barrybecker4.puzzle.rubixcube.ui.render.CubeCanvasContainer.{AA_SAMPLES, APP_CLASS, BACKGROUND}
import com.jme3.math.ColorRGBA
import com.jme3.system.{AppSettings, JmeCanvasContext}

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

  private val clazz = Class.forName(APP_CLASS)
  val renderer: CubeSceneRenderer = clazz.getDeclaredConstructor().newInstance().asInstanceOf[CubeSceneRenderer]
  renderer.setBackgroundColor(BACKGROUND)

  renderer.setPauseOnLostFocus(false)
  val settings = new AppSettings(true)
  settings.setSamples(AA_SAMPLES)
  renderer.setSettings(settings)
  renderer.createCanvas()
  renderer.startCanvas()

  val context: JmeCanvasContext = renderer.getContext.asInstanceOf[JmeCanvasContext]
  var canvas: Canvas = context.getCanvas

  def render(cube: Cube, width: Int, height: Int): Unit = {
    canvas.setSize(width, height)
    renderer.updateCube(cube)
  }

  def rotateSlice(cubeMove: CubeMove, doneRotating: () => Unit): Unit = {
    renderer.animateSliceRotation(cubeMove, doneRotating)
  }
}
