package jopt.csp.spi.arcalgorithm.constraint.num;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import jopt.csp.spi.arcalgorithm.constraint.AbstractConstraint;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericNumSummationArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericNumSummationReflexArc;
import jopt.csp.spi.arcalgorithm.graph.node.DoubleNode;
import jopt.csp.spi.arcalgorithm.graph.node.FloatNode;
import jopt.csp.spi.arcalgorithm.graph.node.GenericNumNode;
import jopt.csp.spi.arcalgorithm.graph.node.IntNode;
import jopt.csp.spi.arcalgorithm.graph.node.LongNode;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.arcalgorithm.variable.GenericNumConstant;
import jopt.csp.spi.util.GenericIndex;
import jopt.csp.spi.util.GenericIndexManager;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.variable.CspGenericIndex;
import jopt.csp.variable.CspGenericIndexRestriction;

/**
 * Constraint representing summation(Ai, i in [1..100]) = Z etc.
 */
public class SummationConstraint extends TwoVarConstraint {
    private MutableNumber minSum = new MutableNumber();
    private MutableNumber maxSum = new MutableNumber();
    private CspGenericIndexRestriction sourceIdxRestriction;
    
    /**
     * Creates a constraint relating variable Z to a summation of values in the generic
     * variable A
     * 
     * @param aexpr                     Generic variable summation is over
     * @param indexRange                Indices within generic variable A that summation is over
     * @param sourceIdxRestriction      Optional restriction of values placed on source index ranges
     * @param zexpr                     Target variable that summation is related to
     * @param constraintType            Constraint relation type (Eq, Lt, Gt, etc.)
     */
    public SummationConstraint(GenericNumExpr aexpr, GenericIndex indexRange[],
            CspGenericIndexRestriction sourceIdxRestriction, 
            NumExpr zexpr, int constraintType) 
    {
        super(aexpr, zexpr, indexRange, constraintType);
        this.sourceIdxRestriction = sourceIdxRestriction;
        
        if (gaexpr==null)
            throw new IllegalStateException("constraint requires variable A to be generic");
        
        //verify that the sum and expression do not share any indices
        if (zexpr instanceof GenericNumExpr)
            verifyIndices(indexRange, ((GenericNumExpr) zexpr).getGenericIndices());
    }
    
    /**
     * Creates a constraint restricting a summation of values in the generic variable A
     * to a constant value z
     * 
     * @param aexpr                     Generic variable summation is over
     * @param indexRange                Indices within generic variable A that summation is over
     * @param sourceIdxRestriction      Optional restriction of values placed on source index ranges
     * @param zexpr                     Target number that summation is related to
     * @param constraintType            Constraint relation type (Eq, Lt, Gt, etc.)
     */
    public SummationConstraint(GenericNumExpr aexpr, GenericIndex indexRange[],
            CspGenericIndexRestriction sourceIdxRestriction,
            Number zexpr, int constraintType) 
    {
        super(aexpr, zexpr, indexRange, constraintType);
        this.sourceIdxRestriction = sourceIdxRestriction;
    }
    
    /**
     * Creates a constraint restricting a summation of values in the generic variable A
     * to a constant value z
     * 
     * @param aexpr                     Generic variable summation is over
     * @param indexRange                Indices within generic variable A that summation is over
     * @param sourceIdxRestriction      Optional restriction of values placed on source index ranges
     * @param zexpr                     Target number that summation is related to
     * @param constraintType            Constraint relation type (Eq, Lt, Gt, etc.)
     */
    public SummationConstraint(GenericNumExpr aexpr, GenericIndex indexRange[],
            CspGenericIndexRestriction sourceIdxRestriction,
            GenericNumConstant zexpr, int constraintType) 
    {
        super(aexpr, zexpr, indexRange, constraintType);
        this.sourceIdxRestriction = sourceIdxRestriction;
    }    
    
    public SummationConstraint createConstraint(Number zexpr, int constraintType) {
        return new SummationConstraint((GenericNumExpr)aexpr, restrictedIndices,sourceIdxRestriction, zexpr, constraintType);
    }
    
    public SummationConstraint createConstraint(GenericNumConstant zexpr, int constraintType) {
        return new SummationConstraint((GenericNumExpr)aexpr, restrictedIndices,sourceIdxRestriction, zexpr, constraintType);
    }
    
    public SummationConstraint createConstraint(NumExpr zexpr, int constraintType) {
        return new SummationConstraint((GenericNumExpr)aexpr, restrictedIndices,sourceIdxRestriction, zexpr, constraintType);
    }
    
    /**
     * Verifies that there are no common indices
     * 
     * @param indexRange	The indicies involved in the summation
     * @param zIndicies		The indicies involved in the expression
     */
    private void verifyIndices(CspGenericIndex[] indexRange, CspGenericIndex[] zIndices) {
        List zIndexList = Arrays.asList(zIndices);
        for(int i=0; i<indexRange.length; i++) {
            if(zIndexList.contains(indexRange[i])) {
                throw new IllegalStateException("summation cannot share indices with associated expression");
            }
        }
    }
    
