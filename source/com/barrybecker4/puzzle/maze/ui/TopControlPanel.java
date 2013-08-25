/** Copyright by Barry G. Becker, 2000-2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.maze.ui;

import com.barrybecker4.puzzle.maze.MazeController;
import com.barrybecker4.ui.components.GradientButton;
import com.barrybecker4.ui.components.NumberInput;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A maze generator and solver
 * @author Barry Becker
 */
public class TopControlPanel extends JPanel
                             implements ActionListener {

    /** the passage thickness in pixels */
    private static final int PASSAGE_THICKNESS = 40;
    private static final int INITIAL_ANIMATION_SPEED = 20;

    protected NumberInput thicknessField = null;

    // ui for entering the direction probabilities.
    protected NumberInput forwardProbField;
    protected NumberInput leftProbField;
    protected NumberInput rightProbField;
    protected NumberInput animationSpeedField;

    protected GradientButton regenerateButton;
    protected GradientButton solveButton;

    private MazeController controller;


    /** constructor */
    public TopControlPanel(MazeController controller) {

        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        this.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        this.controller = controller;
        thicknessField = new NumberInput("Thickness", PASSAGE_THICKNESS,
                                          "The passage thickness", 2, 200, true);
        animationSpeedField = new NumberInput("Speed", INITIAL_ANIMATION_SPEED,
                                               "The animation speed (large number is slow).", 1, 100, true);

        forwardProbField = new NumberInput("Forward", 0.34,
                                            "The probability of moving straight forward", 0, 1.0, false);
        leftProbField = new NumberInput("Left", 0.33,
                                         "The probability of moving left", 0, 1.0, false);
        rightProbField = new NumberInput("Right", 0.33,
                                          "The probability of moving right", 0, 1.0, false);
        add(thicknessField);
        add(animationSpeedField);
        add(forwardProbField);
        add(leftProbField);
        add(rightProbField);

        regenerateButton = new GradientButton( "Generate" );
        regenerateButton.addActionListener(this);
        add(regenerateButton);

        solveButton = new GradientButton( "Solve" );
        solveButton.addActionListener(this);
        add(solveButton);

        controller.setRepaintListener(this);
    }

    /**
     * called when a button is pressed.
     */
    @Override
    public void actionPerformed( ActionEvent e )  {

        Object source = e.getSource();

        if (source == regenerateButton) {
            regenerate();
        }
        if (source == solveButton) {
            controller.solve(getAnimationSpeed());
        }
    }

    public void regenerate() {
        controller.regenerate(getThickness(), getAnimationSpeed(),
                    getForwardProbability(), getLeftProbability(), getRightProbability());
        //invalidate();
        //this.repaint();
    }

    private int getThickness() {
        return  thicknessField.getIntValue();
    }

    private double getForwardProbability() {
        return forwardProbField.getValue();
    }

    private double getLeftProbability() {
        return leftProbField.getValue();
    }

    private double getRightProbability() {
        return rightProbField.getValue();
    }

    private int getAnimationSpeed() {
        return animationSpeedField.getIntValue();
    }

}