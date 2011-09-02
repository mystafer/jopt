package jopt.csp.test.trig;

import jopt.csp.CspSolver;
import jopt.csp.spi.arcalgorithm.constraint.num.CosConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.NumExpr;
import jopt.csp.spi.util.NumConstants;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.CspDoubleVariable;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

public class CosConstraintTest extends TestCase {
    private final static double FIVE_ROTATIONS = 10 * NumConstants.PI;
    
	private CspSolver solver;
    private CspVariableFactory varFactory;
    
    private CspDoubleVariable angle;
    private CspDoubleVariable cosAngle;
    
    private NumExpr angleExpr;
    private NumExpr cosAngleExpr;
    
    public void setUp() {
    	solver = CspSolver.createSolver();
        varFactory = solver.getVarFactory();
        
        angle = varFactory.doubleVar("angle", -FIVE_ROTATIONS, FIVE_ROTATIONS);
        cosAngle = varFactory.doubleVar("cosAngle", -2, 2);
        
        angleExpr = (NumExpr) angle;
        cosAngleExpr = (NumExpr) cosAngle;
    }
    
    public void tearDown() {
    	solver = null;
        varFactory = null;
        
        angle = null;
        cosAngle = null;
        
        angleExpr = null;
        cosAngleExpr = null;
    }
    
