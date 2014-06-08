/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.redpuzzle.ui;

import com.barrybecker4.common.format.FormatUtil;
import com.barrybecker4.puzzle.common.PuzzleRenderer;
import com.barrybecker4.puzzle.redpuzzle.model.Nub;
import com.barrybecker4.puzzle.redpuzzle.model.Piece;
import com.barrybecker4.puzzle.redpuzzle.model.PieceList;
import com.barrybecker4.ui.util.GUIUtil;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * Singleton class that takes a PieceList and renders it for the RedPuzzleViewer.
 * Having the renderer separate from the viewer helps to separate out the rendering logic
 * from other features of the RedPuzzleViewer.
 *
 * @author Barry Becker
 */
public class RedPuzzleRenderer implements PuzzleRenderer<PieceList> {

     /** size of piece in pixels. */
    static final int PIECE_SIZE = 90;
    private static final int THIRD_SIZE = PIECE_SIZE / 3;

    private static final int MARGIN = 75;
    private static final int ORIENT_ARROW_LEN = PIECE_SIZE >> 2;
    private static final int ARROW_HEAD_RAD = 2;

    private static final Color PIECE_TEXT_COLOR = new Color(200, 0, 0);
    private static final Color PIECE_BACKGROUND_COLOR = new Color(255, 205, 215, 55);
    private static final Color GRID_COLOR = new Color(10, 0, 100);
    private static final Color TEXT_COLOR = new Color(0, 0, 0);

    private static final Font NUB_FONT = new Font(GUIUtil.DEFAULT_FONT_FAMILY, Font.PLAIN, 12);
    private static final Font TEXT_FONT = new Font(GUIUtil.DEFAULT_FONT_FAMILY, Font.BOLD, 18);
    // put this here to avoid reallocation during rendering.
    private static char[] symb_ = new char[1];

     // num pieces on edge
    private static final int DIM = (int) Math.sqrt(PieceList.DEFAULT_NUM_PIECES);


    /**
     * private constructor because this class is a singleton.
     * Use getPieceRenderer instead.
     */
    public RedPuzzleRenderer() {}

    /**
     * This renders the current state of the Slider to the screen.
     */
    public void render( Graphics g, PieceList board,  int width, int height ) {

        drawPieceBoundaryGrid((Graphics2D)g, DIM);

        int i;
        // use this to determine of there is a nub mismatch a a given location
        // allocates a little more space tha we actually use, but simpler this way.
        char[][] nubChecks = new char[7][7];

        if (board== null) return;
        for ( i = 0; i < board.size(); i++ ) {
            Piece p = board.get( i );
            int row = i / DIM;
            int col = i % DIM;
            drawPiece(g, p, col, row, nubChecks);
        }
    }

    /**
     * draw the borders around each piece.
     */
    private static void drawPieceBoundaryGrid(Graphics2D g, int dim) {

        int xpos, ypos;

        int rightEdgePos = MARGIN + PIECE_SIZE * dim;
        int bottomEdgePos = MARGIN + PIECE_SIZE * dim;

        // draw the hatches which deliniate the cells
        g.setColor( GRID_COLOR );
        for ( int i = 0; i <= dim; i++ )  //   -----
        {
            ypos = MARGIN + i * PIECE_SIZE;
            g.drawLine( MARGIN, ypos, rightEdgePos, ypos );
        }
        for ( int i = 0; i <= dim; i++ )  //   ||||
        {
            xpos = MARGIN + i * PIECE_SIZE;
            g.drawLine( xpos, MARGIN, xpos, bottomEdgePos );
        }
    }

    /**
     * Draw a puzzle piece at the specified location.
     */
    private static void drawPiece(Graphics g, Piece p, int col, int row, char[][] nubChecks) {

        int xpos = MARGIN + col * PIECE_SIZE + PIECE_SIZE / 9;
        int ypos = MARGIN + row * PIECE_SIZE + 2 * PIECE_SIZE / 9;

        g.setColor( PIECE_BACKGROUND_COLOR );
        g.fillRect( xpos - PIECE_SIZE / 9 + 2, ypos - 2 * PIECE_SIZE / 9 + 1, PIECE_SIZE - 3, PIECE_SIZE - 2 );
        g.setColor( PIECE_TEXT_COLOR );
        g.setFont( NUB_FONT );

        // now draw the pieces that we have so far.
        drawNub(g, p.getTopNub(), xpos, ypos, Piece.Direction.TOP, col, row, nubChecks);
        drawNub(g, p.getRightNub(), xpos, ypos, Piece.Direction.RIGHT, col, row, nubChecks);
        drawNub(g, p.getBottomNub(), xpos, ypos, Piece.Direction.BOTTOM, col, row, nubChecks);
        drawNub(g, p.getLeftNub(), xpos, ypos, Piece.Direction.LEFT, col, row, nubChecks);

        drawOrientationMarker(g, p, xpos, ypos);

        // draw the number in the middle
        g.setColor( TEXT_COLOR );
        g.setFont( TEXT_FONT );
        Integer num = p.getNumber();
        g.drawString( FormatUtil.formatNumber(num), xpos + THIRD_SIZE, ypos + THIRD_SIZE );
    }


