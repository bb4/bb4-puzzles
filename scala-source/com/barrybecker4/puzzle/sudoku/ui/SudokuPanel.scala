// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.ui

import java.awt.Graphics
import javax.swing.JPanel
import com.barrybecker4.common.geometry.Location
import com.barrybecker4.puzzle.sudoku.model.Board
import com.barrybecker4.puzzle.sudoku.SudokuSolver
import com.barrybecker4.puzzle.sudoku.generation.{SimpleSudokuGenerator, SudokuGenerator}

/**
  * Draws the current best solution to the puzzle in a panel.
  * The view in the model-view-controller pattern.
  * @author Barry Becker
  */
final class SudokuPanel private(b: Board) extends JPanel with RepaintListener {

  private val renderer: SudokuRenderer = new SudokuRenderer(b)
  private val inputListener = new UserInputListener(renderer)
  inputListener.addRepaintListener(this)
  addMouseListener(inputListener)
  addKeyListener(inputListener)


  def this(initialData: Array[Array[Int]]) {
    this(new Board(initialData).updateFromInitialData().get)
  }

  def setBoard(b: Board): Unit = {
    renderer.board = b
  }

  def repaint(board: Board): Unit = {
    setBoard(board)
    super.repaint()
  }

  def setShowCandidates(show: Boolean): Unit = {
    renderer.setShowCandidates(show)
    repaint()
  }

  /** Mark the users values as correct or not. */
  def validatePuzzle(): Unit = {
    inputListener.validateValues(getSolvedPuzzle)
    inputListener.useCorrectEntriesAsOriginal(getBoard)
    repaint()
  }

  private def getSolvedPuzzle = {
    val solver = new SudokuSolver
    solver.solvePuzzle(getBoard).get
  }

  /** Reset to new puzzle with specified initial data.
    * @param initialData starting values.
    */
  def reset(initialData: Array[Array[Int]]): Unit = {
    renderer.board = new Board(initialData)
    repaint()
  }

  def startSolving(solver: SudokuSolver): Unit = {
    val solution = solver.solvePuzzle(getBoard)
    showMessage(solution)
    inputListener.clear()
  }

  private def showMessage(solution: Option[Board]): Unit = {
    if (solution.isDefined)
      println("The final solution is shown.")
    else println("This puzzle is not solvable!")
  }

  def generateNewPuzzle(generator: SudokuGenerator, size: Int): Unit = {
    inputListener.clear()
    renderer.board = generator.generatePuzzleBoard(size)
    repaint()
  }

  def getBoard: Board = renderer.board
  def valueEntered(): Unit = { repaint() }

  def cellSelected(location: Location): Unit = {
    repaint()
    // without this we do not get key events.
    requestFocus()
  }

  def requestValidation(): Unit = { validatePuzzle() }

  /** This renders the current state of the PuzzlePanel to the screen. */
  override protected def paintComponent(g: Graphics): Unit = {
    super.paintComponents(g)
    renderer.render(g, inputListener.getUserEnteredValues, inputListener.getCurrentCellLocation, getWidth, getHeight)
  }
}

