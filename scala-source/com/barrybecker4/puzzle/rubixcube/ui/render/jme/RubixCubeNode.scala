package com.barrybecker4.puzzle.rubixcube.ui.render.jme

import com.barrybecker4.puzzle.rubixcube.model.{Cube, FaceColor, Minicube, UP}
import com.jme3.scene.instancing.InstancedNode
import com.barrybecker4.puzzle.rubixcube.model._
import com.barrybecker4.puzzle.rubixcube.ui.render.jme.RubixCubeNode.SCALE
import com.jme3.asset.{AssetKey, AssetManager}
import com.jme3.math.Vector3f


object RubixCubeNode {
  private val SCALE: Float = 1.0f
}


/* Renders the whole cube. The cube is composed by many minicubeNodes. */
class RubixCubeNode(cube: Cube, assetManager: AssetManager)
  extends InstancedNode("cubeNodeParent") {

  val util: JmeUtil = JmeUtil(assetManager: AssetManager)

  cube.locationToMinicube.foreach({case (loc, minicube) =>
    val minicubeNode = new MinicubeNode(assetManager, minicube)
    minicubeNode.setLocalTranslation(new Vector3f(cube.size - loc._3, cube.size - loc._1 - 1, cube.size - loc._2).mult(SCALE))
    attachChild(minicubeNode)
  })

  // should need this, but for scala port
  override def setKey(key: AssetKey[_]): Unit = {
    this.key = key
  }

  override def getKey: AssetKey[_] = key
}
