// Copyright by Barry G. Becker, 2012-2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix1.generation;

import com.barrybecker4.common.geometry.IntLocation;
import com.barrybecker4.common.math.MathUtil;
import com.barrybecker4.puzzle.tantrix1.model.PathColor;
import com.barrybecker4.puzzle.tantrix1.model.Rotation;
import com.barrybecker4.puzzle.tantrix1.model.TantrixBoard;
import com.barrybecker4.puzzle.tantrix1.model.TilePlacement;
import org.junit.Before;
import org.junit.Test;

import static com.barrybecker4.puzzle.tantrix1.TantrixTstUtil.*;
import static org.junit.Assert.assertEquals;

/**
 * @author Barry Becker
 */
public class RandomTilePlacerTest {

    /** instance under test */
    RandomTilePlacer placer;
    TantrixBoard tantrix;


    /** make sure that we get the same random placements every time we run the tests */
    @Before
    public void setUp() {
        MathUtil.RANDOM.setSeed(0);
    }

    @Test
    public void testFindRandomPlacementForFirstTileOfThreeYellowPath() {
        placer = new RandomTilePlacer(PathColor.YELLOW);
        tantrix = new TantrixBoard(THREE_TILES);
        verifyPlacement(new TilePlacement(THREE_TILES.get(1), new IntLocation(22, 21), Rotation.ANGLE_60));
    }

    @Test
    public void testFindRandomPlacementForFirstTileOfThreeRedPath() {
        placer = new RandomTilePlacer(PathColor.RED);
        tantrix = new TantrixBoard(THREE_TILES);
        verifyPlacement(new TilePlacement(THREE_TILES.get(1), new IntLocation(20, 20), Rotation.ANGLE_300));
    }

    @Test
    public void testFindRandomPlacementForTwoOfThree() {
        placer = new RandomTilePlacer(PathColor.YELLOW);
        tantrix = place2of3Tiles_OneThenTwo();
        verifyPlacement(new TilePlacement(THREE_TILES.get(2), new IntLocation(22, 20), Rotation.ANGLE_120));
    }


    /** no tile to place if already a loop */
    @Test
    public void testFindRandomPlacement3of6Unsolved() {
        placer = new RandomTilePlacer(PathColor.BLUE);
        tantrix = place3of6UnsolvedTiles();
        verifyPlacement(new TilePlacement(SIX_TILES.get(5), new IntLocation(19, 21), Rotation.ANGLE_120));
    }

    /** no tile to place if already a loop */
    @Test
    public void testFindRandomPlacement3Loop() {
        placer = new RandomTilePlacer(PathColor.YELLOW);
        tantrix = place3SolvedTiles();
        verifyPlacement(null);
    }

    /** no tile to place if already a loop */
    @Test
    public void testFindRandomPlacement3of6InLoop() {
        placer = new RandomTilePlacer(PathColor.YELLOW);
        tantrix = place3of6SolvedTiles();
        verifyPlacement(null);
    }

    @Test
    public void testFindRandomPlacementFor4UnsolvedTilesYellow() {
        placer = new RandomTilePlacer(PathColor.YELLOW);
        tantrix = place4UnsolvedTiles();
        verifyPlacement(null);
    }

    @Test
    public void testFindRandomPlacementFor4UnsolvedTilesRed() {
        placer = new RandomTilePlacer(PathColor.RED);
        tantrix = place4UnsolvedTiles();
        verifyPlacement(null);
    }

    @Test
    public void testFindRandomPlacementFor9AlmostLoopBlue() {
        placer = new RandomTilePlacer(PathColor.BLUE);
        tantrix = place9AlmostLoop();
        verifyPlacement(new TilePlacement(FOURTEEN_TILES.get(13), new IntLocation(21,23), Rotation.ANGLE_180));
    }

    @Test
    public void testFindRandomPlacementFor9AlmostLoopRed() {
        placer = new RandomTilePlacer(PathColor.RED);
        tantrix = place9AlmostLoop();
        verifyPlacement(new TilePlacement(FOURTEEN_TILES.get(13), new IntLocation(21,22), Rotation.ANGLE_300));
    }


    private void verifyPlacement(TilePlacement expPlacement) {

        TilePlacement placement = placer.generateRandomPlacement(tantrix);
        assertEquals("Unexpected placement.", expPlacement, placement);
    }


}