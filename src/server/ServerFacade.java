package server;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import server.database.Database;
import server.database.DatabaseException;
import shared.communication.DownloadBatch_Param;
import shared.communication.DownloadBatch_Result;
import shared.communication.DownloadFile_Param;
import shared.communication.DownloadFile_Result;
import shared.communication.GetFields_Param;
import shared.communication.GetFields_Result;
import shared.communication.GetProjects_Param;
import shared.communication.GetProjects_Result;
import shared.communication.GetSampleImage_Param;
import shared.communication.GetSampleImage_Result;
import shared.communication.SearchNode;
import shared.communication.Search_Param;
import shared.communication.Search_Result;
import shared.communication.SubmitBatch_Param;
import shared.communication.SubmitBatch_Result;
import shared.communication.ValidateUser_Param;
import shared.communication.ValidateUser_Result;
import shared.model.Batch;
import shared.model.Cell;
import shared.model.Field;
import shared.model.Project;
import shared.model.User;

public class ServerFacade 
{
	
	/**
	 * Needed Operations:
	 * Validate User - x
	 * Get Projects - x
	 * Get Sample Image - x
	 * Download Batch - x
	 * Submit Batch  -x 
	 * Get Fields - x
	 * Search - x
	 * Download File -x
	 */
	
	private static Database db;
	
	public ServerFacade()
	{
		db = new Database();
		try 
		{
			prepareFacade();
		} 
		catch (DatabaseException e) 
		{
			e.printStackTrace();
		}
	}
	
	
	public static void prepareFacade() throws DatabaseException
	{
		Database.initialize();
	}
	
	/**
	 * Take a username and password
	 * return null if user doesn't exist, otherwise return a valid user object
	 * return false if null user is returned
	 * return failed otherwise
	 * @param params - username and password
	 * @return
	 * @throws DatabaseException 
	 * @throws SQLException 
	 */
	public static ValidateUser_Result validateUser(ValidateUser_Param params) throws DatabaseException, SQLException
	{
		ValidateUser_Result result = new ValidateUser_Result();
		try
		{
			db.startTransaction();
			String username = params.getUsername();
			String password = params.getPassword();
			User u = db.getUserDAO().getUserforValidate(username, password);
			result.setUser(u);
			
			if(u == null)
			{
				result.setMessage("FALSE\n");
			}
			db.endTransaction(true);
			return result;
		}
		
		catch(SQLException e)
		{
			db.endTransaction(false);
			e.printStackTrace();
			result.setMessage("FAILED\n");
		}
		
		catch(DatabaseException e)
		{
			db.endTransaction(false);
			e.printStackTrace();
			result.setMessage("FAILED\n");
		}
		return result;
	}
	
	
	/**
	 * If the user is validated, give him a list of all projects
	 * The result's set message contains the outcome
	 * @param params - username and password
	 * @return result - list of projects
	 * @throws SQLException 
	 */
	public static GetProjects_Result getProjects(GetProjects_Param params) throws SQLException
	{
		GetProjects_Result result = new GetProjects_Result();
		try
		{
			db.startTransaction();		
			String username = params.getUsername();
			String password = params.getPassword();
			
			if(username == null || password == null)//
			{
				result.setMessage("FAILED\n");
				db.endTransaction(false);
				return result;
			}
			
			User u = db.getUserDAO().getUserforValidate(username, password);
			
			if(u == null)//if user is not validated
			{
				result.setMessage("FAILED\n");
				db.endTransaction(false);
				return result;
			}
			
			result.setProjects(db.getProjectDAO().getAll());
			db.endTransaction(true);
			return result;
		}
		
		catch(SQLException e)
		{
			db.endTransaction(false);
			e.printStackTrace();
			result.setMessage("FAILED\n");
		}
		catch(DatabaseException e)
		{
			db.endTransaction(false);
			e.printStackTrace();
			result.setMessage("FAILED\n");
		}
		return result;
	}
	
	
	/**
	 * check for invalid project ID, invalid username or password
	 * @param params - project_id, username, password
	 * @return - a sample image's URL from the given projectID
	 */
	public static GetSampleImage_Result getSampleImage(GetSampleImage_Param params)
	{
		GetSampleImage_Result result = new GetSampleImage_Result();
		try
		{
			db.startTransaction();
			String username = params.getUsername();
			String password = params.getPassword();
			int projid = params.getProject_id();
			
			User u = db.getUserDAO().getUserforValidate(username, password);
			if(u == null) //if user is not validated
			{
				result.setMessage("FAILED\n");
				db.endTransaction(false);
				return result;
			}
			
			//If the batchDAO gets you some valid operation
			String sample_url = db.getBatchDAO().getSampleImage(projid);
			db.endTransaction(true);
			result.setImagelocation(sample_url);	
		}
		
		catch(SQLException e)
		{
			db.endTransaction(false);
			e.printStackTrace();
			result.setMessage("FAILED\n");
		}
		
		catch(DatabaseException e)
		{
			db.endTransaction(false);
			e.printStackTrace();
			result.setMessage("FAILED\n");
		}
		return result;
	}
	
