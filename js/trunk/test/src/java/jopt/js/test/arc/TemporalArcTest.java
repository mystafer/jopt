package jopt.js.test.arc;

import jopt.csp.spi.arcalgorithm.domain.IntIntervalDomain;
import jopt.csp.spi.arcalgorithm.graph.node.IntNode;
import jopt.csp.variable.PropagationFailureException;
import jopt.js.spi.domain.activity.ActivityDomain;
import jopt.js.spi.domain.activity.ActOperationDomain;
import jopt.js.spi.domain.activity.ActResourceDomain;
import jopt.js.spi.graph.arc.TemporalArc;
import jopt.js.spi.graph.node.ActivityNode;
import junit.framework.TestCase;


/**
 * @author James Boerkoel
 */
public class TemporalArcTest extends TestCase {
    ActivityNode act1;
    ActivityNode act2;
    IntNode intNode;
    
    
	public TemporalArcTest(String testName) {
        super(testName);
    }
    
    public void setUp() {
    	/*
    	 * **ALL durations will be set at a constant 15**
    	 * 
    	 * Activity 1
    	 * 		OP1			0			50
    	 * 			RS1		|------------|
    	 * 							30				 100
    	 * 			RS2				 |----------------|
    	 * 		OP2			0						 100
    	 * 			RS3		|-------------------------|
    	 * 		OP3			0						 100
    	 * 			RS4		|-------------------------|
    	 * 
    	 * Activity 2
    	 * 		OP4			0		 40
    	 * 			RS5		|---------|
    	 * 						 20			     80
    	 * 			RS6			  |---------------|
    	 * 								  60		 100
    	 * 			RS7					   |----------|
    	 * 		OP5			0						 100
    	 * 			RS8		|-------------------------|
    	 */
    	ActivityDomain dom1 = null;
    	ActivityDomain dom2 = null;
    	
    	try {
	    	dom1 = new ActivityDomain(0,100,16,16);
	    	dom1.setID(1);
	    	dom2 = new ActivityDomain(0,100,16,16);
	    	dom2.setID(2);
	    	ActOperationDomain op1 = new ActOperationDomain(1,1,1);
	    	op1.setID(1);
	    	ActOperationDomain op2 = new ActOperationDomain(1,1,1);
	    	op2.setID(2);
	    	ActOperationDomain op3 = new ActOperationDomain(1,1,1);
	    	op3.setID(3);
	    	ActOperationDomain op4 = new ActOperationDomain(1,1,1);
	    	op4.setID(4);
	    	ActOperationDomain op5 = new ActOperationDomain(1,1,1);
	    	op5.setID(5);
	    	ActResourceDomain rs1 = new ActResourceDomain(0,50,16,16);
	    	rs1.setID(1);
	    	op1.addResource(rs1);
	    	ActResourceDomain rs2 = new ActResourceDomain(30,100,16,16);
	    	rs2.setID(2);
	    	op1.addResource(rs2);
	    	ActResourceDomain rs3 = new ActResourceDomain(0,100,16,16);
	    	rs3.setID(3);
	    	op2.addResource(rs3);
	    	ActResourceDomain rs4 = new ActResourceDomain(0,100,16,16);
	    	rs4.setID(4);
	    	op3.addResource(rs4);
	    	ActResourceDomain rs5 = new ActResourceDomain(0,40,16,16);
	    	rs5.setID(5);
	    	op4.addResource(rs5);
	    	ActResourceDomain rs6 = new ActResourceDomain(20,80,16,16);
	    	rs6.setID(6);
	    	op4.addResource(rs6);
	    	ActResourceDomain rs7 = new ActResourceDomain(60,100,16,16);
	    	rs7.setID(7);
	    	op4.addResource(rs7);
	    	ActResourceDomain rs8 = new ActResourceDomain(0,100,16,16);
	    	rs8.setID(8);
	    	op5.addResource(rs8);

	    	dom1.addOperation(op1);
	    	dom1.addOperation(op2);
	    	dom1.addOperation(op3);
	    	
	    	dom2.addOperation(op4);
	    	dom2.addOperation(op5);
    	}
    	catch(PropagationFailureException pfe) {
    	}
    	
    	act1 = new ActivityNode("act 1",dom1);
    	act2 = new ActivityNode("act 2",dom2);
    	intNode = new IntNode("Int NO de",new IntIntervalDomain(-50,150));
    }
    
    public void testSetup() {
    	assertEquals(0,act1.getEarliestStartTime());
    	assertEquals(15,act1.getEarliestEndTime());
    	assertEquals(0,act2.getEarliestStartTime());
    	assertEquals(15,act2.getEarliestEndTime());
    	
    	assertEquals(100,act1.getLatestStartTime());
    	assertEquals(115,act1.getLatestEndTime());
    	assertEquals(100,act2.getLatestStartTime());
    	assertEquals(115,act2.getLatestEndTime());
    	
    	assertEquals(2,act1.getAvailResourceCount(1));
    	assertEquals(1,act1.getAvailResourceCount(2));
    	assertEquals(1,act1.getAvailResourceCount(3));
    	
    	assertEquals(3,act2.getAvailResourceCount(4));
    	assertEquals(1,act2.getAvailResourceCount(5));
    	
    }
    
