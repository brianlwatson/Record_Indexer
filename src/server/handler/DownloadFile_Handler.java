package server.handler;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import client.communication.ClientCommunicator;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * This one is different from all of the others
 * all you're going to do is make a new file 
 * you're going to copy Files.copy(InputStream Path, OutputStream getResponseBody)
 * close the exchange
 * @author brian
 *
 */
public class DownloadFile_Handler implements HttpHandler
{

	public void handle(HttpExchange exchange) throws IOException 
	{		
		
		String file_location = "Records" + exchange.getRequestURI().toString();
		Path p = Paths.get(file_location);
		byte[] b = Files.readAllBytes(p);
		
		exchange.sendResponseHeaders(200, b.length);
		
		exchange.getResponseBody().write(b);
		exchange.close();
	}

}
