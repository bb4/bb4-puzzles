package com.barrybecker4.puzzle.rubixcube.ui.render.jme

import com.barrybecker4.puzzle.rubixcube.model.Minicube
import com.jme3.asset.AssetManager
import com.jme3.font.{BitmapFont, BitmapText}
import com.jme3.math.{ColorRGBA, Vector3f}
import com.jme3.scene.Spatial.CullHint
import com.jme3.scene.control.BillboardControl
import com.jme3.scene.{Geometry, Node}

case class JmeUtil(assetManager: AssetManager) {

  //val TEXT_FONT: BitmapFont = assetManager.loadFont("Interface/Fonts/Console.fnt")
  val TEXT_FONT: BitmapFont = assetManager.loadFont("Interface/Fonts/Default.fnt")


  def createMinicubeCubeNode(minicube: Minicube): Geometry =
    new MinicubeNode(assetManager, minicube)


  def createTextNode(localPos: Vector3f, text: String): Node = {

    val hudText = new BitmapText(TEXT_FONT, false)

    hudText.setText(text)
    hudText.setSize(0.3f) //TEXT_FONT.getCharSet.getRenderedSize) // font size
    hudText.setColor(ColorRGBA.White)
    //hudText.setLocalTranslation(new Vector3f(0.01f, 0.03f, 0.1f))

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
