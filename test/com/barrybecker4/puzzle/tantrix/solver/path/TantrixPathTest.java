// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path;

import com.barrybecker4.common.geometry.ByteLocation;
import com.barrybecker4.common.math.MathUtil;
import com.barrybecker4.puzzle.tantrix.model.HexTile;
import com.barrybecker4.puzzle.tantrix.model.PathColor;
import com.barrybecker4.puzzle.tantrix.model.Rotation;
import com.barrybecker4.puzzle.tantrix.model.TantrixBoard;
import com.barrybecker4.puzzle.tantrix.model.TilePlacement;
import com.barrybecker4.puzzle.tantrix.model.TilePlacementList;
import org.junit.Test;

import static com.barrybecker4.puzzle.tantrix.TantrixTstUtil.TILES;
import static com.barrybecker4.puzzle.tantrix.TantrixTstUtil.loc;
import static com.barrybecker4.puzzle.tantrix.TantrixTstUtil.place3NonPathTiles;
import static com.barrybecker4.puzzle.tantrix.TantrixTstUtil.place3SolvedTiles;
import static com.barrybecker4.puzzle.tantrix.TantrixTstUtil.place3UnsolvedTiles;
import static com.barrybecker4.puzzle.tantrix.solver.path.permuting.PathTstUtil.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * @author Barry Becker
 */
public class TantrixPathTest {

    /** instance under test */
    private TantrixPath path;

    @Test
    public void test2TilePathConstruction() {

        HexTile pivotTile = TILES.getTile(1);

        TilePlacement first =
                new TilePlacement(TILES.getTile(2), loc(2, 1), Rotation.ANGLE_0);
        TilePlacement second =
                new TilePlacement(TILES.getTile(3), loc(1, 2), Rotation.ANGLE_0);

        TilePlacementList tileList = new TilePlacementList(first, second);
        path = new TantrixPath(tileList, pivotTile.getPrimaryColor());

        assertEquals("Unexpected path tiles", tileList, path.getTilePlacements());
    }


    /**
     * We should get an error if the tiles are not in path order, even if they
     * do form path.
     */
    @Test(expected = IllegalStateException.class)
    public void test5TilePathConstructionWhenPathTilesUnordered() {

        TilePlacement first =
                new TilePlacement(TILES.getTile(4), new ByteLocation(21, 22), Rotation.ANGLE_0);
        TilePlacement second =
                new TilePlacement(TILES.getTile(1), new ByteLocation(22, 21), Rotation.ANGLE_300);
        TilePlacement third =
                new TilePlacement(TILES.getTile(2), new ByteLocation(23, 22), Rotation.ANGLE_180);
        TilePlacement fourth =
                new TilePlacement(TILES.getTile(3), new ByteLocation(20, 21), Rotation.ANGLE_120);
        TilePlacement fifth =
                new TilePlacement(TILES.getTile(5), new ByteLocation(21, 21), Rotation.ANGLE_240);

        TilePlacementList tileList = new TilePlacementList(first, second, third, fourth, fifth);

        new TantrixPath(tileList, PathColor.RED);
    }


    /** we expect an exception because the tiles passed to the constructor do not form a primary path */
    @Test
    public void testNonLoopPathConstruction() {
        TantrixBoard board = place3UnsolvedTiles();

        path =  new TantrixPath(board.getTantrix(), board.getPrimaryColor());
        assertEquals("Unexpected length", 3, path.size());
    }

    /** we expect an exception because the tiles passed to the constructor do not form a primary path */
    @Test(expected = IllegalStateException.class)
    public void testInvalidPathConstruction() {
        TantrixBoard board = place3NonPathTiles();

        new TantrixPath(board.getTantrix(), board.getPrimaryColor());
    }

    @Test
    public void testIsLoop() {
        TantrixBoard board = place3SolvedTiles();
        TantrixPath path = new TantrixPath(board.getTantrix(), board.getPrimaryColor());
        assertTrue("Unexpectedly not a loop", path.isLoop());
    }

    @Test
    public void testIsNotLoop() {
        TantrixBoard board = place3UnsolvedTiles();
        TantrixPath path = new TantrixPath(board.getTantrix(), board.getPrimaryColor());
        assertFalse("Unexpectedly a loop", path.isLoop());
    }

    @Test
    public void testHasOrderedPrimaryPathYellow() {
        TilePlacementList tiles = new TilePlacementList(
                new TilePlacement(TILE2, LOWER_RIGHT, Rotation.ANGLE_60),
                new TilePlacement(TILE1, UPPER, Rotation.ANGLE_0),
                new TilePlacement(TILE3, LOWER_LEFT, Rotation.ANGLE_120));

        assertTrue("Unexpectedly not a loop", TantrixPath.hasOrderedPrimaryPath(tiles, PathColor.YELLOW));
    }

    @Test
    public void testHasOrderedPrimaryPathRed() {
        TilePlacementList tiles = new TilePlacementList(
                new TilePlacement(TILE2, LOWER_RIGHT, Rotation.ANGLE_60),
                new TilePlacement(TILE1, UPPER, Rotation.ANGLE_0),
                new TilePlacement(TILE3, LOWER_LEFT, Rotation.ANGLE_120));

        assertFalse("Unexpectedly found a loop", TantrixPath.hasOrderedPrimaryPath(tiles, PathColor.RED));
    }

    @Test
    public void testFindRandomNeighbor() {
        MathUtil.RANDOM.setSeed(0);
        TantrixBoard board = place3UnsolvedTiles();
        TantrixPath path = new TantrixPath(board.getTantrix(), board.getPrimaryColor());
        TantrixPath nbr = (TantrixPath) path.getRandomNeighbor(0.5);

        TilePlacementList tiles =
                new TilePlacementList(
                        new TilePlacement(TILES.getTile(2), new ByteLocation(22, 20), Rotation.ANGLE_300),
                        new TilePlacement(TILES.getTile(1), new ByteLocation(21, 21), Rotation.ANGLE_0),
                        new TilePlacement(TILES.getTile(3), new ByteLocation(22, 21), Rotation.ANGLE_240));
        TantrixPath expectedPath = new TantrixPath(tiles, PathColor.YELLOW);

        assertEquals("Unexpected random neighbor.", expectedPath, nbr);
    }
}