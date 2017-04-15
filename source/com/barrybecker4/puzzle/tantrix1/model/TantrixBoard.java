// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix1.model;

import com.barrybecker4.common.geometry.Box;
import com.barrybecker4.common.geometry.ByteLocation;
import com.barrybecker4.common.geometry.Location;
import com.barrybecker4.puzzle.tantrix1.model.analysis.fitting.TantrixTileFitter;
import com.barrybecker4.puzzle.tantrix1.solver.verification.SolutionVerifier;

import java.util.Set;

/**
 *  Immutable representation of the current state of the tantrix puzzle.
 *
 *  @author Barry Becker
 */
public class TantrixBoard {

    /** starting position. must be odd I believe. */
    public static final Location INITIAL_LOCATION = new ByteLocation(21, 21);

    /** The 'tantrix'. Map of locations to currently placed tiles. */
    private Tantrix tantrix;

    /** color of the loop path */
    private PathColor primaryColor;

    /** tiles that have not yet been placed on the tantrix */
    private HexTileList unplacedTiles;

    /** number of tiles in the puzzle */
    private byte numTiles;


    /**
     * Constructor that creates a new tantrix instance when placing a move.
     * If the new tile to be placed is in the edge row of the grid, then
     * we need to increase the size of the grid by one in that direction and
     * also only render the inside cells.
     * @param board current tantrix state.
     * @param placement new piece to add to the tantrix and its positioning.
     */
    public TantrixBoard(TantrixBoard board, TilePlacement placement) {
        initializeFromOldBoard(board);

        assert unplacedTiles != null;
        assert placement != null;
        boolean removed = unplacedTiles.remove(placement.getTile());
        assert(removed) : "Did not remove " + placement.getTile() + " from " + unplacedTiles;
        tantrix = tantrix.placeTile(placement);
    }

    public TantrixBoard(HexTileList initialTiles) {

        HexTileList tileList = (HexTileList) initialTiles.clone();
        numTiles = (byte) tileList.size();
        primaryColor = new HexTiles().getTile(numTiles).getPrimaryColor();
        HexTile tile = tileList.remove(0);
        unplacedTiles = (HexTileList) tileList.clone();

        tantrix = new Tantrix(tantrix, new TilePlacement(tile, INITIAL_LOCATION, Rotation.ANGLE_0));
    }

    public Tantrix getTantrix() {
        return tantrix;
    }

    /**
     * Create a board with the specified tile placements (nothing unplaced).
     * @param tiles  specific placements to initialize the board with.
     */
    public TantrixBoard(TilePlacementList tiles, PathColor primaryColor) {
        this.numTiles = (byte)tiles.size();
        this.primaryColor = primaryColor;
        this.unplacedTiles = new HexTileList();
        this.tantrix = new Tantrix(tiles);
    }

    /**
     * Take the specified tile and place it where indicated.
     * @param placement the placement containing the new tile to place.
     * @return the new immutable tantrix instance.
     */
    public TantrixBoard placeTile(TilePlacement placement) {
        return new TantrixBoard(this, placement);
    }

    /**
     * @return true if the puzzle is solved.
     */
    public boolean isSolved() {
        return new SolutionVerifier(this).isSolved();
    }

    /**
     * @param currentPlacement where we are now
     * @param direction side to navigate to to find the neighbor. 0 is to the right.
     * @return the indicated neighbor of the specified tile.
     */
    public TilePlacement getNeighbor(TilePlacement currentPlacement, byte direction) {
        return tantrix.getNeighbor(currentPlacement, direction);
    }

    /**
     * The tile fits if the primary path and all the other paths match for edges that have neighbors.
     * @param placement the tile to check for a valid fit.
     * @return true of the tile fits
     */
    public boolean fits(TilePlacement placement) {

        TantrixTileFitter fitter = new TantrixTileFitter(tantrix, getPrimaryColor());
        return fitter.isFit(placement);
    }

    public HexTileList getUnplacedTiles() {
        return unplacedTiles;
    }

    public TilePlacement getLastTile() {
        return tantrix.getLastTile();
    }

    public PathColor getPrimaryColor() {
        return primaryColor;
    }

    public int getNumTiles() {
        return numTiles;
    }

    /**
     * @return a list of all the tiles in the puzzle (both placed and unplaced)
     */
    public HexTileList getAllTiles() {
        HexTileList tiles = new HexTileList();
        tiles.addAll(getUnplacedTiles());
        for (Location loc : getTantrixLocations()) {
            tiles.add(getTilePlacement(loc).getTile());
        }
        return tiles;
    }

    public int getEdgeLength() {
        return tantrix.getEdgeLength();
    }

    /**
     * @return the position of the top left bbox corner
     */
    public Box getBoundingBox() {
        return tantrix.getBoundingBox();
    }

    public Set<Location> getTantrixLocations() {
        return tantrix.keySet();
    }

    /**
     * @param location get the tile placement for this location.
     * @return null of there is no placement at that location.
     */
    public TilePlacement getTilePlacement(Location location) {
        return tantrix.get(location);
    }

    public boolean isEmpty(Location loc) {
        return getTilePlacement(loc) == null;
    }

    public String toString() {
        StringBuilder bldr = new StringBuilder("\n");
        for (Location loc: tantrix.keySet()) {
             TilePlacement placement = tantrix.get(loc);
             bldr.append(placement);
             bldr.append(" ");
        }

        return bldr.toString();
    }

    /**
     * Initialize the state of the board given some other board
     * @param board the board to initialize from.
     */
    private void initializeFromOldBoard(TantrixBoard board) {

        this.primaryColor = board.primaryColor;
        this.unplacedTiles = (HexTileList) board.unplacedTiles.clone();
        this.numTiles = board.numTiles;
        this.tantrix = new Tantrix(board.tantrix);
    }
}
