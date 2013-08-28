// Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.common.ui;

import com.barrybecker4.common.app.AppContext;
import com.barrybecker4.ui.components.GradientButton;
import com.barrybecker4.ui.util.GUIUtil;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Shows two buttons to control stepping forward and backward through the puzzle solution.
 */
public final class NavigationPanel extends JPanel
                                   implements ActionListener {

    public static final String IMAGE_ROOT = "com/barrybecker4/puzzle/common/ui/images/";   // NON-NLS

    private JButton backButton;
    private JButton forwardButton;

    private JButton startButton;
    private JButton endButton;

    private int currentStep;

    private PathNavigator navigator;

    public NavigationPanel() {
        super(new BorderLayout());

        add(createBackPanel(), BorderLayout.WEST);
        add(createForwardPanel(), BorderLayout.EAST);
    }

    private JPanel createForwardPanel() {
        forwardButton = createButton("FORWARD", "forward_arrow.png");
        endButton = createButton("END", "end_arrow.png");

        JPanel forwardPanel = new JPanel();
        forwardPanel.add(forwardButton);
        forwardPanel.add(endButton);
        return forwardPanel;
    }

    private JPanel createBackPanel() {
        backButton = createButton("BACKWARD", "backward_arrow.png");
        startButton = createButton("START", "start_arrow.png");

        JPanel backPanel = new JPanel();
        backPanel.add(startButton);
        backPanel.add(backButton);
        return backPanel;
    }

    private JButton createButton(String msgKey, String iconName) {
        JButton button = new GradientButton(AppContext.getLabel(msgKey),
                                    GUIUtil.getIcon(IMAGE_ROOT + iconName));
        button.addActionListener(this);
        button.setEnabled(false);
        return button;
    }

    public void setPathNavigator(PathNavigator navigator) {
        this.navigator = navigator;
        currentStep = navigator.getPath().size()-1;
        updateButtonStates();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            moveInPath(-1);
        }
        else if (e.getSource() == forwardButton) {
            moveInPath(1);
        }
        else if (e.getSource() == startButton) {
            moveInPath(-currentStep-1);
        }
        else if (e.getSource() == endButton) {
            int stepsUntilEnd = navigator.getPath().size() - currentStep - 1;
            moveInPath(stepsUntilEnd);
        }
        updateButtonStates();
    }

    private void updateButtonStates() {
        boolean isAtStart =  currentStep == -1;
        boolean isAtEnd = currentStep == navigator.getPath().size()-1;
        backButton.setEnabled(!isAtStart);
        startButton.setEnabled(!isAtStart);
        forwardButton.setEnabled(!isAtEnd);
        endButton.setEnabled(!isAtEnd);
    }

    /**
     * Switch from the current move in the sequence forwards or backwards stepSize.
     * @param stepSize num steps to move.
     */
    public void moveInPath(int stepSize) {
        if (stepSize == 0) return;
        moveInPath(currentStep, stepSize);
        currentStep += stepSize;
    }

    public void moveInPath(int currentPosition, int stepSize) {
        int currentStep = currentPosition;
        boolean moveForward = stepSize > 0;
        int inc = moveForward ? 1 : -1;
        int toStep = currentStep + stepSize;
        if (moveForward) {
            currentStep++;
            toStep++;
        }
        do {
            System.out.println("makeMove " + currentStep + "+ fwd="+ moveForward);
            navigator.makeMove(currentStep, !moveForward);
            currentStep += inc;
        } while (currentStep != toStep);
    }
}

