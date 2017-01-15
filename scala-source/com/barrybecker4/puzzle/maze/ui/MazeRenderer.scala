// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.maze.ui

import com.barrybecker4.common.geometry.Location
import com.barrybecker4.puzzle.maze.model.MazeModel
import com.barrybecker4.ui.util.GUIUtil
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D

/**
  * Responsible for drawing the Maze based on the MazeModel.
  *
  * @author Barry Becker
  */
object MazeRenderer {
  // rendering attributes
  private val WALL_COLOR = new Color(80, 0, 150)
  private val PATH_COLOR = new Color(255, 230, 10)
  private val TEXT_COLOR = new Color(250, 0, 100)
  private val BG_COLOR = new Color(205, 220, 250)
  private val VISITED_COLOR = new Color(255, 255, 255)
  private val WALL_LINE_WIDTH = 3
  private val PATH_LINE_WIDTH = 14
  private val DEFAULT_CELL_SIZE = 40

  private def drawChar(c: String, pos: Location, cellSize: Int, g2: Graphics2D) {
    if (pos != null) g2.drawString(c, ((pos.getX + 0.32) * cellSize).toInt, ((pos.getY + 0.76) * cellSize).toInt)
  }
}

class MazeRenderer() {
  setCellSize(MazeRenderer.DEFAULT_CELL_SIZE)
  private var cellSize = 0
  private var halfCellSize = 0
  private var wallStroke: BasicStroke = _
  private var pathStroke: BasicStroke = _
  private var textFont: Font = _

  def setCellSize(size: Int) {
    cellSize = size
    halfCellSize = (cellSize / 2.0).toInt
    val lineWidth = (MazeRenderer.WALL_LINE_WIDTH * cellSize / 30.0).toInt
    val pathWidth = (MazeRenderer.PATH_LINE_WIDTH * cellSize / 30.0).toInt
    wallStroke = new BasicStroke(lineWidth)
    pathStroke = new BasicStroke(pathWidth, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER)
    val fontSize = 2 + (cellSize >> 1)
    textFont = new Font(GUIUtil.DEFAULT_FONT_FAMILY, Font.BOLD, fontSize)
  }

  /** Render the Environment on the screen. */
  def render(g2: Graphics2D, maze: MazeModel) {
    if (maze == null) return
    drawBackground(g2, maze)
    drawVisitedCells(g2, maze)
    drawWalls(g2, maze)
    drawPath(g2, maze)
    drawStartFinish(g2, maze)
  }

  private def drawBackground(g2: Graphics2D, maze: MazeModel) {
    g2.setColor(MazeRenderer.BG_COLOR)
    val width = maze.width
    val height = maze.height
    g2.fillRect(0, 0, cellSize * width, cellSize * height)
  }

  private def drawVisitedCells(g2: Graphics2D, maze: MazeModel) {
    g2.setColor(MazeRenderer.VISITED_COLOR)
    for (j <- 0 until maze.height) {
      for (i <- 0 until maze.width) {
        val c = maze.getCell(i, j)
        assert(c != null, "Error1 pos i=" + i + " j=" + j + " is out of bounds.")
        val xpos = i * cellSize
        val ypos = j * cellSize
        if (c.visited) {
          g2.setColor(MazeRenderer.VISITED_COLOR)
          g2.fillRect(xpos + 1, ypos + 1, cellSize, cellSize)
        }
      }
    }
  }

  private def drawWalls(g2: Graphics2D, maze: MazeModel) {
    g2.setStroke(wallStroke)
    g2.setColor(MazeRenderer.WALL_COLOR)
    for (j <- 0 until maze.height) {
      for (i <- 0 until maze.width) {
        val c = maze.getCell(i, j)
        assert(c != null, "Error2 pos i=" + i + " j=" + j + " is out of bounds.")
        val xpos = i * cellSize
        val ypos = j * cellSize
        if (c.eastWall) g2.drawLine(xpos + cellSize, ypos, xpos + cellSize, ypos + cellSize)
        if (c.southWall) g2.drawLine(xpos, ypos + cellSize, xpos + cellSize, ypos + cellSize)
      }
    }
  }

  private def drawPath(g2: Graphics2D, maze: MazeModel) {
    g2.setStroke(pathStroke)
    g2.setColor(MazeRenderer.PATH_COLOR)
    for (j <- 0 until maze.height) {
      for (i <- 0 until maze.width) {
        val c = maze.getCell(i, j)
        val xpos = i * cellSize
        val ypos = j * cellSize
        assert(c != null)
        if (c.eastPath) g2.drawLine(xpos + halfCellSize, ypos + halfCellSize, xpos + cellSize, ypos + halfCellSize)
        if (c.westPath) g2.drawLine(xpos, ypos + halfCellSize, xpos + halfCellSize, ypos + halfCellSize)
        if (c.northPath) g2.drawLine(xpos + halfCellSize, ypos + halfCellSize, xpos + halfCellSize, ypos)
        if (c.southPath) g2.drawLine(xpos + halfCellSize, ypos + cellSize, xpos + halfCellSize, ypos + halfCellSize)
      }
    }
  }

  private def drawStartFinish(g2: Graphics2D, maze: MazeModel) {
    g2.setFont(textFont)
    g2.setColor(MazeRenderer.TEXT_COLOR)
    MazeRenderer.drawChar("S", maze.startPosition, cellSize, g2)
    MazeRenderer.drawChar("F", maze.stopPosition, cellSize, g2)
  }
}
