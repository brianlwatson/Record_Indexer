package gui.IndexerFrameLayout.Right;

import java.awt.Color;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;

import gui.BatchState;
import gui.IndexerFrameLayout.Left.FormPanel;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import client.communication.ClientCommunicator;

@SuppressWarnings("serial")
public class RightSubPanel extends JTabbedPane
{
	private JPanel imageNavigator;
	//private JPanel fieldHelper;
	private FieldHelpPanel helpernavigator;
	private BatchState batchstate;
	
	public RightSubPanel(BatchState batchstate) throws IOException
	{
		this.batchstate = batchstate;
		imageNavigator = new JPanel();
		String test = batchstate.getFieldHelpURL();
		
		String url = test;
		helpernavigator = new FieldHelpPanel();
		if(!url.equals("") && batchstate.getCurrentBatch() != null)
		{
			helpernavigator.setPage(url);
		}
		else // testing purposes
		{
			setBackground(Color.WHITE);
		}
		
		JScrollPane scroller = new JScrollPane(helpernavigator);
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		addTab("Field Help",null, scroller, "Field Help");
		addTab("Image Navigation", null, imageNavigator, "Navigator Navigator");
	}
	
	public void setFieldHelp(String url) throws IOException
	{
		if(!url.equals(""))
		{
			helpernavigator.setPage(url);
		}
	}
		
}
