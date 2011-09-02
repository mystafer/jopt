package jopt.csp.spi.arcalgorithm.variable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import jopt.csp.spi.arcalgorithm.constraint.num.AbsConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.AcosConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.AsinConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.AtanConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.CosConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.DiffConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.ExpConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.GenericNumExpr;
import jopt.csp.spi.arcalgorithm.constraint.num.NatLogConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.NumConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.NumExpr;
import jopt.csp.spi.arcalgorithm.constraint.num.NumNotBetweenConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.NumRelationConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.PowerConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.ProdConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.QuotConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.SinConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.SquareConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.SumConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.SummationConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.TanConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.ThreeVarConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.global.NumBetweenConstraint;
import jopt.csp.spi.arcalgorithm.domain.NumDomain;
import jopt.csp.spi.arcalgorithm.graph.GraphConstraint;
import jopt.csp.spi.arcalgorithm.graph.NodeArcGraph;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.solver.VariableChangeListener;
import jopt.csp.spi.solver.VariableChangeSource;
import jopt.csp.spi.util.GenericIndex;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NameUtil;
import jopt.csp.spi.util.NumOperation;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.CspGenericIndexRestriction;
import jopt.csp.variable.CspGenericNumConstant;
import jopt.csp.variable.CspGenericNumExpr;
import jopt.csp.variable.CspNumExpr;

public abstract class NumExprBase extends VariableChangeBase implements CspNumExpr, NumExpr {
    protected String name;
    // Stores operation type.  The interface NumOperation contains an enumeration of possible operations that are associated with different ints.
    protected int operation;
    protected NumExpr aexpr;
    protected Number aconst;
    protected GenericNumConstant aGenConst;
    protected NumExpr bexpr;
    protected Number bconst;
    protected GenericNumConstant bGenConst;
    protected NumDomain domain;
    protected CspConstraint constraint;
    protected boolean calculated;

    protected GenericIndex rangeIndices[];
    private CspGenericIndexRestriction sourceIdxRestriction;

    /**
     * Constructor
     */
    protected NumExprBase(String name) {
        this.name = name;
    }

    /**
     * Constructor
     */
    protected NumExprBase(String name, NumDomain domain) {
        this.name = name;
        this.domain = domain;
    }

    /**
     * Constructor
     */
    protected NumExprBase(String name, CspNumExpr aexpr, int operation, CspNumExpr bexpr, 
            Number aconst, Number bconst, GenericNumConstant aGenConst, GenericNumConstant bGenConst) {

        this.operation = operation;
        this.aexpr = (NumExpr) aexpr;
        this.bexpr = (NumExpr) bexpr;
        this.aGenConst = aGenConst;
        this.bGenConst = bGenConst;
        this.aconst = NumberMath.toConst(aconst);
        this.bconst = NumberMath.toConst(bconst);
        this.calculated = true;

        // generate name if not given; care must be taken to make absolutely sure that the name is unique
        if (name == null) {
            String a = (aexpr==null) ? "const[" + aconst + "]" : aexpr.getName();
            String b = (bexpr==null) ? "const[" + bconst + "]" : bexpr.getName();
            this.name = generateNumName(a, operation, b);
        }
        else
            this.name = name;
    }

    /**
     * Constructor
     */
    protected NumExprBase(String name, CspNumExpr aexpr, int operation, CspNumExpr bexpr) {
        this(name, aexpr, operation, bexpr, null, null, null, null);
    }

    /**
     * Constructor
     */
    protected NumExprBase(String name, Number aconst, int operation, CspNumExpr bexpr) {
        this(name, null, operation, bexpr, aconst, null, null, null);
    }

    /**
     * Constructor
     */
    protected NumExprBase(String name, GenericNumConstant aconst, int operation, CspNumExpr bexpr) {
        this(name, null, operation, bexpr, null, null, aconst, null);
    }

    /**
     * Constructor
     */
    protected NumExprBase(String name, CspNumExpr aexpr, int operation, Number bconst) {
        this(name, aexpr, operation, null, null, bconst, null, null);
    }

    /**
     * Constructor
     */
    protected NumExprBase(String name, CspNumExpr aexpr, int operation, GenericNumConstant bconst) {
        this(name, aexpr, operation, null, null, null, null, bconst);
    }

    /**
     * Constructor
     */
    protected NumExprBase(String name, CspNumExpr aexpr, int operation) {
        this(name, aexpr, operation, null, null, null, null, null);
    }

    /**
     * Constructor
     */
    protected NumExprBase(String name, CspGenericNumExpr aexpr, GenericIndex rangeIndices[],
            CspGenericIndexRestriction sourceIdxRestriction) 
    {
        this(name, aexpr, NumOperation.SUMMATION, null, null, null, null, null);
        this.rangeIndices = rangeIndices;
        this.sourceIdxRestriction = sourceIdxRestriction;
    }

    /**
     * Sets the name of this expression
     */
    public final void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the name of this expression
     */
    public final String getName() {
        return name;
    }

