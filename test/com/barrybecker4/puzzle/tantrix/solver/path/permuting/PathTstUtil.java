// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path.permuting;

import com.barrybecker4.common.geometry.ByteLocation;
import com.barrybecker4.common.geometry.Location;
import com.barrybecker4.puzzle.tantrix.model.HexTile;
import com.barrybecker4.puzzle.tantrix.model.PathColor;
import com.barrybecker4.puzzle.tantrix.model.Rotation;
import com.barrybecker4.puzzle.tantrix.model.TilePlacement;
import com.barrybecker4.puzzle.tantrix.model.TilePlacementList;
import com.barrybecker4.puzzle.tantrix.solver.path.TantrixPath;

import java.util.Arrays;
import java.util.List;

import static com.barrybecker4.puzzle.tantrix.TantrixTstUtil.TILES;

/**
 * @author Barry Becker
 */
public class PathTstUtil {

    public static final Location LOWER_LEFT = new ByteLocation(22, 20);
    public static final Location LOWER_RIGHT = new ByteLocation(22, 21);
    public static final Location UPPER = new ByteLocation(21, 21);
    public static final Location UPPER_LEFT = new ByteLocation(21, 20);

    public static final HexTile TILE1 = TILES.getTile(1);
    public static final HexTile TILE2 = TILES.getTile(2);
    public static final HexTile TILE3 = TILES.getTile(3);
    public static final HexTile TILE4 = TILES.getTile(4);

    public static final TantrixPath LOOP_PATH3 =
            createPath(
                    new TilePlacement(TILE2, LOWER_RIGHT, Rotation.ANGLE_60),
                    new TilePlacement(TILE1, UPPER, Rotation.ANGLE_0),
                    new TilePlacement(TILE3, LOWER_LEFT, Rotation.ANGLE_120));

    /** left end of the yellow path is blocked by tile 2 */
    public static final TantrixPath NON_LOOP_PATH3 =
            createPath(
                    new TilePlacement(TILE2, LOWER_RIGHT, Rotation.ANGLE_0),
                    new TilePlacement(TILE1, UPPER, Rotation.ANGLE_0),
                    new TilePlacement(TILE3, LOWER_LEFT, Rotation.ANGLE_120));

    public static final TantrixPath LOOP_PATH4 = new TantrixPath(
            new TilePlacementList(
                    new TilePlacement(TILE1, LOWER_LEFT, Rotation.ANGLE_0),
                    new TilePlacement(TILE3, LOWER_RIGHT, Rotation.ANGLE_0),
                    new TilePlacement(TILE4, UPPER, Rotation.ANGLE_60),
                    new TilePlacement(TILE2, UPPER_LEFT, Rotation.ANGLE_60)),
            PathColor.RED);

    public static final TantrixPath NON_LOOP_PATH4 = new TantrixPath(
            new TilePlacementList(
                    new TilePlacement(TILE1, LOWER_LEFT, Rotation.ANGLE_120),
                    new TilePlacement(TILE2, UPPER_LEFT, Rotation.ANGLE_60),
                    new TilePlacement(TILE4, UPPER, Rotation.ANGLE_60),
                    new TilePlacement(TILE3, LOWER_RIGHT, Rotation.ANGLE_300)),
            PathColor.RED);


    public static List<TantrixPath> createPathList() {
        // for each of the 7 permuted paths, we expect that tile 2 will be the middle/pivot tile.
        Location lowerLeft = new ByteLocation(22, 20);
        Location lowerRight = new ByteLocation(22, 21);
        TilePlacement pivot = new TilePlacement(TILES.getTile(1), new ByteLocation(21, 21), Rotation.ANGLE_0);
        HexTile tile2 = TILES.getTile(2);
        HexTile tile3 = TILES.getTile(3);

        List<TantrixPath> pathList =  Arrays.asList(
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
        return pathList;
    }

    public static TantrixPath createPath(TilePlacement... placements) {
        return  new TantrixPath(new TilePlacementList(placements), PathColor.YELLOW);
    }

}
