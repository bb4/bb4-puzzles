// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku1.model;

/**
 * Helps with showing large numbers on the board. Two digit numbers are converted to letters.
 * @author Barry Becker
 */
public class ValueConverter {

    /**
     * Get a one character symbol for the value.
     * @param value value to get symbol for.
     * @return symbol corresponding to the specified value.
     */
    public static String getSymbol(int value) {

        String sValue = "-";
        switch (value) {
            case 10 : sValue = "X"; break;
            case 11 : sValue = "A"; break;
            case 12 : sValue = "B"; break;
            case 13 : sValue = "C"; break;
            case 14 : sValue = "D"; break;
            case 15 : sValue = "E"; break;
            case 16 : sValue = "F"; break;
            case 17 : sValue = "G"; break;
            case 18 : sValue = "H"; break;
            case 19 : sValue = "I"; break;
            case 20 : sValue = "J"; break;
            case 21 : sValue = "K"; break;
            case 22 : sValue = "L"; break;
            case 23 : sValue = "M"; break;
            case 24 : sValue = "N"; break;
            case 25 : sValue = "O"; break;
            default : sValue = Integer.toString(value);
        }
        return sValue;
    }

    /**
     * Get the integer value for the specified symbol.
     * @param symbol
     * @param maxValue maximum allowed value
     * @return integer value
     * @throws IllegalArgumentException if not a valid symbol for the puzzle.
     */
    public static int getValue(char symbol, int maxValue) {

        int value;
        Character upperSymb = Character.toString(symbol).toUpperCase().charAt(0);

        switch (upperSymb) {
            case 'X' : value = 10; break;
            case 'A' : value = 11; break;
            case 'B' : value = 12; break;
            case 'C' : value = 13; break;
            case 'D' : value = 14; break;
            case 'E' : value = 15; break;
            case 'F' : value = 16; break;
            case 'G' : value = 17; break;
            case 'H' : value = 18; break;
            case 'I' : value = 19; break;
            case 'J' : value = 20; break;
            case 'K' : value = 21; break;
            case 'L' : value = 22; break;
            case 'M' : value = 23; break;
            case 'N' : value = 24; break;
            case 'O' : value = 25; break;
            default :
                try {
                    value = Integer.parseInt(Character.toString(symbol));
                } catch (NumberFormatException nfe) {
                    throw new IllegalArgumentException("Invalid: " + symbol);
                }
        }
        if (value==0 || value > maxValue) {
            throw new IllegalArgumentException("Invalid: " + symbol);
        }
        return value;
    }

    private ValueConverter() {}
}
