// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix1.model;

import java.util.ArrayList;

/**
 * The complete set of hexagonal tantrix tiles
 *
 * @author Barry Becker
 */
public class HexTileList extends ArrayList<HexTile> {

   public HexTileList()  {}

   public HexTileList(TilePlacementList tiles) {
       for (TilePlacement placement : tiles) {
           this.add(placement.getTile());
       }
   }

}


