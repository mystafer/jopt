package jopt.csp.test.constraint.generics.constantGen;

import jopt.csp.CspSolver;
import jopt.csp.spi.arcalgorithm.variable.GenericDoubleConstant;
import jopt.csp.spi.arcalgorithm.variable.GenericIntConstant;
import jopt.csp.spi.util.GenericIndex;
import jopt.csp.spi.util.GenericIndexManager;
import jopt.csp.spi.util.IndexIterator;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.variable.CspVariableFactory;
import junit.framework.TestCase;

/**
 * Test mapping generic constraints to generic booleans
 *  
 * @author James Boerkoel
 */
public class GenericConstantTest extends TestCase {
    

	GenericIntConstant intTest1;
	GenericIntConstant intTest2;
	GenericIntConstant intTest3;
	GenericDoubleConstant doubTest1;
	GenericDoubleConstant doubTest2;
	GenericDoubleConstant doubTest3;
	CspSolver solver;
	CspVariableFactory varFactory;
	GenericIndex idxI;
	GenericIndex idxJ;
	GenericIndex idxK;
    
    public GenericConstantTest(java.lang.String testName) {
        super(testName);
    }

    public void setUp () {
        solver = CspSolver.createSolver();
        solver.setAutoPropagate(false);
        varFactory = solver.getVarFactory();
		idxI = new GenericIndex("i", 3);
		idxJ = new GenericIndex("j", 3);
		idxK = new GenericIndex("k", 3);
		intTest1 = new GenericIntConstant("GenInts1", new GenericIndex[]{idxI}, new int[]{2,6,5});
		intTest2 = new GenericIntConstant("GenInts2", new GenericIndex[]{idxI}, new int[]{2,0,3});
		intTest3 = new GenericIntConstant("GenInts3", new GenericIndex[]{idxI, idxJ}, new int[]{2,0,3,7,1,9,4,6,8});
		doubTest1 = new GenericDoubleConstant("GenDoubs1", new GenericIndex[]{idxI}, new double[]{4.3,1.6,5.5});
	}
    
    public void tearDown() {
    	intTest1 = null;
    	intTest2 = null;
    	intTest3 = null;
    	doubTest1 = null;
    	doubTest2 = null;
    	doubTest3 = null;
    	solver = null;
    	varFactory = null;
    	idxI = null;
    	idxJ = null;
    	idxK = null;
    }
    
    public void testGetMax() {
        assertEquals("The max should be 6",intTest1.getMax().doubleValue(), 6.0, .001);
    }
    public void testGetMin() {
        assertEquals("The min should be 2",intTest1.getMin().doubleValue(), 2.0, .001);
    }    
    
    public void testGetDoubleMax() {
        assertEquals("The max should be 5.5",doubTest1.getMax().doubleValue(), 5.5, .00000001);
    }
    
    public void testCorrectIndexing() {
        idxI.next();
        assertEquals("The first element should be 2",intTest1.getNumberForIndex(),new MutableNumber((int)2));
        idxI.next();
        assertEquals("The second element should be 6",intTest1.getNumberForIndex(),new MutableNumber((int)6));
        idxI.next();
        assertEquals("The third element should be 5",intTest1.getNumberForIndex(),new MutableNumber((int)5));  
    }
    
