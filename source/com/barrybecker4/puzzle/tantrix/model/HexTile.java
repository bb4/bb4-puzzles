// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.model;

import java.util.List;

/**
 * Represents a single tantrix tile.
 * Immutable.
 * @author Barry Becker
 */
public class HexTile {

    /** number of sides on a hex tile. */
    public static final byte NUM_SIDES = 6;

    private byte tantrixNumber;
    private List<PathColor> edgeColors;
    private PathColor primaryColor;

    /**
     * Constructor.
     */
    public HexTile(byte tantrixNumber, PathColor primaryColor, List<PathColor> edgeColors) {
        assert edgeColors.size() == 6;
        this.tantrixNumber = tantrixNumber;
        this.primaryColor = primaryColor;
        this.edgeColors = edgeColors;
    }

    /** @return the number on the back of the tile */
    public byte getTantrixNumber() {
        return tantrixNumber;
    }

    public PathColor getEdgeColor(int index) {
        return edgeColors.get(index);
    }

    /** The primary path color on the back of the tile */
    public PathColor getPrimaryColor() {
        return primaryColor;
    }

    public String toString() {
        return "tileNum=" + tantrixNumber + " colors: " + edgeColors;
    }

}
