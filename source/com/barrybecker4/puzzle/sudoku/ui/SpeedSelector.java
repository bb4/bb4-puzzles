// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.ui;

import java.awt.Choice;

/**
 * A combo box that allows the user to select the speed at which the puzzle is generated or solved.
 *
 * @author Barry Becker
 */
public final class SpeedSelector extends Choice {

    private static final int DEFAULT_SELECTION = 1;

    private static final String[] SPEED_CHOICES = {
        "Fastest (no animation)",
        "Fastest with animation",
        "Medium speed",
        "Slow speed",
        "Extremely slow"
    };

    /**
     * Constructor.
     */
    public SpeedSelector() {
        for (final String item : SPEED_CHOICES) {
            add(item);
        }
        select(DEFAULT_SELECTION);
    }

    /**
     * @return  the delay for selected speed.
     */
    public int getSelectedDelay() {
        switch (this.getSelectedIndex())  {
            case 0 : return -1;
            case 1 : return 0;
            case 2 : return 10;
            case 3 : return 50;
            case 4 : return 400;
            default: assert false : " undexpected index";
        }
        return 0;
    }
}