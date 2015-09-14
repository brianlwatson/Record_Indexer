package shared.communication;

import java.io.File;
import java.io.Serializable;

/**
 * Output:
 * 	Image URL
 * @author brian
 *
 */

public class GetSampleImage_Result extends DataTransport_Result implements Serializable
{
	private String imagelocation;

	public String getImagelocation() 
	{
		return imagelocation;
	}

	public void setImagelocation(String imagelocation) 
	{
		this.imagelocation = imagelocation;
	}

	public String toString()
	{
		if(imagelocation == null || imagelocation.equals(""))
		{
			return "FAILED\n";
		}
		
		else
		{
			return getDestination() + File.separator + imagelocation;
		}
	}

	
}
