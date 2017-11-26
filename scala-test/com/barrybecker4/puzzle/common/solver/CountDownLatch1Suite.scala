package com.barrybecker4.puzzle.common.solver

import com.barrybecker4.common.concurrency.ThreadUtil
import org.junit.Test
import java.util.concurrent.CountDownLatch

import org.scalatest.{BeforeAndAfter, FunSuite}


/**
  * Here is a pair of classes in which a group of worker threads use two countdown latches:
  * <ul>
  * <li>The first is a start signal that prevents any worker from proceeding
  * until the driver is ready for them to proceed;
  * <li>The second is a completion signal that allows the driver to wait
  * until all workers have completed.
  * </ul>
  */
class CountDownLatch1Suite extends FunSuite with BeforeAndAfter {
  private var counter = 0

  before {
    counter = 0
  }

  test("DriverWith0Threads") {
    runDriver(0)
    assertResult(2) { counter }
  }

  test("DriverWith1Threads") {
    runDriver(1)
    assertResult(12) { counter }
  }

  test("DriverWith2Threads") {
    runDriver(2)
    assertResult(22) { counter }
  }

  /* sometimes fails
  test("DriverWith10Threads(") {
    runDriver(10)
    ThreadUtil.sleep(20)
    assertResult(102) { counter }
  }*/

  private def doSomethingElse(): Unit = {
    //System.out.println("Something else... counter=" + counter)
    counter += 1
  }

  private def runDriver(numThreads: Int): Unit = {
    val startSignal = new CountDownLatch(1)
    val doneSignal = new CountDownLatch(numThreads)
    var i = 0
    while ( {
      i < numThreads
    }) { // create and start threads
      new Thread(new Worker(startSignal, doneSignal)).start()

      {
        i += 1; i
      }
    }
    doSomethingElse() // don't let it run yet

    startSignal.countDown() // let all threads proceed

    doSomethingElse()
    doneSignal.await() // wait for all to finish

  }

  private[solver] class Worker(val startSignal: CountDownLatch, val doneSignal: CountDownLatch) extends Runnable {
    override def run(): Unit = {
      try {
        startSignal.await()
        doWork()
        doneSignal.countDown()
      } catch {
        case ex: InterruptedException =>
      }
    }

    private[solver] def doWork(): Unit = {
      //System.out.println("Before working. counter=" + counter)
      counter += 10
      //System.out.println("...  After working. counter=" + counter)
    }
  }

}
