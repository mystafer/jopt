/*
 * GenericNumNode.java
 * 
 * Created on Mar 19, 2005
 */
package jopt.csp.spi.arcalgorithm.variable;

import java.util.Arrays;

import jopt.csp.spi.arcalgorithm.constraint.num.GenericNumExpr;
import jopt.csp.spi.arcalgorithm.constraint.num.NumExpr;
import jopt.csp.spi.arcalgorithm.constraint.num.NumNotBetweenConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.ThreeVarConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.global.NumBetweenConstraint;
import jopt.csp.spi.arcalgorithm.domain.DomainChangeSource;
import jopt.csp.spi.arcalgorithm.domain.SummationDoubleDomainExpression;
import jopt.csp.spi.arcalgorithm.graph.node.GenericDoubleNode;
import jopt.csp.spi.arcalgorithm.graph.node.GenericNumNode;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.util.GenericIndex;
import jopt.csp.spi.util.IndexIterator;
import jopt.csp.spi.util.NumConstants;
import jopt.csp.spi.util.NumOperation;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.CspDoubleCast;
import jopt.csp.variable.CspDoubleExpr;
import jopt.csp.variable.CspGenericDoubleCast;
import jopt.csp.variable.CspGenericDoubleConstant;
import jopt.csp.variable.CspGenericDoubleExpr;
import jopt.csp.variable.CspGenericIndex;

/**
 * A generic integer variable such as Xi which represents X1, X2, etc.
 */
public class GenericDoubleExpr extends GenericNumExprBase implements  CspGenericDoubleExpr, SummationDoubleDomainExpression {
    protected GenericNumNode genericNode;
    
    /**
     * Constructor
     * 
     * @param name      unique name of this node
     * @param indices   array of indices that generic node is based upon
     * @param exprs     array of expressions that this generic expression wraps
     */
    public GenericDoubleExpr(String name, CspGenericIndex indices[], NumExpr exprs[]) {
        super(name, indices, exprs, NumConstants.DOUBLE);
    }
    
    /**
     * Constructor
     * 
     * @param name      unique name of this node
     * @param indices   array of indices that generic node is based upon
     * @param exprs     array of expressions that this generic expression wraps
     */
    public GenericDoubleExpr(String name, CspGenericIndex indices[], NumExpr exprs[], boolean isSummation) {
        super(name, indices, exprs, NumConstants.DOUBLE);
        
        if (isSummation) {
            this.operation=NumOperation.SUMMATION;
        }
    }

    // Constructors for building expressions with other expression types
    public GenericDoubleExpr(String name, CspGenericDoubleCast aexpr, int operation, DoubleCast bexpr) {
        super(name, aexpr, operation, bexpr, NumConstants.DOUBLE);
    }
    public GenericDoubleExpr(String name, CspGenericDoubleCast aexpr, int operation, CspGenericDoubleCast bexpr) {
        super(name, aexpr, operation, bexpr, NumConstants.DOUBLE);
    }
    public GenericDoubleExpr(String name, DoubleCast aexpr, int operation, CspGenericDoubleCast bexpr) {
        super(name, aexpr, operation, bexpr, NumConstants.DOUBLE);
    }
    public GenericDoubleExpr(String name, double a, int operation, CspGenericDoubleCast bexpr) {
        super(name, new Double(a), operation, bexpr, NumConstants.DOUBLE);
    }
    public GenericDoubleExpr(String name, GenericDoubleConstant a, int operation, CspGenericDoubleCast bexpr) {
        super(name, a, operation, bexpr, NumConstants.DOUBLE);
    }
    public GenericDoubleExpr(String name, CspGenericDoubleCast aexpr, int operation, double b) {
        super(name, aexpr, operation, new Double(b), NumConstants.DOUBLE);
    }
    public GenericDoubleExpr(String name, CspGenericDoubleCast aexpr, int operation, GenericDoubleConstant b) {
        super(name, aexpr, operation, b, NumConstants.DOUBLE);
    }
    public GenericDoubleExpr(String name, CspGenericDoubleCast aexpr, int operation) {
        super(name, aexpr, operation, NumConstants.DOUBLE);
    }

