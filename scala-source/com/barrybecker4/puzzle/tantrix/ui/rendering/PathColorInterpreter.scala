// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.ui.rendering

import java.awt._
import com.barrybecker4.puzzle.tantrix.model.PathColor
import com.barrybecker4.puzzle.tantrix.model.PathColor._

/**
  * Converts from path color to rendering color
  * @author Barry Becker
  */
object PathColorInterpreter {

  private val BLUE_COLOR = new Color(60, 90, 250)
  private val RED_COLOR = new Color(210, 75, 70)
  private val GREEN_COLOR = new Color(40, 210, 50)
  private val YELLOW_COLOR = new Color(230, 230, 40)
  private val WHITE_COLOR = new Color(251, 250, 254)

  private[rendering] def getColorForPathColor(pathColor: PathColor): Color = {
    pathColor match {
      case BLUE => BLUE_COLOR
      case RED => RED_COLOR
      case GREEN => GREEN_COLOR
      case YELLOW => YELLOW_COLOR
      case WHITE => WHITE_COLOR
    }
  }
}

