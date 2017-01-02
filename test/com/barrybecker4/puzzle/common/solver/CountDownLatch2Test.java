package com.barrybecker4.puzzle.common.solver;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

/**
 * A typical usage is to divide a problem into N parts,
 * describe each part with a Runnable that executes that portion and
 * counts down on the latch, and queue all the Runnables to an Executor.
 * When all sub-parts are complete, the coordinating thread will be able to pass through await.
 * (When threads must repeatedly count down in this way, instead use a {@link CyclicBarrier}.)
 */
public class CountDownLatch2Test {

    private AtomicInteger counter = new AtomicInteger(0);


    @Test
    public void testDriverWith1Threads() throws Exception {
        runDriver(1);
        assertEquals("Unexpected counter value", 10, counter.get());
    }

    @Test
    public void testDriverWith3Threads() throws Exception {
        runDriver(3);
        assertEquals("Unexpected counter value", 30, counter.get());
    }

    /* fails for some odd reason when run from cmd line, but not in IDE */
    @Test
    public void testDriverWith2Threads() throws Exception {
        runDriver(2);
        assertEquals("Unexpected counter value", 20, counter.get());
    }

    @Test
    public void testDriverWith5Threads() throws Exception {
        runDriver(5);
        assertEquals("Unexpected counter value", 50, counter.get());
    }

    @Test
    public void testDriverWith10Threads() throws Exception {
        runDriver(10);
        assertEquals("Unexpected counter value", 100, counter.get());
    }

    @Test
    public void testDriverWith100Threads() throws Exception {
        runDriver(100);
        assertEquals("Unexpected counter value", 1000, counter.get());
    }


    private void runDriver(int numThreads) throws InterruptedException {

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
            counter.addAndGet(10);
            System.out.println("...  After working. counter=" + counter);
        }
    }
}
