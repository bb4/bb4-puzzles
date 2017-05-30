// Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.bridge.ui;

import com.barrybecker4.puzzle.bridge.model.Bridge1;
import com.barrybecker4.puzzle.common1.ui.PuzzleRenderer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.List;

/**
 * Singleton class that renders the current state of the Bridge1 puzzle.
 * Having the renderer separate from the viewer helps to separate out the rendering logic
 * from other features of the BridgeViewer1.
 *
 * @author Barry Becker
 */
public class BridgeRenderer1 implements PuzzleRenderer<Bridge1> {

    public static final int INC = 60;
    public static final int BRIDGE_WIDTH = 260;

    private static final int MARGIN = 50;
    private static final int TEXT_WIDTH = 230;
    private static final int TEXT_Y = 190;

    private static final Font FONT = new Font("Sans Serif", Font.PLAIN, INC/2);

    private static final int LIGHT_RADIUS = 30;


    /**
     * private constructor because this class is a singleton.
     * Use getPieceRenderer instead.
     */
    public BridgeRenderer1() {}

    /**
     * This renders the current state of the Bridge1 to the screen.
     * Show the people that have not yet crossed on the left; those that have on the right.
     */
    @Override
    public void render(Graphics g, Bridge1 board, int width, int height ) {

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
            int ypos = TEXT_Y + i;
            g.drawLine(leftX, ypos, rightX, ypos);
        }
    }

    private void drawPeople(Graphics g, List<Integer> people, int xpos) {
        g.setColor(Color.BLACK);
        g.setFont(FONT);
        String peopleListString = people.toString();
        if (people.size() <= 5) {
            g.drawString(peopleListString, xpos + 10, TEXT_Y);
        }
        else {
            // split into 2 lines for better readability
            int idx = peopleListString.indexOf(",", 12) + 1;
            String part1 = peopleListString.substring(0, idx);
            String part2 = peopleListString.substring(idx);
            g.drawString(part1, xpos + 10, TEXT_Y);
            g.drawString(part2, xpos + 10, TEXT_Y + 40);
        }
    }

    private void drawLight(Graphics g, boolean isLightCrossed) {

        int leftPos = MARGIN + 20;
        int rightPos = MARGIN + TEXT_WIDTH + BRIDGE_WIDTH + 20;
        int xpos = isLightCrossed ? rightPos : leftPos;

        g.setColor(Color.YELLOW);
        g.fillOval(xpos, TEXT_Y - 80, LIGHT_RADIUS, LIGHT_RADIUS);
        g.setColor(Color.BLACK);
        g.drawOval(xpos, TEXT_Y - 80, LIGHT_RADIUS, LIGHT_RADIUS);
    }

}


