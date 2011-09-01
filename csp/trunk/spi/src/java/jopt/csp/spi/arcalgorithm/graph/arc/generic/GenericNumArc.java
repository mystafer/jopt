/*
 * GenericNumSumArc.java
 * 
 * Created on Mar 19, 2005
 */
package jopt.csp.spi.arcalgorithm.graph.arc.generic;

import java.util.Iterator;

import jopt.csp.spi.arcalgorithm.graph.arc.NumArc;
import jopt.csp.spi.arcalgorithm.graph.node.GenericNodeIndexManager;
import jopt.csp.spi.arcalgorithm.graph.node.GenericNumNode;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.arcalgorithm.variable.GenericNumConstant;
import jopt.csp.spi.util.GenericIndex;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.variable.CspGenericIndex;
import jopt.csp.variable.CspGenericIndexRestriction;
import jopt.csp.variable.PropagationFailureException;

/**
 * Base class for use by generic arcs that are based upon nodes stemming from generic indices
 */
public abstract class GenericNumArc extends GenericArc implements NumArc {
    protected Number xconst;
    protected GenericNumConstant xGenConst;
    protected NumNode nx;
    protected GenericNumNode gx;
    protected boolean xzIdxSame;
    
    protected Number yconst;
    protected GenericNumConstant yGenConst;
    protected NumNode ny;
    protected GenericNumNode gy;
    protected boolean yzIdxSame;

    protected NumNode nz;
    protected GenericNumNode gz;
    
    protected int nodeType;
    protected int arcType;
    protected int operation;
    
    protected GenericNodeIndexManager idxMgr;
    protected Number 				currentXConst;
    protected GenericNumConstant	currentXGenConst;
    protected Number 				currentYConst;
    protected GenericNumConstant	currentYGenConst;
    protected NumNode           	currentNz;
    protected GenericNumNode    	currentGz;
    protected NumNode           	currentNx;
    protected int               	currentChangedXStart;
    protected int               	currentChangedXEnd;
    protected GenericNumNode    	currentGx;
    protected NumNode           	currentNy;
    protected int               	currentChangedYStart;
    protected int               	currentChangedYEnd;
    protected GenericNumNode  		currentGy;
    
    protected CspGenericIndexRestriction sourceIdxRestriction;
    
    /**
     * Constructor where x and y are both constants and Z is a generic variable
     *
     * @param   x           X variable in equation
     * @param   y           Y variable in equation
     * @param   z           Z variable in equation
     * @param   nodeType    Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType     Arc relation type (Eq, Lt, Gt, etc.)
     */
    protected GenericNumArc(Number x, Number y, Node z, int nodeType, int arcType) 
    {
        this(new Node[]{z}, x, y, z, nodeType, arcType, null, null);
    }
    
    /**
     * Constructor where x and y are both constants and Z is a generic variable
     *
     * @param   x           X variable in equation
     * @param   y           Y variable in equation
     * @param   z           Z variable in equation
     * @param   nodeType    Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType     Arc relation type (Eq, Lt, Gt, etc.)
     */
    protected GenericNumArc(Number x, GenericNumConstant y, Node z, int nodeType, int arcType) 
    {
        this(new Node[]{z}, x, y, z, nodeType, arcType, null, null);
    }
    
    /**
     * Constructor where x and y are both constants and Z is a generic variable
     *
     * @param   x           X variable in equation
     * @param   y           Y variable in equation
     * @param   z           Z variable in equation
     * @param   nodeType    Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType     Arc relation type (Eq, Lt, Gt, etc.)
     */
    protected GenericNumArc(GenericNumConstant x, Number y, Node z, int nodeType, int arcType) 
    {
        this(new Node[]{z}, x, y, z, nodeType, arcType, null, null);
    }
    
    /**
     * Constructor where x and y are both constants and Z is a generic variable
     *
     * @param   x           X variable in equation
     * @param   y           Y variable in equation
     * @param   z           Z variable in equation
     * @param   nodeType    Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType     Arc relation type (Eq, Lt, Gt, etc.)
     */
    protected GenericNumArc(GenericNumConstant x, GenericNumConstant y, Node z, int nodeType, int arcType) 
    {
        this(new Node[]{z}, x, y, z, nodeType, arcType, null, null);
    }
    
