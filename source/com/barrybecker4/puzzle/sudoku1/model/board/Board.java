// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku1.model.board;

import com.barrybecker4.common.geometry.Location;
import com.barrybecker4.puzzle.sudoku1.model.ValueConverter;

/**
 *  The Slider describes the physical layout of the puzzle.
 *
 *  @author Barry Becker
 */
public class Board {

    /** The number of Cells in the board is n^2 * n^2, but there are n * n big cells.   */
    private int n_;
    private int nn_;  // n times n

    private static final int MAX_SIZE = 9;

    private Cell[][] cells_;

    // row and col cells for every row and col.
    private CellArrays rowCells_;
    private CellArrays colCells_;

    /** the internal data structures representing the game board. */
    private BigCellArray bigCells_;

    /** all the values in the big cells or rows/cols 1...nn_ */
    private final ValuesList valuesList_;

    private int numIterations_;

    /**
     * Constructor
     */
    public Board(int size) {
        assert(size > 1 && size < MAX_SIZE);
        n_ = size;
        nn_ = size * size;
        valuesList_ = new ValuesList(nn_);
        reset();
    }

    /**
     * copy constructor
     */
    public Board(Board b) {
        this(b.getBaseSize());
        for (int i=0; i<nn_; i++) {
           for (int j=0; j<nn_; j++) {
               getCell(i, j).setOriginalValue(b.getCell(i, j).getValue());
           }
        }
    }

    public Board(int[][] initialData) {
        this((int) Math.sqrt(initialData.length));

        assert(initialData.length == nn_ && initialData[0].length == nn_);

        for (int i=0; i<nn_; i++) {
           for (int j=0; j<nn_; j++) {
               getCell(i, j).setOriginalValue(initialData[i][j]);
           }
        }
    }

    /**
     * return to original state before attempting solution.
     * Non original values become 0.
     */
    public void reset() {
        cells_ = new Cell[nn_][nn_];
        for (int i=0; i<nn_; i++)  {
           for (int j=0; j<nn_; j++) {
               cells_[i][j] = new Cell(0);
           }
        }
        bigCells_ = new BigCellArray(this);
        rowCells_ = CellArrays.createRowCellArrays(this);
        colCells_ = CellArrays.createColCellArrays(this);
        numIterations_ = 0;
    }

    public CellArrays getRowCells() {
        return rowCells_;
    }

    public CellArrays getColCells() {
        return colCells_;
    }

    public BigCellArray getBigCells() {
        return bigCells_;
    }

    /**
     * @return retrieve the base size of the board (sqrt(edge magnitude)).
     */
    public final int getBaseSize() {
        return n_;
    }

    /**
     * @return  retrieve the edge size of the board.
     */
    public final int getEdgeLength() {
        return nn_;
    }

    public final int getNumCells() {
        return nn_ * nn_;
    }

    /**
     * @return the bigCell at the specified location.
     */
    public final BigCell getBigCell(int row, int col) {

        return bigCells_.getBigCell(row, col);
    }

    /**
     * @param row 0-nn_-1
     * @param col 0-nn_-1
     * @return the cell in the bigCellArray at the specified location.
     */
    public final Cell getCell(int row, int col) {
        return cells_[row][col];
    }

    public final Cell getCell(Location location) {
        return cells_[location.getRow()][location.getCol()];
    }

    /**
     * @param position a number between 0 and nn_^2
     * @return the cell at the specified position.
     */
    public final Cell getCell( int position ) {
        return getCell(position / nn_, position % nn_);
    }

    /**
     * @return true if the board has been successfully solved.
     */
    public boolean solved() {

        return isFilledIn() && hasNoCandidates();
    }

    /**
     * @return true if all the cells have been filled in with a value (even if not a valid solution).
     */
    private boolean isFilledIn() {
        for (int row = 0; row < nn_; row++) {
            for (int col = 0; col < nn_; col++) {
                Cell c = getCell(row, col);
                if (c.getValue() <= 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean hasNoCandidates() {
        for (int row=0; row < nn_; row++) {
            for (int col=0; col < nn_; col++) {
                Cell c = getCell(row, col);
                if (c.getCandidates() != null) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * @return the complete set of allowable values (1,... nn);
     */
    public ValuesList getValuesList() {
        return valuesList_;
    }

    public int getNumIterations() {
        return numIterations_;
    }

    public void incrementNumIterations() {
        numIterations_++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Board board = (Board) o;
        if (n_ != board.n_) return false;
        if (nn_ != board.nn_) return false;

        for (int row=0; row < nn_; row++) {
            for (int col=0; col < nn_; col++) {
                if (this.getCell(row, col).getValue() != board.getCell(row, col).getValue() ) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = n_;
        result = 31 * result + nn_;
        result = 31 * result + (rowCells_ != null ? rowCells_.hashCode() : 0);
        return result;
    }

    public String toString() {
        StringBuilder bldr = new StringBuilder("\n");
        for (int row=0; row < nn_; row++) {
            for (int col=0; col < nn_; col++) {
                bldr.append(ValueConverter.getSymbol(getCell(row, col).getValue()));
                bldr.append(" ");
            }
            bldr.append("\n");
        }
        bldr.append("rowCells=\n").append(rowCells_);
        //bldr.append("colCells=\n" + colCells_);
        bldr.append("bigCells =\n").append(getBigCells());
        return bldr.toString();
    }
}
