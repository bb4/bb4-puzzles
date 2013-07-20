// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.maze.ui;

import com.barrybecker4.common.geometry.Location;
import com.barrybecker4.puzzle.maze.model.MazeCell;
import com.barrybecker4.puzzle.maze.model.MazeModel;
import com.barrybecker4.ui.util.GUIUtil;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;

/**
 * Responsible for drawing the Maze based on the MazeModel.
 * @author Barry Becker
 */
public class MazeRenderer {

    // rendering attributes
    private static final Color WALL_COLOR = new Color( 80, 0, 150 );
    private static final Color PATH_COLOR = new Color( 255, 220, 50);

    private static final Color TEXT_COLOR = new Color( 250, 0, 100 );
    private static final Color BG_COLOR = new Color( 205, 220, 250 );
    private static final Color VISITED_COLOR = new Color( 255, 255, 255 );

    private static final int WALL_LINE_WIDTH = 3;
    private static final int PATH_LINE_WIDTH = 14;

    private static final int DEFAULT_CELL_SIZE = 40;
    private int cellSize;
    private int halfCellSize;

    private Stroke wallStroke;
    private Stroke pathStroke;
    private Font textFont;

    /**
     * Constructor
     */
    public MazeRenderer() {
        setCellSize(DEFAULT_CELL_SIZE);
    }

    public void setCellSize(int size) {
        cellSize = size;
        halfCellSize =  (int) (cellSize/2.0);

        int lineWidth = (int) (WALL_LINE_WIDTH * cellSize / 30.0);
        int pathWidth = (int) (PATH_LINE_WIDTH * cellSize / 30.0);

        wallStroke = new BasicStroke( lineWidth );
        pathStroke = new BasicStroke( pathWidth );

        int fontSize = 2 + (cellSize >> 1);
        textFont = new Font(GUIUtil.DEFAULT_FONT_FAMILY, Font.BOLD, fontSize);
    }

    /**
     * Render the Environment on the screen.
     */
    public void render( Graphics2D g2, MazeModel maze) {

        if (maze == null) return;

        drawBackground(g2, maze);
        drawVisitedCells(g2, maze);
        drawWalls(g2, maze);
        drawPath(g2, maze);

        drawStartFinish(g2, maze);
    }

    private void drawBackground(Graphics2D g2, MazeModel maze) {
        g2.setColor( BG_COLOR );
        int width = maze.getWidth();
        int height = maze.getHeight();
        g2.fillRect( 0, 0, cellSize * width, cellSize * height );
    }

    private void drawVisitedCells(Graphics2D g2, MazeModel maze) {

        g2.setColor( VISITED_COLOR );
        for (int j = 0; j < maze.getHeight(); j++ ) {
            for (int i = 0; i < maze.getWidth(); i++ ) {
                MazeCell c = maze.getCell(i, j);
                assert c != null : "Error1 pos i=" + i + " j=" + j + " is out of bounds." ;
                int xpos = i * cellSize;
                int ypos = j * cellSize;

                if ( c.visited ) {
                    g2.setColor( VISITED_COLOR );
                    g2.fillRect( xpos + 1, ypos + 1, cellSize, cellSize );
                    //g2.setColor(PathColor.black);
                }
            }
        }
    }

    private void drawWalls(Graphics2D g2, MazeModel maze) {

        g2.setStroke( wallStroke );
        g2.setColor( WALL_COLOR );
        for (int j = 0; j < maze.getHeight(); j++ ) {
            for (int i = 0; i < maze.getWidth(); i++ ) {
                MazeCell c = maze.getCell(i, j);
                assert ( c != null ) : "Error2 pos i=" + i + " j=" + j  + " is out of bounds.";

                int xpos = i * cellSize;
                int ypos = j * cellSize;

                if ( c.eastWall ) {
                    g2.drawLine( xpos + cellSize, ypos, xpos + cellSize, ypos + cellSize );
                }
                if ( c.southWall ) {
                    g2.drawLine( xpos, ypos + cellSize, xpos + cellSize, ypos + cellSize );
                }
            }
        }
    }

    private void drawPath(Graphics2D g2, MazeModel maze) {

        g2.setStroke( pathStroke );
        g2.setColor( PATH_COLOR );

        for (int j = 0; j < maze.getHeight(); j++ ) {
            for (int i = 0; i < maze.getWidth(); i++ ) {
                MazeCell c = maze.getCell(i,  j);
                int xpos = i * cellSize;
                int ypos = j * cellSize;

                assert c != null;
                if ( c.eastPath )  {
                     g2.drawLine( xpos + halfCellSize, ypos + halfCellSize, xpos + cellSize, ypos + halfCellSize );
                }
                if ( c.westPath )  {
                     g2.drawLine( xpos, ypos + halfCellSize, xpos + halfCellSize, ypos + halfCellSize );
                }
                if ( c.northPath )  {
                     g2.drawLine( xpos + halfCellSize, ypos + halfCellSize, xpos + halfCellSize, ypos );
                }
                if ( c.southPath )  {
                     g2.drawLine( xpos + halfCellSize, ypos + cellSize, xpos + halfCellSize, ypos + halfCellSize );
                }
             }
         }
    }

    private void drawStartFinish(Graphics2D g2, MazeModel maze) {
        g2.setFont( textFont );
        g2.setColor( TEXT_COLOR );
        drawChar("S", maze.getStartPosition(), cellSize, g2);
        drawChar("F", maze.getStopPosition(), cellSize, g2);
    }

    private static void drawChar(String c, Location pos,  int cellSize, Graphics2D g2) {
        if (pos != null)  {
            g2.drawString( c, (int) ((pos.getX() + 0.32) * cellSize), (int) ((pos.getY() + 0.76) * cellSize) );
        }
    }
}
