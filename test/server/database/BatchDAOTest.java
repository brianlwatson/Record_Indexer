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

import server.BatchDAO;
import server.database.Database;
import server.database.DatabaseException;
import shared.model.Batch;

/**
 * Tests for each DAO operation are included in the same Test method
 * 		This allowed for more variety in testing. 
 * 
 * Tables are dropped and created at the beginning
 * @author brian
 *
 */
public class BatchDAOTest 
{
	private Database db;
	
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
		//System.out.println("TearDown");
		db.endTransaction(false);
		db = null;
	}

	@Test
	public void test() throws SQLException, DatabaseException 
	{
		db = new Database();
		db.startTransaction();
		BatchDAO batchdao = new BatchDAO(db);
		batchdao = db.getBatchDAO();

		//getAll / add(Batch b) / 
		//batchIsAvailable(Batch batch, int findBatchNumber)
		//getBatch(int findBatchNumber)
		//updateBatchAvail(int batch_id, int newAvail)
		//getNextAvailableBatch()
		
		batchdao.add(new Batch(1,1, "batchdaotest1", 1));
		batchdao.add(new Batch(1,2, "batchdaotest2", 0));
		batchdao.add(new Batch(1,3, "batchdaotest3", 2));
		
		assertEquals(3, batchdao.getAll().size());
		
		batchdao.add(new Batch(999, 4 , "grotto", -1));
		assertEquals(4,batchdao.getBatch(4).getImage_id());
		System.out.println("Passed Incrementing Batch");
		System.out.println("Passed Add");
	
		batchdao.updateBatchAvail(1, -1);
		batchdao.updateBatchAvail(2, 2);
		batchdao.updateBatchAvail(3, -1);
		batchdao.updateBatchAvail(4, 3);
		
		assertEquals(-1, batchdao.getBatch(1).getIs_available());
		assertEquals(2, batchdao.getBatch(2).getIs_available());
		assertEquals(-1, batchdao.getBatch(3).getIs_available());
		assertEquals(3, batchdao.getBatch(4).getIs_available());
		System.out.println("Passed updateBatchAvail");
		System.out.println("Passed batchIsAvailable");
		
		batchdao.updateBatchAvail(3, 12);
		batchdao.add(new Batch(10000, 3, "test3", -1));
		
		assertEquals("test3", batchdao.getNextAvailableBatch(3).getUrl());
		assertEquals(5, batchdao.getAll().size());
		assertEquals(5, batchdao.getNextAvailableBatch(3).getImage_id());
		System.out.println("Passed getNextAvailableBatch");
		
		batchdao.updateBatchAvail(4,-1);
		assertEquals(4, batchdao.getNextAvailableBatch(4).getImage_id());
		assertEquals("batchdaotest1", batchdao.getNextAvailableBatch(1).getUrl());
	
		db.endTransaction(true);
	}

}
