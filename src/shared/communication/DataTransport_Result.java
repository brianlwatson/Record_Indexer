package shared.communication;

/**
 * Parent class for the communication Results
 * 	destination is a static string used to contain the URL of host number and port number
 * @author brian
 */
public class DataTransport_Result 
{
	private String message;
	private static String destination;
	
	public DataTransport_Result()
	{
		message = "";
	}

	public static void setDestination(String dest)
	{
		destination = dest;
	}
	
	public String getDestination()
	{
		return destination;
	}
	
	
	public String getMessage() {
	
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
