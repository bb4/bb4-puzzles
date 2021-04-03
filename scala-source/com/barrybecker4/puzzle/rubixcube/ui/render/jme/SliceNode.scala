package com.barrybecker4.puzzle.rubixcube.ui.render.jme

import com.barrybecker4.puzzle.rubixcube.model._
import com.jme3.math.{FastMath, Quaternion, Vector3f}
import com.jme3.scene.instancing.InstancedNode
import com.barrybecker4.puzzle.rubixcube.ui.render.jme.SliceNode.DEFAULT_ROTATION_INCREMENT
import com.jme3.asset.AssetKey



object SliceNode {
  private val DEFAULT_ROTATION_INCREMENT: Float = 0.005f
}


/* Show slice when it is rotating. */
class SliceNode(miniCubes: Seq[MinicubeNode],
                sliceOrientation: Orientation,
                isClockwise: Boolean = false,
                onFinished: Runnable) extends InstancedNode("slice") {

  private var rotationInc: Float = DEFAULT_ROTATION_INCREMENT
  private var sliceRotationAngle: Float = 0f
  miniCubes.foreach( this.attachChild)


  def incrementSliceRotation(): Unit = {
      sliceRotationAngle += rotationInc
      val sign = if (isClockwise) -1 else 1
      rotateSlice(sign * sliceRotationAngle)
      if (sliceRotationAngle >= FastMath.HALF_PI) {
        sliceRotationAngle = 0
        onFinished.run()
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
    this.setLocalRotation(q)
  }


  // should need this, but for scala port
  override def setKey(key: AssetKey[_]): Unit = {
    this.key = key
  }

  override def getKey: AssetKey[_] = key
}
