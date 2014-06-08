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
    public static final int DEFAULT_NUM_PIECES = 9;

    /** use the same seed for repeatable results. */
    private static final int SEED = 5;

    /** There are either 4 or 9 or 16 pieces. */
    private int numPieces = DEFAULT_NUM_PIECES;
    private int edgeLen;

    /**
     * 0 solves in 25,797 tries.
     * 1 solves in 15,444
     * 2 solves in 22,293
     * 3 solves in 21,588
     * 4 solves in  4,005
     * 5 solves in  5,319
     */
    private static Random RANDOM = new Random(SEED);

    private List<Piece> pieces_;


    /**
     * a list of 9 puzzle pieces.
     */
    public PieceList() {
        this(DEFAULT_NUM_PIECES);
    }

    /**
     * a list of puzzle pieces (initially empty).
     */
    private PieceList(int numPieces) {

        if (numPieces == 0)
            throw new IllegalArgumentException("Num pieces should not be 0");
        assert(numPieces==4 || numPieces == 9 || numPieces == 16);
        this.numPieces = numPieces;
        this.edgeLen = (int)Math.sqrt(numPieces);
        pieces_ = new LinkedList<>();
    }

    /**
     * a list of puzzle pieces with pieces to use specified.
     * @param pieces array of pieces to use.
     */
    public PieceList(Piece[] pieces) {
        this(pieces.length);
        addPieces(pieces);
    }

    /**
     * Copy constructor
     */
    public PieceList(PieceList pieces) {
        this(pieces.pieces_, pieces.getTotalNum());
    }

    /**
     * private copy constructor
     */
    private PieceList(List<Piece> pieces, int numTotal) {
        this(numTotal);
        for (Piece p : pieces) {
            pieces_.add(new Piece(p));
        }
    }

    private void addPieces(Piece[] pieces) {
        Collections.addAll(pieces_, pieces);
    }

    /**
     * @param i the index of the piece to get.
     * @return the i'th piece.
     */
    public Piece get(int i)  {
        assert i < numPieces :
                "there are only " + numPieces + " pieces, but you tried to get the " + (i+1) + "th";
        return pieces_.get(i);
    }

    /**
     * @return the last piece added.
     */
    public Piece getLast()  {

        return pieces_.get(pieces_.size() - 1);
    }

    /**
     * @return The total number of pieces in the puzzle.
     */
    public int getTotalNum() {
        return numPieces;
    }

    /**
     * @return the number of pieces in the list.
     */
    public int size() {
        return pieces_.size();
    }

    /**
     * Does this need to be made immutable?
     * Swap 2 pieces.
     */
    public void doSwap(int p1Pos, int p2Pos) {
       assert p1Pos <= numPieces&& p2Pos < numPieces :
                "The position indices must be less than " + numPieces + ".  You had " + p1Pos + ",  " + p2Pos;
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
         assert newPieceList.pieces_.size() <= numPieces :
                "there can only be at most " + numPieces + " pieces.";
         return newPieceList;
    }

    /**
     * @param p the piece to remove.
     * @return true if the list contained this element.
     */
    public PieceList remove(Piece p) {
        PieceList newPieceList = new PieceList(this);
        boolean removed = newPieceList.pieces_.remove(p);
        assert removed: " could not remove " + p + " from " + newPieceList.pieces_;
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
     * ? Does this need to be made immutable?
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
        List<Piece> plist = new ArrayList<>(pieces_.size());
        for (Piece p : pieces_) {
            Piece newp = p.rotate(RANDOM.nextInt(4));
            plist.add(newp);
        }
        Collections.shuffle(plist, RANDOM);
        return new PieceList(plist, numPieces);
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
        int row = numSolved / edgeLen;
        int col = numSolved % edgeLen;
        if ( col > 0 ) {
            // if other than a left edge piece, then we need to match to the left side nub.
            Piece leftPiece = getLast();
            if (!leftPiece.getRightNub().fitsWith(piece.getLeftNub()))
                fits = false;
        }
        if ( row > 0 ) {
            // then we need to match with the top one
            Piece topPiece = get( numSolved - edgeLen );
            if (!topPiece.getBottomNub().fitsWith(piece.getTopNub()) )
                fits = false;
        }

        return fits;
    }

    /**
     * @return the number of matches for the nubs on this piece
     */
    public int getNumFits(int i) {

        assert(i < numPieces);
        // it needs to match the piece to the left and above (if present)
        int numFits = 0;
        int dim = edgeLen;
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
            buf.append(p.toString()).append('\n');
        }
        return buf.toString();
    }
}
