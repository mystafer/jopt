package jopt.csp.spi.arcalgorithm.variable;

import java.util.Arrays;

import jopt.csp.spi.arcalgorithm.constraint.num.GenericNumExpr;
import jopt.csp.spi.arcalgorithm.constraint.num.NumExpr;
import jopt.csp.spi.arcalgorithm.constraint.num.NumNotBetweenConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.ThreeVarConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.global.NumBetweenConstraint;
import jopt.csp.spi.arcalgorithm.domain.DomainChangeSource;
import jopt.csp.spi.arcalgorithm.domain.SummationFloatDomainExpression;
import jopt.csp.spi.arcalgorithm.graph.node.GenericFloatNode;
import jopt.csp.spi.arcalgorithm.graph.node.GenericNumNode;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.util.GenericIndex;
import jopt.csp.spi.util.IndexIterator;
import jopt.csp.spi.util.NumConstants;
import jopt.csp.spi.util.NumOperation;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.CspDoubleExpr;
import jopt.csp.variable.CspFloatCast;
import jopt.csp.variable.CspFloatExpr;
import jopt.csp.variable.CspGenericDoubleCast;
import jopt.csp.variable.CspGenericDoubleConstant;
import jopt.csp.variable.CspGenericDoubleExpr;
import jopt.csp.variable.CspGenericFloatCast;
import jopt.csp.variable.CspGenericFloatConstant;
import jopt.csp.variable.CspGenericFloatExpr;
import jopt.csp.variable.CspGenericIndex;

/**
 * A generic integer variable such as Xi which represents X1, X2, etc.
 */
public class GenericFloatExpr extends GenericNumExprBase implements CspGenericFloatExpr, CspGenericFloatCast, SummationFloatDomainExpression {
    protected GenericNumNode genericNode;
    
    /**
     * Constructor
     * 
     * @param name      unique name of this node
     * @param indices   array of indices that generic node is based upon
     * @param exprs     array of expressions that this generic expression wraps
     */
    public GenericFloatExpr(String name, CspGenericIndex indices[], NumExpr exprs[]) {
        super(name, indices, exprs, NumConstants.FLOAT);
    }
    
    /**
     * Constructor
     * 
     * @param name      unique name of this node
     * @param indices   array of indices that generic node is based upon
     * @param exprs     array of expressions that this generic expression wraps
     */
    public GenericFloatExpr(String name, CspGenericIndex indices[], NumExpr exprs[], boolean isSummation) {
        super(name, indices, exprs, NumConstants.FLOAT);
        
        if (isSummation) {
            this.operation=NumOperation.SUMMATION;
        }
    }

    // Constructors for building expressions with other expression types
    public GenericFloatExpr(String name, CspGenericFloatCast aexpr, int operation, FloatCast bexpr) {
        super(name, aexpr, operation, bexpr, NumConstants.FLOAT);
    }
    public GenericFloatExpr(String name, CspGenericFloatCast aexpr, int operation, CspGenericFloatCast bexpr) {
        super(name, aexpr, operation, bexpr, NumConstants.FLOAT);
    }
    public GenericFloatExpr(String name, FloatCast aexpr, int operation, CspGenericFloatCast bexpr) {
        super(name, aexpr, operation, bexpr, NumConstants.FLOAT);
    }
    public GenericFloatExpr(String name, float a, int operation, CspGenericFloatCast bexpr) {
        super(name, new Float(a), operation, bexpr, NumConstants.FLOAT);
    }
    public GenericFloatExpr(String name, CspGenericFloatCast aexpr, int operation, float b) {
        super(name, aexpr, operation, new Float(b), NumConstants.FLOAT);
    }
    public GenericFloatExpr(String name, GenericFloatConstant a, int operation, CspGenericFloatCast bexpr) {
        super(name, a, operation, bexpr, NumConstants.FLOAT);
    }
    public GenericFloatExpr(String name, CspGenericFloatCast aexpr, int operation, GenericFloatConstant b) {
        super(name, aexpr, operation, b, NumConstants.FLOAT);
    }    
    public GenericFloatExpr(String name, CspGenericFloatCast aexpr, int operation) {
        super(name, aexpr, operation, NumConstants.FLOAT);
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
                FloatCast b = (FloatCast) gbexpr.getNumExpressionForIndex();
                exprs[currOffset] = new FloatExpr(null, currentAConst.intValue(), operation, b);
            }
            else if ((bconst!=null)||(bGenConst!=null)) {
                Number currentBConst = bconst;
                if (bGenConst!=null) {
                    currentBConst = bGenConst.getNumberForIndex();
                }
                FloatCast a = (FloatCast) gaexpr.getNumExpressionForIndex();
                exprs[currOffset] = new FloatExpr(null, a, operation, currentBConst.intValue());
            }
            else if (aexpr instanceof FloatCast){
                FloatCast b = (FloatCast) gbexpr.getNumExpressionForIndex();
                exprs[currOffset] = new FloatExpr(null, (FloatCast)aexpr, operation, b);
            }
            else if (bexpr instanceof FloatCast){
                FloatCast a = (FloatCast) gaexpr.getNumExpressionForIndex();
                exprs[currOffset] = new FloatExpr(null, a, operation, (FloatCast)bexpr);
            }
            else if (bexpr==null) {
                FloatCast a = (FloatCast) gaexpr.getNumExpressionForIndex();
                exprs[currOffset] = new FloatExpr(null, a, operation);
            }
            else {
                FloatCast a = (FloatCast) gaexpr.getNumExpressionForIndex();
                FloatCast b = (FloatCast) gbexpr.getNumExpressionForIndex();
                exprs[currOffset] = new FloatExpr(null, a, operation, b);
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
            genericNode = new GenericFloatNode(name, indices, getExpressionNodes());
        return genericNode;
    }
    
