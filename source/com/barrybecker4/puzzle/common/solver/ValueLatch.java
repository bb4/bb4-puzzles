package com.barrybecker4.puzzle.common.solver;

import java.util.concurrent.CountDownLatch;


/**
 * Result-bearing latch used by ConcurrentPuzzleSolver.
 * The value cannot be retrieved until it is set. It blocks until set.
 * ThreadSafe
 *
 * @author Brian Goetz
 * @author Tim Peierls
 */
public class ValueLatch <T> {

    /** Holds the final result, when it's ready */
    private T value = null;

    /** When this reaches 0, the result is ready. */
    private final CountDownLatch done = new CountDownLatch(1);

    /** @return true when the result value has been set. */
    public boolean isSet() {
        return (done.getCount() == 0);
    }

    /** @param newValue the value to set (as long as it was not already set). */
    public synchronized void setValue(T newValue) {
        if (!isSet()) {
            value = newValue;
            done.countDown();
        }
    }

    /**
     * This blocks until the final value is available.
     * @return the final computed value
     * @throws InterruptedException
     */
    public T getValue() throws InterruptedException {
        done.await();
        synchronized (done) {
            return value;
        }
    }
}
