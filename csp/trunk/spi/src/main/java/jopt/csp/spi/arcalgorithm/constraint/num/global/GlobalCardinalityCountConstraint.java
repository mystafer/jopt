/*
 * NumAllDiffConstraint.java
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
import jopt.csp.spi.arcalgorithm.graph.arc.global.GlobalCardinalityCountArc;
import jopt.csp.spi.arcalgorithm.graph.arc.global.GlobalCountCardinalityArc;
import jopt.csp.spi.arcalgorithm.graph.node.IntNode;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.arcalgorithm.variable.IntExpr;
import jopt.csp.spi.solver.VariableChangeListener;
import jopt.csp.spi.solver.VariableChangeSource;
import jopt.csp.spi.util.GenericIndex;
import jopt.csp.spi.util.NumberMath;

/**
 * Global constraint the constrains the number of occurrences of a value, 
 * as specified in vals, to the numbers present in the Integer node count, 
 * among all the nodes as expressed in sources array .
 */
public class GlobalCardinalityCountConstraint extends AbstractConstraint {
    
    private NumExpr nsources[];
    private NumExpr targets[];
    private Arc[] arc;
    private int numberType;
    private Number[] vals;
    private IntExpr[] count;
    
    /**
     * Creates a constraint that counts the number of times a value appears among a list of sources.
     * @param sources - A list of sources which values will be constrained to be counted among them
     * @param vals - a list of values to count
     * @param count - the number of times each respective value could show up
     */
    public GlobalCardinalityCountConstraint(NumExpr sources[], Number vals[], IntExpr[] count) {
        nsources = new NumExpr[sources.length+count.length];
    	targets=sources;
        
        for (int i=0; i<sources.length;i++) {
        	nsources[i]=sources[i];
        }
        
        for (int i=0; i<count.length;i++) {
        	nsources[sources.length+i] = count[i];
        }
        
        // determine number type
        for (int i=0; i<sources.length; i++) {
            NumExpr n = sources[i];
            numberType = Math.max(numberType, n.getNumberType());
        }
        this.vals = vals;
        this.count=count;
    }

    /**
     * Creates a constraint that counts the number of times a value appears among a list of sources.
     * @param source A generic source whose values will be constrained to be counted among them
     * @param vals a list of values to count
     * @param count the number of times each respective value could show up
     */
    public GlobalCardinalityCountConstraint(GenericNumExpr source, Number vals[], IntExpr[] count) {
        nsources = new NumExpr[source.getExpressionCount()+count.length];
        nsources = new NumExpr[source.getExpressionCount()];
        for (int i=0; i<source.getExpressionCount(); i++) {
            nsources[i]=source.getNumExpression(i);
            targets[i]=source.getNumExpression(i);
        }
        for (int i=0; i<count.length;i++) {
        	nsources[source.getExpressionCount()+i] = count[i];
        }
        numberType = source.getNumberType();
        this.vals = vals;
        this.count=count;
    }
    
    /**
     * Creates arc that will be posted to graph
     */
    private Arc[] createArc() {
        // create array of nodes to use in arc
        NumNode targets[] = new NumNode[this.targets.length];
        NumNode sources[] = new NumNode[nsources.length];
        IntNode countNodes[] = new IntNode[count.length];
        for (int i=0; i<targets.length; i++) {
            targets[i] = (NumNode) this.targets[i].getNode();
            sources[i] = (NumNode) nsources[i].getNode();
        }
        for (int i=0; i<count.length; i++) {
            sources[i+targets.length] =(NumNode)count[i].getNode();
            countNodes[i] = (IntNode)count[i].getNode();
        }
        
        return new Arc[]{new GlobalCardinalityCountArc(sources, targets,vals,countNodes),new GlobalCountCardinalityArc(targets,vals,countNodes)};
    }
    
    // javadoc inherited from NumConstraint
    public boolean isOverRealInterval() {
        return NumberMath.isRealType(numberType);
    }
    
    // javadoc inherited from NumConstraint
    public void postToGraph() {
        if (graph!=null) {
            for (int i=0; i<nsources.length; i++)
                    nsources[i].updateGraph(graph);
            }
            
            // post arc to graph
            if (arc==null) arc = createArc();
            for (int i=0; i<arc.length; i++) {
                graph.addArc(arc[i]);
            }
    }
    
    //We only want to add Source Arcs of Number or Set constraints
    //Java doc inherited
    public Arc[] getBooleanSourceArcs() {
        ArrayList<Arc[]> arcs = new ArrayList<Arc[]>();
//        arcs.addAll(Arrays.asList(gsource.getBooleanSourceArcs()));
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
        throw new UnsupportedOperationException("Creation of a fragment from of the Global Cardinality Count Constraint is not supported.");
    }

    // javadoc inherited from AbstractConstraint
    protected final AbstractConstraint createOpposite() {
        throw new UnsupportedOperationException("The opposite of GlobalCardinalityCountConstraint is not well-defined and thus not supported.");
    }
    
    /**
     * Checks if all diff constraint is violated
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
            if ((maxCount[i]<count[i].getMin()) ||
                (minCount[i]>count[i].getMax())) {
                return true;
            }
        }
        return false;
    }

//    /**
//     * Checks if all cardinality constraint requirements are within the specified allowed range
//     */
//    private boolean isNecessarilyTrue() {
//        //All sources must be bound for this to be true
//        for (int i=0; i<nsources.length; i++) {
//            if (!nsources[i].isBound()) {
//                return false;
//            }
//        }
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
//            if (!((maxCount[i]>=count[i].getMin()) &&
//                (minCount[i]<=count[i].getMax()))) {
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
