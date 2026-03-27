package com.barrybecker4.puzzle.maze

import com.barrybecker4.common.geometry.IntLocation
import com.barrybecker4.math.MathUtil
import com.barrybecker4.puzzle.maze.ui.MazePanel
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite

import java.awt.Dimension

class MazeSolverSuite extends AnyFunSuite with BeforeAndAfterEach {

  override def beforeEach(): Unit =
    MathUtil.RANDOM.setSeed(0)

  /** Avoids real painting during tests. */
  private class TestMazePanel extends MazePanel {
    override def paintCell(p: com.barrybecker4.common.geometry.Location): Unit = ()
  }

  test("solve reaches stop on a simple eastward corridor") {
    val panel = new TestMazePanel()
    panel.setSize(new Dimension(200, 200))
    panel.setThickness(40)
    val maze = panel.maze
    maze.stopPosition = IntLocation(4, 2)
    val solver = new MazeSolver(panel)
    solver.solve()
    assert(maze.getCell(maze.stopPosition).visited)
  }

}
