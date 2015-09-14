package server;


import java.util.*;
import java.sql.*;
import java.util.logging.*;

import server.database.Database;
import server.database.DatabaseException;
import shared.model.*;
/**
 * UserDAO:
 * 	Needs to get user by name and password for validate
 *  Increment the number of records indexed
 *  Give a new batch assignation
 *  
 * @author brian
 *
 */

public class UserDAO 
{
	private Database db;
	
	public UserDAO(Database db) 
	{
		this.db = db;
	}
	
	/**
	 * Adds a new user to the table
	 * @param user : user to be added
	 * @throws SQLException
	 */
	public void add(User user) throws SQLException 
	{
		PreparedStatement stmt = null;
		try 
		{			
			String sql = "insert into user" + "(username, first_name, last_name, password, num_records, email, batchassig)" + "values (?, ?, ?, ?, ?, ?, ?)";
			stmt = db.getConnection().prepareStatement(sql);
			stmt.setString(1, user.getUsername());
			stmt.setString(2, user.getFirst_name());
			stmt.setString(3, user.getLast_name());
			stmt.setString(4, user.getPassword());
			stmt.setInt(5, user.getNum_records());
			stmt.setString(6, user.getEmail());
			stmt.setInt(7, user.getBatchassig());
			stmt.executeUpdate();
			stmt.close();
		}
		catch (SQLException e) 
		{
			throw new SQLException("Could not insert User", e);
		}
		
	}
	
	/**
	 * Increments the number of records the user has indexed by 1
	 * @param user
	 * @throws SQLException
	 */
	public void user_IncRecords(User user) throws SQLException
	{
		PreparedStatement stmt = null;
		try
		{
			String query = "update user set num_records = ? where user_id = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1,  user.getNum_records() + 1);
			stmt.setInt(2,  user.getUser_id());
			stmt.executeUpdate();
			stmt.close();
		}
		catch (SQLException e) 
		{
			throw new SQLException("Could not update user's number of records", e);
		}
	}
	
	/**
	 * Increments the user's number of indexed records by @param total
	 * @param user
	 * @param total
	 * @throws SQLException
	 */
	public void user_IncRecords_byTotal(User user, int total) throws SQLException
	{
		PreparedStatement stmt = null;
		try
		{
			String query = "update user set num_records = ? where user_id = ?";
			stmt = db.getConnection().prepareStatement(query);
			int toadd = user.getNum_records() + total;
			stmt.setInt(1, toadd);
			stmt.setInt(2, user.getUser_id());
			stmt.executeUpdate();
			stmt.close();
		}
		catch (SQLException e) 
		{
			throw new SQLException("Could not update user's number of records", e);
		}
	}

	
	/**
	 * Changes the batch assignation field of the user
	 * @param user
	 * @param newbatchnumber
	 * @throws SQLException
	 */
	public void user_changeBatchAssignation(String username, int newbatchnumber) throws SQLException
	{
		PreparedStatement stmt = null;
		try
		{
			String query = "update user set batchassig = ? where username = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1,  newbatchnumber);
			stmt.setString(2, username);
			stmt.executeUpdate();
			stmt.close();
		}
		catch (SQLException e) 
		{
			throw new SQLException("Could not update user's number of records", e);
		}
	}
	
	
	/**
	 * Returns a user based on username and password
	 * @param username
	 * @param pwd
	 * @return
	 * @throws SQLException
	 * @throws DatabaseException 
	 */
	public User getUserforValidate(String user_name, String pwd) throws SQLException, DatabaseException
	{
		Connection connection = db.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		User u = null;
		
		try
		{
			String query = "select * from user where username = ? AND password = ?";
			stmt = connection.prepareStatement(query);
			stmt.setString(1, user_name);
			stmt.setString(2, pwd);
			rs = stmt.executeQuery();
			
			if(rs.next())
			{
				int user_id = rs.getInt("user_id");
				String first_name = rs.getString("first_name");
				String last_name = rs.getString("last_name");
				int num_records = rs.getInt("num_records");
				String email = rs.getString("email");
				int batchassig = rs.getInt("batchassig");
				u = new User(user_id, user_name, first_name, last_name, pwd, num_records,email, batchassig);
			}
			stmt.close();
			rs.close();
		}
		catch (SQLException e) 
		{
			throw new SQLException("Could not update user's number of records", e);
		}
		
		return u;
	}
	
	/**
	 * Returns a list of all Users in the table
	 * @return
	 * @throws SQLException
	 */
	public List<User> getAll() throws SQLException
	{
		List<User> users = new ArrayList<User>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try
		{
			String query = "select * from user";
			stmt = db.getConnection().prepareStatement(query);
			rs = stmt.executeQuery();
			while(rs.next())
			{
				int user_id = rs.getInt("user_id");
				String user_name = rs.getString("username");
				String first_name = rs.getString("first_name");
				String last_name = rs.getString("last_name");
				String password = rs.getString("password");
				int num_records = rs.getInt("num_records");
				String email = rs.getString("email");
				int batchassig = rs.getInt("batchassig");
				User u = new User(user_id, user_name, first_name, last_name, password, num_records,email, batchassig);
				users.add(u);
			}
			stmt.close();
			rs.close();
			return users;
		}
		
		catch (SQLException e) 
		{
			throw new SQLException("Could not get all users", e);
		}
	}
	
	
	/**
	 * Deletes a user from the table
	 * 	This is only used for testing purposes! Not necessary in the actual implementation
	 */
	public void delete(User user) throws DatabaseException 
	{
		PreparedStatement stmt = null;
		try 
		{
			String query = "delete from user where user_id = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1, user.getUser_id());
			stmt.executeUpdate();
			stmt.close();
		}
		catch (SQLException e) 
		{
			throw new DatabaseException("Could not delete contact", e);
		}
	}
	
}
