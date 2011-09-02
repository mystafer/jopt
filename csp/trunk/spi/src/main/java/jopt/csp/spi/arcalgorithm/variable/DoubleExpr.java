package jopt.csp.spi.arcalgorithm.variable;

import jopt.csp.spi.arcalgorithm.constraint.num.NumNotBetweenConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.ThreeVarConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.global.NumBetweenConstraint;
import jopt.csp.spi.arcalgorithm.domain.BaseDoubleDomain;
import jopt.csp.spi.arcalgorithm.domain.DoubleComputedDomain;
import jopt.csp.spi.arcalgorithm.domain.DoubleDomain;
import jopt.csp.spi.arcalgorithm.domain.DoubleIntervalDomain;
import jopt.csp.spi.arcalgorithm.domain.SummationDoubleComputedDomain;
import jopt.csp.spi.arcalgorithm.domain.SummationDoubleDomainExpression;
import jopt.csp.spi.arcalgorithm.graph.node.DoubleNode;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.util.GenericIndex;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumConstants;
import jopt.csp.spi.util.NumOperation;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.CspDoubleCast;
import jopt.csp.variable.CspDoubleExpr;
import jopt.csp.variable.CspGenericDoubleCast;
import jopt.csp.variable.CspGenericDoubleConstant;
import jopt.csp.variable.CspGenericIndexRestriction;

