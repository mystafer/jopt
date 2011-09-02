package jopt.csp.spi;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Set;

import jopt.csp.spi.arcalgorithm.ArcBasedAcAlg;
import jopt.csp.spi.arcalgorithm.graph.NodeArcGraph;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.node.NodeChangeEvent;
import jopt.csp.variable.CspAlgorithmStrength;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc consistent algorithm based on the AC1 algorithm.  This algorithm
 * is both simple and extremely inefficient as it revisits all arcs if any
 * node changed in the previous iteration.  It is implemented only for
 * demonstration and comparison with better arc algorithms, and it should
 * not be used in production.
 * 
 * Consistency in networks of Relations [AC1-3]
 * A.K. Mackworth, in Artificial Intelligence 8, 99-118, 1977. 
 */
public class AC1 extends ArcBasedAcAlg {
    private int strength;
    private boolean changed;
    
    /**
     * Creates an arc consistent strength algorithm
     */
    public AC1() {
        this(CspAlgorithmStrength.ARC_CONSISTENCY);
    }
    
    /**
     * Creates algorithm with specified strength
     * 
     * @see jopt.csp.variable.CspAlgorithmStrength
     */
    public AC1(int strength) {
        this.strength = strength;
        this.changed = true;
    }


    /**
     * Propagates constraints added to algorithm reducing domains of
     * variables to which constraints are applied.
     *
     * @throws PropagationFailureException      If unable to propagate constraints
     */
    public void propagate() throws PropagationFailureException {
        // retrieve set of all arcs contained in graph
        Set<Arc> arcs = graph.getAllArcs();
        
        // loop until no more changes occur
        while (changed) {
            changed = false;
            
            // if arc set is modified during propagation, mark
            // has changed having occurred and continue
            try {
                // propagate every arc in graph
            	Iterator<Arc> arcIter = arcs.iterator();
                while (arcIter.hasNext()) {
                    Arc arc = (Arc) arcIter.next();
                    arc.propagate();
                }
            }
            catch (ConcurrentModificationException modx) {
                // The ConcurrentModificationException occurs when the arc collection
                // is modified and the iterator is no longer valid.  When this happens,
                // a node has changed and we restart visiting all arcs.
                changed = true;
            }
        }
    }

    /**
     * Updates changed flag to true to allow next call to propagate to
     * process arcs in graph
     */
    public void arcAddedEvent(NodeArcGraph graph, Arc arc) {
        // initialize arc strength
        arc.setAlgorithmStrength(strength);
        arc.setUseDomainDeltas(false);
        
        this.changed = true;
    }
    
    /**
     * Updates changed flag to true to allow next call to propagate to
     * process arcs in graph
     */
    public void nodeChangedEvent(NodeArcGraph graph, NodeChangeEvent evt) {
        this.changed = true;
    }
}