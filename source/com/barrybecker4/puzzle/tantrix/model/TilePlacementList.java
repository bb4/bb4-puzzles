// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

/**
 * A list of tile placements
 *
 * @author Barry Becker
 */
public class TilePlacementList extends LinkedList<TilePlacement> {

    public TilePlacementList()  {}

    public TilePlacementList(Tantrix tantrix) {
        for (TilePlacement p : tantrix.values()) {
           this.add(p);
        }
    }

    /** copy constructor */
    public TilePlacementList(TilePlacementList list) {
        this.addAll(list);
    }

    /** copy constructor */
    public TilePlacementList(Collection<TilePlacement> list) {
        this.addAll(list);
    }

    public TilePlacementList(TilePlacement... placements) {
        addAll(Arrays.asList(placements));
    }

    @Override
    public TilePlacement set(int index, TilePlacement placement) {
        int diff = index - this.size();
        while (diff-- >= 0) {
           add(placement);
        }
        return super.set(index, placement);
    }

    /*
    public TilePlacement getFirst() {
        return this.get(0);
    }

    public TilePlacement getLast() {
        return this.get(this.size()-1);
    }*/
}