    /**
     * Returns domain associated with this expression
     */
    public final NumDomain getDomain() {
        return domain;
    }

    /**
     * Returns true if this expression's domain is bound to a value
     */
    public boolean isBound() {
        return domain.isBound();
    }

    /**
     * Returns collection of all nodes used to build this expression 
     */
    public Collection<Node> getNodeCollection() {
        Collection<Node> nodes = null;

        // retrieve nodes from a
        if (aexpr!=null)
            nodes = aexpr.getNodeCollection();

        // retrieve nodes from b
        if (bexpr!=null) {
            if (nodes==null)
                nodes = bexpr.getNodeCollection();
            else
                nodes.addAll(bexpr.getNodeCollection());
        }

        // add this node to collection
        if (nodes==null)
            nodes = new ArrayList<Node>();
        nodes.add(getNode());

        return nodes;
    }

    /**
     * Returns array of arcs that will affect the boolean true / false
     * value of this constraint upon a change
     */
    public Arc[] getBooleanSourceArcs(){
        ArrayList<Arc> arcs = new ArrayList<Arc>();
        if(constraint == null) {
            constraint = createGraphConstraint();
        }
        if ((constraint!=null)&&(constraint instanceof NumConstraint))
            arcs.addAll(Arrays.asList(((GraphConstraint)constraint).getBooleanSourceArcs()));
        if (aexpr !=null)
            arcs.addAll(Arrays.asList(aexpr.getBooleanSourceArcs()));
        if (bexpr != null)
            arcs.addAll(Arrays.asList(bexpr.getBooleanSourceArcs()));
        return (Arc[])arcs.toArray(new Arc[0]);
    }

    /**
     * Adds arcs representing this expression to the node arc graph
     */
    public void updateGraph(NodeArcGraph graph) {
        // Create constraint if necessary
        if (constraint==null) 
            constraint = createGraphConstraint();

        // Update graph with constraint
        if (!graph.containsNode(getNode())) {
            graph.addNode(getNode());

            if (constraint != null) {
                GraphConstraint gcons = (GraphConstraint) constraint;
                gcons.associateToGraph(graph);
                gcons.postToGraph();
            }
        }
    }

    /**
     * Generates the internal constraint for this expression
     */
    private CspConstraint createGraphConstraint() {
        CspConstraint constraint = null;

        switch (operation) {
        case NumOperation.ADD:
            if (aconst != null)
                constraint = new SumConstraint(aconst, bexpr, this, ThreeVarConstraint.EQ);
            else if (aGenConst != null)
                constraint = new SumConstraint(aGenConst , bexpr, this, ThreeVarConstraint.EQ);                
            else if (bconst != null)
                constraint = new SumConstraint(aexpr, bconst, this, ThreeVarConstraint.EQ);
            else if (bGenConst != null)
                constraint = new SumConstraint(aexpr, bGenConst, this, ThreeVarConstraint.EQ);
            else
                constraint = new SumConstraint(aexpr, bexpr, this, ThreeVarConstraint.EQ);
            break;

        case NumOperation.SUBTRACT:
            if (aconst != null)
                constraint = new DiffConstraint(aconst, bexpr, this, ThreeVarConstraint.EQ);
            else if (aGenConst != null)
                constraint = new DiffConstraint(aGenConst, bexpr, this, ThreeVarConstraint.EQ);
            else if (bconst != null)
                constraint = new DiffConstraint(aexpr, bconst, this, ThreeVarConstraint.EQ);
            else if (bGenConst != null)
                constraint = new DiffConstraint(aexpr, bGenConst, this, ThreeVarConstraint.EQ);
            else
                constraint = new DiffConstraint(aexpr, bexpr, this, ThreeVarConstraint.EQ);
            break;

        case NumOperation.MULTIPLY:
            if (aconst != null)
                constraint = new ProdConstraint(aconst, bexpr, this, ThreeVarConstraint.EQ);
            else if (aGenConst!= null)
                constraint = new ProdConstraint(aGenConst, bexpr, this, ThreeVarConstraint.EQ);
            else if (bconst != null)
                constraint = new ProdConstraint(aexpr, bconst, this, ThreeVarConstraint.EQ);
            else if (bGenConst != null)
                constraint = new ProdConstraint(aexpr, bGenConst, this, ThreeVarConstraint.EQ);
            else
                constraint = new ProdConstraint(aexpr, bexpr, this, ThreeVarConstraint.EQ);
            break;

        case NumOperation.DIVIDE:
            if (aconst != null)
                constraint = new QuotConstraint(aconst, bexpr, this, ThreeVarConstraint.EQ);
            else if (aGenConst != null)
                constraint = new QuotConstraint(aGenConst, bexpr, this, ThreeVarConstraint.EQ);
            else if (bconst != null)
                constraint = new QuotConstraint(aexpr, bconst, this, ThreeVarConstraint.EQ);
            else if (bGenConst != null)
                constraint = new QuotConstraint(aexpr, bGenConst, this, ThreeVarConstraint.EQ);
            else
                constraint = new QuotConstraint(aexpr, bexpr, this, ThreeVarConstraint.EQ);
            break;

        case NumOperation.POWER:
            if (aconst != null)
                constraint = new PowerConstraint(aconst, bexpr, this);
            else if (bconst != null)
                constraint = new PowerConstraint(aexpr, bconst, this);
            else
                constraint = new PowerConstraint(aexpr, bexpr, this);
            break;

        case NumOperation.SQUARE:
            constraint = new SquareConstraint(aexpr, this, ThreeVarConstraint.EQ);
            break;

        case NumOperation.ABS:
            constraint = new AbsConstraint(aexpr, this, ThreeVarConstraint.EQ);
            break;

        case NumOperation.EXP:
            constraint = new ExpConstraint(aexpr, this);
            break;

        case NumOperation.NAT_LOG:
            constraint = new NatLogConstraint(aexpr, this);
            break;

        case NumOperation.SIN:
            constraint = new SinConstraint(aexpr, this);
            break;

        case NumOperation.COS:
            constraint = new CosConstraint(aexpr, this);
            break;

        case NumOperation.TAN:
            constraint = new TanConstraint(aexpr, this);
            break;

        case NumOperation.ASIN:
            constraint = new AsinConstraint(aexpr, this);
            break;

        case NumOperation.ACOS:
            constraint = new AcosConstraint(aexpr, this);
            break;

        case NumOperation.ATAN:
            constraint = new AtanConstraint(aexpr, this);
            break;

        case NumOperation.SUMMATION:
            if (aexpr!=null) {
                constraint = new SummationConstraint((GenericNumExpr) aexpr, rangeIndices, sourceIdxRestriction, this, ThreeVarConstraint.EQ);
            }
            break;
        }

        return constraint;
    }

