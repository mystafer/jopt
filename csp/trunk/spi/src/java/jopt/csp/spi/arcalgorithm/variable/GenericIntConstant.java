package jopt.csp.spi.arcalgorithm.variable;

import jopt.csp.spi.util.GenericIndex;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumConstants;
import jopt.csp.variable.CspGenericIndex;
import jopt.csp.variable.CspGenericIntConstant;

/**
 * A generic integer constant such as Xi representing 1, 7, and 12 for i=0, i=1, and i=2, respectively
 */
public class GenericIntConstant extends GenericNumConstant implements CspGenericIntConstant {

    /**
     * Constructor
     * 
     * @param name      unique name of this generic constant
     * @param indices   array of indices that generic int constant is based upon
     * @param vals		array of Integer objects that this generic constant wraps
     */
    public GenericIntConstant(String name, CspGenericIndex indices[], Integer vals[]) {
        super(name, indices, vals);
    }
    
    /**
     * Constructor
     * 
     * @param name      unique name of this generic constant
     * @param indices   array of indices that generic int constant is based upon
     * @param vals	    array of ints that this generic constant wraps
     */
    public GenericIntConstant(String name, CspGenericIndex indices[], int vals[]) {
        super(name, indices, getObjectArray(vals));
    }   
    
    /**
     * Converts an array of ints to an array of MutableNumber objects 
     */
    private static MutableNumber[] getObjectArray(int vals[]) {
        MutableNumber[] intObAr = new MutableNumber[vals.length];
        for (int i =0; i< vals.length; i++) {
            intObAr[i] = new MutableNumber(vals[i]);
        }
        return intObAr;
    }
    
    // javadoc inherited from CspGenericNumConstant
    public int getNumberType() {
        return NumConstants.INTEGER;
    }
    
    // javadoc inherited from CspGenericIntConstant
    public Integer getIntegerForIndex() {
        // determine index of node to return
        int nodeIdx = 0;
        for (int i=0; i<indices.length; i++)
            nodeIdx += indices[i].currentVal() * indexOffsets[i];
        if (vals[nodeIdx] instanceof MutableNumber) {
            return (Integer)((MutableNumber)vals[nodeIdx]).toConst();
        }
    	return (Integer) new Integer(vals[nodeIdx].intValue());
    }
    
    // javadoc inherited from GenericNumConstant
    public GenericNumConstant generateNumConstant(Number num) {
        if(num instanceof MutableNumber) {
            return new GenericIntConstant(null, new GenericIndex[]{new GenericIndex("",1)}, new Integer[]{(Integer)((MutableNumber)num).toConst()});
        }
        else {
            return new GenericIntConstant(null, new GenericIndex[]{new GenericIndex("",1)}, new Integer[]{(Integer)num});
        }
    }
    
    // javadoc inherited from GenericNumConstant
    public GenericNumConstant generateNumConstant(String name, CspGenericIndex indices[], Number vals[]) {
        return new GenericIntConstant(name, indices, toIntegerArray(vals));
    }
    
    /**
     * Method to convert an array of Number objects to an array of Integer objects
     */
    private static Integer[] toIntegerArray(Number vals[]) {
        if (vals==null) return null;
        Integer[] intObAr = new Integer[vals.length];
        for (int i=0; i<vals.length; i++) {
            if (vals[i] instanceof MutableNumber) {
                intObAr[i] = (Integer)(((MutableNumber)vals[i]).toConst());
            }
            else {
                intObAr[i] = (Integer)vals[i];
            }
        }
        return intObAr;
    }
    
    // javadoc inherited from CspGenericIntConstant
    public Integer[] getIntegerConstants() {
        return (Integer[]) getNumConstants();
    }
    
    // javadoc inherited from CspGenericIntConstant
    public Integer getMin() {
        if (getNumMin() instanceof MutableNumber){
            return (Integer)((MutableNumber)getNumMin()).toConst();
        }
        else {
            return (Integer)getNumMin();   
        }
    }

    // javadoc inherited from CspGenericIntConstant
    public Integer getMax() {
        if (getNumMax() instanceof MutableNumber){
            return (Integer)((MutableNumber)getNumMax()).toConst();
        }
        else {
            return (Integer)getNumMax();   
        }
    }

    // javadoc inherited from CspGenericConstant
    public String toString() {
        name = name==null ? "~Int" : name;
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
