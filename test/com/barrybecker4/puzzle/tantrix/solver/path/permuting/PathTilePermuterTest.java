// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path.permuting;

import com.barrybecker4.common.math.MathUtil;
import com.barrybecker4.puzzle.tantrix.model.PathColor;
import com.barrybecker4.puzzle.tantrix.model.Rotation;
import com.barrybecker4.puzzle.tantrix.model.TilePlacement;
import com.barrybecker4.puzzle.tantrix.model.TilePlacementList;
import com.barrybecker4.puzzle.tantrix.solver.path.TantrixPath;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static com.barrybecker4.puzzle.tantrix.solver.path.permuting.PathTstUtil.*;
import static org.junit.Assert.assertEquals;

/**
 * @author Barry Becker
 */
public class PathTilePermuterTest {

    /** instance under test */
    private PathTilePermuter permuter;

    @Before
    public void setUp() {
        MathUtil.RANDOM.setSeed(0);
    }

    @Test
    public void testPermute3TileLoopFirstTwoPermuted() {
        TantrixPath path = LOOP_PATH3.copy();
        permuter = new PathTilePermuter(path);

        TantrixPath permutedPath = permuter.permute(Arrays.asList(0, 1), Arrays.asList(1, 0));

        TantrixPath expPath =
            createPath(new TilePlacement(TILE1, LOWER_RIGHT, Rotation.ANGLE_240),
                       new TilePlacement(TILE2, UPPER, Rotation.ANGLE_180),
                       new TilePlacement(TILE3, LOWER_LEFT, Rotation.ANGLE_120));

        assertEquals("Unexpected permuted path.", expPath, permutedPath);
    }

    @Test
    public void testPermute3TileLoopSecond2Permuted() {
        TantrixPath path = LOOP_PATH3.copy();
        permuter = new PathTilePermuter(path);
        TantrixPath permutedPath = permuter.permute(Arrays.asList(1, 2), Arrays.asList(2, 1));

        TantrixPath expPath =
                createPath(new TilePlacement(TILE2, LOWER_RIGHT, Rotation.ANGLE_60),
                           new TilePlacement(TILE3, UPPER, Rotation.ANGLE_0),
                           new TilePlacement(TILE1, LOWER_LEFT, Rotation.ANGLE_120));

        assertEquals("Unexpected permuted path.", expPath, permutedPath);
    }

    @Test
    public void testPermute3TileLoopAll3PermutedA() {
        TantrixPath path = LOOP_PATH3.copy();
        permuter = new PathTilePermuter(path);

        TantrixPath permutedPath = permuter.permute(Arrays.asList(0, 1, 2), Arrays.asList(2, 1, 0));

        TantrixPath expPath =
            createPath(new TilePlacement(TILE3, LOWER_RIGHT, Rotation.ANGLE_240),
                       new TilePlacement(TILE1, UPPER, Rotation.ANGLE_0),
                       new TilePlacement(TILE2, LOWER_LEFT, Rotation.ANGLE_300));

        assertEquals("Unexpected permuted path.", expPath, permutedPath);
    }


    @Test
    public void testPermute3TileLoopAll3PermutedB() {
        TantrixPath path = LOOP_PATH3.copy();
        permuter = new PathTilePermuter(path);

        TantrixPath permutedPath = permuter.permute(Arrays.asList(0, 1, 2), Arrays.asList(1, 2, 0));

        TantrixPath expPath =
            createPath(new TilePlacement(TILE1, LOWER_RIGHT, Rotation.ANGLE_240),
                       new TilePlacement(TILE3, UPPER, Rotation.ANGLE_0),
                       new TilePlacement(TILE2, LOWER_LEFT, Rotation.ANGLE_300));

        assertEquals("Unexpected permuted path.", expPath, permutedPath);
    }

    @Test
    public void testPermute3TileNonLoopPath1() {

        TantrixPath path = NON_LOOP_PATH3.copy();
        permuter = new PathTilePermuter(path);
        TantrixPath permutedPath = permuter.permute(Arrays.asList(0, 1), Arrays.asList(1, 0));

        TantrixPath expPath =
                createPath(new TilePlacement(TILE1, LOWER_RIGHT, Rotation.ANGLE_240),
                           new TilePlacement(TILE2, UPPER, Rotation.ANGLE_180),
                           new TilePlacement(TILE3, LOWER_LEFT, Rotation.ANGLE_120));

        assertEquals("Unexpected permuted path.", expPath, permutedPath);
    }

