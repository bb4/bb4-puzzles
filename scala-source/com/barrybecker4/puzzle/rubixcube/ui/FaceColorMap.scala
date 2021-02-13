package com.barrybecker4.puzzle.rubixcube.ui

import com.barrybecker4.puzzle.rubixcube.model.FaceColor.{BLUE, FaceColor, GREEN, ORANGE, RED, WHITE, YELLOW}

import java.awt.Color


object FaceColorMap {
  private val BLUE_COLOR = new Color(70, 100, 250)
  private val RED_COLOR = new Color(250, 30, 80)
  private val GREEN_COLOR = new Color(90, 220, 60)
  private val YELLOW_COLOR = new Color(250, 240, 0)
  private val ORANGE_COLOR = new Color(240, 150, 10)
  private val WHITE_COLOR = new Color(230, 230, 230)

  def getColor(c: FaceColor): Color = {
    c match {
      case BLUE => BLUE_COLOR
      case RED => RED_COLOR
      case GREEN => GREEN_COLOR
      case YELLOW => YELLOW_COLOR
      case ORANGE => ORANGE_COLOR
      case WHITE => WHITE_COLOR
    }
  }
}
