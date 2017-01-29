// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.model.update

import java.lang.reflect.{Constructor, InvocationTargetException}
import java.security.AccessControlException

import com.barrybecker4.puzzle.sudoku.model.board.Board

/**
  * Responsible for updating a board given a list of updaters to apply.
  * Unfortunately we cannot use reflection in an applet without making is a signed applet
  * (or have signed jars in the case of webstart), so we use NonReflectiveBoardUpdater in deployed version.
  *
  * @author Barry Becker
  */
class ReflectiveBoardUpdater(updaterClasses: List[Class[_ <: AbstractUpdater]]) extends IBoardUpdater {

  def this(classes: Class[_ <: AbstractUpdater]*) {
    this(updaterClasses = classes.toList)
  }

  /**
    * Update candidate lists for all cells then set the unique values that are determined.
    * First create the updaters using reflection, then apply them.
    */
  def updateAndSet(board: Board) {
    val updaters = createUpdaters(board)
    for (updater <- updaters) updater.updateAndSet()
  }

  /**
    * Creates the updater instances using reflection. Cool.
    *
    * @param board the board
    * @return list of updaters to apply
    */
  private def createUpdaters(board: Board) = {
    var updaters = List[IUpdater]()
    for (clazz <- updaterClasses) {
      var ctor: Constructor[_ <: AbstractUpdater] = null
      try {
        ctor = clazz.getDeclaredConstructor(classOf[Board])
        try {
          ctor.setAccessible(true)
          val updater: IUpdater = ctor.newInstance(board)
          updaters +:= updater
        } catch {
          case e: InstantiationException =>
            throw new IllegalStateException("Could not instantiate " + clazz.getName, e)
          case e: IllegalAccessException =>
            throw new IllegalStateException("Could not access constructor of " + clazz.getName, e)
          case e: InvocationTargetException =>
            throw new IllegalStateException("Could not invoke constructor of " + clazz.getName, e)
          case e: AccessControlException =>
            System.out.println("allowing access when should not")
        }
      } catch {
        case e: NoSuchMethodException =>
          throw new IllegalStateException("Could not find constructor for " + clazz.getName, e)
      }
    }
    updaters
  }
}
