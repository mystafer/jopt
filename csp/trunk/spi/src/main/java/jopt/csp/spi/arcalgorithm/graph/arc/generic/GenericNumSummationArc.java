package jopt.csp.spi.arcalgorithm.graph.arc.generic;

import jopt.csp.spi.arcalgorithm.graph.arc.NumArc;
import jopt.csp.spi.arcalgorithm.graph.node.GenericNumNode;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.graph.node.NodeChangeEvent;
import jopt.csp.spi.arcalgorithm.graph.node.NodeChangeListener;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.solver.ChoicePointEventListener;
import jopt.csp.spi.solver.ReversibleDoubleArray;
import jopt.csp.spi.solver.ReversibleFloatArray;
import jopt.csp.spi.solver.ReversibleIntArray;
import jopt.csp.spi.solver.ReversibleLongArray;
import jopt.csp.spi.solver.ReversibleNumberArray;
import jopt.csp.spi.util.GenericIndex;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.variable.CspGenericIndexRestriction;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Z < summation(Ai, i in [1..100]) etc.
 */
public class GenericNumSummationArc extends GenericArc implements NumArc, NodeChangeListener, ChoicePointEventListener {
    protected GenericNumNode ga;
    protected NumNode tempNode;
    protected NumNode z;
    protected GenericNumNode gz;
    
    protected int nodeType;
    protected int arcType;
    protected CspGenericIndexRestriction sourceIdxRestriction;
    
    private ReversibleNumberArray maxDeltas;
    private ReversibleNumberArray minDeltas;
    private int minModifiedOffset;
    private int maxModifiedOffset;
    private boolean needsRecalc;
    
    private MutableNumber v1 = new MutableNumber();
    private MutableNumber v2 = new MutableNumber();
    
    /**
     * Constructor
     *
     * @param   a                       A variable in equation
     * @param	tempNode                An intermediate node used in calculations
     * @param   z                       Z variable in equation
     * @param   nodeType                Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType                 Arc relation type (Eq, Lt, Gt, etc.)
     * @param   indexRange              Array of indices that summation is over
     * @param   sourceIdxRestriction    Optional restriction of values placed on source index ranges
     */
    public GenericNumSummationArc(GenericNumNode a, NumNode tempNode, Node z, int nodeType, int arcType, GenericIndex indexRange[],
            CspGenericIndexRestriction sourceIdxRestriction) 
    {
        super(new Node[]{a}, new Node[]{tempNode, z});
        this.ga = a;
        this.tempNode = tempNode;
        if(z instanceof GenericNumNode)
            this.gz = (GenericNumNode) z;
        else
            this.z = (NumNode) z;
        this.nodeType = nodeType;
        this.arcType = arcType;
        this.sourceIdxRestriction = sourceIdxRestriction;
        
        initDeltaArrays(ga.getNodeCount());
        minModifiedOffset = -1;
        maxModifiedOffset = -1;
        a.addInternalRangeChangeListener(this);
        a.setChoicePointEventListener(this);
        needsRecalc = true;
    }
    
    /**
     * Initialize the arrays responsible for tracking changes to the nodes that
     * comprise the summation.
     */
    private void initDeltaArrays(int size) {
        switch(nodeType) {
            case INTEGER:
                minDeltas = new ReversibleIntArray(size);
                maxDeltas = new ReversibleIntArray(size);
                break;
            case LONG:
                minDeltas = new ReversibleLongArray(size);
                maxDeltas = new ReversibleLongArray(size);
                break;
            case FLOAT:
                minDeltas = new ReversibleFloatArray(size);
                maxDeltas = new ReversibleFloatArray(size);
                break;
            default:
                minDeltas = new ReversibleDoubleArray(size);
                maxDeltas = new ReversibleDoubleArray(size);
        }
    }
    
    /** 
     * Performs actual propagation of changes between a (x) and z nodes
     *
     * @throws PropagationFailureException  If domain of target node becomes empty
     */
    protected void propagateBounds() throws PropagationFailureException {
            switch(arcType) {
                case GEQ:
                calcMinSum(ga);
                setMinZ(tempNode.getMin());
                    break;
                    
                case GT:
                calcMinSum(ga);
                NumberMath.next(tempNode.getMin(), z.getPrecision(), v1);
                setMinZ(v1);
                    break;
                    
                case LEQ:
                calcMaxSum(ga);
                setMaxZ(tempNode.getMax());
                    break;
                    
                case LT:
                calcMaxSum(ga);
                NumberMath.previous(tempNode.getMax(), z.getPrecision(), v1);
                setMaxZ(v1);
                    break;
                    
                case EQ:
                calcTotalSum(ga);
                setRangeZ(tempNode.getMin(), tempNode.getMax());
                    break;
                    
                case NEQ:
                Number neqSum = calcNeqSum(ga);
                    if (neqSum!=null) {
                        tempNode.removeValue(neqSum);
                        removeValueZ(neqSum);
                    }
        }
    }
    
