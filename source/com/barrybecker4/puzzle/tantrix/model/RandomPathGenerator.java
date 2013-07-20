// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.model;

import com.barrybecker4.puzzle.tantrix.solver.path.TantrixPath;

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

        TantrixBoard currentBoard = initialBoard;

        while (!currentBoard.getUnplacedTiles().isEmpty()) {
            TilePlacement placement = tilePlacer.generatePlacement(currentBoard);
            //if (placement == null) {
            //    currentBoard.undoLastPlaced();
            //}
            currentBoard = currentBoard.placeTile(placement);
        }
        return new TantrixPath(currentBoard.getTantrix(), initialBoard.getPrimaryColor());
    }
}
