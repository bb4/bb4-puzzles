/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.sudoku.ui;

import com.barrybecker4.common.geometry.Location;
import com.barrybecker4.puzzle.sudoku.model.board.Board;

import java.awt.Point;

/**
 * Locates cell coordinates given a point location on the screen.
 * @author Barry Becker
 */
public interface CellLocator {

    Board getBoard();

    Location getCellCoordinates(Point point);
}
