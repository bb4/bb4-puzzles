// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.model;

import com.barrybecker4.common.geometry.Location;
import com.barrybecker4.common.math.MathUtil;
import com.barrybecker4.puzzle.tantrix.model.fitting.PrimaryPathFitter;

import java.util.Collections;
import java.util.Map;

/**
 * Generates random continuous primary color paths that do not necessarily match on secondary colors.
 *
 * @author Barry Becker
 */
public class RandomTilePlacer {

    private PathColor primaryColor;

    /**
     * Constructor
     */
    public RandomTilePlacer(PathColor primaryColor) {
        this.primaryColor = primaryColor;
    }

    /**
     * For each unplaced tile, find all valid placements given current configuration.
     * Valid placements must extend the primary path but not necessarily match secondary paths.
     * @return List of all valid tile placements for the current tantrix state.
     *  returns null of no placement is possible.
     */
    public TilePlacement generatePlacement(TantrixBoard board) {

        HexTileList unplacedTiles = (HexTileList) board.getUnplacedTiles().clone();
        Collections.shuffle(unplacedTiles, MathUtil.RANDOM);

        TilePlacement nextMove = null;
        int i=0;
        while (nextMove == null && i<unplacedTiles.size())   {
            HexTile tile = unplacedTiles.get(i++);
            boolean isLast = unplacedTiles.isEmpty();
            nextMove = findPrimaryPathPlacementForTile(board, tile, isLast);
        }

        if (nextMove == null) {
            throw new IllegalStateException("We could not find a placement on \n" + board
                    + "\nusing these unplaced tiles:"  + unplacedTiles + " primColor="+ board.getPrimaryColor());
        }
        return nextMove;
    }

    /**
     * A random placement for the specified tile which matches the primary path,
     * but not necessarily the secondary paths. The opposite end of the primary path can only
     * retouch the tantrix if it is the last tile to be placed in the random path.
     *
     * There are usually two ways to place a tile, but there are some rare cases where there can be four.
     * For example, given:
     * [tileNum=6 colors: [Y, R, B, Y, B, R] at (row=20, column=21) ANGLE_240]
     * [tileNum=2 colors: [B, Y, Y, B, R, R] at (row=20, column=20) ANGLE_60]
     * [tileNum=7 colors: [R, Y, R, Y, B, B] at (row=21, column=21) ANGLE_0]
     *
     * There are these 4 valid placements:
     *  [tileNum=4 colors: [B, Y, R, B, R, Y] at (row=21, column=22) ANGLE_0],
     *  [tileNum=4 colors: [B, Y, R, B, R, Y] at (row=21, column=22) ANGLE_60],
     *  [tileNum=4 colors: [B, Y, R, B, R, Y] at (row=21, column=22) ANGLE_240],
     *  [tileNum=4 colors: [B, Y, R, B, R, Y] at (row=21, column=22) ANGLE_300]]
     *  but all of them cause retouching to the main path.
     *
     * @return a valid primary pth placement.
     */
    private TilePlacement findPrimaryPathPlacementForTile(TantrixBoard board, HexTile tile, boolean isLast) {

        TilePlacement lastPlaced = board.getLastTile();
        PrimaryPathFitter fitter = new PrimaryPathFitter(board.getTantrix(), board.getPrimaryColor());

        Map<Integer, Location> outgoing = lastPlaced.getOutgoingPathLocations(primaryColor);
        Location nextLocation = null;
        for (int i : outgoing.keySet()) {
            if (board.getTilePlacement(outgoing.get(i)) == null) {
                nextLocation = outgoing.get(i);
            }
        }
        assert nextLocation != null;

        TilePlacementList validFits =
                fitter.getFittingPlacements(tile, nextLocation);

        if (validFits.isEmpty())  {
            return null;
        }

        return validFits.get(MathUtil.RANDOM.nextInt(validFits.size()));
    }
}
