// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.ui;

import com.barrybecker4.puzzle.common.AlgorithmEnum;
import com.barrybecker4.puzzle.common.PuzzleController;
import com.barrybecker4.puzzle.common.Refreshable;
import com.barrybecker4.puzzle.common.ui.PuzzleApplet;
import com.barrybecker4.puzzle.common.ui.PuzzleViewer;
import com.barrybecker4.puzzle.tantrix.TantrixController;
import com.barrybecker4.puzzle.tantrix.model.TantrixBoard;
import com.barrybecker4.puzzle.tantrix.model.TilePlacement;
import com.barrybecker4.puzzle.tantrix.solver.Algorithm;
import com.barrybecker4.ui.util.GUIUtil;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Tantrix Puzzle Application to show the solving of the puzzle.
 *
 * @author Barry Becker
 */
public final class TantrixPuzzle extends PuzzleApplet<TantrixBoard, TilePlacement>
                                 implements ChangeListener {
    JSpinner spinner;
    private static final int DEFAULT_NUM_TILES = 7;

    /**
     * Required no arg constructor.
     */
    public TantrixPuzzle() {}

    /**
     * Construct the application.
     */
    public TantrixPuzzle(String[] args) {
        super(args);
    }

    @Override
    protected PuzzleViewer<TantrixBoard, TilePlacement> createViewer() {

        //TantrixBoard board = new TantrixBoard(new HexTiles());
        return new TantrixViewer();
    }

    @Override
    protected PuzzleController<TantrixBoard, TilePlacement>
                createController(Refreshable<TantrixBoard, TilePlacement> viewer) {
        TantrixController controller = new TantrixController(viewer);
        controller.setNumTiles(DEFAULT_NUM_TILES);
        return controller;
    }

    @Override
    protected AlgorithmEnum<TantrixBoard, TilePlacement>[] getAlgorithmValues() {
        return Algorithm.values();
    }

    @Override
    protected JPanel createCustomControls() {
        JLabel label = new JLabel("Number of Tiles");
        SpinnerModel model = new SpinnerNumberModel(DEFAULT_NUM_TILES, 3, 30, 1);
        spinner = new JSpinner(model);
        spinner.addChangeListener(this);

        JPanel numTilesSelector = new JPanel();
        numTilesSelector.add(label);
        numTilesSelector.add(spinner);

        return numTilesSelector;
    }

    @Override
    public void stateChanged(ChangeEvent e) {

        getController().setNumTiles((Integer)spinner.getValue());
    }

    private TantrixController getController() {
        return ((TantrixController)controller_);
    }

    /**
     * use this to run as an application instead of an applet.
     */
    public static void main( String[] args )  {

        PuzzleApplet applet = new TantrixPuzzle(args);

        // this will call applet.init() and start() methods instead of the browser
        GUIUtil.showApplet(applet);
    }
}
