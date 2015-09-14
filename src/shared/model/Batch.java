package shared.model;

import java.io.Serializable;

public class Batch implements Serializable
{

	/**
	 * SQL Statement for Batch Table
	 *create table batch
	 *image_id integer not null primary key autoincrement,
	 *project_id not null,
	 *url text not null,
	 *is_available integer not null
	 *
	 *Generate getters/setters
	 **/
	
	private int image_id;
	private int project_id;
	private String url;
	private int is_available;
	
	public Batch(int img_id, int proj_id, String loc, int is_available1)
	{
		project_id = proj_id;
		image_id = img_id;
		url = loc;
		is_available = is_available1;
	}

	public int getImage_id() 
	{
		return image_id;
	}

	public void setImage_id(int image_id) 
	{
		this.image_id = image_id;
	}

	public int getProject_id() 
	{
		return project_id;
	}

	public void setProject_id(int project_id)
	{
		this.project_id = project_id;
	}

	public String getUrl() 
	{
		return url;
	}

	public void setUrl(String url) 
	{
		this.url = url;
	}

	public int getIs_available() 
	{
		return is_available;
	}

	public void setIs_available(int is_available) 
	{
		this.is_available = is_available;
	}

	@Override
	public String toString() 
	{
		return "Batch [image_id=" + image_id + ", project_id=" + project_id
				+ ", url=" + url + ", is_available=" + is_available + "]";
	}
	
	
	
}
