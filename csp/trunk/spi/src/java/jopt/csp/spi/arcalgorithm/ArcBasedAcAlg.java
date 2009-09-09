package jopt.csp.spi.arcalgorithm;

import jopt.csp.spi.arcalgorithm.graph.GraphConstraint;
import jopt.csp.spi.arcalgorithm.graph.NodeArcGraph;
import jopt.csp.spi.arcalgorithm.graph.NodeArcGraphImpl;
import jopt.csp.spi.arcalgorithm.graph.NodeArcGraphListener;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.graph.node.NodeChangeEvent;
import jopt.csp.spi.arcalgorithm.variable.VarFactory;
import jopt.csp.spi.arcalgorithm.variable.Variable;
import jopt.csp.spi.solver.ChoicePointAlgorithm;
import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.CspVariable;
import jopt.csp.variable.CspVariableFactory;

/**
 * Base class for arc-consistency algorithms.  This includes an algorithm
 * that is based on node-arc graph propagation.
 */
public abstract class ArcBasedAcAlg implements ChoicePointAlgorithm, NodeArcGraphListener {
    protected NodeArcGraph graph;
    protected Arc currentArc;
    
    /**
     * Constructor
     */
    public ArcBasedAcAlg() {
        this.graph = new NodeArcGraphImpl(this);
    }
    
    /**
     * Adds a variable that may not a part of a constraint that is posted
     * to be managed by this algorithm
     */
    public void addVariable(CspVariable var) {
    	if (var instanceof Variable) {
		    Variable v = (Variable) var;
		    v.updateGraph(graph);
    	}
    }
    
    /**
     * Adds a constraint to be managed by this algorithm
     */
    public void addConstraint(CspConstraint constraint) {
        GraphConstraint gcons = (GraphConstraint) constraint;
        gcons.associateToGraph(graph);
        gcons.postToGraph();
    }
    
    /**
     * Sets the choicepoint stack associated with this algorithm.
     * Can only be set once.
     */
    public void setChoicePointStack(ChoicePointStack cps) {
    	graph.setChoicePointStack(cps);
    }
    
    /**
     * Returns the variable factory for this algorithm
     */
    public CspVariableFactory getVarFactory(){
        return new VarFactory();
    }
    
    /**
     * Returns the current state of the problem.  This includes
     * arcs, nodes and node domains.
     */
    public Object getProblemState() {
    	return graph.getGraphState();
    }
    
    /**
     * Restores the current state of the problem.  This includes
     * arcs, nodes and node domains.
     */
    public void restoreProblemState(Object state) {
        graph.restoreGraphState(state);
    }
    
    // javadoc inherited
    public void arcAddedEvent(NodeArcGraph graph, Arc arc) {}
    // javadoc inherited
    public void arcRemovedEvent(NodeArcGraph graph, Arc arc) {}
    // javadoc inherited
    public void nodeRemovedEvent(NodeArcGraph graph, Node n) {}
    // javadoc inherited
    public void nodeChangedEvent(NodeArcGraph graph, NodeChangeEvent evt) {}
}