// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix1.model.analysis.fitting;

import com.barrybecker4.common.geometry.Location;
import com.barrybecker4.puzzle.tantrix1.model.HexUtil;
import com.barrybecker4.puzzle.tantrix1.model.PathColor;
import com.barrybecker4.puzzle.tantrix1.model.TilePlacement;

import java.util.Collection;

import static com.barrybecker4.puzzle.tantrix1.model.HexTile.NUM_SIDES;

/**
 *  If you have the tantrix use TantrixTileFitter instead of this class.
 *  @author Barry Becker
 */
public class TileFitter extends AbstractFitter {

    /** Current set of placed tiles */
    private Collection<TilePlacement> tiles;

    /**
     * Used to check the consistency of all the paths.
     */
    public TileFitter(Collection<TilePlacement> tiles, PathColor primaryColor) {
        super(primaryColor);
        this.tiles = tiles;
    }

    /**
     * The tile fits if the primary path and all the other paths match for edges that have neighbors.
     * @param placement the tile to check for a valid fit.
     * @return true of the tile fits
     */
    @Override
    public boolean isFit(TilePlacement placement) {
        boolean primaryPathMatched = false;

        for (byte i=0; i < NUM_SIDES; i++) {
            TilePlacement nbr = getNeighbor(placement, i);

            if (nbr != null) {
                PathColor pathColor = placement.getPathColor(i);

                if (pathColor == nbr.getPathColor((byte)(i+3))) {
                    if (pathColor == primaryColor) {
                        primaryPathMatched = true;
                    }
                }  else {
                    return false;
                }
            }
        }

        return primaryPathMatched;
    }

    /**
     * @param currentPlacement where we are now
     * @param direction side to navigate to to find the neighbor. 0 is to the right.
     * @return the indicated neighbor of the specified tile.
     */
    protected TilePlacement getNeighbor(TilePlacement currentPlacement, byte direction) {

        Location loc =
            HexUtil.getNeighborLocation(currentPlacement.getLocation(), direction);
        for (TilePlacement p : tiles) {
            if (p.getLocation().equals(loc)) {
                return p;
            }
        }
        return null;
    }
}
