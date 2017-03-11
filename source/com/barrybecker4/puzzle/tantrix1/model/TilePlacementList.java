// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix1.model;

import scala.collection.JavaConversions;
import scala.collection.Seq;

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

    public Seq<TilePlacement> asSeq() {
        return JavaConversions.asScalaBuffer(this).toSeq();
    }

    /** copy constructor */
    public TilePlacementList(Collection<TilePlacement> list) {
        this.addAll(list);
    }

    public TilePlacementList(TilePlacement... placements) {
        addAll(Arrays.asList(placements));
    }

    /** Increase the size of this list if index is out of bounds. */
    @Override
    public TilePlacement set(int index, TilePlacement placement) {

        int diff = index - this.size();
        while (diff-- >= 0) {
           this.add(null);
        }
        return super.set(index, placement);
    }
}