    /**
     * Function that is used to produce an array of calculated expressions for use
     * when math operations have been used to create this expression
     */
    protected void generateCalculatedExpressions() {
        IndexIterator idxIterator = new IndexIterator(Arrays.asList(indices));
        int currOffset = 0;
        while (idxIterator.hasNext()) {
        	idxIterator.next();
            
            // create expressions based on input values
            if ((aconst!=null)||(aGenConst!=null)) {
                Number currentAConst = aconst;
                if (aGenConst!=null) {
                    currentAConst = aGenConst.getNumberForIndex();
                }
                DoubleCast b = (DoubleCast) gbexpr.getNumExpressionForIndex();
                exprs[currOffset] = new DoubleExpr(null, currentAConst.intValue(), operation, b);
            }
            else if ((bconst!=null)||(bGenConst!=null)) {
                Number currentBConst = bconst;
                if (bGenConst!=null) {
                    currentBConst = bGenConst.getNumberForIndex();
                }
                DoubleCast a = (DoubleCast) gaexpr.getNumExpressionForIndex();
                exprs[currOffset] = new DoubleExpr(null, a, operation, currentBConst.intValue());
            }
            else if (aexpr instanceof DoubleCast){
                DoubleCast b = (DoubleCast) gbexpr.getNumExpressionForIndex();
                exprs[currOffset] = new DoubleExpr(null, (DoubleCast)aexpr, operation, b);
            }
            else if (bexpr instanceof DoubleCast){
                DoubleCast a = (DoubleCast) gaexpr.getNumExpressionForIndex();
                exprs[currOffset] = new DoubleExpr(null, a, operation, (DoubleCast)bexpr);
            }
            else if (bexpr==null) {
                DoubleCast a = (DoubleCast) gaexpr.getNumExpressionForIndex();
                exprs[currOffset] = new DoubleExpr(null, a, operation);
            }
            else {
                DoubleCast a = (DoubleCast) gaexpr.getNumExpressionForIndex();
                DoubleCast b = (DoubleCast) gbexpr.getNumExpressionForIndex();
                exprs[currOffset] = new DoubleExpr(null, a, operation, b);
            }
            
            // update current offset
            currOffset++;
        }
    }
    
    /**
     * Returns generic node for this variable
     */
    public Node getNode() {
        if (genericNode==null)
            genericNode = new GenericDoubleNode(name, indices, getExpressionNodes());
        return genericNode;
    }
    
    /**
     * Returns the type of expression: Int, Long, etc.
     */
    public int getNumberType() {
        return NumConstants.DOUBLE;
    }
    
    /**
     * Returns minimal value of expression
     */
    public double getMin() {
    	return getSmallestMin();
    }

    /**
     * Returns maximum value of expression
     */
    public double getMax() {
    	return getLargestMax();
    }

    /**
     * Returns the minimum value of the expression that corresponds
     * to the current index combination
     */
    public double getDomainMinForIndex() {
        return ((DoubleExpr) getNumExpressionForIndex()).getMin();
    }
    
    /**
     * Returns the maximum value of the expression that corresponds
     * to the current index combination
     */
    public double getDomainMaxForIndex() {
        return ((DoubleExpr) getNumExpressionForIndex()).getMax();
    }
    
    /**
     * Returns a source for registering a domain change listener
     * corresponding to the current index combination
     */
    public DomainChangeSource getDomainChangeSourceForIndex() {
        return (DomainChangeSource) ((DoubleExpr) getNumExpressionForIndex()).getDomain();
    }
    
    /**
     * Returns an expression representing the quotient of this expression
     * with another expression
     */
    public CspDoubleExpr divide(CspDoubleExpr expr) {
        return null;
    }

    /**
     * Returns the internal variable corresponding to the associated index's current value
     */
    public CspDoubleCast getExpressionForIndex() {
    	return (CspDoubleCast) super.getNumExpressionForIndex();
    }

    /**
     * returns a numeric expression from the internal array
     * 
     * @param offset  Offset of expression in internal expression array
     */
    public CspDoubleExpr getExpression(int offset) {
    	return (CspDoubleExpr) getNumExpression(offset);
    }

