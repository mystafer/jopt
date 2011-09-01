/*
 * Created on May 10, 2005
 */

package jopt.csp.spi.arcalgorithm.variable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import jopt.csp.spi.arcalgorithm.constraint.bool.BoolExpr;
import jopt.csp.spi.arcalgorithm.constraint.bool.BooleanAndConstraint;
import jopt.csp.spi.arcalgorithm.constraint.bool.BooleanConstraint;
import jopt.csp.spi.arcalgorithm.constraint.bool.BooleanEqThreeVarConstraint;
import jopt.csp.spi.arcalgorithm.constraint.bool.BooleanEqTwoVarConstraint;
import jopt.csp.spi.arcalgorithm.constraint.bool.BooleanImpliesThreeVarConstraint;
import jopt.csp.spi.arcalgorithm.constraint.bool.BooleanImpliesTwoVarConstraint;
import jopt.csp.spi.arcalgorithm.constraint.bool.BooleanNotConstraint;
import jopt.csp.spi.arcalgorithm.constraint.bool.BooleanOrConstraint;
import jopt.csp.spi.arcalgorithm.constraint.bool.BooleanXorConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.NumConstraint;
import jopt.csp.spi.arcalgorithm.domain.BooleanComputedDomain;
import jopt.csp.spi.arcalgorithm.domain.BooleanDomain;
import jopt.csp.spi.arcalgorithm.domain.BooleanIntDomain;
import jopt.csp.spi.arcalgorithm.graph.GraphConstraint;
import jopt.csp.spi.arcalgorithm.graph.NodeArcGraph;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.node.BooleanNode;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.solver.VariableChangeListener;
import jopt.csp.spi.solver.VariableChangeSource;
import jopt.csp.spi.util.BoolOperation;
import jopt.csp.variable.CspBooleanExpr;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.CspGenericBooleanConstant;
import jopt.csp.variable.PropagationFailureException;

/**
 * A class used to create boolean expressions including AND, OR, NOT operations, etc.  An
 * expression usually does not have a concrete domain that can be modified, 
 * so it must be wrapped by a variable if it's value is to be directly altered.
 * 
 * @author jboerkoel
 * @author Nick Coleman
 */
public class BooleanExpr extends IntExpr implements CspBooleanExpr, BoolExpr {
    protected CspConstraint wrappedConstraint;
	
	protected boolean useBuildingBlockConstraint;
	
	protected BoolExpr aexpr;
	protected int operation;
	protected BoolExpr bexpr;
    protected boolean notB;
	protected BooleanNode node;
	protected boolean constVal;
	protected GenericBooleanConstant genConstVal;
    protected boolean calculated;
	
    /**
     * Constructor for expression that wraps a constraint and is based upon
     * a domain computed from the constraint
     * 
     * @param name          Unique name assigned to expression
     * @param constraint    Constraint this boolean expression wraps
     */
    public BooleanExpr(String name, CspConstraint constraint) {
        super(name, new BooleanComputedDomain(constraint));
        
        if (name==null) 
            throw new RuntimeException("name cannot be null");
        
        this.calculated = true;
        this.wrappedConstraint = constraint;
        this.useBuildingBlockConstraint = true;
    }
    
    /**
     * Constructor for use when creating generic expressions
     * 
     * @param name          Unique name assigned to expression     
     */
    protected BooleanExpr(String name) {
        super(name);
        
        if (name==null) 
            throw new RuntimeException("name cannot be null");
    }

    /**
     * Internal constructor used by variable class to create a variable
     * based on a concrete domain
     * 
     * @param name          Unique name assigned to expression
     * @param domain        Domain this expression is based upon
     * @see BooleanVariable
     */
    protected BooleanExpr(String name, BooleanDomain domain) {
        super(name, domain);
        
        if (name==null) 
            throw new RuntimeException("name cannot be null");
    }
    
