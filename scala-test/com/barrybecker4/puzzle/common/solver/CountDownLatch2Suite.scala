package com.barrybecker4.puzzle.common.solver

import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.BeforeAndAfter



/**
  * A typical usage is to divide a problem into N parts,
  * describe each part with a Runnable that executes that portion and
  * counts down on the latch, and queue all the Runnables to an Executor.
  * When all sub-parts are complete, the coordinating thread will be able to pass through await.
  * (When threads must repeatedly count down in this way, instead use a CyclicBarrier)
  */
class CountDownLatch2Suite extends AnyFunSuite with BeforeAndAfter {
  private var counter: AtomicInteger = _

  before {
    counter =  new AtomicInteger(0)
  }

  test("DriverWith1Threads") {
    runDriver(1)
    assertResult(10) { counter.get }
  }

  test("DriverWith3Threads") {
    runDriver(3)
    assertResult(30) { counter.get }
  }

  /* fails for some odd reason when run from cmd line, but not in IDE */
  test("DriverWith2Threads("){
    runDriver(2)
    assertResult(20) { counter.get }
  }

  test("DriverWith5Threads") {
    runDriver(5)
    assertResult(50) { counter.get }
  }

  test("DriverWith10Threads") {
    runDriver(10)
    assertResult(100) { counter.get }
  }

  test("DriverWith100Threads") {
    runDriver(100)
    assertResult(1000) { counter.get }
  }

  private def runDriver(numThreads: Int): Unit = {
    val doneSignal = new CountDownLatch(numThreads)
    val e = Executors.newFixedThreadPool(numThreads)
    // create and start threads
    var i = 0
    while ( {
      i < numThreads
    }) {
      e.execute(new WorkerRunnable(doneSignal, i))

      {
        i += 1; i
      }
    }
    // wait for all workers to finish
    doneSignal.await()
    //System.out.println("Finished everything.")
  }

  private[solver] class WorkerRunnable(val doneSignal: CountDownLatch, val i: Int) extends Runnable {
    override def run(): Unit = {
      doWork(i)
      doneSignal.countDown()
    }

    private[solver] def doWork(i: Int) = {
      //System.out.print("Before working on task " + i + ". counter=" + counter)
      counter.addAndGet(10)
      //System.out.println("...  After working. counter=" + counter)
    }
  }

}
