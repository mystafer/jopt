package jopt.csp.spi.solver;

/**
 * Interface to implement in order to receive events related to choice point actions
 */
public interface ChoicePointEventListener {
    /**
     * Method invoked when choice point pop event is fired
     */
    public void choicePointPop();
    
    /**
     * Method invoked when choice point push event is fired
     */
    public void choicePointPush();
}