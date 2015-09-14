package shared.communication;

import java.io.Serializable;

/**
 * Output:
 * 	Returns the message of "TRUE" or "FAILED"
 * @author brian
 *
 */
public class SubmitBatch_Result extends DataTransport_Result implements Serializable
{
	public String toString()
	{
		return getMessage();
	}
	
}
