/*
 * GenericNumNode.java
 * 
 * Created on Mar 19, 2005
 */
package jopt.csp.spi.arcalgorithm.graph.node;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jopt.csp.spi.solver.ChoicePointDataMap;
import jopt.csp.spi.solver.ChoicePointEntryListener;
import jopt.csp.spi.solver.ChoicePointEventListener;
import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.spi.util.DomainChangeType;
import jopt.csp.spi.util.GenericIndex;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.util.DoubleUtil;

/**
 * A generic node relating to a generic variable such as Xi which represents X1, X2, etc.
 */
public abstract class GenericNumNode extends AbstractNode implements NodeChangeListener, ChoicePointEntryListener, GenericNode {
    private ChoicePointDataMap cpdata;
    private int valueModifiedMin;
    private int valueModifiedMax;
    private int rangeModifiedMin;
    private int rangeModifiedMax;
    private int domainModifiedMin;
    private int domainModifiedMax;
    private GenericIndex indices[];
    private int indexOffsets[];
    private NumNode nodes[];
    private boolean overRealInterval;
    protected int nodeType;
    private ChoicePointEventListener cpeListener;
    
    /**
     * Constructor
     * 
     * @param name      unique name of this node
     * @param indices   array of indices that generic node is based upon
     * @param nodes     array of nodes that this generic node wraps
     */
    public GenericNumNode(String name, GenericIndex indices[], NumNode nodes[], int nodeType) {
    	super(name);
        this.indices = indices;
        this.nodes = nodes;
        this.nodeType = nodeType;
        
        // calculate index offsets and total nodes required
        indexOffsets = new int[indices.length];
        indexOffsets[indices.length-1] = 1;
        int totalNodes = indices[indices.length-1].size();
        for (int i=indices.length-2; i>=0; i--) {
            // offset will equal product of previous index size * previous index offset
            int prevIdx = i+1;
            indexOffsets[i] = indices[prevIdx].size() * indexOffsets[prevIdx];
            
            // total nodes is product of all sizes
            totalNodes *= indices[i].size();
        }
        
        // ensure correct number of nodes are given for indices
        if (totalNodes != nodes.length) {
            throw new IllegalStateException("Expected " + totalNodes + " nodes, but received " + nodes.length);
        }
        
        // mark entire range of nodes as modified
        this.domainModifiedMin = 0;
        this.domainModifiedMax = nodes.length-1;
        this.rangeModifiedMin = 0;
        this.rangeModifiedMax = nodes.length-1;
        this.valueModifiedMin = 0;
        this.valueModifiedMax = nodes.length-1;
        
        // register listener with internal nodes
        for (int i=0; i<nodes.length; i++)
            nodes[i].addDomainChangeListener(this, new Integer(i));
    }
    
    /**
     * Returns true if all internal nodes are bound
     */
    public boolean isBound() {
        for (int i=0; i<nodes.length; i++)
            if (!nodes[i].isBound()) return false;
            
    	return true;
    }
    
    /**
     * Returns true if any internal node is bound
     */
    public boolean isAnyBound() {
        for (int i=0; i<nodes.length; i++)
            if (!nodes[i].isBound()) return true;
            
        return false;
    }
    
    /**
     * Returns precision associated with this domain
     */
    public double getPrecision() {
        if (NumberMath.isRealType(nodeType)) {
            // determine minimum precision
            double precision = Double.POSITIVE_INFINITY;
            for (int i=0; i<nodes.length; i++) {
                precision = Math.min(nodes[i].getPrecision(), precision);
                if (precision <= DoubleUtil.DEFAULT_PRECISION) break;
            }
            
            precision = Math.max(precision, DoubleUtil.DEFAULT_PRECISION);
            
            return precision;
        }
        else {
        	return 0;
        }
    }
    
    // javadoc inherited from GenericNode interface
    public GenericIndex[] getIndices() {
    	return indices;
    }
    
    // javadoc inherited from GenericNode interface
    public Node getNodeForIndex() {
        // determine index of node to return
        int nodeIdx = 0;
        for (int i=0; i<indices.length; i++)
            nodeIdx += indices[i].currentVal() * indexOffsets[i];
        
    	return nodes[nodeIdx];
    }
    
    // javadoc inherited from GenericNode interface
    public int getNodeCount() {
        return nodes.length;
    }
    
    // javadoc inherited from GenericNode interface
    public Node getNode(int offset) {
        if (offset < 0 || offset >= nodes.length) return null;
    	return nodes[offset];
    }
    
