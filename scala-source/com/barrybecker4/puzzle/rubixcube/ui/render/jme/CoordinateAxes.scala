package com.barrybecker4.puzzle.rubixcube.ui.render.jme

import com.jme3.asset.{AssetKey, AssetManager}
import com.jme3.material.Material
import com.jme3.math.{ColorRGBA, Vector3f}
import com.jme3.scene.debug.Arrow
import com.jme3.scene.instancing.InstancedNode
import com.jme3.scene.{Geometry, Node}



class CoordinateAxes(pos: Vector3f, assetManager: AssetManager) extends InstancedNode {

  private val util = JmeUtil(assetManager)

  attachCoordinateAxes(pos)

  def attachCoordinateAxes(pos: Vector3f): Unit = {
    addArrow("X", ColorRGBA.White, pos, this)
    addArrow("Y", ColorRGBA.Green, pos, this)
    addArrow("Z", ColorRGBA.Red, pos, this)
    this.setLocalTranslation(pos)
  }

  // should need this, but for scala port
  override def setKey(key: AssetKey[_]): Unit = {
    this.key = key
  }

  override def getKey: AssetKey[_] = key

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

    val textNode = util.createTextNode(arrowDirection, axis)
    parentNode.attachChild(textNode)
  }
}