    /**
     * Calculates range for Z summation
     */
    private void calcTotalSum(Node src) throws PropagationFailureException {
        // if no source is specified, calculate min and max value over entire x range
        if (src == null || (this.minModifiedOffset == -1 || this.maxModifiedOffset == -1) || needsRecalc) {
            v1.set(NumberMath.zero(nodeType));
            v2.set(NumberMath.zero(nodeType));
            for (int i=0; i<ga.getNodeCount(); i++) {
                ga.setIndicesToNodeOffset(i);
                if(sourceIdxRestriction == null || sourceIdxRestriction.currentIndicesValid()) {
                    NumberMath.addNoInvalid(v1, ((NumNode)ga.getNode(i)).getMin(), nodeType, v1);
                    NumberMath.addNoInvalid(v2, ((NumNode)ga.getNode(i)).getMax(), nodeType, v2);
                }
            }
            
            needsRecalc = false;
        }
        // if a source is specified, adjust the min based on the x nodes that changed
        else {
            v1.set(tempNode.getMin());
            v2.set(tempNode.getMax());
            for (int i=this.minModifiedOffset; i<=this.maxModifiedOffset; i++) {
                ga.setIndicesToNodeOffset(i);
                if(sourceIdxRestriction == null || sourceIdxRestriction.currentIndicesValid()) {
                    NumberMath.addNoInvalid(v1, minDeltas.getValue(i), nodeType, v1);
                    NumberMath.addNoInvalid(v2, maxDeltas.getValue(i), nodeType, v2);
                }
            }
        }
        
        this.clearDeltas();
        tempNode.setRange(v1, v2);
    }
    
    /**
     * Calculates minimum summation value
     */
    private void calcMinSum(Node src) throws PropagationFailureException {
        // if no source is specified, calculate min value over entire x range
        if (src == null || (this.minModifiedOffset == -1 || this.maxModifiedOffset == -1) || needsRecalc) {
            v1.set(NumberMath.zero(nodeType));
            for (int i=0; i<ga.getNodeCount(); i++) {
                ga.setIndicesToNodeOffset(i);
                if(sourceIdxRestriction == null || sourceIdxRestriction.currentIndicesValid()) {
                    NumberMath.addNoInvalid(v1, ((NumNode)ga.getNode(i)).getMin(), nodeType, v1);
                }
            }
            
            needsRecalc = false;
        }
        // if a source is specified, adjust the min based on the x nodes that changed
        else {
            v1.set(tempNode.getMin());
            for (int i=this.minModifiedOffset; i<=this.maxModifiedOffset; i++) {
                ga.setIndicesToNodeOffset(i);
                if(sourceIdxRestriction == null || sourceIdxRestriction.currentIndicesValid()) {
                    NumberMath.addNoInvalid(v1, minDeltas.getValue(i), nodeType, v1);
                }
            }
        }
        
        this.clearDeltas();
        tempNode.setMin(v1);
    }
    
    /**
     * Calculates maximum summation value
     */
    private void calcMaxSum(Node src) throws PropagationFailureException {
        // if no source is specified, calculate max value over entire x range
        if (src == null || (this.minModifiedOffset == -1 || this.maxModifiedOffset == -1) || needsRecalc) {
            v1.set(NumberMath.zero(nodeType));
            for (int i=0; i<ga.getNodeCount(); i++) {
                ga.setIndicesToNodeOffset(i);
                if(sourceIdxRestriction == null || sourceIdxRestriction.currentIndicesValid()) {
                    NumberMath.addNoInvalid(v1, ((NumNode)ga.getNode(i)).getMax(), nodeType, v1);
                }
            }
            
            needsRecalc = false;
        }
        // if a source is specified, adjust the max based on the x nodes that changed
        else {
            v1.set(tempNode.getMax());
            for (int i=this.minModifiedOffset; i<=this.maxModifiedOffset; i++) {
                ga.setIndicesToNodeOffset(i);
                if(sourceIdxRestriction == null || sourceIdxRestriction.currentIndicesValid()) {
                    NumberMath.addNoInvalid(v1, maxDeltas.getValue(i), nodeType, v1);
                }
            }
        }
        
        this.clearDeltas();
        tempNode.setMax(v1);
    }

