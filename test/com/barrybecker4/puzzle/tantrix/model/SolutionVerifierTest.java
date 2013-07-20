// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.model;

import com.barrybecker4.puzzle.tantrix.model.verfication.SolutionVerifier;
import junit.framework.TestCase;

import static com.barrybecker4.puzzle.tantrix.TantrixTstUtil.*;

/**
 * @author Barry Becker
 */
public class SolutionVerifierTest extends TestCase {

    /** instance under test */
    private SolutionVerifier verifier;


    public void test3TilesIsNotSolved() {
        verifier = new SolutionVerifier(place3UnsolvedTiles());
        assertFalse("Unexpectedly solved", verifier.isSolved());
    }

    public void test3NonPathTilesIsNotSolved() {
        verifier = new SolutionVerifier(place3NonPathTiles());
        assertFalse("Unexpectedly solved", verifier.isSolved());
    }

    public void test3TilesIsNotSolved2() {
        verifier = new SolutionVerifier(place3UnsolvedTiles2());
        assertFalse("Unexpectedly solved", verifier.isSolved());
    }

    public void test3TilesIsSolved() {
        verifier = new SolutionVerifier(place3SolvedTiles());
        assertTrue("Unexpectedly not solved", verifier.isSolved());
    }

    public void test4TilesIsNotSolved() {
        verifier = new SolutionVerifier(place4UnsolvedTiles());
        assertFalse("Unexpectedly solved", verifier.isSolved());
    }

    public void test4TilesIsSolved() {
        verifier = new SolutionVerifier(place4SolvedTiles());
        assertTrue("Unexpectedly not solved", verifier.isSolved());
    }

}