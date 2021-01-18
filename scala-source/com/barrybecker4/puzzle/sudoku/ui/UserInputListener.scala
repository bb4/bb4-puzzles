// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.ui

import java.awt.event.{KeyEvent, KeyListener, MouseEvent, MouseListener}
import javax.swing.JOptionPane

import com.barrybecker4.common.geometry.Location
import com.barrybecker4.puzzle.sudoku.model.{Board, ValueConverter}

import scala.collection.immutable.HashMap

/**
  * Draws the current best solution to the puzzle in a panel.
  * The view in the model-view-controller pattern.
  * @author Barry Becker
  */
class UserInputListener private[ui](var locator: CellLocator) extends MouseListener with KeyListener {

  private var currentCellLocation: Location = _
  private var userEnteredValues: Map[Location, UserValue] = _
  private var listeners = List[RepaintListener]()
  clear()

  def clear(): Unit = {
    userEnteredValues = HashMap[Location, UserValue]()
  }

  private[ui] def getCurrentCellLocation = currentCellLocation

  private[ui] def getUserEnteredValues = userEnteredValues

  def mouseClicked(e: MouseEvent): Unit = {
    val location = locator.getCellCoordinates(e.getPoint)
    setCurrentLocation(location)
  }

  private def toTuple(loc: Location) = (loc.row + 1, loc.col + 1)

  private[ui] def useCorrectEntriesAsOriginal(board: Board): Unit = {
    for (location <- userEnteredValues.keySet) {
      val value = userEnteredValues.get(location)
      if (value.get.isValid)
        board.setOriginalValue(toTuple(location), value.get.getValue)
    }
  }

  /**
    * Handle keyboard input.
    *
    * @param event the key event corresponding to key pressed.
    */
  def keyPressed(event: KeyEvent): Unit = {
    val key = event.getKeyChar
    val keyCode = event.getKeyCode
    if (keyCode == KeyEvent.VK_ENTER) requestValidation()
    else if (isArrowKey(keyCode)) handleArrowKey(keyCode)
    else if (!isOriginalCell(currentCellLocation)) {
      // only enter the value if its not already a fixed/correct value
      handleValueEntry(key)
    }
  }

  private def isOriginalCell(location: Location) =
    locator.board.getCell(toTuple(location)).originalValue > 0

  private def isArrowKey(keyCode: Int) =
    keyCode >= KeyEvent.VK_LEFT && keyCode <= KeyEvent.VK_DOWN ||
      keyCode >= KeyEvent.VK_KP_UP && keyCode <= KeyEvent.VK_KP_DOWN

  private def handleArrowKey(keyCode: Int): Unit = {
    val location = keyCode match {
      case KeyEvent.VK_LEFT | KeyEvent.VK_KP_LEFT => currentCellLocation.incrementOnCopy(0, -1)
      case KeyEvent.VK_RIGHT | KeyEvent.VK_KP_RIGHT => currentCellLocation.incrementOnCopy(0, 1)
      case KeyEvent.VK_UP | KeyEvent.VK_KP_UP => currentCellLocation.incrementOnCopy(-1, 0)
      case KeyEvent.VK_DOWN | KeyEvent.VK_KP_DOWN => currentCellLocation.incrementOnCopy(1, 0)
    }
    setCurrentLocation(location)
  }

  /**
    * Set the current location if it valid, and notify renderer of change.
    *
    * @param location location
    */
  private def setCurrentLocation(location: Location): Unit = {
    if (isValid(location)) {
      currentCellLocation = location
      notifyCellSelected(currentCellLocation)
    }
  }

  private def isValid(location: Location) = {
    val n = locator.board.edgeLength
    location != null && location.row >= 0 && location.row < n && location.col >= 0 && location.col < n
  }

  private def handleValueEntry(key: Char): Unit = {
    try {
      val value = ValueConverter.getValue(key, locator.board.edgeLength)
      val userValue = new UserValue(value)
      userEnteredValues += (currentCellLocation -> userValue)
      notifyValueEntered()
    } catch {
      case exception: IllegalArgumentException => JOptionPane.showMessageDialog(null, exception.getMessage)
    }
  }

  private[ui] def validateValues(solvedPuzzle: Board): Unit = {
    for (location <- userEnteredValues.keySet) {
      assert(location != null)
      val userValue = userEnteredValues.get(location)
      val valid = userValue.get.getValue == solvedPuzzle.getValue((location.row + 1, location.col + 1))
      userValue.get.setValid(valid)
    }
  }

  private[ui] def addRepaintListener(listener: RepaintListener): Unit = {
    listeners +:= listener
  }

  def removeRepaintListener(listener: RepaintListener): Unit = {
    listeners = listeners.filter(_ != listener)
  }

  private def notifyValueEntered(): Unit = listeners.foreach(x => x.valueEntered())

  private def notifyCellSelected(location: Location): Unit = listeners.foreach(x => x.cellSelected(location))

  private def requestValidation(): Unit = listeners.foreach(x => x.requestValidation())

  def keyTyped(event: KeyEvent): Unit = {}
  def keyReleased(e: KeyEvent): Unit = {}
  def mousePressed(e: MouseEvent): Unit = {}
  def mouseReleased(e: MouseEvent): Unit = {}
  def mouseEntered(e: MouseEvent): Unit = {}
  def mouseExited(e: MouseEvent): Unit = {}
}
