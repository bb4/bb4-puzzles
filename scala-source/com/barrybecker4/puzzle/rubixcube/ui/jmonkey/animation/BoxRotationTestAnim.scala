package com.barrybecker4.puzzle.rubixcube.ui.jmonkey.animation

import com.jme3.app.SimpleApplication
import com.jme3.input.KeyInput
import com.jme3.input.controls.{ActionListener, KeyTrigger}
import com.jme3.light.{AmbientLight, DirectionalLight}
import com.jme3.math.{FastMath, Quaternion, Transform, Vector3f}
import com.jme3.scene.shape.Box
import com.jme3.scene.{Geometry, Node}


object BoxRotationTestAnim extends App {
  val app = new BoxRotationTestAnim
  app.setDisplayStatView(false)
  app.start()
}

class BoxRotationTestAnim extends SimpleApplication with ActionListener {
  private val ROTATE = "ROTATE"
  private val X_AXIS = new Vector3f(1f, 0, 0)
  private val Y_AXIS = new Vector3f(0, 1f, 0)
  private var model: Node = _
  private var rotation: Quaternion = new Quaternion()
  private var doRotation: Boolean = false
  private var rotatorParent: Node = _
  private var angle: Float = 0

  private var boxes: Array[Node] = new Array[Node](4)

  override def simpleInitApp(): Unit = {
    println("start....")
    flyCam.setDragToRotate(true)

    addLights(rootNode)

    model = new Node("model")
    rotatorParent = new Node("rotatorParent")
    model.attachChild(rotatorParent)

    val q = new Quaternion()
    q.fromAngles(0.2f, 0.5f, 0)
    model.setLocalRotation(q)

    boxes(0) = addBox(model, new Vector3f(-1.1f, 0, 0))
    boxes(1) = addBox(model, new Vector3f(1.1f, 0, 0))

    boxes(2) = addBox(model, new Vector3f(-1.1f, 2.2f, .2f))
    boxes(3) = addBox(model, new Vector3f(1.1f, 2.2f, .2f))

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

  private def addBox(parent: Node, translation: Vector3f = Vector3f.ZERO, boxSize: Float = 1f): Node = {
    val geom = createBoxGeom(boxSize)

    val childModel = new Node("childbox")
    childModel.setLocalTranslation(translation)
    childModel.attachChild(geom)
    parent.attachChild(childModel)
    childModel
  }

  private def createBoxGeom(size: Float): Geometry = {
    val box = new Box(size, size, size)
    val geom = new Geometry("box", box)
    geom.setMaterial(assetManager.loadMaterial("com/barrybecker4/puzzle/rubixcube/ui/assets/textures/brickWall/BrickWall.j3m"))
    geom
  }


  override def onAction(binding: String, value: Boolean, tpf: Float): Unit = {
    if (binding == ROTATE && value) {
      doRotation = true
      if (doRotation && !model.getChildren.isEmpty) {
        model.detachChild(boxes(0))
        model.detachChild(boxes(1))

        rotatorParent.attachChild(boxes(0))
        rotatorParent.attachChild(boxes(1))
      }
    }
  }

  private def rotationFinished(): Unit = {
    rotatorParent.detachAllChildren();

    model.attachChild(boxes(0))
    model.attachChild(boxes(1))
    angle = 0
    doRotation = false
  }

  override def simpleUpdate(tpf: Float): Unit = { // Rotate around X axis

    if (doRotation && angle < FastMath.HALF_PI) {
      val rotate = new Quaternion
      angle += 0.001f
      rotate.fromAngleAxis(angle, Vector3f.UNIT_X)
      //rotation.multLocal(rotate)
      rotatorParent.setLocalRotation(rotate)
      if (angle >= FastMath.HALF_PI) {
        rotationFinished()
      }
    }

    // global rotation
    val rotate1 = new Quaternion
    rotate1.fromAngleAxis(tpf, Vector3f.UNIT_X)
    rotation.multLocal(rotate1)
    model.setLocalRotation(rotation)
  }

}