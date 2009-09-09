package jopt.csp.spi.solver;

/**
 * Interface to implement if an object wants to be notified of a choice point
 * data map being rolled back
 */
public interface ChoicePointDataMapListener {
    /**
     * Indicates that the current choice point is about to be rolled back and
     * domain data must be reset
     */
    public void beforeChoicePointPopEvent();

    /**
     * Indicates that the current choice point has been rolled back and
     * domain data must be reset
     */
    public void afterChoicePointPopEvent();

    /**
     * Indicates that current choice point is about to be
     * pushed to allow data to be recorded in stack
     */
    public void beforeChoicePointPushEvent();

    /**
     * Indicates that current choice point has been updated and
     * domain data must be restored
     */
    public void afterChoicePointPushEvent();
}