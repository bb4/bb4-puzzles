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



    static TantrixPath createPath(TilePlacement placement1, TilePlacement placement2, TilePlacement placement3) {
        return  new TantrixPath(new TilePlacementList(placement1, placement2, placement3), PathColor.YELLOW);
    }

}
