// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path.permuting;

import com.barrybecker4.common.geometry.ByteLocation;
import com.barrybecker4.common.math.MathUtil;
import com.barrybecker4.puzzle.tantrix.TantrixTstUtil;
import com.barrybecker4.puzzle.tantrix.model.PathColor;
import com.barrybecker4.puzzle.tantrix.model.Rotation;
import com.barrybecker4.puzzle.tantrix.model.TantrixBoard;
import com.barrybecker4.puzzle.tantrix.model.TilePlacement;
import com.barrybecker4.puzzle.tantrix.model.TilePlacementList;
import com.barrybecker4.puzzle.tantrix.solver.path.PathType;
import com.barrybecker4.puzzle.tantrix.solver.path.TantrixPath;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.barrybecker4.puzzle.tantrix.TantrixTstUtil.TILES;
import static com.barrybecker4.puzzle.tantrix.solver.path.permuting.PathTstUtil.LOWER_LEFT;
import static com.barrybecker4.puzzle.tantrix.solver.path.permuting.PathTstUtil.LOWER_RIGHT;
import static com.barrybecker4.puzzle.tantrix.solver.path.permuting.PathTstUtil.TILE1;
import static com.barrybecker4.puzzle.tantrix.solver.path.permuting.PathTstUtil.TILE2;
import static com.barrybecker4.puzzle.tantrix.solver.path.permuting.PathTstUtil.TILE3;
import static com.barrybecker4.puzzle.tantrix.solver.path.permuting.PathTstUtil.UPPER;
import static org.junit.Assert.assertEquals;

/**
 * @author Barry Becker
 */
public class SameTypeTileMixerTest {

    /** instance under test */
    private SameTypeTileMixer mixer;

    @Before
    public void setUp() {
        MathUtil.RANDOM.setSeed(1);
    }

    @Test
    public void testMix3TilesTIGHT() {

        TantrixBoard board = TantrixTstUtil.place3SolvedTiles();
        mixer =
            new SameTypeTileMixer(PathType.TIGHT_CURVE, new TantrixPath(board.getTantrix(), board.getPrimaryColor()));

        List<TantrixPath> permutedPathList = mixer.findPermutedPaths();
        assertEquals("Unexpected number of permuted paths.", 3, permutedPathList.size());

        List<TantrixPath> expPathList = Arrays.asList(
            createPath(new TilePlacement(TILE3, LOWER_RIGHT, Rotation.ANGLE_240),
                       new TilePlacement(TILE2, UPPER, Rotation.ANGLE_180),
                       new TilePlacement(TILE1, LOWER_LEFT, Rotation.ANGLE_120)),
            createPath(new TilePlacement(TILE1, LOWER_RIGHT, Rotation.ANGLE_240),
                       new TilePlacement(TILE3, UPPER, Rotation.ANGLE_0),
                       new TilePlacement(TILE2, LOWER_LEFT, Rotation.ANGLE_300)),
            createPath(new TilePlacement(TILE1, LOWER_RIGHT, Rotation.ANGLE_240),
                       new TilePlacement(TILE2, UPPER, Rotation.ANGLE_180),
                       new TilePlacement(TILE3, LOWER_LEFT, Rotation.ANGLE_120))
        );

        assertEquals("Unexpected permuted paths.", expPathList, permutedPathList);
    }

    @Test
    public void testMix3TilesWIDE() {

        TantrixBoard board = TantrixTstUtil.place3SolvedTiles();
        mixer =
            new SameTypeTileMixer(PathType.WIDE_CURVE, new TantrixPath(board.getTantrix(), board.getPrimaryColor()));

        List<TantrixPath> permutedPathList = mixer.findPermutedPaths();
        assertEquals("Unexpected number of permuted paths.", 0, permutedPathList.size());
    }

