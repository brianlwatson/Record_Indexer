package server.database;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import server.UserDAO;
import server.database.Database;
import server.database.DatabaseException;
import shared.model.User;

/**
 * Test User model class and UserDAO Test
 * @author brian
 *
 */

public class UserDAOTest {

	private Database db;
	private UserDAO userdao;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception 
	{
		Database.initialize();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		return;
	}

	@Before
	public void setUp() throws Exception 
	{
		db = new Database();
		db.startTransaction();
		Statement stmt = null;
		Connection conn = db.getConnection();
		conn.setAutoCommit(false);
		stmt = conn.createStatement();
		stmt.addBatch("drop table if exists user");
		stmt.addBatch("drop table if exists field");
		stmt.addBatch("drop table if exists project");
		stmt.addBatch("drop table if exists batch");
		stmt.addBatch("drop table if exists cell");
		
		stmt.addBatch("create table project (project_id integer primary key autoincrement," +
				"title text not null, numfields integer not null, numrecords integer not null," +
				"ycoord integer not null, recordheight integer not null);");
		
		stmt.addBatch("create table batch (image_id integer not null primary key autoincrement," +
				"project_id integer not null, url text not null,is_available integer not null);");
		
		stmt.addBatch("create table cell( cell_id integer primary key autoincrement, batch_id integer not null," +
				"field_id integer not null, recordnum integer not null, field_name text not null,knowndata text not null);");
		
		stmt.addBatch("create table field( field_id integer primary key autoincrement, project_id integer not null,"+
				"local_field_id integer not null, knowndata text not null, field_title text not null, width integer not null, xcoord integer not null, helphtml text);");

		stmt.addBatch("create table user( user_id integer primary key autoincrement, username text not null," +
				"first_name text not null, last_name text not null, password text not null, num_records integer not null," +
				"email text not null, batchassig integer not null);");
		
		stmt.executeBatch();
		stmt.close();
		db.endTransaction(true);
	}

	@After
	public void tearDown() throws Exception {
		db = null;
		userdao = null;
	}

	@Test
	public void test() throws SQLException, DatabaseException 
	{
		db = new Database();
		db.startTransaction();
		
		//Verify Getters and Setters
		User u = new User (1, "george11" , "george" , "smith" , "jungle", 0 , "george@yahoo.com", 0 );
		assertEquals(1, u.getUser_id());
		assertEquals("george11" , u.getUsername());
		assertEquals("george" , u.getFirst_name());
		assertEquals("smith", u.getLast_name());
		assertEquals("jungle", u.getPassword());
		assertEquals(0, u.getNum_records());
		assertEquals("george@yahoo.com", u.getEmail());
		assertEquals(0 , u.getBatchassig());
		
		User t = new User (2, "asdf", "fred" , "smith" , "margh" , 2, "fred@asdf.com" , 4);
		
		//Test add
		userdao = db.getUserDAO();
		userdao.add(u);
		assertEquals(1, userdao.getAll().size()); 
		userdao.add(t);
		assertEquals(2, userdao.getAll().size()); System.out.println("Passed Add");
		
		//Test getAll
		for(User x: userdao.getAll())
		{
			userdao.delete(x);
		}
		assertEquals(0, userdao.getAll().size()); System.out.println("Passed Delete");
		
		User tt = new User (2, "asdf", "fred" , "smith" , "margh" , 2, "fred@asdf.com" , 4);
		User uu = new User (1, "george11" , "george" , "smith" , "jungle", 0 , "george@yahoo.com", 0 );
		
		userdao.add(tt);
		userdao.add(uu);
		
		//Test change Batch assignation
		userdao.user_changeBatchAssignation("asdf", 7);
		userdao.user_changeBatchAssignation("george11", 11);
		assertEquals(7, userdao.getUserforValidate("asdf", "margh").getBatchassig());
		assertEquals(11, userdao.getUserforValidate("george11", "jungle").getBatchassig());

		//Test get User for Validate
		assertEquals(2, userdao.getAll().size());
		assertEquals("george", userdao.getUserforValidate("george11", "jungle").getFirst_name());
		assertEquals(null, userdao.getUserforValidate("asdf", "asdf"));
		System.out.println("Passed getUserforValidate");
		db.endTransaction(true);
	}
	
}





