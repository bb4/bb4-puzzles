// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix1.analysis.fitting;

import com.barrybecker4.puzzle.tantrix1.model.PathColor;
import com.barrybecker4.puzzle.tantrix1.model.Tantrix;
import com.barrybecker4.puzzle.tantrix1.model.TilePlacement;

/**
 *  The process of finding neighbors is a bit more efficient if we have the tantrix.
 *  @author Barry Becker
 */
public class TantrixTileFitter extends TileFitter {

    private Tantrix tantrix;

    /**
     * Used to check the consistency of all the paths.
     */
    public TantrixTileFitter(Tantrix tantrix, PathColor primaryColor) {
        super(tantrix.values(), primaryColor);
        this.tantrix = tantrix;
    }

    /**
     * @param currentPlacement where we are now
     * @param direction side to navigate to to find the neighbor. 0 is to the right.
     * @return the indicated neighbor of the specified tile.
     */
    @Override
    protected TilePlacement getNeighbor(TilePlacement currentPlacement, byte direction) {

        return tantrix.getNeighbor(currentPlacement, direction);
    }

}
