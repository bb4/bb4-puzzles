package com.barrybecker4.puzzle.rubixcube.ui.render.jme

import com.jme3.asset.AssetManager
import com.jme3.font.{BitmapFont, BitmapText}
import com.jme3.math.{ColorRGBA, Vector3f}
import com.jme3.scene.Spatial.CullHint
import com.jme3.scene.control.BillboardControl
import com.jme3.scene.Node

case class JmeUtil(assetManager: AssetManager) {
  
  val TEXT_FONT: BitmapFont = assetManager.loadFont("Interface/Fonts/Default.fnt")


  def createTextNode(localPos: Vector3f, text: String): Node = {

    val hudText = new BitmapText(TEXT_FONT, false)

    hudText.setText(text)
    // Keep axis labels small in world space (0.3 looked huge / washed-out on retina + BillboardControl).
    hudText.setSize(0.12f)
    hudText.setColor(ColorRGBA.White)

    val bbControl: BillboardControl = new BillboardControl()
    bbControl.setAlignment(BillboardControl.Alignment.Screen)
    val textNode: Node = new Node("Node for text")
    textNode.setLocalTranslation(localPos)
    textNode.setCullHint(CullHint.Never)
    textNode.addControl(bbControl)
    textNode.attachChild(hudText)
    textNode
  }
}
