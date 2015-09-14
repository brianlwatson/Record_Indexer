package dataimporter;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import server.BatchDAO;
import server.CellDAO;
import server.FieldDAO;
import server.ProjectDAO;
import server.UserDAO;
import server.database.Database;
import server.database.DatabaseException;
import shared.model.*;
import server.database.*;
import server.*;

import org.w3c.dom.*;

/**
 * Handles all of the adding to the database
 * Steps:
 * 	1. create methods for add (each model)
 * 	2. create tables (include drop statements)
 * 		*use statement, you aren't setting anything so you don't need to prepare it
 * 		*this is really going to mirror your database.sqlite file
 *  3. set up your program so that it can read in the information from the XML file
 *  	*this is where your main is going to go
 *  4. establish a root and then pass it into the processes
 * @author brian
 *
 */
public class DataImporter 
{
	private static Database db;
	
	public DataImporter() throws DatabaseException, SQLException
	{
		db = new Database();
		Database.initialize();
	}
	
	public DataImporter(Database db) 
	{
		DataImporter.db = db;
	}
	
	/*
	 * Each of the add Functions are add the associated object through the corresponding DAO
	 */
	public void addUser(User u) throws SQLException, DatabaseException
	{
		try
		{
			UserDAO userdao = new UserDAO(db);
			userdao = db.getUserDAO();
			userdao.add(u);
		}
		catch (SQLException e) 
		{
			throw new SQLException("User cannot be added thru dataimporter", e);	
		}	
	}
	
	public void addCell(Cell c) throws SQLException, DatabaseException
	{
		try
		{
			CellDAO celldao = new CellDAO(db);
			celldao = db.getCellDAO();
			celldao.add(c);
		}
		catch (SQLException e)
		{
			throw new SQLException("Cell cannot be added thru dataimporter", e);	
		}	
	}
	
	public void addBatch(Batch b) throws SQLException, DatabaseException
	{
		try
		{
			BatchDAO batchdao = new BatchDAO(db);
			batchdao = db.getBatchDAO();
			batchdao.add(b);
		}
		catch (SQLException e) 
		{
			throw new SQLException("Batch cannot be added thru dataimporter", e);	
		}	
	}
	
	public void addField(Field f) throws SQLException, DatabaseException
	{
		try 
		{
			FieldDAO fielddao = new FieldDAO(db);
			fielddao = db.getFieldDAO();
			fielddao.add(f);
		}
		catch (SQLException e) 
		{
			throw new SQLException("Field cannot be added thru dataimporter", e);	
		}	
	}
	
	public void addProject(Project p) throws SQLException, DatabaseException
	{
		try
		{
			ProjectDAO projectdao = new ProjectDAO(db);
			projectdao = db.getProjectDAO();
			projectdao.add(p);
		}
		catch (SQLException e)
		{
			throw new SQLException("project cannot be added thru dataimporter", e);
		}
	}
	
	
	/**
	 * public void EstablishTables()
	 * Tables are dropped and rebuilt in the .sqlite database
	 * @throws DatabaseException
	 * @throws SQLException 
	 */
	public void EstablishTables() throws DatabaseException, SQLException
	{
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
	}
		
	/**
	 * Main(String args[]
	 * Drops old tables and creates new information
	 * Handles the database transactions for the whole class
	 * Provides the root for XML parsing
	 */
	public static void main(String args[]) throws Exception
	{
		DataImporter dif = new DataImporter();
		db.startTransaction();
		dif.EstablishTables();
		Element root;
		
		if(args.length == 0) // for testing
		{
			root = dif.getRoot("Records.xml");
			copy("Records/Records.xml");
		}
		else // uses the command line argument 
		{
			copy(args[0]);
			root = dif.getRoot(args[0]);	
		}
	
		dif.processProjects(root);
		dif.processUsers(root);
		dif.addEmptyCells();
		dif.processCells(root);
		db.endTransaction(true);
		System.out.println("COMPLETE");
	}
	
