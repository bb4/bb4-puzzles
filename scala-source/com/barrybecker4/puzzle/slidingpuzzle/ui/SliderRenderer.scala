// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.ui

import java.awt.{Color, Font, Graphics}

import com.barrybecker4.puzzle.common.ui.PuzzleRenderer
import com.barrybecker4.puzzle.slidingpuzzle.model.SliderBoard


/**
  * Singleton class that takes a PieceList and renders it for the SliderViewer.
  * Having the renderer separate from the viewer helps to separate out the rendering logic
  * from other features of the SliderViewer.
  * @author Barry Becker
  */
object SliderRenderer {
  private val INC = 60
  private val SEPARATION = INC / 10
  private val TILE_EDGE = INC - SEPARATION
  private val LEFT_MARGIN = 40
  private val TOP_MARGIN = 55
  private val TILE_COLOR = new Color(235, 145, 255)
  private val FONT = new Font("Sans Serif", Font.PLAIN, INC / 2)
}

class SliderRenderer extends PuzzleRenderer[SliderBoard] {

  /** This renders the current state of the Slider to the screen. */
  def render(g: Graphics, board: SliderBoard, width: Int, height: Int): Unit = {
    val size = board.size
    val rightEdgePos = SliderRenderer.LEFT_MARGIN + SliderRenderer.INC * size
    val bottomEdgePos = SliderRenderer.TOP_MARGIN + SliderRenderer.INC * size
    drawBorder(g, size, rightEdgePos, bottomEdgePos)
    // now draw the pieces that we have so far
    for (row <- 0 until size; col <- 0 until size)
       drawTile(g, board, row, col)
  }

  /** draw the hatches which delineate the cells */
  private def drawBorder(g: Graphics, size: Int, rightEdgePos: Int, bottomEdgePos: Int): Unit = {
    var i = 0
    var ypos = 0
    var xpos = 0
    val offset = SliderRenderer.SEPARATION / 2
    g.setColor(Color.darkGray)
    i = 0
    while (i <= size) {    //   -----
      ypos = SliderRenderer.TOP_MARGIN + i * SliderRenderer.INC - offset
      g.drawLine(SliderRenderer.LEFT_MARGIN - offset, ypos, rightEdgePos - offset, ypos)
      i += size
    }
    i = 0
    while (i <= size) {    //   ||||
      xpos = SliderRenderer.LEFT_MARGIN + i * SliderRenderer.INC - offset
      g.drawLine(xpos, SliderRenderer.TOP_MARGIN - offset, xpos, bottomEdgePos - offset)
      i += size
    }
  }

  private def drawTile(g: Graphics, board: SliderBoard, row: Int, col: Int): Unit = {
    val xpos = SliderRenderer.LEFT_MARGIN + col * SliderRenderer.INC
    val ypos = SliderRenderer.TOP_MARGIN + row * SliderRenderer.INC
    val value = board.getPosition(row.toByte, col.toByte)
    val empty = value == 0
    if (!empty) {
      g.setColor(SliderRenderer.TILE_COLOR)
      g.fillRect(xpos, ypos, SliderRenderer.TILE_EDGE, SliderRenderer.TILE_EDGE)
      g.setColor(Color.BLACK)
      g.setFont(SliderRenderer.FONT)
      g.drawString(Integer.toString(value), xpos + SliderRenderer.INC / 4, ypos + 2 * SliderRenderer.INC / 3)
      g.drawRect(xpos, ypos, SliderRenderer.TILE_EDGE, SliderRenderer.TILE_EDGE)
    }
  }
}


