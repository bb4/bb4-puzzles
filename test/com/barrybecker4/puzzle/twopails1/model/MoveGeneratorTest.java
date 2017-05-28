// Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.twopails1.model;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.barrybecker4.puzzle.twopails1.model.PourOperation.Action.*;
import static com.barrybecker4.puzzle.twopails1.model.PourOperation.Container.*;
import static org.junit.Assert.assertEquals;
/**
 * @author Barry Becker
 */
public class MoveGeneratorTest {

    @Test
    public void testGenerateMovesWhenBothEmpty() {

        Pails pails = new Pails(new PailParams(9, 4, 6));

        List<PourOperation> expectedOps =
                Arrays.asList(new PourOperation(FILL, FIRST), new PourOperation(FILL, SECOND));
        verifyGeneratedMoves(pails, expectedOps);
    }

    @Test
    public void testGenerateMovesWhenBothFull() {

        Pails pails = new Pails(new PailParams(9, 4, 6));
        pails = pails.doMove(new PourOperation(FILL, FIRST), false);
        pails = pails.doMove(new PourOperation(FILL, SECOND), false);

        List<PourOperation> expectedOps =
                Arrays.asList(new PourOperation(EMPTY, FIRST), new PourOperation(EMPTY, SECOND));
        verifyGeneratedMoves(pails, expectedOps);
    }

    @Test
    public void testGenerateMovesWhenAll0() {

        Pails pails = new Pails(new PailParams(0, 0, 0));

        List<PourOperation> expectedOps = Collections.emptyList();
        verifyGeneratedMoves(pails, expectedOps);
    }

    @Test
    public void testGenerateMovesWhenAll1() {

        Pails pails = new Pails(new PailParams(1, 1, 1));

        List<PourOperation> expectedOps =
                Arrays.asList(new PourOperation(FILL, FIRST), new PourOperation(FILL, SECOND));
        verifyGeneratedMoves(pails, expectedOps);
    }

    @Test
    public void testGenerateMovesFirstFull() {

        Pails pails = new Pails(new PailParams(7, 4, 3));
        pails = pails.doMove(new PourOperation(FILL, FIRST), false);

        List<PourOperation> expectedOps = Arrays.asList(
                new PourOperation(FILL, SECOND),
                new PourOperation(EMPTY, FIRST),
                new PourOperation(TRANSFER, FIRST));
        verifyGeneratedMoves(pails, expectedOps);
    }


    @Test
    public void testGenerateMovesSmallerFull() {

        Pails pails = new Pails(new PailParams(7, 4, 3));
        pails = pails.doMove(new PourOperation(FILL, SECOND), false);

        List<PourOperation> expectedOps = Arrays.asList(
                new PourOperation(FILL, FIRST),
                new PourOperation(EMPTY, SECOND),
                new PourOperation(TRANSFER, SECOND));
        verifyGeneratedMoves(pails, expectedOps);
    }

    @Test
    public void testGenerateMovesOnePartiallyFull() {

        Pails pails = new Pails(new PailParams(11, 4, 8));
        pails = pails.doMove(new PourOperation(FILL, SECOND), false);
        pails = pails.doMove(new PourOperation(TRANSFER, SECOND), false);
        pails = pails.doMove(new PourOperation(FILL, SECOND), false);
        //First now has 4 of 11, second has 4 (full)

        List<PourOperation> expectedOps = Arrays.asList(
                new PourOperation(FILL, FIRST),
                new PourOperation(EMPTY, FIRST),
                new PourOperation(EMPTY, SECOND),
                new PourOperation(TRANSFER, SECOND));
        verifyGeneratedMoves(pails, expectedOps);
    }

    @Test
    public void testGenerateMovesOnePartiallyFullButCannotTransferWithoutOverFlowing() {

        Pails pails = new Pails(new PailParams(7, 4, 6));
        pails = pails.doMove(new PourOperation(FILL, SECOND), false);
        pails = pails.doMove(new PourOperation(TRANSFER, SECOND), false);
        pails = pails.doMove(new PourOperation(FILL, SECOND), false);
        //First now has 4 of 7, second has 4 (full)

        List<PourOperation> expectedOps = Arrays.asList(
                new PourOperation(FILL, FIRST),
                new PourOperation(EMPTY, FIRST),
                new PourOperation(EMPTY, SECOND),
                new PourOperation(TRANSFER, SECOND));   // its allowed to overflow
        verifyGeneratedMoves(pails, expectedOps);
    }

    private void verifyGeneratedMoves(Pails initialState, List<PourOperation> expectedOps) {

        MoveGenerator generator = new MoveGenerator(initialState);
        List<PourOperation> possibleOps = generator.generateMoves();

        assertEquals("Unexpected list of candidate operations",
                expectedOps, possibleOps);
    }
}
