package com.barrybecker4.puzzle.maze.model

import org.scalatest.funsuite.AnyFunSuite

/**
  * Deterministic structural checks on the maze grid (no UI or randomness).
  */
class MazeModelSuite extends AnyFunSuite {

  test("dimensions and cell access") {
    val m = new MazeModel(8, 6)
    assert(m.width == 8)
    assert(m.height == 6)
    val c = m.getCell(3, 4)
    c.visited = true
    m.unvisitAll()
    assert(!m.getCell(3, 4).visited)
  }

  test("start position is fixed and inside grid") {
    val m = new MazeModel(10, 10)
    val s = m.startPosition
    assert(s.getX >= 0 && s.getX < m.width)
    assert(s.getY >= 0 && s.getY < m.height)
  }

  test("resize grid replaces cells") {
    val m = new MazeModel(4, 4)
    m.setDimensions(5, 5)
    assert(m.width == 5 && m.height == 5)
    m.getCell(2, 2).visited = true
    m.unvisitAll()
    assert(!m.getCell(2, 2).visited)
  }
}
