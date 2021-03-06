package com.barrybecker4.puzzle.rubixcube.ui.render.jme

import com.barrybecker4.puzzle.rubixcube.model.Cube
import com.jme3.scene.instancing.InstancedNode
import com.jme3.asset.{AssetKey, AssetManager}
import com.jme3.math.Vector3f



/* Renders the whole cube. The cube is composed by many minicubeNodes. */
class RubixCubeNode(cube: Cube, assetManager: AssetManager)
  extends InstancedNode("cubeNodeParent") {

  private val util: JmeUtil = JmeUtil(assetManager: AssetManager)
  private val halfEdgeLen: Float = cube.size / 2.0f
  this.setLocalScale(3f / cube.size)

  cube.locationToMinicube.foreach({case (loc, minicube) =>
    val minicubeNode = new MinicubeNode(assetManager, minicube)
    minicubeNode.setLocalTranslation(new Vector3f(halfEdgeLen - loc._3, halfEdgeLen - loc._1, halfEdgeLen - loc._2))
    attachChild(minicubeNode)
  })


  // should need this, but for scala port
  override def setKey(key: AssetKey[_]): Unit = {
    this.key = key
  }

  override def getKey: AssetKey[_] = key
}
