package shared.communication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import shared.model.Batch;
import shared.model.Field;
import shared.model.Project;

/**
 * Output:
 * Batch (and included fields)
 * Fields Included in the batch(as objects, not strings)
 * Dimensions (included in the project)
 * Message - included in Super Class
 * @author brian
 *
 */

public class DownloadBatch_Result extends DataTransport_Result implements Serializable
{
	Batch batch;
	Project project;
	List<Field> fields = new ArrayList<Field>();
	
	
	public Batch getBatch() 
	{
		return batch;
	}

	public void setBatch(Batch batch) 
	{
		this.batch = batch;
	}

	public Project getProject() 
	{
		return project;
	}

	public void setProject(Project project) 
	{
		this.project = project;
	}

	public List<Field> getFields() 
	{
		return fields;
	}

	public void setFields(List<Field> fields) 
	{
		this.fields = fields;
	}
	
	public String toString()
	{
		if(getMessage().equals("FAILED\n"))
		{
			return getMessage();
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(batch.getImage_id() + "\n");
		sb.append(project.getProject_id() + "\n");
		sb.append(getDestination() + batch.getUrl()+"\n");
		sb.append(project.getYcoord() + "\n");
		sb.append(project.getRecordheight() + "\n");
		sb.append(project.getNumrecords() + "\n");
		sb.append(project.getNumfields() + "\n");
		
		
		for(Field f : fields)
		{
			sb.append(f.getField_id() + "\n");
			sb.append(f.getLocal_field_id() + "\n");
			sb.append(f.getField_title() + "\n");
			sb.append(getDestination() + f.getHelphtml() + "\n");
			sb.append(f.getXcoord() + "\n");
			sb.append(f.getWidth() + "\n");
			
			if(f.getKnowndata().length() > 0)
			{
				sb.append(getDestination() + f.getKnowndata() + "\n");
			}
		}
		
		return sb.toString();
	}
	
}
