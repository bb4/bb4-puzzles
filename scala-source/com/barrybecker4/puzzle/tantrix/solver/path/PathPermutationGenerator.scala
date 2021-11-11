// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path

import com.barrybecker4.puzzle.tantrix.solver.path.PathPermutationGenerator.CACHE
import com.barrybecker4.puzzle.tantrix.solver.path.permuting.{PathPivotPermuter, SameTypeTileMixer}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.util.Random


object PathPermutationGenerator {
  private val CACHE: mutable.Set[TantrixPath] = mutable.Set()
}

/**
  * When finding a random neighbor, we select a tile at random and then consider all the
  * 7 other permutations of attaching the current path segments on either side. If any of those give a path
  * with a higher score, that is what we use for the permuted path.
  * Includes a cache to avoid trying the same random path multiple times.
  *
  * @param path to permute
  * @author Barry Becker
  */
class PathPermutationGenerator private[path](var path: TantrixPath, rnd: Random = new Random()) {

  /**
    * We want to find a potential solution close to the one that we have,
    * with minimal disturbance of the pieces that are already fit, but yet improved from what we had.
    * The main criteria for quality of the path is
    * 1) How close the ends of the path are to each other. Perfection achieved when we have a closed loop.
    * 2) Better if more matching secondary path colors
    * 3) Fewer inner spaces and a bbox with less area.
    *
    * @param radius proportional to the amount of variation. This might be a little difficult for tantrix.
    *               If the radius is small, or there is a closed loop, consider swapping pieces who's
    *               primary path have the same shape. If the radius is large, we could perhaps do random permutation from
    *               more than one spot.
    * @return the random nbr (potential solution).
    */
  private[path] def getRandomNeighbor(radius: Double) = {
    val pathPermutations = findPermutedPaths(radius)
    assert(pathPermutations.nonEmpty, "Could not find any permutations of " + this)
    new PathSelector(rnd).selectPath(pathPermutations)
  }

  /**
    * Try the seven permutation case, and take the one that works best.
    *
    * @param radius the larger the radius the wider the variance of the random paths returned.
    * @return 7 permuted path cases.
    */
  private def findPermutedPaths(radius: Double): ListBuffer[TantrixPath] = {
    val permutedPaths: ListBuffer[TantrixPath] = new ListBuffer[TantrixPath]
    val permuter = new PathPivotPermuter(path)
    val tiles = path.tiles
    val numTiles = path.size
    if (radius >= 0.4) {
      for (i <- 1 until numTiles - 1) {
        addAllPermutedPaths(permuter.findPermutedPaths(i, i), permutedPaths)
      }
    }
    else if (radius >= 0.1) {
      // to avoid trying too many paths, increment by something more than one if many tiles.
      val inc = 1 + path.size / 4
      // n^2 * 7 permuted paths will be added.
      var pivot1 = 1
      while (pivot1 < numTiles - 1) {
        var pivot2 = pivot1
        while (pivot2 < numTiles - 1) {
          addAllPermutedPaths(permuter.findPermutedPaths(pivot1, pivot2), permutedPaths)
          pivot2 += rand(inc)
        }
        pivot1 += rand(inc)
      }
    }
    else if (permutedPaths.isEmpty) {
      val types = rnd.shuffle(PathType.values.toList)
      val typeIter = types.iterator
      while (typeIter.hasNext) {
        val mixer = new SameTypeTileMixer(typeIter.next(), path, rnd)
        addAllPermutedPaths(mixer.findPermutedPaths, permutedPaths)
      }
    }
    // as a last resort use this without checking for it in the cache.
    if (permutedPaths.isEmpty) {
      var paths: Option[ListBuffer[TantrixPath]] = None
      while (paths.isEmpty) {
        val pivotIndex1 = 1 + rnd.nextInt(tiles.size - 2)
        val pivotIndex2 = 1 + rnd.nextInt(tiles.size - 2)
        paths = Some(permuter.findPermutedPaths(pivotIndex1, pivotIndex2))
        println("paths unexpectedly empty! when p1=" + pivotIndex1 + " p2=" + pivotIndex2)
      }
      return paths.get
    }
    permutedPaths
  }

  private def rand(inc: Int) = if (inc <= 1) 1
  else 1 + rnd.nextInt(inc)

  /**
    * Check first that it is not in our global cache of paths already considered.
    */
  private def addAllPermutedPaths(pathsToAdd: ListBuffer[TantrixPath],
                                  permutedPaths: ListBuffer[TantrixPath]): Unit = {
    for (p <- pathsToAdd) {
      addPermutedPath(p, permutedPaths)
    }
  }

  /**
    * Check first that it is not in our global cache of paths already considered.
    */
  private def addPermutedPath(pathToAdd: TantrixPath, permutedPaths: ListBuffer[TantrixPath]): Unit = {
    if (!CACHE.contains(pathToAdd)) {
      permutedPaths.append(pathToAdd)
      CACHE(pathToAdd)
    }
  }
}