    /**
     * Calculates summation to use during an NEQ operation.  Returns
     * null if any variable in summation is not bound
     */
    private MutableNumber calcNeqSum(Node src) {
        // calculate max value for z over specified range
        // if no source is specified, calculate sum value over entire x range
        if (src == null || (this.minModifiedOffset == -1 || this.maxModifiedOffset == -1) || needsRecalc) {
            v1.set(NumberMath.zero(nodeType));
            for (int i=0; i<ga.getNodeCount(); i++) {
                ga.setIndicesToNodeOffset(i);
                if(sourceIdxRestriction == null || sourceIdxRestriction.currentIndicesValid()) {
                    NumNode currentNode = (NumNode)ga.getNode(i);
                    if(!currentNode.isBound()) {
                        return null;
                    }
                    NumberMath.addNoInvalid(v1, currentNode.getMax(), nodeType, v1);
                }
            }
            
            needsRecalc = false;
        }
        // if a source is specified, adjust the sum based on the x nodes that changed
        else {
            v1.set(tempNode.getMax());
            for (int i=this.minModifiedOffset; i<=this.maxModifiedOffset; i++) {
                ga.setIndicesToNodeOffset(i);
                if(sourceIdxRestriction == null || sourceIdxRestriction.currentIndicesValid()) {
                    if(!ga.getNode(i).isBound()) {
                        return null;
                    }
                    NumberMath.addNoInvalid(v1, maxDeltas.getValue(i), nodeType, v1);
                }
            }
        }
        
        this.clearDeltas();
        return v1;
    }
    
    /**
     * Clear the change tracking arrays and reset the change tracking indicies
     */
    private void clearDeltas() {
        maxDeltas.clear();
        minDeltas.clear();
        minModifiedOffset = -1;
        maxModifiedOffset = -1;
    }
    
    // javadoc is inherited
    public void nodeChange(NodeChangeEvent ev) {
        int idx = ((Integer) (ev.getCallbackValue())).intValue();
        if (minModifiedOffset == -1 || idx < minModifiedOffset) {
            minModifiedOffset = idx;
        }
        if (maxModifiedOffset == -1 || idx > maxModifiedOffset) {
            maxModifiedOffset = idx; 
        }
        if(ev.getMinChange()!=null) {
            minDeltas.adjValue(idx, ev.getMinChange());
        }
        if(ev.getMaxChange()!=null) {
            maxDeltas.adjValue(idx, ev.getMaxChange());
        }
    }
    
    // javadoc is inherited
    public void choicePointPop() {
        needsRecalc = true;
    }
    
    // javadoc is inherited
    public void choicePointPush() {
        needsRecalc = true;
    }
    
    /**
     * Sets the minimum value for the current Z
     */
    private void setMinZ(Number n) throws PropagationFailureException {
        if (z != null) {
            z.setMin(n);
        }
        else {
            for (int i=0; i<gz.getNodeCount(); i++) {
                ((NumNode) gz.getNode(i)).setMin(n);
            }
        }
    }
    
    /**
     * Sets the maximum value for the current Z
     */
    private void setMaxZ(Number n) throws PropagationFailureException {
        if (z != null) {
            z.setMax(n);
        }
        else {
            for (int i=0; i<gz.getNodeCount(); i++) {
                ((NumNode) gz.getNode(i)).setMax(n);
            }
        }
    }
    
    /**
     * Sets the domain range for the current Z
     */
    protected void setRangeZ(Number s, Number e) throws PropagationFailureException {
        if (z != null) {
            z.setRange(s,e);
        }
        else if (gz != null){
            for (int i=0; i<gz.getNodeCount(); i++) {
                ((NumNode) gz.getNode(i)).setRange(s,e);
            }
        }
    }
    
    /**
     * Removes a specific value from the domain for the current Z
     */
    protected void removeValueZ(Number n) throws PropagationFailureException {
        if (z != null) {
            z.removeValue(n);
        }
        else if (gz != null){
            for (int i=0; i<gz.getNodeCount(); i++) {
                ((NumNode) gz.getNode(i)).removeValue(n);
            }
        }
    }
    
    // javadoc is inherited
    public void propagate() throws PropagationFailureException {
        propagateBounds();
    }
    
    // javadoc is inherited
    public void propagate(Node src) throws PropagationFailureException {
        propagateBounds();
    }

    public int getComplexity() {
        if (this.useDeltas) {
            return 2;  // TODO: Is this a good thing? It is putting itself in the hyper-arc category instead of the generic-arc category
        }
        else {
            return super.getComplexity();
        }
    }
}
