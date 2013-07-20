/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.adventure.ui;


/**
 * Called when you advance to a different scene of the story.
 * @author Barry Becker
 */
public interface SceneChangeListener {

    /**
     * @param selectionIndex the selected choice leading to the next scene in the story.
     */
    void sceneChanged(int selectionIndex);
}
