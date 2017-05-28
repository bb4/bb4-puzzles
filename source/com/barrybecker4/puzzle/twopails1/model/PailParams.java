// Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.twopails1.model;

/**
 * Defines the two parameters for the two pail problem.
 * Immutable
 * @author Barry Becker
 */
public class PailParams {

    /** the maximum capacity of any pail */
    public static final int MAX_CAPACITY = 99;

    private byte pail1Size;
    private byte pail2Size;
    private byte targetMeasureSize;

    public PailParams(int pail1Size, int pail2Size, int targetMeasureSize) {
        this.pail1Size = (byte) pail1Size;
        this.pail2Size = (byte) pail2Size;
        this.targetMeasureSize = (byte) targetMeasureSize;
    }

    public byte getPail1Size() {
        return pail1Size;
    }

    public byte getPail2Size() {
        return pail2Size;
    }

    public byte getTargetMeasureSize() {
        return targetMeasureSize;
    }

    public byte getBiggest() {
        return (byte) Math.max(pail1Size, pail2Size);
    }

    public String toString() {
        return "pail1="+ pail1Size + " pail2="+ pail2Size + " target=" + targetMeasureSize;
    }
}
