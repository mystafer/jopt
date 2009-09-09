package jopt.csp.variable;

/**
 * Interface implemented by a class that represents a subnotation of a generic
 * variable like Xi or Zj
 */
public interface CspGenericIndex {
	/**
	 * Returns the name of this index
	 * @return	name of this index
	 */
	public String getName();

	/**
	 * Returns the size of the index
	 * @return size of this index
	 */
	public int size();
	
    /**
     * Sets the current value of the index
     * @param	v	value to set this index to
     */
    public void changeVal(int v) ;
    
    /**
     * Returns the current numeric value of the index
     * @return	current value of this index
     */
    public int currentVal();
    
    
}