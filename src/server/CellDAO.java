package server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import server.database.Database;
import shared.model.Cell;

/**
 * Contains:
 * 	getAll()
 *  add()
 *  getCellsByField()
 *  updateCell()
 * @author brian
 *
 */
public class CellDAO 
{
	private Database db;
	
	public CellDAO(Database db) 
	{
		this.db = db;
	}
	
	/**
	 * Returns all Cells inside the database
	 * @return
	 * @throws SQLException
	 */
	public List<Cell> getAll() throws SQLException
	{
		List<Cell> cells = new ArrayList<Cell>();
		PreparedStatement stmt = null;
		Connection conn = db.getConnection();
		try
		{
			String query = "select * from cell";
			stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();

			while(rs.next())
			{
				int cell_id = rs.getInt("cell_id");
				int batch_id = rs.getInt("batch_id");
				int field_id = rs.getInt("field_id");
				int recordnum = rs.getInt("recordnum");
				String field_name = rs.getString("field_name");
				String knowndata = rs.getString("knowndata");
				Cell c = new Cell(cell_id , batch_id, field_id, recordnum, field_name, knowndata);
				cells.add(c);
			}
			rs.close();
			stmt.close();
			return cells;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new SQLException("Cell get all failure", e);
		}
	}
	
	/**
	 * Adds a cell to the database
	 * @param cell
	 * @throws SQLException
	 */
	public void add(Cell cell) throws SQLException
	{
		PreparedStatement stmt = null;
		try
		{
			String query = "insert into cell (batch_id, field_id, recordnum, field_name, knowndata) values (?, ?, ?, ?, ?)";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1, cell.getBatch_id());
			stmt.setInt(2, cell.getField_id());
			stmt.setInt(3,  cell.getRecordnum());
			stmt.setString(4,cell.getField_name());
			stmt.setString(5, cell.getKnowndata());
			stmt.executeUpdate();
			stmt.close();
		}
		catch(SQLException e)
		{
			throw new SQLException("CellDAO add failure", e);
		}
	}
	
	/**
	 * Returns a list of cells pertaining to a specific field
	 * @param field_id
	 * @return
	 * @throws SQLException
	 */
	public List<Cell> getCellsbyField(int field_id) throws SQLException
	{
		List<Cell> cells = new ArrayList<Cell>();
		PreparedStatement stmt = null;
		try
		{		
			String query = "select * from cell where field_id = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1, field_id);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next())
			{
				int cell_id1 = rs.getInt("cell_id");
				int batch_id = rs.getInt("batch_id");
				int recordnum = rs.getInt("recordnum");
				String field_name = rs.getString("field_name");
				String knowndata = rs.getString("knowndata");
				
				Cell c = new Cell(cell_id1, batch_id, field_id, recordnum, field_name, knowndata);
				cells.add(c);
			}
			rs.close();
			stmt.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw new SQLException("Cell get by fields failure", e);
		}
		return cells;
	}
	
	/**
	 * Returns a cell by its corresponding location in the table
	 * Batch_id, Field_id, and recordnumber provide which batch, column and row the cell is found inside the cell table
	 * @param batch_id - batch_id
	 * @param field_id - field_id
	 * @param rec_num - recordnumber
	 * @return
	 * @throws SQLException
	 */
	public Cell getCellbyLocation(int batch_id, int field_id, int rec_num) throws SQLException
	{
		PreparedStatement stmt = null;
		Connection conn = db.getConnection();
		ResultSet rs = null;
		try
		{
			String update = "select * from cell where (batch_id = ? AND field_id = ? AND recordnum = ?)";
			stmt = conn.prepareStatement(update);
			stmt.setInt(1, batch_id);
			stmt.setInt(2, field_id);
			stmt.setInt(3, rec_num);
			rs = stmt.executeQuery();
		
			int cell_id = rs.getInt("cell_id");
			String fieldname = rs.getString("field_name");
			String knowndata = rs.getString("knowndata");

			rs.close();
			stmt.close();
			return new Cell(cell_id, batch_id, field_id, rec_num, fieldname, knowndata);
		}
		catch(SQLException e)
		{
			throw new SQLException("Cell location retrieval failure", e);
		}
	}
	
	
	/**
	 * Updates the cell at a specific location, give the batch_id, field_id, and recordnumber
	 * @param knowndata
	 * @param batch_id
	 * @param field_id
	 * @param rec_num
	 * @throws SQLException
	 */
	public void updateCell(String knowndata, int batch_id, int field_id, int rec_num) throws SQLException
	{
		PreparedStatement stmt = null;
		Connection conn = db.getConnection();
		try
		{
			String update = "update cell set knowndata = ? where (batch_id = ? and field_id = ? and recordnum = ?)";
			stmt = conn.prepareStatement(update);
			stmt.setString(1, knowndata);
			stmt.setInt(2, batch_id);
			stmt.setInt(3, field_id);
			stmt.setInt(4, rec_num);	
			stmt.executeUpdate();
			stmt.close();
		}
		catch(SQLException e)
		{
			throw new SQLException("Cell update failure", e);
		}
		
	}
	
}