    /**
     * Constructor where x and y are both constants and Z is a generic variable
     *
     * @param   sources					Source nodes that will trigger the arc
     * @param   x                       X variable in equation
     * @param   y                       Y variable in equation
     * @param   z                       Z variable in equation
     * @param   nodeType                Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType                 Arc relation type (Eq, Lt, Gt, etc.)
     * @param   restrictedIndices       array if indices that have restricted ranges
     * @param   sourceIdxRestriction    Optional restriction of values placed on source index ranges
     */
    protected GenericNumArc(Node sources[], Number x, Number y, Node z, int nodeType, int arcType, GenericIndex restrictedIndices[],
            CspGenericIndexRestriction sourceIdxRestriction) 
    {
        super(sources, new Node[]{z});
        
        this.nodeType = nodeType;
        this.arcType = arcType;
        this.sourceIdxRestriction = sourceIdxRestriction;
        
        // initialize internal variables
        init(null, x, null,  null, y, null, z, restrictedIndices);
    }
    /**
     * Constructor where x and y are both constants and Z is a generic variable
     *
     * @param   sources					Source nodes that will trigger the arc
     * @param   x                       X variable in equation
     * @param   y                       Y variable in equation
     * @param   z                       Z variable in equation
     * @param   nodeType                Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType                 Arc relation type (Eq, Lt, Gt, etc.)
     * @param   restrictedIndices       array if indices that have restricted ranges
     * @param   sourceIdxRestriction    Optional restriction of values placed on source index ranges
     */
    protected GenericNumArc(Node sources[], Number x, GenericNumConstant y, Node z, int nodeType, int arcType, GenericIndex restrictedIndices[],
            CspGenericIndexRestriction sourceIdxRestriction) 
    {
        super(sources, new Node[]{z});
        
        this.nodeType = nodeType;
        this.arcType = arcType;
        this.sourceIdxRestriction = sourceIdxRestriction;
        
        // initialize internal variables
        init(null, x, null,  null, null, y, z, restrictedIndices);
    }
    /**
     * Constructor where x and y are both constants and Z is a generic variable
     *
     * @param   sources					Source nodes that will trigger the arc
     * @param   x                       X variable in equation
     * @param   y                       Y variable in equation
     * @param   z                       Z variable in equation
     * @param   nodeType                Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType                 Arc relation type (Eq, Lt, Gt, etc.)
     * @param   restrictedIndices       array if indices that have restricted ranges
     * @param   sourceIdxRestriction    Optional restriction of values placed on source index ranges
     */
    protected GenericNumArc(Node sources[], GenericNumConstant x, Number y, Node z, int nodeType, int arcType, GenericIndex restrictedIndices[],
            CspGenericIndexRestriction sourceIdxRestriction) 
    {
        super(sources, new Node[]{z});
        
        this.nodeType = nodeType;
        this.arcType = arcType;
        this.sourceIdxRestriction = sourceIdxRestriction;
        
        // initialize internal variables
        init(null, null, x,  null, y, null, z, restrictedIndices);
    }
    
    protected GenericNumArc(Node sources[], GenericNumConstant x, GenericNumConstant y, Node z, int nodeType, int arcType, GenericIndex restrictedIndices[],
            CspGenericIndexRestriction sourceIdxRestriction) 
    {
        super(sources, new Node[]{z});
        
        this.nodeType = nodeType;
        this.arcType = arcType;
        this.sourceIdxRestriction = sourceIdxRestriction;
        
        // initialize internal variables
        init(null, null, x, null, null, y, z, restrictedIndices);
    }    
    
    /**
     * Constructor where x is a constant and Y and Z are variables
     *
     * @param   x           X variable in equation
     * @param   y           Y variable in equation
     * @param   z           Z variable in equation
     * @param   nodeType    Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType     Arc relation type (Eq, Lt, Gt, etc.)
     */
    protected GenericNumArc(Number x, Node y, Node z, int nodeType, int arcType) 
    {
        super(new Node[]{y}, new Node[]{z});
        
        this.nodeType = nodeType;
        this.arcType = arcType;
        
        // initialize internal variables
        init(null, x, null, y, null, null, z, null);
    }
    
    /**
     * Constructor where x is a constant and Y and Z are variables
     *
     * @param   x           X variable in equation
     * @param   y           Y variable in equation
     * @param   z           Z variable in equation
     * @param   nodeType    Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType     Arc relation type (Eq, Lt, Gt, etc.)
     */
    protected GenericNumArc(GenericNumConstant x, Node y, Node z, int nodeType, int arcType) 
    {
        super(new Node[]{y}, new Node[]{z});
        
        this.nodeType = nodeType;
        this.arcType = arcType;
        
        // initialize internal variables
        init(null, null, x, y, null, null, z, null);
    }    
    
    /**
     * Constructor where y is a constant and X and Z are variables
     *
     * @param   x           X variable in equation
     * @param   y           Y variable in equation
     * @param   z           Z variable in equation
     * @param   nodeType    Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType     Arc relation type (Eq, Lt, Gt, etc.)
     */
    protected GenericNumArc(Node x, Number y, Node z, int nodeType, int arcType) {
        this(new Node[]{x}, x, y, z, nodeType, arcType, null, null);
    }
    
