// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path;

import com.barrybecker4.common.math.MathUtil;
import com.barrybecker4.optimization.parameter.PermutedParameterArray;
import com.barrybecker4.puzzle.tantrix.model.TilePlacementList;
import com.barrybecker4.puzzle.tantrix.solver.path.permuting.PathPivotPermuter;
import com.barrybecker4.puzzle.tantrix.solver.path.permuting.SameTypeTileMixer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * When finding a random neighbor, we select a tile at random and then consider all the
 * 7 other permutations of attaching the current path segments on either side. If any of those give a path
 * with a higher score that is what we use for the permuted path.
 * Includes a cache to avoid trying the same random path multiple times.
 *
 * @author Barry Becker
 */
public class PathPermutationGenerator  {

    private TantrixPath path;
    private PathEvaluator evaluator_ = new PathEvaluator();
    private static Set<TantrixPath> cache = new HashSet<TantrixPath>();

    /**
     * Constructor
     * @param path to permute
     */
    public PathPermutationGenerator(TantrixPath path) {
        this.path = path;
    }

    /**
     * We want to find a potential solution close to the one that we have,
     * with minimal disturbance of the pieces that are already fit, but yet improved from what we had.
     * The main criteria for quality of the path is
     *  1) How close the ends of the path are to each other. Perfection achieved when we have a closed loop.
     *  2) Better if more matching secondary path colors
     *  3) Fewer inner spaces and a bbox with less area.
     *
     * @param radius proportional to the amount of variation. This might be a little difficult for tantrix.
     *   If the radius is small, or there is a closed loop, consider swapping pieces who's
     *   primary path have the same shape. If the radius is large, we could perhaps do random permutation from
     *   more than one spot.
     * @return the random nbr (potential solution).
     */
    public PermutedParameterArray getRandomNeighbor(double radius) {

        List<TantrixPath> pathPermutations = findPermutedPaths(radius);

        assert (!pathPermutations.isEmpty()) :
                "Could not find any permutations of " + this;
        //System.out.println("selecting from among " + pathPermutations.size() +" paths");
        return selectPath(pathPermutations);
    }

    /**
     * try the seven cases and take the one that works best
     * @param radius the larger the radius the wider the variance of the random paths returned.
     * @return 7 permuted path cases.
     */
    private List<TantrixPath> findPermutedPaths(double radius) {

        List<TantrixPath> permutedPaths = new ArrayList<TantrixPath>();
        PathPivotPermuter permuter = new PathPivotPermuter(path);
        TilePlacementList tiles = path.getTilePlacements();
        int numTiles = path.size();

        if (radius >= 0.4) {
            for (int i = 1; i < numTiles - 1; i++) {
                addAllPermutedPaths(permuter.findPermutedPaths(i, i), permutedPaths);
            }
        }
        else if (radius >= 0.1) {
            // to avoid trying too many paths, increment by something more than one if many tiles.
            int inc = 1 + path.size()/4;
            // n^2 * 7 permuted paths will be added.
            for (int pivot1 = 1; pivot1 < numTiles-1; pivot1+=rand(inc)) {
                for (int pivot2 = pivot1; pivot2 < numTiles-1; pivot2+=rand(inc)) {
                    addAllPermutedPaths(permuter.findPermutedPaths(pivot1, pivot2), permutedPaths);
                }
            }
        }
        else if (permutedPaths.isEmpty()) {
            List<PathType> types = Arrays.asList(PathType.values());
            Collections.shuffle(types, MathUtil.RANDOM);
            Iterator<PathType> typeIter = types.iterator();

            do {
                SameTypeTileMixer mixer = new SameTypeTileMixer(typeIter.next(), path);
                addAllPermutedPaths(mixer.findPermutedPaths(), permutedPaths);
            } while (typeIter.hasNext());
        }

        // as a last resort use this without checking for it in the cache.
        if (permutedPaths.isEmpty()) {

            List<TantrixPath> paths;
            do {
                int pivotIndex1 = 1 + MathUtil.RANDOM.nextInt(tiles.size()-2);
                int pivotIndex2 = 1 + MathUtil.RANDOM.nextInt(tiles.size()-2);
                paths = permuter.findPermutedPaths(pivotIndex1, pivotIndex2);
                System.out.println("paths unexpectedly empty! when p1="+pivotIndex1 + " p2="+ pivotIndex2);
            } while (paths.isEmpty());
            return paths;
        }

        return permutedPaths;
    }

    private int rand(int inc) {
        if (inc <= 1) return 1;
        else return 1 + MathUtil.RANDOM.nextInt(inc);
    }

    /**
     * Check first that it is not in our global cache of paths already considered.
     */
    private void addAllPermutedPaths(List<TantrixPath> pathsToAdd, List<TantrixPath> permutedPaths) {

        for (TantrixPath p : pathsToAdd) {
            addPermutedPath(p, permutedPaths);
        }
    }

    /**
     * Check first that it is not in our global cache of paths already considered.
     */
    private void addPermutedPath(TantrixPath pathToAdd, List<TantrixPath> permutedPaths) {

        if (!cache.contains(pathToAdd)) {
            permutedPaths.add(pathToAdd);
            cache.add(pathToAdd);
            //System.out.println("csize=" + cache.size());
        }
    }

    /**
     * @param paths list of paths to evaluate.
     * @return the path with the best score. In other words the path which is closest to a valid solution.
     */
    private TantrixPath selectBestPath(List<TantrixPath> paths) {

        double bestScore = -1;
        TantrixPath bestPath = null;

        for (TantrixPath path : paths) {
            double score = evaluator_.evaluateFitness(path);
            if (score > bestScore) {
                bestPath = path;
                bestScore = score;
            }
        }
        return bestPath;
    }

    /**
     * Skew toward selecting the best, but don't always select the best because then we
     * might always return the same random neighbor.
     * @param paths list of paths to evaluate.
     * @return the path with the best score. In other words the path which is closest to a valid solution.
     */
    private TantrixPath selectPath(List<TantrixPath> paths) {

        double totalScore = 0;
        List<Double> scores = new ArrayList<Double>(paths.size() + 1);

        for (TantrixPath path : paths) {
            double score = evaluator_.evaluateFitness(path);
            if (score >= PathEvaluator.SOLVED_THRESH)  {
               return path;
            }
            totalScore += score;
            scores.add(score);
        }
        scores.add(10000.0);

        double r = MathUtil.RANDOM.nextDouble() * totalScore;

        double total = 0;
        int ct = 0;
        do {
           total += scores.get(ct++);
        } while (r > total);

        return paths.get(ct-1);
    }
}
