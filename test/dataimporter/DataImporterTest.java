package dataimporter;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import dataimporter.DataImporter;
import server.BatchDAO;
import server.database.Database;
import server.database.DatabaseException;
import shared.model.Batch;

public class DataImporterTest {
	


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
		public void setUp() throws Exception {
			db = new Database();
			//you need to clear your database here. otherwise you're going to get wrecked.
			db.startTransaction();
		
		}

		@After
		public void tearDown() throws Exception {
			//System.out.println("TearDown");
			db.endTransaction(false);
			db = null;
		}

		@Test
		public void test() throws Exception 
		{
			db = new Database();
			db.startTransaction();
			
			/**
			 * addUser(User u) 
			 * addProject(Project p)
			 * addCell(Cell c)
			 * addBatch(Batch b)
			 * addField(Field f)
			 * 
			 * EstablishTables()
			 * main()
			 * getRoot()
			 * processProject()
			 * processFields()
			 * 
			 */
			DataImporter di = new DataImporter();
			/*String mer[] = new String[5];
			mer[0] = "Records";
			di.main(mer);*/
			
			di.EstablishTables();
		}

	}


