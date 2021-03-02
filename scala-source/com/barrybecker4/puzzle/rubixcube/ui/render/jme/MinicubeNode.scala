package com.barrybecker4.puzzle.rubixcube.ui.render.jme

import com.barrybecker4.puzzle.rubixcube.model._
import com.barrybecker4.puzzle.rubixcube.ui.render.jme.MinicubeNode.EDGE_LEN
import com.jme3.asset.{AssetKey, AssetManager}
import com.jme3.material.Material
import com.jme3.scene.Geometry
import com.jme3.scene.VertexBuffer.Type
import com.jme3.scene.shape.Box
import com.jme3.util.BufferUtils

import java.awt.Color

object MinicubeNode {
  private val EDGE_LEN: Float = 0.47f
}


case class MinicubeNode(assetManager: AssetManager,
  upColor: Color = Color.GREEN, downColor: Color = Color.BLUE,
  leftColor: Color = Color.ORANGE, rightColor: Color = Color.RED,
  frontColor: Color = Color.WHITE, backColor: Color = Color.YELLOW)
  extends Geometry("Minicube", new Box(EDGE_LEN, EDGE_LEN, EDGE_LEN)) {


  def this(assetManager: AssetManager, minicube: Minicube) {
    this(assetManager,
      minicube.getColorFor(UP), minicube.getColorFor(DOWN),
      minicube.getColorFor(LEFT), minicube.getColorFor(RIGHT),
      minicube.getColorFor(FRONT), minicube.getColorFor(BACK)
    )
  }

  // "Common/MatDefs/Light/Lighting.j3md" or "Common/MatDefs/Misc/ShowNormals.j3md"?
  val mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md")

  mat.setBoolean("VertexColor", true)
  setMaterial(mat)

  private val rightColorF = rightColor.getRGBComponents(null)
  private val leftColorF = leftColor.getRGBComponents(null)
  private val upColorF = upColor.getRGBComponents(null)
  private val downColorF = downColor.getRGBComponents(null)
  private val frontColorF = frontColor.getRGBComponents(null)
  private val backColorF = backColor.getRGBComponents(null)

  def colors(color: Array[Float]): Array[Float] = (for (i <- 0 to 3) yield color).flatten.toArray

  val colorBuf: Array[Float] = Array[Array[Float]](
    colors(rightColorF),
    colors(frontColorF),
    colors(leftColorF),
    colors(backColorF),
    colors(upColorF),
    colors(downColorF)
  ).flatten

  mesh.setBuffer(Type.Color, 4, colorBuf)

  //private val normals = Array[Float](0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1)
  //mesh.setBuffer(Type.Normal, 3, BufferUtils.createFloatBuffer(normals: _*))

  // should need this, but for scala port
  override def setKey(key: AssetKey[_]): Unit = {
    this.key = key
  }

  override def getKey: AssetKey[_] = key
}
