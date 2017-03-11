// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix1.solver.path.permuting;

import com.barrybecker4.common.math.MathUtil;
import com.barrybecker4.puzzle.tantrix1.solver.path.PathType;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static com.barrybecker4.puzzle.tantrix1.solver.path.permuting.PathTstUtil.LOOP_PATH3;
import static com.barrybecker4.puzzle.tantrix1.solver.path.permuting.PathTstUtil.NON_LOOP_PATH4;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Barry Becker
 */
public class TilesOfTypeIndicesTest {

    /** instance under test */
    private TilesOfTypeIndices indices;

    @Before
    public void setUp() {
        MathUtil.RANDOM.setSeed(0);
    }

    @Test
    public void testTIGHTIndicesIn3TileLoop() {

        indices = new TilesOfTypeIndices(PathType.TIGHT_CURVE, LOOP_PATH3);

        assertEquals("Unexpected indices.",
                Arrays.asList(0, 1, 2), indices);
    }

    @Test
    public void testWIDEIndicesIn3TileLoop() {

        indices = new TilesOfTypeIndices(PathType.WIDE_CURVE, LOOP_PATH3);

        assertTrue("Unexpected wide curve tiles found.", indices.isEmpty());
    }

    @Test
    public void testTIGHTIndicesIn4TileNonLoopPath() {

        indices = new TilesOfTypeIndices(PathType.TIGHT_CURVE, NON_LOOP_PATH4);

        assertEquals("Unexpected indices.",
                Arrays.asList(1, 3), indices);
    }

    @Test
    public void testWIDEIndicesIn4TileNonLoopPath() {

        indices = new TilesOfTypeIndices(PathType.WIDE_CURVE, NON_LOOP_PATH4);

        assertEquals("Unexpected indices.",
                Arrays.asList(0, 2), indices);
    }


    //private static TantrixPath createPath(TilePlacement placement1, TilePlacement placement2, TilePlacement placement3) {
    //    return  new TantrixPath(new TilePlacementList(placement1, placement2, placement3), PathColor.YELLOW);
    //}
}
