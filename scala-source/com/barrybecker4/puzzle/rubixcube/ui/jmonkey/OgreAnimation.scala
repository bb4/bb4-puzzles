package com.barrybecker4.puzzle.rubixcube.ui.jmonkey

import com.jme3.anim.AnimClip
import com.jme3.anim.AnimComposer
import com.jme3.anim.SkinningControl
import com.jme3.anim.tween.Tween
import com.jme3.anim.tween.Tweens
import com.jme3.anim.tween.action.Action
import com.jme3.anim.tween.action.BaseAction
import com.jme3.anim.tween.action.LinearBlendSpace
import com.jme3.app.SimpleApplication
import com.jme3.input.KeyInput
import com.jme3.input.controls.ActionListener
import com.jme3.input.controls.KeyTrigger
import com.jme3.light.DirectionalLight
import com.jme3.math.ColorRGBA
import com.jme3.math.Quaternion
import com.jme3.math.Vector3f
import com.jme3.scene.Geometry
import com.jme3.scene.shape.Box


object OgreAnimation extends App {
  val app = new OgreAnimation()
  app.start()
}

// Hit space key to make the Ogre "attack"
class OgreAnimation extends SimpleApplication with ActionListener {
  private var animComposer: AnimComposer = _
  var currentAction: Action = _

  override def simpleInitApp(): Unit = {
    flyCam.setMoveSpeed(10f)
    cam.setLocation(new Vector3f(6.4013605f, 7.488437f, 12.843031f))
    cam.setRotation(new Quaternion(-0.060740203f, 0.93925786f, -0.2398315f, -0.2378785f))

    val dl = new DirectionalLight
    dl.setDirection(new Vector3f(-0.1f, -0.7f, -1).normalizeLocal)
    dl.setColor(new ColorRGBA(1f, 1f, 1f, 1.0f))
    rootNode.addLight(dl)
    val model = assetManager.loadModel("com/barrybecker4/puzzle/rubixcube/ui/assets/models/Oto/Oto.mesh.xml")
    model.center

    animComposer = model.getControl(classOf[AnimComposer])
    animComposer.actionBlended("Attack", new LinearBlendSpace(0f, 0.5f), "Dodge")

    animComposer.getAnimClips.forEach(animClip => {
      var action = animComposer.action(animClip.getName)
      if (!("stand" == animClip.getName))
        action = new BaseAction(Tweens.sequence(action, Tweens.callMethod(this, "backToStand", animComposer)))
      println("adding action for " + animClip.getName)
      animComposer.addAction(animClip.getName, action)
    })
    currentAction = animComposer.setCurrentAction("stand") // Walk, pull, Dodge, stand, push

    val skinningControl = model.getControl(classOf[SkinningControl])
    skinningControl.setHardwareSkinningPreferred(false)
    val b = new Box(.25f, 3f, .25f)
    val item = new Geometry("Item", b)
    item.move(0, 1.5f, 0)
    item.setMaterial(assetManager.loadMaterial("Common/Materials/RedColor.j3m"))
    val n = skinningControl.getAttachmentsNode("hand.right")
    n.attachChild(item)
    rootNode.attachChild(model)
    inputManager.addListener(this, "Attack")
    inputManager.addMapping("Attack", new KeyTrigger(KeyInput.KEY_SPACE))
  }

  def backToStand(animComposer: AnimComposer): Tween = {
    currentAction = animComposer.setCurrentAction("stand")
    currentAction
  }

  override def onAction(binding: String, value: Boolean, tpf: Float): Unit = {
    if (binding == "Attack" && value) if (currentAction != null && !(currentAction == animComposer.getAction("Dodge"))) {
      currentAction = animComposer.setCurrentAction("Dodge")
      currentAction.setSpeed(0.1f)
    }
  }
}