    public void testActivityToActivityStartsBeforeStart() {
	    try {
    		TemporalArc arc = new TemporalArc(act1,act2,TemporalArc.START,TemporalArc.START,TemporalArc.BEFORE);
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	arc.propagate();
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(1,act2.getEarliestStartTime());
	    	assertEquals(16,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.setEarliestEndTime(1,35);
	    	arc.propagate();
	    	
	    	assertEquals(20,act1.getEarliestStartTime());
	    	assertEquals(35,act1.getEarliestEndTime());
	    	assertEquals(21,act2.getEarliestStartTime());
	    	assertEquals(36,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.removePossibleResource(1,1);
	    	arc.propagate();
	    	
	    	assertEquals(30,act1.getEarliestStartTime());
	    	assertEquals(45,act1.getEarliestEndTime());
	    	assertEquals(31,act2.getEarliestStartTime());
	    	assertEquals(46,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.setEarliestStartTime(42);
	    	act1.setLatestStartTime(42);
	    	arc.propagate();
	    	
	    	assertEquals(42,act1.getEarliestStartTime());
	    	assertEquals(57,act1.getEarliestEndTime());
	    	assertEquals(43,act2.getEarliestStartTime());
	    	assertEquals(58,act2.getEarliestEndTime());
	    	
	    	assertEquals(42,act1.getLatestStartTime());
	    	assertEquals(57,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(2,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    }
	    catch(PropagationFailureException pfe) {
	    	fail();
	    }
    }
    
    public void testActivityToActivityStartsAtStart() {
	    try {
    		TemporalArc arc = new TemporalArc(act1,act2,TemporalArc.START,TemporalArc.START,TemporalArc.AT);
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	arc.propagate();
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.setEarliestEndTime(1,35);
	    	arc.propagate();
	    	
	    	assertEquals(20,act1.getEarliestStartTime());
	    	assertEquals(35,act1.getEarliestEndTime());
	    	assertEquals(20,act2.getEarliestStartTime());
	    	assertEquals(35,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.removePossibleResource(1,1);
	    	arc.propagate();
	    	
	    	assertEquals(30,act1.getEarliestStartTime());
	    	assertEquals(45,act1.getEarliestEndTime());
	    	assertEquals(30,act2.getEarliestStartTime());
	    	assertEquals(45,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.setLatestEndTime(4,75);
	    	arc.propagate();
	    	
	    	assertEquals(30,act1.getEarliestStartTime());
	    	assertEquals(45,act1.getEarliestEndTime());
	    	assertEquals(30,act2.getEarliestStartTime());
	    	assertEquals(45,act2.getEarliestEndTime());
	    	
	    	assertEquals(60,act1.getLatestStartTime());
	    	assertEquals(75,act1.getLatestEndTime());
	    	assertEquals(60,act2.getLatestStartTime());
	    	assertEquals(75,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.setEarliestStartTime(42);
	    	act1.setLatestStartTime(42);
	    	arc.propagate();
	    	
	    	assertEquals(42,act1.getEarliestStartTime());
	    	assertEquals(57,act1.getEarliestEndTime());
	    	assertEquals(42,act2.getEarliestStartTime());
	    	assertEquals(57,act2.getEarliestEndTime());
	    	
	    	assertEquals(42,act1.getLatestStartTime());
	    	assertEquals(57,act1.getLatestEndTime());
	    	assertEquals(42,act2.getLatestStartTime());
	    	assertEquals(57,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(1,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    }
	    catch(PropagationFailureException pfe) {
	    	fail();
	    }
    }
    
    public void testActivityToActivityStartsAfterStart() {
	    try {
    		TemporalArc arc = new TemporalArc(act1,act2,TemporalArc.START,TemporalArc.START,TemporalArc.AFTER);
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	arc.propagate();
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(99,act2.getLatestStartTime());
	    	assertEquals(114,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.setLatestEndTime(3,85);
	    	arc.propagate();
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(70,act1.getLatestStartTime());
	    	assertEquals(85,act1.getLatestEndTime());
	    	assertEquals(69,act2.getLatestStartTime());
	    	assertEquals(84,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.removePossibleResource(1,2);
	    	arc.propagate();
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(50,act1.getLatestStartTime());
	    	assertEquals(65,act1.getLatestEndTime());
	    	assertEquals(49,act2.getLatestStartTime());
	    	assertEquals(64,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(2,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.setEarliestStartTime(42);
	    	act1.setLatestStartTime(42);
	    	arc.propagate();
	    	
	    	assertEquals(42,act1.getEarliestStartTime());
	    	assertEquals(57,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(42,act1.getLatestStartTime());
	    	assertEquals(57,act1.getLatestEndTime());
	    	assertEquals(41,act2.getLatestStartTime());
	    	assertEquals(56,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(2,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    }
	    catch(PropagationFailureException pfe) {
	    	fail();
	    }
    }
    
    
    public void testActivityToActivityStartsBeforeEnd() {
	    try {
    		TemporalArc arc = new TemporalArc(act1,act2,TemporalArc.START,TemporalArc.END,TemporalArc.BEFORE);
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	arc.propagate();
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.setEarliestEndTime(1,35);
	    	arc.propagate();
	    	
	    	assertEquals(20,act1.getEarliestStartTime());
	    	assertEquals(35,act1.getEarliestEndTime());
	    	assertEquals(6,act2.getEarliestStartTime());
	    	assertEquals(21,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.removePossibleResource(1,1);
	    	arc.propagate();
	    	
	    	assertEquals(30,act1.getEarliestStartTime());
	    	assertEquals(45,act1.getEarliestEndTime());
	    	assertEquals(16,act2.getEarliestStartTime());
	    	assertEquals(31,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.setEarliestStartTime(42);
	    	act1.setLatestStartTime(42);
	    	arc.propagate();
	    	
	    	assertEquals(42,act1.getEarliestStartTime());
	    	assertEquals(57,act1.getEarliestEndTime());
	    	assertEquals(28,act2.getEarliestStartTime());
	    	assertEquals(43,act2.getEarliestEndTime());
	    	
	    	assertEquals(42,act1.getLatestStartTime());
	    	assertEquals(57,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    }
	    catch(PropagationFailureException pfe) {
	    	fail();
	    }
    }
    
    public void testActivityToActivityStartsAtEnd() {
	    try {
    		TemporalArc arc = new TemporalArc(act1,act2,TemporalArc.START,TemporalArc.END,TemporalArc.AT);
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	arc.propagate();
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(85,act2.getLatestStartTime());
	    	assertEquals(100,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.setEarliestEndTime(1,35);
	    	arc.propagate();
	    	
	    	assertEquals(20,act1.getEarliestStartTime());
	    	assertEquals(35,act1.getEarliestEndTime());
	    	assertEquals(5,act2.getEarliestStartTime());
	    	assertEquals(20,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(85,act2.getLatestStartTime());
	    	assertEquals(100,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.removePossibleResource(1,1);
	    	arc.propagate();
	    	
	    	assertEquals(30,act1.getEarliestStartTime());
	    	assertEquals(45,act1.getEarliestEndTime());
	    	assertEquals(15,act2.getEarliestStartTime());
	    	assertEquals(30,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(85,act2.getLatestStartTime());
	    	assertEquals(100,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.setLatestEndTime(4,75);
	    	arc.propagate();
	    	
	    	assertEquals(30,act1.getEarliestStartTime());
	    	assertEquals(45,act1.getEarliestEndTime());
	    	assertEquals(15,act2.getEarliestStartTime());
	    	assertEquals(30,act2.getEarliestEndTime());
	    	
	    	assertEquals(60,act1.getLatestStartTime());
	    	assertEquals(75,act1.getLatestEndTime());
	    	assertEquals(45,act2.getLatestStartTime());
	    	assertEquals(60,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(2,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.setEarliestStartTime(42);
	    	act1.setLatestStartTime(42);
	    	arc.propagate();
	    	
	    	assertEquals(42,act1.getEarliestStartTime());
	    	assertEquals(57,act1.getEarliestEndTime());
	    	assertEquals(27,act2.getEarliestStartTime());
	    	assertEquals(42,act2.getEarliestEndTime());
	    	
	    	assertEquals(42,act1.getLatestStartTime());
	    	assertEquals(57,act1.getLatestEndTime());
	    	assertEquals(27,act2.getLatestStartTime());
	    	assertEquals(42,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(2,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    }
	    catch(PropagationFailureException pfe) {
	    	fail();
	    }
    }
    
    public void testActivityToActivityStartsAfterEnd() {
	    try {
    		TemporalArc arc = new TemporalArc(act1,act2,TemporalArc.START,TemporalArc.END,TemporalArc.AFTER);
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	arc.propagate();
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(84,act2.getLatestStartTime());
	    	assertEquals(99,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.setLatestEndTime(3,85);
	    	arc.propagate();
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(70,act1.getLatestStartTime());
	    	assertEquals(85,act1.getLatestEndTime());
	    	assertEquals(54,act2.getLatestStartTime());
	    	assertEquals(69,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(2,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.removePossibleResource(1,2);
	    	arc.propagate();
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(50,act1.getLatestStartTime());
	    	assertEquals(65,act1.getLatestEndTime());
	    	assertEquals(34,act2.getLatestStartTime());
	    	assertEquals(49,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(2,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.setEarliestStartTime(42);
	    	act1.setLatestStartTime(42);
	    	arc.propagate();
	    	
	    	assertEquals(42,act1.getEarliestStartTime());
	    	assertEquals(57,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(42,act1.getLatestStartTime());
	    	assertEquals(57,act1.getLatestEndTime());
	    	assertEquals(26,act2.getLatestStartTime());
	    	assertEquals(41,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(2,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    }
	    catch(PropagationFailureException pfe) {
	    	fail();
	    }
    }
    
    public void testActivityToActivityStartsBeforeNode() {
	    try {
    		TemporalArc arc = new TemporalArc(act1,intNode,TemporalArc.START,TemporalArc.CONSTANT,TemporalArc.BEFORE);
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(-50,intNode.getMin().intValue());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(150,intNode.getMax().intValue());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	arc.propagate();
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(1,intNode.getMin().intValue());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(150,intNode.getMax().intValue());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	act1.setEarliestEndTime(1,35);
	    	arc.propagate();
	    	
	    	assertEquals(20,act1.getEarliestStartTime());
	    	assertEquals(35,act1.getEarliestEndTime());
	    	assertEquals(21,intNode.getMin().intValue());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(150,intNode.getMax().intValue());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	act1.removePossibleResource(1,1);
	    	arc.propagate();
	    	
	    	assertEquals(30,act1.getEarliestStartTime());
	    	assertEquals(45,act1.getEarliestEndTime());
	    	assertEquals(31,intNode.getMin().intValue());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(150,intNode.getMax().intValue());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	act1.setEarliestStartTime(42);
	    	act1.setLatestStartTime(42);
	    	arc.propagate();
	    	
	    	assertEquals(42,act1.getEarliestStartTime());
	    	assertEquals(57,act1.getEarliestEndTime());
	    	assertEquals(43,intNode.getMin().intValue());
	    	
	    	assertEquals(42,act1.getLatestStartTime());
	    	assertEquals(57,act1.getLatestEndTime());
	    	assertEquals(150,intNode.getMax().intValue());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    }
	    catch(PropagationFailureException pfe) {
	    	fail();
	    }
    }
    
    public void testActivityToActivityStartsAtNode() {
	    try {
    		TemporalArc arc = new TemporalArc(act1,intNode,TemporalArc.START,TemporalArc.CONSTANT,TemporalArc.AT);
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(-50,intNode.getMin().intValue());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(150,intNode.getMax().intValue());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	arc.propagate();
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(0,intNode.getMin().intValue());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,intNode.getMax().intValue());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	act1.setEarliestEndTime(1,35);
	    	arc.propagate();
	    	
	    	assertEquals(20,act1.getEarliestStartTime());
	    	assertEquals(35,act1.getEarliestEndTime());
	    	assertEquals(20,intNode.getMin().intValue());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,intNode.getMax().intValue());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	act1.removePossibleResource(1,1);
	    	arc.propagate();
	    	
	    	assertEquals(30,act1.getEarliestStartTime());
	    	assertEquals(45,act1.getEarliestEndTime());
	    	assertEquals(30,intNode.getMin().intValue());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,intNode.getMax().intValue());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	act1.setLatestEndTime(4,75);
	    	arc.propagate();
	    	
	    	assertEquals(30,act1.getEarliestStartTime());
	    	assertEquals(45,act1.getEarliestEndTime());
	    	assertEquals(30,intNode.getMin().intValue());
	    	
	    	assertEquals(60,act1.getLatestStartTime());
	    	assertEquals(75,act1.getLatestEndTime());
	    	assertEquals(60,intNode.getMax().intValue());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	act1.setEarliestStartTime(42);
	    	act1.setLatestStartTime(42);
	    	arc.propagate();
	    	
	    	assertEquals(42,act1.getEarliestStartTime());
	    	assertEquals(57,act1.getEarliestEndTime());
	    	assertEquals(42,intNode.getMin().intValue());
	    	
	    	assertEquals(42,act1.getLatestStartTime());
	    	assertEquals(57,act1.getLatestEndTime());
	    	assertEquals(42,intNode.getMax().intValue());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    }
	    catch(PropagationFailureException pfe) {
	    	fail();
	    }
    }
    
    public void testActivityToActivityStartsAfterNode() {
	    try {
    		TemporalArc arc = new TemporalArc(act1,intNode,TemporalArc.START,TemporalArc.CONSTANT,TemporalArc.AFTER);
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(-50,intNode.getMin().intValue());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(150,intNode.getMax().intValue());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	arc.propagate();
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(-50,intNode.getMin().intValue());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(99,intNode.getMax().intValue());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	act1.setLatestEndTime(3,85);
	    	arc.propagate();
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(-50,intNode.getMin().intValue());
	    	
	    	assertEquals(70,act1.getLatestStartTime());
	    	assertEquals(85,act1.getLatestEndTime());
	    	assertEquals(69,intNode.getMax().intValue());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	act1.removePossibleResource(1,2);
	    	arc.propagate();
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(-50,intNode.getMin().intValue());
	    	
	    	assertEquals(50,act1.getLatestStartTime());
	    	assertEquals(65,act1.getLatestEndTime());
	    	assertEquals(49,intNode.getMax().intValue());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	act1.setEarliestStartTime(42);
	    	act1.setLatestStartTime(42);
	    	arc.propagate();
	    	
	    	assertEquals(42,act1.getEarliestStartTime());
	    	assertEquals(57,act1.getEarliestEndTime());
	    	assertEquals(-50,intNode.getMin().intValue());
	    	
	    	assertEquals(42,act1.getLatestStartTime());
	    	assertEquals(57,act1.getLatestEndTime());
	    	assertEquals(41,intNode.getMax().intValue());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    }
	    catch(PropagationFailureException pfe) {
	    	fail();
	    }
    }
    

    public void testActivityToActivityEndsBeforeStart() {
	    try {
    		TemporalArc arc = new TemporalArc(act1,act2,TemporalArc.END,TemporalArc.START,TemporalArc.BEFORE);
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	arc.propagate();
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(16,act2.getEarliestStartTime());
	    	assertEquals(31,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.setEarliestEndTime(1,35);
	    	arc.propagate();
	    	
	    	assertEquals(20,act1.getEarliestStartTime());
	    	assertEquals(35,act1.getEarliestEndTime());
	    	assertEquals(36,act2.getEarliestStartTime());
	    	assertEquals(51,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.removePossibleResource(1,1);
	    	arc.propagate();
	    	
	    	assertEquals(30,act1.getEarliestStartTime());
	    	assertEquals(45,act1.getEarliestEndTime());
	    	assertEquals(46,act2.getEarliestStartTime());
	    	assertEquals(61,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(2,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.setEarliestStartTime(42);
	    	act1.setLatestStartTime(42);
	    	arc.propagate();
	    	
	    	assertEquals(42,act1.getEarliestStartTime());
	    	assertEquals(57,act1.getEarliestEndTime());
	    	assertEquals(58,act2.getEarliestStartTime());
	    	assertEquals(73,act2.getEarliestEndTime());
	    	
	    	assertEquals(42,act1.getLatestStartTime());
	    	assertEquals(57,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(2,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    }
	    catch(PropagationFailureException pfe) {
	    	fail();
	    }
    }
    
    public void testActivityToActivityEndsAtStart() {
	    try {
    		TemporalArc arc = new TemporalArc(act1,act2,TemporalArc.END,TemporalArc.START,TemporalArc.AT);
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	arc.propagate();
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(15,act2.getEarliestStartTime());
	    	assertEquals(30,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.setEarliestEndTime(1,35);
	    	arc.propagate();
	    	
	    	assertEquals(20,act1.getEarliestStartTime());
	    	assertEquals(35,act1.getEarliestEndTime());
	    	assertEquals(35,act2.getEarliestStartTime());
	    	assertEquals(50,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.removePossibleResource(1,1);
	    	arc.propagate();
	    	
	    	assertEquals(30,act1.getEarliestStartTime());
	    	assertEquals(45,act1.getEarliestEndTime());
	    	assertEquals(45,act2.getEarliestStartTime());
	    	assertEquals(60,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(2,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.setLatestEndTime(4,75);
	    	arc.propagate();
	    	
	    	assertEquals(30,act1.getEarliestStartTime());
	    	assertEquals(45,act1.getEarliestEndTime());
	    	assertEquals(45,act2.getEarliestStartTime());
	    	assertEquals(60,act2.getEarliestEndTime());
	    	
	    	assertEquals(60,act1.getLatestStartTime());
	    	assertEquals(75,act1.getLatestEndTime());
	    	assertEquals(75,act2.getLatestStartTime());
	    	assertEquals(90,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(2,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.setEarliestStartTime(42);
	    	act1.setLatestStartTime(42);
	    	arc.propagate();
	    	
	    	assertEquals(42,act1.getEarliestStartTime());
	    	assertEquals(57,act1.getEarliestEndTime());
	    	assertEquals(57,act2.getEarliestStartTime());
	    	assertEquals(72,act2.getEarliestEndTime());
	    	
	    	assertEquals(42,act1.getLatestStartTime());
	    	assertEquals(57,act1.getLatestEndTime());
	    	assertEquals(57,act2.getLatestStartTime());
	    	assertEquals(72,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(1,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    }
	    catch(PropagationFailureException pfe) {
	    	fail();
	    }
    }
    
    public void testActivityToActivityEndsAfterStart() {
	    try {
    		TemporalArc arc = new TemporalArc(act1,act2,TemporalArc.END,TemporalArc.START,TemporalArc.AFTER);
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	arc.propagate();
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.setLatestEndTime(3,85);
	    	arc.propagate();
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(70,act1.getLatestStartTime());
	    	assertEquals(85,act1.getLatestEndTime());
	    	assertEquals(84,act2.getLatestStartTime());
	    	assertEquals(99,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.removePossibleResource(1,2);
	    	arc.propagate();
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(50,act1.getLatestStartTime());
	    	assertEquals(65,act1.getLatestEndTime());
	    	assertEquals(64,act2.getLatestStartTime());
	    	assertEquals(79,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.setEarliestStartTime(42);
	    	act1.setLatestStartTime(42);
	    	arc.propagate();
	    	
	    	assertEquals(42,act1.getEarliestStartTime());
	    	assertEquals(57,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(42,act1.getLatestStartTime());
	    	assertEquals(57,act1.getLatestEndTime());
	    	assertEquals(56,act2.getLatestStartTime());
	    	assertEquals(71,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(2,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    }
	    catch(PropagationFailureException pfe) {
	    	fail();
	    }
    }
    
    
    public void testActivityToActivityEndsBeforeEnd() {
	    try {
    		TemporalArc arc = new TemporalArc(act1,act2,TemporalArc.END,TemporalArc.END,TemporalArc.BEFORE);
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	arc.propagate();
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(1,act2.getEarliestStartTime());
	    	assertEquals(16,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.setEarliestEndTime(1,35);
	    	arc.propagate();
	    	
	    	assertEquals(20,act1.getEarliestStartTime());
	    	assertEquals(35,act1.getEarliestEndTime());
	    	assertEquals(21,act2.getEarliestStartTime());
	    	assertEquals(36,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.removePossibleResource(1,1);
	    	arc.propagate();
	    	
	    	assertEquals(30,act1.getEarliestStartTime());
	    	assertEquals(45,act1.getEarliestEndTime());
	    	assertEquals(31,act2.getEarliestStartTime());
	    	assertEquals(46,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.setEarliestStartTime(42);
	    	act1.setLatestStartTime(42);
	    	arc.propagate();
	    	
	    	assertEquals(42,act1.getEarliestStartTime());
	    	assertEquals(57,act1.getEarliestEndTime());
	    	assertEquals(43,act2.getEarliestStartTime());
	    	assertEquals(58,act2.getEarliestEndTime());
	    	
	    	assertEquals(42,act1.getLatestStartTime());
	    	assertEquals(57,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(2,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    }
	    catch(PropagationFailureException pfe) {
	    	fail();
	    }
    }
    
    public void testActivityToActivityEndsAtEnd() {
	    try {
    		TemporalArc arc = new TemporalArc(act1,act2,TemporalArc.END,TemporalArc.END,TemporalArc.AT);
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	arc.propagate();
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.setEarliestEndTime(1,35);
	    	arc.propagate();
	    	
	    	assertEquals(20,act1.getEarliestStartTime());
	    	assertEquals(35,act1.getEarliestEndTime());
	    	assertEquals(20,act2.getEarliestStartTime());
	    	assertEquals(35,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.removePossibleResource(1,1);
	    	arc.propagate();
	    	
	    	assertEquals(30,act1.getEarliestStartTime());
	    	assertEquals(45,act1.getEarliestEndTime());
	    	assertEquals(30,act2.getEarliestStartTime());
	    	assertEquals(45,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.setLatestEndTime(4,75);
	    	arc.propagate();
	    	
	    	assertEquals(30,act1.getEarliestStartTime());
	    	assertEquals(45,act1.getEarliestEndTime());
	    	assertEquals(30,act2.getEarliestStartTime());
	    	assertEquals(45,act2.getEarliestEndTime());
	    	
	    	assertEquals(60,act1.getLatestStartTime());
	    	assertEquals(75,act1.getLatestEndTime());
	    	assertEquals(60,act2.getLatestStartTime());
	    	assertEquals(75,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.setEarliestStartTime(42);
	    	act1.setLatestStartTime(42);
	    	arc.propagate();
	    	
	    	assertEquals(42,act1.getEarliestStartTime());
	    	assertEquals(57,act1.getEarliestEndTime());
	    	assertEquals(42,act2.getEarliestStartTime());
	    	assertEquals(57,act2.getEarliestEndTime());
	    	
	    	assertEquals(42,act1.getLatestStartTime());
	    	assertEquals(57,act1.getLatestEndTime());
	    	assertEquals(42,act2.getLatestStartTime());
	    	assertEquals(57,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(1,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    }
	    catch(PropagationFailureException pfe) {
	    	fail();
	    }
    }
    
    public void testActivityToActivityEndsAfterEnd() {
	    try {
    		TemporalArc arc = new TemporalArc(act1,act2,TemporalArc.END,TemporalArc.END,TemporalArc.AFTER);
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	arc.propagate();
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(99,act2.getLatestStartTime());
	    	assertEquals(114,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.setLatestEndTime(3,85);
	    	arc.propagate();
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(70,act1.getLatestStartTime());
	    	assertEquals(85,act1.getLatestEndTime());
	    	assertEquals(69,act2.getLatestStartTime());
	    	assertEquals(84,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.removePossibleResource(1,2);
	    	arc.propagate();
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(50,act1.getLatestStartTime());
	    	assertEquals(65,act1.getLatestEndTime());
	    	assertEquals(49,act2.getLatestStartTime());
	    	assertEquals(64,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(2,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.setEarliestStartTime(42);
	    	act1.setLatestStartTime(42);
	    	arc.propagate();
	    	
	    	assertEquals(42,act1.getEarliestStartTime());
	    	assertEquals(57,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(42,act1.getLatestStartTime());
	    	assertEquals(57,act1.getLatestEndTime());
	    	assertEquals(41,act2.getLatestStartTime());
	    	assertEquals(56,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(2,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    }
	    catch(PropagationFailureException pfe) {
	    	fail();
	    }
    }
    
    public void testActivityToActivityEndsBeforeNode() {
	    try {
    		TemporalArc arc = new TemporalArc(act1,intNode,TemporalArc.END,TemporalArc.CONSTANT,TemporalArc.BEFORE);
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(-50,intNode.getMin().intValue());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(150,intNode.getMax().intValue());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	arc.propagate();
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(16,intNode.getMin().intValue());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(150,intNode.getMax().intValue());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	act1.setEarliestEndTime(1,35);
	    	arc.propagate();
	    	
	    	assertEquals(20,act1.getEarliestStartTime());
	    	assertEquals(35,act1.getEarliestEndTime());
	    	assertEquals(36,intNode.getMin().intValue());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(150,intNode.getMax().intValue());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	act1.removePossibleResource(1,1);
	    	arc.propagate();
	    	
	    	assertEquals(30,act1.getEarliestStartTime());
	    	assertEquals(45,act1.getEarliestEndTime());
	    	assertEquals(46,intNode.getMin().intValue());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(150,intNode.getMax().intValue());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	act1.setEarliestStartTime(42);
	    	act1.setLatestStartTime(42);
	    	arc.propagate();
	    	
	    	assertEquals(42,act1.getEarliestStartTime());
	    	assertEquals(57,act1.getEarliestEndTime());
	    	assertEquals(58,intNode.getMin().intValue());
	    	
	    	assertEquals(42,act1.getLatestStartTime());
	    	assertEquals(57,act1.getLatestEndTime());
	    	assertEquals(150,intNode.getMax().intValue());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    }
	    catch(PropagationFailureException pfe) {
	    	fail();
	    }
    }
    
    public void testActivityToActivityEndsAtNode() {
	    try {
    		TemporalArc arc = new TemporalArc(act1,intNode,TemporalArc.END,TemporalArc.CONSTANT,TemporalArc.AT);
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(-50,intNode.getMin().intValue());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(150,intNode.getMax().intValue());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	arc.propagate();
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(15,intNode.getMin().intValue());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(115,intNode.getMax().intValue());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	act1.setEarliestEndTime(1,35);
	    	arc.propagate();
	    	
	    	assertEquals(20,act1.getEarliestStartTime());
	    	assertEquals(35,act1.getEarliestEndTime());
	    	assertEquals(35,intNode.getMin().intValue());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(115,intNode.getMax().intValue());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	act1.removePossibleResource(1,1);
	    	arc.propagate();
	    	
	    	assertEquals(30,act1.getEarliestStartTime());
	    	assertEquals(45,act1.getEarliestEndTime());
	    	assertEquals(45,intNode.getMin().intValue());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(115,intNode.getMax().intValue());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	act1.setLatestEndTime(4,75);
	    	arc.propagate();
	    	
	    	assertEquals(30,act1.getEarliestStartTime());
	    	assertEquals(45,act1.getEarliestEndTime());
	    	assertEquals(45,intNode.getMin().intValue());
	    	
	    	assertEquals(60,act1.getLatestStartTime());
	    	assertEquals(75,act1.getLatestEndTime());
	    	assertEquals(75,intNode.getMax().intValue());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	act1.setEarliestStartTime(42);
	    	act1.setLatestStartTime(42);
	    	arc.propagate();
	    	
	    	assertEquals(42,act1.getEarliestStartTime());
	    	assertEquals(57,act1.getEarliestEndTime());
	    	assertEquals(57,intNode.getMin().intValue());
	    	
	    	assertEquals(42,act1.getLatestStartTime());
	    	assertEquals(57,act1.getLatestEndTime());
	    	assertEquals(57,intNode.getMax().intValue());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    }
	    catch(PropagationFailureException pfe) {
	    	fail();
	    }
    }
    
    public void testActivityToActivityEndsAfterNode() {
	    try {
    		TemporalArc arc = new TemporalArc(act1,intNode,TemporalArc.END,TemporalArc.CONSTANT,TemporalArc.AFTER);
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(-50,intNode.getMin().intValue());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(150,intNode.getMax().intValue());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	arc.propagate();
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(-50,intNode.getMin().intValue());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(114,intNode.getMax().intValue());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	act1.setLatestEndTime(3,85);
	    	arc.propagate();
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(-50,intNode.getMin().intValue());
	    	
	    	assertEquals(70,act1.getLatestStartTime());
	    	assertEquals(85,act1.getLatestEndTime());
	    	assertEquals(84,intNode.getMax().intValue());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	act1.removePossibleResource(1,2);
	    	arc.propagate();
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(-50,intNode.getMin().intValue());
	    	
	    	assertEquals(50,act1.getLatestStartTime());
	    	assertEquals(65,act1.getLatestEndTime());
	    	assertEquals(64,intNode.getMax().intValue());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	act1.setEarliestStartTime(42);
	    	act1.setLatestStartTime(42);
	    	arc.propagate();
	    	
	    	assertEquals(42,act1.getEarliestStartTime());
	    	assertEquals(57,act1.getEarliestEndTime());
	    	assertEquals(-50,intNode.getMin().intValue());
	    	
	    	assertEquals(42,act1.getLatestStartTime());
	    	assertEquals(57,act1.getLatestEndTime());
	    	assertEquals(56,intNode.getMax().intValue());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    }
	    catch(PropagationFailureException pfe) {
	    	fail();
	    }
    }
    

    public void testActivityToActivityNodeBeforeStart() {
	    try {
    		TemporalArc arc = new TemporalArc(intNode,act2,TemporalArc.CONSTANT,TemporalArc.START,TemporalArc.BEFORE);
	    	
	    	assertEquals(-50,intNode.getMin().intValue());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(150,intNode.getMax().intValue());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	arc.propagate();
	    	
	    	assertEquals(-50,intNode.getMin().intValue());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(150,intNode.getMax().intValue());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	intNode.setMin(new Integer(35));
	    	arc.propagate();
	    	
	    	assertEquals(35,intNode.getMin().intValue());
	    	assertEquals(36,act2.getEarliestStartTime());
	    	assertEquals(51,act2.getEarliestEndTime());
	    	
	    	assertEquals(150,intNode.getMax().intValue());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	intNode.setMin(new Integer(42));
	    	intNode.setMax(new Integer(42));
	    	arc.propagate();
	    	
	    	assertEquals(42,intNode.getMin().intValue());
	    	assertEquals(43,act2.getEarliestStartTime());
	    	assertEquals(58,act2.getEarliestEndTime());
	    	
	    	assertEquals(42,intNode.getMax().intValue());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    }
	    catch(PropagationFailureException pfe) {
	    	fail();
	    }
    }
    
    public void testActivityToActivityNodeAtStart() {
	    try {
    		TemporalArc arc = new TemporalArc(intNode,act2,TemporalArc.CONSTANT,TemporalArc.START,TemporalArc.AT);
	    	
    		assertEquals(-50,intNode.getMin().intValue());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(150,intNode.getMax().intValue());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	intNode.setMin(new Integer(35));
	    	arc.propagate();
	    	
	    	assertEquals(35,intNode.getMin().intValue());
	    	assertEquals(35,act2.getEarliestStartTime());
	    	assertEquals(50,act2.getEarliestEndTime());
	    	
	    	assertEquals(150,intNode.getMax().intValue());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	intNode.setMax(new Integer(75));
	    	arc.propagate();
	    	
	    	assertEquals(35,intNode.getMin().intValue());
	    	assertEquals(35,act2.getEarliestStartTime());
	    	assertEquals(50,act2.getEarliestEndTime());
	    	
	    	assertEquals(75,intNode.getMax().intValue());
	    	assertEquals(75,act2.getLatestStartTime());
	    	assertEquals(90,act2.getLatestEndTime());
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	intNode.setMin(new Integer(42));
	    	intNode.setMax(new Integer(42));
	    	arc.propagate();
	    	
	    	assertEquals(42,intNode.getMin().intValue());
	    	assertEquals(42,act2.getEarliestStartTime());
	    	assertEquals(57,act2.getEarliestEndTime());
	    	
	    	assertEquals(42,intNode.getMax().intValue());
	    	assertEquals(42,act2.getLatestStartTime());
	    	assertEquals(57,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    }
	    catch(PropagationFailureException pfe) {
	    	fail();
	    }
    }
    
    public void testActivityToActivityNodeAfterStart() {
	    try {
    		TemporalArc arc = new TemporalArc(intNode,act2,TemporalArc.CONSTANT,TemporalArc.START,TemporalArc.AFTER);
	    	
    		assertEquals(-50,intNode.getMin().intValue());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(150,intNode.getMax().intValue());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	intNode.setMax(new Integer(85));
	    	arc.propagate();
	    	
	    	assertEquals(-50,intNode.getMin().intValue());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(85,intNode.getMax().intValue());
	    	assertEquals(84,act2.getLatestStartTime());
	    	assertEquals(99,act2.getLatestEndTime());
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	intNode.setMin(new Integer(42));
	    	intNode.setMax(new Integer(42));
	    	arc.propagate();
	    	
	    	assertEquals(42,intNode.getMin().intValue());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(42,intNode.getMax().intValue());
	    	assertEquals(41,act2.getLatestStartTime());
	    	assertEquals(56,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    }
	    catch(PropagationFailureException pfe) {
	    	fail();
	    }
    }
    
    
    public void testActivityToActivityNodeBeforeEnd() {
	    try {
    		TemporalArc arc = new TemporalArc(intNode,act2,TemporalArc.CONSTANT,TemporalArc.END,TemporalArc.BEFORE);
	    	
    		assertEquals(-50,intNode.getMin().intValue());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(150,intNode.getMax().intValue());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	intNode.setMin(new Integer(35));
	    	arc.propagate();
	    	
	    	assertEquals(35,intNode.getMin().intValue());
	    	assertEquals(21,act2.getEarliestStartTime());
	    	assertEquals(36,act2.getEarliestEndTime());
	    	
	    	assertEquals(150,intNode.getMax().intValue());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	intNode.setMin(new Integer(42));
	    	intNode.setMax(new Integer(42));
	    	arc.propagate();
	    	
	    	assertEquals(42,intNode.getMin().intValue());
	    	assertEquals(28,act2.getEarliestStartTime());
	    	assertEquals(43,act2.getEarliestEndTime());
	    	
	    	assertEquals(42,intNode.getMax().intValue());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    }
	    catch(PropagationFailureException pfe) {
	    	fail();
	    }
    }
    
    public void testActivityToActivityNodeAtEnd() {
	    try {
    		TemporalArc arc = new TemporalArc(intNode,act2,TemporalArc.CONSTANT,TemporalArc.END,TemporalArc.AT);
	    	
    		assertEquals(-50,intNode.getMin().intValue());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(150,intNode.getMax().intValue());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	intNode.setMin(new Integer(35));
	    	arc.propagate();
	    	
	    	assertEquals(35,intNode.getMin().intValue());
	    	assertEquals(20,act2.getEarliestStartTime());
	    	assertEquals(35,act2.getEarliestEndTime());
	    	
	    	assertEquals(150,intNode.getMax().intValue());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	intNode.setMax(new Integer(75));
	    	arc.propagate();
	    	
	    	assertEquals(35,intNode.getMin().intValue());
	    	assertEquals(20,act2.getEarliestStartTime());
	    	assertEquals(35,act2.getEarliestEndTime());
	    	
	    	assertEquals(75,intNode.getMax().intValue());
	    	assertEquals(60,act2.getLatestStartTime());
	    	assertEquals(75,act2.getLatestEndTime());
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	intNode.setMin(new Integer(42));
	    	intNode.setMax(new Integer(42));
	    	arc.propagate();
	    	
	    	assertEquals(42,intNode.getMin().intValue());
	    	assertEquals(27,act2.getEarliestStartTime());
	    	assertEquals(42,act2.getEarliestEndTime());
	    	
	    	assertEquals(42,intNode.getMax().intValue());
	    	assertEquals(27,act2.getLatestStartTime());
	    	assertEquals(42,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    }
	    catch(PropagationFailureException pfe) {
	    	fail();
	    }
    }
    
    public void testActivityToActivityNodeAfterEnd() {
	    try {
    		TemporalArc arc = new TemporalArc(intNode,act2,TemporalArc.CONSTANT,TemporalArc.END,TemporalArc.AFTER);
	    	
    		assertEquals(-50,intNode.getMin().intValue());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(150,intNode.getMax().intValue());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	intNode.setMax(new Integer(85));
	    	arc.propagate();
	    	
	    	assertEquals(-50,intNode.getMin().intValue());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(85,intNode.getMax().intValue());
	    	assertEquals(69,act2.getLatestStartTime());
	    	assertEquals(84,act2.getLatestEndTime());
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	intNode.setMin(new Integer(42));
	    	intNode.setMax(new Integer(42));
	    	arc.propagate();
	    	
	    	assertEquals(42,intNode.getMin().intValue());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(42,intNode.getMax().intValue());
	    	assertEquals(26,act2.getLatestStartTime());
	    	assertEquals(41,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    }
	    catch(PropagationFailureException pfe) {
	    	fail();
	    }
    }
    

    public void testActivityToActivityStartsBeforeStartWithDelay() {
	    try {
    		TemporalArc arc = new TemporalArc(act1,act2,TemporalArc.START,TemporalArc.START,TemporalArc.BEFORE,5);
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	arc.propagate();
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(6,act2.getEarliestStartTime());
	    	assertEquals(21,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.setEarliestEndTime(1,35);
	    	arc.propagate();
	    	
	    	assertEquals(20,act1.getEarliestStartTime());
	    	assertEquals(35,act1.getEarliestEndTime());
	    	assertEquals(26,act2.getEarliestStartTime());
	    	assertEquals(41,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.removePossibleResource(1,1);
	    	arc.propagate();
	    	
	    	assertEquals(30,act1.getEarliestStartTime());
	    	assertEquals(45,act1.getEarliestEndTime());
	    	assertEquals(36,act2.getEarliestStartTime());
	    	assertEquals(51,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.setEarliestStartTime(42);
	    	act1.setLatestStartTime(42);
	    	arc.propagate();
	    	
	    	assertEquals(42,act1.getEarliestStartTime());
	    	assertEquals(57,act1.getEarliestEndTime());
	    	assertEquals(48,act2.getEarliestStartTime());
	    	assertEquals(63,act2.getEarliestEndTime());
	    	
	    	assertEquals(42,act1.getLatestStartTime());
	    	assertEquals(57,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(2,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    }
	    catch(PropagationFailureException pfe) {
	    	fail();
	    }
    }
    
    public void testActivityToActivityStartsAtStartWithDelay() {
	    try {
    		TemporalArc arc = new TemporalArc(act1,act2,TemporalArc.START,TemporalArc.START,TemporalArc.AT,5);
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	arc.propagate();
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.setEarliestEndTime(1,35);
	    	arc.propagate();
	    	
	    	assertEquals(20,act1.getEarliestStartTime());
	    	assertEquals(35,act1.getEarliestEndTime());
	    	assertEquals(20,act2.getEarliestStartTime());
	    	assertEquals(35,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.removePossibleResource(1,1);
	    	arc.propagate();
	    	
	    	assertEquals(30,act1.getEarliestStartTime());
	    	assertEquals(45,act1.getEarliestEndTime());
	    	assertEquals(30,act2.getEarliestStartTime());
	    	assertEquals(45,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.setLatestEndTime(4,75);
	    	arc.propagate();
	    	
	    	assertEquals(30,act1.getEarliestStartTime());
	    	assertEquals(45,act1.getEarliestEndTime());
	    	assertEquals(30,act2.getEarliestStartTime());
	    	assertEquals(45,act2.getEarliestEndTime());
	    	
	    	assertEquals(60,act1.getLatestStartTime());
	    	assertEquals(75,act1.getLatestEndTime());
	    	assertEquals(60,act2.getLatestStartTime());
	    	assertEquals(75,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.setEarliestStartTime(42);
	    	act1.setLatestStartTime(42);
	    	arc.propagate();
	    	
	    	assertEquals(42,act1.getEarliestStartTime());
	    	assertEquals(57,act1.getEarliestEndTime());
	    	assertEquals(42,act2.getEarliestStartTime());
	    	assertEquals(57,act2.getEarliestEndTime());
	    	
	    	assertEquals(42,act1.getLatestStartTime());
	    	assertEquals(57,act1.getLatestEndTime());
	    	assertEquals(42,act2.getLatestStartTime());
	    	assertEquals(57,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(1,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    }
	    catch(PropagationFailureException pfe) {
	    	fail();
	    }
    }
   
    
    public void testActivityToActivityStartsAtEndWithDelay() {
	    try {
    		TemporalArc arc = new TemporalArc(act1,act2,TemporalArc.START,TemporalArc.END,TemporalArc.AT,5);
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	arc.propagate();
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(85,act2.getLatestStartTime());
	    	assertEquals(100,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.setEarliestEndTime(1,35);
	    	arc.propagate();
	    	
	    	assertEquals(20,act1.getEarliestStartTime());
	    	assertEquals(35,act1.getEarliestEndTime());
	    	assertEquals(5,act2.getEarliestStartTime());
	    	assertEquals(20,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(85,act2.getLatestStartTime());
	    	assertEquals(100,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.removePossibleResource(1,1);
	    	arc.propagate();
	    	
	    	assertEquals(30,act1.getEarliestStartTime());
	    	assertEquals(45,act1.getEarliestEndTime());
	    	assertEquals(15,act2.getEarliestStartTime());
	    	assertEquals(30,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(85,act2.getLatestStartTime());
	    	assertEquals(100,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.setLatestEndTime(4,75);
	    	arc.propagate();
	    	
	    	assertEquals(30,act1.getEarliestStartTime());
	    	assertEquals(45,act1.getEarliestEndTime());
	    	assertEquals(15,act2.getEarliestStartTime());
	    	assertEquals(30,act2.getEarliestEndTime());
	    	
	    	assertEquals(60,act1.getLatestStartTime());
	    	assertEquals(75,act1.getLatestEndTime());
	    	assertEquals(45,act2.getLatestStartTime());
	    	assertEquals(60,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(2,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.setEarliestStartTime(42);
	    	act1.setLatestStartTime(42);
	    	arc.propagate();
	    	
	    	assertEquals(42,act1.getEarliestStartTime());
	    	assertEquals(57,act1.getEarliestEndTime());
	    	assertEquals(27,act2.getEarliestStartTime());
	    	assertEquals(42,act2.getEarliestEndTime());
	    	
	    	assertEquals(42,act1.getLatestStartTime());
	    	assertEquals(57,act1.getLatestEndTime());
	    	assertEquals(27,act2.getLatestStartTime());
	    	assertEquals(42,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(2,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    }
	    catch(PropagationFailureException pfe) {
	    	fail();
	    }
    }
    
    public void testActivityToActivityStartsBeforeNodeWithDelay() {
	    try {
    		TemporalArc arc = new TemporalArc(act1,intNode,TemporalArc.START,TemporalArc.CONSTANT,TemporalArc.BEFORE,5);
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(-50,intNode.getMin().intValue());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(150,intNode.getMax().intValue());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	arc.propagate();
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(6,intNode.getMin().intValue());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(150,intNode.getMax().intValue());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	act1.setEarliestEndTime(1,35);
	    	arc.propagate();
	    	
	    	assertEquals(20,act1.getEarliestStartTime());
	    	assertEquals(35,act1.getEarliestEndTime());
	    	assertEquals(26,intNode.getMin().intValue());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(150,intNode.getMax().intValue());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	act1.removePossibleResource(1,1);
	    	arc.propagate();
	    	
	    	assertEquals(30,act1.getEarliestStartTime());
	    	assertEquals(45,act1.getEarliestEndTime());
	    	assertEquals(36,intNode.getMin().intValue());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(150,intNode.getMax().intValue());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	act1.setEarliestStartTime(42);
	    	act1.setLatestStartTime(42);
	    	arc.propagate();
	    	
	    	assertEquals(42,act1.getEarliestStartTime());
	    	assertEquals(57,act1.getEarliestEndTime());
	    	assertEquals(48,intNode.getMin().intValue());
	    	
	    	assertEquals(42,act1.getLatestStartTime());
	    	assertEquals(57,act1.getLatestEndTime());
	    	assertEquals(150,intNode.getMax().intValue());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    }
	    catch(PropagationFailureException pfe) {
	    	fail();
	    }
    }
    
   
    public void testActivityToActivityEndsBeforeStartWithDelay() {
	    try {
    		TemporalArc arc = new TemporalArc(act1,act2,TemporalArc.END,TemporalArc.START,TemporalArc.BEFORE,5);
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	arc.propagate();
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(21,act2.getEarliestStartTime());
	    	assertEquals(36,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.setEarliestEndTime(1,35);
	    	arc.propagate();
	    	
	    	assertEquals(20,act1.getEarliestStartTime());
	    	assertEquals(35,act1.getEarliestEndTime());
	    	assertEquals(41,act2.getEarliestStartTime());
	    	assertEquals(56,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(2,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.removePossibleResource(1,1);
	    	arc.propagate();
	    	
	    	assertEquals(30,act1.getEarliestStartTime());
	    	assertEquals(45,act1.getEarliestEndTime());
	    	assertEquals(51,act2.getEarliestStartTime());
	    	assertEquals(66,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(2,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.setEarliestStartTime(42);
	    	act1.setLatestStartTime(42);
	    	arc.propagate();
	    	
	    	assertEquals(42,act1.getEarliestStartTime());
	    	assertEquals(57,act1.getEarliestEndTime());
	    	assertEquals(63,act2.getEarliestStartTime());
	    	assertEquals(78,act2.getEarliestEndTime());
	    	
	    	assertEquals(42,act1.getLatestStartTime());
	    	assertEquals(57,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(2,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    }
	    catch(PropagationFailureException pfe) {
	    	fail();
	    }
    }
    
    public void testActivityToActivityEndsBeforeEndWithDelay() {
	    try {
    		TemporalArc arc = new TemporalArc(act1,act2,TemporalArc.END,TemporalArc.END,TemporalArc.BEFORE);
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	arc.propagate();
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(1,act2.getEarliestStartTime());
	    	assertEquals(16,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.setEarliestEndTime(1,35);
	    	arc.propagate();
	    	
	    	assertEquals(20,act1.getEarliestStartTime());
	    	assertEquals(35,act1.getEarliestEndTime());
	    	assertEquals(21,act2.getEarliestStartTime());
	    	assertEquals(36,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.removePossibleResource(1,1);
	    	arc.propagate();
	    	
	    	assertEquals(30,act1.getEarliestStartTime());
	    	assertEquals(45,act1.getEarliestEndTime());
	    	assertEquals(31,act2.getEarliestStartTime());
	    	assertEquals(46,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.setEarliestStartTime(42);
	    	act1.setLatestStartTime(42);
	    	arc.propagate();
	    	
	    	assertEquals(42,act1.getEarliestStartTime());
	    	assertEquals(57,act1.getEarliestEndTime());
	    	assertEquals(43,act2.getEarliestStartTime());
	    	assertEquals(58,act2.getEarliestEndTime());
	    	
	    	assertEquals(42,act1.getLatestStartTime());
	    	assertEquals(57,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(2,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    }
	    catch(PropagationFailureException pfe) {
	    	fail();
	    }
    }
    
    public void testActivityToActivityEndsAtEndWithDelay() {
	    try {
    		TemporalArc arc = new TemporalArc(act1,act2,TemporalArc.END,TemporalArc.END,TemporalArc.AT,5);
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	arc.propagate();
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.setEarliestEndTime(1,35);
	    	arc.propagate();
	    	
	    	assertEquals(20,act1.getEarliestStartTime());
	    	assertEquals(35,act1.getEarliestEndTime());
	    	assertEquals(20,act2.getEarliestStartTime());
	    	assertEquals(35,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.removePossibleResource(1,1);
	    	arc.propagate();
	    	
	    	assertEquals(30,act1.getEarliestStartTime());
	    	assertEquals(45,act1.getEarliestEndTime());
	    	assertEquals(30,act2.getEarliestStartTime());
	    	assertEquals(45,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.setLatestEndTime(4,75);
	    	arc.propagate();
	    	
	    	assertEquals(30,act1.getEarliestStartTime());
	    	assertEquals(45,act1.getEarliestEndTime());
	    	assertEquals(30,act2.getEarliestStartTime());
	    	assertEquals(45,act2.getEarliestEndTime());
	    	
	    	assertEquals(60,act1.getLatestStartTime());
	    	assertEquals(75,act1.getLatestEndTime());
	    	assertEquals(60,act2.getLatestStartTime());
	    	assertEquals(75,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.setEarliestStartTime(42);
	    	act1.setLatestStartTime(42);
	    	arc.propagate();
	    	
	    	assertEquals(42,act1.getEarliestStartTime());
	    	assertEquals(57,act1.getEarliestEndTime());
	    	assertEquals(42,act2.getEarliestStartTime());
	    	assertEquals(57,act2.getEarliestEndTime());
	    	
	    	assertEquals(42,act1.getLatestStartTime());
	    	assertEquals(57,act1.getLatestEndTime());
	    	assertEquals(42,act2.getLatestStartTime());
	    	assertEquals(57,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(1,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    }
	    catch(PropagationFailureException pfe) {
	    	fail();
	    }
    }
    
    public void testActivityToActivityEndsAfterEndWithDelay() {
	    try {
    		TemporalArc arc = new TemporalArc(act1,act2,TemporalArc.END,TemporalArc.END,TemporalArc.AFTER,5);
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	arc.propagate();
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(94,act2.getLatestStartTime());
	    	assertEquals(109,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.setLatestEndTime(3,85);
	    	arc.propagate();
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(70,act1.getLatestStartTime());
	    	assertEquals(85,act1.getLatestEndTime());
	    	assertEquals(64,act2.getLatestStartTime());
	    	assertEquals(79,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.removePossibleResource(1,2);
	    	arc.propagate();
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(50,act1.getLatestStartTime());
	    	assertEquals(65,act1.getLatestEndTime());
	    	assertEquals(44,act2.getLatestStartTime());
	    	assertEquals(59,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(2,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	act1.setEarliestStartTime(42);
	    	act1.setLatestStartTime(42);
	    	arc.propagate();
	    	
	    	assertEquals(42,act1.getEarliestStartTime());
	    	assertEquals(57,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(42,act1.getLatestStartTime());
	    	assertEquals(57,act1.getLatestEndTime());
	    	assertEquals(36,act2.getLatestStartTime());
	    	assertEquals(51,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(2,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    }
	    catch(PropagationFailureException pfe) {
	    	fail();
	    }
    }
    
    public void testActivityToActivityEndsBeforeNodeWithDelay() {
	    try {
    		TemporalArc arc = new TemporalArc(act1,intNode,TemporalArc.END,TemporalArc.CONSTANT,TemporalArc.BEFORE,5);
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(-50,intNode.getMin().intValue());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(150,intNode.getMax().intValue());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	arc.propagate();
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(15,act1.getEarliestEndTime());
	    	assertEquals(21,intNode.getMin().intValue());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(150,intNode.getMax().intValue());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	act1.setEarliestEndTime(1,35);
	    	arc.propagate();
	    	
	    	assertEquals(20,act1.getEarliestStartTime());
	    	assertEquals(35,act1.getEarliestEndTime());
	    	assertEquals(41,intNode.getMin().intValue());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(150,intNode.getMax().intValue());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	act1.removePossibleResource(1,1);
	    	arc.propagate();
	    	
	    	assertEquals(30,act1.getEarliestStartTime());
	    	assertEquals(45,act1.getEarliestEndTime());
	    	assertEquals(51,intNode.getMin().intValue());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(115,act1.getLatestEndTime());
	    	assertEquals(150,intNode.getMax().intValue());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    	act1.setEarliestStartTime(42);
	    	act1.setLatestStartTime(42);
	    	arc.propagate();
	    	
	    	assertEquals(42,act1.getEarliestStartTime());
	    	assertEquals(57,act1.getEarliestEndTime());
	    	assertEquals(63,intNode.getMin().intValue());
	    	
	    	assertEquals(42,act1.getLatestStartTime());
	    	assertEquals(57,act1.getLatestEndTime());
	    	assertEquals(150,intNode.getMax().intValue());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(1,act1.getAvailResourceCount(2));
	    	assertEquals(1,act1.getAvailResourceCount(3));
	    	
	    }
	    catch(PropagationFailureException pfe) {
	    	fail();
	    }
    }

    public void testActivityToActivityNodeBeforeStartWithDelay() {
	    try {
    		TemporalArc arc = new TemporalArc(intNode,act2,TemporalArc.CONSTANT,TemporalArc.START,TemporalArc.BEFORE,5);
	    	
	    	assertEquals(-50,intNode.getMin().intValue());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(150,intNode.getMax().intValue());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	arc.propagate();
	    	
	    	assertEquals(-50,intNode.getMin().intValue());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(150,intNode.getMax().intValue());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	intNode.setMin(new Integer(35));
	    	arc.propagate();
	    	
	    	assertEquals(35,intNode.getMin().intValue());
	    	assertEquals(41,act2.getEarliestStartTime());
	    	assertEquals(56,act2.getEarliestEndTime());
	    	
	    	assertEquals(150,intNode.getMax().intValue());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	intNode.setMin(new Integer(42));
	    	intNode.setMax(new Integer(42));
	    	arc.propagate();
	    	
	    	assertEquals(42,intNode.getMin().intValue());
	    	assertEquals(48,act2.getEarliestStartTime());
	    	assertEquals(63,act2.getEarliestEndTime());
	    	
	    	assertEquals(42,intNode.getMax().intValue());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    }
	    catch(PropagationFailureException pfe) {
	    	fail();
	    }
    }
    
    public void testActivityToActivityNodeAfterStartWithDelay() {
	    try {
    		TemporalArc arc = new TemporalArc(intNode,act2,TemporalArc.CONSTANT,TemporalArc.START,TemporalArc.AFTER,5);
	    	
    		assertEquals(-50,intNode.getMin().intValue());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(150,intNode.getMax().intValue());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	intNode.setMax(new Integer(85));
	    	arc.propagate();
	    	
	    	assertEquals(-50,intNode.getMin().intValue());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(85,intNode.getMax().intValue());
	    	assertEquals(79,act2.getLatestStartTime());
	    	assertEquals(94,act2.getLatestEndTime());
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	intNode.setMin(new Integer(42));
	    	intNode.setMax(new Integer(42));
	    	arc.propagate();
	    	
	    	assertEquals(42,intNode.getMin().intValue());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(42,intNode.getMax().intValue());
	    	assertEquals(36,act2.getLatestStartTime());
	    	assertEquals(51,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    }
	    catch(PropagationFailureException pfe) {
	    	fail();
	    }
    }
    
    public void testActivityToActivityNodeAtEndWithDelay() {
	    try {
    		TemporalArc arc = new TemporalArc(intNode,act2,TemporalArc.CONSTANT,TemporalArc.END,TemporalArc.AT,5);
	    	
    		assertEquals(-50,intNode.getMin().intValue());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(150,intNode.getMax().intValue());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	intNode.setMin(new Integer(35));
	    	arc.propagate();
	    	
	    	assertEquals(35,intNode.getMin().intValue());
	    	assertEquals(20,act2.getEarliestStartTime());
	    	assertEquals(35,act2.getEarliestEndTime());
	    	
	    	assertEquals(150,intNode.getMax().intValue());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	intNode.setMax(new Integer(75));
	    	arc.propagate();
	    	
	    	assertEquals(35,intNode.getMin().intValue());
	    	assertEquals(20,act2.getEarliestStartTime());
	    	assertEquals(35,act2.getEarliestEndTime());
	    	
	    	assertEquals(75,intNode.getMax().intValue());
	    	assertEquals(60,act2.getLatestStartTime());
	    	assertEquals(75,act2.getLatestEndTime());
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	intNode.setMin(new Integer(42));
	    	intNode.setMax(new Integer(42));
	    	arc.propagate();
	    	
	    	assertEquals(42,intNode.getMin().intValue());
	    	assertEquals(27,act2.getEarliestStartTime());
	    	assertEquals(42,act2.getEarliestEndTime());
	    	
	    	assertEquals(42,intNode.getMax().intValue());
	    	assertEquals(27,act2.getLatestStartTime());
	    	assertEquals(42,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    }
	    catch(PropagationFailureException pfe) {
	    	fail();
	    }
    }
    
    public void testActivityToActivityNodeAfterEndWithDelay() {
	    try {
    		TemporalArc arc = new TemporalArc(intNode,act2,TemporalArc.CONSTANT,TemporalArc.END,TemporalArc.AFTER,5);
	    	
    		assertEquals(-50,intNode.getMin().intValue());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(150,intNode.getMax().intValue());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(115,act2.getLatestEndTime());
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	intNode.setMax(new Integer(85));
	    	arc.propagate();
	    	
	    	assertEquals(-50,intNode.getMin().intValue());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(85,intNode.getMax().intValue());
	    	assertEquals(64,act2.getLatestStartTime());
	    	assertEquals(79,act2.getLatestEndTime());
	    	
	    	assertEquals(3,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    	intNode.setMin(new Integer(42));
	    	intNode.setMax(new Integer(42));
	    	arc.propagate();
	    	
	    	assertEquals(42,intNode.getMin().intValue());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(15,act2.getEarliestEndTime());
	    	
	    	assertEquals(42,intNode.getMax().intValue());
	    	assertEquals(21,act2.getLatestStartTime());
	    	assertEquals(36,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act2.getAvailResourceCount(4));
	    	assertEquals(1,act2.getAvailResourceCount(5));
	    	
	    }
	    catch(PropagationFailureException pfe) {
	    	fail();
	    }
    }
    
}
