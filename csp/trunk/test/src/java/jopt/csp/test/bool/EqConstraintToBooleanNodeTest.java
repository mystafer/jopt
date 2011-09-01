package jopt.csp.test.bool;

import jopt.csp.spi.arcalgorithm.domain.BooleanIntDomain;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.arc.PostableConstraint;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericConstraint2BoolArc;
import jopt.csp.spi.arcalgorithm.graph.node.BooleanNode;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.arcalgorithm.variable.BooleanExpr;
import jopt.csp.spi.arcalgorithm.variable.IntVariable;
import jopt.csp.spi.util.NameUtil;
import jopt.csp.variable.CspBooleanExpr;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

/**
 * Tests some different comparison techniques on the
 * TernarnyNumSumArc
 * 
 * @author Nick
 */
public class EqConstraintToBooleanNodeTest extends TestCase {
	private boolean constraintPosted;
	private boolean oppositePosted;
	
    public EqConstraintToBooleanNodeTest(java.lang.String testName) {
        super(testName);
    }

    /**
     * Tests bounds consistency on Z = X + Y
     * 
     * min(z) = min(x) + min(y)
     * max(z) = max(x) + max(y)
     */
    public void testConstraintToBoolPropogation() {
        try {
            IntVariable x = new IntVariable("x", 0, 100);
            IntVariable y = new IntVariable("y", 0, 100);

            NumNode xnode = (NumNode)x.getNode();
            NumNode ynode = (NumNode)y.getNode();
            
            BooleanNode boolNode = new BooleanNode("Test", new BooleanIntDomain());
            CspConstraint constraint = ((x.add(y)).eq(3));
            GenericConstraint2BoolArc boolCon = new GenericConstraint2BoolArc((PostableConstraint)constraint, boolNode);
            
            xnode.setMin(new Integer(5));
            xnode.setMax(new Integer(20));
            
            ynode.setMin(new Integer(20));
            ynode.setMax(new Integer(70));
            
            
            //NumArc arc = new TernaryNumSumArc(xnode, ynode, znode, NodeMath.INTEGER, NumArc.EQ);
            //
            //arc.propagateBounds();
            
             
            
            /*assertEquals("x min", new Integer(5), xnode.getMin());
            assertEquals("x max", new Integer(20), xnode.getMax());
            assertEquals("y min", new Integer(20), ynode.getMin());
            assertEquals("y max", new Integer(70), ynode.getMax());
            assertEquals("z min", new Integer(25), znode.getMin());
            assertEquals("z max", new Integer(90), znode.getMax());*/
            assertFalse("the constraint is not true", constraint.isTrue());
            assertTrue("the constraint is false", constraint.isFalse());
            
            assertFalse("the constraint is not true", boolNode.isTrue());
            assertFalse("the constraint is not false", boolNode.isFalse());
            
            boolCon.propagate();
            assertFalse("the constraint is not true", boolNode.isTrue());
            assertTrue("the constraint is not false", boolNode.isFalse());
            
        }
        catch(PropagationFailureException propx) {
            fail();
        }
    }
    public void testBoolToConstraintPropogationTrue() {
        try {
            BooleanNode boolNode = new BooleanNode("Test", new BooleanIntDomain());
            //CspConstraint constraint = ((x.add(y)).eq(3));
            BoolToVarConstraint constraint = new BoolToVarConstraint(false);
            GenericConstraint2BoolArc boolCon = new GenericConstraint2BoolArc(boolNode,(BoolToVarConstraint)constraint);
            
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is false", constraint.isFalse());
            
            assertFalse("the constraint is not true", boolNode.isTrue());
            assertFalse("the constraint is not false", boolNode.isFalse());
            boolNode.setTrue();
            boolCon.propagate();
            
            assertTrue("the constraint was posted", constraintPosted);
            assertFalse("the opposite was posted", oppositePosted);
        }
        catch(PropagationFailureException propx) {
            fail();
        }
    }
    
    public void testBoolToConstraintPropogationFalse() {
        try {
            BooleanNode boolNode = new BooleanNode("Test", new BooleanIntDomain());
            BoolToVarConstraint constraint = new BoolToVarConstraint(false);
            GenericConstraint2BoolArc boolCon = new GenericConstraint2BoolArc(boolNode,(BoolToVarConstraint)constraint);
            
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is false", constraint.isFalse());
            
            assertFalse("the constraint is not true", boolNode.isTrue());
            assertFalse("the constraint is not false", boolNode.isFalse());
            boolNode.setFalse();
            boolCon.propagate();
            
            assertFalse("the constraint was posted", constraintPosted);
            assertTrue("the opposite was posted", oppositePosted);
        }
        catch(PropagationFailureException propx) {
            fail();
        }
    }
    
    public void setUp() {
    	constraintPosted = false;
    	oppositePosted = false;
    }

    private class BoolToVarConstraint implements PostableConstraint{
    	private boolean isOpposite;
    	
        public BoolToVarConstraint(boolean isOpposite) {
        	this.isOpposite = isOpposite;
        }
    	
    	public boolean isTrue()
        {
        	return false;
        }
        public boolean isFalse()
        {
        	return false;
        }	
    	public void postToGraph() {
        	if (isOpposite)
        		oppositePosted = true;
        	else
        		constraintPosted = true;
        }
        public PostableConstraint getPostableOpposite() {
        	return new BoolToVarConstraint(!isOpposite);
        }
        public Node[] getBooleanSourceNodes() {
        	return null;
        }
        public Arc[] getBooleanSourceArcs() {
        	return null;
        }
        
        
        public CspBooleanExpr toBoolean() {
        	return new BooleanExpr(NameUtil.nextName(), this);
        }
    }

}