    /**
     * Returns constraint restricting this expression to a value
     */
    public CspConstraint eq(CspNumExpr val) {
        return constraint((NumExpr) val, ThreeVarConstraint.EQ);
    }

    /**
     * Returns constraint restricting this expression to values below
     * and including a given maximum
     */
    public CspConstraint leq(CspNumExpr val) {
        return constraint((NumExpr) val, ThreeVarConstraint.LEQ);
    }

    /**
     * Returns constraint restricting this expression to values below
     * a given maximum
     */
    public CspConstraint lt(CspNumExpr val) {
        return constraint((NumExpr) val, ThreeVarConstraint.LT);
    }

    /**
     * Returns constraint restricting this expression to values above
     * a given minimum
     */
    public CspConstraint gt(CspNumExpr val) {
        return constraint((NumExpr) val, ThreeVarConstraint.GT);
    }

    /**
     * Returns constraint restricting this expression to values above
     * and including a given minimum
     */
    public CspConstraint geq(CspNumExpr val) {
        return constraint((NumExpr) val, ThreeVarConstraint.GEQ);
    }

    /**
     * Returns constraint restricting this expression to all values
     * not equivalent to supplied value
     */
    public CspConstraint neq(CspNumExpr val) {
        return constraint((NumExpr) val, ThreeVarConstraint.NEQ);
    }

    /**
     * Returns a constraint restricting this expression to be between a min and max.
     * 
     * @param  min          value that this expression must be greater than
     * @param  minExclusive true if start of range does not include minimum value
     * @param  max          value that this expression must be less than
     * @param  maxExclusive true if end of range does not include maximum value 
     * @return constraint restricting this expression to be between a min and max.
     */
    public CspConstraint between(CspNumExpr min, boolean minExclusive, CspNumExpr max, boolean maxExclusive) {
        return new NumBetweenConstraint((NumExpr) min, minExclusive, (NumExpr) max, maxExclusive, this);
    }

    /**
     * Returns a constraint restricting this expression to be between or equal
     * min and max.
     * 
     * @param  min  value that this expression must be greater than
     * @param  max  value that this expression must be less than
     * @return constraint restricting this expression to be between or equal to min and max
     */
    public CspConstraint between(CspNumExpr min, CspNumExpr max) {
        return new NumBetweenConstraint((NumExpr) min, false, (NumExpr) max, false, this);
    }

    /**
     * Returns a constraint restricting this expression to be between a min and max.
     * 
     * @param  min  value that this expression must be greater than
     * @param  minExclusive true if start of range does not include minimum value
     * @param  max  value that this expression must be less than
     * @param  maxExclusive true if end of range does not include maximum value 
     * @return constraint restricting this expression to be between a min and max.
     */
    public CspConstraint between(CspGenericNumConstant min, boolean minExclusive, CspGenericNumConstant max, boolean maxExclusive) {
        return new NumBetweenConstraint(min, minExclusive, max, maxExclusive, this);
    }

    /**
     * Returns a constraint restricting this expression to be  between or equal
     * min and max.
     * 
     * @param  min  value that this expression must be greater than
     * @param  max  value that this expression must be less than
     * @return constraint restricting this expression to be between or equal to min and max
     */
    public CspConstraint between(CspGenericNumConstant min, CspGenericNumConstant max) {
        return new NumBetweenConstraint(min, false, max, false, this);
    }

