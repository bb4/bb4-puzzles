package com.barrybecker4.puzzle.rubixcube.ui

import com.barrybecker4.puzzle.rubixcube.model.FaceColor.{BLUE, FaceColor, GREEN, ORANGE, RED, WHITE, YELLOW}

import java.awt.Color

object FaceColorMap {
  private val BLUE_COLOR = new Color(120, 140, 255)
  private val RED_COLOR = new Color(255, 110, 130)
  private val GREEN_COLOR = new Color(140, 240, 160)
  private val YELLOW_COLOR = new Color(250, 240, 10)
  private val ORANGE_COLOR = new Color(255, 180, 100)
  private val WHITE_COLOR = new Color(255, 255, 255)

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
