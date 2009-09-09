package jopt.csp.spi.arcalgorithm.variable;

import jopt.csp.spi.arcalgorithm.constraint.num.NumNotBetweenConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.ThreeVarConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.global.NumBetweenConstraint;
import jopt.csp.spi.arcalgorithm.domain.DoubleDomain;
import jopt.csp.spi.arcalgorithm.domain.DoubleDomainWrapper;
import jopt.csp.spi.arcalgorithm.domain.FloatDomain;
import jopt.csp.spi.arcalgorithm.domain.FloatDomainWrapper;
import jopt.csp.spi.arcalgorithm.domain.IntComputedDomain;
import jopt.csp.spi.arcalgorithm.domain.IntDomain;
import jopt.csp.spi.arcalgorithm.domain.IntIntervalDomain;
import jopt.csp.spi.arcalgorithm.domain.LongDomain;
import jopt.csp.spi.arcalgorithm.domain.LongDomainWrapper;
import jopt.csp.spi.arcalgorithm.domain.SummationIntComputedDomain;
import jopt.csp.spi.arcalgorithm.domain.SummationIntDomainExpression;
import jopt.csp.spi.arcalgorithm.graph.node.IntNode;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.util.GenericIndex;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumConstants;
import jopt.csp.spi.util.NumOperation;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.CspDoubleExpr;
import jopt.csp.variable.CspFloatExpr;
import jopt.csp.variable.CspGenericDoubleExpr;
import jopt.csp.variable.CspGenericFloatExpr;
import jopt.csp.variable.CspGenericIndexRestriction;
import jopt.csp.variable.CspGenericIntConstant;
import jopt.csp.variable.CspGenericIntExpr;
import jopt.csp.variable.CspGenericLongExpr;
import jopt.csp.variable.CspIntExpr;
import jopt.csp.variable.CspLongExpr;

public class IntExpr extends NumExprBase implements CspIntExpr, LongCast {
    private MutableNumber min = new MutableNumber();
    private MutableNumber max = new MutableNumber();
    private MutableNumber nextHigher = new MutableNumber();
    private MutableNumber nextLower = new MutableNumber();

    private IntNode node;

    /**
     * Internal Constructor
     */
    public IntExpr(String name, IntExpr aexpr, int operation, IntExpr bexpr) {
        super(name, aexpr, operation, bexpr);
        this.domain = new IntComputedDomain(aexpr.getIntDomain(), bexpr.getIntDomain(), operation);
    }

    /**
     * Constructor
     */
    public IntExpr(String name, IntDomain domain) {
        super(name, domain);
        this.domain = domain;
    }

    /**
     * Constructor for boolean expressions
     */
    protected IntExpr(String name) {
        super(name);
    }

    // Constructors for building expressions with other expression types
    public IntExpr(String name, int a, int operation, IntExpr bexpr) {
        super(name, new Integer(a), operation, bexpr);
        this.domain = new IntComputedDomain(a, bexpr.getIntDomain(), operation);
    }
    public IntExpr(String name, IntExpr aexpr, int operation, int b) {
        super(name, aexpr, operation, new Integer(b));
        this.domain = new IntComputedDomain(aexpr.getIntDomain(), b, operation);
    }
    public IntExpr(String name, IntExpr aexpr, int operation) {
        super(name, aexpr, operation);
        this.domain = new IntComputedDomain(aexpr.getIntDomain(), operation);
    }
    public IntExpr(String name, CspGenericIntExpr aexpr, GenericIndex rangeIndices[],
            CspGenericIndexRestriction sourceIdxRestriction) 
    {
        super(name, aexpr, rangeIndices, sourceIdxRestriction);
        this.domain = new SummationIntComputedDomain((SummationIntDomainExpression) aexpr, sourceIdxRestriction);
        this.operation = NumOperation.SUMMATION;
    }

    /**
     * Retrieves node for an expression
     */
    public Node getNode() {
        if (node == null) {
            IntDomain domain = calculated ? new IntIntervalDomain(getMin(), getMax()) : getIntDomain();
            node = new IntNode(name, domain);
        }
        
        return node;
    }

    
    /**
     * Returns the type of expression: Int, Long, etc.
     */
    public int getNumberType() {
    	return NumConstants.INTEGER;
    }
    
    /**
     * Returns domain of this expression
     */
    public DoubleDomain getDoubleDomain() {
        return new DoubleDomainWrapper((IntDomain) domain);
    }

    /**
     * Returns domain of this expression
     */
    public FloatDomain getFloatDomain() {
        return new FloatDomainWrapper((IntDomain) domain);
    }

    /**
     * Returns domain of this expression
     */
    public LongDomain getLongDomain() {
        return new LongDomainWrapper((IntDomain) domain);
    }

