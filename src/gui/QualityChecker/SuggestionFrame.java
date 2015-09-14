package gui.QualityChecker;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Set;

import gui.BatchState;
import gui.QualityChecker.ISpellCorrector.NoSimilarWordFoundException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

@SuppressWarnings("serial")
public class SuggestionFrame extends JFrame
{
	BatchState batchstate;
	JList<String> alternatives;
	JButton suggestionButton = new JButton("Select Selection");
	JButton cancelButton = new JButton("Cancel");
	int row;
	int col;
	
	@SuppressWarnings("deprecation")
	public SuggestionFrame(BatchState batchState, int selectedrow, int selectedcol) throws NoSimilarWordFoundException, IOException
	{
		row = selectedrow;
		col = selectedcol;
		this.batchstate = batchState;
		Set<String> suggestions = batchstate.getSuggestions(batchstate.gettabledata()[selectedrow][selectedcol]);
		setTitle("Suggestions");
		this.setLayout(new GridBagLayout());
		GridBagConstraints panel_organize = new GridBagConstraints();
		
		String[] suggestions_array = new String[suggestions.size()];
		int i = 0;
		for(String s : suggestions)
		{
			suggestions_array[i] = s;
			i++;
		}
		
		alternatives = new JList<String>(suggestions_array);
		alternatives.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane scroller = new JScrollPane(alternatives);
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		batchstate.getIndexerframe().disable();
		panel_organize.gridx = 0;
		panel_organize.gridy = 0;
		panel_organize.ipadx = 100;
		panel_organize.ipady = 100;
		panel_organize.gridwidth = 4;
		panel_organize.anchor = GridBagConstraints.NORTH;
		panel_organize.fill = GridBagConstraints.HORIZONTAL;
		scroller.setSize(new Dimension(200,100));
		scroller.setMinimumSize(new Dimension(200,10));
		this.add(scroller, panel_organize);
		
		panel_organize.ipadx = 0;
		panel_organize.ipady = 0;
		panel_organize.gridx = 0;
		panel_organize.gridy = 2;
		panel_organize.fill = GridBagConstraints.HORIZONTAL;
		this.add(suggestionButton, panel_organize);
		
		panel_organize.gridx = 2;
		panel_organize.gridy = 4;
		panel_organize.anchor = GridBagConstraints.SOUTH;
		panel_organize.fill = GridBagConstraints.HORIZONTAL;
		this.add(cancelButton, panel_organize);
		
		suggestionButton.setEnabled(false);
		setVisible(true);
		alternatives.addMouseListener(mouseListListener);
		cancelButton.addActionListener(cancelListener);
		suggestionButton.addActionListener(useSuggestionListener);
		setSize(new Dimension(400,200));
		this.setMinimumSize(new Dimension(300,100));
	}
	
	private MouseListener mouseListListener = new MouseAdapter() 
	{
		public void mouseClicked(MouseEvent mouseEvent) 
		{
			if (mouseEvent.getClickCount() > 0) 
			{
				int index = alternatives.locationToIndex(mouseEvent.getPoint());
				if (index >= 0) 
				{
					suggestionButton.setEnabled(true);
				}
			}
		}
		
	};
	
	private ActionListener cancelListener = new ActionListener()
	{
		@SuppressWarnings("deprecation")
		public void actionPerformed(ActionEvent e) 
		{
			batchstate.getIndexerframe().enable();
			setVisible(false);
		}
	};
	
	private ActionListener useSuggestionListener = new ActionListener() 
	{
		@SuppressWarnings("deprecation")
		public void actionPerformed(ActionEvent e) 
		{
			batchstate.getIndexerframe().enable();
			String value = (String)alternatives.getSelectedValue();
			batchstate.changecell(row, col,value);
			batchstate.getIndexerframe().getFormTableEntry().getFormpanel().changeFieldF(row, col, value);
			batchstate.getIndexerframe().getFormTableEntry().getFormpanel().removeFieldHighlight(row,col);
			batchstate.getIndexerframe().getFormTableEntry().repaint();
			setVisible(false);
		}
	};
}
