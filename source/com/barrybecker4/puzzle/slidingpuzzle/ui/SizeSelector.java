/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.slidingpuzzle.ui;

import java.awt.Choice;

/**
 * A combo box that allows the user to select the size of the puzzle
 *
 * @author Barry Becker
 */
public final class SizeSelector extends Choice {

    private static final String[] MENU_ITEMS = {
        "3 Tiles",
        "8 Tiles",
        "15 Tiles",
        "24 Tiles"
    };

    /**
     * Constructor.
     */
    public SizeSelector() {
        for (final String item : MENU_ITEMS) {
            add(item);
        }
        select(1);
    }

    /**
     * @return  the puzzle size for what was selected.
     */
    public int getSelectedSize() {
        return this.getSelectedIndex() + 2;
    }
}