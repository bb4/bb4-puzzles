package com.barrybecker4.puzzle.rubixcube.ui.jmonkey

import com.jme3.export.InputCapsule
import com.jme3.export.JmeExporter
import com.jme3.export.JmeImporter
import com.jme3.export.OutputCapsule
import com.jme3.input.{CameraInput, FlyByCamera, InputManager, MouseInput}
import com.jme3.input.controls._
import com.jme3.math.FastMath
import com.jme3.math.Vector3f
import com.jme3.renderer.Camera
import com.jme3.renderer.RenderManager
import com.jme3.renderer.ViewPort
import com.jme3.scene.Spatial
import com.jme3.scene.control.Control
import com.jme3.util.clone.Cloner
import com.jme3.util.clone.JmeCloneable

import java.io.IOException


/**
  * A camera that follows a spatial and can turn around it by dragging the mouse
  *
  * @author nehon
  */
object MyChaseCamera {
  /**
    * @deprecated use {@link CameraInput# CHASECAM_DOWN}
    */
  @deprecated val ChaseCamDown = "ChaseCamDown"
  /**
    * @deprecated use {@link CameraInput# CHASECAM_UP}
    */
  @deprecated val ChaseCamUp = "ChaseCamUp"
  /**
    * @deprecated use {@link CameraInput# CHASECAM_ZOOMIN}
    */
  @deprecated val ChaseCamZoomIn = "ChaseCamZoomIn"
  /**
    * @deprecated use {@link CameraInput# CHASECAM_ZOOMOUT}
    */
  @deprecated val ChaseCamZoomOut = "ChaseCamZoomOut"
  /**
    * @deprecated use {@link CameraInput# CHASECAM_MOVELEFT}
    */
  @deprecated val ChaseCamMoveLeft = "ChaseCamMoveLeft"
  /**
    * @deprecated use {@link CameraInput# CHASECAM_MOVERIGHT}
    */
  @deprecated val ChaseCamMoveRight = "ChaseCamMoveRight"
  /**
    * @deprecated use {@link CameraInput# CHASECAM_TOGGLEROTATE}
    */
  @deprecated val ChaseCamToggleRotate = "ChaseCamToggleRotate"
}

