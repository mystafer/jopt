/*
 * GlobalCardinalityConstraint.java
 * 
 * Created on Jul 28, 2005
 */
package jopt.csp.spi.arcalgorithm.constraint.num.global;

import java.util.ArrayList;
import java.util.Arrays;

import jopt.csp.spi.arcalgorithm.constraint.AbstractConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.GenericNumExpr;
import jopt.csp.spi.arcalgorithm.constraint.num.NumExpr;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.arc.global.GlobalCardinalityArc;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.solver.VariableChangeListener;
import jopt.csp.spi.solver.VariableChangeSource;
import jopt.csp.spi.util.GenericIndex;
import jopt.csp.spi.util.NumberMath;

/**
 * A global constraint that enforces a minimum and maximum cardinality of certain values among a set of sources.
 * 
 */
public class GlobalCardinalityConstraint extends AbstractConstraint {
    
    private NumExpr nsources[];
    private Arc arc;
    private int numberType;
    private Number[] vals;
    private int[] lb;
    private int[] ub;
    
   /**
    * This will construct a global cardinality constraint among the source expressions, binding the number of times 
    * each val from vals shows up to be between a lower bound lb and upper bound ub
    * @param sources - A list of num expressions of which we are concerned with how many values it contains among them
    * @param vals - a list of values for which we are concerned how often they appear among the 
    * @param lb - the minimum number of times the parallel value from vals shows up among sources
    * @param ub - the maximum number of times the parallel value from vals shows up among sources
    */
    public GlobalCardinalityConstraint(NumExpr sources[], Number vals[], int lb[], int ub[]) {
        nsources = sources;
        // determine number type
        for (int i=0; i<sources.length; i++) {
            NumExpr n = sources[i];
            numberType = Math.max(numberType, n.getNumberType());
        }
        this.vals = vals;
        this.lb=lb;
        this.ub=ub;
    }
    
    /**
     * This will construct a global cardinality constraint among the source expressions, binding the number of times 
     * each val from vals shows up to be between a lower bound lb and upper bound ub
     * Notice we remember that this is derived from a generic source, but we treat it as a normal gcc.  The only time that the genericness
     * affects performance is that we want to retain the ability to generate a gcc based on only a portion of values.
     * @param source A generic num expression of which we are concerned with how many of each value it contains among them
     * @param vals a list of values for which we are concerned how often they appear among the 
     * @param lb the minimum number of times the parallel value from vals shows up among sources
     * @param ub the maximum number of times the parallel value from vals shows up among sources
     */
    public GlobalCardinalityConstraint(GenericNumExpr source, Number vals[], int lb[], int ub[]) {
        nsources = new NumExpr[source.getExpressionCount()];
        for (int i=0; i<source.getExpressionCount(); i++) {
            nsources[i] = source.getNumExpression(i);
        }
        numberType = source.getNumberType();
        this.vals = vals;
        this.lb=lb;
        this.ub=ub;
    }
    
    /**
     * Creates arc that will be posted to graph
     */
    private Arc createArc() {
        // create array of nodes to use in arc
        NumNode nodes[] = new NumNode[nsources.length];
        for (int i=0; i<nodes.length; i++)
            nodes[i] = (NumNode) nsources[i].getNode();
        
        return new GlobalCardinalityArc(nodes,vals,lb,ub);
    }
    
    // javadoc inherited from NumConstraint
    public boolean isOverRealInterval() {
        return NumberMath.isRealType(numberType);
    }
    
    // javadoc inherited from NumConstraint
    public void postToGraph() {
        if (graph!=null) {
            // add nodes to graph
            for (int i=0; i<nsources.length; i++)
                nsources[i].updateGraph(graph);
            
            // post arc to graph
            if (arc==null) arc = createArc();
            graph.addArc(arc);
        }
    }
    
    //We only want to add Source Arcs of Number or Set constraints
    //Java doc inherited
    public Arc[] getBooleanSourceArcs() {
        ArrayList<Arc> arcs = new ArrayList<Arc>();
        arcs.add(createArc());
        return (Arc[])arcs.toArray(new Arc[0]);
    }
    
    // javadoc inherited from NumConstraint
    public Node[] getBooleanSourceNodes() {
            // create array of nodes to use in arc
            NumNode nodes[] = new NumNode[nsources.length];
            for (int i=0; i<nodes.length; i++)
                nodes[i] = (NumNode) nsources[i].getNode();
            
            return nodes;
    }
    
    //  javadoc is inherited
    public void addVariableChangeListener(VariableChangeListener listener) {
        for (int i=0; i< nsources.length; i++) {
            ((VariableChangeSource) nsources[i]).addVariableChangeListener(listener);
        }
    }
    
    //  javadoc is inherited
    public void removeVariableChangeListener(VariableChangeListener listener) {
        for (int i=0; i< nsources.length; i++) {
            ((VariableChangeSource) nsources[i]).removeVariableChangeListener(listener);
        }
    }
    
    
    // javadoc inherited from AbstractConstraint
    protected final AbstractConstraint createConstraintFragment(GenericIndex indices[]) {
        throw new UnsupportedOperationException("Creation of a fragment from of the Global Cardinality Constraint is not supported.");

    }

    // javadoc inherited from AbstractConstraint
    protected final AbstractConstraint createOpposite() { 
        throw new UnsupportedOperationException("The opposite of GlobalCardinalityConstraint is not well-defined and thus not supported.");
    }
    
    /**
     * Checks if gcc constraint is violated
     */
    private boolean isViolated() {
        int minCount[] = new int[vals.length];
        int maxCount[] = new int[vals.length];
        for (int i=0; i<vals.length; i++) {
            if(nsources[i].isBound()) {
                minCount[Arrays.binarySearch(vals, nsources[i].getNumMin())]++;
            }
            Number val = nsources[i].getNumMin();
            while(val.doubleValue()<nsources[i].getNumMin().doubleValue()){
                maxCount[Arrays.binarySearch(vals, val)]++;
                val = nsources[i].getNextHigher(val);
            }
            maxCount[Arrays.binarySearch(vals, nsources[i].getNumMax())]++;
        }
        for (int i=0; i<vals.length; i++) {
            if ((maxCount[i]<lb[i]) ||
                (minCount[i]>ub[i])) {
                return true;
            }
        }
        return false;
    }

//    /**
//     * Checks if all cardinality constraint requirements are within the specified allowed range
//     */
//    private boolean allCardsInRange() {
//        int minCount[] = new int[vals.length];
//        int maxCount[] = new int[vals.length];
//        for (int i=0; i<vals.length; i++) {
//            if(nsources[i].isBound()) {
//                minCount[Arrays.binarySearch(vals, nsources[i].getNumMin())]++;
//            }
//            Number val = nsources[i].getNumMin();
//            while(val.doubleValue()<nsources[i].getNumMin().doubleValue()){
//                maxCount[Arrays.binarySearch(vals, val)]++;
//                val = nsources[i].getNextHigher(val);
//            }
//            maxCount[Arrays.binarySearch(vals, nsources[i].getNumMax())]++;
//        }
//        boolean allCardsInRange = true;
//        for (int i=0; i<vals.length; i++) {
//            if (!((maxCount[i]>=lb[i]) &&
//                (minCount[i]<=ub[i]))) {
//                allCardsInRange = false;
//                break;
//            }
//        }
//        return allCardsInRange;
//     }

    // javadoc inherited from TwoVarConstraint
    public boolean isViolated(boolean allViolated) {
        return isViolated();
    }
}
