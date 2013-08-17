/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.common;

import java.awt.Graphics;

/**
 * Singleton class that takes a PieceList and renders it for the PuzzleViewer.
 * Having the renderer separate from the viewer helps to separate out the rendering logic
 * from other features of the PuzzleViewer.
 *
 * @author Barry Becker
 */
public interface PuzzleRenderer<P> {

    /**
     * This renders the current state of the Slider to the screen.
     */
    void render( Graphics g, P board, int width, int height );
}


