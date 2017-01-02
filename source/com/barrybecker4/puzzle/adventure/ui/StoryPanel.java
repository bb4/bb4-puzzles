// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.adventure.ui;

import com.barrybecker4.puzzle.adventure.Story;
import com.barrybecker4.ui.components.ImageListPanel;

import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

/**
 * This panel is responsible for drawing the Text describing the current scene.
 * @author Barry Becker
 */
public class StoryPanel extends JSplitPane {

    private Story story_;

    public static final Font TEXT_FONT = new Font("Courier", Font.PLAIN, 12);
    private static final int INITAL_LEFT_WIDTH = 600;

    private JTextArea textArea_;
    private ImageListPanel imagePanel_;


    /**
     * Constructor
     * @param story story for which to show text and image in the panel.
     */
    public StoryPanel(Story story) {

        story_ = story;

        setContinuousLayout(true);
        setDividerLocation(INITAL_LEFT_WIDTH);

        textArea_ = createTextArea();
        imagePanel_ = createImagePanel();

        add( imagePanel_, JSplitPane.RIGHT);
        add(textArea_, JSplitPane.LEFT);
    }

    private JTextArea createTextArea() {
        JTextArea textArea = new JTextArea();
        textArea.setFont(TEXT_FONT);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setMinimumSize(new Dimension(INITAL_LEFT_WIDTH/2, 300));
        return textArea;
    }

    private ImageListPanel createImagePanel() {
        ImageListPanel imagePanel = new ImageListPanel();
        imagePanel.setMaxNumSelections(1);
        imagePanel.setPreferredSize(new Dimension(700, 200));
        return imagePanel;
    }

    /**
     * Render the Environment on the screen.
     */
    @Override
    public void paintComponent( Graphics g ) {
        super.paintComponent( g );
        textArea_.setText(story_.getCurrentScene().getText());
        imagePanel_.setSingleImage(story_.getCurrentScene().getImage());
    }
}