	/**
	 * This one you need to make sure that you get a valid user back and that he doesn't have a batch assigned to him
	 * so check if user is null or if the assigned batch is 0 or -1 (used both)
	 * then, you want to get the next available batch, it's parent project and a list of fields for the project
	 * make a new result with those
	 * set the batch as unavailable (set it as something above a 0)
	 * update the user's current batch assignment id
	 * 
	 * @param param - project_id
	 * 
	 * @return - Batch, Project, List<Field>
	 */
	public static DownloadBatch_Result downloadBatch(DownloadBatch_Param param)
	{
		String username = param.getUsername();
		String password = param.getPassword();
		int projid = param.getProject_id();
		
		DownloadBatch_Result result = new DownloadBatch_Result();
		try
		{
			db.startTransaction();
			User u = db.getUserDAO().getUserforValidate(username, password);

			if(projid > db.getProjectDAO().getAll().size() || projid < 1) //checks the bounds of the project_id
			{
				result.setMessage("FAILED\n");
				db.endTransaction(false);
				return result;
			}

			if(u == null || u.getBatchassig() != -1) //if user is not validated, or the current batch is unavailable
			{
				result.setMessage("FAILED\n");
				db.endTransaction(false);
				return result;
			}
			
			Batch b = db.getBatchDAO().getNextAvailableBatch(projid);
			if(b == null) //if no available batch is returned
			{
				db.endTransaction(false);
				result.setMessage("FAILED\n");
				return null;
			}
			Project p = db.getProjectDAO().getProject(projid);

			if(p == null) //safeguard to make sure a project is returned
			{
				db.endTransaction(false);
				result.setMessage("FAILED\n");
				return null;
			}
			
			List<Field> fields = new ArrayList<Field>();
			fields = db.getFieldDAO().getFields_ofProject(projid);
			
			result.setBatch(b);
			result.setProject(p);
			result.setFields(fields);
			
			db.getBatchDAO().updateBatchAvail(b.getImage_id(), 1);
			db.getUserDAO().user_changeBatchAssignation(username, b.getImage_id());
			db.endTransaction(true);
			return result;
		}
		
		catch(SQLException e)
		{
			db.endTransaction(false);
			e.printStackTrace();
			result.setMessage("FAILED\n");
		}
		
		catch(DatabaseException e)
		{
			db.endTransaction(false);
			e.printStackTrace();
			result.setMessage("FAILED\n");
		}
		return result;
	}
	
