package jopt.csp.spi.arcalgorithm.constraint.num;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import jopt.csp.spi.arcalgorithm.constraint.AbstractConstraint;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.variable.GenericNumConstant;
import jopt.csp.spi.util.GenericIndex;
import jopt.csp.spi.util.GenericIndexManager;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.util.DoubleUtil;
import jopt.csp.variable.CspGenericIndex;
import jopt.csp.variable.CspGenericNumConstant;

/**
 * Base class for arcs that restrict numbers to a range 
 */
public abstract class NumRangeConstraint extends NumConstraint {
    
    //sources
    protected NumExpr sourceExprMin;
    protected boolean minExclusive;
    protected NumExpr sourceExprMax;
    protected boolean maxExclusive;
    protected GenericNumExpr genSourceExprMin;
    protected GenericNumExpr genSourceExprMax;
    protected Number sourceConstMin;
    protected Number sourceConstMax;
    protected GenericNumConstant sourceGenConstMin;
    protected GenericNumConstant sourceGenConstMax;
    
    private Node[] sourceNodes;
    protected boolean booleanArcsObtained=false;
    protected Arc[] arcs;
    
    //current information
    protected Number currentMin;
    protected Number currentMax;
    protected NumExpr currentSourceExprMin;
    protected NumExpr currentSourceExprMax;
    protected NumExpr currentGenSourceExprMin;
    protected NumExpr currentGenSourceExprMax;
    
    protected GenericIndex restrictedIndices[];
    
    
    public NumRangeConstraint(NumExpr sourceMin, boolean minExclusive, NumExpr sourceMax, boolean maxExclusive, NumExpr z, GenericIndex restrictedIndices[]) {
            super(z,null, null, RANGE);
            if (sourceMin instanceof GenericNumExpr) {
                genSourceExprMin = (GenericNumExpr)sourceMin;
                currentGenSourceExprMin = sourceMin;
            }
            if (sourceMax instanceof GenericNumExpr) {
                genSourceExprMax = (GenericNumExpr)sourceMax;
                currentGenSourceExprMax = sourceMax;
            }
            this.restrictedIndices = restrictedIndices;
            this.sourceExprMin = sourceMin;
            this.minExclusive = minExclusive;
            this.sourceExprMax = sourceMax;
            this.maxExclusive = maxExclusive;
            this.currentSourceExprMin = sourceMin;
            this.currentSourceExprMax = sourceMax;
    }
    public NumRangeConstraint(Number sourceMin, boolean minExclusive, Number sourceMax, boolean maxExclusive, NumExpr z, GenericIndex restrictedIndices[]) {
        super(z,null, null, RANGE);
        this.restrictedIndices = restrictedIndices;
        this.sourceConstMin = NumberMath.toConst(sourceMin);
        this.minExclusive = minExclusive;
        this.sourceConstMax = NumberMath.toConst(sourceMax);
        this.maxExclusive = maxExclusive;
        this.currentMax = sourceMax;
        this.currentMin = sourceMin;
    }
    public NumRangeConstraint(CspGenericNumConstant sourceMin, boolean minExclusive, CspGenericNumConstant sourceMax, boolean maxExclusive, NumExpr z, GenericIndex restrictedIndices[]) {
        super(z,null, null, RANGE);
        this.restrictedIndices = restrictedIndices;
        this.sourceGenConstMin = (GenericNumConstant)sourceMin;
        this.minExclusive = minExclusive;
        this.sourceGenConstMax = (GenericNumConstant)sourceMax;
        this.maxExclusive = maxExclusive;
    }
    
    /**
     * Creates an array of arcs representing constraint
     */
    protected Arc[] createArcs() {
        if (genSourceExprMin!=null || genSourceExprMax!=null 
            || sourceGenConstMin !=null  ||sourceGenConstMax !=null       
                || gzexpr!=null)
            return createGenericArcs();
        else
        	return createStandardArcs();
    }
    
    /**
     * Creates generic numeric arcs
     */
    protected abstract Arc[] createGenericArcs();
    
    /**
     * Creates standard numeric arcs
     */
    protected abstract Arc[] createStandardArcs();
    
    // javadoc inherited 
    public abstract boolean violated();
    
