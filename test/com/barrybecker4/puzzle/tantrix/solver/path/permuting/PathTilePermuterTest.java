// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path.permuting;

import com.barrybecker4.common.math.MathUtil;
import com.barrybecker4.puzzle.tantrix.model.PathColor;
import com.barrybecker4.puzzle.tantrix.model.Rotation;
import com.barrybecker4.puzzle.tantrix.model.TilePlacement;
import com.barrybecker4.puzzle.tantrix.model.TilePlacementList;
import com.barrybecker4.puzzle.tantrix.solver.path.TantrixPath;
import junit.framework.TestCase;

import java.util.Arrays;

import static com.barrybecker4.puzzle.tantrix.solver.path.permuting.PathTstUtil.*;

/**
 * @author Barry Becker
 */
public class PathTilePermuterTest extends TestCase {

    /** instance under test */
    private PathTilePermuter permuter;


    @Override
    public void setUp() {
        MathUtil.RANDOM.setSeed(0);
    }

    public void testPermut3TileLoop1() {
        TantrixPath path = LOOP_PATH.copy();
        permuter = new PathTilePermuter(path);

        TantrixPath permutedPath = permuter.permute(Arrays.asList(0, 1), Arrays.asList(1, 0));

        TantrixPath expPath =
            createPath(new TilePlacement(TILE1, LOWER_RIGHT, Rotation.ANGLE_240),
                       new TilePlacement(TILE2, UPPER, Rotation.ANGLE_180),
                       new TilePlacement(TILE3, LOWER_LEFT, Rotation.ANGLE_120));

        assertEquals("Unexpected permuted path.", expPath, permutedPath);
    }

    public void testPermut3TileLoop2() {
        TantrixPath path = LOOP_PATH.copy();
        permuter = new PathTilePermuter(path);
        TantrixPath permutedPath = permuter.permute(Arrays.asList(1, 2), Arrays.asList(2, 1));

        TantrixPath expPath =
                createPath(new TilePlacement(TILE2, LOWER_RIGHT, Rotation.ANGLE_60),
                           new TilePlacement(TILE3, UPPER, Rotation.ANGLE_0),
                           new TilePlacement(TILE1, LOWER_LEFT, Rotation.ANGLE_120));

        assertEquals("Unexpected permuted path.", expPath, permutedPath);
    }

    public void testPermut3TileNonLoopPath1() {

        TantrixPath path = NON_LOOP_PATH3.copy();
        permuter = new PathTilePermuter(path);
        TantrixPath permutedPath = permuter.permute(Arrays.asList(0, 1), Arrays.asList(1, 0));

        TantrixPath expPath =
                createPath(new TilePlacement(TILE1, LOWER_RIGHT, Rotation.ANGLE_240),
                           new TilePlacement(TILE2, UPPER, Rotation.ANGLE_180),
                           new TilePlacement(TILE3, LOWER_LEFT, Rotation.ANGLE_120));

        assertEquals("Unexpected permuted path.", expPath, permutedPath);
    }

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
    public void testSwapIn4TileLoopPathWideArc() {

        TantrixPath path = LOOP_PATH4.copy();
        permuter = new PathTilePermuter(path);
        try  {
            permuter.permute(Arrays.asList(0, 2), Arrays.asList(2, 0));
            fail();
        } catch (IllegalStateException e) {
            // expected
        }
    }  */

    /*
    public void testSwapIn4TileNonLoopPathWideArc() {

        TantrixPath path = NON_LOOP_PATH4.copy();
        permuter = new PathTilePermuter(path);
        try  {
            permuter.permute(Arrays.asList(0, 2), Arrays.asList(2, 0));
            fail();
        } catch (IllegalStateException e) {
            // expected
        }
    } */

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
     * Should not be able to swap a tile with itself
     *
    public void testSwapInvalid() {

        permuter = new PathTilePermuter(NON_LOOP_PATH3);
        try {
            permuter.permute(Arrays.asList(0, 0), Arrays.asList(0, 0));
            fail();
        } catch (AssertionError e) {
             //   success
        }
    }*/
}
