// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path.permuting;

import com.barrybecker4.common.geometry.Location;
import com.barrybecker4.puzzle.tantrix.model.HexTile;
import com.barrybecker4.puzzle.tantrix.model.PathColor;
import com.barrybecker4.puzzle.tantrix.model.Rotation;
import com.barrybecker4.puzzle.tantrix.model.TilePlacement;
import com.barrybecker4.puzzle.tantrix.model.TilePlacementList;
import com.barrybecker4.puzzle.tantrix.model.fitting.PrimaryPathFitter;
import com.barrybecker4.puzzle.tantrix.solver.path.TantrixPath;

import java.util.List;

/**
 * Swap tiles in place in a specified originalPath.
 *
 * @author Barry Becker
 */
public class PathTilePermuter {

    private TantrixPath originalPath;
    private PathColor color;

    public PathTilePermuter(TantrixPath path) {
        this.originalPath = path;
        this.color = path.getPrimaryPathColor();
    }

    public TantrixPath permute(List<Integer> oldIndices, List<Integer> newIndices) {
        TantrixPath permutedPath = originalPath.copy();

        TilePlacementList auxList = new TilePlacementList();

        //System.out.println("new indices = " + newIndices);
        //System.out.println("old indices = " + oldIndices);

        for (int i=0; i<oldIndices.size(); i++) {
           auxList.set(i, permutedPath.getTilePlacements().get(newIndices.get(i)));
        }

        PrimaryPathFitter fitter =
                new PrimaryPathFitter(permutedPath.getTilePlacements(), color);

        TilePlacementList origPlacements = permutedPath.getTilePlacements();
        for (int i=0; i<newIndices.size(); i++) {

            int oldIndex = oldIndices.get(i);
            TilePlacement oldPlacement = auxList.get(i);
            TilePlacement newPlacement =
                    findNewPlacement(oldPlacement.getTile(), origPlacements.get(oldIndex).getLocation(), fitter);
            origPlacements.set(oldIndex, newPlacement);
        }

        return permutedPath;
    }

    /**
     * @return The new placement with the tile rotated so it fits at the new location.
     */
    private TilePlacement findNewPlacement(HexTile tile, Location location, PrimaryPathFitter fitter) {
        TilePlacement newPlacement =
                new TilePlacement(tile, location, Rotation.ANGLE_0);
        int ct = 0;
        while (!fitter.isFit(newPlacement) && ct < HexTile.NUM_SIDES) {
            //System.out.println("new placement = " + newPlacement);
            newPlacement = newPlacement.rotate();
            ct++;
        }
        if (ct >= HexTile.NUM_SIDES) {
            throw new IllegalStateException("could not fit " + tile + " at " + location + " in " + fitter.getTantrix());
        }
        return newPlacement;
    }
}
