package jopt.csp.spi.arcalgorithm.variable;

import jopt.csp.spi.arcalgorithm.constraint.num.NumNotBetweenConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.ThreeVarConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.global.NumBetweenConstraint;
import jopt.csp.spi.arcalgorithm.domain.BaseLongDomain;
import jopt.csp.spi.arcalgorithm.domain.DoubleDomain;
import jopt.csp.spi.arcalgorithm.domain.DoubleDomainWrapper;
import jopt.csp.spi.arcalgorithm.domain.FloatDomain;
import jopt.csp.spi.arcalgorithm.domain.FloatDomainWrapper;
import jopt.csp.spi.arcalgorithm.domain.LongComputedDomain;
import jopt.csp.spi.arcalgorithm.domain.LongDomain;
import jopt.csp.spi.arcalgorithm.domain.LongIntervalDomain;
import jopt.csp.spi.arcalgorithm.domain.SummationLongComputedDomain;
import jopt.csp.spi.arcalgorithm.domain.SummationLongDomainExpression;
import jopt.csp.spi.arcalgorithm.graph.node.LongNode;
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
import jopt.csp.variable.CspGenericLongCast;
import jopt.csp.variable.CspGenericLongConstant;
import jopt.csp.variable.CspLongCast;
import jopt.csp.variable.CspLongExpr;

public class LongExpr extends NumExprBase implements CspLongExpr, LongCast {
    private MutableNumber min = new MutableNumber();
    private MutableNumber max = new MutableNumber();
    private MutableNumber nextHigher = new MutableNumber();
    private MutableNumber nextLower = new MutableNumber();
    private LongNode node;

    /**
     * Internal Constructor
     */
    public LongExpr(String name, LongCast aexpr, int operation, LongCast bexpr) {
        super(name, aexpr, operation, bexpr);
        this.domain = new LongComputedDomain(aexpr.getLongDomain(), bexpr.getLongDomain(), operation);
    }

    /**
     * Constructor for extending expression
     */
    protected LongExpr(String name, BaseLongDomain domain) {
        super(name, domain);
    }

    //  Constructors for building expressions with other expression types
    public LongExpr(String name, long a, int operation, LongCast bexpr) {
        super(name, new Long(a), operation, bexpr);
        this.domain = new LongComputedDomain(a, bexpr.getLongDomain(), operation);
    }
    public LongExpr(String name, LongCast aexpr, int operation, long b) {
        super(name, aexpr, operation, new Long(b));
        this.domain = new LongComputedDomain(aexpr.getLongDomain(), b, operation);
    }
    public LongExpr(String name, LongCast aexpr, int operation) {
        super(name, aexpr, operation);
        this.domain = new LongComputedDomain(aexpr.getLongDomain(), operation);
    }
    public LongExpr(String name, CspGenericLongCast aexpr, GenericIndex rangeIndices[],
            CspGenericIndexRestriction sourceIdxRestriction) 
    {
        super(name, aexpr, rangeIndices, sourceIdxRestriction);
        this.domain = new SummationLongComputedDomain((SummationLongDomainExpression) aexpr, sourceIdxRestriction);
        this.operation = NumOperation.SUMMATION;
    }


    /**
     * Retrieves node for an expression
     */
    public Node getNode() {
        if (node == null) {
            LongDomain domain = calculated ? new LongIntervalDomain(getMin(), getMax()) : getLongDomain();
            node = new LongNode(name, domain);
        }
        return node;
    }

    /**
     * Returns the type of expression: Int, Long, etc.
     */
    public int getNumberType() {
        return NumConstants.LONG;
    }
    
    /**
     * Returns domain of this expression
     */
    public DoubleDomain getDoubleDomain() {
        return new DoubleDomainWrapper((LongDomain) domain);
    }

    /**
     * Returns domain of this expression
     */
    public FloatDomain getFloatDomain() {
        return new FloatDomainWrapper((LongDomain) domain);
    }

    /**
     * Returns domain of this expression
     */
    public LongDomain getLongDomain() {
        return (LongDomain) domain;
    }

    /**
     * Returns minimal value of expression
     */
    public long getMin() {
        return getLongDomain().getMin();
    }

    /**
     * Returns maximum value of expression
     */
    public long getMax() {
        return getLongDomain().getMax();
    }

    /**
     * Returns maximum value of this variable's domain
     */
    public Number getNumMax() {
        max.setLongValue(getMax());
        return max;
    } 
    
    /**
     * Returns minimum value of this variable's domain
     */
    public Number getNumMin() {
        min.setLongValue(getMin());
        return min;
    }    
    
    // javadoc inherited
    public Number getNextHigher(Number val) {
        nextHigher.setLongValue(getLongDomain().getNextHigher(val.longValue()));
        return nextHigher;
    }

    // javadoc inherited
    public Number getNextLower(Number val) {
        nextLower.setLongValue(getLongDomain().getNextLower(val.longValue()));
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
            return getLongDomain().isInDomain(n.longValue());
        else
            return false;
    }
    
