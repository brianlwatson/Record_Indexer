package gui.login;

import gui.BatchState;
import gui.IndexerFrameLayout.IndexerFrame;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.Serializable;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import loadsave.IndexerSerializer;
import client.ClientException;
import shared.communication.ValidateUser_Result;

public class WelcomeFrame extends JFrame implements Serializable
{
	public static int DEFAULT_WIDTH = 260;
	public static int DEFAULT_HEIGHT = 120;
	
	private BatchState batchState;
	@SuppressWarnings("deprecation")
	public WelcomeFrame(BatchState batchstate, ValidateUser_Result vu_result)
	{
		batchState = batchstate;
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		GridBagConstraints panel_organize = new GridBagConstraints();
		
		String s1 = "Welcome, " + vu_result.getUser().getFirst_name() + " " + vu_result.getUser().getLast_name() + ".";
		String s2 = "You have indexed " + vu_result.getUser().getNum_records() + " records.";
		
		
		batchstate.getLoginframe().disable();
		panel_organize.gridx = 0;
		panel_organize.gridy = 0;
		panel.add(new JLabel(s1), panel_organize);

		panel_organize.gridx = 0;
		panel_organize.gridy = 1;
		panel.add(new JLabel(s2), panel_organize);
		
		panel_organize.gridx = 0;
		panel_organize.gridy = 2;
		JButton okayButton = new JButton("START");
		panel.add(okayButton,panel_organize);
		
		add(panel);
		
		okayButton.addActionListener(exitListener);

		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
	}
	
	
	private ActionListener exitListener = new ActionListener()
	{
		@SuppressWarnings("deprecation")
		public void actionPerformed(ActionEvent e)
		{
			LoginFrame.LOGIN_COMPLETE = true;
			setVisible(false);
		
			try 
			{
				batchState.reinitIndexerFrame(batchState);
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
}
