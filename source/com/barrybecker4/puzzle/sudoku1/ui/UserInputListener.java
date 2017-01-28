// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku1.ui;

import com.barrybecker4.common.geometry.Location;
import com.barrybecker4.puzzle.sudoku1.model.ValueConverter;
import com.barrybecker4.puzzle.sudoku1.model.board.Board;
import com.barrybecker4.puzzle.sudoku1.model.board.Cell;

import javax.swing.JOptionPane;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Draws the current best solution to the puzzle in a panel.
 * The view in the model-view-controller pattern.
 *
 * @author Barry Becker
 */
public final class UserInputListener implements MouseListener, KeyListener {

    private CellLocator locator;
    private Location currentCellLocation;

    private UserEnteredValues userEnteredValues;
    private List<RepaintListener> listeners;

    /**
     * Constructor. Pass in data for initial Sudoku problem.
     */
    UserInputListener(CellLocator locator) {
        this.locator = locator;
        listeners = new ArrayList<RepaintListener>();
        clear();
    }

    public void clear() {
        userEnteredValues = new UserEnteredValues();
    }

    public Location getCurrentCellLocation() {
        return currentCellLocation;
    }

    public UserEnteredValues getUserEnteredValues() {
        return userEnteredValues;
    }

    public void mouseClicked(MouseEvent e) {
        Location location = locator.getCellCoordinates(e.getPoint());
        setCurrentLocation(location);
    }

    public void useCorrectEntriesAsOriginal(Board board) {
        for (Location location : userEnteredValues.keySet()) {
            UserValue value = userEnteredValues.get(location);
            if (value.isValid())  {
                board.getCell(location).setOriginalValue(value.getValue());
            }
        }
    }

    /**
     * Handle keyboard input.
     * @param event the key event corresponding to key pressed.
     */
    public void keyPressed(KeyEvent event) {

        char key = event.getKeyChar();
        int keyCode = event.getKeyCode();

        if (keyCode == KeyEvent.VK_ENTER)  {
            requestValidation();
        }
        else if (isArrowKey(keyCode) ) {
            handleArrowKey(keyCode);
        }
        else if (!isOriginalCell(currentCellLocation)){
            // only enter the value if its not already a fixed/correct value
            handleValueEntry(key);
        }
    }

    private boolean isOriginalCell(Location location) {
        return locator.getBoard().getCell(location).isOriginal();
    }

    private boolean isArrowKey(int keyCode) {
        return keyCode >= KeyEvent.VK_LEFT && keyCode <= KeyEvent.VK_DOWN
               || keyCode >= KeyEvent.VK_KP_UP && keyCode <= KeyEvent.VK_KP_DOWN;
    }

    private void handleArrowKey(int keyCode) {
        Location location = null;
        switch (keyCode) {
            case KeyEvent.VK_LEFT :
            case KeyEvent.VK_KP_LEFT : location = currentCellLocation.incrementOnCopy(0, -1);
                break;
            case KeyEvent.VK_RIGHT :
            case KeyEvent.VK_KP_RIGHT : location = currentCellLocation.incrementOnCopy(0, 1);
                break;
            case KeyEvent.VK_UP :
            case KeyEvent.VK_KP_UP : location = currentCellLocation.incrementOnCopy(-1, 0);
                break;
            case KeyEvent.VK_DOWN :
            case KeyEvent.VK_KP_DOWN : location = currentCellLocation.incrementOnCopy(1, 0);
                break;
        }

        setCurrentLocation(location);
    }

    /**
     * Set the current location if it valid, and notify renderer of change.
     * @param location
     */
    private void setCurrentLocation(Location location) {
       if (isValid(location)) {
            currentCellLocation = location;
            notifyCellSelected(currentCellLocation);
        }
    }

    private boolean isValid(Location location) {

        int n = locator.getBoard().getEdgeLength();
        return (location != null
             && location.getRow() >=0 && location.getRow() < n
             && location.getCol() >=0 && location.getCol() < n);
    }


    private void handleValueEntry(char key) {
        try {
            int value = ValueConverter.getValue(key, locator.getBoard().getEdgeLength());
            UserValue userValue = new UserValue(currentCellLocation, value);
            userEnteredValues.put(currentCellLocation, userValue);
            notifyValueEntered();
        }
        catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
        }
    }

    public void validateValues(Board solvedPuzzle) {

        for (Location location : userEnteredValues.keySet())  {
            assert location != null;
            UserValue userValue = userEnteredValues.get(location);

            Cell cell = solvedPuzzle.getCell(location.getRow(), location.getCol());
            boolean valid = userValue.getValue() == cell.getValue();
            userValue.setValid(valid);
        }
    }


    public void addRepaintListener(RepaintListener listener) {
         listeners.add(listener);
    }

    public void removeRepaintListener(RepaintListener listener) {
        listeners.remove(listener);
    }

    private void notifyValueEntered() {
        for (RepaintListener listener : listeners) {
            listener.valueEntered();
        }
    }

    private void notifyCellSelected(Location location) {
        for (RepaintListener listener : listeners) {
            listener.cellSelected(location);
        }
    }

    private void requestValidation() {
        for (RepaintListener listener : listeners) {
            listener.requestValidation();
        }
    }

    public void keyTyped(KeyEvent event) {}
    public void keyReleased(KeyEvent e) {}

    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}

