// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku1.model.update.updaters;

import com.barrybecker4.puzzle.sudoku1.model.board.Board;
import com.barrybecker4.puzzle.sudoku1.model.board.Candidates;
import com.barrybecker4.puzzle.sudoku1.model.board.Cell;
import com.barrybecker4.puzzle.sudoku1.model.board.ValuesList;
import com.barrybecker4.puzzle.sudoku1.model.update.AbstractUpdater;

/**
 *  CRB stands for Column, Row, Big Cell.
 *  We scan each for unique values. When we find one we set it permanently in the cell.
 *
 *  @author Barry Becker
 */
public class StandardCRBUpdater extends AbstractUpdater {

    /**
     * Constructor
     */
    public StandardCRBUpdater(Board b) {
        super(b);
    }

    /**
     * update candidate lists for all cells then set the unique values that are determined.
     */
    @Override
    public void updateAndSet() {

        updateCellCandidates();
        checkAndSetUniqueValues();
    }

    private void updateCellCandidates() {

        ValuesList values = board.getValuesList();
        board.getRowCells().updateAll(values);
        board.getColCells().updateAll(values);
        board.getBigCells().update(values);
    }

    /**
     * Takes the intersection of the three sets: row, col, bigCell candidates.
     */
    private void checkAndSetUniqueValues() {

        for (int row = 0; row < board.getEdgeLength(); row++) {
            for (int col = 0; col < board.getEdgeLength(); col++) {
                Cell cell = board.getCell(row, col);
                Candidates cands = cell.getCandidates();
                if (cands!=null && cands.size() == 1) {
                    int unique = cands.getFirst();
                    cell.setValue(unique);
                }
            }
        }
    }
}
