// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.maze.model

import com.barrybecker4.common.geometry.Location

/**
  * A region of space bounded by walls in the maze.
  * @author Barry Becker
  */
class MazeCell() {

  clearPath()
  var visited = false
  // walls in the positive x, y directions.
  // when these are true, we render walls
  var eastWall = false
  var southWall = false
  // the 4 possible exiting paths are: e, w, n, s
  // Show 0 or 2 of them at any given time in a cell when solving the maze
  var eastPath = false
  var westPath = false
  var northPath = false
  var southPath = false
  var depth: Int = 0

  def getNextPosition(currentPosition: Location, dir: Location): Location = {
    visited = true
    currentPosition.incrementOnCopy(dir)
  }

  /** return to initial state. */
  def clear() {
    clearPath()
    visited = false
    depth = 0
  }

  def clearPath() {
    eastPath = false
    westPath = false
    northPath = false
    southPath = false
  }

  override def toString: String = "Cell visited=" + visited + " eastWall=" + eastWall + " southWall=" + southWall
}