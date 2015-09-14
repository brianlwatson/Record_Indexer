package shared.communication;

import java.io.Serializable;

import shared.model.User;
/**
 * Outputs:
 * 	user and corresponding information
 * 
 * @author brian
 *
 */
public class ValidateUser_Result extends DataTransport_Result implements Serializable
{
	User user;

	public ValidateUser_Result() 
	{
		super();
	}

	public User getUser() 
	{
		return user;
	}

	public void setUser(User user) 
	{
		this.user = user;
	}
	
	public String toString()
	{
		String result = new String();
		StringBuilder sb = new StringBuilder(result);
		if(user != null)
		{
			sb.append("TRUE\n");
			sb.append(user.getFirst_name() + "\n");
			sb.append(user.getLast_name() + "\n");
			sb.append(user.getNum_records() + "\n");
			return sb.toString();
		}
		
		else
			return getMessage();
	}
	
}