    public void testCosAngleAdjust() {
    	try {
    		// multiple rotations, no change to angle, but cos = -1 to 1
    		CspConstraint cnst = new CosConstraint(angleExpr, cosAngleExpr);
//    		solver.addConstraint(cnst);
//    		assertFalse("angle bound", angle.isBound());
//    		assertEquals("angle min", -FIVE_ROTATIONS, angle.getMin(), 0.0001);
//    		assertEquals("angle max", FIVE_ROTATIONS, angle.getMax(), 0.0001);
//    		assertFalse("cosAngle bound", cosAngle.isBound());
//    		assertEquals("cosAngle min", -1, cosAngle.getMin(), 0.0001);
//    		assertEquals("cosAngle max", 1, cosAngle.getMax(), 0.0001);
//    		
//    		// single rotation[0..2pi], no change to angle, but cos = -1 to 1
//    		solver.clear();
//    		solver.addVariable(angle);
//    		solver.addVariable(cosAngle);
//    		angle.setMin(0);
//    		angle.setMax(NumConstants.TWO_PI);
//    		cnst = new CosConstraint(angleExpr, cosAngleExpr);
//    		solver.addConstraint(cnst);
//    		assertFalse("angle bound", angle.isBound());
//    		assertEquals("angle min", 0, angle.getMin(), 0.0001);
//    		assertEquals("angle max", NumConstants.TWO_PI, angle.getMax(), 0.0001);
//    		assertFalse("cosAngle bound", cosAngle.isBound());
//    		assertEquals("cosAngle min", -1, cosAngle.getMin(), 0.0001);
//    		assertEquals("cosAngle max", 1, cosAngle.getMax(), 0.0001);
//    		
//    		// half rotation[0..pi], no change to angle, but cos = -1 to 1
//    		solver.clear();
//    		solver.addVariable(angle);
//    		solver.addVariable(cosAngle);
//    		angle.setMin(0);
//    		angle.setMax(NumConstants.PI);
//    		cnst = new CosConstraint(angleExpr, cosAngleExpr);
//    		solver.addConstraint(cnst);
//    		assertFalse("angle bound", angle.isBound());
//    		assertEquals("angle min", 0, angle.getMin(), 0.0001);
//    		assertEquals("angle max", NumConstants.PI, angle.getMax(), 0.0001);
//    		assertFalse("cosAngle bound", cosAngle.isBound());
//    		assertEquals("cosAngle min", -1, cosAngle.getMin(), 0.0001);
//    		assertEquals("cosAngle max", 1, cosAngle.getMax(), 0.0001);
//    		
//    		// half rotation[0.5*pi..1.5*pi], no change to angle, but cos = -1..0
//    		solver.clear();
//    		solver.addVariable(angle);
//    		solver.addVariable(cosAngle);
//    		angle.setMin(NumConstants.HALF_PI);
//    		angle.setMax(NumConstants.ONE_AND_HALF_PI);
//    		cnst = new CosConstraint(angleExpr, cosAngleExpr);
//    		solver.addConstraint(cnst);
//    		assertFalse("angle bound", angle.isBound());
//    		assertEquals("angle min", NumConstants.HALF_PI, angle.getMin(), 0.0001);
//    		assertEquals("angle max", NumConstants.ONE_AND_HALF_PI, angle.getMax(), 0.0001);
//    		assertFalse("cosAngle bound", cosAngle.isBound());
//    		assertEquals("cosAngle min", -1, cosAngle.getMin(), 0.0001);
//    		assertEquals("cosAngle max", 0, cosAngle.getMax(), 0.0001);
//    		
//    		// half rotation[pi..2pi], no change to angle, but cos = -1..1
//    		solver.clear();
//    		solver.addVariable(angle);
//    		solver.addVariable(cosAngle);
//    		angle.setMin(NumConstants.PI);
//    		angle.setMax(NumConstants.TWO_PI);
//    		cnst = new CosConstraint(angleExpr, cosAngleExpr);
//    		solver.addConstraint(cnst);
//    		assertFalse("angle bound", angle.isBound());
//    		assertEquals("angle min", NumConstants.PI, angle.getMin(), 0.0001);
//    		assertEquals("angle max", NumConstants.TWO_PI, angle.getMax(), 0.0001);
//    		assertFalse("cosAngle bound", cosAngle.isBound());
//    		assertEquals("cosAngle min", -1, cosAngle.getMin(), 0.0001);
//    		assertEquals("cosAngle max", 1, cosAngle.getMax(), 0.0001);
//    		
//    		// half rotation[1.5*pi..2.5*pi], no change to angle, but cos = 0..1
//    		solver.clear();
//    		solver.addVariable(angle);
//    		solver.addVariable(cosAngle);
//    		angle.setMin(NumConstants.ONE_AND_HALF_PI);
//    		angle.setMax(2.5 * NumConstants.PI);
//    		cnst = new CosConstraint(angleExpr, cosAngleExpr);
//    		solver.addConstraint(cnst);
//    		assertFalse("angle bound", angle.isBound());
//    		assertEquals("angle min", NumConstants.ONE_AND_HALF_PI, angle.getMin(), 0.0001);
//    		assertEquals("angle max", 2.5 * NumConstants.PI, angle.getMax(), 0.0001);
//    		assertFalse("cosAngle bound", cosAngle.isBound());
//    		assertEquals("cosAngle min", 0, cosAngle.getMin(), 0.0001);
//    		assertEquals("cosAngle max", 1, cosAngle.getMax(), 0.0001);
    		
    		// quarter rotation[0..0.5*pi], no change to angle, but cos = 0..1
    		solver.clear();
    		solver.addVariable(angle);
    		solver.addVariable(cosAngle);
    		angle.setMin(0);
    		angle.setMax(NumConstants.HALF_PI);
    		cnst = new CosConstraint(angleExpr, cosAngleExpr);
    		solver.addConstraint(cnst);
    		assertFalse("angle bound", angle.isBound());
    		assertEquals("angle min", 0, angle.getMin(), 0.0001);
    		assertEquals("angle max", NumConstants.HALF_PI, angle.getMax(), 0.0001);
    		assertFalse("cosAngle bound", cosAngle.isBound());
    		assertEquals("cosAngle min", 0, cosAngle.getMin(), 0.0001);
    		assertEquals("cosAngle max", 1, cosAngle.getMax(), 0.0001);
    		
    		// quarter rotation[0.5pi..pi], no change to angle, but cos = -1..0
    		solver.clear();
    		solver.addVariable(angle);
    		solver.addVariable(cosAngle);
    		angle.setMin(NumConstants.HALF_PI);
    		angle.setMax(NumConstants.PI);
    		cnst = new CosConstraint(angleExpr, cosAngleExpr);
    		solver.addConstraint(cnst);
    		assertFalse("angle bound", angle.isBound());
    		assertEquals("angle min", NumConstants.HALF_PI, angle.getMin(), 0.0001);
    		assertEquals("angle max", NumConstants.PI, angle.getMax(), 0.0001);
    		assertFalse("cosAngle bound", cosAngle.isBound());
    		assertEquals("cosAngle min", -1, cosAngle.getMin(), 0.0001);
    		assertEquals("cosAngle max", 0, cosAngle.getMax(), 0.0001);
    		
    		// quarter rotation[pi..1.5*pi], no change to angle, but cos = -1..0
    		solver.clear();
    		solver.addVariable(angle);
    		solver.addVariable(cosAngle);
    		angle.setMin(NumConstants.PI);
    		angle.setMax(NumConstants.ONE_AND_HALF_PI);
    		cnst = new CosConstraint(angleExpr, cosAngleExpr);
    		solver.addConstraint(cnst);
    		assertFalse("angle bound", angle.isBound());
    		assertEquals("angle min", NumConstants.PI, angle.getMin(), 0.0001);
    		assertEquals("angle max", NumConstants.ONE_AND_HALF_PI, angle.getMax(), 0.0001);
    		assertFalse("cosAngle bound", cosAngle.isBound());
    		assertEquals("cosAngle min", -1, cosAngle.getMin(), 0.0001);
    		assertEquals("cosAngle max", 0, cosAngle.getMax(), 0.0001);
    		
    		// quarter rotation[1.5*pi..2*pi], no change to angle, but cos = 0..1
    		solver.clear();
    		solver.addVariable(angle);
    		solver.addVariable(cosAngle);
    		angle.setMin(NumConstants.ONE_AND_HALF_PI);
    		angle.setMax(NumConstants.TWO_PI);
    		cnst = new CosConstraint(angleExpr, cosAngleExpr);
    		solver.addConstraint(cnst);
    		assertFalse("angle bound", angle.isBound());
    		assertEquals("angle min", NumConstants.ONE_AND_HALF_PI, angle.getMin(), 0.0001);
    		assertEquals("angle max", NumConstants.TWO_PI, angle.getMax(), 0.0001);
    		assertFalse("cosAngle bound", cosAngle.isBound());
    		assertEquals("cosAngle min", 0, cosAngle.getMin(), 0.0001);
    		assertEquals("cosAngle max", 1, cosAngle.getMax(), 0.0001);
    		
    		// quarter rotation[2*pi..2.5*pi], no change to angle, but cos = 0..1
    		solver.clear();
    		solver.addVariable(angle);
    		solver.addVariable(cosAngle);
    		angle.setMin(NumConstants.TWO_PI);
    		angle.setMax(5 * NumConstants.HALF_PI);
    		cnst = new CosConstraint(angleExpr, cosAngleExpr);
    		solver.addConstraint(cnst);
    		assertFalse("angle bound", angle.isBound());
    		assertEquals("angle min", NumConstants.TWO_PI, angle.getMin(), 0.0001);
    		assertEquals("angle max", 5 * NumConstants.HALF_PI, angle.getMax(), 0.0001);
    		assertFalse("cosAngle bound", cosAngle.isBound());
    		assertEquals("cosAngle min", 0, cosAngle.getMin(), 0.0001);
    		assertEquals("cosAngle max", 1, cosAngle.getMax(), 0.0001);
    		
    		// eigth rotation[0*pi..0.25*pi]
    		solver.clear();
    		solver.addVariable(angle);
    		solver.addVariable(cosAngle);
    		angle.setMin(0);
    		angle.setMax(0.25 * NumConstants.PI);
    		cnst = new CosConstraint(angleExpr, cosAngleExpr);
    		solver.addConstraint(cnst);
    		assertFalse("angle bound", angle.isBound());
    		assertEquals("angle min", 0, angle.getMin(), 0.0001);
    		assertEquals("angle max", 0.25 * NumConstants.PI, angle.getMax(), 0.0001);
    		assertFalse("cosAngle bound", cosAngle.isBound());
    		assertEquals("cosAngle min", Math.cos(0.25 * NumConstants.PI), cosAngle.getMin(), 0.0001);
    		assertEquals("cosAngle max", 1, cosAngle.getMax(), 0.0001);
    		
    		// eigth rotation[0.25*pi..0.5*pi]
    		solver.clear();
    		solver.addVariable(angle);
    		solver.addVariable(cosAngle);
    		angle.setMin(0.25 * NumConstants.PI);
    		angle.setMax(0.5 * NumConstants.PI);
    		cnst = new CosConstraint(angleExpr, cosAngleExpr);
    		solver.addConstraint(cnst);
    		assertFalse("angle bound", angle.isBound());
    		assertEquals("angle min", 0.25 * NumConstants.PI, angle.getMin(), 0.0001);
    		assertEquals("angle max", 0.5 * NumConstants.PI, angle.getMax(), 0.0001);
    		assertFalse("cosAngle bound", cosAngle.isBound());
    		assertEquals("cosAngle min", 0, cosAngle.getMin(), 0.0001);
    		assertEquals("cosAngle max", Math.cos(0.25 * NumConstants.PI), cosAngle.getMax(), 0.0001);
    		
    		// eigth rotation[0.5*pi..0.75*pi]
    		solver.clear();
    		solver.addVariable(angle);
    		solver.addVariable(cosAngle);
    		angle.setMin(0.5 * NumConstants.PI);
    		angle.setMax(0.75 * NumConstants.PI);
    		cnst = new CosConstraint(angleExpr, cosAngleExpr);
    		solver.addConstraint(cnst);
    		assertFalse("angle bound", angle.isBound());
    		assertEquals("angle min", 0.5 * NumConstants.PI, angle.getMin(), 0.0001);
    		assertEquals("angle max", 0.75 * NumConstants.PI, angle.getMax(), 0.0001);
    		assertFalse("cosAngle bound", cosAngle.isBound());
    		assertEquals("cosAngle min", Math.cos(0.75 * NumConstants.PI), cosAngle.getMin(), 0.0001);
    		assertEquals("cosAngle max", 0, cosAngle.getMax(), 0.0001);
    		
    		// eigth rotation[0.75*pi..pi]
    		solver.clear();
    		solver.addVariable(angle);
    		solver.addVariable(cosAngle);
    		angle.setMin(0.75 * NumConstants.PI);
    		angle.setMax(NumConstants.PI);
    		cnst = new CosConstraint(angleExpr, cosAngleExpr);
    		solver.addConstraint(cnst);
    		assertFalse("angle bound", angle.isBound());
    		assertEquals("angle min", 0.75 * NumConstants.PI, angle.getMin(), 0.0001);
    		assertEquals("angle max", NumConstants.PI, angle.getMax(), 0.0001);
    		assertFalse("cosAngle bound", cosAngle.isBound());
    		assertEquals("cosAngle min", -1, cosAngle.getMin(), 0.0001);
    		assertEquals("cosAngle max", Math.cos(0.75 * NumConstants.PI), cosAngle.getMax(), 0.0001);
    		
    		// eigth rotation[pi..1.25pi]
    		solver.clear();
    		solver.addVariable(angle);
    		solver.addVariable(cosAngle);
    		angle.setMin(NumConstants.PI);
    		angle.setMax(1.25 * NumConstants.PI);
    		cnst = new CosConstraint(angleExpr, cosAngleExpr);
    		solver.addConstraint(cnst);
    		assertFalse("angle bound", angle.isBound());
    		assertEquals("angle min", NumConstants.PI, angle.getMin(), 0.0001);
    		assertEquals("angle max", 1.25 * NumConstants.PI, angle.getMax(), 0.0001);
    		assertFalse("cosAngle bound", cosAngle.isBound());
    		assertEquals("cosAngle min", -1, cosAngle.getMin(), 0.0001);
    		assertEquals("cosAngle max", Math.cos(1.25 * NumConstants.PI), cosAngle.getMax(), 0.0001);
    		
    		// eigth rotation[1.25pi..1.5*pi]
    		solver.clear();
    		solver.addVariable(angle);
    		solver.addVariable(cosAngle);
    		angle.setMin(1.25 * NumConstants.PI);
    		angle.setMax(1.5 * NumConstants.PI);
    		cnst = new CosConstraint(angleExpr, cosAngleExpr);
    		solver.addConstraint(cnst);
    		assertFalse("angle bound", angle.isBound());
    		assertEquals("angle min", 1.25 * NumConstants.PI, angle.getMin(), 0.0001);
    		assertEquals("angle max", 1.5 * NumConstants.PI, angle.getMax(), 0.0001);
    		assertFalse("cosAngle bound", cosAngle.isBound());
    		assertEquals("cosAngle min", Math.cos(1.25 * NumConstants.PI), cosAngle.getMin(), 0.0001);
    		assertEquals("cosAngle max", 0, cosAngle.getMax(), 0.0001);
    		
    		// eigth rotation[1.5pi..1.75*pi]
    		solver.clear();
    		solver.addVariable(angle);
    		solver.addVariable(cosAngle);
    		angle.setMin(1.5 * NumConstants.PI);
    		angle.setMax(1.75 * NumConstants.PI);
    		cnst = new CosConstraint(angleExpr, cosAngleExpr);
    		solver.addConstraint(cnst);
    		assertFalse("angle bound", angle.isBound());
    		assertEquals("angle min", 1.5 * NumConstants.PI, angle.getMin(), 0.0001);
    		assertEquals("angle max", 1.75 * NumConstants.PI, angle.getMax(), 0.0001);
    		assertFalse("cosAngle bound", cosAngle.isBound());
    		assertEquals("cosAngle min", 0, cosAngle.getMin(), 0.0001);
    		assertEquals("cosAngle max", Math.cos(1.75 * NumConstants.PI), cosAngle.getMax(), 0.0001);
    		
    		// eigth rotation[1.75pi..2*pi]
    		solver.clear();
    		solver.addVariable(angle);
    		solver.addVariable(cosAngle);
    		angle.setMin(1.75 * NumConstants.PI);
    		angle.setMax(2 * NumConstants.PI);
    		cnst = new CosConstraint(angleExpr, cosAngleExpr);
    		solver.addConstraint(cnst);
    		assertFalse("angle bound", angle.isBound());
    		assertEquals("angle min", 1.75 * NumConstants.PI, angle.getMin(), 0.0001);
    		assertEquals("angle max", 2 * NumConstants.PI, angle.getMax(), 0.0001);
    		assertFalse("cosAngle bound", cosAngle.isBound());
    		assertEquals("cosAngle min", Math.cos(1.75 * NumConstants.PI), cosAngle.getMin(), 0.0001);
    		assertEquals("cosAngle max", 1, cosAngle.getMax(), 0.0001);
    		
    		// eigth rotation[2*pi..2.25*pi]
    		solver.clear();
    		solver.addVariable(angle);
    		solver.addVariable(cosAngle);
    		angle.setMin(2 * NumConstants.PI);
    		angle.setMax(2.25 * NumConstants.PI);
    		cnst = new CosConstraint(angleExpr, cosAngleExpr);
    		solver.addConstraint(cnst);
    		assertFalse("angle bound", angle.isBound());
    		assertEquals("angle min", 2 * NumConstants.PI, angle.getMin(), 0.0001);
    		assertEquals("angle max", 2.25 * NumConstants.PI, angle.getMax(), 0.0001);
    		assertFalse("cosAngle bound", cosAngle.isBound());
    		assertEquals("cosAngle min", Math.cos(2.25 * NumConstants.PI), cosAngle.getMin(), 0.0001);
    		assertEquals("cosAngle max", 1, cosAngle.getMax(), 0.0001);
    		
    		// angle bound [0]
    		solver.clear();
    		solver.addVariable(angle);
    		solver.addVariable(cosAngle);
    		angle.setMin(0);
    		angle.setMax(0);
    		cnst = new CosConstraint(angleExpr, cosAngleExpr);
    		solver.addConstraint(cnst);
    		assertTrue("angle bound", angle.isBound());
    		assertEquals("angle min", 0, angle.getMin(), 0.0001);
    		assertEquals("angle max", 0, angle.getMax(), 0.0001);
    		assertTrue("cosAngle bound", cosAngle.isBound());
    		assertEquals("cosAngle min", 1, cosAngle.getMin(), 0.0001);
    		assertEquals("cosAngle max", 1, cosAngle.getMax(), 0.0001);
    		
    		// angle bound [0.25*pi]
    		solver.clear();
    		solver.addVariable(angle);
    		solver.addVariable(cosAngle);
    		angle.setMin(0.25 * NumConstants.PI);
    		angle.setMax(0.25 * NumConstants.PI);
    		cnst = new CosConstraint(angleExpr, cosAngleExpr);
    		solver.addConstraint(cnst);
    		assertTrue("angle bound", angle.isBound());
    		assertEquals("angle min", 0.25 * NumConstants.PI, angle.getMin(), 0.0001);
    		assertEquals("angle max", 0.25 * NumConstants.PI, angle.getMax(), 0.0001);
    		assertTrue("cosAngle bound", cosAngle.isBound());
    		assertEquals("cosAngle min", Math.cos(0.25 * NumConstants.PI), cosAngle.getMin(), 0.0001);
    		assertEquals("cosAngle max", Math.cos(0.25 * NumConstants.PI), cosAngle.getMax(), 0.0001);
    		
    		// angle bound [0.5*pi]
    		solver.clear();
    		solver.addVariable(angle);
    		solver.addVariable(cosAngle);
    		angle.setMin(0.5 * NumConstants.PI);
    		angle.setMax(0.5 * NumConstants.PI);
    		cnst = new CosConstraint(angleExpr, cosAngleExpr);
    		solver.addConstraint(cnst);
    		assertTrue("angle bound", angle.isBound());
    		assertEquals("angle min", 0.5 * NumConstants.PI, angle.getMin(), 0.0001);
    		assertEquals("angle max", 0.5 * NumConstants.PI, angle.getMax(), 0.0001);
    		assertTrue("cosAngle bound", cosAngle.isBound());
    		assertEquals("cosAngle min", 0, cosAngle.getMin(), 0.0001);
    		assertEquals("cosAngle max", 0, cosAngle.getMax(), 0.0001);
    		
    		// angle bound [0.75*pi]
    		solver.clear();
    		solver.addVariable(angle);
    		solver.addVariable(cosAngle);
    		angle.setMin(0.75 * NumConstants.PI);
    		angle.setMax(0.75 * NumConstants.PI);
    		cnst = new CosConstraint(angleExpr, cosAngleExpr);
    		solver.addConstraint(cnst);
    		assertTrue("angle bound", angle.isBound());
    		assertEquals("angle min", 0.75 * NumConstants.PI, angle.getMin(), 0.0001);
    		assertEquals("angle max", 0.75 * NumConstants.PI, angle.getMax(), 0.0001);
    		assertTrue("cosAngle bound", cosAngle.isBound());
    		assertEquals("cosAngle min", Math.cos(0.75 * NumConstants.PI), cosAngle.getMin(), 0.0001);
    		assertEquals("cosAngle max", Math.cos(0.75 * NumConstants.PI), cosAngle.getMax(), 0.0001);
    		
    		// angle bound [pi]
    		solver.clear();
    		solver.addVariable(angle);
    		solver.addVariable(cosAngle);
    		angle.setMin(NumConstants.PI);
    		angle.setMax(NumConstants.PI);
    		cnst = new CosConstraint(angleExpr, cosAngleExpr);
    		solver.addConstraint(cnst);
    		assertTrue("angle bound", angle.isBound());
    		assertEquals("angle min", NumConstants.PI, angle.getMin(), 0.0001);
    		assertEquals("angle max", NumConstants.PI, angle.getMax(), 0.0001);
    		assertTrue("cosAngle bound", cosAngle.isBound());
    		assertEquals("cosAngle min", -1, cosAngle.getMin(), 0.0001);
    		assertEquals("cosAngle max", -1, cosAngle.getMax(), 0.0001);
    		
    		// angle bound [1.25*pi]
    		solver.clear();
    		solver.addVariable(angle);
    		solver.addVariable(cosAngle);
    		angle.setMin(1.25 * NumConstants.PI);
    		angle.setMax(1.25 * NumConstants.PI);
    		solver.addVariable(angle);
    		solver.addVariable(cosAngle);
    		cnst = new CosConstraint(angleExpr, cosAngleExpr);
    		solver.addConstraint(cnst);
    		assertTrue("angle bound", angle.isBound());
    		assertEquals("angle min", 1.25 * NumConstants.PI, angle.getMin(), 0.0001);
    		assertEquals("angle max", 1.25 * NumConstants.PI, angle.getMax(), 0.0001);
    		assertTrue("cosAngle bound", cosAngle.isBound());
    		assertEquals("cosAngle min", Math.cos(1.25 * NumConstants.PI), cosAngle.getMin(), 0.0001);
    		assertEquals("cosAngle max", Math.cos(1.25 * NumConstants.PI), cosAngle.getMax(), 0.0001);
    		
    		// angle bound [1.5*pi]
    		solver.clear();
    		solver.addVariable(angle);
    		solver.addVariable(cosAngle);
    		angle.setMin(1.5 * NumConstants.PI);
    		angle.setMax(1.5 * NumConstants.PI);
    		cnst = new CosConstraint(angleExpr, cosAngleExpr);
    		solver.addConstraint(cnst);
    		assertTrue("angle bound", angle.isBound());
    		assertEquals("angle min", 1.5 * NumConstants.PI, angle.getMin(), 0.0001);
    		assertEquals("angle max", 1.5 * NumConstants.PI, angle.getMax(), 0.0001);
    		assertTrue("cosAngle bound", cosAngle.isBound());
    		assertEquals("cosAngle min", 0, cosAngle.getMin(), 0.0001);
    		assertEquals("cosAngle max", 0, cosAngle.getMax(), 0.0001);
    		
    		// angle bound [1.75*pi]
    		solver.clear();
    		solver.addVariable(angle);
    		solver.addVariable(cosAngle);
    		angle.setMin(1.75 * NumConstants.PI);
    		angle.setMax(1.75 * NumConstants.PI);
    		cnst = new CosConstraint(angleExpr, cosAngleExpr);
    		solver.addConstraint(cnst);
    		assertTrue("angle bound", angle.isBound());
    		assertEquals("angle min", 1.75 * NumConstants.PI, angle.getMin(), 0.0001);
    		assertEquals("angle max", 1.75 * NumConstants.PI, angle.getMax(), 0.0001);
    		assertTrue("cosAngle bound", cosAngle.isBound());
    		assertEquals("cosAngle min", Math.cos(1.75 * NumConstants.PI), cosAngle.getMin(), 0.0001);
    		assertEquals("cosAngle max", Math.cos(1.75 * NumConstants.PI), cosAngle.getMax(), 0.0001);
    		
    		// angle bound [2*pi]
    		solver.clear();
    		solver.addVariable(angle);
    		solver.addVariable(cosAngle);
    		angle.setMin(2 * NumConstants.PI);
    		angle.setMax(2 * NumConstants.PI);
    		cnst = new CosConstraint(angleExpr, cosAngleExpr);
    		solver.addConstraint(cnst);
    		assertTrue("angle bound", angle.isBound());
    		assertEquals("angle min", 2 * NumConstants.PI, angle.getMin(), 0.0001);
    		assertEquals("angle max", 2 * NumConstants.PI, angle.getMax(), 0.0001);
    		assertTrue("cosAngle bound", cosAngle.isBound());
    		assertEquals("cosAngle min", 1, cosAngle.getMin(), 0.0001);
    		assertEquals("cosAngle max", 1, cosAngle.getMax(), 0.0001);
    		
    		// angle bound [2.25*pi]
    		solver.clear();
    		solver.addVariable(angle);
    		solver.addVariable(cosAngle);
    		angle.setMin(2.25 * NumConstants.PI);
    		angle.setMax(2.25 * NumConstants.PI);
    		cnst = new CosConstraint(angleExpr, cosAngleExpr);
    		solver.addConstraint(cnst);
    		assertTrue("angle bound", angle.isBound());
    		assertEquals("angle min", 2.25 * NumConstants.PI, angle.getMin(), 0.0001);
    		assertEquals("angle max", 2.25 * NumConstants.PI, angle.getMax(), 0.0001);
    		assertTrue("cosAngle bound", cosAngle.isBound());
    		assertEquals("cosAngle min", Math.cos(2.25 * NumConstants.PI), cosAngle.getMin(), 0.0001);
    		assertEquals("cosAngle max", Math.cos(2.25 * NumConstants.PI), cosAngle.getMax(), 0.0001);
    	}
    	catch(PropagationFailureException propx) {
    		fail();
    	}
    }