    @Test
    public void testMix3TilesSTRAIGHT() {

        TantrixBoard board = TantrixTstUtil.place3SolvedTiles();
        mixer = new SameTypeTileMixer(PathType.STRAIGHT, new TantrixPath(board.getTantrix(), board.getPrimaryColor()));

        List<TantrixPath> permutedPathList = mixer.findPermutedPaths();
        assertEquals("Unexpected number of permuted paths.", 0, permutedPathList.size());
    }

    @Test
    public void testMix5TilesTIGHT() {

        TantrixPath origPath = createPathOf5Tiles();
        mixer = new SameTypeTileMixer(PathType.TIGHT_CURVE, origPath);

        List<TantrixPath> permutedPathList = mixer.findPermutedPaths();
        assertEquals("Unexpected number of permuted paths.", 1, permutedPathList.size());

        List<TantrixPath> expPathList = Arrays.asList(
                new TantrixPath(new TilePlacementList(
                        new TilePlacement(TILES.getTile(5), new ByteLocation(21, 22), Rotation.ANGLE_120),
                        new TilePlacement(TILES.getTile(2), new ByteLocation(20, 21), Rotation.ANGLE_0),
                        new TilePlacement(TILES.getTile(3), new ByteLocation(21, 21), Rotation.ANGLE_300),
                        new TilePlacement(TILES.getTile(1), new ByteLocation(20, 20), Rotation.ANGLE_300),
                        new TilePlacement(TILES.getTile(4), new ByteLocation(19, 21), Rotation.ANGLE_120)), PathColor.RED)

        );
        assertEquals("Unexpected permuted paths.", expPathList, permutedPathList);
    }

    @Test
    public void testMix5TilesWIDE() {

        TantrixPath origPath = createPathOf5Tiles();
        mixer = new SameTypeTileMixer(PathType.WIDE_CURVE, origPath);

        List<TantrixPath> permutedPathList = mixer.findPermutedPaths();
        assertEquals("Unexpected number of permuted paths.", 1, permutedPathList.size());

        List<TantrixPath> expPathList = Arrays.asList(
                new TantrixPath(new TilePlacementList(
                        new TilePlacement(TILES.getTile(5), new ByteLocation(21, 22), Rotation.ANGLE_120),
                        new TilePlacement(TILES.getTile(3), new ByteLocation(20, 21), Rotation.ANGLE_120),
                        new TilePlacement(TILES.getTile(2), new ByteLocation(21, 21), Rotation.ANGLE_180),
                        new TilePlacement(TILES.getTile(4), new ByteLocation(20, 20), Rotation.ANGLE_180),
                        new TilePlacement(TILES.getTile(1), new ByteLocation(19, 21), Rotation.ANGLE_120)),  // or 240
                    PathColor.RED)

        );
        assertEquals("Unexpected permuted paths.", expPathList, permutedPathList);
    }

    @Test
    public void testMix5TilesSTRAIGHT() {

        TantrixPath origPath = createPathOf5Tiles();
        mixer = new SameTypeTileMixer(PathType.STRAIGHT, origPath);

        List<TantrixPath> permutedPathList = mixer.findPermutedPaths();
        assertEquals("Unexpected number of permuted paths.", 0, permutedPathList.size());
    }

    private TantrixPath createPathOf5Tiles() {
        TilePlacementList tiles = new TilePlacementList(
                new TilePlacement(TILES.getTile(5), new ByteLocation(21, 22), Rotation.ANGLE_120),
                new TilePlacement(TILES.getTile(3), new ByteLocation(20, 21), Rotation.ANGLE_120),
                new TilePlacement(TILES.getTile(2), new ByteLocation(21, 21), Rotation.ANGLE_180),
                new TilePlacement(TILES.getTile(1), new ByteLocation(20, 20), Rotation.ANGLE_300),
                new TilePlacement(TILES.getTile(4), new ByteLocation(19, 21), Rotation.ANGLE_120)
        );
        return new TantrixPath(tiles, PathColor.RED);
    }

    private TantrixPath createPath(TilePlacement placement1, TilePlacement placement2, TilePlacement placement3) {
        return  new TantrixPath(new TilePlacementList(placement1, placement2, placement3), PathColor.YELLOW);
    }
}
