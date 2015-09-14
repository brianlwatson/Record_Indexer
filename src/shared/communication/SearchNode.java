package shared.communication;

import java.io.Serializable;

/**
 * Pairs the SearchResult input into a SearchNode
 * 	Each SearchNode relates to one match
 * @author brian
 *
 */
public class SearchNode implements Serializable
{
	private int batch_id;
	private String image_url;
	private int record_num;
	private int field_id;
	
	public SearchNode(int batch_id, String image_url, int record_num,int field_id) 
	{
		super();
		this.batch_id = batch_id;
		this.image_url = image_url;
		this.record_num = record_num;
		this.field_id = field_id;
	}
	
	public String toString(String url)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(batch_id + "\n");
		sb.append(url +image_url + "\n");
		sb.append(record_num + "\n");
		sb.append(field_id + "\n");
		return sb.toString();
	}

	public int getBatch_id() 
	{
		return batch_id;
	}
	
	public void setBatch_id(int batch_id) 
	{
		this.batch_id = batch_id;
	}
	
	public String getImage_url() 
	{
		return image_url;
	}
	
	public void setImage_url(String image_url) 
	{
		this.image_url = image_url;
	}
	
	public int getRecord_num() 
	{
		return record_num;
	}
	
	public void setRecord_num(int record_num) 
	{
		this.record_num = record_num;
	}
	
	public int getField_id() 
	{
		return field_id;
	}
	
	public void setField_id(int field_id) 
	{
		this.field_id = field_id;
	}

	
	

}