    /**
     * Internal constructor for expression that wraps a constraint and is connected
     * to a specific domain.
     * 
     * @param name          Unique name assigned to expression
     * @param domain        Domain this expression is based upon
     * @param constraint    Constraint this boolean expression wraps
     */
    protected BooleanExpr(String name, BooleanDomain domain, CspConstraint constraint) {
        this(name, domain);
        
        if (name==null) 
            throw new RuntimeException("name cannot be null");
        
        this.wrappedConstraint = constraint;
        this.useBuildingBlockConstraint = true;
    }
    
    /**
     * Basic constructor that is called to initialize internal variables when building expressions
     * 
     * @param name          Unique name assigned to expression
     * @param aexpr         A variable of boolean expression
     * @param operation     Operation this expression represents such as AND, OR, etc.
     * @param bexpr         B variable of boolean expression
     * @param notB          True if operation is on !B instead of B
     * @param bConst        Constant b value of boolean expression
     */
    protected BooleanExpr(String name, BooleanExpr aexpr, int operation, BooleanExpr bexpr, boolean notB, boolean bConst, GenericBooleanConstant genBConst) {
        super(name, createComputedDomain(aexpr, operation, bexpr, notB, bConst,genBConst));
        
        this.aexpr = (BoolExpr)aexpr;
        this.operation = operation;
        this.bexpr = (BoolExpr)bexpr;
        this.constVal = bConst;
        this.genConstVal = genBConst;
        this.notB = notB;
        this.calculated = true;
        
        // generate name if not given
        if (name == null) {
            String a = (aexpr!=null) ? aexpr.getName() : null;	
            String b = null;
            if ((bexpr==null) && (genConstVal==null)) {
            	b = "const[" + bConst + "]";
            }
            else if ((bexpr==null)&& (genConstVal!=null))  {
                b = genConstVal.getName();
            }
            else {
                if (notB) 
                    b = "!" + bexpr.getName();
                else
                	b = bexpr.getName();
            }
            this.name = generateBoolName(a, operation, b);
        }

        this.useBuildingBlockConstraint = true;
    }
    
	/**
	 * Constructor for building expressions based on an operation on two
     * other variables
     * 
     * @param name          Unique name assigned to expression
     * @param aexpr         A variable of boolean expression
     * @param operation     Operation this expression represents such as AND, OR, etc.
     * @param bexpr         B variable of boolean expression
     * @param notB          True if operation is on !B instead of B
	 */
    public BooleanExpr(String name, BooleanExpr aexpr, int operation, BooleanExpr bexpr, boolean notB) {
        this(name, aexpr, operation, bexpr, notB, false, null);
	}
    
    /**
     * Constructor for building expressions based on an operation on one
     * other variable and a constant
     * 
     * @param name          Unique name assigned to expression
     * @param aexpr         A variable of boolean expression
     * @param operation     Operation this expression represents such as AND, OR, etc.
     * @param bConst        Constant b value of boolean expression
     */
    public BooleanExpr(String name, BooleanExpr aexpr, int operation, boolean bConst) {
        this(name, aexpr, operation, null, false, bConst, null);
	}
    
    /**
     * Constructor for building expressions based on an operation on one
     * other variable and a constant
     * 
     * @param name          Unique name assigned to expression
     * @param aexpr         A variable of boolean expression
     * @param operation     Operation this expression represents such as AND, OR, etc.
     * @param bConst        Constant b value of boolean expression
     */
    public BooleanExpr(String name, BooleanExpr aexpr, int operation, GenericBooleanConstant bConst) {
        this(name, aexpr, operation, null, false, false, bConst);
	}    

    /**
     * Constructor for building an expression based on an operation on one
     * other variable
     * 
     * @param name          Unique name assigned to expression
     * @param operation     Operation this expression represents such as EQ, IMPLIES or NOT
     * @param bexpr         A variable of boolean expression
     */
    public BooleanExpr(String name, int operation, BooleanExpr bexpr) {
        this(name, null, operation, bexpr, false, false, null);
	}
	
