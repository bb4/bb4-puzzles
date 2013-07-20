/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.adventure.ui.editor;

import com.barrybecker4.puzzle.adventure.Scene;
import com.barrybecker4.puzzle.adventure.Story;
import com.barrybecker4.ui.components.GradientButton;
import com.barrybecker4.ui.dialogs.AbstractDialog;
import com.barrybecker4.ui.table.TableButtonListener;
import com.barrybecker4.ui.util.GUIUtil;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Allows editing of a story in a separate dialog.
 * You can add/remove/reorder/change scenes in the story.
 * @author Barry Becker
 */
public class StoryEditorDialog extends AbstractDialog
                               implements ActionListener,
                                          TableButtonListener,
                                          ListSelectionListener {
    /** The story to edit */
    private Story story_;

    private SceneEditorPanel sceneEditor;

    private static final Font INSTRUCTION_FONT = new Font(GUIUtil.DEFAULT_FONT_FAMILY, Font.PLAIN, 10);

    private List<Scene>  parentScenes_;
    private ChildTable  childTable_;

    /** click this when done editing the scene. */
    private GradientButton okButton_ = new GradientButton();

    // for adding/removing/reordering scene choice destinations
    private GradientButton addButton_ = new GradientButton();
    private GradientButton removeButton_ = new GradientButton();
    private GradientButton moveUpButton_ = new GradientButton();
    private GradientButton moveDownButton_ = new GradientButton();

    private JComboBox sceneSelector_;

    /** location for images. */
    private static final String IMAGE_PATH = "com/barrybecker4/puzzle/adventure/ui/images/";

    private int selectedChildRow_ = -1;

    /**
     * Constructor
     * @param story creates a copy of this in case we cancel.
     */
    public StoryEditorDialog(Story story) {

        story_ = new Story(story);

        this.setResizable(true);
        setTitle("Story Editor");
        this.setModal( true );
        showContent();
    }

    @Override
    protected JComponent createDialogContent() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setPreferredSize(new Dimension(SceneEditorPanel.EDITOR_WIDTH, 700));
        JPanel editingPane = createEditingPane();
        JLabel title = new JLabel("Navigate through the scene heirarchy and change values for scenes.");
        title.setBorder(BorderFactory.createEmptyBorder(5, 4, 20, 4));
        title.setFont(INSTRUCTION_FONT);

        mainPanel.add(title, BorderLayout.NORTH);
        mainPanel.add(editingPane, BorderLayout.CENTER);
        mainPanel.add(createButtonsPanel(), BorderLayout.SOUTH);

        return mainPanel;
    }

    /**
     * Parent table on top.
     * Scene editor in the middle.
     * Child options on the bottom.
     * @return the panel that holds the story editor controls
     */
    private JPanel createEditingPane() {
        JPanel editingPane = new JPanel(new BorderLayout());

        editingPane.add(createParentTablePanel(), BorderLayout.NORTH);
        editingPane.add(createSceneEditingPanel(), BorderLayout.CENTER);

        return editingPane;
    }

    /**
     * @return  table holding list of scenes that lead to the currently edited scene.
     */
    private JComponent createParentTablePanel() {
        JPanel parentContainer = new JPanel(new BorderLayout());
        parentScenes_ = story_.getParentScenes();
        ParentTable parentTable = new ParentTable(parentScenes_, this);

        JPanel tableHolder = new JPanel();
        tableHolder.setMaximumSize(new Dimension(500, 300));
        parentContainer.setBorder(
                BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(), "Parent Scenes" ));

        parentContainer.add(new JScrollPane(parentTable.getTable()), BorderLayout.WEST);

        parentContainer.setPreferredSize(new Dimension(SceneEditorPanel.WIDTH, 120));
        return parentContainer;
    }

    private JPanel createSceneEditingPanel() {
        JPanel container = new JPanel(new BorderLayout());

        sceneEditor = new SceneEditorPanel(story_.getCurrentScene());

        container.add(sceneEditor, BorderLayout.CENTER);
        container.add(createChildTablePanel(), BorderLayout.SOUTH);

        return container;
    }

    /**
     * @return  table of child scene choices.
     */
    private JComponent createChildTablePanel() {
        JPanel childContainer = new JPanel(new BorderLayout());

        childTable_ = new ChildTable(story_.getCurrentScene().getChoices(), this);
        childTable_.addListSelectionListener(this);

        childContainer.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(), "Choices (to navigate to child scenes)" ) );

        childContainer.add(new JScrollPane(childTable_.getTable()), BorderLayout.CENTER);
        childContainer.add(createChildRowEditButtons(), BorderLayout.SOUTH);

        childContainer.setPreferredSize(new Dimension(SceneEditorPanel.WIDTH, 240));
        return childContainer;
    }

    JPanel createChildRowEditButtons() {
        JPanel leftButtonsPanel = new JPanel( new FlowLayout() );

        initBottomButton( addButton_, "Add",
                "Add a new child scene choice to the current scene before the selected position.");
        initBottomButton( removeButton_, "Remove",
                "Remove the child scene at the selected position.");
        initBottomButton( moveUpButton_, "Up",
                "Move the current scene up one row.");
        initBottomButton( moveDownButton_, "Down",
                "Move the current scene down one row.");

        addButton_.setIcon(GUIUtil.getIcon(IMAGE_PATH + "plus.gif"));
        removeButton_.setIcon(GUIUtil.getIcon(IMAGE_PATH + "minus.gif"));
        moveUpButton_.setIcon(GUIUtil.getIcon(IMAGE_PATH + "up_arrow.png"));
        moveDownButton_.setIcon(GUIUtil.getIcon(IMAGE_PATH + "down_arrow.png"));

        removeButton_.setEnabled(false);
        moveUpButton_.setEnabled(false);
        moveDownButton_.setEnabled(false);

        leftButtonsPanel.add( addButton_ );
        leftButtonsPanel.add( removeButton_ );
        leftButtonsPanel.add( moveUpButton_ );
        leftButtonsPanel.add( moveDownButton_ );
        return leftButtonsPanel;
    }

    /**
     * Create the buttons that go at the botton ( eg row editing buttons and OK, Cancel, ...)
     * @return ok cancel panel.
     */
    JPanel createButtonsPanel() {
        JPanel outerPanel = new JPanel( new BorderLayout() );

        outerPanel.add(createJumpToPanel(), BorderLayout.WEST);
        outerPanel.add(createRightButtons(), BorderLayout.EAST);
        return outerPanel;
    }

    JPanel createJumpToPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Jump to Scene");
        sceneSelector_ = new JComboBox(story_.getAllSceneNames().toArray());
        sceneSelector_.addActionListener(this);
        panel.add(label, BorderLayout.WEST);
        panel.add(sceneSelector_, BorderLayout.CENTER);
        panel.setToolTipText("Jump to a specific scene so you can edit from there.");
        return panel;
    }


    JPanel createRightButtons() {
        JPanel rightButtonsPanel = new JPanel( new FlowLayout() );

        initBottomButton( okButton_, "OK",
                "Save your edits and see the changes in the story. " );
        initBottomButton(cancelButton, "Cancel",
                "Go back to the story without saving your edits." );

        rightButtonsPanel.add( okButton_ );
        rightButtonsPanel.add(cancelButton);
        return rightButtonsPanel;
    }


    /**
     * Called when one of the add/remove/move/ok/cancel buttons are clicked for editing choices.
     */
    @Override
    public void actionPerformed( ActionEvent e )
    {
        super.actionPerformed(e);
        Object source = e.getSource();
        int row = selectedChildRow_;
        ChildTableModel childModel = childTable_.getChildTableModel();

        if ( source == okButton_ ) {
            ok();
        }
        else if (source == addButton_) {
            addNewChoice(row);
        }

        else if (source == removeButton_) {
            //System.out.println("remove row");
            int answer = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete choice "
                    + childModel.getValueAt(row, ChildTable.NAVIGATE_INDEX) +"?");
            if (answer == JOptionPane.YES_OPTION) {
                childModel.removeRow(row);
                story_.getCurrentScene().deleteChoice(row);
            }
        }
        else if (source == moveUpButton_) {
            selectedChildRow_ = childTable_.moveRow(row, row - 1);
            updateMoveButtons();
        }
        else if (source == moveDownButton_) {
            selectedChildRow_ = childTable_.moveRow(row, row + 1);
            updateMoveButtons();
        }

        else if (source == sceneSelector_) {
            commitSceneChanges();
            story_.advanceToScene(sceneSelector_.getSelectedItem().toString());
            showContent();
        }
        // This will prevent this handler from being called multiple times. Don't know why.
        e.setSource(null);
    }

    /**
     * @param row  table row
     * @param col  table column
     * @param buttonId id of buttonEditor clicked.
     */
    public void tableButtonClicked(int row, int col, String buttonId) {

        commitSceneChanges();
        if (ChildTable.NAVIGATE_TO_CHILD_BUTTON_ID.equals(buttonId)) {
            story_.advanceScene(row);
        }
        else if (ParentTable.NAVIGATE_TO_PARENT_BUTTON_ID.equals(buttonId)) {
            story_.advanceToScene(parentScenes_.get(row).getName());
        }
        else {
            assert false : "unexpected id =" + buttonId;
        }
        selectedChildRow_ = -1;
        showContent();
    }

    /**
     * A row in the child table has been selected or selection has changed.
     * @param e event
     */
    public void valueChanged(ListSelectionEvent e) {
        selectedChildRow_ = childTable_.getSelectedRow();
        //System.out.println("selected row now " + selectedChildRow_);
        removeButton_.setEnabled(true);
        updateMoveButtons();
    }

    private void updateMoveButtons() {
        moveUpButton_.setEnabled(selectedChildRow_ > 0);
        moveDownButton_.setEnabled(selectedChildRow_ < childTable_.getNumRows() - 1);
    }

    /**
     * Show a dialog that allows selecting the new child scene destination.
     * This will be either an exisiting scene or a new one.
     * A new row is automatically added to the table.
     * @param newRow row of the new choice in the child table.
     */
    private void addNewChoice(int newRow) {
        int row = (newRow < 0) ? 0 : newRow;
        NewChoiceDialog newChoiceDlg = new NewChoiceDialog(story_.getCandidateDestinationSceneNames());
        ChildTableModel childModel = childTable_.getChildTableModel();

        boolean canceled = newChoiceDlg.showDialog();
        if (!canceled) {
            String addedSceneName = newChoiceDlg.getSelectedDestinationScene();
            childModel.addNewChildChoice(row, addedSceneName);
            String choiceDescription = childModel.getChoiceDescription(row);
            story_.addChoiceToCurrentScene(addedSceneName, choiceDescription);
            newChoiceDlg.close();
        }
    }

    /**
     * @return our edited copy of the story we were passed at construction.
     */
    public Story getEditedStory() {
        return story_;
    }

    void commitSceneChanges() {
        sceneEditor.doSave();
        if (sceneEditor.isSceneNameChanged()) {
             story_.sceneNameChanged(sceneEditor.getOldSceneName(), sceneEditor.getEditedScene().getName());
        }
        // also save the choice text (it may have been modified or reordered)
        childTable_.getChildTableModel().updateSceneChoices(story_.getCurrentScene());
    }

    void ok()  {
        commitSceneChanges();
        this.setVisible( false );
    }
}