    /**
     * Constructor where y is a constant and X and Z are variables
     *
     * @param   x           X variable in equation
     * @param   y           Y variable in equation
     * @param   z           Z variable in equation
     * @param   nodeType    Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType     Arc relation type (Eq, Lt, Gt, etc.)
     */
    protected GenericNumArc(Node x, GenericNumConstant y, Node z, int nodeType, int arcType) {
        this(new Node[]{x}, x, y, z, nodeType, arcType, null, null);
    }
    
    /**
     * Constructor where y is a constant and X and Z are variables
     *
     * @param   sources					Source nodes that will trigger the arc
     * @param   x                       X variable in equation
     * @param   y                       Y variable in equation
     * @param   z                       Z variable in equation
     * @param   nodeType                Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType                 Arc relation type (Eq, Lt, Gt, etc.)
     * @param   excludedIndices         array if indices that should not be included in x, y, or z iterators
     * @param   sourceIdxRestriction    Optional restriction of values placed on source index ranges
     */
    protected GenericNumArc(Node sources[], Node x, Number y, Node z, int nodeType, int arcType, GenericIndex excludedIndices[],
        CspGenericIndexRestriction sourceIdxRestriction) 
    {
        super(sources, new Node[]{z});
        
        this.nodeType = nodeType;
        this.arcType = arcType;
        this.sourceIdxRestriction = sourceIdxRestriction;
        
        // initialize internal variables
        init(x, null, null, null, y, null,  z, excludedIndices);
    }
    
    /**
     * Constructor where y is a constant and X and Z are variables
     *
     * @param   sources					Source nodes that will trigger the arc
     * @param   x                       X variable in equation
     * @param   y                       Y variable in equation
     * @param   z                       Z variable in equation
     * @param   nodeType                Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType                 Arc relation type (Eq, Lt, Gt, etc.)
     * @param   excludedIndices         array if indices that should not be included in x, y, or z iterators
     * @param   sourceIdxRestriction    Optional restriction of values placed on source index ranges
     */
    protected GenericNumArc(Node sources[], Node x, GenericNumConstant y, Node z, int nodeType, int arcType, GenericIndex excludedIndices[],
        CspGenericIndexRestriction sourceIdxRestriction) 
    {
        super(sources, new Node[]{z});
        
        this.nodeType = nodeType;
        this.arcType = arcType;
        this.sourceIdxRestriction = sourceIdxRestriction;
        
        // initialize internal variables
        init(x, null, null, null, null, y, z, excludedIndices);
    }    
    
    /**
     * Constructor for three nodes
     *
     * @param   x           X variable in equation
     * @param   y           Y variable in equation
     * @param   z           Z variable in equation
     * @param   nodeType    Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType     Arc relation type (Eq, Lt, Gt, etc.)
     */
    protected GenericNumArc(Node x, Node y, Node z, int nodeType, int arcType) {
        super(new Node[]{x,y}, new Node[]{z});
        
        this.nodeType = nodeType;
        this.arcType = arcType;
        
        // initialize internal variables
        init(x, null, null, y, null, null, z, null);
    }
    
    /**
     * Initializes internal variables and flags based on arguments that were passed to the constructor
     */
    protected void init(Node xnode, Number xconst, GenericNumConstant xGenConst, Node ynode, 
            Number yconst, GenericNumConstant yGenConst, Node znode, GenericIndex restrictedIndices[]) {
        // ensure at least one node is generic
        if ((xnode==null || !(xnode instanceof GenericNumNode)) &&
            (ynode==null || !(ynode instanceof GenericNumNode)) &&
            (!(znode instanceof GenericNumNode)))
        {
        	throw new IllegalStateException("generic arcs require at least one generic node");
        }
        
        // store z variable
        if (znode instanceof GenericNumNode)
            this.gz = (GenericNumNode) znode;
        else
            this.nz = (NumNode) znode;
        
        // store x variable
        if (xconst != null) {
        	this.xconst = NumberMath.toConst(xconst);
        	this.currentXConst = this.xconst;
        }
        else if (xGenConst != null) {
            this.xGenConst = xGenConst;
            this.currentXGenConst = xGenConst;
        }
        else if (xnode instanceof GenericNumNode)
            this.gx = (GenericNumNode) xnode;
        else
        	this.nx = (NumNode) xnode;
        
        // store y variable
        if (yconst != null) {
            this.yconst = NumberMath.toConst(yconst);
            this.currentYConst = this.yconst;
        }
        else if (yGenConst != null) {
            this.yGenConst = yGenConst;
            this.currentYGenConst = yGenConst;
        }
        else if (ynode instanceof GenericNumNode)
            this.gy = (GenericNumNode) ynode;
        else
            this.ny = (NumNode) ynode;
        
        // create index manager to manage nodes
    	this.idxMgr = createIndexManager(xnode, xGenConst, ynode, yGenConst, znode, restrictedIndices);

    }
    
