package gui.IndexerFrameLayout.Left;

import java.awt.Color;
import java.io.Serializable;

import gui.BatchState;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

public class LeftSubPanel extends JTabbedPane implements Serializable
{
	private JPanel tableEntry = new JPanel();
	private JPanel formEntry = new JPanel();
	
	private TablePanel tablepanel;
	private FormPanel formpanel;
	
	
	public JPanel getTableEntry() {
		return tableEntry;
	}
	
	public void setTableEntry(JPanel tableEntry) {
		this.tableEntry = tableEntry;
	}
	
	public JPanel getFormEntry() {
		return formEntry;
	}
	
	public void setFormEntry(JPanel formEntry) {
		this.formEntry = formEntry;
	}
	
	public TablePanel getTablepanel() {
		return tablepanel;
	}
	
	public void setTablepanel(TablePanel tablepanel) {
		this.tablepanel = tablepanel;
	}
	
	public FormPanel getFormpanel() {
		return formpanel;
	}
	
	public void setFormpanel(FormPanel formpanel) {
		this.formpanel = formpanel;
	}
	
	public void addFormPanel(FormPanel fp)
	{
		this.formpanel = fp;
		formEntry.add(fp);
	}
	
	public void addTablePanel(TablePanel tp)
	{
		this.tablepanel = tp;
		tableEntry.add(tp);
	}
	
	public LeftSubPanel(BatchState batchstate)
	{
		JPanel panel = new JPanel();
		tablepanel = new TablePanel(batchstate);
		if(batchstate.getCurrentProject() != null)
		{
			System.out.println("NOT NULL: " );
			formpanel = new FormPanel(batchstate);
			JScrollPane scroller = new JScrollPane(tablepanel);
			scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			
			addTab("Table Entry", null, scroller, "Table View" );
			addTab("Form Entry", null, formpanel, "Form Entry");
		}
		else
		{
			System.out.println("NULL");
			JPanel blank = new JPanel();
			blank.setBackground(Color.WHITE);
			addTab("Table Entry", null, blank, "Table Entry" );
			addTab("Form Entry", null, blank, "Form Entry");
		}
		
		panel.add(this);
		
	}
	
	
}
