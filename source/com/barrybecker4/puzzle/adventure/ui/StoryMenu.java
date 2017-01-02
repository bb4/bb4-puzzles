// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.adventure.ui;

import com.barrybecker4.common.util.FileUtil;
import com.barrybecker4.puzzle.adventure.Story;
import com.barrybecker4.ui.file.ExtensionFileFilter;
import com.barrybecker4.ui.file.FileChooserUtil;

import javax.swing.BorderFactory;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * File menu for story application.
 * You can open, save, or edit a story file.
 *
 * @author Barry Becker
 */
class StoryMenu extends JMenu implements ActionListener  {

    private GraphicalAdventure storyApp_;

    private JMenuItem openItem_;
    private JMenuItem saveItem_;
    private JMenuItem editItem_;
    private JMenuItem exitItem_;

    private static final String EXT = "xml";

    /**
     * Game application constructor
     * @param storyApp the initially selected game.
     */
    public StoryMenu(GraphicalAdventure storyApp)  {
        super("Story");

        this.setBorder(BorderFactory.createEtchedBorder());

        storyApp_ = storyApp;
        setBorder(BorderFactory.createEtchedBorder());

        openItem_ =  createMenuItem("Open");
        saveItem_ =  createMenuItem("Save");
        editItem_ =  createMenuItem("Edit");
        exitItem_ = createMenuItem("Exit");
        add(openItem_);
        add(saveItem_);
        add(editItem_);
        add(exitItem_);
    }

    /**
     * called when the user has selected a different story file option.
     * @param e action event
     */
    public void actionPerformed( ActionEvent e ) {
         JMenuItem item = (JMenuItem) e.getSource();
         if (item == openItem_)  {
            openStory();
        }
        else if (item == saveItem_) {
            saveStory();
        }
        else if (item == editItem_) {
            storyApp_.editStory();
        }
        else if (item == exitItem_) {
            if (confirmExit())  {
                System.exit(0);
            }
        }
        else {
            assert false : "unexpected menuItem = "+ item.getName();
        }
    }

    /**
     * If there are modifications, confirm before exiting.
     * @return true if exiting was confirm or if no edit was made so confirm not needed.
     */
    private boolean confirmExit() {

        if (storyApp_.isStoryEdited())  {
            int choice = JOptionPane.showConfirmDialog(this,
                    "You have unsaved changes. Are you sure you want to exit?",
                    "Confirm Quit", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.NO_OPTION)
            {
                return false;
            }
        }
        return true;
    }

    /**
     * View the story that the user opens from the file chooser.
     */
    private void openStory() {
        File file = FileChooserUtil.getSelectedFileToOpen(EXT, getDefaultDir());
        if ( file != null)  {
            storyApp_.loadStory(file);
        }
    }

    /**
     * Save the current story to a file.
     */
    private void saveStory() {
        File file = FileChooserUtil.getSelectedFileToSave(EXT, getDefaultDir());
        if ( file != null) {
            // if it does not have the .sgf extension already then add it
            String fPath = file.getAbsolutePath();
            fPath = ExtensionFileFilter.addExtIfNeeded(fPath, EXT);
            storyApp_.saveStory(fPath);
        }
    }

    private File getDefaultDir() {
        String defaultDir = FileUtil.getHomeDir() + "source/" + Story.STORIES_ROOT;
        System.out.println("defaultDir = "+ defaultDir);
        return new File(defaultDir);
    }

    /**
     * Create a menu item.
     * @param name name of the menu item. The label.
     * @return the menu item to add.
     */
    JMenuItem createMenuItem(String name) {
        JMenuItem item = new JMenuItem(name);
        item.addActionListener(this);
        return item;
    }

}
