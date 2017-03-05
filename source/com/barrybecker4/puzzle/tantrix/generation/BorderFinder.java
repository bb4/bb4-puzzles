// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.generation;

import com.barrybecker4.common.geometry.Box;
import com.barrybecker4.common.geometry.Location;
import com.barrybecker4.puzzle.tantrix.model.*;

import java.util.*;

import static com.barrybecker4.puzzle.tantrix.model.HexTile.NUM_SIDES;

/**
 * Finds the set of moves on the border of the current 'tantrix'.
 * The 'tantrix' is the set of currently played consistent tiles.
 * The moves on the border will extend the primary path, but not such that
 * its width or height is more than half the length of the total finished path length.
 *
 * @author Barry Becker
 */
public class BorderFinder {

    private Tantrix tantrix;
    private PathColor primaryColor;
    private Set<Location> visited;
    private int maxHalfPathLength;
    private Box boundingBox;

    /**
     * Constructor
     */
    BorderFinder(Tantrix tantrix, int numTiles, PathColor primaryColor) {
        this.tantrix = tantrix;
        this.primaryColor = primaryColor;
        this.maxHalfPathLength = (numTiles + 1)/2;
        boundingBox = tantrix.getBoundingBox();
    }

    /**
     * Travel the primary path in both directions, adding all adjacent empty placements
     * as long as they do not push either boundingBox dimension beyond maxHalfPathLength.
     * @return list of legal next placements
     */
    Set<Location> findBorderPositions() {
        Set<Location> positions = new LinkedHashSet<>();
        visited = new HashSet<>();

        TilePlacement lastPlaced = tantrix.getLastTile();

        Queue<TilePlacement> searchQueue = new TilePlacementList();
        searchQueue.add(lastPlaced);
        for (TilePlacement placement : tantrix.values()) {
           visited.add(placement.getLocation());
        }

        while (!searchQueue.isEmpty()) {
            TilePlacement placement = searchQueue.remove();
            positions.addAll(findEmptyNeighborLocations(placement));
            searchQueue.addAll(findPrimaryPathNeighbors(placement));
        }

        return positions;
    }

    /**
     * @return all the empty neighbor positions next to the specified placement with primary path match.
     */
    private List<Location> findEmptyNeighborLocations(TilePlacement placement) {
        List<Location> emptyNbrLocations = new LinkedList<>();
        for (byte i=0; i < NUM_SIDES; i++) {

            Location nbrLoc = HexUtil.getNeighborLocation(placement.getLocation(), i);
            TilePlacement nbr = tantrix.get(nbrLoc);
            if (nbr == null && placement.getPathColor(i) == primaryColor) {
                Box newBox = new Box(boundingBox, nbrLoc);
                if (newBox.getMaxDimension() <= maxHalfPathLength) {
                    emptyNbrLocations.add(nbrLoc);
                    boundingBox = newBox;
                }
            }
        }
        return emptyNbrLocations;
    }

    /**
     * @return the one or two neighbors that can be found by following the primary path.
     */
    private TilePlacementList findPrimaryPathNeighbors(TilePlacement previous) {

        TilePlacementList pathNbrs = new TilePlacementList();
        for (byte i=0; i < NUM_SIDES; i++) {
            PathColor color = previous.getPathColor(i);
            if (color == primaryColor) {
                TilePlacement nbr = tantrix.getNeighbor(previous, i);
                if (nbr != null && !visited.contains(nbr.getLocation())) {
                    Box newBox = new Box(boundingBox, nbr.getLocation());
                    if (newBox.getMaxDimension() < maxHalfPathLength) {
                        pathNbrs.add(nbr);
                        visited.add(nbr.getLocation());
                        boundingBox = newBox;
                    }
                }
            }
        }
        return pathNbrs;
    }
}
