package com.barrybecker4.puzzle.rubixcube.ui.util

import com.jme3.asset.AssetManager
import com.jme3.material.Material
import com.jme3.math.{ColorRGBA, Vector3f}
import com.jme3.scene.{Geometry, Mesh, Node}
import com.jme3.scene.debug.Arrow
import com.jme3.math.ColorRGBA
import com.jme3.font.BitmapFont
import com.jme3.font.BitmapText
import com.jme3.scene.Spatial.CullHint
import com.jme3.scene.control.BillboardControl


case class Jme3Util(rootNode: Node, assetManager: AssetManager) {

  //val TEXT_FONT: BitmapFont = assetManager.loadFont("Interface/Fonts/Console.fnt")
  val TEXT_FONT: BitmapFont = assetManager.loadFont("Interface/Fonts/Default.fnt")

  def attachCoordinateAxes(pos: Vector3f, parentNode: Node): Unit = {
    addArrow("X", ColorRGBA.Red, pos, parentNode)
    addArrow("Y", ColorRGBA.Green, pos, parentNode)
    addArrow("Z", ColorRGBA.Blue, pos, parentNode)
  }

  private def addArrow(axis: String, color: ColorRGBA, pos: Vector3f, parentNode: Node): Unit = {

    val arrowDirection = axis match {
      case "X" => Vector3f.UNIT_X
      case "Y" => Vector3f.UNIT_Y
      case "Z" => Vector3f.UNIT_Z
    }
    val arrow = new Arrow(arrowDirection)

    val axisGeom = new Geometry(axis + "coordinate axis", arrow)
    val mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md")
    mat.getAdditionalRenderState.setWireframe(true)
    mat.getAdditionalRenderState.setLineWidth(4)
    mat.setColor("Color", color)
    axisGeom.setMaterial(mat)
    parentNode.attachChild(axisGeom)

    val textNode = createTextNode(pos.add(arrowDirection), axis)
    parentNode.attachChild(textNode)

    axisGeom.setLocalTranslation(pos)
    //textNode.setLocalTranslation(pos)
  }

  def createTextNode(pos: Vector3f, text: String): Node = {

    val hudText = new BitmapText(TEXT_FONT, false)
    hudText.setSize(TEXT_FONT.getCharSet.getRenderedSize) // font size

    hudText.setColor(ColorRGBA.White)
    hudText.setText(text)
    hudText.setLocalTranslation(new Vector3f(0.1f, 0.2f, 0.3f))

    val bbControl: BillboardControl = new BillboardControl();
    bbControl.setAlignment(BillboardControl.Alignment.Screen);
    val textNode: Node = new Node("Node for text");
    textNode.setLocalTranslation(pos);
    textNode.setCullHint(CullHint.Never);
    textNode.addControl(bbControl);
    textNode.attachChild(hudText);
    textNode
  }
}
