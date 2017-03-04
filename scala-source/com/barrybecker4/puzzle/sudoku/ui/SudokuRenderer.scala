// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.ui

import java.awt._

import com.barrybecker4.common.geometry.{ByteLocation, Location}
import com.barrybecker4.puzzle.sudoku.model.ValueConverter
import com.barrybecker4.puzzle.sudoku.model.board.{Board, Cell}
import com.barrybecker4.ui.util.GUIUtil


/**
  * Renders the the sudoku puzzle onscreen.
  *
  * @author Barry Becker
  */
object SudokuRenderer {
  private val MARGIN = 50
  private val CELL_ORIG_TEXT_COLOR = Color.BLACK
  private val CELL_TEXT_COLOR = Color.BLUE
  private val CELL_ORIG_BACKGROUND_COLOR = new Color(215, 225, 238, 200)
  private val CELL_BACKGROUND_COLOR = new Color(245, 245, 255, 100)
  private val CELL_FOCUS_COLOR = new Color(255, 250, 200)
  private val USER_VALUE_COLOR = new Color(155, 5, 40)
  private val USER_VALUE_CORRECT_COLOR = new Color(0, 200, 0)
  private val USER_VALUE_WRONG_COLOR = new Color(255, 10, 0)
  private val BIG_X_COLOR = new Color(255, 0, 0, 50)
  private val BIG_X_STROKE = new BasicStroke(10, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL)
  private val GRID_COLOR = new Color(10, 0, 100)
  private val TEXT_COLOR = new Color(0, 10, 10)
  private val BACKGROUND_COLOR = new Color(220, 220, 240)
  private val CANDIDATE_TEXT_COLOR = new Color(160, 160, 210)
  private val CELL_STROKE = new BasicStroke(0.5f)
  private val BIG_CELL_STROKE = new BasicStroke(3.0f)
}

class SudokuRenderer(var board: Board) extends CellLocator {
  private var showCandidates: Boolean = false
  private var pieceSize: Int = 0


  def setShowCandidates (show: Boolean) {
    showCandidates = show
  }

  /** This renders the current state of the Slider to the screen. */
  def render(g: Graphics, userEnteredValues: Map[Location, UserValue],
             currentFocusLocation: Location, width: Int, height: Int) {
    val g2: Graphics2D = g.asInstanceOf[Graphics2D]
    val minEdge: Int = Math.min (width, height) - 20 - SudokuRenderer.MARGIN
    pieceSize = minEdge / board.edgeLength
    // erase what's there and redraw.
    g.setColor (SudokuRenderer.BACKGROUND_COLOR)
    g.fillRect (0, 0, width, height)
    g.setColor (SudokuRenderer.TEXT_COLOR)
    g.drawString ("Number of tries: " + board.numIterations, SudokuRenderer.MARGIN, SudokuRenderer.MARGIN - 24)
    val len: Int = board.edgeLength
    var xpos: Int = 0
    var ypos: Int = 0
    if (currentFocusLocation != null) {
      drawCurrentFocus (g, currentFocusLocation)
    }

    for (i <- 0 until len; j <- 0 until len) {
      val loc = (i + 1, j + 1)
      val c: Cell = board.getCell(loc)
      val cands = board.getValues(loc)
      xpos = SudokuRenderer.MARGIN + j * pieceSize
      ypos = SudokuRenderer.MARGIN + i * pieceSize
      drawCell(g2, c, cands, xpos, ypos, userEnteredValues.get(new ByteLocation(i, j)))
    }
    drawCellBoundaryGrid (g, len)
  }

  def getCellCoordinates(point: Point): Location = {
    val row: Int = ((point.getY - SudokuRenderer.MARGIN) / pieceSize).toInt
    val col: Int = ((point.getX - SudokuRenderer.MARGIN) / pieceSize).toInt
    new ByteLocation (row, col)
  }

  /**
    * Draw a cell at the specified location.
    */
  private def drawCell(g2: Graphics2D, cell: Cell, cands: Seq[Int], xpos: Int, ypos: Int, userValue: Option[UserValue]) {
    val s: Int = getScale (pieceSize)
    val jitteredXpos: Int = xpos + (Math.random * 3 - 1).toInt
    val jitteredYpos: Int = ypos + (Math.random * 3 - 1).toInt
    val font: Font = new Font (GUIUtil.DEFAULT_FONT_FAMILY, Font.PLAIN, pieceSize >> 1)
    g2.setFont (font)
    val bg = if (cell.originalValue > 0) SudokuRenderer.CELL_ORIG_BACKGROUND_COLOR else SudokuRenderer.CELL_BACKGROUND_COLOR
    g2.setColor(bg)
    g2.fillRect (xpos + 1, ypos + 1, pieceSize - 3, pieceSize - 2)
    if (userValue.isDefined) {
      drawUserValue (g2, userValue.get, s, xpos, ypos)
    }
    else if (cell.proposedValue > 0) {
      g2.setColor (if (cell.originalValue > 0) SudokuRenderer.CELL_ORIG_TEXT_COLOR
      else SudokuRenderer.CELL_TEXT_COLOR)
      g2.drawString (ValueConverter.getSymbol (cell.proposedValue),
        jitteredXpos + (0.8 * s).toInt, (jitteredYpos + s * 1.7).toInt)
    }
    // draw the first 9 numbers in the candidate list, if there are any.
    if (showCandidates) {
      drawCandidates(g2, cands, xpos, ypos)
    }
  }