    /**
     * Returns that largest maximal value of all variables in array
     */
    public double getLargestMax() {
        return getNumLargestMax().doubleValue();
    }
    
    /**
     * Returns that largest maximal value of all variables in array within
     * start and end indices
     */
    public double getLargestMax(int start, int end) {
        return getNumLargestMax(start, end).doubleValue();
    }

    /**
     * Returns that smallest maximal value of all variables in array
     */
    public double getSmallestMax() {
        return getNumSmallestMax().doubleValue();
    }
    
    /**
     * Returns that smallest maximal value of all variables in array within
     * start and end indices
     */
    public double getSmallestMax(int start, int end) {
        return getNumSmallestMax(start, end).doubleValue();
    }

    /**
     * Returns that largest minimal value of all variables in array
     */
    public double getLargestMin() {
        return getNumLargestMin().doubleValue();
    }
    
    /**
     * Returns that largest minimal value of all variables in array within
     * start and end indices
     */
    public double getLargestMin(int start, int end) {
        return getNumLargestMin(start, end).doubleValue();
    }

    /**
     * Returns that smallest minimal value of all variables in array
     */
    public double getSmallestMin() {
        return getNumSmallestMin().doubleValue();
    }
    
    /**
     * Returns that smallest minimal value of all variables in array within
     * start and end indices
     */
    public double getSmallestMin(int start, int end) {
        return getNumSmallestMin(start, end).doubleValue();
    }
    
    /**
     * Returns an expression representing the sum of this expression
     * with a static value
     */
    public CspDoubleExpr add(double d) {
        return new GenericDoubleExpr(null, this, NumOperation.ADD, d);
    }
    
    /**
     * Returns an expression representing the sum of this expression
     * with a static value
     */
    public CspGenericDoubleExpr add(CspGenericDoubleConstant d) {
        return new GenericDoubleExpr(null, this, NumOperation.ADD, (GenericDoubleExpr)d);
    }    

    /**
     * Returns an expression representing the sum of this expression
     * with another expression
     */
    public CspDoubleExpr add(CspDoubleCast expr) {
        if (expr instanceof CspGenericDoubleCast)
            return new GenericDoubleExpr(null, this, NumOperation.ADD, (CspGenericDoubleCast) expr);
        else
        	return new GenericDoubleExpr(null, this, NumOperation.ADD, (DoubleCast)expr);
    }

    /**
     * Returns an expression representing the difference of this expression
     * with a static value
     */
    public CspDoubleExpr subtract(double d) {
        return new GenericDoubleExpr(null, this, NumOperation.SUBTRACT, d);
    }
    
    /**
     * Returns an expression representing the difference of this expression
     * with a static value
     */
    public CspGenericDoubleExpr subtract(CspGenericDoubleConstant d) {
        return new GenericDoubleExpr(null, this, NumOperation.SUBTRACT, (GenericDoubleConstant )d);
    }    

    /**
     * Returns an expression representing the difference of this expression
     * with another expression
     */
    public CspDoubleExpr subtract(CspDoubleCast expr) {
        if (expr instanceof CspGenericDoubleCast)
            return new GenericDoubleExpr(null, this, NumOperation.SUBTRACT, (CspGenericDoubleCast) expr);
        else
        	return new GenericDoubleExpr(null, this, NumOperation.SUBTRACT, (DoubleCast)expr);
    }
    
    /**
     * Returns an expression representing the product of this expression
     * with a static value
     */
    public CspDoubleExpr multiply(double d) {
        return new GenericDoubleExpr(null, this, NumOperation.MULTIPLY, d);
    }
    
    /**
     * Returns an expression representing the product of this expression
     * with a static value
     */
    public CspGenericDoubleExpr multiply(CspGenericDoubleConstant d) {
        return new GenericDoubleExpr(null, this, NumOperation.MULTIPLY, (GenericDoubleConstant )d);
    }    

