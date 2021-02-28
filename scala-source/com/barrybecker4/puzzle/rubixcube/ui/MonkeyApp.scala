package com.barrybecker4.puzzle.rubixcube.ui

import com.jme3.app.SimpleApplication
import com.jme3.material.Material
import com.jme3.math.Vector3f
import com.jme3.scene.{Geometry, Node}
import com.jme3.scene.shape.Box
import com.jme3.system.AppSettings
import com.jme3.light.DirectionalLight
import com.jme3.math.ColorRGBA


object MonkeyApp {

  def main(args: Array[String]): Unit = {
    val app = new MonkeyApp
    val settings = new AppSettings(false)
    settings.setTitle("Rubix Cube Solver")
    app.setSettings(settings)
    app.start()
  }
}

class MonkeyApp extends SimpleApplication {

  private var cube: Geometry = _

  def simpleInitApp(): Unit = {
    val b = new Box(1, 1, 1)
    cube = new Geometry("Box", b)
    val cubeMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md")

    import com.jme3.math.ColorRGBA
    cubeMat.setBoolean("UseMaterialColors", true)
    cubeMat.setColor("Diffuse", ColorRGBA.Green)
    cubeMat.setColor("Specular", ColorRGBA.White)
    cubeMat.setFloat("Shininess", 64f) // [0,128]

    cube.setMaterial(cubeMat)

    val pivotNode = new Node("pivot")
    pivotNode.rotate(0.4f, 0.4f, 0f)
    pivotNode.attachChild(cube)
    rootNode.attachChild(pivotNode) // put this node in the scene

    /** Must add a light to make the lit object visible! */
    val sun: DirectionalLight = new DirectionalLight
    sun.setDirection(new Vector3f(1, 0, -(2)).normalizeLocal)
    sun.setColor(ColorRGBA.White)
    rootNode.addLight(sun)
  }

  // main loop
  override def simpleUpdate(tpf: Float): Unit = {
    cube.rotate(0, 2 * tpf, 0)
  }
}