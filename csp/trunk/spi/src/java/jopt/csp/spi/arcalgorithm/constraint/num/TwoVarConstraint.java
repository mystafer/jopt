package jopt.csp.spi.arcalgorithm.constraint.num;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import jopt.csp.spi.arcalgorithm.constraint.AbstractConstraint;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.variable.GenericNumConstant;
import jopt.csp.spi.solver.VariableChangeListener;
import jopt.csp.spi.solver.VariableChangeSource;
import jopt.csp.spi.util.GenericIndex;
import jopt.csp.spi.util.GenericIndexManager;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.util.DoubleUtil;

/**
 * Base class for numeric constraints based on two variables: A and Z
 */

public abstract class TwoVarConstraint extends NumConstraint {
    private Node[] sourceNodes;
    
    protected NumExpr aexpr;
    protected GenericNumExpr gaexpr;
    protected Arc[] arcs;
    
    protected boolean booleanArcsObtained=false;
    
    protected NumExpr currentA;
    protected GenericNumExpr currentGa;
    
    protected GenericIndex restrictedIndices[];
    
    /**
     * Internal Constructor
     */
    private TwoVarConstraint(NumExpr aexpr, NumExpr zexpr, Number constVal, GenericNumConstant genConstVal,
            GenericIndex restrictedIndices[], int constraintType) 
    {
        super(zexpr, constVal, genConstVal, constraintType);
        this.aexpr = aexpr;
        this.restrictedIndices = restrictedIndices;
        
        if (aexpr instanceof GenericNumExpr) 
            gaexpr = (GenericNumExpr) aexpr;
        
        // Determine node type
        if (aexpr!=null) this.nodeType = Math.max(nodeType, aexpr.getNumberType());
    }
    
    protected TwoVarConstraint(NumExpr a, NumExpr z, GenericIndex restrictedIndices[], int constraintType) {
        this(a, z, null, null, restrictedIndices, constraintType);
    }
    protected TwoVarConstraint(NumExpr a, NumExpr z, int constraintType) {
        this(a, z, null, null,null, constraintType);
    }
    protected TwoVarConstraint(Number a, NumExpr z, int constraintType) {
        this(null, z, a, null,null, constraintType);
    }
    protected TwoVarConstraint(NumExpr a, Number z, GenericIndex restrictedIndices[], int constraintType) {
        this(a, null, z, null, restrictedIndices, constraintType);
    }
    protected TwoVarConstraint(NumExpr a, Number z, int constraintType) {
        this(a, null, z, null, null, constraintType);
    }
    protected TwoVarConstraint(GenericNumConstant a, NumExpr z, int constraintType) {
        this(null, z, null, a,null, constraintType);
    }
    protected TwoVarConstraint(NumExpr a, GenericNumConstant z, GenericIndex restrictedIndices[], int constraintType) {
        this(a, null, null, z, restrictedIndices, constraintType);
    }
    protected TwoVarConstraint(NumExpr a, GenericNumConstant z, int constraintType) {
        this(a, null, null, z, null, constraintType);
    }
    
    /**
     * Creates an array of arcs representing constraint
     */
    protected abstract Arc[] createArcs();
    
    //  javadoc is inherited
    public void postToGraph() {
        if (graph!=null) {
            if (zexpr != null) zexpr.updateGraph(graph);
            if (aexpr != null) aexpr.updateGraph(graph);
            
            if (arcs == null) arcs = createArcs();
            for (int i=0; i<arcs.length; i++)
                graph.addArc(arcs[i]);
        }
    }
    
    //We only want to add Source Arcs of Number or Set constraints
    //Java doc inherited
    public Arc[] getBooleanSourceArcs(boolean useConstraint) {
        if(booleanArcsObtained) {
            return new Arc[0];
        }
        else{
	        booleanArcsObtained = true;
	        ArrayList arcs = new ArrayList();
	        if (aexpr!=null)
	            arcs.addAll(Arrays.asList(aexpr.getBooleanSourceArcs()));
	        if (zexpr!=null)
	        	arcs.addAll(Arrays.asList(zexpr.getBooleanSourceArcs()));
	        if (useConstraint)
	            arcs.addAll(Arrays.asList(createArcs()));
	        return (Arc[])arcs.toArray(new Arc[0]);
        }
    }
    
    
    public Arc[] getBooleanSourceArcs() {
        return getBooleanSourceArcs(true);
    }
    
