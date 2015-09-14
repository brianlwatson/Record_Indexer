package gui;

import gui.IndexerFrameLayout.IndexerFrame;
import gui.QualityChecker.ISpellCorrector.NoSimilarWordFoundException;
import gui.QualityChecker.SpellCorrector;
import gui.login.LoginFrame;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JFrame;

import loadsave.UserPreferences;
import client.ClientException;
import client.communication.ClientCommunicator;
import server.Server;
import server.database.DatabaseException;
import shared.model.Batch;
import shared.model.Field;
import shared.model.Project;
import shared.model.User;

public class BatchState 
{
	static User user;
	static Batch currentBatch;
	static Project currentProject;
	List<Project> allProjects;
	List<Field> allFields = new ArrayList<Field>();
	
	LoginFrame loginframe;
	IndexerFrame indexerframe;
	//private List<BatchStateListener> batchstate_listeners;
	String[][] tabledata;

	int SelectedCellRow;
	int SelectedCellCol;
	



	public String getFieldHelpURL()
	{
		if(allFields.size() > 0 && SelectedCellCol > 0)
		{
			String HelpHtml = allFields.get(SelectedCellCol-1).getHelphtml();
			String CompleteHelpHtml = ClientCommunicator.URL_PREFIX + File.separator + HelpHtml;
			return CompleteHelpHtml;
		}
		return "";
	}

	public int getSelectedCellRow() {
		return SelectedCellRow;
	}

	public void setSelectedCellRow(int selectedCellRow) {
		SelectedCellRow = selectedCellRow;
	}

	public int getSelectedCellCol() {
		return SelectedCellCol;
	}

	public void setSelectedCellCol(int selectedCellCol) {
		SelectedCellCol = selectedCellCol;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		BatchState.user = user;
	}

	public Batch getCurrentBatch() {
		return currentBatch;
	}

	public void setCurrentBatch(Batch currentBatch) {
		BatchState.currentBatch = currentBatch;
	}

	public Project getCurrentProject() {
		return currentProject;
	}

	public void setCurrentProject(Project currentProject) {
		BatchState.currentProject = currentProject;
	}

	public List<Project> getAllProjects() {
		return allProjects;
	}

	public void setAllProjects(List<Project> allProjects) {
		this.allProjects = allProjects;
	}

	public List<Field> getAllFields() {
		return allFields;
	}
	
	public void eraseFields()
	{
		allFields.clear();
	}

	public void setAllFields(List<Field> allFields) {
		this.allFields = allFields;
	}

	public LoginFrame getLoginframe() {
		return loginframe;
	}

	public void setLoginframe(LoginFrame loginframe) {
		this.loginframe = loginframe;
	}

	public IndexerFrame getIndexerframe() {
		return indexerframe;
	}

	public void setIndexerframe(IndexerFrame indexerframe) {
		this.indexerframe = indexerframe;
	}

	public String[][] gettabledata() {
		return tabledata;
	}

	public void settabledata(String[][] tabledata) {
		this.tabledata = tabledata;
	}
	
	public void changecell(int row, int col, String value)
	{
		tabledata[row][col] = value;
	}


	public void reinitIndexerFrame(BatchState batchstate) throws ClientException, IOException, ClassNotFoundException
	{
		IndexerFrame wf = new IndexerFrame(batchstate);
		this.setIndexerframe(wf);
		wf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		wf.setVisible(true);
	}
	
	public static void main(String[] args) throws DatabaseException, ClientException, IOException
	{
		
		if(args.length == 0)
		{
			ClientCommunicator.SERVER_HOST = "localhost";
			ClientCommunicator.SERVER_PORT = 8080;
			ClientCommunicator.URL_PREFIX = ClientCommunicator.PREFIX + ClientCommunicator.SERVER_HOST + ":" + ClientCommunicator.SERVER_PORT;
		}
		else
		{
			ClientCommunicator.SERVER_HOST = args[0];
			ClientCommunicator.SERVER_PORT = Integer.parseInt(args[1]);
			ClientCommunicator.URL_PREFIX = ClientCommunicator.PREFIX + ClientCommunicator.SERVER_HOST + ":" + ClientCommunicator.SERVER_PORT;
		}
		
		Server s = new Server(ClientCommunicator.SERVER_PORT);
		s.run();
		
		BatchState batchstate = new BatchState();
		
		LoginFrame lf = new LoginFrame(batchstate);
		batchstate.setLoginframe(lf);
		lf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		lf.setVisible(true);
		
		s= null;
	}
	
	public String parseTableData()
	{
		int width = allFields.size();
		int rows = currentProject.getNumrecords();
		StringBuilder sb = new StringBuilder();
		
		//If no entries have been submitted, create a blank entry for each cell value in the table
		if(tabledata == null) // just for testing purposes
		{
			for(int i = 0; i < rows; i++)
			{
				for(int j = 1; j < width; j++)
				{
					sb.append("");
					if(j < width -1)
					{
						sb.append(",");
					}
				}
				sb.append(";");
			}
			return sb.toString();
		}
		
		for(int i = 0; i < rows; i++)
		{
			for(int j = 0; j < width; j++)
			{
				if(tabledata[i][j] == null)
				{
					sb.append("");
				}
				if(tabledata[i][j] != null)
				{
					sb.append(tabledata[i][j]);
				}
				if(j < width -1)
				{
					sb.append(",");
				}
			}
			sb.append(";");
		}
		return sb.toString();
	}
	
	public String getKnownDataFiles()
	{
		Set<String> textfiles = new TreeSet<String>();
		String filepath;
		int x = 1;
		for(Field f : getAllFields())
		{
			if(x == SelectedCellCol)
			{
				return filepath = ClientCommunicator.URL_PREFIX +   File.separator +  f.getKnowndata();
			}
			x++;
		}
		return "";
	}
	
	public Set<String> getSuggestions(String word) throws NoSimilarWordFoundException, IOException
	{
		SpellCorrector qc = new SpellCorrector();
		try
		{
			qc.useDictionary(getKnownDataFiles());
		}
		catch(Exception e)
		{
			throw new MalformedURLException("MALFORMED");
		}
		return qc.suggestSimilarWord(word);
	}
	
	
	public void printtable()
	{
		System.out.println("------------------------------------------");
		for(int i = 0; i < currentProject.getNumrecords(); i++)
		{
			for(int j = 0; j < currentProject.getNumfields(); j++)
			{
				System.out.print(tabledata[i][j] + " " );
			}
			System.out.println("");
		}
		System.out.println("------------------------------------------");
	}
}
