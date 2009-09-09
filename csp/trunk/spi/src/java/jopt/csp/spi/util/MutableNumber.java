/*
 * MutableNumber.java
 * 
 * Created on Nov 20, 2005
 */
package jopt.csp.spi.util;


/**
 * Number object where values can be changed
 */
public class MutableNumber extends Number implements Comparable {
	private int type;
    private int intVal;
    private long longVal;
    private float floatVal;
    private double doubleVal;
    private boolean invalid;
    
    public MutableNumber() {
    }
    
    public MutableNumber(Number numVal) {
        if (numVal instanceof MutableNumber) {
            numVal = ((MutableNumber)numVal).toConst();
        }
        if (numVal instanceof Integer) {
            this.setIntValue(numVal.intValue());
        }
        else if (numVal instanceof Double) {
            this.setDoubleValue(numVal.doubleValue());
        }
        else if (numVal instanceof Float) {
            this.setFloatValue(numVal.floatValue());
        }
        else if (numVal instanceof Long) {
            this.setLongValue(numVal.longValue());
        }
    }
    
    public MutableNumber(int intVal) {
        this.setIntValue(intVal);
    }
    
    public MutableNumber(long longVal) {
        this.setLongValue(longVal);
    }
    
    public MutableNumber(float floatVal) {
        this.setFloatValue(floatVal);
    }
    
    public MutableNumber(double doubleVal) {
        this.setDoubleValue(doubleVal);
    }
    
    /**
     * Returns type of value stored in this number
     */
    public int getType() {
    	return type;
    }
    
    // javadoc inherited
    public int intValue() {
        switch(type) {
            case NumConstants.INTEGER:
                return intVal;
            
            case NumConstants.LONG:
                return (int) longVal;
            
            case NumConstants.FLOAT:
                return (int) floatVal;
            
            default:
                return (int) doubleVal;
        }
    }

    // javadoc inherited
    public long longValue() {
        switch(type) {
            case NumConstants.INTEGER:
                return intVal;
            
            case NumConstants.LONG:
                return longVal;
            
            case NumConstants.FLOAT:
                return (long) floatVal;
            
            default:
                return (long) doubleVal;
        }
    }
    
    // javadoc inherited
    public float floatValue() {
        switch(type) {
            case NumConstants.INTEGER:
                return intVal;
            
            case NumConstants.LONG:
                return longVal;
            
            case NumConstants.FLOAT:
                return floatVal;
            
            default:
                return (float) doubleVal;
        }
    }

	// javadoc inherited
	public double doubleValue() {
        switch(type) {
            case NumConstants.INTEGER:
                return intVal;
            
            case NumConstants.LONG:
                return longVal;
            
            case NumConstants.FLOAT:
                return floatVal;
            
            default:
                return doubleVal;
        }
	}

    /**
     * Returns the constant java.lang.Number equivalent value
     */
    public Number toConst() {
        switch(type) {
            case NumConstants.INTEGER:
                return new Integer(intVal);
            
            case NumConstants.LONG:
                return new Long(longVal);
            
            case NumConstants.FLOAT:
                return new Float(floatVal);
            
            default:
                return new Double(doubleVal);
        }
    }
    
	/**
     * Sets integer value for number
	 */
    public void setIntValue(int intVal) {
    	this.type = NumConstants.INTEGER;
        this.intVal = intVal;
        this.invalid = false;
    }

    /**
     * Sets long value for number
     */
    public void setLongValue(long longVal) {
        this.type = NumConstants.LONG;
        this.longVal = longVal;
        this.invalid = false;
    }

    /**
     * Sets float value for number
     */
    public void setFloatValue(float floatVal) {
        this.type = NumConstants.FLOAT;
        this.floatVal = floatVal;
        this.invalid = false;
    }

    /**
     * Sets float value for number
     */
    public void setFloatValue(double floatVal) {
        this.type = NumConstants.FLOAT;
        this.floatVal = (float) floatVal;
        this.invalid = false;
    }

    /**
     * Sets double value for number
     */
    public void setDoubleValue(double doubleVal) {
        this.type = NumConstants.DOUBLE;
        this.doubleVal = doubleVal;
        this.invalid = false;
    }
    
