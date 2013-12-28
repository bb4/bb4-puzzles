package com.barrybecker4.puzzle.tantrix.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Barry Becker
 */
public class RotationTest {

    @Test
    public void testRotateBy2() {

        assertEquals("Unexpected rotation.",
                Rotation.ANGLE_120, Rotation.ANGLE_0.rotateBy(2));
    }

    @Test
    public void testRotateByNegative2() {

        assertEquals("Unexpected rotation.",
                Rotation.ANGLE_240, Rotation.ANGLE_0.rotateBy(-2));
    }

    @Test
    public void testRotateByNothing() {

        assertEquals("Unexpected rotation.",
                Rotation.ANGLE_0, Rotation.ANGLE_0.rotateBy(0));
    }

}
