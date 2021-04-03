package com.barrybecker4.puzzle.rubixcube.ui.render

import com.barrybecker4.puzzle.rubixcube.model._
import com.barrybecker4.puzzle.rubixcube.ui.render.jme.{CoordinateAxes, JmeUtil, RubixCubeNode}
import com.jme3.app.SimpleApplication
import com.jme3.light.DirectionalLight
import com.jme3.math.Vector3f
import com.jme3.system.AppSettings
import com.jme3.math.ColorRGBA

import scala.collection.immutable.Queue


/**
  * Renders the Rubix cube in 3D using JMonkeyEngine.
  */
object CubeSceneRenderer extends App {
  val renderer = new CubeSceneRenderer(5)
  val settings = new AppSettings(false)
  settings.setTitle("Rubix Cube Solver")
  renderer.setSettings(settings)
  renderer.start()
}

class CubeSceneRenderer(initialCubeSize: Int) extends SimpleApplication {

  case class SliceRotation(move: CubeMove, callback: () => Unit)

  private var util: JmeUtil = _
  private var currentCube: Cube = new Cube(initialCubeSize)
  private var rubixCubeNode: RubixCubeNode = _
  private var bgColor: ColorRGBA = _
  private var requestedRotations: Queue[SliceRotation] = Queue()
  private var rotationDoneCallback: Option[() => Unit] = None
  private var cubeSizeChanged: Boolean = false

  def this() = this(3)

  def setBackgroundColor(color: ColorRGBA): Unit = {
    bgColor = color
  }

  override def simpleInitApp(): Unit = {
    util = jme.JmeUtil(assetManager)
    setDisplayStatView(false)
    flyCam.setDragToRotate(true)

    updateCube(currentCube)

    rootNode.attachChild(new CoordinateAxes(new Vector3f(-1.5f, -1.5f, 1.5f), assetManager))
    rootNode.addLight(createDirectionalLight())

    viewPort.setBackgroundColor(bgColor)
  }

  def updateCube(cube: Cube): Unit = {
    val sizeChanged = rubixCubeNode == null || cube.size != currentCube.size
    currentCube = cube

    if (sizeChanged) {
      cubeSizeChanged = true
    }
    else if (rubixCubeNode != null && !cubeSizeChanged) {
      rubixCubeNode.updateCube(currentCube)
    }
  }

  private def initializeNewCube(): Unit = {
    if (rootNode.hasChild(rubixCubeNode)) {
      rootNode.detachChild(rubixCubeNode)
    }
    rubixCubeNode = new RubixCubeNode(currentCube, assetManager)
    rootNode.attachChild(rubixCubeNode)
  }

  // Rotate the slice, then set the new state at the end
  def animateSliceRotation(cubeMove: CubeMove, doneCallback: () => Unit): Unit = {
    requestedRotations = requestedRotations.enqueue(SliceRotation(cubeMove, doneCallback))
  }

  private def createDirectionalLight(): DirectionalLight = {
    val sun: DirectionalLight = new DirectionalLight
    sun.setDirection(new Vector3f(1, 1, 1).normalizeLocal)
    sun.setColor(ColorRGBA.White)
    sun
  }

  override def simpleUpdate(tpf: Float): Unit = {
    // this allows the camera to rotate around a focal point
    cam.setLocation(cam.getDirection.negate.multLocal(cam.getLocation.length))

    if (cubeSizeChanged) {
      initializeNewCube()
      cubeSizeChanged = false
    }
    else {
      if (requestedRotations.nonEmpty && !rubixCubeNode.isRotating) {
        val d = requestedRotations.dequeue
        val nextRotation = d._1
        requestedRotations = d._2
        rotationDoneCallback = Some(nextRotation.callback)
        rubixCubeNode.startRotatingSlice(nextRotation.move)
      }
      if (rubixCubeNode.isRotating) {
        rubixCubeNode.incrementSliceRotation()
      }
      else if (rotationDoneCallback.isDefined) {
        rotationDoneCallback.get.apply()
        rotationDoneCallback = None
      }
    }

  }

}