// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix1.ui.rendering;

import com.barrybecker4.puzzle.tantrix1.model.HexTile;
import com.barrybecker4.puzzle.tantrix1.model.PathColor;
import com.barrybecker4.puzzle.tantrix1.model.TilePlacement;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Set;

import static com.barrybecker4.puzzle.tantrix1.ui.rendering.HexUtil.*;

/**
 * Renders a single tantrix tile.
 *
 * @author Barry Becker
 */
public class PathRenderer {

    private static final Color PATH_BORDER_COLOR = new Color(10, 10, 10);

    private static final float PATH_FRAC = 0.8f;

    /**
     * Create an instance
     */
    public PathRenderer() {}

    /**
     * Draw one of the tile paths which takes one of three forms: corner, curved, or straight
     */
    public void drawPath(Graphics2D g2, int pathNumber, TilePlacement tilePlacement,
                         Point position, double size) {

        HexTile tile = tilePlacement.getTile();
        int pathStartIndex = getPathStartIndex(tile, pathNumber);

        int i = pathStartIndex + 1;

        PathColor pathColor = tile.getEdgeColor(pathStartIndex);
        while (pathColor != tile.getEdgeColor(i++)) {
            assert(i<6): "Should never exceed 6";
        }

        int pathEndIndex = i-1;
        int diff = pathEndIndex - pathStartIndex;
        Color color = PathColorInterpreter.getColorForPathColor(pathColor);

        // account for the rotation.
        pathStartIndex += tilePlacement.getRotation().ordinal();
        pathEndIndex += tilePlacement.getRotation().ordinal();

        switch (diff) {
            case 1: drawCornerPath(g2, position, pathStartIndex, color, size); break;
            case 5: drawCornerPath(g2, position, pathEndIndex, color, size); break;
            case 2: drawCurvedPath(g2, position, pathStartIndex,color, size); break;
            case 4: drawCurvedPath(g2, position, pathEndIndex, color, size); break;
            case 3: drawStraightPath(g2, position, pathStartIndex, color, size); break;
        }
    }

    /**
     * @return index corresponding to the side that the path starts on.
     */
    private int getPathStartIndex(HexTile tile, int pathNumber) {
        Set<PathColor> set = new HashSet<PathColor>();
        int i = 0;
        do {
            PathColor c = tile.getEdgeColor(i++);
            set.add(c);
        } while (set.size() <= pathNumber);
        return i-1;
    }

    private void drawCornerPath(Graphics2D g2, Point position, int firstIndex,
                                Color color, double radius) {
        int startAngle = firstIndex * 60 + 60;
        int angle = 120;
        double rstartAng = rad(startAngle - 30);
        Point center = new Point((int)(position.getX() + radius * Math.cos(rstartAng)),
                                 (int)(position.getY() - radius * Math.sin(rstartAng)));

        drawPathArc(g2, center, color, radius, radius/3.0, startAngle + 90, angle);
    }

    private void drawCurvedPath(Graphics2D g2, Point position, int firstIndex, Color color, double radius) {
        int startAngle = firstIndex * 60 + 60;
        int angle = 60;
        double rstartAng = rad(startAngle);
        double rad = 2 * radius * ROOT3D2;
        Point center = new Point((int)(position.getX() + rad * Math.cos(rstartAng)),
                                 (int)(position.getY() - rad * Math.sin(rstartAng)));

        drawPathArc(g2, center, color, ROOT3*rad, radius/3.0, startAngle + 150, angle);
    }

    private void drawPathArc(Graphics2D g2, Point center, Color color,
                             double radius, double thickness,
                             int startAngle, int angle) {
        // the black border for the path
        g2.setColor(PATH_BORDER_COLOR);
        g2.setStroke(getPathBGStroke(thickness));
        int s = (int)radius;
        Point c = new Point((int)(center.getX() - radius/2), (int)(center.getY() - radius/2));
        g2.drawArc((int)c.getX(), (int)c.getY(), s, s, startAngle, angle);

        // now the colored path
        g2.setColor(color);
        g2.setStroke(getPathStroke(thickness));
        //g2.drawLine((int)center.getX(), (int)center.getY(),(int)center.getX(), (int)center.getY());
        g2.drawArc((int)c.getX(), (int)c.getY(), s, s, startAngle, angle);
    }


    private void drawStraightPath(Graphics2D g2, Point2D position, int firstIndex,
                                  Color color, double radius) {

        double theta1 = rad(-firstIndex * 60);
        double theta2 = rad(-firstIndex * 60 + 180);

        double halfWidth = radius * ROOT3D2;
        int startX = (int)(position.getX() + halfWidth * Math.cos(theta1));
        int startY = (int)(position.getY() + halfWidth * Math.sin(theta1)-1);
        int endX = (int)(position.getX() + halfWidth * Math.cos(theta2));
        int endY = (int)(position.getY() + halfWidth * Math.sin(theta2)-1);
        g2.setColor(PATH_BORDER_COLOR);
        g2.setStroke(getPathBGStroke(radius/3.0));
        g2.drawLine(startX, startY, endX, endY);
        g2.setColor(color);
        g2.setStroke(getPathStroke(radius/3.0));
        g2.drawLine(startX, startY, endX, endY);
    }

    private static Stroke getPathStroke(double thickness) {
        //CAP_BUTT, CAP_ROUND, and CAP_SQUARE.
        //JOIN_BEVEL, JOIN_MITER, and JOIN_ROUND.
        return new BasicStroke((int)(PATH_FRAC * thickness),
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
    }

    private static Stroke getPathBGStroke(double thickness) {
        return new BasicStroke((int)( thickness),
                BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
    }
}
