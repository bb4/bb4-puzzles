package com.barrybecker4.puzzle.rubixcube.ui.jmonkey

import com.jme3.app.SimpleApplication
import com.jme3.input.ChaseCamera
import com.jme3.input.controls.ActionListener
import com.jme3.input.controls.AnalogListener
import com.jme3.input.controls.KeyTrigger
import com.jme3.material.Material
import com.jme3.math.{ColorRGBA, FastMath, Quaternion, Vector3f}
import com.jme3.scene.Geometry
import com.jme3.scene.shape.{Box, Quad}
import com.jme3.input.KeyInput


/** A 3rd-person chase camera orbits a target.
  * Follow the cube with WASD keys, rotate by dragging the mouse. */
object TestChaseCamera extends App {
  val app = new TestChaseCamera
  app.start()
}

class TestChaseCamera extends SimpleApplication with AnalogListener with ActionListener {
  private var cubeGeom: Geometry = _
  private var chaseCam: ChaseCamera = _

  override def simpleInitApp(): Unit = { // Load a teapot model
    cubeGeom = createCube() //assetManager.loadModel("Models/Teapot/Teapot.obj").asInstanceOf[Geometry]
    val ground = createGround()

    rootNode.attachChild(cubeGeom)
    rootNode.attachChild(ground)

    // Disable the default first-person cam!
    flyCam.setEnabled(false)

    chaseCam = new ChaseCamera(cam, cubeGeom, inputManager)
    chaseCam.setSmoothMotion(false)
    //registerInput()
  }

  private def createCube(): Geometry = {
    val boxMesh = new Box(1, 1, 1)
    val material = new Material(assetManager, "Common/MatDefs/Misc/ShowNormals.j3md")
    val offBox = new Geometry("box", boxMesh)
    offBox.setMaterial(material)
    offBox
  }

  private def createGround(): Geometry = {
    val mat_ground = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md")
    //val mat_ground = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md")
    //mat_ground.setBoolean("UseMaterialColors", true)
    //mat_ground.setColor("Diffuse", ColorRGBA.Brown)
    //mat_ground.setColor("Specular", ColorRGBA.Green)

    val ground = new Geometry("ground", new Quad(10, 10))
    ground.setLocalRotation(new Quaternion().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_X))
    ground.setLocalTranslation(-5, -1.1f, 5)
    ground.setMaterial(mat_ground)
    ground
  }

  def registerInput(): Unit = {
    inputManager.addMapping("moveForward", new KeyTrigger(KeyInput.KEY_UP), new KeyTrigger(KeyInput.KEY_W))
    inputManager.addMapping("moveBackward", new KeyTrigger(KeyInput.KEY_DOWN), new KeyTrigger(KeyInput.KEY_S))
    inputManager.addMapping("moveRight", new KeyTrigger(KeyInput.KEY_RIGHT), new KeyTrigger(KeyInput.KEY_D))
    inputManager.addMapping("moveLeft", new KeyTrigger(KeyInput.KEY_LEFT), new KeyTrigger(KeyInput.KEY_A))
    inputManager.addMapping("displayPosition", new KeyTrigger(KeyInput.KEY_P))
    inputManager.addListener(this, "moveForward", "moveBackward", "moveRight", "moveLeft")
    inputManager.addListener(this, "displayPosition")
  }

  override def onAnalog(name: String, value: Float, tpf: Float): Unit = {
    if (name == "moveForward") cubeGeom.move(0, 0, -5 * tpf)
    if (name == "moveBackward") cubeGeom.move(0, 0, 5 * tpf)
    if (name == "moveRight") cubeGeom.move(5 * tpf, 0, 0)
    if (name == "moveLeft") cubeGeom.move(-5 * tpf, 0, 0)
  }

  override def onAction(name: String, keyPressed: Boolean, tpf: Float): Unit = {
    if (name == "displayPosition" && keyPressed) cubeGeom.move(10, 10, 10)
  }

  override def simpleUpdate(tpf: Float): Unit = {
    //super.simpleUpdate(tpf)
  }
}