    /**
     * Returns a constraint restricting this expression to not fall within a given range
     * from a min to max value
     * 
     * @param  min          start of values that this expression may not contain
     * @param  minExclusive true if start of range does not include minimum value
     * @param  max          start of values that this expression may not contain
     * @param  maxExclusive true if end of range does not include maximum value 
     * @return constraint restricting this expression to not fall within a given range
     */
    public CspConstraint notBetween(CspNumExpr min, boolean minExclusive, CspNumExpr max, boolean maxExclusive) {
        return new NumNotBetweenConstraint((NumExpr) min, minExclusive, (NumExpr) max, maxExclusive, this);
    }

    /**
     * Returns a constraint restricting this expression to not fall within a given range
     * greater than or equal to a min value up to less than or equal a max value
     * 
     * @param  min          start of values that this expression may not contain
     * @param  max          start of values that this expression may not contain
     * @return constraint restricting this expression to not fall within a given range
     */
    public CspConstraint notBetween(CspNumExpr min, CspNumExpr max) {
        return new NumNotBetweenConstraint((NumExpr) min, false, (NumExpr) max, false, this);
    }

    /**
     * Returns a constraint restricting this expression to not fall within a given range
     * from a min to max value
     * 
     * @param  min          start of values that this expression may not contain
     * @param  minExclusive true if start of range does not include minimum value
     * @param  max          start of values that this expression may not contain
     * @param  maxExclusive true if end of range does not include maximum value 
     * @return constraint restricting this expression to not fall within a given range
     */
    public CspConstraint notBetween(CspGenericNumConstant min, boolean minExclusive, CspGenericNumConstant max, boolean maxExclusive) {
        return new NumNotBetweenConstraint(min, minExclusive, max, maxExclusive, this);
    }

    /**
     * Returns a constraint restricting this expression to not fall within a given range
     * greater than or equal to a min value up to less than or equal a max value
     * 
     * @param  min          start of values that this expression may not contain
     * @param  max          start of values that this expression may not contain
     * @return constraint restricting this expression to not fall within a given range
     */
    public CspConstraint notBetween(CspGenericNumConstant min, CspGenericNumConstant max) {
        return new NumNotBetweenConstraint(min, false, max, false, this);
    }

