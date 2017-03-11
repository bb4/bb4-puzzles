// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix1.solver.path.permuting;

import com.barrybecker4.puzzle.tantrix1.model.HexTile;
import com.barrybecker4.puzzle.tantrix1.model.PathColor;
import com.barrybecker4.puzzle.tantrix1.model.TilePlacementList;
import com.barrybecker4.puzzle.tantrix1.solver.path.PathType;
import com.barrybecker4.puzzle.tantrix1.solver.path.TantrixPath;

import java.util.ArrayList;
import java.util.List;

/**
 * A list of indices that point to tiles having a specified type within the primary path.
 *
 * @author Barry Becker
 */
class TilesOfTypeIndices extends ArrayList<Integer> {

    private PathColor primColor;

    /**
     * Constructor
     * @param type type of the arc on a tile.
     * @param path tantrix primary path of a single color.
     */
    TilesOfTypeIndices(PathType type, TantrixPath path) {
        initialize(type, path);
    }

    private void initialize(PathType type, TantrixPath path) {

        TilePlacementList tiles = path.getTilePlacements();
        primColor = path.getPrimaryPathColor();

        for (int i=0; i < path.size(); i++) {
            HexTile tile = tiles.get(i).getTile();

            if (isTileType(tile, type)) {
                add(i);
            }
        }
    }

    private boolean isTileType(HexTile tile, PathType type) {

        List<Integer> pathEdges = new ArrayList<>(2);
        int ct = 0;
        while (pathEdges.size() < 2) {
            if (tile.getEdgeColor(ct) == primColor) {
               pathEdges.add(ct);
            }
            ct++;
        }
        int diff = Math.abs(pathEdges.get(0) - pathEdges.get(1));
        diff = (diff == 5) ? 1 : diff;
        diff = (diff == 4) ? 2 : diff;
        return (diff == (type.ordinal() + 1));
    }
}
