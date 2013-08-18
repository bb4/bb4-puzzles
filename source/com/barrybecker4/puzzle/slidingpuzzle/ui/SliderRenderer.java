/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.slidingpuzzle.ui;

import com.barrybecker4.puzzle.common.PuzzleRenderer;
import com.barrybecker4.puzzle.slidingpuzzle.model.Slider;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 * Singleton class that takes a PieceList and renders it for the SliderViewer.
 * Having the renderer separate from the viewer helps to separate out the rendering logic
 * from other features of the SliderViewer.
 *
 * @author Barry Becker
 */
public class SliderRenderer implements PuzzleRenderer<Slider> {

    public static final int INC = 60;
    public static final int SEPARATION = INC/10;
    public static final int TILE_EDGE = INC - SEPARATION;

    private static final int LEFT_MARGIN = 40;
    private static final int TOP_MARGIN = 55;

    private static final Color TILE_COLOR = new Color(235, 145, 255);

    private static final Font FONT = new Font("Sans Serif", Font.PLAIN, INC/2);


    /**
     * private constructor because this class is a singleton.
     * Use getPieceRenderer instead.
     */
    public SliderRenderer() {}

    /**
     * This renders the current state of the Slider to the screen.
     */
    @Override
    public void render( Graphics g, Slider board, int width, int height ) {

        int size = board.getSize();
        int rightEdgePos = LEFT_MARGIN + INC * size;
        int bottomEdgePos = TOP_MARGIN + INC * size;

        drawBorder(g, size, rightEdgePos, bottomEdgePos);


        // now draw the pieces that we have so far
        for (byte row = 0; row < size; row++) {
            for (byte col = 0; col < size; col++) {
                    drawTile(g, board, row, col);
            }
        }
    }

    /**
     *  draw the hatches which delineate the cells
     */
    private void drawBorder(Graphics g, int size, int rightEdgePos, int bottomEdgePos) {
        int i, ypos, xpos;
        int offset = SEPARATION/2;

        g.setColor( Color.darkGray );
        for ( i = 0; i <= size; i+=size )  //   -----
        {
            ypos = TOP_MARGIN + i * INC - offset;
            g.drawLine( LEFT_MARGIN - offset, ypos, rightEdgePos - offset, ypos );
        }
        for ( i = 0; i <= size; i+=size )  //   ||||
        {
            xpos = LEFT_MARGIN + i * INC - offset;
            g.drawLine( xpos, TOP_MARGIN - offset, xpos, bottomEdgePos - offset );
        }
    }

    private void drawTile(Graphics g, Slider board, byte row, byte col) {
        int xpos = LEFT_MARGIN + col * INC ;
        int ypos = TOP_MARGIN + row * INC;

        int value = board.getPosition(row, col);
        boolean empty = value == 0;

        if (!empty) {
            Color c = TILE_COLOR;
            g.setColor(c);
            g.fillRect(xpos, ypos, TILE_EDGE, TILE_EDGE);

            g.setColor(Color.BLACK);
            g.setFont(FONT);
            g.drawString(Integer.toString(value), xpos + INC/4, ypos + 2*INC/3);
            g.drawRect(xpos , ypos, TILE_EDGE, TILE_EDGE);
        }
    }
}