	/**
	 * first check to see if the user is valid and the current param matches his assigned batch
	 *  - otherwise you have the wrong batch+user combination
	 * then check if there is a right number of values
	 * then get a batch, project and list of fields
	 * 
	 * fieldValues is kind of tricky here
	 * 	--The values within a record are delimited by commas. 
	 * 	--Records are delimited by semicolons.
	 * 	--Empty fields are represented with empty strings
	 *  --Example: Jones,Fred,13; Rogers,Susan,42;,,; ,,;Van Fleet, Bill, 23
	 *  
	 *  fieldValues will be just one huge string. you're going to want to split it.
	 *  
	 *  afterwards, you need to increment the number of indexed records for the user
	 *  this is the number of individual names the user has indexed, not the number of batches.
	 *  		**when a batch is submitted, give the user credit for indexing all records on the batch, even if they didn't
	 *  **after a batch has been submitted, the server should allow the batch to be searched by key word
	 *  
	 *   --Example: Jones,Fred,13; Rogers,Susan,42;,,; ,,;Van Fleet, Bill, 23
	 * 	Each record needs to be in its own array slow
	 *	use String split() -> returns an [] of strings
	 *  
	 *  //parsedfieldvalues should contain
	 *	//:Jones,Fred,13     |     Rogers,Susan,42     | etc.
	 * @param param - int BatchID, String fieldValues
	 * @return
	 */
	public static SubmitBatch_Result submitBatch(SubmitBatch_Param param)
	{
		SubmitBatch_Result result = new SubmitBatch_Result();
		String username = param.getUsername();
		String password = param.getPassword();
		String fieldvalues = param.getFieldValues();
		int currentbatch = param.getBatchID();
		try
		{
			db.startTransaction();
			User u = db.getUserDAO().getUserforValidate(username, password);
			
			//Check if user is available or doesn't have a batch assignment
			if(u == null || u.getBatchassig() == -1 || u.getBatchassig() != currentbatch)
			{
				db.endTransaction(false);
				result.setMessage("FAILED\n");
				return result;
			}
			
			Batch b = db.getBatchDAO().getBatch(currentbatch);
			
			if(b == null) //if a batch isn't returned
			{
				db.endTransaction(false);
				result.setMessage("FAILED\n");
				return result;
			}
			
			Project p = db.getProjectDAO().getProject(b.getProject_id());
			List<Field> fields = db.getFieldDAO().getFields_ofProject(p.getProject_id());
			
			//Increment_records determines how many records are supplied in order to increment the user's record count
			int increment_records = 1;
			for(int i =0; i < fieldvalues.length(); i++)
			{
				if(fieldvalues.charAt(i) == ';')
				{
					increment_records++;
				}
			}
			
			if(increment_records==0)
			{
				increment_records = 1;
			}
			
			//Parse the provided values and split them into an array
			fieldvalues = fieldvalues.replace(";", ",");	
			String[] fieldvaluesparsed = fieldvalues.split("," ,-1);
			db.getUserDAO().user_IncRecords_byTotal(u, increment_records); //Increment the user's # of indexed records
			db.getUserDAO().user_changeBatchAssignation(u.getUsername(), -1); //Change the batch assignation of the user to available
			db.getBatchDAO().updateBatchAvail(currentbatch,1); //change the assignation of the batch to unavailable
		
			//Updates the corresponding cell values. 
			//	i represents the Row Number, j represents the column number 
			//	fieldDAO is required to convert the local field ID into the general field id
			int x = 0; //this iterates through the fieldvaluesparsed
			for(int i = 0; i < p.getNumrecords(); i++)
			{
				for(int j = 0; j < fields.size(); j++)
				{
					if(x < fieldvaluesparsed.length)
					{
						String knownvalue = fieldvaluesparsed[x++];
						int abs_field_id = db.getFieldDAO().getFieldbyLocal((j+1), p.getProject_id());
						db.getCellDAO().updateCell(knownvalue, currentbatch, abs_field_id, (i+1));		
					}
				}
			}
			
			db.endTransaction(true);
			result.setMessage("TRUE\n");
			return result;
		}
		
		catch(SQLException e)
		{
			db.endTransaction(false);
			e.printStackTrace();
			result.setMessage("FAILED\n");
		}
		
		catch(DatabaseException e)
		{
			db.endTransaction(false);
			e.printStackTrace();
			result.setMessage("FAILED\n");
		}
		db.endTransaction(true);
		return result;
	}
	
	
	
