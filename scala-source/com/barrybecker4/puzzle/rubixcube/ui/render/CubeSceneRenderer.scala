package com.barrybecker4.puzzle.rubixcube.ui.render

import com.barrybecker4.puzzle.rubixcube.model._
import com.barrybecker4.puzzle.rubixcube.ui.render.jme.{CoordinateAxes, JmeUtil, RubixCubeNode}
import com.jme3.app.SimpleApplication
import com.jme3.light.DirectionalLight
import com.jme3.math.{FastMath, Quaternion, Vector3f}
import com.jme3.system.AppSettings
import com.jme3.math.ColorRGBA

import scala.collection.immutable.Queue


/**
  * Renders the Rubix cube in 3D using JMonkeyEngine.
  */
object CubeSceneRenderer extends App {
  val renderer = new CubeSceneRenderer()
  val settings = new AppSettings(false)
  settings.setTitle("Rubix Cube Solver")
  renderer.setSettings(settings)
  renderer.start()
}

class CubeSceneRenderer() extends SimpleApplication {

  case class SliceRotation(move: CubeMove, callback: () => Unit)

  private var util: JmeUtil = _
  private var currentCube: Cube = new Cube(4)
  private var cubeNodeParent: RubixCubeNode = _
  private var bgColor: ColorRGBA = _
  private var requestedRotations: Queue[SliceRotation] = Queue()
  private var rotationDoneCallback: Option[() => Unit] = None


  def setBackgroundColor(color: ColorRGBA): Unit = {
    bgColor = color
  }

  override def simpleInitApp(): Unit = {
    util = jme.JmeUtil(assetManager)
    setDisplayStatView(false)
    flyCam.setDragToRotate(true)

    updateCube(currentCube)
    cubeNodeParent = createCubeScene(currentCube)
    rootNode.attachChild(cubeNodeParent)

    rootNode.attachChild(new CoordinateAxes(new Vector3f(-1.5f, -1.5f, 1.5f), assetManager))
    rootNode.addLight(createDirectionalLight())

    viewPort.setBackgroundColor(bgColor)
  }

  def updateCube(cube: Cube): Unit = {
    val sizeChanged = cube.size != currentCube.size
    currentCube = cube

    if (sizeChanged) {
      rootNode.detachChildNamed("cubeNodeParent")
      cubeNodeParent = createCubeScene(currentCube)
      rootNode.attachChild(cubeNodeParent)
    }
    else if (cubeNodeParent != null) {
      cubeNodeParent.updateCube(currentCube)
    }
  }

  // Rotate the slice, then set the new state at the end
  def animateSliceRotation(cubeMove: CubeMove, doneCallback: () => Unit): Unit = {
      requestedRotations = requestedRotations.enqueue(SliceRotation(cubeMove, doneCallback))
  }

  /** Global scenegraph */
  private def createCubeScene(cube: Cube): RubixCubeNode = {
    val cubeNodeParent = new RubixCubeNode(cube, assetManager)
    val q = new Quaternion()
    q.fromAngleAxis(0, new Vector3f(0, 1, 0))
    cubeNodeParent.setLocalRotation(q)
    cubeNodeParent
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

    if (requestedRotations.nonEmpty && !cubeNodeParent.isRotating) {
      val d = requestedRotations.dequeue
      val nextRotation = d._1
      requestedRotations = d._2
      rotationDoneCallback = Some(nextRotation.callback)
      cubeNodeParent.startRotatingSlice(nextRotation.move)
    }
    if (cubeNodeParent.isRotating) {
      cubeNodeParent.incrementSliceRotation()
    }
    else if (rotationDoneCallback.isDefined) {
      rotationDoneCallback.get.apply()
      rotationDoneCallback = None
    }
  }

}