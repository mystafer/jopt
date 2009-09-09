package jopt.csp.spi.arcalgorithm.variable;

import java.util.Arrays;

import jopt.csp.spi.arcalgorithm.constraint.num.GenericNumExpr;
import jopt.csp.spi.arcalgorithm.constraint.num.NumExpr;
import jopt.csp.spi.arcalgorithm.constraint.num.NumNotBetweenConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.ThreeVarConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.global.NumBetweenConstraint;
import jopt.csp.spi.arcalgorithm.domain.DomainChangeSource;
import jopt.csp.spi.arcalgorithm.domain.SummationLongDomainExpression;
import jopt.csp.spi.arcalgorithm.graph.node.GenericLongNode;
import jopt.csp.spi.arcalgorithm.graph.node.GenericNumNode;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.util.GenericIndex;
import jopt.csp.spi.util.IndexIterator;
import jopt.csp.spi.util.NumConstants;
import jopt.csp.spi.util.NumOperation;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.CspDoubleExpr;
import jopt.csp.variable.CspFloatExpr;
import jopt.csp.variable.CspGenericDoubleCast;
import jopt.csp.variable.CspGenericFloatCast;
import jopt.csp.variable.CspGenericIndex;
import jopt.csp.variable.CspGenericLongCast;
import jopt.csp.variable.CspGenericLongConstant;
import jopt.csp.variable.CspGenericLongExpr;
import jopt.csp.variable.CspLongCast;
import jopt.csp.variable.CspLongExpr;

/**
 * A generic long variable such as Xi which represents X1, X2, etc.
 */
public class GenericLongExpr extends GenericNumExprBase implements CspGenericLongExpr, CspGenericLongCast, SummationLongDomainExpression {
    protected GenericNumNode genericNode;
    
    /**
     * Constructor
     * 
     * @param name      unique name of this node
     * @param indices   array of indices that generic node is based upon
     * @param exprs     array of expressions that this generic expression wraps
     */
    public GenericLongExpr(String name, CspGenericIndex indices[], NumExpr exprs[]) {
        super(name, indices, exprs, NumConstants.LONG);
    }
    
    /**
     * Constructor
     * 
     * @param name      unique name of this node
     * @param indices   array of indices that generic node is based upon
     * @param exprs     array of expressions that this generic expression wraps
     */
    public GenericLongExpr(String name, CspGenericIndex indices[], NumExpr exprs[], boolean isSummation) {
        super(name, indices, exprs, NumConstants.LONG);
        
        if (isSummation) {
            this.operation=NumOperation.SUMMATION;
        }
    }

    // Constructors for building expressions with other expression types
    public GenericLongExpr(String name, CspGenericLongCast aexpr, int operation, LongCast bexpr) {
        super(name, aexpr, operation, bexpr, NumConstants.LONG);
    }
    public GenericLongExpr(String name, CspGenericLongCast aexpr, int operation, CspGenericLongCast bexpr) {
        super(name, aexpr, operation, bexpr, NumConstants.LONG);
    }
    public GenericLongExpr(String name, LongCast aexpr, int operation, CspGenericLongCast bexpr) {
        super(name, aexpr, operation, bexpr, NumConstants.LONG);
    }
    public GenericLongExpr(String name, long a, int operation, CspGenericLongCast bexpr) {
        super(name, new Long(a), operation, bexpr, NumConstants.LONG);
    }
    public GenericLongExpr(String name, CspGenericLongCast aexpr, int operation, long b) {
        super(name, aexpr, operation, new Long(b), NumConstants.LONG);
    }    
    public GenericLongExpr(String name, GenericLongConstant a, int operation, CspGenericLongCast bexpr) {
        super(name, a, operation, bexpr, NumConstants.LONG);
    }
    public GenericLongExpr(String name, CspGenericLongCast aexpr, int operation, GenericLongConstant b) {
        super(name, aexpr, operation, b, NumConstants.LONG);
    }
    public GenericLongExpr(String name, CspGenericLongCast aexpr, int operation) {
        super(name, aexpr, operation, NumConstants.LONG);
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
                LongCast b = (LongCast) gbexpr.getNumExpressionForIndex();
                exprs[currOffset] = new LongExpr(null, currentAConst.intValue(), operation, b);
            }
            else if ((bconst!=null)||(bGenConst!=null)) {
                Number currentBConst = bconst;
                if (bGenConst!=null) {
                    currentBConst = bGenConst.getNumberForIndex();
                }
                LongCast a = (LongCast) gaexpr.getNumExpressionForIndex();
                exprs[currOffset] = new LongExpr(null, a, operation, currentBConst.intValue());
            }
            else if (aexpr instanceof LongCast){
                LongCast b = (LongCast) gbexpr.getNumExpressionForIndex();
                exprs[currOffset] = new LongExpr(null, (LongCast)aexpr, operation, b);
            }
            else if (bexpr instanceof LongCast){
                LongCast a = (LongCast) gaexpr.getNumExpressionForIndex();
                exprs[currOffset] = new LongExpr(null, a, operation, (LongCast)bexpr);
            }
            else if (bexpr==null) {
                LongCast a = (LongCast) gaexpr.getNumExpressionForIndex();
                exprs[currOffset] = new LongExpr(null, a, operation);
            }
            else {
                LongCast a = (LongCast) gaexpr.getNumExpressionForIndex();
                LongCast b = (LongCast) gbexpr.getNumExpressionForIndex();
                exprs[currOffset] = new LongExpr(null, a, operation, b);
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
            genericNode = new GenericLongNode(name, indices, getExpressionNodes());
        return genericNode;
    }
    
