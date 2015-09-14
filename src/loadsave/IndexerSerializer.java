package loadsave;

import gui.BatchState;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public class IndexerSerializer 
{	
	@SuppressWarnings("resource")
	public static void save(BatchState batchState) throws IOException
	{
		FileOutputStream outfile = new FileOutputStream("*" + batchState.getUser().getUsername() + ".o");
		UserPreferences userinfo = new UserPreferences(batchState);
		
	
		ObjectOutputStream output = new ObjectOutputStream(outfile);
		output.writeObject(userinfo);
		output.flush();
	}
	
	@SuppressWarnings("resource")
	public static void load(BatchState batchState) throws IOException, ClassNotFoundException 
	{
		File file = new File("*" + batchState.getUser().getUsername() + ".o");
		
		
		if(file.isFile())
		{
		FileInputStream infile = new FileInputStream("*" + batchState.getUser().getUsername() + ".o");
		ObjectInputStream input = new ObjectInputStream(infile);
		//user,batch,project,tabledata,fields,selectedcellrow,selectedcell,col
		//size,framelocation,batchinfo
		UserPreferences userinfo = (UserPreferences)input.readObject();
	
		//batchState.getIndexerframe().getButtonsPanel().buttonEnable(true);
		
		batchState.setCurrentBatch(userinfo.getBatch());
		batchState.setCurrentProject(userinfo.getProject());
		System.out.println("USERINFO : " + userinfo.getProject().toString());
		batchState.setAllFields(userinfo.getFields());
		batchState.setUser(userinfo.getUser());
		batchState.settabledata(userinfo.getTabledata());
		batchState.getIndexerframe().setBatchinfo(userinfo.getBatchinfo());
		batchState.getIndexerframe().setSize(userinfo.getSize());
		System.out.println("HERE: " + userinfo.getFramelocation().toString());
		batchState.getIndexerframe().setLocation(userinfo.getFramelocation());
		
		}
		
	}
	
	public static void loadBatchComponents(BatchState batchState) throws ClassNotFoundException, IOException
	{
		File file = new File("*" + batchState.getUser().getUsername() + ".o");
		
		if(file.isFile())
		{
			FileInputStream infile = new FileInputStream("*" + batchState.getUser().getUsername() + ".o");
			ObjectInputStream input = new ObjectInputStream(infile);
		
			UserPreferences userinfo = (UserPreferences)input.readObject();
		
			batchState.getIndexerframe().setHdividerLocation(userinfo.getBatchinfo().getHorizontalDivider());
			batchState.getIndexerframe().setVdividerLocation(userinfo.getBatchinfo().getVerticalDivider());
			batchState.getIndexerframe().setLocation(userinfo.getFramelocation());

		}
	}
	
	
	
}
