/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package StaticClasses;

import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Joel_Ward
 */
public class InfoTableModel extends AbstractTableModel
{

    private Object[][] data;
    private Object[] columnNames;

    public InfoTableModel(Object[][] data, Object[] columnNames)
    {
        super();
        this.data = data;
        this.columnNames = columnNames;
    }

    public int getRowCount()
    {
        return data.length;
    }

    public int getColumnCount()
    {
        return columnNames.length;
    }

    public Object getValueAt(int rowIndex, int columnIndex)
    {
        return data[rowIndex][columnIndex];
    }

    @Override
    public boolean isCellEditable(int row, int col)
    {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
        return false;
    }

    @Override
    public Class getColumnClass(int c)
    {
        return getValueAt(0, c).getClass();
    }

    @Override
    public String getColumnName(int column)
    {
        return columnNames[column].toString();
    }
}
