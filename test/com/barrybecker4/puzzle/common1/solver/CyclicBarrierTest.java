package com.barrybecker4.puzzle.common1.solver;

import com.barrybecker4.common.concurrency.ThreadUtil;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import static org.junit.Assert.assertEquals;

/**
 * <p>A <tt>CyclicBarrier</tt> supports an optional {@link Runnable} command
 * that is run once per barrier point, after the last thread in the party
 * arrives, but before any threads are released.
 *  This <em>barrier action</em> is useful
 *  for updating shared-state before any of the parties continue.
 *  <pre>
 */
public class CyclicBarrierTest {

    private static final float[][] SMALL_MATRIX = new float[][] {
            new float[]{1.0f, 1.0f},
            new float[] {2.0f, 2.0f}
    };

    private static final float[][] MATRIX = new float[][] {
            new float[]{1.0f, 1.0f, 3.1f, 4.2f, 5.0f, 1.0f},
            new float[]{1.0f, 0.0f, 2.1f, 1.2f, 1.0f, 0.0f},
            new float[]{1.0f, 1.0f, 0.1f, 0.2f, 1.0f, 2.1f},
            new float[]{1.0f, 0.0f, 3.1f, 0.2f, 5.0f, 1.1f},
            new float[]{1.0f, 1.0f, 3.1f, 1.2f, 0.0f, 0.0f},
            new float[]{1.0f, 4.0f, 3.1f, 1.2f, 2.0f, 1.1f},
    };

    private static final float[][] BIG_MATRIX = new float[100][100];

    static {
        Random rand = new Random(1);
        for (int i = 0; i < BIG_MATRIX.length; i++) {
            for (int j = 0; j < BIG_MATRIX.length; j++) {
                BIG_MATRIX[i][j] = rand.nextFloat();
            }
        }
    }

    @Test
    public void testSumSmallMatrix() throws Exception {
        Solver solver = new Solver(SMALL_MATRIX);
        assertEquals("Unexpected grand total", 6.0, solver.getTotal(), 0.0001);
    }

    @Test
    public void testSumMatrix() throws Exception {
        Solver solver = new Solver(MATRIX);
        assertEquals("Unexpected grand total", 55.1, solver.getTotal(), 0.0001);
    }

    @Test
    public void testSumBigMatrix() throws Exception {
        Solver solver = new Solver(BIG_MATRIX);
        assertEquals("Unexpected grand total", 5023.338, solver.getTotal(), 0.01);
    }

    /**
     * All the rows of a matrix are summed into a 1D totals array.
     * Then in the barrier thread (that runs after all rows have been summed)
     * the row totals are totaled to a single number.
     */
    class Solver {
        final int N;
        final float[][] data;
        final CyclicBarrier barrier;
        final double[] totals;
        double grandTotal = 0;
        private boolean done = false;

        class Worker implements Runnable {

            int myRow;

            Worker(int row) {
                myRow = row;
            }

            public void run() {
                while (!done) {
                    processRow(myRow);

                    try {
                        int workerNum = barrier.await();
                        System.out.println("await n="+ workerNum);
                    } catch (InterruptedException | BrokenBarrierException ex) {
                        return;
                    }
                }
            }

            void processRow(int myRow) {
                double total = 0;
                for (int i=0; i<data[myRow].length; i++) {
                   total += data[myRow][i];
                }
                totals[myRow] = total;
                System.out.println("done summing row "+ myRow);
            }
        }

        Solver(float[][] matrix) {
            data = matrix;
            N = matrix.length;
            totals = new double[N];

            barrier = new CyclicBarrier(N,
                       new Runnable() {
                          public void run() {
                              mergeRows();
                         }
                      });

            for (int i = 0; i < N; ++i) {
                new Thread(new Worker(i)).start();
            }

            waitUntilDone();
        }

        double getTotal() {
            if (!done) {
               throw new IllegalStateException("Cannot get the result until done processing.");
            }
            return grandTotal;
        }

        private void waitUntilDone() {
            while (!done) {
                System.out.println("not done yet...");
                ThreadUtil.sleep(0);
            }
        }

        /** called once all rows have been summed */
        private void mergeRows() {
            for (int i=0; i<N; i++) {
                grandTotal += totals[i]++;
            }
            System.out.println("grandTotal found = "+ grandTotal);
            done = true;
        }
     }
}
