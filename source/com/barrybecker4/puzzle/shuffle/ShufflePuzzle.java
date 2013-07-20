/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.shuffle;

/**
 * Given a deck of nCards unique cards, cut the deck iCut cards from top and perform a perfect shuffle.
 * A perfect shuffle begins by putting down the bottom card from the top portion of the deck followed by the bottom card
 * from the bottom portion of the deck followed by the next card from the top portion, etc., alternating cards
 * until one portion is used up. The remaining cards go on top. The problem is to find the number of
 * perfect shuffles required to return the deck to its original order. Your function should be declared as:
 *
 *    static long shuffles(int nCards, int iCut);
 *
 * Please send the result of shuffles(1002, 101) along with your program and your resume to 'resume' at nextag.com.
 *
 * @author Barry Becker Date: Jan 3, 2006
 */
public class ShufflePuzzle {

     private ShufflePuzzle() {}

     static long shuffles(int nCards, int iCut) {
         assert (iCut < nCards);
         Deck deck = new Deck(nCards);

         int ct = 1;
         deck.doPerfectShuffle(iCut);
         while (!deck.isSorted() && ct < Integer.MAX_VALUE)  {

             deck.doPerfectShuffle(iCut);
             ct++;
             if (ct % 100000 == 0)  {
                System.out.println(ct + "  " + deck);
             }
         }
         assert (ct < Integer.MAX_VALUE) : "No amount of shuffling will restore the order";
         return ct;
     }

    public static void main(String[] args) {
        long time = System.currentTimeMillis();

        int nCards = 402;
        int iCut = 101;
        System.out.println("A sorted deck of " + nCards + " cards, cut " + iCut + " deep takes "
                           + shuffles(nCards, iCut) + " perfect shuffles to restore the deck.");
        System.out.println("time elapsed = " + (System.currentTimeMillis() - time) +" milliseconds");
    }
}

