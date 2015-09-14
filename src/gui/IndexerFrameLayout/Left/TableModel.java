package gui.IndexerFrameLayout.Left;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import shared.model.Field;
import gui.BatchState;
import gui.QualityChecker.ISpellCorrector.NoSimilarWordFoundException;

@SuppressWarnings("serial")
public class TableModel extends AbstractTableModel implements Serializable
{
	private BatchState batchstate;
	private String[][] tabledata;
	private String[] columntitles ;
	
	TableModel(BatchState batchState)
	{
		this.batchstate = batchState;
		if(batchstate.getUser() != null && batchstate.getCurrentProject() != null)
		{
			int width = batchstate.getCurrentProject().getNumfields() +1;
			int height = batchstate.getCurrentProject().getNumrecords();
			columntitles = new String[width];
			tabledata = new String[height][width];
			nameColumns();
			initTable(height,width);
		}
	}
	
	private void nameColumns()
	{
		List<Field> allFields = batchstate.getAllFields();
		columntitles[0] = "Record Number";
		int i = 1;
		for(Field f : allFields)
		{
			columntitles[i] = f.getField_title();
			i++;
		}
	}
	
	public void editCell(Object value, int row, int col)
	{
		tabledata[row][col] = (String)value;
	}

	public void initTable(int row, int col)
	{
		for(int i = 0; i < row; i++)
		{
			String str = "" + (i+1);
			tabledata[i][0] = str;
		}
	}
	
	public int getRowCount() 
	{
		if(tabledata != null)
		{
			return tabledata.length;
		}
		else
			return 0;
	}

	public int getColumnCount() 
	{
		if(tabledata != null)
		{
			return tabledata[0].length;
		}
		else
			return 0;
	}

	public Object getValueAt(int rowIndex, int columnIndex) 
	{
		return tabledata[rowIndex][columnIndex];
	}
	
	public String[][] getTabledata()
	{
		return tabledata;
	}
	
	public String[] getColumnNames()
	{
		return columntitles;
	}
	
	@Override
	public String getColumnName(int column)
	{
		if(batchstate.getCurrentProject() != null)
		{
			return columntitles[column];
		}
		return "";
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		if(columnIndex > 0)
		{
			return true;
		}
		return false;
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex)
	{
		if(isCellEditable(rowIndex, columnIndex))
		{
			tabledata[rowIndex][columnIndex] = (String) aValue;
			batchstate.settabledata(tabledata);
			try 
			{
				batchstate.getSuggestions((String) aValue);
			}
			catch (NoSimilarWordFoundException e) 
			{
				e.printStackTrace();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	
	}
	
}
