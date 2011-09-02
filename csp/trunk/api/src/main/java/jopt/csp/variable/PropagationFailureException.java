package jopt.csp.variable;

public class PropagationFailureException extends Exception {
    private static final long serialVersionUID = 1L;
	
	public PropagationFailureException() {
        super();
    }
    public PropagationFailureException(String msg) {
        super(msg);
    }
    public PropagationFailureException(String msg, Throwable cause) {
        super(msg, cause);
    }
}