    /**
     * Returns the type of expression: Int, Long, etc.
     */
    public int getNumberType() {
        return NumConstants.FLOAT;
    }
    
    /**
     * Returns minimal value of expression
     */
    public float getMin() {
    	return getSmallestMin();
    }

    /**
     * Returns maximum value of expression
     */
    public float getMax() {
    	return getLargestMax();
    }

    /**
     * Returns the minimum value of the expression that corresponds
     * to the current index combination
     */
    public float getDomainMinForIndex() {
        return ((FloatExpr) getNumExpressionForIndex()).getMin();
    }
    
    /**
     * Returns the maximum value of the expression that corresponds
     * to the current index combination
     */
    public float getDomainMaxForIndex() {
        return ((FloatExpr) getNumExpressionForIndex()).getMax();
    }
    
    /**
     * Returns a source for registering a domain change listener
     * corresponding to the current index combination
     */
    public DomainChangeSource getDomainChangeSourceForIndex() {
        return (DomainChangeSource) ((FloatExpr) getNumExpressionForIndex()).getDomain();
    }
    
    /**
     * Returns an expression representing the sum of this expression
     * with a static value
     */
    public CspFloatExpr add(float f) {
        return new GenericFloatExpr(null, this, NumOperation.ADD, f);
    }
    
    /**
     * Returns an expression representing the sum of this expression
     * with a static value
     */
    public CspGenericFloatExpr add(CspGenericFloatConstant f) {
        return new GenericFloatExpr(null, this, NumOperation.ADD, (GenericFloatConstant)f);
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
        return new GenericDoubleExpr(null, this, NumOperation.ADD, (GenericDoubleConstant)d);
    }
    
    /**
     * Returns an expression representing the sum of this expression
     * with another expression
     */
    public CspFloatExpr add(CspFloatCast expr) {
        if (expr instanceof CspGenericFloatCast)
            return new GenericFloatExpr(null, this, NumOperation.ADD, (CspGenericFloatCast)expr);
        else
            return new GenericFloatExpr(null, this, NumOperation.ADD, (FloatCast)expr);
    }

    /**
     * Returns an expression representing the sum of this expression
     * with another expression
     */
    public CspDoubleExpr add(CspDoubleExpr expr) {
        if (expr instanceof CspGenericDoubleCast)
            return new GenericDoubleExpr(null, this, NumOperation.ADD, (CspGenericDoubleCast)expr);
        else
            return new GenericDoubleExpr(null, this, NumOperation.ADD, (DoubleCast)expr);
    }

    /**
     * Returns an expression representing the difference of this expression
     * with a static value
     */
    public CspFloatExpr subtract(float f) {
        return new GenericFloatExpr(null, this, NumOperation.SUBTRACT, f);
    }
    
