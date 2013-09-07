/** Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.bridge.model;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Barry Becker
 */
public class MoveGeneratorTest {


    @Test
    public void testGenerateMovesForStandardProblemInitialState() {

        Bridge initialState = new Bridge(Arrays.asList(1, 2, 5, 8), Collections.<Integer>emptyList(), false);

        List<BridgeMove> expectedMoves = Arrays.asList(
                new BridgeMove(Arrays.asList(1), true),
                new BridgeMove(Arrays.asList(1, 2), true),
                new BridgeMove(Arrays.asList(1, 5), true),
                new BridgeMove(Arrays.asList(1, 8), true),
                new BridgeMove(Arrays.asList(2), true),
                new BridgeMove(Arrays.asList(2, 5), true),
                new BridgeMove(Arrays.asList(2, 8), true),
                new BridgeMove(Arrays.asList(5), true),
                new BridgeMove(Arrays.asList(5, 8), true),
                new BridgeMove(Arrays.asList(8), true)
            );

        verifyGeneratedMoves(initialState, expectedMoves);
    }

    private void verifyGeneratedMoves(Bridge initialState, List<BridgeMove> expectedMoves) {

        MoveGenerator generator = new MoveGenerator(initialState);
        List<BridgeMove> possibleMoves = generator.generateMoves();

        assertEquals("Unexpected list of candidate moves",
                expectedMoves, possibleMoves);
    }
}