    /**
     * Returns all the nodes wrapped by this generic node
     */
    public NumNode[] getNodes() {
        return nodes;
    }

    /**
     * Returns that largest maximal value of all variables in array
     */
    public Number getLargestMax() {
        return getLargestMax(0, nodes.length-1);
    }
    
    /**
     * Returns minimal value of node
     */
    public Number getMin() {
        return getSmallestMin();
    }

    /**
     * Returns maximum value of node
     */
    public Number getMax() {
        return getLargestMax();
    }

    /**
     * Returns that largest maximal value of all variables in array within
     * start and end indices
     */
    public Number getLargestMax(int start, int end) {
        Number max = NumberMath.minValue(nodeType);
        double precision = getPrecision();
        
        for (int i=start; i<=end; i++) {
            // determine if a new max is located
            Number n = nodes[i].getMax();
            
            if (NumberMath.compare(n, max, precision, nodeType) > 0)
                max = n;
        }
        
        return max;
    }

    /**
     * Returns that smallest maximal value of all variables in array
     */
    public Number getSmallestMax() {
        return getSmallestMax(0, nodes.length-1);
    }
    
    /**
     * Returns that smallest maximal value of all variables in array within
     * start and end indices
     */
    public Number getSmallestMax(int start, int end) {
        Number min = NumberMath.maxValue(nodeType);
        double precision = getPrecision();
        
        for (int i=start; i<=end; i++) {
            // determine if a new min is located
            Number n = nodes[i].getMax();
            
            if (NumberMath.compare(n, min, precision, nodeType) < 0)
                min = n;
        }
        
        return min;
    }

    /**
     * Returns that largest minimal value of all variables in array
     */
    public Number getLargestMin() {
        return getLargestMin(0, nodes.length-1);
    }
    
    /**
     * Returns that largest minimal value of all variables in array within
     * start and end indices
     */
    public Number getLargestMin(int start, int end) {
        Number max = NumberMath.minValue(nodeType);
        double precision = getPrecision();
        
        for (int i=start; i<=end; i++) {
            // determine if a new max is located
            Number n = nodes[i].getMin();
            
            if (NumberMath.compare(n, max, precision, nodeType) > 0)
                max = n;
        }
        
        return max;
    }

    /**
     * Returns that smallest minimal value of all variables in array
     */
    public Number getSmallestMin() {
        return getSmallestMin(0, nodes.length-1);
    }
    
    /**
     * Returns that smallest minimal value of all variables in array within
     * start and end indices
     */
    public Number getSmallestMin(int start, int end) {
        Number min = NumberMath.maxValue(nodeType);
        double precision = getPrecision();
        
        for (int i=start; i<=end; i++) {
            // determine if a new min is located
            Number n = nodes[i].getMin();
            
            if (NumberMath.compare(n, min, precision, nodeType) < 0)
                min = n;
        }
        
        return min;
    }
    
    /**
     * Returns true if a value is contained in this node's domain
     */
    public boolean isInDomain(Object val) {
    	return false;
    }
    
    /**
     * Returns size of domain for this node.  If domain is infinite, value
     * is Integer.MAX_VALUE
     */
    public int getSize() {
    	return 0;
    }
    
    public Iterator<Number> values() {
    	return null;
    }
    
    /**
     * Returns true if domain of node is over real intervals
     */
    public boolean isOverRealInterval() {
    	return overRealInterval;
    }

    /**
     * Called by internally wrapped node when it is altered.  This allows the generic node to
     * track the indices of nodes that have been altered and how much they've been altered
     */
    public void nodeChange(NodeChangeEvent ev) {
        processNodeEvent(ev);
        
        if ((valueModifiedMin < 0 && valueModifiedMax >= 0) ||
            (valueModifiedMin >= 0 && valueModifiedMax < 0) ||
            (rangeModifiedMin < 0 && rangeModifiedMax >= 0) ||
            (rangeModifiedMin >= 0 && rangeModifiedMax < 0) ||
            (domainModifiedMin < 0 && domainModifiedMax >= 0) ||
            (domainModifiedMin >= 0 && domainModifiedMax < 0)) 
        {
        	throw new RuntimeException("min / max modified values in inconsistent state");
        }
        
        fireChangeEvent(ev.getType());
    }
    