    @Test
    public void testSwapIn3TileNonLoopPath2() {

        TantrixPath path = NON_LOOP_PATH3.copy();
        permuter = new PathTilePermuter(path);

        TantrixPath permutedPath =  permuter.permute(Arrays.asList(1, 2), Arrays.asList(2, 1));

        TantrixPath expPath =
                createPath(new TilePlacement(TILE2, LOWER_RIGHT, Rotation.ANGLE_0),
                        new TilePlacement(TILE3, UPPER, Rotation.ANGLE_0),
                        new TilePlacement(TILE1, LOWER_LEFT, Rotation.ANGLE_180));


        assertEquals("Unexpected permuted path.", expPath, permutedPath);
    }
  /*
   java.lang.IllegalStateException: could not fit
      tileNum=6 colors: [Y, R, B, Y, B, R] at (row=22, column=23)
    in
     [tileNum=1 colors: [R, B, R, B, Y, Y] at (row=22, column=23) ANGLE_0]
     [tileNum=2 colors: [B, Y, Y, B, R, R] at (row=22, column=22) ANGLE_0]
     [tileNum=4 colors: [B, Y, R, B, R, Y] at (row=22, column=21) ANGLE_180]
     [tileNum=5 colors: [R, B, B, R, Y, Y] at (row=22, column=20) ANGLE_300]
     [tileNum=7 colors: [R, Y, R, Y, B, B] at (row=21, column=24) ANGLE_300]
     [tileNum=6 colors: [Y, R, B, Y, B, R] at (row=21, column=21) ANGLE_120]
     [tileNum=3 colors: [B, B, R, R, Y, Y] at (row=21, column=23) ANGLE_300]
   */

    @Test
    public void testSwapIn4TileLoopPathWideArc() {

        TantrixPath path = LOOP_PATH4.copy();
        permuter = new PathTilePermuter(path);

        permuter.permute(Arrays.asList(0, 2), Arrays.asList(2, 0));
    }

    @Test
    public void testSwapIn4TileNonLoopPathWideArc() {

        TantrixPath path = NON_LOOP_PATH4.copy();
        permuter = new PathTilePermuter(path);
        permuter.permute(Arrays.asList(0, 2), Arrays.asList(2, 0));
    }

    @Test
    public void testSwapIn4TileNonLoopPathTightArc() {

        TantrixPath path = NON_LOOP_PATH4.copy();
        permuter = new PathTilePermuter(path);
        TantrixPath permutedPath = permuter.permute(Arrays.asList(1, 3), Arrays.asList(3, 1));

        TantrixPath expPath =
            new TantrixPath(new TilePlacementList(
                new TilePlacement(TILE1, LOWER_LEFT, Rotation.ANGLE_120),
                new TilePlacement(TILE3, UPPER_LEFT, Rotation.ANGLE_180),
                new TilePlacement(TILE4, UPPER, Rotation.ANGLE_60),
                new TilePlacement(TILE2, LOWER_RIGHT, Rotation.ANGLE_180)),
            PathColor.RED);

        assertEquals("Unexpected permuted path.", expPath, permutedPath);
    }

    /**
     * We get an exception if the tiles cannot be swapped.
     * Should not be able to swap a tile with itself.
     */
    @Test (expected = AssertionError.class)
    public void testSwapDuplicateIdices() {

        permuter = new PathTilePermuter(NON_LOOP_PATH3);
        permuter.permute(Arrays.asList(0, 0), Arrays.asList(0, 0));
    }

    /**
     * We get an exception if the tiles cannot be swapped.
     * The permutation indices need to match.
     */
    @Test (expected = AssertionError.class)
    public void testSwapInvalidIndices() {

        permuter = new PathTilePermuter(NON_LOOP_PATH3);
        permuter.permute(Arrays.asList(0, 2), Arrays.asList(0, 1));
    }
}
