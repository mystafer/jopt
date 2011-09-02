package jopt.csp.spi.arcalgorithm.graph.arc.generic;

import jopt.csp.spi.arcalgorithm.graph.arc.NumArc;
import jopt.csp.spi.arcalgorithm.graph.node.GenericNumNode;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.arcalgorithm.variable.GenericNumConstant;
import jopt.csp.spi.util.GenericIndex;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.variable.CspGenericIndexRestriction;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing summation(Zi, i in [1..100]) < A etc.
 */
public class GenericNumSummationReflexArc extends GenericArc implements NumArc {
    //TODO: this arc does not currently take advantage of delta values
    protected GenericNumConstant gaConst;
    protected Number aConst;
    protected NumNode a;
    protected GenericNumNode ga;
    protected GenericNumNode gz;
    
    protected int nodeType;
    protected int arcType;
    protected CspGenericIndexRestriction sourceIdxRestriction;
    
    private MutableNumber totalMin = new MutableNumber();
    private MutableNumber totalMax = new MutableNumber();
    private MutableNumber minOther = new MutableNumber();
    private MutableNumber maxOther = new MutableNumber();
    private MutableNumber min = new MutableNumber();
    private MutableNumber max = new MutableNumber();
    
    /**
     * Constructor
     *
     * @param   a                       A variable in equation
     * @param   z                       Z variable in equation
     * @param   nodeType                Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType                 Arc relation type (Eq, Lt, Gt, etc.)
     * @param   indexRange              Array of indices that summation is over
     * @param   sourceIdxRestriction    Optional restriction of values placed on source index ranges
     */
    public GenericNumSummationReflexArc(Node a, GenericNumNode z, int nodeType, int arcType, GenericIndex indexRange[],
            CspGenericIndexRestriction sourceIdxRestriction) {
        super(new Node[]{a,z}, new Node[]{z});
        if(a instanceof GenericNumNode)
            this.ga = (GenericNumNode) a;
        else
            this.a = (NumNode) a;
        this.gz = z;
        this.nodeType = nodeType;
        this.arcType = arcType;
        this.sourceIdxRestriction = sourceIdxRestriction;
    }
    
    /**
     * Constructor
     *
     * @param   a                       A variable in equation
     * @param   z                       Z variable in equation
     * @param   nodeType                Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType                 Arc relation type (Eq, Lt, Gt, etc.)
     * @param   indexRange              Array of indices that summation is over
     * @param   sourceIdxRestriction    Optional restriction of values placed on source index ranges
     */
    public GenericNumSummationReflexArc(Number a, GenericNumNode z, int nodeType, int arcType, GenericIndex indexRange[],
            CspGenericIndexRestriction sourceIdxRestriction) {
        super(new Node[]{z}, new Node[]{z});
        this.aConst = NumberMath.toConst(a);
        this.gz = z;
        this.nodeType = nodeType;
        this.arcType = arcType;
        this.sourceIdxRestriction = sourceIdxRestriction;
    }
    
    /**
     * Constructor
     *
     * @param   a                       A variable in equation
     * @param   z                       Z variable in equation
     * @param   nodeType                Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType                 Arc relation type (Eq, Lt, Gt, etc.)
     * @param   indexRange              Array of indices that summation is over
     * @param   sourceIdxRestriction    Optional restriction of values placed on source index ranges
     */
    public GenericNumSummationReflexArc(GenericNumConstant a, GenericNumNode z, int nodeType, int arcType, GenericIndex indexRange[],
            CspGenericIndexRestriction sourceIdxRestriction) {
        super(new Node[]{z}, new Node[]{z});
        this.gaConst = a;
        this.gz = z;
        this.nodeType = nodeType;
        this.arcType = arcType;
        this.sourceIdxRestriction = sourceIdxRestriction;
    }
    
    
    
