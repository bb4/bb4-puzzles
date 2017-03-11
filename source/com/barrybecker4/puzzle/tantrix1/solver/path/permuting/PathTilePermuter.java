// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix1.solver.path.permuting;

import com.barrybecker4.common.geometry.Location;
import com.barrybecker4.puzzle.tantrix1.analysis.fitting.PrimaryPathFitter;
import com.barrybecker4.puzzle.tantrix1.model.*;
import com.barrybecker4.puzzle.tantrix1.solver.path.TantrixPath;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Swap tiles in place in a specified originalPath.
 *
 * @author Barry Becker
 */
class PathTilePermuter {

    private TantrixPath originalPath;
    private PathColor color;

    PathTilePermuter(TantrixPath path) {
        this.originalPath = path;
        this.color = path.getPrimaryPathColor();
    }

    /**
     * Permutes the tiles at oldIndices to new positions at new Indices
     * @param oldIndices old positions in the path
     * @param newIndices new positions to place the tiles at.
     * @return the new rearranged path.
     */
    TantrixPath permute(List<Integer> oldIndices, List<Integer> newIndices) {

        TantrixPath permutedPath = originalPath.copy();
        TilePlacementList auxList = new TilePlacementList();

        assert consistent(oldIndices, newIndices);

        for (int i=0; i < oldIndices.size(); i++) {
            auxList.set(i, permutedPath.getTilePlacements().get(newIndices.get(i)));
        }

        PrimaryPathFitter fitter =
            new PrimaryPathFitter(permutedPath.getTilePlacements(), color);

        TilePlacementList origPlacements = permutedPath.getTilePlacements();
        for (int i=0; i < newIndices.size(); i++) {

            int oldIndex = oldIndices.get(i);
            TilePlacement oldPlacement = auxList.get(i);
            TilePlacement newPlacement =
                    findNewPlacement(oldPlacement.getTile(), origPlacements.get(oldIndex).getLocation(), fitter);
            origPlacements.set(oldIndex, newPlacement);
        }

        return permutedPath;
    }

    private boolean consistent(List<Integer> oldIndices, List<Integer> newIndices) {
        Set<Integer> uniqueVals = new HashSet<>();
        uniqueVals.addAll(oldIndices);
        return uniqueVals.size() == oldIndices.size() && oldIndices.containsAll(newIndices);
    }

    /**
     * @return The new placement with the tile rotated so it fits at the new location.
     */
    private TilePlacement findNewPlacement(HexTile tile, Location location, PrimaryPathFitter fitter) {

        TilePlacement newPlacement = new TilePlacement(tile, location, Rotation.ANGLE_0);

        int ct = 0;
        while (!fitter.isFit(newPlacement) && ct < HexTile.NUM_SIDES) {
            newPlacement = newPlacement.rotate();
            ct++;
        }
        if (ct >= HexTile.NUM_SIDES) {
            throw new IllegalStateException("could not fit " + tile + " at " + location + " in " + fitter.getTantrix());
        }
        return newPlacement;
    }
}
