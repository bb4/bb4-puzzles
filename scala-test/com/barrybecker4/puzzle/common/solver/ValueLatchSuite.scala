// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.common.solver

import org.scalatest.funsuite.AnyFunSuite
import scala.compiletime.uninitialized


/**
  * The ValueLatch blocks until the its result has been set.
  */
class ValueLatchSuite extends AnyFunSuite {

  private var valueLatch: ValueLatch[Int] = uninitialized

  test("ValueLatchResult") {
    valueLatch = new ValueLatch[Int]
    computeResult(100)
    assertResult(Some(4950)) { valueLatch.getValue }
  }

  test("completeWithoutValue yields None") {
    val latch = new ValueLatch[String]
    latch.completeWithoutValue()
    assertResult(None)(latch.getValue)
  }

  test("setValue and completeWithoutValue are mutually exclusive first wins") {
    val a = new ValueLatch[Int]
    a.setValue(42)
    a.completeWithoutValue()
    assertResult(Some(42))(a.getValue)

    val b = new ValueLatch[Int]
    b.completeWithoutValue()
    b.setValue(99)
    assertResult(None)(b.getValue)
  }

  /**
    * Only the first result is retained. Successive results that are set are ignored.
    * Sometimes fails with 190 instead of 45.
    *
  test("ValueLatchMultipleSets") {
    valueLatch = new ValueLatch[Int]
    computeResult(10)
    ThreadUtil.sleep(50)
    computeResult(20)
    assertResult(45) { valueLatch.getValue }
  }*/

  /** computes the value latch result in a separate thread */
  @throws[InterruptedException]
  private[solver] def computeResult(num: Int): Unit = {
    new Thread(new Worker(num)).start()
  }

  class Worker(var num: Int) extends Runnable {
    override def run(): Unit = {
      valueLatch.setValue((0 until num).sum)
    }
  }

}
