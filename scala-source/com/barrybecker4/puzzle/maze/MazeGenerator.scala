// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.maze

import com.barrybecker4.common.concurrency.ThreadUtil
import com.barrybecker4.common.geometry.IntLocation
import com.barrybecker4.common.geometry.Location
import com.barrybecker4.math.MathUtil
import com.barrybecker4.puzzle.maze.model.MazeCell
import com.barrybecker4.puzzle.maze.model.Probabilities
import com.barrybecker4.puzzle.maze.model.StateStack
import com.barrybecker4.puzzle.maze.ui.MazePanel

/**
  * Program to automatically generate a Maze.
  * Motivation: Help my son, Brian, to excel at Kumon by trying these mazes with a pencil.
  * this is the global space containing all the cells, walls, and particles.
  * Assumes an M*N grid of cells.
  * X axis increases to the left.
  * Y axis increases downwards to be consistent with java graphics.
  *
  * @author Barry Becker
  */
object MazeGenerator {
  /** if the animation speed is less than this things will slow down a lot */
  private val SLOW_SPEED_THRESH = 10
}

class MazeGenerator(val panel: MazePanel) {

  private val maze = panel.maze
  private var stack: StateStack = _
  /** put the stop point at the maximum search depth. */
  private var maxDepth = 0
  /** set this to true to get the generator to stop generating */
  private var interrupted = false

  /** generate the maze. */
  def generate(forwardProb: Double, leftProb: Double, rightProb: Double): Unit = {
    maxDepth = 0
    val probs = Probabilities(forwardProb, leftProb, rightProb)
    stack = new StateStack(probs)
    search()
    panel.repaint()
  }

  /** Do a depth first search (without recursion) of the grid space to determine the graph.
    * Used to use a recursive algorithm but it was slower and would give stack overflow exceptions.
    */
  def search(): Unit = {
    stack.clear()
    val currentPosition = maze.startPosition
    var currentCell = maze.getCell(currentPosition)
    currentCell.visited = true
    // push the initial moves
    stack.pushMoves(currentPosition, IntLocation(0, 1), 0)
    while (!stack.isEmpty && !interrupted)
      currentCell = findNextCell(currentCell)
  }

  /** Stop current work and clear the search stack of states. */
  def interrupt(): Unit = {
    interrupted = true
    if (stack != null) stack.clear()
  }

  /** Find the next cell to visit, given the last cell */
  private def findNextCell(lastCell: MazeCell) = {
    var moved = false
    var currentPosition: Location = null
    var nextCell: MazeCell = null
    var depth: Int = -1
    var dir: Location = null

    while (!moved && !stack.isEmpty && !interrupted) {
      val state = stack.pop()
      currentPosition = state.position
      dir = state.getRelativeMovement
      depth = state.depth
      if (depth > maxDepth) {
        maxDepth = depth
        maze.stopPosition = currentPosition
      }
      if (depth > lastCell.depth)
        lastCell.depth = depth
      val currentCell = maze.getCell(currentPosition)
      val nextPosition = currentCell.getNextPosition(currentPosition, dir)
      nextCell = maze.getCell(nextPosition)
      if (nextCell.visited) {
        addWall(currentCell, dir, nextCell)
      } else {
        moved = true
        nextCell.visited = true
        currentPosition = nextPosition
      }
      refresh()
    }

    refresh()
    if (moved && !interrupted)
      stack.pushMoves(currentPosition, dir, depth + 1)
    nextCell
  }

  /** Place a wall when the path is blocked */
  private def addWall(currentCell: MazeCell, dir: Location, nextCell: MazeCell): Unit = {
    if (dir.getX == 1)          // east
      currentCell.eastWall = true
    else if (dir.getY == 1)     // south
      currentCell.southWall = true
    else if (dir.getX == -1)    // west
      nextCell.eastWall = true
    else if (dir.getY == -1)    // north
      nextCell.southWall = true
  }

  /** This can be really slow if you do a refresh every time */
  private def refresh(): Unit = {
    val speed = panel.animationSpeed
    if (MathUtil.RANDOM.nextDouble() < (2.0 / speed)) {
      panel.paintAll()
      if (speed < MazeGenerator.SLOW_SPEED_THRESH) {
        val diff = MazeGenerator.SLOW_SPEED_THRESH - speed
        ThreadUtil.sleep(diff * diff * 6)
      }
    }
  }
}
