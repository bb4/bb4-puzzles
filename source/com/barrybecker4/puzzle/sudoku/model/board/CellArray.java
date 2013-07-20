/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.sudoku.model.board;

import java.util.Arrays;

/**
 *  An array of cells for a row or column in the puzzle.
 *
 *  @author Barry Becker
 */
public class CellArray implements CellSet {

    /** candidate sets for a row or col.   */
    private Cell[] cells_;

    /** the candidates for the cells in this row or column */
    private Candidates candidates_;

    /**
     * Constructor
     * @param size this size of the row (small grid dim squared).
     */
    private CellArray(int size) {

        candidates_ = new Candidates();
        cells_ = new Cell[size];
    }

    static CellArray createRowCellArray(int row, Board board) {
        CellArray cells = new CellArray(board.getEdgeLength());
        cells.candidates_.addAll(board.getValuesList());
        for (int i=0; i<board.getEdgeLength(); i++) {
            Cell cell = board.getCell(row, i);
            cell.setRowCells(cells);
            cells.cells_[i] = cell;
            if (cell.getValue() > 0) {
                cells.removeCandidate(cell.getValue());
            }
        }
        return cells;
    }

    static CellArray createColCellArray(int col, Board board) {
        CellArray cells = new CellArray(board.getEdgeLength());
        cells.candidates_.addAll(board.getValuesList());
        for (int i=0; i<board.getEdgeLength(); i++) {
            Cell cell = board.getCell(i, col);
            cell.setColCells(cells);
            cells.cells_[i] = cell;
            if (cell.getValue() > 0) {
                cells.removeCandidate(cell.getValue());
            }
        }
        return cells;
    }

    public Cell getCell(int i) {
        return cells_[i];
    }

    public Candidates getCandidates() {
        return candidates_;
    }

    public void removeCandidate(int unique) {
        candidates_.safeRemove(unique);
        for (int i=0; i < numCells(); i++) {
            cells_[i].remove(unique);
        }
    }

    /**
     * We can only add the value if none of our cells already have it set.
     * @param value value to add to cells candidate list and that of rows/cols/bigCell if possible.
     */
    public void addCandidate(int value) {
        candidates_.add(value);
        clearCaches();
    }

    public int numCells() {
        return cells_.length;
    }

    /**
     * Assume all of them, then remove the values that are represented.
     */
    public void updateCandidates(ValuesList values) {

        candidates_.clear();
        candidates_.addAll(values);

        for (int i = 0; i < numCells(); i++) {
           int v = cells_[i].getValue();
           if (v > 0) {
              candidates_.remove(v);
           }
        }
    }

    private void clearCaches() {
       for (int i=0; i < numCells(); i++) {
            cells_[i].clearCache();
       }
    }

    public String toString() {
       return "CellArray cells:" +Arrays.toString(cells_) + "    cands=" + candidates_ + "\n";
    }
}
