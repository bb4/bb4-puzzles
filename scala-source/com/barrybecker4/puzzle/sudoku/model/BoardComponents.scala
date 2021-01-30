// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.model

import com.barrybecker4.puzzle.sudoku.model.BoardComponents._

object BoardComponents {

  /** static because they are the same for every board. */
  val COMPONENTS: Array[BoardComponents] = (0 to 5).map(i => BoardComponents(i)).toArray

  /** @return the cross product of two sequences */
  private def cross(seq1: Seq[Int], seq2:  Seq[Int]): Seq[Location] =
    for (x <- seq1; y <- seq2) yield (x, y)

  /** @return sub sequences. e.g. for a unitSize of 9, these are Seq(1,2,3) Seq(4,5,6) Seq(7,8,9) */
  private def subCellSeqs(baseSize: Int): Seq[Seq[Int]] =
    for (x <- 0 until baseSize) yield {
      for (y <- 1 to baseSize) yield x * baseSize + y
    }
}

/**
  * The internal structures for a board of a specified size.
  * @author Barry Becker
  */
case class BoardComponents(baseSize: Int = 3) {

  assert (baseSize <= 5, "baseSize = " + baseSize)
  val unitSize: Int = baseSize * baseSize

  private val subSeqs = subCellSeqs(baseSize)

  val digits: Seq[Int] = 1 to unitSize
  val digitSet: Set[Int] = digits.toSet
  val squares: Seq[Location] = cross(digits, digits)

  val unitList: Seq[Seq[Location]] =
    (for (c <- digits) yield cross(digits, Seq(c))) ++
    (for (r <- digits) yield cross(Seq(r), digits)) ++
    (for (rs <- subSeqs; cs <- subSeqs) yield cross(rs, cs))

  val units: Map[Location, Seq[Seq[Location]]] =
    (for (s <- squares) yield {
      s -> (for (u <- unitList; if u.contains(s)) yield u)
    }).toMap

  val peers: Map[Location, Set[Location]] =
    (for (s <- squares) yield s -> (units(s).reduceLeft(_ ++ _).toSet - s)).toMap

  val initialValueMap: ValuesMap = (for (s <- squares) yield s -> digitSet).toMap
}