    /**
     * Creates an array of arcs representing constraint
     */
    protected Arc[] createArcs() {
        if (zexpr == null) {
            if(genConstVal!=null) {
		        return new Arc[] {
			            // z < summation(Ai, i in [1..100])
			            new GenericNumSummationReflexArc(genConstVal, (GenericNumNode) aexpr.getNode(), nodeType, ThreeVarConstraint.oppositeArcType(constraintType), 
			                    restrictedIndices, sourceIdxRestriction)
			        };
            }
            else{
		        return new Arc[] {
		            // z < summation(Ai, i in [1..100])
		            new GenericNumSummationReflexArc(constVal, (GenericNumNode) aexpr.getNode(), nodeType, ThreeVarConstraint.oppositeArcType(constraintType), 
		                    restrictedIndices, sourceIdxRestriction)
		        };
            }
            
        }
        else {
            // summation(Ai, i in [1..100]) > Z
            // the temp node is used as an intermediary between the source (a node) and the main target (z node) to maintain
            // the value of the summation when it exceeds the max/min bounds of z facilitating the use of deltas during propagation
            Arc a1 = new GenericNumSummationArc((GenericNumNode) aexpr.getNode(), getTempNode(), zexpr.getNode(), nodeType,
                    ThreeVarConstraint.equivalentArcType(constraintType), restrictedIndices, sourceIdxRestriction);
            
            // Z < summation(Ai, i in [1..100])
            Arc a2 = new GenericNumSummationReflexArc(zexpr.getNode(), (GenericNumNode) aexpr.getNode(), nodeType, ThreeVarConstraint.oppositeArcType(constraintType), 
                    restrictedIndices, sourceIdxRestriction);
            
            return new Arc[] { a1, a2 };
        }
    }

    // javadoc inherited from TwoVarConstraint
    protected AbstractConstraint createConstraint(NumExpr aexpr, NumExpr zexpr, Number numVal, GenericNumConstant genConst, int constraintType) {
        Number numConst = constVal;
        if (numVal!=null) numConst=numVal;    
        AbstractConstraint constraint = null;
        	if (zexpr==null) {
                if(genConst!=null)
                    constraint= new SummationConstraint((GenericNumExpr)aexpr, restrictedIndices, sourceIdxRestriction, genConst, constraintType);
                else
                    constraint= new SummationConstraint((GenericNumExpr)aexpr, restrictedIndices, sourceIdxRestriction, numConst, constraintType);
            }
            else {
                constraint= new SummationConstraint((GenericNumExpr)aexpr, restrictedIndices, sourceIdxRestriction, zexpr, constraintType);
            }
        	constraint.associateToGraph(graph);
        	return constraint;
    }
    
    // javadoc inherited from TwoVarConstraint
    public boolean violated() {
        double precision = getPrecision();
        
        switch (constraintType) {
        	// sum(A)< Z
        	case TwoVarConstraint.LT:
        	    calcMinSum();
        	    return NumberMath.compare(minSum, getSmallestMax(currentZ, currentGz), precision, nodeType) >= 0;
        	
        	// sum(A)<= Z
        	case TwoVarConstraint.LEQ:
        	    calcMinSum();
        	    return NumberMath.compare(minSum, getSmallestMax(currentZ, currentGz), precision, nodeType) > 0;
        	    
        	// sum(A) > Z
        	case TwoVarConstraint.GT:
        	    calcMaxSum();
        	    return NumberMath.compare(maxSum, getLargestMin(currentZ, currentGz), precision, nodeType) <= 0;
        	    
        	// sum(A) >= Z
        	case TwoVarConstraint.GEQ:
        	    calcMaxSum();
        	    return NumberMath.compare(maxSum, getLargestMin(currentZ, currentGz), precision, nodeType) < 0;
        	    
        	// sum(A)!= Z
        	case TwoVarConstraint.NEQ:
        	    GenericIndexManager idxMgr = getIndexManager();
        	    if ( (gaexpr.isBound() || (idxMgr.xIndexIsRestricted() && restrictedExprsBound(idxMgr))) &&
       	             (zexpr==null || zexpr.isBound()) ) {
        	        calcTotalRange();
        	        if(!maxSum.equals(minSum)) {
        	            return false;
        	        }
        	        //Notice since both variables are bound, we can assume that calcMaxSum will be the same
        	        //as calcMin
        	        return NumberMath.compare(maxSum, getLargestMin(currentZ, currentGz), precision, nodeType) == 0;
        	    }
        	    else {
        	        return false;
        	    }
            	
        	// sum(A)== Z
        	default:
        	    calcTotalRange();
        		return ((NumberMath.compare(maxSum,getLargestMin(currentZ, currentGz), precision, nodeType) < 0)  ||
        		        (NumberMath.compare(minSum,getSmallestMax(currentZ, currentGz), precision, nodeType) > 0));  
        }
    }
    
