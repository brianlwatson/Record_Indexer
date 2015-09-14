package server;
/**
 * BatchDAO 
 * contains:
 * 	getAll()
 * 	add()
 * 	available()
 * 	getBatch()
 * 	getNextAvailable()
 */

import java.util.*;
import java.sql.*;

import server.database.Database;
import shared.model.*;

/**
 * BatchDAO
 * needs to be able to:
 *  getAll Batches
 *  add Batch
 *  batch is Available
 *  get Batch by project name
 *  update a batch
 *  get next available batch 
 * @author brian
 *
 */

public class BatchDAO 
{
	private Database db;
	
	
	public BatchDAO(Database db) 
	{
		this.db = db;
	}
	
	public List<Batch> getAll() throws SQLException 
	{
		ArrayList<Batch> result = new ArrayList<Batch>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try 
		{
			String query = "select * from batch";
			stmt = db.getConnection().prepareStatement(query);
			rs = stmt.executeQuery();
			
			while (rs.next()) 
			{
				int proj_id = rs.getInt(1);
				int image_id = rs.getInt(2);
				String loc = rs.getString(3);
				int is_available1 = rs.getInt(4);
				result.add(new Batch(proj_id, image_id, loc, is_available1));
			}
			
			rs.close();
			stmt.close();
		}
		
		catch (SQLException e) 
		{
			throw new SQLException("getAll Batches failed", e);
		}	
		return result;	
	}
	
	
	/**
	 * Adds a batch to the table
	 * @param batch : batch to be added
	 * @throws SQLException : SQL problems
	 */
	public void add(Batch batch) throws SQLException 
	{
		PreparedStatement stmt = null;
		try 
		{	
			String query = "insert into batch (project_id, url, is_available) values (?, ?, ?)";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1, batch.getProject_id());
			stmt.setString(2, batch.getUrl());
			stmt.setInt(3, batch.getIs_available());
			stmt.executeUpdate();
			stmt.close();
		}
		catch (SQLException e) {
			throw new SQLException("Could not insert Batch table", e);
		}
		
	}

	
	/**
	 * Gets batch by project number
	 * @param findBatchNumber : number of Batch to be retrieved
	 * @return Batch based on batch ID
	 * @throws SQLException : indicates SQL problem
	 */
	public Batch getBatch(int findBatchNumber) throws SQLException
	{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			String query = "select * from batch where image_id =" + findBatchNumber;
			stmt = db.getConnection().prepareStatement(query);
			rs = stmt.executeQuery();
			Batch b = null;
			
			//Checks to see if there is a batch with the specified batch number
			if(rs.next()) 
			{
				int image_temp = rs.getInt("image_id");
				int proj_temp = rs.getInt("project_id");
				String url_temp = rs.getString("url");
				int avail_temp = rs.getInt("is_available");
				stmt.close();
				rs.close();
				return new Batch(image_temp, proj_temp, url_temp, avail_temp);
			}
			stmt.close();
			rs.close();
			return b;
		}
		
		catch (SQLException e) 
		{
			throw new SQLException("Could not get Batch", e);
		}	
	}
	
	/**
	 * Returns a list of batches with are part of a given project
	 * @param proj_id
	 * @return
	 * @throws SQLException
	 */
	public List<Batch> getBatch_byProjectNumber(int proj_id) throws SQLException
	{
		List<Batch> batches = new ArrayList<Batch>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			String query = "select * from batch where project_id =" + proj_id;
			stmt = db.getConnection().prepareStatement(query);
			rs = stmt.executeQuery();
			
			while(rs.next())
			{
				int image_temp = rs.getInt("image_id");
				int proj_temp = rs.getInt("project_id");
				String url_temp = rs.getString("url");
				int avail_temp = rs.getInt("is_available");
				batches.add(new Batch(image_temp, proj_temp, url_temp, avail_temp));
			}
			
			stmt.close();
			rs.close();
			return batches;
		}
		
		catch (SQLException e) 
		{
			throw new SQLException("Could not get Batch", e);
		}	
	}
	
	
	/**
	 * Updates the batch's availability
	 * @param batch : updates the given batch
	 * @param newAvail : updates the availability of the batch by assigning it a user_id
	 * @throws SQLException : indicates SQL error
	 */
	public void updateBatchAvail(int batch_id, int newAvail) throws SQLException
	{
		PreparedStatement stmt = null;
		try
		{
			String query = "update batch set is_available = ? where image_id = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1, newAvail);
			stmt.setInt(2,  batch_id);
			stmt.executeUpdate();
			stmt.close();
		}
		
		catch (SQLException e) 
		{
			throw new SQLException("Could not Update Availability", e);
		}
	}
	
	
	/**
	 * returns the next available batch
	 * @param projectid : searches in a project for an available batch
	 * @return next unassigned Batch
	 * @throws SQLException : indicates SQL problem
	 */
	public Batch getNextAvailableBatch(int projid) throws SQLException
	{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Batch batch = null;
		try
		{
			String query = "select * from batch where project_id =" + projid;
			stmt = db.getConnection().prepareStatement(query);
			rs = stmt.executeQuery();
			
			while(rs.next())
			{
				int img_id = rs.getInt("image_id");
				int proj_id = rs.getInt("project_id");
				String location = rs.getString("url");
				int temp_avail = rs.getInt("is_available");
				
				batch = new Batch(img_id, proj_id, location, temp_avail);
				
				if(batch.getIs_available() == -1)
				{
					rs.close();
					stmt.close();
					return batch;
				}
			}
			//In the case that all batches in a project are unavailable, this returns null
			if(batch.getIs_available() != -1)
			{
				return null;
			}
			
			return batch;
		}
			catch (SQLException e) 
			{
				throw new SQLException("Could not Update Availability", e);
			}
	}
	
	/**
	 * Returns a batch ID number that is associated with the provided URL
	 * This is used in submitting a batch, and allows for a sort of join function to work within SubmitBatch
	 */
	public int getBatch_id_byURL(String url) throws SQLException
	{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			String query = "select * from batch where url = ?" ;
			stmt = db.getConnection().prepareStatement(query);
			stmt.setString(1, url);
			rs = stmt.executeQuery();

			int image_temp = rs.getInt("image_id");
			stmt.close();
			rs.close();
			return image_temp;
		}
		catch (SQLException e) 
		{
			throw new SQLException("Could not get Batch ID by URL", e);
		}	
	}
	
	/**
	 * Returns the first URL of a project
	 * @param projid
	 * @return
	 * @throws SQLException
	 */
	public String getSampleImage(int projid) throws SQLException
	{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			String query = "select * from batch where project_id = ?" ;
			
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1, projid);
			rs = stmt.executeQuery();
			
			if(rs.next())
			{
				String sampleimage = rs.getString("url");
				return sampleimage;
			}
			
			stmt.close();
			rs.close();
			return null;
		}
		catch (SQLException e) 
		{
			throw new SQLException("Could not get Sample Image using Project-ID", e);
		}	
		
	}
	
	
	
}
