// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku1.model.board;

/**
 * @author Barry Becker
 */
public class Cell {

    /** must be a number between 1 and nn_  */
    private int value_;

    /** true if part of the original specification.  */
    private boolean original_;

    /** the BigCell to which I belong   */
    private BigCell parentBigCell_;
    private CellSet rowCells_;
    private CellSet colCells_;

    private Candidates cachedCandidates;

    /**
     * Constructor.
     */
    public Cell(int value) {
        setOriginalValue(value);
    }

    void setParent(BigCell parent) {
        parentBigCell_ = parent;
    }

    public int getValue() {
        return value_;
    }

    public boolean isOriginal() {
        return original_;
    }

    public boolean isParent(BigCell bigCell) {
        return bigCell == parentBigCell_;
    }

    /**
     * once the puzzle is started, you can only assign positive values to values of cells.
     * @param value the value to set permanently in the cell (at least until cleared).
     */
    public void setValue(int value) {
        assert(value > 0);
        value_ = value;
        original_ = false;

        parentBigCell_.removeCandidate(value_);
        rowCells_.removeCandidate(value_);
        colCells_.removeCandidate(value_);
        clearCache();
    }

    /**
     * Set the value back to unset and add the old value to the list of candidates
     * The value should only be added back to row/col/bigCell candidates if the value is not already set
     * for respective row/col/bigCell.
     * Clear value should be the inverse of setValue.
     */
    public void clearValue() {
        if (value_ == 0) return;

        int value = value_;
        value_ = 0;
        original_ = false;

        rowCells_.addCandidate(value);
        colCells_.addCandidate(value);
        parentBigCell_.addCandidate(value);
        clearCache();
    }

    /**
     * Once the puzzle is started, you can only assign positive values to values of cells.
     * @param value original value
     */
    public void setOriginalValue(int value) {
        assert(value >= 0);
        value_ = value;

        // if set to 0 initially, then it is a value that needs to be filled in.
        original_ = value > 0;

        if (original_)  {
            parentBigCell_.removeCandidate(value);
            rowCells_.removeCandidate(value);
            colCells_.removeCandidate(value);
        }
    }

    public void remove(int value) {
        if (cachedCandidates != null) {
            cachedCandidates.remove(value);
        }
    }

    void clearCache() {
        cachedCandidates = null;
    }

    /**
     * Intersect the parent big cell candidates with the row and column candidates.
     * [If after doing the intersection, we have only one value, then set it on the cell. ]
     */
    public Candidates getCandidates() {

        if (value_ > 0) return null;

        Candidates candidates;
        if (cachedCandidates != null)  {
            candidates = cachedCandidates;
        }
        else {
            candidates = new Candidates();
            candidates.addAll(parentBigCell_.getCandidates());
            candidates.retainAll(rowCells_.getCandidates());
            candidates.retainAll(colCells_.getCandidates());
            cachedCandidates = candidates;
        }
        return candidates;

    }

    void setRowCells(CellArray rowCells) {
        rowCells_ = rowCells;
    }

    void setColCells(CellArray colCells) {
        colCells_ = colCells;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cell cell = (Cell) o;
        return value_ == cell.value_;
    }

    @Override
    public int hashCode() {
        return value_;
    }

    public String toString() {
        //return (getCandidates() == null)? "[]" : getCandidates().toString();
        return "v=" + getValue() + " cands="+ this.getCandidates().toString();
    }
}