    /**
     * Returns domain of this expression
     */
    public IntDomain getIntDomain() {
        return (IntDomain) domain;
    }

    /**
     * Returns minimal value of expression
     */
    public int getMin() {
        return getIntDomain().getMin();
    }

    /**
     * Returns maximum value of expression
     */
    public int getMax() {
        return getIntDomain().getMax();
    }
    
    /**
     * Returns maximum value of this variable's domain
     */
    public Number getNumMax() {
        max.setIntValue(getMax());
        return max;
    } 
    
    /**
     * Returns minimum value of this variable's domain
     */
    public Number getNumMin() {
        min.setIntValue(getMin());
        return min;
    }    
    
    // javadoc inherited
    public Number getNextHigher(Number val) {
    	nextHigher.setIntValue(getIntDomain().getNextHigher(NumberMath.intFloor(val)));
        return nextHigher;
    }

    // javadoc inherited
    public Number getNextLower(Number val) {
        nextLower.setIntValue(getIntDomain().getNextLower(NumberMath.intCeil(val)));
        return nextLower;
    }
    
    // javadoc inherited
    public void setPrecision(double p) {}
    
    // javadoc inherited
    public double getPrecision() {
        return 0;
    }
    
    // javadoc inherited
    public boolean isInDomain(Number n) {
        if (NumberMath.isIntEquivalent(n))
            return getIntDomain().isInDomain(n.intValue());
        else
            return false;
    }
    
    /**
     * Returns an expression representing the sum of this expression
     * with a static value
     */
    public CspIntExpr add(int i) {
        return new IntExpr(null, this, NumOperation.ADD, i);
    }

    /**
     * Returns an expression representing the sum of this expression
     * with a static value
     */
    public CspLongExpr add(long l) {
        return new LongExpr(null, (LongCast)this, NumOperation.ADD, l);
    }

    /**
     * Returns an expression representing the sum of this expression
     * with a static value
     */
    public CspFloatExpr add(float f) {
        return new FloatExpr(null, (FloatCast)this, NumOperation.ADD, f);
    }

    /**
     * Returns an expression representing the sum of this expression
     * with a static value
     */
    public CspDoubleExpr add(double d) {
        return new DoubleExpr(null, (DoubleCast)this, NumOperation.ADD, d);
    }

    /**
     * Returns an expression representing the sum of this expression
     * with another expression
     */
    public CspIntExpr add(CspIntExpr expr) {
        if (expr instanceof CspGenericIntExpr)
            return new GenericIntExpr(null, this, NumOperation.ADD, (CspGenericIntExpr) expr);
        else
        	return new IntExpr(null, this, NumOperation.ADD, (IntExpr)expr);
    }

    /**
     * Returns an expression representing the sum of this expression
     * with another expression
     */
    public CspLongExpr add(CspLongExpr expr) {
        if (expr instanceof CspGenericLongExpr)
            return new GenericLongExpr(null, this, NumOperation.ADD, (CspGenericLongExpr) expr);
        else
        	return new LongExpr(null, (LongCast)this, NumOperation.ADD, (LongCast)expr);
    }

    /**
     * Returns an expression representing the sum of this expression
     * with another expression
     */
    public CspFloatExpr add(CspFloatExpr expr) {
        if (expr instanceof CspGenericFloatExpr)
            return new GenericFloatExpr(null, this, NumOperation.ADD, (CspGenericFloatExpr) expr);
        else
        	return new FloatExpr(null, (FloatCast)this, NumOperation.ADD, (FloatCast)expr);
    }

    /**
     * Returns an expression representing the sum of this expression
     * with another expression
     */
    public CspDoubleExpr add(CspDoubleExpr expr) {
        if (expr instanceof CspGenericDoubleExpr)
            return new GenericDoubleExpr(null, this, NumOperation.ADD, (CspGenericDoubleExpr) expr);
        else
        	return new DoubleExpr(null, (DoubleCast)this, NumOperation.ADD, (DoubleCast)expr);
    }

    /**
     * Returns an expression representing the difference of this expression
     * with a static value
     */
    public CspIntExpr subtract(int i) {
        return new IntExpr(null, this, NumOperation.SUBTRACT, i);
    }

    /**
     * Returns an expression representing the difference of this expression
     * with a static value
     */
    public CspLongExpr subtract(long l) {
        return new LongExpr(null, (LongCast)this, NumOperation.SUBTRACT, l);
    }

    /**
     * Returns an expression representing the difference of this expression
     * with a static value
     */
    public CspFloatExpr subtract(float f) {
        return new FloatExpr(null, (FloatCast)this, NumOperation.SUBTRACT, f);
    }

