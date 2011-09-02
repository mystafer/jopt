package jopt.csp.variable;

/**
 * Interface for CspGenericNumConstant objects
 * 
 * @author jboerkoel
 */
public interface CspGenericNumConstant extends CspGenericConstant {
    
    /**
     * Returns the constant at the current index value as a Number object
     * @return	Number object at the current index value
     */
    public Number getNumberForIndex();
    
    /** 
     * Returns a value representing the type of constants this generic constant contains
     * @return		constant representing number type
     */
    public int getNumberType();

    /**
     * Returns the constant at the specified offset as a Number object
     * @param	offset	index offset of which to obtain the number
     * @return	Number object at the index value offset
     */
    public Number getNumber(int offset);

    /**
     * Returns a CspGenericNumConstant based on the specified indices
     * @param	fragIndices		array of indices and values to fragment over
     * @return	generic num constant restricted to only the indice values in fragIndices
     */
    public CspGenericNumConstant createFragment(CspGenericIndex fragIndices[]);

    /**
     * Returns all the constants wrapped by this generic constant as an array of Number objects
     * @return	All number objects wrapped by this generic constant
     */
    public Number[] getNumConstants();

    /**
     * Returns true if the given number is one of the constants contained in the generic constant
     * @param	num		Number object to see if it is contained in the generic constants
     * @return	true if one of the generic constants is equivalent to num
     */
    public boolean contains(Number num);

    /**
     * Returns the minimal value of all the constant values
     * @return	Number object representing the constant with the minimum value
     */
    public Number getNumMin();

    /**
     * Returns the maximum value of all the constant values
     * @return	Number object representing the constant with the maximum value
     */
    public Number getNumMax();

    /**
     *  Creates a new GenericNumConstant representing this+num for each value of this.
     * @param	num		Number constant to add to this constant
     * @return	generic constant representing this + num 
     */
    public CspGenericNumConstant add(Number num);

    /**
     * Creates a new GenericNumConstant representing this+num for each value of this
     * and each associated value of the specified CspGenericNumConstant.  
     * @param	num		generic number constant to add to this constant
     * @return	generic constant representing this + num 
     */
    public CspGenericNumConstant add(CspGenericNumConstant num);
    
    /**
     *  Creates a new GenericNumConstant representing num-this for each value of this.
     * @param	num		number constant to subtract this constant from
     * @return	generic constant representing num - this 
     */
    public CspGenericNumConstant subtractFrom(Number num);
    
    /**
     * Creates a new GenericNumConstant representing num-this for each value of this
     * and each associated value of the specified CspGenericNumConstant.  
     * @param	num		generic number constant to subtract this constant from
     * @return	generic constant representing num - this 
     */
    public CspGenericNumConstant subtractFrom(CspGenericNumConstant num);
    
    /**
     *  Creates a new GenericNumConstant representing this-num for each value of this.
     * @param	num		number constant to subtract from this constant
     * @return	generic constant representing this-num 
     */
    public CspGenericNumConstant subtract(Number num);
    
    /**
     * Creates a new GenericNumConstant representing this-num for each value of this
     * and each associated value of the specified CspGenericNumConstant.  
     * @param	num		generic number constant to subtract from this constant
     * @return	generic constant representing this-num
     */
    public CspGenericNumConstant subtract(CspGenericNumConstant num);
    
    /**
     *  Creates a new GenericNumConstant representing num*this for each value of this.
     * @param	num		number constant to multiply by this constant
     * @return	generic constant representing this*num 
     */
    public CspGenericNumConstant multiply(Number num);
    
    /**
     * Creates a new GenericNumConstant representing num*this for each value of this
     * and each associated value of the specified CspGenericNumConstant.  
     * @param	num		generic number constant to multiply by this constant
     * @return	generic constant representing this*num
     */
    public CspGenericNumConstant multiply(CspGenericNumConstant num);
    
    /**
     *  Creates a new GenericNumConstant representing ceil(this/num) for each value of this.
     * @param	num		number constant to divide this constant by
     * @return	generic constant representing ceil[this/num] 
     */
    public CspGenericNumConstant divideByCeil(Number num);
    
    /**
     * Creates a new GenericNumConstant representing ceil(this/num) for each value of this
     * and each associated value of the specified CspGenericNumConstant.  
     * @param	num		generic number constant to divide this constant by
     * @return	generic constant representing ceil[this/num]
     */
    public CspGenericNumConstant divideByCeil(CspGenericNumConstant num);
    
    /**
     *  Creates a new GenericNumConstant representing floor(this/num) for each value of this.
     * @param	num		number constant to divide this constant by
     * @return	generic constant representing floor[this/num]  
     */
    public CspGenericNumConstant divideByFloor(Number num);
    
    /**
     * Creates a new GenericNumConstant representing floor(this/num) for each value of this
     * and each associated value of the specified CspGenericNumConstant.  
     * @param	num		generic number constant to divide this constant by
     * @return	generic constant representing floor[this/num]
     */
    public CspGenericNumConstant divideByFloor(CspGenericNumConstant num);
    
    /**
     *  Creates a new GenericNumConstant representing this/num for each value of this.
     * @param	num		generic number constant to divide this constant by
     * @return	generic constant representing this/num 
     */
    public CspGenericNumConstant divideBy(Number num);
    
    /**
     * Creates a new GenericNumConstant representing this/num for each value of this
     * and each associated value of the specified CspGenericNumConstant.  
     * @param	num		generic number constant to divide this constant by
     * @return	generic constant representing this/num
     */
    public CspGenericNumConstant divideBy(CspGenericNumConstant num);
    
    /**
     *  Creates a new GenericNumConstant representing num/this for each value of this.
     * @param	num		number constant to divide by this constant
     * @return	generic constant representing num/this 
     */
    public CspGenericNumConstant divide(Number num);
    
    /**
     * Creates a new GenericNumConstant representing num/this for each value of this
     * and each associated value of the specified CspGenericNumConstant.
     * @param	num		generic number constant to divide by this constant
     * @return	generic constant representing num/this  
     */
    public CspGenericNumConstant divide(CspGenericNumConstant num);
    
    /**
     *  Creates a new GenericNumConstant representing ceil(num/this) for each value of this.
     * @param	num		number constant to divide by this constant
     * @return	generic constant representing ceil[num/this] 
     */
    public CspGenericNumConstant divideCeil(Number num);
    
    /**
     * Creates a new GenericNumConstant representing ceil(num/this) for each value of this
     * and each associated value of the specified CspGenericNumConstant.  
     * @param	num		generic number constant to divide by this constant
     * @return	generic constant representing ceil[num/this]
     */
    public CspGenericNumConstant divideCeil(CspGenericNumConstant num);
    
    /**
     *  Creates a new GenericNumConstant representing floor(num/this) for each value of this.
     * @param	num		number constant to divide by this constant
     * @return	generic constant representing floor[num/this] 
     */
    public CspGenericNumConstant divideFloor(Number num);
    
    /**
     * Creates a new GenericNumConstant representing floor(num/this) for each value of this
     * and each associated value of the specified CspGenericNumConstant.  
     * @param	num		generic number constant to divide by this constant
     * @return	generic constant representing floor[num/this]
     */
    public CspGenericNumConstant divideFloor(CspGenericNumConstant num);
}