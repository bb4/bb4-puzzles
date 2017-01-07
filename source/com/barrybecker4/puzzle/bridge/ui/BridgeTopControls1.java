// Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.bridge.ui;

import com.barrybecker4.puzzle.bridge.BridgePuzzleController1;
import com.barrybecker4.puzzle.bridge.model.Bridge1;
import com.barrybecker4.puzzle.bridge.model.BridgeMove1;
import com.barrybecker4.puzzle.common.AlgorithmEnum;
import com.barrybecker4.puzzle.common.PuzzleController;
import com.barrybecker4.puzzle.common.ui.TopControlPanel;

import javax.swing.Box;
import javax.swing.JPanel;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Buttons at the top for generating and solving the puzzle using different strategies, and initial configurations.
 *
 * @author Barry Becker
 */
public final class BridgeTopControls1 extends TopControlPanel<Bridge1, BridgeMove1>
                                   implements ItemListener {

    private InitialConfigurationSelector1 configurationSelector;


    /**
     * The solve and generate button at the top.
     */
    public BridgeTopControls1(
            PuzzleController<Bridge1, BridgeMove1> controller, AlgorithmEnum<Bridge1, BridgeMove1>[] algorithmValues) {

        super(controller, algorithmValues);
    }

    protected void addFirstRowControls(JPanel panel) {
        super.addFirstRowControls(panel);
        configurationSelector = new InitialConfigurationSelector1();
        configurationSelector.addItemListener(this);

        panel.add(configurationSelector);
        panel.add(Box.createHorizontalGlue());
    }

    /**
     * size choice selected.
     * @param e  item event.
     */
    @Override
    public void itemStateChanged(ItemEvent e) {

        super.itemStateChanged(e);
        if (e.getSource() == configurationSelector)  {
            ((BridgePuzzleController1)controller_).setConfiguration(configurationSelector.getSelectedConfiguration());
        }
    }
}
