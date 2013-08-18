/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.common.ui;

import com.barrybecker4.common.format.FormatUtil;
import com.barrybecker4.puzzle.common.Refreshable;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

/**
 * Shows the current state of the puzzle in the ui.
 * @author Barry Becker
 */
public abstract class PuzzleViewer<P, M> extends JPanel implements Refreshable<P, M> {

    public static final int MARGIN = 15;
    private static final Color BACKGROUND_COLOR = new Color(235, 235, 240);

    protected P board_;
    protected String status_ = "";
    protected long numTries_;

    long totalMem_ = Runtime.getRuntime().totalMemory();
    long freeMem_ = Runtime.getRuntime().freeMemory();

    /**
     * Creates a new instance of PuzzleViewer
     */
    public PuzzleViewer() {}


    @Override
    public void refresh(P board, long numTries) {
        status_ = createStatusMessage(numTries);
        simpleRefresh(board, numTries);
    }

    @Override
    public void finalRefresh(List<M> path, P position, long numTries, long millis) {

        float time = (float)millis / 1000.0f;
        status_ = "Did not find solution.";
        if (path != null)  {
            status_ = "Found solution with "+ path.size() + " steps in " + FormatUtil.formatNumber(time) + " seconds. "
                    + createStatusMessage(numTries);

        }
        System.out.println(status_);
        if (position != null) {
            simpleRefresh(position, numTries);
        }
        System.gc();
    }

    protected void simpleRefresh(P board, long numTries) {
        board_ = board;
        numTries_ = numTries;
        repaint();
    }

    /**
     * @param numTries number of attempts to solve so far.
     * @return some text to show in the status bar.
     */
    protected String createStatusMessage(long numTries) {
        String msg = "\nNumber of tries :" + FormatUtil.formatNumber(numTries);
        // I think this might be an expensive operation so don't do it every time
        if (Math.random() <.05) {
            totalMem_ = Runtime.getRuntime().totalMemory()/1000;
            freeMem_ = Runtime.getRuntime().freeMemory()/1000;
        }
        msg += "    Memory used = "+ FormatUtil.formatNumber(totalMem_ - freeMem_) +"k";
        return msg;
    }

    /**
     * This renders the current state of the puzzle to the screen.
     */
    @Override
    protected void paintComponent( Graphics g ) {
        super.paintComponent(g);
        clearBackground(g);
        drawStatus(g, status_, MARGIN, MARGIN);
    }

    private void clearBackground(Graphics g) {
        int width = this.getWidth();
        int height = this.getHeight();
        // erase what's there and redraw.
        g.clearRect( 0, 0, width, height );
        g.setColor( BACKGROUND_COLOR );
        g.fillRect( 0, 0, width, height );
    }

    protected void drawStatus(Graphics g, String status, int x, int y) {
        String[] lines = status.split("\n");
        int offset = 0;
        g.setColor( Color.black );
        for (String line : lines) {
            offset += 14;
            g.drawString( line, x, y + offset );
            //System.out.println("drawing " + line);
        }
    }

}