    //  javadoc is inherited
    public GenericIndexManager getIndexManager() {
        if(gim == null) {
            ArrayList<CspGenericIndex> sourceAL = new ArrayList<CspGenericIndex>();
            if (genSourceExprMin != null)
                sourceAL.addAll(Arrays.asList(genSourceExprMin.getGenericIndices()));
            if (genSourceExprMax != null)
                sourceAL.addAll(Arrays.asList(genSourceExprMax.getGenericIndices()));
            if (sourceGenConstMin!= null)
                sourceAL.addAll(Arrays.asList(sourceGenConstMin.getIndices()));
            if (sourceGenConstMax != null)
                sourceAL.addAll(Arrays.asList(sourceGenConstMax.getIndices()));
            GenericIndex sourceidx[] = (GenericIndex[])sourceAL.toArray(new GenericIndex[0]);
            GenericIndex zidx[] = null;
            if (gzexpr != null)
                zidx = gzexpr.getGenericIndices();
            gim = new GenericIndexManager(sourceidx, null, zidx, restrictedIndices, false);
        }
        return gim;
    }
    
    // inherited javadoc from NumConstraint abstract class
    public boolean isViolated(boolean allViolated) {
        GenericIndexManager gim = this.getIndexManager();
        Iterator<CspGenericIndex> gimIt;
        // all combinations must be checked in the case of NEQ constraints
        if(allViolated) {
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
            currentSourceExprMin = null;
            currentSourceExprMax = null;
            currentGenSourceExprMin = genSourceExprMin;
            currentGenSourceExprMax = genSourceExprMax;
            //set current Min
            if (genSourceExprMin==null){
                if (sourceConstMin != null) {
                    currentMin = sourceConstMin;
                }
                else if((sourceGenConstMin != null) && (!idxMgr.useCombinedX()|| allViolated)) {
                    currentMin = sourceGenConstMin.getNumberForIndex();
                }
                else if(sourceExprMin!=null) {
                    currentSourceExprMin = sourceExprMin;
                }
            }
            else if (genSourceExprMin!=null && (!idxMgr.useCombinedX()|| allViolated)) {
                currentSourceExprMin = genSourceExprMin.getNumExpressionForIndex();
            }
            //Set current Max
            if (genSourceExprMax==null){
                if (sourceConstMax != null) {
                    currentMax = sourceConstMax;
                }
                else if((sourceGenConstMax != null) && (!idxMgr.useCombinedX()|| allViolated)) {
                    currentMax = sourceGenConstMax.getNumberForIndex();
                }
                else if(sourceExprMax!=null) {
                    currentSourceExprMax = sourceExprMax;
                }
            }
            else if (genSourceExprMax!=null && (!idxMgr.useCombinedX()|| allViolated)) {
                currentSourceExprMax = genSourceExprMax.getNumExpressionForIndex();
            }
        }
        
        return violated();
    }
    
    protected abstract AbstractConstraint createConstraint(NumExpr sourceMin, boolean minExclusive, Number numMinConst, GenericNumConstant gcmin, NumExpr sourceMax, boolean maxExclusive, Number numMaxConst, GenericNumConstant gcmax, NumExpr z);
    
    // javadoc inherited from AbstractConstraint
    protected final AbstractConstraint createConstraintFragment(GenericIndex indices[]) {
        if (genSourceExprMin==null && genSourceExprMax==null && gzexpr==null) {
            return this;
        }
        else {
            // determine vars to frag
            boolean fragSourceExprMin = false;
            boolean fragSourceExprMax = false;
            boolean fragZ = false;
            boolean fragConstMin = false;
            boolean fragConstMax = false;
            for (int i=0; i<indices.length; i++) {
                GenericIndex idx = indices[i];
                if (genSourceExprMin!=null && genSourceExprMin.containsIndex(idx)) fragSourceExprMin = true;
                if (genSourceExprMax!=null && genSourceExprMax.containsIndex(idx)) fragSourceExprMax = true;
                if (gzexpr!=null && gzexpr.containsIndex(idx)) fragZ = true;
                if (sourceGenConstMin!=null && sourceGenConstMin.containsIndex(idx)) fragConstMin= true;
                if (sourceGenConstMax!=null && sourceGenConstMax.containsIndex(idx)) fragConstMax= true;
            }
            
            // create expressions to base fragment upon
            NumExpr sourceMin = fragSourceExprMin ? genSourceExprMin.createFragment(indices) : genSourceExprMin;
            NumExpr sourceMax = fragSourceExprMax ? genSourceExprMax.createFragment(indices) : genSourceExprMax;
            NumExpr z = fragZ ? gzexpr.createFragment(indices) : zexpr;
            GenericNumConstant gcmin = fragConstMin ? (GenericNumConstant)sourceGenConstMin.createFragment(indices) : sourceGenConstMin;
            GenericNumConstant gcmax = fragConstMax ? (GenericNumConstant)sourceGenConstMax.createFragment(indices) : sourceGenConstMax;
            Number numMinConst = null;
            Number numMaxConst = null;
            if((gcmin!=null)&&(gcmin.getConstantCount()==1)) {
                numMinConst = gcmin.getNumConstants()[0];
            }
            else {
                numMinConst = sourceConstMin;
            }
            if((gcmax!=null)&&(gcmax.getConstantCount()==1)) {
                numMaxConst = gcmax.getNumConstants()[0];
            }
            else {
                numMaxConst = sourceConstMax;
            }
            
            return this.createConstraint(sourceMin, minExclusive, numMinConst, gcmin, sourceMax, maxExclusive, numMaxConst, gcmax,z);
            
        }
    }
    
