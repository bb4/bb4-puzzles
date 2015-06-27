/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.hiq.ui;

import com.barrybecker4.puzzle.common.AlgorithmEnum;
import com.barrybecker4.puzzle.common.PuzzleController;
import com.barrybecker4.common.search.Refreshable;
import com.barrybecker4.puzzle.common.ui.DoneListener;
import com.barrybecker4.puzzle.common.ui.NavigationPanel;
import com.barrybecker4.puzzle.common.ui.PathNavigator;
import com.barrybecker4.puzzle.common.ui.PuzzleApplet;
import com.barrybecker4.puzzle.common.ui.PuzzleViewer;
import com.barrybecker4.puzzle.hiq.Algorithm;
import com.barrybecker4.puzzle.hiq.HiQController;
import com.barrybecker4.puzzle.hiq.model.PegBoard;
import com.barrybecker4.puzzle.hiq.model.PegMove;
import com.barrybecker4.ui.util.GUIUtil;

import javax.swing.JPanel;

/**
 * HiQ Puzzle.
 * This program solves a very difficult classic solitaire puzzle
 * where you jump pegs to remove them in a cross shaped peg-board.
 * The fewer pegs you have remaining at the end, the better.
 * A perfect solution is to have only one peg in the center square
 * at the end.
 *  Assuming an average of 7 different options on each move, there are
 *   7 ^ 32 = 10,000,000,000,000,000,000,000,000,000,000
 *  (10 decillion combinations)
 * Actually this calculation is not correct since many paths lead
 * to the same board positions.
 * There are actually only 23.4 million unique board positions
 * see http://www.durangobill.com/Peg33.html for analysis.
 *    A brute force solution will run for years on today's fastest
 * computers.
 * See http://homepage.sunrise.ch/homepage/pglaus/Solitaire/solitaire.htm
 * for a solution that uses a genetic algorithm to find a solution quickly.
 *
 * My initial approach was to apply a kind of tunnel method.
 * I tried to solve the problem from both ends.
 * First, I work backwards for 32-FORWARD_MOVE's and build a
 * hashmap of all the possible board positions - storing a path to the solution
 * at each hashmap location. Then I traverse forward from the initial position
 * for BACK_MOVE's until I reach one of these positions that I know
 * leads to the solution. Then I combined the 2 paths to see the sequence
 * that will lead to the solution.
 *   Finally, I found that it was enough to search entirely from the beginning
 * and just prune when I reach states I've encountered before.
 * When I first ran this successfully, it took about 1 hour to run on an AMD 64bit 3200.
 * After optimization it  ran in about 3 minutes on a Core2Duo (189 seconds).
 * After parallelizing the algorithm using ConcurrentPuzzleSolver it is down to 93 seconds on the CoreDuo.
 */
public final class HiQPuzzle extends PuzzleApplet<PegBoard, PegMove>
                             implements DoneListener {

    private NavigationPanel navPanel;

    /** Construct the applet */
    public HiQPuzzle() {}

    /** Construct the application */
    public HiQPuzzle(String[] args) {
        super(args);
    }


    @Override
    protected PuzzleViewer<PegBoard, PegMove> createViewer() {
        return new PegBoardViewer(PegBoard.INITIAL_BOARD_POSITION, this);
    }

    @Override
    protected PuzzleController<PegBoard, PegMove> createController(Refreshable<PegBoard, PegMove> viewer_) {
        return new HiQController(viewer_);
    }

    @Override
    protected AlgorithmEnum<PegBoard, PegMove>[] getAlgorithmValues() {
        return Algorithm.values();
    }

    @Override
    protected JPanel createBottomControls() {

        navPanel = new NavigationPanel();
        return navPanel;
    }

    @Override
    public void done() {
        navPanel.setPathNavigator((PathNavigator) viewer_);
    }

    /**
     * Use this to run as an application instead of an applet.
     */
    public static void main(String[] args) {

        PuzzleApplet applet = new HiQPuzzle(args);

        // this will call applet.init() and start() methods instead of the browser
        GUIUtil.showApplet(applet);
    }

}

