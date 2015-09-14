package shared.communication;

import java.io.Serializable;

import shared.model.User;

/**
 * Input:
 * 	User's name and password - included in superclass
 * 	Project ID
 * @author brian
 *
 */
public class GetSampleImage_Param extends DataTransport_Param implements Serializable
{
	private int project_id;
	
	public int getProject_id() 
	{
		return project_id;
	}

	public void setProject_id(int project_id) 
	{
		this.project_id = project_id;
	}
	
}
