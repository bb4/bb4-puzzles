/** Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.twopails.ui;

import com.barrybecker4.puzzle.common.PuzzleRenderer;
import com.barrybecker4.puzzle.twopails.model.PailParams;
import com.barrybecker4.puzzle.twopails.model.Pails;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 * Singleton class that takes a PieceList and renders it for the TwoPailsViewer.
 * Having the renderer separate from the viewer helps to separate out the rendering logic
 * from other features of the TwoPailsViewer.
 *
 * @author Barry Becker
 */
public class TwoPailsRenderer implements PuzzleRenderer<Pails> {

    public static final float MAX_CONTAINER_HEIGHT = 0.8f;
    public static final float CONTAINER_WIDTH = 0.2f;
    public static final float TEXT_WIDTH = 0.11f;
    public static final float SEPARATION = 0.1f;

    private static final int MARGIN = 70;

    private static final Color CONTAINER_COLOR = new Color(5, 0, 80);
    private static final Color LIQUID_COLOR = new Color(95, 145, 255);

    private static final Font FONT = new Font("Sans Serif", Font.PLAIN, 16);


    /**
     * private constructor because this class is a singleton.
     * Use getPieceRenderer instead.
     */
    public TwoPailsRenderer() {}

    /**
     * This renders the current state of the Pails to the screen.
     */
    @Override
    public void render( Graphics g, Pails pails, int width, int height) {

        PailParams params = pails.getParams();
        byte biggest = params.getBiggest();
        float size1 = (float)params.getPail1Size() / biggest;
        float size2 = (float)params.getPail2Size() / biggest;

        int contWidth = (int)(CONTAINER_WIDTH * width);
        int cont1Height = (int)(size1 * MAX_CONTAINER_HEIGHT * height);
        int cont2Height = (int)(size2 * MAX_CONTAINER_HEIGHT * height);

        int middle = MARGIN + (int)((TEXT_WIDTH + CONTAINER_WIDTH + SEPARATION) * width);
        g.setColor(Color.BLACK);
        g.setFont(FONT);
        g.drawString("First container", MARGIN, MARGIN);
        g.drawString("Max  = " + params.getPail1Size(), MARGIN, 2 * MARGIN);
        g.drawString("Fill = " + pails.getFill1(), MARGIN, height - MARGIN);

        g.drawString("Second container", middle, MARGIN);
        g.drawString("Max = " + params.getPail2Size(), middle, 2 * MARGIN);
        g.drawString("Fill = " + pails.getFill2(), middle, height - MARGIN);

        g.setColor(CONTAINER_COLOR);
        g.drawRect(MARGIN + (int)(TEXT_WIDTH * width), height - MARGIN - cont1Height, contWidth, cont1Height);
        g.drawRect(middle + (int)(TEXT_WIDTH * width), height - MARGIN - cont2Height, contWidth, cont2Height);

        g.setColor(LIQUID_COLOR);
        int fillHeight = (int)((float)pails.getFill1()/biggest * MAX_CONTAINER_HEIGHT * height);
        g.fillRect(MARGIN + (int)(TEXT_WIDTH * width), height - MARGIN - fillHeight, contWidth, fillHeight);
        fillHeight = (int)((float)pails.getFill2()/biggest * MAX_CONTAINER_HEIGHT * height);
        g.fillRect(MARGIN + (int)(TEXT_WIDTH * width), height - MARGIN - fillHeight, contWidth, fillHeight);
    }

}