    /**
     * Returns array of arcs that will affect the boolean true / false
     * value of this constraint upon a change
     */
    public Arc[] getBooleanSourceArcs(){
        ArrayList<Arc> arcs = new ArrayList<Arc>();
        arcs.addAll(Arrays.asList(super.getBooleanSourceArcs()));
        if ((wrappedConstraint!=null)&&(wrappedConstraint instanceof NumConstraint))
            arcs.addAll(Arrays.asList(((NumConstraint)wrappedConstraint).getBooleanSourceArcs(false)));
        return (Arc[])arcs.toArray(new Arc[0]);
    }
    
	/**
	 * Returns node associated with this expression
	 */
	public Node getNode() {
		if (node==null) {
            BooleanDomain domain = calculated ? new BooleanIntDomain() : getBooleanDomain();
            node = new BooleanNode(name, domain);
        }
		return node;
	}
	
	/**
	 * Creates a boolean expression resulting from anding a boolean expression
	 * with this expression
	 */
	public CspBooleanExpr and (CspBooleanExpr expr) {
        if (expr instanceof GenericBooleanExpr)
            return new GenericBooleanExpr(null, this, BoolOperation.AND, (BooleanExpr) expr, false);
        else
        	return new BooleanExpr(null, this, BoolOperation.AND, (BooleanExpr) expr, false);
	}

	/**
	 * Creates a boolean expression resulting from anding a constraint
	 * with this expression
	 */
	public CspBooleanExpr and (CspConstraint cons) {
		// convert constraint to boolean variable then return and operation
		return and(new BooleanExpr(generateBoolName(cons), cons));
	}
	
	/**
	 * Creates a boolean expression resulting from anding a constant
	 * with this expression
	 */
	public CspBooleanExpr and (boolean val) {
		return new BooleanExpr(null, this, BoolOperation.AND, val);
	}
	
	/**
	 * Creates a boolean expression resulting from anding a constant
	 * with this expression
	 */
	public CspBooleanExpr and (CspGenericBooleanConstant val) {
		return new BooleanExpr(null, this, BoolOperation.AND, (GenericBooleanConstant)val);
	}
	
	/**
	 * Creates a boolean expression resulting from xoring a boolean expression
	 * with this expression
	 */
	public CspBooleanExpr xor(CspBooleanExpr expr) {
        if (expr instanceof GenericBooleanExpr)
            return new GenericBooleanExpr(null, this, BoolOperation.XOR, (BooleanExpr) expr, false);
        else
        	return new BooleanExpr(null, this, BoolOperation.XOR, (BooleanExpr) expr, false);
	}

	/**
	 * Creates a boolean expression resulting from xoring a constraint
	 * with this expression
	 */
	public CspBooleanExpr xor (CspConstraint cons) {
		// convert constraint to boolean variable then return and operation
		return xor(new BooleanExpr(generateBoolName(cons), cons));
	}
	
	/**
	 * Creates a boolean expression resulting from xoring a constant
	 * with this expression
	 */
	public CspBooleanExpr xor (boolean val) {
		return new BooleanExpr(null, this, BoolOperation.XOR, val);
	}	
	
	/**
	 * Creates a boolean expression resulting from xoring a constant
	 * with this expression
	 */
	public CspBooleanExpr xor (CspGenericBooleanConstant val) {
		return new BooleanExpr(null, this, BoolOperation.XOR, (GenericBooleanConstant) val);
	}	
	
	/**
	 * Creates a boolean expression resulting from this expression
     * implying another expression
	 */
	public CspBooleanExpr implies(CspBooleanExpr expr) {
        if (expr instanceof GenericBooleanExpr)
            return new GenericBooleanExpr(null, this, BoolOperation.IMPLIES, (BooleanExpr) expr, false);
        else
        	return new BooleanExpr(null, this, BoolOperation.IMPLIES, (BooleanExpr) expr, false);
	}

