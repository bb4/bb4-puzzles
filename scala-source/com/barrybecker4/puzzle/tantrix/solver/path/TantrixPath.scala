// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path

import com.barrybecker4.math.MathUtil
import com.barrybecker4.optimization.parameter.types.{IntegerParameter, Parameter}
import com.barrybecker4.optimization.parameter.{ParameterArray, PermutedParameterArray}
import com.barrybecker4.puzzle.tantrix.generation.RandomPathGenerator
import com.barrybecker4.puzzle.tantrix.model.PathColor
import com.barrybecker4.puzzle.tantrix.model.{HexUtil, Rotation, Tantrix, TantrixBoard, TilePlacement}
import com.barrybecker4.puzzle.tantrix.solver.path.TantrixPath._
import scala.util.Random


object TantrixPath {

  /** Stable tile ids for permutation / crossover: sort by location and rotation so both parents agree. */
  private[tantrix] def canonicalOrderedTiles(tiles: Seq[TilePlacement]): IndexedSeq[TilePlacement] =
    tiles.toIndexedSeq.sortBy(tp => (tp.location.row, tp.location.col, tp.rotation.ordinal))

  /** A number that is in the same order as the (row, column, rotation) of the placement, so that sorting by it gives
    * the canonical order without creating a tuple for every comparison. Columns need to be less than 2^20 away from 0.
    */
  private def canonicalSortKey(tp: TilePlacement): Long =
    (tp.location.row.toLong << 32) + ((tp.location.col + (1 << 20)).toLong << 4) + tp.rotation.ordinal

  /** One [[IntegerParameter]] per path position; values are ids from [[canonicalOrderedTiles]]. */
  private def permutationParamsForTiles(tiles: Seq[TilePlacement]): IndexedSeq[Parameter] = {
    // The id is the rank of the tile in canonical order. Ranking positions directly, rather than looking up
    // tiles in a map, avoids hashing every tile. This runs for every new path, and there are a lot of those.
    val keys = tiles.map(canonicalSortKey).toArray
    val positionsInCanonicalOrder = keys.indices.sortBy(keys(_))
    val idOfPosition = new Array[Int](keys.length)
    for (id <- positionsInCanonicalOrder.indices) idOfPosition(positionsInCanonicalOrder(id)) = id
    val maxId = keys.length - 1
    idOfPosition.iterator.map(id => IntegerParameter(id, 0, maxId, "tile"): Parameter).toIndexedSeq
  }

