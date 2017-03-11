// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix1.solver.path;

/**
 * There are 3 types of path shapes on a tantrix tile
 */
public enum PathType {
    /** connects adjacent sides on a tile */
    TIGHT_CURVE,
    /** skips one side */
    WIDE_CURVE,
    /** connects opposite sides of a tile */
    STRAIGHT
}
