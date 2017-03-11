// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix1.solver.path;

import com.barrybecker4.common.math.MathUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Select a path from a set of paths.
 *
 * @author Barry Becker
 */
class PathSelector {

    private PathEvaluator evaluator_;

    /**
     * Constructor
     */
    PathSelector() {
        evaluator_ = new PathEvaluator();
    }

    /**
     * Constructor to use when testing to pass in mock evaluator.
     */
    PathSelector(PathEvaluator evaluator) {
        evaluator_ = evaluator;
    }


    /**
     * Skew toward selecting the best, but don't always select the best because then we
     * might always return the same random neighbor.
     * @param paths list of paths to evaluate.
     * @return a random path with a likely good score. In other words, the path which is close to a valid solution.
     */
    TantrixPath selectPath(List<TantrixPath> paths) {

        double totalScore = 0;
        List<Double> scores = new ArrayList<>(paths.size() + 1);

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