    /**
     * Returns an expression representing the difference of this expression
     * with a static value
     */
    public CspDoubleExpr subtract(double d) {
        return new DoubleExpr(null, (DoubleCast)this, NumOperation.SUBTRACT, d);
    }

    /**
     * Returns an expression representing the difference of this expression
     * with another expression
     */
    public CspIntExpr subtract(CspIntExpr expr) {
        if (expr instanceof CspGenericIntExpr)
            return new GenericIntExpr(null, this, NumOperation.SUBTRACT, (CspGenericIntExpr) expr);
        else
        	return new IntExpr(null, this, NumOperation.SUBTRACT, (IntExpr)expr);
    }

    /**
     * Returns an expression representing the difference of this expression
     * with another expression
     */
    public CspLongExpr subtract(CspLongExpr expr) {
        if (expr instanceof CspGenericLongExpr)
            return new GenericLongExpr(null, this, NumOperation.SUBTRACT, (CspGenericLongExpr) expr);
        else
        	return new LongExpr(null, (LongCast)this, NumOperation.SUBTRACT, (LongCast)expr);
    }

    /**
     * Returns an expression representing the difference of this expression
     * with another expression
     */
    public CspFloatExpr subtract(CspFloatExpr expr) {
        if (expr instanceof CspGenericFloatExpr)
            return new GenericFloatExpr(null, this, NumOperation.SUBTRACT, (CspGenericFloatExpr) expr);
        else
        	return new FloatExpr(null, (FloatCast)this, NumOperation.SUBTRACT, (FloatCast)expr);
    }

    /**
     * Returns an expression representing the difference of this expression
     * with another expression
     */
    public CspDoubleExpr subtract(CspDoubleExpr expr) {
        if (expr instanceof CspGenericDoubleExpr)
            return new GenericDoubleExpr(null, this, NumOperation.SUBTRACT, (CspGenericDoubleExpr) expr);
        else
        	return new DoubleExpr(null, (DoubleCast)this, NumOperation.SUBTRACT, (DoubleCast)expr);
    }

    /**
     * Returns an expression representing the product of this expression
     * with a static value
     */
    public CspIntExpr multiply(int i) {
        return new IntExpr(null, this, NumOperation.MULTIPLY, i);
    }

    /**
     * Returns an expression representing the product of this expression
     * with a static value
     */
    public CspLongExpr multiply(long l) {
        return new LongExpr(null, (LongCast)this, NumOperation.MULTIPLY, l);
    }

    /**
     * Returns an expression representing the product of this expression
     * with a static value
     */
    public CspFloatExpr multiply(float f) {
        return new FloatExpr(null, (FloatCast)this, NumOperation.MULTIPLY, f);
    }

    /**
     * Returns an expression representing the product of this expression
     * with a static value
     */
    public CspDoubleExpr multiply(double d) {
        return new DoubleExpr(null, (DoubleCast)this, NumOperation.MULTIPLY, d);
    }

    /**
     * Returns an expression representing the product of this expression
     * with another expression
     */
    public CspIntExpr multiply(CspIntExpr expr) {
        if (expr == this)
            return new IntExpr(null, this, NumOperation.SQUARE);
        else if (expr instanceof CspGenericIntExpr)
            return new GenericIntExpr(null, this, NumOperation.MULTIPLY, (CspGenericIntExpr) expr);
        else
            return new IntExpr(null, this, NumOperation.MULTIPLY, (IntExpr)expr);
    }

    /**
     * Returns an expression representing the product of this expression
     * with another expression
     */
    public CspLongExpr multiply(CspLongExpr expr) {
        if (expr instanceof CspGenericLongExpr)
            return new GenericLongExpr(null, this, NumOperation.MULTIPLY, (CspGenericLongExpr) expr);
        else
            return new LongExpr(null, (LongCast)this, NumOperation.MULTIPLY, (LongCast)expr);
    }

    /**
     * Returns an expression representing the product of this expression
     * with another expression
     */
    public CspFloatExpr multiply(CspFloatExpr expr) {
        if (expr instanceof CspGenericFloatExpr)
            return new GenericFloatExpr(null, this, NumOperation.MULTIPLY, (CspGenericFloatExpr) expr);
        else
        	return new FloatExpr(null, (FloatCast)this, NumOperation.MULTIPLY, (FloatCast)expr);
    }

    /**
     * Returns an expression representing the product of this expression
     * with another expression
     */
    public CspDoubleExpr multiply(CspDoubleExpr expr) {
        if (expr instanceof CspGenericDoubleExpr)
            return new GenericDoubleExpr(null, this, NumOperation.MULTIPLY, (CspGenericDoubleExpr) expr);
        else
        	return new DoubleExpr(null, (DoubleCast)this, NumOperation.MULTIPLY, (DoubleCast)expr);
    }

