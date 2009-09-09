package jopt.csp.spi.solver;


/**
 * Event generated by variables when internal structure changes occur
 * (typically domain changes).
 *
 * @author Chris Johnson
 */
public class VariableChangeEvent {
	private Object var;
	
	public VariableChangeEvent(Object var) {
		this.var = var;
	}
	
	/**
	 * Returns the variable than generated the event
	 */
	public Object getVariable(){
		return this.var;
	}
}
