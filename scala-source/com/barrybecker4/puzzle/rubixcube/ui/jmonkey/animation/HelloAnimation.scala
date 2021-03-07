package com.barrybecker4.puzzle.rubixcube.ui.jmonkey.animation

import com.jme3.anim.AnimComposer
import com.jme3.animation.AnimChannel
import com.jme3.animation.AnimControl
import com.jme3.animation.AnimEventListener
import com.jme3.animation.LoopMode
import com.jme3.app.SimpleApplication
import com.jme3.input.KeyInput
import com.jme3.input.controls.ActionListener
import com.jme3.input.controls.KeyTrigger
import com.jme3.light.DirectionalLight
import com.jme3.math.ColorRGBA
import com.jme3.math.Vector3f
import com.jme3.scene.Node


/** Sample 7 - how to load an OgreXML model and play an animation,
  * using channels, a controller, and an AnimEventListener. */
object HelloAnimation {
  def main(args: Array[String]): Unit = {
    val app = new HelloAnimation
    app.start()
  }
}

class HelloAnimation extends SimpleApplication with AnimEventListener {
  private var channel: AnimChannel = _

  override def simpleInitApp(): Unit = {
    viewPort.setBackgroundColor(ColorRGBA.LightGray)
    initKeys()

    val dl = new DirectionalLight
    dl.setDirection(new Vector3f(-0.1f, -1f, -1).normalizeLocal)
    rootNode.addLight(dl)

    rootNode.attachChild(createPlayer())
  }

  private def createPlayer(): Node = {
    val player = assetManager.loadModel("com/barrybecker4/puzzle/rubixcube/ui/assets/models/Oto/Oto.mesh.xml").asInstanceOf[Node]
    player.setLocalScale(0.5f)
    val control = player.getControl(classOf[AnimControl])
    control.addListener(this)
    channel = control.createChannel
    channel.setAnim("stand")
    player
  }

  override def onAnimCycleDone(control: AnimControl, channel: AnimChannel, animName: String): Unit = {
    if (animName == "Walk") {
      channel.setAnim("stand", 0.50f)
      channel.setLoopMode(LoopMode.DontLoop)
      channel.setSpeed(1f)
    }
  }

  override def onAnimChange(control: AnimControl, channel: AnimChannel, animName: String): Unit = {
    // unused
  }

  /** Custom Keybinding: Map named actions to inputs. */
  private def initKeys(): Unit = {
    inputManager.addMapping("Walk", new KeyTrigger(KeyInput.KEY_SPACE))
    inputManager.addListener(actionListener, "Walk")
  }

  private val actionListener = new ActionListener() {
    override def onAction(name: String, keyPressed: Boolean, tpf: Float): Unit = {
      if (name == "Walk" && !keyPressed) if (!(channel.getAnimationName == "Walk")) {
        channel.setAnim("Walk", 0.50f)
        channel.setLoopMode(LoopMode.Loop)
      }
    }
  }
}