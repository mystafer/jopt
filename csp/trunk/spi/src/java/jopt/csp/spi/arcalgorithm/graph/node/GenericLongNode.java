/*
 * GenericNumNode.java
 * 
 * Created on Mar 19, 2005
 */
package jopt.csp.spi.arcalgorithm.graph.node;

import jopt.csp.spi.util.GenericIndex;
import jopt.csp.spi.util.NumConstants;


/**
 * A generic long node relating to a generic variable such as Xi which represents X1, X2, etc.
 */
public class GenericLongNode extends GenericNumNode  {
    //NOTE: min/max delta changes are tracked in summation arcs rather than in generic nodes
//    ReversibleLongArray minDeltas;
//    ReversibleLongArray maxDeltas;
    
    /**
     * Constructor
     * 
     * @param name      unique name of this node
     * @param indices   array of indices that generic node is based upon
     * @param nodes     array of nodes that this generic node wraps
     */
    public GenericLongNode(String name, GenericIndex indices[], NumNode nodes[]) {
        super(name, indices, nodes, NumConstants.LONG);
//        minDeltas = new ReversibleLongArray(nodes.length);
//        maxDeltas = new ReversibleLongArray(nodes.length);
    }
    
    /**
     * Returns the type of number this node is based on.
     */
    public int getNumberType() {
        return NumConstants.LONG;
    }
    
//    protected void setCPSOnMinMaxDeltaArrays(ChoicePointStack cps) {
//        minDeltas.setChoicePointStack(cps);
//        maxDeltas.setChoicePointStack(cps);
//    }
//    
//    protected void setMinMaxDeltaChange(int idx, Number minChange, Number maxChange) {
//        if(minChange != null)
//            minDeltas.adjValue(idx, minChange);
//        if(maxChange != null)
//            maxDeltas.adjValue(idx, maxChange);
//    }
//    
//    public void clearMinMaxDeltaArrays() {
//        minDeltas.clear();
//        maxDeltas.clear();
//    }
//    
//    public Number getMaxDelta(int idx) {
//        return maxDeltas.getValue(idx);
//    }
//    public Number getMinDelta(int idx) {
//        return minDeltas.getValue(idx);
//    }
}