    /**
     * Verifies that the expressions involved in a summation are bound
     * even if the generic is not completely bound yet
     */
    private boolean restrictedExprsBound(GenericIndexManager idxMgr) {
        Iterator restrictedIterator = idxMgr.restrictedIterator();
        
        while (restrictedIterator.hasNext()) {
            restrictedIterator.next();
            
            if(sourceIdxRestriction==null) {return false;}
            
            // check if current index combination is included in summation
            if (sourceIdxRestriction.currentIndicesValid()) {
                if (idxMgr.xIndexIsRestricted()) {
                    // set current X if necessary
                    currentA = gaexpr.getNumExpressionForIndex();
                    if(!currentA.isBound()) {
                        return false;
                    }
                }
            }
        }
        
        // all the expressions included in the summation are bound
        return true;
    }
    
    /**
     * Calculates minimum summation value
     * and sets the local variable sumMin correctly
     */
    private void calcMinSum() {
        GenericIndexManager idxMgr = getIndexManager();
        
        // calculate min value for z over specified range
        minSum.set(NumberMath.zero(nodeType));
        Iterator rangeIter = idxMgr.restrictedIterator();
        while (rangeIter==null || rangeIter.hasNext()) {
            if (rangeIter!=null) rangeIter.next();
            
            // check if current index combination is included in summation
            if (sourceIdxRestriction==null || sourceIdxRestriction.currentIndicesValid()) {
                // set current X if necessary
                if (idxMgr.xIndexIsRestricted())
                    currentA = gaexpr.getNumExpressionForIndex();
                
                NumberMath.addNoInvalid(minSum, getLargestMin(currentA), nodeType, minSum);
            }
            
            if (rangeIter==null) break;
        }
    }
        
    /**
     * Calculates maximum summation value
     * and sets the local variable sumMax correctly
     */
    private void calcMaxSum() {
        GenericIndexManager idxMgr = getIndexManager();
        
        // calculate max value for z over specified range
        maxSum.set(NumberMath.zero(nodeType));
        Iterator rangeIter = idxMgr.restrictedIterator();
        while (rangeIter==null || rangeIter.hasNext()) {
            if (rangeIter!=null) rangeIter.next();
            
            // check if current index combination is included in summation
            if (sourceIdxRestriction==null || sourceIdxRestriction.currentIndicesValid()) {
                // set current X if necessary
                if (idxMgr.xIndexIsRestricted())
                    currentA = gaexpr.getNumExpressionForIndex();
                NumberMath.addNoInvalid(maxSum, getSmallestMax(currentA), nodeType, maxSum);
            }
            
            if (rangeIter==null) break;
        }
    }
        
    /**
     * Calculates the smallestMin and largestMax and sets the local variables 
     * minSum and maxSum correctly
     */
    private void calcTotalRange() {
        GenericIndexManager idxMgr = getIndexManager();
        
        // calculate min and max values for z over specified range at
        // same time for speed
        minSum.set(NumberMath.zero(nodeType));
        maxSum.set(minSum);
        Iterator rangeIter = idxMgr.restrictedIterator();
        while (rangeIter==null || rangeIter.hasNext()) {
            if (rangeIter!=null) rangeIter.next();
            
            // check if current index combination is included in summation
            if (sourceIdxRestriction==null || sourceIdxRestriction.currentIndicesValid()) {
                // set current X if necessary
                if (idxMgr.xIndexIsRestricted())
                    currentA = gaexpr.getNumExpressionForIndex();
                
                NumberMath.addNoInvalid(minSum, getLargestMin(currentA), nodeType, minSum);
                NumberMath.addNoInvalid(maxSum, getSmallestMax(currentA), nodeType, maxSum);
            }
            
            if (rangeIter==null) break;
        }
    }
        
    //Method to determine which version of getSmallestMax to call
    private Number getSmallestMax(NumExpr expr) { 
        if (expr instanceof GenericNumExpr) {
            return getSmallestMax(null, (GenericNumExpr) expr);
        }
        else {
            return getSmallestMax(expr, null);
        }
    }
    
    //Method to determine which version of getLargestMin to call    
    private Number getLargestMin(NumExpr expr) { 
        if (expr instanceof GenericNumExpr) {
            return getLargestMin(null, (GenericNumExpr) expr);
        }
        else {
            return getLargestMin(expr, null);
        }
    }
    
    private NumNode getTempNode() {
        switch (nodeType) {
            case INTEGER:
                return new IntNode();
            case LONG:
                return new LongNode();
            case FLOAT:
                return new FloatNode();
            default:
                return new DoubleNode();
        }
    }
}