	/**
	 * Make sure that the user exists and give him the fields of the dao using the current projid
	 * if project ID is not specified (empty string), give the user all possible fields to draw from for later
	 * if the projectID is specified, set the fields with all current fields of the given projid
	 * @param param
	 * @return
	 */
	public static GetFields_Result getFields(GetFields_Param param)
	{
		GetFields_Result result = new GetFields_Result();
		int projid = param.getProject_ID();
		String username = param.getUsername();
		String password = param.getPassword();
		try
		{
			db.startTransaction();
			User u = db.getUserDAO().getUserforValidate(username, password);
			int numprojects = db.getProjectDAO().getAll().size();

			if(u == null || projid > numprojects)
			{
				result.setMessage("FAILED\n");
				db.endTransaction(false);
				return result;
			}
			if(projid > 0)
			{
				result.setFieldsList(db.getFieldDAO().getFields_ofProject(projid));
				db.endTransaction(true);
				return result;
			}
		
			result.setFieldsList(db.getFieldDAO().getAll());
			db.endTransaction(true);
		}
		catch(SQLException e)
		{
			db.endTransaction(false);
			e.printStackTrace();
			result.setMessage("FAILED\n");
		}
		catch(DatabaseException e)
		{
			db.endTransaction(false);
			e.printStackTrace();
			result.setMessage("FAILED\n");
		}
		return result;
	}
	
	/**
	 * validate the user,
	 * 	for each cell
	 * 		get the a list of cells with matching cellvalues and field/id's
	 * 
	 * 		for each cell in the list, 
	 * 			get the batch
	 * 			set the cell to the result
	 * 			set the batch to the result
	 * 				return the result
	 */
	public static Search_Result Search(Search_Param param)
	{
		Search_Result result = new Search_Result();
		String username = param.getUsername();
		String password = param.getPassword();
		
		try
		{
			db.startTransaction();
			//Validates the User and other Parameter inputs
			User u = db.getUserDAO().getUserforValidate(username, password);
			if(u == null || param.getField_IDs().size() == 0 || param.getField_values().size() == 0)
			{
				result.setMessage("FALSE");
				db.endTransaction(false);
				return result;
			}
			
			//Makes sure that no Field_IDs are out of bounds
			for(int i = 0; i < param.getField_IDs().size(); i++)
			{
				if(param.getField_IDs().get(i) > db.getFieldDAO().getAll().size())
				{
					result.setMessage("FAILED\n");
					db.endTransaction(false);
					return result;
				}
			}
			
			//For each column,
			//	Go through the Values and see if there is a match
			
			for(int i = 0; i < param.getField_IDs().size(); i++)
			{
				for(int j = 0; j < param.getField_values().size(); j++)
				{
					int field_id = param.getField_IDs().get(i);
					String knownvalue = param.getField_values().get(j);
					List<Cell> search_results = db.getCellDAO().getCellsbyField(field_id);
					List<Cell> filtered = new ArrayList<Cell>();
					for(Cell c : search_results)
					{
						//if there is a match between the given field_value and the cell value,
						//		add it to the filtered list of cells
						if(c.getKnowndata().toLowerCase().equals(knownvalue.toLowerCase()))
						{
							filtered.add(c);
						}
					}
					//For each match, make a search node and add it to the result
					for(Cell c : filtered)
					{ 
						String loc = db.getBatchDAO().getBatch(c.getBatch_id()).getUrl();
						SearchNode search = new SearchNode(c.getBatch_id(), loc, c.getRecordnum(), c.getField_id());
						result.addNode(search);
					}
				}
			}
			db.endTransaction(true);
			return result;
		}
		
		catch(SQLException e)
		{
			db.endTransaction(false);
			e.printStackTrace();
			result.setMessage("FAILED\n");
		}
		
		catch(DatabaseException e)
		{
			db.endTransaction(false);
			e.printStackTrace();
			result.setMessage("FAILED\n");
		}
		return result;
	}
	
	/**
	 * This doesn't need to do anything right now, but it does need to be here.
	 * @param param
	 * @return
	 */
	public DownloadFile_Result downloadFile(DownloadFile_Param param)
	{
		return null;
	}
}
