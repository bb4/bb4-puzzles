// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku1.model.update.updaters;

import com.barrybecker4.puzzle.sudoku1.model.board.Board;
import com.barrybecker4.puzzle.sudoku1.model.board.Candidates;
import com.barrybecker4.puzzle.sudoku1.model.board.Cell;
import com.barrybecker4.puzzle.sudoku1.model.board.CellSet;
import com.barrybecker4.puzzle.sudoku1.model.update.AbstractUpdater;

import java.util.HashSet;
import java.util.Set;

/**
 * The idea with this updater is that we want to find a set of n cells that contain only a subset of n digits.
 * If we can do that, then all other cells in that row/col/bigCell can have that set of n digits removed as
 * candidates. This is a generalization of the twins and triplets solvers described in the Sudoku Programming book.
 *
 * Grandma wrote:
 * Would the twins be easier to think about if you look for n cells containing some subset of the same n digits?
 * Is that clear?  Here n would be 2, ..., N-2.  See two cells with BD in the 10th row of the
 * last grid you sent me. See also the lower left BigCell where 6 cells contain some subset of {1,5,8,0,D,E}.
 * Then this would mean that the two cells in that mini grid can't contain any of these 6 digits.
 *
 *  @author Barry Becker
 */
public class NakedSubsetUpdater extends AbstractUpdater {

    /**
     * Constructor
     */
    public NakedSubsetUpdater(Board b) {
        super(b);
    }

    /**
     * We will only check for one naked subset in each row/col/bigCell since its rare to get even one,
     * and if there is a second we can always get it on the next iteration.
     *
     * for each cell in a row/col/miniGrid {
     *    n = numDigits in cell;
     *    if we can find n-1 other cells with only a subset of those n digits and none others {
     *       remove those n digits from the candidate lists of all the other cells in that row/col/miniGrid
     *    }
     * }
     */
    @Override
    public void updateAndSet() {

        checkNakedSubsetInRows();
        checkNakedSubsetInCols();
        checkNakedSubsetInBigCells();
    }

    private void checkNakedSubsetInRows() {
        for (int i=0; i<board.getEdgeLength(); i++) {
            CellSet row = board.getRowCells().get(i);
            checkNakedSubset(row);
        }
    }

    private void checkNakedSubsetInCols() {
        for (int i=0; i<board.getEdgeLength(); i++) {
            CellSet col = board.getColCells().get(i);
            checkNakedSubset(col);
        }
    }

    private void checkNakedSubsetInBigCells() {

        for (int i=0; i<board.getBaseSize(); i++) {
            for (int j=0; j<board.getBaseSize(); j++) {

                checkNakedSubset(board.getBigCell(i, j));
            }
        }
    }

    private void checkNakedSubset(CellSet cells) {
        Candidates foundSubset = null;
        Set<Integer> matches = null;

        for (int i = 0; i < cells.numCells(); i++) {
            Candidates cands = cells.getCell(i).getCandidates();
            matches = new HashSet<>();
            matches.add(i);
            if (cands != null)  {
                int n = cands.size();
                for (int j = 0; j < cells.numCells(); j++) {
                    Candidates cands2 = cells.getCell(j).getCandidates();
                    if (j != i && cands2 != null && cands.containsAll(cands2)) {
                       matches.add(j);
                    }
                }
                if (matches.size() == n) {
                    foundSubset = cands;
                    break;
                }
            }
        }

        if (foundSubset != null) {
            for (int i=0; i<cells.numCells(); i++) {
                Cell cell = cells.getCell(i);
                if (!matches.contains(i) && cell.getCandidates() != null) {
                    cell.getCandidates().removeAll(foundSubset);
                }
            }
        }
    }
}
