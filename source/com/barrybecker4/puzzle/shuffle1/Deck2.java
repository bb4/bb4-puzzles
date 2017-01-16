package com.barrybecker4.puzzle.shuffle1;

import com.barrybecker4.common.math.MathUtil;

/**
 * Analytic solution to the card shuffling problem.
 * Web references:
 *  http://www.math.ucsd.edu/~ronspubs/83_05_shuffles.pdf
 *  http://www.oreillynet.com/pub/wlg/5094 (Scott's original link, now broken)
 *  https://www.math.hmc.edu/funfacts/ffiles/20001.1-6.shtml
 *  https://coderanch.com/t/35113/Card-Shuffle
 *
 * @author Scott Sauyet
 */
public class Deck2 implements Deck {

    private int[] cards;
    private int count;

    public long shuffleUntilSorted(int iCut) {

        //Deck2 deck = new Deck2(nCards);
        this.doPerfectShuffle(iCut);
        return MathUtil.lcm(getCycles());
    }

    Deck2(int count) {

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

    public int size() {
        return cards.length;
    }

    public int get(int i) {
        return cards[i];
    }

    public void doPerfectShuffle(int cut) {

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

    private int[] getCycles() {

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


    public static void main(String[] args) {

        assert (args.length == 2) : "Usage: java Deck nCards iCut";

        int cards = 1002;
        int cut = 101;

        try {
            cards = Integer.parseInt(args[0]);
            cut = Integer.parseInt(args[1]);
        } catch (NumberFormatException nfe) {
            System.out.println("Arguments must be numeric.");
            System.exit(2);
        }

        long start = System.currentTimeMillis();
        Deck2 deck = new Deck2(cards);
        long result = deck.shuffleUntilSorted(cut);
        long time = System.currentTimeMillis() - start;

        System.out.println("A perfect shuffleUntilSorted on " + cards + " cards, cut " + cut
                           + " deep, takes " + result + " iterations to restore"
                           + " the deck.");

        System.out.println("Calculation performed in " + time + "ms.");
    }
}
