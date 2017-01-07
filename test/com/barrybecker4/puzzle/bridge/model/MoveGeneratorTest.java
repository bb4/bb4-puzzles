// Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.bridge.model;

import org.junit.Test;
import scala.collection.JavaConversions;
import scala.collection.Seq;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 * @author Barry Becker
 */
public class MoveGeneratorTest {


    @Test
    public void testGenerateMovesForStandardProblemInitialState() {

        Bridge1 initialState = new Bridge1(Arrays.asList(1, 2, 5, 8), Collections.<Integer>emptyList(), false);

        Seq<BridgeMove1> expectedMoves = JavaConversions.asScalaBuffer(Arrays.asList(
                new BridgeMove1(Arrays.asList(1), true),
                new BridgeMove1(Arrays.asList(1, 2), true),
                new BridgeMove1(Arrays.asList(1, 5), true),
                new BridgeMove1(Arrays.asList(1, 8), true),
                new BridgeMove1(Arrays.asList(2), true),
                new BridgeMove1(Arrays.asList(2, 5), true),
                new BridgeMove1(Arrays.asList(2, 8), true),
                new BridgeMove1(Arrays.asList(5), true),
                new BridgeMove1(Arrays.asList(5, 8), true),
                new BridgeMove1(Arrays.asList(8), true)
            )).toSeq();

        verifyGeneratedMoves(initialState, expectedMoves);
    }

    private void verifyGeneratedMoves(Bridge1 initialState, Seq<BridgeMove1> expectedMoves) {

        MoveGenerator1 generator = new MoveGenerator1(initialState);
        Seq<BridgeMove1> possibleMoves = generator.generateMoves();

        assertEquals("Unexpected list of candidate moves",
                expectedMoves, possibleMoves);
    }
}
