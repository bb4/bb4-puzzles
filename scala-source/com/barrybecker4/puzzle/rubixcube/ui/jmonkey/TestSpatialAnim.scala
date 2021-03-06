package com.barrybecker4.puzzle.rubixcube.ui.jmonkey

import com.jme3.anim.AnimClip
import com.jme3.anim.AnimComposer
import com.jme3.anim.AnimTrack
import com.jme3.anim.TransformTrack
import com.jme3.app.SimpleApplication
import com.jme3.light.AmbientLight
import com.jme3.light.DirectionalLight
import com.jme3.math.Quaternion
import com.jme3.math.Vector3f
import com.jme3.scene.Geometry
import com.jme3.scene.Node
import com.jme3.scene.shape.Box


object TestSpatialAnim extends App {
  val app = new TestSpatialAnim
  app.start()
}

class TestSpatialAnim extends SimpleApplication {

  private val ANIM_NAME = "anim"

  override def simpleInitApp(): Unit = {
    val al = new AmbientLight
    rootNode.addLight(al)
    val dl = new DirectionalLight
    dl.setDirection(Vector3f.UNIT_XYZ.negate)
    rootNode.addLight(dl)

    val geom = createGeom()
    val model = new Node("model")
    model.attachChild(geom)

    val childGeom = createChildGeom()

    val childModel = createChildModel(childGeom)
    model.attachChild(childModel)

    val animComposer = createAnimComposer(geom, childGeom)

    model.addControl(animComposer)
    rootNode.attachChild(model)

    model.getControl(classOf[AnimComposer]).setCurrentAction(ANIM_NAME)
  }

  private def createGeom(): Geometry = {
    val box = new Box(1, 1, 1)
    val geom = new Geometry("box", box)
    geom.setMaterial(assetManager.loadMaterial("com/barrybecker4/puzzle/rubixcube/ui/assets/textures/brickWall/BrickWall.j3m"))
    geom
  }

  private def createChildGeom(): Geometry = {
    val child = new Box(0.5f, 0.5f, 0.5f)
    val childGeom = new Geometry("box", child)
    childGeom.setMaterial(assetManager.loadMaterial("com/barrybecker4/puzzle/rubixcube/ui/assets/textures/brickWall/BrickWall.j3m"))
    childGeom
  }

  private def createChildModel(childGeom: Geometry): Node = {
    val childModel = new Node("childmodel")
    childModel.setLocalTranslation(2, 2, 2)
    childModel.attachChild(childGeom)
    childModel
  }

  private def createAnimComposer(geom: Geometry, childGeom: Geometry): AnimComposer = {
    //animation parameters
    val animTime: Float = 5f
    val fps: Float = 25f
    val totalXLength: Float = 10f

    //calculating frames
    val totalFrames = (fps * animTime).toInt
    val dT = animTime / totalFrames
    var t: Float = 0
    val dX = totalXLength / totalFrames
    var x: Float = 0
    val times = new Array[Float](totalFrames)
    val translations = new Array[Vector3f](totalFrames)
    val rotations = new Array[Quaternion](totalFrames)
    val scales = new Array[Vector3f](totalFrames)

    for (i <- 0 until totalFrames) {
      times(i) = t
      t += dT
      translations(i) = new Vector3f(x, 0, 0)
      x += dX
      rotations(i) = Quaternion.IDENTITY
      scales(i) = Vector3f.UNIT_XYZ
    }

    val transformTrack = new TransformTrack(geom, times, translations, rotations, scales)
    val transformTrackChild = new TransformTrack(childGeom, times, translations, rotations, scales)

    val animClip = new AnimClip(ANIM_NAME)
    animClip.setTracks(Array[AnimTrack[_]](transformTrack, transformTrackChild))

    val animComposer = new AnimComposer
    animComposer.addAnimClip(animClip)
    animComposer
  }
}