// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix1.solver.verification;

import com.barrybecker4.puzzle.tantrix1.model.PathColor;
import com.barrybecker4.puzzle.tantrix1.model.TilePlacement;
import com.barrybecker4.puzzle.tantrix1.model.analysis.fitting.TileFitter;

import java.util.Collection;

/**
 *  Checks the consistency of paths between tiles given a collection of tiles.
 *
 *  @author Barry Becker
 */
public class ConsistencyChecker {

    /** tiles that have not yet been placed on the tantrix */
    private Collection<TilePlacement> tiles;

    private TileFitter fitter;


    /**
     * Used to check the consistency of all the paths.
     */
    public ConsistencyChecker(Collection<TilePlacement> tiles, PathColor primaryColor) {
        this.tiles = tiles;
        fitter = new TileFitter(tiles, primaryColor);
    }

    /**
     * @return the number of tiles that fit perfectly.
     */
    public int numFittingTiles() {
        int numFits = 0;
        for (TilePlacement p : tiles) {
            if (fitter.isFit(p)) {
                numFits++;
            }
        }
        return numFits;
    }
}
