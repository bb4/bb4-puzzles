// Copyright by Barry G. Becker, 2021. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.rubixcube.model

import com.barrybecker4.puzzle.rubixcube.model.Direction.Direction
import com.barrybecker4.puzzle.rubixcube.model.FaceColor.FaceColor


/**
  * One of the little part-cubes that compose the big one. Immutable.
  */
case class Minicube(orientationToColor: Map[Orientation, FaceColor]) {

   def rotate(orientation: Orientation, direction: Direction): Minicube =
      Minicube(orientationToColor.map({case (k, v) => k.rotate(orientation, direction) -> v }))

}
