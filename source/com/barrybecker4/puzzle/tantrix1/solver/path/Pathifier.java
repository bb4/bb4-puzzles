// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix1.solver.path;

import com.barrybecker4.common.geometry.Location;
import com.barrybecker4.puzzle.tantrix1.model.PathColor;
import com.barrybecker4.puzzle.tantrix1.model.Tantrix;
import com.barrybecker4.puzzle.tantrix1.model.TilePlacement;
import com.barrybecker4.puzzle.tantrix1.model.TilePlacementList;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Attempts to reorder the tiles so that they show a path of the primary color.
 * No rotations are made, if there is a path, we will find it.
 * If there is no path, give an error
 *
 * @author Barry Becker
 */
class Pathifier {

    private final PathColor primaryPathColor_;

    /**
     * The list of tiles that are passed in must be a continuous primary path,
     * but it is not required that it be a loop, or that any of the secondary colors match.
     * @param primaryColor primary path color
     */
    Pathifier(PathColor primaryColor) {

        if (primaryColor == null) {
            throw new IllegalArgumentException("primaryColor cannot be null");
        }
        primaryPathColor_ = primaryColor;
    }

    /**
     * Attempt to reorder the tiles into a path if possible.
     * Throw an error if no path. Should not change the order if the tiles are already arranged in a path.
     * @param tantrix the tantrix containing the tiles to reorder.
     * @return the tiles in path order. Error if no path.
     */
    TilePlacementList reorder(Tantrix tantrix) {
        TilePlacementList tiles = new TilePlacementList(tantrix);
        if (tantrix.size() <= 1) {
            return tiles;
        }

        return reorderTiles(tiles, tantrix);
    }

    /** make an ordered path list from the tiles in tileList */
    private TilePlacementList reorderTiles(TilePlacementList tileList, Tantrix tantrix) {

        LinkedList<TilePlacement> newList = new LinkedList<>();
        TilePlacement lastAdded = tileList.remove(0);
        newList.add(lastAdded);

        Iterator<Location> outgoing =
                lastAdded.getOutgoingPathLocations(primaryPathColor_).values().iterator();

        addForwardTiles(newList, outgoing.next(), tileList, tantrix);
        if (outgoing.hasNext())  {
            addBackwardTiles(newList, outgoing.next(), tileList, tantrix);
        }
        if (newList.size() != tantrix.size()) {
             throw new IllegalStateException("Did not find a " + primaryPathColor_.name() + " path among " + tileList);
        }

        return new TilePlacementList(newList);
    }

    private void addForwardTiles(LinkedList<TilePlacement> newList, Location outLocation,
                                 TilePlacementList remaining, Tantrix tantrix) {
        addTiles(newList, outLocation, remaining, tantrix, true);
    }

    private void addBackwardTiles(LinkedList<TilePlacement> newList, Location outLocation,
                                  TilePlacementList remaining, Tantrix tantrix) {
        addTiles(newList, outLocation, remaining, tantrix, false);
    }

    /** add forward or backward tiles to the beginning or end of path respectively */
    private void addTiles(LinkedList<TilePlacement> newList, Location outLocation,
                          TilePlacementList remaining, Tantrix tantrix, boolean forward) {
        TilePlacement nextPlacement = tantrix.get(outLocation);

        if (nextPlacement != null && !newList.contains(nextPlacement) && !remaining.isEmpty()) {
            if (forward)
                newList.addLast(nextPlacement);
            else {
                newList.addFirst(nextPlacement);
            }
            remaining.remove(nextPlacement);
            Collection<Location> outgoing = nextPlacement.getOutgoingPathLocations(primaryPathColor_).values();
            for (Location loc : outgoing) {
                addTiles(newList, loc, remaining, tantrix, forward);
            }
        }
    }
}
