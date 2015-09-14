package gui.IndexerFrameLayout;

import gui.BatchState;
import gui.IndexerFrameLayout.Image.DrawingFrame;
import gui.IndexerFrameLayout.Left.LeftSubPanel;
import gui.IndexerFrameLayout.Menu.ButtonsPanel;
import gui.IndexerFrameLayout.Menu.MenuBarFrame;
import gui.IndexerFrameLayout.Right.RightSubPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.io.Serializable;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import loadsave.BatchSpecifics;
import loadsave.IndexerSerializer;
import loadsave.UserPreferences;
import client.ClientException;

@SuppressWarnings("serial")
public class IndexerFrame extends JFrame implements Serializable
{
	public static int DEFAULT_WIDTH = 1000;
	public static int DEFAULT_HEIGHT = 800;
	
	private BatchState batchstate;
	private ButtonsPanel buttonspanel;
	private MenuBarFrame menubar;
	private JSplitPane horizontalSplitPane;
	private JSplitPane verticalSplitPane;
	private LeftSubPanel form_table_entry;
	private RightSubPanel fieldhelpnavigator;
	private DrawingFrame drawframe;
	
	private UserPreferences saver;
	private BatchSpecifics batchinfo;
	
	
	public IndexerFrame(BatchState batchState) throws ClientException, IOException, ClassNotFoundException 
	{
		//Panel Settings
		this.batchstate = batchState;
		batchState.setIndexerframe(this);
		this.setTitle("CS240 RecordIndexer");
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setLayout(new BorderLayout());
		
		saver = new UserPreferences(batchState);
		
		//COMMENT THIS OUT WHEN YOU WANT TO PASSOFF?
		//		IndexerSerializer.load(batchstate);
		
		//Menu Bar
		menubar = new MenuBarFrame(batchstate);
		setJMenuBar(menubar);	
		//Buttons
		buttonspanel = new ButtonsPanel(batchstate);
		add(buttonspanel, BorderLayout.NORTH);
		
		
		//SubPane Initiation
		form_table_entry = new LeftSubPanel(batchstate);
		fieldhelpnavigator= new RightSubPanel(batchstate);
		
		
		
		//Horizontal Split Pane
		horizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, form_table_entry, fieldhelpnavigator);
		horizontalSplitPane.setDividerLocation(DEFAULT_WIDTH/2);
				
		//Vertical Split Pane
		
		
		if(batchstate.getCurrentBatch() != null)//H Split pane is added here
		{
			System.out.println("HERE: " + batchstate.getCurrentBatch().toString());
			String url = batchstate.getCurrentBatch().getUrl();
			drawframe = new DrawingFrame(url, batchstate);
			verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, drawframe, horizontalSplitPane);
			batchinfo = new BatchSpecifics(batchstate);
		}
		
		else
		{
			JPanel panels = new JPanel();
			panels.setSize(new Dimension(800,400));
			verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panels, horizontalSplitPane);
			verticalSplitPane.setDividerLocation(DEFAULT_HEIGHT/2);
		}
		
		//verticalSplitPane.setMinimumSize(new Dimension(200,200));
		
		
		horizontalSplitPane.setResizeWeight(0.5);
		verticalSplitPane.setResizeWeight(0.5);
		add(verticalSplitPane);
		
		setMinimumSize(new Dimension(200,200));
		setLocationRelativeTo(null);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(true);
		
		saver = new UserPreferences(batchState);
		//IndexerSerializer.loadBatchComponents(batchState);
		

		
		repaint();
	}
	
	public MenuBarFrame getMenubar()
	{
		return menubar;
	}
	
	public void setDrawFrame(DrawingFrame df)
	{
		drawframe = df;
	}
	
	public ButtonsPanel getButtonsPanel()
	{
		return buttonspanel;
	}
	
	public DrawingFrame getDrawFrame()
	{
		return drawframe;
	}
	
	
	public RightSubPanel getFieldhelpnavigator() 
	{
		return fieldhelpnavigator;
	}
	
	public LeftSubPanel getFormTableEntry()
	{
		return form_table_entry;
	}

	public void setFieldhelpnavigator(RightSubPanel fieldhelpnavigator) 
	{
		this.fieldhelpnavigator = fieldhelpnavigator;
	}
	
	public int getHdividerlocation()
	{
		return horizontalSplitPane.getDividerLocation();
	}
	
	public int getVdividerlocation()
	{
		return verticalSplitPane.getDividerLocation();
	}
	
	public void setHdividerLocation(int dividerlocation)
	{
		horizontalSplitPane.setDividerLocation(dividerlocation);
	}
	
	public void setVdividerLocation(int dividerlocation)
	{
		verticalSplitPane.setDividerLocation(dividerlocation);
	}
	
	public UserPreferences getSaver() {
		return saver;
	}

	public void setSaver(UserPreferences saver) {
		this.saver = saver;
	}

	public BatchSpecifics getBatchinfo() {
		return batchinfo;
	}

	public void setBatchinfo(BatchSpecifics batchinfo) {
		this.batchinfo = batchinfo;
	}
}
