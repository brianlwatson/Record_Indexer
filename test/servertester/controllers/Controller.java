package servertester.controllers;

import java.io.File;
import java.util.*;

import client.ClientException;
import client.communication.ClientCommunicator;
import server.ServerFacade;
import servertester.views.*;
import shared.communication.DataTransport_Result;
import shared.communication.DownloadBatch_Param;
import shared.communication.DownloadBatch_Result;
import shared.communication.GetFields_Param;
import shared.communication.GetFields_Result;
import shared.communication.GetProjects_Param;
import shared.communication.GetProjects_Result;
import shared.communication.GetSampleImage_Param;
import shared.communication.GetSampleImage_Result;
import shared.communication.Search_Param;
import shared.communication.Search_Result;
import shared.communication.SubmitBatch_Param;
import shared.communication.SubmitBatch_Result;
import shared.communication.ValidateUser_Param;
import shared.communication.ValidateUser_Result;

public class Controller implements IController {

	private IView _view;
	
	public Controller() {
		return;
	}
	
	public IView getView() {
		return _view;
	}
	
	public void setView(IView value) {
		_view = value;
	}
	
	// IController methods
	//
	
	public void initialize() {
		getView().setHost("localhost");
		getView().setPort("39640");
		operationSelected();
	}

	//@Override
	public void operationSelected() {
		ArrayList<String> paramNames = new ArrayList<String>();
		paramNames.add("User");
		paramNames.add("Password");
		
		switch (getView().getOperation()) {
		case VALIDATE_USER:
			break;
		case GET_PROJECTS:
			break;
		case GET_SAMPLE_IMAGE:
			paramNames.add("Project");
			break;
		case DOWNLOAD_BATCH:
			paramNames.add("Project");
			break;
		case GET_FIELDS:
			paramNames.add("Project");
			break;
		case SUBMIT_BATCH:
			paramNames.add("Batch");
			paramNames.add("Record Values");
			break;
		case SEARCH:
			paramNames.add("Fields");
			paramNames.add("Search Values");
			break;
		default:
			assert false;
			break;
		}
		
		getView().setRequest("");
		getView().setResponse("");
		getView().setParameterNames(paramNames.toArray(new String[paramNames.size()]));
	}

	//@Override
	public void executeOperation() {
		switch (getView().getOperation()) {
		case VALIDATE_USER:
			validateUser();
			break;
		case GET_PROJECTS:
			getProjects();
			break;
		case GET_SAMPLE_IMAGE:
			getSampleImage();
			break;
		case DOWNLOAD_BATCH:
			downloadBatch();
			break;
		case GET_FIELDS:
			getFields();
			break;
		case SUBMIT_BATCH:
			submitBatch();
			break;
		case SEARCH:
			search();
			break;
		default:
			assert false;
			break;
		}
	}
	
