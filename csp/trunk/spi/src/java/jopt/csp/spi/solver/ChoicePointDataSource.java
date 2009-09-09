/*
 * Created on Jan 17, 2005
 */
package jopt.csp.spi.solver;

/**
 * @author ncoleman
 */
public interface ChoicePointDataSource {
    /**
     * Sets the choicepoint stack associated with this data source object
     */
    public void setChoicePointStack(ChoicePointStack cps);
    
    /**
     * Returns true if the choicepoint stack has been set
     */
    public boolean choicePointStackSet();
}