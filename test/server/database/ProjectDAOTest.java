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

import server.ProjectDAO;
import server.database.Database;
import server.database.DatabaseException;
import shared.model.Project;
/**
 * Tests for each DAO operation are included in the same Test method
 * 		This allowed for more variety in testing. 
 * 
 * Tables are dropped and created at the beginning
 * @author brian
 *
 */
public class ProjectDAOTest 
{

	private Database db;
	private ProjectDAO projectdao;
	
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
	public void setUp() throws Exception {
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
		//System.out.println("TearDown");
		db.endTransaction(false);
		db = null;
		projectdao = null;
	}

	@Test
	public void test() throws SQLException, DatabaseException 
	{
		db = new Database();
		db.startTransaction();
		projectdao = new ProjectDAO(db);
		//getAll / add / getProject(projid) / getProjectbyTitle(title)
		//Project (int project_id, Str title, int numfields, int numrecords, int ycoord, int recordheight)

		Project p = new Project(0, "1900 Census", 4, 3, 150, 100);
		Project q = new Project(1, "1950", 6,5,300,200);
		
		projectdao.add(p);
		projectdao.add(q);
	
		assertEquals(2, projectdao.getAll().size());
		
		//Test add
		projectdao.add(new Project(2, "1990", 7,6,400,300));
		projectdao.add(new Project(3, "2000", 8, 7, 400,100));
		
		//Test getAll
		assertEquals(4, projectdao.getAll().size());
		System.out.println("Add successful");

		//Test get project by project_id
		assertEquals(4, projectdao.getProject(1).getNumfields());
		assertEquals(300, projectdao.getProject(3).getRecordheight());
		assertEquals(7, projectdao.getProject(4).getNumrecords());
		assertEquals("1900 Census", projectdao.getProject(1).getTitle());
		System.out.println("Get Project by ID Number Successful");
		assertEquals(4, projectdao.getAll().size());

		projectdao.add(new Project(7, "2010", 5,4,300,200));
		projectdao.add(new Project(9999, "2020", 3,2,500,500));
		System.out.println("Project ID's correct");
		assertEquals(6, projectdao.getAll().size());
		System.out.println("Testing Complete - ProjectDAOTest success!");
		
		System.out.println("Total users: " + projectdao.getAll().size());
		db.endTransaction(true);
	}
	
}
	