package jopt.csp.spi.arcalgorithm.variable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import jopt.csp.spi.arcalgorithm.constraint.bool.BoolExpr;
import jopt.csp.spi.arcalgorithm.constraint.bool.GenericBoolExpr;
import jopt.csp.spi.arcalgorithm.graph.GraphConstraint;
import jopt.csp.spi.arcalgorithm.graph.NodeArcGraph;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.node.BooleanNode;
import jopt.csp.spi.arcalgorithm.graph.node.GenericBooleanNode;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.solver.VariableChangeListener;
import jopt.csp.spi.solver.VariableChangeSource;
import jopt.csp.spi.util.BoolOperation;
import jopt.csp.spi.util.GenericIndex;
import jopt.csp.spi.util.IndexIterator;
import jopt.csp.variable.CspBooleanExpr;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.CspGenericBooleanConstant;
import jopt.csp.variable.CspGenericBooleanExpr;
import jopt.csp.variable.CspGenericIndex;

/**
 * A generic boolean variable such as Xi which represents X1, X2, etc.
 */
public class GenericBooleanExpr extends BooleanExpr implements CspGenericBooleanExpr, GenericBoolExpr {
    protected GenericIndex indices[];
    protected int indexOffsets[];
    protected BoolExpr exprs[];
    private BooleanNode nodes[];
    protected GenericBooleanExpr gaexpr;
    protected GenericBooleanExpr gbexpr;
    
    protected GenericBooleanNode genericNode;
    
    /**
     * Constructor for an expression that wraps an array of boolean expressions 
     * 
     * @param name          Unique name assigned to expression
     * @param indices       Array of indices expression is indexed upon
     * @param exprs         Array of boolean expressions to wrap
     */
    public GenericBooleanExpr(String name, CspGenericIndex indices[], BoolExpr exprs[]) 
    {
        super(name);
        this.indices = VarFactory.toGenericIndex(indices);
        this.exprs = exprs;
        
        // initialize object
        init(null);
    }
    
    /**
     * Constructor for generic expression that wraps a constraint
     * 
     * @param name          Unique name assigned to expression
     * @param indices       Array of indices expression is indexed upon
     * @param constraint    Constraint this boolean expression wraps
     */
    public GenericBooleanExpr(String name, CspGenericIndex indices[], CspConstraint constraint) 
    {
        super(name,constraint);
        this.indices = VarFactory.toGenericIndex(indices);
        //Note that caluclated is false because the generic boolean expression
        // is not calculated, as a whole, from the specified generic-based constraint.
        this.calculated=false;
        // initialize object
        init(constraint);
    }
    
