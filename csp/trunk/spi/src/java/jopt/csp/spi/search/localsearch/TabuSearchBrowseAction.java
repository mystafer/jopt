package jopt.csp.spi.search.localsearch;

import java.util.ArrayList;

import jopt.csp.search.CurrentNeighbor;
import jopt.csp.search.Metaheuristic;
import jopt.csp.search.Neighborhood;
import jopt.csp.search.SearchAction;
import jopt.csp.solution.SolverSolution;
import jopt.csp.spi.search.tree.AbstractSearchNodeAction;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.util.DoubleUtil;
import jopt.csp.variable.PropagationFailureException;

import org.apache.commons.collections.primitives.ArrayDoubleList;
import org.apache.commons.collections.primitives.ArrayIntList;

public class TabuSearchBrowseAction extends AbstractSearchNodeAction {
    private ConstraintStore store;
    private SolverSolution initial;
    private Neighborhood hood;
    private Metaheuristic meta;
    private CurrentNeighbor current;
    private int currentNeighbor;
    private int numToCheck;
    private double bestObjectiveVal;
    
    /**
     * Creates new scan neighbors action
     * 
     * @param store         Constraint store containing problem where solution will be restored
     * @param initial       Initial solution related to neighbor
     * @param hood          Neighborhood of solutions, each to be applied as an alternative choices
     * @param current       Updated as neighbors are restored to solver to store the currently selected neighbor
     */
    public TabuSearchBrowseAction(ConstraintStore store, SolverSolution initial, Neighborhood hood, CurrentNeighbor current, int numToCheck, double bestObjectiveVal) {
        this(store, initial, hood, null, current, numToCheck, bestObjectiveVal);
    }
    
    public TabuSearchBrowseAction(ConstraintStore store, SolverSolution initial, Neighborhood hood, Metaheuristic meta, CurrentNeighbor current, int numToCheck, double bestObjectiveVal) {
        this.store = store;
        this.initial = initial;
        this.hood = hood;
        this.meta = meta;
        this.current = current;
        this.currentNeighbor = 0;
        this.numToCheck = numToCheck;
        this.bestObjectiveVal = bestObjectiveVal;
    }
    
    /**
     * Called by search tree to execute this action.
     * 
     * @return Next action to execute in search
     */
    public SearchAction performAction() throws PropagationFailureException {
        // initialize neighborhood and metaheuristic before retrieving first neighbor
        if (currentNeighbor==0) {
            hood.setInitialSolution(initial);
            
            if (meta!=null) {
                meta.setInitialSolution(initial);
            }
        }
        
        // In case the metaheuristic bans all moves, store at least one workable move
        SolverSolution firstConsistentNeighbor = null;
        int firstConsistentNeighborIdx = -1;

        ArrayList sols = new ArrayList();
        ArrayIntList solIndices = new ArrayIntList();
        ArrayDoubleList solObjectives = new ArrayDoubleList();
        while (sols.size()<numToCheck && currentNeighbor < hood.size()) {
            // retrieve next neighbor
            SolverSolution neighbor = hood.getNeighbor(currentNeighbor);

            // check if neighbor is different than the current solution
            if (neighbor!=null && !initial.isDifferent(neighbor)) {
                neighbor = null;
            }
            
            // Try to propagate and get an objective value out of it
            if (neighbor != null) {
                Object state = store.getCurrentState();
                try {
                    store.restoreNeighboringSolution(initial, neighbor);
                    
                    // Propagation worked...
                    if (firstConsistentNeighbor == null) {
                        firstConsistentNeighbor = neighbor;
                        firstConsistentNeighborIdx = currentNeighbor;
                    }
                    
                    // Propagation worked... now find the objective value
                    double objectiveVal;
                    boolean isBest = false;
                    if (initial.isMinimizeObjective()) {
                        objectiveVal = DoubleUtil.getMax(initial.getObjectiveExpression());
                        if (objectiveVal<bestObjectiveVal) isBest = true;
                    }
                    else if (initial.isMaximizeObjective()) {
                        objectiveVal = DoubleUtil.getMin(initial.getObjectiveExpression());
                        if (objectiveVal>bestObjectiveVal) isBest = true;
                        // Flip so that we can assume minimization during comparison later
                        objectiveVal = -objectiveVal;
                    } else {
                        throw new RuntimeException("must have a minimize or maximize objective");
                    }
                    
                    // Aspiration criteria: if it is better than the current best objective value,
                    // no need to test whether it satisfies metaheuristic (tabu) requirements.
                    // Check if neighbor is acceptable for metaheuristic:
                    if (!isBest && meta!=null && !meta.isAcceptableNeighbor(neighbor)) {
                        neighbor = null;
                    }

                    // Store in list of solutions along with objective value
                    if (neighbor != null) {
                        sols.add(neighbor);
                        solIndices.add(currentNeighbor);
                        solObjectives.add(objectiveVal);
                    }
                    
                } catch (PropagationFailureException e) {
                    neighbor = null;
                }
                store.restoreState(state);
            }

            currentNeighbor++;
        }
        
        // Earlier, we flipped objective values so that we can assume minimization here
        double bestObj = Double.MAX_VALUE;
        int bestNeighborIdx = -1;
        SolverSolution neighbor = null;
        for (int i=0; i<sols.size(); i++) {
            if (solObjectives.get(i)<bestObj) {
                bestObj = solObjectives.get(i);
                bestNeighborIdx = solIndices.get(i);
                neighbor = (SolverSolution) sols.get(i);
            }
        }
        
        if (neighbor == null && firstConsistentNeighbor != null) {
            neighbor = firstConsistentNeighbor;
            bestNeighborIdx = firstConsistentNeighborIdx;
            System.out.println("Boxed in: violating tabu criteria");
            ((TabuMetaheuristic) meta).reduceTabuLists();
        }
        
        // check if a neighboring solution was located
        if (neighbor==null)
            throw new PropagationFailureException();
        
        // create action to restore next neighbor
        SearchAction restore = new RestoreNeighbor(bestNeighborIdx, neighbor);
        
        return restore;
    }
    
    /**
     * Action that restores a neighboring solution
     */
    private class RestoreNeighbor implements SearchAction {
        private int index;
        private SolverSolution neighbor;
        
        protected RestoreNeighbor(int index, SolverSolution neighbor) {
            this.index = index;
            this.neighbor = neighbor;
        }
        
        public SearchAction performAction() throws PropagationFailureException {
            try {
                store.restoreNeighboringSolution(initial, neighbor);
                
                // update current neighbor
                current.setNeighborhood(hood);
                current.setMetaheuristic(meta);
                current.setSolution(neighbor);
                current.setIndex(index);
                
                return null;
            }
            catch (PropagationFailureException propx) {
                throw propx;
            }
        }
    }
}
