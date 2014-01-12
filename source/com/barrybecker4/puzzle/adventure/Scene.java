/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.adventure;

import com.barrybecker4.common.util.FileUtil;
import com.barrybecker4.common.xml.DomUtil;
import com.barrybecker4.sound.SoundUtil;
import com.barrybecker4.ui.util.GUIUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.awt.image.BufferedImage;
import java.net.URL;


/**
 * Every scene has a name, some text which describes the scene. and a list of
 * choices which the actor chooses from to decide what to do next.
 * There is a "Return to last scene" choice automatically appended to all list of choices.
 * A scene may also have an associated sound and image.
 *
 * @author Barry Becker
 */
public class Scene {

    private String name;
    private String text;
    private ChoiceList choices;
    private boolean isFirst;
    private URL soundURL_;
    private BufferedImage image_;

    /**
     * @param sceneNode  xml element to initialize from.
     * @param resourcePath where we can find images and sounds.
     */
    public Scene(Node sceneNode, String resourcePath, boolean isFirst) {
        String description = sceneNode.getFirstChild().getTextContent();

        this.isFirst = isFirst;
        commonInit(DomUtil.getAttribute(sceneNode, "name"),
                  description, new ChoiceList(sceneNode, isFirst()), resourcePath);
    }

    /**
     * Copy constructor.
     * @param scene the scene to initialize from.
     */
    public Scene(Scene scene) {
        this.name = scene.getName();
        this.text = scene.getText();
        this.image_ = scene.getImage();
        this.soundURL_ = scene.soundURL_;
        this.choices = new ChoiceList(scene);
        this.isFirst = scene.isFirst();
    }

    /**
     * Constructor for a simple new scene with no media or initial choices
     */
    public Scene(String name, String text, String resourcePath) {
        ChoiceList choices = new ChoiceList();
        commonInit(name, text, choices, resourcePath);
    }

    /**
     * @param document the document to which to append this scene as a child.
     */
    public void appendToDocument(Document document) {

        Element sceneElem = document.createElement("scene");
        sceneElem.setAttribute("name", getName());
        Element descElem = document.createElement("description");
        descElem.setTextContent(getText());
        sceneElem.appendChild(descElem);

        Element choicesElem =  document.createElement("choices");
        sceneElem.appendChild(choicesElem);
        for (int i = 0; i < getChoices().size(); i++) {
            Choice choice = getChoices().get(i);
            choicesElem.appendChild(choice.createElement(document));
        }

        Element rootElement = document.getDocumentElement();
        rootElement.appendChild(sceneElem);
    }

    private void commonInit(String name, String text, ChoiceList choices, String resourcePath) {
        this.name = name;
        this.text = text;
        this.choices = choices;
        loadResources(name, resourcePath);
    }

    private void loadResources(String name, String resourcePath) {
        try {
            System.out.println("Scene load resources path="+ resourcePath);
            String soundPath = resourcePath + "sounds/" + name + ".au";
            soundURL_ = FileUtil.getURL(soundPath, false);

            String imagePath = resourcePath + "images/" +name + ".jpg";
            System.out.println("reading image from " + imagePath);
            image_ = GUIUtil.getBufferedImage(imagePath);
        } catch (NoClassDefFoundError e) {
            System.err.println("You are trying to load sounds and images when only text scenes are supported. " +
                    "If you need this to work add the jai library to your classpath");
        }
    }

    /**
     * @return  choices that will lead to the next scene.
     */
    public ChoiceList getChoices() {
        return choices;
    }

    public void deleteChoice(int choice) {
        choices.remove(choice);
    }

    /**
     * @return the name of the scene
     */
    public String getName() {
        return name;
    }

    /**
     * When changing the name we must call sceneNameChanged on the listeners that are interested in the change.
     * @param name new scene name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return some text that describes the scene.
     */
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    /**
     * @param scene to see if parent
     * @return true if the specified scene is our immediate parent.
     */
    public boolean isParentOf(Scene scene) {
        String sName = scene.getName();
        return choices.isDestination(sName);
    }

    /**
     * @return image associated with this scene if there is one (else null)
     */
    public BufferedImage getImage() {
         return image_;
    }

    public boolean hasSound() {
        if (soundURL_ == null) return false;
        //File file = new File(soundURL_.getFile());
        //return file.exists();
        return true;
    }

    public void playSound() {
        if (hasSound()) {
             SoundUtil.playSound(soundURL_);
        }
    }

    boolean isFirst() {
        return isFirst;
    }

    /**
     *
     * @param choice navigate to the scene indicated by this choice.
     * @return the name of the next scene given the number of the choice.
     */
    public String getNextSceneName(int choice) {

        assert choice >= 0 || choice < choices.size();

        return choices.get(choice).getDestination();
    }

    /**
     * @return true if there are more than one coice for the user to select from.
     */
    public boolean hasChoices() {
        return choices != null;
    }

    /**
     * Prints what is missing if anything for this scene.
     * @return false if something is missing.
     */
    public boolean verifyMedia() {
        if (getImage() == null || !hasSound()) {
            System.out.print("scene: " + getName() );
            if (getImage() == null)
                System.out.print("  missing image");
            if (!hasSound())
                System.out.print("  missing sound" );
            System.out.println("");
            return false;
       }
        return true;
    }

    public String print() {
        StringBuilder buf = new StringBuilder();
        buf.append('\n').append(this.getText()).append('\n');

        if (choices != null) {
            int len = choices.size();
            for (int i=0; i < len; i++)  {
                buf.append(1 + i).append(") ").append(choices.get(i).getDescription()).append('\n');
            }
        }
        return buf.toString();
    }

    /**
     * @return the text and choices.
     */
    @Override
    public String toString() {

        return this.getName();
    }
}
