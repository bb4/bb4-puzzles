/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.adventure;

import com.barrybecker4.common.util.FileUtil;
import com.barrybecker4.common.xml.DomUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Run your own adventure story.
 * Just modify the script in SceneData and run.
 * This program is meant as a very simple example of how you can
 * approach creating a simple adventure game on a computer.
 *
 * There are many improvements that  I will leave as an exercise for reader (Brian I hope).
 * Next to each is a number which is the number of hours I expect it would take me to implement.
 *
 * 1) Keep track of the items that the player has. They initially start with about 10 things but then find/use
 *   items as their adventure progresses.
 * 2) Automatic fighting with monsters. We know the hit points and armor class of the player and monster.
 *  It should be a simple matter to have the compat automatically carried out in order to determine the winner and
 *  supbract hit point losses as appropriate. We can also take into other effects like disease or healing effects.
 *  The player should also be given the option to flee, or instigate other action during the melee.
 * 3) Add a graphical User Interface. We could have windows that pop up to show the players stats or item inventory.
 * 4) Make multi-player (hard)
 * 7) Allow the user to edit the scenes - live through the UI.
 * When editing a scene you are presented with a form that has all the
 * attributes for the scene including a dropdown for selecting which scenes navigate to it and
 * where you can navigate to from this scene. Save and load the xml that defines the game.
 * 8) This type of application could be used for more than just games. Tutorials or an expert system would
 * be other nice applications.
 * 9) Have probabalistic choices. For example, if you encounter a monster and choose to fight it, then
 * the outcome may be one of several different things. We can also influence the outcome by what sort of
 * items the player has.
 * 10) fix sound deploy in ant
 * 11) add means to edit the network of scene from within the application. Show all scene leading to and from
 * the current scene. Allow editing of scene properties and associating media.
 *
 * @author Barry Becker
 */
public class Story {

    /** title of the story */
    private String title;

    /** name of the stoy used as an identifier for by convention resolution of locations and things like that. e.g. ludlow. */
    private String name;

    /** person who created the story document. */
    private String author;

    /** date story was created. */
    private String date;

    /** Maps scene name to the scene. Preserves order of scenes. */
    private LinkedHashMap<String, Scene> sceneMap_;

    /** The scene where the user is now. */
    private Scene currentScene_;

    private static final String ROOT_ELEMENT = "script";

    private String resourcePath_;

    /**
     * A stack of currently visited scenes. There may be duplicates if you visit the same scene twice.
     */
    private List<Scene> visitedScenes_;

    /** all the stories need to be stored at this location */
    public static final String STORIES_ROOT = "com/barrybecker4/puzzle/adventure/stories/";


    /**
     * Construct an adventure given an xml document object
     * @param document containing the scene data
     */
    public Story(Document document) {
        Node root = document.getDocumentElement();
        title = DomUtil.getAttribute(root, "title");
        name =  DomUtil.getAttribute(root, "name");
        author = DomUtil.getAttribute(root, "author");
        date = DomUtil.getAttribute(root, "date");
        resourcePath_ = STORIES_ROOT + name + "/";
        NodeList children = root.getChildNodes();
        Scene[] scenes = new Scene[children.getLength()];
        for (int i=0; i < children.getLength(); i++) {
            scenes[i] = new Scene(children.item(i), resourcePath_, i==0);
        }
        initFromScenes(scenes);
    }

    /**
     * Copy constructor. Creates a deep copy.
     * @param story story to copy
     */
    public Story(Story story) {
        initializeFrom(story);
    }

    public void initializeFrom(Story story) {
        this.title = story.getTitle();
        this.name = story.name;
        this.author = story.author;
        this.date = story.date;
        this.resourcePath_ = story.resourcePath_;
        if (sceneMap_ == null) {
            sceneMap_ = createSceneMap(story.getSceneMap().size());
        }
        this.sceneMap_.clear();
        copySceneMap(story.getSceneMap());
        //this.currentScene_ = story.currentScene_;
        this.advanceToScene(story.getCurrentScene().getName());
        this.visitedScenes_ = new LinkedList<Scene>();
        this.visitedScenes_.addAll(story.visitedScenes_);
    }

    /** @return the title of the story */
    public String getTitle() {
        return title;
    }

    /** Return to the initial sceen from wherever they be now. */
    public void resetToFirstScene() {
        currentScene_ = sceneMap_.values().iterator().next();
    }