    /**
     * Returns the type of expression: Int, Long, etc.
     */
    public int getNumberType() {
        return NumConstants.LONG;
    }
    
    /**
     * Returns minimal value of expression
     */
    public long getMin() {
    	return getSmallestMin();
    }

    /**
     * Returns maximum value of expression
     */
    public long getMax() {
    	return getLargestMax();
    }

    /**
     * Returns the minimum value of the expression that corresponds
     * to the current index combination
     */
    public long getDomainMinForIndex() {
        return ((LongExpr) getNumExpressionForIndex()).getMin();
    }
    
    /**
     * Returns the maximum value of the expression that corresponds
     * to the current index combination
     */
    public long getDomainMaxForIndex() {
        return ((LongExpr) getNumExpressionForIndex()).getMax();
    }
    
    /**
     * Returns a source for registering a domain change listener
     * corresponding to the current index combination
     */
    public DomainChangeSource getDomainChangeSourceForIndex() {
        return (DomainChangeSource) ((LongExpr) getNumExpressionForIndex()).getDomain();
    }
    
    /**
     * Returns an expression representing the sum of this expression
     * with a static value
     */
    public CspLongExpr add(long l) {
        return new GenericLongExpr(null, this, NumOperation.ADD, l);
    }
    
    /**
     * Returns an expression representing the sum of this expression
     * with a static value
     */
    public CspGenericLongExpr add(CspGenericLongConstant l) {
        return new GenericLongExpr(null, this, NumOperation.ADD, (GenericLongConstant)l);
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
    public CspDoubleExpr add(double d) {
        return new GenericDoubleExpr(null, this, NumOperation.ADD, d);
    }

    /**
     * Returns an expression representing the sum of this expression
     * with another expression
     */
    public CspLongExpr add(CspLongCast expr) {
        if (expr instanceof CspGenericLongCast)
            return new GenericLongExpr(null, this, NumOperation.ADD, (CspGenericLongCast)expr);
        else
            return new GenericLongExpr(null, this, NumOperation.ADD, (LongCast)expr);
    }

    /**
     * Returns an expression representing the sum of this expression
     * with another expression
     */
    public CspFloatExpr add(CspFloatExpr expr) {
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
    public CspLongExpr subtract(long l) {
        return new GenericLongExpr(null, this, NumOperation.SUBTRACT, l);
    }
    
    /**
     * Returns an expression representing the difference of this expression
     * with a static value
     */
    public CspGenericLongExpr subtract(CspGenericLongConstant l) {
        return new GenericLongExpr(null, this, NumOperation.SUBTRACT, (GenericLongConstant)l);
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
    public CspDoubleExpr subtract(double d) {
        return new GenericDoubleExpr(null, this, NumOperation.SUBTRACT, d);
    }

    /**
     * Returns an expression representing the difference of this expression
     * with another expression
     */
    public CspLongExpr subtract(CspLongCast expr) {
        if (expr instanceof CspGenericLongCast)
            return new GenericLongExpr(null, this, NumOperation.SUBTRACT, (CspGenericLongCast)expr);
        else
            return new GenericLongExpr(null, this, NumOperation.SUBTRACT, (LongCast)expr);
    }

    /**
     * Returns an expression representing the difference of this expression
     * with another expression
     */
    public CspFloatExpr subtract(CspFloatExpr expr) {
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
    public CspLongExpr multiply(long l) {
        return new GenericLongExpr(null, this, NumOperation.MULTIPLY, l);
    }
    
    /**
     * Returns an expression representing the product of this expression
     * with a static value
     */
    public CspGenericLongExpr multiply(CspGenericLongConstant l) {
        return new GenericLongExpr(null, this, NumOperation.MULTIPLY, (GenericLongConstant)l);
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
    public CspDoubleExpr multiply(double d) {
        return new GenericDoubleExpr(null, this, NumOperation.MULTIPLY, d);
    }

    /**
     * Returns an expression representing the product of this expression
     * with another expression
     */
    public CspLongExpr multiply(CspLongCast expr) {
        if (expr == this)
            return new GenericLongExpr(null, this, NumOperation.SQUARE);
        else if (expr instanceof CspGenericLongCast)
            return new GenericLongExpr(null, this, NumOperation.MULTIPLY, (CspGenericLongCast)expr);
        else
            return new GenericLongExpr(null, this, NumOperation.MULTIPLY, (LongCast)expr);
    }

    /**
     * Returns an expression representing the product of this expression
     * with another expression
     */
    public CspFloatExpr multiply(CspFloatExpr expr) {
        if (expr instanceof CspGenericFloatCast)
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
    public CspLongExpr divide(long l) {
        return new GenericLongExpr(null, this, NumOperation.DIVIDE, l);
    }
    
    /**
     * Returns an expression representing the quotient of this expression
     * with a static value
     */
    public CspGenericLongExpr divide(CspGenericLongConstant l) {
        return new GenericLongExpr(null, this, NumOperation.DIVIDE, (GenericLongConstant)l);
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
    public CspDoubleExpr divide(double d) {
        return new GenericDoubleExpr(null, this, NumOperation.DIVIDE, d);
    }

    /**
     * Returns an expression representing the quotient of this expression
     * with another expression
     */
    public CspLongExpr divide(CspLongCast expr) {
        if (expr instanceof CspGenericLongCast)
            return new GenericLongExpr(null, this, NumOperation.DIVIDE, (CspGenericLongCast)expr);
        else
            return new GenericLongExpr(null, this, NumOperation.DIVIDE, (LongCast)expr);
    }

    /**
     * Returns an expression representing the quotient of this expression
     * with another expression
     */
    public CspFloatExpr divide(CspFloatExpr expr) {
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
    public CspLongCast getExpressionForIndex() {
    	return (CspLongCast) super.getNumExpressionForIndex();
    }

    /**
     * returns a numeric expression from the internal array
     * 
     * @param offset  Offset of expression in internal expression array
     */
    public CspLongCast getExpression(int offset) {
    	return (CspLongCast) getNumExpression(offset);
    }

    /**
     * Returns that largest maximal value of all variables in array
     */
    public long getLargestMax() {
        return getNumLargestMax().longValue();
    }
    
    /**
     * Returns that largest maximal value of all variables in array within
     * start and end indices
     */
    public long getLargestMax(int start, int end) {
        return getNumLargestMax(start, end).longValue();
    }

    /**
     * Returns that smallest maximal value of all variables in array
     */
    public long getSmallestMax() {
        return getNumSmallestMax().longValue();
    }
    
    /**
     * Returns that smallest maximal value of all variables in array within
     * start and end indices
     */
    public long getSmallestMax(int start, int end) {
        return getNumSmallestMax(start, end).longValue();
    }

    /**
     * Returns that largest minimal value of all variables in array
     */
    public long getLargestMin() {
        return getNumLargestMin().longValue();
    }
    
    /**
     * Returns that largest minimal value of all variables in array within
     * start and end indices
     */
    public long getLargestMin(int start, int end) {
        return getNumLargestMin(start, end).longValue();
    }

    /**
     * Returns that smallest minimal value of all variables in array
     */
    public long getSmallestMin() {
        return getNumSmallestMin().longValue();
    }
    
    /**
     * Returns that smallest minimal value of all variables in array within
     * start and end indices
     */
    public long getSmallestMin(int start, int end) {
        return getNumSmallestMin(start, end).longValue();
    }
    
    /**
     * Returns constraint restricting this expression to a value
     */
    public CspConstraint eq(long val) {
        return constraint(new Long(val), ThreeVarConstraint.EQ);
    }
    
    /**
     * Returns constraint restricting this expression to a value
     */
    public CspConstraint eq(CspGenericLongConstant vals) {
        return constraint((GenericLongConstant)vals, ThreeVarConstraint.EQ);
    }    

    /**
     * Returns constraint restricting this expression to values below
     * a given maximum
     */
    public CspConstraint lt(long val) {
        return constraint(new Long(val), ThreeVarConstraint.LT);
    }

    /**
     * Returns constraint restricting this expression to values below
     * a given maximum
     */
    public CspConstraint lt(CspGenericLongConstant vals) {
        return constraint((GenericLongConstant)vals, ThreeVarConstraint.LT);
    }
    
    /**
     * Returns constraint restricting this expression to values below
     * and including a given maximum
     */
    public CspConstraint leq(long val) {
        return constraint(new Long(val), ThreeVarConstraint.LEQ);
    }
    
    /**
     * Returns constraint restricting this expression to values below
     * and including a given maximum
     */
    public CspConstraint leq(CspGenericLongConstant vals) {
        return constraint((GenericLongConstant)vals, ThreeVarConstraint.LEQ);
    }

    /**
     * Returns constraint restricting this expression to values above
     * a given minimum
     */
    public CspConstraint gt(long val) {
        return constraint(new Long(val), ThreeVarConstraint.GT);
    }

    /**
     * Returns constraint restricting this expression to values above
     * a given minimum
     */
    public CspConstraint gt(CspGenericLongConstant vals) {
        return constraint((GenericLongConstant)vals, ThreeVarConstraint.GT);
    }
    
    /**
     * Returns constraint restricting this expression to values above
     * and including a given minimum
     */
    public CspConstraint geq(long val) {
        return constraint(new Long(val), ThreeVarConstraint.GEQ);
    }

    /**
     * Returns constraint restricting this expression to values above
     * and including a given minimum
     */
    public CspConstraint geq(CspGenericLongConstant vals) {
        return constraint((GenericLongConstant)vals, ThreeVarConstraint.GEQ);
    }
    
    /**
     * Returns constraint restricting this expression to all values
     * not equivalent to supplied value
     */
    public CspConstraint neq(long val) {
        return constraint(new Long(val), ThreeVarConstraint.NEQ);
    }
    
    /**
     * Returns constraint restricting this expression to all values
     * not equivalent to supplied value
     */
    public CspConstraint neq(CspGenericLongConstant vals) {
        return constraint((GenericLongConstant)vals, ThreeVarConstraint.NEQ);
    }
    
    // javadoc inherited
    public CspConstraint between(long min, boolean minExclusive, long max, boolean maxExclusive) {
        return new NumBetweenConstraint(new Long(min), minExclusive, new Long(max), maxExclusive, this);
    }
    
    // javadoc inherited
    public CspConstraint between(long min, long max) {
        return new NumBetweenConstraint(new Long(min), false, new Long(max), false, this);
    }
    
    // javadoc inherited
    public CspConstraint notBetween(long min, boolean minExclusive, long max, boolean maxExclusive) {
        return new NumNotBetweenConstraint(new Long(min), minExclusive, new Long(max), maxExclusive, this);
    }
    
    // javadoc inherited
    public CspConstraint notBetween(long min, long max) {
        return new NumNotBetweenConstraint(new Long(min), false, new Long(max), false, this);
    }
    
    // javadoc inherited from GenericNumExprBase
    protected GenericNumExpr createTypeSpecificExpr(String name, GenericIndex indices[], NumExpr exprs[]) {
        return new GenericLongExpr(name, indices, exprs);
    }
}
