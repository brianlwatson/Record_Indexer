package gui.login;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.NotSerializableException;
import java.io.Serializable;

import gui.BatchState;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import loadsave.IndexerSerializer;
import client.communication.ClientCommunicator;
import shared.communication.ValidateUser_Param;
import shared.communication.ValidateUser_Result;

@SuppressWarnings("serial")
public class LoginFrame extends JFrame implements Serializable
{
	//Set the dimensions of the login window to 470x120
	public static int DEFAULT_WIDTH = 470;
	public static int DEFAULT_HEIGHT = 120;
	
	//Create JTextFields for username and password
	private static JTextField usernameField = new JTextField(30);
	private static JPasswordField passwordField = new JPasswordField(30);
	
	private BatchState batchstate;
	public static boolean LOGIN_COMPLETE = false;
	
	public LoginFrame(BatchState batchState)
	{
		LOGIN_COMPLETE = false;
		batchstate = batchState;
		batchstate.setLoginframe(this);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		this.setTitle("LOGIN");
		GridBagConstraints panel_organize = new GridBagConstraints();
		panel_organize.anchor = GridBagConstraints.CENTER;
		
		panel_organize.gridheight = 1;
		panel_organize.gridwidth = 1;
		
		panel_organize.gridx = 0;
		panel_organize.gridy = 0;
		panel.add(new JLabel("Username:  "), panel_organize);
		panel_organize.gridx = 1;
		panel_organize.gridy = 0;
		panel.add(usernameField, panel_organize);
		
		panel_organize.gridx = 0;
		panel_organize.gridy = 1;
		panel.add(new JLabel("Password:  "), panel_organize);
		panel_organize.gridx = 1;
		panel_organize.gridy = 1;
		panel.add(passwordField, panel_organize);
		
		JButton loginButton = new JButton("Login");
		JButton exitButton = new JButton("Exit");

		panel_organize.anchor = GridBagConstraints.WEST;
		panel_organize.gridx = 1;
		panel_organize.gridy = 2;
		panel.add(loginButton, panel_organize);

		panel_organize.gridx = 0;
		panel_organize.gridy = 2;
		panel.add(exitButton, panel_organize);
		
		add(panel);
		
		loginButton.addActionListener(loginListener);
		exitButton.addActionListener(exitListener);
		
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
	}
	
	
	
	private ActionListener exitListener = new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			System.exit(0);
		}
	};
	
	
	private ActionListener loginListener = new ActionListener()
	{
		@SuppressWarnings("deprecation")
		public void actionPerformed(ActionEvent e)
		{	
			ValidateUser_Param vu_param = new ValidateUser_Param();
			vu_param.setPassword(passwordField.getText());
			vu_param.setUsername(usernameField.getText());
			
			try
			{
				ClientCommunicator cc = new ClientCommunicator(ClientCommunicator.SERVER_HOST, ClientCommunicator.SERVER_PORT);
				ValidateUser_Result vu_result = cc.validateUser(vu_param);
				
				if(vu_result != null && !vu_result.toString().equals("FALSE\n")) // User is validated
				{
					batchstate.setUser(vu_result.getUser());
					batchstate.getLoginframe().setVisible(false);
					
					if(vu_result.getUser().getBatchassig() < 1)
					{
						batchstate.setCurrentBatch(null);
					}
					//Open the Welcome Window here
					WelcomeFrame welcomeFrame = new WelcomeFrame(batchstate, vu_result);
					welcomeFrame.setVisible(true);
					
					
					
				}
				
				if(vu_result == null || vu_result.getMessage().equals("FALSE\n") || vu_result.getMessage().equals("FAILED\n"))
				{
					LoginErrorFrame errorFrame = new LoginErrorFrame(batchstate);
					errorFrame.setVisible(true);
				}
			}
			catch(Exception exc)
			{
				System.out.println("INVALID USER IN GUI: " + exc.getMessage());
				exc.printStackTrace();
			}	
		}
	};
	
	public static void clearTextFields()
	{
		usernameField.setText("");
		passwordField.setText("");
	}

}
