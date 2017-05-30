// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.hiq.ui;

import com.barrybecker4.puzzle.common1.ui.PuzzleRenderer;
import com.barrybecker4.puzzle.hiq.model.PegBoard1;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Singleton class that takes a PieceList and renders it for the PegBoardViewer1.
 * Having the renderer separate from the viewer helps to separate out the rendering logic
 * from other features of the PegBoardViewer1.
 *
 * @author Barry Becker
 */
public class PegBoardRenderer1 implements PuzzleRenderer<PegBoard1> {

    public static final int INC = 10;

    private static final int LEFT_MARGIN = 50;
    private static final int TOP_MARGIN = 55;

    private static final Color FILLED_HOLE_COLOR = new Color(120, 0, 190);
    private static final Color EMPTY_HOLE_COLOR = new Color(55, 55, 65, 150);
    private static final int FILLED_HOLE_RAD = 16;
    private static final int EMPTY_HOLE_RAD = 9;

    /**
     * private constructor because this class is a singleton.
     * Use getPieceRenderer instead.
     */
    public PegBoardRenderer1() {}

    /**
     * This renders the current state of the Slider to the screen.
     */
    @Override
    public void render(Graphics g, PegBoard1 board, int width, int height ) {

        int size = PegBoard1.SIZE;
        int rightEdgePos = LEFT_MARGIN + 3 * INC * size;
        int bottomEdgePos = TOP_MARGIN + 3 * INC * size;

        drawGrid(g, size, rightEdgePos, bottomEdgePos);


        // now draw the pieces that we have so far
        for (byte row = 0; row < size; row++) {
            for (byte col = 0; col < size; col++) {

                if (PegBoard1.isValidPosition(row, col)) {

                    drawPegLocation(g, board, row, col);
                }
            }
        }
    }

    /**
     *  draw the hatches which delineate the cells
     */
    private void drawGrid(Graphics g, int size, int rightEdgePos, int bottomEdgePos) {
        int i, ypos, xpos;

        g.setColor( Color.darkGray );
        for ( i = 0; i <= size; i++ )  //   -----
        {
            ypos = TOP_MARGIN + i * 3 * INC;
            g.drawLine( LEFT_MARGIN, ypos, rightEdgePos, ypos );
        }
        for ( i = 0; i <= size; i++ )  //   ||||
        {
            xpos = LEFT_MARGIN + i * 3 * INC;
            g.drawLine( xpos, TOP_MARGIN, xpos, bottomEdgePos );
        }
    }

    private void drawPegLocation(Graphics g, PegBoard1 board, byte row, byte col) {
        int xpos;
        int ypos;
        xpos = LEFT_MARGIN + col * 3 * INC + INC / 3;
        ypos = TOP_MARGIN + row * 3 * INC + 2 * INC / 3;

        boolean empty = board.isEmpty(row, col);
        Color c = empty ?  EMPTY_HOLE_COLOR : FILLED_HOLE_COLOR;
        int r = empty ? EMPTY_HOLE_RAD : FILLED_HOLE_RAD;
        g.setColor(c);
        int rr = r / 2;

        g.fillOval(xpos + INC - rr, ypos + INC - rr, r, r);
    }
}


