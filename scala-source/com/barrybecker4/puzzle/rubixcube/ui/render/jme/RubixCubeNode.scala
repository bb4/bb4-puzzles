package com.barrybecker4.puzzle.rubixcube.ui.render.jme

import com.barrybecker4.puzzle.rubixcube.model.{Cube, Location}
import com.jme3.scene.instancing.InstancedNode
import com.jme3.asset.{AssetKey, AssetManager}
import com.jme3.math.Vector3f



/* Renders the whole cube. The cube is composed by many minicubeNodes. */
class RubixCubeNode(cube: Cube, assetManager: AssetManager)
  extends InstancedNode("cubeNodeParent") {

  //private val util: JmeUtil = JmeUtil(assetManager: AssetManager)
  private val halfEdgeLen: Float = cube.size / 2.0f
  this.setLocalScale(3f / cube.size)

  // maintain a map from locations to minicubeNodes so that we can easily upate the colors
  private var locToMinicube: Map[Location, MinicubeNode] = Map()

  cube.locationToMinicube.foreach({case (loc, minicube) =>
    val minicubeNode = new MinicubeNode(assetManager, minicube)
    minicubeNode.setLocalTranslation(new Vector3f(halfEdgeLen - loc._3, halfEdgeLen - loc._1, halfEdgeLen - loc._2))
    locToMinicube += loc -> minicubeNode
    attachChild(minicubeNode)
  })

  // the new cube must be the same size as the old
  def updateCube(newCube: Cube): Unit = {
    newCube.locationToMinicube.foreach({case (loc, minicube) =>
      val minicubeNode = locToMinicube(loc)
      minicubeNode.updateColors(minicube)
    })
  }

  // should need this, but for scala port
  override def setKey(key: AssetKey[_]): Unit = {
    this.key = key
  }

  override def getKey: AssetKey[_] = key
}
