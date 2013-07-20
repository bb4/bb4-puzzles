/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.maze.model;

import com.barrybecker4.common.geometry.Location;

/**
 *  A region of space bounded by walls in the maze.
 *
 *  @author Barry Becker
 */
public class MazeCell {

    public boolean visited;

    // walls in the positive x, y directions.
    // when these are true, we render walls
    public boolean eastWall;
    public boolean southWall;

    // the 4 possible paths (e, w, n, s)
    // we show 0 or 2 of them at any given time in a cell when solving the maze
    public boolean eastPath;
    public boolean westPath ;
    public boolean northPath;
    public boolean southPath;

    private int depth;

    /** Constructor */
    public MazeCell() {
        visited = false;
        eastWall = false;
        southWall = false;
        depth = 0;
        clearPath();
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int d) {
        depth = d;
    }

    public Location getNextPosition(Location currentPosition, Location dir) {
        visited = true;
        return currentPosition.incrementOnCopy(dir);
    }

    /**
     * return to initial state.
     */
    public void clear() {
        clearPath();
        visited = false;
        depth = 0;
    }

    public void clearPath() {
        eastPath = false;
        westPath = false;
        northPath = false;
        southPath = false;
    }

    public String toString() {
       return "Cell visited=" + visited + " eastWall="  + eastWall + " southWall=" + southWall; // NON-NLS
    }
}


