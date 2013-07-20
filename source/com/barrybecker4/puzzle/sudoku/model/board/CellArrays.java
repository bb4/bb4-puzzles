/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.sudoku.model.board;


import java.util.Arrays;

/**
 *  All the arrays of cells for all the rows or columns in the puzzle.
 *
 *  @author Barry Becker
 */
public class CellArrays {

    /** candidate sets for a row or col.   */
    private CellArray[] cellArrays_;

    private int size;

    /**
     * Constructor
     */
    private CellArrays(int size) {

        this.size = size;
        cellArrays_ = new CellArray[size];
    }

    public static CellArrays createRowCellArrays(Board board) {

        CellArrays cellArrays = new CellArrays(board.getEdgeLength());
        for (int i=0; i<board.getEdgeLength(); i++) {
            cellArrays.cellArrays_[i] = CellArray.createRowCellArray(i, board);
        }
        return cellArrays;
    }

    public static CellArrays createColCellArrays(Board board) {
        CellArrays cellArrays = new CellArrays(board.getEdgeLength());
        for (int i=0; i<board.getEdgeLength(); i++) {
            cellArrays.cellArrays_[i] = CellArray.createColCellArray(i, board);
        }
        return cellArrays;
    }

    public CellArray get(int i) {
        return cellArrays_[i];
    }


    public void updateAll(ValuesList values) {

        for (int entry = 0; entry < size; entry++) {
            cellArrays_[entry].updateCandidates(values);
        }
    }

    public String toString() {
       return Arrays.toString(cellArrays_);
    }
}
