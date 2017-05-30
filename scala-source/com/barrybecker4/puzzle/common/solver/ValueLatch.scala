package com.barrybecker4.puzzle.common.solver

import java.util.concurrent.CountDownLatch

/**
  * Result-bearing latch used by ConcurrentPuzzleSolver.
  * The value cannot be retrieved until it is set. It blocks until set.
  * ThreadSafe
  *
  * @author Brian Goetz
  * @author Tim Peierls
  */
class ValueLatch[T] {

  /** Holds the final result, when it's ready */
  private var value: T = _

  /** When this reaches 0, the result is ready. */
  final private val done = new CountDownLatch(1)

  /** @return true when the result value has been set. */
  def isSet: Boolean = done.getCount == 0

  /** @param newValue the value to set (as long as it was not already set). */
  def setValue(newValue: T): Unit = {
    if (!isSet) {
      value = newValue
      done.countDown()
    }
  }

  /**
    * This blocks until the final value is available.
    *
    * @return the final computed value
    * @throws InterruptedException if interrupted
    */
  @throws[InterruptedException]
  def getValue: T = {
    done.await()
    done synchronized
      value
  }
}

