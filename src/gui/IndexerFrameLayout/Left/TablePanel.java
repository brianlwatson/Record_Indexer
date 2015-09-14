package gui.IndexerFrameLayout.Left;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.Serializable;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import gui.BatchState;
import gui.QualityChecker.SuggestionFrame;
import gui.QualityChecker.ISpellCorrector.NoSimilarWordFoundException;

@SuppressWarnings("serial")
public class TablePanel extends JPanel implements Serializable
{
	private BatchState batchstate;
	private JTable table;
	private TableModel tablemodel;
	
	public TablePanel(BatchState batchState) 
	{
		this.batchstate = batchState;
		setBackground(Color.WHITE);
		if(batchstate.getCurrentBatch() != null)
		{
			this.tablemodel = new TableModel(batchState);
			this.table = new JTable(tablemodel);
			
			table.setRowHeight(25);
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			table.setCellSelectionEnabled(true);
			table.getTableHeader().setReorderingAllowed(false);
			
			TableColumnModel columnModel = table.getColumnModel();
			for(int i = 0; i < tablemodel.getColumnCount(); i++)
			{
				TableColumn column = columnModel.getColumn(i);
				column.setPreferredWidth(70);
			}
			for(int i = 1; i < tablemodel.getColumnCount(); i++)
			{
				TableColumn column = columnModel.getColumn(i);
				column.setCellRenderer(new IndexerTableCellRenderer(batchstate));
			}
			
			table.addMouseListener(new RightClickAdapter(batchState));
			setLayout(new BorderLayout());
			
			add(table.getTableHeader(), BorderLayout.NORTH);
			add(table, BorderLayout.CENTER);
		}
	}
	

	
	private class RightClickAdapter extends MouseAdapter
	{
		BatchState batchstate;
		
		public RightClickAdapter(BatchState batchState)
		{
			super();
			this.batchstate = batchState;
		}
		
		@Override
		public void mouseClicked(MouseEvent e)
		{
			if (SwingUtilities.isRightMouseButton(e)) 
			{
				JTable target = (JTable) e.getSource();
				int clickedRow = target.rowAtPoint(e.getPoint());
				int clickedColumn = target.columnAtPoint(e.getPoint());
				batchstate.setSelectedCellCol(clickedColumn);
				batchstate.setSelectedCellRow(clickedRow);
				try 
				{
					if(batchstate.gettabledata()[clickedRow][clickedColumn] != null)
					{
						if(batchstate.getSuggestions(batchstate.gettabledata()[clickedRow][clickedColumn]).size() > 0
								&& !batchstate.getSuggestions(batchstate.gettabledata()[clickedRow][clickedColumn]).contains(batchstate.gettabledata()[clickedRow][clickedColumn]))
						{
							try 
							{
								JFrame suggestionbox = new SuggestionFrame(batchstate,clickedRow, clickedColumn);
								suggestionbox.setLocationRelativeTo(null);
							} 
							catch (NoSimilarWordFoundException e1) 
							{
								e1.printStackTrace();
							} 
							catch (IOException e1) 
							{
								e1.printStackTrace();
							}
						}
					}
				} 
				catch (NoSimilarWordFoundException e1) 
				{
					e1.printStackTrace();
				} 
				catch (IOException e1) 
				{
					e1.printStackTrace();
				}
			}
		}
	}
	
	class IndexerTableCellRenderer extends JLabel implements TableCellRenderer
	{
		private Border border = BorderFactory.createLineBorder(Color.BLUE, 2);
		private Border borderblack = BorderFactory.createLineBorder(Color.BLACK, 1);
		private BatchState batchstate;
		
		public IndexerTableCellRenderer(BatchState batchState)
		{
			this.batchstate = batchState;
		}
		
		//This guy processes your table and your selection. this is going to e your highlighter and your fieldhelper changer
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) 
		{
			this.setBorder(borderblack);
			if(isSelected)
			{
				this.setBorder(border);
				batchstate.setSelectedCellCol(column);
				batchstate.setSelectedCellRow(row+1);
				
				try
				{
					batchstate.getIndexerframe().getFieldhelpnavigator().setFieldHelp(batchstate.getFieldHelpURL());
					batchstate.getIndexerframe().getFieldhelpnavigator().repaint();
					batchstate.getIndexerframe().getDrawFrame().getDrawingComponent().highlightRectangle(batchstate.getSelectedCellRow(), batchstate.getSelectedCellCol());
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				} 
			}
			else
			{
				this.setBorder(borderblack);
			}
			
			this.setText((String) value);
			if(value != null && !value.equals(""))
			{
				try 
				{
					batchstate.setSelectedCellCol(column);
					batchstate.setSelectedCellRow(row);
					if(!batchstate.getSuggestions((String)value).contains(value))
					{
						this.setBorder(BorderFactory.createLineBorder(Color.RED, 4));
					}
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
			return this;
		}
	}


}

