package shared.model;

import java.io.Serializable;


/**
 * Represents Cell table
 * indexed_information_id integer primary key autoincrement,
 * cell_id integer integer primary key autoincrement,
 *	batchid integer not null,
 *	fieldid integer not null,
 *	recordnum integer not null,
 *	field_name text not null,
 *	knowndata text not null,
 * Generate Getters/Setters/toString
**/
public class Cell implements Serializable
{
	private int cell_id;
	private int batch_id;
	private int field_id;
	private int recordnum;
	private String field_name;
	private String knowndata;
	
	public Cell(int cell_id, int batch_id, int field_id, int recordnum,
			String field_name, String knowndata) 
	{
		super();
		this.cell_id = cell_id;
		this.batch_id = batch_id;
		this.field_id = field_id;
		this.recordnum = recordnum;
		this.field_name = field_name;
		this.knowndata = knowndata;
	}

	public int getCell_id() 
	{
		return cell_id;
	}

	public void setCell_id(int cell_id) 
	{
		this.cell_id = cell_id;
	}

	public int getBatch_id() 
	{
		return batch_id;
	}

	public void setBatch_id(int batch_id) 
	{
		this.batch_id = batch_id;
	}

	public int getField_id() 
	{
		return field_id;
	}

	public void setField_id(int field_id) 
	{
		this.field_id = field_id;
	}

	public int getRecordnum() 
	{
		return recordnum;
	}

	public void setRecordnum(int recordnum) 
	{
		this.recordnum = recordnum;
	}

	public String getField_name() 
	{
		return field_name;
	}

	public void setField_name(String field_name) 
	{
		this.field_name = field_name;
	}

	public String getKnowndata() 
	{
		return knowndata;
	}

	public void setKnowndata(String knowndata) 
	{
		this.knowndata = knowndata;
	}

	@Override
	public String toString() 
	{
		return "Cell [cell_id=" + cell_id + ", batch_id=" + batch_id
				+ ", field_id=" + field_id + ", recordnum=" + recordnum
				+ ", field_name=" + field_name + ", knowndata=" + knowndata
				+ "]";
	}
	
}
