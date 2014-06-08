// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver;

import com.barrybecker4.optimization.OptimizationListener;
import com.barrybecker4.optimization.Optimizer;
import com.barrybecker4.optimization.optimizee.Optimizee;
import com.barrybecker4.optimization.parameter.ParameterArray;
import com.barrybecker4.optimization.strategy.OptimizationStrategyType;
import com.barrybecker4.puzzle.common.PuzzleController;
import com.barrybecker4.puzzle.tantrix.model.TantrixBoard;
import com.barrybecker4.puzzle.tantrix.model.TilePlacement;
import com.barrybecker4.puzzle.tantrix.model.TilePlacementList;
import com.barrybecker4.puzzle.tantrix.solver.path.PathEvaluator;
import com.barrybecker4.puzzle.tantrix.solver.path.TantrixPath;

import static com.barrybecker4.puzzle.tantrix.solver.path.PathEvaluator.SOLVED_THRESH;

/**
 * Solve the Tantrix puzzle using a genetic search algorithm.
 *
 * @author Barry Becker
 */
public class GeneticSearchSolver extends TantrixSolver
                                 implements Optimizee, OptimizationListener {

    /** either genetic or concurrent genetic strategy. */
    private OptimizationStrategyType strategy;
    private int numTries_;

    private PathEvaluator evaluator;
    private PuzzleController<TantrixBoard, TilePlacement> controller;


    /** Constructor */
    public GeneticSearchSolver(PuzzleController<TantrixBoard, TilePlacement> controller,
                               boolean useConcurrency) {
        super(controller.initialPosition());
        this.controller = controller;
        strategy = useConcurrency ? OptimizationStrategyType.CONCURRENT_GENETIC_SEARCH :
                                    OptimizationStrategyType.GENETIC_SEARCH;
        evaluator = new PathEvaluator();
    }

    /**
     * @return list of moves to a solution.
     */
    @Override
    public TilePlacementList solve()  {

        ParameterArray initialGuess = new TantrixPath(board);
        assert(initialGuess.size() > 0) : "The random path should have some tiles!";
        long startTime = System.currentTimeMillis();

        Optimizer optimizer = new Optimizer(this);
        optimizer.setListener(this);

        ParameterArray solution =
            optimizer.doOptimization(strategy, initialGuess, SOLVED_THRESH);

        solution_ =
            new TantrixBoard(((TantrixPath)solution).getTilePlacements(), board.getPrimaryColor());

        TilePlacementList tilePlacements;
        if (evaluateFitness(solution) <= 0) {
            tilePlacements = ((TantrixPath)solution).getTilePlacements();
        } else {
            tilePlacements = null;
        }
        long elapsedTime = System.currentTimeMillis() - startTime;
        controller.finalRefresh(tilePlacements, solution_, numTries_, elapsedTime);

        return tilePlacements;
    }

    public String getName() {
         return "Genetic Search Solver for Tantrix Puzzle";
    }

    /**
     * terminate the solver if we find a solution with this fitness.
     */
    public double getOptimalFitness() {
        return SOLVED_THRESH;
    }

    public boolean evaluateByComparison() {
        return false;
    }

    /**
     * Return 0 or less if a perfect solution has been found.
     *
     * @param params  parameters
     * @return fitness value. High is good.
     */
    public double evaluateFitness(ParameterArray params) {

        double fitness = evaluator.evaluateFitness((TantrixPath) params);
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
     * @param params optimized array of parameters representing tiles
     */
    public void optimizerChanged(ParameterArray params) {
        // update our current best guess at the solution.
        TantrixPath path = (TantrixPath)params;
        solution_ = new TantrixBoard(path.getTilePlacements(), path.getPrimaryPathColor());
        controller.refresh(solution_, numTries_++);
    }
}
