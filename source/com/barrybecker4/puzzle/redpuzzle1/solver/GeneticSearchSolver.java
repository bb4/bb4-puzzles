// Copyright by Barry G. Becker, 2000-2014. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle1.solver;

import com.barrybecker4.optimization.OptimizationListener;
import com.barrybecker4.optimization.Optimizer;
import com.barrybecker4.optimization.optimizee.Optimizee;
import com.barrybecker4.optimization.parameter.ParameterArray;
import com.barrybecker4.optimization.strategy.OptimizationStrategyType;
import com.barrybecker4.puzzle.common1.PuzzleController;
import com.barrybecker4.puzzle.redpuzzle1.model.Piece;
import com.barrybecker4.puzzle.redpuzzle1.model.PieceList;
import com.barrybecker4.puzzle.redpuzzle1.model.PieceParameterArray;
import scala.Option;
import scala.collection.JavaConversions;
import scala.collection.Seq;

import static com.barrybecker4.puzzle.redpuzzle1.solver.FitnessFinder.MAX_FITS;

/**
 * Solve the red puzzle using a genetic search algorithm.
 * Solves the puzzle in 3.5 seconds on Core2 duo system (6 generations).
 *
 * @author Barry Becker
 */
public class GeneticSearchSolver extends RedPuzzleSolver
                                 implements Optimizee, OptimizationListener {

    /** either genetic or concurrent genetic strategy. */
    private OptimizationStrategyType strategy;

    private FitnessFinder fitnessFinder;

    private double currentBestFitness = MAX_FITS;

    /** Constructor */
    GeneticSearchSolver(PuzzleController<PieceList, Piece> puzzle,
                               boolean useConcurrency) {
        super(puzzle);
        strategy = useConcurrency ? OptimizationStrategyType.CONCURRENT_GENETIC_SEARCH :
                                    OptimizationStrategyType.GENETIC_SEARCH;
        fitnessFinder = new FitnessFinder();
    }

    /**
     * @return list of moves to a solution.
     */
    @Override
    public Option<Seq<Piece>> solve()  {

        ParameterArray initialGuess = new PieceParameterArray(pieces_);
        solution_ = pieces_;
        long startTime = System.currentTimeMillis();

        Optimizer optimizer = new Optimizer(this);
        optimizer.setListener(this);

        ParameterArray solution =
            optimizer.doOptimization(strategy, initialGuess, MAX_FITS);

        solution_ = ((PieceParameterArray)solution).getPieceList();
        System.out.println("Solution = " + solution_);

        Option<Seq<Piece>> moves = Option.empty();
        if (evaluateFitness(solution) >= MAX_FITS) {
            moves = Option.apply(JavaConversions.asScalaBuffer(solution_.getPieces()).toSeq());
        }
        long elapsedTime = System.currentTimeMillis() - startTime;
        puzzle.finalRefresh(moves, Option.apply(solution_), numTries_, elapsedTime);

        return moves;
    }

    public String getName() {
         return "Genetic Search Solver for Red Puzzle";
    }

    /**
     * terminate the solver if we find a solution with this fitness.
     */
    public double getOptimalFitness() {
        return 0;
    }

    public boolean evaluateByComparison() {
        return false;
    }

    /**
     * Return a low score if there are a lot of fits among the pieces.
     *
     * @param params  parameters
     * @return fitness value. Low is good.
     */
    public double evaluateFitness(ParameterArray params) {
        PieceList pieces = ((PieceParameterArray) params).getPieceList();
        double fitness = fitnessFinder.calculateFitness(pieces);
        if (fitness < currentBestFitness) {
            currentBestFitness = fitness;
        }
        params.setFitness(fitness);
        return fitness;
    }

    public double compareFitness(ParameterArray params1, ParameterArray params2) {
        assert false : "compareFitness not used since we evaluate in an absolute way.";
        return 0;
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
        System.out.println("current best = " + currentBestFitness);
    }
}
