package jopt.csp.spi;

import jopt.csp.spi.arcalgorithm.NodeQueueAcAlg;
import jopt.csp.variable.CspAlgorithmStrength;

/**
 * Generic Arc Consistency algorithms that uses a node queue for propagation
 * 
 * Implementation is based upon AC5 which utilizes deltas of values removed from
 * domains to improve performance of filtering.
 * 
 * A Generic Arc-Consistency Algorithm and its Specifications [AC5]
 * P.V. Hentenryck, Y.Deville and C.M Teng, in Artificial Intelligence 57(2-3), pages 291-321, 1992. 
 */
public class AC5 extends NodeQueueAcAlg {
    /**
     * Creates an arc consistent strength algorithm
     */
    public AC5() {
        this(CspAlgorithmStrength.ARC_CONSISTENCY);
    }

    /**
     * Creates algorithm with specified strength
     * 
     * @see jopt.csp.variable.CspAlgorithmStrength
     */
    public AC5(int strength) {
        super(strength, true);
    }
}