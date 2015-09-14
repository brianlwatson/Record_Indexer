package shared.communication;

/**
 * Parent class for the communication params
 * @author brian
 */
public abstract class DataTransport_Param 
{
	private String username;
	private String password;
	
	public DataTransport_Param(String username, String password) 
	{
		super();
		this.username = username;
		this.password = password;
	}
	
	public DataTransport_Param()
	{
		username = "";
		password = "";
	}

	public String getUsername() 
	{
		return username;
	}

	public void setUsername(String username) 
	{
		this.username = username;
	}

	public String getPassword() 
	{
		return password;
	}

	public void setPassword(String password) 
	{
		this.password = password;
	}
}
