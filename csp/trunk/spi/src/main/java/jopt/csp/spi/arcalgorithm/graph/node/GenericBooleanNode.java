
package jopt.csp.spi.arcalgorithm.graph.node;

import java.util.HashMap;
import java.util.Iterator;

import jopt.csp.spi.solver.ChoicePointDataMap;
import jopt.csp.spi.solver.ChoicePointEntryListener;
import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.spi.util.GenericIndex;

/**
 * A generic node relating to a generic variable such as Xi which represents X1, X2, etc.
 */
public class GenericBooleanNode extends AbstractNode implements NodeChangeListener, ChoicePointEntryListener, GenericNode {
    private ChoicePointDataMap cpdata;
    private int domainModifiedMin;
    private int domainModifiedMax;
    private GenericIndex indices[];
    private int indexOffsets[];
    private BooleanNode nodes[];
    
    /**
     * Constructor
     * 
     * @param name      unique name of this node
     * @param indices   array of indices that generic node is based upon
     * @param nodes     array of nodes that this generic node wraps
     */
    public GenericBooleanNode(String name, GenericIndex indices[], BooleanNode nodes[]) {
        super(name);
        this.indices = indices;
        this.nodes = nodes;
        
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
    
    public Iterator<Boolean> values() {
    	return null;
    }
    
    /**
     * Returns true if domain of node is over real intervals
     */
    public boolean isOverRealInterval() {
        return false;
    }

    /**
     * Called by internally wrapped node when it is altered.  This allows the generic node to
     * track the indices of nodes that have been altered
     */
    public void nodeChange(NodeChangeEvent ev) {
        int idx = ((Integer) (ev.getCallbackValue())).intValue();
        HashMap<String, Integer> modifyMap = getModifiedMinMaxChangeMap();
        
        // add to domain modified set
        if (domainModifiedMin==-1 || idx < domainModifiedMin) {
            domainModifiedMin = idx;
            if (modifyMap!=null) modifyMap.put("mnd", new Integer(idx));
        }
        
        if (domainModifiedMax==-1 || idx > domainModifiedMax) {
            domainModifiedMax = idx;
            if (modifyMap!=null) modifyMap.put("mxd", new Integer(idx));
        }
        
        fireChangeEvent(ev.getType());
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

        // clear modified node lists
        HashMap<String, Integer> modifyMap = getModifiedMinMaxChangeMap();
        if (modifyMap!=null) modifyMap.clear();
        
        // reset min / max modified offsets
        domainModifiedMin = -1;
        domainModifiedMax = -1;
    }
    
    /**
     * Returns the set of changes to modified node min / max value
     */
    private HashMap<String, Integer> getModifiedMinMaxChangeMap() {
        if (cpdata==null) return null;
        @SuppressWarnings("unchecked")
		HashMap<String, Integer> modifyMap = (HashMap<String, Integer>) cpdata.get("mm");
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

        if (cps==null)
            this.cpdata=null;
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
        HashMap<String, Integer> modifyMap = getModifiedMinMaxChangeMap();
        
        //If no modify data is stored, there can be no popping back
        if (modifyMap==null) {
            return;
        }
        
        // restore modified min / max values
        Integer min = (Integer)modifyMap.get("mnd");
        if (min!=null) domainModifiedMin = min.intValue();
        else domainModifiedMin = -1;
        Integer max = (Integer)modifyMap.get("mxd");
        if (max!=null) domainModifiedMax = max.intValue();
        else domainModifiedMax = -1;
    }

    // javadoc is inherited
    public void afterChoicePointPopEvent() {
    }

    // javadoc is inherited
    public void beforeChoicePointPushEvent() {
        // do nothing, state is recorded as changes occur
    }

    // javadoc is inherited
    public void afterChoicePointPushEvent() {
        // process for push is same as pop
        beforeChoicePointPopEvent();
    }
    
    /**
     * Stores appropriate data for future restoration.
     */
    public Object getState() {
        // store state for each node in a statemap
        HashMap<String, Object> stateMap = new HashMap<String, Object>();
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
		HashMap<String, Object> stateMap = (HashMap<String, Object>) state;
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

    /**
     * Returns true if any node in the array evaluates to false
     */
    public boolean isAnyFalse() {
        return isAnyFalse(0, nodes.length-1);
    }
    
    /**
     * Returns true if any node in the array evaluates to false within
     * start and end indices
     */
    public boolean isAnyFalse(int s, int e) {
        for (int i=s; i<=e; i++)
            if (nodes[i].isFalse()) return true;
            
        return false;
    }
    
    /**
     * Returns true if all nodes in the array evaluate to false
     */
    public boolean isAllFalse() {
        return isAllFalse(0, nodes.length-1);
    }
    
    /**
     * Returns true if all nodes in the array evaluate to false within
     * start and end indices
     */
    public boolean isAllFalse(int s, int e) {
        for (int i=s; i<=e; i++)
            if (!nodes[i].isFalse()) return false;
            
        return true;
    }
    
    /**
     * Returns true if any node in the array evaluates to false
     */
    public boolean isAnyTrue() {
        return isAnyTrue(0, nodes.length-1);
    }
    
    /**
     * Returns true if any node in the array evaluates to true within
     * start and end indices
     */
    public boolean isAnyTrue(int s, int e) {
        for (int i=s; i<=e; i++)
            if (nodes[i].isTrue()) return true;
            
        return false;
    }
    
    /**
     * Returns true if all nodes in the array evaluate to true
     */
    public boolean isAllTrue() {
        return isAllTrue(0, nodes.length-1);
    }
    
    /**
     * Returns true if all nodes in the array evaluate to true within
     * start and end indices
     */
    public boolean isAllTrue(int s, int e) {
        for (int i=s; i<=e; i++)
            if (!nodes[i].isFalse()) return false;
            
        return true;
    }
    
}
