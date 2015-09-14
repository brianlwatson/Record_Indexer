package server.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.rmi.ServerException;

import server.ServerFacade;
import shared.communication.DownloadBatch_Param;
import shared.communication.DownloadBatch_Result;
import shared.communication.GetFields_Param;
import shared.communication.GetFields_Result;
import shared.communication.GetSampleImage_Param;
import shared.communication.GetSampleImage_Result;
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
 * basically the same thing as download batch handler
 * @author brian
 *
 */
public class GetFields_Handler implements HttpHandler
{
	
	public void handle(HttpExchange exchange) throws IOException 
	{
		XStream myxs = new XStream(new DomDriver());
		GetFields_Result result = new GetFields_Result();

		try
		{
			ServerFacade sf = new ServerFacade();
			GetFields_Param params = new GetFields_Param();
			params = (GetFields_Param) myxs.fromXML(exchange.getRequestBody());
			result = ServerFacade.getFields(params);

			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			myxs.toXML(result, exchange.getResponseBody());
			exchange.getResponseBody().close();
		
		}
		catch (ServerException e) 
		{
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
		}
	}
}