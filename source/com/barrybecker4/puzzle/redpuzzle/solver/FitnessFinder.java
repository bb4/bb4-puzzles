// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle.solver;

import com.barrybecker4.puzzle.redpuzzle.model.PieceList;

/**
 * Calculates the number of matching nubs and uses MAX_FITS minus that as a fitness value.
 *
 * @author Barry Becker
 */
class FitnessFinder  {

    /** bonuses given to the scoring algorithm if 3 nubs fit on a side piece. */
    private static final double THREE_FIT_BOOST = 0.1;

    /** bonuses given to the scoring algorithm if 4 nubs on the center piece fit. */
    private static final double FOUR_FIT_BOOST = 0.6;

    /**
     * The puzzle is solved if we reach this score.
     * There are 24 nubs that need to fit for all the pieces.
     * There are 4 edge pieces that get THREE_FIT_BOOST if all 3 nubs fit.
     */
    static final double MAX_FITS = 24 + 4 * THREE_FIT_BOOST + FOUR_FIT_BOOST;


    /**
     * Return a high score if there are a lot of fits among the pieces.
     * For every nub that fits we count 1
     *
     * @param pieces the current state of the pieces
     * @return fitness value. High is good.
     */
    double calculateFitness(PieceList pieces) {
        return MAX_FITS - getNumFits(pieces);
    }

    /**
     * @return the number of matches for all the nubs.
     */
    private double getNumFits(PieceList pieces) {
        double totalFits = 0;
        for (int i = 0; i < pieces.size(); i++) {
            double nFits = pieces.getNumFits(i);
            totalFits += nFits;
            // give a boost if a give piece has 3 or 4 fits.
            if (nFits == 3) {
                totalFits += THREE_FIT_BOOST;
            } else if (nFits == 4) {
                // center piece
                totalFits += FOUR_FIT_BOOST;
            }
        }
        assert(totalFits <= MAX_FITS) :
                "fits exceeded " + MAX_FITS + ". Fits=" + totalFits + " pieces=" + pieces;
        return totalFits;
    }
}
