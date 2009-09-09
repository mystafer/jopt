package jopt.csp.variable;

public class PropagationFailureException extends Exception {
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