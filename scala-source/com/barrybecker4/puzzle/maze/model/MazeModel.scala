// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.maze.model

import scala.compiletime.uninitialized
import com.barrybecker4.common.geometry.{IntLocation, Location}

/**
  * The model part of the model view controller pattern for the maze.
  * @author Barry Becker
  */
class MazeModel(var width: Int, var height: Int) {

  /** The grid of cells that make up the maze paths in x,y (col, row) order. */
  private var grid: Array[Array[MazeCell]] = uninitialized
  val startPosition: Location = IntLocation(2, 2)
  /** Set by [[com.barrybecker4.puzzle.maze.MazeGenerator]] to the deepest cell reached. */
  var stopPosition: Location = uninitialized

  setDimensions(width, height)

  def setDimensions(width: Int, height: Int): Unit = {
    this.width = width
    this.height = height
    this.grid = createGrid(width, height)
    // a border around the whole maze
    setConstraints()
  }

  private def createGrid(width: Int, height: Int) = {
    val grid = Array.ofDim[MazeCell](width, height)
    for (j <- 0 until height; i <- 0 until width)
      grid(i)(j) = new MazeCell
    grid
  }

  def getCell(p: Location): MazeCell = getCell(p.getX, p.getY)

  def getCell(x: Int, y: Int): MazeCell = {
    assert(x < width, "Tried to get x = " + x + " when width = " + width)
    assert(y < height, "Tried to get y = " + y + " when height = " + height)
    grid(x)(y)
  }

  /** mark all the cells unvisited. */
  def unvisitAll(): Unit = {
    for (j <- 0 until height; i <- 0 until width) {
      val c = grid(i)(j)
      c.clear()
    }
  }

  /** Set walls. Mark all the cells around the periphery as visited so there will be walls generated there */
  private def setConstraints(): Unit = {
    setRightLeftConstraints()
    setTopAndBottomConstraints()
  }

  private def setRightLeftConstraints(): Unit = {
    for (j <- 0 until height) {
      grid(0)(j).visited = true
      grid(width - 1)(j).visited = true
    }
  }

  private def setTopAndBottomConstraints(): Unit = {
    for (i <- 0 until width) {
      grid(i)(0).visited = true
      grid(i)(height - 1).visited = true
    }
  }
}

