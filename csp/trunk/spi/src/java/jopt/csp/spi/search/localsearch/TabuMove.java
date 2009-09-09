/*
 * TabuMove.java
 * 
 * Created on Jun 24, 2005
 */
package jopt.csp.spi.search.localsearch;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jopt.csp.solution.VariableSolution;
import jopt.csp.variable.CspVariable;

/**
 * Represents a single move made during a local search that is added
 * to a tabu list to keep the move from occuring again
 * 
 * @author  Nick Coleman
 * @version $Revision: 1.2 $
 * @see TabuList
 */
class TabuMove {
	private Map changes;
    
    public TabuMove() {
    	this.changes = new HashMap();
    }
    
    /**
     * Adds a variable solution containing an alteration
     * to an initial solution that is part of this move
     * 
     * @param sol   Solution to add as part of move
     */
    public void addVariableSolution(VariableSolution sol) {
        if (sol==null) return;
    	changes.put(sol.getVariable(), sol.clone());
    }
    
    /**
     * Returns true if a move will alter any of the changes this
     * move made
     */
    public boolean conflicts (TabuMove move) {
        boolean retval = false;
        
        // loop over all variables changed with move
        Iterator moveIter = move.changes.keySet().iterator();
        while (moveIter.hasNext()) {
            CspVariable var = (CspVariable) moveIter.next();
            
            // check if variable altered is contained within this move
            VariableSolution thisSol = (VariableSolution) changes.get(var);
            if (thisSol!=null) {

                // retrieve corresponding alteration for move to compare
                VariableSolution moveSol = (VariableSolution) move.changes.get(var);

                // if change in move is not same as this move, it is in conflict
                if(!moveSol.equals(thisSol)) {
                    retval = true;
                    break;
                }
            }
        }
        
        return retval;
    }
    
    /**
     * Returns true if the specified move will repeat any of the changes this
     * move made
     */
    public boolean repeats (TabuMove move) {
        boolean retval = false;
        
        // loop over all variables changed with move
        Iterator moveIter = move.changes.keySet().iterator();
        while (moveIter.hasNext()) {
            CspVariable var = (CspVariable) moveIter.next();
            
            // check if variable altered is contained within this move
            VariableSolution thisSol = (VariableSolution) changes.get(var);
            if (thisSol!=null) {

                // retrieve corresponding alteration for move to compare
                VariableSolution moveSol = (VariableSolution) move.changes.get(var);

                // if change is equal to this alteration, it is a repeat
                if (moveSol.equals(thisSol)) {
                    retval = true;
                    break;
                }
            }
        }
        
        return retval;
    }

    public String toString() {
    	return changes.toString();
    }
}