    public void testIndexManagerSingleIndexCombinationGeneration() {
        GenericIndexManager gim = new GenericIndexManager(new GenericIndex[]{idxI}, null, new GenericIndex[]{idxI}, null,false);
        IndexIterator idxIter = gim.iterator();
        idxIter.next();
        assertEquals("The first element of intTest1 should be 2",new MutableNumber((int)2),intTest1.getNumberForIndex());
        assertEquals("The first element of intTest2 should be 2",new MutableNumber((int)2),intTest2.getNumberForIndex());
        idxIter.next();
        assertEquals("The second element of intTest1 should be 6",new MutableNumber((int)6),intTest1.getNumberForIndex());
        assertEquals("The second element of intTest2 should be 0",new MutableNumber((int)0),intTest2.getNumberForIndex());
        idxIter.next();
        assertEquals("The second element of intTest1 should be 5",new MutableNumber((int)5),intTest1.getNumberForIndex());
        assertEquals("The second element of intTest2 should be 3",new MutableNumber((int)3),intTest2.getNumberForIndex());    
    }
    public void testIndexManagerOneCommonIndexCombinationGeneration() {
        GenericIndexManager gim = new GenericIndexManager(new GenericIndex[]{idxI}, new GenericIndex[]{idxI}, new GenericIndex[]{idxI, idxJ}, null,false);
        IndexIterator idxIter = gim.iterator();
        idxIter.next();
        assertEquals("The first element of intTest1 should be 2",new MutableNumber((int)2),intTest1.getNumberForIndex());
        assertEquals("The first element of intTest2 should be 2",new MutableNumber((int)2),intTest2.getNumberForIndex());
        assertEquals("The first element of intTest3 should be 2",new MutableNumber((int)2),intTest3.getNumberForIndex());
        //Notice only intTest3 should change for the first two, and the others change for every third,
        // giving us 9 different unique combinations
        idxIter.next();
        assertEquals("The first element of intTest1 should be 2",new MutableNumber((int)2),intTest1.getNumberForIndex());
        assertEquals("The first element of intTest2 should be 2",new MutableNumber((int)2),intTest2.getNumberForIndex());
        assertEquals("The second element of intTest3 should be 0",new MutableNumber((int)0),intTest3.getNumberForIndex());
        idxIter.next();
        assertEquals("The first element of intTest1 should be 2",new MutableNumber((int)2),intTest1.getNumberForIndex());
        assertEquals("The first element of intTest2 should be 2",new MutableNumber((int)2),intTest2.getNumberForIndex());
        assertEquals("The third element of intTest3 should be 3",new MutableNumber((int)3),intTest3.getNumberForIndex());        
        idxIter.next();
        assertEquals("The second element of intTest1 should be 6",new MutableNumber((int)6),intTest1.getNumberForIndex());
        assertEquals("The second element of intTest2 should be 0",new MutableNumber((int)0),intTest2.getNumberForIndex());
        assertEquals("The fourth element of intTest3 should be 7",new MutableNumber((int)7),intTest3.getNumberForIndex());        
        idxIter.next();
        assertEquals("The second element of intTest1 should be 6",new MutableNumber((int)6),intTest1.getNumberForIndex());
        assertEquals("The second element of intTest2 should be 0",new MutableNumber((int)0),intTest2.getNumberForIndex());
        assertEquals("The fifth element of intTest3 should be 1",new MutableNumber((int)1),intTest3.getNumberForIndex());
        idxIter.next();
        assertEquals("The second element of intTest1 should be 6",new MutableNumber((int)6),intTest1.getNumberForIndex());
        assertEquals("The second element of intTest2 should be 0",new MutableNumber((int)0),intTest2.getNumberForIndex());
        assertEquals("The sixth element of intTest3 should be 9",new MutableNumber((int)9),intTest3.getNumberForIndex());
        idxIter.next();
        assertEquals("The third element of intTest1 should be 5",new MutableNumber((int)5),intTest1.getNumberForIndex());
        assertEquals("The third element of intTest2 should be 3",new MutableNumber((int)3),intTest2.getNumberForIndex());
        assertEquals("The seventh element of intTest3 should be 4",new MutableNumber((int)4),intTest3.getNumberForIndex());
        idxIter.next();
        assertEquals("The third element of intTest1 should be 5",new MutableNumber((int)5),intTest1.getNumberForIndex());
        assertEquals("The third element of intTest2 should be 3",new MutableNumber((int)3),intTest2.getNumberForIndex());
        assertEquals("The seventh element of intTest3 should be 6",new MutableNumber((int)6),intTest3.getNumberForIndex());
        idxIter.next();
        assertEquals("The third element of intTest1 should be 5",new MutableNumber((int)5),intTest1.getNumberForIndex());
        assertEquals("The third element of intTest2 should be 3",new MutableNumber((int)3),intTest2.getNumberForIndex());
        assertEquals("The seventh element of intTest3 should be 8",new MutableNumber((int)8),intTest3.getNumberForIndex());        
    }
}