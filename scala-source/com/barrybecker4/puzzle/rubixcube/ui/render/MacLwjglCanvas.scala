// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.rubixcube.ui.render

import com.jme3.system.AppSettings
import com.jme3.system.lwjgl.{LwjglCanvas, LwjglWindow}
import org.lwjgl.opengl.awt.GLData

import java.lang.reflect.Field
import javax.swing.SwingUtilities


/**
  * JME 3.10's [[LwjglCanvas.createContext]] forces `forwardCompatible = false` for Windows.
  * On macOS that selects a compatibility profile, and NSOpenGL rejects GL 3.x compatibility
  * (`macOS NSOpenGL does not support compatibility profiles newer than OpenGL 2.1`).
  *
  * Same setup as stock 3.10, but CORE + forward-compatible on Mac.
  */
class MacLwjglCanvas extends LwjglCanvas {

  /**
    * 3.10 schedules [[run]] via `invokeLater` (EDT). Context may have been created on another
    * thread — bind it before any GL call or LWJGL aborts.
    */
  override protected def runLoop(): Unit = {
    val canvas = MacLwjglCanvas.field(classOf[LwjglCanvas], "canvas").get(this)
    MacLwjglCanvas.invoke(canvas, "lock", Array(), Array())
    try {
      MacLwjglCanvas.invoke(canvas, "makeCurrent", Array(), Array())
      super.runLoop()
    } finally {
      MacLwjglCanvas.invoke(canvas, "unlock", Array(), Array())
    }
  }

  override protected def createContext(settings: AppSettings): Unit = {
    val canvas = MacLwjglCanvas.field(classOf[LwjglCanvas], "canvas").get(this)
    val glData = MacLwjglCanvas.field(classOf[LwjglCanvas], "glData").get(this).asInstanceOf[GLData]

    MacLwjglCanvas.invoke(canvas, "createPlatform", Array(classOf[AppSettings]), Array(settings))

    glData.majorVersion = 3
    glData.minorVersion = 2
    glData.profile = GLData.Profile.CORE
    glData.forwardCompatible = true

    if (settings.getBitsPerPixel == 24) {
      glData.redSize = 8
      glData.greenSize = 8
      glData.blueSize = 8
    } else if (settings.getBitsPerPixel == 16) {
      glData.redSize = 5
      glData.greenSize = 6
      glData.blueSize = 5
    }

    glData.swapInterval = Integer.valueOf(if (settings.isVSync) 1 else 0)
    glData.alphaSize = settings.getAlphaBits
    glData.sRGB = settings.isGammaCorrection
    glData.depthSize = settings.getDepthBits
    glData.stencilSize = settings.getStencilBits
    glData.samples = settings.getSamples
    glData.stereo = settings.useStereo3D
    glData.debug = settings.isGraphicsDebug
    glData.api = GLData.API.GL

    MacLwjglCanvas.field(classOf[LwjglWindow], "allowSwapBuffers")
      .setBoolean(this, settings.isSwapBuffers)

    MacLwjglCanvas.invoke(canvas, "createContext", Array(), Array())
    try {
      MacLwjglCanvas.invoke(canvas, "lock", Array(), Array())
      MacLwjglCanvas.invoke(canvas, "makeCurrent", Array(), Array())
    } finally {
      MacLwjglCanvas.invoke(canvas, "unlock", Array(), Array())
    }

    SwingUtilities.invokeLater(() =>
      MacLwjglCanvas.invoke(canvas, "validate", Array(), Array()))
  }
}


object MacLwjglCanvas {
  private def field(cls: Class[?], name: String): Field = {
    val f = cls.getDeclaredField(name)
    f.setAccessible(true)
    f
  }

  private def invoke(target: AnyRef, name: String, types: Array[Class[?]], args: Array[AnyRef]): AnyRef = {
    val m = target.getClass.getMethod(name, types*)
    m.setAccessible(true)
    m.invoke(target, args*)
  }
}
