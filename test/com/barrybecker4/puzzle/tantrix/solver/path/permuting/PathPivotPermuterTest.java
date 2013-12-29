// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path.permuting;

import com.barrybecker4.common.geometry.ByteLocation;
import com.barrybecker4.common.geometry.Location;
import com.barrybecker4.puzzle.tantrix.TantrixTstUtil;
import com.barrybecker4.puzzle.tantrix.model.HexTile;
import com.barrybecker4.puzzle.tantrix.model.PathColor;
import com.barrybecker4.puzzle.tantrix.model.Rotation;
import com.barrybecker4.puzzle.tantrix.model.TantrixBoard;
import com.barrybecker4.puzzle.tantrix.model.TilePlacement;
import com.barrybecker4.puzzle.tantrix.solver.path.TantrixPath;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.barrybecker4.puzzle.tantrix.TantrixTstUtil.TILES;
import static com.barrybecker4.puzzle.tantrix.solver.path.permuting.PathTstUtil.createPath;
import static org.junit.Assert.assertEquals;

/**
 * @author Barry Becker
 */
public class PathPivotPermuterTest {

    /** instance under test */
    private PathPivotPermuter permuter;

    @Test
    public void testPermute3TilePath() {

        TantrixBoard board = TantrixTstUtil.place3UnsolvedTiles();
        permuter = new PathPivotPermuter(new TantrixPath(board.getTantrix(), board.getPrimaryColor()));

        List<TantrixPath> permutedPathList = permuter.findPermutedPaths(1, 1);
        assertEquals("Unexpected number of permuted paths.", 7, permutedPathList.size());

        // for each of the 7 permuted paths, we expect that tile 2 will be the middle/pivot tile.
        Location lowerLeft = new ByteLocation(22, 20);
        Location lowerRight = new ByteLocation(22, 21);
        TilePlacement pivot = new TilePlacement(TILES.getTile(1), new ByteLocation(21, 21), Rotation.ANGLE_0);
        HexTile tile2 = TILES.getTile(2);
        HexTile tile3 = TILES.getTile(3);

        List<TantrixPath> expPathList =  Arrays.asList(
            createPath(new TilePlacement(tile2, lowerLeft, Rotation.ANGLE_0), pivot,
                       new TilePlacement(tile3, lowerRight, Rotation.ANGLE_240)),
            createPath(new TilePlacement(tile2, lowerLeft, Rotation.ANGLE_300), pivot,
                       new TilePlacement(tile3, lowerRight, Rotation.ANGLE_180)),
            createPath(new TilePlacement(tile2, lowerLeft, Rotation.ANGLE_300), pivot,
                       new TilePlacement(tile3, lowerRight, Rotation.ANGLE_240)), // complete loop!
            createPath(new TilePlacement(tile3, lowerLeft, Rotation.ANGLE_120), pivot,
                       new TilePlacement(tile2, lowerRight, Rotation.ANGLE_60)),
            createPath(new TilePlacement(tile3, lowerLeft, Rotation.ANGLE_120),
                       pivot, new TilePlacement(tile2, lowerRight, Rotation.ANGLE_0)),
            createPath(new TilePlacement(tile3, lowerLeft, Rotation.ANGLE_180),
                       pivot, new TilePlacement(tile2, lowerRight, Rotation.ANGLE_60)),  // complete loop!
            createPath(new TilePlacement(tile3, lowerLeft, Rotation.ANGLE_180), pivot,
                       new TilePlacement(tile2, lowerRight, Rotation.ANGLE_0))
        );

        assertEquals("Unexpected permuted paths.", expPathList, permutedPathList);
    }

    @Test
    public void testPermute9AlmostLoop() {

        TantrixBoard board = TantrixTstUtil.place9AlmostLoop();
        permuter = new PathPivotPermuter(new TantrixPath(board.getTantrix(), PathColor.RED));

        List<TantrixPath> permutedPathList = permuter.findPermutedPaths(1, 1);
        assertEquals("Unexpected number of permuted paths.", 7, permutedPathList.size());
    }
}