    /**
     * Write the story document back to xml.
     * @param destFileName file to write to.
     */
    public void saveStoryDocument(String destFileName) {
         try {
             Document document = createStoryDocument();
             DomUtil.writeXMLFile(destFileName, document, "script.dtd");
             System.out.println("done saving.");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    private LinkedHashMap<String, Scene> getSceneMap() {
        return sceneMap_;
    }

    /**
     * @return the story document based on the current state of the story.
     */
    private Document createStoryDocument() {

        Document document = DomUtil.createNewDocument();

        Element rootElement = document.createElement(ROOT_ELEMENT);
        rootElement.setAttribute("author", author);
        rootElement.setAttribute("name", name);
        rootElement.setAttribute("date", date);
        rootElement.setAttribute("title", title);
        document.appendChild(rootElement);

        for (String sceneName : sceneMap_.keySet()) {
            Scene scene = sceneMap_.get(sceneName);
            scene.appendToDocument(document);
        }

        return document;
    }

    /**
     * If args[0] does not have the name of the document to use, use a default.
     * @param args command line args (0 or 1 if name of xml doc is specified.)
     * @return the loaded Document that contains the adventure.
     */
    public static Document importStoryDocument(String[] args) {
        Document document;

        assert args != null;
        // default story
        URL url = FileUtil.getURL(STORIES_ROOT + "ludlow/ludlowScript.xml");
        if (args.length == 1) {
            System.out.println("args[0]=" + args[0]);
            url = FileUtil.getURL(STORIES_ROOT + args[0]);
        }
        else if (args.length > 1){
            System.out.println("importStoryDocument Args=" + Arrays.toString(args));
            url = FileUtil.getURL(STORIES_ROOT + args[1]);
        }
        //throw new IllegalStateException("bad url=" + url + "args="+ args);
        System.out.println("about to parse url=" + url + "\n story file location");
        document = DomUtil.parseXML(url);
        //DomUtil.printTree(document, 0);
        return document;
    }

    /**
     * Construct an adventure given a list of scenes.
     * @param scenes array of scenes to use in this story.
     */
    public Story(Scene[] scenes) {
        initFromScenes(scenes);
    }

    /** must be ordered */
    private LinkedHashMap<String, Scene> createSceneMap(int size) {
       return new LinkedHashMap<String, Scene>(size);
    }

    private void copySceneMap(LinkedHashMap<String, Scene> fromMap) {
        for (String sceneName : fromMap.keySet()) {
            Scene scene = fromMap.get(sceneName);
            // add deep copies of the scene.
            sceneMap_.put(sceneName, new Scene(scene));
        }
    }

    void initFromScenes(Scene[] scenes)  {
        sceneMap_ = createSceneMap(scenes.length);

        for (final Scene scene : scenes) {
            assert scene.getChoices() != null;
            sceneMap_.put(scene.getName(), scene);
        }
        verifyScenes();

        currentScene_ = scenes[0];
        visitedScenes_ = new LinkedList<Scene>();
    }

    public Scene getCurrentScene()  {
        return currentScene_;
    }

    public boolean isOver() {
       return getCurrentScene() == null;
    }

    /**
     * Advance the story to the next scene based on the specified choice
     * @param choice index of the selected choice.
     */
    public void advanceScene(int choice) {
        if (choice < 0) {
            currentScene_ = null;   // game over
            return;
        }

        String nextSceneName = currentScene_.getNextSceneName(choice);
        advanceToScene(nextSceneName);
    }

    /**
     * Jump to some arbitrary scene.
     * Not typically used. Should use advanceScene for normal navigation.
     * @param nextSceneName name of the scene to navigate to.
     */
    public void advanceToScene(String nextSceneName) {

        if (nextSceneName != null) {
            if (currentScene_ != null)
                visitedScenes_.add(currentScene_);
            currentScene_ = sceneMap_.get( nextSceneName );
            assert (currentScene_ != null)  : "Could not find a scene named '"+ nextSceneName+"'.";
        }
    }

    /**
     * @return a list of all the scenes that led to the current scene.
     */
    public List<Scene> getParentScenes()  {
        List<Scene> parentScenes = new ArrayList<Scene>();
        // loop through all the scenes, and if any of them have us as a child, add to the list
        for (String sceneName : sceneMap_.keySet()) {
            Scene s = sceneMap_.get(sceneName);
            if (s.isParentOf(currentScene_)) {
                parentScenes.add(s);
            }
        }
        return parentScenes;
    }

    /**
     *
     * @param newSceneName name of the new scene. It may or may not exist already.
     * @param choiceDescription  text describing what you will do to go to the destination.
     */
    public void addChoiceToCurrentScene(String newSceneName, String choiceDescription) {
        // if we do not already have this scene, we need to create it.
        if (!sceneMap_.containsKey(newSceneName)) {
            Scene newScene = new Scene(newSceneName, " --- describe the scene here ---", resourcePath_);
            sceneMap_.put(newSceneName, newScene);
        }
        this.getCurrentScene().getChoices().add( new Choice(choiceDescription, newSceneName));
    }

    /**
     * @return a list of all the existing scenes that we could navigate to
     *   that are not already included in the current scene's list of choices.
     */
    public List<String> getCandidateDestinationSceneNames() {
        List<String> candidateSceneNames = new ArrayList<String>();

         for (String sceneName : sceneMap_.keySet()) {
            if (!getCurrentScene().getChoices().isDestination(sceneName)) {
                candidateSceneNames.add(sceneName);
            }
        }
        return candidateSceneNames;
    }

    public List<String> getAllSceneNames() {
        return new ArrayList<String>(sceneMap_.keySet());
    }

    /**
     * make sure the set of scenes in internally consistent.
     */
    private void verifyScenes() {
        for (Scene scene : sceneMap_.values()) {
            scene.verifyMedia();

            for (Choice choice : scene.getChoices())  {
                String dest = choice.getDestination();
                if (dest != null  && sceneMap_.get(choice.getDestination()) == null) {
                    assert false : "No scene named " + choice.getDestination() + " desc="+ choice.getDescription();
                }
            }
        }
    }

    /**
     * Since the name of one of the scenes has changed we need to update the sceneMap.
     */
    public void sceneNameChanged(String oldSceneName, String newSceneName) {
        Scene changedScene = sceneMap_.remove(oldSceneName);
        //System.out.println("oldScene name=" + oldSceneName +"  newSceneName="+ newSceneName+"  changedScene=" + changedScene.getName());
        sceneMap_.put(newSceneName, changedScene);
        // also need to update the references to named scenes in the choices.
        for (String sceneName : sceneMap_.keySet()) {
            sceneMap_.get(sceneName).getChoices().sceneNameChanged(oldSceneName, newSceneName);
        }
    }
}