case class MyChaseCamera(cam: Camera) extends ActionListener with AnalogListener with Control with JmeCloneable {

  private var initialUpVec: Vector3f = cam.getUp.clone
  protected var target: Spatial = _
  protected var minVerticalRotation: Float = -FastMath.PI
  protected var maxVerticalRotation: Float = FastMath.PI
  protected var minDistance = 1.0f
  protected var maxDistance = 40.0f
  protected var distance: Float = 20
  protected var rotationSpeed = 1.0f
  protected var rotation: Float = 0
  protected var trailingRotationInertia = 0.05f
  protected var zoomSensitivity = 2f
  protected var rotationSensitivity: Float = 5f
  protected var chasingSensitivity = 5f
  protected var trailingSensitivity = 0.5f
  protected var vRotation: Float = FastMath.PI / 6
  protected var smoothMotion = false
  protected var trailingEnabled = true
  protected var rotationLerpFactor: Float = 0
  protected var trailingLerpFactor: Float = 0
  protected var rotating = false
  protected var vRotating = false
  protected var targetRotation: Float = rotation
  protected var inputManager: InputManager = _
  protected var targetVRotation: Float = vRotation
  protected var vRotationLerpFactor: Float = 0
  protected var targetDistance: Float = distance
  protected var distanceLerpFactor: Float = 0
  protected var zooming = false
  protected var trailing = false
  protected var chasing = false
  protected var veryCloseRotation = true
  protected var canRotate = false
  protected var offsetDistance = 0.002f
  protected var prevPos: Vector3f = _
  protected var targetMoves = false
  protected var enabled = true
  final protected val targetDir = new Vector3f
  protected var previousTargetRotation: Float = .0f
  final protected val pos = new Vector3f
  protected var targetLocation = new Vector3f(0, 0, 0)
  protected var dragToRotate = true
  protected var lookAtOffset = new Vector3f(0, 0, 0)
  protected var leftClickRotate = true
  protected var rightClickRotate = true
  protected var temp = new Vector3f(0, 0, 0)
  protected var invertYaxis = false
  protected var invertXaxis = false
  protected var zoomin = false
  protected var hideCursorOnRotate = true

  /**
    * Constructs the chase camera
    *
    * @param cam    the application camera
    * @param target the spatial to follow
    */
  def this(cam: Camera, target: Spatial) {
    this(cam)
    target.addControl(this)
  }

  /**
    * Constructs the chase camera, and registers inputs
    * if you use this constructor you have to attach the cam later to a spatial
    * doing spatial.addControl(chaseCamera);
    *
    * @param cam          the application camera
    * @param inputManager the inputManager of the application to register inputs
    */
  def this(cam: Camera, inputManager: InputManager) {
    this(cam)
    registerWithInput(inputManager)
  }

  /**
    * Constructs the chase camera, and registers inputs
    *
    * @param cam          the application camera
    * @param target       the spatial to follow
    * @param inputManager the inputManager of the application to register inputs
    */
  def this(cam: Camera, target: Spatial, inputManager: InputManager) {
    this(cam, target)
    registerWithInput(inputManager)
  }

  override def onAction(name: String, keyPressed: Boolean, tpf: Float): Unit = {
    if (dragToRotate) if (name == CameraInput.CHASECAM_TOGGLEROTATE && enabled) if (keyPressed) {
      canRotate = true
      if (hideCursorOnRotate) inputManager.setCursorVisible(false)
    }
    else {
      canRotate = false
      if (hideCursorOnRotate) inputManager.setCursorVisible(true)
    }
  }

  override def onAnalog(name: String, value: Float, tpf: Float): Unit = {
    if (name == CameraInput.CHASECAM_MOVELEFT) rotateCamera(-value)
    else if (name == CameraInput.CHASECAM_MOVERIGHT) rotateCamera(value)
    else if (name == CameraInput.CHASECAM_UP) vRotateCamera(value)
    else if (name == CameraInput.CHASECAM_DOWN) vRotateCamera(-value)
    else if (name == CameraInput.CHASECAM_ZOOMIN) {
      zoomCamera(-value)
      if (!zoomin) distanceLerpFactor = 0
      zoomin = true
    }
    else if (name == CameraInput.CHASECAM_ZOOMOUT) {
      zoomCamera(+value)
      if (zoomin) distanceLerpFactor = 0
      zoomin = false
    }
  }

  /**
    * Registers inputs with the input manager
    */
  final def registerWithInput(inputManager: InputManager): Unit = {
    val inputs = Array(CameraInput.CHASECAM_TOGGLEROTATE, CameraInput.CHASECAM_DOWN, CameraInput.CHASECAM_UP, CameraInput.CHASECAM_MOVELEFT, CameraInput.CHASECAM_MOVERIGHT, CameraInput.CHASECAM_ZOOMIN, CameraInput.CHASECAM_ZOOMOUT)
    this.inputManager = inputManager
    if (!invertYaxis) {
      inputManager.addMapping(CameraInput.CHASECAM_DOWN, new MouseAxisTrigger(MouseInput.AXIS_Y, true))
      inputManager.addMapping(CameraInput.CHASECAM_UP, new MouseAxisTrigger(MouseInput.AXIS_Y, false))
    }
    else {
      inputManager.addMapping(CameraInput.CHASECAM_DOWN, new MouseAxisTrigger(MouseInput.AXIS_Y, false))
      inputManager.addMapping(CameraInput.CHASECAM_UP, new MouseAxisTrigger(MouseInput.AXIS_Y, true))
    }
    inputManager.addMapping(CameraInput.CHASECAM_ZOOMIN, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false))
    inputManager.addMapping(CameraInput.CHASECAM_ZOOMOUT, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true))
    if (!invertXaxis) {
      inputManager.addMapping(CameraInput.CHASECAM_MOVELEFT, new MouseAxisTrigger(MouseInput.AXIS_X, true))
      inputManager.addMapping(CameraInput.CHASECAM_MOVERIGHT, new MouseAxisTrigger(MouseInput.AXIS_X, false))
    }
    else {
      inputManager.addMapping(CameraInput.CHASECAM_MOVELEFT, new MouseAxisTrigger(MouseInput.AXIS_X, false))
      inputManager.addMapping(CameraInput.CHASECAM_MOVERIGHT, new MouseAxisTrigger(MouseInput.AXIS_X, true))
    }
    inputManager.addMapping(CameraInput.CHASECAM_TOGGLEROTATE, new MouseButtonTrigger(MouseInput.BUTTON_LEFT))
    inputManager.addMapping(CameraInput.CHASECAM_TOGGLEROTATE, new MouseButtonTrigger(MouseInput.BUTTON_RIGHT))
    inputManager.addListener(this, inputs: _*)
  }

  /**
    * Cleans up the input mappings from the input manager.
    * Undoes the work of registerWithInput().
    *
    * @param inputManager InputManager from which to cleanup mappings.
    */
  def cleanupWithInput(mgr: InputManager): Unit = {
    mgr.deleteMapping(CameraInput.CHASECAM_TOGGLEROTATE)
    mgr.deleteMapping(CameraInput.CHASECAM_DOWN)
    mgr.deleteMapping(CameraInput.CHASECAM_UP)
    mgr.deleteMapping(CameraInput.CHASECAM_MOVELEFT)
    mgr.deleteMapping(CameraInput.CHASECAM_MOVERIGHT)
    mgr.deleteMapping(CameraInput.CHASECAM_ZOOMIN)
    mgr.deleteMapping(CameraInput.CHASECAM_ZOOMOUT)
    mgr.removeListener(this)
  }

  /**
    * Sets custom triggers for toggling the rotation of the cam
    * default are
    * new MouseButtonTrigger(MouseInput.BUTTON_LEFT)  left mouse button
    * new MouseButtonTrigger(MouseInput.BUTTON_RIGHT)  right mouse button
    */
  def setToggleRotationTrigger(triggers: Trigger*): Unit = {
    inputManager.deleteMapping(CameraInput.CHASECAM_TOGGLEROTATE)
    inputManager.addMapping(CameraInput.CHASECAM_TOGGLEROTATE, triggers: _*)
    inputManager.addListener(this, CameraInput.CHASECAM_TOGGLEROTATE)
  }

  /**
    * Sets custom triggers for zooming in the cam
    * default is
    * new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true)  mouse wheel up
    */
  def setZoomInTrigger(triggers: Trigger*): Unit = {
    inputManager.deleteMapping(CameraInput.CHASECAM_ZOOMIN)
    inputManager.addMapping(CameraInput.CHASECAM_ZOOMIN, triggers: _*)
    inputManager.addListener(this, CameraInput.CHASECAM_ZOOMIN)
  }

  /**
    * Sets custom triggers for zooming out the cam
    * default is
    * new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false)  mouse wheel down
    */
  def setZoomOutTrigger(triggers: Trigger*): Unit = {
    inputManager.deleteMapping(CameraInput.CHASECAM_ZOOMOUT)
    inputManager.addMapping(CameraInput.CHASECAM_ZOOMOUT, triggers: _*)
    inputManager.addListener(this, CameraInput.CHASECAM_ZOOMOUT)
  }

  protected def computePosition(): Unit = {
    val hDistance = distance * FastMath.sin((FastMath.PI / 2) - vRotation)
    pos.set(hDistance * FastMath.cos(rotation), distance * FastMath.sin(vRotation), hDistance * FastMath.sin(rotation))
    pos.addLocal(target.getWorldTranslation)
  }

  //rotate the camera around the target on the horizontal plane
  protected def rotateCamera(value: Float): Unit = {
    if (!canRotate || !enabled) return
    rotating = true
    targetRotation += value * rotationSpeed
  }

  //move the camera toward or away the target
  protected def zoomCamera(value: Float): Unit = {
    if (!enabled) return
    zooming = true
    targetDistance += value * zoomSensitivity
    if (targetDistance > maxDistance) targetDistance = maxDistance
    if (targetDistance < minDistance) targetDistance = minDistance
    if (veryCloseRotation) if ((targetVRotation < minVerticalRotation) && (targetDistance > (minDistance + 1.0f))) targetVRotation = minVerticalRotation
  }

  //rotate the camera around the target on the vertical plane
  protected def vRotateCamera(value: Float): Unit = {
    if (!canRotate || !enabled) return
    vRotating = true
    val lastGoodRot = targetVRotation
    targetVRotation += value * rotationSpeed
    if (targetVRotation > maxVerticalRotation) targetVRotation = lastGoodRot
    if (veryCloseRotation) if ((targetVRotation < minVerticalRotation) && (targetDistance > (minDistance + 1.0f))) targetVRotation = minVerticalRotation
    else if (targetVRotation < -FastMath.DEG_TO_RAD * 90) targetVRotation = lastGoodRot
    else if (targetVRotation < minVerticalRotation) targetVRotation = lastGoodRot
  }

  /**
    * Updates the camera, should only be called internally
    */
  protected def updateCamera(tpf: Float): Unit = {
    if (enabled) {
      targetLocation.set(target.getWorldTranslation).addLocal(lookAtOffset)
      if (smoothMotion) { //computation of target direction
        targetDir.set(targetLocation).subtractLocal(prevPos)
        val dist = targetDir.length
        //Low pass filtering on the target postition to avoid shaking when physics are enabled.
        if (offsetDistance < dist) { //target moves, start chasing.
          chasing = true
          //target moves, start trailing if it has to.
          if (trailingEnabled) trailing = true
          //target moves...
          targetMoves = true
        }
        else { //if target was moving, we compute a slight offset in rotation to avoid a rought stop of the cam
          //We do not if the player is rotationg the cam
          if (targetMoves && !canRotate) if (targetRotation - rotation > trailingRotationInertia) targetRotation = rotation + trailingRotationInertia
          else if (targetRotation - rotation < -trailingRotationInertia) targetRotation = rotation - trailingRotationInertia
          //Target stops
          targetMoves = false
        }
        //the user is rotating the cam by dragging the mouse
        if (canRotate) { //reseting the trailing lerp factor
          trailingLerpFactor = 0
          //stop trailing user has the control
          trailing = false
        }
        if (trailingEnabled && trailing) {
          if (targetMoves) { //computation if the inverted direction of the target
            val a = targetDir.negate.normalizeLocal
            //the x unit vector
            val b = Vector3f.UNIT_X
            //2d is good enough
            a.y = 0
            //computation of the rotation angle between the x axis and the trail
            if (targetDir.z > 0) targetRotation = FastMath.TWO_PI - FastMath.acos(a.dot(b))
            else targetRotation = FastMath.acos(a.dot(b))
            if (targetRotation - rotation > FastMath.PI || targetRotation - rotation < -FastMath.PI) targetRotation -= FastMath.TWO_PI
            //if there is an important change in the direction while trailing reset of the lerp factor to avoid jumpy movements
            if (targetRotation != previousTargetRotation && FastMath.abs(targetRotation - previousTargetRotation) > FastMath.PI / 8) trailingLerpFactor = 0
            previousTargetRotation = targetRotation
          }
          //computing lerp factor
          trailingLerpFactor = Math.min(trailingLerpFactor + tpf * tpf * trailingSensitivity, 1f)
          //computing rotation by linear interpolation
          rotation = FastMath.interpolateLinear(trailingLerpFactor, rotation, targetRotation)
          //if the rotation is near the target rotation we're good, that's over
          if (targetRotation + 0.01f >= rotation && targetRotation - 0.01f <= rotation) {
            trailing = false
            trailingLerpFactor = 0
          }
        }
        //linear interpolation of the distance while chasing
        if (chasing) {
          distance = temp.set(targetLocation).subtractLocal(cam.getLocation).length
          distanceLerpFactor = Math.min(distanceLerpFactor + (tpf * tpf * chasingSensitivity * 0.05f), 1)
          distance = FastMath.interpolateLinear(distanceLerpFactor, distance, targetDistance)
          if (targetDistance + 0.01f >= distance && targetDistance - 0.01f <= distance) {
            distanceLerpFactor = 0
            chasing = false
          }
        }
        //linear interpolation of the distance while zooming
        if (zooming) {
          distanceLerpFactor = Math.min(distanceLerpFactor + (tpf * tpf * zoomSensitivity), 1)
          distance = FastMath.interpolateLinear(distanceLerpFactor, distance, targetDistance)
          if (targetDistance + 0.1f >= distance && targetDistance - 0.1f <= distance) {
            zooming = false
            distanceLerpFactor = 0
          }
        }
        //linear interpolation of the rotation while rotating horizontally
        if (rotating) {
          rotationLerpFactor = Math.min(rotationLerpFactor + tpf * tpf * rotationSensitivity, 1f)
          rotation = FastMath.interpolateLinear(rotationLerpFactor, rotation, targetRotation)
          if (targetRotation + 0.01f >= rotation && targetRotation - 0.01f <= rotation) {
            rotating = false
            rotationLerpFactor = 0
          }
        }
        //linear interpolation of the rotation while rotating vertically
        if (vRotating) {
          vRotationLerpFactor = Math.min(vRotationLerpFactor + tpf * tpf * rotationSensitivity, 1f)
          vRotation = FastMath.interpolateLinear(vRotationLerpFactor, vRotation, targetVRotation)
          if (targetVRotation + 0.01f >= vRotation && targetVRotation - 0.01f <= vRotation) {
            vRotating = false
            vRotationLerpFactor = 0
          }
        }
        //computing the position
        computePosition()
        //setting the position at last
        cam.setLocation(pos.addLocal(lookAtOffset))
      }
      else { //easy no smooth motion
        vRotation = targetVRotation
        rotation = targetRotation
        distance = targetDistance
        computePosition()
        cam.setLocation(pos.addLocal(lookAtOffset))
      }
      //keeping track on the previous position of the target
      prevPos.set(targetLocation)
      //the cam looks at the target
      cam.lookAt(targetLocation, initialUpVec)
    }
  }

  /**
    * Return the enabled/disabled state of the camera
    *
    * @return true if the camera is enabled
    */
  def isEnabled: Boolean = enabled

  /**
    * Enable or disable the camera
    *
    * @param enabled true to enable
    */
  def setEnabled(enabled: Boolean): Unit = {
    this.enabled = enabled
    if (!enabled) canRotate = false // reset this flag in-case it was on before
  }

  /**
    * Returns the max zoom distance of the camera (default is 40)
    *
    * @return maxDistance
    */
  def getMaxDistance: Float = maxDistance

  /**
    * Sets the max zoom distance of the camera (default is 40)
    *
    * @param maxDistance
    */
  def setMaxDistance(maxDistance: Float): Unit = {
    this.maxDistance = maxDistance
    if (maxDistance < distance) zoomCamera(maxDistance - distance)
  }

  /**
    * Returns the min zoom distance of the camera (default is 1)
    *
    * @return minDistance
    */
  def getMinDistance: Float = minDistance

  /**
    * Sets the min zoom distance of the camera (default is 1)
    */
  def setMinDistance(minDistance: Float): Unit = {
    this.minDistance = minDistance
    if (minDistance > distance) zoomCamera(distance - minDistance)
  }

  /**
    * clone this camera for a spatial
    *
    * @param spatial
    * @return
    */
  override def cloneForSpatial(spatial: Spatial): Control = {
    val cc = new MyChaseCamera(cam, spatial, inputManager)
    cc.setMaxDistance(getMaxDistance)
    cc.setMinDistance(getMinDistance)
    cc
  }

  override def jmeClone: Any = {
    val cc = new MyChaseCamera(cam, inputManager)
    cc.target = target
    cc.setMaxDistance(getMaxDistance)
    cc.setMinDistance(getMinDistance)
    cc
  }

  override def cloneFields(cloner: Cloner, original: Any): Unit = {
    this.target = cloner.clone(target)
    computePosition()
    prevPos = new Vector3f(target.getWorldTranslation)
    cam.setLocation(pos)
  }

  /**
    * Sets the spacial for the camera control, should only be used internally
    *
    * @param spatial
    */
  override def setSpatial(spatial: Spatial): Unit = {
    target = spatial
    if (spatial == null) return
    computePosition()
    prevPos = new Vector3f(target.getWorldTranslation)
    cam.setLocation(pos)
  }

  /**
    * update the camera control, should only be used internally
    *
    * @param tpf
    */
  override def update(tpf: Float): Unit = {
    updateCamera(tpf)
  }

  /**
    * renders the camera control, should only be used internally
    *
    * @param rm
    * @param vp
    */
  override def render(rm: RenderManager, vp: ViewPort): Unit = {
    //nothing to render
  }

  /**
    * Write the camera
    *
    * @param ex the exporter
    * @throws IOException
    */
  @throws[IOException]
  override def write(ex: JmeExporter): Unit = {
    throw new UnsupportedOperationException("remove ChaseCamera before saving")
  }

  /**
    * Read the camera
    *
    * @param im
    * @throws IOException
    */
  @throws[IOException]
  override def read(im: JmeImporter): Unit = {
    val ic = im.getCapsule(this)
    maxDistance = ic.readFloat("maxDistance", 40)
    minDistance = ic.readFloat("minDistance", 1)
  }

  /**
    * @return The maximal vertical rotation angle in radian of the camera around the target
    */
  def getMaxVerticalRotation: Float = maxVerticalRotation

  /**
    * Sets the maximal vertical rotation angle in radian of the camera around the target. Default is Pi/2;
    *
    * @param maxVerticalRotation
    */
  def setMaxVerticalRotation(maxVerticalRotation: Float): Unit = {
    this.maxVerticalRotation = maxVerticalRotation
  }

  /**
    *
    * @return The minimal vertical rotation angle in radian of the camera around the target
    */
  def getMinVerticalRotation: Float = minVerticalRotation

  /**
    * Sets the minimal vertical rotation angle in radian of the camera around the target default is 0;
    *
    * @param minHeight
    */
  def setMinVerticalRotation(minHeight: Float): Unit = {
    this.minVerticalRotation = minHeight
  }

  /**
    * @return True is smooth motion is enabled for this chase camera
    */
  def isSmoothMotion: Boolean = smoothMotion

  /**
    * Enables smooth motion for this chase camera
    *
    * @param smoothMotion
    */
  def setSmoothMotion(smoothMotion: Boolean): Unit = {
    this.smoothMotion = smoothMotion
  }

  /**
    * returns the chasing sensitivity
    *
    * @return
    */
  def getChasingSensitivity: Float = chasingSensitivity

  /**
    *
    * Sets the chasing sensitivity, the lower the value the slower the camera will follow the target when it moves
    * default is 5
    * Only has an effect if smoothMotion is set to true and trailing is enabled
    *
    * @param chasingSensitivity
    */
  def setChasingSensitivity(chasingSensitivity: Float): Unit = {
    this.chasingSensitivity = chasingSensitivity
  }

  /**
    * Returns the rotation sensitivity
    *
    * @return
    */
  def getRotationSensitivity: Float = rotationSensitivity

  /**
    * Sets the rotation sensitivity, the lower the value the slower the camera will rotates around the target when dragging with the mouse
    * default is 5, values over 5 should have no effect.
    * If you want a significant slow down try values below 1.
    * Only has an effect if smoothMotion is set to true
    *
    * @param rotationSensitivity
    */
  def setRotationSensitivity(rotationSensitivity: Float): Unit = {
    this.rotationSensitivity = rotationSensitivity
  }

  /**
    * returns true if the trailing is enabled
    *
    * @return
    */
  def isTrailingEnabled: Boolean = trailingEnabled

  /**
    * Enable the camera trailing : The camera smoothly go in the targets trail when it moves.
    * Only has an effect if smoothMotion is set to true
    *
    * @param trailingEnabled
    */
  def setTrailingEnabled(trailingEnabled: Boolean): Unit = {
    this.trailingEnabled = trailingEnabled
  }

  /**
    *
    * returns the trailing rotation inertia
    *
    * @return
    */
  def getTrailingRotationInertia: Float = trailingRotationInertia

  /**
    * Sets the trailing rotation inertia : default is 0.1. This prevent the camera to roughtly stop when the target stops moving
    * before the camera reached the trail position.
    * Only has an effect if smoothMotion is set to true and trailing is enabled
    *
    * @param trailingRotationInertia
    */
  def setTrailingRotationInertia(trailingRotationInertia: Float): Unit = {
    this.trailingRotationInertia = trailingRotationInertia
  }

  /**
    * returns the trailing sensitivity
    *
    * @return
    */
  def getTrailingSensitivity: Float = trailingSensitivity

  /**
    * Only has an effect if smoothMotion is set to true and trailing is enabled
    * Sets the trailing sensitivity, the lower the value, the slower the camera will go in the target trail when it moves.
    * default is 0.5;
    *
    * @param trailingSensitivity
    */
  def setTrailingSensitivity(trailingSensitivity: Float): Unit = {
    this.trailingSensitivity = trailingSensitivity
  }

  /**
    * returns the zoom sensitivity
    *
    * @return
    */
  def getZoomSensitivity: Float = zoomSensitivity

  /**
    * Sets the zoom sensitivity, the lower the value, the slower the camera will zoom in and out.
    * default is 2.
    *
    * @param zoomSensitivity
    */
  def setZoomSensitivity(zoomSensitivity: Float): Unit = {
    this.zoomSensitivity = zoomSensitivity
  }

  /**
    * Returns the rotation speed when the mouse is moved.
    *
    * @return the rotation speed when the mouse is moved.
    */
  def getRotationSpeed: Float = rotationSpeed

  /**
    * Sets the rotate amount when user moves his mouse, the lower the value,
    * the slower the camera will rotate. default is 1.
    *
    * @param rotationSpeed Rotation speed on mouse movement, default is 1.
    */
  def setRotationSpeed(rotationSpeed: Float): Unit = {
    this.rotationSpeed = rotationSpeed
  }

  /**
    * Sets the default distance at start of application
    *
    * @param defaultDistance
    */
  def setDefaultDistance(defaultDistance: Float): Unit = {
    distance = defaultDistance
    targetDistance = distance
  }

  /**
    * sets the default horizontal rotation in radian of the camera at start of the application
    *
    * @param angleInRad
    */
  def setDefaultHorizontalRotation(angleInRad: Float): Unit = {
    rotation = angleInRad
    targetRotation = angleInRad
  }

  /**
    * sets the default vertical rotation in radian of the camera at start of the application
    *
    * @param angleInRad
    */
  def setDefaultVerticalRotation(angleInRad: Float): Unit = {
    vRotation = angleInRad
    targetVRotation = angleInRad
  }

  /**
    * @return If drag to rotate feature is enabled.
    * @see FlyByCamera#setDragToRotate(boolean)
    */
  def isDragToRotate: Boolean = dragToRotate

  /**
    * @param dragToRotate When true, the user must hold the mouse button
    *                     and drag over the screen to rotate the camera, and the cursor is
    *                     visible until dragged. Otherwise, the cursor is invisible at all times
    *                     and holding the mouse button is not needed to rotate the camera.
    *                     This feature is disabled by default.
    */
  def setDragToRotate(dragToRotate: Boolean): Unit = {
    this.dragToRotate = dragToRotate
    this.canRotate = !dragToRotate
    inputManager.setCursorVisible(dragToRotate)
  }

  /**
    * @param rotateOnlyWhenClose When this flag is set to false the chase
    *                            camera will always rotate around its spatial independently of their
    *                            distance to one another. If set to true, the chase camera will only
    *                            be allowed to rotated below the "horizon" when the distance is smaller
    *                            than minDistance + 1.0f (when fully zoomed-in).
    */
  def setDownRotateOnCloseViewOnly(rotateOnlyWhenClose: Boolean): Unit = {
    veryCloseRotation = rotateOnlyWhenClose
  }

  /**
    * @return True if rotation below the vertical plane of the spatial tied
    *         to the camera is allowed only when zoomed in at minDistance + 1.0f.
    *         False if vertical rotation is always allowed.
    */
  def getDownRotateOnCloseViewOnly: Boolean = veryCloseRotation

  /**
    * return the current distance from the camera to the target
    *
    * @return
    */
  def getDistanceToTarget: Float = distance

  /**
    * returns the current horizontal rotation around the target in radians
    *
    * @return
    */
  def getHorizontalRotation: Float = rotation

  /**
    * returns the current vertical rotation around the target in radians.
    *
    * @return
    */
  def getVerticalRotation: Float = vRotation

  /**
    * returns the offset from the target's position where the camera looks at
    *
    * @return
    */
  def getLookAtOffset: Vector3f = lookAtOffset

  /**
    * Sets the offset from the target's position where the camera looks at
    *
    * @param lookAtOffset
    */
  def setLookAtOffset(lookAtOffset: Vector3f): Unit = {
    this.lookAtOffset = lookAtOffset
  }

  /**
    * Sets the up vector of the camera used for the lookAt on the target
    *
    * @param up
    */
  def setUpVector(up: Vector3f): Unit = {
    initialUpVec = up
  }

  /**
    * Returns the up vector of the camera used for the lookAt on the target
    *
    * @return
    */
  def getUpVector: Vector3f = initialUpVec

  def isHideCursorOnRotate: Boolean = hideCursorOnRotate

  def setHideCursorOnRotate(hideCursorOnRotate: Boolean): Unit = {
    this.hideCursorOnRotate = hideCursorOnRotate
  }

  /**
    * invert the vertical axis movement of the mouse
    *
    * @param invertYaxis
    */
  def setInvertVerticalAxis(invertYaxis: Boolean): Unit = {
    this.invertYaxis = invertYaxis
    inputManager.deleteMapping(CameraInput.CHASECAM_DOWN)
    inputManager.deleteMapping(CameraInput.CHASECAM_UP)
    if (!invertYaxis) {
      inputManager.addMapping(CameraInput.CHASECAM_DOWN, new MouseAxisTrigger(MouseInput.AXIS_Y, true))
      inputManager.addMapping(CameraInput.CHASECAM_UP, new MouseAxisTrigger(MouseInput.AXIS_Y, false))
    }
    else {
      inputManager.addMapping(CameraInput.CHASECAM_DOWN, new MouseAxisTrigger(MouseInput.AXIS_Y, false))
      inputManager.addMapping(CameraInput.CHASECAM_UP, new MouseAxisTrigger(MouseInput.AXIS_Y, true))
    }
    inputManager.addListener(this, CameraInput.CHASECAM_DOWN, CameraInput.CHASECAM_UP)
  }

  /**
    * invert the Horizontal axis movement of the mouse
    *
    * @param invertXaxis
    */
  def setInvertHorizontalAxis(invertXaxis: Boolean): Unit = {
    this.invertXaxis = invertXaxis
    inputManager.deleteMapping(CameraInput.CHASECAM_MOVELEFT)
    inputManager.deleteMapping(CameraInput.CHASECAM_MOVERIGHT)
    if (!invertXaxis) {
      inputManager.addMapping(CameraInput.CHASECAM_MOVELEFT, new MouseAxisTrigger(MouseInput.AXIS_X, true))
      inputManager.addMapping(CameraInput.CHASECAM_MOVERIGHT, new MouseAxisTrigger(MouseInput.AXIS_X, false))
    }
    else {
      inputManager.addMapping(CameraInput.CHASECAM_MOVELEFT, new MouseAxisTrigger(MouseInput.AXIS_X, false))
      inputManager.addMapping(CameraInput.CHASECAM_MOVERIGHT, new MouseAxisTrigger(MouseInput.AXIS_X, true))
    }
    inputManager.addListener(this, CameraInput.CHASECAM_MOVELEFT, CameraInput.CHASECAM_MOVERIGHT)
  }
}