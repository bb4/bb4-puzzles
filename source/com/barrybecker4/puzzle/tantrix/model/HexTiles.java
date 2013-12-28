// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.model;

import com.barrybecker4.common.math.MathUtil;

import java.util.Collections;
import java.util.Random;

import static com.barrybecker4.puzzle.tantrix.model.PathColor.*;

/**
 * The complete set of hexagonal tantrix tiles. There are 56 of them.
 *
 * @author Barry Becker
 */
public class HexTiles extends HexTileList {

    public HexTiles()  {
        super();
        byte i = 1;
        add(new HexTile(i++, YELLOW, new PathColors(RED, BLUE, RED, BLUE, YELLOW, YELLOW)));
        add(new HexTile(i++, YELLOW, new PathColors(BLUE, YELLOW, YELLOW, BLUE, RED, RED)));
        add(new HexTile(i++, YELLOW, new PathColors(BLUE, BLUE, RED, RED, YELLOW, YELLOW)));
        add(new HexTile(i++, RED, new PathColors(BLUE, YELLOW, RED, BLUE, RED, YELLOW)));
        add(new HexTile(i++, RED, new PathColors(RED, BLUE, BLUE, RED, YELLOW, YELLOW)));
        add(new HexTile(i++, BLUE, new PathColors(YELLOW, RED, BLUE, YELLOW, BLUE, RED)));
        add(new HexTile(i++, BLUE, new PathColors(RED, YELLOW, RED, YELLOW, BLUE, BLUE))); // 7
        add(new HexTile(i++, BLUE, new PathColors(YELLOW, RED, YELLOW, RED, BLUE, BLUE)));
        add(new HexTile(i++, YELLOW, new PathColors(RED, YELLOW, BLUE, RED, BLUE, YELLOW)));
        add(new HexTile(i++, RED, new PathColors(RED, YELLOW, YELLOW, BLUE, RED, BLUE))); // 10
        add(new HexTile(i++, RED, new PathColors(BLUE, RED, RED, YELLOW, BLUE, YELLOW)));
        add(new HexTile(i++, YELLOW, new PathColors(YELLOW, RED, RED, BLUE, YELLOW, BLUE)));
        add(new HexTile(i++, BLUE, new PathColors(YELLOW, BLUE, BLUE, YELLOW, RED, RED))); // 13
        add(new HexTile(i++, BLUE, new PathColors(BLUE, YELLOW, YELLOW, RED, RED, BLUE)));
        add(new HexTile(i++, RED, new PathColors(RED, GREEN, GREEN, RED, YELLOW, YELLOW))); // 15
        add(new HexTile(i++, RED, new PathColors(YELLOW, RED, RED, YELLOW, GREEN, GREEN)));
        add(new HexTile(i++, YELLOW, new PathColors(GREEN, YELLOW, YELLOW, RED, GREEN, RED))); // 17
        add(new HexTile(i++, RED, new PathColors(RED, YELLOW, YELLOW, GREEN, RED, GREEN )));
        add(new HexTile(i++, RED, new PathColors(GREEN, RED, RED, YELLOW, GREEN, YELLOW)));   // 19
        add(new HexTile(i++, YELLOW, new PathColors(YELLOW, RED, RED, GREEN, YELLOW, GREEN)));
        add(new HexTile(i++, YELLOW, new PathColors(GREEN, YELLOW, YELLOW, RED, RED, GREEN)));
        add(new HexTile(i++, YELLOW, new PathColors(GREEN, YELLOW, YELLOW, GREEN, RED, RED)));
        add(new HexTile(i++, YELLOW, new PathColors(RED, YELLOW, YELLOW, GREEN, GREEN, RED))); // 23
        add(new HexTile(i++, RED, new PathColors(BLUE, RED, RED, BLUE,GREEN, GREEN)));
        add(new HexTile(i++, RED, new PathColors(BLUE, RED, RED, GREEN, GREEN, BLUE)));
        add(new HexTile(i++, RED, new PathColors(GREEN, BLUE,BLUE, GREEN, RED, RED)));
        add(new HexTile(i++, RED, new PathColors(BLUE, RED, RED, GREEN, BLUE, GREEN)));
        add(new HexTile(i++, RED, new PathColors(BLUE, GREEN, GREEN,RED, RED, BLUE)));
        add(new HexTile(i++, RED, new PathColors(GREEN, RED, RED, BLUE, GREEN, BLUE)));  // 29
        add(new HexTile(i++, RED, new PathColors(RED, GREEN, GREEN, RED, BLUE, BLUE)));
        add(new HexTile(i++, YELLOW, new PathColors(RED, GREEN, GREEN,YELLOW, RED, YELLOW)));
        add(new HexTile(i++, GREEN, new PathColors(GREEN, YELLOW, RED, GREEN, RED,YELLOW)));
        add(new HexTile(i++, GREEN, new PathColors(YELLOW, GREEN, GREEN, RED, YELLOW, RED)));
        add(new HexTile(i++, GREEN, new PathColors( RED, YELLOW, GREEN, RED, GREEN, YELLOW)));
        add(new HexTile(i++, GREEN, new PathColors(YELLOW, RED, GREEN, YELLOW, GREEN, RED)));  // 35
        add(new HexTile(i++, GREEN, new PathColors( RED, GREEN, GREEN, BLUE, RED, BLUE)));
        add(new HexTile(i++, GREEN, new PathColors(GREEN, BLUE, BLUE, RED, GREEN, RED)));
        add(new HexTile(i++, GREEN, new PathColors( RED, BLUE, BLUE, GREEN, RED, GREEN)));
        add(new HexTile(i++, BLUE, new PathColors(GREEN, BLUE, RED, GREEN, RED, BLUE)));
        add(new HexTile(i++, BLUE, new PathColors(BLUE, GREEN, RED, BLUE, RED, GREEN)));   // 40
        add(new HexTile(i++, BLUE, new PathColors(BLUE, GREEN, GREEN, RED, BLUE, RED)));
        add(new HexTile(i++, BLUE, new PathColors(RED, GREEN, BLUE, RED, BLUE, GREEN)));
        add(new HexTile(i++, BLUE, new PathColors(GREEN, YELLOW, YELLOW, BLUE, BLUE, GREEN)));
        add(new HexTile(i++, YELLOW, new PathColors( YELLOW, GREEN, BLUE, YELLOW, BLUE, GREEN)));
        add(new HexTile(i++, GREEN, new PathColors(BLUE, YELLOW, YELLOW, GREEN, GREEN, BLUE)));
        add(new HexTile(i++, GREEN, new PathColors(BLUE, GREEN, GREEN, YELLOW, BLUE, YELLOW)));
        add(new HexTile(i++, GREEN, new PathColors( YELLOW, GREEN, GREEN, YELLOW, BLUE, BLUE)));
        add(new HexTile(i++, WHITE, new PathColors(BLUE, GREEN, GREEN, BLUE, YELLOW, YELLOW)));
        add(new HexTile(i++, WHITE, new PathColors(GREEN, YELLOW, YELLOW, GREEN, BLUE, BLUE)));
        add(new HexTile(i++, WHITE, new PathColors(BLUE, GREEN, YELLOW, BLUE, YELLOW, GREEN)));  // 50
        add(new HexTile(i++, WHITE, new PathColors(GREEN, YELLOW, BLUE, GREEN, BLUE, YELLOW)));
        add(new HexTile(i++, WHITE, new PathColors( YELLOW, GREEN, GREEN, BLUE, YELLOW, BLUE)));
        add(new HexTile(i++, WHITE, new PathColors(GREEN, YELLOW, YELLOW, BLUE, GREEN, BLUE)));
        add(new HexTile(i++, WHITE, new PathColors( BLUE, YELLOW, YELLOW, GREEN, BLUE, GREEN)));
        add(new HexTile(i++, WHITE, new PathColors( GREEN, BLUE, BLUE, YELLOW, GREEN, YELLOW)));
        add(new HexTile(i, WHITE, new PathColors( YELLOW, BLUE, BLUE, GREEN, YELLOW, GREEN)));
    }

    /**
     * Get a specific tile by its tantrix number (base index of 1, not 0).
     * @param tantrixNumber the number on the back of the tile.
     */
    public HexTile getTile(int tantrixNumber) {
        return super.get(tantrixNumber - 1);
    }

    public HexTileList createRandomList(int numTiles) {
       return createRandomList(numTiles, MathUtil.RANDOM);
    }

    /**
     * @param numTiles  the number of tiles to draw from the master list starting with 1.
     * @return a random collection of tantrix tiles.
     */
    public HexTileList createRandomList(int numTiles, Random rnd) {
        HexTileList tiles = createOrderedList(numTiles);
        Collections.shuffle(tiles, rnd);
        return tiles;
    }

    /**
     * @param numTiles the number of tiles to draw from the master list starting with tile 1.
     * @return a random collection of tantrix tiles.
     */
    public HexTileList createOrderedList(int numTiles) {
        HexTileList tiles = new HexTileList();
        for (int i=0; i<numTiles; i++) {
            tiles.add(this.get(i));
        }
        return tiles;
    }
}