    /**
     * Creates index manager to use during propagation
     */
    protected GenericNodeIndexManager createIndexManager(Node xnode, GenericNumConstant xconst,
            Node ynode, GenericNumConstant yconst, Node znode, GenericIndex restrictedIndices[]) {
        return new GenericNodeIndexManager(xnode, xconst, ynode, yconst, znode, restrictedIndices, false);
    }
    
    
    /**
     * Called when a specific X or Y node has been changed and initiates propagation
     */
    protected void propagateForInitiatingSource(Node n) throws PropagationFailureException {
        // if node is not generic, it is the same as propagating
        // every index
    	if (n instanceof NumNode || n.equals(gz)) {
    		propagateAllSources();
        }
        
        // need to iterate over changes to the X
        // node
        else if (n.equals(gx)) {
            propagateForChangeToGenericX();
        }
        
        // need to iterate over changes to the Y
        // node
        else {
            propagateForChangeToGenericY();
        }
    }
    
    /**
     * Propagate changes based on the fact that the generic node X has had changes
     * to internal nodes
     */
    protected void propagateForChangeToGenericX() throws PropagationFailureException {
        // loop over the specific modified values in X and propagate changes
        if (!idxMgr.useCombinedX() || arcType==NEQ) {
            // determine range of values to iterator over
            int start = -1;
            int end = -1;
            if (arcType==NEQ) {
            	start = gx.valueModifiedMinOffset();
                end = gx.valueModifiedMaxOffset();
            }
            else {
                start = gx.rangeModifiedMinOffset();
                end = gx.rangeModifiedMaxOffset();
            }
            
            // note that this may be a sign of a larger problem as this method
            // should not have been called if no change to the generic x node
            // occurred (ie. if none of its component nodes was altered)
            if(start == end && end == -1) {
                return;
            }
            
            for (int i=start; i<=end; i++) {
                // move indices for x to correspond to the offset currently being evaluated
                gx.setIndicesToNodeOffset(i);
                currentNx = (NumNode) gx.getNode(i);
                
                // perform propagation using iterator to set Y and Z nodes
                propagateUsingIterator(idxMgr.xControlledIterator(), false, true);
            }
        }
        
        // determine if the X variable as a whole can be combined to update
        // the target
        else {
            // set current node
            currentNx = null; currentGx = gx;
            
            // determine range of changed values
            currentChangedXStart = gx.rangeModifiedMinOffset();
            currentChangedXEnd = gx.rangeModifiedMaxOffset();
            currentChangedYStart = -1;
            currentChangedYEnd = -1;
            
            // ensure range of changed values is valid
            if (currentChangedXEnd < currentChangedXStart) return;
            
            // perform propagation using iterator to set Y and Z nodes
            propagateUsingIterator(idxMgr.xControlledIterator(), false, true);
        }
    }
    
    /**
     * Propagate changes based on the fact that the generic node Y has had changes
     * to internal nodes
     */
    protected void propagateForChangeToGenericY() throws PropagationFailureException {
        // loop over the specific modified values in Y and propagate changes
        if (!idxMgr.useCombinedY() || arcType==NEQ) {
            // determine range of values to iterator over
            int start = -1;
            int end = -1;
            if (arcType==NEQ) {
                start = gy.valueModifiedMinOffset();
                end = gy.valueModifiedMaxOffset();
            }
            else {
                start = gy.rangeModifiedMinOffset();
                end = gy.rangeModifiedMaxOffset();
            }
            
            // note that this may be a sign of a larger problem as this method
            // should not have been called if no change to the generic x node
            // occurred (ie. if none of its component nodes was altered)
            if(start == end && end == -1) {
                return;
            }
            
            for (int i=start; i<=end; i++) {
                // move indices for y to correspond to the offset currently being evaluated
                gy.setIndicesToNodeOffset(i);
                currentNy = (NumNode) gy.getNode(i);
                
                // perform propagation using iterator to set X and Z nodes
                propagateUsingIterator(idxMgr.yControlledIterator(), true, false);
            }
        }
        
        // determine if the Y variable as a whole can be combined to update
        // the target
        else {
            // set current node
            currentNy = null; currentGy = gy;
            
            // determine range of changed values
            currentChangedXStart = -1;
            currentChangedXEnd = -1;
            currentChangedYStart = gy.rangeModifiedMinOffset();
            currentChangedYEnd = gy.rangeModifiedMaxOffset();
            
            // ensure range of changed values is valid
            if (currentChangedYEnd < currentChangedYStart) return;
            
            // perform propagation using iterator to set X and Z nodes
            propagateUsingIterator(idxMgr.yControlledIterator(), true, false);
        }
    }
    
