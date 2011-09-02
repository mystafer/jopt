package jopt.csp.test.trig;

import jopt.csp.CspSolver;
import jopt.csp.spi.arcalgorithm.constraint.num.CosConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.NumExpr;
import jopt.csp.spi.arcalgorithm.constraint.num.SinConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.TanConstraint;
import jopt.csp.spi.util.NumConstants;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.CspDoubleVariable;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

public class TanConstraintTest extends TestCase {
	private final static double FIVE_ROTATIONS = 10 * NumConstants.PI;
	
	private CspSolver solver;
	private CspVariableFactory varFactory;
	
	private CspDoubleVariable angle;
	private CspDoubleVariable tanAngle;
	private CspDoubleVariable cosAngle;
	private CspDoubleVariable sinAngle;
	
	private NumExpr angleExpr;
	private NumExpr tanAngleExpr;
	private NumExpr cosAngleExpr;
	private NumExpr sinAngleExpr;
	
	public void setUp() {
		solver = CspSolver.createSolver();
		varFactory = solver.getVarFactory();
		
		angle = varFactory.doubleVar("angle", -FIVE_ROTATIONS, FIVE_ROTATIONS);
		angleExpr = (NumExpr) angle;
		
		tanAngle = varFactory.doubleVar("tanAngle", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
		tanAngleExpr = (NumExpr) tanAngle;
		
		cosAngle = varFactory.doubleVar("cosAngle", -2, 2);
		cosAngleExpr = (NumExpr) cosAngle;
		
		sinAngle = varFactory.doubleVar("sinAngle", -2, 2);
		sinAngleExpr = (NumExpr) sinAngle;
	}
	
	public void tearDown() {
		solver = null;
		varFactory = null;
		
		angle = null;
		tanAngle = null;
		cosAngle = null;
		sinAngle = null;
		
		angleExpr = null;
		tanAngleExpr = null;
		cosAngleExpr = null;
		sinAngleExpr = null;
	}
	
	public void testTanAngleAdjust() {
		CspConstraint cnst;
		
		try {
			// multiple rotations, no change to angle, but tan = -Inf to Inf
			cnst = new TanConstraint(angleExpr, tanAngleExpr);
			solver.addConstraint(cnst);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", -FIVE_ROTATIONS, angle.getMin(), 0.0001);
			assertEquals("angle max", FIVE_ROTATIONS, angle.getMax(), 0.0001);
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Double.NEGATIVE_INFINITY, tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Double.POSITIVE_INFINITY, tanAngle.getMax(), 0.0001);
			
			// single rotation[0..2pi], no change to angle, but tan = -Inf to Inf
			solver.clear();
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			angle.setMin(0);
			angle.setMax(NumConstants.TWO_PI);
			cnst = new TanConstraint(angleExpr, tanAngleExpr);
			solver.addConstraint(cnst);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", 0, angle.getMin(), 0.0001);
			assertEquals("angle max", NumConstants.TWO_PI, angle.getMax(), 0.0001);
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Double.NEGATIVE_INFINITY, tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Double.POSITIVE_INFINITY, tanAngle.getMax(), 0.0001);
			
			// half rotation[0..pi], no change to angle, but tan = -Inf to Inf
			solver.clear();
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			angle.setMin(0);
			angle.setMax(NumConstants.PI);
			cnst = new TanConstraint(angleExpr, tanAngleExpr);
			solver.addConstraint(cnst);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", 0, angle.getMin(), 0.0001);
			assertEquals("angle max", NumConstants.PI, angle.getMax(), 0.0001);
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Double.NEGATIVE_INFINITY, tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Double.POSITIVE_INFINITY, tanAngle.getMax(), 0.0001);
			
