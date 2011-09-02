package jopt.csp.spi.arcalgorithm.variable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import jopt.csp.spi.arcalgorithm.constraint.num.GenericNumExpr;
import jopt.csp.spi.arcalgorithm.constraint.num.NumExpr;
import jopt.csp.spi.arcalgorithm.constraint.num.NumNotBetweenConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.ThreeVarConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.global.NumBetweenConstraint;
import jopt.csp.spi.arcalgorithm.domain.DomainChangeSource;
import jopt.csp.spi.arcalgorithm.domain.SummationIntDomainExpression;
import jopt.csp.spi.arcalgorithm.graph.node.GenericIntNode;
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
import jopt.csp.variable.CspGenericDoubleConstant;
import jopt.csp.variable.CspGenericDoubleExpr;
import jopt.csp.variable.CspGenericFloatCast;
import jopt.csp.variable.CspGenericFloatConstant;
import jopt.csp.variable.CspGenericFloatExpr;
import jopt.csp.variable.CspGenericIndex;
import jopt.csp.variable.CspGenericIntConstant;
import jopt.csp.variable.CspGenericIntExpr;
import jopt.csp.variable.CspGenericLongCast;
import jopt.csp.variable.CspGenericLongConstant;
import jopt.csp.variable.CspGenericLongExpr;
import jopt.csp.variable.CspIntExpr;
import jopt.csp.variable.CspLongExpr;

/**
 * A generic integer variable such as Xi which represents X1, X2, etc.
 */
public class GenericIntExpr extends GenericNumExprBase implements CspGenericIntExpr, CspGenericLongCast, CspIntExpr, SummationIntDomainExpression {
    protected GenericNumNode genericNode;
    
    /**
     * Constructor
     * 
     * @param name      unique name of this node
     * @param indices   array of indices that generic node is based upon
     * @param exprs     array of expressions that this generic expression wraps
     */
    public GenericIntExpr(String name, CspGenericIndex indices[], NumExpr exprs[], boolean isSummation) {
        super(name, indices, exprs, NumConstants.INTEGER);
/*  This piece of code effectively allows the distinction of classifying this
 *  particular GenericIntExpr as being built from a summation.  This allows for expressions 
 *  to be built that use the sumamtion directly, instead of using an intermediate NumRelation
 *  constraint.  Implementing in this way should improve performance by eliminating heavily used arcs.
 * This is removed due to changes in the way that Summations are calculated using deltas, and will be layered
 * in after the above implementation is completed.
 */ 
//        if (isSummation) {
//            this.operation=NumOperation.SUMMATION;
//        }
    }
    
    /**
     * Constructor
     * 
     * @param name      unique name of this node
     * @param indices   array of indices that generic node is based upon
     * @param exprs     array of expressions that this generic expression wraps
     */
    public GenericIntExpr(String name, CspGenericIndex indices[], NumExpr exprs[]) {
        this(name,indices,exprs,false);
    }

    // Constructors for building expressions with other expression types
    public GenericIntExpr(String name, CspGenericIntExpr aexpr, int operation, CspIntExpr bexpr) {
        super(name, aexpr, operation, bexpr, NumConstants.INTEGER);
    }
    public GenericIntExpr(String name, CspGenericIntExpr aexpr, int operation, CspGenericIntExpr bexpr) {
        super(name, aexpr, operation, bexpr, NumConstants.INTEGER);
    }
    public GenericIntExpr(String name, CspIntExpr aexpr, int operation, CspGenericIntExpr bexpr) {
        super(name, aexpr, operation, bexpr, NumConstants.INTEGER);
    }
    public GenericIntExpr(String name, int a, int operation, CspGenericIntExpr bexpr) {
        super(name, new Integer(a), operation, bexpr, NumConstants.INTEGER);
    }
    public GenericIntExpr(String name, CspGenericIntExpr aexpr, int operation, int b) {
        super(name, aexpr, operation, new Integer(b), NumConstants.INTEGER);
    }
    public GenericIntExpr(String name, GenericIntConstant a, int operation, CspGenericIntExpr bexpr) {
        super(name, a, operation, bexpr, NumConstants.INTEGER);
    }
    public GenericIntExpr(String name, CspGenericIntExpr aexpr, int operation, GenericIntConstant b) {
        super(name, aexpr, operation, b, NumConstants.INTEGER);
    }    
    
