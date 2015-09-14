package server.database;


/**
 * Tests for each DAO operation are included in the same Test method
 * 		This allowed for more variety in testing. 
 * 
 * Tables are dropped and created at the beginning
 * @author brian
 *
 */
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import server.FieldDAO;
import server.database.Database;
import server.database.DatabaseException;
import shared.model.Field;

public class FieldDAOTest 
{
		private Database db;
		private FieldDAO fielddao;
		
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
			db.endTransaction(false);
			db = null;
			fielddao = null;
		}

		@Test
		public void test() throws SQLException, DatabaseException 
		{
			db = new Database();
			db.startTransaction();
			fielddao = db.getFieldDAO();
		
			//Check add
			fielddao.add(new Field(999, 1, 1, "Smith", "Last_name", 200, 300,"help.html"));
			fielddao.add(new Field(100000, 1, 1, "Jones", "Last_name", 250, 400,"help.html"));
			fielddao.add(new Field(6, 2, 2, "Isaac", "First_name", 100, 200,"help.html"));
			
			//Check getall
			assertEquals(3, fielddao.getAll().size());
			System.out.println("Passed GetAll");
			fielddao.add(new Field(11, 2, 3,  "Pritchett", "dumbname", 100,300,"help.html"));
			fielddao.add(new Field(11, 2, 1,"SUP" , "dumbername", 100,400,"help.html"));
			System.out.println("Passed Add");
			
			//Check get Fields by Project
			assertEquals(2, fielddao.getFields_ofProject(1).size());
			assertEquals(3, fielddao.getFields_ofProject(2).size());
			System.out.println("Passed getFields_ofProject");
			assertEquals(0, fielddao.getFields_ofProject(4).size());
			assertEquals(0, fielddao.getFields_ofProject(10).size());
			System.out.println("Passed getFields_ofProjectByField");
			db.endTransaction(true);
		}
}
