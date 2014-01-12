/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.adventure.ui.editor;

import com.barrybecker4.puzzle.adventure.Scene;
import com.barrybecker4.puzzle.adventure.ui.StoryPanel;
import com.barrybecker4.ui.components.GradientButton;
import com.barrybecker4.ui.components.ScrollingTextArea;
import com.barrybecker4.ui.components.TextInput;
import com.barrybecker4.ui.dialogs.ImagePreviewDialog;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Used to edit an individual scene.
 * @author Barry Becker
 */
class SceneEditorPanel extends JPanel implements ActionListener {

    /** The scene to edit */
    private Scene scene_;

    private GradientButton showImageButton_;
    private GradientButton playSoundButton_;

    private TextInput nameInput_ ;
    private ScrollingTextArea sceneText_;

    private String oldSceneName_;

    public static final int EDITOR_WIDTH = 900;

    /**
     * Constructor
     * @param scene the scene to populate the editor with.
     */
    public  SceneEditorPanel(Scene scene) {

        scene_ = scene;
        oldSceneName_ = scene_.getName();
        createUI();
    }


    void createUI() {
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(EDITOR_WIDTH, 600));

        this.setBorder(
                BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(), "Edit current Scene" ) );

        nameInput_= new TextInput("name:", scene_.getName());
        nameInput_.setColumns(50);

        sceneText_ = new ScrollingTextArea();
        sceneText_.setEditable(true);
        sceneText_.setFont(StoryPanel.TEXT_FONT);
        sceneText_.setText(scene_.getText());

        add(nameInput_, BorderLayout.NORTH);
        add(sceneText_, BorderLayout.CENTER);
        add(createMediaButtons(), BorderLayout.SOUTH);
    }

    /**
     * for sound and image and whatever else is associated with the scene.
     * @return image and sound buttons in a panel.
     */
    private JPanel createMediaButtons() {
        JPanel buttonPanel = new JPanel(new FlowLayout());

        showImageButton_ = new GradientButton("Image");
        showImageButton_.addActionListener(this);
        showImageButton_.setEnabled(scene_.getImage() != null);

        playSoundButton_ = new GradientButton("Sound");
        playSoundButton_.addActionListener(this);
        playSoundButton_.setEnabled(scene_.hasSound());

        buttonPanel.add(showImageButton_);
        buttonPanel.add(playSoundButton_);

        return buttonPanel;
    }

    public void actionPerformed( ActionEvent e )  {
        Object source = e.getSource();

        if ( source == showImageButton_ ) {
            ImagePreviewDialog imgPreviewDlg = new ImagePreviewDialog(scene_.getImage());
            imgPreviewDlg.showDialog();
        }
        else if ( source == playSoundButton_ ) {
            scene_.playSound();
        }
    }

    public boolean isSceneNameChanged() {
        return !oldSceneName_.equals(nameInput_.getValue());
    }

    public String getOldSceneName() {
        return oldSceneName_;
    }

    public Scene getEditedScene() {
        return scene_;
    }

    /**
     * Persist the scene changes to the story.
     */
    public void doSave() {
        if (isSceneNameChanged()) {
            scene_.setName(nameInput_.getValue());
        }
        scene_.setText(sceneText_.getText());
    }
}
