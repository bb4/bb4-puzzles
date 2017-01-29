// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku1.ui;

/**
 * A user entered value and it cell location.
 * @author Barry Becker
 */
public class UserValue {
    private int value;
    private boolean isValid = false;
    private boolean isValidated = false;


    UserValue(int value) {
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

    boolean isValidated() {
        return isValidated;
    }
}
