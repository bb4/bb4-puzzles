// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix1.solver.path.permuting;

import com.barrybecker4.common.geometry.ByteLocation;
import com.barrybecker4.puzzle.tantrix1.model.PathColor;
import com.barrybecker4.puzzle.tantrix1.model.Rotation;
import com.barrybecker4.puzzle.tantrix1.model.TilePlacement;
import com.barrybecker4.puzzle.tantrix1.model.TilePlacementList;
import com.barrybecker4.puzzle.tantrix1.solver.path.TantrixPath;

import static com.barrybecker4.puzzle.tantrix1.TantrixTstUtil.TILES;
import static com.barrybecker4.puzzle.tantrix1.TantrixTstUtil.loc;
import static org.junit.Assert.assertEquals;

/**
 * @author Barry Becker
 */
public class SubPathReverserTest extends SubPathMutatorBase {

    @Override
    protected SubPathMutator createMutator(PathColor primaryColor) {
        return new SubPathReverser(primaryColor);
    }

    @Override
    protected void verifyMutated1TilePath(TantrixPath resultPath) {
        assertEquals("Unexpected result for " + mutator,
                1, resultPath.size());

        TilePlacement first = new TilePlacement(TILES.getTile(2), loc(2, 1), Rotation.ANGLE_60);
        TilePlacementList expList = new TilePlacementList(first);
        assertEquals("Unexpected reversal.", expList, resultPath.getTilePlacements());
    }

    @Override
    protected void verifyMutated2TilePath(TantrixPath resultPath) {
        assertEquals("unexpected size.", 2, resultPath.size());

        TilePlacement first = new TilePlacement(TILES.getTile(3), loc(2, 1), Rotation.ANGLE_180);
        TilePlacement second = new TilePlacement(TILES.getTile(2), loc(1, 2), Rotation.ANGLE_180);

        TilePlacementList expList = new TilePlacementList(first, second);
        assertEquals("Unexpected reversal.", expList, resultPath.getTilePlacements());
    }

    @Override
    protected void verifyMutated3TilePath(TantrixPath resultPath) {
        assertEquals("unexpected size.", 3, resultPath.size());

        TilePlacement first =
                new TilePlacement(TILES.getTile(2), new ByteLocation(19, 19), Rotation.ANGLE_60);
        TilePlacement second =
                new TilePlacement(TILES.getTile(5), new ByteLocation(19, 20), Rotation.ANGLE_180);
        TilePlacement third =
                new TilePlacement(TILES.getTile(1), new ByteLocation(19, 21), Rotation.ANGLE_60);

        TilePlacementList expList = new TilePlacementList(first, second, third);
        assertEquals("Unexpected reversal.", expList, resultPath.getTilePlacements());
    }

    @Override
    protected void verifyMutated3aTilePath(TantrixPath resultPath) {
        assertEquals("unexpected size.", 3, resultPath.size());

        TilePlacement first =
                new TilePlacement(TILES.getTile(4), new ByteLocation(23, 21), Rotation.ANGLE_300);
        TilePlacement second =
                new TilePlacement(TILES.getTile(1), new ByteLocation(23, 20), Rotation.ANGLE_240);
        TilePlacement third =
                new TilePlacement(TILES.getTile(2), new ByteLocation(24, 19), Rotation.ANGLE_120);

        TilePlacementList expList = new TilePlacementList(first, second, third);
        assertEquals("Unexpected reversal.", expList, resultPath.getTilePlacements());
    }
}