	/**
	 * public static String copy
	 * Uses ApacheImporter to copy files into the Records Folder
	 */
	public static String copy(String file)
	{
		File xmlFile = new File(file);
		File dest = new File("Records");
		try
		{
			//	We make sure that the directory we are copying is not the the destination
			//	directory.  Otherwise, we delete the directories we are about to copy.
			if(!xmlFile.getParentFile().getCanonicalPath().equals(dest.getCanonicalPath()))
				FileUtils.deleteDirectory(dest);
				
			//	Copy the directories (recursively) from our source to our destination.
			FileUtils.copyDirectory(xmlFile.getParentFile(), dest);
			
			//	Overwrite my existing *.sqlite database with an empty one.  Now, my
			//	database is completelty empty and ready to load with data.
		}
		catch (IOException e)
		{
			//logger.log(Level.SEVERE, "Unable to deal with the IOException thrown", e);
		}
		
		return dest.getPath() + File.separator + dest.getName();
	}
	
	/**
	 * Steps in loading XML 
	 * 	1. Find the root of the XML
	 * @throws IOException, SAXException 
	 * @throws ParserConfigurationException 
	 */
	
	public Element getRoot(String filename) throws IOException, SAXException, ParserConfigurationException 
	{
		File xmlFile = new File(filename);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		try
		{
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
			doc.getDocumentElement().normalize();
			Element root = (Element) doc.getDocumentElement();
			return root;
		}
		catch(IOException ex)
		{
			throw new IOException("IOException in get Root", ex);
		} 
		catch (SAXException e) 
		{
			e.printStackTrace();
		}
		catch (ParserConfigurationException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Each element of your elements by tag name will represent a project
	 * Note that there are various fields in each project
	 * Note that there are also various images in each project
	 * 
	 * project_id integer primary key autoincrement,
	 * 		title text not null,
	 * 		numfields integer not null,
	 *		numrecords integer not null,
	 * 		ycoord integer not null,
	 * 		recordheight integer not null
	 * 
	 * processProjects(Element e)
	 * Parses and adds projects to the database
	 * @param e
	 * @throws DatabaseException 
	 * @throws SQLException 
	 */
	private void processProjects(Element e) throws SQLException, DatabaseException
	{
		//Project (int project_id, Str title, int numfields, int numrecords, int ycoord, int recordheight)
		NodeList projectlist = e.getElementsByTagName("project");
		for(int i = 0; i < projectlist.getLength();i++)
		{
			int projid = i+1;
			Element project = (Element)projectlist.item(i);
			
			String title = project.getElementsByTagName("title").item(0).getTextContent();
			int numrecords = Integer.parseInt(project.getElementsByTagName("recordsperimage").item(0).getTextContent());
			int ycoord = Integer.parseInt(project.getElementsByTagName("firstycoord").item(0).getTextContent());
			int recordheight = Integer.parseInt(project.getElementsByTagName("recordheight").item(0).getTextContent());
			int numfields = project.getElementsByTagName("field").getLength();
			
			Project p = new Project(projid, title, numfields, numrecords, ycoord, recordheight);
			addProject(p);
			processFields(project, i);
			processBatchs(project, i);
		}
	}
	
	/**
	 * Process Fields
	 * Add new fields to the database
	 * 	this runs after each project is processed in the XML
	 * @param e
	 * @throws SQLException
	 * @throws DatabaseException
	 */
	private void processFields(Element e, int projid) throws SQLException, DatabaseException
	{	
		NodeList fieldlist = e.getElementsByTagName("field");
		for(int j = 0; j < fieldlist.getLength(); j++)
		{
			int project_id = projid + 1;
			int local_field_id = j+1;
			Element field = (Element)fieldlist.item(j);
		
			String field_title = field.getElementsByTagName("title").item(0).getTextContent();
			int width = Integer.parseInt(field.getElementsByTagName("width").item(0).getTextContent());
			int xcoord = Integer.parseInt(field.getElementsByTagName("xcoord").item(0).getTextContent());
			String help_html = field.getElementsByTagName("helphtml").item(0).getTextContent();
			String knowndata = "";
			
			if(field.getElementsByTagName("knowndata").getLength() > 0)
			{
				knowndata = field.getElementsByTagName("knowndata").item(0).getTextContent();
			}
			else
			{
				knowndata = "";
			}
			Field f = new Field(0, project_id, local_field_id, knowndata, field_title, width, xcoord, help_html);
			addField(f);
		}
	}
	
	/**
	 * 
	 * @param e
	 * @throws SQLException
	 * @throws DatabaseException
	 * For each batch, 
	 * 		For each project
	 * 		For each row
	 * 			For each column
	 * 	update the existing empty cell		
	 * 
	 *  processCells(Element e)
	 *  	updates the values of empty cells using known values from the XML
	 */
	private void processCells(Element e) throws SQLException, DatabaseException
	{
		//BatchDAO contains the most information, so you're going to want to get your information from there
		//Cell(int cell_id, int batch_id, int field_id, int recordnum, Str field_name, String knowndata)
			NodeList cells = e.getElementsByTagName("image");
			for(int i = 0; i < cells.getLength(); i++) //For each image in the XML
			{
				NodeList batch_ids = e.getElementsByTagName("file");
				String bi = batch_ids.item(i).getTextContent();
				
				BatchDAO bd = db.getBatchDAO();
				int batch_to = bd.getBatch_id_byURL(bi);
				Batch b = bd.getBatch((i+1));
				List<Field> fields = db.getFieldDAO().getFields_ofProject(b.getProject_id());
				Element cell = (Element) cells.item(i);
				NodeList records = cell.getElementsByTagName("record");
				
				for(int recnum = 0; recnum < records.getLength(); recnum++) //for each record
				{	
					Element record = (Element) records.item(recnum);
					NodeList values = record.getElementsByTagName("value");
					for(int colnum = 0; colnum < values.getLength(); colnum++) //for each cell
					{
						String known = values.item(colnum).getTextContent();
						int colnumone = fields.get(colnum).getField_id();
						int recnumone = recnum + 1;
						db.getCellDAO().updateCell(known, batch_to, colnumone, recnumone);
					}
				}
			}
		}
		
	
	/**
	 * Populates the database with empty cells which can later be updated
	 */
	public void addEmptyCells() throws SQLException
	{
		try
		{
			List<Batch> batches = db.getBatchDAO().getAll();
			for(Batch b : batches)
			{
				Project p = db.getProjectDAO().getProject(b.getProject_id());
				List<Field> fields = db.getFieldDAO().getFields_ofProject(b.getProject_id());
				int rows = p.getNumrecords();
				
				for(int i = 0; i < rows; i++)
				{
					for(Field f : fields)
					{
						int recnum = i + 1;
						Cell cell = new Cell(0, b.getImage_id(), f.getField_id(), recnum, "", "" );
						addCell(cell);
					}
				}
			}
		}
		catch(DatabaseException e)
		{
			db.endTransaction(false);
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Populates the batch table in the database
	 * 		Runs each time a project is processed
	 * @param e
	 * @throws SQLException
	 * @throws DatabaseException
	 */
	private void processBatchs(Element e, int projide) throws SQLException, DatabaseException
	{
			NodeList batchlist = e.getElementsByTagName("image");
			for(int j = 0; j < batchlist.getLength(); j++)
			{
				Element image = (Element)batchlist.item(j);
			
				int projid = projide+1;
				int batchid = j+1;
				String location = image.getElementsByTagName("file").item(0).getTextContent();
				//-1 is the starting batch availability value. It indicates the batch is available
				Batch b = new Batch(batchid, projid,location, -1);
				addBatch(b);
			}
	}
	
	
	/**	
	 * Adds the users to the user table in the database
	 * User(int user_id, Str username, Str first_name, Str last_name, Str password, int num_records, Str email, int batchassig)

	 * @param e
	 * @throws SQLException
	 * @throws DatabaseException
	 */
	public void processUsers(Element e) throws SQLException, DatabaseException
	{
		NodeList userlist = e.getElementsByTagName("user");
		for(int i = 0; i < userlist.getLength(); i++) // this will express user name, 1 is the starting user_id, not 0
		{
			Element user = (Element) userlist.item(i);
			String username = user.getElementsByTagName("username").item(0).getTextContent();
			String password = user.getElementsByTagName("password").item(0).getTextContent();
			String firstname = user.getElementsByTagName("firstname").item(0).getTextContent();
			String lastname = user.getElementsByTagName("lastname").item(0).getTextContent();
			String email = user.getElementsByTagName("email").item(0).getTextContent();
			int indexedrecords = Integer.parseInt(user.getElementsByTagName("indexedrecords").item(0).getTextContent());
			
			User u = new User(i+1, username, firstname, lastname, password, indexedrecords, email, -1);
			addUser(u);
		}
	}
	
}
