package client.communication;

import java.io.*;
import java.net.*;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import shared.communication.*;
import client.*;

/**
 * Remember that you don't want to be carrying around the whole URL
 * 	*Break it up into parts
 * Make sure that you have a default version of ClientCommunicator for testing
 * Make another version of ClientCommunicator allows for a Host and Port to be passed in
 * @author brian
 *
 */

public class ClientCommunicator 
{
	public static String SERVER_HOST;
	public static int SERVER_PORT;
	public static final String PREFIX = "http://";
	public static String URL_PREFIX;
	private static final String HTTP_GET = "GET";
	private static final String HTTP_POST = "POST";

	public ClientCommunicator(String host, String port) 
	{
		ClientCommunicator.SERVER_PORT = Integer.parseInt(port);
		ClientCommunicator.SERVER_HOST = host;
		ClientCommunicator.URL_PREFIX = PREFIX + SERVER_HOST + ":" + SERVER_PORT;// +  "/Records";
	}
	
	public ClientCommunicator(String host, int port)
	{
		ClientCommunicator.SERVER_PORT = port;
		ClientCommunicator.SERVER_HOST = host;
		ClientCommunicator.URL_PREFIX = PREFIX + SERVER_HOST + ":" + SERVER_PORT;// +  "/Records";
	}

	public ClientCommunicator()
	{
		ClientCommunicator.SERVER_PORT = 1234;
		ClientCommunicator.SERVER_HOST = "x.byu.edu";
		ClientCommunicator.URL_PREFIX = PREFIX + SERVER_HOST + ":" + SERVER_PORT;// + "/Records";
	}
	
	
	public ValidateUser_Result validateUser(ValidateUser_Param param) throws ClientException 
	{
		return(ValidateUser_Result)doPost("/ValidateUser", param);
	}
	
	public GetProjects_Result getProjects(GetProjects_Param param) throws ClientException
	{
		return (GetProjects_Result)doPost("/GetProjects", param);
	}
	
	public GetSampleImage_Result getSampleImage(GetSampleImage_Param param) throws ClientException
	{
		return (GetSampleImage_Result)doPost("/GetSampleImage", param);
	}
	
	public DownloadBatch_Result downloadBatch(DownloadBatch_Param param) throws ClientException
	{
		return (DownloadBatch_Result)doPost("/DownloadBatch", param);
	}
	
	public Search_Result searchResult(Search_Param param) throws ClientException
	{
		return (Search_Result)doPost("/SearchResult", param);
	}
	
	public DownloadFile_Result downloadFile(DownloadFile_Param param) throws ClientException
	{
		return (DownloadFile_Result)doPost("/", param);
	}
	
	public SubmitBatch_Result submitBatch(SubmitBatch_Param param) throws ClientException
	{
		return (SubmitBatch_Result)doPost("/SubmitBatch", param);
	}
	
	public GetFields_Result getFields(GetFields_Param param) throws ClientException
	{
		return (GetFields_Result)doPost("/GetFields", param);
	}
	
	/**
	 * doPost is used rather than doGet because it allows for information to be retrieved and updated
	 * @param urlPath
	 * @param postData
	 * @return
	 * @throws ClientException
	 */
	private static Object doPost(String urlPath, Object postData) throws ClientException
	{
		try 
		{
			XStream xstream = new XStream(new DomDriver());
			
			URL url = new URL(URL_PREFIX + urlPath);
			URL_PREFIX = PREFIX + SERVER_HOST + ":" + SERVER_PORT;
			
			DataTransport_Result.setDestination(URL_PREFIX);
			
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod(HTTP_POST);
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.connect();
			
			xstream.toXML(postData, connection.getOutputStream());
			if(connection.getResponseCode() == HttpURLConnection.HTTP_OK)
			{
				Object result = xstream.fromXML(connection.getInputStream());	
				return result;
			}
			return null;
		}
		
		//The catch is important because it handles the case for invalid port numbers.
		//returning null and setting the message as "FAILED\n" will allow for the testdriver to process invalid port numbers
		catch (IOException e) 
		{
			DataTransport_Result.setDestination("FAILED\n");
			e.printStackTrace();
			return null;
		}
		
	}

}
