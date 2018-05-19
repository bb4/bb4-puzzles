// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.maze.model

import com.barrybecker4.common.geometry.{IntLocation, Location}

/**
  * The model part of the model view controller pattern for the maze.
  *
  * @author Barry Becker
  */
class MazeModel(var width: Int, var height: Int) {
  setDimensions(width, height)

  /** The grid of cells that make up the maze paths in x,y (col, row) order. */
  private var grid: Array[Array[MazeCell]] = _
  val startPosition: Location = IntLocation(2, 2)
  var stopPosition: Location = _

  def setDimensions(width: Int, height: Int) {
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
  def unvisitAll() {
    for (j <- 0 until height; i <- 0 until width) {
      val c = grid(i)(j)
      c.clear()
    }
  }

  /** Set walls. Mark all the cells around the periphery as visited so there will be walls generated there */
  private def setConstraints() {
    setRightLeftConstraints()
    setTopAndBottomConstraints()
  }

  private def setRightLeftConstraints() {
    for (j <- 0 until height) {
      // left
      var cell = grid(0)(j)
      cell.visited = true
      // right
      cell = grid(width - 1)(j)
      cell.visited = true
    }
  }

  private def setTopAndBottomConstraints() {
    for (i <- 0 until width) {
        // bottom
        var cell = grid(i)(0)
        cell.visited = true
        // top
        cell = grid(i)(height - 1)
        cell.visited = true
    }
  }
}