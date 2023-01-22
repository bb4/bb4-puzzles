package com.barrybecker4.puzzle.tantrix.solver.verification

import com.barrybecker4.puzzle.tantrix.model.HexTile
import com.barrybecker4.puzzle.tantrix.model.HexUtil
import com.barrybecker4.puzzle.tantrix.solver.path.TantrixPath
import com.barrybecker4.puzzle.tantrix.solver.verification.CompactnessCalculator.DENOMINATOR

case class CompactnessCalculator() {

  /** First add all the tiles to a hash keyed on location.
    * Then for every one of the six sides of each tile, add one if the
    * neighbor is in the hash. Return (num nbrs in hash - 2(numTiles-1)) / numTiles
    *
    * @param path the path to determine compactness of.
    * @return measure of path compactness between 0 and ~1 where 1 is most compact.
    */
  def determineCompactness(path: TantrixPath): Double = {
    val locationHash = path.tiles.map(_.location).toSet
    val numTiles = path.size

    var ct = 0
    for (p <- path.tiles) {
      for (i <- 0 until HexTile.NUM_SIDES) {
        if (locationHash.contains(HexUtil.getNeighborLocation(p.location, i)))
          ct += 1
      }
    }
    (ct / 2.0 - (numTiles - 1)) / DENOMINATOR(numTiles)
  }
}

/**
  * The compactness is calculated as follows:
  * numerator = (<num nbr connections> - 2(<num tiles> - 1))
  * denominator = (<max connections possible> - 2(<num tiles> - 1))
  * numerator / denominator = compactness
  */
object CompactnessCalculator {
  private val DENOMINATOR: Map[Int, Int] = Map(
    1 -> 1, // should be 0, but use 1 to avoid div by 0
    2 -> 1, // should be 0, but use 1 to avoid div by 0
    3 -> 1, // 3 - 2
    4 -> 2, // 5 - 3
    5 -> 3, // 7 - 4
    6 -> 4, // 9 - 5
    7 -> 6, // 12 - 6
    8 -> 7, // 14 - 7
    9 -> 8, // 16 - 8
    10 -> 10, // 19 - 9
    11 -> 11, // 21 - 10
    12 -> 12, // 23 - 11
    13 -> 13, // 25 - 12
    14 -> 14, // 28 - 13
    15 -> 16, // 30 - 14
    16 -> 18, // 33 - 15
    17 -> 19, // 35 - 16
    18 -> 21, // 38 - 17
    19 -> 23, // 41 - 18
    20 -> 24, // 43 - 19
    21 -> 26, // 46 - 20
    22 -> 27, // 48 - 21
    23 -> 29, // 51 - 22
    24 -> 31, // 54 - 23
    25 -> 32, // 56 - 24
    26 -> 33, // 58 - 25
    27 -> 35, // 61 - 26
    28 -> 37, // 64 - 27
    29 -> 38, // 66 - 28
    30 -> 39, // 68 - 29
    31 -> 41, // 71 - 30
    32 -> 43, // 74 - 31
    33 -> 44, // 76 - 32
    34 -> 45, // 78 - 33
    35 -> 47, // 81 - 34
    36 -> 49, // 84 - 35
    37 -> 50, // 86 - 36
    38 -> 51, // 88 - 37
    39 -> 53, // 91 - 38
    40 -> 55, // 94 - 39
    41 -> 56, // 96 - 40
    42 -> 57, // 98 - 41
    43 -> 59, // 101 - 42
    44 -> 61, // 104 - 43
    45 -> 62, // 106 - 44
    46 -> 63, // 108 - 45
    47 -> 65, // 111 - 46
    48 -> 67, // 114 - 47
    49 -> 68, // 116 - 48
    50 -> 69, // 118 - 49
    51 -> 71, // 121 - 50
    52 -> 73, // 124 - 51
    53 -> 74, // 126 - 52
    54 -> 75, // 128 - 53
    55 -> 77, // 131 - 54
    56 -> 79, // 134 - 55
    57 -> 80, // 136 - 56
    58 -> 81, // 138 - 57
    59 -> 83, // 141 - 58
    60 -> 85, // 144 - 59
    61 -> 87, // 147 - 60
    62 -> 88, // 149 - 61
    63 -> 90  // 152 - 62
  )
}