  /** There is an ordered primary path if all the successive tiles are connected by the primary path.
    * @return true if there exists a primary path or loop.
    */
  def hasOrderedPrimaryPath(tiles: Seq[TilePlacement], primaryColor: PathColor): Boolean = {
    val iterator = tiles.iterator
    var ordered = true
    if (iterator.hasNext) {
      var previous = iterator.next()
      while (ordered && iterator.hasNext) {
        val current = iterator.next()
        ordered = current.hasPathTo(primaryColor, previous.location)
        previous = current
      }
    }
    ordered
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
  * @author Barry Becker
  */
class TantrixPath(val tiles: Seq[TilePlacement], val primaryPathColor: PathColor, val desiredLength: Int,
                  rnd: Random = MathUtil.RANDOM)
    extends PermutedParameterArray(TantrixPath.permutationParamsForTiles(tiles), rnd) {

  assert(desiredLength >= tiles.length)
  if (!hasOrderedPrimaryPath(tiles, primaryPathColor))
    throw new IllegalStateException(s"The following ${tiles.size} tiles must form a primary path ($primaryPathColor) :\n$tiles")

  /** The list of tiles that are passed in must be a continuous primary path,
    * but it is not required that it be a loop, or that any of the secondary colors match.
    * @param tantrix ordered path tiles.
    * @param primaryColor primary color
    */
  def this(tantrix: Tantrix, primaryColor: PathColor, desiredLength: Int, rnd: Random) = {
    this(new Pathifier(primaryColor).reorder(tantrix), primaryColor, desiredLength, rnd)
  }

  /** Creates a random path given a board state.
    * @param board placed tiles
    */
  def this(board: TantrixBoard, rnd: Random) = {
    this(getPathTilesFromBoard(board), board.primaryColor, board.numTiles, rnd)
  }

  def getFirst: TilePlacement = tiles.head
  def getLast: TilePlacement = tiles.last
  override def getSamplePopulationSize: Int = size * size

  /**
    * Exhaustive global permutation sampling ([[com.barrybecker4.optimization.parameter.sampling.PermutedGlobalSampler]])
    * calls [[com.barrybecker4.optimization.parameter.PermutedParameterArray.setPermutation]], which would otherwise
    * return a plain [[com.barrybecker4.optimization.parameter.PermutedParameterArray]] and break Tantrix optimizees.
    */
  override def setPermutation(indices: List[Int]): PermutedParameterArray =
    rebuildAfterOrderCrossover(indices.map(get).toIndexedSeq, rnd)

  /** Multistart hill climbing must not replace this with a raw [[com.barrybecker4.optimization.parameter.PermutedParameterArray]]. */
  override def forkWithRnd(newRnd: Random): PermutedParameterArray =
    new TantrixPath(tiles, primaryPathColor, desiredLength, newRnd)

  /** The start index is not necessarily smaller than the end index.
    * @param startIndex tile to add first
    * @param endIndex   tile to add last
    * @return sub path
    */
  def subPath(startIndex: Int, endIndex: Int): TantrixPath = {
    val pathTiles =
      if (startIndex <= endIndex) tiles.slice(startIndex, endIndex + 1)
      else tiles.slice(endIndex, startIndex + 1).reverse
    new TantrixPath(pathTiles, primaryPathColor, desiredLength, rnd)
  }

  /** We want to find a potential solution close to the one that we have,
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
    generator.getRandomNeighbor(radius)
  }

  /** @return a completely random path through the same tiles (not just this path again).
    * The random path generator needs a board where the first tile is placed and all the others are still unplaced.
    * If it were given a board with every tile already placed, it would have nothing to randomize, and the
    * global samples would all be the same path.
    */
  override def getRandomSample: ParameterArray = {
    val hexTiles = tiles.map(_.tile)
    val first = TilePlacement(hexTiles.head, TantrixBoard.INITIAL_LOCATION, Rotation.ANGLE_0)
    val board = TantrixBoard(new Tantrix(Seq(first)), primaryPathColor, hexTiles.tail, desiredLength)
    new RandomPathGenerator(board, rnd).generateRandomPath
  }

  override def rebuildAfterOrderCrossover(childParams: IndexedSeq[Parameter], rnd: Random): PermutedParameterArray = {
    val canonical = TantrixPath.canonicalOrderedTiles(tiles)
    val newTiles = childParams.map(p => canonical(p.getValue.toInt))
    try new TantrixPath(newTiles, primaryPathColor, desiredLength, rnd)
    catch {
      case _: IllegalStateException =>
        new TantrixPath(tiles, primaryPathColor, desiredLength, rnd)
    }
  }

  /** It's a loop if the beginning of the path connects with the end.
    * Having the distance between beginning and end be 0 is a prerequisite and quicker to compute.
    * @return true if the path is a complete loop (ignoring secondary paths)
    */
  def isLoop: Boolean =
    size > 2 && getEndPointDistance == 0 && getFirst.hasPathTo(primaryPathColor, getLast.location)

  /** Two adjacent tiles have a distance of 1. If the path length is one, then distance of 1 is returned.
    * @return the distance between the path end points. A distance of 1 does not automatically mean there is a loop.
    */
  def getEndPointDistance: Double = tiles.size match {
    case 0 => 1000.0
    case 1 => 1.0
    case _ => if (endsConnectViaPrimaryPath) 0.0 else HexUtil.distanceBetween(getFirst.location, getLast.location)
  }

  /** @return true if both end tiles have a primary path leading directly to the other end tile. */
  private def endsConnectViaPrimaryPath: Boolean =
    getFirst.hasPathTo(primaryPathColor, getLast.location) && getLast.hasPathTo(primaryPathColor, getFirst.location)

  /**
    * The parameters are ranks of the tile placements within this particular path, so different paths (different
    * placements or rotations) can have the same parameter values. The candidate is the tiles in path order.
    * Genetic search compares this when it removes duplicates.
    */
  override def candidateIdentity: Any = (primaryPathColor, tiles.toList)

  override def equals(o: Any): Boolean = o match {
    case that: TantrixPath =>
      (this eq that) || (primaryPathColor eq that.primaryPathColor) && tiles == that.tiles
    case _ => false
  }

  /** Cached because paths are put in sets, and hashing all of the tiles each time is slow. */
  override lazy val hashCode: Int =
    31 * tiles.hashCode + primaryPathColor.hashCode

  override def toString: String = tiles.toString

  /** @return the parameters in a string of Comma Separated Values. */
  override def toCSVString: String = toString
}