	//Verifies that all elements of args are not empty
	private boolean verifyParam(String[] args)
	{
		for(int i = 0; i < args.length; i++)
		{
			if(args[i].length() == 0)
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Implements and errorchecks validateUser() operation in ServerFacade
	 */
	private void validateUser() 
	{
		ClientCommunicator cc = new ClientCommunicator(_view.getHost(), _view.getPort());
		String[] args = _view.getParameterValues();
		
		if(!verifyParam(args))
		{
			_view.setResponse("FALSE\n");
			return;
		}
		
		//Error Checks Params
		ValidateUser_Param params = new ValidateUser_Param();
		params.setUsername(args[0]);
		params.setPassword(args[1]);
		if(params.getUsername() == null || params.getPassword() == null)
		{
			_view.setResponse("FALSE\n");
			return;
		}
		
		//Retrieves the result and sets it to the response
		ValidateUser_Result result = new ValidateUser_Result();
		try 
		{
			result = cc.validateUser(params);
			if(result != null)
			{
				_view.setResponse(result.toString());
			}
			else
			{
				_view.setResponse("FAILED\n");
			}
		}
		catch (ClientException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Implements and getProjects() from ServerFacade
	 */
	private void getProjects() 
	{
		ClientCommunicator cc = new ClientCommunicator(_view.getHost(), _view.getPort());
		String[] args = _view.getParameterValues();
		
		if(!verifyParam(args))
		{
			_view.setResponse("FAILED\n");
			return;
		}
		//Error Checks Params
		GetProjects_Param params = new GetProjects_Param();
		params.setUsername(args[0]);
		params.setPassword(args[1]);
		
		//Gets the result, sets it as the response
		GetProjects_Result result = new GetProjects_Result();
		try 
		{
			result = cc.getProjects(params);
			if(result != null)
			{
				_view.setResponse(result.toString());
			}
			else
			{
				_view.setResponse("FAILED\n");
				return;
			}
		} 
		catch (ClientException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the sample image and returns it with the appended HOST and PORT
	 */
	private void getSampleImage() 
	{
		ClientCommunicator cc = new ClientCommunicator(_view.getHost(), _view.getPort());
		String[] args = _view.getParameterValues();
		
		if(!verifyParam(args))
		{
			_view.setResponse("FAILED\n");
			return;
		}
		
		//Error checking parameters
		GetSampleImage_Param param = new GetSampleImage_Param();
		param.setUsername(args[0]);
		param.setPassword(args[1]);
		param.setProject_id(Integer.valueOf(args[2]));
		
		try
		{
			GetSampleImage_Result result = cc.getSampleImage(param);
			if(result == null || result.getImagelocation() == null)
			{
				_view.setResponse("FAILED\n");
				return;
			}
			else //if result is valid, set the response as the image location
			{
				String imagelocation = "http://" + _view.getHost() + ":" + _view.getPort() + File.separator
						+ result.getImagelocation();
				_view.setResponse(imagelocation);
			}
		}
		 catch (ClientException e) 
		{
				e.printStackTrace();
		}
	}
	
	
	/**
	 * corresponds to downloadBatch() in ServerFacade
	 */
	private void downloadBatch() 
	{
		ClientCommunicator cc = new ClientCommunicator(_view.getHost(), _view.getPort());
		String[] args = _view.getParameterValues();
		
		if(!verifyParam(args))
		{
			_view.setResponse("FAILED\n");
			return;
		}
		
		DownloadBatch_Param params = new DownloadBatch_Param();
		params.setUsername(args[0]);
		params.setPassword(args[1]);
		params.setProject_id(Integer.parseInt(args[2]));
		
		try
		{
			DownloadBatch_Result result = cc.downloadBatch(params);
			if(result != null && !result.getMessage().equals("FAILED\n"))
			{
				String news = "http://" + _view.getHost() +":" + _view.getPort()+ File.separator;
				//Set the Communication Result's Destination to the new http://host:port/
				DataTransport_Result.setDestination(news);
				//After adding the host:port, the result is appended to it
				_view.setResponse(result.toString());
			}
			else
			{
				_view.setResponse("FAILED\n");
			}
		}
		 catch (ClientException e) 
		{
				e.printStackTrace();
		}
	}
	
	/**
	 * Corresponds to getFields() in ServerFacade
	 */
	private void getFields() 
	{
		ClientCommunicator cc = new ClientCommunicator(_view.getHost(), _view.getPort());
		String[] args = _view.getParameterValues();
		GetFields_Result result = new GetFields_Result();
		
		//Verifies the parameters are valid
		if(args[0].length() == 0 || args[1].length() == 0)
		{
			result.setMessage("FAILED\n");
			_view.setResponse(result.getMessage());
			return;
		}
		GetFields_Param param = new GetFields_Param();
		param.setUsername(args[0]);
		param.setPassword(args[1]);
		
		//If project ID is mentioned
		if(!args[2].isEmpty())
		{
			param.setProject_ID(Integer.parseInt(args[2]));
		}
		
		//if not project ID is included, this method in the Facade, will return all fields
		try
		{
			result = cc.getFields(param);
			if(result != null)
			{
				_view.setResponse(result.toString());
			}
			else
			{
				_view.setResponse("FAILED\n");
			}
		}
		 catch (ClientException e) 
		{
			e.printStackTrace();
		}
		
	}
	
	/**
	 * corresponds to submitBatch() in ServerFacade
	 */
	private void submitBatch() 
	{
		ClientCommunicator cc = new ClientCommunicator(_view.getHost(), _view.getPort());
		String[] args = _view.getParameterValues();
		
		if(!verifyParam(args))
		{
			_view.setResponse("FAILED\n");
			return;
		}
		//prepares params
		SubmitBatch_Param params = new SubmitBatch_Param();
		params.setUsername(args[0]);
		params.setPassword(args[1]);
		params.setBatchID(Integer.parseInt(args[2]));
		params.setFieldValues(args[3]);
		
		//retrieves result and sets the response
		try
		{
			SubmitBatch_Result result = cc.submitBatch(params);
			if(result != null)
			{
				_view.setResponse(result.toString());
			}
			else
			{
				_view.setResponse("FAILED\n");
			}
		}
		catch (ClientException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * corresponds to search in ServerFacade
	 */
	@SuppressWarnings("unused")
	private void search() 
	{
		ClientCommunicator cc = new ClientCommunicator(_view.getHost(), _view.getPort());
		String[] args = _view.getParameterValues();
		
		if(!verifyParam(args))
		{
			_view.setResponse("FAILED\n");
			return;
		}
		
		//Prepare params
		Search_Param params = new Search_Param();
		params.setUsername(args[0]);
		params.setPassword(args[1]);
		
		//Parse the two sets of strings, and add them to params
		String s2[] = args[2].split("," , -1);
		params.addAllIDs(s2);
		
		String s1[] = args[3].split("," , -1);
		params.addAllValues(s1);
		
		//execute search using params
		try
		{
			Search_Result result = cc.searchResult(params);
			System.out.println("RESULTDONE" + result.toString());
			if(result != null)
			{
				String s =  "http://" + _view.getHost() +":" + _view.getPort()+ File.separator;
				DataTransport_Result.setDestination(s);
				_view.setResponse(result.toString());
			}
			
			else
				_view.setResponse("FAILED\n");
		}
		catch (ClientException e) 
		{
			e.printStackTrace();
		}
	}

}

