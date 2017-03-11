// Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix1.solver.path;

import com.barrybecker4.common.math.MathUtil;
import com.barrybecker4.puzzle.tantrix1.solver.path.permuting.PathTstUtil;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Barry Becker
 */
public class PathSelectorTest {

    /** instance under test */
    private PathSelector selector;

    @Before
    public void setUp() {
       MathUtil.RANDOM.setSeed(0);
    }

    @Test
    public void testSelectFromUniformDistr() {
        selector = new PathSelector(new UniformMockPathEvaluator());
        verifySelectedPathIndices(5, 1, 4, 3, 4, 2);
    }


    /** Uses the real evaluator rather than mock */
    @Test
    public void testIntegrationSelect() {
        selector = new PathSelector();
        // two is always returned because that is a perfect path
        verifySelectedPathIndices(2, 2, 2, 2, 2);
    }

    /** Uses the real evaluator rather than mock */
    @Test
    public void testSelectFromSingleLargeDistr() {
        selector = new PathSelector(new SingleLargeMockPathEvaluator());
        // the first one is 3, but the rest are random.
        verifySelectedPathIndices(3, 5, 1, 4);
    }


    /** Uses the real evaluator rather than mock */
    @Test
    public void testSelectFromIncreasingDistr() {
        selector = new PathSelector(new MonotonicallyIncreasingPathEvaluator());
        // should be skewed toward higher indices, but becomes 0 once the
        // perfect path threshold is reached
        verifySelectedPathIndices(6, 2, 4, 4, 4, 2, 2, 6, 6, 0, 0, 0);
    }

    /** verify the exact sequence of selected paths */
    private void verifySelectedPathIndices(int... indices) {

        List<TantrixPath> paths = PathTstUtil.createPathList();

        for (int i=0; i < indices.length; i++) {
           assertEquals("Unexpected path index " + i + ".",
                   indices[i], paths.indexOf(selector.selectPath(paths)));
        }
    }

    /** uniform distribution of selected paths */
    class UniformMockPathEvaluator extends PathEvaluator {
        public double evaluateFitness(TantrixPath path) {
             return 1.0;
        }
    }

    /** returns small number except for third call, when it returns large */
    class SingleLargeMockPathEvaluator extends PathEvaluator {
        private int count;
        public double evaluateFitness(TantrixPath path) {
            return (count++ == 3) ? 1000.0 : 0.0001;
        }
    }

    /** returns small number except for third call, when it returns large */
    class MonotonicallyIncreasingPathEvaluator extends PathEvaluator {
        private int count;
        public double evaluateFitness(TantrixPath path) {
            return count++ / 20.0;
        }
    }
}