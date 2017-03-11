// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix1.ui.rendering;

import com.barrybecker4.puzzle.tantrix1.model.PathColor;

import java.awt.*;

/**
 * Renders a single tantrix tile.
 *
 * @author Barry Becker
 */
public class PathColorInterpreter {

    private static final Color BLUE_COLOR = new Color(60, 90, 250);
    private static final Color RED_COLOR = new Color(210, 75, 70);
    private static final Color GREEN_COLOR = new Color(40, 210, 50);
    private static final Color YELLOW_COLOR = new Color(230, 230, 40);
    private static final Color WHITE_COLOR = new Color(251, 250, 254);

    public static Color getColorForPathColor(PathColor pathColor) {

        Color color = Color.GRAY;
        switch (pathColor) {
            case BLUE: color = BLUE_COLOR; break;
            case RED : color = RED_COLOR;  break;
            case GREEN : color = GREEN_COLOR; break;
            case YELLOW : color = YELLOW_COLOR; break;
            case WHITE : color = WHITE_COLOR; break;
        }
        return color; // never returns
    }

    /** hidden constructor */
    private PathColorInterpreter() {}
}
