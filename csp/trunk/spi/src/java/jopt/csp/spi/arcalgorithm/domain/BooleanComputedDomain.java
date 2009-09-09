package jopt.csp.spi.arcalgorithm.domain;

import jopt.csp.spi.arcalgorithm.variable.GenericBooleanConstant;
import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.spi.util.BoolOperation;
import jopt.csp.util.IntSparseSet;
import jopt.csp.util.NumSet;
import jopt.csp.util.NumberIterator;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.PropagationFailureException;

public class BooleanComputedDomain extends AbstractDomain implements BooleanDomain {
    private BooleanDomain domA;
    private BooleanDomain domB;
    private boolean notB;
    private boolean constVal;
    private GenericBooleanConstant genConstVal;
    private CspConstraint constraint;
    private int operation;
    private boolean isTrue;
    private boolean isFalse;
    private boolean needsRecalc;
    private DelegatedDomainListener changeListener;

    /**
     * Creates new boolean computed domain
     */
    private BooleanComputedDomain(BooleanDomain domA, CspConstraint constraint, 
            BooleanDomain domB, boolean notB, boolean constVal, GenericBooleanConstant genConstVal, int operation) 
    {
        this.domA = domA;
        this.constraint = constraint;
        this.domB = domB;
        this.notB = notB;
        this.constVal = constVal;
        this.genConstVal = genConstVal;
        this.operation = operation;
        this.needsRecalc = true;

        // Set listener for delegated domains
        this.changeListener = new DelegatedDomainListener();
        if (domA != null) ((DomainChangeSource) domA).addDomainChangeListener(changeListener);
        if (domB != null) ((DomainChangeSource) domB).addDomainChangeListener(changeListener);
    }

    /**
     * Creates new boolean domain that wraps a constraint
     */
    public BooleanComputedDomain(CspConstraint constraint) 
    {
        this(null, constraint, null, false, false, null, BoolOperation.EQ);
    }
    
    /**
     * Creates new boolean computed domain based on a an expression built from other boolean
     * domains
     */
    public BooleanComputedDomain(BooleanDomain domA, BooleanDomain domB, boolean notB, boolean constVal, GenericBooleanConstant genConstVal, int operation) 
    {
        this(domA, null, domB, notB, constVal, genConstVal, operation);
    }
    
    /**
     * Helper function to return true if A is false
     */
    private boolean isFalseA() {
        return (domA != null) ? domA.isFalse() : true ;
    }
    
    /**
     * Helper function to return true if A is true
     */
    private boolean isTrueA() {
        return (domA != null) ? domA.isTrue() : true ;
    }
    
    /**
     * Helper function to return true if B is false
     */
    private boolean isFalseB() {
        if (domB!=null) {
            if (notB)
                return domB.isTrue();
            else
            	return domB.isFalse();
        }
        else
            return !constVal;
    }
    
    /**
     * Helper function to return true if B is true
     */
    private boolean isTrueB() {
        if (domB!=null) {
            if (notB)
                return domB.isFalse();
            else
                return domB.isTrue();
        }
        else
            return constVal;
    }
    
    /**
     * Recalculates isTrue and isFalse values
     */
    private void recalc() {
//        boolean oldTrue = isTrue;
//        boolean oldFalse = isFalse;
        
        switch(operation) {
            case BoolOperation.EQ:
                if (constraint==null) {
                    isTrue = (isTrueA() && isTrueB()) || (isFalseA() && isFalseB());
                    isFalse = !isTrue && (isFalseA() && isTrueB()) || (isTrueA() && isFalseB());
                }
                else {
                    isTrue = constraint.isTrue();
                    isFalse = !isTrue && constraint.isFalse();
                }
                break;
                
            case BoolOperation.AND:
            	isFalse = isFalseA() || isFalseB();
                isTrue = !isFalse &&
                         ((isFalseA() && isFalseB()) ||
                          (isTrueA() && isTrueB()));
                break;
                  
            case BoolOperation.OR:
                isTrue = isTrueA() || isTrueB();
                isFalse = !isTrue && isFalseA() && isFalseB();
                break;
           
            case BoolOperation.XOR:
                isTrue = (isTrueA() && isFalseB()) ||
                         (isFalseA() && isTrueB());
            	isFalse = !isTrue &&
                          ((isTrueA() && isTrueB()) ||
                           (isFalseA() && isFalseB()));
                break;
              
            case BoolOperation.NOT:
                isTrue = isFalseB();
                isFalse = !isTrue && isTrueB();
                break;
                
            case BoolOperation.IMPLIES:
                isTrue = (isTrueA() && isTrueB()) || isFalseA();
            	isFalse = !isTrue && isTrueA() && isFalseB();
            	break;
        }

//        try {
//            if (oldTrue!=isTrue || oldFalse!=isFalse)
//                notifyDomainChange();
//        }
//        catch(Exception e){
//            e.printStackTrace();
//        }
    }

    /**
     * Returns true if this domain is bound to a true value
     */
    public boolean isTrue() {
        if (needsRecalc) recalc();
        return isTrue;
    }

    /**
     * Returns true if this domain is bound to a false value
     */
    public boolean isFalse() {
        if (needsRecalc) recalc();
        return isFalse;
    }
    
    /**
     * Returns size of domain
     */
    public int getSize() {
        if (needsRecalc) recalc();
        return (isTrue || isFalse) ? 1 : 2;
    }