    /**
     * Returns an expression representing the difference of this expression
     * with a static value
     */
    public CspGenericFloatExpr subtract(CspGenericFloatConstant f) {
        return new GenericFloatExpr(null, this, NumOperation.SUBTRACT, (GenericFloatConstant)f);
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
        return new GenericDoubleExpr(null, this, NumOperation.SUBTRACT, (GenericDoubleConstant) d);
    }
    
    /**
     * Returns an expression representing the difference of this expression
     * with another expression
     */
    public CspFloatExpr subtract(CspFloatCast expr) {
        if (expr instanceof CspGenericDoubleCast)
            return new GenericFloatExpr(null, this, NumOperation.SUBTRACT, (CspGenericFloatCast)expr);
        else
            return new GenericFloatExpr(null, this, NumOperation.SUBTRACT, (FloatCast)expr);
    }

    /**
     * Returns an expression representing the difference of this expression
     * with another expression
     */
    public CspDoubleExpr subtract(CspDoubleExpr expr) {
        if (expr instanceof CspGenericDoubleCast)
            return new GenericDoubleExpr(null, this, NumOperation.SUBTRACT, (CspGenericDoubleCast)expr);
        else
            return new GenericDoubleExpr(null, this, NumOperation.SUBTRACT, (DoubleCast)expr);
    }
    
    /**
     * Returns an expression representing the product of this expression
     * with a static value
     */
    public CspFloatExpr multiply(float f) {
        return new GenericFloatExpr(null, this, NumOperation.MULTIPLY, f);
    }

