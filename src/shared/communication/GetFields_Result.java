package shared.communication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import shared.model.Field;

/**
 * Output:
 * 	Project, Field_ID and Field_Title of each Field in fields
 *  Project ID (contained in Field)
 * @author brian
 *
 */
public class GetFields_Result extends DataTransport_Result implements Serializable
{
	List<Field> fields = new ArrayList<Field>();
	
	public GetFields_Result()
	{
		fields = new ArrayList<Field>();
	}
	
	public GetFields_Result(List<Field> fieldst)
	{
		fields = fieldst;
	}
	
	public void addField(Field field)
	{
		fields.add(field);
	}
	
	public void removeField(Field field)
	{
		fields.remove(field);
	}
	
	public void setFieldsList(List<Field> fieldst)
	{
		this.fields = fieldst;
	}
	
	public List<Field> getFields()
	{
		return fields;
	}

	public String toString()
	{
		if(fields.size() == 0)
		{
			return getMessage();
		}
		
		StringBuilder sb = new StringBuilder();
		for(Field f : fields)
		{
			sb.append(f.getProject_id() + "\n");
			sb.append(f.getField_id() + "\n");
			sb.append(f.getField_title() + "\n");
		}
		return sb.toString();
	}
}