    /**
     * Returns true if domain is bound to a value
     */
    public boolean isBound() {
        return (domA == null || domA.isBound()) &&
               (domB == null || domB.isBound());
    }

    /**
     * Returns true if value is in domain
     */
    public boolean isInDomain(boolean bool) {
        if (needsRecalc) recalc();
        if (bool)
            return !isFalse;
        else
            return !isTrue;
    }
    
    /**
     * Attempts to reduce domain to a true value
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setTrue() {
        // computed domain - do nothing
    }

    /**
     * Attempts to reduce domain to a false value
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setFalse(){
        // computed domain - do nothing
    }

    /**
     * Sets the choicepoint stack associated with this domain.  Can only
     * be set once
     */
    public void setChoicePointStack(ChoicePointStack cps) {
        // computed domain - do nothing
    }
    
    /**
     * Returns true if a call to setChoicePointStack will fail
     */
    public boolean choicePointStackSet() {
        return false;
    }

    /**
     * Stores all necessary information for this domain allowing it to be restored
     * to a previous state at a later point in time.
     * 
     * @see jopt.csp.spi.arcalgorithm.domain.Domain#restoreDomainState(java.lang.Object)
     */
	public Object getDomainState() {
		// computed domain - return null
		return null;
	}
	
	/**
     * Restores a domain to a previous state using the information contained in
     * the state parameter.
     * 
	 * @see jopt.csp.spi.arcalgorithm.domain.Domain#restoreDomainState(java.lang.Object)
	 */
	public void restoreDomainState(Object state) {
		// computed domain - do nothing
	}
	
    /**
     * Returns iterator of values in node's domain
     */
    public NumberIterator values() {
        return getDeltaSet().values();
    }
    
    /**
     * Returns iterator of values in node's delta
     */
    public NumberIterator deltaValues() {
        return getDeltaSet().values();
    }

    /**
     * Returns set of Numbers and NumIntervals representing domain
     */
    public NumSet toSet() {
        if (needsRecalc) recalc();
        
        IntSparseSet set = new IntSparseSet();
        if (isTrue) set.add(1);
        else if (isFalse) set.add(0);
        else set.add(0, 1);
        return set;
    }

    /**
     * Returns the delta set for this domain
     */
    public NumSet getDeltaSet() {
        return new IntSparseSet();
    }
    
    /**
     * Clears the delta set for this domain
     */
    public void clearDelta() {
        // computed domain - do nothing
    }

    public String toString() {
        if (needsRecalc) recalc();
        
        if (isFalse)
            return "[false]";
        else if (isTrue)
            return "[true]";
        else
            return "[unbound]";
    }
    
    // IntDomain compatibility
    public int getMax() {
        if (needsRecalc) recalc();
        return isFalse ? 0 : 1;
    }

    // IntDomain compatibility
    public int getMin() {
        if (needsRecalc) recalc();
        return isTrue ? 1 : 0;
    }

    // IntDomain compatibility
    public boolean isInDomain(Number val) {
        return isInDomain(val.intValue());
    }
    
    // IntDomain compatibility
    public boolean isInDomain(int val) {
        if(needsRecalc) recalc();
        switch(val) {
            case 0:
                return isInDomain(false);
                
            case 1:
                return isInDomain(true);
                
            default:
                return false;
        }
    }

    // IntDomain compatibility
    public Number getNextHigher(Number val) {
        return new Integer(getNextHigher(val.intValue()));
    }

    // IntDomain compatibility
    public int getNextHigher(int val) {
        if(needsRecalc) recalc();
        if (val>=1) return val;
        else if (val<1 && !isFalse) return 1;
        else return 0;
    }

    // IntDomain compatibility
    public Number getNextLower(Number val) {
        return new Integer(getNextLower(val.intValue()));
    }

    // IntDomain compatibility
    public int getNextLower(int val) {
        if(needsRecalc) recalc();
        if (val<=0) return val;
        else if (val>0 && !isTrue) return 0;
        else return 1;
    }

    // IntDomain compatibility
    public void setMax(int val) {
        // computed domain - do nothing
    }

    // IntDomain compatibility
    public void setMin(int val){
        // computed domain - do nothing
    }

    // IntDomain compatibility
    public void setValue(int val) {
        // computed domain - do nothing
    }

    // IntDomain compatibility
    public void removeValue(int val){
        // computed domain - do nothing
    }

    // IntDomain compatibility
    public void setRange(int start, int end) {
        // computed domain - do nothing
    }

    // IntDomain compatibility
    public void removeRange(int start, int end){
        // computed domain - do nothing
    }

    // IntDomain compatibility
    public void setDomain(NumSet s) {
        // computed domain - do nothing
    }

    // IntDomain compatibility
    public void removeDomain(NumSet vals){
        // computed domain - do nothing
    }
    
    /**
     * Helper class to listen when DOM A or DOM B changes
     */
    private class DelegatedDomainListener implements DomainChangeListener {
        /**
         * Method invoked by domain when a domain change event is fired
         */
        public void domainChange(DomainChangeEvent ev) throws PropagationFailureException {
            needsRecalc = true;
            notifyRangeChange();
        }
        
        public void choicePointPop() {
            needsRecalc = true;
            notifyChoicePointPop();
        }
        
        public void choicePointPush() {
            needsRecalc = true;
            notifyChoicePointPush();
        }
    }

    public Object clone() {
        return new BooleanComputedDomain(domA, constraint, domB, notB, constVal, genConstVal, operation);
    }
}