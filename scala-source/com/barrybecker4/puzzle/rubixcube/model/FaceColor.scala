// Copyright by Barry G. Becker, 2021. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.rubixcube.model

import java.awt.Color

/**
  * There are 6 colors in a rubix cube of any size. One for each face
  */
object FaceColor extends Enumeration {

  type FaceColor = Value
  val RED, GREEN , BLUE, YELLOW, ORANGE, WHITE = Value
}