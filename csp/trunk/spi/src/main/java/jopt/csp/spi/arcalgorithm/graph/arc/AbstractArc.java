package jopt.csp.spi.arcalgorithm.graph.arc;

import jopt.csp.variable.CspAlgorithmStrength;



public abstract class AbstractArc implements Arc {
    protected int strength          = CspAlgorithmStrength.BOUNDS_CONSISTENCY;
    protected boolean useDeltas     = true;
    
    /**
     * Sets the strength of the algorithm that is propagating the arc.  This
     * may affect how much filtering occurs by the constraint.
     */
    public void setAlgorithmStrength(int strength) {
    	this.strength = strength;
    }
    
    /**
     * Sets a flag indicating if the algorithm wants domain delta's to be
     * consulted when arc is propagating.  The default value for this
     * flag is true.
     */
    public void setUseDomainDeltas(boolean useDeltas) {
    	this.useDeltas = useDeltas;
    }

}