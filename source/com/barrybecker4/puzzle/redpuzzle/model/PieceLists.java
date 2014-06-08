/** Copyright by Barry G. Becker, 2011-2014. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.redpuzzle.model;

/**
 * Some standard puzzle configurations to try.
 *
 * @author Barry Becker
 */
public class PieceLists {

    /** this defines the puzzle pieces for the standard 9x9 puzzle (not sorted).*/
    static final Piece[] RED_INITIAL_PIECES_9 = {
        new Piece( Nub.OUTY_SPADE,  Nub.OUTY_DIAMOND,  Nub.INNY_HEART, Nub.INNY_DIAMOND, 1),  // 0
        new Piece( Nub.OUTY_CLUB,  Nub.OUTY_HEART,  Nub.INNY_DIAMOND, Nub.INNY_CLUB, 2),     // 1
        new Piece( Nub.OUTY_HEART,  Nub.OUTY_SPADE,  Nub.INNY_SPADE, Nub.INNY_CLUB, 3),     // 2
        new Piece( Nub.OUTY_CLUB,  Nub.OUTY_HEART,  Nub.INNY_SPADE, Nub.INNY_HEART, 4),    // 3
        new Piece( Nub.INNY_SPADE,  Nub.INNY_HEART,    Nub.OUTY_SPADE,   Nub.OUTY_DIAMOND, 5),
        new Piece( Nub.OUTY_HEART,  Nub.OUTY_DIAMOND,  Nub.INNY_DIAMOND, Nub.INNY_HEART, 6),
        new Piece( Nub.OUTY_HEART,  Nub.OUTY_DIAMOND,  Nub.INNY_CLUB, Nub.INNY_CLUB, 7),
        new Piece( Nub.OUTY_DIAMOND,  Nub.OUTY_CLUB,  Nub.INNY_CLUB, Nub.INNY_DIAMOND, 8),
        new Piece( Nub.OUTY_SPADE,  Nub.OUTY_SPADE,  Nub.INNY_HEART, Nub.INNY_CLUB, 9),
     };

    /**
     * Mapping from Lynette's puzzle
     *         1 = heart
     *         2 = diamond
     *         3 = club
     *         4 = spade        A = inny   B = outy
     */
    static final Piece[] LYNETTE_INITIAL_PIECES_9 = {
            new Piece( Nub.INNY_HEART,  Nub.OUTY_SPADE,  Nub.OUTY_DIAMOND, Nub.OUTY_CLUB, 1),  // 0
            new Piece( Nub.OUTY_SPADE,  Nub.INNY_CLUB,  Nub.INNY_HEART, Nub.INNY_DIAMOND, 2),  // 1
            new Piece( Nub.OUTY_HEART,  Nub.OUTY_DIAMOND,  Nub.INNY_SPADE, Nub.INNY_HEART, 3), // 2
            new Piece( Nub.INNY_HEART,  Nub.INNY_CLUB,  Nub.OUTY_SPADE, Nub.OUTY_DIAMOND, 4),  // 3
            new Piece( Nub.INNY_SPADE,  Nub.OUTY_DIAMOND,    Nub.INNY_HEART,   Nub.OUTY_CLUB, 5),
            new Piece( Nub.OUTY_DIAMOND,  Nub.INNY_HEART,  Nub.INNY_CLUB, Nub.INNY_SPADE, 6),
            new Piece( Nub.OUTY_HEART,  Nub.OUTY_SPADE,  Nub.OUTY_DIAMOND, Nub.OUTY_CLUB, 7),
            new Piece( Nub.OUTY_HEART,  Nub.INNY_DIAMOND,  Nub.INNY_SPADE, Nub.OUTY_CLUB, 8),
            new Piece( Nub.OUTY_SPADE,  Nub.INNY_DIAMOND,  Nub.INNY_CLUB, Nub.INNY_SPADE, 9),
    };

    /** This defines the puzzle pieces for a simpler 4x4 puzzle. */
    static final Piece[] INITIAL_PIECES_4 = {
         RED_INITIAL_PIECES_9[0], RED_INITIAL_PIECES_9[1], RED_INITIAL_PIECES_9[2], RED_INITIAL_PIECES_9[3]
    };


    public static PieceList getInitialPuzzlePieces() {
        return getInitialPuzzlePieces(PieceList.DEFAULT_NUM_PIECES);
    }


    /**
     * Factory method for creating the initial puzzle pieces.
     * @return the initial 9 pieces (in random order) to use when solving.
     */
    static PieceList getInitialPuzzlePieces(int numPieces) {

        Piece[] initialPieces = null;
        switch (numPieces) {
            case 4 : initialPieces = PieceLists.INITIAL_PIECES_4; break;
            case 9 : initialPieces = PieceLists.RED_INITIAL_PIECES_9; break;
            default: assert false: "We only support 4 or 9 piece red puzzles at this time.";
        }
        PieceList pieces = new PieceList(initialPieces);

        // shuffle the pieces so we get difference solutions -
        // or at least different approaches to the solution if there is only one.
        return pieces.shuffle();
    }

}