    /**
     * Returns an expression representing the quotient of this expression
     * with a static value
     */
    public CspIntExpr divide(int i) {
        return new IntExpr(null, this, NumOperation.DIVIDE, i);
    }

    /**
     * Returns an expression representing the quotient of this expression
     * with a static value
     */
    public CspLongExpr divide(long l) {
        return new LongExpr(null, (LongCast)this, NumOperation.DIVIDE, l);
    }

    /**
     * Returns an expression representing the quotient of this expression
     * with a static value
     */
    public CspFloatExpr divide(float f) {
        return new FloatExpr(null, this, NumOperation.DIVIDE, f);
    }

    /**
     * Returns an expression representing the quotient of this expression
     * with a static value
     */
    public CspDoubleExpr divide(double d) {
        return new DoubleExpr(null, this, NumOperation.DIVIDE, d);
    }

    /**
     * Returns an expression representing the quotient of this expression
     * with another expression
     */
    public CspIntExpr divide(CspIntExpr expr) {
        if (expr instanceof CspGenericIntExpr)
            return new GenericIntExpr(null, this, NumOperation.DIVIDE, (CspGenericIntExpr) expr);
        else
        	return new IntExpr(null, this, NumOperation.DIVIDE, (IntExpr)expr);
    }

    /**
     * Returns an expression representing the quotient of this expression
     * with another expression
     */
    public CspLongExpr divide(CspLongExpr expr) {
        if (expr instanceof CspGenericLongExpr)
            return new GenericLongExpr(null, this, NumOperation.DIVIDE, (CspGenericLongExpr) expr);
        else
        	return new LongExpr(null, (LongCast)this, NumOperation.DIVIDE, (LongCast)expr);
    }

    /**
     * Returns an expression representing the quotient of this expression
     * with another expression
     */
    public CspFloatExpr divide(CspFloatExpr expr) {
        if (expr instanceof CspGenericFloatExpr)
            return new GenericFloatExpr(null, this, NumOperation.DIVIDE, (CspGenericFloatExpr) expr);
        else
        	return new FloatExpr(null, (FloatCast)this, NumOperation.DIVIDE, (FloatCast)expr);
    }

    /**
     * Returns an expression representing the quotient of this expression
     * with another expression
     */
    public CspDoubleExpr divide(CspDoubleExpr expr) {
        if (expr instanceof CspGenericDoubleExpr)
            return new GenericDoubleExpr(null, this, NumOperation.ADD, (CspGenericDoubleExpr) expr);
        else
        	return new DoubleExpr(null, (DoubleCast)this, NumOperation.DIVIDE, (DoubleCast)expr);
    }

    /**
     * Returns constraint restricting this expression to a value
     */
    public CspConstraint eq(int val) {
        return constraint(new Integer(val), ThreeVarConstraint.EQ);
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
     * and including a given maximum
     */
    public CspConstraint leq(int val) {
        return constraint(new Integer(val), ThreeVarConstraint.LEQ);
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
     * and including a given minimum
     */
    public CspConstraint geq(int val) {
        return constraint(new Integer(val), ThreeVarConstraint.GEQ);
    }

    /**
     * Returns constraint restricting this expression to all values
     * not equivalent to supplied value
     */
    public CspConstraint neq(int val) {
        return constraint(new Integer(val), ThreeVarConstraint.NEQ);
    }
    
    /**
     * Returns constraint restricting this expression to a value
     */
    public CspConstraint eq(CspGenericIntConstant val) {
        return constraint((GenericIntConstant)val, ThreeVarConstraint.EQ);
    }

    /**
     * Returns constraint restricting this expression to values below
     * a given maximum
     */
    public CspConstraint lt(CspGenericIntConstant val) {
        return constraint((GenericIntConstant)val, ThreeVarConstraint.LT);
    }

    /**
     * Returns constraint restricting this expression to values below
     * and including a given maximum
     */
    public CspConstraint leq(CspGenericIntConstant val) {
        return constraint((GenericIntConstant)val, ThreeVarConstraint.LEQ);
    }

    /**
     * Returns constraint restricting this expression to values above
     * a given minimum
     */
    public CspConstraint gt(CspGenericIntConstant val) {
        return constraint((GenericIntConstant)val, ThreeVarConstraint.GT);
    }

    /**
     * Returns constraint restricting this expression to values above
     * and including a given minimum
     */
    public CspConstraint geq(CspGenericIntConstant val) {
        return constraint((GenericIntConstant)val, ThreeVarConstraint.GEQ);
    }

    /**
     * Returns constraint restricting this expression to all values
     * not equivalent to supplied value
     */
    public CspConstraint neq(CspGenericIntConstant val) {
        return constraint((GenericIntConstant)val, ThreeVarConstraint.NEQ);
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