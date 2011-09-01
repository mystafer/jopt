/*
 * LocalSearchImpl.java
 * 
 * Created on Jun 22, 2005
 */
package jopt.csp.spi.search;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import jopt.csp.search.CurrentNeighbor;
import jopt.csp.search.LocalSearch;
import jopt.csp.search.Metaheuristic;
import jopt.csp.search.Neighborhood;
import jopt.csp.search.RandomizedNeighborhood;
import jopt.csp.search.SearchAction;
import jopt.csp.search.SearchGoal;
import jopt.csp.search.UnifiedNeighborhood;
import jopt.csp.search.WeightedRandomizedNeighborhood;
import jopt.csp.solution.SolverSolution;
import jopt.csp.spi.search.localsearch.BrowseNeighborhoodAction;
import jopt.csp.spi.search.localsearch.BrowseNeighborsAction;
import jopt.csp.spi.search.localsearch.FlipNeighborhood;
import jopt.csp.spi.search.localsearch.ImproveSolutionAction;
import jopt.csp.spi.search.localsearch.NeighborMoveAction;
import jopt.csp.spi.search.localsearch.SelectCurrentNeighborAction;
import jopt.csp.spi.search.localsearch.SwapNeighborhood;
import jopt.csp.spi.search.localsearch.TabuMetaheuristic;
import jopt.csp.spi.search.localsearch.TabuMoveAction;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.variable.CspIntVariable;
import jopt.csp.variable.CspVariable;

/**
 * Creates and returns common local search objects that can be used
 * to build searches
 * 
 * @author Nick Coleman
 * @version $Revision: 1.10 $
 */
public class LocalSearchImpl implements LocalSearch {
    private ConstraintStore store;
    
    public LocalSearchImpl(ConstraintStore store) {
        this.store = store;
    }

    //clone constructor
    protected LocalSearchImpl(LocalSearchImpl lsi) {
        this.store = lsi.store;
    }
    
    /**
     * Adds all variables to the constraint store to ensure their states are
     * maintained correctly while searching
     */
    private void addVarsToCs(CspVariable vars[]) {
        for (int i=0; i<vars.length; i++)
            store.addVariable(vars[i], true);
    }

    /**
     * Adds all variables to the constraint store to ensure their states are
     * maintained correctly while searching
     */
    private void addVarsToCs(Collection<CspVariable> vars) {
        Iterator<CspVariable> varIter = vars.iterator();
        while (varIter.hasNext())
            store.addVariable((CspVariable) varIter.next(), true);
    }

    // javadoc inherited from LocalSearch
    public SearchAction browseNeighbors(SolverSolution initial, SolverSolution neighbors[]) {
        addVarsToCs(initial.variables());
        return new BrowseNeighborsAction(store, initial, Arrays.asList(neighbors));
    }
    
    // javadoc inherited from LocalSearch
    public SearchAction moveToNeighbor(SolverSolution initial, SolverSolution neighbor) {
        return browseNeighbors(initial, new SolverSolution[]{neighbor});
    }
    
    // javadoc inherited from LocalSearch
    public Neighborhood flipNeighborhood(CspIntVariable vars[]) {
        addVarsToCs(vars);
    	return new FlipNeighborhood(vars);
    }

    // javadoc inherited from LocalSearch
    public Neighborhood swapNeighborhood(CspIntVariable vars[]) {
        addVarsToCs(vars);
        return new SwapNeighborhood(vars);
    }
    
    // javadoc inherited from LocalSearch
    public SearchAction browseNeighborhood(SolverSolution initial, Neighborhood hood, CurrentNeighbor current) {
        addVarsToCs(initial.variables());
    	return new BrowseNeighborhoodAction(store, initial, hood, current);
    }

    // javadoc inherited from LocalSearch
    public Neighborhood unifiedNeighborhood(Neighborhood[] neighborhoods) {
        return new UnifiedNeighborhood(neighborhoods);
    }
    
    // javadoc inherited from LocalSearch
    public Neighborhood randomize(Neighborhood[] neighborhoods) {
        return new RandomizedNeighborhood(neighborhoods);
    }
    
    // javadoc inherited from LocalSearch
    public Neighborhood randomize(Neighborhood neighborhood) {
        return new RandomizedNeighborhood(neighborhood);
    }
    