    /**
     * Returns an expression representing the product of this expression
     * with another expression
     */
    public CspDoubleExpr multiply(CspDoubleCast expr) {
        if (expr == this)
            return new GenericDoubleExpr(null, this, NumOperation.SQUARE);
        else if (expr == this)
            return new GenericDoubleExpr(null, this, NumOperation.SQUARE);
        else
            return new GenericDoubleExpr(null, this, NumOperation.MULTIPLY, (DoubleCast)expr);
    }
    
    /**
     * Returns an expression representing the quotient of this expression
     * with a static value
     */
    public CspDoubleExpr divide(double d) {
        return new GenericDoubleExpr(null, this, NumOperation.DIVIDE, d);
    }
    
    /**
     * Returns an expression representing the quotient of this expression
     * with a static value
     */
    public CspGenericDoubleExpr divide(CspGenericDoubleConstant d) {
        return new GenericDoubleExpr(null, this, NumOperation.DIVIDE, (GenericDoubleConstant )d);
    }    

    /**
     * Returns an expression representing the quotient of this expression
     * with another expression
     */
    public CspDoubleExpr divide(CspDoubleCast expr) {
        if (expr instanceof CspGenericDoubleCast)
            return new GenericDoubleExpr(null, this, NumOperation.DIVIDE, (CspGenericDoubleCast) expr);
        else
        	return new GenericDoubleExpr(null, this, NumOperation.DIVIDE, (DoubleCast)expr);
    }
    
    /**
     * Returns constraint restricting this expression to a value
     */
    public CspConstraint eq(double val) {
        return constraint(new Double(val), ThreeVarConstraint.EQ);
    }
    
    /**
     * Returns constraint restricting this expression to a value
     */
    public CspConstraint eq(CspGenericDoubleConstant vals) {
        return constraint((GenericDoubleConstant)vals, ThreeVarConstraint.EQ);
    }    

    /**
     * Returns constraint restricting this expression to values below
     * and including a given maximum
     */
    public CspConstraint leq(double val) {
        return constraint(new Double(val), ThreeVarConstraint.LEQ);
    }

    /**
     * Returns constraint restricting this expression to values below
     * and including a given maximum
     */
    public CspConstraint leq(CspGenericDoubleConstant vals) {
        return constraint((GenericDoubleConstant)vals, ThreeVarConstraint.LEQ);
    }
    
    /**
     * Returns constraint restricting this expression to values above
     * and including a given minimum
     */
    public CspConstraint geq(double val) {
        return constraint(new Double(val), ThreeVarConstraint.GEQ);
    }

    /**
     * Returns constraint restricting this expression to values above
     * and including a given minimum
     */
    public CspConstraint geq(CspGenericDoubleConstant vals) {
        return constraint((GenericDoubleConstant)vals, ThreeVarConstraint.GEQ);
    }
    
    // javadoc inherited
    public CspConstraint between(double min, boolean minExclusive, double max, boolean maxExclusive) {
        return new NumBetweenConstraint(new Double(min), minExclusive, new Double(max), maxExclusive, this);
    }
    
    // javadoc inherited
    public CspConstraint between(double min, double max) {
        return new NumBetweenConstraint(new Double(min), false, new Double(max), false, this);
    }
    
    // javadoc inherited
    public CspConstraint notBetween(double min, boolean minExclusive, double max, boolean maxExclusive) {
        return new NumNotBetweenConstraint(new Double(min), minExclusive, new Double(max), maxExclusive, this);
    }
    
    // javadoc inherited
    public CspConstraint notBetween(double min, double max) {
        return new NumNotBetweenConstraint(new Double(min), false, new Double(max), false, this);
    }
    
    //javadoc inherited
    public CspConstraint strictlyBetween(CspGenericDoubleConstant min, CspGenericDoubleConstant max) {
        return new NumBetweenConstraint(min, true, max, true, this);
    }
    
    //javadoc inherited
    public CspConstraint inBetween(CspGenericDoubleConstant min, CspGenericDoubleConstant max){
        return new NumBetweenConstraint(min, false, max, false, this);
    }
    // javadoc inherited from GenericNumExprBase
    protected GenericNumExpr createTypeSpecificExpr(String name, GenericIndex indices[], NumExpr exprs[]) {
        return new GenericDoubleExpr(name, indices, exprs);
    }
}
