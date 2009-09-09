package jopt.csp.spi.solver;

/**
 * Interface to implemented by choicepoint stack to be notified
 * when a map closes
 */
public interface ChoicePointDataMapCloseListener {
    public void mapClosedEvent(Integer mapID);
}