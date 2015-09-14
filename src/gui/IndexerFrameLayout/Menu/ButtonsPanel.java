package gui.IndexerFrameLayout.Menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Serializable;

import gui.BatchState;

import javax.swing.JButton;
import javax.swing.JPanel;

import loadsave.IndexerSerializer;
import client.ClientException;
import client.communication.ClientCommunicator;
import shared.communication.SubmitBatch_Param;
import shared.communication.ValidateUser_Param;
import shared.model.User;

@SuppressWarnings("serial")
public class ButtonsPanel extends JPanel implements Serializable
{
	/**
	 * Zoom In
	 * Zoom Out
	 * Invert Image
	 * Toggle Highlights
	 * Save
	 * Submit
	 */
	
	private BatchState batchstate;

	private JButton ZoomInButton;
	private JButton ZoomOutButton;
	private JButton InvertImageButton;
	private JButton ToggleHighlightsButton;
	private JButton SaveButton;
	private JButton SubmitButton;
	
	public ButtonsPanel(BatchState batchState)
	{
		batchstate = batchState;
		ZoomInButton = new JButton("Zoom In");
		ZoomOutButton = new JButton("Zoom Out");
		InvertImageButton = new JButton("Invert Image");
		ToggleHighlightsButton = new JButton ("Toggle Highlights");
		SaveButton = new JButton("Save");
		SubmitButton = new JButton("Submit");
		
		add(ZoomInButton);
		add(ZoomOutButton);
		add(InvertImageButton);
		add(ToggleHighlightsButton);
		add(SaveButton);
		add(SubmitButton);
		
		if(batchstate.getCurrentBatch() == null)
		{
			buttonEnable(false);
		}
		
		ZoomInButton.addActionListener(ZoomInListener);
		ZoomOutButton.addActionListener(ZoomOutListener);
		InvertImageButton.addActionListener(InvertImageListener);
		ToggleHighlightsButton.addActionListener(ToggleHighlightsListener);
		SubmitButton.addActionListener(SubmitBatchListener);
		SaveButton.addActionListener(SaveListener);
	}
	
	public void buttonEnable(boolean enable)
	{
		ZoomInButton.setEnabled(enable);
		ZoomOutButton.setEnabled(enable);
		InvertImageButton.setEnabled(enable);
		ToggleHighlightsButton.setEnabled(enable);
		SaveButton.setEnabled(enable);
		SubmitButton.setEnabled(enable);
	}
	
	private ActionListener ZoomInListener = new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{	
			batchstate.getIndexerframe().getDrawFrame().getDrawingComponent().zoomIn();
		}
	};
	
	private ActionListener ZoomOutListener = new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{	
			batchstate.getIndexerframe().getDrawFrame().getDrawingComponent().zoomOut();
		}
	};
	
	private ActionListener InvertImageListener = new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			try 
			{
				batchstate.getIndexerframe().getDrawFrame().getDrawingComponent().InvertImage();
			} 
			catch (IOException e1) 
			{
				e1.printStackTrace();
			}
		}
	};
	
	private ActionListener ToggleHighlightsListener = new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			batchstate.getIndexerframe().getDrawFrame().getDrawingComponent().enableHighlighting();
		}
	};
	
	private ActionListener SubmitBatchListener = new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
				ClientCommunicator cc = new ClientCommunicator(ClientCommunicator.SERVER_HOST, ClientCommunicator.SERVER_PORT);
				String parsedvalues = batchstate.parseTableData();
				SubmitBatch_Param sb_param = new SubmitBatch_Param();
				sb_param.setUsername(batchstate.getUser().getUsername());
				sb_param.setPassword(batchstate.getUser().getPassword());
				sb_param.setBatchID(batchstate.getCurrentBatch().getImage_id());
				sb_param.setFieldValues(parsedvalues);
				try 
				{
					cc.submitBatch(sb_param);
				} 
				catch (ClientException e1) 
				{
					e1.printStackTrace();
				}
			
				try 
				{
					batchstate.setCurrentBatch(null);
					ValidateUser_Param vu_param = new ValidateUser_Param();
					vu_param.setUsername(batchstate.getUser().getUsername());
					vu_param.setPassword(batchstate.getUser().getPassword());
					User revalidated_user = cc.validateUser(vu_param).getUser();
					batchstate.setUser(revalidated_user);					
					batchstate.getIndexerframe().setVisible(false);
					batchstate.reinitIndexerFrame(batchstate);
				} 
				catch (ClientException e1) 
				{
					e1.printStackTrace();
				}
				catch (IOException e1) 
				{
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		}
	};
	
	
	private ActionListener SaveListener = new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			try {
				
				IndexerSerializer.save(batchstate);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	};
}
