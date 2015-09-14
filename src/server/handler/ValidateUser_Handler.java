package server.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.rmi.ServerException;
import java.sql.SQLException;

import server.ServerFacade;
import server.database.DatabaseException;
import shared.communication.ValidateUser_Param;
import shared.communication.ValidateUser_Result;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * 1. initialize server facade
 * 2. get your params
 * 2a. deserialize your params
 * 3. get your result from the server facade using deserialized params
 * 3a. serialize the result
 * 4. make a new output stream using exchange.getResponseBody
 * 5. write out the response.getBytes
 * 6. send the response headers using HTTPOK (Status 200) with the length of the response....?
 * Same as others
 * @author brian
 *
 */
public class ValidateUser_Handler implements HttpHandler
{

	public void handle(HttpExchange exchange) throws IOException 
	{
		XStream myxs = new XStream(new DomDriver());
		ValidateUser_Result result = new ValidateUser_Result();

		try
		{
			ServerFacade sf = new ServerFacade();
			ValidateUser_Param params = new ValidateUser_Param();
			params = (ValidateUser_Param) myxs.fromXML(exchange.getRequestBody());
			result = ServerFacade.validateUser(params);

			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			myxs.toXML(result, exchange.getResponseBody());
			exchange.getResponseBody().close();
		}
		
		catch (DatabaseException e) 
		{
			e.printStackTrace();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
	}
}
