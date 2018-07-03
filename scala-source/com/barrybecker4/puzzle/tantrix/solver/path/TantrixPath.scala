// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path

import com.barrybecker4.common.math.MathUtil
import com.barrybecker4.optimization.parameter.{ParameterArray, PermutedParameterArray}
import com.barrybecker4.puzzle.tantrix.generation.RandomPathGenerator
import com.barrybecker4.puzzle.tantrix.model.PathColor.PathColor
import com.barrybecker4.puzzle.tantrix.model.{HexUtil, Tantrix, TantrixBoard, TilePlacement}
import com.barrybecker4.puzzle.tantrix.solver.path.TantrixPath._

import scala.util.Random

object TantrixPath {

  /**
    * There is an ordered primary path if all the successive tiles are connected by the primary path.
    *
    * @return true if there exists a primary path or loop.
    */
  def hasOrderedPrimaryPath(tiles: Seq[TilePlacement], primaryColor: PathColor): Boolean = {
    if (tiles.size < 2) return true
    var lastTile = tiles.head
    for (i <- 1 until tiles.size) {
      val currentTile = tiles(i)
      val outgoing = currentTile.getOutgoingPathLocations(primaryColor)
      if (!outgoing.values.exists(_ == lastTile.location)) return false
      lastTile = currentTile
    }
    true
  }

  private def getPathTilesFromBoard(board: TantrixBoard) = {
    val gen = new RandomPathGenerator(board)
    val path = gen.generateRandomPath
    path.tiles
  }
}

/**
  * A list of tiles representing a primary color path that is used when searching to find a tantrix solution.
  * It has some unique properties.
  * For example, when finding a random neighbor, we select a tile at random and then consider all the
  * 7 other permutations of attaching the current path segments on either side. If any of those give a path
  * with a higher score, then that is what we use for the permuted path.
  * throws IllegalStateException if tiles do not form a primary path.
  *
  * @param tiles ordered path tiles.  The list of tiles that are passed in must be a continuous primary path,
  * but it is not required that it be a loop, or that any of the secondary colors match.
  * @param primaryPathColor primary path color
  */
case class TantrixPath(tiles: Seq[TilePlacement], primaryPathColor: PathColor, rnd: Random = MathUtil.RANDOM)
  extends PermutedParameterArray(rnd) {

  if (!hasOrderedPrimaryPath(tiles, primaryPathColor))
    throw new IllegalStateException("The following " + tiles.size + " tiles must form a primary path :\n" + tiles)

  /**
    * The list of tiles that are passed in must be a continuous primary path,
    * but it is not required that it be a loop, or that any of the secondary colors match.
    *
    * @param tantrix ordered path tiles.
    * @param primaryColor primary color
    */
  def this(tantrix: Tantrix, primaryColor: PathColor, rnd: Random) {
    this(new Pathifier(primaryColor).reorder(tantrix), primaryColor, rnd)
  }

  /**
    * Creates a random path given a board state.
    * @param board placed tiles
    */
  def this(board: TantrixBoard, rnd: Random) = {
    this(getPathTilesFromBoard(board), board.primaryColor, rnd)
  }

  def getFirst: TilePlacement = tiles.head
  def getLast: TilePlacement = tiles.last

  override def getSamplePopulationSize: Int = size * size

  override def copy: TantrixPath = {
    val copy = new TantrixPath(tiles, primaryPathColor, rnd)
    copy.setFitness(this.getFitness)
    copy
  }

  def getTilePlacements: Seq[TilePlacement] = tiles

  /**
    * The start index is not necessarily smaller than the end index.
    *
    * @param startIndex tile to add first
    * @param endIndex   tile to add last
    * @return sub path
    */
  def subPath(startIndex: Int, endIndex: Int): TantrixPath = {
    var pathTiles: Array[TilePlacement] = Array()
    if (startIndex <= endIndex) {
      for (i <- startIndex to endIndex)
        pathTiles :+= this.tiles(i)
    }
    else {
      var i = startIndex
      for (i <- startIndex to endIndex by -1)
        pathTiles :+= this.tiles(i)
    }
    new TantrixPath(pathTiles, primaryPathColor, rnd)
  }

  /**
    * We want to find a potential solution close to the one that we have,
    * with minimal disturbance of the pieces that are already fit, but yet improved from what we had.
    * The main criteria for quality of the path is
    * 1) How close the ends of the path are to each other. Perfection achieved when we have a closed loop.
    * 2) Better if more matching secondary path colors
    * 3) Fewer inner spaces and a bounding box with less area.
    *
    * param radius proportional to the amount of variation. This might be a little difficult for tantrix.
    *               If the radius is small, or there is a closed loop, consider swapping pieces who's
    *               primary path have the same shape. If the radius is large, we could perhaps do random permutation from
    *               more than one spot.
    * @return the random nbr (potential solution).
    */
  override def getRandomNeighbor(radius: Double): PermutedParameterArray = {
    val generator = new PathPermutationGenerator(this, rnd)
    println("r = " + radius)
    generator.getRandomNeighbor(radius)
  }

  /**
    * @return get a completely random solution in the parameter space.
    */
  override def getRandomSample: ParameterArray = {
    val board: TantrixBoard = new TantrixBoard(tiles, primaryPathColor)
    val gen = new RandomPathGenerator(board)
    gen.generateRandomPath
  }

  /**
    * It's a loop if the beginning of the path connects with the end.
    * Having the distance between beginning and end be 0 is a prerequisite and quicker to compute.
    *
    * @return true if the path is a complete loop (ignoring secondary paths)
    */
  def isLoop: Boolean = {
    if (size <= 2) return false
    val distance = getEndPointDistance
    if (distance == 0) {
      val outgoing = getFirst.getOutgoingPathLocations(primaryPathColor)
      if (outgoing.values.exists(_ == getLast.location)) return true
    }
    false
  }

  /**
    * Two adjacent tiles have a distance of 1. If the path length is one, then distance of 1 is returned.
    *
    * @return the distance between the path end points. A distance of 1 does not automatically mean there is a loop.
    */
  def getEndPointDistance: Double = {
    if (tiles.isEmpty) return 1000.0
    if (tiles.size == 1) return 1.0
    val first = getFirst
    val last = getLast
    val end1 = first.location
    val end2 = last.location
    // if they touch return distance of 0
    if (first.getOutgoingPathLocations(primaryPathColor).values.exists(_ == end2) &&
        last.getOutgoingPathLocations(primaryPathColor).values.exists(_ == end1))
      return 0
    HexUtil.distanceBetween(end1, end2)
  }

  override def equals(o: Any): Boolean = {
    //if (this == o) return true
    if (o == null || (getClass ne o.getClass)) return false
    val that = o.asInstanceOf[TantrixPath]
    val isEqual = if (tiles != null) tiles == that.tiles
    else that.tiles == null
    (primaryPathColor eq that.primaryPathColor) && isEqual
  }

  override def hashCode: Int = {
    var result = super.hashCode
    result = 31 * result + (if (tiles != null) tiles.hashCode else 0)
    result = 31 * result + (if (primaryPathColor != null) primaryPathColor.hashCode else 0)
    result
  }

  /**
    * @return the number of parameters in the array.
    */
  override def size: Int = tiles.size
  override def toString: String = tiles.toString

  /** @return the parameters in a string of Comma Separated Values. */
  override def toCSVString: String = toString
}