    private void processNodeEvent(NodeChangeEvent ev) {
        int idx = ((Integer) (ev.getCallbackValue())).intValue();
        Map<String, Integer> modifyMap = getModifiedMinMaxChangeMap();

        // add to value modified set
        if (ev.getType() == DomainChangeType.VALUE) {
        	boolean changed = false;
        	
            if (valueModifiedMin==-1 || idx < valueModifiedMin) {
                valueModifiedMin = idx;
                changed = true;
            }
            
            if (valueModifiedMax==-1 || idx > valueModifiedMax) {
                valueModifiedMax = idx;
                changed = true;
            }

            // update modify map
            if (modifyMap!=null && changed) {
                modifyMap.put("mnv", new Integer(valueModifiedMin));
                modifyMap.put("mxv", new Integer(valueModifiedMax));
            }
        }

        // add to range modified set
        if (ev.getType() >= DomainChangeType.RANGE) {
        	boolean changed = false;
        	
            if (rangeModifiedMin==-1 || idx < rangeModifiedMin) {
                rangeModifiedMin = idx;
                changed = true;
            }
            
            if (rangeModifiedMax==-1 || idx > rangeModifiedMax) {
                rangeModifiedMax = idx;
                changed = true;
            }

            // update modify map
            if (modifyMap!=null && changed) {
                modifyMap.put("mnr", new Integer(rangeModifiedMin));
                modifyMap.put("mxr", new Integer(rangeModifiedMax));
            }
        }
        
        // add to domain modified set
    	boolean changed = false;
        if (domainModifiedMin==-1 || idx < domainModifiedMin) {
            domainModifiedMin = idx;
            changed = true;
        }
        
        if (domainModifiedMax==-1 || idx > domainModifiedMax) {
            domainModifiedMax = idx;
            changed = true;
        }

        // update modify map
        if (modifyMap!=null && changed) {
            modifyMap.put("mnd", new Integer(domainModifiedMin));
            modifyMap.put("mxd", new Integer(domainModifiedMax));
        }
    }
    
    /**
     * Clears the delta set for this node's domain
     */
    public void clearDelta() {
        // clear delta for all child nodes that are not specifically in any graph
        for (int i=0; i<nodes.length; i++) {
        	Node n = nodes[i];
            if (!n.inGraph()) n.clearDelta();
        }

        // reset min / max modified offsets
        valueModifiedMin = -1;
        valueModifiedMax = -1;
        rangeModifiedMin = -1;
        rangeModifiedMax = -1;
        domainModifiedMin = -1;
        domainModifiedMax = -1;
    }
    
    /**
     * Returns the set of changes to modified node min / max value
     */
    private Map<String, Integer> getModifiedMinMaxChangeMap() {
        if (cpdata==null){
            return null;
        }
        
        // retrieve map of modifications
        @SuppressWarnings("unchecked")
		Map<String, Integer> modifyMap = (Map<String, Integer>) cpdata.get("mm");
        if (modifyMap == null) {
            modifyMap = new HashMap<String, Integer>();
            cpdata.put("mm", modifyMap);
        }
        
        return modifyMap;
    }

    /**
     * Sets the choicepoint stack associated with this graph
     * Can only be set once
     */
    public void setChoicePointStack(ChoicePointStack cps) {
        if (this.cpdata!=null && cps!=null) {
            throw new IllegalStateException("Choice point stack already set for node");
        }

        // set choicepoint stack for all child nodes that are not specifically in any graph
        for (int i=0; i<nodes.length; i++) {
            Node n = nodes[i];
            if (!n.inGraph() && !n.choicePointStackSet()) n.setChoicePointStack(cps);
        }
        
        if (cps==null){
            this.cpdata=null;
        }
        else
            this.cpdata = cps.newDataMap(this);
    }
    
    /**
     * Returns true if a call to setChoicePointStack will fail
     */
    public boolean choicePointStackSet() {
        if(this.cpdata==null)
            return false;
        return true;
    }

