// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix1.ui.rendering;

import com.barrybecker4.common.format.FormatUtil;
import com.barrybecker4.common.geometry.Location;
import com.barrybecker4.puzzle.tantrix1.model.TilePlacement;
import com.barrybecker4.ui.util.GUIUtil;

import java.awt.*;

import static com.barrybecker4.puzzle.tantrix1.ui.rendering.TantrixBoardRenderer.TOP_MARGIN;

/**
 * Renders a single tantrix tile.
 *
 * @author Barry Becker
 */
public class HexTileRenderer {

    private static final Font TILE_FONT = new Font(GUIUtil.DEFAULT_FONT_FAMILY, Font.PLAIN, 9 );
    private static final Stroke TILE_STROKE = new BasicStroke(1);
    private static final Color TILE_BORDER_COLOR = new Color(70, 70, 70);
    private static final Color TILE_BG_COLOR = new Color(200, 200, 200);

    private PathRenderer pathRenderer;


    /**
     * Create an instance
     */
    public HexTileRenderer() {
        pathRenderer = new PathRenderer();
    }

    /**
     * Draw the poker hand (the cards are all face up or all face down)
     */
    public void render(Graphics2D g2, TilePlacement tilePlacement,
                       Location topLeftCorner, double radius) {

        if (tilePlacement == null) return;
        boolean isOddRow = tilePlacement.getLocation().getRow() % 2 == 1;
        Location location =
            tilePlacement.getLocation().decrementOnCopy(topLeftCorner);

        double x = radius/2
                + ((location.getCol() - (isOddRow ? -0.25:  -0.75)) * 2 * radius * HexUtil.ROOT3D2);
        double y = radius/2  + TOP_MARGIN
                + ((location.getRow() + 0.6) * 3.0 * radius / 2.0);

        Point point = new Point((int)x, (int)y);
        drawHexagon(g2, point, radius);
        pathRenderer.drawPath(g2, 0, tilePlacement, point, radius);
        pathRenderer.drawPath(g2, 1, tilePlacement, point, radius);
        pathRenderer.drawPath(g2, 2, tilePlacement, point, radius);

        drawTileNumber(g2, tilePlacement, radius, x, y);
    }

    private void drawTileNumber(Graphics2D g2, TilePlacement tilePlacement, double radius, double x, double y) {
        g2.setColor(Color.BLACK);
        g2.setFont(TILE_FONT);
        g2.drawString(FormatUtil.formatNumber(tilePlacement.getTile().getTantrixNumber()),
                      (int)x, (int)(y + radius/2));
    }

    private void drawHexagon(Graphics2D g2, Point point, double radius) {

        int numPoints = 7;
        int[] xpoints = new int[numPoints];
        int[] ypoints = new int[numPoints];

        for (int i = 0; i <= 6; i++) {
            double angStart = HexUtil.rad(30 + 60 * i);
            xpoints[i] = (int)(point.getX() + radius * Math.cos(angStart));
            ypoints[i] = (int)(point.getY() + radius * Math.sin(angStart));
        }

        Polygon poly = new Polygon(xpoints, ypoints, numPoints);
        g2.setColor(TILE_BG_COLOR);
        g2.fillPolygon(poly);
        g2.setColor(TILE_BORDER_COLOR);
        g2.setStroke(TILE_STROKE);
        g2.drawPolygon(poly);
    }
}
