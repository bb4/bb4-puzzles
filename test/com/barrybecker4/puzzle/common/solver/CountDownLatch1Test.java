package com.barrybecker4.puzzle.common.solver;

import org.junit.Test;
import java.util.concurrent.CountDownLatch;
import static org.junit.Assert.assertEquals;

/**
 * Here is a pair of classes in which a group of worker threads use two countdown latches:
 * <ul>
 * <li>The first is a start signal that prevents any worker from proceeding
 * until the driver is ready for them to proceed;
 * <li>The second is a completion signal that allows the driver to wait
 * until all workers have completed.
 * </ul>
 */
public class CountDownLatch1Test {

    private volatile int counter = 0;

    @Test
    public void testDriverWith0Threads() throws Exception {
        runDriver(0);
        assertEquals("Unexpected counter value", 2, counter);
    }

    @Test
    public void testDriverWith1Threads() throws Exception {
        runDriver(1);
        assertEquals("Unexpected counter value", 12, counter);
    }

    @Test
    public void testDriverWith2Threads() throws Exception {
        runDriver(2);
        assertEquals("Unexpected counter value", 22, counter);
    }

    @Test
    public void testDriverWith10Threads() throws Exception {
        runDriver(10);
        assertEquals("Unexpected counter value", 102, counter);
    }

    void doSomethingElse() {
        System.out.println("Something else... counter=" + counter);
        counter++;
    }


    void runDriver(int numThreads) throws InterruptedException {

        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(numThreads);

        for (int i = 0; i < numThreads; ++i) { // create and start threads
            new Thread(new Worker(startSignal, doneSignal)).start();
        }

         doSomethingElse();            // don't let run yet
         startSignal.countDown();      // let all threads proceed
         doSomethingElse();
         doneSignal.await();           // wait for all to finish
    }


     class Worker implements Runnable {

           private final CountDownLatch startSignal;
           private final CountDownLatch doneSignal;

           Worker(CountDownLatch startSignal, CountDownLatch doneSignal) {
              this.startSignal = startSignal;
              this.doneSignal = doneSignal;
           }
           public void run() {
              try {
                startSignal.await();
                doWork();
                doneSignal.countDown();
              }
              catch (InterruptedException ex) {}
           }

           void doWork() {
               System.out.print("Before working. counter=" + counter);
               counter += 10;
               System.out.println("...  After working. counter=" + counter);
           }
       }

}
