// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.rubixcube.ui

import com.barrybecker4.puzzle.common.ui.PuzzleRenderer
import com.barrybecker4.puzzle.rubixcube.model.Cube

import java.awt.{Color, Font, Graphics}


/**
  * Singleton class that takes a PieceList and renders it for the SliderViewer.
  * Having the renderer separate from the viewer helps to separate out the rendering logic
  * from other features of the SliderViewer.
  * @author Barry Becker
  */
object CubeRenderer {
  private val INC = 60
  private val SEPARATION = INC / 10
  private val TILE_EDGE = INC - SEPARATION
  private val LEFT_MARGIN = 40
  private val TOP_MARGIN = 55
  private val TILE_COLOR = new Color(235, 145, 255)
  private val FONT = new Font("Sans Serif", Font.PLAIN, INC / 2)
}

class CubeRenderer extends PuzzleRenderer[Cube] {

  /** This renders the current state of the Cube to the screen. */
  def render(g: Graphics, board: Cube, width: Int, height: Int): Unit = {
    val size = board.size
    val rightEdgePos = CubeRenderer.LEFT_MARGIN + CubeRenderer.INC * size
    val bottomEdgePos = CubeRenderer.TOP_MARGIN + CubeRenderer.INC * size
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
    val offset = CubeRenderer.SEPARATION / 2
    g.setColor(Color.darkGray)
    i = 0
    while (i <= size) {    //   -----
      ypos = CubeRenderer.TOP_MARGIN + i * CubeRenderer.INC - offset
      g.drawLine(CubeRenderer.LEFT_MARGIN - offset, ypos, rightEdgePos - offset, ypos)
      i += size
    }
    i = 0
    while (i <= size) {    //   ||||
      xpos = CubeRenderer.LEFT_MARGIN + i * CubeRenderer.INC - offset
      g.drawLine(xpos, CubeRenderer.TOP_MARGIN - offset, xpos, bottomEdgePos - offset)
      i += size
    }
  }

  private def drawTile(g: Graphics, board: Cube, row: Int, col: Int): Unit = {
    val xpos = CubeRenderer.LEFT_MARGIN + col * CubeRenderer.INC
    val ypos = CubeRenderer.TOP_MARGIN + row * CubeRenderer.INC
    val value = 123

    g.setColor(CubeRenderer.TILE_COLOR)
    g.fillRect(xpos, ypos, CubeRenderer.TILE_EDGE, CubeRenderer.TILE_EDGE)
    g.setColor(Color.BLACK)
    g.setFont(CubeRenderer.FONT)
    g.drawString(Integer.toString(value), xpos + CubeRenderer.INC / 4, ypos + 2 * CubeRenderer.INC / 3)
    g.drawRect(xpos, ypos, CubeRenderer.TILE_EDGE, CubeRenderer.TILE_EDGE)

  }
}