    /**
     * Returns constraint restricting this expression to a value
     */
    protected CspConstraint constraint(Number val, int constraintType) {
        CspConstraint constraint = null;

        // Current expression is a variable
        if (operation == NumOperation.NONE)
            constraint = new NumRelationConstraint(this, val, constraintType);

        // Current expression is composed of 2 other expressions
        else if (aexpr!=null && bexpr != null) {
            switch(operation) {
            case NumOperation.ADD:
                constraint = new SumConstraint(aexpr, bexpr, val, constraintType);
                break;

            case NumOperation.SUBTRACT:
                constraint = new DiffConstraint(aexpr, bexpr, val, constraintType);
                break;

            case NumOperation.MULTIPLY:
                constraint = new ProdConstraint(aexpr, bexpr, val, constraintType);
                break;

            case NumOperation.DIVIDE:
                constraint = new QuotConstraint(aexpr, bexpr, val, constraintType);
                break;

            case NumOperation.POWER:
                constraint = new PowerConstraint(aexpr, bexpr, val);
                break;
            }
        }

        // Current expression is composed of 1 expression and 1 constant
        else if (aconst != null) {
            MutableNumber n = new MutableNumber();
            int nodeType = NumberMath.numberType(aconst);

            switch(operation) {
            case NumOperation.ADD:
                // a + Z = v -> Z = v - a
                NumberMath.subtractNoInvalid(val, aconst, nodeType, n);
                break;

            case NumOperation.SUBTRACT:
                // a - Z = v -> Z = a - v
                NumberMath.subtractNoInvalid(aconst, val, nodeType, n);
                break;

            case NumOperation.MULTIPLY:
                // a * Z = v -> Z = v / a
                if (constraintType == ThreeVarConstraint.LT || constraintType == ThreeVarConstraint.LEQ)
                    NumberMath.divideCeil(val, aconst, nodeType, n);

                else if (constraintType == ThreeVarConstraint.GT || constraintType == ThreeVarConstraint.GEQ)
                    NumberMath.divideFloor(val, aconst, nodeType, n);

                else {
                    NumberMath.divideNoInvalid(val, aconst, nodeType, n);
                }
                break;

            case NumOperation.DIVIDE:
                // a / Z = v -> Z = a / v
                if (constraintType == ThreeVarConstraint.LT || constraintType == ThreeVarConstraint.LEQ)
                    NumberMath.divideCeil(aconst, val, nodeType, n);

                else if (constraintType == ThreeVarConstraint.GT || constraintType == ThreeVarConstraint.GEQ)
                    NumberMath.divideFloor(aconst, val, nodeType, n);

                else {
                    NumberMath.divideNoInvalid(aconst, val, nodeType, n);
                }
                break;

            case NumOperation.POWER:
                // a^Z = v -> Z = loga(v)
                n.setDoubleValue(Math.log(aconst.doubleValue()) / Math.log(val.doubleValue()));
                break;
            }

            constraint = new NumRelationConstraint(bexpr, n, constraintType);
        }

        // Current expression is composed of 1 expression and 1 generic constant
        else if (aGenConst != null) {
            CspGenericNumConstant n = null;


            switch(operation) {
            case NumOperation.ADD:
                // a + Z = v -> Z = v - a
                n = aGenConst.subtractFrom(val);
                break;

            case NumOperation.SUBTRACT:
                // a - Z = v -> Z = a - v
                n= aGenConst.subtract(val);
                break;

            case NumOperation.MULTIPLY:
                // a * Z = v -> Z = v / a
                if (constraintType == ThreeVarConstraint.LT || constraintType == ThreeVarConstraint.LEQ)
                    n = aGenConst.divideCeil(val);

                else if (constraintType == ThreeVarConstraint.GT || constraintType == ThreeVarConstraint.GEQ)
                    n = aGenConst.divideFloor(val);

                else {
                    n = aGenConst.divide(val);
                }
                break;

            case NumOperation.DIVIDE:
                // a / Z = v -> Z = a / v
                if (constraintType == ThreeVarConstraint.LT || constraintType == ThreeVarConstraint.LEQ)
                    n = aGenConst.divideByCeil(val);

                else if (constraintType == ThreeVarConstraint.GT || constraintType == ThreeVarConstraint.GEQ)
                    n = aGenConst.divideByFloor(val);

                else {
                    n = aGenConst.divideBy(val);
                }
                break;

            case NumOperation.POWER:
                // a^Z = v -> Z = loga(v)
                //NOTE: The PowerConstraint does not support generic constants at this point
                break;
            }

            constraint = new NumRelationConstraint(bexpr, n, constraintType);
        }

        // Current expression is composed of 1 expression and 1 constant
        else if (bconst != null) {
            MutableNumber n =  new MutableNumber();
            int nodeType = NumberMath.numberType(bconst);

            switch(operation) {
            case NumOperation.ADD:
                // Z + b = v -> Z = v - b
                NumberMath.subtractNoInvalid(val, bconst, nodeType, n);
                break;

            case NumOperation.SUBTRACT:
                // Z - b = v -> Z = v + b
                NumberMath.addNoInvalid(val, bconst, nodeType, n);
                break;

            case NumOperation.MULTIPLY:
                // Z * b = v -> Z = v / b
                if (constraintType == ThreeVarConstraint.LT || constraintType == ThreeVarConstraint.LEQ)
                    NumberMath.divideCeil(val, bconst, nodeType, n);

                else if (constraintType == ThreeVarConstraint.GT || constraintType == ThreeVarConstraint.GEQ)
                    NumberMath.divideFloor(val, bconst, nodeType, n);

                else {
                    NumberMath.divideNoInvalid(val, bconst, nodeType, n);
                }
                break;

            case NumOperation.DIVIDE:
                // Z / b = v -> Z = v * b
                NumberMath.multiplyNoInvalid(val, bconst, nodeType, n);
                break;

            case NumOperation.POWER:
                // Z^b = v -> Z = v^(1/b)
                n.setDoubleValue(Math.pow(val.doubleValue(), 1.0d / bconst.doubleValue()));
                break;
            }

            constraint = new NumRelationConstraint(aexpr, n, constraintType);
        }

        // Current expression is composed of 1 expression and 1 generic constant
        else if (bGenConst != null) {
            CspGenericNumConstant n = null;

            switch(operation) {
            case NumOperation.ADD:
                // Z + b = v -> Z = v - b
                n = bGenConst.subtractFrom(val);
                break;

            case NumOperation.SUBTRACT:
                // Z - b = v -> Z = v + b
                n = bGenConst.add(val);
                break;

            case NumOperation.MULTIPLY:
                // Z * b = v -> Z = v / b
                if (constraintType == ThreeVarConstraint.LT || constraintType == ThreeVarConstraint.LEQ)
                    n = bGenConst.divideCeil(val);

                else if (constraintType == ThreeVarConstraint.GT || constraintType == ThreeVarConstraint.GEQ)
                    n = bGenConst.divideFloor(val);

                else {
                    n = bGenConst.divide(val);
                }
                break;

            case NumOperation.DIVIDE:
                // Z / b = v -> Z = v * b
                n = bGenConst.multiply(val);
                break;

            case NumOperation.POWER:
                // Z^b = v -> Z = v^(1/b)
                //NOTE:  PowerConstraint does not currently support generic constants
                break;
            }

            constraint = new NumRelationConstraint(aexpr, n, constraintType);
        }

        // Current expression is composed of an operation on a single expression
        else {
            switch (operation) {
            case NumOperation.SQUARE:
                constraint = new SquareConstraint(aexpr, val, constraintType);
                break;

                //TODO:  Change1  Changes necessary to make direct creation of summation possible
            case NumOperation.SUMMATION:
//              if(aexpr==null) {
//              if ((this.constraint!=null)&&(this.constraint instanceof SummationConstraint)) {
//              constraint = ((SummationConstraint)this.constraint).createConstraint(val,constraintType);
//              }
//              }
//              else {
                constraint = new SummationConstraint((GenericNumExpr) aexpr, rangeIndices, sourceIdxRestriction, val, constraintType);
//              }
                break;

            case NumOperation.ABS:
                constraint = new AbsConstraint(val, aexpr, constraintType);
                break;
            }
        }

        return constraint;
    }

