// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.ui;

import com.barrybecker4.common.geometry.Location;

/**
 * A user entered value and it cell location.
 * @author Barry Becker
 */
public class UserValue {
    private Location location;
    private int value;
    private boolean isValid = false;
    private boolean isValidated = false;


    public UserValue(Location location, int value) {
        this.location = location;
        this.value = value;
        this.isValidated = false;
    }

    public int getValue() {
        return value;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
        isValidated = true;
    }

    public boolean isValidated() {
        return isValidated;
    }

}
