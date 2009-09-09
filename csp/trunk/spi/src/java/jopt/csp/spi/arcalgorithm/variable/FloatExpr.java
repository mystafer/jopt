package jopt.csp.spi.arcalgorithm.variable;

import jopt.csp.spi.arcalgorithm.constraint.num.NumNotBetweenConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.ThreeVarConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.global.NumBetweenConstraint;
import jopt.csp.spi.arcalgorithm.domain.BaseFloatDomain;
import jopt.csp.spi.arcalgorithm.domain.DoubleDomain;
import jopt.csp.spi.arcalgorithm.domain.DoubleDomainWrapper;
import jopt.csp.spi.arcalgorithm.domain.FloatComputedDomain;
import jopt.csp.spi.arcalgorithm.domain.FloatDomain;
import jopt.csp.spi.arcalgorithm.domain.FloatIntervalDomain;
import jopt.csp.spi.arcalgorithm.domain.SummationFloatComputedDomain;
import jopt.csp.spi.arcalgorithm.domain.SummationFloatDomainExpression;
import jopt.csp.spi.arcalgorithm.graph.node.FloatNode;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.util.GenericIndex;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumConstants;
import jopt.csp.spi.util.NumOperation;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.CspDoubleExpr;
import jopt.csp.variable.CspFloatCast;
import jopt.csp.variable.CspFloatExpr;
import jopt.csp.variable.CspGenericDoubleExpr;
import jopt.csp.variable.CspGenericFloatCast;
import jopt.csp.variable.CspGenericFloatConstant;
import jopt.csp.variable.CspGenericIndexRestriction;

public class FloatExpr extends NumExprBase implements CspFloatExpr, FloatCast {
    private MutableNumber min = new MutableNumber();
    private MutableNumber max = new MutableNumber();
    private MutableNumber nextHigher = new MutableNumber();
    private MutableNumber nextLower = new MutableNumber();
    private FloatNode node;

    /**
     * Internal Constructor
     */
    public FloatExpr(String name, FloatCast aexpr, int operation, FloatCast bexpr) {
        super(name, aexpr, operation, bexpr);
        this.domain = new FloatComputedDomain(aexpr.getFloatDomain(), bexpr.getFloatDomain(), operation);
    }

    /**
     * Constructor for extending expression
     */
    protected FloatExpr(String name, BaseFloatDomain domain) {
        super(name, domain);
    }

    //  Constructors for building expressions with other expression types
    public FloatExpr(String name, float a, int operation, FloatCast bexpr) {
        super(name, new Float(a), operation, bexpr);
        this.domain = new FloatComputedDomain(a, bexpr.getFloatDomain(), operation);
    }
    public FloatExpr(String name, FloatCast aexpr, int operation, float b) {
        super(name, aexpr, operation, new Float(b));
        this.domain = new FloatComputedDomain(aexpr.getFloatDomain(), b, operation);
    }
    public FloatExpr(String name, FloatCast aexpr, int operation) {
        super(name, aexpr, operation);
        this.domain = new FloatComputedDomain(aexpr.getFloatDomain(), operation);
    }
    public FloatExpr(String name, CspGenericFloatCast aexpr, GenericIndex rangeIndices[],
            CspGenericIndexRestriction sourceIdxRestriction) 
    {
        super(name, aexpr, rangeIndices, sourceIdxRestriction);
        this.domain = new SummationFloatComputedDomain((SummationFloatDomainExpression) aexpr, sourceIdxRestriction);
        this.operation = NumOperation.SUMMATION;
    }

    /**
     * Retrieves node for an expression
     */
    public Node getNode() {
        if (node == null) {
            FloatDomain domain = calculated ? new FloatIntervalDomain(getMin(), getMax()) : getFloatDomain();
            node = new FloatNode(name, domain);
        }
        return node;
    }

    /**
     * Returns the type of expression: Int, Long, etc.
     */
    public int getNumberType() {
        return NumConstants.FLOAT;
    }
    
    /**
     * Returns domain of this expression
     */
    public DoubleDomain getDoubleDomain() {
        return new DoubleDomainWrapper((FloatDomain) domain);
    }

    /**
     * Returns domain of this expression
     */
    public FloatDomain getFloatDomain() {
        return (FloatDomain) domain;
    }

    /**
     * Returns minimal value of expression
     */
    public float getMin() {
        return getFloatDomain().getMin();
    }

    /**
     * Returns maximum value of expression
     */
    public float getMax() {
        return getFloatDomain().getMax();
    }

    /**
     * Returns maximum value of this variable's domain
     */
    public Number getNumMax() {
        max.setFloatValue(getMax());
        return max;
    } 
    
