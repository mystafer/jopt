package jopt.csp.spi;

import jopt.csp.spi.arcalgorithm.ArcQueueAcAlg;
import jopt.csp.variable.CspAlgorithmStrength;

/**
 * Arc consistent algorithm based on the AC3 algorithm
 * 
 * Consistency in networks of Relations [AC1-3]
 * A.K. Mackworth, in Artificial Intelligence 8, 99-118, 1977. 
 */
public class AC3 extends ArcQueueAcAlg {
    /**
     * Creates an arc consistent strength algorithm
     */
    public AC3() {
        this(CspAlgorithmStrength.ARC_CONSISTENCY);
    }
    
    /**
     * Creates algorithm with specified strength
     * 
     * @see jopt.csp.variable.CspAlgorithmStrength
     */
    public AC3(int strength) {
        super(strength);
    }
}