    /**
     * Creates a boolean expression resulting from this expression
     * implying another expression
     */
    public CspBooleanExpr implies(boolean val) {
        String name = generateBoolName(this.name, BoolOperation.IMPLIES, Boolean.toString(val));
        
        // if implying T, the result will always be true
        if (val) {
        	BooleanVariable b = new BooleanVariable(name);
            
            // this is a brand new variable, it should never fail
            try {
            	b.setTrue();
            }
            catch(PropagationFailureException ignored){}
            
            return b;
        }
        
        // if implying false, resulting expression must be equal
        // to false to still evaluate to true (I don't like this statement.
        // It's dizzying)
        else {
            return eq(false);
        }
    }
    
	/**
	 * Creates a boolean expression resulting from anding a constant
	 * with this expression
	 */
	public CspBooleanExpr implies(CspGenericBooleanConstant val) {
		return new BooleanExpr(null, this, BoolOperation.IMPLIES, (GenericBooleanConstant)val);
	}

    /**
     * Creates a boolean expression resulting from this expression
     * implying a constraint
     */
	public CspBooleanExpr implies(CspConstraint cons) {
		// convert constraint to boolean variable then return implies operation
		return implies(new BooleanExpr(generateBoolName(cons), cons));
	}
	
	/**
	 * Creates a boolean expression resulting from oring a boolean expression
	 * with this expression
	 */
	public CspBooleanExpr or(CspBooleanExpr expr) {
        if (expr instanceof GenericBooleanExpr)
            return new GenericBooleanExpr(null, (BooleanExpr) expr, BoolOperation.OR, this, false);
        else
        	return new BooleanExpr(null, (BooleanExpr) expr, BoolOperation.OR, this, false);
	}

	/**
	 * Creates a boolean expression resulting from oring a constraint
	 * with this expression
	 */
	public CspBooleanExpr or (CspConstraint cons) {
		// convert constraint to boolean variable then return or operation
		return or(new BooleanExpr(generateBoolName(cons), cons));
	}
	
	/**
	 * Creates a boolean expression resulting from oring a constant
	 * with this expression
	 */
	public CspBooleanExpr or (boolean val) {
		return new BooleanExpr(null, this, BoolOperation.OR, val);
	}	
	
	/**
	 * Creates a boolean expression resulting from oring a constant
	 * with this expression
	 */
	public CspBooleanExpr or (CspGenericBooleanConstant val) {
		return new BooleanExpr(null, this, BoolOperation.OR,(GenericBooleanConstant) val);
	}		
	
	/**
	 * Creates a boolean expression resulting from setting a boolean expression
	 * equal to this expression
	 */
	public CspBooleanExpr eq (CspBooleanExpr expr) {
        if (expr instanceof GenericBooleanExpr)
            return new GenericBooleanExpr(null, this, BoolOperation.EQ, (BooleanExpr) expr, false);
        else
        	return new BooleanExpr(null, this, BoolOperation.EQ, (BooleanExpr) expr, false);
	}

	/**
	 * Creates a boolean expression resulting from setting a boolean expression
	 * equal to this expression
	 */
	public CspBooleanExpr eq (CspConstraint cons) {
		return new BooleanExpr(generateBoolName(cons), cons);
	}

    /**
     * Creates a boolean expression resulting from setting a boolean expression
     * equal to this expression
     */
    public CspBooleanExpr eq (boolean val) {
        if (val)
            return this;
        else
        	return not();
    }

    // javadoc inherited from CspBooleanExpr
	public CspConstraint toConstraint() {
	    if(constraint==null) {
	        constraint = createExternalConstraint();
	    }
		return constraint;
	}
	
