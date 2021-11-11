package com.barrybecker4.puzzle.rubixcube.ui.render.jme

import com.barrybecker4.puzzle.rubixcube.model._
import com.barrybecker4.puzzle.rubixcube.model.Orientation._
import com.barrybecker4.puzzle.rubixcube.ui.render.jme.MinicubeNode.EDGE_LEN
import com.jme3.asset.{AssetKey, AssetManager}
import com.jme3.material.Material
import com.jme3.scene.Geometry
import com.jme3.scene.VertexBuffer.Type
import com.jme3.scene.shape.Box


object MinicubeNode {
  private val EDGE_LEN: Float = 0.47f
}


case class MinicubeNode(assetManager: AssetManager, minicube: Minicube)
  extends Geometry("Minicube", new Box(EDGE_LEN, EDGE_LEN, EDGE_LEN)) {

  // hack to avoid error overriding method clone in trait CloneableSmartAsset
  override def clone: MinicubeNode = super.clone.asInstanceOf[MinicubeNode]
  
  // "Common/MatDefs/Light/Lighting.j3md" or "Common/MatDefs/Misc/ShowNormals.j3md"?
  val mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md")

  mat.setBoolean("VertexColor", true)
  setMaterial(mat)
  def colors(color: Array[Float]): Array[Float] = (for (i <- 0 to 3) yield color).flatten.toArray

  updateColors(minicube)


  def updateColors(minicube: Minicube): Unit = {
    val colorBuf: Array[Float] = Array[Array[Float]](
      colors(minicube.getColorFor(RIGHT).getRGBComponents(null)),
      colors(minicube.getColorFor(FRONT).getRGBComponents(null)),
      colors(minicube.getColorFor(LEFT).getRGBComponents(null)),
      colors(minicube.getColorFor(BACK).getRGBComponents(null)),
      colors(minicube.getColorFor(UP).getRGBComponents(null)),
      colors(minicube.getColorFor(DOWN).getRGBComponents(null))
    ).flatten

    mesh.setBuffer(Type.Color, 4, colorBuf)
  }

  // should need this, but for scala port
  override def setKey(key: AssetKey[_]): Unit = {
    this.key = key
  }

  override def getKey: AssetKey[_] = key
}
