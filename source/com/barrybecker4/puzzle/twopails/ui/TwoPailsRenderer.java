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

    public static final float CONTAINER_WIDTH = 0.2f;
    public static final float SEPARATION = 0.11f;
    public static final int TEXT_WIDTH = 70;
    public static final int TEXT_OFFSET = 10;

    private static final int TOP_MARGIN = 160;
    private static final int MARGIN = 60;

    private static final Color CONTAINER_COLOR = new Color(5, 0, 80);
    private static final Color LIQUID_COLOR = new Color(95, 145, 255);

    private static final Font FONT = new Font("Sans Serif", Font.BOLD, 16);


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

        int usableWidth = width - MARGIN;
        int contWidth = (int)(CONTAINER_WIDTH * usableWidth);
        int cont1Height = (int)(size1 * (height - TOP_MARGIN));
        int cont2Height = (int)(size2 * (height - TOP_MARGIN));

        int middle = MARGIN + TEXT_WIDTH + (int)((CONTAINER_WIDTH + SEPARATION) * usableWidth);
        int container1Y = height - MARGIN - cont1Height;
        int container2Y = height - MARGIN - cont2Height;

        g.setColor(Color.BLACK);
        g.setFont(FONT);
        g.drawString("First Pail", MARGIN, MARGIN + TEXT_OFFSET);
        g.drawString("Max  = " + params.getPail1Size(), MARGIN, container1Y + TEXT_OFFSET );
        g.drawString("Fill = " + pails.getFill1(), MARGIN, height - MARGIN);

        g.drawString("Second Pail", middle, MARGIN + TEXT_OFFSET);
        g.drawString("Max = " + params.getPail2Size(), middle, container2Y + TEXT_OFFSET);
        g.drawString("Fill = " + pails.getFill2(), middle, height - MARGIN);

        // show outlines for two containers
        g.setColor(CONTAINER_COLOR);
        g.drawRect(MARGIN + TEXT_WIDTH, container1Y, contWidth, cont1Height);
        g.drawRect(middle + TEXT_WIDTH, container2Y, contWidth, cont2Height);

        g.setColor(LIQUID_COLOR);
        // show fill for first container
        int fillHeight = (int)((float)pails.getFill1()/biggest * cont1Height) - 2;
        g.fillRect(MARGIN + TEXT_WIDTH + 1, height - MARGIN - fillHeight, contWidth-1, fillHeight);

        // show fill for second container
        fillHeight = (int)((float)pails.getFill2()/biggest * cont1Height) - 2;
        g.fillRect(middle + TEXT_WIDTH + 1, height - MARGIN - fillHeight, contWidth-1, fillHeight);
    }

}


