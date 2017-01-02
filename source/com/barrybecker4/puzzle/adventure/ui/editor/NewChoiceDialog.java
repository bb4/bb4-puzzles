// Copyright by Barry G. Becker, 2000-2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.adventure.ui.editor;

import com.barrybecker4.ui.components.GradientButton;
import com.barrybecker4.ui.components.TextInput;
import com.barrybecker4.ui.dialogs.AbstractDialog;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Allow the user to select the name of the destination scene
 * or type in the name of a new scene.
 * @author Barry Becker
 */
class NewChoiceDialog extends AbstractDialog
                      implements ActionListener {

    /** click this when done selecting a name for the destination scene. */
    private  GradientButton okButton_ = new GradientButton();

    private JComboBox sceneSelector_;
    private TextInput sceneTextInput_;
    private List<String> candidateDestinations_;

    private String selectedDestinationScene_;

    /**
     * Constructor.
     * @param candidateDestinations used to populate .
     */
    public NewChoiceDialog(List<String> candidateDestinations) {

        candidateDestinations_ = candidateDestinations;

        this.setResizable(false);
        setTitle("New Scene Choice");
        this.setModal( true );
        showContent();
    }


    @Override
    protected JComponent createDialogContent() {
        JPanel outerPanel = new JPanel(new BorderLayout());
        JPanel mainPanel = new JPanel();
        BoxLayout layout = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);
        mainPanel.setLayout(layout);

        sceneSelector_ = new JComboBox<>(candidateDestinations_.toArray());
        sceneSelector_.setAlignmentX( Component.LEFT_ALIGNMENT);
        sceneSelector_.setBorder(
                BorderFactory.createTitledBorder("Select an existing scene or type in the name for a new scene."));

        JLabel orLabel = new JLabel("or");
        orLabel.setAlignmentX( Component.LEFT_ALIGNMENT);

        sceneTextInput_ = new TextInput("New scene name");
        sceneTextInput_.setColumns(30);
        sceneTextInput_.setAlignmentX(Component.LEFT_ALIGNMENT);
        sceneTextInput_.setBorder(
                BorderFactory.createTitledBorder("Enter the name for a new scene."));

        mainPanel.add(sceneSelector_);
        mainPanel.add(orLabel);
        mainPanel.add(sceneTextInput_);
        outerPanel.add(mainPanel, BorderLayout.CENTER);
        outerPanel.add(createButtonsPanel(), BorderLayout.SOUTH);

        return outerPanel;
    }

    public String getSelectedDestinationScene() {
        return selectedDestinationScene_;
    }


    /**
     *  create the buttons that go at the button ( eg OK, Cancel, ...)
     * @return buttons panel.
     */
    JPanel createButtonsPanel() {
        JPanel buttonsPanel = new JPanel();

        initBottomButton( okButton_, "OK", "Use the selected scene as the new choice destination. " );
        initBottomButton(cancelButton, "Cancel", "Do not select any scene." );

        buttonsPanel.add( okButton_ );
        buttonsPanel.add(cancelButton);

        return buttonsPanel;
    }


    @Override
    public void actionPerformed( ActionEvent e ) {
        super.actionPerformed(e);
        Object source = e.getSource();

        if ( source == okButton_ ) {
            ok();
        }
    }

    void ok() {
        String customSceneName = sceneTextInput_.getValue();
        if (!customSceneName.equals("")) {
            selectedDestinationScene_ = customSceneName;
        }
        else {
            selectedDestinationScene_ = sceneSelector_.getSelectedItem().toString();
        }
        this.setVisible( false );
    }
}