    //  javadoc is inherited
    public Node[] getBooleanSourceNodes() {
        if (sourceNodes==null) {
            Collection nodes = null;
            
            // retrieve nodes from a
            if (aexpr!=null)
                nodes = aexpr.getNodeCollection();
            
            // retrieve nodes from z
            if (zexpr!=null) {
                if (nodes==null)
                    nodes = zexpr.getNodeCollection();
                else
                    nodes.addAll(zexpr.getNodeCollection());
            }
            
            sourceNodes = (Node[]) nodes.toArray(new Node[nodes.size()]);
        }
        
        return sourceNodes;
    }
    
    //  javadoc is inherited
    public GenericIndexManager getIndexManager() {
        if(gim == null) {
            GenericIndex aidx[] = null;
            if (gaexpr != null)
                aidx = gaexpr.getGenericIndices();
            else if ((aexpr==null)&&(genConstVal != null))
                aidx = (GenericIndex[])genConstVal.getIndices();
            GenericIndex zidx[] = null;
            if (gzexpr != null)
                zidx = gzexpr.getGenericIndices();
            else if ((zexpr==null)&&(genConstVal != null))
                zidx = (GenericIndex[])genConstVal.getIndices();
            gim = new GenericIndexManager(aidx, null, zidx, restrictedIndices, false);
        }
        return gim;
    }
    
    // inherited javadoc from NumConstraint abstract class
    public boolean isViolated(boolean allViolated) {
        GenericIndexManager gim = this.getIndexManager();
        Iterator gimIt;
        // all combinations must be checked in the case of NEQ constraints
        if( (constraintType == TwoVarConstraint.NEQ || allViolated)) {
            gimIt = gim.allIterator();
        }
        else {
            gimIt = gim.iterator();
        }
        // null iterator means no need for looping
        if(gimIt == null) {
            return checkForViolations(gim, allViolated);
        }
        
        // perform the check differently based on the value of the allViolated param
        if(allViolated) {
            while(gimIt.hasNext()) {
                gimIt.next();
                if(!checkForViolations(gim, allViolated)) {
                    return false;
                }
            }
            return true;
        }
        else {
            while(gimIt.hasNext()) {
                gimIt.next();
                if(checkForViolations(gim, allViolated)) {
                    return true;
                }
            }
            return false;
        }
    }
    
    private boolean checkForViolations(GenericIndexManager idxMgr, boolean allViolated) {
        currentConstVal = null;
        currentGenConstVal = genConstVal;
        
        //retrieve next z to apply changes
        if (!idxMgr.zIndexIsRestricted()) {
            currentZ = null;
            currentGz = gzexpr;
            
            if (gzexpr==null) {
                currentZ = zexpr;
                if (zexpr==null){
                    if (constVal != null) {
                        currentConstVal = constVal;
                    }
                    else if(genConstVal != null&& (!idxMgr.useCombinedZ() || constraintType==ThreeVarConstraint.NEQ|| allViolated)) {
                        currentConstVal = genConstVal.getNumberForIndex();
                    }
                }
            }
            else if (gzexpr!=null && (!idxMgr.useCombinedZ() || constraintType==ThreeVarConstraint.NEQ|| allViolated)) 
                currentZ = gzexpr.getNumExpressionForIndex();
        }
        
        // retrieve sources to apply changes
        if (!idxMgr.xIndexIsRestricted()) {
            currentA = null;
            currentGa = gaexpr;
            
            if (gaexpr==null){
                currentA = aexpr;
                if (aexpr==null){
                    if (constVal != null) {
                        currentConstVal = constVal;
                    }
                    else if((genConstVal != null) && (!idxMgr.useCombinedX() || constraintType==ThreeVarConstraint.NEQ|| allViolated)) {
                        currentConstVal = genConstVal.getNumberForIndex();
                    }
                }
            }
            else if (gaexpr!=null && (!idxMgr.useCombinedX() || constraintType==ThreeVarConstraint.NEQ|| allViolated))
                currentA = gaexpr.getNumExpressionForIndex();
        }
        
        return violated();
    }
    
