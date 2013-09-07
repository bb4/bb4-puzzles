/** Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.bridge.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Immutable representation a bridge and the state at which people have crossed or not.
 * @author Barry Becker
 */
public class Bridge {

    /** Represents the speeds of the people that have not crossed yet*/
    private List<Integer> peopleUncrossed;

    /** Represents the the people (really their speeds) that have crossed */
    private List<Integer> peopleCrossed;

    private boolean lightCrossed;

    /**
     * Constructor that creates an initial bridge state
     * @param people array of people (represented by their speeds) that need to cross the bridge.
     */
    public Bridge(Integer[] people) {
        this(Arrays.asList(people), Collections.<Integer>emptyList(), false);
    }

    /**
     * Constructor with everything specified
     */
    public Bridge(List<Integer> uncrossed, List<Integer> crossed, boolean lightCrossed) {
        peopleUncrossed = Collections.unmodifiableList(uncrossed);
        peopleCrossed = Collections.unmodifiableList(crossed);
        this.lightCrossed = lightCrossed;
    }

    /**
     * Constructor
     * create a new bridge state by applying th specified move
     * Applying the same move a second time will undo it because it just swaps tiles.
     */
    public Bridge applyMove(BridgeMove move) {
        List<Integer> uncrossed = new ArrayList<>();
        List<Integer> crossed = new ArrayList<>();
        uncrossed.addAll(getUncrossedPeople());
        crossed.addAll(getCrossedPeople());

        if (move.getDirection()) {
            crossed.addAll(move.getPeople());
            uncrossed.removeAll(move.getPeople());
        }
        else {
            crossed.removeAll(move.getPeople());
            uncrossed.addAll(move.getPeople());
        }
        return new Bridge(uncrossed, crossed, move.getDirection());
    }

    public List<Integer> getUncrossedPeople() {
        return peopleUncrossed;
    }

    public List<Integer> getCrossedPeople() {
        return peopleCrossed;
    }

    public boolean isLightCrossed() {
        return lightCrossed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bridge bridge = (Bridge) o;

        return lightCrossed == bridge.lightCrossed
                && peopleCrossed.equals(bridge.peopleCrossed)
                && peopleUncrossed.equals(bridge.peopleUncrossed);
    }

    @Override
    public int hashCode() {
        int result = peopleUncrossed.hashCode();
        result = 31 * result + peopleCrossed.hashCode();
        result = 31 * result + (lightCrossed ? 1 : 0);
        return result;
    }

    /**
     * @return true if all the tiles, when read across and down, are in increasing order.
     */
    public boolean isSolved() {
        return peopleUncrossed.isEmpty();
    }

    /** @return the sum of the speeds of the people to cross is a rough estimate of the distance to the goal */
    public int distanceFromGoal() {
        int sum = 0;
        for (int person : peopleUncrossed) {
            sum += person;
        }
        return sum;
    }
}