    //  javadoc is inherited
    public void postToGraph() {
        if (graph!=null) {
            if (zexpr != null) zexpr.updateGraph(graph);
            if (sourceExprMin != null) sourceExprMin.updateGraph(graph);
            if (sourceExprMax != null) sourceExprMax.updateGraph(graph);
            
            if (arcs == null) arcs = createArcs();
            for (int i=0; i<arcs.length; i++)
                graph.addArc(arcs[i]);
        }
    }
    
    public Number getSmallestMin(NumExpr n, GenericNumExpr gn) {
        if (n!=null) return n.getNumMin();
        if (gn!=null) return gn.getNumSmallestMin();
        if(currentMin!=null) return currentMin; 
        return sourceGenConstMin.getNumMin();
    }
    
    public Number getLargestMin(NumExpr n, GenericNumExpr gn) {
        if (n!=null) return n.getNumMin();
        if (gn!=null) return gn.getNumLargestMin();
        if(currentMin!=null) return currentMin; 
        return sourceGenConstMin.getNumMax();
    }
    
    public Number getSmallestMax(NumExpr n, GenericNumExpr gn) {
        if (n!=null) return n.getNumMax();
        if (gn!=null) return gn.getNumSmallestMax();
        if(currentMax!=null) return currentMax; 
        return sourceGenConstMax.getNumMin();
    }
    
    public Number getLargestMax(NumExpr n, GenericNumExpr gn) {
        if (n!=null) return n.getNumMax();
        if (gn!=null) return gn.getNumLargestMax();
        if(currentMax!=null) return currentMax; 
        return sourceGenConstMax.getNumMax();
    }
    
    protected double getPrecision() {
        if (!NumberMath.isRealType(nodeType)) return 0;
        
        double precision = Double.POSITIVE_INFINITY;
        
        if (sourceExprMin!=null) 
            precision = Math.min(precision, sourceExprMin.getPrecision());
        if (sourceExprMax!=null) 
            precision = Math.min(precision, sourceExprMax.getPrecision());
        if (zexpr!=null) 
            precision = Math.min(precision, zexpr.getPrecision());
        
        return Math.max(precision, DoubleUtil.DEFAULT_PRECISION);
    }
    
    public Node[] getBooleanSourceNodes() {
        if (sourceNodes==null) {
            Collection<Node> nodes = null;
            
            // retrieve nodes from a min
            if (sourceExprMin!=null) {
                nodes = sourceExprMin.getNodeCollection();
            }
//          retrieve nodes from a max
            if (sourceExprMax!=null) {
                if (nodes==null)
                    nodes = sourceExprMax.getNodeCollection();
                else
                    nodes.addAll(sourceExprMax.getNodeCollection());
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
    
    //We only want to add Source Arcs of Number or Set constraints
    //Java doc inherited
    public Arc[] getBooleanSourceArcs(boolean useConstraint) {
        if(booleanArcsObtained) {
            return new Arc[0];
        }
        else{
	        booleanArcsObtained = true;
	        ArrayList<Arc> arcs = new ArrayList<Arc>();
	        if (sourceExprMin!=null)
	            arcs.addAll(Arrays.asList(sourceExprMin.getBooleanSourceArcs()));
	        if (sourceExprMax!=null)
	            arcs.addAll(Arrays.asList(sourceExprMax.getBooleanSourceArcs()));
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

}