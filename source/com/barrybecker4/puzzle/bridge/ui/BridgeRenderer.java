/** Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.bridge.ui;

import com.barrybecker4.puzzle.bridge.model.Bridge;
import com.barrybecker4.puzzle.common.PuzzleRenderer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.List;

/**
 * Singleton class that renders the current state of the Bridge puzzle.
 * Having the renderer separate from the viewer helps to separate out the rendering logic
 * from other features of the BridgeViewer.
 *
 * @author Barry Becker
 */
public class BridgeRenderer implements PuzzleRenderer<Bridge> {

    public static final int INC = 60;
    public static final int BRIDGE_WIDTH = 250;

    private static final int MARGIN = 50;
    private static final int TEXT_WIDTH = 100;
    private static final int TEXT_Y = 100;

    private static final Font FONT = new Font("Sans Serif", Font.PLAIN, INC/2);


    /**
     * private constructor because this class is a singleton.
     * Use getPieceRenderer instead.
     */
    public BridgeRenderer() {}

    /**
     * This renders the current state of the Bridge to the screen.
     * Show the people that have not yet crossed on the left; those that have on the right.
     */
    @Override
    public void render( Graphics g, Bridge board, int width, int height ) {


        drawBridge(g);

        drawPeople(g, board.getUncrossedPeople(), MARGIN);
        drawPeople(g, board.getCrossedPeople(), MARGIN + TEXT_WIDTH + BRIDGE_WIDTH);

        drawLight(g, board.isLightCrossed());
    }

    /**
     * Draw the bridge that the people will cross with the light
     */
    private void drawBridge(Graphics g) {
        int leftX = MARGIN + TEXT_WIDTH;
        int rightX = leftX + BRIDGE_WIDTH;

        g.setColor( Color.darkGray );
        for ( int i = 0; i <= 10; i++ ) {
            int ypos = 20 + TEXT_Y + i;
            g.drawLine(leftX, ypos, rightX, ypos);
        }
    }

    private void drawPeople(Graphics g, List<Integer> people, int xpos) {
        g.setColor(Color.BLACK);
        g.setFont(FONT);
        g.drawString(people.toString(), xpos, TEXT_Y);
    }

    private void drawLight(Graphics g, boolean isLightCrossed) {

        int leftPos = MARGIN + 10;
        int rightPos = MARGIN + TEXT_WIDTH + BRIDGE_WIDTH + 10;
        int xpos = isLightCrossed ? rightPos : leftPos;

        g.setColor(Color.YELLOW);
        g.fillOval(xpos, TEXT_Y + 30, 10, 10);
    }

}


