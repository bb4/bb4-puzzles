// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.ui

import javax.swing._
import javax.swing.event.{ChangeEvent, ChangeListener}

import com.barrybecker4.common.search.Refreshable
import com.barrybecker4.puzzle.common.ui.{PuzzleApplet, PuzzleViewer}
import com.barrybecker4.puzzle.common.{AlgorithmEnum, PuzzleController}
import com.barrybecker4.puzzle.tantrix.TantrixController
import com.barrybecker4.puzzle.tantrix.model.{HexTiles, TantrixBoard, TilePlacement}
import com.barrybecker4.puzzle.tantrix.solver.Algorithm
import com.barrybecker4.ui.util.GUIUtil

/**
  * Tantrix Puzzle Application to show the solving of the puzzle.
  *
  * @author Barry Becker
  */
object TantrixPuzzle {
  private val DEFAULT_NUM_TILES = 7

  /**
    * use this to run as an application instead of an applet.
    */
  def main(args: Array[String]) {
    val applet = new TantrixPuzzle(args)
    // this will call applet.init() and start() methods instead of the browser
    GUIUtil.showApplet(applet)
  }
}

final class TantrixPuzzle(args: Array[String]) extends PuzzleApplet[TantrixBoard, TilePlacement] with ChangeListener {

  private var spinner: JSpinner = _
  private var primaryColorLabel: JLabel = _

  def this() { this(Array[String]())}

  protected def createViewer: PuzzleViewer[TantrixBoard, TilePlacement] = {
    //TantrixBoard board = new TantrixBoard(new HexTiles());
    new TantrixViewer
  }

  protected def createController(viewer: Refreshable[TantrixBoard, TilePlacement]): PuzzleController[TantrixBoard, TilePlacement] = {
    val controller = new TantrixController(viewer)
    controller.setNumTiles(TantrixPuzzle.DEFAULT_NUM_TILES)
    controller
  }

  protected def getAlgorithmValues: Array[AlgorithmEnum[TantrixBoard, TilePlacement]] = Algorithm.VALUES

  override protected def createBottomControls: JPanel = {
    val label = new JLabel("Number of Tiles")
    val model = new SpinnerNumberModel(TantrixPuzzle.DEFAULT_NUM_TILES, 3, 30, 1)
    spinner = new JSpinner(model)
    spinner.addChangeListener(this)
    val numTilesSelector = new JPanel
    val primaryColorPrefix = new JLabel("Primary color: ")
    primaryColorLabel = new JLabel("")
    setPrimaryColor(TantrixPuzzle.DEFAULT_NUM_TILES)
    numTilesSelector.add(label)
    numTilesSelector.add(spinner)
    numTilesSelector.add(primaryColorLabel)
    numTilesSelector
  }

  def stateChanged(e: ChangeEvent) {
    val tileNum = spinner.getValue.asInstanceOf[Integer]
    getController.setNumTiles(tileNum)
    setPrimaryColor(tileNum)
  }

  private def setPrimaryColor(tileNum: Int): Unit = {
    primaryColorLabel.setText(HexTiles.TILES.getTile(tileNum).primaryColor.toString)
  }

  private def getController = controller_.asInstanceOf[TantrixController]
}
