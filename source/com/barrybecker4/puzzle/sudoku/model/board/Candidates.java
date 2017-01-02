// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.model.board;

import com.barrybecker4.puzzle.sudoku.model.ValueConverter;

import java.util.Arrays;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * The numbers which have not yet been used in this big cell.
 * Consider implementing list and then returning a synchronized version only for rendering.
 * @author Barry Becker
 */
public class Candidates extends ConcurrentSkipListSet<Integer> {

    public Candidates() {}

    public Candidates(Candidates cands) {
        addAll(cands);
    }

    public Candidates copy() {
        return new Candidates(this);
    }

    /**
     * Convenient for testing.
     * @param values
     */
    public Candidates(Integer... values) {
        if (values.length > 0) {
            this.addAll(Arrays.asList(values));
        }
    }

    public Candidates(ValuesList values) {
        this.addAll(values);
    }

    public Integer getFirst() {
        return this.iterator().next();
    }

    public boolean safeRemove(Object v) {
        /*
        if (!this.contains(v)) {
            System.out.println("Cannot remove " + v + " from candidates = " + this);
            throw new IllegalStateException("Cannot remove " + v + " from candidates = " + this);
        }  */
        return super.remove(v);
    }


    public String toString() {
        StringBuilder bldr = new StringBuilder("[");
        for (int v : this) {
            bldr.append(ValueConverter.getSymbol(v)).append(",");
        }
        return bldr.substring(0, bldr.length()-1) + "]";
    }
}
