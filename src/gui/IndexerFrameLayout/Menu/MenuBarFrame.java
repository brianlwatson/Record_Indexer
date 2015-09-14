package gui.IndexerFrameLayout.Menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

import gui.BatchState;
import gui.login.LoginFrame;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import loadsave.IndexerSerializer;
import loadsave.UserPreferences;

@SuppressWarnings("serial")
public class MenuBarFrame extends JMenuBar implements Serializable
{
	//Use JMenuItem here
	private JMenuItem downloadBatch_menu;
	private JMenuItem logoutBatch_menu;
	private JMenuItem exitBatch_menu;
	
	private BatchState batchstate;
	private DownloadBatchFrame dbf;
	private UserPreferences saver;

	public DownloadBatchFrame getDBF()
	{
		return dbf;
	}
	
	public MenuBarFrame(BatchState batchState)
	{
		batchstate = batchState;
		
		//Use JMenu
		JMenu menubar = new JMenu("File");
		menubar.setMnemonic(KeyEvent.VK_F);
		downloadBatch_menu = menubar.add("Download Batch");
		logoutBatch_menu = menubar.add("Logout");
		exitBatch_menu = menubar.add("Exit");
		
		add(menubar);
		
		logoutBatch_menu.addActionListener(LogOutListener);
		exitBatch_menu.addActionListener(exitListener);
		downloadBatch_menu.addActionListener(DownloadBatchListener);
		//You still need to add your listeners!!!
		saver = new UserPreferences(batchstate);
	}
	

	
	private ActionListener DownloadBatchListener = new ActionListener()
	{
		@SuppressWarnings("deprecation")
		public void actionPerformed(ActionEvent e)
		{
			try 
			{
				batchstate.getIndexerframe().disable();
				dbf = new DownloadBatchFrame(batchstate);
				dbf.setVisible(true);
			} 
			catch (Exception e1) 
			{
				e1.printStackTrace();
			}
			
		}
	};
	
	
	private ActionListener LogOutListener = new ActionListener()
	{
		@SuppressWarnings("deprecation")
		public void actionPerformed(ActionEvent e) 
		{
			try {
				IndexerSerializer.save(batchstate);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			batchstate.getIndexerframe().setVisible(false);
			batchstate.getLoginframe().setVisible(true);
			batchstate.getLoginframe().enable();
			batchstate.setUser(null);
			batchstate.setCurrentBatch(null);
			batchstate.setCurrentProject(null);
			batchstate.eraseFields();
			batchstate.getIndexerframe().repaint();
			LoginFrame.clearTextFields();
		
		}
	};
	
	
	private ActionListener exitListener = new ActionListener()
	{
		public void actionPerformed(ActionEvent e) 
		{
			System.exit(0);
		}
	};
	
	
}
