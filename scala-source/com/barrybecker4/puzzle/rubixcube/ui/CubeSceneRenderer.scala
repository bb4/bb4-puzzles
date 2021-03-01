package com.barrybecker4.puzzle.rubixcube.ui

import com.barrybecker4.puzzle.rubixcube.ui.util.{CoordinateAxes, Jme3Util}
import com.jme3.app.SimpleApplication
import com.jme3.material.Material
import com.jme3.scene.{Geometry, Mesh, Spatial}
import com.jme3.scene.shape.Box
import com.jme3.input.ChaseCamera
import com.jme3.light.DirectionalLight
import com.jme3.math.{ColorRGBA, FastMath, Quaternion, Vector3f}
import com.jme3.scene.VertexBuffer.Type
import com.jme3.scene.instancing.InstancedNode
import com.jme3.system.AppSettings
import com.jme3.util.BufferUtils


/**
  * Renders the rubix cube in 3D using JMonkeyEngine.
  */
object CubeSceneRenderer extends App {
  val app = new CubeSceneRenderer

  val settings = new AppSettings(false)
  settings.setTitle("Rubix Cube Solver")
  app.setSettings(settings)
  app.setDisplayStatView(false)

  app.start()
}

class CubeSceneRenderer extends SimpleApplication {

  override def simpleInitApp(): Unit = {
    flyCam.setEnabled(false)
    //flyCam.setDragToRotate(true)

    val cubeNode = createCubeScene()
    rootNode.attachChild(cubeNode)

    val q = new Quaternion()
    q.fromAngleAxis((Math.PI / 2).toFloat, new Vector3f(0, 1, 0))
    rootNode.setLocalRotation(q)

    rootNode.attachChild(new CoordinateAxes(new Vector3f(-1.5f, -1.5f, 1.5f), assetManager))
    rootNode.addLight(createDirectionalLight())

    useChaseCamera()
  }

  /** Global scenegraph */
  private def createCubeScene(): Spatial = {
    val cube = createCube()

    val cubeNode = new InstancedNode()

    val q = new Quaternion()
    q.fromAngleAxis(0, new Vector3f(0, 1, 0))
    cubeNode.setLocalRotation(q)
    cubeNode.attachChild(cube)
    cubeNode
  }

  private def useChaseCamera(): Unit = {
    val chaseCam = new ChaseCamera(cam, rootNode, inputManager)
    chaseCam.setMaxVerticalRotation(2 * FastMath.PI)
    chaseCam.setMinVerticalRotation(-2 * FastMath.PI)
    chaseCam.setSmoothMotion(false)
    chaseCam.setDefaultDistance(4)
  }

  private def createDirectionalLight(): DirectionalLight = {
    val sun: DirectionalLight = new DirectionalLight
    sun.setDirection(new Vector3f(1, 1, 1).normalizeLocal)
    sun.setColor(ColorRGBA.White)
    sun
  }

  private def createCube() = {

    val mesh: Box = new Box(1, 1, 1);
    val geom: Geometry = new Geometry("Minicube", mesh);

    val mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md")
    //val mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md")
    //val mat = new Material(assetManager, "Common/MatDefs/Misc/ShowNormals.j3md")

    mat.setBoolean("VertexColor", true)
    geom.setMaterial(mat);
    //rootNode.attachChild(geom);

    //mat.setBoolean("UseVertexColor", true); // UserVertexColor?
    //mat.setBoolean("UseMaterialColors", true);
    //mat.setColor("Ambient", ColorRGBA.Yellow);
    //mat.setColor("Diffuse", ColorRGBA.White);
    //mat.setColor("Specular", specular); //Using yellow for example
    //mat.setBoolean("VertexLighting", true);

    mesh.setBuffer(Type.Color, 4, Array[Float](
      1, 0.5f, 0, 1,
      1, 0.5f, 0, 1,
      1, 0.5f, 0, 1,
      1, 0.5f, 0, 1,

      1, 1, 1, 1,
      1, 1, 1, 1,
      1, 1, 1, 1,
      1, 1, 1, 1,

      1, 0, 0.1f, 1,
      1, 0, 0.1f, 1,
      1, 0, 0.1f, 1,
      1, 0, 0.1f, 1,

      1, 1, 0, 1,
      1, 1, 0, 1,
      1, 1, 0, 1,
      1, 1, 0, 1,

      0, 1, 0, 1,
      0, 1, 0, 1,
      0, 1, 0, 1,
      0, 1, 0, 1,

      0, 0, 1, 1,
      0, 0, 1, 1,
      0, 0, 1, 1,
      0, 0, 1, 1,
    ));

    val normals = Array[Float](0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1)
    mesh.setBuffer(Type.Normal, 3, BufferUtils.createFloatBuffer(normals: _*))

    geom
  }



  override def simpleUpdate(tpf: Float): Unit = {
    //val q = new Quaternion
  }

}