/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.adventure.ui;

import com.barrybecker4.common.xml.DomUtil;
import com.barrybecker4.puzzle.adventure.Story;
import com.barrybecker4.puzzle.adventure.TextAdventure;
import com.barrybecker4.puzzle.adventure.ui.editor.StoryEditorDialog;
import com.barrybecker4.ui.application.ApplicationApplet;
import com.barrybecker4.ui.dialogs.PasswordDialog;
import com.barrybecker4.ui.util.GUIUtil;
import org.w3c.dom.Document;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

/**
 * Run your own adventure story.
 * This version runs the adventure in Graphical mode (with images and sound).
 * @see TextAdventure
 *
 * @author Barry Becker
 */
public final class GraphicalAdventure extends ApplicationApplet
                                      implements SceneChangeListener {

    /**
     * The top secret password - don't tell anyone.
     * This could be Base64 encoded or encrypted to make more secure.
     */
    private static final String PASSWORD = "ludlow"; //NON-NLS

    private Story story_;
    private ChoicePanel choicePanel_ = null;
    private JPanel mainPanel_;
    private boolean storyEdited_ = false;

    public GraphicalAdventure() {
        this(new String[] {}, getDefaultStory());
    }

    /**
     * Constructor.
     * @param story initial story to show.
     */
    public GraphicalAdventure(String[] args, Story story) {
        super(args);
        story_ = story;
        JFrame frame = GUIUtil.showApplet( this);

        StoryMenu storyMenu = new StoryMenu(this);

        JMenuBar menubar = new JMenuBar();
        menubar.add(storyMenu);

        frame.setJMenuBar(menubar);
        frame.invalidate();
        frame.validate();
    }

    public static Story getDefaultStory() {
        Document document = Story.importStoryDocument(new String[]{});
        return new Story(document);
    }

    @Override
    public String getName() {
        return story_.getTitle();
    }

    /**
     * Build the user interface with parameter input controls at the top.
     */
    @Override
    protected JPanel createMainPanel() {
        mainPanel_ = new JPanel();
        mainPanel_.setLayout( new BorderLayout() );

        setStory(story_);

        return mainPanel_;
    }

    /**
     * If a new story is loaded, call this method to update the ui.
     * @param story new story to present.
     */
    public void setStory(Story story) {
        if (story == null) return;

        mainPanel_.removeAll();
        story_ = story;

        StoryPanel storyPanel = new StoryPanel(story_);

        // setup for initial scene
        choicePanel_ = new ChoicePanel(story_.getCurrentScene().getChoices());
        story_.getCurrentScene().playSound();

        choicePanel_.addSceneChangeListener(this);

        mainPanel_.add( storyPanel, BorderLayout.CENTER );
        mainPanel_.add( choicePanel_, BorderLayout.SOUTH );
        refresh();
    }


    public Story getStory() {
        return story_;
    }

    void refresh() {
        mainPanel_.invalidate();
        mainPanel_.validate();
        mainPanel_.repaint();
    }

    /**
     * Allow user to edit the current story if they know the password.
     */
    public void editStory() {
        // show password dialog.
        PasswordDialog pwDlg = new PasswordDialog(PASSWORD);
        boolean canceled = pwDlg.showDialog();
        if ( canceled ) return;

        StoryEditorDialog storyEditor = new StoryEditorDialog(story_);
        boolean editingCanceled = storyEditor.showDialog();
        if (!editingCanceled) {
            // show the edited version.
            story_.initializeFrom(storyEditor.getEditedStory());
            story_.resetToFirstScene();
            setStory(story_);
            storyEdited_ = true;
        }
    }

    public boolean isStoryEdited() {
        return storyEdited_;
    }

    public void loadStory(File file) {
         Story story = new Story(importStoryDocument(file));
         setStory(story);
    }

    /**
     * @param file name of the xml document to import.
     * @return the imported story xml document.
     */
    private static Document importStoryDocument(File file) {
        Document document = null;
        // first try to load it as a file. If that doesn't work, try as a URL.
        if (file.exists()) {
            document = DomUtil.parseXMLFile(file);
        }
        return document;
    }

    /**
     * @param fPath fully qualified filename and path to save to.
     */
    public void saveStory(String fPath) {
        getStory().saveStoryDocument(fPath);
        storyEdited_ = false;
    }

    /**
     * called when a button is pressed.
     */
    @Override
    public void sceneChanged( int selectedChoiceIndex ) {
        story_.advanceScene(selectedChoiceIndex);
        refresh();
        choicePanel_.setChoices(story_.getCurrentScene().getChoices());
        story_.getCurrentScene().playSound();
    }


    @Override
    public Dimension getSize() {
        return new Dimension(1000, 700);
    }

    /**
     * Entry point for applet.
     */
    @Override
    public void init()  {
        super.init();
        if (story_ == null)  {
            Document document = Story.importStoryDocument(new String[]{});
            Story story = new Story(document);
            setStory(story);
        }
    }

    /**
     * Graphical Adventure application entrance point.
     */
    public static void main( String[] args ) throws IOException {


        new GraphicalAdventure(args, getDefaultStory());
    }
}

