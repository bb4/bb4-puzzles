// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix1.solver.path.permuting;

import com.barrybecker4.common.geometry.Location;
import com.barrybecker4.puzzle.tantrix1.model.PathColor;
import com.barrybecker4.puzzle.tantrix1.model.TilePlacement;
import com.barrybecker4.puzzle.tantrix1.solver.path.TantrixPath;

import java.util.Map;

/**
 * Given a TantrixPath and a pivot tile index, find the permuted paths.
 * Since there are 8 total ways to permute and the path already represents one of them,
 * the permuter will never return more than 7 valid permuted paths.
 *
 * @author Barry Becker
 */
public abstract class SubPathMutator {

    protected PathColor primaryColor;

    /**
     * Constructor
     */
    SubPathMutator(PathColor primaryColor) {
        this.primaryColor = primaryColor;
    }

    /**
     * Do something to mutate this subpath - like swap or reverse it.
     * @param subPath the subpath to reverse relative to the pivot tile.
     * @return the mutated subpath.
     */
    public abstract TantrixPath mutate(TilePlacement pivotTile, TantrixPath subPath);


    protected boolean fits(TilePlacement currentPlacement, TilePlacement previousPlacement) {
        Map<Integer, Location> outgoingPathLocations = currentPlacement.getOutgoingPathLocations(primaryColor);
        for (Location loc : outgoingPathLocations.values()) {
            if (loc.equals(previousPlacement.getLocation())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Of the two outgoing path locations coming out from previousPlacement pick the one that is not the excludeLocation.
     * @param sourcePlacement the tile to consider outgoing paths from.
     * @param excludeLocation we want to chose the other location that is not this when leaving the source.
     * @return the other outgoing location for the sourcePlacement.
     */
    Location findOtherOutgoingLocation(TilePlacement sourcePlacement, Location excludeLocation) {

       Map<Integer, Location> outgoingPathLocations = sourcePlacement.getOutgoingPathLocations(primaryColor);
       Location loc = null;
       for (int rot : outgoingPathLocations.keySet()) {
           loc = outgoingPathLocations.get(rot);
           if (!loc.equals(excludeLocation)) {
               break;
           }
       }
       return loc;
    }

    /**
     * Find the direction to the specified outgoing location.
     * @param sourcePlacement the tile to consider outgoing paths from.
     * @param location we want to chose the direction to this location.
     * @return the other outgoing location for the sourcePlacement.
     */
    protected int findOutgoingDirection(TilePlacement sourcePlacement, Location location) {

        Map<Integer, Location> outgoingPathLocations = sourcePlacement.getOutgoingPathLocations(primaryColor);

        for (int rot : outgoingPathLocations.keySet()) {
            if (outgoingPathLocations.get(rot).equals(location)) {
                return rot;
            }
        }
        assert false : location + " was not on an outgoing path from " + sourcePlacement;
        return -1;
    }
}
