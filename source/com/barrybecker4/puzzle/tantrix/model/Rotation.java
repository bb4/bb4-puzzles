// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.model;

/**
 * Valid rotations for the hexagonal tiles.
 * @author Barry Becker
 */
public enum Rotation {
    ANGLE_0,
    ANGLE_60,
    ANGLE_120,
    ANGLE_180,
    ANGLE_240,
    ANGLE_300;

    public Rotation rotateBy(int numRotations) {
        int rot = numRotations;
        while (rot < 0)  {
            rot += HexTile.NUM_SIDES;
        }
        return Rotation.values()[(this.ordinal() + rot) % HexTile.NUM_SIDES];
    }
}
