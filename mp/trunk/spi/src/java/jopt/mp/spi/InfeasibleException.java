package jopt.mp.spi;

/**
 * Thrown when problem is determined to be infeasible
 */
public class InfeasibleException extends Exception {
	public InfeasibleException(String msg) {
		super(msg);
	}
}
