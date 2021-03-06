package com.barrybecker4.puzzle.rubixcube.ui.render

import com.barrybecker4.puzzle.rubixcube.model._
import com.barrybecker4.puzzle.rubixcube.ui.render.jme.{CoordinateAxes, JmeUtil, RubixCubeNode}
import com.jme3.app.SimpleApplication
import com.jme3.input.ChaseCamera
import com.jme3.light.DirectionalLight
import com.jme3.math.{FastMath, Quaternion, Vector3f}
import com.jme3.scene.Spatial
import com.jme3.system.AppSettings
import com.jme3.math.ColorRGBA


/**
  * Renders the rubix cube in 3D using JMonkeyEngine.
  */
object CubeSceneRenderer extends App {
  val renderer = new CubeSceneRenderer()

  val settings = new AppSettings(false)
  settings.setTitle("Rubix Cube Solver")
  renderer.setSettings(settings)
  renderer.start()
}

class CubeSceneRenderer() extends SimpleApplication {

  private var util: JmeUtil = _
  private var currentCube: Cube = new Cube(4)
  private var cubeNodeParent: Spatial = _
  private var bgColor: ColorRGBA = _

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
      if (cube.size != currentCube.size) {
        // recreate cube geometry
        currentCube = cube

        rootNode.detachChildNamed("cubeNodeParent")
        cubeNodeParent = createCubeScene(currentCube)
        rootNode.attachChild(cubeNodeParent)
      }
      else {
        // update cube geometry
        currentCube = cube
      }
  }

  /** Global scenegraph */
  private def createCubeScene(cube: Cube): Spatial = {

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
    //val q = new Quaternion
  }

}