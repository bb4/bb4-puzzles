// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix1.solver.verification;

import com.barrybecker4.common.geometry.Box;
import com.barrybecker4.common.geometry.ByteLocation;
import com.barrybecker4.common.geometry.Location;
import com.barrybecker4.puzzle.tantrix1.model.HexUtil;
import com.barrybecker4.puzzle.tantrix1.model.Tantrix;

import java.util.*;

import static com.barrybecker4.puzzle.tantrix1.model.HexTile.NUM_SIDES;

/**
 * Used to determine if a candidate solution has empty spaces within the tantrix.
 * A val;id solution cannot have such spaces.
 *
 *  @author Barry Becker
 */
public class InnerSpaceDetector {

    private Tantrix tantrix;

    /**
     * Constructor.
     * @param tantrix the tantrix state to test for solution.
     */
    public InnerSpaceDetector(Tantrix tantrix) {
        this.tantrix = tantrix;
    }

    /**
     * Start with an empty position on the border of the bbox.
     * Do a seed fill to visit all the spaces connected to that.
     * Finally, if there are any empty spaces inside the bbox that are not visited,
     * then there are inner spaces and it is not a valid solution.
     * @return true if there are no inner empty spaces.
     */
    public boolean hasInnerSpaces() {

        Set<Location> seedEmpties = findEmptyBorderPositions();
        Set<Location> visited = findConnectedEmpties(seedEmpties);
        return !allEmptiesVisited(visited);
    }

    /**
     * @return all the empty positions on the border
     */
    private Set<Location> findEmptyBorderPositions() {

        Box bbox = tantrix.getBoundingBox();
        Set<Location> empties = new HashSet<Location>();

        for (int i = bbox.getMinCol(); i <= bbox.getMaxCol(); i++) {
            Location loc = new ByteLocation(bbox.getMinRow(), i);
            if (tantrix.get(loc) == null)  {
                empties.add(loc);
            }
            loc = new ByteLocation(bbox.getMaxRow(), i);
            if (tantrix.get(loc) == null)  {
                empties.add(loc);
            }
        }

        for (int i = bbox.getMinRow() + 1; i < bbox.getMaxRow(); i++) {
            Location loc = new ByteLocation(i, bbox.getMinCol());
            if (tantrix.get(loc) == null)  {
                empties.add(loc);
            }
            loc = new ByteLocation(i, bbox.getMaxCol());
            if (tantrix.get(loc) == null)  {
                empties.add(loc);
            }
        }

        int totalLocs = (bbox.getHeight() + 1) * (bbox.getWidth() + 1);
        assert (totalLocs == tantrix.size() || empties.size() > 0):
                "We should have found at least one empty position on the border. Num Tiles ="
                + tantrix.size() + " bbox area = " + bbox.getArea();
        return empties;
    }

    private Set<Location> findConnectedEmpties(Set<Location> seedEmpties) {
        Set<Location> visited = new HashSet<Location>();

        Queue<Location> searchQueue = new LinkedList<Location>();
        searchQueue.addAll(seedEmpties);
        visited.addAll(seedEmpties);

        while (!searchQueue.isEmpty()) {
            Location loc = searchQueue.remove();
            List<Location> nbrEmpties = findEmptyNeighborLocations(loc);
            for (Location empty : nbrEmpties) {
                if (!visited.contains(empty)) {
                    visited.add(empty);
                    searchQueue.add(empty);
                }
            }
        }

        return visited;
    }

    /**
     * @return all the empty neighbor positions next to the current one.
     */
    private List<Location> findEmptyNeighborLocations(Location loc) {
        List<Location> emptyNbrLocations = new LinkedList<Location>();
        Box bbox = tantrix.getBoundingBox();

        for (byte i=0; i< NUM_SIDES; i++) {

            Location nbrLoc = HexUtil.getNeighborLocation(loc, i);
            if (tantrix.get(nbrLoc) == null && bbox.contains(nbrLoc)) {
                emptyNbrLocations.add(nbrLoc);
            }
        }
        return emptyNbrLocations;
    }

    /**
     * @param visited set of visited empties.
     * @return true if any empties in the tantrix bbox are not visited
     */
    private boolean allEmptiesVisited(Set<Location> visited) {
        Box bbox = tantrix.getBoundingBox();
        for (int i = bbox.getMinRow(); i < bbox.getMaxRow(); i++) {
            for (int j = bbox.getMinCol(); j <= bbox.getMaxCol(); j++)  {
                Location loc = new ByteLocation(i, j);
                if (tantrix.get(loc) == null && !visited.contains(loc))  {
                    return false;
                }
            }
        }
        return true;
    }

}
