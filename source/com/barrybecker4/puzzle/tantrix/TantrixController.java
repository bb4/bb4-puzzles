// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix;

import com.barrybecker4.puzzle.common.Refreshable;
import com.barrybecker4.puzzle.common.ui.AbstractPuzzleController;
import com.barrybecker4.puzzle.tantrix.model.HexTiles;
import com.barrybecker4.puzzle.tantrix.model.MoveGenerator;
import com.barrybecker4.puzzle.tantrix.model.TantrixBoard;
import com.barrybecker4.puzzle.tantrix.model.TilePlacement;
import com.barrybecker4.puzzle.tantrix.model.TilePlacementList;
import com.barrybecker4.puzzle.tantrix.solver.Algorithm;
import com.barrybecker4.puzzle.tantrix.solver.path.PathEvaluator;
import com.barrybecker4.puzzle.tantrix.solver.path.TantrixPath;

/**
 * The controller allows the solver to do its thing by providing the PuzzleController api.
 *
 * The generic solvers (sequential and concurrent) expect the first class param
 * to represent the state of a board, and the TilePlacement (second param)
 * to represent a move. The way a move is applied is simply to add the piece to the
 * end of the current list.
 *
 * @author Barry Becker
 */
public class TantrixController
       extends AbstractPuzzleController<TantrixBoard, TilePlacement> {

    public static final int MIN_NUM_TILES = 3;
    int numTiles = MIN_NUM_TILES;

    private PathEvaluator evaluator = new PathEvaluator();

    /**
     * Creates a new instance of the Controller
     */
    public TantrixController(Refreshable<TantrixBoard, TilePlacement> ui) {
        super(ui);
        algorithm_ = Algorithm.SEQUENTIAL;
    }

    public void setNumTiles(int numTiles) {
        this.numTiles = numTiles;
    }

    public TantrixBoard initialPosition() {
        //MathUtil.RANDOM.setSeed(1);
        return new TantrixBoard(new HexTiles().createRandomList(numTiles));
    }

    /**
     * @return true if there is a loop of the primary color and all the
     * secondary color path connections match.
     */
    public boolean isGoal(TantrixBoard position) {
        return position.isSolved();
    }

    public TilePlacementList legalMoves(TantrixBoard position) {
        return new MoveGenerator(position).generateMoves();
    }

    public TantrixBoard move(TantrixBoard position, TilePlacement move) {
        return position.placeTile(move);
    }

    /**
     * @return estimate of the cost to reach the goal from the specified position
     */
    public int distanceFromGoal(TantrixBoard position) {
        TantrixPath path = new TantrixPath(position.getTantrix(), position.getPrimaryColor());
        double fitness = evaluator.evaluateFitness(path);
        return (int) (10.0 * Math.max(0, PathEvaluator.SOLVED_THRESH - fitness));
    }

}
