// Copyright by Barry G. Becker, 2000-2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.common.ui

import com.barrybecker4.common.concurrency.ThreadUtil
import com.barrybecker4.common.format.FormatUtil
import com.barrybecker4.common.math.MathUtil
import com.barrybecker4.common.search.Refreshable
import com.barrybecker4.sound.MusicMaker
import javax.swing._
import java.awt._
import java.util


/**
  * Shows the current state of the puzzle in the ui.
  *
  * @author Barry Becker
  */
object PuzzleViewer {
  private val MARGIN = 15
  private val BACKGROUND_COLOR = new Color(235, 235, 240)
}

abstract class PuzzleViewer[P, M]() extends JPanel with Refreshable[P, M] {
  protected var board: P = _
  protected var status: String = ""
  private var numTries = 0L
  private var totalMem = Runtime.getRuntime.totalMemory
  private var freeMem = Runtime.getRuntime.freeMemory
  /** play a sound effect when a piece goes into place. */
  protected var musicMaker: MusicMaker = new MusicMaker

  override def refresh(board: P, numTries: Long): Unit = {
    status = createStatusMessage(numTries)
    simpleRefresh(board, numTries)
    makeSound()
  }

  override def finalRefresh(path: util.List[M], position: P, numTries: Long, millis: Long): Unit = {
    System.gc()
    status = createFinalStatusMessage(numTries, millis, path)
    System.out.println(status)
    if (position != null) {
      simpleRefresh(position, numTries)
      // give other repaints a chance to process. hack :(
      ThreadUtil.sleep(100)
      simpleRefresh(position, numTries)
    }
  }

  protected def simpleRefresh(theBoard: P, numTries: Long): Unit = {
    board = theBoard
    this.numTries = numTries
    repaint()
  }

  def makeSound(): Unit = {
    musicMaker.playNote(60, 5, 940)
  }

  /**
    * @param numTries number of attempts to solve so far.
    * @return some text to show in the status bar.
    */
  protected def createStatusMessage(numTries: Long): String = {
    var msg = "\nNumber of tries :" + FormatUtil.formatNumber(numTries)
    // I think this might be an expensive operation so don't do it every time
    if (MathUtil.RANDOM.nextDouble < .05) {
      totalMem = Runtime.getRuntime.totalMemory / 1000
      freeMem = Runtime.getRuntime.freeMemory / 1000
    }
    msg += "    Memory used = " + FormatUtil.formatNumber(totalMem - freeMem) + "k"
    msg
  }

  protected def createFinalStatusMessage(numTries: Long, millis: Long, path: util.List[M]): String = {
    val time = millis.toFloat / 1000.0f
    var msg = "Did not find solution."
    if (path != null) {
      msg = "Found solution with " + path.size + " steps in " +
        FormatUtil.formatNumber(time) + " seconds. " + createStatusMessage(numTries)
    }
    msg
  }

  /**
    * This renders the current state of the puzzle to the screen.
    */
  override protected def paintComponent(g: Graphics): Unit = {
    super.paintComponent(g)
    clearBackground(g)
    drawStatus(g, status, PuzzleViewer.MARGIN, PuzzleViewer.MARGIN)
  }

  private def clearBackground(g: Graphics) = {
    val width = this.getWidth
    val height = this.getHeight
    // erase what's there and redraw.
    g.clearRect(0, 0, width, height)
    g.setColor(PuzzleViewer.BACKGROUND_COLOR)
    g.fillRect(0, 0, width, height)
  }

  private def drawStatus(g: Graphics, status: String, x: Int, y: Int) = {
    val lines = status.split("\n")
    var offset = 0
    g.setColor(Color.black)
    for (line <- lines) {
      offset += 14
      g.drawString(line, x, y + offset)
    }
  }
}
