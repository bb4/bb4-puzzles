package com.barrybecker4.puzzle.rubixcube.ui.jmonkey.animation

import com.jme3.anim.{AnimClip, AnimComposer, AnimTrack, TransformTrack}
import com.jme3.animation.{AnimChannel, AnimControl, AnimEventListener}
import com.jme3.app.SimpleApplication
import com.jme3.input.KeyInput
import com.jme3.input.controls.{ActionListener, KeyTrigger}
import com.jme3.light.{AmbientLight, DirectionalLight}
import com.jme3.math.{Quaternion, Vector3f}
import com.jme3.scene.{Geometry, Node}
import com.jme3.scene.shape.Box


object TestSpatialAnim extends App {
  val app = new TestSpatialAnim
  app.start()
}

class TestSpatialAnim extends SimpleApplication with ActionListener with AnimEventListener {
  private val ANIM_NAME = "anim"
  private val ROTATE = "ROTATE"
  private val X_AXIS = new Vector3f(1f, 0, 0)
  private val Y_AXIS = new Vector3f(0, 1f, 0)
  private var model: Node = _

  override def simpleInitApp(): Unit = {
    flyCam.setDragToRotate(true)

    addLights(rootNode)

    model = new Node("model")

    addBox(model, new Vector3f(0, 0, -1.1f))
    addBox(model, new Vector3f(0, 0, 1.1f))

    addBox(model, new Vector3f(0, 3, -1.1f))
    addBox(model, new Vector3f(0, 3, 1.1f))

    val animComposer = createAnimComposer(model)

    model.addControl(animComposer)
    rootNode.attachChild(model)

    initActions()
  }

  private def initActions(): Unit = {
    inputManager.addListener(this, ROTATE)
    inputManager.addMapping(ROTATE, new KeyTrigger(KeyInput.KEY_SPACE))
  }

  private def addLights(rootNode: Node): Unit = {
    val al = new AmbientLight
    rootNode.addLight(al)
    val dl = new DirectionalLight
    dl.setDirection(Vector3f.UNIT_XYZ.negate)
    rootNode.addLight(dl)
  }

  private def addBox(parent: Node, translation: Vector3f = Vector3f.ZERO, boxSize: Float = 1f): Unit = {
    val geom = createBoxGeom(boxSize)

    val childModel = new Node("childbox")
    childModel.setLocalTranslation(translation)
    childModel.attachChild(geom)
    parent.attachChild(childModel)
  }

  private def createBoxGeom(size: Float): Geometry = {
    val box = new Box(size, size, size)
    val geom = new Geometry("box", box)
    geom.setMaterial(assetManager.loadMaterial("com/barrybecker4/puzzle/rubixcube/ui/assets/textures/brickWall/BrickWall.j3m"))
    geom
  }

  private def createAnimComposer(geom: Node): AnimComposer = {
    val animTime: Float = 1f
    val fps: Float = 25f
    val totalXLength: Float = 5f

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
      val q = new Quaternion()
      q.fromAngleAxis(t * 3, Y_AXIS)
      val q2 = new Quaternion()
      q2.fromAngleAxis(t, X_AXIS)
      rotations(i) = q   //Quaternion.IDENTITY
      scales(i) = Vector3f.UNIT_XYZ
    }

    val transformTrack = new TransformTrack(geom, times, translations, rotations, scales)

    val animClip = new AnimClip(ANIM_NAME)
    animClip.setTracks(Array[AnimTrack[_]](transformTrack))

    val animComposer = new AnimComposer
    animComposer.addAnimClip(animClip)
    animComposer
  }

  override def onAction(binding: String, value: Boolean, tpf: Float): Unit = {
    if (binding == ROTATE && value) {
      println("seting action to " + ANIM_NAME)
      model.getControl(classOf[AnimComposer]).setCurrentAction(ANIM_NAME)
    }

  }

  override def onAnimCycleDone(control: AnimControl, channel: AnimChannel, animName: String): Unit = {
    if (animName.equals(ANIM_NAME)) {
      println("done " + ANIM_NAME)
    }
  }

  override def onAnimChange(control: AnimControl, channel: AnimChannel, animName: String): Unit = {
  }
}