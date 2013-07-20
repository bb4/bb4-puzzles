// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.model;

/**
 */
public enum PathColor {
    RED,
    GREEN,
    BLUE,
    YELLOW,
    WHITE;

    public String toString() {
        return this.name().substring(0, 1);
    }
}
