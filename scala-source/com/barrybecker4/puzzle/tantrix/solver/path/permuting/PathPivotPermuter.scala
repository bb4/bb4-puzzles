// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path.permuting

import com.barrybecker4.optimization.parameter.PermutedParameterArray
import com.barrybecker4.puzzle.tantrix.model.TilePlacement
import com.barrybecker4.puzzle.tantrix.solver.path.TantrixPath
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

  override def setPermutation(indices: List[Int]): PathPivotPermuter = {
    val tilePlacements: Seq[TilePlacement] = indices.map(myPath.tiles(_))
    new PathPivotPermuter(new TantrixPath(tilePlacements, myPath.primaryPathColor, myPath.desiredLength))
  }

  /** Try the seven cases and take any that are valid.
    * @return no more than 7 permuted path cases.
    */
  def findPermutedPaths(pivotIndex1: Int, pivotIndex2: Int): ListBuffer[TantrixPath] = {
    val lowerIndex = Math.min(pivotIndex1, pivotIndex2)
    val upperIndex = Math.max(pivotIndex1, pivotIndex2)
    val subPath1 = myPath.subPath(lowerIndex - 1, 0)
    val pivotPath = myPath.subPath(lowerIndex, upperIndex)
    val subPath2 = myPath.subPath(upperIndex + 1, myPath.size - 1)
    createPermutedPathList(subPath1, pivotPath, subPath2)
  }

  /**
    * @param subPath1 path coming out of pivot tile
    * @param subPath2 the other path coming out of pivot tile.
    * @return list of permuted paths.
    */
  private def createPermutedPathList(subPath1: TantrixPath, pivotPath: TantrixPath, subPath2: TantrixPath)
      : ListBuffer[TantrixPath] =
    ListBuffer.from(
      subPathCombinations(subPath1, pivotPath, subPath2).flatMap { case (before, after) =>
        createPermutedPath(before, pivotPath, after)
      }
    )

  /** The 7 ways (in a fixed order) to attach the two sub paths to the pivot, other than how they are already attached.
    * Each sub path may be reversed (keeping its end next to the pivot), or swapped to the pivot's other primary
    * path (and then optionally reversed). When swapped, the sub path that was on the other side comes first.
    * @return pairs of (path that will be placed before the pivot, path that will be placed after the pivot)
    */
  private def subPathCombinations(subPath1: TantrixPath, pivotPath: TantrixPath, subPath2: TantrixPath)
      : Seq[(TantrixPath, TantrixPath)] = {
    val reverser = new SubPathReverser(myPath.primaryPathColor)
    val swapper = new SubPathSwapper(myPath.primaryPathColor)
    val (firstPivot, lastPivot) = (pivotPath.getFirst, pivotPath.getLast)
    val subPath1Reversed = reverser.mutate(firstPivot, subPath1)
    val subPath2Reversed = reverser.mutate(lastPivot, subPath2)
    val subPath1Swapped = swapper.mutate(firstPivot, subPath1)
    val subPath2Swapped = swapper.mutate(lastPivot, subPath2)
    val subPath1RevSwapped = swapper.mutate(firstPivot, subPath1Reversed)
    val subPath2RevSwapped = swapper.mutate(lastPivot, subPath2Reversed)
    Seq(
      (subPath1, subPath2Reversed),
      (subPath1Reversed, subPath2),
      (subPath1Reversed, subPath2Reversed),
      (subPath2Swapped, subPath1Swapped),
      (subPath2Swapped, subPath1RevSwapped),
      (subPath2RevSwapped, subPath1Swapped),
      (subPath2RevSwapped, subPath1RevSwapped)
    )
  }

  /** Combine the sub paths on either side of the pivot to make a new path.
    * The first sub path leads away from the pivot, so it needs to be reversed when adding.
    * @param subPath1 path that comes before the pivot
    * @param subPath2 path that comes after the pivot
    * @return `None` if the resulting permuted path is not valid (i.e. has overlaps). The TantrixPath constructor
    *         verifies that the tiles form a continuous primary path.
    */
  private def createPermutedPath(subPath1: TantrixPath, pivotPath: TantrixPath, subPath2: TantrixPath)
      : Option[TantrixPath] = {
    val tiles = subPath1.tiles.reverse ++ pivotPath.tiles ++ subPath2.tiles
    Option.when(isValid(tiles))(new TantrixPath(tiles, myPath.primaryPathColor, myPath.desiredLength))
  }

  /**
    * @param tiles tiles to check
    * @return true if no overlapping tiles, and each one leads to the next by the primary path.
    */
  private def isValid(tiles: Seq[TilePlacement]): Boolean =
    tiles.map(_.location).toSet.size == tiles.size &&
      TantrixPath.hasOrderedPrimaryPath(tiles, myPath.primaryPathColor)
}

