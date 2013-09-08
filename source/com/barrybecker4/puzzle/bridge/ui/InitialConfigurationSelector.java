/** Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.bridge.ui;

import java.awt.Choice;

import static com.barrybecker4.puzzle.bridge.model.InitialConfiguration.*;

/**
 * A combo box that allows the user to select the sort of people that need to cross
 *
 * @author Barry Becker
 */
public final class InitialConfigurationSelector extends Choice {


    private static final String[] MENU_ITEMS = {
        STANDARD_PROBLEM.getLabel(),
        ALTERNATIVE_PROBLEM.getLabel(),
        DIFFICULT_PROBLEM.getLabel(),
        SUPER_HARD.getLabel()
    };

    /**
     * Constructor.
     */
    public InitialConfigurationSelector() {
        for (final String item : MENU_ITEMS) {
            add(item);
        }
        select(0);
    }

    /**
     * @return  the puzzle size for what was selected.
     */
    public Integer[] getSelectedConfiguration() {
        int selected = getSelectedIndex();
        switch(selected) {
            case 0 : return STANDARD_PROBLEM.getPeopleSpeeds();
            case 1 : return ALTERNATIVE_PROBLEM.getPeopleSpeeds();
            case 2 : return DIFFICULT_PROBLEM.getPeopleSpeeds();
            case 3 : return SUPER_HARD.getPeopleSpeeds();
            default: throw new IllegalArgumentException("Unexpected selected index: " + selected);
        }
    }
}