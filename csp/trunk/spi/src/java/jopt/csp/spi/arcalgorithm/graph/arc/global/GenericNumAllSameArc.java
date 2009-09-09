/*
 * GenericNumAllDiffArc.java
 * 
 * Created on Jul 28, 2005
 */
package jopt.csp.spi.arcalgorithm.graph.arc.global;

import jopt.csp.spi.arcalgorithm.graph.arc.NumArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericArc;
import jopt.csp.spi.arcalgorithm.graph.node.GenericNumNode;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.graph.node.NodeMath;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.util.DomainChangeType;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.variable.PropagationFailureException;

/**
 * Generic arc that is used as an opposite to the all-different arc
 * 
 * @author Nick Coleman
 * @version $Revision $
 */
public class GenericNumAllSameArc extends GenericArc implements NumArc {

    private int nodeType;
    private boolean isGeneric;
    
    private GenericNumAllSameArc(Node sources[], boolean isGeneric) {
        super(sources, sources);
        this.isGeneric = isGeneric;
        
        // initialize source dependencies to nodes
        sourceDependencies = new int[sources.length];
        for (int i=0; i<sources.length; i++)
            sourceDependencies[i] = DomainChangeType.VALUE;
    }
    
    public GenericNumAllSameArc(NumNode sources[]) {
        this(sources, false);
        
        // determine node type of arc
        for (int i=0; i<sources.length; i++) {
        	NumNode n = sources[i];
            nodeType = Math.max(nodeType, NodeMath.nodeType(n));
        }
    }
    
    public GenericNumAllSameArc(GenericNumNode source) {
        this(new Node[]{source}, true);
        nodeType = NodeMath.nodeType(source);
    }
    
    /**
     * Calculates precision to use during math operations
     */
    private double calculatePrecision() {
        if (!NumberMath.isRealType(nodeType)) return 0;
        
        // use precision from double
        if (isGeneric) {
        	return ((GenericNumNode) sources[0]).getPrecision();
        }
        
        // determine smallest precision for array
        else {
            double p = Double.POSITIVE_INFINITY;
            for (int i=0; i<sources.length; i++) {
            	NumNode n = (NumNode) sources[i];
                p = Math.min(p, n.getPrecision());
            }
            
            return p;
        }
    }

    /**
     * Sets the minimum and maximum value of all nodes to the same value
     */
    private void propagateMinMax(Number min, Number max, Node sources[]) throws PropagationFailureException {
        double precision = calculatePrecision();
        
        // make sure min is not greater than max
        if (NumberMath.compare(min, max, precision, nodeType) > 0)
            throw new PropagationFailureException();
        
        for (int i=0; i<sources.length; i++)
            ((NumNode) sources[i]).setRange(min, max);
    }
    
    /**
     * Handles propagating changes for a generic node
     */
    private void propagateGenericNode() throws PropagationFailureException {
        GenericNumNode gn = (GenericNumNode) sources[0];

        // Determine new min and max for node
        Number min = null;
        Number max = null;
        if (gn.valueModifiedMinOffset() > 0) {
            min = gn.getLargestMin(gn.valueModifiedMinOffset(), gn.valueModifiedMaxOffset());
            max = gn.getSmallestMax(gn.valueModifiedMinOffset(), gn.valueModifiedMaxOffset());
        }
        
        // no nodes were marked as having a value modified, process all nodes
        else {
            min = gn.getLargestMin();
            max = gn.getSmallestMax();
        }
        
        // update range for nodes
        propagateMinMax(min, max, gn.getNodes());
    }
    
    // javadoc inherited from Arc
    public void propagate(Node src) throws PropagationFailureException {
            // propagate when source node is generic
            if (isGeneric) {
            propagateGenericNode();
        }
        
        // propagate for node that was altered
        else {
            NumNode n = (NumNode) src;
        	propagateMinMax(n.getMin(), n.getMax(), sources);
        }
    }
    
    // javadoc inherited from Arc
    public void propagate() throws PropagationFailureException {
        // propagate when source node is generic
        if (isGeneric) {
            propagateGenericNode();
        }
        
        // propagate all nodes
        else {
            double precision = calculatePrecision();
            
            // determine new min and max
            Number min = new Double(Double.NEGATIVE_INFINITY);
            Number max = new Double(Double.POSITIVE_INFINITY);
            
            for (int i=0; i<sources.length; i++) {
                NumNode n = (NumNode) sources[i];
                
                // locate largest min
                if (NumberMath.compare(min, n.getMin(), precision, nodeType) < 0)
                    min = n.getMin();
                
                // locate smallest max
                if (NumberMath.compare(max, n.getMax(), precision, nodeType) > 0)
                    max = n.getMax();
            }

            // update source nodes with new min and max
            propagateMinMax(min, max, sources);
        }
    }
}
