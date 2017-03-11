// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix1.solver.path.permuting;

import com.barrybecker4.common.math.MathUtil;
import com.barrybecker4.puzzle.tantrix1.solver.path.PathType;
import com.barrybecker4.puzzle.tantrix1.solver.path.TantrixPath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Mix tiles of the same type without changing the primary originalPath.
 *
 * @author Barry Becker
 */
public class SameTypeTileMixer {

    private static final int MAX_ITER = 20;
    private PathType type;
    private TantrixPath originalPath;

    /**
     * Constructor
     * @param type one of the 3 path types
     * @param path the path to create permutations of.
     */
    public SameTypeTileMixer(PathType type, TantrixPath path) {
        this.type = type;
        this.originalPath = path;
    }

    public List<TantrixPath> findPermutedPaths() {

        TilesOfTypeIndices indices = new TilesOfTypeIndices(type, originalPath);
        return findPermutedPaths(indices);
    }

    private List<TantrixPath> findPermutedPaths(TilesOfTypeIndices indices) {

        List<TantrixPath> permutedPaths = new LinkedList<>();
        PathTilePermuter permuter = new PathTilePermuter(originalPath);

        if (indices.size() == 2) {
            List<Integer> newOrder = new ArrayList<>(indices);
            Collections.reverse(newOrder);
            TantrixPath permutedPath = permuter.permute(newOrder, indices);
            permutedPaths.add(permutedPath);
        }
        else if (indices.size() > 2) {
            // add the original originalPath for now, to be sure we do not duplicated it, but return at end.
            permutedPaths.add(originalPath);

            int numIter = Math.min(originalPath.size() + 1, MAX_ITER);

            for (int i = 0; i < numIter; i++) {
                List<Integer> newOrder = new ArrayList<>(indices);
                Collections.shuffle(newOrder, MathUtil.RANDOM);
                TantrixPath permutedPath = permuter.permute(newOrder, indices);
                if (!permutedPaths.contains(permutedPath)) {
                    permutedPaths.add(permutedPath);
                }
            }
            permutedPaths.remove(0);
        }
        return permutedPaths;
    }
}
