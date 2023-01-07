// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.verification

import com.barrybecker4.puzzle.tantrix.model.PathColor
import com.barrybecker4.puzzle.tantrix.model.TilePlacement
import com.barrybecker4.puzzle.tantrix.model.analysis.fitting.TileFitter


/**
  * Checks the consistency of paths between tiles given a collection of tiles.
  * @param tiles tiles that have not yet been placed on the tantrix
  * @author Barry Becker
  */
class ConsistencyChecker(var tiles: Iterable[TilePlacement], val primaryColor: PathColor) {

  /** Used to check the consistency of all the paths. */
  val fitter: TileFitter = new TileFitter(tiles, primaryColor)

  /** @return the number of tiles that fit perfectly.  */
  def numFittingTiles: Int = tiles.foldLeft(0) {
    (z, placement) => { z + (if (fitter.isFit(placement)) 1 else 0) }
  }

}
