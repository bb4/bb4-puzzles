// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.maze.ui

import java.awt.Cursor
import javax.swing.JPanel

import com.barrybecker4.common.concurrency.Worker
import com.barrybecker4.puzzle.maze.{MazeGenerator, MazeSolver}
import com.barrybecker4.ui.sliders.{LabeledSlider, SliderChangeListener}

/**
  * Controller part of the MVC pattern.
  * Launches generator and solvers in separate threads so the UI is not locked.
  *
  * @author Barry Becker
  */
final class MazeController(var mazePanel: MazePanel)

/**
  * Constructor.
  */
  extends SliderChangeListener {
  private var solver = new MazeSolver(mazePanel)
  private var generateWorker: Worker = _
  private var generator: MazeGenerator = _
  private var repaintListener: JPanel = _

  /**
    * This panel will be repainted when the regeneration is complete.
    * Without this, the top controls do not refresh properly when shown in an applet (and only the applet).
    *
    * @param panel the repaint listener
    */
  def setRepaintListener(panel: JPanel): Unit = {
    repaintListener = panel
  }

  /** called when the animation speed changes */
  def sliderChanged(slider: LabeledSlider): Unit = {
    mazePanel.animationSpeed = slider.getValue.toInt
  }

  /** Regenerate the maze based on the current UI parameter settings and current size of the panel. */
  def regenerate(thickness: Int, animationSpeed: Int, forwardP: Double, leftP: Double, rightP: Double): Unit = {
    if (solver.isWorking) solver.interrupt()
    if (generator != null) {
      generator.interrupt()
      // blocks until done working (which will be soon now that it has been interrupted)
      generateWorker.get
    }

    class GeneratorWorker extends Worker {

      def construct(): AnyRef = {
        generator = new MazeGenerator(mazePanel)
        mazePanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR))
        val sum: Double = forwardP + leftP + rightP
        mazePanel.animationSpeed = animationSpeed
        mazePanel.setThickness(thickness)
        generator.generate(forwardP / sum, leftP / sum, rightP / sum)
        boolean2Boolean(true)
      }

      override def finished(): Unit = {
        mazePanel.setCursor(Cursor.getDefaultCursor)
        if (repaintListener != null) repaintListener.repaint()
      }
    }

    generateWorker = new GeneratorWorker()
    generateWorker.start()
  }

  /**
    * Don't solve if already generating or solving.
    * @param animationSpeed the speed at which to show the solution.
    */
  def solve(animationSpeed: Int): Unit = {
    if (!generateWorker.isWorking) {
      if (solver.isWorking) solver.interrupt()

      val worker: Worker = new Worker() {
        def construct: AnyRef = {
          mazePanel.animationSpeed = animationSpeed
          solver = new MazeSolver(mazePanel)
          solver.solve()
          boolean2Boolean(true)
        }

        override def finished(): Unit = {
          mazePanel.repaint()
        }
      }
      worker.start()
    }
  }

}
