// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.model;

import com.barrybecker4.common.geometry.ByteLocation;
import com.barrybecker4.common.math.MathUtil;
import com.barrybecker4.puzzle.tantrix.solver.path.TantrixPath;
import org.junit.Before;
import org.junit.Test;

import static com.barrybecker4.puzzle.tantrix.TantrixTstUtil.TILES;
import static com.barrybecker4.puzzle.tantrix.TantrixTstUtil.place3UnsolvedTiles;
import static org.junit.Assert.assertEquals;

/**
 * @author Barry Becker
 */
public class RandomPathGeneratorTest {

    /** instance under test */
    private RandomPathGenerator pathGenerator;

    @Before
    public void setUp() {
        MathUtil.RANDOM.setSeed(0);
    }

    @Test
    public void test3TilesPathGen() {
        pathGenerator = new RandomPathGenerator(place3UnsolvedTiles());
        TantrixPath rPath = pathGenerator.generateRandomPath();

        System.out.println("rpath=" + rPath);
        assertEquals("Unexpected length for randomly generated path.", 3, rPath.size());

        TilePlacementList tiles =
                new TilePlacementList(
                        new TilePlacement(TILES.getTile(2), new ByteLocation(22, 20), Rotation.ANGLE_0),
                        new TilePlacement(TILES.getTile(1), new ByteLocation(21, 21), Rotation.ANGLE_0),
                        new TilePlacement(TILES.getTile(3), new ByteLocation(22, 21), Rotation.ANGLE_180));
        TantrixPath expectedPath = new TantrixPath(tiles, PathColor.YELLOW);

        assertEquals("Unexpected path.", expectedPath, rPath);
    }

}