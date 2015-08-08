package models;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;

public class RunTest {
	
	private static int validClientId = 3;
	private static int invalidClientId = 0;
	
	private static String validTitle = "title";
	private static String invalidTitle1 = "t";
	private static String invalidTitle2 = "ttttttttttttttttttttttttttttttttt";
	
	private static String validDescription = "description";
	private static String invalidDescription = "de";
	
	private static String validRunType1 = "Offer";
	private static String validRunType2 = "Bid";
	private static String invalidRunType1 = "asdf";
	private static String invalidRunType2 = "Biddd";
	
	private static String validAmount = "20";
	private static String invalidAmount1 = "-1";
	private static String invalidAmount2 = "asdf";
	
	private static String validAddress = "1 utsa circle";
	private static String invalidAddress = "1";
	
	private static String validCity1 = "San Antonio";
	private static String validCity2 = "city";
	private static String invalidCity1 = "1435";
	private static String invalidCity2 = "";
	
	private static String validState1 = "Texas";
	private static String validState2 = "CA";
	private static String invalidState1 = "1234";
	private static String invalidState2 = "";
	
	private static String validZipCode = "78230";
	private static String invalidZipCode = "asdf";
	
	private static Run nullRun;
	private static Run emptyRun;
	private static Run validRun1;
	private static Run validRun2;
	private static Run invalidRun1;
	private static Run invalidRun2;
	
	private static ArrayList<Run> qRuns;
	private static ArrayList<Run> aRuns;
	private static ArrayList<Run> cRuns;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		nullRun = new Run(0,null, null, null, null, null, null, null, null);
		emptyRun = new Run(0,"", "", "", "","","","", "");
		
		validRun1 = new Run(validClientId, validTitle, validDescription, validRunType1, validAmount, 
				validAddress, validCity1, validState1, validZipCode);
		validRun2 = new Run(validClientId, validTitle, validDescription, validRunType2,validAmount, 
				validAddress, validCity2, validState2, validZipCode);
		
		invalidRun1 = new Run(invalidClientId, invalidTitle1, invalidDescription, invalidRunType1,invalidAmount1, 
				invalidAddress, invalidCity1, invalidState1, invalidZipCode);
		invalidRun2 = new Run(invalidClientId, invalidTitle2, invalidDescription, invalidRunType2,invalidAmount2, 
				invalidAddress, invalidCity2, invalidState2, invalidZipCode);
		
		qRuns = new ArrayList<Run>();
		aRuns = new ArrayList<Run>();
		cRuns = new ArrayList<Run>();
		
		validRun1.setRunId(1);
		qRuns.add(validRun1);
		validRun2.setRunId(2);
		qRuns.add(validRun2);
	}
	
	@Test
	public void testNullRun(){
		HashMap<String, String> map = nullRun.getErrors();
		//System.out.println(map.toString());
		assertEquals(8, map.size());
	}
	
	@Test
	public void testEmptyRun() {
		HashMap<String, String> map = emptyRun.getErrors();
		//System.out.println(map.toString());
		assertEquals(8, map.size());
	}
	
	@Test
	public void testValidRun1() {
		HashMap<String, String> map = validRun1.getErrors();
		//System.out.println(map.toString());
		assertEquals(0, map.size());
	}
	
	@Test
	public void testValidRun2() {
		HashMap<String, String> map = validRun2.getErrors();
		//System.out.println(map.toString());
		assertEquals(0, map.size());
	}
	
	@Test
	public void testinValidRun1() {
		HashMap<String, String> map = invalidRun1.getErrors();
		System.out.println(map.toString());
		assertEquals(9, map.size());
	}
	
	@Test
	public void testinValidRun2() {
		HashMap<String, String> map = invalidRun2.getErrors();
		//System.out.println(map.toString());
		assertEquals(9, map.size());
	}
	
	@Test
	public void testValidEditRun1() {
		validRun1.EditRun("title2", "description2", "40", "3 fire lane", "city", "state", "78249");
		HashMap<String, String> map = validRun1.getErrors();
		//System.out.println(map.toString());
		assertEquals(0, map.size());
	}
	
	@Test
	public void testValidEditRun2() {
		validRun2.EditRun("title3", "description34", "47", "16 bright run", "city2", "state2", "782349");
		HashMap<String, String> map = validRun2.getErrors();
		//System.out.println(map.toString());
		assertEquals(0, map.size());
	}
	
	@Test
	public void testinValidEditRun1() {
		validRun1.EditRun(invalidTitle1, invalidDescription, invalidAmount1,
				invalidAddress, invalidCity1, invalidState1, invalidZipCode);
		HashMap<String, String> map = validRun1.getErrors();
		//System.out.println(map.toString());
		assertEquals(7, map.size());
	}
	
	@Test
	public void testinValidEditRun2() {
		validRun2.EditRun(invalidTitle2, invalidDescription, invalidAmount2,
				invalidAddress, invalidCity2, invalidState2, invalidZipCode);
		HashMap<String, String> map = validRun2.getErrors();
		//System.out.println(map.toString());
		assertEquals(7, map.size());
	}
	
	@Test
	public void testAccepted(){
		validRun1.accepted(qRuns, aRuns);
		assertEquals(1, aRuns.size());
		assertEquals(1, qRuns.size());
	}
	
	@Test
	public void testCompleted(){
		validRun1.completed(aRuns, cRuns);
		assertEquals(0, aRuns.size());
		assertEquals(1, cRuns.size());
		System.out.println(cRuns);
	}

}
