/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.redpuzzle.solver;

import com.barrybecker4.optimization.OptimizationListener;
import com.barrybecker4.optimization.optimizee.Optimizee;
import com.barrybecker4.optimization.Optimizer;
import com.barrybecker4.optimization.parameter.ParameterArray;
import com.barrybecker4.optimization.strategy.OptimizationStrategyType;
import com.barrybecker4.puzzle.common.PuzzleController;
import com.barrybecker4.puzzle.redpuzzle.model.Piece;
import com.barrybecker4.puzzle.redpuzzle.model.PieceList;
import com.barrybecker4.puzzle.redpuzzle.model.PieceParameterArray;

import java.util.List;

/**
 * Solve the red puzzle using a genetic search algorithm.
 * Solves the puzzle in 3.5 seconds on Core2 duo system (6 generations).
 *
 * @author Barry Becker
 */
public class GeneticSearchSolver extends RedPuzzleSolver
                                 implements Optimizee, OptimizationListener {

    /** bonuses given to the scoring algorithm if 3 nubs fit on a side piece. */
    public static final double THREE_FIT_BOOST = 0.1;

    /** bonuses given to the scoring algorithm if 4 nubs on the center piece fit. */
    public static final double FOUR_FIT_BOOST = 0.6;

    /**
     * The puzzle is solved if we reach this score.
     * There are 24 nubs that need to fit for all the pieces.
     * There are 4 edge pieces that get THREE_FIT_BOOST if all 3 nubs fit.
     */
    public static final double MAX_FITS = 24 + 4 * THREE_FIT_BOOST + FOUR_FIT_BOOST;

    /** either genetic or concurrent genetic strategy. */
    private OptimizationStrategyType strategy;


    /** Constructor */
    public GeneticSearchSolver(PuzzleController<PieceList, Piece> puzzle,
                               boolean useConcurrency) {
        super(puzzle);
        strategy = useConcurrency ? OptimizationStrategyType.CONCURRENT_GENETIC_SEARCH :
                                    OptimizationStrategyType.GENETIC_SEARCH;
    }

    /**
     * @return list of moves to a solution.
     */
    @Override
    public List<Piece> solve()  {

        ParameterArray initialGuess = new PieceParameterArray(pieces_);
        solution_ = pieces_;
        long startTime = System.currentTimeMillis();

        Optimizer optimizer = new Optimizer(this);

        optimizer.setListener(this);

        ParameterArray solution =
            optimizer.doOptimization(strategy, initialGuess, MAX_FITS);

        solution_ = ((PieceParameterArray)solution).getPieceList();
        List<Piece> moves;
        if (evaluateFitness(solution) >= MAX_FITS) {
            moves = solution_.getPieces();
        } else {
            moves = null;
        }
        long elapsedTime = System.currentTimeMillis() - startTime;
        puzzle.finalRefresh(moves, solution_, numTries_, elapsedTime);

        return moves;
    }

    public String getName() {
         return "Genetic Search Solver for Red Puzzle";
    }

    /**
     * terminate the solver if we find a solution with this fitness.
     */
    public double getOptimalFitness() {
        return MAX_FITS;
    }

    public boolean evaluateByComparison() {
        return false;
    }

    /**
     * Return a high score if there are a lot of fits among the pieces.
     * For every nub that fits we count 1
     *
     * @param params  parameters
     * @return fitness value. High is good.
     */
    public double evaluateFitness(ParameterArray params) {
        PieceList pieces = ((PieceParameterArray) params).getPieceList();
        double fitness = MAX_FITS - getNumFits(pieces);
        params.setFitness(fitness);
        return fitness;
    }

    public double compareFitness(ParameterArray params1, ParameterArray params2) {
        assert false : "compareFitness not used since we evaluate in an absolute way.";
        return 0;
    }

    /**
     * @return the number of matches for all the nubs.
     */
    private static double getNumFits(PieceList pieces) {
        double totalFits = 0;
        for (int i=0; i < pieces.size(); i++) {
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
                "fits exceeded " + MAX_FITS +". Fits="+totalFits +" pieces="+pieces;
        return totalFits;
    }

    /**
     * Called when the optimizer has made some progress optimizing.
     * Shows the current status.
     * @param params optimization parameters
     */
    public void optimizerChanged(ParameterArray params) {
        // update our current best guess at the solution.
        solution_ = ((PieceParameterArray) params).getPieceList();
        numTries_ ++;
        puzzle.refresh(solution_, numTries_);
    }
}
