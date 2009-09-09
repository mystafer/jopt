package jopt.js.test.domain;

import java.util.Arrays;

import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.util.IntIntervalSet;
import jopt.csp.variable.PropagationFailureException;
import jopt.js.spi.domain.activity.ActivityDomain;
import jopt.js.spi.domain.activity.ActOperationDomain;
import jopt.js.spi.domain.activity.ActResourceDomain;
import junit.framework.TestCase;


/**
 * @author James Boerkoel
 */
public class ActivityDomainTest extends TestCase {
	ActivityDomain aa;
	ActivityDomain dom;
	ActivityDomain dom2;
	ActOperationDomain oa1;
	ActOperationDomain oa2;
	ActResourceDomain ra1;
	ActResourceDomain ra2;
	ActResourceDomain ra3;
	ActResourceDomain ra4;
	ActResourceDomain ra5;
	
	public ActivityDomainTest(String testName) {
		super(testName);
	}
	
	public void setUp() {
		try {
			oa1 = new ActOperationDomain(1,1,1);
			oa1.setID(1);
			ra1 = new ActResourceDomain(0,100,5,10);
			ra1.setID(1);
			ra2 = new ActResourceDomain(0,100,5,10);
			ra2.setID(2);
			ra3 = new ActResourceDomain(0,100,5,10);
			ra3.setID(3);
			oa1.addResource(ra1);
			oa1.addResource(ra2);
			oa1.addResource(ra3);
			
			oa2 = new ActOperationDomain(1,1,1);
			oa2.setID(2);
			ra4 = new ActResourceDomain(0,100,5,10);
			ra4.setID(4);
			ra5 = new ActResourceDomain(0,100,5,10);
			ra4.setID(5);
			oa2.addResource(ra4);
			oa2.addResource(ra5);
			
			aa = new ActivityDomain(0,100,5,10);
			aa.addOperation(oa1);
			aa.addOperation(oa2);
			
			ActOperationDomain op1 = new ActOperationDomain(1,1,1);
			op1.setID(1);
			ActOperationDomain op2 = new ActOperationDomain(1,1,1);
			op2.setID(2);
			ActOperationDomain op3 = new ActOperationDomain(1,1,1);
			op3.setID(3);
			
			ActResourceDomain res = new ActResourceDomain(10,40,1,1);
			res.setID(1);
			op1.addResource(res);
			
			res = new ActResourceDomain(0,100,1,1);
			res.setID(2);
			op1.addResource(res);
			
			res = new ActResourceDomain(0,100,1,1);
			res.setID(2);
			op2.addResource(res);
			
			res = new ActResourceDomain(60,90,1,1);
			res.setID(3);
			op2.addResource(res);
			
			res = new ActResourceDomain(0,50,1,1);
			res.setID(4);
			op3.addResource(res);
			
			res = new ActResourceDomain(50,110,1,1);
			res.setID(5);
			op3.addResource(res);
			
			res = new ActResourceDomain(30,70,1,1);
			res.setID(6);
			op3.addResource(res);
			
			/*What dom will look like post initialization
			 * Operation    Resource    Timeline
			 *  1               1              10----40
			 *  1               2           0------------------------100
			 * 
			 *  2               2           0------------------------100
			 *  2               3                           60----90
			 * 
			 *  3               4           0-----------50
			 *  3               5                       50--------------110
			 *  3               6                   30--------70
			 */
			
			dom = new ActivityDomain(0,110,1,1);
			dom.addOperation(op1);
			dom.addOperation(op2);
			dom.addOperation(op3);
			dom.setBuilt(true);
			
	        /*
	         * At this time I will define what domain 2 (dom2) should look like upont initialization
	         * and subsequent tests will be written according to this.  
	         * 
	         * 
	         * Operation	Relationship	Resource		Timeline
	         * 		1			REQ				1				0------------------------100
	         * 		1			REQ				2						    50-----------100
	         * 
	         * 		2			CONS			1				0------------------------100
	         * 		2			CONS			3				0-----------50
	         * 
	         * 		3			PROD			4				0------33			75---100
	         * 		3			PROD			5					   33---------66
	         * 		3			PROD			6								  66-----100	
	         * 
	         *      4			CONS			1				0------------------------100
	         * 		4			CONS			2							50-----------100
	         * 
	         * 		5			REQ				1				0------------------------100
	         * 		5			REQ				3				0-----------50
	    	
	         * 
	         * 
	         * For ease of testing, assume that all relationships produce/consume/require 3 units of 
	         * resource #X.
	         * 
	         */
			
			op1 = new ActOperationDomain(1,1,1);
			op1.setID(1);
			op2 = new ActOperationDomain(1,1,1);
			op2.setID(2);
			op3 = new ActOperationDomain(1,1,1);
			op3.setID(3);
			ActOperationDomain op4 = new ActOperationDomain(1,1,1);
			op4.setID(4);
			ActOperationDomain op5 = new ActOperationDomain(1,1,1);
			op4.setID(5);
			
			res = new ActResourceDomain(0,100,0,0);
			res.setID(1);
			op1.addResource(res);
			
			res = new ActResourceDomain(50,100,0,0);
			res.setID(2);
			op1.addResource(res);
			
			res = new ActResourceDomain(0,100,0,0);
			res.setID(1);
			op2.addResource(res);
			
			res = new ActResourceDomain(0,50,0,0);
			res.setID(3);
			op2.addResource(res);
			
			res = new ActResourceDomain(0,100,0,0);
			res.removeStartTimes(34,74,true);
			res.setID(4);
			op3.addResource(res);
			
			res = new ActResourceDomain(33,66,0,0);
			res.setID(5);
			op3.addResource(res);
			
			res = new ActResourceDomain(66,100,0,0);
			res.setID(6);
			op3.addResource(res);
			
			res = new ActResourceDomain(0,100,0,0);
			res.setID(1);
			op4.addResource(res);
			
			res = new ActResourceDomain(50,100,0,0);
			res.setID(2);
			op4.addResource(res);
			
			res = new ActResourceDomain(0,100,0,0);
			res.setID(1);
			op5.addResource(res);
			
			res = new ActResourceDomain(0,50,0,0);
			res.setID(3);
			op5.addResource(res);
			
			dom2 = new ActivityDomain(0,110,0,0);
			dom2.addOperation(op1);
			dom2.addOperation(op2);
			dom2.addOperation(op3);
			dom2.addOperation(op4);
			dom2.addOperation(op5);
			dom2.setBuilt(true);
			
		}
		catch (PropagationFailureException pfe) {
			fail();
		}
	}
	
	/*****************
	 * BASIC TESTING *
	 *****************/
	public void testSetup() {
		assertEquals(aa.getEarliestStartTime(),ra1.getEarliestStartTime());
		assertEquals(aa.getEarliestEndTime(),ra1.getEarliestEndTime());
		assertEquals(aa.getLatestStartTime(),ra1.getLatestStartTime());
		assertEquals(aa.getLatestEndTime(),ra1.getLatestEndTime());
		assertEquals(aa.getDurationMax(),ra1.getDurationMax());
		assertEquals(aa.getDurationMin(),ra1.getDurationMin());
		
		assertEquals(aa.getEarliestStartTime(),ra2.getEarliestStartTime());
		assertEquals(aa.getEarliestEndTime(),ra2.getEarliestEndTime());
		assertEquals(aa.getLatestStartTime(),ra2.getLatestStartTime());
		assertEquals(aa.getLatestEndTime(),ra2.getLatestEndTime());
		assertEquals(aa.getDurationMax(),ra2.getDurationMax());
		assertEquals(aa.getDurationMin(),ra2.getDurationMin());
		
		assertEquals(aa.getEarliestStartTime(),ra3.getEarliestStartTime());
		assertEquals(aa.getEarliestEndTime(),ra3.getEarliestEndTime());
		assertEquals(aa.getLatestStartTime(),ra3.getLatestStartTime());
		assertEquals(aa.getLatestEndTime(),ra3.getLatestEndTime());
		assertEquals(aa.getDurationMax(),ra3.getDurationMax());
		assertEquals(aa.getDurationMin(),ra3.getDurationMin());    	
	}
	
    public void testConsistentDomains() {
    	//set lst to 9, and make sure domain is consistent
    	try{
			ActResourceDomain ra1 = new ActResourceDomain(10,31,3,3);
			
			ra1.removeStartTimes(13,17,true);
			ra1.removeStartTimes(21,25,true);
			ActResourceDomain ra2 = new ActResourceDomain(9,31,3,3);
			ActResourceDomain ra3 = new ActResourceDomain(9,31,3,3);
			ActResourceDomain ra4 = new ActResourceDomain(9,31,3,3);
			ActResourceDomain ra5 = new ActResourceDomain(9,31,3,3);
			ActOperationDomain oa = new ActOperationDomain(ActOperationDomain.REQUIRES,1,1);
			oa.addResource(ra1);
			oa.addResource(ra2);
			oa.addResource(ra3);
			oa.addResource(ra4);
			oa.addResource(ra5);
			ActivityDomain ad1 = new ActivityDomain(9,31,3,3);
			ad1.addOperation(oa);
			
			
			ad1.setLatestStartTime(9);
			
			
			
    	}
    	catch (PropagationFailureException pfe) {
    		pfe.printStackTrace();
    		fail(pfe.getLocalizedMessage());
    	}
    	
    	
    }
    
    public void testConsistentDomains2() {
    	ChoicePointStack cps = new ChoicePointStack();
    	//set lst to 9, and make sure domain is consistent
    	try{
			ActResourceDomain ra1 = new ActResourceDomain(1,0,50,3,3);
			ActResourceDomain ra2 = new ActResourceDomain(2,0,50,3,3);
			ActResourceDomain ra3 = new ActResourceDomain(3,0,50,3,3);
			ActResourceDomain ra4 = new ActResourceDomain(4,0,50,3,3);
			ActResourceDomain ra5 = new ActResourceDomain(5,0,50,3,3);
			ActOperationDomain oa = new ActOperationDomain(ActOperationDomain.REQUIRES,1,1);
			oa.setID(17);
			oa.addResource(ra1);
			oa.addResource(ra2);
			oa.addResource(ra3);
			oa.addResource(ra4);
			oa.addResource(ra5);
			ActivityDomain ad1 = new ActivityDomain(9,31,3,3);
			ad1.setChoicePointStack(cps);
			ad1.addOperation(oa);
			ad1.setID(27);
			assertEquals(9,ad1.getEarliestStartTime());
			assertEquals(33,ad1.getLatestEndTime());

			cps.push();
			ad1.setEarliestStartTime(1,10);
			ad1.removeStartTimes(1,13,17);
			ad1.removeStartTimes(1,21,25);
			cps.push();
			
			ad1.setEarliestStartTime(9);
			ad1.setLatestStartTime(9);
			
			assertEquals(9,ad1.getEarliestStartTime());
			assertEquals(11,ad1.getLatestEndTime());
			
			cps.pop();
			
			ad1.setEarliestStartTime(10);
			
			assertEquals(10,ad1.getEarliestStartTime());
			assertEquals(33,ad1.getLatestEndTime());
			
    	}
    	catch (PropagationFailureException pfe) {
    		fail(pfe.getLocalizedMessage());
    	}
    	
    	
    }
	