    /**
     * Returns an expression representing the sum of this expression
     * with a static value
     */
    public CspLongExpr add(long l) {
        return new LongExpr(null, this, NumOperation.ADD, l);
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
    public CspLongExpr add(CspLongCast expr) {
        if (expr instanceof CspGenericLongCast)
            return new GenericLongExpr(null, this, NumOperation.ADD, (CspGenericLongCast) expr);
        else
        	return new LongExpr(null, this, NumOperation.ADD, (LongCast)expr);
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
    public CspLongExpr subtract(long l) {
        return new LongExpr(null, this, NumOperation.SUBTRACT, l);
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
    public CspLongExpr subtract(CspLongCast expr) {
        if (expr instanceof CspGenericLongCast)
            return new GenericLongExpr(null, this, NumOperation.SUBTRACT, (CspGenericLongCast) expr);
        else
        	return new LongExpr(null, this, NumOperation.SUBTRACT, (LongCast) expr);
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
    public CspLongExpr multiply(long l) {
        return new LongExpr(null, this, NumOperation.MULTIPLY, l);
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
    public CspLongExpr multiply(CspLongCast expr) {
        if (expr == this)
            return new LongExpr(null, this, NumOperation.SQUARE);
        else if (expr instanceof CspGenericLongCast)
            return new GenericLongExpr(null, this, NumOperation.MULTIPLY, (CspGenericLongCast) expr);
        else
            return new LongExpr(null, this, NumOperation.MULTIPLY, (LongCast)expr);
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
    public CspLongExpr divide(long l) {
        return new LongExpr(null, this, NumOperation.DIVIDE, l);
    }

    /**
     * Returns an expression representing the quotient of this expression
     * with a static value
     */
    public CspFloatExpr divide(float f) {
        return new FloatExpr(null, (FloatCast)this, NumOperation.DIVIDE, f);
    }

    /**
     * Returns an expression representing the quotient of this expression
     * with a static value
     */
    public CspDoubleExpr divide(double d) {
        return new DoubleExpr(null, (DoubleCast)this, NumOperation.DIVIDE, d);
    }

    /**
     * Returns an expression representing the quotient of this expression
     * with another expression
     */
    public CspLongExpr divide(CspLongCast expr) {
        if (expr instanceof CspGenericLongCast)
            return new GenericLongExpr(null, this, NumOperation.DIVIDE, (CspGenericLongCast) expr);
        else
        	return new LongExpr(null, this, NumOperation.DIVIDE, (LongCast)expr);
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
            return new GenericDoubleExpr(null, this, NumOperation.DIVIDE, (CspGenericDoubleExpr) expr);
        else
        	return new DoubleExpr(null, (DoubleCast)this, NumOperation.DIVIDE, (DoubleCast)expr);
    }

    /**
     * Returns constraint restricting this expression to a value
     */
    public CspConstraint eq(long val) {
        return constraint(new Long(val), ThreeVarConstraint.EQ);
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
     * and including a given maximum
     */
    public CspConstraint leq(long val) {
        return constraint(new Long(val), ThreeVarConstraint.LEQ);
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
     * and including a given minimum
     */
    public CspConstraint geq(long val) {
        return constraint(new Long(val), ThreeVarConstraint.GEQ);
    }

    /**
     * Returns constraint restricting this expression to all values
     * not equivalent to supplied value
     */
    public CspConstraint neq(long val) {
        return constraint(new Long(val), ThreeVarConstraint.NEQ);
    }
    
    /**
     * Returns constraint restricting this expression to a value
     */
    public CspConstraint eq(CspGenericLongConstant val) {
        return constraint((GenericLongConstant)val, ThreeVarConstraint.EQ);
    }

    /**
     * Returns constraint restricting this expression to values below
     * a given maximum
     */
    public CspConstraint lt(CspGenericLongConstant val) {
        return constraint((GenericLongConstant)val, ThreeVarConstraint.LT);
    }

    /**
     * Returns constraint restricting this expression to values below
     * and including a given maximum
     */
    public CspConstraint leq(CspGenericLongConstant val) {
        return constraint((GenericLongConstant)val, ThreeVarConstraint.LEQ);
    }

    /**
     * Returns constraint restricting this expression to values above
     * a given minimum
     */
    public CspConstraint gt(CspGenericLongConstant val) {
        return constraint((GenericLongConstant)val, ThreeVarConstraint.GT);
    }

    /**
     * Returns constraint restricting this expression to values above
     * and including a given minimum
     */
    public CspConstraint geq(CspGenericLongConstant val) {
        return constraint((GenericLongConstant)val, ThreeVarConstraint.GEQ);
    }

    /**
     * Returns constraint restricting this expression to all values
     * not equivalent to supplied value
     */
    public CspConstraint neq(CspGenericLongConstant val) {
        return constraint((GenericLongConstant)val, ThreeVarConstraint.NEQ);
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
}