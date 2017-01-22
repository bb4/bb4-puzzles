// Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle1.ui;

import com.barrybecker4.puzzle.common.AlgorithmEnum;
import com.barrybecker4.puzzle.common.PuzzleController;
import com.barrybecker4.puzzle.common.ui.TopControlPanel;
import com.barrybecker4.puzzle.slidingpuzzle1.SlidingPuzzleController;
import com.barrybecker4.puzzle.slidingpuzzle1.model.SlideMove;
import com.barrybecker4.puzzle.slidingpuzzle1.model.SliderBoard;

import javax.swing.Box;
import javax.swing.JPanel;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Buttons at the top for generating and solving the puzzle using different strategies.
 *
 * @author Barry Becker
 */
public final class SliderTopControls extends TopControlPanel<SliderBoard, SlideMove>
                                   implements ItemListener {

    private SizeSelector sizeSelector_;


    /**
     * The solve and generate button at the top.
     */
    SliderTopControls(
            PuzzleController<SliderBoard, SlideMove> controller, AlgorithmEnum<SliderBoard,  SlideMove>[] algorithmValues) {

        super(controller, algorithmValues);
    }

    protected void addFirstRowControls(JPanel panel) {
        super.addFirstRowControls(panel);
        sizeSelector_ = new SizeSelector();
        sizeSelector_.addItemListener(this);

        panel.add(sizeSelector_);
        panel.add(Box.createHorizontalGlue());
    }

    /**
     * size choice selected.
     * @param e  item event.
     */
    @Override
    public void itemStateChanged(ItemEvent e) {

        super.itemStateChanged(e);
        if (e.getSource() == sizeSelector_)  {
            ((SlidingPuzzleController)controller_).setSize(sizeSelector_.getSelectedSize());
        }
    }
}