    /**
     * Returns constraint restricting this expression to a value
     */
    protected CspConstraint constraint(NumExpr val, int constraintType) {
        CspConstraint constraint = null;

        // Current expression is a variable
        if (operation == NumOperation.NONE)
            constraint = new NumRelationConstraint(this, val, constraintType);

        // Current expression is composed of 2 other expressions
        else if (aexpr!=null && bexpr != null) {
            switch(operation) {
            case NumOperation.ADD:
                constraint =  new SumConstraint(aexpr, bexpr, val, constraintType);
                break;

            case NumOperation.SUBTRACT:
                constraint =  new DiffConstraint(aexpr, bexpr, val, constraintType);
                break;

            case NumOperation.MULTIPLY:
                constraint =  new ProdConstraint(aexpr, bexpr, val, constraintType);
                break;

            case NumOperation.DIVIDE:
                constraint =  new QuotConstraint(aexpr, bexpr, val, constraintType);
                break;

            case NumOperation.POWER:
                constraint = new PowerConstraint(aexpr, bexpr, val);
                break;
            }
        }

        // Current expression is composed of 1 expression and 1 constant
        else if ((aconst != null) || (aGenConst!=null)) {
            if (aconst!=null) {
                switch(operation) {
                case NumOperation.ADD:
                    constraint =  new SumConstraint(aconst, bexpr, val, constraintType);
                    break;

                case NumOperation.SUBTRACT:
                    constraint =  new DiffConstraint(aconst, bexpr, val, constraintType);
                    break;

                case NumOperation.MULTIPLY:
                    constraint =  new ProdConstraint(aconst, bexpr, val, constraintType);
                    break;

                case NumOperation.DIVIDE:
                    constraint =  new QuotConstraint(aconst, bexpr, val, constraintType);
                    break;

                case NumOperation.POWER:
                    constraint = new PowerConstraint(aconst, bexpr, val);
                    break;
                }
            }
            else {
                switch(operation) {
                case NumOperation.ADD:
                    constraint =  new SumConstraint(aGenConst, bexpr, val, constraintType);
                    break;

                case NumOperation.SUBTRACT:
                    constraint =  new DiffConstraint(aGenConst, bexpr, val, constraintType);
                    break;

                case NumOperation.MULTIPLY:
                    constraint =  new ProdConstraint(aGenConst, bexpr, val, constraintType);
                    break;

                case NumOperation.DIVIDE:
                    constraint =  new QuotConstraint(aGenConst, bexpr, val, constraintType);
                    break;

                }
            }
        }

        // Current expression is composed of 1 expression and 1 constant
        else if ((bconst != null)||(bGenConst!=null)) {
            if (bconst != null) {

                switch(operation) {
                case NumOperation.ADD:
                    constraint =  new SumConstraint(aexpr, bconst, val, constraintType);
                    break;

                case NumOperation.SUBTRACT:
                    constraint =  new DiffConstraint(aexpr, bconst, val, constraintType);
                    break;

                case NumOperation.MULTIPLY:
                    constraint =  new ProdConstraint(aexpr, bconst, val, constraintType);
                    break;

                case NumOperation.DIVIDE:
                    constraint =  new QuotConstraint(aexpr, bconst, val, constraintType);
                    break;

                case NumOperation.POWER:
                    constraint = new PowerConstraint(aexpr, bconst, val);
                    break;
                }
            }
            else {
                switch(operation) {
                case NumOperation.ADD:
                    constraint =  new SumConstraint(aexpr, bGenConst, val, constraintType);
                    break;

                case NumOperation.SUBTRACT:
                    constraint =  new DiffConstraint(aexpr, bGenConst, val, constraintType);
                    break;

                case NumOperation.MULTIPLY:
                    constraint =  new ProdConstraint(aexpr, bGenConst, val, constraintType);
                    break;

                case NumOperation.DIVIDE:
                    constraint =  new QuotConstraint(aexpr, bGenConst, val, constraintType);
                    break;
                } 
            }
        }

        // Current expression is composed of an operation on a single expression
        else {
            switch (operation) {
            case NumOperation.SQUARE:
                constraint = new SquareConstraint(aexpr, val, constraintType);
                break;

            case NumOperation.ABS:
                constraint = new AbsConstraint(aexpr, val, constraintType);
                break;

            case NumOperation.SUMMATION:
//              TODO: Change2Changes necessary to make direct creation of summation possible
                constraint = new SummationConstraint((GenericNumExpr) aexpr, rangeIndices, sourceIdxRestriction, val, constraintType);
//              if(aexpr==null) {
//              if ((this.constraint!=null)&&(this.constraint instanceof SummationConstraint)) {
//              constraint = ((SummationConstraint)this.constraint).createConstraint(val,constraintType);
//              }
//              }
//              else {
//              constraint = new SummationConstraint((GenericNumExpr) aexpr, rangeIndices, sourceIdxRestriction, val, constraintType);
//              }
                break;
            }
        }

        return constraint;
    }

