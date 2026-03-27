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

  /** None = not yet completed; Some(inner) = completed (inner None means no result, e.g. no solution). */
  private var result: Option[Option[T]] = None

  /** When this reaches 0, the result is ready. */
  final private val done = new CountDownLatch(1)

  /** @return true when the result value has been set. */
  def isSet: Boolean = done.getCount == 0

  /** @param newValue the value to set (as long as it was not already set). */
  def setValue(newValue: T): Unit = {
    if (!isSet) {
      result = Some(Some(newValue))
      done.countDown()
    }
  }

  /**
    * Complete the latch with no value (e.g. search exhausted with no solution).
    * Idempotent with [[setValue]]: only the first completion wins.
    */
  def completeWithoutValue(): Unit = {
    if (!isSet) {
      result = Some(None)
      done.countDown()
    }
  }

  /**
    * This blocks until the final value is available.
    * throws InterruptedException if interrupted
    * @return [[Some]] if a value was supplied, [[None]] if completed via [[completeWithoutValue]]
    */
  @throws[InterruptedException]
  def getValue: Option[T] = {
    done.await()
    // CountDownLatch release happens-before await return; read of result is safe here.
    result match {
      case Some(inner) => inner
      case None =>
        throw new IllegalStateException("Latch completed but result was not set")
    }
  }
}
