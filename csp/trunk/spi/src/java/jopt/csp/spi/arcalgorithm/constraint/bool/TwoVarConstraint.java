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

/**
 * Base class for constraints with one variable
 */
public abstract class TwoVarConstraint extends BooleanConstraint {
    protected BoolExpr aexpr;
    protected GenericBoolExpr gaexpr;
    
    protected BoolExpr currentA;
    protected GenericBoolExpr currentGa;
    
    public TwoVarConstraint(BoolExpr aexpr, BoolExpr zexpr, boolean constVal) {
        super(zexpr, constVal);
        this.aexpr = aexpr;
        
        if (aexpr instanceof GenericBoolExpr) 
            gaexpr = (GenericBoolExpr) aexpr;
    }
    
    public TwoVarConstraint(BoolExpr aexpr, BoolExpr zexpr, GenericBooleanConstant genConstVal) {
        super(zexpr, genConstVal);
        this.aexpr = aexpr;
        
        if (aexpr instanceof GenericBoolExpr) 
            gaexpr = (GenericBoolExpr) aexpr;
    }	
    
    /**
     * Creates an array of arcs representing constraint
     */
    protected final Arc[] createArcs() {
        if (gaexpr!=null || gzexpr!=null)
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
    
    //We only want to add Source Arcs of Number or Set constraints
    //Java doc inherited
    public Arc[] getBooleanSourceArcs() {
        ArrayList arcs = new ArrayList();
        if (aexpr!=null)
            arcs.addAll(Arrays.asList(aexpr.getBooleanSourceArcs()));
        if (zexpr!=null)
            arcs.addAll(Arrays.asList(zexpr.getBooleanSourceArcs()));
        
        return (Arc[])arcs.toArray(new Arc[0]);
    }
    
    //  javadoc is inherited
    public void postToGraph() {
        if (graph!=null) {
            if (aexpr != null) aexpr.updateGraph(graph);
            if (zexpr != null) zexpr.updateGraph(graph);
            
            if (arcs == null) {
                ArrayList arcsList = new ArrayList();
                arcsList.addAll(Arrays.asList(createArcs()));
                arcsList.addAll(Arrays.asList(getBooleanSourceArcs()));
                arcs = (Arc[])arcsList.toArray(new Arc[0]);
            }
            for (int i=0; i<arcs.length; i++)
                graph.addArc(arcs[i]);
        }
    }
    
    // 
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
            BoolExpr a = fragA ? gaexpr.createFragment(indices) : aexpr;
            BoolExpr z = fragZ ? gzexpr.createFragment(indices) : zexpr;
            GenericBooleanConstant bc = fragConst ? (GenericBooleanConstant)genConstVal.createFragment(indices) : genConstVal;
            Boolean boolConst = null;
            if((bc!=null)&&(bc.getConstantCount()==1)) {
                boolConst = new Boolean(bc.getBooleanConstants()[0]);
            }
            
            if(boolConst!=null) {
                //              create the constraint for the fragment expressions
                return createConstraint(a, z, boolConst, null);    
            }
            else {
                //create the constraint for the fragment expressions
                return createConstraint(a, z, null, bc);
            }
            
        }
    }
    
    //  javadoc is inherited
    public Node[] getBooleanSourceNodes() {
        ArrayList sourceNodes = new ArrayList();
        BooleanNode anode = (aexpr!=null) ? (BooleanNode)aexpr.getNode() : null;
        BooleanNode znode = (zexpr!=null) ? (BooleanNode)zexpr.getNode() : null;
        
        ArrayList anodeColl =  (aexpr instanceof GenericBoolExpr) ? new ArrayList(aexpr.getNodeCollection()) : null;
        ArrayList znodeColl =  (zexpr instanceof GenericBoolExpr) ? new ArrayList(zexpr.getNodeCollection()) : null;
        
        if (boolSourceNodes==null) {
            if (anode!=null)
                sourceNodes.add(anode);
            if (znode!=null)
                sourceNodes.add(znode);
            if (anodeColl!=null)
                sourceNodes.addAll(anodeColl);
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
            GenericIndex aidx[] = null;
            if (gaexpr != null)
                aidx = gaexpr.getGenericIndices();
            else if ((aexpr==null)&&(genConstVal != null))
                genConstVal.getIndices();
            GenericIndex zidx[] = null;
            if (gzexpr != null)
                zidx = gzexpr.getGenericIndices();
            else if ((zexpr==null)&&(genConstVal != null))
                zidx = (GenericIndex[])genConstVal.getIndices();
            gim = new GenericIndexManager(aidx, null, zidx, null, false);
        }
        return gim;
    }
    
    // inherited javadoc from BooleanConstraint
    public boolean isViolated(boolean allViolated) {
        GenericIndexManager gim = this.getIndexManager();
        
        Iterator gimIt;
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
        currentConstVal = null;
        currentGenConstVal = genConstVal;
        //retrieve next z to apply changes
        if (!idxMgr.zIndexIsRestricted()) {
            currentZ = null;
            currentGz = gzexpr;
            
            if (gzexpr==null) {
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
            currentA = null;
            currentGa = gaexpr;
            
            if (gaexpr==null) {
                currentA = aexpr;
                if (aexpr==null){
                    if (genConstVal == null) {
                        currentConstVal = new Boolean(constVal);
                    }
                    else if((genConstVal != null) && (!idxMgr.useCombinedX() ||  allViolated)) {
                        currentConstVal = new Boolean(genConstVal.getBooleanForIndex());
                    }
                }
            }
            else if (gaexpr!=null && (!idxMgr.useCombinedX() || allViolated))
                currentA = gaexpr.getBoolExpressionForIndex();
        }
        
        return violated();
    }
    
    /**
     * Returns whether or not the constraint is violated for the current nodes in question
     */
    protected abstract boolean violated();
    
    //  javadoc is inherited
    public void addVariableChangeListener(VariableChangeListener listener) {
        addVariableChangeListener(listener,false);
    }
    
    //  javadoc is inherited
    public void addVariableChangeListener(VariableChangeListener listener, boolean firstTime) {
        super.addVariableChangeListener(listener,firstTime);
        if (aexpr!=null) {
            if (aexpr instanceof BooleanExpr) {
                ((BooleanExpr) aexpr).addVariableChangeListener(listener,firstTime);
            }
            else {
                ((VariableChangeSource) aexpr).addVariableChangeListener(listener);
            }
        }
    }
    
    //  javadoc is inherited
    public void removeVariableChangeListener(VariableChangeListener listener) {
        super.removeVariableChangeListener(listener);
        if (aexpr!=null)
            ((VariableChangeSource) aexpr).removeVariableChangeListener(listener);
    }    
    
    /**
     * Returns true if any A node is false
     */
    protected boolean isAnyAFalse() {
        if (gaexpr != null) return gaexpr.isAnyFalse();
        if (aexpr != null) return aexpr.isFalse();
        if (currentConstVal != null) return !currentConstVal.booleanValue(); 
        return currentGenConstVal.isAnyFalse();
        
    }
    
    /**
     * Returns true if any A node is true
     */
    protected boolean isAnyATrue() {
        if (gaexpr != null) return gaexpr.isAnyTrue();
        if (aexpr != null) return aexpr.isTrue();
        if (currentConstVal != null) return currentConstVal.booleanValue(); 
        return currentGenConstVal.isAnyTrue();
    }
    
    /**
     * Method that must be implemented to create a constraint that is the equivalent
     * of the current constraint, but based on the passed expressions.
     * 
     * @param aexpr     A expression to use when building constraint
     * @param zexpr     Z expression to use when building constraint
     * @param gc		generic constant to use when building constraint
     * @return Constraint based on passed expressions
     */
    protected abstract AbstractConstraint createConstraint(BoolExpr aexpr, BoolExpr zexpr, Boolean boolConst, GenericBooleanConstant gc);
}