    /**
     * Returns constraint restricting this expression to a value
     */
    protected CspConstraint constraint(GenericNumConstant vals, int constraintType) {
        CspConstraint constraint = null;

        // Current expression is a variable
        if (operation == NumOperation.NONE)
            constraint = new NumRelationConstraint(this, vals, constraintType);

        // Current expression is composed of 2 other expressions
        else if (aexpr!=null && bexpr != null) {
            switch(operation) {
            case NumOperation.ADD:
                constraint = new SumConstraint(aexpr, bexpr, vals, constraintType);
                break;

            case NumOperation.SUBTRACT:
                constraint = new DiffConstraint(aexpr, bexpr, vals, constraintType);
                break;

            case NumOperation.MULTIPLY:
                constraint = new ProdConstraint(aexpr, bexpr, vals, constraintType);
                break;

            case NumOperation.DIVIDE:
                constraint = new QuotConstraint(aexpr, bexpr, vals, constraintType);
                break;
            }
        }

        // Current expression is composed of 1 expression and 1 constant
        else if (aconst != null) {
            CspGenericNumConstant n=null;
            switch(operation) {
            case NumOperation.ADD:
                // a + Z = v -> Z = v - a
                n = vals.subtract(aconst);
                break;

            case NumOperation.SUBTRACT:
                // a - Z = v -> Z = a - v
                n = vals.subtractFrom(aconst);
                break;

            case NumOperation.MULTIPLY:
                // a * Z = v -> Z = v / a
                if (constraintType == ThreeVarConstraint.LT || constraintType == ThreeVarConstraint.LEQ)
                    n = vals.divideByCeil(aconst);

                else if (constraintType == ThreeVarConstraint.GT || constraintType == ThreeVarConstraint.GEQ)
                    n = vals.divideByFloor(aconst);

                else {
                    n = vals.divideBy(aconst);
                }
                break;

            case NumOperation.DIVIDE:
                // a / Z = v -> Z = a / v
                if (constraintType == ThreeVarConstraint.LT || constraintType == ThreeVarConstraint.LEQ)
                    n = vals.divideCeil(aconst);

                else if (constraintType == ThreeVarConstraint.GT || constraintType == ThreeVarConstraint.GEQ)
                    n = vals.divideFloor(aconst);

                else {
                    n = vals.divide(aconst);
                }
                break;
            }

            constraint = new NumRelationConstraint(bexpr, n, constraintType);
        }

        // Current expression is composed of 1 expression and 1 generic constant
        else if (aGenConst != null) {
            CspGenericNumConstant n = null;
            switch(operation) {
            case NumOperation.ADD:
                // a + Z = v -> Z = v - a
                n = aGenConst.subtractFrom(vals);
                break;

            case NumOperation.SUBTRACT:
                // a - Z = v -> Z = a - v
                n= aGenConst.subtract(vals);
                break;

            case NumOperation.MULTIPLY:
                // a * Z = v -> Z = v / a
                if (constraintType == ThreeVarConstraint.LT || constraintType == ThreeVarConstraint.LEQ)
                    n = aGenConst.divideCeil(vals);

                else if (constraintType == ThreeVarConstraint.GT || constraintType == ThreeVarConstraint.GEQ)
                    n = aGenConst.divideFloor(vals);

                else {
                    n = aGenConst.divide(vals);
                }
                break;

            case NumOperation.DIVIDE:
                // a / Z = v -> Z = a / v
                if (constraintType == ThreeVarConstraint.LT || constraintType == ThreeVarConstraint.LEQ)
                    n = aGenConst.divideByCeil(vals);

                else if (constraintType == ThreeVarConstraint.GT || constraintType == ThreeVarConstraint.GEQ)
                    n = aGenConst.divideByFloor(vals);

                else {
                    n = aGenConst.divideBy(vals);
                }
                break;

            case NumOperation.POWER:
                // a^Z = v -> Z = loga(v)
                //NOTE: The PowerConstraint does not support generic constants at this point
                break;
            }
            constraint = new NumRelationConstraint(bexpr, n, constraintType);
        }

        // Current expression is composed of 1 expression and 1 constant
        else if (bconst != null) {
            CspGenericNumConstant n = null;

            switch(operation) {
            case NumOperation.ADD:
                // Z + b = v -> Z = v - b
                n = vals.subtract(bconst);
                break;

            case NumOperation.SUBTRACT:
                // Z - b = v -> Z = v + b
                n = vals.add(bconst);
                break;

            case NumOperation.MULTIPLY:
                // Z * b = v -> Z = v / b
                if (constraintType == ThreeVarConstraint.LT || constraintType == ThreeVarConstraint.LEQ)
                    n = vals.divideByCeil(bconst);

                else if (constraintType == ThreeVarConstraint.GT || constraintType == ThreeVarConstraint.GEQ)
                    n = vals.divideByFloor(bconst);

                else {
                    n = vals.divideBy(bconst);
                }
                break;

            case NumOperation.DIVIDE:
                // Z / b = v -> Z = v * b
                n = vals.multiply(bconst);
                break;
            }

            constraint = new NumRelationConstraint(aexpr, n, constraintType);
        }

        // Current expression is composed of 1 expression and 1 generic constant
        else if (bGenConst != null) {
            CspGenericNumConstant n = null;
            switch(operation) {
            case NumOperation.ADD:
                // Z + b = v -> Z = v - b
                n = bGenConst.subtractFrom(vals);
                break;

            case NumOperation.SUBTRACT:
                // Z - b = v -> Z = v + b
                n = bGenConst.add(vals);
                break;

            case NumOperation.MULTIPLY:
                // Z * b = v -> Z = v / b
                if (constraintType == ThreeVarConstraint.LT || constraintType == ThreeVarConstraint.LEQ)
                    n = bGenConst.divideCeil(vals);

                else if (constraintType == ThreeVarConstraint.GT || constraintType == ThreeVarConstraint.GEQ)
                    n = bGenConst.divideFloor(vals);

                else {
                    n = bGenConst.divide(vals);
                }
                break;

            case NumOperation.DIVIDE:
                // Z / b = v -> Z = v * b
                n = bGenConst.multiply(vals);
                break;

            case NumOperation.POWER:
                // Z^b = v -> Z = v^(1/b)
                //NOTE:  PowerConstraint does not currently support generic constants
                break;
            }

            constraint = new NumRelationConstraint(aexpr, n, constraintType);

        }
//      TODO: Change 3  Changes necessary to make direct creation of summation possible
//      // Current expression is composed of an operation on a single expression
//      else {
//      switch (operation) {
//      case NumOperation.SUMMATION:
//      if(aexpr==null) {
//      if ((this.constraint!=null)&&(this.constraint instanceof SummationConstraint)) {
//      constraint = ((SummationConstraint)this.constraint).createConstraint(vals,constraintType);
//      }
//      }
//      else {
//      constraint = new SummationConstraint((GenericNumExpr) aexpr, rangeIndices, sourceIdxRestriction, vals, constraintType);
//      }
//      break;
//      }
//      }
        return constraint;
    }


