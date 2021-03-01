// Copyright by Barry G. Becker, 2021 Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.rubixcube.ui

import com.jme3.app.{LegacyApplication, SimpleApplication}
import com.jme3.system.{AppSettings, JmeCanvasContext}

import java.awt.Canvas


/**
  * Encapsulates some JMonkeyEngine magic that can be used to create a java Canvas that
  * will render the 3D cube so that we can put it in the CubeViewer JPanel.
  */
object CubeCanvasFactory {
  private val APP_CLASS: String = "com.barrybecker4.puzzle.rubixcube.ui.CubeSceneRenderer"
  private val AA_SAMPLES: Int = 5

  def createCanvas(): Canvas = {
    val clazz = Class.forName(APP_CLASS)
    val app: LegacyApplication = clazz.getDeclaredConstructor().newInstance().asInstanceOf[LegacyApplication]

    app.setPauseOnLostFocus(false)
    val settings = new AppSettings(true)
    settings.setSamples(AA_SAMPLES)
    app.setSettings(settings)
    app.createCanvas()
    app.startCanvas()

    val context: JmeCanvasContext = app.getContext.asInstanceOf[JmeCanvasContext]
    context.getCanvas
  }
}
