package shared.communication;

import java.io.Serializable;

/**
 * Input:
 * 	User name and password
 *  **Because the super class inherits these, this class is empty for now
 * @author brian
 *
 */
public class ValidateUser_Param extends DataTransport_Param implements Serializable
{

	@Override
	public String toString() 
	{
		return "ValidateUser_Param [getUsername()=" + getUsername()
				+ ", getPassword()=" + getPassword() + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
	

	
	
}
