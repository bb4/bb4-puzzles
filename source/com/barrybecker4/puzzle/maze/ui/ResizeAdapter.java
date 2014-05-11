// Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.maze.ui;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Called whenever the maze window resizes. Avoids redrawing if the size did not change.
 * @author Barry Becker
 */
public class ResizeAdapter extends ComponentAdapter {

    private MazePanel mazePanel;
    private TopControlPanel topControls;
    private Dimension oldSize;

    ResizeAdapter(MazePanel panel, TopControlPanel topControls)  {
        this.mazePanel = panel;
        this.topControls = topControls;
    }

    @Override
    public void componentResized( ComponentEvent ce )  {

        // only resize if the dimensions have changed
        Dimension newSize = mazePanel.getSize();
        boolean changedSize = (oldSize == null ||
                oldSize.getWidth() != newSize.getWidth() ||
                oldSize.getHeight() != newSize.getHeight());

        if ( changedSize ) {
            oldSize = newSize;
            if (newSize.getWidth() > 0) {
                topControls.regenerate();
            }
        }
    }
}
