// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.model.fitting;

import com.barrybecker4.puzzle.tantrix.model.PathColor;
import com.barrybecker4.puzzle.tantrix.model.Tantrix;
import com.barrybecker4.puzzle.tantrix.model.TilePlacement;
import com.barrybecker4.puzzle.tantrix.model.TilePlacementList;

import static com.barrybecker4.puzzle.tantrix.model.HexTile.NUM_SIDES;

/**
 * Determines valid primary path fits for a specified tile relative to an existing set at a specific location.
 * This is less strict than tile fitter - which checks all paths.
 *
 * @author Barry Becker
 */
public class PrimaryPathFitter extends AbstractFitter {

    private Tantrix tantrix;

    /**
     * Constructor
     */
    public PrimaryPathFitter(Tantrix tantrix, PathColor primaryColor) {
        super(primaryColor);
        this.tantrix = tantrix;
    }

    /**
     * Constructor
     */
    public PrimaryPathFitter(TilePlacementList tiles, PathColor primaryColor) {
        super(primaryColor);
        this.tantrix = new Tantrix(tiles);
    }

    /**
     * The tile fits if the primary path fits against all neighbors.
     * All adjacent primary paths must match an edge on the tile being fit.
     * Check all the neighbors (that exist) and verify that if that direction is a primary path output, then it matches.
     * If none of the neighbor edges has a primary path, then rotations where non-primary path edges touch those neighbors
     * are allowed as fits.
     * @param placement the tile to check for a valid fit.
     * @return true of the tile fits
     */
    @Override
    public boolean isFit(TilePlacement placement) {

        boolean fits = true;
        for (byte i = 0; i < NUM_SIDES; i++) {
            TilePlacement nbr = tantrix.getNeighbor(placement, i);

            if (nbr != null) {
                PathColor pathColor = placement.getPathColor(i);
                PathColor nbrColor = nbr.getPathColor(i+3);

                if ((pathColor == primaryColor || nbrColor == primaryColor) && pathColor != nbrColor) {
                    fits = false;
                }
            }
        }

        return fits;
    }

    /**
     * @param placement the tile to check for a valid fit.
     * @return the number of primary path matches to neighboring tiles.
     */
    public int numPrimaryFits(TilePlacement placement) {

        int numFits = 0;
        for (byte i = 0; i < NUM_SIDES; i++) {
            TilePlacement nbr = tantrix.getNeighbor(placement, i);

            if (nbr != null) {
                PathColor pathColor = placement.getPathColor(i);

                if (pathColor == primaryColor && pathColor == nbr.getPathColor(i+3)) {
                    numFits++;
                }
            }
        }
        assert numFits <= 2 : "There cannot be more than 2 primary path fits.";
        return numFits;
    }

    /**
     * @return total number of primary path fits for the whole tantrix.
     */
    public int numPrimaryFits() {
        if (tantrix.size() < 2) return 0;

        int numFits = 0;
        for (TilePlacement p : tantrix.values()) {
            numFits += numPrimaryFits(p);
        }

        return numFits;
    }

    /** used only for debugging */
    public Tantrix getTantrix() {
        return tantrix;
    }


}
