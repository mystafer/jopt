package jopt.csp.spi.arcalgorithm.constraint.set;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import jopt.csp.spi.arcalgorithm.constraint.AbstractConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.NumExpr;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.graph.node.SetNode;
import jopt.csp.spi.solver.VariableChangeListener;
import jopt.csp.spi.solver.VariableChangeSource;
import jopt.csp.spi.util.GenericIndex;

/**
 * Base Constraint class for Set Constraints
 */
public abstract class SetConstraint extends AbstractConstraint implements VariableChangeSource {
    private Node booleanNodes[] = null;
    private SetNode sourceNodes[] = null;
    private SetNode targetNodes[] = null;
    
    protected NumExpr exprSource;
    protected SetVariable sources[];
    protected NumExpr exprTarget;
    protected SetVariable targets[];
    protected Arc[] arcs;
    
    /**
     * Constructor for set constraints with a numeric expression for a source
     */
    protected SetConstraint(NumExpr source, SetVariable targets[]) 
    {
        this.exprSource = source;
        this.targets = targets;
    }
    
    /**
     * Constructor for set constraints with a numeric expression for a target
     */
    protected SetConstraint(SetVariable sources[], NumExpr target) 
    {
        // Ensure that at least 1 source node exists
        if (sources.length < 1) {
            throw new IllegalArgumentException("At least 1 source variable must be given");
        }
        
        this.sources = sources;
        this.exprTarget = target;
    }
    
    /**
     * Constructor for set constraints based only on set variables
     */
    protected SetConstraint(SetVariable sources[], SetVariable targets[]) 
    {
        // Ensure that at least 1 source node exists
        if (sources.length <= 1) {
            throw new IllegalArgumentException("At least 1 source variable must be given");
        }
        
        this.sources = sources;
        this.targets = targets;
    }
    
    //  javadoc is inherited
    public boolean isOverRealInterval() {
        return false;
    }
    
    /**
     * Creates an array of arcs representing constraint
     */
    protected abstract Arc[] createArcs();
    
    //  javadoc is inherited
    public void postToGraph() {
        if (arcs == null) arcs = createArcs();
        
        if (targets!=null) {
            for (int i=0; i<targets.length; i++)
                targets[i].updateGraph(graph);
        }
        else {
        	exprTarget.updateGraph(graph);
        }
        
        if (sources!=null) {
            for (int i=0; i<sources.length; i++)
                sources[i].updateGraph(graph);
        }
        else {
        	exprSource.updateGraph(graph);
        }
        
        for (int i=0; i<arcs.length; i++)
            graph.addArc(arcs[i]);
    }
    
    //We only want to add Source Arcs of Number or Set constraints
    //Java doc inherited
    public Arc[] getBooleanSourceArcs() {
        ArrayList arcs = new ArrayList();
        //NOTE: only the below code is currently necessary because set variables do not contain arcs
        arcs.addAll(Arrays.asList(createArcs()));
        return (Arc[])arcs.toArray(new Arc[arcs.size()]);
    }
    
    //  javadoc is inherited
    public Node[] getBooleanSourceNodes() {
        // initialize node array if not yet created
        if (booleanNodes == null) {
            if (sources!=null) {
            	booleanNodes = getSetSourceNodes();
            }
            else {
                Collection c = exprSource.getNodeCollection();
                booleanNodes = (Node[]) c.toArray(new Node[c.size()]);
            }
        }
        
        return booleanNodes;
    }
    
    //  javadoc is inherited
    public void addVariableChangeListener(VariableChangeListener listener) {
        if (sources!=null) {
            for (int i=0; i<sources.length; i++)
                ((VariableChangeSource) sources[i]).addVariableChangeListener(listener);
        }
        else {
        	((VariableChangeSource) exprSource).addVariableChangeListener(listener);
        }
    }
    
    //  javadoc is inherited
    public void removeVariableChangeListener(VariableChangeListener listener) {
        if (sources!=null) {
            for (int i=0; i<sources.length; i++)
                ((VariableChangeSource) sources[i]).removeVariableChangeListener(listener);
        }
        else {
            ((VariableChangeSource) exprSource).removeVariableChangeListener(listener);
        }
    }
    
    /**
     * Returns source nodes corresponding to source variables
     * in constraint for use when building arcs
     */
    protected SetNode[] getSetSourceNodes() {
        if (sourceNodes == null) {
            this.sourceNodes = new SetNode[sources.length];
            for (int i=0; i<sourceNodes.length; i++)
                sourceNodes[i] = sources[i].getNode();
        }
        
        return sourceNodes;
    }
    
    /**
     * Returns target nodes corresponding to target variables
     * in constraint for use when building arcs
     */
    protected SetNode[] getSetTargetNodes() {
        if (targetNodes == null) {
            this.targetNodes = new SetNode[targets.length];
            for (int i=0; i<targetNodes.length; i++)
                targetNodes[i] = targets[i].getNode();
        }
        
        return targetNodes;
    }
    
    // javadoc inherited from AbstractConstraint
    public boolean isViolated(boolean allViolated) {
    	return false;
    }

    // javadoc inherited from AbstractConstraint
    protected AbstractConstraint createConstraintFragment(GenericIndex indices[]) {
        return this;
    }
}