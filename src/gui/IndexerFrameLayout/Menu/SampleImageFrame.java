package gui.IndexerFrameLayout.Menu;

import gui.BatchState;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class SampleImageFrame extends JFrame implements Serializable
{
	private int DEFAULT_WIDTH = 700;
	private int DEFAULT_HEIGHT = 550;
	private BatchState batchstate;
	private JButton closeButton;

	
	@SuppressWarnings("deprecation")
	public SampleImageFrame(BatchState batchState, String url) throws IOException
	{
		setTitle("Sample Image");
		batchstate = batchState;
		batchstate.getIndexerframe().disable();
		batchstate.getIndexerframe().getMenubar().getDBF().disable();
		
		JPanel panel = new JPanel();
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		panel.setLayout(new BorderLayout());
		
		URL path = new URL(url);
		Image image = ImageIO.read(path);
		ImageIcon img = new ImageIcon(image.getScaledInstance(700, 500, 4));
		
		JLabel imagedisplay = new JLabel(img);
		closeButton = new JButton("Close");
		
		panel.add(imagedisplay);
		panel.add(closeButton, BorderLayout.SOUTH);
		add(panel);
		
		closeButton.addActionListener(CloseListener);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setAlwaysOnTop (true);
	}
	
	private ActionListener CloseListener = new ActionListener()
	{
		@SuppressWarnings("deprecation")
		public void actionPerformed(ActionEvent e)
		{	
			setVisible(false);
			batchstate.getIndexerframe().getMenubar().getDBF().enable();
		}
	};

	
	
}
