// Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.twopails1.ui;

import com.barrybecker4.puzzle.common1.AlgorithmEnum;
import com.barrybecker4.puzzle.common1.PuzzleController;
import com.barrybecker4.puzzle.common1.ui.TopControlPanel;
import com.barrybecker4.puzzle.twopails1.TwoPailsPuzzleController;
import com.barrybecker4.puzzle.twopails1.model.PailParams;
import com.barrybecker4.puzzle.twopails1.model.Pails;
import com.barrybecker4.puzzle.twopails1.model.PourOperation;
import com.barrybecker4.ui.components.NumberInput;

import javax.swing.JPanel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static com.barrybecker4.puzzle.twopails1.model.PailParams.MAX_CAPACITY;

/**
 * Buttons at the top for generating and solving the puzzle using different strategies.
 *
 * @author Barry Becker
 */
public final class TopControls extends TopControlPanel<Pails, PourOperation> implements KeyListener {

    // ui for entering the direction probabilities.
    private NumberInput firstPailSize;
    private NumberInput secondPailSize;
    private NumberInput targetMeasure;


    /**
     * The solve and generate button at the top.
     */
    TopControls(
            PuzzleController<Pails, PourOperation> controller,
            AlgorithmEnum<Pails, PourOperation>[] algorithmValues) {

        super(controller, algorithmValues);
    }

    @Override
    protected void addAdditionalControls(JPanel panel) {
        super.addAdditionalControls(panel);

        firstPailSize = new NumberInput("First Pail Size", 9,
                          "The fill capacity of the first container", 1, MAX_CAPACITY, true);
        secondPailSize = new NumberInput("Second Pail Size", 4,
                          "The fill capacity of the second container", 1, MAX_CAPACITY, true);
        targetMeasure = new NumberInput("Target measure", 6,
                          "The amount of liquid that is to be measured out exactly", 1, MAX_CAPACITY, true);

        firstPailSize.addKeyListener(this);
        secondPailSize.addKeyListener(this);
        targetMeasure.addKeyListener(this);

        panel.add(firstPailSize);
        panel.add(secondPailSize);
        panel.add(targetMeasure);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {

        PailParams params =
                new PailParams(firstPailSize.getIntValue(), secondPailSize.getIntValue(), targetMeasure.getIntValue());
        ((TwoPailsPuzzleController)controller_).setParams(params);
    }
}
