// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.model.update.updaters;

import com.barrybecker4.puzzle.sudoku.model.board.BigCell;
import com.barrybecker4.puzzle.sudoku.model.board.Board;
import com.barrybecker4.puzzle.sudoku.model.board.Candidates;
import com.barrybecker4.puzzle.sudoku.model.board.Cell;
import com.barrybecker4.puzzle.sudoku.model.update.AbstractUpdater;

/**
 *  If there is a row or column within a bigCell that uniquely contains a value in its cell candidate  lists,
 *  then we can check the cells in that same row or column outside the bigCell for places where there are only
 *  two candidates and one is that value. If we find places like that, then we can set those outside cells to the
 *  pair value that is not the one within the bigCell.
 *
 *  @author Barry Becker
 */
public class BigCellScoutUpdater extends AbstractUpdater {

    /**
     * Constructor
     */
    public BigCellScoutUpdater(Board b) {
        super(b);
    }

    /**
     * Have a method on a mini-grid (bigCell in my terminology) that checks all its small rows and columns
     * for values that appear only in that row or column and not the rest of the mini-grid.
     * Then I can take any of those cells for which that value appear and remove that value from
     * all cells that are in that row (or column) and outside that mini-grid.
     */
    @Override
    public void updateAndSet() {

        int n = board.getBaseSize();
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                BigCell bigCell = board.getBigCell(row, col);
                for (int value : board.getValuesList())  {
                    int uniqueRow = bigCell.findUniqueRowFor(value);
                    if (uniqueRow >= 0) {
                        checkAndSetRowOutside(bigCell, uniqueRow + row * n, value);
                    }
                    int uniqueCol = bigCell.findUniqueColFor(value);
                    if (uniqueCol >= 0) {
                        checkAndSetColOutside(bigCell, uniqueCol + col * n, value);
                    }
                }
            }
        }
    }

    /** Check for cells we can set in the row outside the bigCell */
    private void checkAndSetRowOutside(BigCell bigCell, int uniqueRow, int value) {

        for (int col = 0; col < board.getEdgeLength(); col++) {
            Cell cell = board.getCell(uniqueRow, col);
            if (!cell.isParent(bigCell)) {
                checkIfCanSetOutsideCellValue(value, cell);
            }
        }
    }

    /** Check for cells we can set in the column outside the bigCell */
    private void checkAndSetColOutside(BigCell bigCell, int uniqueCol, int value) {

        for (int row = 0; row < board.getEdgeLength(); row++) {
            Cell cell = board.getCell(row, uniqueCol);
            if (!cell.isParent(bigCell)) {
                checkIfCanSetOutsideCellValue(value, cell);
            }
        }
    }

    /**
     * If the outside cell has only 2 values and one of them is a value that must be in the bigCell
     * then the other value can be set.
     */
    private void checkIfCanSetOutsideCellValue(int value, Cell cell) {
        Candidates cands = cell.getCandidates();
        if (cands != null && cands.size() == 2 && cands.contains(value)) {
            for (int candValue : cands) {

                if (candValue != value) {
                    cell.setValue(candValue);
                }
            }
        }
    }
}
