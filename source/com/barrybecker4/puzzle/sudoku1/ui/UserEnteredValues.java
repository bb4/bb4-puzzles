// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku1.ui;

import com.barrybecker4.common.geometry.ByteLocation;
import com.barrybecker4.common.geometry.Location;

import java.util.HashMap;

/**
 * A map of user entered values.
 * @author Barry Becker
 */
public class UserEnteredValues extends HashMap<Location, UserValue> {


    public UserEnteredValues() {
    }

    public UserValue get(int row, int col)  {
        return get(new ByteLocation(row, col));
    }
}
