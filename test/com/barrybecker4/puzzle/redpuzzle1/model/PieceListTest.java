package com.barrybecker4.puzzle.redpuzzle1.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Barry Becker
 */
public class PieceListTest {

    /** instance under test */
    private PieceList pieceList;

    @Test
    public void testConstructionOfEmptyList() {

        pieceList = new PieceList();
        assertEquals("Unexpected size.", 0, pieceList.size());
    }

    @Test
    public void testConstruction() {

        pieceList = new PieceList(PieceLists.INITIAL_PIECES_4);
        assertEquals("Unexpected size.", 4, pieceList.size());
        assertTrue("Piece should have been there.", pieceList.contains(PieceLists.INITIAL_PIECES_4[1]));
        assertEquals("Unexpected number of fits.", 1, pieceList.getNumFits(0));
        assertEquals("Unexpected number of fits.", 0, pieceList.getNumFits(1));
        assertEquals("Unexpected number of fits.", 1, pieceList.getNumFits(2));
        assertEquals("Unexpected number of fits.", 0, pieceList.getNumFits(3));
    }

    @Test
    public void testFits() {

        pieceList = new PieceList(PieceLists.INITIAL_PIECES_4);

        assertEquals("Unexpected number of fits before rotating.",
                0, pieceList.getNumFits(1));
        // after rotating there should be a fit
        pieceList.rotate(1, 1);
        assertEquals("Unexpected number of fits after rotating.",
                1, pieceList.getNumFits(1));
    }

}
