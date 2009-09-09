/*
 * NumAllDiffConstraint.java
 * 
 * Created on Jul 28, 2005
 */
package jopt.csp.spi.arcalgorithm.constraint.num.global;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import jopt.csp.spi.arcalgorithm.constraint.AbstractConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.GenericNumExpr;
import jopt.csp.spi.arcalgorithm.constraint.num.NumExpr;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.arc.global.GenericNumAllDiffArc;
import jopt.csp.spi.arcalgorithm.graph.arc.global.GenericNumAllSameArc;
import jopt.csp.spi.arcalgorithm.graph.node.GenericNumNode;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.solver.VariableChangeListener;
import jopt.csp.spi.solver.VariableChangeSource;
import jopt.csp.spi.util.GenericIndex;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumberMath;

/**
 * Generic constraint that will ensure all variables that are constrained will
 * be bound to a single value.  If a generic variable is passed, the constraint
 * will apply to all of the generic variables.
 */
public class NumAllDiffConstraint extends AbstractConstraint {
    MutableNumber min = new MutableNumber();
    MutableNumber max = new MutableNumber();
    
    private NumExpr nsources[];
    private GenericNumExpr gsource;
    private Arc arc;
    private boolean opposite;
    private int numberType;
    
    public NumAllDiffConstraint(NumExpr sources[]) {
        nsources = sources;

        // determine number type
        for (int i=0; i<sources.length; i++) {
            NumExpr n = sources[i];
            numberType = Math.max(numberType, n.getNumberType());
        }
    }
    
    public NumAllDiffConstraint(GenericNumExpr source) {
        gsource = source;
        numberType = source.getNumberType();
    }
    
    /**
     * Creates arc that will be posted to graph
     */
    private Arc createArc() {
        if (gsource != null) {
            if (opposite)
                return new GenericNumAllSameArc((GenericNumNode) gsource.getNode());
            else
            	return new GenericNumAllDiffArc((GenericNumNode) gsource.getNode());
        }
        else {
            // create array of nodes to use in arc
            NumNode nodes[] = new NumNode[nsources.length];
            for (int i=0; i<nodes.length; i++)
                nodes[i] = (NumNode) nsources[i].getNode();
            
            if (opposite)
                return new GenericNumAllSameArc(nodes);
            else
            	return new GenericNumAllDiffArc(nodes);
        }
    }
    
    // javadoc inherited from NumConstraint
    public boolean isOverRealInterval() {
        return NumberMath.isRealType(numberType);
    }
    
    // javadoc inherited from NumConstraint
    public void postToGraph() {
        if (graph!=null) {
            // add nodes to graph
            if (gsource != null) {
                gsource.updateGraph(graph);
            }
            else {
                for (int i=0; i<nsources.length; i++)
                    nsources[i].updateGraph(graph);
            }
            
            // post arc to graph
            if (arc==null) arc = createArc();
            graph.addArc(arc);
        }
    }
    
    //We only want to add Source Arcs of Number or Set constraints
    //Java doc inherited
    public Arc[] getBooleanSourceArcs() {
        ArrayList arcs = new ArrayList();
        arcs.addAll(Arrays.asList(gsource.getBooleanSourceArcs()));
        arcs.add(createArc());
        return (Arc[])arcs.toArray(new Arc[0]);
    }
    
    // javadoc inherited from NumConstraint
    public Node[] getBooleanSourceNodes() {
        if (gsource != null) {
            return new Node[]{gsource.getNode()};
        }
        else{
            // create array of nodes to use in arc
            NumNode nodes[] = new NumNode[nsources.length];
            for (int i=0; i<nodes.length; i++)
                nodes[i] = (NumNode) nsources[i].getNode();
            
            return nodes;
        }
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
        if (gsource == null) {
            return this;
        }
        else {
            // determine if and index is used in the expression is required
            boolean createFrag = false;
            for (int i=0; i<indices.length; i++) {
                if (gsource.containsIndex(indices[i])) {
                	createFrag = true;
                    break;
                }
            }
            
            // if fragment is not necessary, return this constraint
            if (!createFrag) {
            	return this;
            }
            else {
            	NumExpr fragExpr = gsource.createFragment(indices);
                
                // check if fragment is still a generic
                NumAllDiffConstraint fragCons = null;
                if (fragExpr instanceof GenericNumExpr)
                	fragCons = new NumAllDiffConstraint((GenericNumExpr) fragExpr);
                else
                    fragCons = new NumAllDiffConstraint(new NumExpr[]{fragExpr});
                
                fragCons.opposite = this.opposite;
                
                return fragCons;
            }
        }
    }

