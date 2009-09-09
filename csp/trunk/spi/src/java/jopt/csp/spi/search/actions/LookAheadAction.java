package jopt.csp.spi.search.actions;

import jopt.csp.search.SearchAction;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.variable.PropagationFailureException;

/**
 * A unique combination of actions that allows the search to try a change
 * (an assignment for instance) before deciding how to proceed;
 * hence, "look ahead".
 * <p>
 * First, the pseudo-change action is attempted.  If it is successful,
 * the success action is returned (and will be performed next); otherwise,
 * the failure action is returned (and will be performed next, instead).
 * And, regardless of the success or failure of the psuedo-change action,
 * its effects on the state of the problem are always rolled-back
 * (ie. popped off the choicepoint stack).
 * <p>
 * Basically, you get to test the waters before deciding whether you
 * jump in or sit on the beach instead.
 * 
 * @author Chris Johnson
 */
public class LookAheadAction implements SearchAction {
    private ConstraintStore store;
    private SearchAction pseudoChange;
    private SearchAction successAction;
    private SearchAction failureAction;
    
    public LookAheadAction(ConstraintStore store, SearchAction pseudoChange, SearchAction successAction, SearchAction failureAction) {
        this.store = store;
        this.pseudoChange = pseudoChange;
        this.successAction = successAction;
        this.failureAction = failureAction;
    }
    
    public SearchAction performAction() throws PropagationFailureException {
        SearchAction toReturn = this.successAction;
        
        store.getChoicePointStack().push();
        try {
            pseudoChange.performAction();
        }
        catch (PropagationFailureException pfe) {
            toReturn = this.failureAction;
        }
        store.getChoicePointStack().pop();

        return toReturn;
    }
    
    public String toString() {
    	return "look-ahead(pseduoChange["+pseudoChange+"], successAction["+successAction+"], failureAction["+failureAction+"])";
    }
}