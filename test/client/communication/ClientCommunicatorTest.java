package client.communication;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import server.Server;
import server.database.DatabaseException;
import shared.model.*;
import shared.communication.*;
import client.ClientException;
import client.communication.ClientCommunicator;
import dataimporter.DataImporter;

public class ClientCommunicatorTest 
{
	private static Server s;
	private static ClientCommunicator comm;
	
	@BeforeClass
	//Run new server at default port 
	//Run DATAIMPORTER BEFORE YOU RUN THIS TEST!!!
	//NOTE: All operations are performed in the test case in order to simulate a user's complete interactions
	
	public static void setup() throws Exception
	{
		s = new Server(8080);
		s.run();
		String arg[] = new String[0];
		DataImporter.main(arg);
	}
	
	@AfterClass
	//End server and ClientCommunicator
	public static void end()
	{
		s = null;
		comm = null;
	}
	
	@Test
	public void test() throws ClientException, IOException 
	{
		comm = new ClientCommunicator("localhost", "8080");
		ValidateUser_Param vu_param = new ValidateUser_Param();
		GetProjects_Param gp_param = new GetProjects_Param();
		GetSampleImage_Param gsi_param = new GetSampleImage_Param();
		DownloadBatch_Param db_param = new DownloadBatch_Param();
		SubmitBatch_Param sb_param = new SubmitBatch_Param();
		GetFields_Param gf_param = new GetFields_Param();
		Search_Param s_param = new Search_Param();
		DownloadFile_Param df_param = new DownloadFile_Param();
		
		//Test Validate User
		//-----------------------------------------------------------------------------------------------------------------
		//Pass Case
		vu_param.setUsername("sheila");
		vu_param.setPassword("parker");
		ValidateUser_Result vu_result = comm.validateUser(vu_param);
		assertEquals("Sheila", vu_result.getUser().getFirst_name());
		assertEquals("sheila", vu_result.getUser().getUsername());
		System.out.println("Passed Valid User Validate");
		//Fail Case
		ValidateUser_Param vu_param2 = new ValidateUser_Param();
		vu_param2.setUsername("not");
		vu_param2.setPassword("goingtowork");
		ValidateUser_Result vu_result2 = comm.validateUser(vu_param2);
		assertEquals("FALSE\n", vu_result2.toString());
		System.out.println("Passed NonExistant User Validate");
		//Fail Case
		ValidateUser_Param vu_param3 = new ValidateUser_Param();
		vu_param3.setUsername("test1");
		vu_param3.setPassword("test2");
		ValidateUser_Result vu_result3 = comm.validateUser(vu_param3);
		assertEquals(null, vu_result3.getUser());
		System.out.println("Passed Wrong Password Valid User Validate\n\n");
		//-----------------------------------------------------------------------------------------------------------------
		
		
		//Test Get Projects
		//-----------------------------------------------------------------------------------------------------------------
		
		gp_param.setUsername("sheila");
		gp_param.setPassword("parker");
		GetProjects_Result gp_result = comm.getProjects(gp_param);
		assertEquals(3, gp_result.getProjects().size());
		//Valid Case
		assertEquals("1890 Census", gp_result.getProjects().get(0).getTitle());
		assertEquals("1900 Census", gp_result.getProjects().get(1).getTitle());
		//String s = "3\nDraft Records\n1\n1890 Census \n2\n1900 Census\n";
	
		//Invalid Case
		GetProjects_Param gp_param2 = new GetProjects_Param();
		gp_param.setUsername("fail");
		gp_param.setPassword("fail");
		GetProjects_Result gp_result2 = comm.getProjects(gp_param2);
		assertEquals("FAILED\n", gp_result2.toString());
		System.out.println("Passed GetProjects");
		
		//-----------------------------------------------------------------------------------------------------------------
		//Test Get Sample Image
		
		//You might need to add a URL and a port through client communicator
		//Valid case
		gsi_param.setUsername("sheila");
		gsi_param.setPassword("parker");
		gsi_param.setProject_id(1);
		GetSampleImage_Result gsi_result = comm.getSampleImage(gsi_param);
		System.out.println(gsi_result.toString() + "\n\n");
		assertEquals("http://localhost:8080/images/1890_image0.png", gsi_result.toString());
		
		//Invalid Test Case
		GetSampleImage_Param gsi_param2 = new GetSampleImage_Param();
		gsi_param2.setUsername("fail");
		gsi_param2.setUsername("fail");
		GetSampleImage_Result gsi_result2 = comm.getSampleImage(gsi_param2);
		assertEquals("FAILED\n", gsi_result2.toString());
		//-----------------------------------------------------------------------------------------------------------------
		//Download Batch
		//
		//This needs to be changing the assignment of the batch
		//each time you run this, the user should get a new batch.
		//Valid
		db_param.setProject_id(2);
		db_param.setUsername("sheila");
		db_param.setPassword("parker");
		DownloadBatch_Result db_result = comm.downloadBatch(db_param);
		assertEquals(21, db_result.getBatch().getImage_id());

		//Invalid case - user cannot download a batch now that one is assigned
		db_param.setUsername("sheila");
		db_param.setPassword("parker");
		DownloadBatch_Result db_result2 = comm.downloadBatch(db_param);
		assertEquals("FAILED\n", db_result2.toString());
		
		//If another user tries to download a batch, the next sequential batch is the one to be assigned
		DownloadBatch_Param db_param2 = new DownloadBatch_Param();
		db_param2.setProject_id(2);
		db_param2.setUsername("test1");
		db_param2.setPassword("test1");
		DownloadBatch_Result db_result_2 = comm.downloadBatch(db_param2);
		assertEquals(22, db_result_2.getBatch().getImage_id());

		
		//-----------------------------------------------------------------------------------------------------------------
		//SubmitBatch_Param
		//Valid case
		vu_param.setUsername("sheila");
		vu_param.setPassword("parker");
		ValidateUser_Result vu_result6 = comm.validateUser(vu_param);
		int previous_records = vu_result6.getUser().getNum_records();
		sb_param.setUsername("sheila");
		sb_param.setPassword("parker");
		sb_param.setBatchID(21);
		sb_param.setFieldValues("Jones,Fred,13,Idaho,USA;Rogers,Susan,42,Utah,USA;,,,,;,,,,;Van Fleet,Bill,23,Cali,USA;Watson,Brian,23,Done,Testing");
		SubmitBatch_Result sb_result = comm.submitBatch(sb_param);
		System.out.println("after sb");
		assertEquals(previous_records+6, comm.validateUser(vu_param).getUser().getNum_records());
		System.out.println("Passed Increment Records");
		assertEquals(-1, comm.validateUser(vu_param).getUser().getBatchassig());
		System.out.println("Passed User Set Batch Assignment to -1");
		vu_result6 = comm.validateUser(vu_param);
		assertEquals("TRUE\n", sb_result.toString());
		
		//Invalid case -- Trying to submit with a user that has no information
		// 		The same param was used in this one to assure that information was updated correctly
		assertEquals("FAILED\n", comm.submitBatch(sb_param).toString());
		
		//Invalid case -- User that doesn't have an assigned batch
		SubmitBatch_Param sb_param2 = new SubmitBatch_Param();
		sb_param2.setUsername("test1");
		sb_param2.setPassword("test1");
		sb_param2.setFieldValues("Jones,Fred,13,Idaho,USA;Rogers,Susan,42,Utah,USA;,,,,;,,,,;Van Fleet,Bill,23,Cali,USA;Watson,Brian,23,Done,Testing");
		sb_param2.setBatchID(2);
		SubmitBatch_Result sb_result2 = new SubmitBatch_Result();
		assertEquals("FAILED\n", comm.submitBatch(sb_param2).toString());
		
		//-----------------------------------------------------------------------------------------------------------------
		//Get Fields
		//Valid user - specific project
		gf_param.setProject_ID(1);
		gf_param.setUsername("test1");
		gf_param.setPassword("test1");
		GetFields_Result gf_result = comm.getFields(gf_param);
		assertEquals(4, gf_result.getFields().size());
		System.out.println("\n\nPassed Get Fields of Valid User");
		
		//Valid user - unspecified project
		GetFields_Param gf_param2 = new GetFields_Param();
		gf_param2.setUsername("sheila");
		gf_param2.setPassword("parker");
		GetFields_Result gf_result2 = comm.getFields(gf_param2);
		assertEquals(13, gf_result2.getFields().size());
		System.out.println("Passed Get Fields of Unspecified");
		
		//Valid user- nonexistent project
		gf_param2.setProject_ID(6);
		gf_result2 = comm.getFields(gf_param2);
		assertEquals(0, gf_result2.getFields().size());
		System.out.println("Passed Get Fields of Invalid User");
		
		//Invalid User
		gf_param.setUsername("fail");
		gf_param.setPassword("fail");
		assertEquals("FAILED\n", comm.getFields(gf_param).toString());
		
		//-----------------------------------------------------------------------------------------------------------------
		//Search_Param
		//Valid Search
		List<String> sp_test_fv = new ArrayList<String>();
		List<Integer> sp_test_fids = new ArrayList<Integer>();
		sp_test_fids.add(1);
		sp_test_fids.add(7);
		sp_test_fids.add(10);
		
		sp_test_fv.add("FiScHeR");
		sp_test_fv.add("CHAVEZ");
		
		s_param.setUsername("test1");
		s_param.setPassword("test1");
		s_param.setField_IDs(sp_test_fids);
		s_param.setField_values(sp_test_fv);
		
		Search_Result sr = comm.searchResult(s_param);
		//3 Hits, information looked up in the tables
		assertEquals(3, sr.getNodes().size());
		//Test the URL to prepend
		assertEquals("http://localhost:8080", sr.getDestination());
		System.out.println("Correctly Gets the Host:Port");
		
		//If invalid Fields are Provided, return fail
		sp_test_fv.clear();
		assertEquals("FAILED\n", comm.searchResult(s_param).toString());
		
		//Readd the valid test field values, test invalid username
		sp_test_fv.add("FiScHeR");
		sp_test_fv.add("CHAVEZ");
		s_param.setUsername("test2");
		assertEquals("FAILED\n", comm.searchResult(s_param).toString());

		//valid username and password, no Field IDs
		s_param.setUsername("test1");
		sp_test_fids.clear();
		assertEquals("FAILED\n", comm.searchResult(s_param).toString());

		//Invalid Field ID
		sp_test_fids.add(1);
		sp_test_fids.add(7);
		sp_test_fids.add(15);
		assertEquals("FAILED\n", comm.searchResult(s_param).toString());

		
		//Invalid Search
		//-----------------------------------------------------------------------------------------------------------------
		//Download_File
		//Run server and put sample image result into browser
		
	}	
}