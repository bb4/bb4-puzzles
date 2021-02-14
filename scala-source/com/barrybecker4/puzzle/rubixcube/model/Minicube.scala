// Copyright by Barry G. Becker, 2021. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.rubixcube.model

import com.barrybecker4.puzzle.rubixcube.model.Direction.Direction
import com.barrybecker4.puzzle.rubixcube.model.FaceColor.FaceColor


/**
  * One of the little part-cubes that compose the big one. Immutable.
  */
case class Minicube(orientationToColor: Map[Orientation, FaceColor]) {

   def rotate(orientation: Orientation, direction: Direction): Minicube =
      Minicube(orientationToColor.map({ case (oriented, color) => oriented.rotate(orientation, direction) -> color }))

   def getColorForOrientation(orientation: Orientation): FaceColor =
      if (orientationToColor.contains(orientation))
         orientationToColor(orientation)
      else throw new IllegalArgumentException(s"Could not find $orientation in ${orientationToColor.keySet}")
}
