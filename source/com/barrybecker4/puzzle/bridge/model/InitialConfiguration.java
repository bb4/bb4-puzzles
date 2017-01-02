// Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.bridge.model;

import java.util.Arrays;

/**
 * A combo box that allows the user to select the sort of people that need to cross
 *
 * @author Barry Becker
 */
public enum InitialConfiguration {

    STANDARD_PROBLEM("Standard Problem", 1, 2, 5, 8),       // shortest = 15
    ALTERNATIVE_PROBLEM("Alternative Problem", 5, 10, 20, 25), // shortest = 60
    DIFFICULT_PROBLEM("Hard", 1, 2, 5, 7, 8, 12, 15), // shortest = 47
    SUPER_HARD("Harder", 7, 11, 2, 3, 5, 4, 1, 3, 12, 3, 15, 19, 8), // shortest = 79
    TRIVIAL_PROBLEM("Trivial Problem", 1, 2, 5);           // shortest = 8


    private String label;
    private Integer[] peopleSpeeds;

    /**
     * Constructor.
     */
    InitialConfiguration(String label, Integer... peopleSpeeds) {
        this.label = label;
        this.peopleSpeeds = peopleSpeeds;
    }

    public String getLabel() {
        return label +  ": " + Arrays.toString(peopleSpeeds);
    }

    public Integer[] getPeopleSpeeds() {
        return peopleSpeeds;
    }

}