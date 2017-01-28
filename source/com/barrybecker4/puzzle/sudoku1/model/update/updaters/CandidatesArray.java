// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku1.model.update.updaters;

import com.barrybecker4.puzzle.sudoku1.model.board.Candidates;

import java.util.Arrays;

/**
 *  An array of sets of integers representing the candidates for the cells in a row or column.
 *
 *  @author Barry Becker
 */
public class CandidatesArray {

    /** candidate sets for a row or col.   */
    private Candidates[] candidates_;

    public CandidatesArray(Candidates[] cands) {
        candidates_ = cands;
    }

    public Candidates get(int i) {
        return candidates_[i];
    }

    public int size() {
        return candidates_.length;
    }

    public String toString() {
       return Arrays.toString(candidates_);
    }

}
