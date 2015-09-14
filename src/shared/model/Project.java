package shared.model;

import java.io.Serializable;

public class Project implements Serializable
{
	/**
	 * Project is going to be the representative of the Project table
	 * 
	 *project_id primary key autoincrement,
	 * title text not null,
	 * numfields not null,
	 * numrecords not null,
	 * ycoord integer not null,
	 * recordheight integer not null,
	 * Generate Getters and Setters
	 **/
	
	private int project_id;
	private String title;
	private int numfields;
	private int numrecords;
	private int ycoord;
	private int recordheight;
	
	public Project(int project_id, String title, int numfields, int numrecords, int ycoord, int recordheight) 
	{
		super();
		this.project_id = project_id;
		this.title = title;
		this.numfields = numfields;
		this.numrecords = numrecords;
		this.ycoord = ycoord;
		this.recordheight = recordheight;
	}
	
	public int getProject_id() 
	{
		return project_id;
	}
	
	public void setProject_id(int project_id)
	{
		this.project_id = project_id;
	}
	
	public String getTitle() 
	{
		return title;
	}
	
	public void setTitle(String title) 
	{
		this.title = title;
	}
	
	public int getNumfields() 
	{
		return numfields;
	}
	
	public void setNumfields(int numfields) 
	{
		this.numfields = numfields;
	}
	
	public int getNumrecords() 
	{
		return numrecords;
	}
	
	public void setNumrecords(int numrecords) 
	{
		this.numrecords = numrecords;
	}
	
	public int getYcoord() 
	{
		return ycoord;
	}
	
	public void setYcoord(int ycoord) 
	{
		this.ycoord = ycoord;
	}
	
	public int getRecordheight() 
	{
		return recordheight;
	}
	
	public void setRecordheight(int recordheight) 
	{
		this.recordheight = recordheight;
	}

	@Override
	public String toString() 
	{
		return "Project [project_id=" + project_id + ", title=" + title
				+ ", numfields=" + numfields + ", numrecords=" + numrecords
				+ ", ycoord=" + ycoord + ", recordheight=" + recordheight + "]";
	}
	

	
	
}