/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.slidingpuzzle.ui;

import com.barrybecker4.puzzle.common.AlgorithmEnum;
import com.barrybecker4.puzzle.common.PuzzleController;
import com.barrybecker4.puzzle.common.ui.TopControlPanel;
import com.barrybecker4.puzzle.slidingpuzzle.SlidingPuzzleController;
import com.barrybecker4.puzzle.slidingpuzzle.model.SlideMove;
import com.barrybecker4.puzzle.slidingpuzzle.model.Slider;

import javax.swing.Box;
import javax.swing.JPanel;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Buttons at the top for generating and solving the puzzle using different strategies.
 *
 * @author Barry Becker
 */
public final class SliderTopControls extends TopControlPanel<Slider, SlideMove>
                                   implements ItemListener {

    private SizeSelector sizeSelector_;


    /**
     * The solve and generate button at the top.
     */
    public SliderTopControls(
            PuzzleController<Slider, SlideMove> controller, AlgorithmEnum<Slider,  SlideMove>[] algorithmValues) {

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