    /**
     * Returns a hash code based on this variable's name (if one exists)
     */
    public int hashCode() {
        if(name != null) {
            return this.name.hashCode();
        }
        else {
            return super.hashCode();
        }
    }

    /**
     * Allows for equality comparisons between NumExprBase objects
     * 
     * @param obj
     * @return true if the specified object is a NumExprBase
     * and its name is identical to this NumExprBase object
     */
    // This has to be final because the way it's currently done with
    // instanceof instead of getClass means that if it is overridden
    // it will violate the reflexive contract for equals.
    public final boolean equals(Object obj) {
        if(obj instanceof NumExprBase) {
            return ((NumExprBase) obj).getName().equals(this.name);
        }
        else {
            return false;
        }
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();

        if (name == null)
            buf.append("~");
        else
            buf.append(name);

        buf.append(":");
        buf.append(domain);

        return buf.toString();
    }

    /**
     * Adds a listener interested in variable change events
     */
    public void addVariableChangeListener(VariableChangeListener listener) {
        super.addVariableChangeListener(listener);

        if (aexpr!=null)
            ((VariableChangeSource) aexpr).addVariableChangeListener(listener);
        if (bexpr!=null)
            ((VariableChangeSource) bexpr).addVariableChangeListener(listener);
    }

    /**
     * Removes a variable listener from this variable
     */
    public void removeVariableChangeListener(VariableChangeListener listener) {
        super.removeVariableChangeListener(listener);

        if (aexpr!=null)
            ((VariableChangeSource) aexpr).removeVariableChangeListener(listener);
        if (bexpr!=null)
            ((VariableChangeSource) bexpr).removeVariableChangeListener(listener);
    }

    /**
     * Generates a unique numeric name for variables that are built
     * via operations
     */
    private String generateNumName(String operand1, int operation, String operand2) {
        StringBuffer buf = new StringBuffer("_"+NameUtil.nextName()+"(");
        buf.append(operand1);
        buf.append(" ");
        buf.append(NumOperation.operationName(operation));

        if (operation != NumOperation.SQUARE) {
            buf.append(" ");
            buf.append(operand2);
        }

        buf.append(")");
        //   return buf.toString();
        return NameUtil.nextName();
    }

}