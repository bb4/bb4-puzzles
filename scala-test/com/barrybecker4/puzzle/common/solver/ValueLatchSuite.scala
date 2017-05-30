// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.common.solver

import com.barrybecker4.common.concurrency.ThreadUtil
import org.scalatest.FunSuite


/**
  * The ValueLatch blocks until the its result has been set.
  */
class ValueLatchSuite extends FunSuite {

  private var valueLatch: ValueLatch[Int] = _

  test("ValueLatchResult") {
    valueLatch = new ValueLatch[Int]
    computeResult(100)
    assertResult(4950) { valueLatch.getValue }
  }

  /**
    * Only the first result is retained. Successive results that are set are ignored.
    * Sometimes fails with 190 instead of 45.
    */
  test("ValueLatchMultipleSets") {
    valueLatch = new ValueLatch[Int]
    computeResult(10)
    ThreadUtil.sleep(50)
    computeResult(20)
    assertResult(45) { valueLatch.getValue }
  }

  /** computes the value latch result in a separate thread */
  @throws[InterruptedException]
  private[solver] def computeResult(num: Int) = {
    new Thread(new Worker(num)).start()
  }

  class Worker(var num: Int) extends Runnable {
    override def run(): Unit = {
      valueLatch.setValue((0 until num).sum)
    }
  }

}
