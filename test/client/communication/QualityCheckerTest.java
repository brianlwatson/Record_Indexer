package client.communication;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import gui.QualityChecker.ISpellCorrector.NoSimilarWordFoundException;
import gui.QualityChecker.SpellCorrector;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class QualityCheckerTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception 
	{
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws IOException, NoSimilarWordFoundException 
	{
		SpellCorrector qc = new SpellCorrector();
		Set<String> totalknowndata = new TreeSet<String>();
		qc.useDictionary("indexer_data/Records/knowndata/1890_first_names.txt");
		qc.useDictionary("indexer_data/Records/knowndata/1890_last_names.txt");
		qc.useDictionary("indexer_data/Records/knowndata/1900_last_names.txt");
		qc.useDictionary("indexer_data/Records/knowndata/1900_first_names.txt");
		qc.useDictionary("indexer_data/Records/knowndata/draft_first_names.txt");
		qc.useDictionary("indexer_data/Records/knowndata/draft_last_names.txt");
		qc.useDictionary("indexer_data/Records/knowndata/ethnicities.txt");
		qc.useDictionary("indexer_data/Records/knowndata/genders.txt");
		System.out.println("SIZE: " + qc.getTrie().getWords().size());
	
		assertEquals(2609, qc.getTrie().getWords().size());
		
		//Testing unreasonable names
		assertEquals(0, qc.suggestSimilarWord("asdfasdf").size());
		assertEquals(0, qc.suggestSimilarWord("murghrh").size());
		assertEquals(0, qc.suggestSimilarWord("yoruqyeox").size());
		
		
		//Return One Name
			//Murphy
		assertEquals(1, qc.suggestSimilarWord("murghh").size()); 
			//Casandra - should test lower case too
		assertEquals(1, qc.suggestSimilarWord("CASANDRE").size());

		
		//Return multiple names
		assertEquals(7, qc.suggestSimilarWord("shelt").size());
		assertNotSame(0, qc.suggestSimilarWord("shelt").size());
		assertNotSame(0, qc.suggestSimilarWord("sxith").size());
		
		//Should return martin, martinez, martina
		assertEquals(3, qc.suggestSimilarWord("Martines").size());
		
		//Schroeder
		assertEquals(1, qc.suggestSimilarWord("SCHROEDEZ").size());

	}

}
