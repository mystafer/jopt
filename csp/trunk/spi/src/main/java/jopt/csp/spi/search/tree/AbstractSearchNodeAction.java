package jopt.csp.spi.search.tree;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import jopt.csp.search.SearchAction;

/**
 * This class is used by the search tree to build search nodes. A search
 * node wraps an action and executes the action when the node is activated.
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
 * multiple operations to be combined.
 *  
 * @author Nick Coleman
 */
public abstract class AbstractSearchNodeAction implements SearchAction {
    /**
     * Returns an action that is composed of a choice between two other actions
     * 
     * @param firstAction   First action in choice
     * @param secondAction  Second action in choice
     */
    protected ChoicePoint choice(SearchAction firstAction, SearchAction secondAction) {
        LinkedList<SearchAction> actions = new LinkedList<SearchAction>();
        actions.add(firstAction);
        actions.add(secondAction);
        return new ChoicePoint(actions);
    }

    /**
     * Returns an action that is composed of choice between a collection of other actions
     * 
     * @param actions  Collection of actions to choose between
     */
    protected ChoicePoint choice(Collection<SearchAction> actions) {
        return new ChoicePoint(actions);
    }
    
    /**
     * Returns an action that is composed of a combination of two other actions
     * 
     * @param firstAction   First action in combination
     * @param secondAction  Second action in combination
     */
    protected CombinedAction combineActions(SearchAction firstAction, SearchAction secondAction) {
        LinkedList<SearchAction> actions = new LinkedList<SearchAction>();
        actions.add(firstAction);
        actions.add(secondAction);
        return new CombinedAction(actions);
    }

    /**
     * Returns an action that is composed of a list of other actions.  The first
     * element in the list will be the first to be performed.
     * 
     * @param actions  List of actions to combine
     */
    protected CombinedAction combineActions(List<? extends SearchAction> actions) {
        return new CombinedAction(actions);
    }
}
