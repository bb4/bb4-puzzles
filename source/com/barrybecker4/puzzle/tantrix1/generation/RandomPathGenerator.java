// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix1.generation;

import com.barrybecker4.puzzle.tantrix1.model.TantrixBoard;
import com.barrybecker4.puzzle.tantrix1.model.TilePlacement;
import com.barrybecker4.puzzle.tantrix1.solver.path.TantrixPath;

/**
 * Generates random continuous primary color paths that do not necessarily match on secondary colors.
 *
 * @author Barry Becker
 */
public class RandomPathGenerator {

    private TantrixBoard initialBoard;
    private RandomTilePlacer tilePlacer;


    /**
     * Constructor
     */
    public RandomPathGenerator(TantrixBoard board) {
        this.initialBoard = board;
        tilePlacer = new RandomTilePlacer(board.getPrimaryColor());
    }

    /**
     * @return a random path.
     */
    public TantrixPath generateRandomPath() {

        TantrixBoard currentBoard;

        boolean foundPath = false;
        do {
            currentBoard = initialBoard;
            while (!currentBoard.getUnplacedTiles().isEmpty()) {
                TilePlacement placement = tilePlacer.generateRandomPlacement(currentBoard);
                if (placement == null) break;
                currentBoard = currentBoard.placeTile(placement);
            }
            foundPath = currentBoard.getUnplacedTiles().isEmpty();
        } while (!foundPath);

        return new TantrixPath(currentBoard.getTantrix(), initialBoard.getPrimaryColor());
    }
}
