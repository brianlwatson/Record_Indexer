package gui.IndexerFrameLayout.Menu;

import gui.BatchState;
import gui.IndexerFrameLayout.IndexerFrame;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import client.ClientException;
import client.communication.ClientCommunicator;
import shared.communication.DataTransport_Result;
import shared.communication.DownloadBatch_Param;
import shared.communication.DownloadBatch_Result;
import shared.communication.GetProjects_Param;
import shared.communication.GetProjects_Result;
import shared.communication.GetSampleImage_Param;
import shared.communication.GetSampleImage_Result;
import shared.model.Batch;
import shared.model.Project;
import shared.model.User;

@SuppressWarnings("serial")
public class DownloadBatchFrame extends JFrame implements Serializable
{
	public static int DEFAULT_HEIGHT = 300;
	public static int DEFAULT_WIDTH = 400;
	
	private BatchState batchstate;
	//View Sample Button/ Cancel Button / Download Button
	private static JButton sampleButton;
	private static JButton cancelButton;
	private static JButton downloadButton;
	private static JComboBox<String> projectlist;
	private List<Project> allprojects;
	public static boolean panelenable = true;
	
	@SuppressWarnings("deprecation")
	public DownloadBatchFrame(BatchState batchState) throws Exception
	{
		batchstate = batchState;
		batchstate.getIndexerframe().disable();
		
		JPanel panel = new JPanel();
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		panel.setLayout(new GridBagLayout());
		GridBagConstraints panel_organize = new GridBagConstraints();
		
		panel_organize.gridx = 0;
		panel_organize.gridy = 0;
		panel.add(new JLabel("Project:"), panel_organize);
		
		panel_organize.gridx = 1;
		panel_organize.gridy = 0;
		
		projectlist = makeProjectList();
		panel.add(projectlist, panel_organize);
		
		panel_organize.gridx = 2;
		panel_organize.gridy = 0;
		sampleButton = new JButton("View Sample");
		panel.add(sampleButton, panel_organize);
		
		panel_organize.gridx = 0;
		panel_organize.gridy = 1;
		cancelButton = new JButton("Cancel");
		panel.add(cancelButton, panel_organize);
		
		panel_organize.gridx = 1;
		panel_organize.gridy = 1;
		downloadButton = new JButton("Download");
		panel.add(downloadButton, panel_organize);
		
		add(panel);
		
		cancelButton.addActionListener(CancelListener);
		downloadButton.addActionListener(DownloadListener);
		sampleButton.addActionListener(SampleImageListener);
		addWindowListener(windowListener);
		pack();
		setLocationRelativeTo(null);
		setResizable(false);
		setAlwaysOnTop (true);
		
		manageDownloadButton();
	}

	
	public JComboBox<String> makeProjectList() throws ClientException
	{
		JComboBox<String> projectlist = new JComboBox<String>();
		GetProjects_Param params = new GetProjects_Param();
		User u = batchstate.getUser();
		String username = u.getUsername();
		String password = u.getPassword();
		params.setUsername(username);
		params.setPassword(password);
		GetProjects_Result result = new GetProjects_Result();
		
		try
		{
			ClientCommunicator cc = new ClientCommunicator(ClientCommunicator.SERVER_HOST, ClientCommunicator.SERVER_PORT);
			result = cc.getProjects(params);
			List<Project> projects = result.getProjects();
			this.allprojects = projects;
			for(Project p : projects)
			{
				String proj_title = p.getTitle();
				projectlist.addItem(proj_title);
			}
			return projectlist;
		}
		catch(ClientException e)
		{
			throw new ClientException("Couldn't fill project list");
		}
	}
	
	private ActionListener SampleImageListener = new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			String selectedItem = (String) projectlist.getSelectedItem();
			GetSampleImage_Param gsi_param = new GetSampleImage_Param();
			gsi_param.setUsername(batchstate.getUser().getUsername());
			gsi_param.setPassword(batchstate.getUser().getPassword());
			
			for(Project p : allprojects)
			{
				if(p.getTitle().equals(selectedItem))
				{
					gsi_param.setProject_id(p.getProject_id());
				}
			}
			
			try
			{
				ClientCommunicator cc = new ClientCommunicator(ClientCommunicator.SERVER_HOST, ClientCommunicator.SERVER_PORT);
				GetSampleImage_Result gsi_result = cc.getSampleImage(gsi_param);
				String url = ClientCommunicator.URL_PREFIX + File.separator + gsi_result.getImagelocation();
				SampleImageFrame sf = new SampleImageFrame(batchstate,url);
				sf.setVisible(true);
			}
			catch (ClientException fe)
			{
				fe.printStackTrace();
			} 
			catch (IOException e1) 
			{
				e1.printStackTrace();
			}
			
		}
	};
	
	private ActionListener CancelListener = new ActionListener()
	{
		@SuppressWarnings("deprecation")
		public void actionPerformed(ActionEvent e)
		{	
			batchstate.getIndexerframe().enable();
			setVisible(false);
		}
	};
	
	private ActionListener DownloadListener = new ActionListener()
	{
		@SuppressWarnings("deprecation")
		public void actionPerformed(ActionEvent e)
		{
			batchstate.getIndexerframe().enable();
			DownloadBatch_Param params = new DownloadBatch_Param();
			
			String username = batchstate.getUser().getUsername();
			String password = batchstate.getUser().getPassword();
			
			params.setUsername(username);
			params.setPassword(password);
			
			String selectedItem = (String) projectlist.getSelectedItem();
		
			for(Project p : allprojects)
			{
				if(p.getTitle().equals(selectedItem))
				{
					params.setProject_id(p.getProject_id());
					batchstate.setCurrentProject(p);
				}
			}
			
			try
			{
				ClientCommunicator cc = new ClientCommunicator(ClientCommunicator.SERVER_HOST, ClientCommunicator.SERVER_PORT);
				DownloadBatch_Result result = cc.downloadBatch(params);
				Batch b = result.getBatch();
				DataTransport_Result.setDestination(ClientCommunicator.URL_PREFIX);
				b.setUrl(ClientCommunicator.URL_PREFIX + File.separator+ result.getBatch().getUrl());

				batchstate.setCurrentBatch(b);
				batchstate.getUser().setBatchassig(result.getBatch().getImage_id());
				batchstate.setAllFields(result.getFields());
				String[][] newtable = new String[batchstate.getCurrentProject().getNumrecords()][batchstate.getCurrentProject().getNumfields()];
				//System.out.println("Initializing new table: " + batchstate.getCurrentProject().getNumrecords() + "  " + batchstate.getCurrentProject().getNumfields());
				batchstate.settabledata(newtable);
				try 
				{
					batchstate.getIndexerframe().setVisible(false);
					batchstate.reinitIndexerFrame(batchstate);
				} 
				catch (IOException e1) 
				{
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
				setVisible(false);
			}
			catch (ClientException fe)
			{
				fe.printStackTrace();
			}
		}
	};
	
	public void manageDownloadButton()
	{
		User u = batchstate.getUser();
		if(u.getBatchassig() > 0)
		{
			DownloadBatchFrame.downloadButton.setEnabled(false);
		}
		else
		{
			DownloadBatchFrame.downloadButton.setEnabled(true);
		}
	}
	
	private WindowAdapter windowListener = new WindowAdapter()
	{
	    @SuppressWarnings("deprecation")
		public void windowClosing(WindowEvent e)
	    {
	        batchstate.getIndexerframe().enable();
	        
	    }
	};
	
	
}
