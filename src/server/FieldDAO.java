package server;

import java.util.*;
import java.sql.*;
import java.util.logging.*;

import server.database.Database;
import shared.model.*;

/**
 * FieldDAO
 * 	getAll Fields
 *  add a Field
 *  get Fields of corresponding project
 * @author brian
 *
 */
public class FieldDAO 
{
	private Database db;

	
	public FieldDAO(Database db) 
	{
		this.db = db;
	}

	/**
	 * Returns a general Field_ID of a Field provided it's local_field_id and the project it belongs to
	 * @param local_fid
	 * @param project_id
	 * @return
	 * @throws SQLException
	 */
	public int getFieldbyLocal(int local_fid, int project_id) throws SQLException
	{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			String query = "select * from  field where local_field_id = ? and project_id = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1, local_fid);
			stmt.setInt(2, project_id);
			rs = stmt.executeQuery();
			int general_field_id = rs.getInt("field_id");
			rs.close();
			stmt.close();
			return general_field_id;
		}
		catch (SQLException e) 
		{
			throw new SQLException("Could not get absolute field", e);
		}	
	}
	
	
	/**
	 * Returns all Fields in the table
	 * @return
	 * @throws SQLException
	 */
	public List<Field> getAll() throws SQLException
	{
		ArrayList<Field> lists = new ArrayList<Field>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try 
		{
			String query = "select * from field";
			stmt = db.getConnection().prepareStatement(query);
			rs = stmt.executeQuery();
			while (rs.next()) 
			{
				int field_id = rs.getInt("field_id");
				int project_id = rs.getInt("project_id");
				int local_field_id = rs.getInt("local_field_id");
				String knowndata = rs.getString("knowndata");
				String field_title = rs.getString("field_title");
				int width = rs.getInt("width");
				int xcoord = rs.getInt("xcoord");
				String helphtml = rs.getString("helphtml");
				Field f = new Field(field_id, project_id, local_field_id, knowndata, field_title, width, xcoord, helphtml);
				lists.add(f);
			}
			rs.close();
			stmt.close();
			return lists;
		}
		
		catch (SQLException e) 
		{
			throw new SQLException("Could not get all", e);
		}	
	}
	
	
	/**
	 * Adds a Field to the database
	 * @param field
	 * @throws SQLException
	 */
	public void add(Field field) throws SQLException 
	{
		PreparedStatement stmt = null;	
		try 
		{
			String query = "insert into field ( project_id, local_field_id, knowndata, field_title, width, xcoord, helphtml) values (  ?, ?, ?, ?, ?, ?, ?)";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1, field.getProject_id());
			stmt.setInt(2, field.getLocal_field_id());
			stmt.setString(3, field.getKnowndata());
			stmt.setString(4, field.getField_title());
			stmt.setInt(5, field.getWidth());
			stmt.setInt(6, field.getXcoord());
			stmt.setString(7, field.getHelphtml());
			stmt.executeUpdate();
			stmt.close();
		}
		
		catch (SQLException e) 
		{
			throw new SQLException("Could not insert field", e);
		}
		
	}
	
	

	/**
	 * gets a list of fields by corresponding project number
	 * @param projid
	 * @return
	 * @throws SQLException
	 */
	public List<Field> getFields_ofProject(int projid) throws SQLException
	{
		List<Field> fields = new ArrayList<Field>();
		PreparedStatement stmt = null;
		try
		{
			String query = "select * from field where project_id = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1, projid);
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next())
			{
				int field_id = rs.getInt("field_id");
				int project_id = rs.getInt("project_id");
				int local_field_id = rs.getInt("local_field_id");
				String knowndata = rs.getString("knowndata");
				String ftitle = rs.getString("field_title");
				int width = rs.getInt("width");
				int xcoord = rs.getInt("xcoord");
				String helphtml = rs.getString("helphtml");
				
				Field f = new Field(field_id, project_id, local_field_id, knowndata,ftitle,width,xcoord, helphtml);
				fields.add(f);
			}
			return fields;
		}
		catch (SQLException e) 
		{
			throw new SQLException("Could not return project's fields", e);
		}
	}
	

	/**
	 * 
	 * @param field_title
	 * @param proj_id
	 * @return
	 * @throws SQLException
	 */
	public int getField_id(String field_title, int proj_id) throws SQLException
	{
		PreparedStatement stmt = null;
		try
		{
			String query = "select * from field where project_id = ? and field_title = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1, proj_id);
			stmt.setString(2, field_title);
			
			ResultSet rs = stmt.executeQuery();	
			int field_id = rs.getInt("field_id");
			stmt.close();
			rs.close();
			return field_id;
		}
		catch (SQLException e) 
		{
			throw new SQLException("Could not return project's fields", e);
		}
	}
	
	/**
	 * Calculates the local field ID based on the field's title and project_ID
	 * 	Used in testing
	 * @param field_title
	 * @param proj_id
	 * @return
	 * @throws SQLException
	 */
	public int calculateLocalFieldID(String field_title, int proj_id) throws SQLException
	{
		List<Field> locals = new ArrayList<Field>();
		try
		{
			locals = getAll();
			for(Field f : locals)
			{
				int i = 1;
				if(f.getProject_id() == proj_id)
				{
					i++;
					if(f.getField_title().equals(field_title))
					{
						return i;
					}
				}
			}
		}
		catch (SQLException e) 
		{
			throw new SQLException("Could not find local ID of Field", e);
		}
		return -1;
	}
	
}