    /** 
     * Function that iterates over appropriate X, Y and Z combinations and calls
     * propagateCurrentNodes for appropriate combinations
     *
     * @throws PropagationFailureException  If domain of target node becomes empty
     */
    protected void propagateAllSources() throws PropagationFailureException {
        currentChangedXStart = -1;
        currentChangedXEnd = -1;
        currentChangedYStart = -1;
        currentChangedYEnd = -1;
        
        // perform propagation using iterator to set X, Y and Z nodes
        if (arcType==NEQ)
            propagateUsingIterator(idxMgr.allIterator(), true, true);
        else
        	propagateUsingIterator(idxMgr.iterator(), true, true);
    }
    
    /** 
     * Function that iterates over appropriate X, Y and Z combinations and calls
     * setCurrentNodeAndPropagate to complete propagation
     *
     * @param idxIterator   iterator object to use when looping over X, Y and Z combinations
     * @param setX          true if current X node should be set as iterator is processed
     * @param setY          true if current Y node should be set as iterator is processed
     * @throws PropagationFailureException  If domain of target node becomes empty
     */
    protected void propagateUsingIterator(Iterator<CspGenericIndex> idxIterator,
            boolean setX, boolean setY) throws PropagationFailureException 
    {
        // no need to iterate over nodes
        if (idxIterator==null) {
        	setCurrentNodeAndPropagate(setX, setY);
        }
        
        // iterate over nodes to process
        else {
            while (idxIterator.hasNext()) {
                idxIterator.next();
                setCurrentNodeAndPropagate(setX, setY);
            }
        }
    }
    
    /** 
     * Function that will set nodes based on current index settings and
     * and flags indicating which nodes should be set then calls propagateCurrentNodes for 
     * appropriate combinations
     *
     * @param setX  true if current X node should be set as iterator is processed
     * @param setY  true if current Y node should be set as iterator is processed
     * @throws PropagationFailureException  If domain of target node becomes empty
     */
    protected void setCurrentNodeAndPropagate(boolean setX, boolean setY) throws PropagationFailureException 
    {
        //retrieve next z to apply changes
        if (!idxMgr.zIndexIsRestricted()) {
            currentNz = null; currentGz = gz;
            if (nz!=null) 
                currentNz = nz;
            else if (!idxMgr.useCombinedZ()) 
                currentNz = (NumNode) gz.getNodeForIndex();
        }
        
        // retrieve sources to apply changes
        if (setX && !idxMgr.xIndexIsRestricted()) {
            currentNx = null; currentGx = gx;
            currentXConst=null; currentXGenConst = xGenConst;
            if (nx!=null)
                currentNx = nx;
            else if (gx!=null && (!idxMgr.useCombinedX() || arcType==NEQ))
                currentNx = (NumNode) gx.getNodeForIndex();
            else if (xGenConst!=null && (!idxMgr.useCombinedX() || arcType==NEQ))
                currentXConst = xGenConst .getNumberForIndex();
            else if (xconst!=null) 
                currentXConst = xconst;
        }
        
        if (setY && !idxMgr.yIndexIsRestricted()) {
            currentNy = null; currentGy = gy;
            currentYConst=null; currentYGenConst = yGenConst;
            if (ny!=null)
                currentNy = ny;
            else if (gy!=null && (!idxMgr.useCombinedY() || arcType==NEQ))
                currentNy = (NumNode) gy.getNodeForIndex();
            else if (yGenConst!=null && (!idxMgr.useCombinedY() || arcType==NEQ))
                currentYConst = yGenConst .getNumberForIndex();
            else if (yconst!=null) 
                currentYConst = yconst;
        }
        
        // perform updates to nodes
        propagateCurrentNodes();
    }
    
    /** 
     * Performs actual propagation of changes between x, y and z nodes
     *
     * @throws PropagationFailureException  If domain of target node becomes empty
     */
    protected abstract void propagateCurrentNodes() throws PropagationFailureException;
    
    
    /**
     * Sets the minimum value for the current Z
     */
    protected void setMinZ(Number n) throws PropagationFailureException {
        if (currentNz != null) {
            currentNz.setMin(n);
        }
        else if (currentGz != null){
            for (int i=0; i<currentGz.getNodeCount(); i++) {
                ((NumNode) currentGz.getNode(i)).setMin(n);
            }
        }
    }
    
    /**
     * Sets the maximum value for the current Z
     */
    protected void setMaxZ(Number n) throws PropagationFailureException {
        if (currentNz != null) {
            currentNz.setMax(n);
        }
        else if (currentGz != null){
            for (int i=0; i<currentGz.getNodeCount(); i++) {
                ((NumNode) currentGz.getNode(i)).setMax(n);
            }
        }
    }
    
    /**
     * Sets the value for the current Z
     */
    protected void setValueZ(Number n) throws PropagationFailureException {
        if (currentNz != null) {
            currentNz.setValue(n);
        }
        else if (currentGz != null){
            for (int i=0; i<currentGz.getNodeCount(); i++) {
                ((NumNode) currentGz.getNode(i)).setValue(n);
            }
        }
    }
    