    /** 
     * Performs actual propagation of changes between x and z nodes
     *
     * @throws PropagationFailureException  If domain of target node becomes empty
     */
    protected void propagateForInitiatingSource(Node src) throws PropagationFailureException {
        totalMin.set(NumberMath.zero(nodeType));
        totalMax.set(NumberMath.zero(nodeType));
        for (int i=0; i<gz.getNodeCount(); i++) {
            gz.setIndicesToNodeOffset(i);
            if(sourceIdxRestriction == null || sourceIdxRestriction.currentIndicesValid()) {
                NumberMath.addNoInvalid(totalMin, ((NumNode)gz.getNode(i)).getMin(), nodeType, totalMin);
                NumberMath.addNoInvalid(totalMax, ((NumNode)gz.getNode(i)).getMax(), nodeType, totalMax);
            }
        }
        
        // retrieve total of min and max a
        Number minA = getMinA();
        Number maxA = getMaxA();
        
        // set min and max for each individual z in range
        for (int i=0; i<gz.getNodeCount(); i++) {
            gz.setIndicesToNodeOffset(i);
            if(sourceIdxRestriction == null || sourceIdxRestriction.currentIndicesValid()) {
                NumNode currentZ = (NumNode) gz.getNodeForIndex();
                // calculate the min and max sum of others
                // (ie  sum of others for i is oi = z0 + z1 + .. + zi-1 + zi+1 .. + zi),
                //
                // then calculate difference of min / max from X and sum of others
                // (ie  z equals total minus sum of other: zi = a - oi)
                //
                // only do calculations necessary for arc type
                if (arcType==GEQ || arcType==GT || arcType==EQ || arcType==NEQ) {
                    NumberMath.subtractNoInvalid(totalMax, currentZ.getMax(), nodeType, maxOther);
                    NumberMath.subtractNoInvalid(minA, maxOther, nodeType, min);
                }
                if (arcType==LEQ || arcType==LT || arcType==EQ || arcType==NEQ) {
                    NumberMath.subtractNoInvalid(totalMin, currentZ.getMin(), nodeType, minOther);
                    NumberMath.subtractNoInvalid(maxA, minOther, nodeType, max);
                }
                
                // update domain of current z node
                switch(arcType) {
                    case GEQ:
                        currentZ.setMin(min);
                        break;
                        
                    case GT:
                        NumberMath.next(min, gz.getPrecision(), min);
                        currentZ.setMin(min);
                        break;
                        
                    case LEQ:
                        currentZ.setMax(max);
                        break;
                        
                    case LT:
                        NumberMath.previous(max, gz.getPrecision(), max);
                        currentZ.setMax(max);
                        break;
                        
                    case EQ:
                        currentZ.setRange(min, max);
                        break;
                        
                    case NEQ:
                        if(anyAIsBound() && (NumberMath.compare(minOther, maxOther, gz.getPrecision(), nodeType) == 0))
                            currentZ.removeValue(min);
                        break;
                }
            }
        }
    }
    
    private Number getMinA() {
        if (gaConst != null) return gaConst.getNumMin();
        if (aConst != null) return aConst;
        if (a != null) return a.getMin();
        return ga.getLargestMin();
    }
    
    private Number getMaxA() {
        if (gaConst != null) return gaConst.getNumMax();
        if (aConst != null) return aConst;
        if (a != null) return a.getMax();
        return ga.getSmallestMax();
    }
    
    private boolean anyAIsBound() {
        if (gaConst != null) return true;
        if (aConst != null) return true;
        if (a != null) return a.isBound();
        return ga.isAnyBound();
    }
    
    // javadoc is inherited
    public void propagate() throws PropagationFailureException {
        propagate(null);
    }

    // javadoc is inherited
    public void propagate(Node src) throws PropagationFailureException {
//        if (useDeltas) {
//            propagateForInitiatingSource(src);
//        }
//        else {
            propagateForInitiatingSource(null);
//        }
    }
}
