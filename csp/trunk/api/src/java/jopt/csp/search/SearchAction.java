package jopt.csp.search;

import jopt.csp.variable.PropagationFailureException;

/**
 * This class is used by the search tree to build search nodes. A search
 * node contains an action that is executed when the node is activated.
 * The action can return child nodes that allow further nodes to be
 * attached to the search tree.
 * 
 * If this action is a choice point, the child actions returned will
 * each be inserted into the search tree as separate nodes.  The
 * child actions are used as alternate operations that may occur
 * at this point in the tree.
 * 
 * If this action is not a choice point, the child actions returned
 * will be executed as a part of the current search node allowing
 * multiple operations to be performed at a single node.
 *  
 * @author Nick Coleman
 */
public interface SearchAction {
    /**
     * Executes this action and returns the next action to execute.  Generally 
     * called when a search node is activated or reactivated.
     * 
     * @return Next action to execute in search
     */
    public SearchAction performAction() throws PropagationFailureException;
}