    public GenericIntExpr(String name, CspGenericIntExpr aexpr, int operation) {
        super(name, aexpr, operation, NumConstants.INTEGER);
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
            if ((aconst!=null) || (aGenConst!=null)) {
                Number currentAConst = aconst;
                if (aGenConst != null) {
                    currentAConst = aGenConst.getNumberForIndex();
                }
                IntExpr b = (IntExpr) gbexpr.getNumExpressionForIndex();
                exprs[currOffset] = new IntExpr(null, currentAConst.intValue(), operation, b);
            }
            else if ((bconst!=null)||(bGenConst!=null)) {
                Number currentBConst = bconst;
                if (bGenConst != null) {
                    currentBConst = bGenConst.getNumberForIndex();
                }
                IntExpr a = (IntExpr) gaexpr.getNumExpressionForIndex();
                exprs[currOffset] = new IntExpr(null, a, operation, currentBConst.intValue());
            }
            else if (aexpr instanceof IntExpr){
                IntExpr b = (IntExpr) gbexpr.getNumExpressionForIndex();
                exprs[currOffset] = new IntExpr(null, (IntExpr)aexpr, operation, b);
            }
            else if (bexpr instanceof IntExpr){
                IntExpr a = (IntExpr) gaexpr.getNumExpressionForIndex();
                exprs[currOffset] = new IntExpr(null, a, operation, (IntExpr)bexpr);
            }
            else if (bexpr==null) {
                IntExpr a = (IntExpr) gaexpr.getNumExpressionForIndex();
                exprs[currOffset] = new IntExpr(null, a, operation);
            }
            else {
                IntExpr a = (IntExpr) gaexpr.getNumExpressionForIndex();
                IntExpr b = (IntExpr) gbexpr.getNumExpressionForIndex();
                exprs[currOffset] = new IntExpr(null, a, operation, b);
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
            genericNode = new GenericIntNode(name, indices, getExpressionNodes());
        return genericNode;
    }
    
    /**
     * Returns the type of expression: Int, Long, etc.
     */
    public int getNumberType() {
        return NumConstants.INTEGER;
    }
    
    /**
     * Returns minimal value of expression
     */
    public int getMin() {
    	return getSmallestMin();
    }

    /**
     * Returns maximum value of expression
     */
    public int getMax() {
    	return getLargestMax();
    }
    
    /**
     * Returns the minimum value of the expression that corresponds
     * to the current index combination
     */
    public int getDomainMinForIndex() {
        return ((IntExpr) getNumExpressionForIndex()).getMin();
    }
    
    /**
     * Returns the maximum value of the expression that corresponds
     * to the current index combination
     */
    public int getDomainMaxForIndex() {
        return ((IntExpr) getNumExpressionForIndex()).getMax();
    }
    
    /**
     * Returns a source for registering a domain change listener
     * corresponding to the current index combination
     */
    public DomainChangeSource getDomainChangeSourceForIndex() {
        return (DomainChangeSource) ((IntExpr) getNumExpressionForIndex()).getDomain();
    }
    
    /**
     * Returns an expression representing the sum of this expression
     * with a static value
     */
    public CspIntExpr add(int i) {
        return new GenericIntExpr(null, this, NumOperation.ADD, i);
    }

    /**
     * Returns an expression representing the sum of this expression
     * with a static generic value
     */
    public CspGenericIntExpr add(CspGenericIntConstant i) {
        return new GenericIntExpr(null, this, NumOperation.ADD, (GenericIntConstant)i);
    }
    
    /**
     * Returns an expression representing the sum of this expression
     * with a static generic value
     */
    public CspGenericIntExpr add(CspGenericIntExpr i) {
        return new GenericIntExpr(null, this, NumOperation.ADD, (GenericIntExpr)i);
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
     * with a static generic value
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
     * with a static generic value
     */
    public CspGenericFloatExpr add(CspGenericFloatConstant f) {
        return new GenericFloatExpr(null, this, NumOperation.ADD, (GenericFloatConstant)f);
    }

    /**
     * Returns an expression representing the sum of this expression
     * with a static generic value
     */
    public CspDoubleExpr add(double d) {
        return new GenericDoubleExpr(null, this, NumOperation.ADD, d);
    }
    
    /**
     * Returns an expression representing the sum of this expression
     * with a static generic value
     */
    public CspGenericDoubleExpr add(CspGenericDoubleConstant d) {
        return new GenericDoubleExpr(null, this, NumOperation.ADD, (GenericDoubleConstant)d);
    }    

    /**
     * Returns an expression representing the sum of this expression
     * with another expression
     */
    public CspIntExpr add(CspIntExpr expr) {
        if (expr instanceof CspGenericIntExpr)
        	return new GenericIntExpr(null, this, NumOperation.ADD, (CspGenericIntExpr)expr);
        else
            return new GenericIntExpr(null, this, NumOperation.ADD, (IntExpr)expr);
    }

    /**
     * Returns an expression representing the sum of this expression
     * with another expression
     */
    public CspLongExpr add(CspLongExpr expr) {
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
    public CspIntExpr subtract(int i) {
        return new GenericIntExpr(null, this, NumOperation.SUBTRACT, i);
    }
    
    /**
     * Returns an expression representing the difference of this expression
     * with a static value
     */
    public CspGenericIntExpr subtract(CspGenericIntConstant i) {
        return new GenericIntExpr(null, this, NumOperation.SUBTRACT, (GenericIntConstant)i);
    }    
    
    /**
     * Returns an expression representing the difference of this expression
     * with a static value
     */
    public CspGenericIntExpr subtract(CspGenericIntExpr i) {
        return new GenericIntExpr(null, this, NumOperation.SUBTRACT, (GenericIntExpr)i);
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
        return new GenericDoubleExpr(null, this, NumOperation.SUBTRACT, (GenericDoubleConstant)d);
    }    

    /**
     * Returns an expression representing the difference of this expression
     * with another expression
     */
    public CspIntExpr subtract(CspIntExpr expr) {
        if (expr instanceof CspGenericIntExpr)
            return new GenericIntExpr(null, this, NumOperation.SUBTRACT, (CspGenericIntExpr)expr);
        else
            return new GenericIntExpr(null, this, NumOperation.SUBTRACT, (IntExpr)expr);
    }

    /**
     * Returns an expression representing the difference of this expression
     * with another expression
     */
    public CspLongExpr subtract(CspLongExpr expr) {
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
    public CspIntExpr multiply(int i) {
        return new GenericIntExpr(null, this, NumOperation.MULTIPLY, i);
    }
    
    /**
     * Returns an expression representing the product of this expression
     * with a static value
     */
    public CspGenericIntExpr multiply(CspGenericIntConstant i) {
        return new GenericIntExpr(null, this, NumOperation.MULTIPLY, (GenericIntConstant)i);
    }    
    
    /**
     * Returns an expression representing the product of this expression
     * with a static value
     */
    public CspGenericIntExpr multiply(CspGenericIntExpr i) {
        return new GenericIntExpr(null, this, NumOperation.MULTIPLY, (GenericIntExpr)i);
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
        return new GenericDoubleExpr(null, this, NumOperation.MULTIPLY, (GenericDoubleConstant)d);
    }
    
    /**
     * Returns an expression representing the product of this expression
     * with another expression
     */
    public CspIntExpr multiply(CspIntExpr expr) {
        if (expr == this)
            return new GenericIntExpr(null, this, NumOperation.SQUARE);
        else if (expr instanceof CspGenericIntExpr)
            return new GenericIntExpr(null, this, NumOperation.MULTIPLY, (CspGenericIntExpr)expr);
        else
            return new GenericIntExpr(null, this, NumOperation.MULTIPLY, (IntExpr)expr);
    }

    /**
     * Returns an expression representing the product of this expression
     * with another expression
     */
    public CspLongExpr multiply(CspLongExpr expr) {
        if (expr instanceof CspGenericLongCast)
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
    public CspIntExpr divide(int i) {
        return new GenericIntExpr(null, this, NumOperation.DIVIDE, i);
    }

    /**
     * Returns an expression representing the quotient of this expression
     * with a static value
     */
    public CspGenericIntExpr divide(CspGenericIntConstant i) {
        return new GenericIntExpr(null, this, NumOperation.DIVIDE, (GenericIntConstant)i);
    }
    
    /**
     * Returns an expression representing the quotient of this expression
     * with a static value
     */
    public CspGenericIntExpr divide(CspGenericIntExpr i) {
        return new GenericIntExpr(null, this, NumOperation.DIVIDE, (GenericIntExpr)i);
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
    public CspGenericFloatExpr divide(CspGenericFloatConstant f) {
        return new GenericFloatExpr(null, this, NumOperation.DIVIDE, (GenericFloatConstant)f);
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
       return new GenericDoubleExpr(null, this, NumOperation.DIVIDE, (GenericDoubleConstant)d);
   }

    /**
     * Returns an expression representing the quotient of this expression
     * with another expression
     */
    public CspIntExpr divide(CspIntExpr expr) {
        if (expr instanceof CspGenericIntExpr)
            return new GenericIntExpr(null, this, NumOperation.DIVIDE, (CspGenericIntExpr)expr);
        else
            return new GenericIntExpr(null, this, NumOperation.DIVIDE, (IntExpr)expr);
    }

    /**
     * Returns an expression representing the quotient of this expression
     * with another expression
     */
    public CspLongExpr divide(CspLongExpr expr) {
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
    public CspIntExpr getExpressionForIndex() {
    	return (CspIntExpr) super.getNumExpressionForIndex();
    }

    /**
     * returns a numeric expression from the internal array
     * 
     * @param offset  Offset of expression in internal expression array
     */
    public CspIntExpr getExpression(int offset) {
    	return (CspIntExpr) getNumExpression(offset);
    }

    /**
     * Returns that largest maximal value of all variables in array
     */
    public int getLargestMax() {
        return getNumLargestMax().intValue();
    }
    
    /**
     * Returns that largest maximal value of all variables in array within
     * start and end indices
     */
    public int getLargestMax(int start, int end) {
        return getNumLargestMax(start, end).intValue();
    }

    /**
     * Returns that smallest maximal value of all variables in array
     */
    public int getSmallestMax() {
        return getNumSmallestMax().intValue();
    }
    
    /**
     * Returns that smallest maximal value of all variables in array within
     * start and end indices
     */
    public int getSmallestMax(int start, int end) {
    	return getNumSmallestMax(start, end).intValue();
    }

    /**
     * Returns that largest minimal value of all variables in array
     */
    public int getLargestMin() {
        return getNumLargestMin().intValue();
    }
    
    /**
     * Returns that largest minimal value of all variables in array within
     * start and end indices
     */
    public int getLargestMin(int start, int end) {
        return getNumLargestMin(start, end).intValue();
    }

    /**
     * Returns that smallest minimal value of all variables in array
     */
    public int getSmallestMin() {
        return getNumSmallestMin().intValue();
    }
    
    /**
     * Returns that smallest minimal value of all variables in array within
     * start and end indices
     */
    public int getSmallestMin(int start, int end) {
        return getNumSmallestMin(start, end).intValue();
    }
    
    /**
     * Returns constraint restricting this expression to a value
     */
    public CspConstraint eq(int val) {
        return constraint(new Integer(val), ThreeVarConstraint.EQ);
    }
    
    /**
     * Returns constraint restricting this expression to a value
     */
    public CspConstraint eq(CspGenericIntConstant vals) {
        return constraint((GenericIntConstant)vals, ThreeVarConstraint.EQ);
    }    

    /**
     * Returns constraint restricting this expression to values below
     * a given maximum
     */
    public CspConstraint lt(int val) {
        return constraint(new Integer(val), ThreeVarConstraint.LT);
    }
    
    /**
     * Returns constraint restricting this expression to values below
     * a given maximum
     */
    public CspConstraint lt(CspGenericIntConstant vals) {
        return constraint((GenericIntConstant)vals, ThreeVarConstraint.LT);
    }    

    /**
     * Returns constraint restricting this expression to values below
     * and including a given maximum
     */
    public CspConstraint leq(int val) {
        return constraint(new Integer(val), ThreeVarConstraint.LEQ);
    }

    /**
     * Returns constraint restricting this expression to values below
     * and including a given maximum
     */
    public CspConstraint leq(CspGenericIntConstant vals) {
        return constraint((GenericIntConstant)vals, ThreeVarConstraint.LEQ);
    }
    
    /**
     * Returns constraint restricting this expression to values above
     * a given minimum
     */
    public CspConstraint gt(int val) {
        return constraint(new Integer(val), ThreeVarConstraint.GT);
    }
    
    /**
     * Returns constraint restricting this expression to values above
     * a given minimum
     */
    public CspConstraint gt(CspGenericIntConstant vals) {
        return constraint((GenericIntConstant)vals, ThreeVarConstraint.GT);
    }

    /**
     * Returns constraint restricting this expression to values above
     * and including a given minimum
     */
    public CspConstraint geq(int val) {
        return constraint(new Integer(val), ThreeVarConstraint.GEQ);
    }
    
    /**
     * Returns constraint restricting this expression to values above
     * and including a given minimum
     */
    public CspConstraint geq(CspGenericIntConstant vals) {
        return constraint((GenericIntConstant)vals, ThreeVarConstraint.GEQ);
    }    

    /**
     * Returns constraint restricting this expression to all values
     * not equivalent to supplied value
     */
    public CspConstraint neq(int val) {
        return constraint(new Integer(val), ThreeVarConstraint.NEQ);
    }
    
    /**
     * Returns constraint restricting this expression to all values
     * not equivalent to supplied value
     */
    public CspConstraint neq(CspGenericIntConstant vals) {
        return constraint((GenericIntConstant)vals, ThreeVarConstraint.NEQ);
    }

    // javadoc inherited from GenericNumExprBase
    protected GenericNumExpr createTypeSpecificExpr(String name, GenericIndex indices[], NumExpr exprs[]) {
        return new GenericIntExpr(name, indices, exprs);
    }
    
    public Collection<Node> getNodeCollection() {
        Collection<Node> nodes = null; 
            if (super.getNodeCollection()!=null)
                    nodes = super.getNodeCollection();
            
        
       for (int i=0; i< exprs.length; i++ ){
           // retrieve nodes from b
           if (exprs[i]!=null) {
               if (nodes==null)
               	nodes = exprs[i].getNodeCollection();
               else
                   nodes.addAll(exprs[i].getNodeCollection());
           }
       }
        
        // add this node to collection
        if (nodes==null)
        	nodes = new ArrayList<Node>();
        nodes.add(getNode());
        
    	return nodes;
    }

    // javadoc inherited
    public CspConstraint between(int min, boolean minExclusive, int max, boolean maxExclusive) {
        return new NumBetweenConstraint(new Integer(min), minExclusive, new Integer(max), maxExclusive, this);
    }
    
    // javadoc inherited
    public CspConstraint between(int min, int max) {
        return new NumBetweenConstraint(new Integer(min), false, new Integer(max), false, this);
    }
    
    // javadoc inherited
    public CspConstraint notBetween(int min, boolean minExclusive, int max, boolean maxExclusive) {
        return new NumNotBetweenConstraint(new Integer(min), minExclusive, new Integer(max), maxExclusive, this);
    }
    
    // javadoc inherited
    public CspConstraint notBetween(int min, int max) {
        return new NumNotBetweenConstraint(new Integer(min), false, new Integer(max), false, this);
    }
}
