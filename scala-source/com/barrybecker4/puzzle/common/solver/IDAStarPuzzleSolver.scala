// Copyright by Barry G. Becker, 2021. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.common.solver

import com.barrybecker4.search.IDAStarSearch
import com.barrybecker4.search.space.SearchSpace


/**
  * @author Barry Becker
  */
class IDAStarPuzzleSolver[P, M](searchSpace: SearchSpace[P, M])
  extends IDAStarSearch[P, M](searchSpace)
  with PuzzleSolver[M] {
}
