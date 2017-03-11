// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix1.generation;

import com.barrybecker4.common.geometry.ByteLocation;
import com.barrybecker4.puzzle.tantrix1.model.Rotation;
import com.barrybecker4.puzzle.tantrix1.model.TantrixBoard;
import com.barrybecker4.puzzle.tantrix1.model.TilePlacement;
import org.junit.Test;

import java.util.List;

import static com.barrybecker4.puzzle.tantrix1.TantrixTstUtil.*;
import static org.junit.Assert.assertEquals;

/**
 * @author Barry Becker
 */
public class MoveGeneratorTest {

    /** instance under test */
    MoveGenerator generator;
    TantrixBoard board;

    @Test
    public void testMoveGenerationFromTwoOfThreeTilesA() {
        board = place2of3Tiles_OneThenTwo();
        generator = new MoveGenerator(board);

        List<TilePlacement> moves = generator.generateMoves();

        assertEquals("Unexpected number of next moves.", 1, moves.size());
        assertEquals("Unexpected first next move.",
            new TilePlacement(TILES.getTile(3), new ByteLocation(22, 20), Rotation.ANGLE_120), moves.get(0));
        //assertEquals("Unexpected second next move.",
        //    new TilePlacement(TILES.getTile(3), new ByteLocation(1, 0), Rotation.ANGLE_0), moves.get(1));
    }

    @Test
    public void testMoveGenerationFromTwoOfThreeTilesB() {
        board = place2of3Tiles_OneThenThree();
        generator = new MoveGenerator(board);

        List<TilePlacement> moves = generator.generateMoves();

        System.out.println("moves = " + moves);
        assertEquals("Unexpected number of next moves.", 1, moves.size());
    }

    @Test
    public void testMoveGenerationFromFirstTileOfThree() {
        board = new TantrixBoard(THREE_TILES);
        generator = new MoveGenerator(board);

        List<TilePlacement> moves = generator.generateMoves();

        System.out.println("moves = " + moves);
        assertEquals("Unexpected number of next moves.", 8, moves.size());
    }

}

