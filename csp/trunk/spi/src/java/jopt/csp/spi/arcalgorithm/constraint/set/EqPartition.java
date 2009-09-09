package jopt.csp.spi.arcalgorithm.constraint.set;

import java.util.ArrayList;
import java.util.Arrays;

import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericSetNullIntersectionArc;
import jopt.csp.spi.arcalgorithm.graph.node.SetNode;

/**
 * Constraint representing partition( sources ) = target.  This is similar to 
 * a union constraint except the source variables cannot share any common
 * values.
 */
public class EqPartition extends EqUnion {
    /**
     * Creates new partition constraint
     * 
     * @param x         First variable used to form union constrained to have no common values with y
     * @param y         Second variable used to form union constrained to have no common values with x
     * @param z         Target variable that is constrained to be equal to the union of the sources
     */
    public EqPartition(SetVariable x, SetVariable y, SetVariable z) {
        this(new SetVariable[] {x, y}, z, false);
    }
    
    /**
     * Creates new partition constraint
     * 
     * @param sources           Array of sources that form the union constrained to have no common values
     * @param target            Target variable that is constrained to be equal to the union of the sources
     */
    public EqPartition(SetVariable sources[], SetVariable target) {
        super(sources, target, null, false);
    }
    
    /**
     * Creates new partition constraint that has more advanced filtering than normal
     * 
     * @param x                 First variable used to form union constrained to have no common values with y
     * @param y                 Second variable used to form union constrained to have no common values with x
     * @param z                 Target variable that is constrained to be equal to the union of the sources
     * @param advancedFilter    True if advanced filtering should be performed which will reduce
     *                              domains more than normal but takes longer to run
     */
    public EqPartition(SetVariable x, SetVariable y, SetVariable z, boolean advancedFilter) {
        this(new SetVariable[] {x, y}, z, advancedFilter);
    }
    
    /**
     * Creates new partition constraint that has more advanced filtering than normal
     * 
     * @param sources           Array of sources that form the union constrained to have no common values
     * @param target            Target variable that is constrained to be equal to the union of the sources
     * @param advancedFilter    True if advanced filtering should be performed which will reduce
     *                              domains more than normal but takes longer to run
     */
    public EqPartition(SetVariable sources[], SetVariable target, boolean advancedFilter) {
        super(sources, target, advancedFilter);
    }
    
    /**
     * Creates an array of arcs representing constraint
     */
    protected Arc[] createArcs() {
        SetNode sourceNodes[] = getSetSourceNodes();
        ArrayList arcs = new ArrayList(Arrays.asList(super.createArcs()));
        arcs.add(new GenericSetNullIntersectionArc(sourceNodes));
        return (Arc[]) arcs.toArray(new Arc[arcs.size()]);
    }
}