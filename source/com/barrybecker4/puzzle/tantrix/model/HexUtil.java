// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.model;

import com.barrybecker4.common.geometry.IntLocation;
import com.barrybecker4.common.geometry.Location;

import java.awt.geom.Point2D;


/**
 *  Used to find neighboring locations in hex space.
 *  Tiles are arranged like this:
 *    0,0   0,1   0,2
 *    1,0   1,1   1,2   1,3
 *    2,0   2,1   2,2
 *
 *  @author Barry Becker
 */
public class HexUtil {

    /**
     * Odd rows are shifted back one.
     * @param loc source location
     * @param direction side to navigate to to find the neighbor. 0 is to the right.
     * @return the indicated neighbor of the specified tile.
     */
    public static Location getNeighborLocation(Location loc, int direction) {

        int row = loc.getRow();
        int col = loc.getCol();

        int colOffset = (Math.abs(row) % 2 == 1) ? -1 : 0;
        Location nbrLoc = null;

        switch (direction) {
            case 0 : nbrLoc = new IntLocation(row, col + 1); break;
            case 1 : nbrLoc = new IntLocation(row - 1, col + colOffset + 1); break;
            case 2 : nbrLoc = new IntLocation(row - 1, col + colOffset); break;
            case 3 : nbrLoc = new IntLocation(row, col - 1); break;
            case 4 : nbrLoc = new IntLocation(row + 1, col + colOffset); break;
            case 5 : nbrLoc = new IntLocation(row + 1, col + colOffset + 1); break;
            default : assert false;
        }
        return nbrLoc;
    }

    /**
     * Convert to cartesian space, then computer the distance.
     * @param loc1
     * @param loc2
     * @return distance between two hex locations.
     */
    public static double distanceBetween(Location loc1, Location loc2) {
        int row1 = loc1.getRow();
        int row2 = loc2.getRow();
        Point2D point1 = new Point2D.Double(loc1.getCol() + (row1 % 2 == 1 ? -0.5 : 0), row1);
        Point2D point2 = new Point2D.Double(loc2.getCol() + (row2 % 2 == 1 ? -0.5 : 0), row2);

        return point1.distance(point2);
    }

    /** hidden constructor */
    private HexUtil() {}
}
