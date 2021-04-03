package com.barrybecker4.puzzle.rubixcube.ui.render.jme

import com.barrybecker4.puzzle.rubixcube.Location
import com.barrybecker4.puzzle.rubixcube.model.{Cube, CubeMove, Direction}
import com.jme3.scene.instancing.InstancedNode
import com.jme3.asset.{AssetKey, AssetManager}
import com.jme3.math.Vector3f



/* Renders the whole cube. The cube is composed of many minicubeNodes.
 * We do not render cubes that are on the interior.
 */
class RubixCubeNode(cube: Cube, assetManager: AssetManager)
  extends InstancedNode("cubeNodeParent") {

  private val halfEdgeLen: Float = cube.size / 2.0f
  private var sliceNode: Option[SliceNode] = None
  private var sliceMinicubes: Seq[MinicubeNode] = _


  this.setLocalScale(3f / cube.size)

  // maintain a map from locations to minicubeNodes so that we can easily update the colors, or rotate slices
  private var locToMinicubeNode: Map[Location, MinicubeNode] = Map()

  cube.locationToMinicube.foreach({case (loc, minicube) =>
    val minicubeNode = new MinicubeNode(assetManager, minicube)
    minicubeNode.setLocalTranslation(getPosition(loc))
    locToMinicubeNode += loc -> minicubeNode
    attachChild(minicubeNode)
  })

  // Rotate the slice, then set the new state at the end
  def startRotatingSlice(cubeMove: CubeMove): Unit = {

    val loc2Mini = cube.getSlice(cubeMove.orientation, cubeMove.level)

    var sliceMinis: Seq[MinicubeNode] = Seq()
    loc2Mini.keys.foreach(loc => {
      val miniNode = locToMinicubeNode(loc)
      this.detachChild(miniNode)
      sliceMinis :+= miniNode
    })

    sliceMinicubes = sliceMinis

    val isClockwise = cubeMove.direction == Direction.CLOCKWISE
    val sn = new SliceNode(sliceMinicubes, cubeMove.orientation, isClockwise, () => doneRotating())
    this.attachChild(sn)
    sliceNode = Some(sn)
  }

  def isRotating: Boolean = sliceNode.isDefined

  def incrementSliceRotation(): Unit = {
    if (sliceNode.isDefined) {
      sliceNode.get.incrementSliceRotation()
    }
  }

  private def getPosition(loc: Location): Vector3f = {
    new Vector3f(halfEdgeLen - loc._3 + 0.5f, halfEdgeLen - loc._1 + 0.5f, halfEdgeLen - loc._2 + 0.5f)
  }

  def doneRotating(): Unit = {
    this.detachChild(sliceNode.get)
    sliceMinicubes.foreach(this.attachChild)
    sliceMinicubes = Seq()
    sliceNode = None
  }

  // the new cube must be the same size as the old
  def updateCube(newCube: Cube): Unit = {
    assert(newCube.size == cube.size, "newCube has size " + newCube.size + " but should have " + cube.size)
    newCube.locationToMinicube.foreach({case (loc, minicube) =>
      locToMinicubeNode(loc).updateColors(minicube)
    })
  }

  // should need this, but for scala port
  override def setKey(key: AssetKey[_]): Unit = {
    this.key = key
  }

  override def getKey: AssetKey[_] = key
}
