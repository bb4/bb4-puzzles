/** Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.bridge.ui;

import java.awt.Choice;
import java.util.Arrays;

/**
 * A combo box that allows the user to select the sort of people that need to cross
 *
 * @author Barry Becker
 */
public final class InitialConfigurationSelector extends Choice {

    public static final Integer[] STANDARD_CONFIG =  new Integer[] {1, 2, 5, 8};
    public static final Integer[] ALTERNATIVE_CONFIG =  new Integer[] {5, 10, 20, 25};
    public static final Integer[] DIFFICULT_CONFIG =  new Integer[] {1, 2, 5, 7, 8, 12, 15};

    private static final String[] MENU_ITEMS = {
        "Standard problem: " + Arrays.toString(STANDARD_CONFIG),
        "Alternative problem: " + Arrays.toString(DIFFICULT_CONFIG),
        "Difficult problem: " + Arrays.toString(DIFFICULT_CONFIG)
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
            case 0 : return STANDARD_CONFIG;
            case 1 : return ALTERNATIVE_CONFIG;
            case 2 : return DIFFICULT_CONFIG;
            default: throw new IllegalArgumentException("Unexpected selected index: " + selected);
        }
    }
}