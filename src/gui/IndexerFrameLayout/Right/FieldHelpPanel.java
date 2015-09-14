package gui.IndexerFrameLayout.Right;

import java.awt.Color;
import java.io.IOException;
import javax.swing.JEditorPane;

/**
 * JEditorPane allows you to look at the html
 * Set the background to white
 * and set the size of the pane
 * make sure it isn't editable
 * let it load text/html 
 * use set just like you use add with panels
 * @author brian
 *
 */
@SuppressWarnings("serial")
public class FieldHelpPanel extends JEditorPane
{
	public FieldHelpPanel() throws IOException
	{
		setOpaque(true);
		setBackground(Color.white);
		setEditable(false);
		setContentType("text/html");
	}
	
	public void loadFieldHelp(String url) throws IOException
	{
		System.out.println("badz");
		setPage(url);
	}
}
