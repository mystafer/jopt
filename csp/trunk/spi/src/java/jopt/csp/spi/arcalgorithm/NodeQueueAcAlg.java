package jopt.csp.spi.arcalgorithm;

import jopt.csp.spi.arcalgorithm.graph.NodeArcGraph;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.graph.node.NodeChangeEvent;
import jopt.csp.spi.arcalgorithm.util.ArcQueue;
import jopt.csp.spi.arcalgorithm.util.NodeQueue;
import jopt.csp.spi.util.DomainChangeType;
import jopt.csp.variable.PropagationFailureException;

/**
 * Base class for arc-consistency algorithms that use a node queue
 * for propagation.
 */
public abstract class NodeQueueAcAlg extends ArcBasedAcAlg {
    private int strength;
    private boolean useDeltas;
    private ArcQueue zeroComplexityArcQueue;
    private NodeQueue newNodeQueue;
    private NodeQueue complexNodeQueue;
    private ArcQueue genericArcQueue;
    
    /**
     * Constructor
     */
    public NodeQueueAcAlg(int strength, boolean useDeltas) {
        this.strength = strength;
        this.useDeltas = useDeltas;
        this.zeroComplexityArcQueue = new ArcQueue();
        this.newNodeQueue = new NodeQueue();
        this.complexNodeQueue = new NodeQueue();
        this.genericArcQueue = new ArcQueue();
    }
    
    /**
     * Propagates any waiting node-arcs in the node-arc queue
     */
    private void processNodeArcs() throws PropagationFailureException  {
        // Remove node arcs and reduce node domains if any exist
        currentArc = zeroComplexityArcQueue.next();
        while (currentArc != null) {
            // Reduce node domain by propagating node arc
//            System.out.println("current arc: "+currentArc);
            currentArc.propagate();

            // Remove next arc from queue
            currentArc = zeroComplexityArcQueue.next();
        }
    }

    /**
     * Propagates constraints added to algorithm reducing domains of
     * variables to which constraints are applied.
     *
     * @throws PropagationFailureException      If unable to propagate constraints
     */
    public void propagate() throws PropagationFailureException {
        try {
//          System.out.println("************* start prop *************");
//          System.out.println(graph);

            // process any node arcs first since they will not need
            // to be propagated again
            processNodeArcs();

            // Process until node queue is empty
//          System.out.println("initial queue: " + newNodeQueue);
            NodeChangeEvent evt = newNodeQueue.nextChange();
            while (evt != null) {
//              System.out.println("==============================");
//              System.out.println("current node: " + evt);
//              System.out.println("current queue: " + newNodeQueue);
                ArcQueue arcQueue = getArcsForNodeChange(evt);
//              System.out.println("arcs for node: " + arcQueue.size());  
//              System.out.println("arcs: " + arcQueue);
                Node currentNode = evt.getNode();
                while (arcQueue.size() > 0 && arcQueue.getMinComplexity() <= 2) {
                    // Remove next arc from queue
                    currentArc = arcQueue.next();

//                  System.out.println("current arc: " + currentArc);
//                  System.out.println("propagating arc: " + currentArc);
//                  System.out.println("deltas: " + useDeltas);
//                  String nb = graph.nodesDescription();
                    if (useDeltas)
                        currentArc.propagate(currentNode);
                    else
                        currentArc.propagate();
//                  String na = graph.nodesDescription();
//                  if (!na.equals(nb)) {
//                  System.out.println("--- nodes changed ---");
//                  System.out.println(".....before....");
//                  System.out.println(nb);
//                  System.out.println(".....after....");
//                  System.out.println(na);
//                  System.out.println("---------------------");
//                  }
                }

                // Clear delta for current node after arcs with complexity <= 2 have been processed
                if (useDeltas)
                    currentNode.clearDelta();

                // add to node queue for arcs with complexity > 2 if arcs remain
                if (arcQueue.size() > 0)
                    complexNodeQueue.add(currentNode, evt.getType(), arcQueue);

                // add node to additional arc queue if 
                // Remove next event from queue
//              System.out.println("result queue: " + newNodeQueue);
                evt = newNodeQueue.nextChange();

                // If no new node, propagate any nodes with higher complexity arcs
                while (evt == null && complexNodeQueue.size() > 0) {
                    evt = complexNodeQueue.nextChange();
                    arcQueue = complexNodeQueue.getCurrentArcs();

                    // Propagate arcs
//                  System.out.println("==============================");
//                  System.out.println("current complex node: " + evt);
//                  System.out.println("current complex queue: " + complexNodeQueue);
//                  System.out.println("arcs for node: " + arcQueue.size());   
                    currentNode = evt.getNode();
                    currentArc = arcQueue.next();
                    while (currentArc != null) {
//                      System.out.println("current arc: " + currentArc);
//                      String nb = graph.nodesDescription();                
                        // higher complexity arcs cannot take advantage of delta
                        if (currentArc.getArcType() == Arc.GENERIC) {
                            genericArcQueue.add(currentArc);
                        }
                        else {
                            currentArc.propagate();
//                          String na = graph.nodesDescription();

//                          if (!na.equals(nb)) {
//                          System.out.println("--- nodes changed ---");
//                          System.out.println(".....before....");
//                          System.out.println(nb);
//                          System.out.println(".....after....");
//                          System.out.println(na);
//                          System.out.println("---------------------");
//                          }

                            // since complex arcs may post new constraints, check for new node arcs
                            if (zeroComplexityArcQueue.size() > 0)
                                processNodeArcs();
                        }

                        currentArc = arcQueue.next();
                    }

                    // Check if new node queue is still empty
                    evt = newNodeQueue.nextChange();
                }

                // If no new node, propagate any complex generic arcs
                while (evt == null && genericArcQueue.size() > 0) {
                    currentArc = genericArcQueue.next();
//                    System.out.println("current arc: "+currentArc);
                    currentArc.propagate();

                    // since complex arcs may post new constraints, check for new node arcs
                    if (zeroComplexityArcQueue.size() > 0)
                        processNodeArcs();

                    // Check if new node queue is still empty
                    evt = newNodeQueue.nextChange();
                }
            }
//          System.out.println("************* end prop *************");
//          System.out.println(graph);
        }
        finally {
//          zeroComplexityArcQueue.clear();
//          newNodeQueue.clear();
//          complexNodeQueue.clear();
//          genericArcQueue.clear();
        }
    }
    
