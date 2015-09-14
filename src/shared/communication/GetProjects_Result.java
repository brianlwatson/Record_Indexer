package shared.communication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import shared.model.Project;
/**
 * Output:
 * 	Project ID and Title of each Project in projects
 * @author brian
 *
 */
public class GetProjects_Result extends DataTransport_Result implements Serializable
{
	private List<Project> projects = new ArrayList<Project>();

	public GetProjects_Result() 
	{
		super();
	}
	
	public List<Project> getProjects() 
	{
		return projects;
	}

	public void setProjects(List<Project> projects1) 
	{	
		projects.addAll(projects1);
	}

	public void GetProjects_Result_addProject(Project p)
	{
		projects.add(p);
	}
	
	public void GetProjects_Result_removeProject(Project p)
	{
		projects.remove(p);
	}
	
	public String toString() throws ClassCastException
	{
		if(getMessage().equals("FAILED\n"))
		{
			return getMessage();
		}
		
		StringBuilder sb = new StringBuilder();
		for(Project p : projects)
		{
			sb.append(p.getProject_id() + "\n");
			sb.append(p.getTitle() + "\n");
		}
		return sb.toString();
	}
}
