// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.twopails.ui

import com.barrybecker4.puzzle.common.PuzzleRenderer
import com.barrybecker4.puzzle.twopails.model.Pails
import java.awt.Color
import java.awt.Font
import java.awt.Graphics


/**
  * Singleton class that takes a PieceList and renders it for the TwoPailsViewer.
  * Having the renderer separate from the viewer helps to separate out the rendering logic
  * from other features of the TwoPailsViewer.
  *
  * @author Barry Becker
  */
object TwoPailsRenderer {
  private val CONTAINER_WIDTH = 0.2f
  private val SEPARATION = 0.11f
  private val TEXT_WIDTH = 70
  private val TEXT_OFFSET = 10
  private val TOP_MARGIN = 160
  private val MARGIN = 60
  private val CONTAINER_COLOR = new Color(5, 0, 80)
  private val LIQUID_COLOR = new Color(95, 145, 255)
  private val FONT = new Font("Sans Serif", Font.BOLD, 16)
}

class TwoPailsRenderer extends PuzzleRenderer[Pails] {

  /** This renders the current state of the Pails to the screen. */
  override def render(g: Graphics, pails: Pails, width: Int, height: Int): Unit = {
    val params = pails.getParams
    val biggest = params.getBiggest
    val size1 = params.pail1Size.toFloat / biggest
    val size2 = params.pail2Size.toFloat / biggest
    val usableWidth = width - TwoPailsRenderer.MARGIN
    val contWidth = (TwoPailsRenderer.CONTAINER_WIDTH * usableWidth).toInt
    val cont1Height = (size1 * (height - TwoPailsRenderer.TOP_MARGIN)).toInt
    val cont2Height = (size2 * (height - TwoPailsRenderer.TOP_MARGIN)).toInt
    val middle = TwoPailsRenderer.MARGIN + TwoPailsRenderer.TEXT_WIDTH +
      ((TwoPailsRenderer.CONTAINER_WIDTH + TwoPailsRenderer.SEPARATION) * usableWidth).toInt
    val container1Y = height - TwoPailsRenderer.MARGIN - cont1Height
    val container2Y = height - TwoPailsRenderer.MARGIN - cont2Height

    g.setColor(Color.BLACK)
    g.setFont(TwoPailsRenderer.FONT)
    g.drawString("First Pail", TwoPailsRenderer.MARGIN, TwoPailsRenderer.MARGIN + TwoPailsRenderer.TEXT_OFFSET)
    g.drawString("Max  = " + params.pail1Size, TwoPailsRenderer.MARGIN, container1Y + TwoPailsRenderer.TEXT_OFFSET)
    g.drawString("Fill = " + pails.fill1, TwoPailsRenderer.MARGIN, height - TwoPailsRenderer.MARGIN)
    g.drawString("Second Pail", middle, TwoPailsRenderer.MARGIN + TwoPailsRenderer.TEXT_OFFSET)
    g.drawString("Max = " + params.pail2Size, middle, container2Y + TwoPailsRenderer.TEXT_OFFSET)
    g.drawString("Fill = " + pails.fill2, middle, height - TwoPailsRenderer.MARGIN)

    // show outlines for two containers
    g.setColor(TwoPailsRenderer.CONTAINER_COLOR)
    g.drawRect(TwoPailsRenderer.MARGIN + TwoPailsRenderer.TEXT_WIDTH, container1Y, contWidth, cont1Height)
    g.drawRect(middle + TwoPailsRenderer.TEXT_WIDTH, container2Y, contWidth, cont2Height)
    g.setColor(TwoPailsRenderer.LIQUID_COLOR)
    // show fill for first container
    var fillHeight = (pails.fill1.toFloat / biggest * cont1Height).toInt - 2
    g.fillRect(TwoPailsRenderer.MARGIN + TwoPailsRenderer.TEXT_WIDTH + 1, height - TwoPailsRenderer.MARGIN - fillHeight, contWidth - 1, fillHeight)
    // show fill for second container
    fillHeight = (pails.fill2.toFloat / biggest * cont1Height).toInt - 2
    g.fillRect(middle + TwoPailsRenderer.TEXT_WIDTH + 1, height - TwoPailsRenderer.MARGIN - fillHeight, contWidth - 1, fillHeight)
  }
}


