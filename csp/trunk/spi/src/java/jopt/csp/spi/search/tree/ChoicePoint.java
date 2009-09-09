package jopt.csp.spi.search.tree;

import java.util.Collection;
import java.util.LinkedList;

import jopt.csp.search.SearchAction;
import jopt.csp.variable.PropagationFailureException;

/**
 * Special action that represents a choice point for the search tree.  When
 * a tree encounters this type of action, it separates each child action into
 * a separate search node for further traversal by the search routine.  This
 * is the only SearchAction that creates child nodes.
 * 
 * @author  Nick Coleman
 */
public final class ChoicePoint implements SearchAction {
    private Collection actions;

    /**
     * Creates a new choice point
     *   
     * @param action1   First choice
     * @param action2   Second choice
     */
    public ChoicePoint(SearchAction action1, SearchAction action2) {
        this.actions = new LinkedList();
        actions.add(action1);
        actions.add(action2);
    }
    
    /**
     * Creates a new choice point
     *  
     * @param actions       Collection of actions that should be separated into individual search nodes
     */
    public ChoicePoint(Collection actions) {
        this.actions = actions;
    }
    
    /**
     * Returns the child actions that should be split into separate source nodes
     */
    public Collection getChildActions() {
        return actions;
    }

    /**
     * This method does not actually do anything and is not used by the search tree.
     * It exists only to satisfy requirements of the action class.  The actual creation
     * of child nodes is handled
     */
    public SearchAction performAction() throws PropagationFailureException {
        return null;
    }

    public String toString() {
        return "choicepoint(" + actions + ")";
    }
}