// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix1;

import com.barrybecker4.common.geometry.ByteLocation;
import com.barrybecker4.common.geometry.Location;
import com.barrybecker4.puzzle.tantrix1.model.*;

import static com.barrybecker4.puzzle.tantrix1.model.TantrixBoard.INITIAL_LOCATION;


/**
 * @author Barry Becker
 */
public class TantrixTstUtil {

    public static final HexTiles TILES = new HexTiles();
    public static final HexTileList THREE_TILES = TILES.createOrderedList(3);
    public static final HexTileList FOUR_TILES = TILES.createOrderedList(4);
    public static final HexTileList FIVE_TILES = TILES.createOrderedList(5);
    public static final HexTileList SIX_TILES = TILES.createOrderedList(6);
    public static final HexTileList TEN_TILES = TILES.createOrderedList(10);
    public static final HexTileList FOURTEEN_TILES = TILES.createOrderedList(14);

    private TantrixTstUtil() {}

    /** Places first tile in the middle and one of two remaining placed */
    public static TantrixBoard place2of3Tiles_OneThenTwo() {

        TantrixBoard board = new TantrixBoard(THREE_TILES);
        TilePlacement tile2 = new TilePlacement(TILES.getTile(2), loc(2, 1), Rotation.ANGLE_60);
        board = new TantrixBoard(board, tile2);
        return board;
    }

    /** Places first tile in the middle and one of two remaining placed */
    public static TantrixBoard place2of3Tiles_OneThenThree() {

        TantrixBoard board = new TantrixBoard(THREE_TILES);
        TilePlacement tile2 = new TilePlacement(TILES.getTile(3), loc(2, 1), Rotation.ANGLE_240);
        board = new TantrixBoard(board, tile2);
        return board;
    }

    /** Places first tile in the middle */
    public static TantrixBoard place3UnsolvedTiles() {
        return place3Unsolved(THREE_TILES);
    }

    /** Places first tile in the middle */
    public static TantrixBoard place6UnsolvedTiles() {
        return place6Unsolved(SIX_TILES);
    }

    /** Places first tile in the middle. Three unplaced tiles remain. */
    public static TantrixBoard place3of6UnsolvedTiles() {
        TantrixBoard board = new TantrixBoard(SIX_TILES);
        TilePlacement tile2 = new TilePlacement(TILES.getTile(2), loc(0, 1), Rotation.ANGLE_60);
        TilePlacement tile3 = new TilePlacement(TILES.getTile(3), loc(-1, 2), Rotation.ANGLE_180);
        board = new TantrixBoard(board, tile2);
        board = new TantrixBoard(board, tile3);
        return board;
    }

    /** Places first tile in the middle. Three unplaced tiles remain. */
    public static TantrixBoard place1of5UnsolvedTiles() {
        return new TantrixBoard(FIVE_TILES);
    }

    /** its a yellow path but not a loop */
    private static TantrixBoard place3Unsolved(HexTileList tiles) {
        TantrixBoard board = new TantrixBoard(tiles);
        TilePlacement tile2 = new TilePlacement(TILES.getTile(2), loc(2, 0), Rotation.ANGLE_0);
        TilePlacement tile3 = new TilePlacement(TILES.getTile(3), loc(2, 1), Rotation.ANGLE_180);
        board = new TantrixBoard(board, tile2);
        board = new TantrixBoard(board, tile3);
        return board;
    }

    /** its a blue path but not a loop */
    private static TantrixBoard place6Unsolved(HexTileList tiles) {
        TantrixBoard board = new TantrixBoard(tiles);
        TilePlacementList tileList= new TilePlacementList(
              new TilePlacement(TILES.getTile(2), loc(0, 1), Rotation.ANGLE_60),
              new TilePlacement(TILES.getTile(3), loc(-1, 2), Rotation.ANGLE_180),
              new TilePlacement(TILES.getTile(4), loc(-1, 1), Rotation.ANGLE_0),
              new TilePlacement(TILES.getTile(5), loc(-1, 0), Rotation.ANGLE_240),
              new TilePlacement(TILES.getTile(6), loc(0, 0), Rotation.ANGLE_240)
        );
        for (TilePlacement placement : tileList) {
            board = new TantrixBoard(board, placement);
        }
        return board;
    }

    /** Places first tile in the middle, and the three tiles do not form a primary path */
    public static TantrixBoard place3NonPathTiles() {

        TantrixBoard board = new TantrixBoard(THREE_TILES);
        TilePlacement tile2 = new TilePlacement(TILES.getTile(2), loc(2, 0), Rotation.ANGLE_0);
        TilePlacement tile3 = new TilePlacement(TILES.getTile(3), loc(2, 1), Rotation.ANGLE_120);
        board = new TantrixBoard(board, tile2);
        board = new TantrixBoard(board, tile3);
        return board;
    }

    /** Places first tile in the middle. Not a valid primary path. */
    public static TantrixBoard place3UnsolvedTiles2() {

        TantrixBoard board = new TantrixBoard(THREE_TILES);

        TilePlacement tile2 = new TilePlacement(TILES.getTile(3), loc(2, 1), Rotation.ANGLE_0);
        TilePlacement tile3 = new TilePlacement(TILES.getTile(2), loc(3, 2), Rotation.ANGLE_0);
        board = new TantrixBoard(board, tile2);
        board = new TantrixBoard(board, tile3);
        return board;
    }