    public void testCosResultAdjust() {
    	try {
            // full rotation[0..2pi]
            solver.clear();
            solver.addVariable(angle);
            solver.addVariable(cosAngle);
            cosAngle.setMin(-1);
            cosAngle.setMax(1);
            CspConstraint cnst = new CosConstraint(angleExpr, cosAngleExpr);
            solver.addConstraint(cnst);
            assertFalse("cosAngle bound", cosAngle.isBound());
            assertEquals("cosAngle min", -1, cosAngle.getMin(), 0.0001);
            assertEquals("cosAngle max", 1, cosAngle.getMax(), 0.0001);
            assertFalse("angle bound", angle.isBound());
            assertEquals("angle min", -FIVE_ROTATIONS, angle.getMin(), 0.0001);
            assertEquals("angle max", FIVE_ROTATIONS, angle.getMax(), 0.0001);
            
            // move[cos(0)..cos(0.25pi)]
            solver.clear();
            solver.addVariable(angle);
            solver.addVariable(cosAngle);
            cosAngle.setMin(Math.cos(0.25 * NumConstants.PI));
            cosAngle.setMax(1);
            cnst = new CosConstraint(angleExpr, cosAngleExpr);
            solver.addConstraint(cnst);
            assertFalse("cosAngle bound", cosAngle.isBound());
            assertEquals("cosAngle min", Math.cos(0.25 * NumConstants.PI), cosAngle.getMin(), 0.0001);
            assertEquals("cosAngle max", 1, cosAngle.getMax(), 0.0001);
            assertFalse("angle bound", angle.isBound());
            assertEquals("angle min", -FIVE_ROTATIONS, angle.getMin(), 0.0001);
            assertEquals("angle max", FIVE_ROTATIONS, angle.getMax(), 0.0001);
            
            // move[cos(0.25pi)..cos(0.5pi)]
            solver.clear();
            solver.addVariable(angle);
            solver.addVariable(cosAngle);
            cosAngle.setMin(0);
            cosAngle.setMax(Math.cos(0.25 * NumConstants.PI));
            cnst = new CosConstraint(angleExpr, cosAngleExpr);
            solver.addConstraint(cnst);
            assertFalse("cosAngle bound", cosAngle.isBound());
            assertEquals("cosAngle min", 0, cosAngle.getMin(), 0.0001);
            assertEquals("cosAngle max", Math.cos(0.25 * NumConstants.PI), cosAngle.getMax(), 0.0001);
            assertFalse("angle bound", angle.isBound());
            assertEquals("angle min", -FIVE_ROTATIONS + 0.25 * NumConstants.PI, angle.getMin(), 0.0001);
            assertEquals("angle max", FIVE_ROTATIONS - 0.25 * NumConstants.PI, angle.getMax(), 0.0001);
            
            // move[cos(0.5pi)..cos(0.75pi)]
            solver.clear();
            solver.addVariable(angle);
            solver.addVariable(cosAngle);
            cosAngle.setMin(Math.cos(0.75 * NumConstants.PI));
            cosAngle.setMax(0);
            cnst = new CosConstraint(angleExpr, cosAngleExpr);
            solver.addConstraint(cnst);
            assertFalse("cosAngle bound", cosAngle.isBound());
            assertEquals("cosAngle min", Math.cos(0.75 * NumConstants.PI), cosAngle.getMin(), 0.0001);
            assertEquals("cosAngle max", 0, cosAngle.getMax(), 0.0001);
            assertFalse("angle bound", angle.isBound());
            assertEquals("angle min", -FIVE_ROTATIONS + 0.5 * NumConstants.PI, angle.getMin(), 0.0001);
            assertEquals("angle max", FIVE_ROTATIONS - 0.5 * NumConstants.PI, angle.getMax(), 0.0001);
            
            // move[cos(0.75pi)..cos(pi)]
            solver.clear();
            solver.addVariable(angle);
            solver.addVariable(cosAngle);
            cosAngle.setMin(-1);
            cosAngle.setMax(Math.cos(0.75 * NumConstants.PI));
            cnst = new CosConstraint(angleExpr, cosAngleExpr);
            solver.addConstraint(cnst);
            assertFalse("cosAngle bound", cosAngle.isBound());
            assertEquals("cosAngle min", -1, cosAngle.getMin(), 0.0001);
            assertEquals("cosAngle max", Math.cos(0.75 * NumConstants.PI), cosAngle.getMax(), 0.0001);
            assertFalse("angle bound", angle.isBound());
            assertEquals("angle min", -FIVE_ROTATIONS + 0.75 * NumConstants.PI, angle.getMin(), 0.0001);
            assertEquals("angle max", FIVE_ROTATIONS - 0.75 * NumConstants.PI, angle.getMax(), 0.0001);
            
            // move[cos(pi)..cos(1.25pi)]
            solver.clear();
            solver.addVariable(angle);
            solver.addVariable(cosAngle);
            cosAngle.setMin(-1);
            cosAngle.setMax(Math.cos(1.25 * NumConstants.PI));
            cnst = new CosConstraint(angleExpr, cosAngleExpr);
            solver.addConstraint(cnst);
            assertFalse("cosAngle bound", cosAngle.isBound());
            assertEquals("cosAngle min", -1, cosAngle.getMin(), 0.0001);
            assertEquals("cosAngle max", Math.cos(1.25 * NumConstants.PI), cosAngle.getMax(), 0.0001);
            assertFalse("angle bound", angle.isBound());
            assertEquals("angle min", -FIVE_ROTATIONS + 0.75 * NumConstants.PI, angle.getMin(), 0.0001);
            assertEquals("angle max", FIVE_ROTATIONS - 0.75 * NumConstants.PI, angle.getMax(), 0.0001);
            
            // move[cos(1.25pi)..cos(1.5pi)]
            solver.clear();
            solver.addVariable(angle);
            solver.addVariable(cosAngle);
            cosAngle.setMin(Math.cos(1.25 * NumConstants.PI));
            cosAngle.setMax(0);
            cnst = new CosConstraint(angleExpr, cosAngleExpr);
            solver.addConstraint(cnst);
            assertFalse("cosAngle bound", cosAngle.isBound());
            assertEquals("cosAngle min", Math.cos(1.25 * NumConstants.PI), cosAngle.getMin(), 0.0001);
            assertEquals("cosAngle max", 0, cosAngle.getMax(), 0.0001);
            assertFalse("angle bound", angle.isBound());
            assertEquals("angle min", -FIVE_ROTATIONS + 0.5 * NumConstants.PI, angle.getMin(), 0.0001);
            assertEquals("angle max", FIVE_ROTATIONS - 0.5 * NumConstants.PI, angle.getMax(), 0.0001);
            
            // move[cos(1.5pi)..cos(1.75pi)]
            solver.clear();
            solver.addVariable(angle);
            solver.addVariable(cosAngle);
            cosAngle.setMin(0);
            cosAngle.setMax(Math.cos(1.75 * NumConstants.PI));
            cnst = new CosConstraint(angleExpr, cosAngleExpr);
            solver.addConstraint(cnst);
            assertFalse("cosAngle bound", cosAngle.isBound());
            assertEquals("cosAngle min", 0, cosAngle.getMin(), 0.0001);
            assertEquals("cosAngle max", Math.cos(1.75 * NumConstants.PI), cosAngle.getMax(), 0.0001);
            assertFalse("angle bound", angle.isBound());
            assertEquals("angle min", -FIVE_ROTATIONS + 0.25 * NumConstants.PI, angle.getMin(), 0.0001);
            assertEquals("angle max", FIVE_ROTATIONS - 0.25 * NumConstants.PI, angle.getMax(), 0.0001);
            
            // move[cos(1.75pi)..cos(2pi)]
            solver.clear();
            solver.addVariable(angle);
            solver.addVariable(cosAngle);
            cosAngle.setMin(Math.cos(1.75 * NumConstants.PI));
            cosAngle.setMax(1);
            cnst = new CosConstraint(angleExpr, cosAngleExpr);
            solver.addConstraint(cnst);
            assertFalse("cosAngle bound", cosAngle.isBound());
            assertEquals("cosAngle min", Math.cos(1.75 * NumConstants.PI), cosAngle.getMin(), 0.0001);
            assertEquals("cosAngle max", 1, cosAngle.getMax(), 0.0001);
            assertFalse("angle bound", angle.isBound());
            assertEquals("angle min", -FIVE_ROTATIONS, angle.getMin(), 0.0001);
            assertEquals("angle max", FIVE_ROTATIONS, angle.getMax(), 0.0001);
            
            // move[cos(2pi)..cos(2.25pi)]
            solver.clear();
            solver.addVariable(angle);
            solver.addVariable(cosAngle);
            cosAngle.setMin(Math.cos(2.25 * NumConstants.PI));
            cosAngle.setMax(1);
            cnst = new CosConstraint(angleExpr, cosAngleExpr);
            solver.addConstraint(cnst);
            assertFalse("cosAngle bound", cosAngle.isBound());
            assertEquals("cosAngle min", Math.cos(2.25 * NumConstants.PI), cosAngle.getMin(), 0.0001);
            assertEquals("cosAngle max", 1, cosAngle.getMax(), 0.0001);
            assertFalse("angle bound", angle.isBound());
            assertEquals("angle min", -FIVE_ROTATIONS, angle.getMin(), 0.0001);
            assertEquals("angle max", FIVE_ROTATIONS, angle.getMax(), 0.0001);
            
            // move[cos(2.25pi)..cos(2.5pi)]
            solver.clear();
            solver.addVariable(angle);
            solver.addVariable(cosAngle);
            cosAngle.setMin(Math.cos(2.5 * NumConstants.PI));
            cosAngle.setMax(Math.cos(2.25 * NumConstants.PI));
            cnst = new CosConstraint(angleExpr, cosAngleExpr);
            solver.addConstraint(cnst);
            assertFalse("cosAngle bound", cosAngle.isBound());
            assertEquals("cosAngle min", Math.cos(2.5 * NumConstants.PI), cosAngle.getMin(), 0.0001);
            assertEquals("cosAngle max", Math.cos(2.25 * NumConstants.PI), cosAngle.getMax(), 0.0001);
            assertFalse("angle bound", angle.isBound());
            assertEquals("angle min", -FIVE_ROTATIONS + 0.25 * NumConstants.PI, angle.getMin(), 0.0001);
            assertEquals("angle max", FIVE_ROTATIONS - 0.25 * NumConstants.PI, angle.getMax(), 0.0001);
    	}
    	catch(PropagationFailureException propx) {
            propx.printStackTrace();
    		fail();
    	}
    }
    
