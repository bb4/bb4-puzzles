// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.model.board;

/**
 *  An array of sets of integers representing the candidates for the cells in a row or column.
 *
 *  @author Barry Becker
 */
public class BigCellArray {

    /** n by n grid of big cells.   */
    private BigCell[][] bigCells_;

    private int size_;

    /**
     * Constructor
     */
    public BigCellArray(Board board) {

        size_ = board.getBaseSize();
        bigCells_ = new BigCell[size_][size_];

        for (int i=0; i<size_; i++) {
           for (int j=0; j<size_; j++) {
               bigCells_[i][j] = new BigCell(board, size_ * i, size_ * j);
           }
        }
    }

    public BigCell getBigCell(int i, int j) {
        assert ( i >= 0 && i < size_ && j >= 0 && j < size_);
        return bigCells_[i][j];
    }

    /**
     * @return the size of the edge of a big cell. e.g. 3 for a typical board.
     */
    public int getSize() {
        return size_;
    }

    public void update(ValuesList values) {
        for (int i=0; i<size_; i++) {
            for (int j=0; j<size_; j++) {
                getBigCell(i, j).updateCandidates(values);
            }
        }
    }

    public String toString() {
        StringBuilder bldr = new StringBuilder();
        for (int row=0; row < size_; row++) {
           for (int col=0; col < size_; col++) {
               bldr.append("cands(").append(row).append(", ").append(col).append(")=").append(getBigCell(row, col).getCandidates()).append("\n");
            }
        }
        return bldr.toString();
    }
}