    /**
     * Returns minimum value of this variable's domain
     */
    public Number getNumMin() {
        min.setFloatValue(getMin());
        return min;
    }    
    
    // javadoc inherited
    public Number getNextHigher(Number val) {
        nextHigher.setFloatValue(getFloatDomain().getNextHigher(val.floatValue()));
        return nextHigher;
    }

    // javadoc inherited
    public Number getNextLower(Number val) {
        nextLower.setFloatValue(getFloatDomain().getNextLower(val.floatValue()));
        return nextLower;
    }
    
    // javadoc inherited
    public void setPrecision(double p) {
    	getFloatDomain().setPrecision((float) p);
    }
    
    // javadoc inherited
    public double getPrecision() {
        return getFloatDomain().getPrecision();
    }
    
    // javadoc inherited
    public boolean isInDomain(Number n) {
        if (NumberMath.isIntEquivalent(n))
            return getFloatDomain().isInDomain(n.floatValue());
        else
            return false;
    }
    
    /**
     * Returns an expression representing the sum of this expression
     * with a static value
     */
    public CspFloatExpr add(float f) {
        return new FloatExpr(null, this, NumOperation.ADD, f);
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
    public CspFloatExpr add(CspFloatCast expr) {
        if (expr instanceof CspGenericFloatCast)
            return new GenericFloatExpr(null, this, NumOperation.ADD, (CspGenericFloatCast) expr);
        else
        	return new FloatExpr(null, this, NumOperation.ADD, (FloatCast)expr);
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
    public CspFloatExpr subtract(float f) {
        return new FloatExpr(null, this, NumOperation.SUBTRACT, f);
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
    public CspFloatExpr subtract(CspFloatCast expr) {
        if (expr instanceof CspGenericFloatCast)
            return new GenericFloatExpr(null, this, NumOperation.SUBTRACT, (CspGenericFloatCast) expr);
        else
        	return new FloatExpr(null, this, NumOperation.SUBTRACT, (FloatCast)expr);
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
    public CspFloatExpr multiply(float f) {
        return new FloatExpr(null, this, NumOperation.MULTIPLY, f);
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
    public CspFloatExpr multiply(CspFloatCast expr) {
        if (expr == this)
            return new FloatExpr(null, this, NumOperation.SQUARE);
        else if (expr instanceof CspGenericFloatCast)
            return new GenericFloatExpr(null, this, NumOperation.MULTIPLY, (CspGenericFloatCast) expr);
        else
            return new FloatExpr(null, this, NumOperation.MULTIPLY, (FloatCast)expr);
    }
    
    /**
     * Returns an expression representing the product of this expression
     * with another expression
     */
    public CspFloatExpr multiply(FloatExpr expr) {
        if (expr == this)
            return new FloatExpr(null, this, NumOperation.SQUARE);
        else if (expr instanceof CspGenericFloatCast)
            return new GenericFloatExpr(null, this, NumOperation.MULTIPLY, (CspGenericFloatCast) expr);
        else
            return new FloatExpr(null, this, NumOperation.MULTIPLY, expr);
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
    public CspFloatExpr divide(float f) {
        return new FloatExpr(null, this, NumOperation.DIVIDE, f);
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
    public CspFloatExpr divide(CspFloatCast expr) {
        if (expr instanceof CspGenericFloatCast)
            return new GenericFloatExpr(null, this, NumOperation.DIVIDE, (CspGenericFloatCast) expr);
        else
        	return new FloatExpr(null, this, NumOperation.DIVIDE, (FloatCast)expr);
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
    public CspConstraint eq(float val) {
        return constraint(new Float(val), ThreeVarConstraint.EQ);
    }

    /**
     * Returns constraint restricting this expression to values below
     * and including a given maximum
     */
    public CspConstraint leq(float val) {
        return constraint(new Float(val), ThreeVarConstraint.LEQ);
    }

    /**
     * Returns constraint restricting this expression to values above
     * and including a given minimum
     */
    public CspConstraint geq(float val) {
        return constraint(new Float(val), ThreeVarConstraint.GEQ);
    }
    
    /**
     * Returns constraint restricting this expression to a value
     */
    public CspConstraint eq(CspGenericFloatConstant val) {
        return constraint((GenericFloatConstant)val, ThreeVarConstraint.EQ);
    }

    /**
     * Returns constraint restricting this expression to values below
     * and including a given maximum
     */
    public CspConstraint leq(CspGenericFloatConstant val) {
        return constraint((GenericFloatConstant)val, ThreeVarConstraint.LEQ);
    }

    /**
     * Returns constraint restricting this expression to values above
     * and including a given minimum
     */
    public CspConstraint geq(CspGenericFloatConstant val) {
        return constraint((GenericFloatConstant)val, ThreeVarConstraint.GEQ);
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
}