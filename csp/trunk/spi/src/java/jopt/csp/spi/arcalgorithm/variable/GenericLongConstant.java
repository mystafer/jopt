package jopt.csp.spi.arcalgorithm.variable;

import jopt.csp.spi.util.GenericIndex;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumConstants;
import jopt.csp.variable.CspGenericIndex;
import jopt.csp.variable.CspGenericLongConstant;

/**
 * A generic long constant such as Xi representing 1, 7, and 12 for i=0, i=1, and i=2, respectively
 */
public class GenericLongConstant extends GenericNumConstant implements CspGenericLongConstant{

    /**
     * Constructor
     * 
     * @param name      unique name of this node
     * @param indices   array of indices that generic node is based upon
     * @param vals      array of Long objects that this generic constant wraps
     */
    public GenericLongConstant(String name, CspGenericIndex indices[], Long vals[]) {
        super(name, indices, vals);
    }
    
    /**
     * Constructor
     * 
     * @param name      unique name of this node
     * @param indices   array of indices that generic node is based upon
     * @param vals      array of longs that this generic constant wraps
     */
    public GenericLongConstant(String name, CspGenericIndex indices[], long vals[]) {
        super(name, indices, getObjectArray(vals));
    }   
    
    /**
     * Converts an array of longs to an array of Long objects 
     */
    private static MutableNumber[] getObjectArray(long vals[]) {
        MutableNumber[] longObAr = new MutableNumber[vals.length];
        for (int i =0; i< vals.length; i++) {
            longObAr[i] = new MutableNumber(vals[i]);
        }
        return longObAr;
    }
    
    // javadoc inherited from CspGenericNumConstant
    public int getNumberType() {
        return NumConstants.LONG;
    }
    
    // javadoc inherited from CspGenericLongConstant
    public Long getLongForIndex() {
        // determine index of node to return
        int nodeIdx = 0;
        for (int i=0; i<indices.length; i++)
            nodeIdx += indices[i].currentVal() * indexOffsets[i];
        
        if (vals[nodeIdx] instanceof MutableNumber) {
            return (Long)((MutableNumber)vals[nodeIdx]).toConst();
        }
        
    	return (Long) new Long(vals[nodeIdx].longValue());
    }
    
    // javadoc inherited from GenericNumConstant
    public GenericNumConstant generateNumConstant(Number num) {
        return new GenericLongConstant(null, new GenericIndex[]{new GenericIndex("",1)}, new Long[]{(Long)num});
    }
    
    // javadoc inherited from GenericNumConstant
    public GenericNumConstant generateNumConstant(String name, CspGenericIndex indices[], Number vals[]) {
        return new GenericLongConstant(name, indices, toLongArray(vals));
    }
    
    /**
     * Method to convert an array of Number objects to an array of Long objects
     */
    private static Long[] toLongArray(Number vals[]) {
        if (vals==null) return null;
        Long[] intObAr = new Long[vals.length];
        for (int i =0; i< vals.length; i++) {
            intObAr[i] = (Long)vals[i];
        }
        return intObAr;
    }
    
    // javadoc inherited from CspGenericLongConstant
    public Long[] getLongConstants() {
        return (Long[]) getNumConstants();
    }
    
    // javadoc inherited from CspGenericIntConstant
    public Long getMin() {
        if (getNumMin() instanceof MutableNumber){
            return (Long)((MutableNumber)getNumMin()).toConst();
        }
        else {
            return (Long)getNumMin();   
        }
    }

    // javadoc inherited from CspGenericIntConstant
    public Long getMax() {
        if (getNumMax() instanceof MutableNumber){
            return (Long)((MutableNumber)getNumMax()).toConst();
        }
        else {
            return (Long)getNumMax();   
        }
    }

    // javadoc inherited from CspGenericConstant
    public String toString() {
        name = name==null ? "~Long" : name;
        StringBuffer buf = new StringBuffer(name);
        buf.append(":[");
        for (int i=0; i<indices.length; i++) {
            if (i>0) buf.append(",");
            buf.append(indices[i].getName());
        }
        buf.append("]");
        
        return buf.toString();
    }
}
