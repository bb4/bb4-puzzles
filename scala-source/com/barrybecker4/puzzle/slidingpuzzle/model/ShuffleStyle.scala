// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.model

/**
  * How the tiles of a new puzzle are mixed up.
  */
enum ShuffleStyle {
  /**
    * Easy. Slides tiles at random until the blank has been in every cell. The positions that come out are
    * shallow, close to solved (about 23 slides from solved for the 15 puzzle), so they are not hard to solve.
    */
  case RANDOM_WALK
  /**
    * Hard. Every position that can be solved is equally likely, so most are far from solved (about 53 slides
    * for the 15 puzzle if solved the quickest way, and around 100 for the 24 puzzle).
    */
  case UNIFORM_RANDOM
}