    // javadoc inherited from CspBooleanExpr
	public CspConstraint createExternalConstraint() {
	    CspConstraint constraint = null;
	    
	    // Because equality and implication can be either a three variable or two variable operation,
	    // special considerations must be made when exposing a constraint respresenting this expression
	    if (wrappedConstraint!=null) {
	        constraint = createBuildingBlockConstraint();
	    }
	    
	    else if (aexpr==null) {
	        //TODO: simply calculate; note that given an expr and true, the expr is determined
	    }
	    
	    else if (bexpr==null) {
	        //TODO: simply calculate; note that given an expr, a const, and true, the expr is determined
	    }
	    
	    // If none of the above conditions are met, the external constraint is the building
	    // block constraint set to true
	    else {
	        switch(operation) {
                case BoolOperation.AND:
                    constraint = new BooleanAndConstraint(aexpr, bexpr, notB, true);
                    break;
                    
                case BoolOperation.OR:
                    constraint = new BooleanOrConstraint(aexpr, bexpr, notB, true);
                    break;
                    
                case BoolOperation.XOR:
                    constraint = new BooleanXorConstraint(aexpr, bexpr, notB, true);
                    break;
                    
                case BoolOperation.EQ:
                    constraint = new BooleanEqThreeVarConstraint(aexpr, bexpr, notB, true);
                	break;
                
                case BoolOperation.IMPLIES:
                    constraint = new BooleanImpliesThreeVarConstraint(aexpr, bexpr, notB, true);
                	break;
            }
	    }
	    
	    return constraint;
	}
	
	
    // javadoc inherited from CspBooleanExpr
    public CspConstraint createBuildingBlockConstraint() {
        CspConstraint constraint = null;

        if(wrappedConstraint!=null) {
            constraint = new BooleanEqTwoVarConstraint(this, (GraphConstraint) wrappedConstraint);
        }
        
        else if (aexpr==null) {
            switch(operation) {
                case BoolOperation.NOT:
                    constraint = new BooleanNotConstraint(this, bexpr);
                	break;
                
                case BoolOperation.EQ:
                    constraint = new BooleanEqTwoVarConstraint(this, bexpr);
                	break;
                
                case BoolOperation.IMPLIES:
                    constraint = new BooleanImpliesTwoVarConstraint(this, bexpr);
                	break;
            }
        }
        else if (bexpr==null) {
            switch(operation) {
                case BoolOperation.AND:
                    constraint = new BooleanAndConstraint(aexpr, constVal, this);
                    break;
                    
                case BoolOperation.OR:
                    constraint = new BooleanOrConstraint(aexpr, constVal, this);
                    break;
                    
                case BoolOperation.XOR:
                    constraint = new BooleanXorConstraint(aexpr, constVal, this);
                    break;
                    
                case BoolOperation.EQ:
                    constraint = new BooleanEqThreeVarConstraint(aexpr, constVal, this);
                    break;
                    
                case BoolOperation.IMPLIES:
                    constraint = new BooleanImpliesThreeVarConstraint(aexpr, constVal, this);
                	break;   
            }
        }
        else {
            switch(operation) {
                case BoolOperation.AND:
                    constraint = new BooleanAndConstraint(aexpr, bexpr, notB, this);
                    break;
                    
                case BoolOperation.OR:
                    constraint = new BooleanOrConstraint(aexpr, bexpr, notB, this);
                    break;
                    
                case BoolOperation.XOR:
                    constraint = new BooleanXorConstraint(aexpr, bexpr, notB, this);
                    break;
                    
                case BoolOperation.EQ:
                    constraint = new BooleanEqThreeVarConstraint(aexpr, bexpr, notB, this);
                	break;
                
                case BoolOperation.IMPLIES:
                    constraint = new BooleanImpliesThreeVarConstraint(aexpr, bexpr, notB, this);
                	break;
            }
        }
        
        return constraint;
    }
    
    /**
     * Returns a Boolean Variable equal to the Not of this one
     */
    public CspBooleanExpr not() {
    	return new BooleanExpr(null, BoolOperation.NOT, this);
    }
    
    /**
     * Returns a BoolExpr equal to the Not of this one
     */
    public BoolExpr notExpr() {
    	return (BoolExpr) not();
    }
    