    public void testCosStrattleAngleAdjust() {
        try {
            // [-0.25pi..0.25pi]
            solver.clear();
            solver.addVariable(angle);
            solver.addVariable(cosAngle);
            angle.setMin(-0.25 * NumConstants.PI);
            angle.setMax(0.25 * NumConstants.PI);
            CspConstraint cnst = new CosConstraint(angleExpr, cosAngleExpr);
            solver.addConstraint(cnst);
            assertFalse("cosAngle bound", cosAngle.isBound());
            assertEquals("cosAngle min", Math.cos(0.25 * NumConstants.PI), cosAngle.getMin(), 0.0001);
            assertEquals("cosAngle max", 1, cosAngle.getMax(), 0.0001);
            assertFalse("angle bound", angle.isBound());
            assertEquals("angle min", -0.25 * NumConstants.PI, angle.getMin(), 0.0001);
            assertEquals("angle max", 0.25 * NumConstants.PI, angle.getMax(), 0.0001);
            
            // [0.25pi..0.75pi]
            solver.clear();
            solver.addVariable(angle);
            solver.addVariable(cosAngle);
            angle.setMin(0.25 * NumConstants.PI);
            angle.setMax(0.75 * NumConstants.PI);
            cnst = new CosConstraint(angleExpr, cosAngleExpr);
            solver.addConstraint(cnst);
            assertFalse("cosAngle bound", cosAngle.isBound());
            assertEquals("cosAngle min", Math.cos(0.75 * NumConstants.PI), cosAngle.getMin(), 0.0001);
            assertEquals("cosAngle max", Math.cos(0.25 * NumConstants.PI), cosAngle.getMax(), 0.0001);
            assertFalse("angle bound", angle.isBound());
            assertEquals("angle min", 0.25 * NumConstants.PI, angle.getMin(), 0.0001);
            assertEquals("angle max", 0.75 * NumConstants.PI, angle.getMax(), 0.0001);
            
            // [0.75pi..1.25pi]
            solver.clear();
            solver.addVariable(angle);
            solver.addVariable(cosAngle);
            angle.setMin(0.75 * NumConstants.PI);
            angle.setMax(1.25 * NumConstants.PI);
            cnst = new CosConstraint(angleExpr, cosAngleExpr);
            solver.addConstraint(cnst);
            assertFalse("cosAngle bound", cosAngle.isBound());
            assertEquals("cosAngle min", -1, cosAngle.getMin(), 0.0001);
            assertEquals("cosAngle max", Math.cos(1.25 * NumConstants.PI), cosAngle.getMax(), 0.0001);
            assertFalse("angle bound", angle.isBound());
            assertEquals("angle min", 0.75 * NumConstants.PI, angle.getMin(), 0.0001);
            assertEquals("angle max", 1.25 * NumConstants.PI, angle.getMax(), 0.0001);
            
            // [1.25pi..1.75pi]
            solver.clear();
            solver.addVariable(angle);
            solver.addVariable(cosAngle);
            angle.setMin(1.25 * NumConstants.PI);
            angle.setMax(1.75 * NumConstants.PI);
            cnst = new CosConstraint(angleExpr, cosAngleExpr);
            solver.addConstraint(cnst);
            assertFalse("cosAngle bound", cosAngle.isBound());
            assertEquals("cosAngle min", Math.cos(1.25 * NumConstants.PI), cosAngle.getMin(), 0.0001);
            assertEquals("cosAngle max", Math.cos(1.75 * NumConstants.PI), cosAngle.getMax(), 0.0001);
            assertFalse("angle bound", angle.isBound());
            assertEquals("angle min", 1.25 * NumConstants.PI, angle.getMin(), 0.0001);
            assertEquals("angle max", 1.75 * NumConstants.PI, angle.getMax(), 0.0001);
            
            // [1.75pi..2.25pi]
            solver.clear();
            solver.addVariable(angle);
            solver.addVariable(cosAngle);
            angle.setMin(1.75 * NumConstants.PI);
            angle.setMax(2.25 * NumConstants.PI);
            cnst = new CosConstraint(angleExpr, cosAngleExpr);
            solver.addConstraint(cnst);
            assertFalse("cosAngle bound", cosAngle.isBound());
            assertEquals("cosAngle min", Math.cos(1.75 * NumConstants.PI), cosAngle.getMin(), 0.0001);
            assertEquals("cosAngle max", 1, cosAngle.getMax(), 0.0001);
            assertFalse("angle bound", angle.isBound());
            assertEquals("angle min", 1.75 * NumConstants.PI, angle.getMin(), 0.0001);
            assertEquals("angle max", 2.25 * NumConstants.PI, angle.getMax(), 0.0001);
        }
        catch(PropagationFailureException propx) {
            propx.printStackTrace();
            fail();
        }
    }

