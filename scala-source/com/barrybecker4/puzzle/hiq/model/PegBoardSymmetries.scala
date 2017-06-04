// Copyright by Barry G. Becker, 2000-2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.hiq.model

/**
  * Static PegBoard symmetries.
  *
  * @author Barry Becker
  */
object PegBoardSymmetries {
  /**
    * The number of symmetries the board has.
    * Each odd and even pair are mirror images of a 90 degree rotation.
    */
  private[model] val SYMMETRIES = 8

  /**
    * The 8 fold symmetry of the board.
    */
  private val BOARD_SYMMETRY = Array(
    Array[Byte]() /* placeholder for 0 index. i.e. 0-31 */,
    Array[Byte](2, 1, 0, 5, 4, 3, 12, 11, 10, 9, 8, 7, 6, 19, 18, 17, 16, 15, 14, 13, 26, 25, 24, 23, 22, 21, 20, 29, 28, 27, 32, 31, 30),
    Array[Byte](12, 19, 26, 11, 18, 25, 2, 5, 10, 17, 24, 29, 32, 1, 4, 9, 16, 23, 28, 31, 0, 3, 8, 15, 22, 27, 30, 7, 14, 21, 6, 13, 20),
    Array[Byte](26, 19, 12, 25, 18, 11, 32, 29, 24, 17, 10, 5, 2, 31, 28, 23, 16, 9, 4, 1, 30, 27, 22, 15, 8, 3, 0, 21, 14, 7, 20, 13, 6),
    Array[Byte](32, 31, 30, 29, 28, 27, 26, 25, 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0),
    Array[Byte](30, 31, 32, 27, 28, 29, 20, 21, 22, 23, 24, 25, 26, 13, 14, 15, 16, 17, 18, 19, 6, 7, 8, 9, 10, 11, 12, 3, 4, 5, 0, 1, 2),
    Array[Byte](20, 13, 6, 21, 14, 7, 30, 27, 22, 15, 8, 3, 0, 31, 28, 23, 16, 9, 4, 1, 32, 29, 24, 17, 10, 5, 2, 25, 18, 11, 26, 19, 12),
    Array[Byte](6, 13, 20, 7, 14, 21, 0, 3, 8, 15, 22, 27, 30, 1, 4, 9, 16, 23, 28, 31, 2, 5, 10, 17, 24, 29, 32, 11, 18, 25, 12, 19, 26)
  )

  private[model] def getSymmetry(symmIndex: Int) = BOARD_SYMMETRY(symmIndex)
}
