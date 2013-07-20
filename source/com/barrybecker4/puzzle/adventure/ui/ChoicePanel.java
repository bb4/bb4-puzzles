/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.adventure.ui;

import com.barrybecker4.puzzle.adventure.Choice;
import com.barrybecker4.puzzle.adventure.ChoiceList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


/**
 * This panel shows a list of options for what to do next.
 * You press the button to take the action.
 * @author Barry Becker
 */
class ChoicePanel extends JPanel implements ActionListener {

    private List<SceneChangeListener> sceneChangeListeners_;


    /**
     * Construct based on a list of Choices.
     */
    public ChoicePanel(ChoiceList choices) {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        sceneChangeListeners_ = new ArrayList<SceneChangeListener>();

        setChoices(choices);
    }

    /**
     * Update the list of options shown
     * @param choices
     */
    public void setChoices(ChoiceList choices) {

         this.removeAll();

         // for each choice add a button and text.
         int i=1;
         for (Choice choice : choices) {
             addOption(i++, choice);
         }
         this.revalidate();
         this.repaint();
    }

    public void addSceneChangeListener(SceneChangeListener listener) {
        sceneChangeListeners_.add(listener);
    }

    public void removeSceneChangeListener(SceneChangeListener listener) {
        sceneChangeListeners_.remove(listener);
    }

    private void addOption(int index, Choice choice) {
        JPanel choiceElement = new JPanel();
        choiceElement.setLayout(new BoxLayout(choiceElement, BoxLayout.X_AXIS));

        JButton button = new JButton(Integer.toString(index));
        button.addActionListener(this);

        JLabel label = new JLabel(choice.getDescription());
        choiceElement.add(button);
        choiceElement.add(label);
        choiceElement.add(Box.createHorizontalGlue());
        this.add(choiceElement);
    }

    /**
     * called when a button is pressed.
     */
    public void actionPerformed( ActionEvent e )
    {
        JButton sourceButton = (JButton)e.getSource();
        int selectedChoiceIndex = Integer.parseInt(sourceButton.getText()) - 1;

        for (SceneChangeListener listener : sceneChangeListeners_) {
            listener.sceneChanged(selectedChoiceIndex);
        }
    }
}