    /**
     * Reduces the domain range for the current Z
     */
    protected void setRangeZ(Number s, Number e) throws PropagationFailureException {
        if (currentNz != null) {
            currentNz.setRange(s,e);
        }
        else if (currentGz != null){
            for (int i=0; i<currentGz.getNodeCount(); i++) {
                ((NumNode) currentGz.getNode(i)).setRange(s,e);
            }
        }
    }
    
    /**
     * Removes a range of values from the domain for the current Z
     */
    protected void removeRangeZ(Number s, Number e) throws PropagationFailureException {
        if (currentNz != null) {
            currentNz.removeRange(s,e);
        }
        else if (currentGz != null){
            for (int i=0; i<currentGz.getNodeCount(); i++) {
                ((NumNode) currentGz.getNode(i)).removeRange(s,e);
            }
        }
    }
    
    /**
     * Removes a specific value from the domain for the current Z
     */
    protected void removeValueZ(Number n) throws PropagationFailureException {
        if (currentNz != null) {
            currentNz.removeValue(n);
        }
        else if (currentGz != null){
            for (int i=0; i<currentGz.getNodeCount(); i++) {
                ((NumNode) currentGz.getNode(i)).removeValue(n);
            }
        }
    }
    
    /**
     * Fetches the largest 'minimum' value from all internal nodes for the current X node
     */
    protected Number getLargestMinX() {
        if (currentXConst!= null) return currentXConst;
        if (currentXGenConst != null) return currentXGenConst.getNumMax();
        if (currentNx != null) return currentNx.getMin();
        if (currentChangedXStart >= 0) return currentGx.getLargestMin(currentChangedXStart, currentChangedXEnd);
        return currentGx.getLargestMin();
    }

    /**
     * Fetches the largest 'maximum' value from all internal nodes for the current X node
     */
    protected Number getLargestMaxX() {
        if (currentXConst!= null) return currentXConst;
        if (currentXGenConst != null) return currentXGenConst.getNumMax();
        if (currentNx != null) return currentNx.getMax();
        if (currentChangedXStart >= 0) return currentGx.getLargestMax(currentChangedXStart, currentChangedXEnd);
        return currentGx.getLargestMax();
    }

    /**
     * Fetches the smallest 'minimum' value from all internal nodes for the current X node
     */
    protected Number getSmallestMinX() {
        if (currentXConst!= null) return currentXConst;
        if (currentXGenConst != null) return currentXGenConst.getNumMin();
        if (currentNx != null) return currentNx.getMin();
        if (currentChangedXStart >= 0) return currentGx.getSmallestMin(currentChangedXStart, currentChangedXEnd);
        return currentGx.getSmallestMin();
    }

    /**
     * Fetches the smallest 'maximum' value from all internal nodes for the current X node
     */
    protected Number getSmallestMaxX() {
        if (currentXConst != null) return currentXConst;
        if (currentXGenConst != null) return currentXGenConst.getNumMin();
        if (currentNx != null) return currentNx.getMax();
        if (currentChangedXStart >= 0) return currentGx.getSmallestMax(currentChangedXStart, currentChangedXEnd);
        return currentGx.getSmallestMax();
    }

    /**
     * Fetches the largest 'minimum' value from all internal nodes for the current Y node
     */
    protected Number getLargestMinY() {
        if (currentYConst != null) return currentYConst;
        if (currentYGenConst != null) return currentYGenConst.getNumMax();
        if (currentNy != null) return currentNy.getMin();
        if (currentChangedYStart >= 0) return currentGy.getLargestMin(currentChangedYStart, currentChangedYEnd);
        return currentGy.getLargestMin();
    }

    /**
     * Fetches the largest 'maximum' value from all internal nodes for the current Y node
     */
    protected Number getLargestMaxY() {
        if (currentYConst!= null) return currentYConst;
        if (currentYGenConst != null) return currentYGenConst.getNumMax();
        if (currentNy != null) return currentNy.getMax();
        if (currentChangedYStart >= 0) return currentGy.getLargestMax(currentChangedYStart, currentChangedYEnd);
        return currentGy.getLargestMax();
    }

    /**
     * Fetches the smallest 'minimum' value from all internal nodes for the current Y node
     */
    protected Number getSmallestMinY() {
        if (currentYConst!= null) return currentYConst;
        if (currentYGenConst != null) return currentYGenConst.getNumMin();
        if (currentNy != null) return currentNy.getMin();
        if (currentChangedYStart >= 0) return currentGy.getSmallestMin(currentChangedYStart, currentChangedYEnd);
        return currentGy.getSmallestMin();
    }

