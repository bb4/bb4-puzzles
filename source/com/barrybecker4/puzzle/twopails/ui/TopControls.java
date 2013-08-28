/** Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.twopails.ui;

import com.barrybecker4.puzzle.common.AlgorithmEnum;
import com.barrybecker4.puzzle.common.PuzzleController;
import com.barrybecker4.puzzle.common.ui.TopControlPanel;
import com.barrybecker4.puzzle.twopails.TwoPailsPuzzleController;
import com.barrybecker4.puzzle.twopails.model.PailParams;
import com.barrybecker4.puzzle.twopails.model.Pails;
import com.barrybecker4.puzzle.twopails.model.PourOperation;
import com.barrybecker4.ui.components.NumberInput;

import javax.swing.JPanel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static com.barrybecker4.puzzle.twopails.model.PailParams.MAX_CAPACITY;

/**
 * Buttons at the top for generating and solving the puzzle using different strategies.
 *
 * @author Barry Becker
 */
public final class TopControls extends TopControlPanel<Pails, PourOperation> implements KeyListener {

    /** click this button to generate a new puzzle */
    //private JButton generateButton;

    // ui for entering the direction probabilities.
    protected NumberInput firstPailSize;
    protected NumberInput secondPailSize;
    protected NumberInput targetMeasure;


    /**
     * The solve and generate button at the top.
     */
    public TopControls(
            PuzzleController<Pails, PourOperation> controller, AlgorithmEnum<Pails, PourOperation>[] algorithmValues) {

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
        //generateButton = new GradientButton("Generate");
        //generateButton.addActionListener(this);

        panel.add(firstPailSize);
        panel.add(secondPailSize);
        panel.add(targetMeasure);
        //add(generateButton);
        //add(Box.createHorizontalGlue());
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
