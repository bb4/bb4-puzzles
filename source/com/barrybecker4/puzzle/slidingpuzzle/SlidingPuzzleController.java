// Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle;

import com.barrybecker4.common.search.Refreshable;
import com.barrybecker4.puzzle.common.ui.AbstractPuzzleController;
import com.barrybecker4.puzzle.slidingpuzzle.model.SliderBoard;
import com.barrybecker4.puzzle.slidingpuzzle.model.SlideMove;
import com.barrybecker4.puzzle.slidingpuzzle.model.MoveGenerator;

import java.util.List;

/**
 * Sliding Puzzle Controller.
 * See puzzle.common for puzzle framework classes.
 * See http://kociemba.org/fifteen/fifteensolver.html
 * Inspired by Peter Norvig's "Design of Computer Programs" class on Udacity.
 *
 * @author Barry Becker
 */
public class SlidingPuzzleController extends AbstractPuzzleController<SliderBoard, SlideMove> {

    private static final byte DEFAULT_SIZE = 3;
    private static final MoveGenerator generator = new MoveGenerator();

    private SliderBoard initialPosition;

    /**
     * @param ui shows the current state on the screen.
     */
    public SlidingPuzzleController(Refreshable<SliderBoard, SlideMove> ui) {
        super(ui);
        initialPosition = new SliderBoard(DEFAULT_SIZE, true);
        algorithm_ = Algorithm.A_STAR_SEQUENTIAL;
    }

    /** @param size the edge length of the puzzle to be solved */
    public void setSize(int size) {
        initialPosition = new SliderBoard((byte)size, true);
        if (ui_ != null) ui_.refresh(initialPosition, 0);
    }

    @Override
    public SliderBoard initialState() {
        return initialPosition;
    }

    @Override
    public boolean isGoal(SliderBoard position) {
        return position.isSolved();
    }

    @Override
    public List<SlideMove> legalTransitions(SliderBoard position) {
        return generator.generateMoves(position);
    }

    @Override
    public SliderBoard transition(SliderBoard position, SlideMove move) {
        return position.doMove(move);
    }

    /**
     * There are several commonly used "admissible" heuristics for determining this distance.
     * Here they are in order of easy/low quality to hard/high quality. Option 2 (manhattan) is implemented here.
     * 1) Number of tiles not in final position. Easies, but not that great.
     * 2) Manhattan distance: for each piece, sum the manhattan distance to its goal position. Easy, but not the best
     * 3) Walking distance (http://www.ic-net.or.jp/home/takaken/e/15pz/wd.gif). Hard, but better.
     * 4) disjoint pattern databases. See http://heuristicswiki.wikispaces.com/pattern+database
     * @return estimate of the cost to reach the goal of all 9 pieces successfully placed
     */
    public int distanceFromGoal(SliderBoard position) {
        return position.distanceToGoal();
    }
}
