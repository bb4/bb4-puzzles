/** Copyright by Barry G. Becker, 20013. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.twopails.model;


import com.barrybecker4.puzzle.common.model.Move;

/**
 * There are 6 legal pour operations:
 * 1) fill first container to top
 * 2) fill second container to top
 * 3) empty first container
 * 4) empty second container
 * 5) pour everything in first container to second.
 * 6) pour everything in second container to first.
 * Immutable.
 *@author Barry Becker
 */
public final class PourOperation implements Move {

    public enum Action {FILL, EMPTY, TRANSFER}
    public enum Container {FIRST, SECOND}

    private Action action;
    private Container container;

    /**
     * create a pour operation representing a state transition .
     */
    PourOperation(Action action, Container container) {
        this.action = action;
        this.container = container;
    }

    Action getAction() {
        return action;
    }

    Container getContainer() {
        return container;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(action).append(" ").append(container);
        return s.toString();
    }
}

