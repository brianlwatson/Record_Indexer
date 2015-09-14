package gui.IndexerFrameLayout.Left;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import gui.BatchState;
import gui.QualityChecker.SuggestionFrame;
import gui.QualityChecker.ISpellCorrector.NoSimilarWordFoundException;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import shared.model.Field;

@SuppressWarnings("serial")
public class FormPanel extends JPanel implements Serializable
{
	String[][] tabledata;
	List<ArrayList<JTextField>> fieldsf = new ArrayList< ArrayList< JTextField>>();
	List< JTextField> fields;
	List<JLabel> labels;
	int location = 0;
	JList<Integer> list;
	List<JPanel> listpanels;
	BatchState batchstate;
	
	public FormPanel(BatchState batchstate)
	{
		this.batchstate = batchstate;
		this.fields = new ArrayList<JTextField>();
		this.labels = new ArrayList<JLabel>();
		this.tabledata = batchstate.gettabledata();
		
		List<Field> allfields = batchstate.getAllFields();
		
		for(int i = 0; i < allfields.size(); i++)
		{
			Field f = allfields.get(i);
			JTextField text = new JTextField();
			fields.add(text);
			labels.add(new JLabel(f.getField_title()));
			
			text.addFocusListener(new FieldFocusListener(i));
			text.getDocument().addDocumentListener(new DocListener());
			text.addMouseListener(new RightClickAdapter(batchstate,i));
		}
		
		for(int i = 0; i < batchstate.getCurrentProject().getNumrecords(); i++)
		{
			fieldsf.add((ArrayList<JTextField>) fields);
		}
		
		DefaultListModel<Integer> listModel = new DefaultListModel<Integer>();
		//Add the numbers to the list menu
		for(int i = 0; i < batchstate.getCurrentProject().getNumrecords(); i++)
		{
			listModel.add(i,i+1);
		}
		this.list = new JList<Integer>(listModel);
		
		DefaultListCellRenderer renderer =  (DefaultListCellRenderer)list.getCellRenderer();  
		renderer.setHorizontalAlignment(JLabel.CENTER); 
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setFixedCellWidth(150);
		list.setFixedCellHeight(20);
		setLayout(new BorderLayout());
		list.setSelectedIndex(0);

		JComponent panel = new JPanel();
		GroupLayout layout = new GroupLayout(panel);
		panel.setLayout(layout);
		
		layout.setAutoCreateGaps(true);
		GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
		GroupLayout.Group yLabelGroup = layout.createParallelGroup(GroupLayout.Alignment.TRAILING);
		hGroup.addGroup(yLabelGroup);
		GroupLayout.Group yFieldGroup = layout.createParallelGroup();
		hGroup.addGroup(yFieldGroup);
		layout.setHorizontalGroup(hGroup);
		GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
		layout.setVerticalGroup(vGroup);

		int p = GroupLayout.PREFERRED_SIZE;
		for (JLabel label : labels) 
		{
			yLabelGroup.addComponent(label);
		}
	
		List<JTextField> fieldsofrecord = fieldsf.get(location);
	
		for (JTextField field1 : fieldsofrecord) 
		{	
			yFieldGroup.addComponent(field1, p, 100, 125);
		}
		
		for (int ii = 0; ii < labels.size(); ii++) 
		{
			vGroup.addGroup(layout.createParallelGroup().
					addComponent(labels.get(ii)).
					addComponent(fieldsf.get(location).get(ii), p, p, p));
		}

		list.addMouseListener(mouseListListener);

		//this is good
		JScrollPane listScrollPanel = new JScrollPane();
		listScrollPanel.setViewportView(list);

		//this needs to include more stuff
		JScrollPane fieldScrollPanel = new JScrollPane();
		fieldScrollPanel.setViewportView(panel);
		
		
		if(batchstate.getCurrentBatch() != null)
		{
			add(fieldScrollPanel, BorderLayout.CENTER);
			add(listScrollPanel, BorderLayout.WEST);
		}
		else
		{
			this.setBackground(Color.WHITE);
		}
		
		
	}
	
	//@Override
	public void selectedCellChanged(int row, int col) 
	{
		list.setSelectedIndex(row);
		for(int i = 0; i < fields.size(); i++)
		{
			String value = batchstate.gettabledata()[row][i+1];
			fieldsf.get(location).get(i).setText(value);
		}

		fieldsf.get(location).get(col).requestFocus();
	}


