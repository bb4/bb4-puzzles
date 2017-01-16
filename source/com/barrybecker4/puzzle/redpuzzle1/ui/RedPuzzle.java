// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle1.ui;

import com.barrybecker4.puzzle.common.AlgorithmEnum;
import com.barrybecker4.puzzle.common.PuzzleController;
import com.barrybecker4.common.search.Refreshable;
import com.barrybecker4.puzzle.common.ui.PuzzleApplet;
import com.barrybecker4.puzzle.common.ui.PuzzleViewer;
import com.barrybecker4.puzzle.redpuzzle1.RedPuzzleController;
import com.barrybecker4.puzzle.redpuzzle1.model.Piece;
import com.barrybecker4.puzzle.redpuzzle1.model.PieceList;
import com.barrybecker4.puzzle.redpuzzle1.solver.Algorithm;
import com.barrybecker4.ui.sliders.LabeledSlider;
import com.barrybecker4.ui.sliders.SliderChangeListener;
import com.barrybecker4.ui.util.GUIUtil;

import javax.swing.JPanel;

/**
 * Red Puzzle Application to show the solving of the puzzle.
 * This program solves a 9 piece puzzle that has nubs on all 4 sides of every piece.
 * Its virtually impossible to solve by hand because of all the possible permutations.
 * This program can usually solve it by trying between 6,000 and 20,000 combinations
 * in a brute force manner. Other more sophisticated solvers can do it in far fewer tries.
 *
 * For random number seed =5 and mutable piece objects it takes
 * BruteForce < 8.0s and Genetic= 3.0s
 * After refactoring and applying the generic solver pattern (see puzzle.common) things were faster
 * BruteForce Sequential &lt; 1.0s  BruteForce concurrent &lt; 0.1s
 * @author Barry Becker
 */
public final class RedPuzzle extends PuzzleApplet<PieceList, Piece>
                            implements SliderChangeListener {

    /** allows you to change the animation speed. */
    private LabeledSlider animSpeedSlider_;

    /**
     * No argument Construction so it can be created with reflection.
     */
    public RedPuzzle() {
    }

    /**
     * Construct the application and set the look and feel.
     */
    public RedPuzzle(String[] args) {
       super(args);
    }

    @Override
    protected PuzzleViewer<PieceList, Piece> createViewer() {
        return new RedPuzzleViewer();
    }

    @Override
    protected PuzzleController<PieceList, Piece> createController(Refreshable<PieceList, Piece> viewer_) {
        return new RedPuzzleController(viewer_);
    }

    @Override
    protected AlgorithmEnum<PieceList, Piece>[] getAlgorithmValues() {
        return Algorithm.values();
    }

    @Override
    protected JPanel createBottomControls() {

        animSpeedSlider_ =
            new LabeledSlider("Speed ",
                    RedPuzzleViewer.INITIAL_ANIM_SPEED, 1,
                    RedPuzzleViewer.MAX_ANIM_SPEED);
        animSpeedSlider_.setResolution(RedPuzzleViewer.MAX_ANIM_SPEED - 1);
        animSpeedSlider_.setShowAsInteger(true);
        animSpeedSlider_.addChangeListener(this);

        return animSpeedSlider_;
    }

    @Override
    public void sliderChanged(LabeledSlider slider) {
        ((RedPuzzleViewer)viewer_).setAnimationSpeed((int) animSpeedSlider_.getValue());
    }


    /**
     * use this to run as an application instead of an applet.
     */
    public static void main(String[] args)  {

        PuzzleApplet applet = new RedPuzzle(args);

        // this will call applet.init() and start() methods instead of the browser
        GUIUtil.showApplet(applet);
    }
}
