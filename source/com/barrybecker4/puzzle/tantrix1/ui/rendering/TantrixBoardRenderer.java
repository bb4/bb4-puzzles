// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix1.ui.rendering;

import com.barrybecker4.common.geometry.Location;
import com.barrybecker4.puzzle.common.PuzzleRenderer;
import com.barrybecker4.puzzle.tantrix1.model.TantrixBoard;
import com.barrybecker4.puzzle.tantrix1.model.TilePlacement;

import java.awt.*;

import static com.barrybecker4.puzzle.tantrix1.ui.rendering.HexUtil.ROOT3;

/**
 * Renders the the tantrix puzzle onscreen.
 * @author Barry Becker
 */
public class TantrixBoardRenderer implements PuzzleRenderer<TantrixBoard> {

    private static final double MARGIN_FRAC = 0.2;
    static final int TOP_MARGIN = 15;

    private static final Color GRID_COLOR = new Color(130, 140, 170);

    private double hexRadius;
    private HexTileRenderer tileRenderer;

    /**
     * Constructor
     */
    public TantrixBoardRenderer() {
        tileRenderer = new HexTileRenderer();
    }

    /**
     * This renders the current state of the TantrixBoard to the screen.
     */
    public void render(Graphics g, TantrixBoard board, int width, int height)  {

        if (board == null) return;
        Graphics2D g2 = (Graphics2D) g;
        int minEdge = Math.min(width, height);
        hexRadius = (1.0 - MARGIN_FRAC) * minEdge / (board.getEdgeLength() * ROOT3 * .9);

        setHints(g2);
        drawGrid(g2, board);

        Location topLeftCorner = board.getBoundingBox().getTopLeftCorner();

        for (Location loc : board.getTantrixLocations()) {
            TilePlacement placement = board.getTilePlacement(loc);
            tileRenderer.render(g2, placement, topLeftCorner, hexRadius);
        }
    }

    private void setHints(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
    }

    /**
     * Draw the gridlines over the background.
     */
    protected void drawGrid(Graphics2D g2, TantrixBoard board) {

        int edgeLen = board.getEdgeLength();
        int xpos, ypos;
        int i;
        int start = 0;
        int margin = (int)(hexRadius/2.0);
        double hexWidth = ROOT3 * hexRadius;
        int rightEdgePos = (int)(margin + hexWidth * edgeLen);
        int bottomEdgePos = (int)(TOP_MARGIN + margin + hexWidth * edgeLen);

        g2.setColor( GRID_COLOR );
        for ( i = start; i <= edgeLen; i++ )  //   -----
        {
            ypos = (int)(TOP_MARGIN + margin + i * hexWidth);
            g2.drawLine( margin, ypos, rightEdgePos, ypos );
        }
        for ( i = start; i <= edgeLen; i++ )  //   ||||
        {
            xpos = (int)(margin + i * hexWidth);
            g2.drawLine( xpos, TOP_MARGIN + margin, xpos, bottomEdgePos );
        }
    }
}