package com.barrybecker4.puzzle.rubixcube.ui.render.jme

import com.barrybecker4.puzzle.rubixcube.model.{Cube, FRONT, LEFT, Location, Orientation, UP}
import com.jme3.scene.instancing.InstancedNode
import com.jme3.asset.{AssetKey, AssetManager}
import com.jme3.math.{FastMath, Quaternion, Vector3f}
import com.jme3.scene.Node



/* Renders the whole cube. The cube is composed by many minicubeNodes. */
class RubixCubeNode(cube: Cube, assetManager: AssetManager)
  extends InstancedNode("cubeNodeParent") {

  private val halfEdgeLen: Float = cube.size / 2.0f
  private val sliceNode: Node = new Node("slice")
  private var sliceOrientation: Orientation = _

  private var cubeStateAfterRotation: Option[Cube] = None
  private var sliceRotationAngle: Float = 0
  private var isUndo: Boolean = false

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
  def startRotatingSlice(orientation: Orientation, level: Int, undo: Boolean, newCubeState: Cube): Unit = {
    assert(cubeStateAfterRotation.isEmpty, "Only one active slice allowed at a time.")
    cubeStateAfterRotation = Some(newCubeState)
    isUndo = undo
    createSlice(orientation, level)
  }

  def isRotating: Boolean = cubeStateAfterRotation.nonEmpty

  def incrementSliceRotation(): Unit = {
    if (cubeStateAfterRotation.nonEmpty) {
      sliceRotationAngle += 0.001f
      val sign = if (isUndo) -1 else 1
      rotateSlice(sign * sliceRotationAngle)
      if (sliceRotationAngle >= FastMath.HALF_PI) {
        sliceRotationAngle = 0
        restoreSlice()
        updateCube(cubeStateAfterRotation.get)
        cubeStateAfterRotation = None
      }
    }
  }

  private def getPosition(loc: Location): Vector3f = {
    new Vector3f(halfEdgeLen - loc._3 + 0.5f, halfEdgeLen - loc._1 + 0.5f, halfEdgeLen - loc._2 + 0.5f)
  }

  // There can only be one slice rotating at any given time
  private def createSlice(orientation: Orientation, level: Int): Unit = {
    assert(sliceNode.getChildren.isEmpty, "Only 1 slice allowed at a time.")
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
      case LEFT => Vector3f.UNIT_X
      case FRONT => Vector3f.UNIT_Z
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
    //sliceNode.detachAllChildren()
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