	public void updateFieldValues() throws NoSimilarWordFoundException, IOException{
		int row = batchstate.getSelectedCellRow();
		if (list.getSelectedIndex() == row)
		{
			for (int i = 0; i < fields.size(); i++)
			{
				System.out.println("FI: "  +fields.get(i));
				fields.get(i).setText(batchstate.gettabledata()[row][i+1]);
				batchstate.setSelectedCellCol(i+1);
				if (batchstate.getSuggestions(batchstate.gettabledata()[row][i+1]).size() == 0)
				{
					fields.get(i).setBackground(Color.white);
				}
				else 
				{
					System.out.println(batchstate.getSuggestions(batchstate.gettabledata()[row][i]).toString());
					fields.get(i).setBackground(Color.red);
				}
			}
		}
	}
	
	
	private MouseListener mouseListListener = new MouseAdapter() 
	{
		public void mouseClicked(MouseEvent mouseEvent) 
		{
			if (mouseEvent.getClickCount() > 0) 
			{
				int index = list.locationToIndex(mouseEvent.getPoint());
				if (index >= 0) 
				{
					batchstate.setSelectedCellCol(batchstate.getSelectedCellCol());
					batchstate.setSelectedCellRow(index);
					location = index;
					for(int i = 0; i < batchstate.getCurrentProject().getNumfields(); i++)
					{
						String cellvalue = batchstate.gettabledata()[index][i];
						if(cellvalue != null)
						{
							changeFieldF(index,i, cellvalue);
							try 
							{
								if(!batchstate.getSuggestions(cellvalue).contains(cellvalue) && cellvalue.length() > 0)
								{
									fieldsf.get(index).get(i).setBackground(Color.RED);
								}
								else
								{
									fieldsf.get(index).get(i).setBackground(Color.WHITE);
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
						
						else
						{
							changeFieldF(index,i, "");
						}
					}
				}
			}

			if (SwingUtilities.isRightMouseButton(mouseEvent)) 
			{
				
			}
		}
	};
	
	
	
	private class FieldFocusListener implements FocusListener{

		private int col;
		public FieldFocusListener(int i){
			super();
			this.col = i;
		}

		//@Override
		public void focusGained(FocusEvent e) 
		{
			batchstate.setSelectedCellCol(col+1);
			batchstate.setSelectedCellRow(list.getSelectedIndex());
			try 
			{
				batchstate.getIndexerframe().getFieldhelpnavigator().setFieldHelp(batchstate.getFieldHelpURL());
				batchstate.getIndexerframe().getFieldhelpnavigator().repaint();
			} 
			catch (IOException e1) 
			{
				e1.printStackTrace();
			}
		}

		public void focusLost(FocusEvent e) 
		{}
	}

	private class DocListener implements DocumentListener  
	{
		public void changedUpdate(DocumentEvent documentEvent) 
		{
			printIt(documentEvent);
		}
		
		public void insertUpdate(DocumentEvent documentEvent) 
		{
			printIt(documentEvent);
		}
		
		public void removeUpdate(DocumentEvent documentEvent) 
		{
			printIt(documentEvent);
		}
		private void printIt(DocumentEvent documentEvent) 
		{	  
			for (int i = 0; i < fieldsf.get(location).size(); i++)
			{
				if (documentEvent.getDocument() == fieldsf.get(location).get(i).getDocument())
				{					
					String value = fieldsf.get(location).get(i).getText();
					if(value != null)
					{
						batchstate.changecell(location,i,value);
						try 
						{
							if(!batchstate.getSuggestions((String)value).contains(value) && value.length() > 0 )
							{
								fieldsf.get(location).get(i).setBackground(Color.RED);
							}
							else
							{
								fieldsf.get(location).get(i).setBackground(Color.WHITE);
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
				}
			}	
		}
	}
	
	private class RightClickAdapter extends MouseAdapter
	{
		BatchState batchstate;
		int column;

		public RightClickAdapter(BatchState batchstate, int col){

			super();
			this.column = col;
			this.batchstate = batchstate;
		}

		public void mouseClicked(MouseEvent e)
		{
			if (SwingUtilities.isRightMouseButton(e)) 
			{
				int clickedRow = list.getSelectedIndex();
				int clickedColumn = column;

				batchstate.setSelectedCellCol(clickedColumn +1);
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
								changeFieldF(clickedRow,clickedColumn, batchstate.gettabledata()[clickedRow][clickedColumn]);								
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
	
	public void changeFieldF(int row, int col, String value)
	{
		fieldsf.get(row).get(col).setText(value);
	}
	
	public void removeFieldHighlight(int row, int col)
	{
		fieldsf.get(row).get(col).setBackground(Color.WHITE);
	}
	
}
