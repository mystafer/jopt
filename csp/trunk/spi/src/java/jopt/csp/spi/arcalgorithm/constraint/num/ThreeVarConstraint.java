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

public abstract class ThreeVarConstraint extends NumConstraint {
    private Node[] sourceNodes;
    
    protected NumExpr xexpr;
    protected GenericNumExpr gxexpr;
    protected NumExpr yexpr;
    protected GenericNumExpr gyexpr;
    protected Arc[] arcs;
    
    //This is to prevent any cyclic dependencies, leading to infinite loops.
    //This was a constraint will only get its booleanArcs
    protected boolean booleanArcsObtained=false;
    
    protected NumExpr currentX;
    protected NumExpr currentY;
    protected GenericNumExpr currentGx;
    protected GenericNumExpr currentGy;
    
    /**
     * Internal Constructor
     */
    protected ThreeVarConstraint(NumExpr xexpr, NumExpr yexpr, NumExpr zexpr, Number constVal, GenericNumConstant genConstVal, int constraintType) 
    {
        super(zexpr, constVal, genConstVal, constraintType);
        this.xexpr = xexpr;
        this.yexpr = yexpr;
        
        if (xexpr instanceof GenericNumExpr) 
            gxexpr = (GenericNumExpr) xexpr;
        if (yexpr instanceof GenericNumExpr) 
            gyexpr = (GenericNumExpr) yexpr;
        
        // Determine node type
        if (xexpr!=null) this.nodeType = Math.max(nodeType, xexpr.getNumberType());
        if (yexpr!=null) this.nodeType = Math.max(nodeType, yexpr.getNumberType());
    }
    
    /**
     * Creates an array of arcs representing constraint
     */
    protected abstract Arc[] createArcs();
    
