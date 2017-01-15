package com.barrybecker4.puzzle.maze

import com.barrybecker4.common.geometry.IntLocation
import com.barrybecker4.common.geometry.Location
import com.barrybecker4.puzzle.maze.model.GenState
import com.barrybecker4.puzzle.maze.model.MazeCell
import com.barrybecker4.puzzle.maze.model.MazeModel
import com.barrybecker4.puzzle.maze.model.StateStack
import com.barrybecker4.puzzle.maze.ui.MazePanel

/**
  * Solves a maze using depth first search.
  *
  * @author Barry Becker
  */
class MazeSolver(var panel: MazePanel) {

  private var maze: MazeModel = panel.maze
  private var stack: StateStack = new StateStack
  var isWorking: Boolean = false
  private var interrupted: Boolean = false

  /** Stop current work and clear the search stack of states. */
  def interrupt () {
    interrupted = true
    stack.clear ()
  }

  /**
    * Do a depth first search (without recursion) of the grid space to determine the solution to the maze.
    * Very similar to search (see MazeGenerator), but now we are solving it.
    */
  def solve () {
    isWorking = true
    interrupted = false
    maze.unvisitAll()
    stack.clear()
    findSolution()
    panel.paintAll()
    isWorking = false
  }

  /** Keep track of the current path, since backtracking along it may be necessary if we encounter a dead end. */
  private def findSolution() {
    var solutionPath: List[Location] = List()
    var currentPosition: Location = maze.startPosition
    var currentCell: MazeCell = maze.getCell(currentPosition)
    // push the initial moves
    stack.pushMoves(currentPosition, new IntLocation (0, 1), 0)
    panel.paintAll()
    var dir: Location = null
    var depth: Int = 0
    var solved: Boolean = false
    // while there are still paths to try and we have not yet encountered the finish
    while (!stack.isEmpty && !solved && !interrupted) {
      val state: GenState = stack.pop()
      currentPosition = state.position
      solutionPath +:= currentPosition
      if (currentPosition == maze.stopPosition) {
        solved = true
      }
      dir = state.getRelativeMovement
      depth = state.depth
      if (depth > currentCell.depth) {
        currentCell.depth = depth
      }
    }
    currentCell = maze.getCell (currentPosition)
    val nextPosition: Location = currentCell.getNextPosition (currentPosition, dir)
    search(solutionPath, currentCell, dir, depth, nextPosition)
  }

  /** @return path to the solution */
  private def search(solutionPath: List[Location], currentCell: MazeCell,
                     dir: Location, depth: Int, nextPosition: Location): List[Location] = {
    var path = solutionPath
    val nextCell: MazeCell = maze.getCell (nextPosition)
    val eastBlocked: Boolean = dir.getX == 1 && currentCell.eastWall
    val westBlocked: Boolean = dir.getX == - 1 && nextCell.eastWall
    val southBlocked: Boolean = dir.getY == 1 && currentCell.southWall
    val northBlocked: Boolean = dir.getY == - 1 && nextCell.southWall
    val pathBlocked: Boolean = eastBlocked || westBlocked || southBlocked || northBlocked
    if (!pathBlocked) {
      advanceToNextCell(currentCell, dir, depth, nextPosition, nextCell)
    }
    else {
      path = backTrack(path)
    }
    path
  }

  private def advanceToNextCell(currentCell: MazeCell, dir: Location, depth: Int,
                                 nextPosition: Location, nextCell: MazeCell) {
    var currentPosition: Location = null
    if (dir.getX == 1) {   // east
      currentCell.eastPath = true
      nextCell.westPath = true
    }
    else if (dir.getY == 1) {   // south
      currentCell.southPath = true
      nextCell.northPath = true
    }
    else if (dir.getX == - 1) {   // west
      currentCell.westPath = true
      nextCell.eastPath = true
    }
    else if (dir.getY == - 1) {   // north
      currentCell.northPath = true
      nextCell.southPath = true
    }
    nextCell.visited = true
    currentPosition = nextPosition
    // now at a new location
    stack.pushMoves(currentPosition, dir, depth + 1)
    panel.paintCell(currentPosition)
  }

  /**
    * Back up to the next path that will be tried
    *
    * @param solutionPath list of locations leading ot the solution.
    */
  private def backTrack(solutionPath: List[Location]): List[Location] = {
    println("s stack empty = " + stack.isEmpty)
    val lastState: GenState = stack.peek()
    var path = solutionPath
    var pos: Location = null
    do {
      pos = path.head
      path = path.tail
      val cell: MazeCell = maze.getCell(pos)
      cell.clearPath()
    } while (!pos.eq(lastState.position))
    println("path len after bactracking: " + path.length)
    path
  }
}
