package jopt.csp.spi.arcalgorithm.constraint.set;

import java.util.ArrayList;

import jopt.csp.spi.arcalgorithm.constraint.AbstractConstraint;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinarySetSubsetArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericSetUnionAdvancedFilterArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericSetUnionIntersectFilterArc;
import jopt.csp.spi.arcalgorithm.graph.arc.hyper.HyperSetUnionArc;
import jopt.csp.spi.arcalgorithm.graph.node.SetNode;

/**
 * Constraint representing union( sources ) = target
 */
public class EqUnion extends SetConstraint {
    protected SetVariable intersect;
    protected SetNode intersectNode;
    protected boolean advancedFilter;
    
    /**
     * Creates new union constraint
     * 
     * @param x         First variable used to form union
     * @param y         Second variable used to form union
     * @param z         Target variable that is constrained to be equal to the union of the sources
     */
    public EqUnion(SetVariable x, SetVariable y, SetVariable z) {
        this(new SetVariable[] {x, y}, z, null, false);
    }
    
    /**
     * Creates new union constraint
     * 
     * @param sources           Array of sources that form the union
     * @param target            Target variable that is constrained to be equal to the union of the sources
     */
    public EqUnion(SetVariable sources[], SetVariable target) {
        this(sources, target, null, false);
    }
    
    /**
     * Creates new union constraint that can utilize a variable that is the intersection
     * of X and Y variables to further reduce the target Z than normal
     * 
     * @param x         First variable used to form union
     * @param y         Second variable used to form union
     * @param z         Target variable that is constrained to be equal to the union of the sources
     * @param intersect This variable must be the intersection of X and Y for this constraint to
     *                      work properly
     */
    public EqUnion(SetVariable x, SetVariable y, SetVariable z, SetVariable intersect) {
        this(new SetVariable[] {x, y}, z, intersect, false);
    }
    
    /**
     * Creates new union constraint that has more advanced filtering than normal
     * 
     * @param sources           Array of sources that form the union
     * @param target            Target variable that is constrained to be equal to the union of the sources
     * @param advancedFilter    True if advanced filtering should be performed which will reduce
     *                              domains more than normal but takes longer to run
     */
    public EqUnion(SetVariable sources[], SetVariable target, boolean advancedFilter) {
        this(sources, target, null, advancedFilter);
    }
    
    /**
     * Internal constructor
     * 
     * @param sources           Array of sources that form the union
     * @param target            Target variable that is constrained to be equal to the union of the sources
     * @param intersect         This is an optional variable that must be the intersection of X and Y for 
     *                              this constraint to work properly
     * @param advancedFilter    True if advanced filtering should be performed which will reduce
     *                              domains more than normal but takes longer to run
     */
    protected EqUnion(SetVariable sources[], SetVariable target, SetVariable intersect, boolean advancedFilter) {
        super(sources, new SetVariable[]{target});
        this.intersect = intersect;
        
        if (intersect != null)
            intersectNode = intersect.getNode();
        
        this.advancedFilter = advancedFilter;
    }
    
    /**
     * Creates an array of arcs representing constraint
     */
    protected Arc[] createArcs() {
        SetNode sourceNodes[] = getSetSourceNodes();
        SetNode targetNodes[] = getSetTargetNodes();
        ArrayList arcs = new ArrayList();
        
        // Create arc removing values from source not possible in target
        for (int i=0; i<sourceNodes.length; i++)
            arcs.add(new BinarySetSubsetArc(targetNodes[0], sourceNodes[i], false));
        
        // Create arc forcing target to be union of nodes
        arcs.add(new HyperSetUnionArc(sourceNodes, targetNodes[0]));
        
        // Check if intersection node is supplied
        // and create arc further reducing union based on intersection
        if (intersectNode != null) {
            arcs.add(new GenericSetUnionIntersectFilterArc(targetNodes[0], sourceNodes[0], sourceNodes[1], intersectNode));
        }
        
        // Check if advanced filtering should be performed
        // to further reduced source variables based on changes to the
        // union
        if (advancedFilter) {
            arcs.add(new GenericSetUnionAdvancedFilterArc(targetNodes[0], sourceNodes));
        }
        
        return (Arc[]) arcs.toArray(new Arc[arcs.size()]);
    }
    
    // javadoc inherited from SetConstraint
    protected boolean isViolated() {
        return false;
    }
    
    // javadoc inherited from AbstractConstraint
    protected AbstractConstraint createOpposite() {
        return null;
    }
}