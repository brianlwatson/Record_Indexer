package servertester;

import java.awt.*;
import java.sql.SQLException;

import dataimporter.DataImporter;
import server.Server;
import server.database.DatabaseException;
import servertester.controllers.*;
import servertester.views.*;

public class GuiTester {

	public static void main(String[] args) 
	{
		EventQueue.invokeLater(
				new Runnable() 
				{
					public void run() 
					{
						
						Server s = new Server(39640);
						try 
						{
							s.run();
						} 
						catch (DatabaseException e) 
						{	
							System.out.println("GUITESTER SERVER RUN FAIL");
							e.printStackTrace();
						}
						
						IndexerServerTesterFrame frame = new IndexerServerTesterFrame();			
						Controller controller = new Controller();
						frame.setController(controller);			
						controller.setView(frame);
						controller.initialize();
						frame.setVisible(true);
					}
				}
		);

	}

}

