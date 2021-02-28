package com.barrybecker4.puzzle.rubixcube.ui

import com.jme3.app.SimpleApplication
import com.jme3.input.{ChaseCamera, KeyInput}
import com.jme3.input.controls.ActionListener
import com.jme3.input.controls.KeyTrigger
import com.jme3.material.Material
import com.jme3.math.ColorRGBA
import com.jme3.math.FastMath
import com.jme3.math.Quaternion
import com.jme3.math.Vector3f
import com.jme3.renderer.Camera
import com.jme3.renderer.ViewPort
import com.jme3.scene.Geometry
import com.jme3.scene.shape.Box
import com.jme3.texture.FrameBuffer
import com.jme3.texture.Image.Format
import com.jme3.texture.Texture
import com.jme3.texture.Texture2D
import com.jme3.input.ChaseCamera
import com.jme3.math.Vector3f
import com.jme3.scene.CameraNode
import com.jme3.scene.control.CameraControl.ControlDirection
import com.jme3.scene.instancing.InstancedNode


/**
  * This test renders a scene to a texture, then displays the texture on a cube.
  */
object CubeSceneRenderer extends App {
  val app = new CubeSceneRenderer
  app.start()
}

class CubeSceneRenderer extends SimpleApplication with ActionListener {
  private val TOGGLE_UPDATE = "Toggle Update"

  private var offBox: Geometry = _
  private var angle: Float = 0
  private var offView: ViewPort = _


  def setupOffscreenView(): Texture = {

    val offCamera = createOffscreenCamera()

    offView = renderManager.createPreView("Offscreen View", offCamera)
    offView.setClearFlags(true, true, true)
    offView.setBackgroundColor(ColorRGBA.DarkGray)

    val offTex = createTexture()
    val offBuffer = createFrameBuffer(offTex)

    offView.setOutputFrameBuffer(offBuffer)
    offBox = createCube()

    // attach the scene to the viewport to be rendered
    offView.attachScene(offBox)
    offTex
  }

  private def createOffscreenCamera(): Camera = {

    val offCamera = new Camera(512, 512)
    offCamera.setFrustumPerspective(45f, 1f, 1f, 1000f)

    offCamera.lookAt(new Vector3f(0f, 0f, 0f), Vector3f.UNIT_Y)
    offCamera.setLocation(new Vector3f(0f, 0f, -5f))
    offCamera
  }

  private def createFrameBuffer(texture: Texture2D): FrameBuffer = {
    val offBuffer = new FrameBuffer(512, 512, 1)
    //setup framebuffer to use texture
    offBuffer.setDepthBuffer(Format.Depth)
    offBuffer.setColorTexture(texture)
    offBuffer
  }

  private def createTexture(): Texture2D = {
    val offTex = new Texture2D(512, 512, Format.RGBA8)
    offTex.setMinFilter(Texture.MinFilter.Trilinear)
    offTex.setMagFilter(Texture.MagFilter.Bilinear)
    offTex
  }

  private def createCube(): Geometry = {
    val boxMesh = new Box(1, 1, 1)
    val material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md")
    val offBox = new Geometry("box", boxMesh)
    offBox.setMaterial(material)
    offBox
  }

  override def simpleInitApp(): Unit = {
    //cam.setLocation(new Vector3f(0, 0, 0))
    //cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y)

    flyCam.setEnabled(false)
    //flyCam.setDragToRotate(true)

    val quad = createQuad()

    val cubeNode = new InstancedNode()
    cubeNode.attachChild(quad)

    rootNode.attachChild(cubeNode)

    val chaseCam = new ChaseCamera(cam, cubeNode, inputManager)
    chaseCam.setSmoothMotion(false)
    chaseCam.setDefaultDistance(4)

    inputManager.addMapping(TOGGLE_UPDATE, new KeyTrigger(KeyInput.KEY_SPACE))
    inputManager.addListener(this, TOGGLE_UPDATE)
  }

  private def createQuad() = {
    val quad = new Geometry("box", new Box(1, 1, 1))
    val offTex = setupOffscreenView()

    val mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md")
    mat.setTexture("ColorMap", offTex)
    quad.setMaterial(mat)
    quad
  }

  override def simpleUpdate(tpf: Float): Unit = {
    val q = new Quaternion
    if (offView.isEnabled) {
      angle += tpf
      angle %= FastMath.TWO_PI
      q.fromAngles(angle, 0, angle)
      offBox.setLocalRotation(q)
      offBox.updateLogicalState(tpf)
      offBox.updateGeometricState()
    }
  }

  override def onAction(name: String, isPressed: Boolean, tpf: Float): Unit = {
    if (name == TOGGLE_UPDATE && isPressed) offView.setEnabled(!offView.isEnabled)
  }
}