// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle.ui

import javax.swing.JPanel

import com.barrybecker4.search.Refreshable
import com.barrybecker4.puzzle.common.ui.PuzzleApplet
import com.barrybecker4.puzzle.common.{AlgorithmEnum, PuzzleController}
import com.barrybecker4.puzzle.redpuzzle.RedPuzzleController
import com.barrybecker4.puzzle.redpuzzle.model.{OrientedPiece, Piece, PieceList}
import com.barrybecker4.puzzle.redpuzzle.solver.Algorithm
import com.barrybecker4.ui.sliders.{LabeledSlider, SliderChangeListener}
import com.barrybecker4.ui.util.GUIUtil
import RedPuzzleViewer._

/**
  * Red Puzzle Application to show the solving of the puzzle.
  * This program solves a 9 piece puzzle that has nubs on all 4 sides of every piece.
  * Its virtually impossible to solve by hand because of all the possible permutations.
  *
  * For random number seed =5 and mutable piece objects it takes
  * BruteForce < 8.0s and Genetic= 3.0s
  * After refactoring and applying the generic solver pattern (see puzzle.common) things were faster
  * BruteForce Sequential &lt; 1.0s  BruteForce concurrent &lt; 0.1s
  *
  * @author Barry Becker
  */
object RedPuzzle extends App {
  val applet = new RedPuzzle(args)
  // this will call applet.init() and start() methods instead of the browser
  GUIUtil.showApplet(applet)
}

final class RedPuzzle(args: Array[String]) extends PuzzleApplet[PieceList, OrientedPiece] with SliderChangeListener {

  /** allows you to change the animation speed. */
  private var animSpeedSlider: LabeledSlider = _

  def this() { this(Array[String]())}

  protected def createViewer = new RedPuzzleViewer

  protected def createController(
           viewer: Refreshable[PieceList, OrientedPiece] ): PuzzleController[PieceList, OrientedPiece] = {
    new RedPuzzleController(viewer)
  }

  protected def getAlgorithmValues: Array[AlgorithmEnum[PieceList, OrientedPiece]] = Algorithm.VALUES

  override protected def createBottomControls: JPanel = {
    animSpeedSlider = new LabeledSlider("Speed ", INITIAL_ANIM_SPEED, 1, MAX_ANIM_SPEED)
    animSpeedSlider.setResolution(MAX_ANIM_SPEED - 1)
    animSpeedSlider.setShowAsInteger(true)
    animSpeedSlider.addChangeListener(this)
    animSpeedSlider
  }

  def sliderChanged(slider: LabeledSlider): Unit = {
    viewer.asInstanceOf[RedPuzzleViewer].setAnimationSpeed(animSpeedSlider.getValue.toInt)
  }
}
