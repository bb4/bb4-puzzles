// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix1.solver.path.permuting;

import com.barrybecker4.common.geometry.Location;
import com.barrybecker4.optimization.parameter.PermutedParameterArray;
import com.barrybecker4.puzzle.tantrix1.model.PathColor;
import com.barrybecker4.puzzle.tantrix1.model.TilePlacement;
import com.barrybecker4.puzzle.tantrix1.model.TilePlacementList;
import com.barrybecker4.puzzle.tantrix1.solver.path.TantrixPath;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Given a TantrixPath and a pivot tile index, find the permuted paths.
 * Since there are 8 total ways to permute and the path already represents one of them,
 * the permuter will never return more than 7 valid permuted paths.
 *
 * @author Barry Becker
 */
public class PathPivotPermuter extends PermutedParameterArray {

    private TantrixPath path_;

    /** The pivot path remains unchanged while the ends change. */
    private TantrixPath pivotPath;


    /**
     * The list of tiles that are passed in must be a continuous primary path,
     *  but it is not required that it be a loop, or that any of the secondary colors match.
     * @param path ordered path tiles.
     */
    public PathPivotPermuter(TantrixPath path) {
        path_ = path;
    }

    /**
     * try the seven cases and take any that are valid for the n^2 positions of the pivot path.
     * @return no more than 7 permuted path cases.
     */
    public List<TantrixPath> findAllPermutedPaths() {

        List<TantrixPath> pathPermutations = new ArrayList<>();

        int lowerIndexStart = 1;
        int upperIndexStop = path_.size() - 2;

        for (int i=lowerIndexStart; i<upperIndexStop; i++) {
            for (int j=upperIndexStop; j>=i; j--) {
                TantrixPath subPath1 = path_.subPath(i - 1, 0);
                pivotPath = path_.subPath(i, j);
                TantrixPath subPath2 =  path_.subPath(j + 1, path_.size() - 1);
                pathPermutations.addAll( createPermutedPathList(subPath1, subPath2));
            }
        }

        return pathPermutations;
    }

    @Override
    public void setPermutation(List<Integer> indices) {

        TilePlacementList tilePlacements = new TilePlacementList();

        for (int i : indices) {
            tilePlacements.add(path_.getTilePlacements().get(i));
        }

        path_ = new TantrixPath(tilePlacements, path_.getPrimaryPathColor());
    }

    /**
     * Try the seven cases and take any that are valid.
     * @return no more than 7 permuted path cases.
     */
    public List<TantrixPath> findPermutedPaths(int pivotIndex1, int pivotIndex2) {

        int lowerIndex = Math.min(pivotIndex1, pivotIndex2);
        int upperIndex = Math.max(pivotIndex1, pivotIndex2);

        TantrixPath subPath1 = path_.subPath(lowerIndex - 1, 0);
        pivotPath = path_.subPath(lowerIndex, upperIndex);
        TantrixPath subPath2 =  path_.subPath(upperIndex + 1, path_.size() - 1);

        return createPermutedPathList(subPath1, subPath2);
    }

    /**
     * @param subPath1 path coming out of pivot tile
     * @param subPath2 the other path coming out of pivot tile.
     * @return list of permuted paths.
     */
    private List<TantrixPath> createPermutedPathList(TantrixPath subPath1, TantrixPath subPath2) {
        PathColor primaryColor = path_.getPrimaryPathColor();
        SubPathMutator swapper = new SubPathSwapper(primaryColor);
        SubPathMutator reverser = new SubPathReverser(primaryColor);
        TilePlacement firstPivot = pivotPath.getFirst();
        TilePlacement lastPivot = pivotPath.getLast();

        TantrixPath subPath1Reversed = reverser.mutate(firstPivot, subPath1);
        TantrixPath subPath2Reversed = reverser.mutate(lastPivot, subPath2);
        TantrixPath subPath1Swapped = swapper.mutate(firstPivot, subPath1);
        TantrixPath subPath2Swapped = swapper.mutate(lastPivot, subPath2);
        TantrixPath subPath1RevSwapped = swapper.mutate(firstPivot, subPath1Reversed);
        TantrixPath subPath2RevSwapped = swapper.mutate(lastPivot, subPath2Reversed);

        List<TantrixPath> pathPermutations = new ArrayList<TantrixPath>();

        addIfNotNull(createPermutedPath(subPath1, subPath2Reversed), pathPermutations);
        addIfNotNull(createPermutedPath(subPath1Reversed, subPath2), pathPermutations);
        addIfNotNull(createPermutedPath(subPath1Reversed, subPath2Reversed), pathPermutations);

        addIfNotNull(createPermutedPath(subPath2Swapped, subPath1Swapped), pathPermutations);
        addIfNotNull(createPermutedPath(subPath2Swapped, subPath1RevSwapped), pathPermutations);
        addIfNotNull(createPermutedPath(subPath2RevSwapped, subPath1Swapped), pathPermutations);
        addIfNotNull(createPermutedPath(subPath2RevSwapped, subPath1RevSwapped), pathPermutations);
        return pathPermutations;
    }

    private void addIfNotNull(TantrixPath path, List<TantrixPath> pathPermutations) {
        if (path != null) {
            pathPermutations.add(path);
        }
    }

    /**
     * Combine supPath1 and subPath2 to make a new path. SubPath1 needs to be reversed when adding.
     * @param subPath1 first path
     * @param subPath2 second path
     * @return null if the resulting permuted path is not valid (i.e. has overlaps)
     */
    private TantrixPath createPermutedPath(TantrixPath subPath1, TantrixPath subPath2) {

        // add tiles from the first path in reverse order
        TilePlacementList tiles = new TilePlacementList();
        for (TilePlacement p : subPath1.getTilePlacements()) {
            tiles.addFirst(p);
        }

        tiles.addAll(pivotPath.getTilePlacements());
        tiles.addAll(subPath2.getTilePlacements());
        TantrixPath path = null;
        if (isValid(tiles)) {
            assert (TantrixPath.hasOrderedPrimaryPath(tiles, path_.getPrimaryPathColor())) :
                    "out of order path tiles \nsubpath1" + subPath1 + "\npivot="+ pivotPath
                            + "\nsubpath2=" + subPath2 + "\norigPath="+ path_;

            path = new TantrixPath(tiles, path_.getPrimaryPathColor());
        }
        return path;
    }

    /**
     * @param tiles tiles to check
     * @return true if no overlapping tiles.
     */
    private boolean isValid(TilePlacementList tiles) {
        Set<Location> tileLocations = new HashSet<Location>();
        for (TilePlacement placement : tiles) {
            if (tileLocations.contains(placement.getLocation())) {
                return false;
            }
            tileLocations.add(placement.getLocation());
        }
        return true;
    }
}