public class DoubleExpr extends NumExprBase implements CspDoubleExpr,
		DoubleCast {
	private MutableNumber min = new MutableNumber();

	private MutableNumber max = new MutableNumber();

	private MutableNumber nextHigher = new MutableNumber();

	private MutableNumber nextLower = new MutableNumber();
	
	private DoubleNode node;

	/**
	 * Constructor for combining double expression with another expression
	 */
	public DoubleExpr(String name, DoubleCast aexpr, int operation,
			DoubleCast bexpr) {
		super(name, aexpr, operation, bexpr);
		this.domain = new DoubleComputedDomain(aexpr.getDoubleDomain(), bexpr
				.getDoubleDomain(), operation);
	}

	/**
	 * Constructor for combining double constant with another expression
	 */
	public DoubleExpr(String name, double a, int operation, DoubleCast bexpr) {
		super(name, new Double(a), operation, bexpr);
		this.domain = new DoubleComputedDomain(a, bexpr.getDoubleDomain(),
				operation);
	}

	/**
	 * Constructor for combining double expression with a constant
	 * @param name
	 * @param aexpr
	 * @param operation
	 * @param b
	 */
	public DoubleExpr(String name, DoubleCast aexpr, int operation, double b) {
		super(name, aexpr, operation, new Double(b));
		this.domain = new DoubleComputedDomain(aexpr.getDoubleDomain(), b,
				operation);
	}

	/**
	 * Constructor for operations such as squaring
	 */
	public DoubleExpr(String name, DoubleCast aexpr, int operation) {
		super(name, aexpr, operation);
		this.domain = new DoubleComputedDomain(aexpr.getDoubleDomain(),
				operation);
	}

	/**
	 * Constructor for extending expression
	 */
	protected DoubleExpr(String name, BaseDoubleDomain domain) {
		super(name, domain);
	}

	/**
	 * Constructor for summation
	 */
	public DoubleExpr(String name, CspGenericDoubleCast aexpr,
			GenericIndex rangeIndices[],
			CspGenericIndexRestriction sourceIdxRestriction) {
		super(name, aexpr, rangeIndices, sourceIdxRestriction);
		this.domain = new SummationDoubleComputedDomain(
				(SummationDoubleDomainExpression) aexpr, sourceIdxRestriction);
		this.operation = NumOperation.SUMMATION;
	}

	/**
	 * Retrieves node for an expression
	 */
	public Node getNode() {
		if (node == null) {
			DoubleDomain domain = calculated ? new DoubleIntervalDomain(
					getMin(), getMax()) : getDoubleDomain();
			node = new DoubleNode(name, domain);
		}
		return node;
	}

	/**
	 * Returns the type of expression: Int, Long, etc.
	 */
	public int getNumberType() {
		return NumConstants.DOUBLE;
	}

	/**
	 * Returns domain of this expression
	 */
	public DoubleDomain getDoubleDomain() {
		return (DoubleDomain) domain;
	}

	/**
	 * Returns minimal value of expression
	 */
	public double getMin() {
		return getDoubleDomain().getMin();
	}

	/**
	 * Returns maximum value of expression
	 */
	public double getMax() {
		return getDoubleDomain().getMax();
	}

	/**
	 * Returns maximum value of this variable's domain
	 */
	public Number getNumMax() {
		max.setDoubleValue(getMax());
		return max;
	}

	/**
	 * Returns minimum value of this variable's domain
	 */
	public Number getNumMin() {
		min.setDoubleValue(getMin());
		return min;
	}

	// javadoc inherited
	public Number getNextHigher(Number val) {
		nextHigher.setDoubleValue(getDoubleDomain().getNextHigher(
				val.doubleValue()));
		return nextHigher;
	}

	// javadoc inherited
	public Number getNextLower(Number val) {
		nextLower.setDoubleValue(getDoubleDomain().getNextLower(
				val.doubleValue()));
		return nextLower;
	}

	// javadoc inherited
	public void setPrecision(double p) {
		getDoubleDomain().setPrecision(p);
	}

	// javadoc inherited
	public double getPrecision() {
		return getDoubleDomain().getPrecision();
	}

	// javadoc inherited
	public boolean isInDomain(Number n) {
		if (NumberMath.isIntEquivalent(n))
			return getDoubleDomain().isInDomain(n.doubleValue());
		else
			return false;
	}

	/**
	 * Returns an expression representing the sum of this expression with a
	 * static value
	 */
	public CspDoubleExpr add(double d) {
		return new DoubleExpr(null, this, NumOperation.ADD, d);
	}

	/**
	 * Returns an expression representing the sum of this expression with
	 * another expression
	 */
	public CspDoubleExpr add(CspDoubleCast expr) {
		if (expr instanceof CspGenericDoubleCast)
			return new GenericDoubleExpr(null, this, NumOperation.ADD,
					(CspGenericDoubleCast) expr);
		else
			return new DoubleExpr(null, this, NumOperation.ADD,
					(DoubleCast) expr);
	}

	/**
	 * Returns an expression representing the difference of this expression with
	 * a static value
	 */
	public CspDoubleExpr subtract(double d) {
		return new DoubleExpr(null, this, NumOperation.SUBTRACT, d);
	}

	/**
	 * Returns an expression representing the difference of this expression with
	 * another expression
	 */
	public CspDoubleExpr subtract(CspDoubleCast expr) {
		if (expr instanceof CspGenericDoubleCast)
			return new GenericDoubleExpr(null, this, NumOperation.SUBTRACT,
					(CspGenericDoubleCast) expr);
		else
			return new DoubleExpr(null, this, NumOperation.SUBTRACT,
					(DoubleCast) expr);
	}

	/**
	 * Returns an expression representing the product of this expression with a
	 * static value
	 */
	public CspDoubleExpr multiply(double d) {
		return new DoubleExpr(null, this, NumOperation.MULTIPLY, d);
	}

	/**
	 * Returns an expression representing the product of this expression with
	 * another expression
	 */
	public CspDoubleExpr multiply(CspDoubleCast expr) {
		if (expr == this)
			return new DoubleExpr(null, this, NumOperation.SQUARE);
		else if (expr instanceof CspGenericDoubleCast)
			return new GenericDoubleExpr(null, this, NumOperation.MULTIPLY,
					(CspGenericDoubleCast) expr);
		else
			return new DoubleExpr(null, this, NumOperation.MULTIPLY,
					(DoubleCast) expr);
	}

	/**
	 * Returns an expression representing the quotient of this expression with a
	 * static value
	 */
	public CspDoubleExpr divide(double d) {
		return new DoubleExpr(null, this, NumOperation.DIVIDE, d);
	}

	/**
	 * Returns an expression representing the quotient of this expression with
	 * another expression
	 */
	public CspDoubleExpr divide(CspDoubleCast expr) {
		if (expr instanceof CspGenericDoubleCast)
			return new GenericDoubleExpr(null, this, NumOperation.DIVIDE,
					(CspGenericDoubleCast) expr);
		else
			return new DoubleExpr(null, this, NumOperation.DIVIDE,
					(DoubleCast) expr);
	}

	/**
	 * Returns constraint restricting this expression to a value
	 */
	public CspConstraint eq(double val) {
		return constraint(new Double(val), ThreeVarConstraint.EQ);
	}

	/**
	 * Returns constraint restricting this expression to values below and
	 * including a given maximum
	 */
	public CspConstraint leq(double val) {
		return constraint(new Double(val), ThreeVarConstraint.LEQ);
	}

	/**
	 * Returns constraint restricting this expression to values above and
	 * including a given minimum
	 */
	public CspConstraint geq(double val) {
		return constraint(new Double(val), ThreeVarConstraint.GEQ);
	}

	/**
	 * Returns constraint restricting this expression to a value
	 */
	public CspConstraint eq(CspGenericDoubleConstant val) {
		return constraint((GenericDoubleConstant) val, ThreeVarConstraint.EQ);
	}

	/**
	 * Returns constraint restricting this expression to values below and
	 * including a given maximum
	 */
	public CspConstraint leq(CspGenericDoubleConstant val) {
		return constraint((GenericDoubleConstant) val, ThreeVarConstraint.LEQ);
	}

	/**
	 * Returns constraint restricting this expression to values above and
	 * including a given minimum
	 */
	public CspConstraint geq(CspGenericDoubleConstant val) {
		return constraint((GenericDoubleConstant) val, ThreeVarConstraint.GEQ);
	}

	// javadoc inherited
	public CspConstraint between(double min, boolean minExclusive, double max,
			boolean maxExclusive) {
		return new NumBetweenConstraint(new Double(min), minExclusive,
				new Double(max), maxExclusive, this);
	}

	// javadoc inherited
	public CspConstraint between(double min, double max) {
		return new NumBetweenConstraint(new Double(min), false,
				new Double(max), false, this);
	}

	// javadoc inherited
	public CspConstraint notBetween(double min, boolean minExclusive,
			double max, boolean maxExclusive) {
		return new NumNotBetweenConstraint(new Double(min), minExclusive,
				new Double(max), maxExclusive, this);
	}

	// javadoc inherited
	public CspConstraint notBetween(double min, double max) {
		return new NumNotBetweenConstraint(new Double(min), false, new Double(
				max), false, this);
	}
}