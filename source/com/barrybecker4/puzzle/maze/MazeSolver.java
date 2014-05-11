/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.maze;

import com.barrybecker4.common.geometry.IntLocation;
import com.barrybecker4.common.geometry.Location;
import com.barrybecker4.puzzle.maze.model.GenState;
import com.barrybecker4.puzzle.maze.model.MazeCell;
import com.barrybecker4.puzzle.maze.model.MazeModel;
import com.barrybecker4.puzzle.maze.model.StateStack;
import com.barrybecker4.puzzle.maze.ui.MazePanel;

import java.util.LinkedList;
import java.util.List;

/**
 * Solves a maze.
 * @author Barry Becker
 */
public class MazeSolver {

    private MazePanel panel_;
    private MazeModel maze;
    private StateStack stack;
    private boolean isWorking;
    private boolean interrupted;

    /** Constructor */
    public MazeSolver(MazePanel panel) {
        panel_ = panel;
        maze = panel_.getMaze();
        stack = new StateStack();
        isWorking = false;
        interrupted = false;
    }

    /**
     * do a depth first search (without recursion) of the grid space to determine the solution to the maze.
     * very similar to search (see MazeGenerator), but now we are solving it.
     */
    public void solve() {

        isWorking = true;
        maze.unvisitAll();
        stack.clear();

        findSolution();

        panel_.paintAll();
        isWorking = false;
    }

    private void findSolution() {

        // Keep track of our current path. We may need to backtrack along it if we encounter a dead end.
        List<Location> solutionPath = new LinkedList<Location>();

        Location currentPosition = maze.getStartPosition();
        MazeCell currentCell = maze.getCell(currentPosition);

        // push the initial moves
        stack.pushMoves( currentPosition, new IntLocation(0, 1), 1);
        panel_.paintAll();

        Location dir;
        int depth;
        boolean solved = false;

        // while there are still paths to try and we have not yet encountered the finish
        while ( !stack.isEmpty() && !solved && !interrupted) {

            GenState state = stack.remove(0);  // pop

            currentPosition = state.getPosition();
            solutionPath.add(0, currentPosition);

            if (currentPosition.equals(maze.getStopPosition()))  {
                solved = true;
            }

            dir = state.getDirection();
            depth = state.getDepth();
            if ( depth > currentCell.getDepth() ) {
                currentCell.setDepth(depth);
            }

            currentCell = maze.getCell(currentPosition);
            Location nextPosition = currentCell.getNextPosition(currentPosition,  dir);

            search(solutionPath, currentCell, dir, depth, nextPosition);
        }
    }

    public boolean isWorking() {
        return isWorking;
    }

    /** Stop current work and clear the search stack of states. */
    public void interrupt() {
        interrupted = true;
        stack.clear();
    }

    private void search(List<Location> solutionPath, MazeCell currentCell,
                        Location dir, int depth, Location nextPosition) {
        MazeCell nextCell = maze.getCell(nextPosition);
        boolean eastBlocked = dir.getX() ==  1 && currentCell.eastWall;
        boolean westBlocked =  dir.getX() == -1 && nextCell.eastWall;
        boolean southBlocked = dir.getY() ==  1 && currentCell.southWall;
        boolean northBlocked = dir.getY() == -1 && nextCell.southWall;

        boolean pathBlocked = eastBlocked || westBlocked || southBlocked || northBlocked;

        if (!pathBlocked)  {
            advanceToNextCell(currentCell, dir, depth, nextPosition, nextCell);
        }
        else {
            backTrack(solutionPath);
        }
    }


    private void advanceToNextCell(MazeCell currentCell, Location dir, int depth,
                                   Location nextPosition, MazeCell nextCell) {
        Location currentPosition;
        if ( dir.getX() == 1 ) {// east
            currentCell.eastPath = true;
            nextCell.westPath = true;
        }
        else if ( dir.getY() == 1 ) { // south
            currentCell.southPath = true;
            nextCell.northPath = true;
        }
        else if ( dir.getX() == -1 ) {  // west
            currentCell.westPath = true;
            nextCell.eastPath = true;
        }
        else if ( dir.getY() == -1 )  { // north
            currentCell.northPath = true;
            nextCell.southPath = true;
        }

        nextCell.visited = true;
        currentPosition = nextPosition;

        // now at a new location
        stack.pushMoves(currentPosition, dir, ++depth);
        panel_.paintCell(currentPosition);
    }

    /**
     * Back up to the next path that will be tried
     * @param solutionPath list of locations leading ot the solution.
     */
    private void backTrack(List<Location> solutionPath) {
        GenState lastState = stack.get(0);

        Location pos;
        do {
            pos =  solutionPath.remove(0);
            MazeCell cell = maze.getCell(pos);
            cell.clearPath();
        } while ( pos != lastState.getPosition());
    }
}
