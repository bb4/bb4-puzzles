/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.sudoku.ui;

import com.barrybecker4.common.geometry.Location;

/**
 * Called when the user enters a value.
 * @author Barry Becker
 */
public interface RepaintListener {


    void valueEntered();

    void cellSelected(Location location);

    void requestValidation();
}
