package server;

import java.io.*;
import java.net.*;
import java.util.logging.*;

import com.sun.net.httpserver.*;

import server.ServerFacade;
import server.database.DatabaseException;
import server.handler.*;

public class Server 
{
	private static int SERVER_PORT_NUMBER;
	private static final int MAX_WAITING_CONNECTIONS = 10;
	private static Logger logger;
	private HttpServer server;

	private DownloadBatch_Handler downloadBatch_h = new DownloadBatch_Handler();
	private DownloadFile_Handler downloadFile_h = new DownloadFile_Handler();
	private GetFields_Handler getFields_h = new GetFields_Handler();
	private GetProjects_Handler getProjects_h = new GetProjects_Handler();
	private GetSampleImage_Handler getSampleImage_h = new GetSampleImage_Handler();
	private Search_Handler search_h = new Search_Handler();
	private SubmitBatch_Handler submitBatch_h = new SubmitBatch_Handler();
	private ValidateUser_Handler validateUser_h = new ValidateUser_Handler();
	
	static 
	{
		try 
		{
			initLog();
		}
		catch (IOException e) 
		{
			System.out.println("Could not initialize log: " + e.getMessage());
		}
	}
	
	/**
	 * Initializes logger
	 * @throws IOException
	 */
	private static void initLog() throws IOException 
	{
		Level logLevel = Level.FINE;
		
		logger = Logger.getLogger("contactmanager"); 
		logger.setLevel(logLevel);
		logger.setUseParentHandlers(false);
		
		Handler consoleHandler = new ConsoleHandler();
		consoleHandler.setLevel(logLevel);
		consoleHandler.setFormatter(new SimpleFormatter());
		logger.addHandler(consoleHandler);

		FileHandler fileHandler = new FileHandler("log.txt", false);
		fileHandler.setLevel(logLevel);
		fileHandler.setFormatter(new SimpleFormatter());
		logger.addHandler(fileHandler);
	}

	/**
	 * Server constructor which allows the port number to be passed in
	 * @param server_port
	 */
	public Server(int server_port) 
	{
		SERVER_PORT_NUMBER = server_port;
		return;
	}
	
	/**
	 * Runs the server
	 * @throws DatabaseException
	 */
	public void run() throws DatabaseException 
	{
		logger.info("Initializing Model");
		try 
		{
			ServerFacade.prepareFacade();		
		}
		catch (DatabaseException e) 
		{
			logger.log(Level.SEVERE, e.getMessage(), e);
			return;
		}
		
		logger.info("Initializing HTTP Server");
		
		try 
		{
			server = HttpServer.create(new InetSocketAddress(SERVER_PORT_NUMBER), MAX_WAITING_CONNECTIONS);
		} 
		catch (IOException e) 
		{
			logger.log(Level.SEVERE, e.getMessage(), e);			
			return;
		}

		server.setExecutor(null); // use the default executor - we don't really want to do that
		
		//if you have this url path, call this handler
		server.createContext("/DownloadBatch", downloadBatch_h);
		server.createContext("/", downloadFile_h);
		server.createContext("/GetFields", getFields_h);
		server.createContext("/GetProjects", getProjects_h);
		server.createContext("/GetSampleImage", getSampleImage_h);
		server.createContext("/Search", search_h);
		server.createContext("/SubmitBatch", submitBatch_h);
		server.createContext("/ValidateUser", validateUser_h);
		
		logger.info("Starting HTTP Server");
		server.start();
	}
	

	public static void main(String[] args) throws DatabaseException 
	{
		if(args.length == 0)
		{
			//Default Settings for Testing
			new Server(8080).run();
			System.out.println("Running server at 8080");
		}
		else
		{
			new Server(Integer.parseInt(args[0])).run();
			System.out.println("Running server at: " + Integer.parseInt(args[0]));
		}
	}

}
