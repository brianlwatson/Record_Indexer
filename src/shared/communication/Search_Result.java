package shared.communication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Searches the indexed records for the specified strings
 *The user specifies one or more fields to be searched, and one or more strings to search for. The fields to
 *be searched are specified by “field ID”. (Note, field IDs are unique across all fields in the system.)
 *The Server searches all indexed records containing the specified fields for the specified strings, and
 *returns a list of all matches. In order to constitute a match, a value must appear in one of the search
 *fields, and be exactly equal (ignoring case) to one of the search strings.
 *
 *SearchResults are contained in SearchNode objects which effectively pair the corresponding information
 * @author brian
 *
 */

public class Search_Result extends DataTransport_Result implements Serializable
{
	private List<SearchNode> nodes = new ArrayList<SearchNode>();
	
	public void addNode(SearchNode n)
	{
		nodes.add(n);
	}
	
	public List<SearchNode> getNodes()
	{
		return nodes;
	}
	
	public String toString()
	{
		if(nodes.size() == 0)
		{
			return "FAILED\n";
		}
		
		StringBuilder sb = new StringBuilder();
		for(SearchNode sn : nodes)
		{
			sb.append(sn.toString(getDestination()));
		}
		
		return sb.toString();
	}
}