    private static void drawNub(Graphics g, Nub nub, int xpos, int ypos, Piece.Direction dir,
                                int col, int row, char[][] nubChecks) {

        int x = 0;
        int y = 0;
        boolean outy = nub.isOuty();
        symb_[0] = nub.getSuitSymbol();
        int ncx = 0;
        int ncy = 0;
        int cx = 0;
        int cy = 0;

        switch (dir) {
            case TOP:
                x = xpos + THIRD_SIZE;
                y = outy? ypos - THIRD_SIZE : ypos;
                ncx = 2 * col + 1;
                ncy = 2 * row;
                cx = xpos + THIRD_SIZE + 2;
                cy = ypos - 2 * PIECE_SIZE / 9;
                break;
            case RIGHT:
                x = outy? xpos + PIECE_SIZE : xpos + 2 * THIRD_SIZE;
                y = ypos + THIRD_SIZE;
                ncx = 2 * col + 2;
                ncy = 2 * row + 1;
                cx = xpos + 8 * PIECE_SIZE / 9;
                cy = ypos + 2 * PIECE_SIZE / 9;
                break;
            case BOTTOM:
                x = xpos + THIRD_SIZE;
                y = outy? ypos + PIECE_SIZE : ypos + 2 * THIRD_SIZE;
                ncx = 2 * col + 1;
                ncy = 2 * row + 2;
                cx = xpos + THIRD_SIZE + 2;
                cy = ypos + PIECE_SIZE;
                break;
            case LEFT:
                x = outy? xpos - THIRD_SIZE : xpos;
                y = ypos + THIRD_SIZE;
                ncx = 2 * col;
                ncy = 2 * row + 1;
                cx = xpos - PIECE_SIZE / 9;
                cy = ypos + 2 * PIECE_SIZE / 9;
                break;
        }

        if (nubChecks[ncx][ncy] == 0)
            nubChecks[ncx][ncy] = symb_[0];

        g.drawChars( symb_, 0, 1, x, y );

        // draw a circle around nubs that are in conflict.
        if (nubChecks[ncx][ncy] != symb_[0]) {
            int diameter = PIECE_SIZE >> 1;
            int rad = diameter >> 1;
            g.drawOval(cx - rad, cy - rad, diameter, diameter);
        }
    }

    /**
     *  draw a marker line to indicate the orientation.
     */
    private static void drawOrientationMarker(Graphics g, Piece p, int xpos, int ypos) {

        int len2 = ORIENT_ARROW_LEN >> 1;
        int x1 = 0, y1 = 0, x2 = 0, y2 = 0, cx = 0, cy = 0;
        int f = PIECE_SIZE / 7;

        switch (p.getOrientation()) {
            case TOP :
                x1 = xpos - len2 + 3*f;
                y1 = ypos + f;
                cx =x2 = xpos + len2 + 3*f;
                cy = y2 = ypos + f;
                break;
            case RIGHT :
                x1 = xpos + 4*f;
                y1 = ypos - len2 + 2*f;
                cx = x2 = xpos + 4*f;
                cy = y2 = ypos + len2 + 2*f;
                break;
            case BOTTOM :
                cx = x1 = xpos - len2 + 3*f;
                cy = y1 = ypos + 3*f;
                x2 = xpos + len2 + 3*f;
                y2 = ypos + 3*f;
                break;
            case LEFT :
                cx = x1 = xpos + 2*f;
                cy = y1 = ypos - len2 + 2*f;
                x2 = xpos + 2*f;
                y2 = ypos + len2 + 2*f;
                break;
        }
        g.drawLine(x1, y1, x2, y2);
        int ahd2 = ARROW_HEAD_RAD >> 1;
        g.drawOval(cx - ahd2, cy - ahd2, ARROW_HEAD_RAD, ARROW_HEAD_RAD);
    }

}


