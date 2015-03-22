/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.common.ui;

import com.barrybecker4.common.app.AppContext;
import com.barrybecker4.puzzle.common.AlgorithmEnum;
import com.barrybecker4.puzzle.common.PuzzleController;
import com.barrybecker4.ui.components.GradientButton;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Shows the main button controls in a panel above the puzzle.
 *
 * @author Barry Becker
 */
public class TopControlPanel<P, M> extends JPanel
                                   implements ActionListener, ItemListener {
    protected PuzzleController<P, M> controller_;
    protected AlgorithmEnum<P, M>[] algorithmValues_;

    private JButton solveButton_;
    private JComboBox<String> algorithmChoice_;

    /**
     * Constructor.
     */
    public TopControlPanel(PuzzleController<P, M> controller, AlgorithmEnum<P, M>[] algorithmValues) {
        controller_ = controller;
        algorithmValues_ = algorithmValues;

        JPanel firstRowPanel = new JPanel(createLayout());
        addFirstRowControls(firstRowPanel);
        firstRowPanel.add(new JPanel());

        JPanel additionalControlsPanel = new JPanel(createLayout());
        addAdditionalControls(additionalControlsPanel);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(firstRowPanel, BorderLayout.NORTH);
        if (additionalControlsPanel.getComponents().length > 0)  {
            mainPanel.add(additionalControlsPanel, BorderLayout.CENTER);
        }
        add(mainPanel);
    }

    private FlowLayout createLayout() {
        FlowLayout layout = new FlowLayout();
        layout.setAlignment(FlowLayout.LEADING);
        return layout;
    }

    protected void addFirstRowControls(JPanel firstRowPanel)  {

        solveButton_ = new GradientButton(AppContext.getLabel("SOLVE"));
                solveButton_.addActionListener(this);

        firstRowPanel.add(solveButton_);
        firstRowPanel.add(createAlgorithmDropdown());
    }

    protected void addAdditionalControls(JPanel panel) {
        // override to add stuff
    }

    protected PuzzleController<P, M> getController() {
        return controller_;
    }

    /**
     * The dropdown menu at the top for selecting an algorithm for solving the puzzle.
     * @return a dropdown/down component.
     */
    private JComboBox createAlgorithmDropdown() {
        algorithmChoice_ = new JComboBox<>();
        algorithmChoice_.addItemListener(this);
        for (AlgorithmEnum a: algorithmValues_) {
            algorithmChoice_.addItem(a.getLabel());
        }
        algorithmChoice_.setSelectedIndex(controller_.getAlgorithm().ordinal());
        return algorithmChoice_;
    }

    /**
     * algorithm selected.
     * @param e item event
     */
    @Override
    public void itemStateChanged(ItemEvent e) {

        int selected = algorithmChoice_.getSelectedIndex();
        controller_.setAlgorithm(algorithmValues_[selected]);
    }

    /**
     * Solve button clicked.
     * Must execute long tasks in a separate thread,
     * otherwise you don't see the steps of the animation.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        Object src = e.getSource();

        if (src == solveButton_)  {
            controller_.startSolving();
        }
    }
}

