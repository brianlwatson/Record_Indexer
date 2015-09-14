package shared.model;

import java.io.Serializable;

/**
 * Field class
 * Represents the field table, with accompanying getters and setters
 * This is going to need more customizable options as Field is going to be widely used
 * @author brian
 *	SQL Table
 	field_id integer primary key autoincrement,
	project_id integer not null,
	knowndata text not null,
	field_title text not null,
	width integer not null,
	xcoord integer not null
 * Generate Getters/Setters/toString
 */

public class Field implements Serializable
{
	private int field_id;
	private int project_id;
	private int local_field_id;
	private String knowndata;
	private String field_title;
	private int width;
	private int xcoord;
	private String helphtml;
	
	public Field(int field_id, int project_id, int local_field_id,
			String knowndata, String field_title, int width, int xcoord,
			String helphtml) 
	{
		super();
		this.field_id = field_id;
		this.project_id = project_id;
		this.local_field_id = local_field_id;
		this.knowndata = knowndata;
		this.field_title = field_title;
		this.width = width;
		this.xcoord = xcoord;
		this.helphtml = helphtml;
	}

	public int getField_id() 
	{
		return field_id;
	}
	
	public void setField_id(int field_id) 
	{
		this.field_id = field_id;
	}
	
	public int getProject_id() 
	{
		return project_id;
	}
	
	public void setProject_id(int project_id) 
	{
		this.project_id = project_id;
	}
	
	public int getLocal_field_id() 
	{
		return local_field_id;
	}
	
	public void setLocal_field_id(int local_field_id) 
	{
		this.local_field_id = local_field_id;
	}
	
	public String getKnowndata()
	{
		return knowndata;
	}
	
	public void setKnowndata(String knowndata) 
	{
		this.knowndata = knowndata;
	}
	
	public String getField_title() 
	{
		return field_title;
	}
	
	public void setField_title(String field_title) 
	{
		this.field_title = field_title;
	}
	
	public int getWidth() 
	{
		return width;
	}
	
	public void setWidth(int width) 
	{
		this.width = width;
	}
	
	public int getXcoord() 
	{
		return xcoord;
	}
	
	public void setXcoord(int xcoord) 
	{
		this.xcoord = xcoord;
	}
	
	public String getHelphtml()
	{
		return helphtml;
	}
	
	public void setHelphtml(String helphtml) 
	{
		this.helphtml = helphtml;
	}

	@Override
	public String toString() 
	{
		return "Field [field_id=" + field_id + ", project_id=" + project_id
				+ ", local_field_id=" + local_field_id + ", knowndata="
				+ knowndata + ", field_title=" + field_title + ", width="
				+ width + ", xcoord=" + xcoord + ", helphtml=" + helphtml + "]";
	}
	
	
	
	

	
	
}
