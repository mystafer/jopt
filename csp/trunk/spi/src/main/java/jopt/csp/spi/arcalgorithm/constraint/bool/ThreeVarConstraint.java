package jopt.csp.spi.arcalgorithm.constraint.bool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import jopt.csp.spi.arcalgorithm.constraint.AbstractConstraint;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.node.BooleanNode;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.variable.BooleanExpr;
import jopt.csp.spi.arcalgorithm.variable.GenericBooleanConstant;
import jopt.csp.spi.solver.VariableChangeListener;
import jopt.csp.spi.solver.VariableChangeSource;
import jopt.csp.spi.util.GenericIndex;
import jopt.csp.spi.util.GenericIndexManager;
import jopt.csp.variable.CspGenericIndex;

/**
 * Base class for constraints with one variable
 */
public abstract class ThreeVarConstraint extends BooleanConstraint {
	protected BoolExpr xexpr;
    protected GenericBoolExpr gxexpr;
	protected BoolExpr yexpr;
    protected GenericBoolExpr gyexpr;
    protected boolean notY;

    protected BoolExpr currentX;
    protected BoolExpr currentY;
    protected GenericBoolExpr currentGx;
    protected GenericBoolExpr currentGy;
    
	public ThreeVarConstraint(BoolExpr xexpr, BoolExpr yexpr, boolean notY, BoolExpr zexpr, boolean constVal) {
		super(zexpr, constVal);
		this.xexpr = xexpr;
        this.yexpr = yexpr;
        this.notY = notY;
        
        if (xexpr instanceof GenericBoolExpr) 
            gxexpr = (GenericBoolExpr) xexpr;
        if (yexpr instanceof GenericBoolExpr) 
            gyexpr = (GenericBoolExpr) yexpr;
	}
	
	public ThreeVarConstraint(BoolExpr xexpr, BoolExpr yexpr, boolean notY, BoolExpr zexpr, GenericBooleanConstant genConstVal) {
		super(zexpr, genConstVal);
		this.xexpr = xexpr;
        this.yexpr = yexpr;
        this.notY = notY;
        
        if (xexpr instanceof GenericBoolExpr) 
            gxexpr = (GenericBoolExpr) xexpr;
        if (yexpr instanceof GenericBoolExpr) 
            gyexpr = (GenericBoolExpr) yexpr;
	}	
	
    /**
     * Creates an array of arcs representing constraint
     */
    protected final Arc[] createArcs() {
        if (gxexpr!=null || gyexpr!=null || gzexpr!=null)
            return createGenericArcs();
        else
            return createStandardArcs();
    }
    
    /**
     * Creates generic boolean arcs
     */
    protected abstract Arc[] createGenericArcs();
    
    /**
     * Creates standard boolean arcs
     */
    protected abstract Arc[] createStandardArcs();
    
    //  javadoc is inherited
    public void postToGraph() {
        if (graph!=null) {
            if (xexpr != null) xexpr.updateGraph(graph);
            if (yexpr != null) yexpr.updateGraph(graph);
            if (zexpr != null) zexpr.updateGraph(graph);
            
            if (arcs == null) {
                ArrayList<Arc> arcsList = new ArrayList<Arc>();
                arcsList.addAll(Arrays.asList(createArcs()));
                arcsList.addAll(Arrays.asList(getBooleanSourceArcs()));
                arcs = (Arc[])arcsList.toArray(new Arc[0]);
            }
            for (int i=0; i<arcs.length; i++)
                graph.addArc(arcs[i]);
        }
    }

