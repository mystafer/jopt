package jopt.csp.spi.arcalgorithm.variable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import jopt.csp.spi.arcalgorithm.constraint.num.GenericNumExpr;
import jopt.csp.spi.arcalgorithm.constraint.num.NumConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.NumExpr;
import jopt.csp.spi.arcalgorithm.graph.NodeArcGraph;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.solver.VariableChangeListener;
import jopt.csp.spi.solver.VariableChangeSource;
import jopt.csp.spi.util.GenericIndex;
import jopt.csp.spi.util.IndexIterator;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.util.DoubleUtil;
import jopt.csp.variable.CspGenericIndex;
import jopt.csp.variable.CspNumExpr;

/**
 * A generic variable such as Xi which represents X1, X2, etc.
 */
public abstract class GenericNumExprBase extends NumExprBase implements GenericNumExpr {
    protected GenericIndex indices[];
    protected int indexOffsets[];
    protected NumExpr exprs[];
    private NumNode nodes[];
    protected GenericNumExprBase gaexpr;
    protected GenericNumExprBase gbexpr;
    protected int numberType;
    
    private static int nextFragName;
    
    /**
     * Constructor for building expression from array of numeric expressions
     */
    protected GenericNumExprBase(String name, CspGenericIndex indices[], NumExpr exprs[], int numberType) 
    {
        super(name);
        this.indices = VarFactory.toGenericIndex(indices);
        this.exprs = exprs;
        this.numberType = numberType;
        
        // initialize object
        init();
    }
    
    /**
     * Internal Constructor for building expressions
     */
    private GenericNumExprBase(String name, CspNumExpr aexpr, int operation, CspNumExpr bexpr, 
            Number aconst, Number bconst, GenericNumConstant aGenConst, GenericNumConstant bGenConst, int numberType) 
    {
        super(name, aexpr, operation, bexpr, aconst, bconst, aGenConst, bGenConst);
        
        // ensure at least one expression is an index
        if (aexpr instanceof GenericNumExprBase) gaexpr = (GenericNumExprBase) aexpr;
        if (bexpr instanceof GenericNumExprBase) gbexpr = (GenericNumExprBase) bexpr;
        if (gaexpr==null && gbexpr==null) {
            throw new IllegalStateException("At least one generic expression is required to perform a numeric operation with generic expressions");
        }
        
        // build list of indices
        HashSet<CspGenericIndex> indexSet = new HashSet<CspGenericIndex>();
        if (gaexpr!=null) indexSet.addAll(Arrays.asList(gaexpr.getIndices()));
        if (gbexpr!=null) indexSet.addAll(Arrays.asList(gbexpr.getIndices()));
        if (aGenConst!=null) indexSet.addAll(Arrays.asList(aGenConst.getIndices()));
        if (bGenConst!=null) indexSet.addAll(Arrays.asList(bGenConst.getIndices()));
        
        this.indices = (GenericIndex[]) indexSet.toArray(new GenericIndex[indexSet.size()]);
        
        this.numberType = numberType;
        
        // initialize object
        init();
    }
    
    /**
     * Constructor for building expressions
     */
    protected GenericNumExprBase(String name, CspNumExpr aexpr, int operation, CspNumExpr bexpr, int numberType) {
        this(name, aexpr, operation, bexpr, null, null, null, null, numberType);
    }

    /**
     * Constructor for building expressions
     */
    protected GenericNumExprBase(String name, Number aconst, int operation, CspNumExpr bexpr, int numberType) {
        this(name, null, operation, bexpr, aconst, null, null, null, numberType);
    }

    /**
     * Constructor for building expressions
     */
    protected GenericNumExprBase(String name, GenericNumConstant aGenConst, int operation, CspNumExpr bexpr, int numberType) {
        this(name, null, operation, bexpr, null, null, aGenConst, null, numberType);
    }
    
    /**
     * Constructor for building expressions
     */
    protected GenericNumExprBase(String name, CspNumExpr aexpr, int operation, Number bconst, int numberType) {
        this(name, aexpr, operation, null, null, bconst, null, null, numberType);
    }
    
    /**
     * Constructor for building expressions
     */
    protected GenericNumExprBase(String name, CspNumExpr aexpr, int operation, GenericNumConstant bGenConst, int numberType) {
        this(name, aexpr, operation, null, null, null, null, bGenConst, numberType);
    }

