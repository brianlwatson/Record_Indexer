package shared.communication;

import java.io.Serializable;

import shared.model.User;

/**
 * inputs:
 * 	user name - inherited
 * 	user password - inherited
 * 	project_ID
 * @author brian
 *
 */

public class DownloadBatch_Param extends DataTransport_Param implements Serializable
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
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(getUsername() + "\n");
		sb.append(getPassword() + "\n");
		sb.append(project_id + "\n");
		return sb.toString();
	}
}
