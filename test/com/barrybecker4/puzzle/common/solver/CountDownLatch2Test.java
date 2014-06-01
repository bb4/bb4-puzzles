package com.barrybecker4.puzzle.common.solver;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

/**
 * A typical usage is to divide a problem into N parts,
 * describe each part with a Runnable that executes that portion and
 * counts down on the latch, and queue all the Runnables to an
 * Executor.  When all sub-parts are complete, the coordinating thread
 * will be able to pass through await.
 * (When threads must repeatedly count down in this way, instead use a {@link CyclicBarrier}.)
 */
public class CountDownLatch2Test {

    private volatile int counter = 0;


    @Test
    public void testDriverWith1Threads() throws Exception {
        runDriver(1);
        assertEquals("Unexpected counter value", 10, counter);
    }

    @Test
    public void testDriverWith2Threads() throws Exception {
        runDriver(2);
        assertEquals("Unexpected counter value", 20, counter);
    }

    @Test
    public void testDriverWith10Threads() throws Exception {
        runDriver(10);
        assertEquals("Unexpected counter value", 100, counter);
    }


    void runDriver(int numThreads) throws InterruptedException {

        CountDownLatch doneSignal = new CountDownLatch(numThreads);
        Executor e = Executors.newFixedThreadPool(numThreads);

        // create and start threads
        for (int i = 0; i < numThreads; ++i) {
            e.execute(new WorkerRunnable(doneSignal, i));
        }

        // wait for all workers to finish
        doneSignal.await();
        System.out.println("Finished everything.");
    }


    class WorkerRunnable implements Runnable {
        private final CountDownLatch doneSignal;
        private final int i;

        WorkerRunnable(CountDownLatch doneSignal, int i) {
            this.doneSignal = doneSignal;
            this.i = i;
        }
        public void run() {
           doWork(i);
           doneSignal.countDown();
        }

        void doWork(int i) {
            System.out.print("Before working on task " + i + ". counter=" + counter);
            counter += 10;
            System.out.println("...  After working. counter=" + counter);
        }
    }
}
