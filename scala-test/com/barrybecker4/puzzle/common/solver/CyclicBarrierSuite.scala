package com.barrybecker4.puzzle.common.solver

import com.barrybecker4.common.concurrency.ThreadUtil
import java.util.Random
import java.util.concurrent.BrokenBarrierException
import java.util.concurrent.CyclicBarrier

import org.junit.Assert.assertEquals
import CyclicBarrierSuite._
import org.scalatest.FunSuite
import org.scalactic.{Equality, TolerantNumerics}


/**
  * <p>A <tt>CyclicBarrier</tt> supports an optional {@link Runnable} command
  * that is run once per barrier point, after the last thread in the party
  * arrives, but before any threads are released.
  * This <em>barrier action</em> is useful
  * for updating shared-state before any of the parties continue.
  * <pre>
  */
object CyclicBarrierSuite {
  private val SMALL_MATRIX = Array[Array[Float]](Array[Float](1.0f, 1.0f), Array[Float](2.0f, 2.0f))
  private val MATRIX = Array[
    Array[Float]](Array[Float](1.0f, 1.0f, 3.1f, 4.2f, 5.0f, 1.0f),
    Array[Float](1.0f, 0.0f, 2.1f, 1.2f, 1.0f, 0.0f),
    Array[Float](1.0f, 1.0f, 0.1f, 0.2f, 1.0f, 2.1f),
    Array[Float](1.0f, 0.0f, 3.1f, 0.2f, 5.0f, 1.1f),
    Array[Float](1.0f, 1.0f, 3.1f, 1.2f, 0.0f, 0.0f),
    Array[Float](1.0f, 4.0f, 3.1f, 1.2f, 2.0f, 1.1f)
  )
  private val BIG_MATRIX = Array.ofDim[Float](100, 100)

  val rand = new Random(1)
  for (i <- BIG_MATRIX.indices ) {
    for (j <- BIG_MATRIX.indices) {
      BIG_MATRIX(i)(j) = rand.nextFloat
    }
  }
}

class CyclicBarrierSuite extends FunSuite {

  val epsilon = 1e-3f
  implicit val doubleEq: Equality[Double] = TolerantNumerics.tolerantDoubleEquality(epsilon)

  test("SumSmallMatrix") {
    val solver = new Solver(SMALL_MATRIX)
    //assert(6.0 === solver.getTotal)
    assertResult(6.0) { solver.getTotal }
  }

  test("SumMatrix") {
    val solver = new Solver(MATRIX)
    assert(55.1 === solver.getTotal)
    //assertEquals("Unexpected grand total", 55.1, solver.getTotal, 0.0001)
  }

  test("SumBigMatrix") {
    val solver = new Solver(BIG_MATRIX)
    assert(5023.338 === solver.getTotal)
    assertEquals("Unexpected grand total", 5023.338, solver.getTotal, 0.01)
  }

  /**
    * All the rows of a matrix are summed into a 1D totals array.
    * Then in the barrier thread (that runs after all rows have been summed)
    * the row totals are totaled to a single number.
    */
  private class Solver (val data: Array[Array[Float]]) {
    private var N = data.length
    private var totals = new Array[Double](N)
    private var grandTotal: Double = 0
    private var done: Boolean = false

    private var barrier = new CyclicBarrier(N, () => {
      mergeRows()
    })

    for (i <- 0 until N) {
      new Thread(new Worker(i)).start()
    }
    waitUntilDone()



    private class Worker private[solver](var myRow: Int) extends Runnable {
      override def run(): Unit = {
        while (!done) {
          processRow(myRow)
          try {
            val workerNum = barrier.await
            //System.out.println("await n=" + workerNum)
          } catch {
            case ex@(_: InterruptedException | _: BrokenBarrierException) =>
              return
          }
        }
      }

      private def processRow(myRow: Int): Unit = {
        var total: Double = 0
        for (i <- data(myRow).indices) {
          total += data(myRow)(i)      // simlify to total = data(myRow).sum
        }
        totals(myRow) = total
        //System.out.println("done summing row " + myRow)
      }
    }

    def getTotal: Double = {
      if (!done) throw new IllegalStateException("Cannot get the result until done processing.")
      grandTotal
    }

    private def waitUntilDone(): Unit = {
      while (!done) {
        //System.out.println("not done yet...")
        ThreadUtil.sleep(5)
      }
    }

    /** called once all rows have been summed */
    private def mergeRows(): Unit = {
      for (i <- 0 until N)
        grandTotal += totals(i) // grandTotal = totals.sum
      //System.out.println("grandTotal found = " + grandTotal)
      done = true
    }
  }

}
