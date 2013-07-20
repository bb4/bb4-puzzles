// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path.permuting;

import com.barrybecker4.common.geometry.ByteLocation;
import com.barrybecker4.puzzle.tantrix.model.HexTile;
import com.barrybecker4.puzzle.tantrix.model.PathColor;
import com.barrybecker4.puzzle.tantrix.model.Rotation;
import com.barrybecker4.puzzle.tantrix.model.TilePlacement;
import com.barrybecker4.puzzle.tantrix.model.TilePlacementList;
import com.barrybecker4.puzzle.tantrix.solver.path.TantrixPath;
import junit.framework.TestCase;

import static com.barrybecker4.puzzle.tantrix.TantrixTstUtil.TILES;
import static com.barrybecker4.puzzle.tantrix.TantrixTstUtil.loc;

/**
 * Base class for sub path mutator tests.
 * @author Barry Becker
 */
public abstract class SubPathMutatorBase extends TestCase {

    /** instance under test */
    protected SubPathMutator mutator;

    /** creates the mutator to test */
    protected abstract SubPathMutator createMutator(PathColor primaryColor);

    public void testMutating1TilePath() {

        HexTile pivotTile = TILES.getTile(1);


        TilePlacement pivotTilePlacement =
                new TilePlacement(pivotTile, loc(1, 1), Rotation.ANGLE_0);
        TilePlacement firstTilePlacement =
                new TilePlacement(TILES.getTile(2), loc(2, 1), Rotation.ANGLE_0);

        TilePlacementList tileList = new TilePlacementList();
        tileList.add(firstTilePlacement);
        TantrixPath path = new TantrixPath(tileList, PathColor.YELLOW);
        mutator = createMutator(PathColor.YELLOW);

        TantrixPath resultPath = mutator.mutate(pivotTilePlacement, path);

        verifyMutated1TilePath(resultPath);
    }

    public void testMutating2TilePath() {

        HexTile pivotTile = TILES.getTile(1);

        TilePlacement pivotTilePlacement =
                new TilePlacement(pivotTile, loc(1, 1), Rotation.ANGLE_0);
        TilePlacement firstTilePlacement =
                new TilePlacement(TILES.getTile(2), loc(2, 1), Rotation.ANGLE_0);
        TilePlacement secondTilePlacement =
                new TilePlacement(TILES.getTile(3), loc(1, 2), Rotation.ANGLE_0);

        TilePlacementList tileList = new TilePlacementList();
        tileList.add(firstTilePlacement);
        tileList.add(secondTilePlacement);
        TantrixPath path = new TantrixPath(tileList, PathColor.YELLOW);
        mutator = createMutator(PathColor.YELLOW);

        TantrixPath resultPath = mutator.mutate(pivotTilePlacement, path);
        verifyMutated2TilePath(resultPath);
    }

    public void testMutating3TilePath() {

        HexTile pivotTile = TILES.getTile(3);

        TilePlacement pivotTilePlacement =
                new TilePlacement(pivotTile, new ByteLocation(20, 19), Rotation.ANGLE_300);

        TilePlacement firstTilePlacement =
                new TilePlacement(TILES.getTile(1), new ByteLocation(19, 19), Rotation.ANGLE_300);
        TilePlacement secondTilePlacement =
                new TilePlacement(TILES.getTile(5), new ByteLocation(18, 19), Rotation.ANGLE_60);
        TilePlacement thirdTilePlacement =
                new TilePlacement(TILES.getTile(2), new ByteLocation(17, 20), Rotation.ANGLE_300);

        TilePlacementList tileList = new TilePlacementList();
        tileList.add(firstTilePlacement);
        tileList.add(secondTilePlacement);
        tileList.add(thirdTilePlacement);
        TantrixPath path = new TantrixPath(tileList, PathColor.RED);
        mutator = createMutator(PathColor.RED);

        TantrixPath resultPath = mutator.mutate(pivotTilePlacement, path);
        verifyMutated3TilePath(resultPath);
    }

    public void testMutating3aTilePath() {

        HexTile pivotTile = TILES.getTile(3);

        TilePlacement pivotTilePlacement =
                new TilePlacement(pivotTile, new ByteLocation(22, 21), Rotation.ANGLE_120);

        TilePlacement firstTilePlacement =
                new TilePlacement(TILES.getTile(2), new ByteLocation(23, 21), Rotation.ANGLE_180);
        TilePlacement secondTilePlacement =
                new TilePlacement(TILES.getTile(1), new ByteLocation(22, 20), Rotation.ANGLE_300);
        TilePlacement thirdTilePlacement =
                new TilePlacement(TILES.getTile(4), new ByteLocation(21, 21), Rotation.ANGLE_0);

        TilePlacementList tileList = new TilePlacementList();
        tileList.add(firstTilePlacement);
        tileList.add(secondTilePlacement);
        tileList.add(thirdTilePlacement);
        TantrixPath path = new TantrixPath(tileList, PathColor.RED);
        mutator = createMutator(PathColor.RED);

        TantrixPath resultPath = mutator.mutate(pivotTilePlacement, path);
        verifyMutated3aTilePath(resultPath);
    }

    protected abstract void verifyMutated1TilePath(TantrixPath resultPath);
    protected abstract void verifyMutated2TilePath(TantrixPath resultPath);
    protected abstract void verifyMutated3TilePath(TantrixPath resultPath);
    protected abstract void verifyMutated3aTilePath(TantrixPath resultPath);

}