    public void testCosStrattleResultAdjust() {
        try {
            // [-0.25pi..0.25pi]
            solver.clear();
            solver.addVariable(angle);
            solver.addVariable(cosAngle);
            cosAngle.setMin(Math.cos(-0.25 * NumConstants.PI));
            cosAngle.setMax(Math.cos(0.25 * NumConstants.PI));
            CspConstraint cnst = new CosConstraint(angleExpr, cosAngleExpr);
            solver.addConstraint(cnst);
            assertTrue("cosAngle bound", cosAngle.isBound());
            assertEquals("cosAngle min", Math.cos(-0.25 * NumConstants.PI), cosAngle.getMin(), 0.0001);
            assertEquals("cosAngle max", Math.cos(0.25 * NumConstants.PI), cosAngle.getMax(), 0.0001);
            assertFalse("angle bound", angle.isBound());
            assertEquals("angle min", -FIVE_ROTATIONS + 0.25 * NumConstants.PI, angle.getMin(), 0.0001);
            assertEquals("angle max", FIVE_ROTATIONS - 0.25 * NumConstants.PI, angle.getMax(), 0.0001);
            
            // [0.25pi..0.75pi]
            solver.clear();
            solver.addVariable(angle);
            solver.addVariable(cosAngle);
            cosAngle.setMin(Math.cos(0.75 * NumConstants.PI));
            cosAngle.setMax(Math.cos(0.25 * NumConstants.PI));
            cnst = new CosConstraint(angleExpr, cosAngleExpr);
            solver.addConstraint(cnst);
            assertFalse("cosAngle bound", cosAngle.isBound());
            assertEquals("cosAngle min", Math.cos(0.75 * NumConstants.PI), cosAngle.getMin(), 0.0001);
            assertEquals("cosAngle max", Math.cos(0.25 * NumConstants.PI), cosAngle.getMax(), 0.0001);
            assertFalse("angle bound", angle.isBound());
            assertEquals("angle min", -FIVE_ROTATIONS + 0.25 * NumConstants.PI, angle.getMin(), 0.0001);
            assertEquals("angle max", FIVE_ROTATIONS - 0.25 * NumConstants.PI, angle.getMax(), 0.0001);
            
            // [0.75pi..1.25pi]
            solver.clear();
            solver.addVariable(angle);
            solver.addVariable(cosAngle);
            cosAngle.setMin(Math.cos(0.75d * NumConstants.PI));
            cosAngle.setMax(Math.cos(1.25d * NumConstants.PI));
            cnst = new CosConstraint(angleExpr, cosAngleExpr);
            solver.addConstraint(cnst);
            assertEquals("cosAngle min", Math.cos(0.75 * NumConstants.PI), cosAngle.getMin(), 0.0001);
            assertEquals("cosAngle max", Math.cos(1.25 * NumConstants.PI), cosAngle.getMax(), 0.0001);
            assertFalse("angle bound", angle.isBound());
            assertEquals("angle min", -FIVE_ROTATIONS + 0.75 * NumConstants.PI, angle.getMin(), 0.0001);
            assertEquals("angle max", FIVE_ROTATIONS - 0.75 * NumConstants.PI, angle.getMax(), 0.0001);
            
            // [1.25pi..1.75pi]
            solver.clear();
            solver.addVariable(angle);
            solver.addVariable(cosAngle);
            cosAngle.setMin(Math.cos(1.25 * NumConstants.PI));
            cosAngle.setMax(Math.cos(1.75 * NumConstants.PI));
            cnst = new CosConstraint(angleExpr, cosAngleExpr);
            solver.addConstraint(cnst);
            assertFalse("cosAngle bound", cosAngle.isBound());
            assertEquals("cosAngle min", Math.cos(1.25 * NumConstants.PI), cosAngle.getMin(), 0.0001);
            assertEquals("cosAngle max", Math.cos(1.75 * NumConstants.PI), cosAngle.getMax(), 0.0001);
            assertFalse("angle bound", angle.isBound());
            assertEquals("angle min", -FIVE_ROTATIONS + 0.25 * NumConstants.PI, angle.getMin(), 0.0001);
            assertEquals("angle max", FIVE_ROTATIONS - 0.25 * NumConstants.PI, angle.getMax(), 0.0001);
            
            // [1.75pi..2.25pi]
            solver.clear();
            solver.addVariable(angle);
            solver.addVariable(cosAngle);
            cosAngle.setMin(Math.cos(1.75 * NumConstants.PI));
            cosAngle.setMax(Math.cos(2.25 * NumConstants.PI));
            cnst = new CosConstraint(angleExpr, cosAngleExpr);
            solver.addConstraint(cnst);
            assertEquals("cosAngle min", Math.cos(1.75 * NumConstants.PI), cosAngle.getMin(), 0.0001);
            assertEquals("cosAngle max", Math.cos(2.25 * NumConstants.PI), cosAngle.getMax(), 0.0001);
            assertFalse("angle bound", angle.isBound());
            assertEquals("angle min", -FIVE_ROTATIONS + 0.25 * NumConstants.PI, angle.getMin(), 0.0001);
            assertEquals("angle max", FIVE_ROTATIONS - 0.25 * NumConstants.PI, angle.getMax(), 0.0001);
        }
        catch(PropagationFailureException propx) {
            propx.printStackTrace();
            fail();
        }
    }
}