    /**
     * Returns true if value is not a number
     */
    public boolean isInvalid() {
    	return invalid;
    }
    
    /**
     * Sets invalid flag
     */
    public void setInvalid(boolean invalid) {
    	this.invalid = invalid;
    }
    
    /**
     * Returns true if values in not a number
     */
    public boolean isNaN() {
    	if (isInvalid()) return true;
        
        switch(type) {
            case NumConstants.FLOAT:
                return Float.isNaN(floatVal);
                
            case NumConstants.DOUBLE:
                return Double.isNaN(doubleVal);
        }
        
        return false;
    }
    
    /**
     * Sets this number to an equivalent value of another number
     */
    public void set(Number n) {
        NumberMath.numberType(n);
        
        switch(type) {
            case NumConstants.INTEGER:
                this.intVal = n.intValue();
            
            case NumConstants.LONG:
                this.longVal = n.longValue();
            
            case NumConstants.FLOAT:
                this.floatVal = n.floatValue();
            
            default:
                this.doubleVal = n.doubleValue();
        }
    }
    
    public boolean equals(Object obj) {
    	if (obj instanceof MutableNumber) {
    		MutableNumber n = (MutableNumber) obj;
            
            if (n.type == this.type) {
                switch(type) {
                    case NumConstants.INTEGER:
                        return this.intVal == n.intVal;
                    
                    case NumConstants.LONG:
                        return this.longVal == n.longVal;
                    
                    case NumConstants.FLOAT:
                        return this.floatVal == n.floatVal;
                    
                    default:
                        return this.doubleVal == n.doubleVal;
                }
            }
        }
    	else if (obj instanceof Integer) {
    	    return this.intVal == ((Integer)obj).intValue();
    	}
    	else if (obj instanceof Long) {
    	    return this.longVal == ((Long)obj).longValue();
    	}
    	else if (obj instanceof Double) {
    	    return this.doubleVal == ((Double)obj).doubleValue();
    	}
    	else if (obj instanceof Float) {
    	    return this.floatVal == ((Float)obj).floatValue();
    	}
        
        return false;
    }
    
    public boolean equals(Object obj, double precision) {
    	if (obj instanceof MutableNumber) {
    		MutableNumber n = (MutableNumber) obj;
            
            if (n.type == this.type) {
                switch(type) {
                    case NumConstants.INTEGER:
                        return equals(obj);
                    
                    case NumConstants.LONG:
                        return equals(obj);
                    
                    case NumConstants.FLOAT:
                        return ((this.floatVal <= n.floatVal+Math.abs(precision))
                                &&(this.floatVal >= n.floatVal-Math.abs(precision)));
                    
                    default:
                        return ((this.doubleVal <= n.doubleVal+Math.abs(precision))
                                &&(this.doubleVal >= n.doubleVal-Math.abs(precision)));
                }
            }
        }
        
        return false;
    }
    
    public String toString() {
        switch(type) {
            case NumConstants.INTEGER:
                return Integer.toString(intVal);
            
            case NumConstants.LONG:
                return Long.toString(longVal);
            
            case NumConstants.FLOAT:
                return Float.toString(floatVal);
            
            default:
                return Double.toString(doubleVal);
        }
    }
    
    public int compareTo(Object o1){
        Number n1 = (Number) o1;
        Number n2 = (Number) this;
        if ((n2.doubleValue()- n1.doubleValue())<0) {
            return -1;
        }
        else if ((n2.doubleValue()- n1.doubleValue())==0) {
            return 0;
        }
        else {
            return 1;
        }
    }
    
    /**
     * Returns a hash code for this <code>Mutable</code> object. The
     * result is the exact same hash Code as <code>Integer</code>, <code>Long</code>,
     * <code>Float</code>, <code>Double</code>
     *
     * @return a hash code value for this object.  
     */
    public int hashCode() {
        switch(type) {
            case NumConstants.INTEGER:
                return intVal;
            
            case NumConstants.LONG:
                return (int)(longVal ^ (longVal >>> 32));
            
            case NumConstants.FLOAT:
                return Float.floatToIntBits(floatVal);
            
            default:
            	long bits = Double.doubleToLongBits(doubleVal);
        		return (int)(bits ^ (bits >>> 32));
        }
    }
}
