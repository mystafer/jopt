package jopt.csp.spi.arcalgorithm.variable;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import jopt.csp.spi.util.GenericIndex;
import jopt.csp.variable.CspGenericBooleanConstant;
import jopt.csp.variable.CspGenericIndex;

/**
 * A generic constant that acts as an array of booleans, but can
 * be indexed over custom indexes
 * 
 * @author Jim Boerkoel
 */
public class GenericBooleanConstant implements CspGenericBooleanConstant, GenericConstant {
    protected GenericIndex indices[];
    protected int indexOffsets[];
    protected boolean vals[];
    protected String name;
    /**
     * Constructor
     * 
     * @param name      unique name of this node
     * @param indices   array of indices that generic node is based upon
     * @param vals     array of vals that this generic node wraps
     */
    public GenericBooleanConstant(String name, CspGenericIndex indices[], boolean vals[]) {
        this.indices = VarFactory.toGenericIndex(indices);
        this.vals = vals;
        this.name = name;
        
        // calculate index offsets and total vals required
        indexOffsets = new int[indices.length];
        indexOffsets[indices.length-1] = 1;
        int totalVals = indices[indices.length-1].size();
        for (int i=indices.length-2; i>=0; i--) {
            // offset will equal product of previous index size * previous index offset
            int prevIdx = i+1;
            indexOffsets[i] = indices[prevIdx].size() * indexOffsets[prevIdx];
            
            // total vals is product of all sizes
            totalVals *= indices[i].size();
        }
        
        // ensure correct number of vals are given for indices
        if (totalVals != vals.length) {
            throw new IllegalStateException("Expected " + totalVals + " vals, but received " + vals.length);
        }
        
    }
        
    // javadoc inherited from CspGenericConstant interface
    public CspGenericIndex[] getIndices() {
    	return indices;
    }

    // javadoc inherited from CspGenericBooleanConstant interface
    public CspGenericBooleanConstant generateBooleanConstant(boolean bool) {
        return new GenericBooleanConstant(null, new GenericIndex[]{new GenericIndex("",1)}, new boolean[]{bool});
    }
    
    // javadoc inherited from CspGenericBooleanConstant interface
    public boolean getBooleanForIndex() {
        // determine index of node to return
        int nodeIdx = 0;
        for (int i=0; i<indices.length; i++)
            nodeIdx += indices[i].currentVal() * indexOffsets[i];
        
    	return vals[nodeIdx];
    }
    
    // javadoc inherited from CspGenericConstant interface
    public int getConstantCount() {
        return vals.length;
    }
    
    // javadoc inherited from CspGenericConstant interface
    public boolean containsIndex(CspGenericIndex index) {
    	for (int i=0; i<indices.length; i++)
            if (index.equals(indices[i]))
                return true;
            
        return false;
    }
    
    // javadoc inherited from CspGenericBooleanConstant interface
    public boolean isAnyFalse() {
        for (int i=0; i< vals.length; i++) {
            if (!vals[i]) {
                return true;
            }
        }
        return false;
    }
    
    // javadoc inherited from CspGenericBooleanConstant interface
    public boolean isAnyTrue() {
        for (int i=0; i< vals.length; i++) {
            if (vals[i]) {
                return true;
            }
        }
        return false;
    }
    
    // javadoc inherited from CspGenericBooleanConstant interface
    public String getName() {
        return name;
    }
    
    // javadoc inherited from CspGenericBooleanConstant interface    
    public CspGenericBooleanConstant createFragment(CspGenericIndex fragIndices[]) {
        // build list of indices that are not contained in expression
        List remainingIdxList = new LinkedList(Arrays.asList(indices));
        List fragIdxList = Arrays.asList(fragIndices);
        remainingIdxList.removeAll(fragIdxList);
        
        // if all indices are used for variable, return the specific
        // expression at the given indice combination
        if (remainingIdxList.size()==0) {
            return generateBooleanConstant(getBooleanForIndex());
        }
        
        // no indices are used, return this expression as a whole
        else if (remainingIdxList.size() == indices.length) {
        	return this;
        }
        
        //TODO:  Do something more useful than this
        return null;
    }
    
    // javadoc inherited from CspGenericBooleanConstant interface
    public boolean[] getBooleanConstants() {
        return vals;
    }

    // javadoc inherited from CspGenericBooleanConstant interface
    public boolean contains(boolean bool) {
        for (int i=0; i< vals.length; i++) {
            if (vals[i]==(bool)) return true;
        }
        return false;
    }
    
    public GenericBooleanConstant getOpposite() {
        boolean oppVals[] = new boolean[vals.length];
        for (int i=0; i< vals.length; i++) {
            oppVals[i]=!vals[i];
        }
        return new GenericBooleanConstant(name+"Opp",indices, oppVals);
    }
    
    
    // javadoc inherited from CspGenericConstant interface
    public void setIndicesToNodeOffset(int offset) {
    	for (int i=0; i<indexOffsets.length; i++) {
            int idxOffset = indexOffsets[i];
    		int idxVal = offset / idxOffset;
            
            // update index
            indices[i].changeVal(idxVal);
            
            // strip current index from offset
            offset %= idxOffset;
        }
    }
    
    // javadoc inherited from CspGenericConstant
    public String toString() {
        name = name==null ? "~Boolean" : name;
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
