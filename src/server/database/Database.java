package server.database;

import java.io.*;
import java.sql.*;
import java.util.logging.*;

import server.CellDAO;
import server.FieldDAO;
import server.ProjectDAO;
import server.BatchDAO;
import server.UserDAO;


public class Database 
{
	private static Logger logger;
	
	private ProjectDAO projectDAO;
	private UserDAO userDAO;
	private FieldDAO fieldDAO;
	private BatchDAO batchDAO;
	private CellDAO cellDAO;
	private Connection connection;
	
	static 
	{
		logger = Logger.getLogger("recordindexer");
	}

	public Database() 
	{
		projectDAO = new ProjectDAO(this);
		userDAO = new UserDAO(this);
		fieldDAO = new FieldDAO(this);
		batchDAO = new BatchDAO(this);
		cellDAO = new CellDAO(this);
		connection = null;
	}
	
	public static void initialize() throws DatabaseException {
		try {
			final String driver = "org.sqlite.JDBC";
			Class.forName(driver);
		}
		catch(ClassNotFoundException e) {
			
			DatabaseException serverEx = new DatabaseException("Could not load database driver", e);
			logger.throwing("server.database.Database", "initialize", serverEx);
			throw serverEx; 
		}
	}
	
	public ProjectDAO getProjectDAO() 
	{
		return projectDAO;
	}
	
	public UserDAO getUserDAO()
	{
		return userDAO;
	}
	
	public BatchDAO getBatchDAO()
	{
		return batchDAO;
	}
	
	public FieldDAO getFieldDAO()
	{
		return fieldDAO;
	}
	
	public CellDAO getCellDAO()
	{
		return cellDAO;
	}
	
	public Connection getConnection() {
		return connection;
	}

	public void startTransaction() throws DatabaseException 
	{
		String dbName = "db22.sqlite";
		final String subURL = "database" + File.separator;
		final String URL = "jdbc:sqlite:" + subURL + dbName;
		
		//jdbc:sqlite:database+File.separator+contactmanager.sqlite
		//cotactmanager.sqlite found in database folder
		
		//database/recordindexer.sqlite
		//System.out.println("Path: " + URL);
		try 
		{
			assert (connection == null);
		
			connection = DriverManager.getConnection(URL);
			connection.setAutoCommit(false);
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not connect to database. Make sure " + 
				dbName + " is available in ./" + subURL, e);
		}
		
		
	}
	
	public void endTransaction(boolean commit) 
	{
		if (connection != null) 
		{		
			try 
			{
				if (commit) 
				{
					connection.commit();
				}
				else 
				{
					connection.rollback();
				}
			}
			catch (SQLException e) 
			{
				System.out.println("Could not end transaction");
				e.printStackTrace();
			}
			finally 
			{
				safeClose(connection);
				connection = null;
			}
		}
	}
	
	public static void safeClose(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			}
			catch (SQLException e) {
				// ...
			}
		}
	}
	
	public static void safeClose(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			}
			catch (SQLException e) {
				// ...
			}
		}
	}
	
	public static void safeClose(PreparedStatement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			}
			catch (SQLException e) {
				// ...
			}
		}
	}
	
	public static void safeClose(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			}
			catch (SQLException e) {
				// ...
			}
		}
	}
	
}






