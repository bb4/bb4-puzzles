/** Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.bridge.ui;

import com.barrybecker4.puzzle.bridge.BridgePuzzleController;
import com.barrybecker4.puzzle.bridge.model.Bridge;
import com.barrybecker4.puzzle.bridge.model.BridgeMove;
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
public final class BridgeTopControls extends TopControlPanel<Bridge, BridgeMove>
                                   implements ItemListener {

    private InitialConfigurationSelector configurationSelector;


    /**
     * The solve and generate button at the top.
     */
    public BridgeTopControls(
            PuzzleController<Bridge, BridgeMove> controller, AlgorithmEnum<Bridge, BridgeMove>[] algorithmValues) {

        super(controller, algorithmValues);
    }

    protected void addFirstRowControls(JPanel panel) {
        super.addFirstRowControls(panel);
        configurationSelector = new InitialConfigurationSelector();
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
            ((BridgePuzzleController)controller_).setConfiguration(configurationSelector.getSelectedConfiguration());
        }
    }
}
