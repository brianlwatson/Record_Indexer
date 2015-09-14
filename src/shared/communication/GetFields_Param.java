package shared.communication;

import java.io.Serializable;

import shared.model.User;

/**
 * Input:
 * 	User Name - included in superclass
 *  User Password - included in superclass
 *   Project ID
 * @author brian
 *
 */
public class GetFields_Param extends DataTransport_Param implements Serializable
{
	private int project_ID;

	public int getProject_ID() 
	{
		return project_ID;
	}

	public void setProject_ID(int project_ID) 
	{
		this.project_ID = project_ID;
	}
}
