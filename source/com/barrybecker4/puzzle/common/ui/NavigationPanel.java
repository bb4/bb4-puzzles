// Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.common.ui;

import com.barrybecker4.common.app.AppContext;

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

    private JButton backButton_;
    private JButton forwardButton_;
    private int currentStep_;

    private PathNavigator navigator;

    public NavigationPanel() {
        super(new BorderLayout());

        backButton_ = new JButton(AppContext.getLabel("BACKWARD"));
        forwardButton_ = new JButton(AppContext.getLabel("FORWARD"));
        backButton_.addActionListener(this);
        forwardButton_.addActionListener(this);
        backButton_.setEnabled(false);
        forwardButton_.setEnabled(false);

        add(backButton_, BorderLayout.WEST);
        add(forwardButton_, BorderLayout.EAST);
    }

    public void setPathNavigator(PathNavigator navigator) {
        this.navigator = navigator;
        currentStep_ = navigator.getPath().size()-1;
        backButton_.setEnabled(true);
        forwardButton_.setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton_) {
            moveInPath(-1);
            backButton_.setEnabled((currentStep_ >= 0));
            forwardButton_.setEnabled(true);
        }
        else if (e.getSource() == forwardButton_) {
            moveInPath(1);
            boolean enable = (currentStep_ < navigator.getPath().size()-1);
            forwardButton_.setEnabled(enable);
            backButton_.setEnabled(true);
        }
    }

    /**
     * Switch from the current move in the sequence forwards or backwards stepSize.
     * @param stepSize num steps to move.
     */
    public void moveInPath(int stepSize) {
        if (stepSize == 0) return;
        moveInPath(currentStep_, stepSize);
        currentStep_ += stepSize;
    }


    public void moveInPath(int currentPosition, int stepSize) {
        int currentStep = currentPosition;
        int inc = stepSize > 0 ? 1 : -1;
        int toStep = currentStep + stepSize;
        if (inc > 0) {
            currentStep++;
            toStep++;
        }
        do {
            navigator.makeMove(currentStep, (inc < 0));
            currentStep += inc;
        } while (currentStep != toStep);
    }
}

