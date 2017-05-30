// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix1;

import com.barrybecker4.common.search.Refreshable;
import com.barrybecker4.puzzle.common1.ui.AbstractPuzzleController;
import com.barrybecker4.puzzle.tantrix1.generation.MoveGenerator;
import com.barrybecker4.puzzle.tantrix1.model.HexTiles;
import com.barrybecker4.puzzle.tantrix1.model.TantrixBoard;
import com.barrybecker4.puzzle.tantrix1.model.TilePlacement;
import com.barrybecker4.puzzle.tantrix1.solver.Algorithm;
import com.barrybecker4.puzzle.tantrix1.solver.path.PathEvaluator;
import com.barrybecker4.puzzle.tantrix1.solver.path.TantrixPath;
import scala.collection.Seq;

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

    private static final int MIN_NUM_TILES = 3;
    private int numTiles = MIN_NUM_TILES;

    private PathEvaluator evaluator = new PathEvaluator();

    /**
     * Creates a new instance of the Controller
     */
    public TantrixController(Refreshable<TantrixBoard, TilePlacement> ui) {
        super(ui);
        algorithm_ = Algorithm.SIMPLE_SEQUENTIAL;
    }

    public void setNumTiles(int numTiles) {
        this.numTiles = numTiles;
    }

    public TantrixBoard initialState() {
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

    public Seq<TilePlacement> legalTransitions(TantrixBoard position) {
        return new MoveGenerator(position).generateMoves().asSeq();
    }

    public TantrixBoard transition(TantrixBoard position, TilePlacement move) {
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