    /**
     * Returns a constraint that is a fragment of the current constraint based upon
     * a set of indices that indication what portion of constraint should be returned
     * 
     * @param indices   Array of indices that indicate fragment to return.  Each index
     *                  must be set to a specific value to indicate fragment desired.
     */
    protected AbstractConstraint createConstraintFragment(GenericIndex indices[]) {
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
            BoolExpr x = fragX ? gxexpr.createFragment(indices) : xexpr;
            BoolExpr y = fragY ? gyexpr.createFragment(indices) : yexpr;
            BoolExpr z = fragZ ? gzexpr.createFragment(indices) : zexpr;
            GenericBooleanConstant bc = fragConst ? (GenericBooleanConstant)genConstVal.createFragment(indices) : genConstVal;
            Boolean boolConst = null;
            if((bc!=null)&&(bc.getConstantCount()==1)) {
                boolConst = new Boolean(bc.getBooleanConstants()[0]);
            }
            
            if(boolConst!=null) {
//              create the constraint for the fragment expressions
                return createConstraint(x, y, z, boolConst, null);    
            }
            else {
                //create the constraint for the fragment expressions
                return createConstraint(x, y, z, null, bc);
            }
        }
    }
    
    //We only want to add Source Arcs of Number or Set constraints
    //Java doc inherited
    public Arc[] getBooleanSourceArcs() {
        ArrayList<Arc> arcs = new ArrayList<Arc>();
        if (xexpr!=null)
        	arcs.addAll(Arrays.asList(xexpr.getBooleanSourceArcs()));
        if (yexpr!=null)
        	arcs.addAll(Arrays.asList(yexpr.getBooleanSourceArcs()));
        if (zexpr!=null)
            arcs.addAll(Arrays.asList(zexpr.getBooleanSourceArcs()));
        
        return (Arc[])arcs.toArray(new Arc[arcs.size()]);
    }
    
    //  javadoc is inherited
    public Node[] getBooleanSourceNodes() {
        ArrayList<Node> sourceNodes = new ArrayList<Node>();
        
        BooleanNode xnode = (xexpr!=null) ? (BooleanNode)xexpr.getNode() : null;
        BooleanNode ynode = (yexpr!=null) ? (BooleanNode)yexpr.getNode() : null;
        BooleanNode znode = (zexpr!=null) ? (BooleanNode)zexpr.getNode() : null;
        
        ArrayList<Node> xnodeColl =  (xexpr instanceof GenericBoolExpr) ? new ArrayList<Node>(xexpr.getNodeCollection()) : null;
        ArrayList<Node> ynodeColl =  (yexpr instanceof GenericBoolExpr) ? new ArrayList<Node>(yexpr.getNodeCollection()) : null;
        ArrayList<Node> znodeColl =  (zexpr instanceof GenericBoolExpr) ? new ArrayList<Node>(zexpr.getNodeCollection()) : null;
        
        if (boolSourceNodes==null) {
            if (xnode!=null)
                sourceNodes.add(xnode);
            if (ynode!=null)
                sourceNodes.add(ynode);
            if (znode!=null)
                sourceNodes.add(znode);
            if (xnodeColl!=null)
                sourceNodes.addAll(xnodeColl);
            if (ynodeColl!=null)
                sourceNodes.addAll(ynodeColl);
            if (znodeColl!=null)
                sourceNodes.addAll(znodeColl);
            
            boolSourceNodes = (BooleanNode[]) sourceNodes.toArray(new BooleanNode[0]);
            
        }
        return boolSourceNodes;
    }

    /**
     * Retrieve a <code>GenericIndexManager</code> for this NumConstraint
     * 
     * @return an index manager taking into account the appropriate restrictions
     */
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
    
    // inherited javadoc from BooleanConstraint
    public boolean isViolated(boolean allViolated) {
        GenericIndexManager gim = this.getIndexManager();
        
        Iterator<CspGenericIndex> gimIt;
        if(allViolated)
            gimIt = gim.allIterator();
        else
            gimIt = gim.iterator();
        
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
        //retrieve next z to apply changes
        if (!idxMgr.zIndexIsRestricted()) {
            currentZ = null;
            currentGz = gzexpr;
            
            if (gzexpr==null){ 
                currentZ = zexpr;
                if (zexpr==null){
                    if (genConstVal == null) {
                        currentConstVal = new Boolean(constVal);
                    }
                    else if(genConstVal != null&& (!idxMgr.useCombinedZ() || allViolated)) {
                        currentConstVal = new Boolean(genConstVal.getBooleanForIndex());
                    }
                }
            }
            else if (gzexpr!=null && (!idxMgr.useCombinedZ() || allViolated)) 
                currentZ = gzexpr.getBoolExpressionForIndex();
        }
        
        // retrieve sources to apply changes
        if (!idxMgr.xIndexIsRestricted()) {
            currentX = null;
            currentGx = gxexpr;
            
            if (gxexpr==null){
                currentX = xexpr;
                if (xexpr==null){
                    if (genConstVal == null) {
                        currentConstVal = new Boolean(constVal);
                    }
                    else if(genConstVal != null&& (!idxMgr.useCombinedX() || allViolated)) {
                        currentConstVal = new Boolean(genConstVal.getBooleanForIndex());
                    }
                }
            }
            else if (gxexpr!=null && (!idxMgr.useCombinedX() || allViolated))
                currentX = gxexpr.getBoolExpressionForIndex();
        }
        
        if (!idxMgr.yIndexIsRestricted()) {
            currentY = null;
            currentGy = gyexpr;
            
            if (gyexpr==null){
                currentY = yexpr;
                if (yexpr==null){
                    if (genConstVal == null) {
                        currentConstVal = new Boolean(constVal);
                    }
                    else if(genConstVal != null&& (!idxMgr.useCombinedY() || allViolated)) {
                        currentConstVal = new Boolean(genConstVal.getBooleanForIndex());
                    }
                }
            }
            else if (gyexpr!=null && (!idxMgr.useCombinedY() || allViolated))
                currentY = gyexpr.getBoolExpressionForIndex();
        }
        
        return violated();
    }

    /**
     * Returns whether or not the constraint is violated for the current nodes in question
     */
    protected abstract boolean violated();
    
    //  javadoc is inherited
    public void addVariableChangeListener(VariableChangeListener listener, boolean firstTime) {
        super.addVariableChangeListener(listener, firstTime);
        if (xexpr!=null) {
            if (xexpr instanceof BooleanExpr) {
                ((BooleanExpr) xexpr).addVariableChangeListener(listener,firstTime);
            }
            else {
                ((VariableChangeSource) xexpr).addVariableChangeListener(listener);
            }
        }
        if (yexpr!=null){
            if (yexpr instanceof BooleanExpr) {
                ((BooleanExpr) yexpr).addVariableChangeListener(listener,firstTime);
            }
            else {
                ((VariableChangeSource) yexpr).addVariableChangeListener(listener);
            }
        }
    }
    
    //  javadoc is inherited
    public void addVariableChangeListener(VariableChangeListener listener) {
        addVariableChangeListener(listener, true);
    }
    
    //  javadoc is inherited
    public void removeVariableChangeListener(VariableChangeListener listener) {
        super.removeVariableChangeListener(listener);
        if (xexpr!=null)
            ((VariableChangeSource) xexpr).removeVariableChangeListener(listener);
        if (yexpr!=null)
            ((VariableChangeSource) yexpr).removeVariableChangeListener(listener);
    }
    
    /**
     * Returns true if any X node is false
     */
    protected boolean isAnyXFalse() {
        if (gxexpr != null) return gxexpr.isAnyFalse();
        if (xexpr != null) return xexpr.isFalse();
        if (currentConstVal != null) return !currentConstVal.booleanValue();
        return currentGenConstVal.isAnyFalse();
    }

    /**
     * Returns true if any X node is true
     */
    protected boolean isAnyXTrue() {
        if (gxexpr != null) return gxexpr.isAnyTrue();
        if (xexpr != null) return xexpr.isTrue();
        if (currentConstVal != null) return currentConstVal.booleanValue();
        return currentGenConstVal.isAnyTrue();
    }

    /**
     * Returns true if any Y node is false
     */
    protected boolean isAnyYFalse() {
        if (gyexpr != null) return (notY) ? gyexpr.isAnyTrue() : gyexpr.isAnyFalse();
        if (yexpr != null) return (notY) ? yexpr.isTrue() : yexpr.isFalse();
        if (currentConstVal != null) return !(currentConstVal.booleanValue() ^ notY); 
        return currentGenConstVal.isAnyFalse();
        
    }

    /**
     * Returns true if any X node is true
     */
    public boolean isAnyYTrue() {
        if (gyexpr != null) return (notY) ? gyexpr.isAnyFalse() : gyexpr.isAnyTrue();
        if (yexpr != null) return (notY) ? yexpr.isFalse() : yexpr.isTrue();
        if (currentConstVal != null) return currentConstVal.booleanValue() ^ notY; 
        return currentGenConstVal.isAnyTrue();
        
    }
    
    /**
     * Method that must be implemented to create a constraint that is the equivalent
     * of the current constraint, but based on the passed expressions.
     * 
     * @param xexpr     X expression to use when building constraint
     * @param yexpr     Y expression to use when building constraint
     * @param zexpr     Z expression to use when building constraint
     * @param gc		generic constant to use when building constraint
     * @return Constraint based on passed expressions
     */
    protected abstract AbstractConstraint createConstraint(BoolExpr xexpr, BoolExpr yexpr, BoolExpr zexpr, Boolean boolConst, GenericBooleanConstant gc);
}
