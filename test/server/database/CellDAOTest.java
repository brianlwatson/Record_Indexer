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

import server.CellDAO;
import server.database.Database;
import server.database.DatabaseException;
import shared.model.Cell;

/**
 * Tests for each DAO operation are included in the same Test method
 * 		This allowed for more variety in testing. 
 * 
 * Tables are dropped and created at the beginning
 * @author brian
 *
 */


public class CellDAOTest 
{
	private Database db;
	private CellDAO celldao;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception 
	{
		Database.initialize();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception 
	{
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
	public void tearDown() throws Exception 
	{
		db = null;
		celldao = null;
	}

	@Test
	public void test() throws SQLException, DatabaseException 
	{
		db = new Database();
		db.startTransaction();
		celldao = db.getCellDAO();
		
		//getAll() / getCellsbyField(int field_id) // changeCellValue(field_id, record_id, str known)
		//add
		
		//Test Add
		celldao.add(new Cell(9999, 1, 1, 1, "first_name", "Brian"));
		celldao.add(new Cell(999, 1, 2, 4, "last_name", "Watson"));
		celldao.add(new Cell(123, 1, 3, 3, "username", "watsonb"));
		
		assertEquals(3, celldao.getAll().size());
		System.out.println("Passed Add");
		
		celldao.add(new Cell(0123, 1, 1, 3, "first_name", "Isaac"));
		celldao.add(new Cell(12, 1,1,4, "first_name", "Blaine"));
		celldao.add(new Cell(123, 1,1,5,"first_name" , "food"));
		
		//Verify the size of cell's
		assertEquals(4, celldao.getCellsbyField(1).size());
		assertEquals(1, celldao.getCellsbyField(2).size());
		assertEquals(0, celldao.getCellsbyField(6).size());
		
		celldao.add(new Cell(0, 100,100,100, "invalid", "invalidzies"));
		assertEquals(1, celldao.getCellsbyField(100).size());
		
		//check each cell by comparing field name
		for(Cell c : celldao.getCellsbyField(1))
		{
			assertEquals("first_name" , c.getField_name());
		}
		System.out.println("Passed getCellsbyField");
		
		//check each cell by known data
		assertEquals("Isaac", celldao.getCellbyLocation(1,1,3).getKnowndata());
		assertEquals("Blaine", celldao.getCellbyLocation(1,1,4).getKnowndata());
		System.out.println("Passed getCellbyLocation");
		
		//check update cell
		Cell c = new Cell(123, 1, 1, 3, "not_first_name", "James");
		celldao.updateCell("not_first_name", 1,1,3);
		assertEquals("not_first_name", celldao.getCellbyLocation(1,1,3).getKnowndata());
		System.out.println("Passed updateCell");
		db.endTransaction(true);
	}
}
