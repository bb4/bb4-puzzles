// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path.permuting

import com.barrybecker4.common.geometry.Location
import com.barrybecker4.optimization.parameter.PermutedParameterArray
import com.barrybecker4.puzzle.tantrix.model.TilePlacement
import com.barrybecker4.puzzle.tantrix.solver.path.TantrixPath

import scala.collection.JavaConverters._
import scala.collection.mutable.ListBuffer

/**
  * Given a TantrixPath and a pivot tile index, find the permuted paths.
  * Since there are 8 total ways to permute, and the path already represents one of them,
  * the permuter will never return more than 7 valid permuted paths.
  *
  * The list of tiles that are passed in must be a continuous primary path,
  * but it is not required that it be a loop, or that any of the secondary colors match.
  *
  * @param myPath ordered path tiles.
  * @author Barry Becker
  */
class PathPivotPermuter(val myPath: TantrixPath) extends PermutedParameterArray {

  /** The pivot path remains unchanged while the ends change. */
  private var pivotPath: TantrixPath = _

  /**
    * Try the seven cases and take any that are valid for the n squared positions of the pivot path.
    * @return no more than 7 permuted path cases.
    */
  def findAllPermutedPaths: ListBuffer[TantrixPath] = {
    val pathPermutations: ListBuffer[TantrixPath] = new ListBuffer[TantrixPath]()
    val lowerIndexStart = 1
    val upperIndexStop = myPath.size - 2
    for (i <- lowerIndexStart until upperIndexStop) {
      for (j <- upperIndexStop to i) {
        val subPath1 = myPath.subPath(i - 1, 0)
        pivotPath = myPath.subPath(i, j)
        val subPath2 = myPath.subPath(j + 1, myPath.size - 1)
        pathPermutations.append(createPermutedPathList(subPath1, subPath2):_*)
      }
    }
    pathPermutations
  }

  override def setPermutation(indices: List[Int]): PathPivotPermuter = {
    val tilePlacements: Seq[TilePlacement] = indices.map(myPath.tiles(_))
    new PathPivotPermuter(new TantrixPath(tilePlacements, myPath.primaryPathColor))
  }

  /**
 Try the seven cases and take any that are valid.
    * @return no more than 7 permuted path cases.
    */
  def findPermutedPaths(pivotIndex1: Int, pivotIndex2: Int): ListBuffer[TantrixPath] = {
    val lowerIndex = Math.min(pivotIndex1, pivotIndex2)
    val upperIndex = Math.max(pivotIndex1, pivotIndex2)
    val subPath1 = myPath.subPath(lowerIndex - 1, 0)
    pivotPath = myPath.subPath(lowerIndex, upperIndex)
    val subPath2 = myPath.subPath(upperIndex + 1, myPath.size - 1)
    createPermutedPathList(subPath1, subPath2)
  }

  /**
    * @param subPath1 path coming out of pivot tile
    * @param subPath2 the other path coming out of pivot tile.
    * @return list of permuted paths.
    */
  private def createPermutedPathList(subPath1: TantrixPath, subPath2: TantrixPath): ListBuffer[TantrixPath] = {
    val primaryColor = myPath.primaryPathColor
    val swapper = new SubPathSwapper(primaryColor)
    val reverser = new SubPathReverser(primaryColor)
    val firstPivot = pivotPath.getFirst
    val lastPivot = pivotPath.getLast
    val subPath1Reversed = reverser.mutate(firstPivot, subPath1)
    val subPath2Reversed = reverser.mutate(lastPivot, subPath2)
    val subPath1Swapped = swapper.mutate(firstPivot, subPath1)
    val subPath2Swapped = swapper.mutate(lastPivot, subPath2)
    val subPath1RevSwapped = swapper.mutate(firstPivot, subPath1Reversed)
    val subPath2RevSwapped = swapper.mutate(lastPivot, subPath2Reversed)
    val pathPermutations = new ListBuffer[TantrixPath]()
    addIfDefineed(createPermutedPath(subPath1, subPath2Reversed), pathPermutations)
    addIfDefineed(createPermutedPath(subPath1Reversed, subPath2), pathPermutations)
    addIfDefineed(createPermutedPath(subPath1Reversed, subPath2Reversed), pathPermutations)
    addIfDefineed(createPermutedPath(subPath2Swapped, subPath1Swapped), pathPermutations)
    addIfDefineed(createPermutedPath(subPath2Swapped, subPath1RevSwapped), pathPermutations)
    addIfDefineed(createPermutedPath(subPath2RevSwapped, subPath1Swapped), pathPermutations)
    addIfDefineed(createPermutedPath(subPath2RevSwapped, subPath1RevSwapped), pathPermutations)
    pathPermutations
  }

  private def addIfDefineed(path: Option[TantrixPath], pathPermutations: ListBuffer[TantrixPath]) {
    if (path.isDefined) pathPermutations.append(path.get)
  }

  /** Combine supPath1 and subPath2 to make a new path. SubPath1 needs to be reversed when adding.
    * @param subPath1 first path
    * @param subPath2 second path
    * @return null if the resulting permuted path is not valid (i.e. has overlaps)
    */
  private def createPermutedPath(subPath1: TantrixPath, subPath2: TantrixPath) = {
    // add tiles from the first path in reverse order
    val tiles: ListBuffer[TilePlacement] = new ListBuffer[TilePlacement]()
    for (p <- subPath1.tiles) {
      tiles.prepend(p)
    }
    tiles.append(pivotPath.tiles:_*)
    tiles.append(subPath2.tiles:_*)
    val path: Option[TantrixPath] = if (isValid(tiles)) {
      assert(TantrixPath.hasOrderedPrimaryPath(tiles, myPath.primaryPathColor),
        "out of order path tiles \nsubpath1" + subPath1 + "\npivot=" + pivotPath + "\nsubpath2=" + subPath2 + "\norigPath=" + myPath)
      Some(new TantrixPath(tiles, myPath.primaryPathColor))
    } else None
    path
  }

  /**
    * @param tiles tiles to check
    * @return true if no overlapping tiles.
    */
  private def isValid(tiles: Seq[TilePlacement]): Boolean = {
    var tileLocations: Set[Location] = Set()
    for (placement <- tiles) {
      if (tileLocations.contains(placement.location)) return false
      tileLocations += placement.location
    }
    true
  }
}