    /**
     * Returns an expression representing the product of this expression
     * with a static value
     */
    public CspGenericFloatExpr multiply(CspGenericFloatConstant f) {
        return new GenericFloatExpr(null, this, NumOperation.MULTIPLY, (GenericFloatConstant)f);
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
    public CspFloatExpr multiply(CspFloatCast expr) {
        if (expr == this)
            return new GenericFloatExpr(null, this, NumOperation.SQUARE);
        else if (expr instanceof CspGenericFloatCast)
            return new GenericFloatExpr(null, this, NumOperation.MULTIPLY, (CspGenericFloatCast)expr);
        else
            return new GenericFloatExpr(null, this, NumOperation.MULTIPLY, (FloatCast)expr);
    }

    /**
     * Returns an expression representing the product of this expression
     * with another expression
     */
    public CspDoubleExpr multiply(CspDoubleExpr expr) {
        if (expr instanceof CspGenericDoubleCast)
            return new GenericDoubleExpr(null, this, NumOperation.MULTIPLY, (CspGenericDoubleCast)expr);
        else
            return new GenericDoubleExpr(null, this, NumOperation.MULTIPLY, (DoubleCast)expr);
    }

    /**
     * Returns an expression representing the quotient of this expression
     * with a static value
     */
    public CspFloatExpr divide(float f) {
        return new GenericFloatExpr(null, this, NumOperation.DIVIDE, f);
    }
    
    /**
     * Returns an expression representing the quotient of this expression
     * with a static value
     */
    public CspGenericFloatExpr divide(CspGenericFloatConstant f) {
        return new GenericFloatExpr(null, this, NumOperation.DIVIDE, (GenericFloatConstant) f);
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
        return new GenericDoubleExpr(null, this, NumOperation.DIVIDE, (GenericDoubleConstant) d);
    }
    
    /**
     * Returns an expression representing the quotient of this expression
     * with another expression
     */
    public CspFloatExpr divide(CspFloatCast expr) {
        if (expr instanceof CspGenericFloatCast)
            return new GenericFloatExpr(null, this, NumOperation.DIVIDE, (CspGenericFloatCast)expr);
        else
            return new GenericFloatExpr(null, this, NumOperation.DIVIDE, (FloatCast)expr);
    }

    /**
     * Returns an expression representing the quotient of this expression
     * with another expression
     */
    public CspDoubleExpr divide(CspDoubleExpr expr) {
        if (expr instanceof CspGenericDoubleCast)
            return new GenericDoubleExpr(null, this, NumOperation.DIVIDE, (CspGenericDoubleCast)expr);
        else
            return new GenericDoubleExpr(null, this, NumOperation.DIVIDE, (DoubleCast)expr);
    }

    /**
     * Returns the internal variable corresponding to the associated index's current value
     */
    public CspFloatCast getExpressionForIndex() {
    	return (CspFloatCast) super.getNumExpressionForIndex();
    }

    /**
     * returns a numeric expression from the internal array
     * 
     * @param offset  Offset of expression in internal expression array
     */
    public CspFloatCast getExpression(int offset) {
    	return (CspFloatCast) getNumExpression(offset);
    }

    /**
     * Returns that largest maximal value of all variables in array
     */
    public float getLargestMax() {
        return getNumLargestMax().floatValue();
    }
    
    /**
     * Returns that largest maximal value of all variables in array within
     * start and end indices
     */
    public float getLargestMax(int start, int end) {
        return getNumLargestMax(start, end).floatValue();
    }

    /**
     * Returns that smallest maximal value of all variables in array
     */
    public float getSmallestMax() {
        return getNumSmallestMax().floatValue();
    }
    
    /**
     * Returns that smallest maximal value of all variables in array within
     * start and end indices
     */
    public float getSmallestMax(int start, int end) {
        return getNumSmallestMax(start, end).floatValue();
    }

    /**
     * Returns that largest minimal value of all variables in array
     */
    public float getLargestMin() {
        return getNumLargestMin().floatValue();
    }
    
    /**
     * Returns that largest minimal value of all variables in array within
     * start and end indices
     */
    public float getLargestMin(int start, int end) {
        return getNumLargestMin(start, end).floatValue();
    }

    /**
     * Returns that smallest minimal value of all variables in array
     */
    public float getSmallestMin() {
        return getNumSmallestMin().floatValue();
    }
    
    /**
     * Returns that smallest minimal value of all variables in array within
     * start and end indices
     */
    public float getSmallestMin(int start, int end) {
        return getNumSmallestMin(start, end).floatValue();
    }
    
    /**
     * Returns constraint restricting this expression to a value
     */
    public CspConstraint eq(float val) {
        return constraint(new Float(val), ThreeVarConstraint.EQ);
    }

    /**
     * Returns constraint restricting this expression to a value
     */
    public CspConstraint eq(CspGenericFloatConstant vals) {
        return constraint((GenericFloatConstant)vals, ThreeVarConstraint.EQ);
    }
    
    /**
     * Returns constraint restricting this expression to values below
     * and including a given maximum
     */
    public CspConstraint leq(float val) {
        return constraint(new Float(val), ThreeVarConstraint.LEQ);
    }
    
    /**
     * Returns constraint restricting this expression to values below
     * and including a given maximum
     */
    public CspConstraint leq(CspGenericFloatConstant vals) {
        return constraint((GenericFloatConstant)vals, ThreeVarConstraint.LEQ);
    }

    /**
     * Returns constraint restricting this expression to values above
     * and including a given minimum
     */
    public CspConstraint geq(float val) {
        return constraint(new Float(val), ThreeVarConstraint.GEQ);
    }
    
    /**
     * Returns constraint restricting this expression to values above
     * and including a given minimum
     */
    public CspConstraint geq(CspGenericFloatConstant vals) {
        return constraint((GenericFloatConstant)vals, ThreeVarConstraint.GEQ);
    }
    
    // javadoc inherited
    public CspConstraint between(float min, boolean minExclusive, float max, boolean maxExclusive) {
        return new NumBetweenConstraint(new Float(min), minExclusive, new Float(max), maxExclusive, this);
    }
    
    // javadoc inherited
    public CspConstraint between(float min, float max) {
        return new NumBetweenConstraint(new Float(min), false, new Float(max), false, this);
    }
    
    // javadoc inherited
    public CspConstraint notBetween(float min, boolean minExclusive, float max, boolean maxExclusive) {
        return new NumNotBetweenConstraint(new Float(min), minExclusive, new Float(max), maxExclusive, this);
    }
    
    // javadoc inherited
    public CspConstraint notBetween(float min, float max) {
        return new NumNotBetweenConstraint(new Float(min), false, new Float(max), false, this);
    }
    
    //javadoc inherited
    public CspConstraint strictlyBetween(CspGenericFloatConstant min, CspGenericFloatConstant max) {
        return new NumBetweenConstraint(min, true, max, true, this);
    }
    
   //javadoc inherited
    public CspConstraint inBetween(CspGenericFloatConstant min, CspGenericFloatConstant max){
        return new NumBetweenConstraint(min, false, max, false, this);
    }

    // javadoc inherited from GenericNumExprBase
    protected GenericNumExpr createTypeSpecificExpr(String name, GenericIndex indices[], NumExpr exprs[]) {
        return new GenericFloatExpr(name, indices, exprs);
    }
}
