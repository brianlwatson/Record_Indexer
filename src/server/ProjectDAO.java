package server;

import java.util.*;
import java.sql.*;
import java.util.logging.*;

import server.database.Database;
import server.database.DatabaseException;
import shared.model.*;

/**
 * ProjectDAO
 * 	getAll Projects
 *  add project
 *  return project by ID
 * @author brian
 *
 */
public class ProjectDAO 
{
	private Database db;
	
	public ProjectDAO(Database db) 
	{
		this.db = db;
	}
	
	/**
	 * Returns all projects in the database
	 * @return
	 * @throws DatabaseException
	 */
	public List<Project> getAll() throws DatabaseException 
	{
		Connection connection = db.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<Project> result = new ArrayList<Project>();
		try 
		{
			String query = "select * from project";
			stmt = connection.prepareStatement(query);
			rs = stmt.executeQuery();
			
			while (rs.next()) 
			{
				int project_id = rs.getInt("project_id");
				String title = rs.getString("title");
				int numfields = rs.getInt("numfields");
				int numrecords = rs.getInt("numrecords");
				int ycoord = rs.getInt("ycoord");
				int recordheight = rs.getInt("recordheight");
				Project p = new Project(project_id, title, numfields, numrecords, ycoord, recordheight);
				result.add(p);
			}
			
			stmt.close();
			rs.close();
			return result;
		}
		
		catch (SQLException e) 
		{
			throw new DatabaseException(e.getMessage(), e);
		}		
	}
	
	
	/**
	 * adds a project to the table
	 * @param project
	 * @throws SQLException
	 */
	public void add(Project project) throws SQLException 
	{
		PreparedStatement stmt = null;	
		try 
		{
			String query = "insert into project ( title, numfields, numrecords, ycoord, recordheight) values (?, ?, ?, ?, ?)";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setString(1, project.getTitle());
			stmt.setInt(2, project.getNumfields());
			stmt.setInt(3, project.getNumrecords());
			stmt.setInt(4, project.getYcoord());
			stmt.setInt(5, project.getRecordheight());
			stmt.executeUpdate();
			stmt.close();
		}
		catch (SQLException e) 
		{
			throw new SQLException("Could not add new project onto table", e);
		}
		
	}
	
	/**
	 * Returns a project by ID number
	 * @param projid
	 * @return
	 * @throws SQLException
	 */
	public Project getProject(int projid) throws SQLException
	{
		PreparedStatement stmt = null;
		try
		{
			String query = "select * from project where project_id = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1,projid);
			ResultSet rs = stmt.executeQuery();
			
			String title = rs.getString("title");
			int numfields = rs.getInt("numfields");
			int numrecords = rs.getInt("numrecords");
			int ycoord = rs.getInt("ycoord");
			int recordheight = rs.getInt("recordheight");
			stmt.close();
			rs.close();
			
			return new Project(projid, title, numfields, numrecords, ycoord, recordheight);
		}
		catch (SQLException e) 
		{
			throw new SQLException("Could not find project by ID", e);
		}
	}
	
}

