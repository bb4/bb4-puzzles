// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.shuffle;

/**
 * @author Barry Becker
 */
public interface Deck {

    int size();
    int get(int i);
    long shuffleUntilSorted(int iCut);
    void doPerfectShuffle(int iCut);
}
