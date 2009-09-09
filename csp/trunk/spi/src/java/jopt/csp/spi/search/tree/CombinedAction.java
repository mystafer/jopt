package jopt.csp.spi.search.tree;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import jopt.csp.search.SearchAction;
import jopt.csp.variable.PropagationFailureException;

/**
 * Special action that represents a combination of actions that should
 * all be performed within the same search node
 * 
 * @author  Nick Coleman
 */
public final class CombinedAction implements SearchAction {
    // List implementing a stack of action to be executed.
    // The last element in the list is the first to be executed.
    private LinkedList actionStack;

    /**
     * Creates a new combined action
     *  
     * @param action1   First action to perform
     * @param action2   Second action to perform
     */
    public CombinedAction(SearchAction action1, SearchAction action2) {
        this.actionStack = new LinkedList();
        actionStack.add(action2);
        actionStack.add(action1);
    }
    
    /**
     * Creates a new combined action
     *  
     * @param action1   First action to perform
     * @param action2   Second action to perform
     * @param action3   Third action to perform
     */
    public CombinedAction(SearchAction action1, SearchAction action2, SearchAction action3) {
        this.actionStack = new LinkedList();
        actionStack.add(action3);
        actionStack.add(action2);
        actionStack.add(action1);
    }
    
    /**
     * Creates a new combined action
     *  
     * @param actions   List of actions that should make up this combined action.
     *                  Actions will be performed in the order they exist in the list.
     */
    public CombinedAction(List actions) {
        this.actionStack = new LinkedList(actions);
        
        // Necessary because internally we perform the last element of the list first
        // but the List that is passed in wants us to perferm the first element first.
        Collections.reverse(actionStack);
    }
    
    /**
     * Constructor used when a choicepoint is encountered during processing of child actions
     *  
     * @param cpAction          Choice point action that should be first action to perform
     * @param remainingActions  Remaining actions in list that need to be performed
     */
    private CombinedAction(Object cpAction, LinkedList remainingActions) {
        this.actionStack = (LinkedList) remainingActions.clone();
        actionStack.add(cpAction);
    }
    
    /**
     * Iteratively executed the actions stored in this <code>CombinedAction</code>.  Handles
     * choice points and search technique changes appropriately so that all actions in the
     * combined action will eventually be executed.
     */
    public SearchAction performAction() throws PropagationFailureException {
        LinkedList workingStack = (LinkedList) actionStack.clone();
        
        // loop over actions and process each
        SearchAction currentAction = (SearchAction) workingStack.removeLast();
        while (currentAction!=null) {
            // if this is a choice point action, need to return it to allow
            // further building of tree.
            if (currentAction instanceof ChoicePoint) {
                return processChoicePoint(workingStack, currentAction);
            }
            
            // if this action is a combined action, add all of it's actions
            // to the top of this stack
            else if (currentAction instanceof CombinedAction) {
                CombinedAction cbAction = (CombinedAction) currentAction;
                workingStack.addAll(cbAction.actionStack);
            }
            
            // action is a search technique change, store for use by search node
            else if (currentAction instanceof SearchTechniqueChange) {
                return processSearchChange(workingStack, currentAction);
            }
            
            // perform action requested
            else {
            	SearchAction nextAction = currentAction.performAction();
                if (nextAction!=null) workingStack.add(nextAction);
            }

            // pop top action on stack
            if (workingStack.size()>0)
            	currentAction = (SearchAction) workingStack.removeLast();
            else
                currentAction = null;
        }
        
        // no more actions exist to perform
        return null;
    }

    /**
     * This method handles a search technique change encountered when there exist
     * actions in the stack that have not yet executed.  It ensures that the
     * rest of the stack is executed after the technique change is done.
     */
    public SearchAction processSearchChange(LinkedList workingStack, SearchAction searchAction) throws PropagationFailureException {
        SearchTechniqueChange tc = (SearchTechniqueChange) searchAction;
        
        // no next action exists
        if (workingStack.size() == 0) {
            return tc;
        }
        
        // build combined action for left over operation
        else {
            CombinedAction nextAction = new CombinedAction(workingStack);
            nextAction.actionStack.addLast(searchAction.performAction());
            return new SearchTechniqueChange(tc.getGoal(), tc.getTechnique(), nextAction);
        }
    }

    /**
     * This method handles creating combined choice point if additional actions exist
     * that have not yet executed in the stack
     */
    public SearchAction processChoicePoint(LinkedList workingStack, SearchAction searchAction) throws PropagationFailureException {
        // If no more actions remain in the stack, simply the choice point as next action
        if (workingStack.size()==0) {
            return searchAction;
        }
        
        // Since there are remaining actions in the stack, we need to return
        // a new choice point that contains combined actions made by
        // appending the working stack at the end of each action in the original
        // choice point.
        else {
            ChoicePoint cp = (ChoicePoint) searchAction;
            
            // build collection of combined actions for each child
            // action in choicepoint
            LinkedList newCpActions = new LinkedList();
            Iterator oldCpActionIter = cp.getChildActions().iterator();
            while (oldCpActionIter.hasNext()) {
                Object cpAction = oldCpActionIter.next();
                
                // create new combined action for choice point
                newCpActions.add(new CombinedAction(cpAction, workingStack));
            }
            
            return new ChoicePoint(newCpActions);
        }
    }

    public String toString() {
        return "combined-action(" + actionStack + ")";
    }
}