    // javadoc inherited from LocalSearch
    public Neighborhood weightedRandomize(Neighborhood[] neighborhoods, double[] weights) {
        return new WeightedRandomizedNeighborhood(neighborhoods, weights);
    }
    
    // javadoc inherited from LocalSearch
    public SearchAction browseNeighborhood(SolverSolution initial, Neighborhood hood, Metaheuristic meta, CurrentNeighbor current) {
        addVarsToCs(initial.variables());
        return new BrowseNeighborhoodAction(store, initial, hood, meta, current);
    }
    
    // javadoc inherited from LocalSearch
    public SearchAction selectCurrentNeighbor(SolverSolution initial, CurrentNeighbor current) {
        addVarsToCs(initial.variables());
        return new SelectCurrentNeighborAction(store, initial, current);
    }
    
    // javadoc inherited from LocalSearch
    public SearchAction improve(SolverSolution solution, int step) {
        addVarsToCs(solution.variables());
    	return new ImproveSolutionAction(store, solution, step);
    }

    // javadoc inherited from LocalSearch
    public SearchAction improve(SolverSolution solution, float step) {
        return new ImproveSolutionAction(store, solution, step);
    }

    // javadoc inherited from LocalSearch
    public SearchAction improve(SolverSolution solution, long step) {
        return new ImproveSolutionAction(store, solution, step);
    }

    // javadoc inherited from LocalSearch
    public SearchAction improve(SolverSolution solution, double step) {
        return new ImproveSolutionAction(store, solution, step);
    }

    // javadoc inherited from LocalSearch
    public SearchAction neighborMove(SolverSolution solution, Neighborhood hood, SearchGoal goal) {
        return new NeighborMoveAction(store, solution, hood, goal);
    }

    // javadoc inherited from LocalSearch
    public SearchAction neighborMove(SolverSolution solution, Neighborhood hood, Metaheuristic meta, SearchGoal goal) {
        return new NeighborMoveAction(store, solution, hood, meta, goal);
    }

    // javadoc inherited from LocalSearch
    public SearchAction neighborMove(SolverSolution solution, Neighborhood hood) {
        return new NeighborMoveAction(store, solution, hood);
    }
    
    // javadoc inherited from LocalSearch
    public SearchAction neighborMove(SolverSolution solution, Neighborhood hood, Metaheuristic meta) {
        return new NeighborMoveAction(store, solution, hood, meta);
    }
    
    // javadoc inherited from LocalSearch
    public SearchAction tabuMove(SolverSolution solution, Neighborhood hood, SearchGoal goal, int numToCheck) {
        return new TabuMoveAction(store, solution, hood, goal, numToCheck);
    }

    // javadoc inherited from LocalSearch
    public SearchAction tabuMove(SolverSolution solution, Neighborhood hood, Metaheuristic meta, SearchGoal goal, int numToCheck) {
        return new TabuMoveAction(store, solution, hood, meta, goal, numToCheck);
    }

    // javadoc inherited from LocalSearch
    public SearchAction tabuMove(SolverSolution solution, Neighborhood hood, int numToCheck) {
        return new TabuMoveAction(store, solution, hood, numToCheck);
    }
    
    // javadoc inherited from LocalSearch
    public SearchAction tabuMove(SolverSolution solution, Neighborhood hood, Metaheuristic meta, int numToCheck) {
        return new TabuMoveAction(store, solution, hood, meta, numToCheck);
    }

    // javadoc inherited from LocalSearch
    public Metaheuristic tabu(int forbiddenUndoMoves, int forbiddenAlterMoves, double objectiveGap) {
        return new TabuMetaheuristic(store, forbiddenUndoMoves, forbiddenAlterMoves, objectiveGap);
    }
        
    // javadoc inherited from LocalSearch
    public Metaheuristic tabu(int forbiddenUndoMoves, double objectiveGap) {
        return new TabuMetaheuristic(store, forbiddenUndoMoves, objectiveGap);
    }
    
    // javadoc inherited from LocalSearch
    public Metaheuristic tabu(int forbiddenUndoMoves) {
        return new TabuMetaheuristic(store, forbiddenUndoMoves);
    }
}
