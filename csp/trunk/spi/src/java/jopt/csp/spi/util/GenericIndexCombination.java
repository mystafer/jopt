/*
 * GenericIndexCombination.java
 * 
 * Created on Jun 6, 2005
 */
package jopt.csp.spi.util;

/**
 * Stores a combination of values for a set of indices that can be compared
 * at with another combination to determine if the combinations are
 * equal
 */
public class GenericIndexCombination {
	private GenericIndex indices[];
    private int vals[];
    private int hashCode;
    
    public GenericIndexCombination(GenericIndex indices[]) {
    	this.indices = indices;
        this.vals = new int[indices.length];
        
        // store current value of each indices
        for (int i=0; i<indices.length; i++) {
            int v = indices[i].currentVal();
            vals[i] = v;
            hashCode += v;
        }
    }
    
    public int hashCode() {
        return hashCode;
    }
    
    // This has to be final because the way it's currently done with
    // instanceof instead of getClass means that if it is overridden
    // it will violate the reflexive contract for equals.
	public final boolean equals(Object obj) {
        if (obj==null) return false;
        if (!(obj instanceof GenericIndexCombination)) return false;
        
        GenericIndexCombination cmb = (GenericIndexCombination) obj;
        if (cmb.vals.length != vals.length || cmb.hashCode != this.hashCode) return false;
        
        for (int i=0; i<indices.length; i++) {
        	if (!indices[i].equals(cmb.indices[i])) return false;
            if (vals[i] != cmb.vals[i]) return false;
        }
        
        return true;
	}
}