    /**
     * Fetches the smallest 'maximum' value from all internal nodes for the current Y node
     */
    protected Number getSmallestMaxY() {
        if (currentYConst!= null) return currentYConst;
        if (currentYGenConst != null) return currentYGenConst.getNumMin();
        if (currentNy != null) return currentNy.getMax();
        if (currentChangedYStart >= 0) return currentGy.getSmallestMax(currentChangedYStart, currentChangedYEnd);
        return currentGy.getSmallestMax();
    }

    /**
     * Fetches the largest 'minimum' value from all internal nodes for the current Z node
     */
    protected Number getLargestMinZ() {
        if (currentNz != null) return currentNz.getMin();
        return currentGz.getLargestMin();
    }

    /**
     * Fetches the largest 'maximum' value from all internal nodes for the current Z node
     */
    protected Number getLargestMaxZ() {
        if (currentNz != null) return currentNz.getMax();
        return currentGz.getLargestMax();
    }

    /**
     * Fetches the smallest 'minimum' value from all internal nodes for the current Z node
     */
    protected Number getSmallestMinZ() {
        if (currentNz != null) return currentNz.getMin();
        return currentGz.getSmallestMin();
    }

    /**
     * Fetches the smallest 'maximum' value from all internal nodes for the current Z node
     */
    protected Number getSmallestMaxZ() {
        if (currentNz != null) return currentNz.getMax();
        return currentGz.getSmallestMax();
    }

    /**
     * Returns true if Y variable is bound
     */
    protected boolean isBoundY() {
        if (currentYConst != null) return true;
        if (currentYGenConst != null) return true;
        if (currentNy != null) return currentNy.isBound();
        return currentGy.isBound();
    }

    /**
     * Returns true if X variable is bound
     */
    protected boolean isBoundX() {
        if (currentXConst != null) return true;
        if (currentXGenConst != null) return true;
        if (currentNx != null) return currentNx.isBound();
        return currentGx.isBound();
    }
    
    /**
     * Returns precision that is used for X
     */
    protected double getPrecisionX() {
        if (currentXConst != null) return getPrecisionZ();
        if (currentXGenConst != null) return getPrecisionZ();
        if (currentNx != null) return currentNx.getPrecision();
        return currentGx.getPrecision();
    }

    /**
     * Returns precision that is used for Y
     */
    protected double getPrecisionY() {
        if (currentYConst != null) return getPrecisionZ();
        if (currentYGenConst != null) return getPrecisionZ();
        if (currentNy != null) return currentNy.getPrecision();
        return currentGy.getPrecision();
    }

    /**
     * Returns precision that is used for Z
     */
    protected double getPrecisionZ() {
        if (currentNz != null) return currentNz.getPrecision();
        return currentGz.getPrecision();
    }

    /**
     * Returns true if value is in any X variable domain
     */
    public boolean isInDomainX(Number val) {
        if (currentXConst!=null) return NumberMath.compare(currentXConst, val, getPrecisionZ(), nodeType) == 0;
        if (currentNx != null) return currentNx.isInDomain(val);

        // check if node is valid range of constants
        if(currentXGenConst != null) {
            for (int i=0; i<currentXGenConst.getConstantCount(); i++){
                if (NumberMath.compare(currentXGenConst.getNumber(i),(val), getPrecisionZ(), nodeType)==0)
                    return true;
            }
            return false;
        }
        // check if node is within X of the modified range
        for (int i=0; i<currentGx.getNodeCount(); i++){
            if (currentGx.getNode(i).isInDomain(val))
                return true;
        }
        
        return false;
    }
    
    /**
     * Returns true if value is in any Y variable domain
     */
    public boolean isInDomainY(Number val) {
        if (currentYConst!=null) return NumberMath.compare(currentYConst, val, getPrecisionZ(), nodeType) == 0;
        if (currentNy != null) return currentNy.isInDomain(val);

        if (currentYGenConst != null) {
            // check if node is valid range of constants
            for (int i=0; i<currentYGenConst.getConstantCount(); i++){
                if (NumberMath.compare(currentYGenConst.getNumber(i),(val), getPrecisionZ(), nodeType)==0)
                    return true;
            }
            return false;
        }
        
        // check if node is within Y of the modified range
        for (int i=0; i<currentGy.getNodeCount(); i++){
            if (currentGy.getNode(i).isInDomain(val))
                return true;
        }
        
        return false;
    }
    
    /**
     * Returns true if value is in any Z variable domain
     */
    public boolean isInDomainZ(Number val) {
        if (currentNz != null) return currentNz.isInDomain(val);

        // check if node is within Z of the modified range
        for (int i=0; i<currentGz.getNodeCount(); i++){
            if (currentGz.getNode(i).isInDomain(val))
                return true;
        }
        
        return false;
    }
    
