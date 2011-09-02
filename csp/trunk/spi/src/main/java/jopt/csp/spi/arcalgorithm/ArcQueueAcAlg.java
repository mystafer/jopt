package jopt.csp.spi.arcalgorithm;

import jopt.csp.spi.arcalgorithm.graph.NodeArcGraph;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.node.NodeChangeEvent;
import jopt.csp.spi.arcalgorithm.util.ArcQueue;
import jopt.csp.spi.util.DomainChangeType;
import jopt.csp.variable.PropagationFailureException;

/**
 * Base class for arc-consistency algorithms that use an arc queue
 * for propagation.
 */
public abstract class ArcQueueAcAlg extends ArcBasedAcAlg {
    private int strength;
    private ArcQueue nodeArcQueue;
    private ArcQueue arcQueue;
    private ArcQueue genericArcQueue;

    /**
     * Constructor
     */
    public ArcQueueAcAlg(int strength) {
        this.strength = strength;
        // Holds only node arcs, those which point back at their source node.
        this.nodeArcQueue = new ArcQueue();
        // Holds any other type of arc including binary, hyper, generic, etc.
        this.arcQueue = new ArcQueue();
        // Temporarily holds generic arcs encountered in the arcQueue
        // because we want to process these after all other arcs.
        this.genericArcQueue = new ArcQueue();
    }

    /**
     * Propagates any waiting node-arcs in the node-arc queue
     */
    private void processNodeArcs() throws PropagationFailureException  {
        // Remove node arcs and reduce node domains if any exist
        currentArc = nodeArcQueue.next();
        while (currentArc != null) {
            // Reduce node domain by propagating node arc
            currentArc.propagate();

            // Remove next arc from queue
            currentArc = nodeArcQueue.next();
        }
    }

    /**
     * Propagates constraints added to algorithm reducing domains of
     * variables to which constraints are applied.
     *
     * @throws PropagationFailureException      If unable to propagate constraints
     */
    public void propagate() throws PropagationFailureException {
        // process any node arcs first since they will not need
        // to be propagated again
        processNodeArcs();

        // Process until arc queue is empty
        currentArc = arcQueue.next();
        while (currentArc != null) {
            // Push generic arcs for processing after all binary and hyper arcs
            if (currentArc.getArcType() != Arc.GENERIC) {
                currentArc.propagate();
            }
            else {
                genericArcQueue.add(currentArc);
            }

            // Remove next arc from queue
            currentArc = arcQueue.next();

            // if no current arc, retrieve waiting generic arc
            if (currentArc==null) {
                currentArc = genericArcQueue.next();
                while (currentArc != null)  {
                    currentArc.propagate();

                    // since generic arcs may post new constraints, check for new node arcs
                    if (nodeArcQueue.size() > 0)
                        processNodeArcs();

                    currentArc = genericArcQueue.next();
                }

                // check for another arc after processing generics
                currentArc = arcQueue.next();
            }
        }
    }

    /**
     * Adds initially added arcs to queue for revision.  Also
     * informs the arc of the algorithm strength being used
     * and whether deltas are being used.
     */
    public void arcAddedEvent(NodeArcGraph graph, Arc arc) {
        // initialize arc strength
        arc.setAlgorithmStrength(strength);
        arc.setUseDomainDeltas(false);

        // Add arc to queue for revision
        if (arc.getArcType()==Arc.NODE) {
            nodeArcQueue.add(arc);
        }
        else {
            arcQueue.add(arc);
        }
    }

    /**
     * Removes any arcs from waiting queues
     */
    public void arcRemovedEvent(NodeArcGraph graph, Arc arc) {
        if (arc.getArcType()==Arc.NODE) {
            nodeArcQueue.remove(arc);
        }
        else {
            arcQueue.remove(arc);
            genericArcQueue.remove(arc);
        }
    }

    /**
     * Adds arcs for changed nodes to queue for revision.
     */
    public void nodeChangedEvent(NodeArcGraph graph, NodeChangeEvent evt) {
        // Note: we rely on the fact that generally no node arcs (complexity=0)
        // need to be revisited when a node is changed during propagation.  Node
        // arcs should only need to be visited at the beginning of propagation.
        switch(evt.getType()) {
        case DomainChangeType.VALUE:
            arcQueue.addAll(graph.getValueSourceArcs(evt.getNode()));

        case DomainChangeType.RANGE:
            arcQueue.addAll(graph.getRangeSourceArcs(evt.getNode()));

        default:
            arcQueue.addAll(graph.getDomainSourceArcs(evt.getNode()));
        }
    }
}