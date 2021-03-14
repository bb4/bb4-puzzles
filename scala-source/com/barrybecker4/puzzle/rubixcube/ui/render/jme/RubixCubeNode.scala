package com.barrybecker4.puzzle.rubixcube.ui.render.jme

import com.barrybecker4.puzzle.rubixcube.model.{Cube, CubeMove, Direction, FRONT, LEFT, Location, Orientation, UP}
import com.jme3.scene.instancing.InstancedNode
import com.jme3.asset.{AssetKey, AssetManager}
import com.jme3.math.{FastMath, Quaternion, Vector3f}
import com.jme3.scene.Node
import com.barrybecker4.puzzle.rubixcube.ui.render.jme.RubixCubeNode.DEFAULT_ROTATION_INCREMENT


object RubixCubeNode {
  val DEFAULT_ROTATION_INCREMENT: Float = 0.001f
}


/* Renders the whole cube. The cube is composed by many minicubeNodes. */
class RubixCubeNode(cube: Cube, assetManager: AssetManager)
  extends InstancedNode("cubeNodeParent") {

  private var rotationInc: Float = DEFAULT_ROTATION_INCREMENT
  private val halfEdgeLen: Float = cube.size / 2.0f
  private val sliceNode: Node = new Node("slice")
  private var sliceOrientation: Orientation = _
  private var sliceRotationAngle: Float = 0f
  private var isClockwise: Boolean = false

  this.setLocalScale(3f / cube.size)
  // The slice node is used to rotate slices to show animated cube rotations
  this.attachChild(sliceNode)

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
    assert(!isRotating, "Only one active slice allowed at a time.")
    isClockwise = cubeMove.direction == Direction.CLOCKWISE
    createSlice(cubeMove.orientation, cubeMove.level)
  }

  def isRotating: Boolean = !sliceNode.getChildren.isEmpty

  def incrementSliceRotation(): Unit = {

    if (isRotating) {
      sliceRotationAngle += rotationInc
      val sign = if (isClockwise) -1 else 1
      rotateSlice(sign * sliceRotationAngle)
      if (sliceRotationAngle >= FastMath.HALF_PI) {
        sliceRotationAngle = 0
        restoreSlice()
      }
    }
  }

  private def getPosition(loc: Location): Vector3f = {
    new Vector3f(halfEdgeLen - loc._3 + 0.5f, halfEdgeLen - loc._1 + 0.5f, halfEdgeLen - loc._2 + 0.5f)
  }

  // There can only be one slice rotating at any given time
  private def createSlice(orientation: Orientation, level: Int): Unit = {
    assert(!isRotating, "Only 1 slice allowed at a time.")

    sliceOrientation = orientation
    val m = cube.getSlice(orientation, level)
    for (loc <- m.keys) {
      val mini = locToMinicubeNode(loc)
      this.detachChild(mini)
      sliceNode.attachChild(mini)
    }
  }

  def rotateSlice(angle: Float): Unit = {
    val q = new Quaternion()
    val axis = sliceOrientation match {
      case UP => Vector3f.UNIT_Y
      case LEFT => Vector3f.UNIT_Z
      case FRONT => Vector3f.UNIT_X
    }
    q.fromAngleAxis(angle, axis)
    sliceNode.setLocalRotation(q)
  }

  def restoreSlice(): Unit = {
    for (i <- sliceNode.getChildren.size() - 1 to 0 by -1) {
      val mini = sliceNode.getChild(i)
      sliceNode.detachChild(mini)
      this.attachChild(mini)
    }
    assert(sliceNode.getChildren.isEmpty)
  }

  // the new cube must be the same size as the old
  def updateCube(newCube: Cube): Unit = {
    assert(newCube.size == cube.size, "newCube has size " + newCube.size + " but should have " + cube.size)
    newCube.locationToMinicube.foreach({case (loc, minicube) =>
      val minicubeNode = locToMinicubeNode(loc)
      minicubeNode.updateColors(minicube)
    })
  }

  // should need this, but for scala port
  override def setKey(key: AssetKey[_]): Unit = {
    this.key = key
  }

  override def getKey: AssetKey[_] = key
}
