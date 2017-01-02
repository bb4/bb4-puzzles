// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.model.update;

import com.barrybecker4.puzzle.sudoku.model.board.Board;
import com.barrybecker4.puzzle.sudoku.model.update.updaters.BigCellScoutUpdater;
import com.barrybecker4.puzzle.sudoku.model.update.updaters.LoneRangerUpdater;
import com.barrybecker4.puzzle.sudoku.model.update.updaters.NakedSubsetUpdater;
import com.barrybecker4.puzzle.sudoku.model.update.updaters.StandardCRBUpdater;

import java.util.ArrayList;
import java.util.List;

/**
 * Default board updater applies all the standard updaters.
 * Must use this version from an applet (or webstart) to avoid Security exception :(
 * We can use reflection in applet/webstart only if we sign the applet/jars.
 *
 * @author Barry Becker
 */
public class NonReflectiveBoardUpdater implements IBoardUpdater {

    /** Constructor */
    public NonReflectiveBoardUpdater() {
    }

    /**
     * Update candidate lists for all cells then set the unique values that are determined.
     * First create the updaters using reflection, then apply them.
     */
    public void updateAndSet(Board board) {

        List<IUpdater> updaters = createUpdaters(board);

        for (IUpdater updater : updaters) {
            updater.updateAndSet();
        }
    }

    private List<IUpdater> createUpdaters(Board board) {
        List<IUpdater> updaters = new ArrayList<IUpdater>();
        updaters.add(new StandardCRBUpdater(board));
        updaters.add(new LoneRangerUpdater(board));
        updaters.add(new BigCellScoutUpdater(board));
        updaters.add(new NakedSubsetUpdater(board));
        return updaters;
    }
}
