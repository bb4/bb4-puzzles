// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.shuffle;

import com.barrybecker4.common.math.MathUtil;

/**
 * Analytic solution to the card shuffling problem.
 *
 * @author Scott Sauyet (from http://www.oreillynet.com/pub/wlg/5094)
 */
public class Deck2 {

    public static long shuffle(int nCards, int iCut) {

        Deck2 deck = new Deck2(nCards);
        deck.shuffle(iCut);
        return MathUtil.lcm(deck.getCycles());
    }

    public Deck2(int count) {

        if (count < 1) {
            throw new IllegalArgumentException("Deck must contain at least " +
                                    "one card.  You entered " + count + '.');
        }

        this.count = count;
        cards = new int[count];
        for (int i = 0 ; i < count; i++) {
            cards[i] = i;
        }
    }

    public void shuffle(int cut) {

        cut %= count;
        int[] top = new int[cut];

        System.arraycopy(cards, 0, top, 0, cut);

        int[] bottom = new int[count - cut];

        System.arraycopy(cards, cut, bottom, 0, count - cut);

        int[] updated = new int[count];
        int shared = Math.min(cut, count - cut);
        int different = count - shared - shared;
        int[] extras = new int[different];

        if (cut > count - cut) {
            System.arraycopy(top, 0, extras, 0, different);
        } else {
            System.arraycopy(bottom, 0, extras, 0, different);
        }

        for (int i = 0; i < shared; i++) {

            updated[count - (2 * i) - 1] = top[cut - i - 1];
            updated[count - (2 * i) - 2] = bottom[count - cut - i - 1];
        }

        System.arraycopy(extras, 0, updated, 0, different);
        cards = updated;
    }



    public int[] getCycles() {

        int[] cycles = new int[count];

        for (int i = 0; i < count; i++) {

            int index = i;
            int result = 1;
            while (cards[index] != i) {
                result++;
                index = cards[index];
            }
            cycles[i] = result;
        }

        return cycles;
    }

    @Override
    public String toString() {

        StringBuilder buffer = new StringBuilder();
        buffer.append("Deck [");
        buffer.append(cards[0]);

        for (int i = 1; i < count; i++) {
            buffer.append(", ");
            buffer.append(cards[i]);
        }

        buffer.append("]");
        return buffer.toString();
    }

    private int[] cards;
    private int count;

    public static void main(String[] args) {

        if (args.length != 2 && args.length != 0) {
            System.out.println("Usage: java Deck nCards iCut");
            System.exit(1);
        }

        int cards = 1002;
        int cut = 101;

        if (args.length == 2) {
            try {
                cards = Integer.parseInt(args[0]);
                cut = Integer.parseInt(args[1]);
            } catch (NumberFormatException nfe) {
                System.out.println("Arguments must be numeric.");
                System.exit(2);
            }
        }

        long start = System.currentTimeMillis();
        long result = shuffle(cards, cut);
        long time = System.currentTimeMillis() - start;

        System.out.println("A perfect shuffle on " + cards + " cards, cut " + cut
                           + " deep, takes " + result + " iterations to restore"
                           + " the deck.");

        System.out.println("Calculation performed in " + time + "ms.");
    }

}

