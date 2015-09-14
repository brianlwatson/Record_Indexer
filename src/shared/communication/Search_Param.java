package shared.communication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Search_Param
 * When searching, a user will type in a given field, this field will be parsed and added into field_values
 * Specs state that the fields to be searched are contained in Field ID
 * return a list of all matches and the locations of the fields
 * @author brian
 *
 */
public class Search_Param extends DataTransport_Param implements Serializable
{
	List<String> field_values = new ArrayList<String>();
	List<Integer> field_IDs = new ArrayList<Integer>();
	
	public List<String> getField_values() 
	{
		return field_values;
	}
	
	public void setField_values(List<String> field_values) 
	{
		this.field_values = field_values;
	}
	
	public List<Integer> getField_IDs() 
	{
		return field_IDs;
	}
	
	public void setField_IDs(List<Integer> field_IDs) 
	{
		this.field_IDs = field_IDs;
	}
	
	public void addValue(String s)
	{
		field_values.add(s);
	}
	
	public void addID(String s)
	{
		field_IDs.add(Integer.parseInt(s));
	}
	
	public void addAllValues(String[] s)
	{
		for(String s1: s)
		{
			try
			{
				field_values.add(s1.toLowerCase());
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage() + "NOTWORKING");
			}
		}
	}
	
	public void addAllIDs(String[] ids)
	{
		for(String i : ids)
		{
			field_IDs.add(Integer.parseInt(i));
		}
	}
	
	//Search_Param toString() was created only for testing purposes
	//For this reason it uses two stringbuilders
	public String toString() 
	{
		StringBuilder sb = new StringBuilder();
		for(String s2: field_values)
		{
			sb.append(s2 +" ");
		}
		
		StringBuilder sb2 = new StringBuilder();
		for(int s1: field_IDs)
		{
			sb2.append(s1 + " ");
		}
		
		return "Search_Param [field_values=" + sb.toString() + "\t field_IDs="
				+ sb2.toString() + "]";
	}
}
