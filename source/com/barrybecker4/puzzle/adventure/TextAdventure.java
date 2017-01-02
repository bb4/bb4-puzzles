// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.adventure;

import com.barrybecker4.puzzle.adventure.ui.GraphicalAdventure;
import org.w3c.dom.Document;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Run your own adventure story.
 * This version runs the adventure in text only mode.
 * @see GraphicalAdventure
 *
 * @author Barry Becker
 */
public final class TextAdventure {

    private TextAdventure() {}


    /**
     * Adventure application entrance point.
     */
    public static void main( String[] args ) throws IOException {

        Document document = Story.importStoryDocument(args);

        Story story = new Story(document);

        Scanner scanner = new Scanner(System.in).useDelimiter("\n");
        do {

            Scene currentScene = story.getCurrentScene();
            System.out.println(currentScene.print());

            int nextSceneIndex = getNextSceneIndex(currentScene, scanner);
            story.advanceScene(nextSceneIndex);

        } while (!story.isOver());

        scanner.close();
    }


    /**
     * Retrieve the selection from the player using the scanner.
     * @return the next scene to advance to.
     */
    private static int getNextSceneIndex(Scene scene, Scanner scanner) {

        int sceneIndex = -1;

        if (scene.hasChoices())  {

            int nextInt = -1;
            boolean valid = true;
            while (nextInt < 1) {
                try {
                    //do {} while (!scanner.hasNext());
                    nextInt = scanner.nextInt();
                } catch (InputMismatchException e) {
                    valid = false;
                    scanner.next();
                }
                if (nextInt < 1 || !valid) {
                    System.out.println("You must enter a number from among the choices.");
                }
            }
            sceneIndex = nextInt - 1;
        }
        return sceneIndex;
    }
}



