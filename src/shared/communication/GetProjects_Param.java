package shared.communication;

import java.io.Serializable;

import shared.model.User;

/**
 * Input:
 * 	User Name - inherited
 * 	User Password - inherited
 *  Because this has a superclass now, this gets taken over and doesn't need to be here
 * @author brian
 *
 */
public class GetProjects_Param extends DataTransport_Param implements Serializable
{
	
}
