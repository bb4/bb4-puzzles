// Copyright by Barry G. Becker, 2021. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.rubixcube.model

import java.awt.Color

/**
  * There are 6 colors in a rubix cube of any size. One for each face
  */
object FaceColor extends Enumeration {

  type FaceColor = Value

  protected case class Val(color: Color) extends super.Val

  val RED: Val = Val(Color.RED)
  val GREEN: Val = Val(Color.GREEN)
  val BLUE: Val = Val(Color.BLUE)
  val YELLOW: Val = Val(Color.YELLOW)
  val ORANGE: Val = Val(Color.ORANGE)
  val WHITE: Val = Val(Color.WHITE)
}