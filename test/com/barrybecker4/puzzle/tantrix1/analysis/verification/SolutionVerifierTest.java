// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix1.analysis.verification;

import com.barrybecker4.puzzle.tantrix1.solver.verfication.SolutionVerifier;
import org.junit.Test;

import static com.barrybecker4.puzzle.tantrix1.TantrixTstUtil.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Barry Becker
 */
public class SolutionVerifierTest {

    /** instance under test */
    private SolutionVerifier verifier;

    @Test
    public void test3TilesIsNotSolved() {
        verifier = new SolutionVerifier(place3UnsolvedTiles());
        assertFalse("Unexpectedly solved", verifier.isSolved());
    }

    @Test
    public void test3NonPathTilesIsNotSolved() {
        verifier = new SolutionVerifier(place3NonPathTiles());
        assertFalse("Unexpectedly solved", verifier.isSolved());
    }

    @Test
    public void test3TilesIsNotSolved2() {
        verifier = new SolutionVerifier(place3UnsolvedTiles2());
        assertFalse("Unexpectedly solved", verifier.isSolved());
    }

    @Test
    public void test3TilesIsSolved() {
        verifier = new SolutionVerifier(place3SolvedTiles());
        assertTrue("Unexpectedly not solved", verifier.isSolved());
    }

    @Test
    public void test4TilesIsNotSolved() {
        verifier = new SolutionVerifier(place4UnsolvedTiles());
        assertFalse("Unexpectedly solved", verifier.isSolved());
    }

    @Test
    public void test4TilesIsSolved() {
        verifier = new SolutionVerifier(place4SolvedTiles());
        assertTrue("Unexpectedly not solved", verifier.isSolved());
    }

    @Test
    public void test10TilesWithSpacesIsNotSolved() {
        verifier = new SolutionVerifier(place10LoopWithInnerSpace());
        assertFalse("Unexpectedly solved", verifier.isSolved());
    }
}