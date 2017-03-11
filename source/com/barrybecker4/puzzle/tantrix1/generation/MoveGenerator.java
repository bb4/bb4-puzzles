// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix1.generation;

import com.barrybecker4.common.geometry.Location;
import com.barrybecker4.puzzle.tantrix1.analysis.fitting.TantrixTileFitter;
import com.barrybecker4.puzzle.tantrix1.model.HexTile;
import com.barrybecker4.puzzle.tantrix1.model.HexTileList;
import com.barrybecker4.puzzle.tantrix1.model.TantrixBoard;
import com.barrybecker4.puzzle.tantrix1.model.TilePlacementList;

import java.util.Set;

/**
 * Tantrix puzzle move generator. Generates valid next moves given the current state.
 *
 * @author Barry Becker
 */
public class MoveGenerator {

    private TantrixBoard board;

    /** a set of all the places that a tile might be placed next. */
    private Set<Location> borderSpaces;


    /**
     * Constructor
     */
    public MoveGenerator(TantrixBoard board) {
        this.board = board;
        this.borderSpaces =
            new BorderFinder(board.getTantrix(), board.getNumTiles(), board.getPrimaryColor()).findBorderPositions();
    }

    /**
     * For each unplaced tile, find all valid placements given current configuration.
     * Valid placements must extend the primary path.
     * @return List of all valid tile placements for the current tantrix state.
     */
    public TilePlacementList generateMoves() {
        TilePlacementList moves = new TilePlacementList();
        HexTileList unplacedTiles = board.getUnplacedTiles();

        for (HexTile tile : unplacedTiles) {
            moves.addAll(findPlacementsForTile(tile));
        }
        return moves;
    }

    /**
     * @return list of all the legal placements for the specified tile.
     */
    private TilePlacementList findPlacementsForTile(HexTile tile) {
        TilePlacementList placements = new TilePlacementList();
        TantrixTileFitter fitter =
                new TantrixTileFitter(board.getTantrix(), board.getPrimaryColor());

        for (Location loc : borderSpaces)  {
            TilePlacementList validFits = fitter.getFittingPlacements(tile, loc);
            placements.addAll(validFits);
        }

        return placements;
    }
}
