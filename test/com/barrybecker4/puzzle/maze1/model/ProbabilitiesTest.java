// Copyright by Barry G. Becker, 2014. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.maze1.model;

import com.barrybecker4.common.math.MathUtil;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test that sort distributions come out within expected norms for different probability settings.
 *
 * @author Barry Becker
 */
public class ProbabilitiesTest {

    /** instance under test */
    Probabilities probs;

    @Before
    public void setUp() {
       MathUtil.RANDOM.setSeed(0);
    }

    @Test
    public void testEqualProbabilities() throws Exception {
        probs = new Probabilities(1.0, 1.0, 1.0);

        DistributionMap expDist = new DistributionMap(
                Arrays.asList(31, 33, 36),
                Arrays.asList(34, 33, 33),
                Arrays.asList(35, 34, 31)
        );

        assertEquals("Unexpected distribution", expDist, getDistribution(100));
    }

    @Test
    public void testSkewForwardProbabilities() throws Exception {
        probs = new Probabilities(3.0, 1.0, 1.0);

        DistributionMap expDist = new DistributionMap(
                Arrays.asList(127, 55, 18),
                Arrays.asList(34, 72, 94),
                Arrays.asList(39, 73, 88)
        );

        assertEquals("Unexpected distribution", expDist, getDistribution(200));
    }

    @Test
    public void testSkewLeftProbabilities() throws Exception {
        probs = new Probabilities(0.9, 1.9, 0.1);

        DistributionMap expDist = new DistributionMap(
                Arrays.asList(71, 109, 20),
                Arrays.asList(124, 73, 3),
                Arrays.asList(5, 18, 177)
        );

        assertEquals("Unexpected distribution", expDist, getDistribution(200));
    }


    @Test
    public void testSkewRightProbabilities() throws Exception {
        probs = new Probabilities(0.1, 0.5, 8.0);

        DistributionMap expDist = new DistributionMap(
                Arrays.asList(4, 35, 161),
                Arrays.asList(14, 148, 38),
                Arrays.asList(182, 17, 1)
        );

        assertEquals("Unexpected distribution", expDist, getDistribution(200));
    }

    /**
     * @param num number of shuffled trials to run
     * @return map from direction to list of counts for the three positions that direction fell in.
     */
    private DistributionMap getDistribution(int num) {
        DistributionMap dist = new DistributionMap();

        for (int i=0; i<num; i++) {
            List<Direction> dirs = probs.getShuffledDirections();
            dist.increment(dirs);
        }
        return dist;
    }
}
