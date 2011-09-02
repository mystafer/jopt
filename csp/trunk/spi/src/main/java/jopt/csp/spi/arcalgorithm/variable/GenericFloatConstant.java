package jopt.csp.spi.arcalgorithm.variable;

import jopt.csp.spi.util.GenericIndex;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumConstants;
import jopt.csp.variable.CspGenericFloatConstant;
import jopt.csp.variable.CspGenericIndex;

/**
 * A generic float constant such as Xi representing 1.1, 7.3, and 12.8 for i=0, i=1, and i=2, respectively
 */
public class GenericFloatConstant extends GenericNumConstant implements CspGenericFloatConstant{

    /**
     * Constructor
     * 
     * @param name      unique name of this node
     * @param indices   array of indices that generic node is based upon
     * @param vals      array of vals that this generic constant wraps
     */
    public GenericFloatConstant(String name, CspGenericIndex indices[], Float vals[]) {
        super(name, indices, vals);
    }
    /**
     * Constructor
     * 
     * @param name      unique name of this node
     * @param indices   array of indices that generic node is based upon
     * @param vals      array of vals that this generic constant wraps
     */
    public GenericFloatConstant(String name, CspGenericIndex indices[], float vals[]) {
        super(name, indices, getObjectArray(vals));
    }
    
    /**
     * Method to convert an array of floats to an Object array
     */
    private static MutableNumber[] getObjectArray(float vals[]) {
        MutableNumber[] intObAr = new MutableNumber[vals.length];
        for (int i =0; i< vals.length; i++) {
            intObAr[i] = new MutableNumber(vals[i]);
        }
        return intObAr;
    }
    
    // javadoc inherited from GenericNumConstant
    public GenericNumConstant generateNumConstant(Number num) {
        return new GenericFloatConstant(null, new GenericIndex[]{new GenericIndex("",1)}, new Float[]{(Float)num});
    }
    
    // javadoc inherited from GenericNumConstant
    public GenericNumConstant generateNumConstant(String name, CspGenericIndex indices[], Number vals[]) {
        return new GenericFloatConstant(name, indices, toFloatArray(vals));
    }
    
    /**
     * Method to convert an array of Number objects to an array of Float objects
     */
    private static Float[] toFloatArray(Number vals[]) {
        if (vals==null) return null;
        Float[] intObAr = new Float[vals.length];
        for (int i =0; i< vals.length; i++) {
            intObAr[i] = (Float)vals[i];
        }
        return intObAr;
    }
    
    // javadoc inherited from CspGenericFloatConstant
    public Float getFloatForIndex() {
        // determine index of node to return
        int nodeIdx = 0;
        for (int i=0; i<indices.length; i++)
            nodeIdx += indices[i].currentVal() * indexOffsets[i];
        if (vals[nodeIdx] instanceof MutableNumber) {
            return (Float)((MutableNumber)vals[nodeIdx]).toConst();
        }
    	return (Float) new Float(vals[nodeIdx].floatValue());
    }
    
    
    // javadoc inherited from CspGenericNumConstant
    public int getNumberType() {
        return NumConstants.FLOAT;
    }
    
    // javadoc inherited from CspGenericFloatConstant
    public Float[] getFloatConstants() {
        return (Float[]) getNumConstants();
    }
    
    // javadoc inherited from CspGenericIntConstant
    public Float getMin() {
        if (getNumMin() instanceof MutableNumber){
            return (Float)((MutableNumber)getNumMin()).toConst();
        }
        else {
            return (Float)getNumMin();   
        }
    }

    // javadoc inherited from CspGenericIntConstant
    public Float getMax() {
        if (getNumMax() instanceof MutableNumber){
            return (Float)((MutableNumber)getNumMax()).toConst();
        }
        else {
            return (Float)getNumMax();   
        }
    }
        
    
    // javadoc inherited from CspGenericConstant
    public String toString() {
        name = name==null ? "~Float" : name;
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
