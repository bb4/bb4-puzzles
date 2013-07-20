// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix;

import com.barrybecker4.common.geometry.ByteLocation;
import com.barrybecker4.common.geometry.Location;
import com.barrybecker4.puzzle.tantrix.model.HexTileList;
import com.barrybecker4.puzzle.tantrix.model.HexTiles;
import com.barrybecker4.puzzle.tantrix.model.Rotation;
import com.barrybecker4.puzzle.tantrix.model.TantrixBoard;
import com.barrybecker4.puzzle.tantrix.model.TilePlacement;

import static com.barrybecker4.puzzle.tantrix.model.TantrixBoard.INITIAL_LOCATION;


/**
 * @author Barry Becker
 */
public class TantrixTstUtil {

    public static final HexTiles TILES = new HexTiles();
    public static final HexTileList threeTiles = TILES.createOrderedList(3);
    public static final HexTileList fourTiles = TILES.createOrderedList(4);

    private TantrixTstUtil() {}

    /** Places first tile in the middle and one of two remaining placed */
    public static TantrixBoard place2of3Tiles_OneThenTwo() {

        TantrixBoard board = new TantrixBoard(threeTiles);
        TilePlacement tile2 = new TilePlacement(TILES.getTile(2), loc(2, 1), Rotation.ANGLE_60);
        board = new TantrixBoard(board, tile2);
        return board;
    }

    /** Places first tile in the middle and one of two remaining placed */
    public static TantrixBoard place2of3Tiles_OneThenThree() {

        TantrixBoard board = new TantrixBoard(threeTiles);
        TilePlacement tile2 = new TilePlacement(TILES.getTile(3), loc(2, 1), Rotation.ANGLE_240);
        board = new TantrixBoard(board, tile2);
        return board;
    }

    /** Places first tile in the middle */
    public static TantrixBoard place3UnsolvedTiles() {

        TantrixBoard board = new TantrixBoard(threeTiles);
        TilePlacement tile2 = new TilePlacement(TILES.getTile(2), loc(2, 0), Rotation.ANGLE_0);
        TilePlacement tile3 = new TilePlacement(TILES.getTile(3), loc(2, 1), Rotation.ANGLE_180);
        board = new TantrixBoard(board, tile2);
        board = new TantrixBoard(board, tile3);
        return board;
    }

    /** Places first tile in the middle, and the three tiles do not form a primary path */
    public static TantrixBoard place3NonPathTiles() {

        TantrixBoard board = new TantrixBoard(threeTiles);
        TilePlacement tile2 = new TilePlacement(TILES.getTile(2), loc(2, 0), Rotation.ANGLE_0);
        TilePlacement tile3 = new TilePlacement(TILES.getTile(3), loc(2, 1), Rotation.ANGLE_120);
        board = new TantrixBoard(board, tile2);
        board = new TantrixBoard(board, tile3);
        return board;
    }

    /** Places first tile in the middle. Not a valid primary path. */
    public static TantrixBoard place3UnsolvedTiles2() {

        TantrixBoard board = new TantrixBoard(threeTiles);

        TilePlacement tile2 = new TilePlacement(TILES.getTile(3), loc(2, 1), Rotation.ANGLE_0);
        TilePlacement tile3 = new TilePlacement(TILES.getTile(2), loc(3, 2), Rotation.ANGLE_0);
        board = new TantrixBoard(board, tile2);
        board = new TantrixBoard(board, tile3);
        return board;
    }

    /** constructor places first tile in the middle */
    public static TantrixBoard place3SolvedTiles() {
        System.out.println("3 tiles =" + threeTiles);
        TantrixBoard board = new TantrixBoard(threeTiles);

        TilePlacement tile2 = new TilePlacement(TILES.getTile(2), loc(2, 1), Rotation.ANGLE_60);
        TilePlacement tile3 = new TilePlacement(TILES.getTile(3), loc(2, 0), Rotation.ANGLE_120);
        board = new TantrixBoard(board, tile2);
        board = new TantrixBoard(board, tile3);
        System.out.println(board);
        return board;
    }

    /** Places second tile in the middle */
    public static TantrixBoard place1of3Tiles_startingWithTile2() {
        HexTileList list = new HexTileList();
        list.add(TILES.getTile(2));
        list.add(TILES.getTile(3));
        list.add(TILES.getTile(1));
        return new TantrixBoard(list);
    }

    /** Places first tile in the middle */
    public static TantrixBoard place4UnsolvedTiles() {
        TantrixBoard board = new TantrixBoard(fourTiles);

        TilePlacement tile2 = new TilePlacement(TILES.getTile(2), loc(1, 2), Rotation.ANGLE_240);
        TilePlacement tile3 = new TilePlacement(TILES.getTile(3), loc(0, 0), Rotation.ANGLE_120);
        TilePlacement tile4 = new TilePlacement(TILES.getTile(4), loc(1, 0), Rotation.ANGLE_180);
        board = new TantrixBoard(board, tile2);
        board = new TantrixBoard(board, tile3);
        board = new TantrixBoard(board, tile4);
        return board;
    }

    /** Places first tile in the middle */
    public static TantrixBoard place4SolvedTiles() {
        System.out.println("4 tiles =" + fourTiles);
        TantrixBoard board = new TantrixBoard(fourTiles);

        TilePlacement tile2 = new TilePlacement(TILES.getTile(2), loc(1, 2), Rotation.ANGLE_240);
        TilePlacement tile3 = new TilePlacement(TILES.getTile(3), loc(0, 0), Rotation.ANGLE_180);
        TilePlacement tile4 = new TilePlacement(TILES.getTile(4), loc(0, 1), Rotation.ANGLE_60);
        board = new TantrixBoard(board, tile2);
        board = new TantrixBoard(board, tile3);
        board = new TantrixBoard(board, tile4);
        return board;
    }

    public static Location loc(int row, int col) {
        return new ByteLocation(row, col).incrementOnCopy(INITIAL_LOCATION).incrementOnCopy(-1, -1);
    }
}