// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.model;

import com.barrybecker4.common.geometry.Location;

import java.util.HashMap;
import java.util.Map;

import static com.barrybecker4.puzzle.tantrix.model.HexTile.NUM_SIDES;

/**
 * Represents the positioning of a tantrix tile on the tantrix.
 * Immutable.
 * @author Barry Becker
 */
public class TilePlacement {

    private Location location;
    private Rotation rotation;
    private HexTile tile;

    /**
     * Constructor.
     */
    public TilePlacement(HexTile tile, Location location, Rotation rotation) {
        assert location != null;
        assert rotation != null;

        assert tile != null;
        this.tile = tile;
        this.location = location;
        this.rotation = rotation;
    }

    /**
     * Constructor for empty location.
     */
    public TilePlacement(Location location) {
        this(null, location, Rotation.ANGLE_0);
    }

    /**
     * Constructor.
     */
    public HexTile getTile() {
        return tile;
    }

    public PathColor getPathColor(int i) {
        int index = (i - rotation.ordinal()) % NUM_SIDES;
        index = (index < 0) ? index + NUM_SIDES : index;
        return tile.getEdgeColor(index);
    }

    public Location getLocation() {
        return location;
    }

    /** @return the amount that the tile is rotated. */
    public Rotation getRotation() {
        return rotation;
    }

    /**
     * Turn the new tile based on the old tile, but rotated counter-clockwise once.
     * @return new immutable TilePlacement instance.
     */
    public TilePlacement rotate() {
        Rotation newRotation = Rotation.values()[(rotation.ordinal() + 1) % NUM_SIDES];
        return new TilePlacement(tile, location, newRotation);
    }

    public Map<Integer, Location> getOutgoingPathLocations(PathColor primaryColor) {
        Map<Integer, Location> outgoingPathLocations = new HashMap<Integer, Location>();
        for (int i=0; i < NUM_SIDES; i++)  {
            if (primaryColor == getPathColor(i)) {
                outgoingPathLocations.put(i, HexUtil.getNeighborLocation(getLocation(), i));
            }
        }

        assert outgoingPathLocations.size() == 2: "There must always be two paths. Instead had "
                + outgoingPathLocations + " for "+this+" pcolor="+ primaryColor;
        return outgoingPathLocations;
    }


    public String toString() {
        return  "["  + tile +" at " + location + " " + rotation + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TilePlacement that = (TilePlacement) o;
        if (location != null ? !location.equals(that.location) : that.location != null) return false;
        if (rotation != that.rotation) return false;
        if (tile != null ? !tile.equals(that.tile) : that.tile != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = location != null ? location.hashCode() : 0;
        result = 31 * result + (rotation != null ? rotation.hashCode() : 0);
        result = 31 * result + (tile != null ? tile.hashCode() : 0);
        return result;
    }
}
