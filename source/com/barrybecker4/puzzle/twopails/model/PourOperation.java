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

    /**
     * @return the reverse of this operation
     */
    PourOperation reverse() {
        Action action = getAction();
        Container container = getContainer();
        switch (getAction()) {
            case FILL : action = Action.EMPTY; break;
            case EMPTY: action = Action.FILL; break;
            case TRANSFER: container = getContainer() == Container.FIRST ? Container.SECOND : Container.FIRST;
        }
        return new PourOperation(action, container);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(action).append(" ").append(container);
        return s.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PourOperation that = (PourOperation) o;
        return action == that.action && container == that.container;
    }

    @Override
    public int hashCode() {
        int result = action.hashCode();
        result = 31 * result + container.hashCode();
        return result;
    }
}

