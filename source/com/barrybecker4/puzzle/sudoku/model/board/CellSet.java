// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.model.board;

/**
 * An array of cells in a row column or bigCell in the puzzle.
 *
 * @author Barry Becker
 */
public interface CellSet {

    Cell getCell(int i);

    Candidates getCandidates();

    void removeCandidate(int unique);

    void addCandidate(int value);

    int numCells();

    /**
     * Assume all of them, then remove the values that are represented.
     */
    void updateCandidates(ValuesList values);

}