    /**
     * Constructor for building expressions
     */
    protected GenericNumExprBase(String name, CspNumExpr aexpr, int operation, int numberType) {
        this(name, aexpr, operation, null, null, null, null, null, numberType);
    }
    
    /**
     * Function that is used to produce an array of calculated expressions for use
     * when math operations have been used to create this expression
     */
    protected abstract void generateCalculatedExpressions();

    /**
     * Initializes internal variables during construction of object
     */
    private void init() {
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
            this.exprs = new NumExpr[totalExprs];
        	generateCalculatedExpressions();
        }
        
        // ensure correct number of expressions are given for indices
        else if (totalExprs != exprs.length) {
            throw new IllegalStateException("Expected " + totalExprs + " expressions, but received " + exprs.length);
        }
    }
    
    /**
     * Returns array of arcs that will affect the boolean true / false
     * value of this constraint upon a change
     */
    public Arc[] getBooleanSourceArcs(){
        ArrayList<Arc> arcs = new ArrayList<Arc>();
        	if ((constraint !=null)&&(constraint instanceof NumConstraint))
        	    arcs.addAll(Arrays.asList(super.getBooleanSourceArcs()));
        	for (int i=0; i< exprs.length; i++) {
        	    if (exprs[i]!= null)
        	        arcs.addAll(Arrays.asList(exprs[i].getBooleanSourceArcs()));
        	}
        return (Arc[])arcs.toArray(new Arc[0]);
    }
    
    /**
     * Returns the nodes for all expressions in the internal array
     */
    public NumNode[] getExpressionNodes() {
        // retrieve nodes for expression
        if (nodes==null) {
            this.nodes = new NumNode[exprs.length];
            for (int i=0; i<exprs.length; i++) {
                NumExpr expr = exprs[i];
                nodes[i] = (NumNode)expr.getNode();
            }
        }
        
        return nodes;
    }
    
    /**
     * Sets precision associated with this domain
     */
    public void setPrecision(double p) {
        for (int i=0; i<exprs.length; i++)
            exprs[i].setPrecision(p);
    }
    
    /**
     * Returns precision associated with this domain
     */
    public double getPrecision() {
        // determine minimum precision
        double precision = Double.POSITIVE_INFINITY;
        for (int i=0; i<exprs.length; i++) {
            precision = Math.min(exprs[i].getPrecision(), precision);
            if (precision <= DoubleUtil.DEFAULT_PRECISION) break;
        }
        
        precision = Math.max(precision, DoubleUtil.DEFAULT_PRECISION);
        
        return precision;
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
    public NumExpr getNumExpressionForIndex() {
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
     * returns a numeric expression from the internal array
     * 
     * @param offset  Offset of expression in internal expression array
     */
    public NumExpr getNumExpression(int offset) {
        if (offset < 0 || offset >= exprs.length) return null;
    	return exprs[offset];
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
            NumExpr expr = exprs[i];
            expr.updateGraph(graph);
        }
    }

    /**
     * Returns that largest maximal value of all variables in array
     */
    public Number getNumLargestMax() {
        return getNumLargestMax(0, exprs.length-1);
    }
    
    /**
     * Returns minimal value of node
     */
    public final Number getNumMin() {
        return getNumSmallestMin();
    }

    /**
     * Returns maximum value of node
     */
    public final Number getNumMax() {
        return getNumLargestMax();
    }

    /**
     * Returns that largest maximal value of all variables in array within
     * start and end indices
     */
    public Number getNumLargestMax(int start, int end) {
        Number v = NumberMath.minValue(numberType);
        double precision = getPrecision();
        
        for (int i=start; i<=end; i++) {
            Number max = exprs[i].getNumMax();
            if (NumberMath.compare(max,v,precision,numberType) > 0) v = max;
        }
        
        return v;
    }

    /**
     * Returns that smallest maximal value of all variables in array
     */
    public Number getNumSmallestMax() {
        return getNumSmallestMax(0, exprs.length-1);
    }
    
    /**
     * Returns that smallest maximal value of all variables in array within
     * start and end indices
     */
    public Number getNumSmallestMax(int start, int end) {
        Number v = NumberMath.maxValue(numberType);
        double precision = getPrecision();
        
        for (int i=start; i<=end; i++) {
            Number max = exprs[i].getNumMax();
            if (NumberMath.compare(max,v,precision,numberType) < 0) v = max;
        }
        
        return v;
    }

    /**
     * Returns that largest minimal value of all variables in array
     */
    public Number getNumLargestMin() {
        return getNumLargestMin(0, exprs.length-1);
    }
    
    /**
     * Returns that largest minimal value of all variables in array within
     * start and end indices
     */
    public Number getNumLargestMin(int start, int end) {
        Number v = NumberMath.minValue(numberType);
        double precision = getPrecision();
        
        for (int i=start; i<=end; i++) {
            Number min = exprs[i].getNumMin();
            if (NumberMath.compare(min,v,precision,numberType) > 0) v = min;
        }
        
        return v;
    }

    /**
     * Returns that smallest minimal value of all variables in array
     */
    public Number getNumSmallestMin() {
        return getNumSmallestMin(0, exprs.length-1);
    }
    
    /**
     * Returns that smallest minimal value of all variables in array within
     * start and end indices
     */
    public Number getNumSmallestMin(int start, int end) {
        Number v = NumberMath.maxValue(numberType);
        double precision = getPrecision();
        
        for (int i=start; i<=end; i++) {
            Number min = exprs[i].getNumMin();
            if (NumberMath.compare(min,v,precision,numberType) < 0) v = min;
        }
        
        return v;
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


    // javadoc inherited
    public Number getNextHigher(Number val) {
        return null;
    }

    // javadoc inherited
    public Number getNextLower(Number val) {
        return null;
    }
    
    // javadoc inherited
    public boolean isInDomain(Number n) {
        return false;
    }
    
    
    // javadoc inherited from jopt.csp.spi.arcalgorithm.constraint.num.GenericNumExpr
    // used by constraints when creating fragments of constraints
    public final NumExpr createFragment(GenericIndex fragIndices[]) {
        // build list of indices that are not contained in expression
        List<CspGenericIndex> remainingIdxList = new LinkedList<CspGenericIndex>(Arrays.asList(indices));
        List<GenericIndex> fragIdxList = Arrays.asList(fragIndices);
        remainingIdxList.removeAll(fragIdxList);
        
        // if all indices are used for variable, return the specific
        // expression at the given indice combination
        if (remainingIdxList.size()==0) {
            return getNumExpressionForIndex();
        }
        
        // no indices are used, return this expression as a whole
        else if (remainingIdxList.size() == indices.length) {
        	return this;
        }
        
        else {
            // Create a unique name for this fragment
            StringBuffer fragName = new StringBuffer("_(");
            fragName.append(this.name);
            fragName.append(VarMath.indicesToString(fragIndices, true));
            fragName.append("frag").append(nextFragName++).append("[");
            
            // Iterate over remaing indices and build list of variables
            // that make up fragment
            LinkedList<NumExpr> fragExprs = new LinkedList<NumExpr>();
            IndexIterator iter = new IndexIterator(remainingIdxList);
            while (iter.hasNext()) {
                iter.next();
                NumExpr exprAtIndex = getNumExpressionForIndex();
                fragExprs.add(exprAtIndex);
                fragName.append("(").append(exprAtIndex).append(")");
            }
            fragName.append("])");
            
            // create generic expression that represents fragment
            GenericIndex remainingIndices[] = (GenericIndex[]) remainingIdxList.toArray(new GenericIndex[remainingIdxList.size()]);
            NumExpr exprs[] = (NumExpr[]) fragExprs.toArray(new NumExpr[fragExprs.size()]);
            return createTypeSpecificExpr(fragName.toString(), remainingIndices, exprs);
        }
    }

    /**
     * Called by <code>GenericNumExprBase</code> to create a type specific generic expression
     * based on an array of indices and expressions
     * 
     * @param name		Name for the type specific expression
     * @param indices   Array of indices to base generic upon
     * @param exprs     Array of expressions to base generic upon
     * @return Type specific generic expression
     */
    protected abstract GenericNumExpr createTypeSpecificExpr(String name, GenericIndex indices[], NumExpr exprs[]);
}