	public void testSettingEST() {
		try{
			ra1.setEarliestStartTime(15,true);
			ra2.setEarliestStartTime(17,true);
			ra3.setEarliestStartTime(13,true);
			
			assertEquals(13,oa1.getEarliestStartTime());
			assertEquals(17,oa1.getEarliestEndTime());
			
			assertEquals(13,aa.getEarliestStartTime());
			assertEquals(17,aa.getEarliestEndTime());
			
			ra4.setEarliestStartTime(17,true);
			ra5.setEarliestStartTime(27,true);
			
			assertEquals(17,oa2.getEarliestStartTime());
			assertEquals(21,oa2.getEarliestEndTime());
			
			assertEquals(17,aa.getEarliestStartTime());
			assertEquals(21,aa.getEarliestEndTime());
			//Since this is no longer a possibility for aa, it should be removed from everything
			assertEquals(17,oa1.getEarliestStartTime());
			assertEquals(21,oa1.getEarliestEndTime());
			
			assertEquals(17,ra1.getEarliestStartTime());
			assertEquals(21,ra1.getEarliestEndTime());
			assertEquals(17,ra2.getEarliestStartTime());
			assertEquals(21,ra2.getEarliestEndTime());
			assertEquals(17,ra3.getEarliestStartTime());
			assertEquals(21,ra3.getEarliestEndTime());
			assertEquals(17,ra4.getEarliestStartTime());
			assertEquals(21,ra4.getEarliestEndTime());
			assertEquals(27,ra5.getEarliestStartTime());
			assertEquals(31,ra5.getEarliestEndTime());
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
	
	public void testRemovalOfStartTime() {
		try {
			ra1.setEarliestStartTime(15,true);
			ra2.setEarliestStartTime(17,true);
			ra3.setEarliestStartTime(13,true);
			
			assertEquals(13,aa.getEarliestStartTime());
			assertEquals(17,aa.getEarliestEndTime());
			
			ra4.removeStartTimes(14,16,true);
			ra5.removeStartTimes(14,26,true);
			
			assertEquals(13,aa.getEarliestStartTime());
			assertEquals(17,aa.getEarliestEndTime());
			
			oa1.removeStartTime(13);
			
			assertEquals(17,aa.getEarliestStartTime());
			assertEquals(17,aa.getEarliestEndTime());
			
			assertEquals(17,ra1.getEarliestStartTime());
			assertEquals(17,ra2.getEarliestStartTime());
			assertEquals(17,ra3.getEarliestStartTime());
			assertEquals(17,ra4.getEarliestStartTime());
			assertEquals(27,ra5.getEarliestStartTime());
		}
		catch (PropagationFailureException pfe) {
			fail();
		}
	}
	
	
	public void testSetEst() {
		try {
			dom.setEarliestStartTime(41);
			
			assertEquals("41 is the est", 41, dom.getEarliestStartTime());
			assertEquals("100 is the lst", 100, dom.getLatestStartTime());
			
			assertEquals("1 resources for op1", 1, dom.getAvailResourceCount(1));
			assertEquals("2 resources for op2", 2, dom.getAvailResourceCount(2));
			assertEquals("3 resources for op3", 3, dom.getAvailResourceCount(3));
			
			int[] resources = dom.getRemainingResources(1);
			assertTrue("op1 uses resource 2", Arrays.binarySearch(resources, 1) <= -1);
			assertTrue("op1 uses resource 2", Arrays.binarySearch(resources, 2) > -1);
			resources = dom.getRemainingResources(2);
			assertTrue("op2 uses resource 2, 3", Arrays.binarySearch(resources, 2) > -1);
			assertTrue("op2 uses resource 2, 3", Arrays.binarySearch(resources, 3) > -1);
			resources = dom.getRemainingResources(3);
			assertTrue("op3 uses resource 4, 5, 6", Arrays.binarySearch(resources, 4) > -1);
			assertTrue("op3 uses resource 4, 5, 6", Arrays.binarySearch(resources, 5) > -1);
			assertTrue("op3 uses resource 4, 5, 6", Arrays.binarySearch(resources, 6) > -1);
			
			assertEquals("1 ops are assigned to resources", 1, dom.getAssignedOperations().length);
			assertEquals("1 ops are assigned to resources", 2, dom.getUnassignedOperations().length);
			
//			TODO            assertEquals("1 units of resource 2 are required", 1, dom.getActualRequiredAmount(2));
			
			assertEquals("resource 1: ---", Integer.MAX_VALUE, dom.getEarliestStartTime(1,1));
			assertEquals("resource 1: ---", Integer.MIN_VALUE, dom.getLatestStartTime(1,1));
			assertEquals("resource 2: 41..100", 41, dom.getEarliestStartTime(1,2));
			assertEquals("resource 2: 41..100", 100, dom.getLatestStartTime(1,2));
			assertEquals("resource 2: 41..100", 41, dom.getEarliestStartTime(2,2));
			assertEquals("resource 2: 41..100", 100, dom.getLatestStartTime(2,2));
			assertEquals("resource 3: 60..90", 60, dom.getEarliestStartTime(2,3));
			assertEquals("resource 3: 60..90", 90, dom.getLatestStartTime(2,3));
			assertEquals("resource 4: 41..50", 41, dom.getEarliestStartTime(3,4));
			assertEquals("resource 4: 41..50", 50, dom.getLatestStartTime(3,4));
			assertEquals("resource 5: 50..100", 50, dom.getEarliestStartTime(3,5));
			assertEquals("resource 5: 50..100", 100, dom.getLatestStartTime(3,5));
			assertEquals("resource 6: 41..70", 41, dom.getEarliestStartTime(3,6));
			assertEquals("resource 6: 41..70", 70, dom.getLatestStartTime(3,6));
			
			assertFalse("the domain is not bound", dom.isBound());
		}
		catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	public void testSetEstOnResourceWithoutEstChange() {
		try {
			dom.setEarliestStartTime(1,25);
			
			assertEquals("0 is the est", 0, dom.getEarliestStartTime());
			assertEquals("100 is the lst", 100, dom.getLatestStartTime());
			
			assertEquals("2 resources for op1", 2, dom.getAvailResourceCount(1));
			assertEquals("2 resources for op2", 2, dom.getAvailResourceCount((2)));
			assertEquals("3 resources for op3", 3, dom.getAvailResourceCount((3)));
			
			int[] resources = dom.getRemainingResources((1));
			assertTrue("op1 uses resource 1, 2", Arrays.binarySearch(resources, (1)) > -1);
			assertTrue("op1 uses resource 1, 2", Arrays.binarySearch(resources, (2)) > -1);
			resources = dom.getRemainingResources((2));
			assertTrue("op2 uses resource 2, 3", Arrays.binarySearch(resources, (2)) > -1);
			assertTrue("op2 uses resource 2, 3", Arrays.binarySearch(resources, (3)) > -1);
			resources = dom.getRemainingResources((3));
			assertTrue("op3 uses resource 4, 5, 6", Arrays.binarySearch(resources, (4)) > -1);
			assertTrue("op3 uses resource 4, 5, 6", Arrays.binarySearch(resources, (5)) > -1);
			assertTrue("op3 uses resource 4, 5, 6", Arrays.binarySearch(resources, (6)) > -1);
			
			assertEquals("no ops are assigned to resources", 0, dom.getAssignedOperations().length);
			assertEquals("no ops are assigned to resources", 3, dom.getUnassignedOperations().length);
			
			assertEquals("resource 1: 25..40", 25, dom.getEarliestStartTime(1,1));
			assertEquals("resource 1: 25..40", 40, dom.getLatestStartTime(1,1));
			assertEquals("resource 2: 0..100", 0, dom.getEarliestStartTime(1,2));
			assertEquals("resource 2: 0..100", 100, dom.getLatestStartTime(1,2));
			assertEquals("resource 2: 0..100", 0, dom.getEarliestStartTime(2,2));
			assertEquals("resource 2: 0..100", 100, dom.getLatestStartTime(2,2));
			assertEquals("resource 3: 60..90", 60, dom.getEarliestStartTime(2,3));
			assertEquals("resource 3: 60..90", 90, dom.getLatestStartTime(2,3));
			assertEquals("resource 4: 0..50", 0, dom.getEarliestStartTime(3,4));
			assertEquals("resource 4: 0..50", 50, dom.getLatestStartTime(3,4));
			assertEquals("resource 5: 50..100", 50, dom.getEarliestStartTime(3,5));
			assertEquals("resource 5: 50..100", 100, dom.getLatestStartTime(3,5));
			assertEquals("resource 6: 30..70", 30, dom.getEarliestStartTime(3,6));
			assertEquals("resource 6: 30..70", 70, dom.getLatestStartTime(3,6));
			
			assertFalse("the domain is not bound", dom.isBound());
		}
		catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	public void testSetEstOnResourceWithAssignedOperations() {
		try {
			dom.setEarliestStartTime(2,41);
			
			assertEquals("41 is the est", 41, dom.getEarliestStartTime());
			assertEquals("100 is the lst", 100, dom.getLatestStartTime());
			
			assertEquals("1 resources for op1", 1, dom.getAvailResourceCount((1)));
			assertEquals("2 resources for op2", 2, dom.getAvailResourceCount((2)));
			assertEquals("3 resources for op3", 3, dom.getAvailResourceCount((3)));
			
			int[] resources = dom.getRemainingResources((1));
			assertTrue("op1 uses resource 2", Arrays.binarySearch(resources, (1)) <= -1);
			assertTrue("op1 uses resource 2", Arrays.binarySearch(resources, (2)) > -1);
			resources = dom.getRemainingResources((2));
			assertTrue("op2 uses resource 2, 3", Arrays.binarySearch(resources, (2)) > -1);
			assertTrue("op2 uses resource 2, 3", Arrays.binarySearch(resources, (3)) > -1);
			resources = dom.getRemainingResources((3));
			assertTrue("op3 uses resource 4, 5, 6", Arrays.binarySearch(resources, (4)) > -1);
			assertTrue("op3 uses resource 4, 5, 6", Arrays.binarySearch(resources, (5)) > -1);
			assertTrue("op3 uses resource 4, 5, 6", Arrays.binarySearch(resources, (6)) > -1);
			
			assertEquals("1 ops are assigned to resources", 1, dom.getAssignedOperations().length);
			assertEquals("1 ops are assigned to resources", 2, dom.getUnassignedOperations().length);
			
//			TODO:such a method is not currently exposed            assertEquals("1 units of resource 2 are required", 1, dom.getActualRequiredAmount(2));
			
			assertEquals("resource 1: ---", Integer.MAX_VALUE, dom.getEarliestStartTime(1,1));
			assertEquals("resource 1: ---", Integer.MIN_VALUE, dom.getLatestStartTime(1,1));
			assertEquals("resource 2: 41..100", 41, dom.getEarliestStartTime(1,2));
			assertEquals("resource 2: 41..100", 100, dom.getLatestStartTime(1,2));
			assertEquals("resource 2: 41..100", 41, dom.getEarliestStartTime(2,2));
			assertEquals("resource 2: 41..100", 100, dom.getLatestStartTime(2,2));
			assertEquals("resource 3: 60..90", 60, dom.getEarliestStartTime(2,3));
			assertEquals("resource 3: 60..90", 90, dom.getLatestStartTime(2,3));
			assertEquals("resource 4: 41..50", 41, dom.getEarliestStartTime(3,4));
			assertEquals("resource 4: 41..50", 50, dom.getLatestStartTime(3,4));
			assertEquals("resource 5: 50..100", 50, dom.getEarliestStartTime(3,5));
			assertEquals("resource 5: 50..100", 100, dom.getLatestStartTime(3,5));
			assertEquals("resource 6: 41..70", 41, dom.getEarliestStartTime(3,6));
			assertEquals("resource 6: 41..70", 70, dom.getLatestStartTime(3,6));
			
			assertFalse("the domain is not bound", dom.isBound());
		}
		catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	public void testSetEstWithBoundDomain() {
		try {
			dom.setEarliestStartTime(100);
			
			assertEquals("100 is the est", 100, dom.getEarliestStartTime());
			assertEquals("100 is the lst", 100, dom.getLatestStartTime());
			
			assertEquals("1 resources for op1", 1, dom.getAvailResourceCount((1)));
			assertEquals("1 resources for op2", 1, dom.getAvailResourceCount((2)));
			assertEquals("1 resources for op3", 1, dom.getAvailResourceCount((3)));
			
			int[] resources = dom.getRemainingResources((1));
			assertTrue("op1 uses resource 2", Arrays.binarySearch(resources, (1)) <= -1);
			assertTrue("op1 uses resource 2", Arrays.binarySearch(resources, (2)) > -1);
			resources = dom.getRemainingResources((2));
			assertTrue("op2 uses resource 2", Arrays.binarySearch(resources, (2)) > -1);
			assertTrue("op2 uses resource 2", Arrays.binarySearch(resources, (3)) <= -1);
			resources = dom.getRemainingResources((3));
			assertTrue("op3 uses resource 5", Arrays.binarySearch(resources, (4)) <= -1);
			assertTrue("op3 uses resource 5", Arrays.binarySearch(resources, (5)) > -1);
			assertTrue("op3 uses resource 5", Arrays.binarySearch(resources, (6)) <= -1);
			
			assertEquals("all ops are assigned to resources", 3, dom.getAssignedOperations().length);
			assertEquals("all ops are assigned to resources", 0, dom.getUnassignedOperations().length);
			
//			TODO: this functionality does not currently exist, review            assertEquals("1 units of resource 2 are required", 1, dom.getActualRequiredAmount((2)));
//			assertEquals("1 units of resource 2 are consumed", 1, dom.getActualConsumedAmount((2)));
//			assertEquals("1 units of resource 5 are produced", 1, dom.getActualProducedAmount((5)));
			
			assertEquals("resource 1: ---", Integer.MAX_VALUE, dom.getEarliestStartTime(1,1));
			assertEquals("resource 1: ---", Integer.MIN_VALUE, dom.getLatestStartTime(1,1));
			assertEquals("resource 2: 100..100", 100, dom.getEarliestStartTime(1,2));
			assertEquals("resource 2: 100..100", 100, dom.getLatestStartTime(1,2));
			assertEquals("resource 2: 100..100", 100, dom.getEarliestStartTime(2,2));
			assertEquals("resource 2: 100..100", 100, dom.getLatestStartTime(2,2));
			assertEquals("resource 3: ---", Integer.MAX_VALUE, dom.getEarliestStartTime(2,3));
			assertEquals("resource 3: ---", Integer.MIN_VALUE, dom.getLatestStartTime(2,3));
			assertEquals("resource 4: ---", Integer.MAX_VALUE, dom.getEarliestStartTime(3,4));
			assertEquals("resource 4: ---", Integer.MIN_VALUE, dom.getLatestStartTime(3,4));
			assertEquals("resource 5: 100..100", 100, dom.getEarliestStartTime(3,5));
			assertEquals("resource 5: 100..100", 100, dom.getLatestStartTime(3,5));
			assertEquals("resource 6: ---", Integer.MAX_VALUE, dom.getEarliestStartTime(3,6));
			assertEquals("resource 6: ---", Integer.MIN_VALUE, dom.getLatestStartTime(3,6));
			
			assertTrue("the domain is bound", dom.isBound());
			assertEquals("100 is the activity time", 100, dom.getBoundStartTime());
//			TODO: this functionality may not make sense in the new model            assertEquals("100 is the activity time", 100, dom.getBoundTime((2)));
//			assertEquals("100 is the activity time", 100, dom.getBoundTime((5)));
		}
		catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	public void testSetLst() {
		try {
			dom.setLatestStartTime(70);
			
			assertEquals("0 is the est", 0, dom.getEarliestStartTime());
			assertEquals("70 is the lst", 70, dom.getLatestStartTime());
			
			assertEquals("2 resources for op1", 2, dom.getAvailResourceCount((1)));
			assertEquals("2 resources for op2", 2, dom.getAvailResourceCount((2)));
			assertEquals("3 resources for op3", 3, dom.getAvailResourceCount((3)));
			
			int[] resources = dom.getRemainingResources((1));
			assertTrue("op1 uses resource 1, 2", Arrays.binarySearch(resources, (1)) > -1);
			assertTrue("op1 uses resource 1, 2", Arrays.binarySearch(resources, (2)) > -1);
			resources = dom.getRemainingResources((2));
			assertTrue("op2 uses resource 2, 3", Arrays.binarySearch(resources, (2)) > -1);
			assertTrue("op2 uses resource 2, 3", Arrays.binarySearch(resources, (3)) > -1);
			resources = dom.getRemainingResources((3));
			assertTrue("op3 uses resource 4, 5, 6", Arrays.binarySearch(resources, (4)) > -1);
			assertTrue("op3 uses resource 4, 5, 6", Arrays.binarySearch(resources, (5)) > -1);
			assertTrue("op3 uses resource 4, 5, 6", Arrays.binarySearch(resources, (6)) > -1);
			
			assertEquals("no ops are assigned to resources", 0, dom.getAssignedOperations().length);
			assertEquals("no ops are assigned to resources", 3, dom.getUnassignedOperations().length);
			
			assertEquals("resource 1: 10..40", 10, dom.getEarliestStartTime(1,1));
			assertEquals("resource 1: 10..40", 40, dom.getLatestStartTime(1,1));
			assertEquals("resource 2: 0..70", 0, dom.getEarliestStartTime(1,2));
			assertEquals("resource 2: 0..70", 70, dom.getLatestStartTime(1,2));
			assertEquals("resource 2: 0..70", 0, dom.getEarliestStartTime(2,2));
			assertEquals("resource 2: 0..70", 70, dom.getLatestStartTime(2,2));
			assertEquals("resource 3: 60..70", 60, dom.getEarliestStartTime(2,3));
			assertEquals("resource 3: 60..70", 70, dom.getLatestStartTime(2,3));
			assertEquals("resource 4: 0..50", 0, dom.getEarliestStartTime(3,4));
			assertEquals("resource 4: 0..50", 50, dom.getLatestStartTime(3,4));
			assertEquals("resource 5: 50..70", 50, dom.getEarliestStartTime(3,5));
			assertEquals("resource 5: 50..70", 70, dom.getLatestStartTime(3,5));
			assertEquals("resource 6: 30..70", 30, dom.getEarliestStartTime(3,6));
			assertEquals("resource 6: 30..70", 70, dom.getLatestStartTime(3,6));
			
			assertFalse("the domain is not bound", dom.isBound());
		}
		catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	public void testSetLstOnResourceWithoutLstChange() {
		try {
			dom.setLatestStartTime(3,75);
			
			assertEquals("0 is the est", 0, dom.getEarliestStartTime());
			assertEquals("100 is the lst", 100, dom.getLatestStartTime());
			
			assertEquals("2 resources for op1", 2, dom.getAvailResourceCount((1)));
			assertEquals("2 resources for op2", 2, dom.getAvailResourceCount((2)));
			assertEquals("3 resources for op3", 3, dom.getAvailResourceCount((3)));
			
			int[] resources = dom.getRemainingResources((1));
			assertTrue("op1 uses resource 1, 2", Arrays.binarySearch(resources, (1)) > -1);
			assertTrue("op1 uses resource 1, 2", Arrays.binarySearch(resources, (2)) > -1);
			resources = dom.getRemainingResources((2));
			assertTrue("op2 uses resource 2, 3", Arrays.binarySearch(resources, (2)) > -1);
			assertTrue("op2 uses resource 2, 3", Arrays.binarySearch(resources, (3)) > -1);
			resources = dom.getRemainingResources((3));
			assertTrue("op3 uses resource 4, 5, 6", Arrays.binarySearch(resources, (4)) > -1);
			assertTrue("op3 uses resource 4, 5, 6", Arrays.binarySearch(resources, (5)) > -1);
			assertTrue("op3 uses resource 4, 5, 6", Arrays.binarySearch(resources, (6)) > -1);
			
			assertEquals("no ops are assigned to resources", 0, dom.getAssignedOperations().length);
			assertEquals("no ops are assigned to resources", 3, dom.getUnassignedOperations().length);
			
			assertEquals("resource 1: 10..40", 10, dom.getEarliestStartTime(1,1));
			assertEquals("resource 1: 10..40", 40, dom.getLatestStartTime(1,1));
			assertEquals("resource 2: 0..100", 0, dom.getEarliestStartTime(1,2));
			assertEquals("resource 2: 0..100", 100, dom.getLatestStartTime(1,2));
			assertEquals("resource 2: 0..100", 0, dom.getEarliestStartTime(2,2));
			assertEquals("resource 2: 0..100", 100, dom.getLatestStartTime(2,2));
			assertEquals("resource 3: 60..75", 60, dom.getEarliestStartTime(2,3));
			assertEquals("resource 3: 60..75", 75, dom.getLatestStartTime(2,3));
			assertEquals("resource 4: 0..50", 0, dom.getEarliestStartTime(3,4));
			assertEquals("resource 4: 0..50", 50, dom.getLatestStartTime(3,4));
			assertEquals("resource 5: 50..100", 50, dom.getEarliestStartTime(3,5));
			assertEquals("resource 5: 50..100", 100, dom.getLatestStartTime(3,5));
			assertEquals("resource 6: 30..70", 30, dom.getEarliestStartTime(3,6));
			assertEquals("resource 6: 30..70", 70, dom.getLatestStartTime(3,6));
			
			assertFalse("the domain is not bound", dom.isBound());
		}
		catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	public void testSetLstOnResourceWithLstChange() {
		try {
			dom.setLatestStartTime(2,25);
			
			assertEquals("0 is the est", 0, dom.getEarliestStartTime());
			assertEquals("25 is the lst", 25, dom.getLatestStartTime());
			
			assertEquals("2 resources for op1", 2, dom.getAvailResourceCount((1)));
			assertEquals("1 resources for op2", 1, dom.getAvailResourceCount((2)));
			assertEquals("1 resources for op3", 1, dom.getAvailResourceCount((3)));
			
			int[] resources = dom.getRemainingResources((1));
			assertTrue("op1 uses resource 1, 2", Arrays.binarySearch(resources, (1)) > -1);
			assertTrue("op1 uses resource 1, 2", Arrays.binarySearch(resources, (2)) > -1);
			resources = dom.getRemainingResources((2));
			assertTrue("op2 uses resource 2", Arrays.binarySearch(resources, (2)) > -1);
			assertTrue("op2 uses resource 2", Arrays.binarySearch(resources, (3)) <= -1);
			resources = dom.getRemainingResources((3));
			assertTrue("op3 uses resource 4", Arrays.binarySearch(resources, (4)) > -1);
			assertTrue("op3 uses resource 4", Arrays.binarySearch(resources, (5)) <= -1);
			assertTrue("op3 uses resource 4", Arrays.binarySearch(resources, (6)) <= -1);
			
			assertEquals("2 ops are assigned to resources", 2, dom.getAssignedOperations().length);
			assertEquals("2 ops are assigned to resources", 1, dom.getUnassignedOperations().length);
			
//			TODO:            assertEquals("1 units of resource 2 are consumed", 1, dom.getActualConsumedAmount((2)));
//			assertEquals("1 units of resource 4 are produced", 1, dom.getActualProducedAmount((4)));
			
			assertEquals("resource 1: 10..25", 10, dom.getEarliestStartTime(1,1));
			assertEquals("resource 1: 10..25", 25, dom.getLatestStartTime(1,1));
			assertEquals("resource 2: 0..25", 0, dom.getEarliestStartTime(1,2));
			assertEquals("resource 2: 0..25", 25, dom.getLatestStartTime(1,2));
			assertEquals("resource 2: 0..25", 0, dom.getEarliestStartTime(2,2));
			assertEquals("resource 2: 0..25", 25, dom.getLatestStartTime(2,2));
			assertEquals("resource 3: ---", Integer.MAX_VALUE, dom.getEarliestStartTime(2,3));
			assertEquals("resource 3: ---", Integer.MIN_VALUE, dom.getLatestStartTime(2,3));
			assertEquals("resource 4: 0..25", 0, dom.getEarliestStartTime(3,4));
			assertEquals("resource 4: 0..25", 25, dom.getLatestStartTime(3,4));
			assertEquals("resource 5: ---", Integer.MAX_VALUE, dom.getEarliestStartTime(3,5));
			assertEquals("resource 5: ---", Integer.MIN_VALUE, dom.getLatestStartTime(3,5));
			assertEquals("resource 6: ---", Integer.MAX_VALUE, dom.getEarliestStartTime(3,6));
			assertEquals("resource 6: ---", Integer.MIN_VALUE, dom.getLatestStartTime(3,6));
			
			assertFalse("the domain is not bound", dom.isBound());
		}
		catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	public void testSetLstWithBoundDomain() {
		try {
			dom.setLatestStartTime(0);
			
			assertEquals("0 is the est", 0, dom.getEarliestStartTime());
			assertEquals("0 is the lst", 0, dom.getLatestStartTime());
			
			assertEquals("1 resources for op1", 1, dom.getAvailResourceCount((1)));
			assertEquals("1 resources for op2", 1, dom.getAvailResourceCount((2)));
			assertEquals("1 resources for op3", 1, dom.getAvailResourceCount((3)));
			
			int[] resources = dom.getRemainingResources((1));
			assertTrue("op1 uses resource 2", Arrays.binarySearch(resources, (1)) <= -1);
			assertTrue("op1 uses resource 2", Arrays.binarySearch(resources, (2)) > -1);
			resources = dom.getRemainingResources((2));
			assertTrue("op2 uses resource 2", Arrays.binarySearch(resources, (2)) > -1);
			assertTrue("op2 uses resource 2", Arrays.binarySearch(resources, (3)) <= -1);
			resources = dom.getRemainingResources((3));
			assertTrue("op3 uses resource 4", Arrays.binarySearch(resources, (4)) > -1);
			assertTrue("op3 uses resource 4", Arrays.binarySearch(resources, (5)) <= -1);
			assertTrue("op3 uses resource 4", Arrays.binarySearch(resources, (6)) <= -1);
			
			assertEquals("all ops are assigned to resources", 3, dom.getAssignedOperations().length);
			assertEquals("all ops are assigned to resources", 0, dom.getUnassignedOperations().length);
			
//			TODO            assertEquals("1 units of resource 2 are required", 1, dom.getActualRequiredAmount((2)));
//			assertEquals("1 units of resource 2 are consumed", 1, dom.getActualConsumedAmount((2)));
//			assertEquals("1 units of resource 4 are produced", 1, dom.getActualProducedAmount((4)));
			
			assertEquals("resource 1: ---", Integer.MAX_VALUE, dom.getEarliestStartTime(1,1));
			assertEquals("resource 1: ---", Integer.MIN_VALUE, dom.getLatestStartTime(1,1));
			assertEquals("resource 2: 0..0", 0, dom.getEarliestStartTime(1,2));
			assertEquals("resource 2: 0..0", 0, dom.getLatestStartTime(1,2));
			assertEquals("resource 2: 0..0", 0, dom.getEarliestStartTime(2,2));
			assertEquals("resource 2: 0..0", 0, dom.getLatestStartTime(2,2));
			assertEquals("resource 3: ---", Integer.MAX_VALUE, dom.getEarliestStartTime(2,3));
			assertEquals("resource 3: ---", Integer.MIN_VALUE, dom.getLatestStartTime(2,3));
			assertEquals("resource 4: 0..0", 0, dom.getEarliestStartTime(3,4));
			assertEquals("resource 4: 0..0", 0, dom.getLatestStartTime(3,4));
			assertEquals("resource 5: ---", Integer.MAX_VALUE, dom.getEarliestStartTime(3,5));
			assertEquals("resource 5: ---", Integer.MIN_VALUE, dom.getLatestStartTime(3,5));
			assertEquals("resource 6: ---", Integer.MAX_VALUE, dom.getEarliestStartTime(3,6));
			assertEquals("resource 6: ---", Integer.MIN_VALUE, dom.getLatestStartTime(3,6));
			
			assertTrue("the domain is bound", dom.isBound());
			assertEquals("0 is the activity time", 0, dom.getBoundStartTime());
//			assertEquals("0 is the activity time", 0, dom.getBoundTime((2)));
//			assertEquals("0 is the activity time", 0, dom.getBoundTime((4)));
		}
		catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	public void testRemoveTimeRange() {
		try {
			dom.removeStartTimes(30, 70);
			
			assertEquals("0 is the est", 0, dom.getEarliestStartTime());
			assertEquals("100 is the lst", 100, dom.getLatestStartTime());
			
			assertEquals("2 resources for op1", 2, dom.getAvailResourceCount((1)));
			assertEquals("2 resources for op2", 2, dom.getAvailResourceCount((2)));
			assertEquals("2 resources for op3", 2, dom.getAvailResourceCount((3)));
			
			int[] resources = dom.getRemainingResources((1));
			assertTrue("op1 uses resource 1, 2", Arrays.binarySearch(resources, (1)) > -1);
			assertTrue("op1 uses resource 1, 2", Arrays.binarySearch(resources, (2)) > -1);
			resources = dom.getRemainingResources((2));
			assertTrue("op2 uses resource 2, 3", Arrays.binarySearch(resources, (2)) > -1);
			assertTrue("op2 uses resource 2, 3", Arrays.binarySearch(resources, (3)) > -1);
			resources = dom.getRemainingResources((3));
			assertTrue("op3 uses resource 4, 5", Arrays.binarySearch(resources, (4)) > -1);
			assertTrue("op3 uses resource 4, 5", Arrays.binarySearch(resources, (5)) > -1);
			assertTrue("op3 uses resource 4, 5", Arrays.binarySearch(resources, (6)) <= -1);
			
			assertEquals("no ops are assigned to resources", 0, dom.getAssignedOperations().length);
			assertEquals("no ops are assigned to resources", 3, dom.getUnassignedOperations().length);
			
			assertEquals("resource 1: 10..29", 10, dom.getEarliestStartTime(1,1));
			assertEquals("resource 1: 10..29", 29, dom.getLatestStartTime(1,1));
			assertEquals("resource 3: 71..90", 71, dom.getEarliestStartTime(2,3));
			assertEquals("resource 3: 71..90", 90, dom.getLatestStartTime(2,3));
			assertEquals("resource 4: 0..29", 0, dom.getEarliestStartTime(3,4));
			assertEquals("resource 4: 0..29", 29, dom.getLatestStartTime(3,4));
			assertEquals("resource 5: 71..100", 71, dom.getEarliestStartTime(3,5));
			assertEquals("resource 5: 71..100", 100, dom.getLatestStartTime(3,5));
			assertEquals("resource 6: ---", Integer.MAX_VALUE, dom.getEarliestStartTime(3,6));
			assertEquals("resource 6: ---", Integer.MIN_VALUE, dom.getLatestStartTime(3,6));
			
			assertFalse("the domain is not bound", dom.isBound());
		}
		catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	
	public void testRemoveTimeRangeOnResourceWithoutChange() {
		try {
			dom.removeStartTimes((6), 40, 60);
			
			assertEquals("0 is the est", 0, dom.getEarliestStartTime());
			assertEquals("100 is the lst", 100, dom.getLatestStartTime());
			
			assertEquals("2 resources for op1", 2, dom.getAvailResourceCount((1)));
			assertEquals("2 resources for op2", 2, dom.getAvailResourceCount((2)));
			assertEquals("3 resources for op3", 3, dom.getAvailResourceCount((3)));
			
			int[] resources = dom.getRemainingResources((1));
			assertTrue("op1 uses resource 1, 2", Arrays.binarySearch(resources, (1)) > -1);
			assertTrue("op1 uses resource 1, 2", Arrays.binarySearch(resources, (2)) > -1);
			resources = dom.getRemainingResources((2));
			assertTrue("op2 uses resource 2, 3", Arrays.binarySearch(resources, (2)) > -1);
			assertTrue("op2 uses resource 2, 3", Arrays.binarySearch(resources, (3)) > -1);
			resources = dom.getRemainingResources((3));
			assertTrue("op3 uses resource 4, 5, 6", Arrays.binarySearch(resources, (4)) > -1);
			assertTrue("op3 uses resource 4, 5, 6", Arrays.binarySearch(resources, (5)) > -1);
			assertTrue("op3 uses resource 4, 5, 6", Arrays.binarySearch(resources, (6)) > -1);
			
			assertEquals("no ops are assigned to resources", 0, dom.getAssignedOperations().length);
			assertEquals("no ops are assigned to resources", 3, dom.getUnassignedOperations().length);
			
			assertEquals("resource 1: 10..40", 10, dom.getEarliestStartTime(1,1));
			assertEquals("resource 1: 10..40", 40, dom.getLatestStartTime(1,1));
			assertEquals("resource 2: 0..100", 0, dom.getEarliestStartTime(1,2));
			assertEquals("resource 2: 0..100", 100, dom.getLatestStartTime(1,2));
			assertEquals("resource 2: 0..100", 0, dom.getEarliestStartTime(2,2));
			assertEquals("resource 2: 0..100", 100, dom.getLatestStartTime(2,2));
			assertEquals("resource 3: 60..90", 60, dom.getEarliestStartTime(2,3));
			assertEquals("resource 3: 60..90", 90, dom.getLatestStartTime(2,3));
			assertEquals("resource 4: 0..50", 0, dom.getEarliestStartTime(3,4));
			assertEquals("resource 4: 0..50", 50, dom.getLatestStartTime(3,4));
			assertEquals("resource 5: 50..100", 50, dom.getEarliestStartTime(3,5));
			assertEquals("resource 5: 50..100", 100, dom.getLatestStartTime(3,5));
			assertEquals("resource 6: 30..70", 30, dom.getEarliestStartTime(3,6));
			assertEquals("resource 6: 30..70", 70, dom.getLatestStartTime(3,6));
			
//			Timeline tl = dom.getTimeline((6));
//			int index = tl.getFirstIntervalIndex();
//			int count = 0;
//			while (index != -1) {
//			switch (count) {
//			case 0:
//			assertEquals("resource 6: 30..39, 61..70", 30, tl.getMin(index));
//			assertEquals("resource 6: 30..39, 61..70", 39, tl.getMax(index));
//			break;
//			case 1:
//			assertEquals("resource 6: 30..39, 61..70", 61, tl.getMin(index));
//			assertEquals("resource 6: 30..39, 61..70", 70, tl.getMax(index));
//			break;
//			}
//			
//			index = tl.getNextIntervalIndex(index);
//			count++;
//			}
			
			assertFalse("the domain is not bound", dom.isBound());
		}
		catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	public void testRemoveTimeRangeOnResourceWithChange() {
		try {
			dom.removeStartTimes(2, 35, 65);
			
			assertEquals("0 is the est", 0, dom.getEarliestStartTime());
			assertEquals("100 is the lst", 100, dom.getLatestStartTime());
			
			assertEquals("2 resources for op1", 2, dom.getAvailResourceCount((1)));
			assertEquals("2 resources for op2", 2, dom.getAvailResourceCount((2)));
			assertEquals("3 resources for op3", 3, dom.getAvailResourceCount((3)));
			
			int[] resources = dom.getRemainingResources((1));
			assertTrue("op1 uses resource 1, 2", Arrays.binarySearch(resources, (1)) > -1);
			assertTrue("op1 uses resource 1, 2", Arrays.binarySearch(resources, (2)) > -1);
			resources = dom.getRemainingResources((2));
			assertTrue("op2 uses resource 2, 3", Arrays.binarySearch(resources, (2)) > -1);
			assertTrue("op2 uses resource 2, 3", Arrays.binarySearch(resources, (3)) > -1);
			resources = dom.getRemainingResources((3));
			assertTrue("op3 uses resource 4, 5, 6", Arrays.binarySearch(resources, (4)) > -1);
			assertTrue("op3 uses resource 4, 5, 6", Arrays.binarySearch(resources, (5)) > -1);
			assertTrue("op3 uses resource 4, 5, 6", Arrays.binarySearch(resources, (6)) > -1);
			
			assertEquals("no ops are assigned to resources", 0, dom.getAssignedOperations().length);
			assertEquals("no ops are assigned to resources", 3, dom.getUnassignedOperations().length);
			
			assertEquals("resource 1: 10..34", 10, dom.getEarliestStartTime(1,1));
			assertEquals("resource 1: 10..34", 34, dom.getLatestStartTime(1,1));
//			Timeline tl = dom.getTimeline((2));
//			int index = tl.getFirstIntervalIndex();
//			int count = 0;
//			while (index != -1) {
//			switch (count) {
//			case 0:
//			assertEquals("resource 2: 0..34, 66..100", 0, tl.getMin(index));
//			assertEquals("resource 2: 0..34, 66..100", 34, tl.getMax(index));
//			break;
//			case 1:
//			assertEquals("resource 2: 0..34, 66..100", 66, tl.getMin(index));
//			assertEquals("resource 2: 0..34, 66..100", 100, tl.getMax(index));
//			break;
//			}
//			
//			index = tl.getNextIntervalIndex(index);
//			count++;
//			}
			assertEquals("resource 2: 0..100", 0, dom.getEarliestStartTime(1,2));
			assertEquals("resource 2: 0..100", 100, dom.getLatestStartTime(1,2));
			assertEquals("resource 2: 0..100", 0, dom.getEarliestStartTime(2,2));
			assertEquals("resource 2: 0..100", 100, dom.getLatestStartTime(2,2));
			assertEquals("resource 3: 66..90", 66, dom.getEarliestStartTime(2,3));
			assertEquals("resource 3: 66..90", 90, dom.getLatestStartTime(2,3));
			assertEquals("resource 4: 0..34", 0, dom.getEarliestStartTime(3,4));
			assertEquals("resource 4: 0..34", 34, dom.getLatestStartTime(3,4));
			assertEquals("resource 5: 66..100", 66, dom.getEarliestStartTime(3,5));
			assertEquals("resource 5: 66..100", 100, dom.getLatestStartTime(3,5));
			assertEquals("resource 6: 66..100", 66, dom.getEarliestStartTime(3,5));
			assertEquals("resource 6: 66..100", 100, dom.getLatestStartTime(3,5));
//			tl = dom.getTimeline((6));
//			index = tl.getFirstIntervalIndex();
//			count = 0;
//			while (index != -1) {
//			switch (count) {
//			case 0:
//			assertEquals("resource 6: 30..34, 66..70", 30, tl.getMin(index));
//			assertEquals("resource 6: 30..34, 66..70", 34, tl.getMax(index));
//			break;
//			case 1:
//			assertEquals("resource 6: 30..34, 66..70", 66, tl.getMin(index));
//			assertEquals("resource 6: 30..34, 66..70", 70, tl.getMax(index));
//			break;
//			}
//			
//			index = tl.getNextIntervalIndex(index);
//			count++;
//			}
			
			assertFalse("the domain is not bound", dom.isBound());
		}
		catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	public void testRemovePossibleResourceWithoutChange() {
		try {
			dom.removePossibleResource(1, 1);
			
			assertEquals("0 is the est", 0, dom.getEarliestStartTime());
			assertEquals("100 is the lst", 100, dom.getLatestStartTime());
			
			assertEquals("1 resources for op1", 1, dom.getAvailResourceCount((1)));
			assertEquals("2 resources for op2", 2, dom.getAvailResourceCount((2)));
			assertEquals("3 resources for op3", 3, dom.getAvailResourceCount((3)));
			
			int[] resources = dom.getRemainingResources((1));
			assertTrue("op1 uses resource 2", Arrays.binarySearch(resources, (1)) <= -1);
			assertTrue("op1 uses resource 2", Arrays.binarySearch(resources, (2)) > -1);
			resources = dom.getRemainingResources((2));
			assertTrue("op2 uses resource 2, 3", Arrays.binarySearch(resources, (2)) > -1);
			assertTrue("op2 uses resource 2, 3", Arrays.binarySearch(resources, (3)) > -1);
			resources = dom.getRemainingResources((3));
			assertTrue("op3 uses resource 4, 5, 6", Arrays.binarySearch(resources, (4)) > -1);
			assertTrue("op3 uses resource 4, 5, 6", Arrays.binarySearch(resources, (5)) > -1);
			assertTrue("op3 uses resource 4, 5, 6", Arrays.binarySearch(resources, (6)) > -1);
			
			assertEquals("1 ops are assigned to resources", 1, dom.getAssignedOperations().length);
			assertEquals("1 ops are assigned to resources", 2, dom.getUnassignedOperations().length);
			
//			TODO            assertEquals("1 units of resource 2 are required", 1, dom.getActualRequiredAmount((2)));
			
			assertEquals("resource 1: Empty!", Integer.MAX_VALUE, dom.getEarliestStartTime(1,1));
			assertEquals("resource 1: Empty!", Integer.MIN_VALUE, dom.getLatestStartTime(1,1));
			assertEquals("resource 2: 0..100", 0, dom.getEarliestStartTime(1,2));
			assertEquals("resource 2: 0..100", 100, dom.getLatestStartTime(1,2));
			assertEquals("resource 2: 0..100", 0, dom.getEarliestStartTime(2,2));
			assertEquals("resource 2: 0..100", 100, dom.getLatestStartTime(2,2));
			assertEquals("resource 3: 60..90", 60, dom.getEarliestStartTime(2,3));
			assertEquals("resource 3: 60..90", 90, dom.getLatestStartTime(2,3));
			assertEquals("resource 4: 0..50", 0, dom.getEarliestStartTime(3,4));
			assertEquals("resource 4: 0..50", 50, dom.getLatestStartTime(3,4));
			assertEquals("resource 5: 50..100", 50, dom.getEarliestStartTime(3,5));
			assertEquals("resource 5: 50..100", 100, dom.getLatestStartTime(3,5));
			assertEquals("resource 6: 30..70", 30, dom.getEarliestStartTime(3,6));
			assertEquals("resource 6: 30..70", 70, dom.getLatestStartTime(3,6));
			
			assertFalse("the domain is not bound", dom.isBound());
		}
		catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	public void testRemovePossibleResourceWithChange() {
		try {
			dom.removePossibleResource(1, 2);
			
			assertEquals("10 is the est", 10, dom.getEarliestStartTime());
			assertEquals("40 is the lst", 40, dom.getLatestStartTime());
			
			assertEquals("1 resources for op1", 1, dom.getAvailResourceCount((1)));
			assertEquals("1 resources for op2", 1, dom.getAvailResourceCount((2)));
			assertEquals("2 resources for op3", 2, dom.getAvailResourceCount((3)));
			
			int[] resources = dom.getRemainingResources((1));
			assertTrue("op1 uses resource 1", Arrays.binarySearch(resources, (1)) > -1);
			assertTrue("op1 uses resource 1", Arrays.binarySearch(resources, (2)) <= -1);
			resources = dom.getRemainingResources((2));
			assertTrue("op2 uses resource 2", Arrays.binarySearch(resources, (2)) > -1);
			assertTrue("op2 uses resource 2", Arrays.binarySearch(resources, (3)) <= -1);
			resources = dom.getRemainingResources((3));
			assertTrue("op3 uses resource 4, 6", Arrays.binarySearch(resources, (4)) > -1);
			assertTrue("op3 uses resource 4, 6", Arrays.binarySearch(resources, (5)) <= -1);
			assertTrue("op3 uses resource 4, 6", Arrays.binarySearch(resources, (6)) > -1);
			
			assertEquals("2 ops are assigned to resources", 2, dom.getAssignedOperations().length);
			assertEquals("2 ops are assigned to resources", 1, dom.getUnassignedOperations().length);
			
//			TODO            assertEquals("1 units of resource 1 are required", 1, dom.getActualRequiredAmount((1)));
//			assertEquals("1 units of resource 2 are consumed", 1, dom.getActualConsumedAmount((2)));
			
			assertEquals("resource 1: 10..40", 10, dom.getEarliestStartTime(1,1));
			assertEquals("resource 1: 10..40", 40, dom.getLatestStartTime(1,1));
			assertEquals("resource 2: Empty", Integer.MAX_VALUE, dom.getEarliestStartTime(1,2));
			assertEquals("resource 2: Empty", Integer.MIN_VALUE, dom.getLatestStartTime(1,2));
			assertEquals("resource 2: 10..40", 10, dom.getEarliestStartTime(2,2));
			assertEquals("resource 2: 10..40", 40, dom.getLatestStartTime(2,2));
			assertEquals("resource 3: ---", Integer.MAX_VALUE, dom.getEarliestStartTime(2,3));
			assertEquals("resource 3: ---", Integer.MIN_VALUE, dom.getLatestStartTime(2,3));
			assertEquals("resource 4: 10..40", 10, dom.getEarliestStartTime(3,4));
			assertEquals("resource 4: 10..40", 40, dom.getLatestStartTime(3,4));
			assertEquals("resource 5: ---", Integer.MAX_VALUE, dom.getEarliestStartTime(3,5));
			assertEquals("resource 5: ---", Integer.MIN_VALUE, dom.getLatestStartTime(3,5));
			assertEquals("resource 6: 30..40", 30, dom.getEarliestStartTime(3,6));
			assertEquals("resource 6: 30..40", 40, dom.getLatestStartTime(3,6));
			
			assertFalse("the domain is not bound", dom.isBound());
		}
		catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	public void testSetRequiredResource() {
		try {
			dom.setRequiredResource(3,6);
			
			assertEquals("30 is the est", 30, dom.getEarliestStartTime());
			assertEquals("70 is the lst", 70, dom.getLatestStartTime());
			
			assertEquals("1 resources for op1", 2, dom.getAvailResourceCount((1)));
			assertEquals("2 resources for op2", 2, dom.getAvailResourceCount((2)));
			assertEquals("1 resources for op3", 1, dom.getAvailResourceCount((3)));
			
			int[] resources = dom.getRemainingResources((1));
			assertTrue("op1 uses resource 1, 2", Arrays.binarySearch(resources, (1)) > -1);
			assertTrue("op1 uses resource 1, 2", Arrays.binarySearch(resources, (2)) > -1);
			resources = dom.getRemainingResources((2));
			assertTrue("op2 uses resource 2, 3", Arrays.binarySearch(resources, (2)) > -1);
			assertTrue("op2 uses resource 2, 3", Arrays.binarySearch(resources, (3)) > -1);
			resources = dom.getRemainingResources((3));
			assertTrue("op3 uses resource 6", Arrays.binarySearch(resources, (4)) <= -1);
			assertTrue("op3 uses resource 6", Arrays.binarySearch(resources, (5)) <= -1);
			assertTrue("op3 uses resource 6", Arrays.binarySearch(resources, (6)) > -1);
			
			assertEquals("1 ops are assigned to resources", 1, dom.getAssignedOperations().length);
			assertEquals("1 ops are assigned to resources", 2, dom.getUnassignedOperations().length);
			
//			TODO            assertEquals("1 units of resource 6 are produced", 1, dom.getActualProducedAmount((6)));
			
			assertEquals("resource 1: 30..40", 30, dom.getEarliestStartTime(1,1));
			assertEquals("resource 1: 30..40", 40, dom.getLatestStartTime(1,1));
			assertEquals("resource 2: 30..70", 30, dom.getEarliestStartTime(1,2));
			assertEquals("resource 2: 30..70", 70, dom.getLatestStartTime(1,2));
			assertEquals("resource 2: 30..70", 30, dom.getEarliestStartTime(2,2));
			assertEquals("resource 2: 30..70", 70, dom.getLatestStartTime(2,2));
			assertEquals("resource 3: 60..70", 60, dom.getEarliestStartTime(2,3));
			assertEquals("resource 3: 60..70", 70, dom.getLatestStartTime(2,3));
			assertEquals("resource 4: ---", Integer.MAX_VALUE, dom.getEarliestStartTime(3,4));
			assertEquals("resource 4: ---", Integer.MIN_VALUE, dom.getLatestStartTime(3,4));
			assertEquals("resource 5: ---", Integer.MAX_VALUE, dom.getEarliestStartTime(3,5));
			assertEquals("resource 5: ---", Integer.MIN_VALUE, dom.getLatestStartTime(3,5));
			assertEquals("resource 6: 30..70", 30, dom.getEarliestStartTime(3,6));
			assertEquals("resource 6: 30..70", 70, dom.getLatestStartTime(3,6));
			
			assertFalse("the domain is not bound", dom.isBound());
		}
		catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	public void testCPS() {
		try {
			ChoicePointStack cps = new ChoicePointStack();
			dom.setChoicePointStack(cps);
			
			dom.setEarliestStartTime(45);
			
			assertEquals("45 is the est", 45, dom.getEarliestStartTime());
			assertEquals("100 is the lst", 100, dom.getLatestStartTime());
			
			assertEquals("1 resources for op1", 1, dom.getAvailResourceCount((1)));
			assertEquals("2 resources for op2", 2, dom.getAvailResourceCount((2)));
			assertEquals("3 resources for op3", 3, dom.getAvailResourceCount((3)));
			
			int[] resources = dom.getRemainingResources((1));
			assertTrue("op1 uses resource 2", Arrays.binarySearch(resources, (1)) <= -1);
			assertTrue("op1 uses resource 2", Arrays.binarySearch(resources, (2)) > -1);
			resources = dom.getRemainingResources((2));
			assertTrue("op2 uses resource 2, 3", Arrays.binarySearch(resources, (2)) > -1);
			assertTrue("op2 uses resource 2, 3", Arrays.binarySearch(resources, (3)) > -1);
			resources = dom.getRemainingResources((3));
			assertTrue("op3 uses resource 4, 5, 6", Arrays.binarySearch(resources, (4)) > -1);
			assertTrue("op3 uses resource 4, 5, 6", Arrays.binarySearch(resources, (5)) > -1);
			assertTrue("op3 uses resource 4, 5, 6", Arrays.binarySearch(resources, (6)) > -1);
			
			assertEquals("1 ops are assigned to resources", 1, dom.getAssignedOperations().length);
			assertEquals("1 ops are assigned to resources", 2, dom.getUnassignedOperations().length);
			
//			TODO            assertEquals("1 units of resource 2 are required", 1, dom.getActualRequiredAmount((2)));
			
			assertEquals("resource 1: ---", Integer.MAX_VALUE, dom.getEarliestStartTime(1,1));
			assertEquals("resource 1: ---", Integer.MIN_VALUE, dom.getLatestStartTime(1,1));
			assertEquals("resource 2: 45..100", 45, dom.getEarliestStartTime(1,2));
			assertEquals("resource 2: 45..100", 100, dom.getLatestStartTime(1,2));
			assertEquals("resource 2: 45..100", 45, dom.getEarliestStartTime(2,2));
			assertEquals("resource 2: 45..100", 100, dom.getLatestStartTime(2,2));
			assertEquals("resource 3: 60..90", 60, dom.getEarliestStartTime(2,3));
			assertEquals("resource 3: 60..90", 90, dom.getLatestStartTime(2,3));
			assertEquals("resource 4: 45..50", 45, dom.getEarliestStartTime(3,4));
			assertEquals("resource 4: 45..50", 50, dom.getLatestStartTime(3,4));
			assertEquals("resource 5: 50..100", 50, dom.getEarliestStartTime(3,5));
			assertEquals("resource 5: 50..100", 100, dom.getLatestStartTime(3,5));
			assertEquals("resource 6: 45..70", 45, dom.getEarliestStartTime(3,6));
			assertEquals("resource 6: 45..70", 70, dom.getLatestStartTime(3,6));
			
			assertFalse("the domain is not bound", dom.isBound());
			
			// push the cps
			cps.push();
			
			dom.setLatestStartTime(55);
			
			assertEquals("45 is the est", 45, dom.getEarliestStartTime());
			assertEquals("55 is the lst", 55, dom.getLatestStartTime());
			
			assertEquals("1 resources for op1", 1, dom.getAvailResourceCount((1)));
			assertEquals("1 resources for op2", 1, dom.getAvailResourceCount((2)));
			assertEquals("3 resources for op3", 3, dom.getAvailResourceCount((3)));
			
			resources = dom.getRemainingResources((1));
			assertTrue("op1 uses resource 2", Arrays.binarySearch(resources, (1)) <= -1);
			assertTrue("op1 uses resource 2", Arrays.binarySearch(resources, (2)) > -1);
			resources = dom.getRemainingResources((2));
			assertTrue("op2 uses resource 2", Arrays.binarySearch(resources, (2)) > -1);
			assertTrue("op2 uses resource 2", Arrays.binarySearch(resources, (3)) <= -1);
			resources = dom.getRemainingResources((3));
			assertTrue("op3 uses resource 4, 5, 6", Arrays.binarySearch(resources, (4)) > -1);
			assertTrue("op3 uses resource 4, 5, 6", Arrays.binarySearch(resources, (5)) > -1);
			assertTrue("op3 uses resource 4, 5, 6", Arrays.binarySearch(resources, (6)) > -1);
			
			assertEquals("2 ops are assigned to resources", 2, dom.getAssignedOperations().length);
			assertEquals("2 ops are assigned to resources", 1, dom.getUnassignedOperations().length);
			
//			TODO            assertEquals("1 units of resource 2 are required", 1, dom.getActualRequiredAmount((2)));
//			assertEquals("1 units of resource 2 are consumed", 1, dom.getActualConsumedAmount((2)));
			
			assertEquals("resource 1: ---", Integer.MAX_VALUE, dom.getEarliestStartTime(1,1));
			assertEquals("resource 1: ---", Integer.MIN_VALUE, dom.getLatestStartTime(1,1));
			assertEquals("resource 2: 45..55", 45, dom.getEarliestStartTime(1,2));
			assertEquals("resource 2: 45..55", 55, dom.getLatestStartTime(1,2));
			assertEquals("resource 2: 45..55", 45, dom.getEarliestStartTime(2,2));
			assertEquals("resource 2: 45..55", 55, dom.getLatestStartTime(2,2));
			assertEquals("resource 3: ---", Integer.MAX_VALUE, dom.getEarliestStartTime(2,3));
			assertEquals("resource 3: ---", Integer.MIN_VALUE, dom.getLatestStartTime(2,3));
			assertEquals("resource 4: 45..50", 45, dom.getEarliestStartTime(3,4));
			assertEquals("resource 4: 45..50", 50, dom.getLatestStartTime(3,4));
			assertEquals("resource 5: 50..55", 50, dom.getEarliestStartTime(3,5));
			assertEquals("resource 5: 50..55", 55, dom.getLatestStartTime(3,5));
			assertEquals("resource 6: 45..55", 45, dom.getEarliestStartTime(3,6));
			assertEquals("resource 6: 45..55", 55, dom.getLatestStartTime(3,6));
			
			assertFalse("the domain is not bound", dom.isBound());
			
			// pop the cps and hang onto the changes
			Object lst55 = cps.popDelta();
			
			assertEquals("45 is the est", 45, dom.getEarliestStartTime());
			assertEquals("100 is the lst", 100, dom.getLatestStartTime());
			
			assertEquals("1 resources for op1", 1, dom.getAvailResourceCount((1)));
			assertEquals("2 resources for op2", 2, dom.getAvailResourceCount((2)));
			assertEquals("3 resources for op3", 3, dom.getAvailResourceCount((3)));
			
			resources = dom.getRemainingResources((1));
			assertTrue("op1 uses resource 2", Arrays.binarySearch(resources, (1)) <= -1);
			assertTrue("op1 uses resource 2", Arrays.binarySearch(resources, (2)) > -1);
			resources = dom.getRemainingResources((2));
			assertTrue("op2 uses resource 2, 3", Arrays.binarySearch(resources, (2)) > -1);
			assertTrue("op2 uses resource 2, 3", Arrays.binarySearch(resources, (3)) > -1);
			resources = dom.getRemainingResources((3));
			assertTrue("op3 uses resource 4, 5, 6", Arrays.binarySearch(resources, (4)) > -1);
			assertTrue("op3 uses resource 4, 5, 6", Arrays.binarySearch(resources, (5)) > -1);
			assertTrue("op3 uses resource 4, 5, 6", Arrays.binarySearch(resources, (6)) > -1);
			
			assertEquals("1 ops are assigned to resources", 1, dom.getAssignedOperations().length);
			assertEquals("2 op is unassigned to resources", 2, dom.getUnassignedOperations().length);
			
//			TODO            assertEquals("1 units of resource 2 are required", 1, dom.getActualRequiredAmount((2)));
			
			assertEquals("resource 1: ---", Integer.MAX_VALUE, dom.getEarliestStartTime(1,1));
			assertEquals("resource 1: ---", Integer.MIN_VALUE, dom.getLatestStartTime(1,1));
			assertEquals("resource 2: 45..100", 45, dom.getEarliestStartTime(1,2));
			assertEquals("resource 2: 45..100", 100, dom.getLatestStartTime(1,2));
			assertEquals("resource 2: 45..100", 45, dom.getEarliestStartTime(2,2));
			assertEquals("resource 2: 45..100", 100, dom.getLatestStartTime(2,2));
			assertEquals("resource 3: 60..90", 60, dom.getEarliestStartTime(2,3));
			assertEquals("resource 3: 60..90", 90, dom.getLatestStartTime(2,3));
			assertEquals("resource 4: 45..50", 45, dom.getEarliestStartTime(3,4));
			assertEquals("resource 4: 45..50", 50, dom.getLatestStartTime(3,4));
			assertEquals("resource 5: 50..100", 50, dom.getEarliestStartTime(3,5));
			assertEquals("resource 5: 50..100", 100, dom.getLatestStartTime(3,5));
			assertEquals("resource 6: 45..70", 45, dom.getEarliestStartTime(3,6));
			assertEquals("resource 6: 45..70", 70, dom.getLatestStartTime(3,6));
			
			assertFalse("the domain is not bound", dom.isBound());
			
			// push the changes back on
			cps.pushDelta(lst55);
			
			assertEquals("45 is the est", 45, dom.getEarliestStartTime());
			assertEquals("55 is the lst", 55, dom.getLatestStartTime());
			
			assertEquals("1 resources for op1", 1, dom.getAvailResourceCount((1)));
			assertEquals("1 resources for op2", 1, dom.getAvailResourceCount((2)));
			assertEquals("3 resources for op3", 3, dom.getAvailResourceCount((3)));
			
			resources = dom.getRemainingResources((1));
			assertTrue("op1 uses resource 2", Arrays.binarySearch(resources, (1)) <= -1);
			assertTrue("op1 uses resource 2", Arrays.binarySearch(resources, (2)) > -1);
			resources = dom.getRemainingResources((2));
			assertTrue("op2 uses resource 2", Arrays.binarySearch(resources, (2)) > -1);
			assertTrue("op2 uses resource 2", Arrays.binarySearch(resources, (3)) <= -1);
			resources = dom.getRemainingResources((3));
			assertTrue("op3 uses resource 4, 5, 6", Arrays.binarySearch(resources, (4)) > -1);
			assertTrue("op3 uses resource 4, 5, 6", Arrays.binarySearch(resources, (5)) > -1);
			assertTrue("op3 uses resource 4, 5, 6", Arrays.binarySearch(resources, (6)) > -1);
			
			assertEquals("2 ops are assigned to resources", 2, dom.getAssignedOperations().length);
			assertEquals("2 ops are assigned to resources", 1, dom.getUnassignedOperations().length);
			
//			TODO            assertEquals("1 units of resource 2 are required", 1, dom.getActualRequiredAmount((2)));
//			assertEquals("1 units of resource 2 are consumed", 1, dom.getActualConsumedAmount((2)));
			
			assertEquals("resource 1: ---", Integer.MAX_VALUE, dom.getEarliestStartTime(1,1));
			assertEquals("resource 1: ---", Integer.MIN_VALUE, dom.getLatestStartTime(1,1));
			assertEquals("resource 2: 45..55", 45, dom.getEarliestStartTime(1,2));
			assertEquals("resource 2: 45..55", 55, dom.getLatestStartTime(1,2));
			assertEquals("resource 2: 45..55", 45, dom.getEarliestStartTime(2,2));
			assertEquals("resource 2: 45..55", 55, dom.getLatestStartTime(2,2));
			assertEquals("resource 3: ---", Integer.MAX_VALUE, dom.getEarliestStartTime(2,3));
			assertEquals("resource 3: ---", Integer.MIN_VALUE, dom.getLatestStartTime(2,3));
			assertEquals("resource 4: 45..50", 45, dom.getEarliestStartTime(3,4));
			assertEquals("resource 4: 45..50", 50, dom.getLatestStartTime(3,4));
			assertEquals("resource 5: 50..55", 50, dom.getEarliestStartTime(3,5));
			assertEquals("resource 5: 50..55", 55, dom.getLatestStartTime(3,5));
			assertEquals("resource 6: 45..55", 45, dom.getEarliestStartTime(3,6));
			assertEquals("resource 6: 45..55", 55, dom.getLatestStartTime(3,6));
			
			assertFalse("the domain is not bound", dom.isBound());
			
			// completely reset the cps
			cps.reset();
			
			assertEquals("0 is the est", 0, dom.getEarliestStartTime());
			assertEquals("100 is the lst", 100, dom.getLatestStartTime());
			
			assertEquals("2 resources for op1", 2, dom.getAvailResourceCount((1)));
			assertEquals("2 resources for op2", 2, dom.getAvailResourceCount((2)));
			assertEquals("3 resources for op3", 3, dom.getAvailResourceCount((3)));
			
			resources = dom.getRemainingResources((1));
			assertFalse("op1 uses resource 2", Arrays.binarySearch(resources, (1)) > -1);
			assertTrue("op1 uses resource 2", Arrays.binarySearch(resources, (2)) > -1);
			resources = dom.getRemainingResources((2));
			assertTrue("op2 uses resource 2", Arrays.binarySearch(resources, (2)) > -1);
			assertTrue("op2 uses resource 2", Arrays.binarySearch(resources, (3)) > -1);
			resources = dom.getRemainingResources((3));
			assertTrue("op3 uses resource 4, 5, 6", Arrays.binarySearch(resources, (4)) > -1);
			assertTrue("op3 uses resource 4, 5, 6", Arrays.binarySearch(resources, (5)) > -1);
			assertTrue("op3 uses resource 4, 5, 6", Arrays.binarySearch(resources, (6)) > -1);
			
			assertEquals("0 ops are assigned to resources", 0, dom.getAssignedOperations().length);
			assertEquals("3 op is assigned to resources", 3, dom.getUnassignedOperations().length);
			
//			TODO            assertEquals("0 units of resource 1 are required", 0, dom.getActualRequiredAmount((1)));
//			assertEquals("0 units of resource 2 are required", 0, dom.getActualRequiredAmount((2)));
//			assertEquals("0 units of resource 2 are consumed", 0, dom.getActualConsumedAmount((2)));
//			assertEquals("0 units of resource 3 are consumed", 0, dom.getActualConsumedAmount((3)));
//			assertEquals("0 units of resource 4 are produced", 0, dom.getActualProducedAmount((4)));
//			assertEquals("0 units of resource 5 are produced", 0, dom.getActualProducedAmount((5)));
//			assertEquals("0 units of resource 6 are produced", 0, dom.getActualProducedAmount((6)));
			
			//TODO: is this first one correct?
			assertEquals("resource 1: 10..40", 10, dom.getEarliestStartTime(1,1));
			assertEquals("resource 1: 10..40", 40, dom.getLatestStartTime(1,1));
			assertEquals("resource 2: 0..100", 0, dom.getEarliestStartTime(1,2));
			assertEquals("resource 2: 0..100", 100, dom.getLatestStartTime(1,2));
			assertEquals("resource 2: 0..100", 0, dom.getEarliestStartTime(2,2));
			assertEquals("resource 2: 0..100", 100, dom.getLatestStartTime(2,2));
			assertEquals("resource 3: 60..90", 60, dom.getEarliestStartTime(2,3));
			assertEquals("resource 3: 60..90", 90, dom.getLatestStartTime(2,3));
			assertEquals("resource 4: 0..50", 0, dom.getEarliestStartTime(3,4));
			assertEquals("resource 4: k,0..50", 50, dom.getLatestStartTime(3,4));
			assertEquals("resource 5: 50..100", 50, dom.getEarliestStartTime(3,5));
			assertEquals("resource 5: 50..100", 100, dom.getLatestStartTime(3,5));
			assertEquals("resource 6: 30..70", 30, dom.getEarliestStartTime(3,6));
			assertEquals("resource 6: 30..70", 70, dom.getLatestStartTime(3,6));
			
			assertFalse("the domain is not bound", dom.isBound());
		}
		catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	public void testMultipleChanges() {
		try {
			dom.setEarliestStartTime(10);
			
			dom.setRequiredResource(2, 2);
			
			dom.removePossibleResource(3, 5);
			
			dom.removeStartTimes(4, 30, 50);
			
			assertEquals("10 is the est", 10, dom.getEarliestStartTime());
			assertEquals("70 is the lst", 70, dom.getLatestStartTime());
			
			assertEquals("2 resources for op1", 2, dom.getAvailResourceCount((1)));
			assertEquals("1 resources for op2", 1, dom.getAvailResourceCount((2)));
			assertEquals("2 resources for op3", 2, dom.getAvailResourceCount((3)));
			
			int[] resources = dom.getRemainingResources((1));
			assertTrue("op1 uses resource 1, 2", Arrays.binarySearch(resources, (1)) > -1);
			assertTrue("op1 uses resource 1, 2", Arrays.binarySearch(resources, (2)) > -1);
			resources = dom.getRemainingResources((2));
			assertTrue("op2 uses resource 2", Arrays.binarySearch(resources, (2)) > -1);
			assertTrue("op2 uses resource 2", Arrays.binarySearch(resources, (3)) <= -1);
			resources = dom.getRemainingResources((3));
			assertTrue("op3 uses resource 4, 6", Arrays.binarySearch(resources, (4)) > -1);
			assertTrue("op3 uses resource 4, 6", Arrays.binarySearch(resources, (5)) <= -1);
			assertTrue("op3 uses resource 4, 6", Arrays.binarySearch(resources, (6)) > -1);
			
			assertEquals("1 ops are assigned to resources", 1, dom.getAssignedOperations().length);
			assertEquals("1 ops are assigned to resources", 2, dom.getUnassignedOperations().length);
			
//			TODO            assertEquals("1 units of resource 2 are consumed", 1, dom.getActualConsumedAmount(2));
			
			assertEquals("resource 1: 10..40", 10, dom.getEarliestStartTime(1,1));
			assertEquals("resource 1: 10..40", 40, dom.getLatestStartTime(1,1));
			assertEquals("resource 2: 10..70", 10, dom.getEarliestStartTime(1,2));
			assertEquals("resource 2: 10..70", 70, dom.getLatestStartTime(1,2));
			assertEquals("resource 2: 10..70", 10, dom.getEarliestStartTime(2,2));
			assertEquals("resource 2: 10..70", 70, dom.getLatestStartTime(2,2));
			assertEquals("resource 3: ---", Integer.MAX_VALUE, dom.getEarliestStartTime(2,3));
			assertEquals("resource 3: ---", Integer.MIN_VALUE, dom.getLatestStartTime(2,3));
			assertEquals("resource 4: 10..29", 10, dom.getEarliestStartTime(3,4));
			assertEquals("resource 4: 10..29", 29, dom.getLatestStartTime(3,4));
			assertEquals("resource 5: ---", Integer.MAX_VALUE, dom.getEarliestStartTime(3,5));
			assertEquals("resource 5: ---", Integer.MIN_VALUE, dom.getLatestStartTime(3,5));
			assertEquals("resource 6: 30..70", 30, dom.getEarliestStartTime(3,6));
			assertEquals("resource 6: 30..70", 70, dom.getLatestStartTime(3,6));
			
			assertFalse("the domain is not bound", dom.isBound());
		}
		catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}    
	
	public void testSetEstLstFailure() {
        try {
            dom.setEarliestStartTime(55);
            dom.setLatestStartTime(45);
            // should cause an exception
            fail();
        }
        catch (RuntimeException rte) {
            fail();
        }
        catch (PropagationFailureException pfe) {
            // expected
        }
        catch (Exception e) {
            fail();
        }
    }
    //Remove too many resources
    public void removeAllResourcesFromOperation() {
        try {
            dom.removePossibleResource(2,2);
            dom.removePossibleResource(2,1);
            dom.removePossibleResource(2,0);
            // should cause an exception
            fail();
        }
        catch (RuntimeException rte) {
            fail();
        }
        catch (PropagationFailureException pfe) {
            // expected
        }
        catch (Exception e) {
            fail();
        }
    }
    
    public void testReseourceAssignmentFailure() {
        try {
            dom.setRequiredResource((1), (1));
            assertEquals(10, dom.getEarliestStartTime());
            assertEquals(40, dom.getLatestStartTime());
            dom.setRequiredResource((2), (3));
            // should cause an exception
            fail();
        }
        catch (RuntimeException rte) {
            fail();
        }
        catch (PropagationFailureException pfe) {
            // expected
        }
        catch (Exception e) {
            fail();
        }
    }
    
    public void testResourceTimingFailure() {
        try {
            dom.setEarliestStartTime((1), 50);
            dom.setEarliestStartTime((2), 40);
            dom.setLatestStartTime((2), 39);
            // should cause an exception
            fail();
        }
        catch (RuntimeException rte) {
            fail();
        }
        catch (PropagationFailureException pfe) {
            // expected
        }
        catch (Exception e) {
            fail();
        }
    }
    
    public void testResourcesCombinationFailure() {
        try {
            dom.setLatestStartTime(2, 40);
            dom.setRequiredResource(3, 5);
            // should cause an exception
            fail();
        }
        catch (RuntimeException rte) {
            fail();
        }
        catch (PropagationFailureException pfe) {
            // expected
        }
        catch (Exception e) {
            fail();
        }
    }
    
// TODO: update this test   /*****************
//     * BASIC TESTING *
//     *****************/
//    public void testGetRequiredAmount() {
//        try {
//	    	//The only resources that could possibly be 
//	    	//required are resources 0 and 1
//	    	assertEquals("6 units of resource 0 may be required",dom2.getPossibleRequiredAmount((1)),6);
//	    	assertEquals("3 units of resource 1 may be required",dom2.getPossibleRequiredAmount((2)),3);
//	    	assertEquals("3 units of resource 2 may be required",dom2.getPossibleRequiredAmount((3)),3);
//	    	assertEquals("0 units of resource 3 may be required",dom2.getPossibleRequiredAmount((4)),0);
//	    	assertEquals("0 units of resource 4 may be required",dom2.getPossibleRequiredAmount((5)),0);
//	    	assertEquals("0 units of resource 5 may be required",dom2.getPossibleRequiredAmount((6)),0);
//	    	//Nothing has been bound at all, thus all should have no actual requirements
//	    	assertEquals("0 units of resource 0 are required",dom2.getActualRequiredAmount((1)),0);
//	    	assertEquals("0 units of resource 1 are required",dom2.getActualRequiredAmount((2)),0);
//	    	assertEquals("0 units of resource 2 are required",dom2.getActualRequiredAmount((3)),0);
//	    	assertEquals("0 units of resource 3 are required",dom2.getActualRequiredAmount((4)),0);
//	    	assertEquals("0 units of resource 4 are required",dom2.getActualRequiredAmount((5)),0);
//	    	assertEquals("0 units of resource 5 are required",dom2.getActualRequiredAmount((6)),0);
//	    	
//	    	dom2.removePossibleResource((1),(2));
//	    	//Now that resource 1 has been removed from actvity 0, it has a possible required amount of 0
//	    	//while the actual amound required of resource of 0 is now 3 since operation one
//	    	//must require resource 0 
//	    	
//	    	assertEquals("6 units of resource 0 may be required",dom2.getPossibleRequiredAmount((1)),6);
//	    	assertEquals("0 units of resource 1 may be required",dom2.getPossibleRequiredAmount((2)),0);
//	    	assertEquals("3 units of resource 2 may be required",dom2.getPossibleRequiredAmount((3)),3);
//	    	assertEquals("0 units of resource 3 may be required",dom2.getPossibleRequiredAmount((4)),0);
//	    	assertEquals("0 units of resource 4 may be required",dom2.getPossibleRequiredAmount((5)),0);
//	    	assertEquals("0 units of resource 5 may be required",dom2.getPossibleRequiredAmount((6)),0);
//	
//	    	assertEquals("3 units of resource 0 are required",dom2.getActualRequiredAmount((1)),3);
//	    	assertEquals("0 units of resource 1 are required",dom2.getActualRequiredAmount((2)),0);
//	    	assertEquals("0 units of resource 2 are required",dom2.getActualRequiredAmount((3)),0);
//	    	assertEquals("0 units of resource 3 are required",dom2.getActualRequiredAmount((4)),0);
//	    	assertEquals("0 units of resource 4 are required",dom2.getActualRequiredAmount((5)),0);
//	    	assertEquals("0 units of resource 5 are required",dom2.getActualRequiredAmount((6)),0);
//        }
//        catch(PropagationFailureException pfe) {
//            fail(pfe.getLocalizedMessage());
//        }
//    }
//    public void testGetConsumedAmount() {
//        try {
//	    	//The only resources that could possibly be 
//	    	//consumed are resources 0 and 1
//	    	assertEquals("6 units of resource 0 may be consumed",dom2.getPossibleConsumedAmount(0),6);
//	    	assertEquals("3 units of resource 1 may be consumed",dom2.getPossibleConsumedAmount(1),3);
//	    	assertEquals("3 units of resource 2 may be consumed",dom2.getPossibleConsumedAmount(2),3);
//	    	assertEquals("0 units of resource 3 may be consumed",dom2.getPossibleConsumedAmount(3),0);
//	    	assertEquals("0 units of resource 4 may be consumed",dom2.getPossibleConsumedAmount(4),0);
//	    	assertEquals("0 units of resource 5 may be consumed",dom2.getPossibleConsumedAmount(5),0);
//	    	//Nothing has been bound at all, thus all should have no actual requirements
//	    	assertEquals("0 units of resource 0 are consumed",dom2.getActualConsumedAmount(0),0);
//	    	assertEquals("0 units of resource 1 are consumed",dom2.getActualConsumedAmount(1),0);
//	    	assertEquals("0 units of resource 2 are consumed",dom2.getActualConsumedAmount(2),0);
//	    	assertEquals("0 units of resource 3 are consumed",dom2.getActualConsumedAmount(3),0);
//	    	assertEquals("0 units of resource 4 are consumed",dom2.getActualConsumedAmount(4),0);
//	    	assertEquals("0 units of resource 5 are consumed",dom2.getActualConsumedAmount(5),0);
//	    	
//	    	dom2.removePossibleResource(3,1);
//	    	//Now that resource 1 has been removed from actvity 0, it has a possible consumed amount of 0
//	    	//while the actual amound consumed of resource of 0 is now 3 since operation one
//	    	//must require resource 0 
//	    	
//	    	assertEquals("6 units of resource 0 may be consumed",dom2.getPossibleConsumedAmount(0),6);
//	    	assertEquals("0 units of resource 1 may be consumed",dom2.getPossibleConsumedAmount(1),0);
//	    	assertEquals("3 units of resource 2 may be consumed",dom2.getPossibleConsumedAmount(2),3);
//	    	assertEquals("0 units of resource 3 may be consumed",dom2.getPossibleConsumedAmount(3),0);
//	    	assertEquals("0 units of resource 4 may be consumed",dom2.getPossibleConsumedAmount(4),0);
//	    	assertEquals("0 units of resource 5 may be consumed",dom2.getPossibleConsumedAmount(5),0);
//	
//	    	assertEquals("3 units of resource 0 are consumed",dom2.getActualConsumedAmount(0),3);
//	    	assertEquals("0 units of resource 1 are consumed",dom2.getActualConsumedAmount(1),0);
//	    	assertEquals("0 units of resource 2 are consumed",dom2.getActualConsumedAmount(2),0);
//	    	assertEquals("0 units of resource 3 are consumed",dom2.getActualConsumedAmount(3),0);
//	    	assertEquals("0 units of resource 4 are consumed",dom2.getActualConsumedAmount(4),0);
//	    	assertEquals("0 units of resource 5 are consumed",dom2.getActualConsumedAmount(5),0);
//        }
//        catch(PropagationFailureException pfe) {
//            fail(pfe.getLocalizedMessage());
//        }
//    }
//    
//    public void testGetProducedAmount() {
//        try {
//	    	//The only resources that could possibly be 
//	    	//produced are resources 3, 4, and 5
//	    	assertEquals("0 units of resource 0 may be produced",dom2.getPossibleProducedAmount(0),0);
//	    	assertEquals("0 units of resource 1 may be produced",dom2.getPossibleProducedAmount(1),0);
//	    	assertEquals("0 units of resource 2 may be produced",dom2.getPossibleProducedAmount(2),0);
//	    	assertEquals("3 units of resource 3 may be produced",dom2.getPossibleProducedAmount(3),3);
//	    	assertEquals("3 units of resource 4 may be produced",dom2.getPossibleProducedAmount(4),3);
//	    	assertEquals("3 units of resource 5 may be produced",dom2.getPossibleProducedAmount(5),3);
//	    	//Nothing has been bound at all, thus all should have no actual requirements
//	    	assertEquals("0 units of resource 0 are produced",dom2.getActualProducedAmount(0),0);
//	    	assertEquals("0 units of resource 1 are produced",dom2.getActualProducedAmount(1),0);
//	    	assertEquals("0 units of resource 2 are produced",dom2.getActualProducedAmount(2),0);
//	    	assertEquals("0 units of resource 3 are produced",dom2.getActualProducedAmount(3),0);
//	    	assertEquals("0 units of resource 4 are produced",dom2.getActualProducedAmount(4),0);
//	    	assertEquals("0 units of resource 5 are produced",dom2.getActualProducedAmount(5),0);
//	    	
//	    	dom2.removePossibleResource(dom2.getOperationIndex(3),(4));
//	    	dom2.removePossibleResource(dom2.getOperationIndex(3),(5));
//	    	//Now that resources 4 and 5  has been removed from actvity 2, they have a possible produced amount of 0
//	    	//while the actual amound produced of resource of 3 is now 3 since operation one
//	    	//must require resource 0 
//	    	
//	    	assertEquals("0 units of resource 0 may be produced",dom2.getPossibleProducedAmount(0),0);
//	    	assertEquals("0 units of resource 1 may be produced",dom2.getPossibleProducedAmount(1),0);
//	    	assertEquals("0 units of resource 2 may be produced",dom2.getPossibleProducedAmount(2),0);
//	    	assertEquals("3 units of resource 3 may be produced",dom2.getPossibleProducedAmount(3),3);
//	    	assertEquals("0 units of resource 4 may be produced",dom2.getPossibleProducedAmount(4),0);
//	    	assertEquals("0 units of resource 5 may be produced",dom2.getPossibleProducedAmount(5),0);
//	
//	    	assertEquals("0 units of resource 0 are produced",dom2.getActualProducedAmount(0),0);
//	    	assertEquals("0 units of resource 1 are produced",dom2.getActualProducedAmount(1),0);
//	    	assertEquals("0 units of resource 2 are produced",dom2.getActualProducedAmount(2),0);
//	    	assertEquals("3 units of resource 3 are produced",dom2.getActualProducedAmount(3),3);
//	    	assertEquals("0 units of resource 4 are produced",dom2.getActualProducedAmount(4),0);
//	    	assertEquals("0 units of resource 5 are produced",dom2.getActualProducedAmount(5),0);
//        }
//        catch(PropagationFailureException pfe) {
//            fail(pfe.getLocalizedMessage());
//        }
//    }
//    
//	public void testDomainInitialization() {
//		try {
//            assertEquals("0 is the est", 0, dom.getEarliestStartTime());
//            assertEquals("100 is the lst", 100, dom.getLatestStartTime());
//            
//            assertEquals("3 operations", 3, dom.getOperationCount());
//            
//            assertEquals("2 resources for op1", 2, dom.getAvailResourceCount((1)));
//            assertEquals("2 resources for op2", 2, dom.getAvailResourceCount((2)));
//            assertEquals("3 resources for op3", 3, dom.getAvailResourceCount((3)));
//            
//            int[] resources = dom.getRemainingResources((1));
//            assertTrue("op1 uses resource 1, 2", Arrays.binarySearch(resources, (1)) > -1);
//            assertTrue("op1 uses resource 1, 2", Arrays.binarySearch(resources, (2)) > -1);
//            resources = dom.getRemainingResources((2));
//            assertTrue("op2 uses resource 2, 3", Arrays.binarySearch(resources, (2)) > -1);
//            assertTrue("op2 uses resource 2, 3", Arrays.binarySearch(resources, (3)) > -1);
//            resources = dom.getRemainingResources((3));
//            assertTrue("op3 uses resource 4, 5, 6", Arrays.binarySearch(resources, (4)) > -1);
//            assertTrue("op3 uses resource 4, 5, 6", Arrays.binarySearch(resources, (5)) > -1);
//            assertTrue("op3 uses resource 4, 5, 6", Arrays.binarySearch(resources, (6)) > -1);
//            
//            assertEquals("no ops are assigned to resources", 0, dom.getAssignedOperations().length);
//            assertEquals("no ops are assigned to resources", 3, dom.getUnassignedOperations().length);
//            
//            assertEquals("up to 1 units of resource 1 may be required", 1, dom.getPossibleRequiredAmount((1)));
//            assertEquals("up to 1 units of resource 2 may be required", 1, dom.getPossibleRequiredAmount((2)));
//            assertEquals("up to 1 units of resource 2 may be consumed", 1, dom.getPossibleConsumedAmount((2)));
//            assertEquals("up to 1 units of resource 3 may be consumed", 1, dom.getPossibleConsumedAmount((3)));
//            assertEquals("up to 1 units of resource 4 may be produced", 1, dom.getPossibleProducedAmount((4)));
//            assertEquals("up to 1 units of resource 5 may be produced", 1, dom.getPossibleProducedAmount((5)));
//            assertEquals("up to 1 units of resource 6 may be produced", 1, dom.getPossibleProducedAmount((6)));
//            
//            assertEquals("0 units of resource 1 are required", 0, dom.getActualRequiredAmount((1)));
//            assertEquals("0 units of resource 2 are required", 0, dom.getActualRequiredAmount((2)));
//            assertEquals("0 units of resource 2 are consumed", 0, dom.getActualConsumedAmount((2)));
//            assertEquals("0 units of resource 3 are consumed", 0, dom.getActualConsumedAmount((3)));
//            assertEquals("0 units of resource 4 are produced", 0, dom.getActualProducedAmount((4)));
//            assertEquals("0 units of resource 5 are produced", 0, dom.getActualProducedAmount((5)));
//            assertEquals("0 units of resource 6 are produced", 0, dom.getActualProducedAmount((6)));
//            
//            assertEquals("resource 1: 10..40", 10, dom.getEarliestStartTime((1)));
//            assertEquals("resource 1: 10..40", 40, dom.getLatestStartTime((1)));
//            assertEquals("resource 2: 0..100", 0, dom.getEarliestStartTime((2)));
//            assertEquals("resource 2: 0..100", 100, dom.getLatestStartTime((2)));
//            assertEquals("resource 3: 60..90", 60, dom.getEarliestStartTime((3)));
//            assertEquals("resource 3: 60..90", 90, dom.getLatestStartTime((3)));
//            assertEquals("resource 4: 0..50", 0, dom.getEarliestStartTime((4)));
//            assertEquals("resource 4: 0..50", 50, dom.getLatestStartTime((4)));
//            assertEquals("resource 5: 50..100", 50, dom.getEarliestStartTime((5)));
//            assertEquals("resource 5: 50..100", 100, dom.getLatestStartTime((5)));
//            assertEquals("resource 6: 30..70", 30, dom.getEarliestStartTime((6)));
//            assertEquals("resource 6: 30..70", 70, dom.getLatestStartTime((6)));
//            
//            assertFalse("the domain is not bound", dom.isBound());
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            fail();
//        }
//	}
    
    public void testRemoveResourceOnDomain2() {
        try {
            //We are removing resource 5 from operation 3.  This leaves a gap
            // in the middle of many domains
            dom2.removePossibleResource(3,5);
            
            assertEquals("no ops are assigned to resources", 0, dom.getAssignedOperations().length);
            assertEquals("no ops are assigned to resources", 3, dom.getUnassignedOperations().length);
            assertEquals("resource 0: 0-33, 66-100", 0, dom2.getEarliestStartTime(1,1));
            assertEquals("resource 0: 0-33, 66-100", 100, dom2.getLatestStartTime(1,1));
            //We need to make sure that the range from 33 to 66 is removed from resource 0
            
            assertEquals("resource 1: 66-100", 66, dom2.getEarliestStartTime(1,2));
            assertEquals("resource 1: 66-100", 100, dom2.getLatestStartTime(1,2));
            assertEquals("resource 2: 0-33", 0, dom2.getEarliestStartTime(2,3));
            assertEquals("resource 2: 0-33", 33, dom2.getLatestStartTime(2,3));
            assertEquals("resource 3: 0-33, 75-100", 0, dom2.getEarliestStartTime(3,4));
            assertEquals("resource 3: 0-33, 75-100", 100, dom2.getLatestStartTime(3,4));
            assertEquals("resource 4: ---", Integer.MAX_VALUE, dom2.getEarliestStartTime(3,5));
            assertEquals("resource 4: --", Integer.MIN_VALUE, dom2.getLatestStartTime(3,5));
            assertEquals("resource 5: 66-100", 66, dom2.getEarliestStartTime(3,6));
            assertEquals("resource 5: 66-100", 100, dom2.getLatestStartTime(3,6));
            
            dom2.removePossibleResource(2,3);
            
            assertEquals("no ops are assigned to resources", 0, dom.getAssignedOperations().length);
            assertEquals("no ops are assigned to resources", 3, dom.getUnassignedOperations().length);
            
            assertEquals("resource 1: 0-33, 75-100", 0, dom2.getEarliestStartTime(1,1));
            assertEquals("resource 1: 0-33, 75-100", 100, dom2.getLatestStartTime(1,1));
            //We need to make sure that the range from 33 to 75 is removed from resource 0
            
            assertEquals("resource 2: 66-100", 66, dom2.getEarliestStartTime(1,2));
            assertEquals("resource 2: 66-100", 100, dom2.getLatestStartTime(1,2));
            assertEquals("resource 3: ---", Integer.MAX_VALUE, dom2.getEarliestStartTime(2,3));
            assertEquals("resource 3: ---", Integer.MIN_VALUE, dom2.getLatestStartTime(2,3));
            assertEquals("resource 4: 0-33, 75-100", 0, dom2.getEarliestStartTime(3,4));
            assertEquals("resource 4: 0-33, 75-100", 100, dom2.getLatestStartTime(3,4));
            assertEquals("resource 5: ---", Integer.MAX_VALUE, dom2.getEarliestStartTime(3,5));
            assertEquals("resource 5: ---", Integer.MIN_VALUE, dom2.getLatestStartTime(3,5));
            assertEquals("resource 6: 66-100", 66, dom2.getEarliestStartTime(3,6));
            assertEquals("resource 6: 66-100", 100, dom2.getLatestStartTime(3,6));
            
            
        }
        catch (PropagationFailureException pfe) {
            fail(pfe.getLocalizedMessage());
        }
        
    }
    
    public void testForcedRemovalofResourceOnDomain2() {
        try {
            //By setting est to 50, we remove the first part of the domaind of resource 3, and remove 2 altogether
            dom2.setEarliestStartTime(50);
            
            
            assertEquals("resource 1: 50-100", 50, dom2.getEarliestStartTime(1,1));
            assertEquals("resource 1: 50-100", 100, dom2.getLatestStartTime(1,1));
            //We need to make sure that the range from 33 to 66 is removed from resource 0
            
            assertEquals("resource 2: 50-100", 50, dom2.getEarliestStartTime(1,2));
            assertEquals("resource 2: 50-100", 100, dom2.getLatestStartTime(1,2));
            assertEquals("resource 3: 50-50", 50, dom2.getEarliestStartTime(2,3));
            assertEquals("resource 3: 50-50", 50, dom2.getLatestStartTime(2,3));
            assertEquals("resource 4: 75-100", 75, dom2.getEarliestStartTime(3,4));
            assertEquals("resource 4: 75-100", 100, dom2.getLatestStartTime(3,4));
            assertEquals("resource 5: 50-66", 50, dom2.getEarliestStartTime(3,5));
            assertEquals("resource 5: 50-66", 66, dom2.getLatestStartTime(3,5));
            assertEquals("resource 6: 66-100", 66, dom2.getEarliestStartTime(3,6));
            assertEquals("resource 6: 66-100", 100, dom2.getLatestStartTime(3,6));
            
            
        }
        catch (PropagationFailureException pfe) {
            fail(pfe.getLocalizedMessage());
        }
        
    }
    
    public void testAddingResourceAfterTheFact() {
    	try{
	    	ChoicePointStack cps = new ChoicePointStack();
	    	dom.setChoicePointStack(cps);
	    	cps.push();
	    	assertEquals(0, dom.getEarliestEndTime());
	    	assertEquals(100, dom.getLatestEndTime());
	    	assertEquals(2,dom.getAvailResourceCount(1));
	    	
	    	ActResourceDomain ra = new ActResourceDomain(30,80,1,1);
	    	ra.setID(17);
	    	dom.addResource(1,ra);
	    	
	    	assertEquals(0, dom.getEarliestEndTime());
	    	assertEquals(100, dom.getLatestEndTime());
	    	assertEquals(3,dom.getAvailResourceCount(1));
	    	
	    	dom.removePossibleResource(1,2);
	    	
	    	assertEquals(10, dom.getEarliestEndTime());
	    	assertEquals(80, dom.getLatestEndTime());
	    	assertEquals(2,dom.getAvailResourceCount(1));
	    	
	    	Object delta =cps.popDelta();
	    	
	    	assertEquals(0, dom.getEarliestEndTime());
	    	assertEquals(100, dom.getLatestEndTime());
	    	assertEquals(3,dom.getAvailResourceCount(1));
	    	
	    	cps.pushDelta(delta);
	    	
	    	assertEquals(10, dom.getEarliestEndTime());
	    	assertEquals(80, dom.getLatestEndTime());
	    	assertEquals(2,dom.getAvailResourceCount(1));
	    	
    	}
    	catch(PropagationFailureException pfe) {
    		fail(pfe.getLocalizedMessage());
    	}
    }
    
    public void testAddingOperationAfterTheFact() {
    	try{
	    	ChoicePointStack cps = new ChoicePointStack();
	    	dom.setChoicePointStack(cps);
	    	cps.push();
	    	
	    	assertEquals(2,dom.getAvailResourceCount(1));
	    	ActResourceDomain ra = new ActResourceDomain(30,80,1,1);
	    	ra.setID(17);
	    	ActOperationDomain oa = new ActOperationDomain(1,1,1);
	    	oa.setID(19);
	    	oa.addResource(ra);
	    	dom.addOperation(oa);
	    	
	    	assertEquals(30, dom.getEarliestEndTime());
	    	assertEquals(80, dom.getLatestEndTime());
	    	assertEquals(2,dom.getAvailResourceCount(1));
	    	assertEquals(2,dom.getAvailResourceCount(2));
	    	assertEquals(3,dom.getAvailResourceCount(3));
	    	assertEquals(1,dom.getAvailResourceCount(19));
	    	
	    	dom.removePossibleResource(3,4);
	    	
	    	assertEquals(30, dom.getEarliestEndTime());
	    	assertEquals(80, dom.getLatestEndTime());
	    	assertEquals(2,dom.getAvailResourceCount(1));
	    	assertEquals(2,dom.getAvailResourceCount(2));
	    	assertEquals(2,dom.getAvailResourceCount(3));
	    	assertEquals(1,dom.getAvailResourceCount(19));
	    	
	    	//This should bind things to 45 since there is only 1 resource for each operation that is available at ime 45
	    	dom.setStartTime(45);
	    	
	    	assertEquals(45, dom.getEarliestEndTime());
	    	assertEquals(45, dom.getLatestEndTime());
	    	assertEquals(1,dom.getAvailResourceCount(1));
	    	assertEquals(1,dom.getAvailResourceCount(2));
	    	assertEquals(1,dom.getAvailResourceCount(3));
	    	assertEquals(1,dom.getAvailResourceCount(19));
	    	
	    	
	    	Object delta =cps.popDelta();
	    	
	    	assertEquals(0, dom.getEarliestEndTime());
	    	assertEquals(100, dom.getLatestEndTime());
	    	assertEquals(2,dom.getAvailResourceCount(1));
	    	assertEquals(2,dom.getAvailResourceCount(2));
	    	assertEquals(3,dom.getAvailResourceCount(3));
	    	assertEquals(0,dom.getAvailResourceCount(19));
	    	
	    	cps.pushDelta(delta);
	    	
	    	assertEquals(45, dom.getEarliestEndTime());
	    	assertEquals(45, dom.getLatestEndTime());
	    	assertEquals(1,dom.getAvailResourceCount(1));
	    	assertEquals(1,dom.getAvailResourceCount(2));
	    	assertEquals(1,dom.getAvailResourceCount(3));
	    	assertEquals(1,dom.getAvailResourceCount(19));
	    	
    	}
    	catch(PropagationFailureException pfe) {
    		fail();
    	}
    }
    
    public void testCPSWithOpAndResourceAddition(){
    	try{
    		ChoicePointStack cps = new ChoicePointStack();
    		dom.setChoicePointStack(cps);
    		cps.push();
    		
    		assertEquals(2,dom.getAvailResourceCount(1));
    		ActResourceDomain ra = new ActResourceDomain(30,80,1,1);
    		ra.setID(17);
    		ActOperationDomain oa = new ActOperationDomain(1,1,1);
    		oa.setID(19);
    		oa.addResource(ra);
    		dom.addOperation(oa);
    		
    		assertEquals(30, dom.getEarliestEndTime());
	    	assertEquals(80, dom.getLatestEndTime());
	    	assertEquals(2,dom.getAvailResourceCount(1));
	    	assertEquals(2,dom.getAvailResourceCount(2));
	    	assertEquals(3,dom.getAvailResourceCount(3));
	    	assertEquals(1,dom.getAvailResourceCount(19));
	    	
	    	Object delta1 = cps.popDelta();
	    	
	    	assertEquals(0, dom.getEarliestEndTime());
	    	assertEquals(100, dom.getLatestEndTime());
	    	assertEquals(2,dom.getAvailResourceCount(1));
	    	assertEquals(2,dom.getAvailResourceCount(2));
	    	assertEquals(3,dom.getAvailResourceCount(3));
	    	assertEquals(0,dom.getAvailResourceCount(19));
	    	
	    	cps.pushDelta(delta1);
	    	
	    	assertEquals(30, dom.getEarliestEndTime());
	    	assertEquals(80, dom.getLatestEndTime());
	    	assertEquals(2,dom.getAvailResourceCount(1));
	    	assertEquals(2,dom.getAvailResourceCount(2));
	    	assertEquals(3,dom.getAvailResourceCount(3));
	    	assertEquals(1,dom.getAvailResourceCount(19));
	    	
	    	ra = new ActResourceDomain(40,60,1,1);
    		ra.setID(18);
    		
	    	dom.addResource(19,ra);
	    	
	    	assertEquals(30, dom.getEarliestEndTime());
	    	assertEquals(80, dom.getLatestEndTime());
	    	assertEquals(2,dom.getAvailResourceCount(1));
	    	assertEquals(2,dom.getAvailResourceCount(2));
	    	assertEquals(3,dom.getAvailResourceCount(3));
	    	assertEquals(2,dom.getAvailResourceCount(19));
	    	
	    	dom.removePossibleResource(19,17);
	    	
	    	assertEquals(40, dom.getEarliestEndTime());
	    	assertEquals(60, dom.getLatestEndTime());
	    	assertEquals(2,dom.getAvailResourceCount(1));
	    	assertEquals(2,dom.getAvailResourceCount(2));
	    	assertEquals(3,dom.getAvailResourceCount(3));
	    	assertEquals(1,dom.getAvailResourceCount(19));
	    	
	    	cps.popDelta();
	    	
	    	assertEquals(0, dom.getEarliestEndTime());
	    	assertEquals(100, dom.getLatestEndTime());
	    	assertEquals(2,dom.getAvailResourceCount(1));
	    	assertEquals(2,dom.getAvailResourceCount(2));
	    	assertEquals(3,dom.getAvailResourceCount(3));
	    	assertEquals(0,dom.getAvailResourceCount(19));

//	    	TODO: uncomment and figure out why concurrentModification occurs
//	    	cps.pushDelta(delta2);
//	    	
//	    	assertEquals(40, dom.getEarliestEndTime());
//	    	assertEquals(60, dom.getLatestEndTime());
//	    	assertEquals(2,dom.getAvailResourceCount(1));
//	    	assertEquals(2,dom.getAvailResourceCount(2));
//	    	assertEquals(3,dom.getAvailResourceCount(3));
//	    	assertEquals(1,dom.getAvailResourceCount(19));	    	
    	}
    	catch (PropagationFailureException pfe) {
    		fail();
    	}
    }
    
    public void testRemovingTimelines() {
    	try {
			assertEquals(5,aa.getDurationMin());
			assertEquals(10,aa.getDurationMax());
			assertEquals(0,aa.getEarliestStartTime());
			assertEquals(100,aa.getLatestStartTime());
			assertEquals(4,aa.getEarliestEndTime());
			assertEquals(109,aa.getLatestEndTime());
			assertEquals(0,oa1.getEarliestStartTime());
			assertEquals(100,oa1.getLatestStartTime());
			assertEquals(4,oa1.getEarliestEndTime());
			assertEquals(109,oa1.getLatestEndTime());
			
			IntIntervalSet iis = new IntIntervalSet();
			iis.add(0,110);
			aa.setTimeline(1,1,iis);
			aa.setTimeline(1,2,iis);
			aa.setTimeline(1,3,iis);
			
			assertEquals(5,aa.getDurationMin());
			assertEquals(10,aa.getDurationMax());
			assertEquals(0,aa.getEarliestStartTime());
			assertEquals(100,aa.getLatestStartTime());
			assertEquals(4,aa.getEarliestEndTime());
			assertEquals(109,aa.getLatestEndTime());
			assertEquals(0,oa1.getEarliestStartTime());
			assertEquals(100,oa1.getLatestStartTime());
			assertEquals(4,oa1.getEarliestEndTime());
			assertEquals(109,oa1.getLatestEndTime());
			
			iis.removeStartingAfter(100);
			aa.setTimeline(1,1,iis);
			aa.setTimeline(1,2,iis);
			aa.setTimeline(1,3,iis);
			
			assertEquals(5,aa.getDurationMin());
			assertEquals(10,aa.getDurationMax());
			assertEquals(0,aa.getEarliestStartTime());
			assertEquals(96,aa.getLatestStartTime());
			assertEquals(4,aa.getEarliestEndTime());
			assertEquals(100,aa.getLatestEndTime());
			assertEquals(0,oa1.getEarliestStartTime());
			assertEquals(96,oa1.getLatestStartTime());
			assertEquals(4,oa1.getEarliestEndTime());
			assertEquals(100,oa1.getLatestEndTime());
			
			iis.remove(4,6);
			aa.setTimeline(1,1,iis);
			aa.setTimeline(1,2,iis);
			aa.setTimeline(1,3,iis);
			
			assertEquals(5,aa.getDurationMin());
			assertEquals(10,aa.getDurationMax());
			assertEquals(7,aa.getEarliestStartTime());
			assertEquals(96,aa.getLatestStartTime());
			assertEquals(11,aa.getEarliestEndTime());
			assertEquals(100,aa.getLatestEndTime());
			assertEquals(7,oa1.getEarliestStartTime());
			assertEquals(96,oa1.getLatestStartTime());
			assertEquals(11,oa1.getEarliestEndTime());
			assertEquals(100,oa1.getLatestEndTime());
			
			iis.remove(13,94);
			aa.setTimeline(1,1,iis);
			aa.setTimeline(1,2,iis);
			aa.setTimeline(1,3,iis);
			
			assertEquals(5,aa.getDurationMin());
			assertEquals(10,aa.getDurationMax());
			assertEquals(7,aa.getEarliestStartTime());
			assertEquals(96,aa.getLatestStartTime());
			assertEquals(11,aa.getEarliestEndTime());
			assertEquals(100,aa.getLatestEndTime());
			assertEquals(7,oa1.getEarliestStartTime());
			assertEquals(96,oa1.getLatestStartTime());
			assertEquals(11,oa1.getEarliestEndTime());
			assertEquals(100,oa1.getLatestEndTime());
			
			assertEquals(4,aa.getStartTimes().size());
    	}
    	catch(PropagationFailureException pfe) {
    		fail();
    	}
    }
	
	
}
