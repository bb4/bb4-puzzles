/** Copyright by Barry G. Becker, 2000-2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.adventure.ui.editor;

import com.barrybecker4.puzzle.adventure.Scene;

import javax.swing.table.DefaultTableModel;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * Basically the DefaultTableModel with a few customizations.
 *
 * @author Barry Becker
 */
public class ChildTableModel extends DefaultTableModel {

    public ChildTableModel(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
    }

    public ChildTableModel(Object[] columnNames, int rowCount) {
        super(columnNames, rowCount);
    }

    /**
     * Make the text for the scene choice descriptions match the scene passed in.
     * Also the order may have changed, so that needs to be checked as well.
     * @param currentScene scene to update to.
     */
    public void updateSceneChoices(Scene currentScene) {
        LinkedHashMap<String, String> choiceMap = new LinkedHashMap<String, String>();

        for (int i = 0; i < getRowCount(); i++) {
            String dest = (String) getValueAt(i, ChildTable.NAVIGATE_INDEX);
            choiceMap.put(dest, getValueAt(i, ChildTable.CHOICE_DESCRIPTION_INDEX).toString());
        }
        currentScene.getChoices().update(choiceMap);
    }

    public String getChoiceDescription(int row) {
        return (String) this.getValueAt(row, ChildTable.CHOICE_DESCRIPTION_INDEX);
    }

    /**
     * Set the scene name of the current add row and add another add row.
     * @param row location to add the new choice
     * @param addedSceneName  name pf the scene to add.
     */
    public void addNewChildChoice(int row, String addedSceneName) {

        Object d[] = new Object[this.getColumnCount()];
        d[ChildTable.NAVIGATE_INDEX] = addedSceneName;
        d[ChildTable.CHOICE_DESCRIPTION_INDEX] = ChildTable.NEW_CHOICE_DESC_LABEL;
        this.insertRow(row, d);

        this.fireTableRowsInserted(row, row);  // need this
    }

    @Override
    public Class getColumnClass(int col)  {
        List v = (List) dataVector.elementAt(0);
        return v.get(col).getClass();
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return true;
    }
}
