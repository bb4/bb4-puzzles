// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.adventure.ui.editor;

import javax.swing.table.DefaultTableModel;
import java.util.List;


/**
 * Basically the DefaultTableModel with a few customizations
 *
 * @author Barry Becker
 */
public class ParentTableModel extends DefaultTableModel {

    private static final long serialVersionUID = 0;

    public ParentTableModel(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
    }

    public ParentTableModel(Object[] columnNames, int rowCount) {
        super(columnNames, rowCount);
    }

    @Override
    public Class getColumnClass(int col) {
        List v = (List)dataVector.elementAt(0);
        return v.get(col).getClass();
    }

    @Override
    public boolean isCellEditable(int row, int column)  {

        return (column == ParentTable.NAVIGATE_INDEX);
    }
}