// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix1.solver.path;

import com.barrybecker4.common.geometry.Location;
import com.barrybecker4.puzzle.tantrix1.model.HexTile;
import com.barrybecker4.puzzle.tantrix1.model.HexUtil;
import com.barrybecker4.puzzle.tantrix1.model.Tantrix;
import com.barrybecker4.puzzle.tantrix1.model.TilePlacement;
import com.barrybecker4.puzzle.tantrix1.solver.verification.ConsistencyChecker;
import com.barrybecker4.puzzle.tantrix1.solver.verification.InnerSpaceDetector;

import java.util.HashSet;
import java.util.Set;


/**
 * Evaluates the fitness of a tantrix path.
 * It gets the top score if it is a loop and all the path colors match.
 *
 * @author Barry Becker
 */
public class PathEvaluator {

    /** When reached, the puzzle is solved. */
    public static final double SOLVED_THRESH = 3.1;

    /** How close are the endpoints of the primary path from forming a loop. */
    private static final double LOOP_PROXIMITY_WEIGHT = 0.3;

    /** Weight to give if we actually have a primary path loop. */
    private static final double LOOP_WEIGHT = 0.3;

    /** Weight to give matching paths (includes secondary paths) */
    private static final double PATH_MATCH_WEIGHT = 1.0;

    /** We have a loop and all paths match */
    private static final double CONSISTENT_LOOP_BONUS = 0.6;

    /** consistent loop and no inner spaces. */
    private static final double PERFECT_LOOP_BONUS = 2.0;

    /** A measure of compactness. Avoids inner spaces. */
    private static final double COMPACTNESS = 0.2;


    /**
     * The main criteria for quality of the path is
     *  1) How close the ends of the path are to each other. Perfection achieved when we have a closed loop.
     *  2) Better if more matching secondary path colors
     *  3) Fewer inner spaces and a bbox with less area.
     * @return a measure of how good the path is.
     */
    public double evaluateFitness(TantrixPath path) {

        int numTiles = path.size();
        double distance = path.getEndPointDistance();
        boolean isLoop = distance == 0 && path.isLoop();

        ConsistencyChecker checker = new ConsistencyChecker(path.getTilePlacements(), path.getPrimaryPathColor());
        int numFits = checker.numFittingTiles();
        boolean allFit = (numFits == numTiles);
        boolean consistentLoop = isLoop && allFit;
        boolean perfectLoop = false;
        double compactness = determineCompactness(path);

        if (consistentLoop) {
            Tantrix tantrix = new Tantrix(path.getTilePlacements());
            InnerSpaceDetector innerDetector = new InnerSpaceDetector(tantrix);
            perfectLoop = !innerDetector.hasInnerSpaces();
        }
        assert numFits <= numTiles;

        double fitness = SOLVED_THRESH
                - LOOP_PROXIMITY_WEIGHT * (numTiles - distance) / (0.1 + numTiles)
                - (isLoop ? LOOP_WEIGHT : 0)
                - (double)numFits / numTiles * PATH_MATCH_WEIGHT
                - compactness * COMPACTNESS
                - (consistentLoop ? CONSISTENT_LOOP_BONUS : 0)
                - (perfectLoop ? PERFECT_LOOP_BONUS : 0);

        assert !Double.isNaN(fitness) :
                "Invalid fitness  isLoop=" + isLoop + " consistentLoop=" + consistentLoop
                + " numTiles=" + numTiles + " distance=" + distance;
        return Math.max(0, fitness);
    }

    /**
     * First add all the tiles to a hash keyed on location.
     * Then for every one of the six sides of each tile, add one if the
     * neighbor is in the hash. Return (num nbrs in hash - 2(numTiles-1))/numTiles
     * @param path the path to determine compactness of.
     * @return measure of path compactness between 0 and ~1
     */
    private double determineCompactness(TantrixPath path) {
        Set<Location> locationHash = new HashSet<>();
        int numTiles = path.size();
        for (TilePlacement p : path.getTilePlacements()) {
            locationHash.add(p.getLocation());
        }
        int ct = 0;
        for (TilePlacement p : path.getTilePlacements()) {
            for (int i = 0; i < HexTile.NUM_SIDES; i++)   {
                if (locationHash.contains(HexUtil.getNeighborLocation(p.getLocation(), i))) {
                    ct++;
                }
            }
        }
        return (ct - 2.0 * (numTiles-1)) / numTiles * 0.5;
    }
}
