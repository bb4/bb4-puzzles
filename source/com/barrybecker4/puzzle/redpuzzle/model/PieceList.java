/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.redpuzzle.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * The pieces that are in the red puzzle.
 * model in the model-view-controller pattern.
 * This class is only mostly immutable.
 * There is a cost for immutability, and I did not do it for methods that are not needed for concurrency safety.
 *
 * @author Barry Becker
 */
public class PieceList {

    /** the real game has 9 pieces, but I might experiment with 4 or 16 for testing. */
    public static final int NUM_PIECES = 9;

    private int EDGE_LENGTH = (int)Math.sqrt(NUM_PIECES);

    private static final int SEED = 5;
    // use the same seed for repeatable results.
    // 0 solves in 25,797 tries.
    // 1 solves in 15,444
    // 2 solves in 22,293
    // 3 solves in 21,588
    // 4 solves in  4,005
    // 5 solves in  5,319
    private static Random RANDOM = new Random(SEED);

    private List<Piece> pieces_;

    /** this defines the puzzle pieces for the standard 9x9 puzzle (not sorted).*/
    private static final Piece[] INITIAL_PIECES_9 = {
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

    /***
     * Mapping from Lynette's puzzle
     *         1 = heart
     *         2 = diamond
     *         3 = club
     *         4 = spade        A = inny   B = outy
     *
    private static final Piece[] INITIAL_PIECES_9 = {
            new Piece( Nub.INNY_HEART,  Nub.OUTY_SPADE,  Nub.OUTY_DIAMOND, Nub.OUTY_CLUB, 1),  // 0
            new Piece( Nub.OUTY_SPADE,  Nub.INNY_CLUB,  Nub.INNY_HEART, Nub.INNY_DIAMOND, 2),    // 1
            new Piece( Nub.OUTY_HEART,  Nub.OUTY_DIAMOND,  Nub.INNY_SPADE, Nub.INNY_HEART, 3),   // 2
            new Piece( Nub.INNY_HEART,  Nub.INNY_CLUB,  Nub.OUTY_SPADE, Nub.OUTY_DIAMOND, 4),    // 3
            new Piece( Nub.INNY_SPADE,  Nub.OUTY_DIAMOND,    Nub.INNY_HEART,   Nub.OUTY_CLUB, 5),
            new Piece( Nub.OUTY_DIAMOND,  Nub.INNY_HEART,  Nub.INNY_CLUB, Nub.INNY_SPADE, 6),
            new Piece( Nub.OUTY_HEART,  Nub.OUTY_SPADE,  Nub.OUTY_DIAMOND, Nub.OUTY_CLUB, 7),
            new Piece( Nub.OUTY_HEART,  Nub.INNY_DIAMOND,  Nub.INNY_SPADE, Nub.OUTY_CLUB, 8),
            new Piece( Nub.OUTY_SPADE,  Nub.INNY_DIAMOND,  Nub.INNY_CLUB, Nub.INNY_SPADE, 9),
    }; */

    /** this defines the puzzle pieces for a simpler 4x4 puzzle. */
    private static final Piece[] INITIAL_PIECES_4 = {
         INITIAL_PIECES_9[0], INITIAL_PIECES_9[1], INITIAL_PIECES_9[2], INITIAL_PIECES_9[3]
    };


    /**
     * a list of 9 puzzle pieces.
     */
    public PieceList() {

        this(NUM_PIECES);
    }

    /**
     * a list of puzzle pieces (initially empty).
     */
    private PieceList(int numPieces) {

        assert(numPieces==4 || numPieces == 9);
        pieces_ = new LinkedList<Piece>();
    }

    /**
     * Copy constructor
     */
    public PieceList(PieceList pieces) {
        this(pieces.pieces_);
    }

    /**
     * private copy constructor
     */
    private PieceList(List<Piece> pieces) {
        this();
        for (Piece p : pieces) {
            pieces_.add(new Piece(p));
        }
    }

    public static PieceList getInitialPuzzlePieces() {
        return getInitialPuzzlePieces(NUM_PIECES);
    }

    /**
     * Factory method for creating the initial puzzle pieces.
     * @return the initial 9 pieces (in random order) to use when solving.
     */
    private static PieceList getInitialPuzzlePieces(int numPieces) {

        // reset the random number generator.
        RANDOM = new Random(SEED);
        PieceList pieces = new PieceList();
        Piece[] initialPieces = null;
        switch (numPieces) {
            case 4 : initialPieces = INITIAL_PIECES_4; break;
            case 9 : initialPieces = INITIAL_PIECES_9; break;
            default: assert false: "We only support 4 or 9 piece red puzzles at this time.";
        }
        pieces.addPieces(initialPieces);

        // shuffle the pieces so we get difference solutions -
        // or at least different approaches to the solution if there is only one.
        return pieces.shuffle();
    }

    private void addPieces(Piece[] pieces) {
        Collections.addAll(pieces_, pieces);
    }

    /**
     * @param i the index of the piece to get.
     * @return the i'th piece.
     */
    public Piece get(int i)  {
        assert i < NUM_PIECES :
                "there are only " + NUM_PIECES + " pieces, but you tried to get the "+(i+1)+"th";
        return pieces_.get(i);
    }

    /**
     * @return the last piece added.
     */
    public Piece getLast()  {

        return pieces_.get(pieces_.size() - 1);
    }

    /**
     *? Does this need to be made immutable?
     * Swap 2 pieces.
     */
    public void doSwap(int p1Pos, int p2Pos) {
       assert p1Pos <= NUM_PIECES && p2Pos < NUM_PIECES :
                "The position indices must be less than " + NUM_PIECES + ".  You had " + p1Pos + ",  " + p2Pos;
        Piece piece2 = get(p2Pos);
        Piece piece1 = pieces_.remove(p1Pos);
        pieces_.add(p1Pos, piece2);
        pieces_.remove(piece2);
        pieces_.add(p2Pos, piece1);
    }

    /**
     * @param p piece to add to the end of the list.
     */
    public PieceList add(Piece p) {
        return add(pieces_.size(), p);
    }

    /**
      * @param p piece to add to the end of the list.
      */
     public PieceList add(int i, Piece p) {
         PieceList newPieceList = new PieceList(this);
         newPieceList.pieces_.add(i, p);
         assert newPieceList.pieces_.size() <= NUM_PIECES :
                "there can only be at most " + NUM_PIECES + " pieces.";
         return newPieceList;
    }

    /**
     * @param p the piece to remove.
     * @return true if the list contained this element.
     */
    public PieceList remove(Piece p) {
        PieceList newPieceList = new PieceList(this);
        boolean removed = newPieceList.pieces_.remove(p);
        assert removed: " could not remove "+p+" from "+newPieceList.pieces_;
        return newPieceList;
    }

    /**
     * @return piece list after removing the last element.
     */
    public PieceList removeLast() {
        return remove(pieces_.size() - 1);
    }

    /**
     * @return the list with the ith element removed.
     */
    public PieceList remove(int index) {
         PieceList newPieceList = new PieceList(this);
         newPieceList.pieces_.remove(index);
         return newPieceList;
    }

    /**
     *? Does this need to be made immutable?
     */
    public void rotate(int k, int numRotations) {
        Piece p = pieces_.get(k);
        Piece rp = p.rotate(numRotations);
        pieces_.set(k, rp);
    }

    /**
     *@return a new shuffled PieceList object based on the old.
     */
    public PieceList shuffle() {
        // randomly rotate all the pieces
        List<Piece> plist = new ArrayList<Piece>(pieces_.size());
        for (Piece p : pieces_) {
            Piece newp = p.rotate(RANDOM.nextInt(4));
            plist.add(newp);
        }
        Collections.shuffle(plist, RANDOM);
        return new PieceList(plist);
    }

    /**
     * Try the piece.
     * @param piece the piece to try to fit into our current solution.
     * @return true if it fits.
     */
    public boolean fits(Piece piece) {

        // it needs to match the piece to the left and above (if present)
        boolean fits = true;
        int numSolved = size();
        int row = numSolved / EDGE_LENGTH;
        int col = numSolved % EDGE_LENGTH;
        if ( col > 0 ) {
            // if other than a left edge piece, then we need to match to the left side nub.
            Piece leftPiece = getLast();
            if (!leftPiece.getRightNub().fitsWith(piece.getLeftNub()))
                fits = false;
        }
        if ( row > 0 ) {
            // then we need to match with the top one
            Piece topPiece = get( numSolved - EDGE_LENGTH );
            if (!topPiece.getBottomNub().fitsWith(piece.getTopNub()) )
                fits = false;
        }

        return fits;
    }

    /**
     * @return the number of pieces in the list.
     */
    public int size() {
        return pieces_.size();
    }

    /**
     * @return the number of matches for the nubs on this piece
     */
    public  int getNumFits(int i) {

        assert(i<9);
        // it needs to match the piece to the left and above (if present)
        int numFits = 0;
        int dim = 3;
        int row = i / dim;
        int col = i % dim;
        Piece piece = get(i);

        if ( col > 0 ) {
            // if other than a left edge piece, then we need to match to the left side nub.
            Piece leftPiece = get( i - 1 );
            if (leftPiece.getRightNub().fitsWith(piece.getLeftNub()))
                numFits++;
        }
        if ( row > 0 ) {
            // then we need to match with the top one
            Piece topPiece = get( i - dim );
            if (topPiece.getBottomNub().fitsWith(piece.getTopNub()))
                numFits++;
        }

        if ( col < (dim-1) ) {
            // if other than a right edge piece, then we need to match to the right side nub.
            Piece rightPiece = get( i + 1 );
            if (rightPiece.getLeftNub().fitsWith(piece.getRightNub()))
                numFits++;
        }
        if ( row < (dim-1) ) {
            // then we need to match with the bottom one
            Piece bottomPiece = get( i + dim );
            if (bottomPiece.getTopNub().fitsWith(piece.getBottomNub()))
                numFits++;
        }

        return numFits;
    }

    /**
     *@return as unmodifiable list so there can be no malicious modification of our immutable state.
     */
    public List<Piece> getPieces() {
        return Collections.unmodifiableList(pieces_);
    }

    /**
     *@return true if we contain p
     */
    public boolean contains(Piece p) {
        return pieces_.contains(p);
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder("PieceList: ("+size()+" pieces)\n");
        for (Piece p : pieces_) {
            buf.append(p.toString() + '\n');
        }
        return buf.toString();
    }

/*
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PieceList pieceList = (PieceList) o;
        if (pieces_ != null ? !pieces_.equals(pieceList.pieces_) : pieceList.pieces_ != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return pieces_ != null ? pieces_.hashCode() : 0;
    }  */
}
