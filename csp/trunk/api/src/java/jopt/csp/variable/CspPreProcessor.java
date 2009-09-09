package jopt.csp.variable;


/**
 * Interface implemented by optional preprocessors used within the CspSolver.
 * Whenever a problem is reset, the preProcess method is called on any
 * <code>PreProcessor</code> associated to the CspSolver.
 * 
 * @author cjohnson
 */
public interface CspPreProcessor {
    /**
     * Performs any preprocessing necessary whenever a CSP is reset
     */
    public void preProcess() throws PropagationFailureException;
}