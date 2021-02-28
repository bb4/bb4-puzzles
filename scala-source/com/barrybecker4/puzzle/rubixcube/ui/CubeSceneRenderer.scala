package com.barrybecker4.puzzle.rubixcube.ui

import com.jme3.app.SimpleApplication
import com.jme3.input.KeyInput
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


/**
  * This test renders a scene to a texture, then displays the texture on a cube.
  */
object CubeSceneRenderer {
  private val TOGGLE_UPDATE = "Toggle Update"

  def main(args: Array[String]): Unit = {
    val app = new CubeSceneRenderer
    app.start()
  }
}

class CubeSceneRenderer extends SimpleApplication with ActionListener {
  private var offBox: Geometry = _
  private var angle: Float = 0
  private var offView: ViewPort = _

  def setupOffscreenView: Texture = {
    val offCamera = new Camera(512, 512)
    offView = renderManager.createPreView("Offscreen View", offCamera)
    offView.setClearFlags(true, true, true)
    offView.setBackgroundColor(ColorRGBA.DarkGray)
    // create offscreen framebuffer
    val offBuffer = new FrameBuffer(512, 512, 1)
    //setup framebuffer's cam
    offCamera.setFrustumPerspective(45f, 1f, 1f, 1000f)
    offCamera.setLocation(new Vector3f(0f, 0f, -5f))
    offCamera.lookAt(new Vector3f(0f, 0f, 0f), Vector3f.UNIT_Y)
    //setup framebuffer's texture
    val offTex = new Texture2D(512, 512, Format.RGBA8)
    offTex.setMinFilter(Texture.MinFilter.Trilinear)
    offTex.setMagFilter(Texture.MagFilter.Bilinear)
    //setup framebuffer to use texture
    offBuffer.setDepthBuffer(Format.Depth)
    offBuffer.setColorTexture(offTex)
    //set viewport to render to offscreen framebuffer
    offView.setOutputFrameBuffer(offBuffer)
    // setup framebuffer's scene
    val boxMesh = new Box(1, 1, 1)
    val material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md")
    //assetManager.loadMaterial("Interface/Logo/Logo.j3m")
    offBox = new Geometry("box", boxMesh)
    offBox.setMaterial(material)
    // attach the scene to the viewport to be rendered
    offView.attachScene(offBox)
    offTex
  }

  override def simpleInitApp(): Unit = {
    cam.setLocation(new Vector3f(3, 3, 3))
    cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y)
    flyCam.setDragToRotate(true)

    //setup main scene
    val quad = new Geometry("box", new Box(1, 1, 1))
    val offTex = setupOffscreenView
    val mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md")
    mat.setTexture("ColorMap", offTex)
    quad.setMaterial(mat)
    rootNode.attachChild(quad)
    inputManager.addMapping(CubeSceneRenderer.TOGGLE_UPDATE, new KeyTrigger(KeyInput.KEY_SPACE))
    inputManager.addListener(this, CubeSceneRenderer.TOGGLE_UPDATE)
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
    if (name == CubeSceneRenderer.TOGGLE_UPDATE && isPressed) offView.setEnabled(!offView.isEnabled)
  }
}