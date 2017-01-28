// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku1.model.update.updaters;

import com.barrybecker4.puzzle.sudoku1.model.board.BigCell;
import com.barrybecker4.puzzle.sudoku1.model.board.Board;
import com.barrybecker4.puzzle.sudoku1.model.board.Candidates;
import com.barrybecker4.puzzle.sudoku1.model.board.Cell;
import com.barrybecker4.puzzle.sudoku1.model.update.AbstractUpdater;

import java.util.ArrayList;
import java.util.List;

/**
 *  Lone rangers are cells that have a candidate (among others) that is unique when
 *  compared with the other candidates in other cells in that row, column, or bigCell.
 *
 *  @author Barry Becker
 */
public class LoneRangerUpdater extends AbstractUpdater {

    /**
     * Constructor
     */
    public LoneRangerUpdater(Board b) {
        super(b);
    }

    /**
     * Lone rangers are cells than have a unique candidate. For example, consider these
     * candidates for cells in a row (or column, or bigCell).
     * 23 278 13 28 238 23
     * Then the second cell has 7 as a lone ranger.
     */
    @Override
    public void updateAndSet() {

        checkForLoneRangers();
    }

    /** Look for lone rangers in row, col, and bigCell */
    private void checkForLoneRangers() {

        int n = board.getBaseSize();

        for (int row = 0; row < board.getEdgeLength(); row++) {
            for (int col = 0; col < board.getEdgeLength(); col++) {
                Cell cell = board.getCell(row, col);

                BigCell bigCell = board.getBigCell(row / n, col / n);
                CandidatesArray bigCellCands = getCandidatesArrayExcluding(bigCell, row % n, col % n);
                CandidatesArray rowCellCands = getCandidatesArrayForRowExcludingCol(row, col);
                CandidatesArray colCellCands = getCandidatesArrayForColExcludingRow(row, col);

                checkAndSetLoneRangers(bigCellCands, cell);
                checkAndSetLoneRangers(rowCellCands, cell);
                checkAndSetLoneRangers(colCellCands, cell);
            }
        }
    }


    /** @return all the candidate lists for all the cells in the bigCell except the one specified. */
    private CandidatesArray getCandidatesArrayExcluding(BigCell bigCell, int row, int col) {

        List<Candidates> cands = new ArrayList<Candidates>();

        int n = bigCell.getSize();
        for (int i = 0; i <n; i++) {
           for (int j = 0; j < n; j++) {
               Candidates c = bigCell.getCell(i, j).getCandidates();
               if ((i != row || j != col) && c != null) {
                   cands.add(c);
               }
           }
        }
        return new CandidatesArray(cands.toArray(new Candidates[cands.size()]));
    }

    private CandidatesArray getCandidatesArrayForRowExcludingCol(int row, int col) {
        List<Candidates> cands = new ArrayList<Candidates>();

        for (int i = 0; i < board.getEdgeLength(); i++) {
           Candidates c = board.getCell(row, i).getCandidates();
           if ((i != col) && c != null) {
               cands.add(c);
           }
        }
        return new CandidatesArray(cands.toArray(new Candidates[cands.size()]));
    }

    private CandidatesArray getCandidatesArrayForColExcludingRow(int row, int col) {
        List<Candidates> cands = new ArrayList<Candidates>();

        for (int i = 0; i < board.getEdgeLength(); i++) {
           Candidates c = board.getCell(i, col).getCandidates();
           if ((i != row) && c != null) {
               cands.add(c);
           }
        }
        return new CandidatesArray(cands.toArray(new Candidates[cands.size()]));
    }


    private void checkAndSetLoneRangers(CandidatesArray candArray, Cell cell) {

        if (cell.getCandidates() == null) return;

        Candidates candsCopy = cell.getCandidates().copy();

        int i=0;
        while (i<candArray.size() && candsCopy.size() > 0) {

            Candidates c = candArray.get(i++);
            if (c != null)  {
                candsCopy.removeAll(c);
            }
        }

        if (candsCopy.size() == 1) {
            int unique = candsCopy.getFirst();
            cell.setValue(unique);
        }
    }
}