    //  javadoc is inherited
    public void postToGraph() {
        if (graph!=null) {
            // create arcs for variables
            if (arcs == null) arcs = createArcs();
            
            if (xexpr != null) xexpr.updateGraph(graph);
            if (yexpr != null) yexpr.updateGraph(graph);
            if (zexpr != null) zexpr.updateGraph(graph);
            
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
	        if(xexpr!=null)
	            arcs.addAll(Arrays.asList(xexpr.getBooleanSourceArcs()));
	        if (yexpr!=null)
	            arcs.addAll(Arrays.asList(yexpr.getBooleanSourceArcs()));
	        if ((zexpr!=null))
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
            
            // retrieve nodes from x
            if (xexpr!=null)
                nodes = xexpr.getNodeCollection();
            
            // retrieve nodes from y
            if (yexpr!=null) {
                if (nodes==null)
                    nodes = yexpr.getNodeCollection();
                else
                    nodes.addAll(yexpr.getNodeCollection());
            }
            
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
    
    /**
     * Returns precision that should be used for arcs based on real intervals
     */
    protected double getPrecision() {
        if (!NumberMath.isRealType(nodeType)) return 0;
        
        double precision = Double.POSITIVE_INFINITY;
        
        if (xexpr!=null) 
            precision = Math.min(precision, xexpr.getPrecision());
        
        if (yexpr!=null) 
            precision = Math.min(precision, yexpr.getPrecision());
        
        if (zexpr!=null) 
            precision = Math.min(precision, zexpr.getPrecision());
        
        return Math.max(precision, DoubleUtil.DEFAULT_PRECISION);
    }
    
    /**
     * Returns min node X
     */
    protected Number minX() {
        if (xexpr!=null) return xexpr.getNumMin();
        if (currentGenConstVal != null) return currentGenConstVal.getNumMin();
        return currentConstVal;
    }
    
    /**
     * Returns max node X
     */
    protected Number maxX() {
        if (xexpr!=null) return xexpr.getNumMax();
        if (currentGenConstVal != null) return currentGenConstVal.getNumMax();
        return currentConstVal;
    }
    
    /**
     * Returns min node Y
     */
    protected Number minY() {
        if (yexpr!=null) return yexpr.getNumMin();
        if (currentConstVal != null)  return currentConstVal; 
        return currentGenConstVal.getNumMin();
        
    }
    
    /**
     * Returns max node Y
     */
    protected Number maxY() {
        if (yexpr!=null) return yexpr.getNumMax();
        if (currentConstVal != null) return currentConstVal;
        return currentGenConstVal.getNumMax();
    }
    
    //  javadoc is inherited
    public GenericIndexManager getIndexManager() {
        if(gim == null) {
            GenericIndex xidx[] = null;
            if (gxexpr != null)
                xidx = gxexpr.getGenericIndices();
            else if ((xexpr==null)&&(genConstVal != null))
                xidx = (GenericIndex[])genConstVal.getIndices();
            
            GenericIndex yidx[] = null;
            if (gyexpr != null)
                yidx = gyexpr.getGenericIndices();
            else if ((yexpr==null)&&(genConstVal != null))
                yidx = (GenericIndex[])genConstVal.getIndices();
            
            GenericIndex zidx[] = null;
            if (gzexpr != null)
                zidx = gzexpr.getGenericIndices();
            else if ((zexpr==null)&&(genConstVal != null))
                zidx = (GenericIndex[])genConstVal.getIndices();
            
            gim = new GenericIndexManager(xidx, yidx, zidx, null, false);
        }
        return gim;
    }
    
    // inherited javadoc from NumConstraint abstract class
    public boolean isViolated(boolean allViolated) {
        GenericIndexManager gim = this.getIndexManager();
        Iterator gimIt;
        // all combinations must be checked in the case of NEQ constraints
        if(constraintType == ThreeVarConstraint.NEQ || allViolated) {
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
            
            if (gzexpr==null){ 
                currentZ = zexpr;
                if (zexpr==null){
                    if (constVal != null) {
                        currentConstVal = constVal;
                    }
                    else if(genConstVal != null && (!idxMgr.useCombinedZ() || constraintType==ThreeVarConstraint.NEQ|| allViolated)) {
                        currentConstVal = genConstVal.getNumberForIndex();
                    }
                }
            }
            else if (gzexpr!=null && (!idxMgr.useCombinedZ() || constraintType==ThreeVarConstraint.NEQ|| allViolated)) 
                currentZ = gzexpr.getNumExpressionForIndex();
        }
        
        // retrieve sources to apply changes
        if (!idxMgr.xIndexIsRestricted()) {
            currentX = null;
            currentGx = gxexpr;
            
            if (gxexpr==null) {
                currentX = xexpr;
                if (xexpr==null){
                    if (constVal != null) {
                        currentConstVal = constVal;
                    }
                    else if(genConstVal != null&& (!idxMgr.useCombinedX() || constraintType==ThreeVarConstraint.NEQ|| allViolated)) {
                        currentConstVal = genConstVal.getNumberForIndex();
                    }
                }
            }
            else if (gxexpr!=null && (!idxMgr.useCombinedX() || constraintType==ThreeVarConstraint.NEQ || allViolated))
                currentX = gxexpr.getNumExpressionForIndex();
        }
        
        if (!idxMgr.yIndexIsRestricted()) {
            currentY = null;
            currentGy = gyexpr;
            
            if (gyexpr==null) {
                currentY = yexpr;
                if (yexpr==null){
                    if (constVal != null) {
                        currentConstVal = constVal;
                    }
                    else if(genConstVal != null&& (!idxMgr.useCombinedY() || constraintType==ThreeVarConstraint.NEQ|| allViolated)) {
                        currentConstVal = genConstVal.getNumberForIndex();
                    }
                }
            }
            else if (gyexpr!=null && (!idxMgr.useCombinedY() || constraintType==ThreeVarConstraint.NEQ|| allViolated))
                currentY = gyexpr.getNumExpressionForIndex();
        }
        
        return violated();
    }
    
    /**
     * Returns whether or not the constraint is violated for the current nodes in question
     */
    protected abstract boolean violated();
    
    //  javadoc is inherited
    public void addVariableChangeListener(VariableChangeListener listener) {
        super.addVariableChangeListener(listener);
        if (xexpr!=null)
            ((VariableChangeSource) xexpr).addVariableChangeListener(listener);
        if (yexpr!=null)
            ((VariableChangeSource) yexpr).addVariableChangeListener(listener);
    }
    
    //  javadoc is inherited
    public void removeVariableChangeListener(VariableChangeListener listener) {
        super.removeVariableChangeListener(listener);
        if (xexpr!=null)
            ((VariableChangeSource) xexpr).removeVariableChangeListener(listener);
        if (yexpr!=null)
            ((VariableChangeSource) yexpr).removeVariableChangeListener(listener);
    }
    
    // javadoc inherited from AbstractConstraint
    protected final AbstractConstraint createConstraintFragment(GenericIndex indices[]) {
        if (gxexpr==null && gyexpr==null && gzexpr==null) {
            return this;
        }
        else {
            // determine vars to frag
            boolean fragX = false;
            boolean fragY = false;
            boolean fragZ = false;
            boolean fragConst = false;
            
            for (int i=0; i<indices.length; i++) {
                GenericIndex idx = indices[i];
                if (gxexpr!=null && gxexpr.containsIndex(idx)) fragX = true;
                if (gyexpr!=null && gyexpr.containsIndex(idx)) fragY = true;
                if (gzexpr!=null && gzexpr.containsIndex(idx)) fragZ = true;
                if (genConstVal!=null && genConstVal.containsIndex(idx)) fragConst = true;
            }
            
            // create expressions to base fragment upon
            NumExpr x = fragX ? gxexpr.createFragment(indices) : xexpr;
            NumExpr y = fragY ? gyexpr.createFragment(indices) : yexpr;
            NumExpr z = fragZ ? gzexpr.createFragment(indices) : zexpr;
            GenericNumConstant gc = fragConst ? (GenericNumConstant)genConstVal.createFragment(indices) : genConstVal;
            Number numConst = null;
            if((gc!=null)&&(gc.getConstantCount()==1)) {
                numConst = gc.getNumConstants()[0];
            }
            
            if(numConst!=null) {
                // create the constraint for the fragment expressions
                return createConstraint(x, y, z, numConst,null, constraintType);   
            }
            else {
                // create the constraint for the fragment expressions
                return createConstraint(x, y, z, null, gc, constraintType);
            }
            
        }
    }
    
    /**
     * Returns a constraint that is the exact opposite of this constraint
     */
    protected final AbstractConstraint createOpposite() {
        return createConstraint(xexpr, yexpr, zexpr, constVal, genConstVal, oppositeConstraintType());
    }
    
    /**
     * Method that must be implemented to create a constraint that is the equivalent
     * of the current constraint, but based on the passed expressions.
     * 
     * @param xexpr     		X expression to use when building constraint
     * @param yexpr     		Y expression to use when building constraint
     * @param zexpr     		Z expression to use when building constraint
     * @param gc				generic constant to use when building constraint
     * @param constraintType	the type (=, <, etc) of constraint
     * @return Constraint based on specified expressions
     */
    protected abstract AbstractConstraint createConstraint(NumExpr xexpr, NumExpr yexpr, NumExpr zexpr, Number numConst, GenericNumConstant gc, int constraintType);    
}