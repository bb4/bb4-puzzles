// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path

import com.barrybecker4.puzzle.tantrix.PathTstUtil
import org.scalatest.funsuite.AnyFunSuite

import scala.collection.mutable.ListBuffer
import scala.util.Random

class PathSelectorSuite extends AnyFunSuite {

  private val eval = PathEvaluator()

  test("selectPath single candidate returns it") {
    val sel = new PathSelector(eval, new Random(0))
    val buf = ListBuffer(PathTstUtil.LOOP_PATH3)
    assert(sel.selectPath(buf) eq PathTstUtil.LOOP_PATH3)
  }

  test("selectPath deterministic choice for two candidates with fixed RNG") {
    val sel = new PathSelector(eval, new Random(42))
    // Prefer better-fitness path first in buffer; roulette still picks one of the two deterministically.
    val a = PathTstUtil.LOOP_PATH3
    val b = PathTstUtil.NON_LOOP_PATH3
    val first = sel.selectPath(ListBuffer(a, b))
    val second = sel.selectPath(ListBuffer(b, a))
    assert(first == a || first == b)
    assert(second == a || second == b)
    // Same RNG stream: same decision for same buffer order
    val sel2 = new PathSelector(eval, new Random(42))
    assert(sel2.selectPath(ListBuffer(a, b)) == first)
  }

  test("selectPath with many duplicate high-quality paths returns one of them") {
    val sel = new PathSelector(eval, new Random(7))
    val p = PathTstUtil.LOOP_PATH4
    val buf = ListBuffer(p, PathTstUtil.LOOP_PATH4, PathTstUtil.LOOP_PATH4)
    val chosen = sel.selectPath(buf)
    assert(chosen.primaryPathColor == p.primaryPathColor)
    assert(chosen.size == p.size)
  }

  /** A random number generator whose nextDouble always returns the same value. */
  private class FixedDrawRandom(draw: Double) extends Random(0) {
    override def nextDouble(): Double = draw
  }

  private def selectWithDraw(draw: Double, paths: TantrixPath*): TantrixPath =
    new PathSelector(eval, new FixedDrawRandom(draw)).selectPath(ListBuffer.from(paths))

  test("selectPath with equally good candidates picks according to where the roulette wheel lands") {
    val first = PathTstUtil.LOOP_PATH3
    val second = first.subPath(0, 2) // equal to the first, but not the same instance
    assert(eval.evaluateFitness(first) < PathEvaluator.FITNESS_RANGE)
    assert(eval.evaluateFitness(first) == eval.evaluateFitness(second))

    assert(selectWithDraw(0.25, first, second) eq first)
    assert(selectWithDraw(0.75, first, second) eq second)
    assert(selectWithDraw(0.999999, first, second) eq second)
  }

  test("selectPath does not fail when the random draw is exactly zero") {
    val first = PathTstUtil.LOOP_PATH3
    val second = first.subPath(0, 2)
    assert(selectWithDraw(0.0, first, second) eq first)
  }

  test("selectPath requires at least one path") {
    intercept[IllegalArgumentException] {
      new PathSelector(eval, new Random(0)).selectPath(ListBuffer.empty[TantrixPath])
    }
  }
}
