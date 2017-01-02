// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.maze.ui;

import com.barrybecker4.common.concurrency.ThreadUtil;
import com.barrybecker4.common.geometry.Location;
import com.barrybecker4.common.math.MathUtil;
import com.barrybecker4.puzzle.maze.model.MazeModel;

import javax.swing.JComponent;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * This panel is responsible for drawing the MazeModel using the MazeRenderer.
 * @author Barry Becker
 */
public class MazePanel extends JComponent {

    /** represents the maze that we need to render. */
    private MazeModel maze_;
    private int animationSpeed_;
    private int cellSize;
    private MazeRenderer renderer;


    public MazePanel() {
        maze_ = new MazeModel(100, 100);
        renderer = new MazeRenderer();
    }

    public MazeModel getMaze() {
        return maze_;
    }

    public void setAnimationSpeed(int animationSpeed) {
        animationSpeed_ = animationSpeed;
    }

    public int getAnimationSpeed() {
        return animationSpeed_;
    }

    public void setThickness(int thickness) {

        Dimension dim = getSize();
        if (dim.width <= 0 || dim.height < 0)  {
            return;
        }

        cellSize = thickness;
        renderer.setCellSize(cellSize);
        int width = dim.width / thickness;
        int height = dim.height / thickness;
        maze_.setDimensions(width, height);
    }

    /**
     * paint the whole window right now!
     */
    public void paintAll() {
        Dimension d = this.getSize();
        this.paintImmediately( 0, 0, (int) d.getWidth(), (int) d.getHeight() );
    }

    /**
     * Paint just the region around a single cell for performance.
     * @param point location of the cell to render.
     */
    public void paintCell(Location point) {
        int csized2 = (cellSize/2) + 2;
        int xpos = (point.getX() * cellSize);
        int ypos = (point.getY() * cellSize);

        if (animationSpeed_ <= 10)  {
            // this paints just the cell immediately (sorta slow)
            this.paintImmediately( xpos-csized2, ypos-csized2, (2*cellSize), (2*cellSize));
            if (animationSpeed_ < 9) {
                ThreadUtil.sleep(400 / (animationSpeed_) - 40);
            }
        }
        else  {
            double rand = MathUtil.RANDOM.nextDouble();
            //if (rand < (6.0/(double)(animationSpeed_ * animationSpeed_)))  {
            //    paintAll();
            //}
            if (rand < 1.0/animationSpeed_) {
                this.repaint(xpos-csized2, ypos-csized2, (2*cellSize), (2*cellSize));
            }
        }
    }

    /**
     * Render the Environment on the screen.
     */
    @Override
    public void paintComponent( Graphics g ) {

        super.paintComponent( g );
        renderer.render((Graphics2D)g, maze_);
    }
}
