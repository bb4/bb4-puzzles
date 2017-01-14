// Copyright by Barry G. Becker, 2000-2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.maze1;

import com.barrybecker4.common.concurrency.ThreadUtil;
import com.barrybecker4.common.geometry.IntLocation;
import com.barrybecker4.common.geometry.Location;
import com.barrybecker4.common.math.MathUtil;
import com.barrybecker4.puzzle.maze1.model.GenState;
import com.barrybecker4.puzzle.maze1.model.MazeCell;
import com.barrybecker4.puzzle.maze1.model.MazeModel;
import com.barrybecker4.puzzle.maze1.model.Probabilities;
import com.barrybecker4.puzzle.maze1.model.StateStack;
import com.barrybecker4.puzzle.maze1.ui.MazePanel;

/**
 *  Program to automatically generate a Maze.
 *  Motivation: Get my son, Brian, to excel at Kumon by trying these mazes with a pencil.
 *  this is the global space containing all the cells, walls, and particles.
 *  Assumes an M*N grid of cells.
 *  X axis increases to the left.
 *  Y axis increases downwards to be consistent with java graphics.
 *
 *  @author Barry Becker
 */
public class MazeGenerator {

    /** if the animation speed is less than this things will slow down a lot */
    private static final int SLOW_SPEED_THRESH = 10;

    private MazeModel maze;
    private MazePanel panel;
    private StateStack stack;

    /** put the stop point at the maximum search depth. */
    private int maxDepth = 0;

    /** set this to true to get the generator to stop generating */
    private boolean interrupted;

    /** Constructor */
    public  MazeGenerator(MazePanel panel) {
        this.panel = panel;
        interrupted = false;
        maze = this.panel.getMaze();
    }

    /**
     * generate the maze.
     */
    public void generate(double forwardProb, double leftProb, double rightProb) {

        maxDepth = 0;

        Probabilities probs = new Probabilities(forwardProb, leftProb, rightProb);
        stack = new StateStack(probs);

        search();
        panel.repaint();
    }

    /**
     * Do a depth first search (without recursion) of the grid space to determine the graph.
     * Used to use a recursive algorithm but it was slower and would give stack overflow
     * exceptions even for moderately sized mazes.
     */
    public void search() {
        stack.clear();

        Location currentPosition = maze.getStartPosition();
        MazeCell currentCell = maze.getCell(currentPosition);
        currentCell.visited = true;

        // push the initial moves
        stack.pushMoves(currentPosition, new IntLocation(0, 1), 0);

        while ( !stack.isEmpty() && !interrupted ) {
            currentCell = findNextCell(currentCell);
        }
    }

    /** Stop current work and clear the search stack of states. */
    public void interrupt() {
        interrupted = true;
        if (stack != null) {
            stack.clear();
        }
    }

    /** Find the next cell to visit, given the last cell */
    private MazeCell findNextCell(MazeCell lastCell) {

        boolean moved = false;

        Location currentPosition;
        MazeCell nextCell;
        int depth;
        Location dir;

        do {
            GenState state = stack.remove(0);  // pop

            currentPosition = state.getPosition();
            dir = state.getRelativeMovement();
            depth = state.getDepth();

            if ( depth > maxDepth) {
                maxDepth = depth;
                maze.setStopPosition(currentPosition);
            }
            if ( depth > lastCell.getDepth() )  {
                lastCell.setDepth(depth);
            }

            MazeCell currentCell = maze.getCell(currentPosition);
            Location nextPosition = currentCell.getNextPosition(currentPosition, dir);
            nextCell = maze.getCell(nextPosition);

            if (nextCell.visited) {
                addWall(currentCell, dir, nextCell);
            }
            else {
                moved = true;
                nextCell.visited = true;
                currentPosition = nextPosition;
            }

            refresh();
        } while ( !moved && !stack.isEmpty() && !interrupted );

        refresh();
        // now at a new location
        if ( moved && !interrupted)  {
            stack.pushMoves(currentPosition, dir, ++depth);
        }
        return nextCell;
    }

    /** Place a wall when the path is blocked */
    private void addWall(MazeCell currentCell, Location dir, MazeCell nextCell) {
        // add a wall
        if ( dir.getX() == 1 ) {         // east
            currentCell.eastWall = true;
        } else if ( dir.getY() == 1 ) {  // south
            currentCell.southWall = true;
        } else if ( dir.getX() == -1 )  { // west
            nextCell.eastWall = true;
        } else if ( dir.getY() == -1 ) {  // north
            nextCell.southWall = true;
        }
    }


    /** this can be really slow if you do a refresh every time */
    private void refresh() {
        int speed = panel.getAnimationSpeed();
        if (MathUtil.RANDOM.nextDouble() < (2.0 / speed)) {
            panel.paintAll();
            if (speed < SLOW_SPEED_THRESH) {
                int diff = SLOW_SPEED_THRESH - speed;
                ThreadUtil.sleep(diff * diff * 6);
            }
        }
    }

}