    // javadoc inherited from AbstractConstraint
    protected final AbstractConstraint createOpposite() {
        // duplicate constraint
        NumAllDiffConstraint constraint = null;
        if (gsource != null)
            constraint = new NumAllDiffConstraint(gsource);
        else
            constraint = new NumAllDiffConstraint(nsources);
        
        // flip opposite flag
        constraint.opposite = !this.opposite;
        
    	return constraint;
    }
    
    /**
     * Checks if all diff constraint is violated
     */
    private boolean isAllDiffViolated() {
            // process generic class
            if (gsource!=null) {
                // build set of values that are bound in variables
                HashSet boundValues = new HashSet();
                for (int i=0; i<gsource.getExpressionCount(); i++) {
                    NumExpr expr = gsource.getNumExpression(i);
                    
                    // all expressions must be bound for this check to work
                    if (!expr.isBound()) return false;
                    
                    // value cannot already be bound to another variable
                    // or constraint is violated
                Number val = expr.getNumMin();
                    if (boundValues.contains(val)) return true;
                    
                    // add value to list of bound values
                    boundValues.add(val);
                }
            }
        
        // process array of individual values
        else {
            // build set of values that are bound in variables
            HashSet boundValues = new HashSet();
            for (int i=0; i<nsources.length; i++) {
                NumExpr expr = nsources[i];
                
                // all expressions must be bound for this check to work
                if (!expr.isBound()) return false;
                
                // value cannot already be bound to another variable
                // or constraint is violated
                Number val = expr.getNumMin();
                if (val instanceof MutableNumber){
                    val = ((MutableNumber)val).toConst();
                }
                boolean contained = boundValues.contains(val);
                if (contained) return true;
                
                // add value to list of bound values
                boundValues.add(val);
            }
        }

        return false;
    }

    /**
     * Checks if all same constraint is violated
     */
    private boolean isAllSameViolated() {
            // process generic class
            if (gsource!=null) {
                // determine minimum and maximum values that all variables
                // must fall between
            min.setInvalid(true);
                for (int i=0; i<gsource.getExpressionCount(); i++) {
                    NumExpr expr = gsource.getNumExpression(i);
                    
                    // check if this is first expression
                if (min.isInvalid()) {
                	min.set(expr.getNumMin());
                    max.set(expr.getNumMax());
                }
                else {
                    NumberMath.max(min, expr.getNumMin(), min);
                    NumberMath.min(max, expr.getNumMin(), max);
                }
            }
            
            // ensure all expressions fall within min and max
            for (int i=0; i<gsource.getExpressionCount(); i++) {
                NumExpr expr = gsource.getNumExpression(i);
                double precision = expr.getPrecision();
                
                if (NumberMath.compare(expr.getNumMin(), max, precision, numberType) > 0 ||
                    NumberMath.compare(expr.getNumMax(), min, precision, numberType) < 0)
                {
                	return true;
                }
            }
        }
        
        // process array of individual values
        else {
            // determine minimum and maximum values that all variables
            // must fall between
            min.setInvalid(true);
            for (int i=0; i<nsources.length; i++) {
                NumExpr expr = nsources[i];
                
                // check if this is first expression
                if (min.isInvalid()) {
                    min.set(expr.getNumMin());
                    max.set(expr.getNumMax());
                }
                else {
                    NumberMath.max(min, expr.getNumMin(), min);
                    NumberMath.min(max, expr.getNumMin(), max);
                }
            }
            
            // ensure all expressions fall within min and max
            for (int i=0; i<nsources.length; i++) {
                NumExpr expr = nsources[i];
                double precision = expr.getPrecision();
                
                if (NumberMath.compare(expr.getNumMin(), max, precision, numberType) > 0 ||
                    NumberMath.compare(expr.getNumMax(), min, precision, numberType) < 0)
                {
                    return true;
                }
            }
        }

        return false;
    }

    // javadoc inherited from TwoVarConstraint
    public boolean isViolated(boolean allViolated) {
        if (opposite)
            return isAllSameViolated();
        else
            return isAllDiffViolated();
    }
}
