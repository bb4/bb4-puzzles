package com.barrybecker4.puzzle.redpuzzle.solver;

import com.barrybecker4.puzzle.redpuzzle.model.Nub;
import com.barrybecker4.puzzle.redpuzzle.model.Piece;
import com.barrybecker4.puzzle.redpuzzle.model.PieceList;
import com.barrybecker4.puzzle.redpuzzle.model.PieceLists;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Barry Becker
 */
public class FitnessFinderTest {

    /** instance under test */
    private FitnessFinder fitnessFinder = new FitnessFinder();


    @Test
    public void testFits() {

        PieceList pieceList = new PieceList(PieceLists.getInitialPuzzlePieces());

        assertEquals("Unexpected number of fits.",
                23.0, fitnessFinder.calculateFitness(pieceList), 0.0001);
    }

    @Test
    public void test4PieceSomeFits() {

        PieceList pieceList = new PieceList(new Piece[] {
            new Piece( Nub.OUTY_SPADE,  Nub.OUTY_DIAMOND,  Nub.INNY_HEART, Nub.INNY_DIAMOND, 1),
            new Piece( Nub.OUTY_CLUB,  Nub.OUTY_HEART,  Nub.INNY_DIAMOND, Nub.INNY_CLUB, 2),
            new Piece( Nub.OUTY_HEART,  Nub.OUTY_SPADE,  Nub.INNY_SPADE, Nub.INNY_CLUB, 3),
            new Piece( Nub.OUTY_CLUB,  Nub.OUTY_HEART,  Nub.INNY_SPADE, Nub.INNY_HEART, 4)
        });

        assertEquals("Unexpected number of fits.",
                23.0, fitnessFinder.calculateFitness(pieceList), 0.0001);
    }

    @Test
    public void test4PieceAllFits() {

        PieceList pieceList = new PieceList(new Piece[] {
            new Piece( Nub.OUTY_SPADE,  Nub.OUTY_DIAMOND,  Nub.INNY_HEART, Nub.INNY_DIAMOND, 1),
            new Piece( Nub.OUTY_CLUB,  Nub.OUTY_HEART,  Nub.INNY_CLUB, Nub.INNY_DIAMOND, 2),
            new Piece( Nub.OUTY_HEART,  Nub.OUTY_SPADE,  Nub.INNY_SPADE, Nub.INNY_CLUB, 3),
            new Piece( Nub.OUTY_CLUB,  Nub.OUTY_HEART,  Nub.INNY_DIAMOND, Nub.INNY_SPADE, 4)
        });

        assertEquals("Unexpected number of fits.",
                17.0, fitnessFinder.calculateFitness(pieceList), 0.0001);
    }

    @Test
    public void test9PieceSomeFits() {
        PieceList pieceList = PieceLists.getInitialPuzzlePieces();
        assertEquals("Unexpected number of fits.",
                23.0, fitnessFinder.calculateFitness(pieceList), 0.0001);
    }

    /** should get exactly 1 3 fit bonus */
    @Test
    public void test9Piece3FitBonus() {
        PieceList pieceList = new PieceList(new Piece[] {
            new Piece( Nub.OUTY_SPADE,  Nub.OUTY_CLUB,  Nub.INNY_HEART, Nub.INNY_DIAMOND, 1),
            new Piece( Nub.OUTY_CLUB,  Nub.OUTY_HEART,  Nub.INNY_DIAMOND, Nub.INNY_CLUB, 2),
            new Piece( Nub.OUTY_HEART,  Nub.OUTY_SPADE,  Nub.INNY_SPADE, Nub.INNY_HEART, 3),
            new Piece( Nub.OUTY_CLUB,  Nub.OUTY_HEART,  Nub.INNY_SPADE, Nub.INNY_HEART, 4),
            new Piece( Nub.OUTY_DIAMOND, Nub.INNY_HEART,  Nub.OUTY_SPADE,   Nub.OUTY_DIAMOND, 5),
            new Piece( Nub.OUTY_HEART,  Nub.OUTY_DIAMOND,  Nub.INNY_DIAMOND, Nub.INNY_HEART, 6),
            new Piece( Nub.OUTY_HEART,  Nub.OUTY_DIAMOND,  Nub.INNY_CLUB, Nub.INNY_CLUB, 7),
            new Piece( Nub.OUTY_DIAMOND,  Nub.OUTY_CLUB,  Nub.INNY_CLUB, Nub.INNY_DIAMOND, 8),
            new Piece( Nub.OUTY_SPADE,  Nub.OUTY_SPADE,  Nub.INNY_HEART, Nub.INNY_CLUB, 9),
        });

        assertEquals("Unexpected number of fits.",
                14.9, fitnessFinder.calculateFitness(pieceList), 0.0001);
    }

    @Test
    public void test9PieceAllFit() {
        PieceList pieceList = new PieceList(new Piece[] {
            new Piece( Nub.OUTY_SPADE,  Nub.OUTY_DIAMOND,  Nub.INNY_HEART, Nub.INNY_DIAMOND, 1),
            new Piece( Nub.INNY_CLUB,  Nub.OUTY_CLUB,  Nub.OUTY_HEART, Nub.INNY_DIAMOND, 2),
            new Piece( Nub.INNY_CLUB,  Nub.OUTY_HEART,  Nub.OUTY_DIAMOND, Nub.INNY_CLUB, 3),
            new Piece( Nub.OUTY_HEART,  Nub.OUTY_SPADE,  Nub.INNY_SPADE, Nub.INNY_CLUB, 4),
            new Piece( Nub.INNY_HEART, Nub.OUTY_CLUB,  Nub.OUTY_HEART,   Nub.INNY_SPADE, 5),
            new Piece( Nub.INNY_DIAMOND,  Nub.OUTY_DIAMOND,  Nub.OUTY_CLUB, Nub.INNY_CLUB, 6),
            new Piece( Nub.OUTY_SPADE,  Nub.OUTY_DIAMOND,  Nub.INNY_SPADE, Nub.INNY_HEART, 7),
            new Piece( Nub.INNY_HEART,  Nub.OUTY_HEART,  Nub.OUTY_DIAMOND, Nub.INNY_DIAMOND, 8),
            new Piece( Nub.INNY_CLUB,  Nub.OUTY_SPADE,  Nub.OUTY_SPADE, Nub.INNY_HEART, 9)

        });

        assertEquals("Unexpected number of fits.",
                0.0, fitnessFinder.calculateFitness(pieceList), 0.0001);
    }

}
