// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path.permuting

import com.barrybecker4.puzzle.tantrix.solver.path.PathType
import com.barrybecker4.puzzle.tantrix.solver.path.TantrixPath

import scala.collection.mutable.ListBuffer
import scala.util.Random


object SameTypeTileMixer {
  private val MAX_ITER = 20
}

/**
  * Mix tiles of the same type without changing the primary originalPath.
  * @param pathType one of the 3 path types
  * @param originalPath the path to create permutations of.
  * @author Barry Becker
  */
class SameTypeTileMixer(var pathType: PathType, var originalPath: TantrixPath, rnd: Random = new Random()) {

  def findPermutedPaths: ListBuffer[TantrixPath] = {
    val indices = new TilesOfTypeIndices(pathType, originalPath)
    findPermutedPaths(indices)
  }

  private def findPermutedPaths(indices: TilesOfTypeIndices) = {
    val permutedPaths = new ListBuffer[TantrixPath]
    val permuter = new PathTilePermuter(originalPath)
    if (indices.size == 2) {
      val newOrder: ListBuffer[Int] = indices.list.reverse
      val permutedPath = permuter.permute(newOrder, indices.list)
      permutedPaths.append(permutedPath)
    }
    else if (indices.size > 2) {
      // add the original originalPath for now, to be sure we do not duplicate it, but remove it before returning.
      permutedPaths.append(originalPath)
      val numIter = Math.min(originalPath.size + 1, SameTypeTileMixer.MAX_ITER)

      for (i <- 0 until numIter) {
        val newOrder = rnd.shuffle(indices.list)  // permutations.next()

        //println("valOrder = " + newOrder)
        val permutedPath = permuter.permute(indices.list, newOrder)
        if (!permutedPaths.contains(permutedPath))
          permutedPaths.append(permutedPath)
      }
      permutedPaths.remove(0)
    }
    //println("The number of permuted paths = " + permutedPaths.size)
    permutedPaths
  }
}
