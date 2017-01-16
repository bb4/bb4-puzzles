// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle1.model;

import com.barrybecker4.common.math.MathUtil;
import com.barrybecker4.optimization.parameter.ParameterArray;
import com.barrybecker4.optimization.parameter.PermutedParameterArray;

import java.util.List;

/**
 * The parameter array to use when searching (using optimization) to find a red puzzle solution.
 * It has some unique properties.
 * For example, when finding a random neighbor, we consider rotations of
 * non-fitting pieces rather than just offsetting the number by some random amount.
 *
 * @author Barry Becker
 */
public class PieceParameterArray extends PermutedParameterArray {

    private PieceList pieces_ ;
    private static final int SAMPLE_POPULATION_SIZE = 400;


    public PieceParameterArray(PieceList pieces) {
        pieces_ = pieces;
    }

    @Override
    public PieceParameterArray copy() {
        PieceParameterArray copy = new PieceParameterArray(pieces_);

        copy.setFitness(this.getFitness());
        return copy;
    }

    @Override
    public int getSamplePopulationSize()  {
        return SAMPLE_POPULATION_SIZE;
    }

    /**
     * We want to find a potential solution close to the one that we have,
     * with minimal disturbance of the pieces that are already fit.
     *
     * @param radius proportional to the number of pieces that you want to vary.
     * @return the random nbr (potential solution).
     */
    @Override
    public PermutedParameterArray getRandomNeighbor(double radius) {
        PieceList pieces = new PieceList(pieces_);

        int numSwaps = 1;   //Math.max(1, (int) (rad * 2.0));

        for (int i = 0; i < numSwaps; i++) {
            doPieceSwap(pieces);
        }
        //assert !this.equals(new PieceParameterArray(pieces)) :
        //    "The piecelists should not be equal new=" + pieces + " orig=" + pieces_;

        assert (pieces.size() == pieces.getTotalNum());
        // make a pass over all the pieces.
        // If rotating a piece leads to more fits, then do it.
        for ( int k = 0; k < pieces.size(); k++) {

            int numFits = pieces.getNumFits(k);
            int bestNumFits = numFits;
            int bestRot = 1;
            for (int i = 0; i < 3; i++) {

                pieces.rotate(k, 1);  // fix
                numFits = pieces.getNumFits(k);
                if (numFits > bestNumFits) {
                    bestNumFits = numFits;
                    bestRot = 2 + i;
                }
            }
            // rotate the piece to position of best fit.
            pieces.rotate(k, bestRot); // fix
        }

        return new PieceParameterArray(pieces);
    }

    /**
     * Exchange 2 pieces, even if it means the fitness gets worse.
     *
     * Skew away from selecting pieces that have fits.
     * The probability of selecting pieces that already have fits is sharply reduced.
     * The denominator is 1 + the number of fits that the piece has.
     */
    private void doPieceSwap(PieceList pieces) {

        double[] swapProbabilities = findSwapProbabilities(pieces);
        double totalProb = 0;
        for (int i = 0; i < pieces.getTotalNum(); i++) {
            totalProb += swapProbabilities[i];
        }
        int p1 = getPieceFromProb(totalProb * MathUtil.RANDOM.nextDouble(), swapProbabilities);
        int p2;
        do {
            p2 = getPieceFromProb(totalProb * MathUtil.RANDOM.nextDouble(), swapProbabilities);
        } while (p2 == p1);

        pieces.doSwap(p1, p2);
    }

    /**
     * @param pieces piece list to find probabilities for.
     * @return probability used to determine if we do a piece swap.
     *   Pieces that already fit have a low probability of being swapped.
     */
    private static double[] findSwapProbabilities(PieceList pieces) {

        double[] swapProbabilities = new double[pieces.getTotalNum()];
        for (int i=0; i<pieces.getTotalNum(); i++) {
            swapProbabilities[i] = 1.0 / (1.0 + pieces.getNumFits(i)); //Math.pow(pieces.getNumFits(i), 2));
        }
        return swapProbabilities;
    }

    /**
     * @param p some value between 0 and the totalProbability (i.e. 100%).
     * @return the piece that was selected given the probability.
     */
    private int getPieceFromProb(double p, double[] probabilities) {
        double total = 0;
        int i = 0;
        while (total < p && i<pieces_.getTotalNum()) {
            total += probabilities[i++];
        }
        return --i;
    }

    /**
     * @return get a completely random solution in the parameter space.
     */
    @Override
    public ParameterArray getRandomSample() {
       PieceList pl = new PieceList(pieces_);
       PieceList shuffledPieces = pl.shuffle();
       return new PieceParameterArray(shuffledPieces);
    }

    @Override
    public void setPermutation(List<Integer> indices) {

        PieceList newParams = new PieceList();

        for (int i : indices) {
            newParams.add(pieces_.get(i));
        }
        pieces_ = newParams;
    }

    /**
     *
     * @return the piece list corresponding to the encoded parameter array.
     */
    public PieceList getPieceList() {
        return pieces_;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PieceParameterArray that = (PieceParameterArray) o;
        return pieces_.getPieces().equals(that.pieces_.getPieces());
    }

    @Override
    public int hashCode() {
        return pieces_ != null ? pieces_.getPieces().hashCode() : 0;
    }

    /**
     * @return the number of parameters in the array.
     */
    @Override
    public int size() {
        return pieces_.size();
    }

    @Override
    public String toString() {
        return pieces_.toString();
    }

    /**
     * @return  the parameters in a string of Comma Separated Values.
     */
    @Override
    public String toCSVString() {
        return toString();
    }
}
