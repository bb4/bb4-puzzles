// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path;

import com.barrybecker4.common.geometry.ByteLocation;
import com.barrybecker4.puzzle.tantrix.model.PathColor;
import com.barrybecker4.puzzle.tantrix.model.Rotation;
import com.barrybecker4.puzzle.tantrix.model.Tantrix;
import com.barrybecker4.puzzle.tantrix.model.TilePlacement;
import com.barrybecker4.puzzle.tantrix.model.TilePlacementList;
import junit.framework.TestCase;

import static com.barrybecker4.puzzle.tantrix.TantrixTstUtil.TILES;
import static com.barrybecker4.puzzle.tantrix.TantrixTstUtil.loc;

/**
 * @author Barry Becker
 */
public class PathifierTest extends TestCase {

    /** instance under test */
    private Pathifier pathifier;


    @Override
    public void setUp() {
        pathifier = new Pathifier(TILES.getTile(1).getPrimaryColor());
    }

    public void test2TilePathConstruction() {

        TilePlacement firstTilePlacement =
                new TilePlacement(TILES.getTile(2), loc(2, 1), Rotation.ANGLE_0);
        TilePlacement secondTilePlacement =
                new TilePlacement(TILES.getTile(3), loc(1, 2), Rotation.ANGLE_0);

        TilePlacementList tileList = new TilePlacementList();
        tileList.add(firstTilePlacement);
        tileList.add(secondTilePlacement);

        assertEquals("Unexpected tiles", tileList, pathifier.reorder(new Tantrix(tileList)));
    }

    public void test3TilePathConstruction() {

        TilePlacement first =
                new TilePlacement(TILES.getTile(1), loc(1, 1), Rotation.ANGLE_0);
        TilePlacement second =
                new TilePlacement(TILES.getTile(2), loc(2, 1), Rotation.ANGLE_0);
        TilePlacement third =
                new TilePlacement(TILES.getTile(3), loc(1, 2), Rotation.ANGLE_0);

        TilePlacementList tileList = new TilePlacementList(first, second, third);

        assertEquals("Unexpected tiles", tileList, pathifier.reorder(new Tantrix(tileList)));
    }

    /** We should get an error if there is no path that can be found from rearranging the tiles without rotation. */
    public void testOutOfOrder2TilePathConstruction() {

        TilePlacement first =
                new TilePlacement(TILES.getTile(2), loc(2, 1), Rotation.ANGLE_60);
        TilePlacement second =
                new TilePlacement(TILES.getTile(3), loc(1, 2), Rotation.ANGLE_120);

        TilePlacementList tileList = new TilePlacementList(first, second);

        try {
            pathifier.reorder(new Tantrix(tileList));
            fail("did not expect to get here");
        } catch (IllegalStateException e) {
            // success
        }
    }

    public void testOutOfOrder5TilePathConstruction() {


        pathifier = new Pathifier(PathColor.RED);

        TilePlacement first =
                new TilePlacement(TILES.getTile(4), new ByteLocation(21, 22), Rotation.ANGLE_0);
        TilePlacement second =
                new TilePlacement(TILES.getTile(1), new ByteLocation(22, 21), Rotation.ANGLE_300);
        TilePlacement third =
                new TilePlacement(TILES.getTile(2), new ByteLocation(23, 22), Rotation.ANGLE_180);
        TilePlacement fourth =
                new TilePlacement(TILES.getTile(3), new ByteLocation(20, 21), Rotation.ANGLE_120);
        TilePlacement fifth =
                new TilePlacement(TILES.getTile(5), new ByteLocation(21, 21), Rotation.ANGLE_240);

        TilePlacementList tileList = new TilePlacementList(first, second, third, fourth, fifth);
        //TilePlacementList expReorderedList = new TilePlacementList(third, second, first, fourth, fifth);
        TilePlacementList expReorderedList = new TilePlacementList(fifth, fourth, first, second, third);

        assertEquals("Unexpected tiles", expReorderedList, pathifier.reorder(new Tantrix(tileList)));
    }

}