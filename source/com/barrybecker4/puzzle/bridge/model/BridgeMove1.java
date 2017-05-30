// Copyright by Barry G. Becker, 20013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.bridge.model;


import com.barrybecker4.puzzle.common1.model.Move;

import java.util.List;

/**
 * Represents the act of one or two people crossing the bride.
 * At most two people can cross at once or the bridge may crash.
 * The light/torch is always transferred with the people that are crossing.
 * Immutable.
 * @author Barry Becker
 */
public final class BridgeMove1 implements Move, Comparable<BridgeMove1> {

    /** if true then crossing */
    private boolean direction;

    /** the speeds of the person or people that are crossing */
    private List<Integer> people;

    /** The time for the slowest person out of everyone crossing at the same time */
    private int cost;

    /**
     * create a move object representing a transition on the board.
     */
    BridgeMove1(List<Integer> people, boolean direction) {
        this.people = people;
        this.direction = direction;
        cost = determineCost();
    }

    public List<Integer> getPeople() {
        return people;
    }
    public boolean getDirection() {
        return direction;
    }

    private int determineCost() {
        if (people.size() == 1) {
            return people.get(0);
        }
        return Math.max(people.get(0), people.get(1));
    }

    public int getCost() {
        return cost;
    }


    /** @return the from and to positions */
    @Override
    public String toString() {
        return "people: " + people + (direction ? " -> " : " <- ");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BridgeMove1 that = (BridgeMove1) o;
        return direction == that.direction && people.equals(that.people);
    }

    @Override
    public int hashCode() {
        int result = (direction ? 1 : 0);
        result = 31 * result + people.hashCode();
        return result;
    }

    @Override
    public int compareTo(BridgeMove1 m) {
        return getCost() - m.getCost();
    }
}

