// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix1.model.analysis.fitting;

import com.barrybecker4.common.geometry.Location;
import com.barrybecker4.puzzle.tantrix1.model.*;

import static com.barrybecker4.puzzle.tantrix1.model.HexTile.NUM_SIDES;

/**
 *  @author Barry Becker
 */
public abstract class AbstractFitter {

    /** color of the loop path */
    protected PathColor primaryColor;

    /**
     * Used to check the consistency of all the paths.
     */
    AbstractFitter(PathColor primaryColor) {
        this.primaryColor = primaryColor;
    }

    /**
     * The number of placements can be 0, 1, 2, or 3 (rare).
     * PrimaryPathFitter can never have just one, because there are two outputs for every path on a tile.
     *
     * @param tile the tile to place.
     * @param loc the location to try and place it at.
     * @return the placements (at most 3) if any could be found, else an empty list.
     */
    public TilePlacementList getFittingPlacements(HexTile tile, Location loc) {
        TilePlacement placement = new TilePlacement(tile, loc, Rotation.ANGLE_0);
        TilePlacementList validPlacements = new TilePlacementList();

        for (int i = 0; i < NUM_SIDES; i++) {
            if (isFit(placement)) {
                validPlacements.add(placement);
            }
            placement = placement.rotate();
        }
        return validPlacements;
    }

    /**
     * The tile fits if the primary path and all the other paths match for edges that have neighbors.
     * @param placement the tile to check for a valid fit.
     * @return true of the tile fits
     */
    public abstract boolean isFit(TilePlacement placement);

}
