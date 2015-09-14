package gui.login;

import gui.BatchState;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class LoginErrorFrame extends JFrame implements Serializable
{
	public static int DEFAULT_WIDTH = 260;
	public static int DEFAULT_HEIGHT = 180;
	
	private BatchState batchState;
	
	
	@SuppressWarnings("deprecation")
	public LoginErrorFrame(BatchState batchstate)
	{
		batchState = batchstate;
		JPanel panel = new JPanel();
		this.setTitle("ERROR");
		panel.setLayout(new GridBagLayout());
		GridBagConstraints panel_organize = new GridBagConstraints();
		
		batchState.getLoginframe().disable();
		panel_organize.gridx = 0;
		panel_organize.gridy = 0;
		panel.add(new JLabel("\tInvalid and/or Password"), panel_organize);

		panel_organize.gridx = 0;
		panel_organize.gridy = 1;
		JButton okayButton = new JButton("OK");
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
			batchState.getLoginframe().enable();
			setVisible(false);
		}
	};
}
