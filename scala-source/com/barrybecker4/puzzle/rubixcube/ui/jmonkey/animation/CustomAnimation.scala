package com.barrybecker4.puzzle.rubixcube.ui.jmonkey.animation

import com.jme3.anim.{Armature, Joint, SkinningControl}
import com.jme3.app.SimpleApplication
import com.jme3.light.{AmbientLight, DirectionalLight}
import com.jme3.math.{Quaternion, Transform, Vector3f}
import com.jme3.scene.{Geometry, Node, VertexBuffer}
import com.jme3.scene.VertexBuffer.{Format, Type, Usage}
import com.jme3.scene.shape.Box

import java.nio.{ByteBuffer, FloatBuffer}


object CustomAnimation extends App {
    val app = new CustomAnimation
    app.start()
}

class CustomAnimation extends SimpleApplication {
  private var bone: Joint = _
  private var armature: Armature = _
  private val rotation: Quaternion = new Quaternion

  override def simpleInitApp(): Unit = {
    addLights(rootNode)
    val model: Node = createModel()
    rootNode.attachChild(model)
  }

  private def createModel(): Node = {
    val geom = createBoxGeom()

    bone = new Joint("root")
    bone.setLocalTransform(new Transform(Vector3f.ZERO, Quaternion.IDENTITY, Vector3f.UNIT_XYZ))
    armature = new Armature(Array[Joint](bone))

    val model = new Node("model")
    model.attachChild(geom)
    // Create skeleton control
    val skinningControl = new SkinningControl(armature)
    model.addControl(skinningControl)
    model
  }

  private def createBoxGeom(): Geometry = {
    val box = new Box(1, 1, 1)
    val weightsHW = new VertexBuffer(Type.HWBoneWeight)
    val indicesHW = new VertexBuffer(Type.HWBoneIndex)
    indicesHW.setUsage(Usage.CpuOnly)
    weightsHW.setUsage(Usage.CpuOnly)
    box.setBuffer(weightsHW)
    box.setBuffer(indicesHW)

    // Setup bone weight buffer
    val weights = FloatBuffer.allocate(box.getVertexCount * 4)
    val weightsBuf = new VertexBuffer(Type.BoneWeight)
    weightsBuf.setupData(Usage.CpuOnly, 4, Format.Float, weights)
    box.setBuffer(weightsBuf)

    // Setup bone index buffer
    val indices: ByteBuffer = ByteBuffer.allocate(box.getVertexCount * 4)
    val indicesBuf = new VertexBuffer(Type.BoneIndex)
    indicesBuf.setupData(Usage.CpuOnly, 4, Format.UnsignedByte, indices)
    box.setBuffer(indicesBuf)
    box.generateBindPose()

    // Assign all vertices to bone 0 with weight 1
    var i: Int = 0
    while (i < box.getVertexCount * 4) {
      // assign vertex to bone index 0
      val ia = indices.array()
      ia(i + 0) = 0
      ia(i + 1) = 0
      ia(i + 2) = 0
      ia(i + 3) = 0

      // set weight to 1 only for first entry
      val wa = weights.array()
      wa(i + 0) = 1
      wa(i + 1) = 0
      wa(i + 2) = 0
      wa(i + 3) = 0

      i += 4
    }
    // Maximum number of weights per bone is 1
    box.setMaxNumWeights(1)
    // Create model
    val geom = new Geometry("box", box)
    geom.setMaterial(assetManager.loadMaterial("com/barrybecker4/puzzle/rubixcube/ui/assets/textures/brickWall/BrickWall.j3m"))
    //geom.setMaterial(assetManager.loadMaterial("Common/Materials/RedColor.j3m"))
    geom
  }

  private def addLights(rootNode: Node): Unit = {
    val al = new AmbientLight
    rootNode.addLight(al)
    val dl = new DirectionalLight
    dl.setDirection(Vector3f.UNIT_XYZ.negate)
    rootNode.addLight(dl)
  }

  override def simpleUpdate(tpf: Float): Unit = { // Rotate around X axis
    val rotate = new Quaternion
    rotate.fromAngleAxis(tpf, Vector3f.UNIT_X)
    // Combine rotation with previous
    rotation.multLocal(rotate)
    // Set new rotation into bone
    bone.setLocalTransform(new Transform(Vector3f.ZERO, rotation, Vector3f.UNIT_XYZ))
    // After changing skeleton transforms, must update world data
    armature.update()
  }
}