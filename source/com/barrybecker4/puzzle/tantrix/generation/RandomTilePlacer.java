// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.generation;

import com.barrybecker4.common.geometry.Location;
import com.barrybecker4.common.math.MathUtil;
import com.barrybecker4.puzzle.tantrix.analysis.fitting.PrimaryPathFitter;
import com.barrybecker4.puzzle.tantrix.model.HexTile;
import com.barrybecker4.puzzle.tantrix.model.HexTileList;
import com.barrybecker4.puzzle.tantrix.model.PathColor;
import com.barrybecker4.puzzle.tantrix.model.TantrixBoard;
import com.barrybecker4.puzzle.tantrix.model.TilePlacement;
import com.barrybecker4.puzzle.tantrix.model.TilePlacementList;

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
     * Considering each unplaced tile, find a single random placement given current configuration.
     * Valid placements must extend the primary path but not necessarily match secondary paths.
     * @return a random tile placement for the current tantrix state and set of unplaced tiles.
     *  returns null if no placement is possible - such as when we have a loop, the end is blocked,
     *  or there are no more unplaced tiles.
     */
    public TilePlacement generateRandomPlacement(TantrixBoard board) {

        HexTileList unplacedTiles = (HexTileList) board.getUnplacedTiles().clone();
        Collections.shuffle(unplacedTiles, MathUtil.RANDOM);

        TilePlacement nextMove = null;
        int i=0;
        while (nextMove == null && i < unplacedTiles.size()) {
            HexTile tile = unplacedTiles.get(i++);
            nextMove = findPrimaryPathPlacementForTile(board, tile);
        }
        if (nextMove == null) {
            System.out.println("no valid placements found among " + unplacedTiles
                    + " to match existing tantrix of "+ board.getTantrix());
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
     *  [tileNum=6 colors: [Y, R, B, Y, B, R] at (row=20, column=21) ANGLE_240]
     *  [tileNum=2 colors: [B, Y, Y, B, R, R] at (row=20, column=20) ANGLE_60]
     *  [tileNum=7 colors: [R, Y, R, Y, B, B] at (row=21, column=21) ANGLE_0]
     *
     * There are these 4 valid placements:
     *  [tileNum=4 colors: [B, Y, R, B, R, Y] at (row=21, column=22) ANGLE_0],
     *  [tileNum=4 colors: [B, Y, R, B, R, Y] at (row=21, column=22) ANGLE_60],
     *  [tileNum=4 colors: [B, Y, R, B, R, Y] at (row=21, column=22) ANGLE_240],
     *  [tileNum=4 colors: [B, Y, R, B, R, Y] at (row=21, column=22) ANGLE_300]]
     *  but all of them cause retouching to the main path.
     *
     * @return a valid primary path placement.
     */
    private TilePlacement findPrimaryPathPlacementForTile(TantrixBoard board, HexTile tile) {

        TilePlacement lastPlaced = board.getLastTile();
        PrimaryPathFitter fitter = new PrimaryPathFitter(board.getTantrix(), board.getPrimaryColor());

        Map<Integer, Location> outgoing = lastPlaced.getOutgoingPathLocations(primaryColor);
        Location nextLocation = null;
        for (int i : outgoing.keySet()) {
            if (board.getTilePlacement(outgoing.get(i)) == null) {
                nextLocation = outgoing.get(i);
            }
        }
        // this could happen if there is a loop, or the openQueue end of the primary path is blocked.
        if (nextLocation == null) {
            return null;
        }

        TilePlacementList validFits = fitter.getFittingPlacements(tile, nextLocation);

        if (validFits.isEmpty())  {
            return null;
        }

        return validFits.get(MathUtil.RANDOM.nextInt(validFits.size()));
    }
}