  private def drawUserValue (g2: Graphics2D, userValue: UserValue, s: Int, xpos: Int, ypos: Int) {
    if (userValue.isValidated) {
      if (userValue.isValid) {
        g2.setColor(SudokuRenderer.USER_VALUE_CORRECT_COLOR)
      }
      else {
        drawBigX (g2, s, xpos, ypos)
        g2.setColor(SudokuRenderer.USER_VALUE_WRONG_COLOR)
      }
    }
    else {
      g2.setColor(SudokuRenderer.USER_VALUE_COLOR)
    }
    g2.drawString(ValueConverter.getSymbol (userValue.getValue), xpos + (0.8 * s).toInt, (ypos + s * 1.7).toInt)
  }

  private def drawBigX(g2: Graphics2D, s: Int, xpos: Int, ypos: Int) {
    g2.setColor (SudokuRenderer.BIG_X_COLOR)
    g2.setStroke (SudokuRenderer.BIG_X_STROKE)
    val leftX: Int = xpos + (0.15 * s).toInt
    val rightX: Int = xpos + (2.3 * s).toInt
    val bottomY: Int = ypos + (2.45 * s).toInt
    val topY: Int = ypos + (0.1 * s).toInt
    g2.drawLine (leftX, topY, rightX, bottomY)
    g2.drawLine (rightX, topY, leftX, bottomY)
  }

  private def drawCandidates(g: Graphics, candidates: Seq[Int], xpos: Int, ypos: Int) {
    if (candidates.nonEmpty) {
      g.setColor (SudokuRenderer.CANDIDATE_TEXT_COLOR)
      val candidateFont: Font = new Font ("Sans Serif", Font.PLAIN, (pieceSize >> 2) - 2)
      g.setFont (candidateFont)
      drawHints (g, candidates, xpos, ypos, getScale(pieceSize) )
    }
  }

  private def drawCurrentFocus(g: Graphics, focusLocation: Location) {
    val xpos: Int = SudokuRenderer.MARGIN + focusLocation.getCol * pieceSize
    val ypos: Int = SudokuRenderer.MARGIN + focusLocation.getRow * pieceSize
    g.setColor (SudokuRenderer.CELL_FOCUS_COLOR)
    g.fillRect (xpos, ypos, pieceSize, pieceSize)
  }

  private def getScale(pieceSize: Int): Int = (pieceSize * 0.4).toInt

  private def drawHints(g: Graphics, candidates: Seq[Int], x: Int, y: Int, scale: Int) {
    val xOffsetLow: Int = (0.3 * scale).toInt
    val xOffsetMed: Int = (1.1 * scale).toInt
    val xOffsetHi: Int = (1.9 * scale).toInt
    val yOffsetLow: Int = (0.7 * scale).toInt
    val yOffsetMed: Int = (1.5 * scale).toInt
    val yOffsetHi: Int = (2.3 * scale).toInt
    val offsets: Array[Array[Int]] = Array (
      Array (xOffsetLow, yOffsetLow),
      Array (xOffsetMed, yOffsetLow),
      Array (xOffsetHi, yOffsetLow),
      Array (xOffsetLow, yOffsetMed),
      Array (xOffsetMed, yOffsetMed),
      Array (xOffsetHi, yOffsetMed),
      Array (xOffsetLow, yOffsetHi),
      Array (xOffsetMed, yOffsetHi),
      Array (xOffsetHi, yOffsetHi)
    )
    var ct: Int = 0
    for (cand <- candidates if ct < 9) {
      g.drawString(ValueConverter.getSymbol(cand), x + offsets(ct)(0), y + offsets(ct)(1) )
      ct += 1
    }
  }

  /** draw the borders around each piece. */
  private def drawCellBoundaryGrid (g: Graphics, edgeLen: Int) {
    val g2: Graphics2D = g.asInstanceOf[Graphics2D]
    var xpos: Int = 0
    var ypos: Int = 0
    val rightEdgePos: Int = SudokuRenderer.MARGIN + pieceSize * edgeLen
    val bottomEdgePos: Int = SudokuRenderer.MARGIN + pieceSize * edgeLen
    val bigCellLen: Int = Math.sqrt (edgeLen).toInt
    // draw the hatches which delineate the cells
    g.setColor (SudokuRenderer.GRID_COLOR)

    for (i <- 0 to edgeLen) { //   -----
      ypos = SudokuRenderer.MARGIN + i * pieceSize
      g2.setStroke(if (i % bigCellLen == 0) SudokuRenderer.BIG_CELL_STROKE else SudokuRenderer.CELL_STROKE)
      g2.drawLine (SudokuRenderer.MARGIN, ypos, rightEdgePos, ypos)
    }

    for (i <- 0 to edgeLen) { //   ||||
      xpos = SudokuRenderer.MARGIN + i * pieceSize
      g2.setStroke (if (i % bigCellLen == 0) SudokuRenderer.BIG_CELL_STROKE else SudokuRenderer.CELL_STROKE)
      g2.drawLine (xpos, SudokuRenderer.MARGIN, xpos, bottomEdgePos)
    }
  }
}