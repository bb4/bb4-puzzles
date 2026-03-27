package com.barrybecker4.puzzle.maze.model

import com.barrybecker4.common.geometry.IntLocation
import com.barrybecker4.math.MathUtil
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite

class StateStackSuite extends AnyFunSuite with BeforeAndAfterEach {

  override def beforeEach(): Unit =
    MathUtil.RANDOM.setSeed(0)

  test("pushMoves adds three states at the same position") {
    val stack = new StateStack(Probabilities(1.0, 1.0, 1.0))
    val pos = IntLocation(3, 4)
    val baseDir = IntLocation(0, 1)
    stack.pushMoves(pos, baseDir, 2)
    assert(!stack.isEmpty)
    val a = stack.pop()
    val b = stack.pop()
    val c = stack.pop()
    assert(a.position == pos && b.position == pos && c.position == pos)
    assert(a.depth == 2 && b.depth == 2 && c.depth == 2)
    assert(stack.isEmpty)
    val moves = Set(a.movement, b.movement, c.movement)
    assert(moves.size == 3)
  }

  test("pushMoves uses relative transforms of base direction") {
    val stack = new StateStack(Probabilities(1.0, 1.0, 1.0))
    val baseDir = IntLocation(1, 0)
    stack.pushMoves(IntLocation(0, 0), baseDir, 0)
    val popped = List(stack.pop(), stack.pop(), stack.pop())
    val xs = popped.map(_.movement.getX).sum
    val ys = popped.map(_.movement.getY).sum
    assert(xs.abs <= 3 && ys.abs <= 3)
  }
}