    /**
     * Internal Constructor for building expressions
     */
    private GenericBooleanExpr(String name, BooleanExpr aexpr, int operation, BooleanExpr bexpr, boolean notB, boolean bConst, GenericBooleanConstant genBConst)
    {
        super(name, aexpr, operation, bexpr, bConst, notB, genBConst);
        
        // ensure at least one expression is an index
        if (aexpr instanceof GenericBooleanExpr) gaexpr = (GenericBooleanExpr) aexpr;
        if (bexpr instanceof GenericBooleanExpr) gbexpr = (GenericBooleanExpr) bexpr;
        if (gaexpr==null && gbexpr==null) {
            throw new IllegalStateException("At least one generic expression is required to perform a boolean operation with generic expressions");
        }
        
        // build list of indices
        HashSet<CspGenericIndex> indexSet = new HashSet<CspGenericIndex>();
        if (gaexpr!=null) indexSet.addAll(Arrays.asList(gaexpr.getIndices()));
        if (gbexpr!=null) indexSet.addAll(Arrays.asList(gbexpr.getIndices()));
        this.indices = (GenericIndex[]) indexSet.toArray(new GenericIndex[indexSet.size()]);
        
        // initialize object
        init(null);
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
    public GenericBooleanExpr(String name, BooleanExpr aexpr, int operation, BooleanExpr bexpr, boolean notB) {
    	this(name, aexpr, operation, bexpr, notB, false,null);
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
    public GenericBooleanExpr(String name, BooleanExpr aexpr, int operation, boolean bConst) {
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
    public GenericBooleanExpr(String name, BooleanExpr aexpr, int operation, GenericBooleanConstant bConst) {
        this(name, aexpr, operation, null, false, false, bConst);
    }

    /**
     * Constructor for building an expression based on an operation on one
     * other variable
     * 
     * @param name          Unique name assigned to expression
     * @param operation     Operation this expression represents such as EQ or NOT
     * @param bexpr         A variable of boolean expression
     */
    public GenericBooleanExpr(String name, int operation, BooleanExpr bexpr) {
        this(name, null, operation, bexpr, false, false, null);
    }
    
    /**
     * Initializes internal variables during construction of object
     * 
     * @param fragCnst	Constraint that is going to be fragmented when mapping a generic to a constraint
     */
    private void init(CspConstraint fragCnst) {
        // calculate index offsets and total expressions required
        indexOffsets = new int[indices.length];
        indexOffsets[indices.length-1] = 1;
        int totalExprs = indices[indices.length-1].size();
        for (int i=indices.length-2; i>=0; i--) {
            // offset will equal product of previous index size * previous index offset
            int prevIdx = i+1;
            indexOffsets[i] = indices[prevIdx].size() * indexOffsets[prevIdx];
            
            // total nodes is product of all sizes
            totalExprs *= indices[i].size();
        }

        // generate expressions for indices
        if (exprs==null) {
            this.exprs = new BooleanExpr[totalExprs];
            
            if (calculated)
            	generateCalculatedExpressions();
            else
                generateConstraintFragmentExpressions((GraphConstraint) fragCnst);
        }
        
        // ensure correct number of expressions are given for indices
        else if (totalExprs != exprs.length) {
            throw new IllegalStateException("Expected " + totalExprs + " expressions, but received " + exprs.length);
        }
    }
    
    /**
     * Function that is used to produce an array of calculated expressions for use
     * when boolean operations have been used to create this expression
     */
    protected void generateCalculatedExpressions() {
        IndexIterator idxIterator = new IndexIterator(Arrays.asList(indices));
        int currOffset = 0;
        while (idxIterator.hasNext()) {
            idxIterator.next();
            
            // create expressions based on input values
            if (aexpr==null) {
                BooleanExpr b = (BooleanExpr) gbexpr.getBoolExpressionForIndex();
                exprs[currOffset] = new BooleanExpr(null, operation, b);
            }
            if (bexpr==null) {
                BooleanExpr a = (BooleanExpr) gaexpr.getBoolExpressionForIndex();
                //Check to see if we need to find the current constant
                boolean currentConstVal=constVal;
                if (genConstVal!=null) currentConstVal=genConstVal.getBooleanForIndex();
                exprs[currOffset] = new BooleanExpr(null, a, operation, currentConstVal);
            }
            else {
                BooleanExpr a = null;
                BooleanExpr b = null;
                if(gaexpr!=null){
                    a = (BooleanExpr) gaexpr.getBoolExpressionForIndex();
                }
                else {
                    a = (BooleanExpr)aexpr;
                }
                if(gbexpr!=null){
                    b = (BooleanExpr) gbexpr.getBoolExpressionForIndex();
                }
                else {
                    b = (BooleanExpr)bexpr;
                }
                exprs[currOffset] = new BooleanExpr(null, a, operation, b, notB);
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
            genericNode = new GenericBooleanNode(name, indices, getExpressionNodes());
        return genericNode;
    }

    /**
     * Function that is used to produce an array of expressions where each
     * on is built upon a fragment of the expression this generic is
     * based upon
     */
    private void generateConstraintFragmentExpressions(GraphConstraint gc) {
        // loop over indices for boolean and generate expressions for
        // each fragment of constraint
        IndexIterator idxIterator = new IndexIterator(Arrays.asList(indices));
        int currOffset = 0;
        while (idxIterator.hasNext()) {
            idxIterator.next();
            
            // create fragment of constraint
            CspConstraint cnst = gc.getGraphConstraintFragment(indices);
            
            // create expression based on constraint
            StringBuffer name = new StringBuffer("_frag");
            name.append(currOffset).append("[").append(cnst).append("]");
            exprs[currOffset] = new BooleanExpr(name.toString(), cnst);
            
            // update current offset
            currOffset++;
        }
    }

    /**
     * Returns the nodes for all expressions in the internal array
     */
    public BooleanNode[] getExpressionNodes() {
        // retrieve nodes for expression
        if (nodes==null) {
            this.nodes = new BooleanNode[exprs.length];
            for (int i=0; i<exprs.length; i++) {
                BoolExpr expr = exprs[i];
                nodes[i] = (BooleanNode)expr.getNode();
            }
        }
        
        return nodes;
    }
    
    /**
     * Returns the generic indices that is associated with this expression
     */
    public CspGenericIndex[] getIndices() {
    	return indices;
    }
    
    /**
     * Returns the generic indices that is associated with this expression
     */
    public GenericIndex[] getGenericIndices() {
    	return indices;
    }
    
    /**
     * Returns true if this expression contains the given index
     */
    public boolean containsIndex(GenericIndex index) {
    	for (int i=0; i<indices.length; i++)
            if (index.equals(indices[i]))
                return true;
            
        return false;
    }
    
    /**
     * Returns true if all internal expressions are bound
     */
    public boolean isBound() {
        for (int i=0; i<exprs.length; i++)
            if (!exprs[i].isBound()) return false;
            
        return true;
    }

    /**
     * Returns the internal variable corresponding to the associated index's current value
     */
    public BoolExpr getBoolExpressionForIndex() {
        // determine index of expression to return
        int exprIdx = 0;
        for (int i=0; i<indices.length; i++)
            exprIdx += indices[i].currentVal() * indexOffsets[i];
        
    	return exprs[exprIdx];
    }
    
    /**
     * Returns the number of expressions that are wrapped by this generic node
     */
    public int getExpressionCount() {
        return exprs.length;
    }
    
    /**
     * returns a boolean expression from the internal array
     * 
     * @param offset  Offset of expression in internal expression array
     */
    public BoolExpr getBoolExpression(int offset) {
        if (offset < 0 || offset >= exprs.length) return null;
    	return exprs[offset];
    }
    
    // javadoc inherited from CspGenericBooleanExpr
    public CspBooleanExpr getExpression(int offset) {
        return (CspBooleanExpr) getBoolExpression(offset);
    }
    
    /**
     * Displays name of variable along with indices
     */
    public String toString() {
        if (name==null) {
            name = "~";
        }
        StringBuffer buf = new StringBuffer(name);
        buf.append(":[");
        for (int i=0; i<indices.length; i++) {
        	if (i>0) buf.append(",");
            buf.append(indices[i].getName());
        }
        buf.append("]");
        
        return buf.toString();
    }

    /**
     * Adds a listener interested in variable change events
     */
    public void addVariableChangeListener(VariableChangeListener listener) {
        super.addVariableChangeListener(listener);
        
        for (int i=0; i<exprs.length; i++) {
            ((VariableChangeSource) exprs[i]).addVariableChangeListener(listener);
        }
    }
    
    /**
     * Adds a listener interested in variable change events
     */
    public void addVariableChangeListener(VariableChangeListener listener, boolean firstTime) {
        super.addVariableChangeListener(listener, firstTime);
        for (int i=0; i<exprs.length; i++) {
            ((VariableChangeSource) exprs[i]).addVariableChangeListener(listener);
        }
    }
    
    /**
     * Removes a variable listener from this variable
     */
    public void removeVariableChangeListener(VariableChangeListener listener) {
        super.removeVariableChangeListener(listener);
        
        for (int i=0; i<exprs.length; i++) {
            ((VariableChangeSource) exprs[i]).removeVariableChangeListener(listener);
        }
    }

    /**
     * Adds arcs representing this expression to the node arc graph
     */
    public void updateGraph(NodeArcGraph graph) {
        super.updateGraph(graph);
        
        // update graph with child expressions
        for (int i=0; i<exprs.length; i++) {
            // add child expressions and constraints
            // of child to graph
            BoolExpr expr = exprs[i];
            expr.updateGraph(graph);
        }
    }

    /**
     * Updates associated indices to values corresponding to the node offset in
     * the internal array
     */
    public final void setIndicesToVariableOffset(int offset) {
        for (int i=0; i<indexOffsets.length; i++) {
            int idxOffset = indexOffsets[i];
            int idxVal = offset / idxOffset;
            
            // update index
            indices[i].changeVal(idxVal);
            
            // strip current index from offset
            offset %= idxOffset;
        }
    }

    // javadoc inherited from jopt.csp.spi.arcalgorithm.constraint.num.GenericBoolExpr
    // used by constraints when creating fragments of constraints
    public final BoolExpr createFragment(GenericIndex fragIndices[]) {
        // build list of indices that are not contained in expression
        List<CspGenericIndex> remainingIdxList = new LinkedList<CspGenericIndex>(Arrays.asList(indices));
        List<GenericIndex> fragIdxList = Arrays.asList(fragIndices);
        remainingIdxList.removeAll(fragIdxList);
        
        // if all indices are used for variable, return the specific
        // expression at the given indice combination
        if (remainingIdxList.size()==0) {
            return getBoolExpressionForIndex();
        }
        
        // no indices are used, return this expression as a whole
        else if (remainingIdxList.size() == indices.length) {
        	return this;
        }
        
        else {
            // Iterator over remaing indices and build list of variables
            // that make up fragment
            LinkedList<BoolExpr> fragExprs = new LinkedList<BoolExpr>();
            IndexIterator iter = new IndexIterator(remainingIdxList);
            while (iter.hasNext()) {
                iter.next();
                fragExprs.add(getBoolExpressionForIndex());
            }
            
            // create generic expression that represents fragment
            GenericIndex remainingIndices[] = (GenericIndex[]) remainingIdxList.toArray(new GenericIndex[remainingIdxList.size()]);
            BoolExpr exprs[] = (BoolExpr[]) fragExprs.toArray(new BoolExpr[fragExprs.size()]);
            return new GenericBooleanExpr(null, remainingIndices, exprs);
        }
    }

    /**
     * Creates a boolean expression resulting from anding a boolean expression
     * with this expression
     */
    public CspBooleanExpr and (CspBooleanExpr expr) {
        return new GenericBooleanExpr(null, this, BoolOperation.AND, (BooleanExpr) expr, false);
    }

    /**
     * Creates a boolean expression resulting from anding a constraint
     * with this expression
     */
    public CspBooleanExpr and (CspConstraint cons) {
        // convert constraint to boolean variable then return and operation
        return and(new GenericBooleanExpr(null, indices, cons));
    }
    
    /**
     * Creates a boolean expression resulting from anding a constant
     * with this expression
     */
    public CspBooleanExpr and (boolean val) {
        return new GenericBooleanExpr(null, this, BoolOperation.AND, val);
    }
    
    /**
     * Creates a boolean expression resulting from anding a constant
     * with this expression
     */
    public CspBooleanExpr and (CspGenericBooleanConstant val) {
        return new GenericBooleanExpr(null, this, BoolOperation.AND, (GenericBooleanConstant)val);
    }
    
    
    /**
     * Creates a boolean expression resulting from xoring a boolean expression
     * with this expression
     */
    public CspBooleanExpr xor(CspBooleanExpr expr) {
        return new GenericBooleanExpr(null, this, BoolOperation.XOR, (BooleanExpr) expr, false);
    }

    /**
     * Creates a boolean expression resulting from xoring a constraint
     * with this expression
     */
    public CspBooleanExpr xor (CspConstraint cons) {
        // convert constraint to boolean variable then return and operation
        return xor(new GenericBooleanExpr(null, indices, cons));
    }
    
    /**
     * Creates a boolean expression resulting from xoring a constant
     * with this expression
     */
    public CspBooleanExpr xor (boolean val) {
        return new GenericBooleanExpr(null, this, BoolOperation.XOR, val);
    }   
    
    /**
     * Creates a boolean expression resulting from xoring a constant
     * with this expression
     */
    public CspBooleanExpr xor (CspGenericBooleanConstant val) {
        return new GenericBooleanExpr(null, this, BoolOperation.XOR, (GenericBooleanConstant)val);
    } 
    
    
    /**
     * Creates a boolean expression resulting from this expression
     * implying another expression
     */
    public CspBooleanExpr implies(CspBooleanExpr expr) {
        return new GenericBooleanExpr(null, this, BoolOperation.IMPLIES, (BooleanExpr) expr, false);
    }

    /**
     * Creates a boolean expression resulting from this expression
     * implying another expression
     */
    public CspBooleanExpr implies(boolean val) {
        return new GenericBooleanExpr(null, this, BoolOperation.IMPLIES, val);
    }
    
    /**
     * Creates a boolean expression resulting from this expression
     * implying another expression
     */
    public CspBooleanExpr implies(CspGenericBooleanConstant val) {
        return new GenericBooleanExpr(null, this, BoolOperation.IMPLIES, (GenericBooleanConstant)val);
    } 

    /**
     * Creates a boolean expression resulting from oring a boolean expression
     * with this expression
     */
    public CspBooleanExpr or(CspBooleanExpr expr) {
        return new GenericBooleanExpr(null, (BooleanExpr) expr, BoolOperation.OR, this, false);
    }

    /**
     * Creates a boolean expression resulting from oring a constraint
     * with this expression
     */
    public CspBooleanExpr or (CspConstraint cons) {
        // convert constraint to boolean variable then return and operation
        return or(new GenericBooleanExpr(null, indices, cons));
    }
    
    /**
     * Creates a boolean expression resulting from oring a constant
     * with this expression
     */
    public CspBooleanExpr or (boolean val) {
        return new GenericBooleanExpr(null, this, BoolOperation.OR, val);
    }   

    /**
     * Creates a boolean expression resulting from oring a constant
     * with this expression
     */
    public CspBooleanExpr or (CspGenericBooleanConstant val) {
        return new GenericBooleanExpr(null, this, BoolOperation.OR, (GenericBooleanConstant) val);
    }   
    
    /**
     * Creates a boolean expression resulting from setting a boolean expression
     * equal to this expression
     */
    public CspBooleanExpr eq (CspBooleanExpr expr) {
        return new GenericBooleanExpr(this.getName()+"="+expr.getName(), this, BoolOperation.EQ, (BooleanExpr) expr, false);
    }
    
    /**
     * Returns array of arcs that will affect the boolean true / false
     * value of this constraint upon a change
     */
    public Arc[] getBooleanSourceArcs(){
        ArrayList<Arc> arcs = new ArrayList<Arc>();
//            arcs.addAll(Arrays.asList(super.getBooleanSourceArcs()));
        for (int i=0; i< exprs.length; i++) {
            if (exprs[i]!= null)
       	        arcs.addAll(Arrays.asList(exprs[i].getBooleanSourceArcs()));
        	}
        return (Arc[])arcs.toArray(new Arc[0]);
    }
    /**
     * Creates a boolean expression resulting from setting a boolean expression
     * equal to this expression
     */
    public CspBooleanExpr eq (CspGenericBooleanConstant expr) {
        return new GenericBooleanExpr(this.getName()+"="+expr.getName(),this, BoolOperation.EQ, (GenericBooleanConstant) expr);
    }

    
    /**
     * Creates a boolean expression resulting from setting a boolean expression
     * equal to this expression
     */
    public CspBooleanExpr eq (CspConstraint cons) {
        return new GenericBooleanExpr(null, indices, cons);
    }
    
    /**
     * Returns a Boolean Variable equal to the Not of this one
     */
    public CspBooleanExpr not() {
    	return new GenericBooleanExpr(null, BoolOperation.NOT, this);
    }
    
    /**
     * Returns a BoolExpr equal to the Not of this one
     */
    public BoolExpr notExpr() {
    	return (GenericBoolExpr) not();
    }


    // javadoc is inherited
    public boolean isAnyFalse() {
        return isAnyFalse(0, exprs.length-1);
    }
    
    // javadoc is inherited
    public boolean isAnyFalse(int s, int e) {
        for (int i=s; i<=e; i++)
            if (exprs[i].isFalse()) return true;
            
        return false;
    }
    
    // javadoc is inherited
    public boolean isAllFalse() {
        return isAllFalse(0, exprs.length-1);
    }
    
    // javadoc is inherited
    public boolean isAllFalse(int s, int e) {
        for (int i=s; i<=e; i++)
            if (!exprs[i].isFalse()) return false;
            
        return true;
    }
    
    // javadoc is inherited
    public boolean isAnyTrue() {
        return isAnyTrue(0, exprs.length-1);
    }
    
    // javadoc is inherited
    public boolean isAnyTrue(int s, int e) {
        for (int i=s; i<=e; i++)
            if (exprs[i].isTrue()) return true;
            
        return false;
    }
    
    // javadoc is inherited
    public boolean isAllTrue() {
        return isAllTrue(0, exprs.length-1);
    }
    
    // javadoc is inherited
    public boolean isAllTrue(int s, int e) {
        for (int i=s; i<=e; i++)
            if (!exprs[i].isTrue()) return false;
            
        return true;
    }
    
    // javadoc is inherited
    public boolean isTrue() {
        return isAllTrue();
    }
    
    // javadoc is inherited
    public boolean isFalse() {
        return isAllFalse();
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
   
}