    /**
     * Returns true if variable cannot be satisfied
     */
    public boolean isFalse() {
        return getBooleanDomain().isFalse();
    }
    
    /**
     * Returns true if variable cannot be dissatisfied
     */
    public boolean isTrue() {
        return getBooleanDomain().isTrue();
    }

    /**
     * Returns the size of this variable's domain
     */
    public int getSize() {
        return domain.getSize();
    }
    
    /**
     * Returns the domain corresponding to this expression
     */
    public BooleanDomain getBooleanDomain() {
    	return (BooleanDomain) domain;
    }
    
    /**
     * Adds arcs representing this expression to the node arc graph
     */
    public void updateGraph(NodeArcGraph graph) {
        //Update graph with constraint
        if(!graph.containsNode(getNode())) {
            graph.addNode(getNode());
            
            if(useBuildingBlockConstraint) {
                GraphConstraint gcons = (GraphConstraint) createBuildingBlockConstraint();
                if (gcons!=null) {
	                gcons.associateToGraph(graph);
	                gcons.postToGraph();
                }
            }
        }
    }
    
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(name);
        buf.append(":");
        buf.append(domain);
        
        return buf.toString();
    }

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
     * Generates a unique boolean name for variables that wraps a constraint
     */
    private String generateBoolName(CspConstraint cnst) {
        StringBuffer buf = new StringBuffer("_(cns-wrap[");
        buf.append(cnst);
        buf.append("])");
        return buf.toString();
    }
    
    /**
     * Generates a unique boolean name for variables that are built
     * via operations
     */
    private String generateBoolName(String operand1, int operation, String operand2) {
        StringBuffer buf = new StringBuffer("_(");
        if (operand1!=null) {
            buf.append(operand1);
            buf.append(" ");
        }
        buf.append(boolOpDesc(operation));
        buf.append(" ");
        buf.append(operand2);
        buf.append(")");
        return buf.toString();
    }
    
    /**
     * Converts a boolean operation to a string
     */
    private String boolOpDesc(int operation) {
        switch(operation) {
            case BoolOperation.EQ:
                return "=";
            case BoolOperation.AND:
                return "&";
            case BoolOperation.OR:
                return "|"; 
            case BoolOperation.XOR:
                return "^";
            case BoolOperation.NOT:
                return "!";
            case BoolOperation.IMPLIES:
                return "->";
            default:
                return "?";
        }
    }
    
    /**
     * Creates a computed domain
     */
    private static BooleanDomain createComputedDomain(BooleanExpr aexpr, int operation, BooleanExpr bexpr, boolean notB, boolean bConst, GenericBooleanConstant genConstVal) {
        
        BooleanDomain adom = (aexpr!=null) ? aexpr.getBooleanDomain() : null;
        BooleanDomain bdom = (bexpr!=null) ? bexpr.getBooleanDomain() : null;
        return new BooleanComputedDomain(adom, bdom, notB, bConst, genConstVal, operation);
    }
    
    /**
     * Adds a listener interested in variable change events
     */
    public void addVariableChangeListener(VariableChangeListener listener) {
        addVariableChangeListener(listener,true);
    }
    
    /**
     * Adds a listener interested in variable change events
     */
    public void addVariableChangeListener(VariableChangeListener listener, boolean firstTime) {
        super.addVariableChangeListener(listener);
        
        if (aexpr!=null)
            ((VariableChangeSource) aexpr).addVariableChangeListener(listener);
        if (bexpr!=null)
            ((VariableChangeSource) bexpr).addVariableChangeListener(listener);
        if(firstTime) {
            CspConstraint buildingBlockConstraint = null;
            if (useBuildingBlockConstraint)
                buildingBlockConstraint = createBuildingBlockConstraint();
            if ((buildingBlockConstraint!=null) && (buildingBlockConstraint instanceof BooleanConstraint)){
                ((BooleanConstraint)buildingBlockConstraint).addVariableChangeListener(listener, false);
            }
        }
    }
}