    // javadoc is inherited
    public void beforeChoicePointPopEvent() {
        // Add any values that were removed
        Map<String, Integer> modifyMap = getModifiedMinMaxChangeMap();
        
        //If no modify data is stored, there can be no popping back
        if (modifyMap==null) {
            return;
        }
        
        // restore modified min / max values
        Number min = (Number)modifyMap.get("mnv");
        if (min!=null) valueModifiedMin = min.intValue();
        else valueModifiedMin = -1;
        Number max = (Number)modifyMap.get("mxv");
        if (max!=null) valueModifiedMax = max.intValue();
        else valueModifiedMax = -1;

        min = (Number)modifyMap.get("mnr");
        if (min!=null) rangeModifiedMin = min.intValue();
        else rangeModifiedMin = -1;
        max = (Number)modifyMap.get("mxr");
        if (max!=null) rangeModifiedMax = max.intValue();
        else rangeModifiedMax = -1;

        min = (Number)modifyMap.get("mnd");
        if (min!=null) domainModifiedMin = min.intValue();
        else domainModifiedMin = -1;
        max = (Number)modifyMap.get("mxd");
        if (max!=null) domainModifiedMax = max.intValue();
        else domainModifiedMax = -1;

        if ((valueModifiedMin < 0 && valueModifiedMax >= 0) ||
            (valueModifiedMin >= 0 && valueModifiedMax < 0) ||
            (rangeModifiedMin < 0 && rangeModifiedMax >= 0) ||
            (rangeModifiedMin >= 0 && rangeModifiedMax < 0) ||
            (domainModifiedMin < 0 && domainModifiedMax >= 0) ||
            (domainModifiedMin >= 0 && domainModifiedMax < 0)) 
        {
        	throw new RuntimeException("min / max modified values in inconsistent state");
        }
    }

    // javadoc is inherited
    public void afterChoicePointPopEvent() {
        if(cpeListener != null)
            cpeListener.choicePointPop();
    }

    // javadoc is inherited
    public void beforeChoicePointPushEvent() {
        // do nothing, state is recorded as changes occur
    }

    // javadoc is inherited
    public void afterChoicePointPushEvent() {
        // process for push is same as pop
        beforeChoicePointPopEvent();
        
        if(cpeListener != null)
            cpeListener.choicePointPush();
    }
    
    /**
     * Stores appropriate data for future restoration.
     */
    public Object getState() {
        // store state for each node in a statemap
        Map<String, Object> stateMap = new HashMap<String, Object>();
        for (int i=0; i<nodes.length; i++) {
            Node n = nodes[i];
            stateMap.put(n.getName(), n.getState());
        }
        
        return stateMap;
    }
    
    /**
     *  Restores variable information from stored data.
     */
    public void restoreState(Object state) {
        // restore state for each node in a statemap
    	@SuppressWarnings("unchecked")
		Map<String, Object> stateMap = (Map<String, Object>) state;
        for (int i=0; i<nodes.length; i++) {
            Node n = nodes[i];
            n.restoreState(stateMap.get(n.getName()));
        }
    }
    
    // javadoc inherited from GenericNode interface
    public void setIndicesToNodeOffset(int offset) {
    	for (int i=0; i<indexOffsets.length; i++) {
            int idxOffset = indexOffsets[i];
    		int idxVal = offset / idxOffset;
            
            // update index
            indices[i].changeVal(idxVal);
            
            // strip current index from offset
            offset %= idxOffset;
        }
    }
    
    /**
     * Returns minimum offset of a node having a value-modification
     */
    public int valueModifiedMinOffset() {
        return valueModifiedMin;
    }

    /**
     * Returns maximum offset of a node having a value-modification
     */
    public int valueModifiedMaxOffset() {
        return valueModifiedMax;
    }

    /**
     * Returns minimum offset of a node having a range-modification
     */
    public int rangeModifiedMinOffset() {
        return rangeModifiedMin;
    }

    /**
     * Returns maximum offset of a node having a range-modification
     */
    public int rangeModifiedMaxOffset() {
        return rangeModifiedMax;
    }

    /**
     * Returns minimum offset of a node having a domain-modification
     */
    public int domainModifiedMinOffset() {
        return domainModifiedMin;
    }

    /**
     * Returns maximum offset of a node having a domain-modification
     */
    public int domainModifiedMaxOffset() {
        return domainModifiedMax;
    }
    
    public void setChoicePointEventListener(ChoicePointEventListener cpel) {
        cpeListener = cpel;
    }
    
    /**
     * Adds a listener to this node's internal nodes interested in range and value
     * events
     */
    public void addInternalRangeChangeListener(NodeChangeListener listener) {
        // register listener with internal nodes
        for (int i=0; i<nodes.length; i++)
            nodes[i].addRangeChangeListener(listener, new Integer(i));
    }
    
    /**
     * Displays name of node along with indices
     */
    public String toString() {
        String name = getName()==null ? "~" : getName();
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
     * Returns the type of number this node is based on.
     */
    public abstract int getNumberType();
}