    /**
     * Locates the next number higher than a given value that exists in all domains of X.  Val
     * is returned if no value can be found.
     */
    protected Number getNextHigherX(Number val) {
        // single constant is being used, check if it is higher than value
        if (currentXConst!= null) {
            if (NumberMath.compare(currentXConst, val, getPrecisionZ(), nodeType)>0)
                return currentXConst;
        }
        
        // generic constant is being used, locate smallest value above val
        else if (currentXGenConst!= null) {
            
            // locate first value above given value
            for (int i=0; i<currentXGenConst.getConstantCount(); i++){
                
                // compare current value in generic constant to argument of method
                Number curVal = currentXGenConst.getNumber(i);
                if (NumberMath.compare(curVal, val, getPrecisionZ(), nodeType)>0)
                    return curVal;
            }
        }
        
        // single variable is being used, call get next higher function
        else if (currentNx!= null) {
            return currentNx.getNextHigher(val);
        }
        
        // generic variable is being used, locate smallest value above val in all domains
        else if (currentGx!= null) {
        	int xstart = currentChangedXStart;
            int xend = (xstart > 0) ? currentChangedXEnd : currentGx.getNodeCount() - 1;
            
            // locate variable with smallest domain
            NumNode smallestNode = (NumNode) currentGx.getNode(xstart);
            for (int i=xstart+1; i<=xend; i++) {
                NumNode curNode = (NumNode) currentGx.getNode(i);
                
                if (curNode.getSize() < smallestNode.getSize())
                    smallestNode = curNode;
            }
            
            // looping through available values in smallest node, search for value
            // in all domains
            Number prevVal = val;
            Number nextVal = smallestNode.getNextHigher(val);
            while (NumberMath.compare(nextVal, prevVal, getPrecisionZ(), nodeType)>0) {
            	if (isInDomainX(nextVal)) return nextVal;
                prevVal = nextVal;
                
                // locate next value to use
                for (int i=xstart+1; i<=xend; i++) {
                    NumNode curNode = (NumNode) currentGx.getNode(i);
                    Number tmp = curNode.getNextHigher(prevVal);

                    // need to use highest value above current for all nodes
                    if (NumberMath.compare(nextVal, tmp, getPrecisionZ(), nodeType)>0)
                        nextVal = tmp;
                }
            }
        }
        
        return val;
    }

    /**
     * Locates the next lower higher than a given value that exists in all domains of X.  Val
     * is returned if no value can be found.
     */
    protected Number getNextLowerX(Number val) {
        // single constant is being used, check if it is lower than value
        if (currentXConst!= null) {
            if (NumberMath.compare(currentXConst, val, getPrecisionZ(), nodeType)<0)
                return currentXConst;
        }
        
        // generic constant is being used, locate largest value below val
        else if (currentXGenConst!= null) {
            
            // locate first value above given value
            for (int i=0; i<currentXGenConst.getConstantCount(); i++){
                
                // compare current value in generic constant to argument of method
                Number curVal = currentXGenConst.getNumber(i);
                if (NumberMath.compare(curVal, val, getPrecisionZ(), nodeType)<0)
                    return curVal;
            }
        }
        
        // single variable is being used, call get next lower function
        else if (currentNx!= null) {
            return currentNx.getNextLower(val);
        }
        
        // generic variable is being used, locate largest value below val in all domains
        else if (currentGx!= null) {
            int xstart = currentChangedXStart;
            int xend = (xstart > 0) ? currentChangedXEnd : currentGx.getNodeCount() - 1;
            
            // locate variable with smallest domain
            NumNode smallestNode = (NumNode) currentGx.getNode(xstart);
            for (int i=xstart+1; i<=xend; i++) {
                NumNode curNode = (NumNode) currentGx.getNode(i);
                
                if (curNode.getSize() < smallestNode.getSize())
                    smallestNode = curNode;
            }
            
            // looping through available values in smallest node, search for value
            // in all domains
            Number prevVal = val;
            Number nextVal = smallestNode.getNextLower(val);
            while (NumberMath.compare(nextVal, prevVal, getPrecisionZ(), nodeType)<0) {
                if (isInDomainX(nextVal)) return nextVal;
                prevVal = nextVal;
                
                // locate next value to use
                for (int i=xstart+1; i<=xend; i++) {
                    NumNode curNode = (NumNode) currentGx.getNode(i);
                    Number tmp = curNode.getNextLower(prevVal);

                    // need to use highest value below current for all nodes
                    if (NumberMath.compare(nextVal, tmp, getPrecisionZ(), nodeType)<0)
                        nextVal = tmp;
                }
            }
        }
        
        return val;
    }

    public void propagate() throws PropagationFailureException {
        propagateAllSources();
    }
    
    public void propagate(Node src) throws PropagationFailureException {
        propagateForInitiatingSource(src);
    }
}
