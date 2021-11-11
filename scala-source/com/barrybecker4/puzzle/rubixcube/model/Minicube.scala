// Copyright by Barry G. Becker, 2021. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.rubixcube.model

import com.barrybecker4.puzzle.rubixcube.Location
import com.barrybecker4.puzzle.rubixcube.model.Direction
import com.barrybecker4.puzzle.rubixcube.model.FaceColor
import com.barrybecker4.puzzle.rubixcube.ui.FaceColorMap.getColor
import java.awt.Color


/**
  * One of the little part-cubes that compose the big one. Immutable.
  * The origPosition is mainly used to get a unique hashcode
  */
case class Minicube(orientationToColor: Map[Orientation, FaceColor], origPosition: Location) {

  def rotate(orientation: Orientation, direction: Direction): Minicube =
    Minicube(orientationToColor.map({ case (oriented, color) => oriented.rotate(orientation, direction) -> color }), origPosition)

  // this throws error if no color for specified orientation
  def getColorForOrientation(orientation: Orientation): FaceColor =
    if (orientationToColor.contains(orientation))
      orientationToColor(orientation)
    else throw new IllegalArgumentException(s"Could not find $orientation in ${orientationToColor.keySet}")

  // this returns gray if no color for specified orientation
  def getColorFor(orientation: Orientation): Color =
    if (orientationToColor.contains(orientation)) getColor(orientationToColor(orientation)) else Color.BLACK

}
