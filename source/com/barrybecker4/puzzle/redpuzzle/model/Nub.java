// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle.model;

/**
 * Its that little thing on the edge of a piece that allows it to connect to another.
 * It can either be an inny or an outty like your belly button.
 * Immutable.
 *
 * @author Barry Becker
 */
public class Nub {

    /**
     * the complete set of nubs.
     */
    public static final Nub INNY_SPADE = new Nub(Suit.SPADE, false);
    public static final Nub OUTY_SPADE = new Nub(Suit.SPADE, true);

    public static final Nub INNY_CLUB = new Nub(Suit.CLUB, false);
    public static final Nub OUTY_CLUB = new Nub(Suit.CLUB, true);

    public static final Nub INNY_HEART = new Nub(Suit.HEART, false);
    public static final Nub OUTY_HEART = new Nub(Suit.HEART, true);

    public static final Nub INNY_DIAMOND = new Nub(Suit.DIAMOND, false);
    public static final Nub OUTY_DIAMOND = new Nub(Suit.DIAMOND, true);


    private Suit suit_;
    private boolean isOuty_;

    private Nub(Suit suit, boolean isOuty) {
        suit_ = suit;
        isOuty_ = isOuty;
    }

    /**
     * @return the char symbol used to represent this nub's suit.
     */
    public char getSuitSymbol() {
       return getSuit().getSymbol();
    }

    /**
     * @return the suit shape of the nub.
     */
    Suit getSuit() {
        return suit_;
    }

    /**
     * @return true if an outward facing nub.
     */
    public boolean isOuty() {
        return isOuty_;
    }

    /**
     * Check to see if this nub fits with another one.
     * @param nub other nub to try and fit with.
     * @return true if the nubs fit together.
     */
    boolean fitsWith(Nub nub) {
        boolean suitMatch = this.getSuit() == nub.getSuit();
        boolean nubMatch =  this.isOuty() != nub.isOuty();
        return suitMatch && nubMatch;
    }

    /**
     * @return nice readable string representation of a nub
     */
    @Override
    public String toString() {
       return  (isOuty() ? "outy" : "inny") + ' ' +getSuit();
    }


    /**
     * Determines the shape of the nub.
     */
    public static enum Suit {

        SPADE('S'),
        CLUB('C'),
        HEART('H'),
        DIAMOND('D');

        private final char symbol_;

        private Suit(char symbol) {
            symbol_ = symbol;
        }

        /**
         * @return the character symbol associated with this Suit
         */
        public char getSymbol() {
            return symbol_;
        }
    }
}
