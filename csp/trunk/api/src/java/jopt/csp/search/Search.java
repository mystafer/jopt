package jopt.csp.search;

/**
 * The <code>Search</code> interface defines basic functionality related to searching.
 * Regardless of underlying implemenation, all searches must define the methods specified here.
 */
public interface Search extends Cloneable {
    /**
     * Sets the goal that this search should attempt to satisfy
     */
    public void setGoal(SearchGoal goal);
    
    /**
     * True indicates that each solution returned should be as good or better
     * than the last (determined by the goal that was given to the search).
     * False means that all solutions should be located
     * during the first solution search and only the best solutions should
     * be returned.  This method requires a search goal to be assigned
     * to the search to be effective.
     */
    public void setContinuallyImprove(boolean improve);

    /**
     * Searches the tree for solutions.
     * 
     * @return True indicates a solution has been found.  False means no (more) solutions exist.
     */
    public boolean nextSolution();
}