    /**
     * Returns whether or not the constraint is violated for the current nodes in question
     */
    protected abstract boolean violated();
    
    /**
     * Returns precision that should be used for arcs based on real intervals
     */
    protected double getPrecision() {
        if (!NumberMath.isRealType(nodeType)) return 0;
        
        double precision = Double.POSITIVE_INFINITY;
        
        if (aexpr!=null) 
            precision = Math.min(precision, aexpr.getPrecision());
        
        if (zexpr!=null) 
            precision = Math.min(precision, zexpr.getPrecision());
        
        return Math.max(precision, DoubleUtil.DEFAULT_PRECISION);
    }
    
    /**
     * Returns min node A
     */
    protected Number minA() {
        if (aexpr!=null) return aexpr.getNumMin();
        if (currentGenConstVal != null) return currentGenConstVal.getNumMin();
        return currentConstVal;
    }
    
    /**
     * Returns max node A
     */
    protected Number maxA() {
        if (aexpr!=null) return aexpr.getNumMax();
        if (currentConstVal != null) return currentConstVal;
        return currentGenConstVal.getNumMax();
    }
    
    /**
     * Adds a listener interested in variable change events
     */
    public void addVariableChangeListener(VariableChangeListener listener) {
        super.addVariableChangeListener(listener);
        if (aexpr!=null)
            ((VariableChangeSource) aexpr).addVariableChangeListener(listener);
    }
    
    //  javadoc is inherited
    public void removeVariableChangeListener(VariableChangeListener listener) {
        super.removeVariableChangeListener(listener);
        if (aexpr!=null)
            ((VariableChangeSource) aexpr).removeVariableChangeListener(listener);
    }
    
    // javadoc inherited from AbstractConstraint
    protected final AbstractConstraint createConstraintFragment(GenericIndex indices[]) {
        if (gaexpr==null && gzexpr==null) {
            return this;
        }
        else {
            // determine vars to frag
            boolean fragA = false;
            boolean fragZ = false;
            boolean fragConst = false;
            for (int i=0; i<indices.length; i++) {
                GenericIndex idx = indices[i];
                if (gaexpr!=null && gaexpr.containsIndex(idx)) fragA = true;
                if (gzexpr!=null && gzexpr.containsIndex(idx)) fragZ = true;
                if (genConstVal!=null && genConstVal.containsIndex(idx)) fragConst = true;
            }
            
            // create expressions to base fragment upon
            NumExpr a = fragA ? gaexpr.createFragment(indices) : aexpr;
            NumExpr z = fragZ ? gzexpr.createFragment(indices) : zexpr;
            GenericNumConstant gc = fragConst ? (GenericNumConstant)genConstVal.createFragment(indices) : genConstVal;
            Number numConst = null;
            if((gc!=null)&&(gc.getConstantCount()==1)) {
                numConst = gc.getNumConstants()[0];
            }
            else {
                numConst = constVal;
            }
            
            if(numConst!=null) {
                // create the constraint for the fragment expressions
                return createConstraint(a, z, numConst, null, constraintType);    
            }
            else {
                //create the constraint for the fragment expressions
                return createConstraint(a, z, null, gc, constraintType);
            }
            
        }
    }
    
    /**
     * Returns a constraint that is the exact opposite of this constraint
     */
    protected final AbstractConstraint createOpposite() { 
        return createConstraint(aexpr, zexpr, constVal, genConstVal, oppositeConstraintType());
    }
    
    /**
     * Method that must be implemented to create a constraint that is the equivalent
     * of the current constraint, but based on the passed expressions.
     * 
     * @param aexpr     		A expression to use when building constraint
     * @param zexpr     		Z expression to use when building constraint
     * @param gc				generic constant to use when building constraint
     * @param constraintType	the type (=, <, etc) of constraint
     * @return Constraint based on specified expressions
     */
    protected abstract AbstractConstraint createConstraint(NumExpr aexpr, NumExpr zexpr, Number numConst, GenericNumConstant gc, int constraintType);
}