    /** constructor places first tile in the middle */
    public static TantrixBoard place3SolvedTiles() {
        return place3Solved(THREE_TILES);
    }

    /** constructor places first tile in the middle */
    public static TantrixBoard place3of6SolvedTiles() {
        return place3Solved(SIX_TILES);
    }

    private static TantrixBoard place3Solved(HexTileList tiles) {
        TantrixBoard board = new TantrixBoard(tiles);

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
        TantrixBoard board = new TantrixBoard(FOUR_TILES);

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
        TantrixBoard board = new TantrixBoard(FOUR_TILES);

        TilePlacement tile2 = new TilePlacement(TILES.getTile(2), loc(1, 2), Rotation.ANGLE_240);
        TilePlacement tile3 = new TilePlacement(TILES.getTile(3), loc(0, 0), Rotation.ANGLE_180);
        TilePlacement tile4 = new TilePlacement(TILES.getTile(4), loc(0, 1), Rotation.ANGLE_60);
        board = new TantrixBoard(board, tile2);
        board = new TantrixBoard(board, tile3);
        board = new TantrixBoard(board, tile4);
        return board;
    }


    /** There are 10 tiles that form a red loop, and there are two empty spaces within the loop.  */
    public static TantrixBoard place10LoopWithInnerSpace() {
        TantrixBoard board = new TantrixBoard(TEN_TILES);

        TilePlacementList tiles = new TilePlacementList();
        tiles.add(new TilePlacement(TILES.getTile(5), loc(0, 0), Rotation.ANGLE_120));
        tiles.add(new TilePlacement(TILES.getTile(6), loc(-1, 0), Rotation.ANGLE_0));
        tiles.add(new TilePlacement(TILES.getTile(2), loc(-2, 0), Rotation.ANGLE_0));
        tiles.add(new TilePlacement(TILES.getTile(3), loc(-1, 1), Rotation.ANGLE_300));
        tiles.add(new TilePlacement(TILES.getTile(10), loc(-2, 1), Rotation.ANGLE_0));
        tiles.add(new TilePlacement(TILES.getTile(4), loc(-2, 2), Rotation.ANGLE_60));
        tiles.add(new TilePlacement(TILES.getTile(8), loc(-1, 3), Rotation.ANGLE_60));
        tiles.add(new TilePlacement(TILES.getTile(9), loc(0,2), Rotation.ANGLE_60));
        tiles.add(new TilePlacement(TILES.getTile(7), loc(1, 2), Rotation.ANGLE_60));

        for (TilePlacement tile :  tiles) {
            board = new TantrixBoard(board, tile);
        }
        return board;
    }


    /** There are 9 tiles that almost form a red loop. One more will complete it. */
    public static TantrixBoard place9AlmostLoop() {
        TantrixBoard board = new TantrixBoard(FOURTEEN_TILES);

        TilePlacementList tiles = new TilePlacementList();
        tiles.add(new TilePlacement(TILES.getTile(5), loc(0, 0), Rotation.ANGLE_120));
        tiles.add(new TilePlacement(TILES.getTile(6), loc(-1, 0), Rotation.ANGLE_0));
        tiles.add(new TilePlacement(TILES.getTile(2), loc(-2, 0), Rotation.ANGLE_0));
        tiles.add(new TilePlacement(TILES.getTile(3), loc(-1, 1), Rotation.ANGLE_300));
        tiles.add(new TilePlacement(TILES.getTile(10), loc(-2, 1), Rotation.ANGLE_0));
        tiles.add(new TilePlacement(TILES.getTile(4), loc(-2, 2), Rotation.ANGLE_60));
        tiles.add(new TilePlacement(TILES.getTile(8), loc(-1, 3), Rotation.ANGLE_60));
        tiles.add(new TilePlacement(TILES.getTile(9), loc(0,2), Rotation.ANGLE_60));

        for (TilePlacement tile :  tiles) {
            board = new TantrixBoard(board, tile);
        }
        return board;
    }

    /** There are 9 tiles that almost form a red loop. One more will complete it. */
    public static TantrixBoard placeJumbled9() {
        TantrixBoard board = new TantrixBoard(FOURTEEN_TILES);

        TilePlacementList tiles = new TilePlacementList();
        tiles.add(new TilePlacement(TILES.getTile(9), loc(0, 0), Rotation.ANGLE_120));
        tiles.add(new TilePlacement(TILES.getTile(3), loc(-1, 0), Rotation.ANGLE_60));
        tiles.add(new TilePlacement(TILES.getTile(6), loc(-2, 0), Rotation.ANGLE_0));
        tiles.add(new TilePlacement(TILES.getTile(5), loc(-1, 1), Rotation.ANGLE_300));
        tiles.add(new TilePlacement(TILES.getTile(8), loc(-2, 1), Rotation.ANGLE_0));
        tiles.add(new TilePlacement(TILES.getTile(2), loc(-2, 2), Rotation.ANGLE_60));
        tiles.add(new TilePlacement(TILES.getTile(10), loc(-1, 3), Rotation.ANGLE_60));
        tiles.add(new TilePlacement(TILES.getTile(4), loc(0,2), Rotation.ANGLE_60));

        for (TilePlacement tile :  tiles) {
            board = new TantrixBoard(board, tile);
        }
        return board;
    }

    public static Location loc(int row, int col) {
        return new ByteLocation(row, col).incrementOnCopy(INITIAL_LOCATION).incrementOnCopy(-1, -1);
    }
}