// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.model.verfication;

import com.barrybecker4.puzzle.tantrix.model.PathColor;
import com.barrybecker4.puzzle.tantrix.model.TantrixBoard;
import com.barrybecker4.puzzle.tantrix.model.TilePlacement;

import static com.barrybecker4.puzzle.tantrix.model.HexTile.NUM_SIDES;

/**
 * Used to determine whether or not a given tantrix is a loop.
 *
 *  @author Barry Becker
 */
public class LoopDetector {

    TantrixBoard board;

    /**
     * Constructor.
     * @param board the tantrix state to test for solution.
     */
    public LoopDetector(TantrixBoard board) {
        this.board = board;
    }

    /**
     * True if loop of the primary color.
     * Does not check the consistency of secondary paths.
     * @return true if solved.
     */
    public boolean hasLoop() {
        if (!board.getUnplacedTiles().isEmpty()) {
            return false;
        }

        int numVisited = 0;
        TilePlacement lastTilePlaced = board.getLastTile();
        TilePlacement currentTile = lastTilePlaced;
        TilePlacement previousTile = null;
        TilePlacement nextTile;

        do {
            nextTile = findNeighborTile(currentTile, previousTile);
            previousTile = currentTile;
            currentTile = nextTile;
            numVisited++;
        } while (currentTile != null && !currentTile.equals(lastTilePlaced));

        return (numVisited == board.getNumTiles() && lastTilePlaced.equals(currentTile));
    }

    /**
     * Loop through the edges until we find the primary color.
     * If it does not direct us back to where we came from then go that way.
     * @param currentPlacement where we are now
     * @param previousTile where we were
     * @return the next tile in the path if there is one. Otherwise null.
     */
    private TilePlacement findNeighborTile(TilePlacement currentPlacement, TilePlacement previousTile) {

        for (byte i = 0; i < NUM_SIDES; i++) {
            PathColor color = currentPlacement.getPathColor(i);
            if (color == board.getPrimaryColor()) {
                TilePlacement nbr = board.getNeighbor(currentPlacement, i);
                if (nbr != null && !nbr.equals(previousTile)) {
                    return nbr;
                }
            }
        }
        return null;
    }
}