    /**
     * Retrieves set of arcs for a node event
     */
    private ArcQueue getArcsForNodeChange(NodeChangeEvent evt) {
        ArcQueue arcQueue = new ArcQueue();
        arcQueue.setRequiredMinComplexity(1);
        
        switch(evt.getType()) {
            case DomainChangeType.VALUE:
                arcQueue.addAll(graph.getValueSourceArcs(evt.getNode()));
                
            case DomainChangeType.RANGE:
                arcQueue.addAll(graph.getRangeSourceArcs(evt.getNode()));
                
            default:
                arcQueue.addAll(graph.getDomainSourceArcs(evt.getNode()));
        }
        
        return arcQueue;
    }
    
    /**
     * Adds initially added arcs to queue for revision.  Also
     * informs the arc of the algorithm strength being used
     * and whether deltas are being used.
     */
    public void arcAddedEvent(NodeArcGraph graph, Arc arc) {
        // initialize arc strength
        arc.setAlgorithmStrength(strength);
        arc.setUseDomainDeltas(useDeltas);
        
        // Add arc to queue for revision
        if (arc.getComplexity()==0) {
            zeroComplexityArcQueue.add(arc);
        }
        else {
        	newNodeQueue.addArc(arc);
        }
    }

    /**
     * Removes any arcs from waiting queues
     */
    public void arcRemovedEvent(NodeArcGraph graph, Arc arc) {
    	if (arc.getComplexity()==0)
            zeroComplexityArcQueue.remove(arc);
    	
    	else if (arc.getArcType()==Arc.GENERIC)
    		genericArcQueue.remove(arc);
    }

    /**
     * Removes nodes from queues that are waiting
     */
    public void nodeRemovedEvent(NodeArcGraph graph, Node n) {
    	newNodeQueue.remove(n);
    	complexNodeQueue.remove(n);
    }
    
    /**
     * Adds changed nodes to queue for revision
     */
    public void nodeChangedEvent(NodeArcGraph graph, NodeChangeEvent evt) {
//System.out.println("Node added: " + evt.getNode());    	
    	NodeChangeEvent oldEvt = complexNodeQueue.remove(evt.getNode());
    	
    	int type = (oldEvt == null) ? evt.getType() : Math.max(oldEvt.getType(), evt.getType());
    	newNodeQueue.add(evt.getNode(), type);
    }
    
}