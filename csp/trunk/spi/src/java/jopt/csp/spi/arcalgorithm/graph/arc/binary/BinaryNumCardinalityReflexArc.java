package jopt.csp.spi.arcalgorithm.graph.arc.binary;

import java.util.Iterator;

import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.arcalgorithm.graph.node.SetNode;
import jopt.csp.spi.util.DomainChangeType;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing cardinality(Z) = X
 */
public class BinaryNumCardinalityReflexArc extends BinaryNumArc {
    private Integer constSource;
    
    /**
     * Constructor
     *
     * @param   x          Source node in equation
     * @param   z          target expression in equation
     * @param   arcType    Arc relation type (Eq, Lt, Gt, etc.)
     */
    public BinaryNumCardinalityReflexArc(NumNode x, SetNode z, int arcType) {
        super(x, z);
        this.arcType = arcType;
        this.sourceDependency = DomainChangeType.DOMAIN;
    }

    /**
     * Constructor
     *
     * @param   x          Constant source set in equation
     * @param   z          target expression in equation
     * @param   arcType    Arc relation type (Eq, Lt, Gt, etc.)
     */
    public BinaryNumCardinalityReflexArc(Integer x, SetNode z, int arcType) {
        super(z, z);
        this.arcType = arcType;
        this.sourceDependency = DomainChangeType.DOMAIN;
        this.constSource = x;
    }

    protected void propagateBounds() throws PropagationFailureException {
        SetNode z = (SetNode) target;
        
        // determine min and max for target
        int min = 0;
        int max = 0;
        boolean xBound = false;
        if (constSource != null) {
            min = constSource.intValue();
            max = min;
            xBound = true;
        }
        else {
            NumNode x = (NumNode) source;
            min = NumberMath.intCeil(x.getMin());
            max = NumberMath.intFloor(x.getMax());
            xBound = x.isBound();
        }

        switch(arcType) {
            case GEQ:
                // make sure minimum number of values exist
                if (z.getPossibleCardinality() < min)
                    throw new PropagationFailureException();
                
                // require all values if z possible is equal to min
                else if (z.getPossibleCardinality() == min)
                    requireAll(z);
                break;

            case GT:
                // make sure minimum number of values exist
                if (z.getPossibleCardinality() <= min)
                    throw new PropagationFailureException();
                
                // require all values if z possible is equal to min
                else if (z.getPossibleCardinality() == min + 1)
                    requireAll(z);
                break;

            case LEQ:
                // make sure maximum number of values are not exceeded
                if (z.getRequiredCardinality() > max)
                    throw new PropagationFailureException();

                // if required values have reached max possible, remove all others
                else if (z.getRequiredCardinality() == max)
                    removeAllNotRequired(z);
                break;

            case LT:
                // make sure maximum number of values are not exceeded
                if (z.getRequiredCardinality() >= max)
                    throw new PropagationFailureException();

                // if required values have reached max possible, remove all others
                else if (z.getRequiredCardinality() == max - 1)
                    removeAllNotRequired(z);
                break;

            case EQ:
                // make sure minimum number of values exist
                if (z.getPossibleCardinality() < min)
                    throw new PropagationFailureException();
                
                // require all values if z possible is equal to min
                else if (z.getPossibleCardinality() == min)
                    requireAll(z);
                
                // make sure maximum number of values are not exceeded
                if (z.getRequiredCardinality() > max)
                    throw new PropagationFailureException();

                // if required values have reached max possible, remove all others
                else if (z.getRequiredCardinality() == max)
                    removeAllNotRequired(z);
                break;

            case NEQ:
                // make sure number of values assigned to z are not equal to number of values
                // allowed in x
                if (z.isBound() && xBound && z.getRequiredCardinality() == min)
                    throw new PropagationFailureException();
                break;
        }
    }
    
    /**
     * Helper function to all possible values to required set
     */
    private void requireAll(SetNode z) throws PropagationFailureException {
    	Iterator valIter = z.getPossibleSet().iterator();
        while (valIter.hasNext())
            z.addRequired(valIter.next());
    }

    /**
     * Helper function to remove all possible values not yet required
     */
    private void removeAllNotRequired(SetNode z) throws PropagationFailureException {
        Iterator valIter = z.getPossibleSet().iterator();
        while (valIter.hasNext()) {
            Object val = valIter.next();
            if (!z.isRequired(val))
            	z.removePossible(val);
        }
    }

    protected void propagateEqArcConsistent() throws PropagationFailureException {
        // not necessary for real numbers
    }
        
    protected void propagateEqArcConsistentNoDeltas() throws PropagationFailureException {
        // not necessary for real numbers
    }

    protected void targetExprToString(StringBuffer buf) {
        buf.append("cardinality Z(");
        buf.append(target);
        buf.append(") ");
    }
    
    protected void exprToString(StringBuffer buf) {
        buf.append(" X(");
        buf.append(source);
        buf.append(")");
    }

}

