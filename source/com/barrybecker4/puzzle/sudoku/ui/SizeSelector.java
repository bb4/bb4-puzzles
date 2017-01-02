// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.ui;

import java.awt.Choice;

/**
 * A combo box that allows the user to select the size of the puzzle
 *
 * @author Barry Becker
 */
public final class SizeSelector extends Choice {

    private static final String[] SIZE_MENU_ITEMS = {
        "4 cells on a side",
        "9 cells on a side",
        "16 cells on a side",
        "25 cells (prepare to wait)"
    };

    /**
     * Constructor.
     */
    public SizeSelector() {
        for (final String item : SIZE_MENU_ITEMS) {
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