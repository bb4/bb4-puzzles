/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.slidingpuzzle.ui;

import com.barrybecker4.puzzle.common.PuzzleRenderer;
import com.barrybecker4.puzzle.slidingpuzzle.model.Board;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Singleton class that takes a PieceList and renders it for the BoardViewer.
 * Having the renderer separate from the viewer helps to separate out the rendering logic
 * from other features of the BoardViewer.
 *
 * @author Barry Becker
 */
public class BoardRenderer implements PuzzleRenderer<Board> {

    public static final int INC = 60;
    public static final int SEPARATION = INC/20;

    private static final int LEFT_MARGIN = 40;
    private static final int TOP_MARGIN = 55;

    private static final Color BLANK_COLOR = new Color(255, 255, 255);
    private static final Color TILE_COLOR = new Color(235, 145, 255);


    /**
     * private constructor because this class is a singleton.
     * Use getPieceRenderer instead.
     */
    public BoardRenderer() {}

    /**
     * This renders the current state of the Board to the screen.
     */
    @Override
    public void render( Graphics g, Board board, int width, int height ) {

        int size = board.getSize();
        int rightEdgePos = LEFT_MARGIN + INC * size;
        int bottomEdgePos = TOP_MARGIN + INC * size;

        drawGrid(g, size, rightEdgePos, bottomEdgePos);


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
    private void drawGrid(Graphics g, int size, int rightEdgePos, int bottomEdgePos) {
        int i, ypos, xpos;

        g.setColor( Color.darkGray );
        for ( i = 0; i <= size; i++ )  //   -----
        {
            ypos = TOP_MARGIN + i * INC;
            g.drawLine( LEFT_MARGIN, ypos, rightEdgePos, ypos );
        }
        for ( i = 0; i <= size; i++ )  //   ||||
        {
            xpos = LEFT_MARGIN + i * INC;
            g.drawLine( xpos, TOP_MARGIN, xpos, bottomEdgePos );
        }
    }

    private void drawTile(Graphics g, Board board, byte row, byte col) {
        int xpos = LEFT_MARGIN + col * INC ;
        int ypos = TOP_MARGIN + row * INC;

        int value = board.getPosition(row, col);
        boolean empty = value == 0;
        Color c = empty ?  BLANK_COLOR : TILE_COLOR;
        g.setColor(c);

        g.fillRect(xpos , ypos, INC - SEPARATION, INC - SEPARATION);
        g.setColor(Color.BLACK);
        g.drawString(Integer.toString(value), xpos + INC/3, ypos + INC/2);
        g.drawRect(xpos , ypos, INC - SEPARATION, INC - SEPARATION);
    }
}