			// half rotation[0.5*pi..1.5*pi], no change to angle, but tan = -Inf..Inf
			solver.clear();
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			angle.setMin(NumConstants.HALF_PI);
			angle.setMax(NumConstants.ONE_AND_HALF_PI);
			cnst = new TanConstraint(angleExpr, tanAngleExpr);
			solver.addConstraint(cnst);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", NumConstants.HALF_PI, angle.getMin(), 0.0001);
			assertEquals("angle max", NumConstants.ONE_AND_HALF_PI, angle.getMax(), 0.0001);
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Double.NEGATIVE_INFINITY, tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Double.POSITIVE_INFINITY, tanAngle.getMax(), 0.0001);
			
			// half rotation[pi..2pi], no change to angle, but tan = -Inf..Inf
			solver.clear();
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			angle.setMin(NumConstants.PI);
			angle.setMax(NumConstants.TWO_PI);
			cnst = new TanConstraint(angleExpr, tanAngleExpr);
			solver.addConstraint(cnst);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", NumConstants.PI, angle.getMin(), 0.0001);
			assertEquals("angle max", NumConstants.TWO_PI, angle.getMax(), 0.0001);
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Double.NEGATIVE_INFINITY, tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Double.POSITIVE_INFINITY, tanAngle.getMax(), 0.0001);
			
			// half rotation[1.5*pi..2.5*pi], no change to angle, but tan = -Inf..Inf
			solver.clear();
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			angle.setMin(NumConstants.ONE_AND_HALF_PI);
			angle.setMax(2.5 * NumConstants.PI);
			cnst = new TanConstraint(angleExpr, tanAngleExpr);
			solver.addConstraint(cnst);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", NumConstants.ONE_AND_HALF_PI, angle.getMin(), 0.0001);
			assertEquals("angle max", 2.5 * NumConstants.PI, angle.getMax(), 0.0001);
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Double.NEGATIVE_INFINITY, tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Double.POSITIVE_INFINITY, tanAngle.getMax(), 0.0001);
			
			// quarter rotation[0..0.5*pi], no change to angle, but tan = -Inf..Inf
			solver.clear();
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			angle.setMin(0);
			angle.setMax(NumConstants.HALF_PI);
			cnst = new TanConstraint(angleExpr, tanAngleExpr);
			solver.addConstraint(cnst);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", 0, angle.getMin(), 0.0001);
			assertEquals("angle max", NumConstants.HALF_PI, angle.getMax(), 0.0001);
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Double.NEGATIVE_INFINITY, tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Double.POSITIVE_INFINITY, tanAngle.getMax(), 0.0001);
			
			// quarter rotation[0.5pi..pi], no change to angle, but tan = -Inf..Inf
			solver.clear();
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			angle.setMin(NumConstants.HALF_PI);
			angle.setMax(NumConstants.PI);
			cnst = new TanConstraint(angleExpr, tanAngleExpr);
			solver.addConstraint(cnst);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", NumConstants.HALF_PI, angle.getMin(), 0.0001);
			assertEquals("angle max", NumConstants.PI, angle.getMax(), 0.0001);
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Double.NEGATIVE_INFINITY, tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Double.POSITIVE_INFINITY, tanAngle.getMax(), 0.0001);
			
			// quarter rotation[pi..1.5*pi], no change to angle, but tan = -Inf..Inf
			solver.clear();
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			angle.setMin(NumConstants.PI);
			angle.setMax(NumConstants.ONE_AND_HALF_PI);
			cnst = new TanConstraint(angleExpr, tanAngleExpr);
			solver.addConstraint(cnst);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", NumConstants.PI, angle.getMin(), 0.0001);
			assertEquals("angle max", NumConstants.ONE_AND_HALF_PI, angle.getMax(), 0.0001);
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Double.NEGATIVE_INFINITY, tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Double.POSITIVE_INFINITY, tanAngle.getMax(), 0.0001);
			
			// quarter rotation[1.5*pi..2*pi], no change to angle, but tan = -Inf..Inf
			solver.clear();
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			angle.setMin(NumConstants.ONE_AND_HALF_PI);
			angle.setMax(NumConstants.TWO_PI);
			cnst = new TanConstraint(angleExpr, tanAngleExpr);
			solver.addConstraint(cnst);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", NumConstants.ONE_AND_HALF_PI, angle.getMin(), 0.0001);
			assertEquals("angle max", NumConstants.TWO_PI, angle.getMax(), 0.0001);
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Double.NEGATIVE_INFINITY, tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Double.POSITIVE_INFINITY, tanAngle.getMax(), 0.0001);
			
			// quarter rotation[2*pi..2.5*pi], no change to angle, but tan = -Inf..Inf
			solver.clear();
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			angle.setMin(NumConstants.TWO_PI);
			angle.setMax(5 * NumConstants.HALF_PI);
			cnst = new TanConstraint(angleExpr, tanAngleExpr);
			solver.addConstraint(cnst);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", NumConstants.TWO_PI, angle.getMin(), 0.0001);
			assertEquals("angle max", 5 * NumConstants.HALF_PI, angle.getMax(), 0.0001);
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Double.NEGATIVE_INFINITY, tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Double.POSITIVE_INFINITY, tanAngle.getMax(), 0.0001);
			
			// eigth rotation[0*pi..0.25*pi]
			solver.clear();
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			angle.setMin(0);
			angle.setMax(0.25 * NumConstants.PI);
			cnst = new TanConstraint(angleExpr, tanAngleExpr);
			solver.addConstraint(cnst);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", 0, angle.getMin(), 0.0001);
			assertEquals("angle max", 0.25 * NumConstants.PI, angle.getMax(), 0.0001);
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", 0, tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Math.tan(0.25 * NumConstants.PI), tanAngle.getMax(), 0.0001);
			
			// eigth rotation[0.25*pi..0.5*pi]
			solver.clear();
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			angle.setMin(0.25 * NumConstants.PI);
			angle.setMax(0.5 * NumConstants.PI);
			cnst = new TanConstraint(angleExpr, tanAngleExpr);
			solver.addConstraint(cnst);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", 0.25 * NumConstants.PI, angle.getMin(), 0.0001);
			assertEquals("angle max", 0.5 * NumConstants.PI, angle.getMax(), 0.0001);
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Double.NEGATIVE_INFINITY, tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Double.POSITIVE_INFINITY, tanAngle.getMax(), 0.0001);
			
			// eigth rotation[0.5*pi..0.75*pi]
			solver.clear();
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			angle.setMin(0.5 * NumConstants.PI);
			angle.setMax(0.75 * NumConstants.PI);
			cnst = new TanConstraint(angleExpr, tanAngleExpr);
			solver.addConstraint(cnst);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", 0.5 * NumConstants.PI, angle.getMin(), 0.0001);
			assertEquals("angle max", 0.75 * NumConstants.PI, angle.getMax(), 0.0001);
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Double.NEGATIVE_INFINITY, tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Double.POSITIVE_INFINITY, tanAngle.getMax(), 0.0001);
			
			// eigth rotation[0.75*pi..pi]
			solver.clear();
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			angle.setMin(0.75 * NumConstants.PI);
			angle.setMax(NumConstants.PI);
			cnst = new TanConstraint(angleExpr, tanAngleExpr);
			solver.addConstraint(cnst);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", 0.75 * NumConstants.PI, angle.getMin(), 0.0001);
			assertEquals("angle max", NumConstants.PI, angle.getMax(), 0.0001);
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Math.tan(0.75 * NumConstants.PI), tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", 0, tanAngle.getMax(), 0.0001);
			
			// eigth rotation[pi..1.25pi]
			solver.clear();
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			angle.setMin(NumConstants.PI);
			angle.setMax(1.25 * NumConstants.PI);
			cnst = new TanConstraint(angleExpr, tanAngleExpr);
			solver.addConstraint(cnst);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", NumConstants.PI, angle.getMin(), 0.0001);
			assertEquals("angle max", 1.25 * NumConstants.PI, angle.getMax(), 0.0001);
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", 0, tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Math.tan(1.25 * NumConstants.PI), tanAngle.getMax(), 0.0001);
			
			// eigth rotation[1.25pi..1.5*pi]
			solver.clear();
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			angle.setMin(1.25 * NumConstants.PI);
			angle.setMax(1.5 * NumConstants.PI);
			cnst = new TanConstraint(angleExpr, tanAngleExpr);
			solver.addConstraint(cnst);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", 1.25 * NumConstants.PI, angle.getMin(), 0.0001);
			assertEquals("angle max", 1.5 * NumConstants.PI, angle.getMax(), 0.0001);
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Double.NEGATIVE_INFINITY, tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Double.POSITIVE_INFINITY, tanAngle.getMax(), 0.0001);
			
			// eigth rotation[1.5pi..1.75*pi]
			solver.clear();
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			angle.setMin(1.5 * NumConstants.PI);
			angle.setMax(1.75 * NumConstants.PI);
			cnst = new TanConstraint(angleExpr, tanAngleExpr);
			solver.addConstraint(cnst);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", 1.5 * NumConstants.PI, angle.getMin(), 0.0001);
			assertEquals("angle max", 1.75 * NumConstants.PI, angle.getMax(), 0.0001);
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Double.NEGATIVE_INFINITY, tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Double.POSITIVE_INFINITY, tanAngle.getMax(), 0.0001);
			
			// eigth rotation[1.75pi..2*pi]
			solver.clear();
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			angle.setMin(1.75 * NumConstants.PI);
			angle.setMax(2 * NumConstants.PI);
			cnst = new TanConstraint(angleExpr, tanAngleExpr);
			solver.addConstraint(cnst);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", 1.75 * NumConstants.PI, angle.getMin(), 0.0001);
			assertEquals("angle max", 2 * NumConstants.PI, angle.getMax(), 0.0001);
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Math.tan(1.75 * NumConstants.PI), tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", 0, tanAngle.getMax(), 0.0001);
			
			// eigth rotation[2*pi..2.25*pi]
			solver.clear();
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			angle.setMin(2 * NumConstants.PI);
			angle.setMax(2.25 * NumConstants.PI);
			cnst = new TanConstraint(angleExpr, tanAngleExpr);
			solver.addConstraint(cnst);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", 2 * NumConstants.PI, angle.getMin(), 0.0001);
			assertEquals("angle max", 2.25 * NumConstants.PI, angle.getMax(), 0.0001);
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", 0, tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Math.tan(2.25 * NumConstants.PI), tanAngle.getMax(), 0.0001);
			
			// angle bound [0]
			solver.clear();
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			angle.setMin(0);
			angle.setMax(0);
			cnst = new TanConstraint(angleExpr, tanAngleExpr);
			solver.addConstraint(cnst);
			assertTrue("angle bound", angle.isBound());
			assertEquals("angle min", 0, angle.getMin(), 0.0001);
			assertEquals("angle max", 0, angle.getMax(), 0.0001);
			assertTrue("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", 0, tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", 0, tanAngle.getMax(), 0.0001);
			
			// angle bound [0.25*pi]
			solver.clear();
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			angle.setMin(0.25 * NumConstants.PI);
			angle.setMax(0.25 * NumConstants.PI);
			cnst = new TanConstraint(angleExpr, tanAngleExpr);
			solver.addConstraint(cnst);
			assertTrue("angle bound", angle.isBound());
			assertEquals("angle min", 0.25 * NumConstants.PI, angle.getMin(), 0.0001);
			assertEquals("angle max", 0.25 * NumConstants.PI, angle.getMax(), 0.0001);
			assertTrue("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Math.tan(0.25 * NumConstants.PI), tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Math.tan(0.25 * NumConstants.PI), tanAngle.getMax(), 0.0001);
			
			// angle bound [0.5*pi]
			solver.clear();
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			angle.setMin(0.5 * NumConstants.PI);
			angle.setMax(0.5 * NumConstants.PI);
			cnst = new TanConstraint(angleExpr, tanAngleExpr);
			solver.addConstraint(cnst);
			assertTrue("angle bound", angle.isBound());
			assertEquals("angle min", 0.5 * NumConstants.PI, angle.getMin(), 0.0001);
			assertEquals("angle max", 0.5 * NumConstants.PI, angle.getMax(), 0.0001);
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Double.NEGATIVE_INFINITY, tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Double.POSITIVE_INFINITY, tanAngle.getMax(), 0.0001);
			
			// angle bound [0.75*pi]
			solver.clear();
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			angle.setMin(0.75 * NumConstants.PI);
			angle.setMax(0.75 * NumConstants.PI);
			cnst = new TanConstraint(angleExpr, tanAngleExpr);
			solver.addConstraint(cnst);
			assertTrue("angle bound", angle.isBound());
			assertEquals("angle min", 0.75 * NumConstants.PI, angle.getMin(), 0.0001);
			assertEquals("angle max", 0.75 * NumConstants.PI, angle.getMax(), 0.0001);
			assertTrue("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Math.tan(0.75 * NumConstants.PI), tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Math.tan(0.75 * NumConstants.PI), tanAngle.getMax(), 0.0001);
			
			// angle bound [pi]
			solver.clear();
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			angle.setMin(NumConstants.PI);
			angle.setMax(NumConstants.PI);
			cnst = new TanConstraint(angleExpr, tanAngleExpr);
			solver.addConstraint(cnst);
			assertTrue("angle bound", angle.isBound());
			assertEquals("angle min", NumConstants.PI, angle.getMin(), 0.0001);
			assertEquals("angle max", NumConstants.PI, angle.getMax(), 0.0001);
			assertTrue("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", 0, tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", 0, tanAngle.getMax(), 0.0001);
			
			// angle bound [1.25*pi]
			solver.clear();
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			angle.setMin(1.25 * NumConstants.PI);
			angle.setMax(1.25 * NumConstants.PI);
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			cnst = new TanConstraint(angleExpr, tanAngleExpr);
			solver.addConstraint(cnst);
			assertTrue("angle bound", angle.isBound());
			assertEquals("angle min", 1.25 * NumConstants.PI, angle.getMin(), 0.0001);
			assertEquals("angle max", 1.25 * NumConstants.PI, angle.getMax(), 0.0001);
			assertTrue("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Math.tan(1.25 * NumConstants.PI), tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Math.tan(1.25 * NumConstants.PI), tanAngle.getMax(), 0.0001);
			
			// angle bound [1.5*pi]
			solver.clear();
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			angle.setMin(1.5 * NumConstants.PI);
			angle.setMax(1.5 * NumConstants.PI);
			cnst = new TanConstraint(angleExpr, tanAngleExpr);
			solver.addConstraint(cnst);
			assertTrue("angle bound", angle.isBound());
			assertEquals("angle min", 1.5 * NumConstants.PI, angle.getMin(), 0.0001);
			assertEquals("angle max", 1.5 * NumConstants.PI, angle.getMax(), 0.0001);
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Double.NEGATIVE_INFINITY, tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Double.POSITIVE_INFINITY, tanAngle.getMax(), 0.0001);
			
			// angle bound [1.75*pi]
			solver.clear();
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			angle.setMin(1.75 * NumConstants.PI);
			angle.setMax(1.75 * NumConstants.PI);
			cnst = new TanConstraint(angleExpr, tanAngleExpr);
			solver.addConstraint(cnst);
			assertTrue("angle bound", angle.isBound());
			assertEquals("angle min", 1.75 * NumConstants.PI, angle.getMin(), 0.0001);
			assertEquals("angle max", 1.75 * NumConstants.PI, angle.getMax(), 0.0001);
			assertTrue("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Math.tan(1.75 * NumConstants.PI), tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Math.tan(1.75 * NumConstants.PI), tanAngle.getMax(), 0.0001);
			
			// angle bound [2*pi]
			solver.clear();
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			angle.setMin(2 * NumConstants.PI);
			angle.setMax(2 * NumConstants.PI);
			cnst = new TanConstraint(angleExpr, tanAngleExpr);
			solver.addConstraint(cnst);
			assertTrue("angle bound", angle.isBound());
			assertEquals("angle min", 2 * NumConstants.PI, angle.getMin(), 0.0001);
			assertEquals("angle max", 2 * NumConstants.PI, angle.getMax(), 0.0001);
			assertTrue("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", 0, tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", 0, tanAngle.getMax(), 0.0001);
			
			// angle bound [2.25*pi]
			solver.clear();
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			angle.setMin(2.25 * NumConstants.PI);
			angle.setMax(2.25 * NumConstants.PI);
			cnst = new TanConstraint(angleExpr, tanAngleExpr);
			solver.addConstraint(cnst);
			assertTrue("angle bound", angle.isBound());
			assertEquals("angle min", 2.25 * NumConstants.PI, angle.getMin(), 0.0001);
			assertEquals("angle max", 2.25 * NumConstants.PI, angle.getMax(), 0.0001);
			assertTrue("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Math.tan(2.25 * NumConstants.PI), tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Math.tan(2.25 * NumConstants.PI), tanAngle.getMax(), 0.0001);
		}
		catch(PropagationFailureException propx) {
			propx.printStackTrace();
			fail();
		}
	}
	
	public void testTanResultAdjust() {
		try {
			// full rotation[0..2pi]
			solver.clear();
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			tanAngle.setMin(Double.NEGATIVE_INFINITY);
			tanAngle.setMax(Double.POSITIVE_INFINITY);
			CspConstraint cnst = new TanConstraint(angleExpr, tanAngleExpr);
			solver.addConstraint(cnst);
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Double.NEGATIVE_INFINITY, tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Double.POSITIVE_INFINITY, tanAngle.getMax(), 0.0001);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", -FIVE_ROTATIONS, angle.getMin(), 0.0001);
			assertEquals("angle max", FIVE_ROTATIONS, angle.getMax(), 0.0001);
			
			// move[tan(0)..tan(0.25pi)]
			solver.clear();
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			tanAngle.setMin(0);
			tanAngle.setMax(Math.tan(0.25 * NumConstants.PI));
			cnst = new TanConstraint(angleExpr, tanAngleExpr);
			solver.addConstraint(cnst);
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", 0, tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Math.tan(0.25 * NumConstants.PI), tanAngle.getMax(), 0.0001);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", -FIVE_ROTATIONS, angle.getMin(), 0.0001);
			assertEquals("angle max", FIVE_ROTATIONS, angle.getMax(), 0.0001);
			
			// move[tan(0)..tan(1.25pi)]
			solver.clear();
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			tanAngle.setMin(0);
			tanAngle.setMax(Math.tan(1.25 * NumConstants.PI));
			cnst = new TanConstraint(angleExpr, tanAngleExpr);
			solver.addConstraint(cnst);
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", 0, tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Math.tan(0.25 * NumConstants.PI), tanAngle.getMax(), 0.0001);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", -FIVE_ROTATIONS, angle.getMin(), 0.0001);
			assertEquals("angle max", FIVE_ROTATIONS, angle.getMax(), 0.0001);
			
			// move[tan(0.125pi)..tan(0.25pi)]
			solver.clear();
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			tanAngle.setMin(Math.tan(0.125 * NumConstants.PI));
			tanAngle.setMax(Math.tan(0.25 * NumConstants.PI));
			cnst = new TanConstraint(angleExpr, tanAngleExpr);
			solver.addConstraint(cnst);
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Math.tan(0.125 * NumConstants.PI), tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Math.tan(0.25 * NumConstants.PI), tanAngle.getMax(), 0.0001);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", -FIVE_ROTATIONS + 0.75 * NumConstants.PI, angle.getMin(), 0.0001);
			assertEquals("angle max", FIVE_ROTATIONS - 0.75 * NumConstants.PI, angle.getMax(), 0.0001);
			
			// move[tan(0.25pi)..tan(0.375pi)]
			solver.clear();
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			tanAngle.setMin(Math.tan(0.25 * NumConstants.PI));
			tanAngle.setMax(Math.tan(0.375 * NumConstants.PI));
			cnst = new TanConstraint(angleExpr, tanAngleExpr);
			solver.addConstraint(cnst);
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Math.tan(0.25 * NumConstants.PI), tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Math.tan(0.375 * NumConstants.PI), tanAngle.getMax(), 0.0001);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", -FIVE_ROTATIONS + 0.625 * NumConstants.PI, angle.getMin(), 0.0001);
			assertEquals("angle max", FIVE_ROTATIONS - 0.625 * NumConstants.PI, angle.getMax(), 0.0001);
			
			
			// move[tan(0.375pi)..tan(0.5pi)]
			solver.clear();
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			tanAngle.setMin(Math.tan(0.375 * NumConstants.PI));
			tanAngle.setMax(Math.tan(0.5 * NumConstants.PI));
			cnst = new TanConstraint(angleExpr, tanAngleExpr);
			solver.addConstraint(cnst);
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Math.tan(0.375 * NumConstants.PI), tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Math.tan(0.5 * NumConstants.PI), tanAngle.getMax(), 0.0001);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", -FIVE_ROTATIONS + 0.5 * NumConstants.PI, angle.getMin(), 0.0001);
			assertEquals("angle max", FIVE_ROTATIONS - 0.5 * NumConstants.PI, angle.getMax(), 0.0001);
		}
		catch(PropagationFailureException propx) {
			propx.printStackTrace();
			fail();
		}
	}
	
	public void testTanCompareCosSinAssignTan() {
		try {
			solver.clear();
			angle.setMin(0);
			angle.setMax(NumConstants.TWO_PI);
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			solver.addVariable(cosAngle);
			solver.addVariable(sinAngle);
			tanAngle.setMin(Double.NEGATIVE_INFINITY);
			tanAngle.setMax(Double.POSITIVE_INFINITY);
			solver.addConstraint(new TanConstraint(angleExpr, tanAngleExpr));
			solver.addConstraint(new CosConstraint(angleExpr, cosAngleExpr));
			solver.addConstraint(new SinConstraint(angleExpr, sinAngleExpr));
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Double.NEGATIVE_INFINITY, tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Double.POSITIVE_INFINITY, tanAngle.getMax(), 0.0001);
			assertFalse("cosAngle bound", cosAngle.isBound());
			assertEquals("cosAngle min", -1, cosAngle.getMin(), 0.0001);
			assertEquals("cosAngle max", 1, cosAngle.getMax(), 0.0001);
			assertFalse("sinAngle bound", sinAngle.isBound());
			assertEquals("sinAngle min", -1, sinAngle.getMin(), 0.0001);
			assertEquals("sinAngle max", 1, sinAngle.getMax(), 0.0001);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", 0, angle.getMin(), 0.0001);
			assertEquals("angle max", NumConstants.TWO_PI, angle.getMax(), 0.0001);
			
			// [0]
			solver.clear();
			angle.setMin(0);
			angle.setMax(NumConstants.TWO_PI);
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			solver.addVariable(cosAngle);
			solver.addVariable(sinAngle);
			tanAngle.setMin(0);
			tanAngle.setMax(0);
			solver.addConstraint(new TanConstraint(angleExpr, tanAngleExpr));
			solver.addConstraint(new CosConstraint(angleExpr, cosAngleExpr));
			solver.addConstraint(new SinConstraint(angleExpr, sinAngleExpr));
			assertTrue("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", 0, tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", 0, tanAngle.getMax(), 0.0001);
			assertFalse("cosAngle bound", cosAngle.isBound());
			assertEquals("cosAngle min", -1, cosAngle.getMin(), 0.0001);
			assertEquals("cosAngle max", 1, cosAngle.getMax(), 0.0001);
			assertFalse("sinAngle bound", sinAngle.isBound());
			assertEquals("sinAngle min", -1, sinAngle.getMin(), 0.0001);
			assertEquals("sinAngle max", 1, sinAngle.getMax(), 0.0001);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", 0, angle.getMin(), 0.0001);
			assertEquals("angle max", NumConstants.TWO_PI, angle.getMax(), 0.0001);
			
			// [0.125pi]
			solver.clear();
			angle.setMin(0);
			angle.setMax(2 * NumConstants.PI);
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			solver.addVariable(cosAngle);
			solver.addVariable(sinAngle);
			tanAngle.setMin(Math.tan(0.125 * NumConstants.PI));
			tanAngle.setMax(Math.tan(0.125 * NumConstants.PI));
			solver.addConstraint(new TanConstraint(angleExpr, tanAngleExpr));
			solver.addConstraint(new CosConstraint(angleExpr, cosAngleExpr));
			solver.addConstraint(new SinConstraint(angleExpr, sinAngleExpr));
			assertTrue("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Math.tan(0.125 * NumConstants.PI), tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Math.tan(0.125 * NumConstants.PI), tanAngle.getMax(), 0.0001);
			assertEquals("tanAngle min", Math.tan(1.125 * NumConstants.PI), tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Math.tan(1.125 * NumConstants.PI), tanAngle.getMax(), 0.0001);
			assertFalse("cosAngle bound", cosAngle.isBound());
			assertEquals("cosAngle min", -1, cosAngle.getMin(), 0.0001);
			assertEquals("cosAngle max", Math.cos(0.125 * NumConstants.PI), cosAngle.getMax(), 0.0001);
			assertEquals("cosAngle max", Math.cos(1.875 * NumConstants.PI), cosAngle.getMax(), 0.0001);
			assertFalse("sinAngle bound", sinAngle.isBound());
			assertEquals("sinAngle min", 0, sinAngle.getMin(), 0.0001);
			assertEquals("sinAngle max", 1, sinAngle.getMax(), 0.0001);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", 0.125 * NumConstants.PI, angle.getMin(), 0.0001);
			assertEquals("angle max", NumConstants.PI, angle.getMax(), 0.0001);
			
			// [0.25pi]
			solver.clear();
			angle.setMin(0);
			angle.setMax(2 * NumConstants.PI);
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			solver.addVariable(cosAngle);
			solver.addVariable(sinAngle);
			tanAngle.setMin(Math.tan(0.25 * NumConstants.PI));
			tanAngle.setMax(Math.tan(0.25 * NumConstants.PI));
			solver.addConstraint(new TanConstraint(angleExpr, tanAngleExpr));
			solver.addConstraint(new CosConstraint(angleExpr, cosAngleExpr));
			solver.addConstraint(new SinConstraint(angleExpr, sinAngleExpr));
			assertTrue("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Math.tan(0.25 * NumConstants.PI), tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Math.tan(0.25 * NumConstants.PI), tanAngle.getMax(), 0.0001);
			assertEquals("tanAngle min", Math.tan(1.25 * NumConstants.PI), tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Math.tan(1.25 * NumConstants.PI), tanAngle.getMax(), 0.0001);
			assertFalse("cosAngle bound", cosAngle.isBound());
			assertEquals("cosAngle min", -1, cosAngle.getMin(), 0.0001);
			assertEquals("cosAngle max", Math.cos(0.25 * NumConstants.PI), cosAngle.getMax(), 0.0001);
			assertEquals("cosAngle max", Math.cos(1.75 * NumConstants.PI), cosAngle.getMax(), 0.0001);
			assertFalse("sinAngle bound", sinAngle.isBound());
			assertEquals("sinAngle min", 0, sinAngle.getMin(), 0.0001);
			assertEquals("sinAngle max", 1, sinAngle.getMax(), 0.0001);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", 0.25 * NumConstants.PI, angle.getMin(), 0.0001);
			assertEquals("angle max", NumConstants.PI, angle.getMax(), 0.0001);
			
			// [0.375pi]
			solver.clear();
			angle.setMin(0);
			angle.setMax(2 * NumConstants.PI);
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			solver.addVariable(cosAngle);
			solver.addVariable(sinAngle);
			tanAngle.setMin(Math.tan(0.375 * NumConstants.PI));
			tanAngle.setMax(Math.tan(0.375 * NumConstants.PI));
			solver.addConstraint(new TanConstraint(angleExpr, tanAngleExpr));
			solver.addConstraint(new CosConstraint(angleExpr, cosAngleExpr));
			solver.addConstraint(new SinConstraint(angleExpr, sinAngleExpr));
			assertTrue("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Math.tan(0.375 * NumConstants.PI), tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Math.tan(0.375 * NumConstants.PI), tanAngle.getMax(), 0.0001);
			assertEquals("tanAngle min", Math.tan(1.375 * NumConstants.PI), tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Math.tan(1.375 * NumConstants.PI), tanAngle.getMax(), 0.0001);
			assertFalse("cosAngle bound", cosAngle.isBound());
			assertEquals("cosAngle min", -1, cosAngle.getMin(), 0.0001);
			assertEquals("cosAngle max", Math.cos(0.375 * NumConstants.PI), cosAngle.getMax(), 0.0001);
			assertEquals("cosAngle max", Math.cos(1.625 * NumConstants.PI), cosAngle.getMax(), 0.0001);
			assertFalse("sinAngle bound", sinAngle.isBound());
			assertEquals("sinAngle min", 0, sinAngle.getMin(), 0.0001);
			assertEquals("sinAngle max", 1, sinAngle.getMax(), 0.0001);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", 0.375 * NumConstants.PI, angle.getMin(), 0.0001);
			assertEquals("angle max", NumConstants.PI, angle.getMax(), 0.0001);
			
			// [0.625pi]
			solver.clear();
			angle.setMin(0);
			angle.setMax(2 * NumConstants.PI);
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			solver.addVariable(cosAngle);
			solver.addVariable(sinAngle);
			tanAngle.setMin(Math.tan(0.625 * NumConstants.PI));
			tanAngle.setMax(Math.tan(0.625 * NumConstants.PI));
			solver.addConstraint(new TanConstraint(angleExpr, tanAngleExpr));
			solver.addConstraint(new CosConstraint(angleExpr, cosAngleExpr));
			solver.addConstraint(new SinConstraint(angleExpr, sinAngleExpr));
			assertTrue("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Math.tan(0.625 * NumConstants.PI), tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Math.tan(0.625 * NumConstants.PI), tanAngle.getMax(), 0.0001);
			assertEquals("tanAngle min", Math.tan(1.625 * NumConstants.PI), tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Math.tan(1.625 * NumConstants.PI), tanAngle.getMax(), 0.0001);
			assertFalse("cosAngle bound", cosAngle.isBound());
			assertEquals("cosAngle min", -1, cosAngle.getMin(), 0.0001);
			assertEquals("cosAngle max", -Math.cos(0.625 * NumConstants.PI), cosAngle.getMax(), 0.0001);
			assertEquals("cosAngle max", Math.cos(1.625 * NumConstants.PI), cosAngle.getMax(), 0.0001);
			assertFalse("sinAngle bound", sinAngle.isBound());
			assertEquals("sinAngle min", -1, sinAngle.getMin(), 0.0001);
			assertEquals("sinAngle max", 0, sinAngle.getMax(), 0.0001);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", NumConstants.PI, angle.getMin(), 0.0001);
			assertEquals("angle max", 1.625 * NumConstants.PI, angle.getMax(), 0.0001);
			
			// [0.75pi]
			solver.clear();
			angle.setMin(0);
			angle.setMax(2 * NumConstants.PI);
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			solver.addVariable(cosAngle);
			solver.addVariable(sinAngle);
			tanAngle.setMin(Math.tan(0.75 * NumConstants.PI));
			tanAngle.setMax(Math.tan(0.75 * NumConstants.PI));
			solver.addConstraint(new TanConstraint(angleExpr, tanAngleExpr));
			solver.addConstraint(new CosConstraint(angleExpr, cosAngleExpr));
			solver.addConstraint(new SinConstraint(angleExpr, sinAngleExpr));
			assertTrue("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Math.tan(0.75 * NumConstants.PI), tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Math.tan(0.75 * NumConstants.PI), tanAngle.getMax(), 0.0001);
			assertEquals("tanAngle min", Math.tan(1.75 * NumConstants.PI), tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Math.tan(1.75 * NumConstants.PI), tanAngle.getMax(), 0.0001);
			assertFalse("cosAngle bound", cosAngle.isBound());
			assertEquals("cosAngle min", -1, cosAngle.getMin(), 0.0001);
			assertEquals("cosAngle max", -Math.cos(0.75 * NumConstants.PI), cosAngle.getMax(), 0.0001);
			assertEquals("cosAngle max", Math.cos(1.75 * NumConstants.PI), cosAngle.getMax(), 0.0001);
			assertFalse("sinAngle bound", sinAngle.isBound());
			assertEquals("sinAngle min", -1, sinAngle.getMin(), 0.0001);
			assertEquals("sinAngle max", 0, sinAngle.getMax(), 0.0001);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", NumConstants.PI, angle.getMin(), 0.0001);
			assertEquals("angle max", 1.75 * NumConstants.PI, angle.getMax(), 0.0001);
			
			// [0.825pi]
			solver.clear();
			angle.setMin(0);
			angle.setMax(2 * NumConstants.PI);
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			solver.addVariable(cosAngle);
			solver.addVariable(sinAngle);
			tanAngle.setMin(Math.tan(0.825 * NumConstants.PI));
			tanAngle.setMax(Math.tan(0.825 * NumConstants.PI));
			solver.addConstraint(new TanConstraint(angleExpr, tanAngleExpr));
			solver.addConstraint(new CosConstraint(angleExpr, cosAngleExpr));
			solver.addConstraint(new SinConstraint(angleExpr, sinAngleExpr));
			assertTrue("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Math.tan(0.825 * NumConstants.PI), tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Math.tan(0.825 * NumConstants.PI), tanAngle.getMax(), 0.0001);
			assertEquals("tanAngle min", Math.tan(1.825 * NumConstants.PI), tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Math.tan(1.825 * NumConstants.PI), tanAngle.getMax(), 0.0001);
			assertFalse("cosAngle bound", cosAngle.isBound());
			assertEquals("cosAngle min", -1, cosAngle.getMin(), 0.0001);
			assertEquals("cosAngle max", -Math.cos(0.825 * NumConstants.PI), cosAngle.getMax(), 0.0001);
			assertEquals("cosAngle max", Math.cos(1.825 * NumConstants.PI), cosAngle.getMax(), 0.0001);
			assertFalse("sinAngle bound", sinAngle.isBound());
			assertEquals("sinAngle min", -1, sinAngle.getMin(), 0.0001);
			assertEquals("sinAngle max", 0, sinAngle.getMax(), 0.0001);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", NumConstants.PI, angle.getMin(), 0.0001);
			assertEquals("angle max", 1.825 * NumConstants.PI, angle.getMax(), 0.0001);
			
		}
		catch(PropagationFailureException propx) {
			propx.printStackTrace();
			fail();
		}
	}
	
	public void testTanCompareCosSinAssignCos() {
		try {
			// [0]
			solver.clear();
			angle.setMin(0);
			angle.setMax(2 * NumConstants.PI);
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			solver.addVariable(cosAngle);
			solver.addVariable(sinAngle);
			cosAngle.setMin(Math.cos(0));
			cosAngle.setMax(Math.cos(0));
			solver.addConstraint(new TanConstraint(angleExpr, tanAngleExpr));
			solver.addConstraint(new CosConstraint(angleExpr, cosAngleExpr));
			solver.addConstraint(new SinConstraint(angleExpr, sinAngleExpr));
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Double.NEGATIVE_INFINITY, tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Double.POSITIVE_INFINITY, tanAngle.getMax(), 0.0001);
			assertTrue("cosAngle bound", cosAngle.isBound());
			assertEquals("cosAngle min", Math.cos(0), cosAngle.getMin(), 0.0001);
			assertEquals("cosAngle max", Math.cos(0), cosAngle.getMax(), 0.0001);
			assertEquals("cosAngle max", Math.cos(NumConstants.TWO_PI), cosAngle.getMax(), 0.0001);
			assertFalse("sinAngle bound", sinAngle.isBound());
			assertEquals("sinAngle min", -1, sinAngle.getMin(), 0.0001);
			assertEquals("sinAngle max", 1, sinAngle.getMax(), 0.0001);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", 0, angle.getMin(), 0.0001);
			assertEquals("angle max", NumConstants.TWO_PI, angle.getMax(), 0.0001);
			
			// [0.125pi]
			solver.clear();
			angle.setMin(0);
			angle.setMax(2 * NumConstants.PI);
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			solver.addVariable(cosAngle);
			solver.addVariable(sinAngle);
			cosAngle.setMin(Math.cos(0.125 * NumConstants.PI));
			cosAngle.setMax(Math.cos(0.125 * NumConstants.PI));
			solver.addConstraint(new TanConstraint(angleExpr, tanAngleExpr));
			solver.addConstraint(new CosConstraint(angleExpr, cosAngleExpr));
			solver.addConstraint(new SinConstraint(angleExpr, sinAngleExpr));
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Double.NEGATIVE_INFINITY, tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Double.POSITIVE_INFINITY, tanAngle.getMax(), 0.0001);
			assertTrue("cosAngle bound", cosAngle.isBound());
			assertEquals("cosAngle min", Math.cos(0.125 * NumConstants.PI), cosAngle.getMin(), 0.0001);
			assertEquals("cosAngle max", Math.cos(0.125 * NumConstants.PI), cosAngle.getMax(), 0.0001);
			assertEquals("cosAngle max", Math.cos(1.875 * NumConstants.PI), cosAngle.getMax(), 0.0001);
			assertFalse("sinAngle bound", sinAngle.isBound());
			assertEquals("sinAngle min", -1, sinAngle.getMin(), 0.0001);
			assertEquals("sinAngle max", 1, sinAngle.getMax(), 0.0001);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", 0.125 * NumConstants.PI, angle.getMin(), 0.0001);
			assertEquals("angle max", 1.875 * NumConstants.PI, angle.getMax(), 0.0001);
			
			// [0.25pi]
			solver.clear();
			angle.setMin(0);
			angle.setMax(2 * NumConstants.PI);
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			solver.addVariable(cosAngle);
			solver.addVariable(sinAngle);
			cosAngle.setMin(Math.cos(0.25 * NumConstants.PI));
			cosAngle.setMax(Math.cos(0.25 * NumConstants.PI));
			solver.addConstraint(new TanConstraint(angleExpr, tanAngleExpr));
			solver.addConstraint(new CosConstraint(angleExpr, cosAngleExpr));
			solver.addConstraint(new SinConstraint(angleExpr, sinAngleExpr));
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Double.NEGATIVE_INFINITY, tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Double.POSITIVE_INFINITY, tanAngle.getMax(), 0.0001);
			assertTrue("cosAngle bound", cosAngle.isBound());
			assertEquals("cosAngle min", Math.cos(0.25 * NumConstants.PI), cosAngle.getMin(), 0.0001);
			assertEquals("cosAngle max", Math.cos(0.25 * NumConstants.PI), cosAngle.getMax(), 0.0001);
			assertEquals("cosAngle max", Math.cos(1.75 * NumConstants.PI), cosAngle.getMax(), 0.0001);
			assertFalse("sinAngle bound", sinAngle.isBound());
			assertEquals("sinAngle min", -1, sinAngle.getMin(), 0.0001);
			assertEquals("sinAngle max", 1, sinAngle.getMax(), 0.0001);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", 0.25 * NumConstants.PI, angle.getMin(), 0.0001);
			assertEquals("angle max", 1.75 * NumConstants.PI, angle.getMax(), 0.0001);
			
			// [0.375pi]
			solver.clear();
			angle.setMin(0);
			angle.setMax(2 * NumConstants.PI);
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			solver.addVariable(cosAngle);
			solver.addVariable(sinAngle);
			cosAngle.setMin(Math.cos(0.375 * NumConstants.PI));
			cosAngle.setMax(Math.cos(0.375 * NumConstants.PI));
			solver.addConstraint(new TanConstraint(angleExpr, tanAngleExpr));
			solver.addConstraint(new CosConstraint(angleExpr, cosAngleExpr));
			solver.addConstraint(new SinConstraint(angleExpr, sinAngleExpr));
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Double.NEGATIVE_INFINITY, tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Double.POSITIVE_INFINITY, tanAngle.getMax(), 0.0001);
			assertTrue("cosAngle bound", cosAngle.isBound());
			assertEquals("cosAngle min", Math.cos(0.375 * NumConstants.PI), cosAngle.getMin(), 0.0001);
			assertEquals("cosAngle max", Math.cos(0.375 * NumConstants.PI), cosAngle.getMax(), 0.0001);
			assertEquals("cosAngle max", Math.cos(1.625 * NumConstants.PI), cosAngle.getMax(), 0.0001);
			assertFalse("sinAngle bound", sinAngle.isBound());
			assertEquals("sinAngle min", -1, sinAngle.getMin(), 0.0001);
			assertEquals("sinAngle max", 1, sinAngle.getMax(), 0.0001);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", 0.375 * NumConstants.PI, angle.getMin(), 0.0001);
			assertEquals("angle max", 1.625 * NumConstants.PI, angle.getMax(), 0.0001);
			
			// [0.5pi]
			solver.clear();
			angle.setMin(0);
			angle.setMax(2 * NumConstants.PI);
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			solver.addVariable(cosAngle);
			solver.addVariable(sinAngle);
			cosAngle.setMin(Math.cos(0.5 * NumConstants.PI));
			cosAngle.setMax(Math.cos(0.5 * NumConstants.PI));
			solver.addConstraint(new TanConstraint(angleExpr, tanAngleExpr));
			solver.addConstraint(new CosConstraint(angleExpr, cosAngleExpr));
			solver.addConstraint(new SinConstraint(angleExpr, sinAngleExpr));
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Double.NEGATIVE_INFINITY, tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Double.POSITIVE_INFINITY, tanAngle.getMax(), 0.0001);
			assertTrue("cosAngle bound", cosAngle.isBound());
			assertEquals("cosAngle min", Math.cos(0.5 * NumConstants.PI), cosAngle.getMin(), 0.0001);
			assertEquals("cosAngle max", Math.cos(0.5 * NumConstants.PI), cosAngle.getMax(), 0.0001);
			assertEquals("cosAngle max", Math.cos(1.5 * NumConstants.PI), cosAngle.getMax(), 0.0001);
			assertFalse("sinAngle bound", sinAngle.isBound());
			assertEquals("sinAngle min", -1, sinAngle.getMin(), 0.0001);
			assertEquals("sinAngle max", 1, sinAngle.getMax(), 0.0001);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", 0.5 * NumConstants.PI, angle.getMin(), 0.0001);
			assertEquals("angle max", 1.5 * NumConstants.PI, angle.getMax(), 0.0001);
			
			// [0.625pi]
			solver.clear();
			angle.setMin(0);
			angle.setMax(2 * NumConstants.PI);
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			solver.addVariable(cosAngle);
			solver.addVariable(sinAngle);
			cosAngle.setMin(Math.cos(0.625 * NumConstants.PI));
			cosAngle.setMax(Math.cos(0.625 * NumConstants.PI));
			solver.addConstraint(new TanConstraint(angleExpr, tanAngleExpr));
			solver.addConstraint(new CosConstraint(angleExpr, cosAngleExpr));
			solver.addConstraint(new SinConstraint(angleExpr, sinAngleExpr));
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Math.tan(0.625 * NumConstants.PI), tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Math.tan(1.375 * NumConstants.PI), tanAngle.getMax(), 0.0001);
			assertTrue("cosAngle bound", cosAngle.isBound());
			assertEquals("cosAngle min", Math.cos(0.625 * NumConstants.PI), cosAngle.getMin(), 0.0001);
			assertEquals("cosAngle max", Math.cos(0.625 * NumConstants.PI), cosAngle.getMax(), 0.0001);
			assertEquals("cosAngle max", Math.cos(1.375 * NumConstants.PI), cosAngle.getMax(), 0.0001);
			assertFalse("sinAngle bound", sinAngle.isBound());
			assertEquals("sinAngle min", -Math.sin(0.625 * NumConstants.PI), sinAngle.getMin(), 0.0001);
			assertEquals("sinAngle max", -Math.sin(1.375 * NumConstants.PI), sinAngle.getMax(), 0.0001);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", 0.625 * NumConstants.PI, angle.getMin(), 0.0001);
			assertEquals("angle max", 1.375 * NumConstants.PI, angle.getMax(), 0.0001);
			
			// [0.75pi]
			solver.clear();
			angle.setMin(0);
			angle.setMax(2 * NumConstants.PI);
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			solver.addVariable(cosAngle);
			solver.addVariable(sinAngle);
			cosAngle.setMin(Math.cos(0.75 * NumConstants.PI));
			cosAngle.setMax(Math.cos(0.75 * NumConstants.PI));
			solver.addConstraint(new TanConstraint(angleExpr, tanAngleExpr));
			solver.addConstraint(new CosConstraint(angleExpr, cosAngleExpr));
			solver.addConstraint(new SinConstraint(angleExpr, sinAngleExpr));
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Math.tan(0.75 * NumConstants.PI), tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Math.tan(1.25 * NumConstants.PI), tanAngle.getMax(), 0.0001);
			assertTrue("cosAngle bound", cosAngle.isBound());
			assertEquals("cosAngle min", Math.cos(0.75 * NumConstants.PI), cosAngle.getMin(), 0.0001);
			assertEquals("cosAngle max", Math.cos(0.75 * NumConstants.PI), cosAngle.getMax(), 0.0001);
			assertEquals("cosAngle max", Math.cos(1.25 * NumConstants.PI), cosAngle.getMax(), 0.0001);
			assertFalse("sinAngle bound", sinAngle.isBound());
			assertEquals("sinAngle min", -Math.sin(0.75 * NumConstants.PI), sinAngle.getMin(), 0.0001);
			assertEquals("sinAngle max", -Math.sin(1.25 * NumConstants.PI), sinAngle.getMax(), 0.0001);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", 0.75 * NumConstants.PI, angle.getMin(), 0.0001);
			assertEquals("angle max", 1.25 * NumConstants.PI, angle.getMax(), 0.0001);
			
			// [0.875pi]
			solver.clear();
			angle.setMin(0);
			angle.setMax(2 * NumConstants.PI);
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			solver.addVariable(cosAngle);
			solver.addVariable(sinAngle);
			cosAngle.setMin(Math.cos(0.875 * NumConstants.PI));
			cosAngle.setMax(Math.cos(0.875 * NumConstants.PI));
			solver.addConstraint(new TanConstraint(angleExpr, tanAngleExpr));
			solver.addConstraint(new CosConstraint(angleExpr, cosAngleExpr));
			solver.addConstraint(new SinConstraint(angleExpr, sinAngleExpr));
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Math.tan(0.875 * NumConstants.PI), tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Math.tan(1.125 * NumConstants.PI), tanAngle.getMax(), 0.0001);
			assertTrue("cosAngle bound", cosAngle.isBound());
			assertEquals("cosAngle min", Math.cos(0.875 * NumConstants.PI), cosAngle.getMin(), 0.0001);
			assertEquals("cosAngle max", Math.cos(0.875 * NumConstants.PI), cosAngle.getMax(), 0.0001);
			assertEquals("cosAngle max", Math.cos(1.125 * NumConstants.PI), cosAngle.getMax(), 0.0001);
			assertFalse("sinAngle bound", sinAngle.isBound());
			assertEquals("sinAngle min", -Math.sin(0.875 * NumConstants.PI), sinAngle.getMin(), 0.0001);
			assertEquals("sinAngle max", -Math.sin(1.125 * NumConstants.PI), sinAngle.getMax(), 0.0001);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", 0.875 * NumConstants.PI, angle.getMin(), 0.0001);
			assertEquals("angle max", 1.125 * NumConstants.PI, angle.getMax(), 0.0001);
			
			// [pi]
			solver.clear();
			angle.setMin(0);
			angle.setMax(2 * NumConstants.PI);
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			solver.addVariable(cosAngle);
			solver.addVariable(sinAngle);
			cosAngle.setMin(Math.cos(NumConstants.PI));
			cosAngle.setMax(Math.cos(NumConstants.PI));
			solver.addConstraint(new TanConstraint(angleExpr, tanAngleExpr));
			solver.addConstraint(new CosConstraint(angleExpr, cosAngleExpr));
			solver.addConstraint(new SinConstraint(angleExpr, sinAngleExpr));
			assertTrue("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Math.tan(NumConstants.PI), tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Math.tan(NumConstants.PI), tanAngle.getMax(), 0.0001);
			assertTrue("cosAngle bound", cosAngle.isBound());
			assertEquals("cosAngle min", Math.cos(NumConstants.PI), cosAngle.getMin(), 0.0001);
			assertEquals("cosAngle max", Math.cos(NumConstants.PI), cosAngle.getMax(), 0.0001);
			assertTrue("sinAngle bound", sinAngle.isBound());
			assertEquals("sinAngle min", -Math.sin(NumConstants.PI), sinAngle.getMin(), 0.0001);
			assertEquals("sinAngle max", -Math.sin(NumConstants.PI), sinAngle.getMax(), 0.0001);
			assertTrue("angle bound", angle.isBound());
			assertEquals("angle min", NumConstants.PI, angle.getMin(), 0.0001);
			assertEquals("angle max", NumConstants.PI, angle.getMax(), 0.0001);
			
		}
		catch(PropagationFailureException propx) {
			propx.printStackTrace();
			fail();
		}
	}
	
	public void testTanCompareCosSinAssignSin() {
		try {
			// [0]
			solver.clear();
			angle.setMin(0);
			angle.setMax(2 * NumConstants.PI);
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			solver.addVariable(cosAngle);
			solver.addVariable(sinAngle);
			sinAngle.setMin(Math.sin(0));
			sinAngle.setMax(Math.sin(0));
			solver.addConstraint(new TanConstraint(angleExpr, tanAngleExpr));
			solver.addConstraint(new CosConstraint(angleExpr, cosAngleExpr));
			solver.addConstraint(new SinConstraint(angleExpr, sinAngleExpr));
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Double.NEGATIVE_INFINITY, tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Double.POSITIVE_INFINITY, tanAngle.getMax(), 0.0001);
			assertFalse("cosAngle bound", cosAngle.isBound());
			assertEquals("cosAngle min", -1, cosAngle.getMin(), 0.0001);
			assertEquals("cosAngle max", 1, cosAngle.getMax(), 0.0001);
			assertTrue("sinAngle bound", sinAngle.isBound());
			assertEquals("sinAngle min", 0, sinAngle.getMin(), 0.0001);
			assertEquals("sinAngle max", 0, sinAngle.getMax(), 0.0001);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", 0, angle.getMin(), 0.0001);
			assertEquals("angle max", NumConstants.TWO_PI, angle.getMax(), 0.0001);
			
			// [0.125pi]
			solver.clear();
			angle.setMin(0);
			angle.setMax(2 * NumConstants.PI);
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			solver.addVariable(cosAngle);
			solver.addVariable(sinAngle);
			sinAngle.setMin(Math.sin(0.125 * NumConstants.PI));
			sinAngle.setMax(Math.sin(0.125 * NumConstants.PI));
			solver.addConstraint(new TanConstraint(angleExpr, tanAngleExpr));
			solver.addConstraint(new CosConstraint(angleExpr, cosAngleExpr));
			solver.addConstraint(new SinConstraint(angleExpr, sinAngleExpr));
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Double.NEGATIVE_INFINITY, tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Double.POSITIVE_INFINITY, tanAngle.getMax(), 0.0001);
			assertFalse("cosAngle bound", cosAngle.isBound());
			assertEquals("cosAngle min", Math.cos(0.875 * NumConstants.PI), cosAngle.getMin(), 0.0001);
			assertEquals("cosAngle max", Math.cos(0.125 * NumConstants.PI), cosAngle.getMax(), 0.0001);
			assertTrue("sinAngle bound", sinAngle.isBound());
			assertEquals("sinAngle min", Math.sin(0.125 * NumConstants.PI), sinAngle.getMin(), 0.0001);
			assertEquals("sinAngle max", Math.sin(0.125 * NumConstants.PI), sinAngle.getMax(), 0.0001);
			assertEquals("sinAngle max", Math.sin(0.875 * NumConstants.PI), sinAngle.getMax(), 0.0001);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", 0.125 * NumConstants.PI, angle.getMin(), 0.0001);
			assertEquals("angle max", 0.875 * NumConstants.PI, angle.getMax(), 0.0001);
			
			// [0.25pi]
			solver.clear();
			angle.setMin(0);
			angle.setMax(2 * NumConstants.PI);
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			solver.addVariable(cosAngle);
			solver.addVariable(sinAngle);
			sinAngle.setMin(Math.sin(0.25 * NumConstants.PI));
			sinAngle.setMax(Math.sin(0.25 * NumConstants.PI));
			solver.addConstraint(new TanConstraint(angleExpr, tanAngleExpr));
			solver.addConstraint(new CosConstraint(angleExpr, cosAngleExpr));
			solver.addConstraint(new SinConstraint(angleExpr, sinAngleExpr));
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Double.NEGATIVE_INFINITY, tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Double.POSITIVE_INFINITY, tanAngle.getMax(), 0.0001);
			assertFalse("cosAngle bound", cosAngle.isBound());
			assertEquals("cosAngle min", Math.cos(0.75 * NumConstants.PI), cosAngle.getMin(), 0.0001);
			assertEquals("cosAngle max", Math.cos(0.25 * NumConstants.PI), cosAngle.getMax(), 0.0001);
			assertTrue("sinAngle bound", sinAngle.isBound());
			assertEquals("sinAngle min", Math.sin(0.25 * NumConstants.PI), sinAngle.getMin(), 0.0001);
			assertEquals("sinAngle max", Math.sin(0.25 * NumConstants.PI), sinAngle.getMax(), 0.0001);
			assertEquals("sinAngle max", Math.sin(0.75 * NumConstants.PI), sinAngle.getMax(), 0.0001);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", 0.25 * NumConstants.PI, angle.getMin(), 0.0001);
			assertEquals("angle max", 0.75 * NumConstants.PI, angle.getMax(), 0.0001);
			
			// [0.375pi]
			solver.clear();
			angle.setMin(0);
			angle.setMax(2 * NumConstants.PI);
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			solver.addVariable(cosAngle);
			solver.addVariable(sinAngle);
			sinAngle.setMin(Math.sin(0.375 * NumConstants.PI));
			sinAngle.setMax(Math.sin(0.375 * NumConstants.PI));
			solver.addConstraint(new TanConstraint(angleExpr, tanAngleExpr));
			solver.addConstraint(new CosConstraint(angleExpr, cosAngleExpr));
			solver.addConstraint(new SinConstraint(angleExpr, sinAngleExpr));
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Double.NEGATIVE_INFINITY, tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Double.POSITIVE_INFINITY, tanAngle.getMax(), 0.0001);
			assertFalse("cosAngle bound", cosAngle.isBound());
			assertEquals("cosAngle min", Math.cos(0.625 * NumConstants.PI), cosAngle.getMin(), 0.0001);
			assertEquals("cosAngle max", Math.cos(0.375 * NumConstants.PI), cosAngle.getMax(), 0.0001);
			assertTrue("sinAngle bound", sinAngle.isBound());
			assertEquals("sinAngle min", Math.sin(0.375 * NumConstants.PI), sinAngle.getMin(), 0.0001);
			assertEquals("sinAngle max", Math.sin(0.375 * NumConstants.PI), sinAngle.getMax(), 0.0001);
			assertEquals("sinAngle max", Math.sin(0.625 * NumConstants.PI), sinAngle.getMax(), 0.0001);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", 0.375 * NumConstants.PI, angle.getMin(), 0.0001);
			assertEquals("angle max", 0.625 * NumConstants.PI, angle.getMax(), 0.0001);
			
			// [0.5pi]
			solver.clear();
			angle.setMin(0);
			angle.setMax(2 * NumConstants.PI);
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			solver.addVariable(cosAngle);
			solver.addVariable(sinAngle);
			sinAngle.setMin(Math.sin(0.5 * NumConstants.PI));
			sinAngle.setMax(Math.sin(0.5 * NumConstants.PI));
			solver.addConstraint(new TanConstraint(angleExpr, tanAngleExpr));
			solver.addConstraint(new CosConstraint(angleExpr, cosAngleExpr));
			solver.addConstraint(new SinConstraint(angleExpr, sinAngleExpr));
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Double.NEGATIVE_INFINITY, tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Double.POSITIVE_INFINITY, tanAngle.getMax(), 0.0001);
			assertTrue("cosAngle bound", cosAngle.isBound());
			assertEquals("cosAngle min", 0, cosAngle.getMin(), 0.0001);
			assertEquals("cosAngle max", 0, cosAngle.getMax(), 0.0001);
			assertTrue("sinAngle bound", sinAngle.isBound());
			assertEquals("sinAngle min", Math.sin(0.5 * NumConstants.PI), sinAngle.getMin(), 0.0001);
			assertEquals("sinAngle max", Math.sin(0.5 * NumConstants.PI), sinAngle.getMax(), 0.0001);
			assertTrue("angle bound", angle.isBound());
			assertEquals("angle min", NumConstants.HALF_PI, angle.getMin(), 0.0001);
			assertEquals("angle max", NumConstants.HALF_PI, angle.getMax(), 0.0001);
			
			// [0.625pi]
			solver.clear();
			angle.setMin(0);
			angle.setMax(2 * NumConstants.PI);
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			solver.addVariable(cosAngle);
			solver.addVariable(sinAngle);
			sinAngle.setMin(Math.sin(0.625 * NumConstants.PI));
			sinAngle.setMax(Math.sin(0.625 * NumConstants.PI));
			solver.addConstraint(new TanConstraint(angleExpr, tanAngleExpr));
			solver.addConstraint(new CosConstraint(angleExpr, cosAngleExpr));
			solver.addConstraint(new SinConstraint(angleExpr, sinAngleExpr));
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Double.NEGATIVE_INFINITY, tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Double.POSITIVE_INFINITY, tanAngle.getMax(), 0.0001);
			assertFalse("cosAngle bound", cosAngle.isBound());
			assertEquals("cosAngle min", Math.cos(0.625 * NumConstants.PI), cosAngle.getMin(), 0.0001);
			assertEquals("cosAngle max", Math.cos(0.375 * NumConstants.PI), cosAngle.getMax(), 0.0001);
			assertTrue("sinAngle bound", sinAngle.isBound());
			assertEquals("sinAngle min", Math.sin(0.625 * NumConstants.PI), sinAngle.getMin(), 0.0001);
			assertEquals("sinAngle max", Math.sin(0.625 * NumConstants.PI), sinAngle.getMax(), 0.0001);
			assertEquals("sinAngle max", Math.sin(0.375 * NumConstants.PI), sinAngle.getMax(), 0.0001);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", 0.375 * NumConstants.PI, angle.getMin(), 0.0001);
			assertEquals("angle max", 0.625 * NumConstants.PI, angle.getMax(), 0.0001);
			
			// [0.75pi]
			solver.clear();
			angle.setMin(0);
			angle.setMax(2 * NumConstants.PI);
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			solver.addVariable(cosAngle);
			solver.addVariable(sinAngle);
			sinAngle.setMin(Math.sin(0.75 * NumConstants.PI));
			sinAngle.setMax(Math.sin(0.75 * NumConstants.PI));
			solver.addConstraint(new TanConstraint(angleExpr, tanAngleExpr));
			solver.addConstraint(new CosConstraint(angleExpr, cosAngleExpr));
			solver.addConstraint(new SinConstraint(angleExpr, sinAngleExpr));
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Double.NEGATIVE_INFINITY, tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Double.POSITIVE_INFINITY, tanAngle.getMax(), 0.0001);
			assertFalse("cosAngle bound", cosAngle.isBound());
			assertEquals("cosAngle min", Math.cos(0.75 * NumConstants.PI), cosAngle.getMin(), 0.0001);
			assertEquals("cosAngle max", Math.cos(0.25 * NumConstants.PI), cosAngle.getMax(), 0.0001);
			assertTrue("sinAngle bound", sinAngle.isBound());
			assertEquals("sinAngle min", Math.sin(0.75 * NumConstants.PI), sinAngle.getMin(), 0.0001);
			assertEquals("sinAngle max", Math.sin(0.75 * NumConstants.PI), sinAngle.getMax(), 0.0001);
			assertEquals("sinAngle max", Math.sin(0.25 * NumConstants.PI), sinAngle.getMax(), 0.0001);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", 0.25 * NumConstants.PI, angle.getMin(), 0.0001);
			assertEquals("angle max", 0.75 * NumConstants.PI, angle.getMax(), 0.0001);
			
			// [0.875pi]
			solver.clear();
			angle.setMin(0);
			angle.setMax(2 * NumConstants.PI);
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			solver.addVariable(cosAngle);
			solver.addVariable(sinAngle);
			sinAngle.setMin(Math.sin(0.875 * NumConstants.PI));
			sinAngle.setMax(Math.sin(0.875 * NumConstants.PI));
			solver.addConstraint(new TanConstraint(angleExpr, tanAngleExpr));
			solver.addConstraint(new CosConstraint(angleExpr, cosAngleExpr));
			solver.addConstraint(new SinConstraint(angleExpr, sinAngleExpr));
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Double.NEGATIVE_INFINITY, tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Double.POSITIVE_INFINITY, tanAngle.getMax(), 0.0001);
			assertFalse("cosAngle bound", cosAngle.isBound());
			assertEquals("cosAngle min", Math.cos(0.875 * NumConstants.PI), cosAngle.getMin(), 0.0001);
			assertEquals("cosAngle max", Math.cos(0.125 * NumConstants.PI), cosAngle.getMax(), 0.0001);
			assertTrue("sinAngle bound", sinAngle.isBound());
			assertEquals("sinAngle min", Math.sin(0.875 * NumConstants.PI), sinAngle.getMin(), 0.0001);
			assertEquals("sinAngle max", Math.sin(0.875 * NumConstants.PI), sinAngle.getMax(), 0.0001);
			assertEquals("sinAngle max", Math.sin(0.125 * NumConstants.PI), sinAngle.getMax(), 0.0001);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", 0.125 * NumConstants.PI, angle.getMin(), 0.0001);
			assertEquals("angle max", 0.875 * NumConstants.PI, angle.getMax(), 0.0001);
			
			// [pi]
			solver.clear();
			angle.setMin(0);
			angle.setMax(2 * NumConstants.PI);
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			solver.addVariable(cosAngle);
			solver.addVariable(sinAngle);
			sinAngle.setMin(Math.sin(NumConstants.PI));
			sinAngle.setMax(Math.sin(NumConstants.PI));
			solver.addConstraint(new TanConstraint(angleExpr, tanAngleExpr));
			solver.addConstraint(new CosConstraint(angleExpr, cosAngleExpr));
			solver.addConstraint(new SinConstraint(angleExpr, sinAngleExpr));
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Double.NEGATIVE_INFINITY, tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Double.POSITIVE_INFINITY, tanAngle.getMax(), 0.0001);
			assertFalse("cosAngle bound", cosAngle.isBound());
			assertEquals("cosAngle min", -1, cosAngle.getMin(), 0.0001);
			assertEquals("cosAngle max", 1, cosAngle.getMax(), 0.0001);
			assertTrue("sinAngle bound", sinAngle.isBound());
			assertEquals("sinAngle min", 0, sinAngle.getMin(), 0.0001);
			assertEquals("sinAngle max", 0, sinAngle.getMax(), 0.0001);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", 0, angle.getMin(), 0.0001);
			assertEquals("angle max", 2 * NumConstants.PI, angle.getMax(), 0.0001);
			
			
			// [1.125pi]
			solver.clear();
			angle.setMin(0);
			angle.setMax(2 * NumConstants.PI);
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			solver.addVariable(cosAngle);
			solver.addVariable(sinAngle);
			sinAngle.setMin(Math.sin(1.125 * NumConstants.PI));
			sinAngle.setMax(Math.sin(1.125 * NumConstants.PI));
			solver.addConstraint(new TanConstraint(angleExpr, tanAngleExpr));
			solver.addConstraint(new CosConstraint(angleExpr, cosAngleExpr));
			solver.addConstraint(new SinConstraint(angleExpr, sinAngleExpr));
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Double.NEGATIVE_INFINITY, tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Double.POSITIVE_INFINITY, tanAngle.getMax(), 0.0001);
			assertFalse("cosAngle bound", cosAngle.isBound());
			assertEquals("cosAngle min", Math.cos(1.125 * NumConstants.PI), cosAngle.getMin(), 0.0001);
			assertEquals("cosAngle max", Math.cos(1.875 * NumConstants.PI), cosAngle.getMax(), 0.0001);
			assertTrue("sinAngle bound", sinAngle.isBound());
			assertEquals("sinAngle min", Math.sin(1.125 * NumConstants.PI), sinAngle.getMin(), 0.0001);
			assertEquals("sinAngle max", Math.sin(1.125 * NumConstants.PI), sinAngle.getMax(), 0.0001);
			assertEquals("sinAngle max", Math.sin(1.875 * NumConstants.PI), sinAngle.getMax(), 0.0001);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", 1.125 * NumConstants.PI, angle.getMin(), 0.0001);
			assertEquals("angle max", 1.875 * NumConstants.PI, angle.getMax(), 0.0001);
			
			// [1.25pi]
			solver.clear();
			angle.setMin(0);
			angle.setMax(2 * NumConstants.PI);
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			solver.addVariable(cosAngle);
			solver.addVariable(sinAngle);
			sinAngle.setMin(Math.sin(1.25 * NumConstants.PI));
			sinAngle.setMax(Math.sin(1.25 * NumConstants.PI));
			solver.addConstraint(new TanConstraint(angleExpr, tanAngleExpr));
			solver.addConstraint(new CosConstraint(angleExpr, cosAngleExpr));
			solver.addConstraint(new SinConstraint(angleExpr, sinAngleExpr));
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Double.NEGATIVE_INFINITY, tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Double.POSITIVE_INFINITY, tanAngle.getMax(), 0.0001);
			assertFalse("cosAngle bound", cosAngle.isBound());
			assertEquals("cosAngle min", Math.cos(1.25 * NumConstants.PI), cosAngle.getMin(), 0.0001);
			assertEquals("cosAngle max", Math.cos(1.75 * NumConstants.PI), cosAngle.getMax(), 0.0001);
			assertTrue("sinAngle bound", sinAngle.isBound());
			assertEquals("sinAngle min", Math.sin(1.25 * NumConstants.PI), sinAngle.getMin(), 0.0001);
			assertEquals("sinAngle max", Math.sin(1.25 * NumConstants.PI), sinAngle.getMax(), 0.0001);
			assertEquals("sinAngle max", Math.sin(1.75 * NumConstants.PI), sinAngle.getMax(), 0.0001);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", 1.25 * NumConstants.PI, angle.getMin(), 0.0001);
			assertEquals("angle max", 1.75 * NumConstants.PI, angle.getMax(), 0.0001);
			
			// [1.375pi]
			solver.clear();
			angle.setMin(0);
			angle.setMax(2 * NumConstants.PI);
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			solver.addVariable(cosAngle);
			solver.addVariable(sinAngle);
			sinAngle.setMin(Math.sin(1.375 * NumConstants.PI));
			sinAngle.setMax(Math.sin(1.375 * NumConstants.PI));
			solver.addConstraint(new TanConstraint(angleExpr, tanAngleExpr));
			solver.addConstraint(new CosConstraint(angleExpr, cosAngleExpr));
			solver.addConstraint(new SinConstraint(angleExpr, sinAngleExpr));
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Double.NEGATIVE_INFINITY, tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Double.POSITIVE_INFINITY, tanAngle.getMax(), 0.0001);
			assertFalse("cosAngle bound", cosAngle.isBound());
			assertEquals("cosAngle min", Math.cos(1.375 * NumConstants.PI), cosAngle.getMin(), 0.0001);
			assertEquals("cosAngle max", Math.cos(1.625 * NumConstants.PI), cosAngle.getMax(), 0.0001);
			assertTrue("sinAngle bound", sinAngle.isBound());
			assertEquals("sinAngle min", Math.sin(1.375 * NumConstants.PI), sinAngle.getMin(), 0.0001);
			assertEquals("sinAngle max", Math.sin(1.375 * NumConstants.PI), sinAngle.getMax(), 0.0001);
			assertEquals("sinAngle max", Math.sin(1.625 * NumConstants.PI), sinAngle.getMax(), 0.0001);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", 1.375 * NumConstants.PI, angle.getMin(), 0.0001);
			assertEquals("angle max", 1.625 * NumConstants.PI, angle.getMax(), 0.0001);
			
			// [1.5pi]
			solver.clear();
			angle.setMin(0);
			angle.setMax(2 * NumConstants.PI);
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			solver.addVariable(cosAngle);
			solver.addVariable(sinAngle);
			sinAngle.setMin(Math.sin(1.5 * NumConstants.PI));
			sinAngle.setMax(Math.sin(1.5 * NumConstants.PI));
			solver.addConstraint(new TanConstraint(angleExpr, tanAngleExpr));
			solver.addConstraint(new CosConstraint(angleExpr, cosAngleExpr));
			solver.addConstraint(new SinConstraint(angleExpr, sinAngleExpr));
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Double.NEGATIVE_INFINITY, tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Double.POSITIVE_INFINITY, tanAngle.getMax(), 0.0001);
			assertTrue("cosAngle bound", cosAngle.isBound());
			assertEquals("cosAngle min", 0, cosAngle.getMin(), 0.0001);
			assertEquals("cosAngle max", 0, cosAngle.getMax(), 0.0001);
			assertTrue("sinAngle bound", sinAngle.isBound());
			assertEquals("sinAngle min", -1, sinAngle.getMin(), 0.0001);
			assertEquals("sinAngle max", -1, sinAngle.getMax(), 0.0001);
			assertTrue("angle bound", angle.isBound());
			assertEquals("angle min", 1.5 * NumConstants.PI, angle.getMin(), 0.0001);
			assertEquals("angle max", 1.5 * NumConstants.PI, angle.getMax(), 0.0001);
			
			// [1.625pi]
			solver.clear();
			angle.setMin(0);
			angle.setMax(2 * NumConstants.PI);
			solver.addVariable(angle);
			solver.addVariable(tanAngle);
			solver.addVariable(cosAngle);
			solver.addVariable(sinAngle);
			sinAngle.setMin(Math.sin(1.625 * NumConstants.PI));
			sinAngle.setMax(Math.sin(1.625 * NumConstants.PI));
			solver.addConstraint(new TanConstraint(angleExpr, tanAngleExpr));
			solver.addConstraint(new CosConstraint(angleExpr, cosAngleExpr));
			solver.addConstraint(new SinConstraint(angleExpr, sinAngleExpr));
			assertFalse("tanAngle bound", tanAngle.isBound());
			assertEquals("tanAngle min", Double.NEGATIVE_INFINITY, tanAngle.getMin(), 0.0001);
			assertEquals("tanAngle max", Double.POSITIVE_INFINITY, tanAngle.getMax(), 0.0001);
			assertFalse("cosAngle bound", cosAngle.isBound());
			assertEquals("cosAngle min", Math.cos(1.375 * NumConstants.PI), cosAngle.getMin(), 0.0001);
			assertEquals("cosAngle max", Math.cos(1.625 * NumConstants.PI), cosAngle.getMax(), 0.0001);
			assertTrue("sinAngle bound", sinAngle.isBound());
			assertEquals("sinAngle min", Math.sin(1.375 * NumConstants.PI), sinAngle.getMin(), 0.0001);
			assertEquals("sinAngle max", Math.sin(1.375 * NumConstants.PI), sinAngle.getMax(), 0.0001);
			assertEquals("sinAngle max", Math.sin(1.625 * NumConstants.PI), sinAngle.getMax(), 0.0001);
			assertFalse("angle bound", angle.isBound());
			assertEquals("angle min", 1.375 * NumConstants.PI, angle.getMin(), 0.0001);
			assertEquals("angle max", 1.625 * NumConstants.PI, angle.getMax(), 0.0001);
		}
		catch(PropagationFailureException propx) {
			propx.printStackTrace();
			fail();
		}
	}
}
