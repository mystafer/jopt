package jopt.csp.spi.arcalgorithm.variable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import jopt.csp.spi.util.GenericIndex;
import jopt.csp.spi.util.IndexIterator;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumConstants;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.variable.CspGenericIndex;
import jopt.csp.variable.CspGenericNumConstant;

/**
 * A generic constant that acts as an array of numbers, but can
 * be indexed over custom indexes
 * 
 * @author Jim Boerkoel
 */
public abstract class GenericNumConstant implements CspGenericNumConstant, GenericConstant {
    protected GenericIndex indices[];
    protected int indexOffsets[];
    protected MutableNumber vals[];
    protected String name;
    
    /**
     * Constructor
     * 
     * @param name      unique name of this constant
     * @param indices   array of indices that generic constant is based upon
     * @param vals		array of values that this generic constant wraps
     */
    protected GenericNumConstant(String name, CspGenericIndex indices[], Number vals[]) {
        this.indices = VarFactory.toGenericIndex(indices);
        this.vals = new MutableNumber[vals.length];
        for (int i=0; i<vals.length; i++) {
            this.vals[i] = new MutableNumber(vals[i]);
        }
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

    // javadoc inherited from CspGenericNumConstant interface
    public Number getNumberForIndex() {
        // determine index of node to return
        int nodeIdx = 0;
        for (int i=0; i<indices.length; i++)
            nodeIdx += indices[i].currentVal() * indexOffsets[i];
        
    	return vals[nodeIdx];
    }

    /**
     * Method that allows setting number according to the current index instead of passing
     * it into the initial array of data upon construction
     * @param num		The number representing the new value for the current index
     */
    protected void setNumberForIndex(Number num) {
        // determine index of node to set
        int nodeIdx = 0;
        for (int i=0; i<indices.length; i++)
            nodeIdx += indices[i].currentVal() * indexOffsets[i];
        
    	vals[nodeIdx]=(MutableNumber)num;
    }
    
    
    // javadoc inherited from CspGenericConstant interface
    public int getConstantCount() {
        return vals.length;
    }

    // javadoc inherited from CspGenericNumConstant interface
    public Number getNumber(int offset) {
        if (offset < 0 || offset >= vals.length) return null;
    	return vals[offset];
    }

    // javadoc inherited from CspGenericConstant interface
    public boolean containsIndex(CspGenericIndex index) {
    	for (int i=0; i<indices.length; i++)
            if (index.equals(indices[i]))
                return true;
            
        return false;
    }
    
    // javadoc inherited from CspGenericNumConstant interface
    public CspGenericNumConstant createFragment(CspGenericIndex fragIndices[]) {
        // build list of indices that are not contained in expression
        List remainingIdxList = new LinkedList(Arrays.asList(indices));
        List fragIdxList = Arrays.asList(fragIndices);
        remainingIdxList.removeAll(fragIdxList);
        
        // if all indices are used for variable, return the specific
        // expression at the given indice combination
        if (remainingIdxList.size()==0) {
            return generateNumConstant(getNumberForIndex());
        }
        
        // no indices are used, return this expression as a whole
        else if (remainingIdxList.size() == indices.length) {
        	return this;
        }
        
        //TODO:  Do something more useful than this
        return null;
    }

    // javadoc inherited from CspGenericNumConstant interface
    public Number[] getNumConstants() {
        return vals;
    }

    // javadoc inherited from CspGenericNumConstant interface
    public boolean contains(Number num) {
        for (int i=0; i< vals.length; i++) {
            if (vals[i].equals(num)) return true;
        }
        return false;
    }
    
    // javadoc inherited from CspGenericNumConstant interface
    public Number getNumMin() {
        double possMin = vals[0].doubleValue();
        int index=0;
        for (int i=0; i< vals.length; i++) {
            if (possMin > vals[i].doubleValue()) {
                possMin = vals[i].doubleValue();
                index = i;
            }
        }
        return vals[index];
    }

    // javadoc inherited from CspGenericNumConstant interface
    public Number getNumMax() {
        double possMax = vals[0].doubleValue();
        int index=0;
        for (int i=0; i< vals.length; i++) {
            if (possMax < vals[i].doubleValue()) {
                possMax = vals[i].doubleValue();
                index = i;
            }
        }
        return vals[index];
    }
        
    
    // javadoc inherited from CspGenericNumConstant interface
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
    
    /**
     * Generates a GenericNumConstant based on only one number
     * @param    num     number to base singly indexed constant on
     * @return   a generic num constant that has a single value
     */
    public abstract GenericNumConstant generateNumConstant(Number num);
    
    /**
     * Create a new GenericNumConstant with the given indices and constant values
     * 
     * @param name		Name of the new generic constant
     * @param indices	Indices on which the generic constant is based
     * @param vals		Array of constant values that the generic constant will wrap
     * @return			A new CspGenericNumConstant created according to the specified parameters
     */
    public abstract GenericNumConstant generateNumConstant(String name, CspGenericIndex indices[], Number vals[]);
    
    // javadoc inherited from CspGenericNumConstant interface
    public CspGenericNumConstant add(Number num){
        MutableNumber newConsts[] = new MutableNumber[vals.length];
        for (int i=0; i< vals.length; i++) {
            MutableNumber n = new MutableNumber();
            NumberMath.addNoInvalid(vals[i], num, this.getNumberType(), n);
            newConsts[i] = n;
        }
        return generateNumConstant(name+"+"+num,indices,newConsts);
    }
    
    // javadoc inherited from CspGenericNumConstant interface
    public CspGenericNumConstant add(CspGenericNumConstant num){
        return operate(num, NumConstants.ADD);
    }
    
    /**
     *  Method that returns a CspGenericConst representing this ? num
     *  where ? = operation passed into problem
     */ 
    private CspGenericNumConstant operate(CspGenericNumConstant num, int operation){
        CspGenericIndex[] bIndices = getIndices();
        CspGenericIndex[] aIndices = num.getIndices();
        HashSet indices = new HashSet(Arrays.asList(aIndices));
        indices.addAll(Arrays.asList(bIndices));
        CspGenericIndex newIndex[] = (CspGenericIndex[])indices.toArray(new CspGenericIndex[0]);
        int newSize = 1;
        for (int i=0; i<newIndex.length; i++) {
            newSize *= newIndex[i].size();
        }
        GenericNumConstant newNumConst = (GenericNumConstant)generateNumConstant(name+"+"+num,newIndex,new Number[newSize]);
        
        //IndexIterator iterator = new IndexIterator(new ArrayList(indices));
        IndexIterator iterator = new IndexIterator(Arrays.asList(newNumConst.getIndices()));
        MutableNumber newNumForIndex=new MutableNumber();
        while(iterator.hasNext()){
            iterator.next();
            switch(operation) {
                case (NumConstants.ADD): {
                    NumberMath.addNoInvalid(this.getNumberForIndex(),num.getNumberForIndex(), this.getNumberType(), newNumForIndex);
                    break;
                }
                case (NumConstants.MULTIPLY): {
                    NumberMath.multiplyNoInvalid(this.getNumberForIndex(),num.getNumberForIndex(), this.getNumberType(), newNumForIndex);
                    break;
                }
                case (NumConstants.SUBTRACT_FROM): {
                    NumberMath.subtractNoInvalid(num.getNumberForIndex(),this.getNumberForIndex(), this.getNumberType(), newNumForIndex);
                    break;
                }
                case (NumConstants.SUBTRACT): {
                    NumberMath.subtractNoInvalid(this.getNumberForIndex(),num.getNumberForIndex(), this.getNumberType(), newNumForIndex);
                    break;
                }
                case (NumConstants.DIVIDE): {
                    NumberMath.divideNoInvalid(num.getNumberForIndex(),this.getNumberForIndex(), this.getNumberType(), newNumForIndex);
                    break;
                }
                case (NumConstants.DIVIDE_CEIL): {
                    NumberMath.divideCeil(num.getNumberForIndex(),this.getNumberForIndex(), this.getNumberType(), newNumForIndex);
                    break;
                }
                case (NumConstants.DIVIDE_FLOOR): {
                    NumberMath.divideFloor(num.getNumberForIndex(),this.getNumberForIndex(), this.getNumberType(), newNumForIndex);
                    break;
                }
                case (NumConstants.DIVIDE_BY): {
                    NumberMath.divideNoInvalid(this.getNumberForIndex(),num.getNumberForIndex(), this.getNumberType(), newNumForIndex);
                    break;
                }
                case (NumConstants.DIVIDE_BY_CEIL): {
                    NumberMath.divideCeil(this.getNumberForIndex(), num.getNumberForIndex(),this.getNumberType(), newNumForIndex);
                    break;
                }
                case (NumConstants.DIVIDE_BY_FLOOR): {
                    NumberMath.divideFloor(this.getNumberForIndex(), num.getNumberForIndex(),this.getNumberType(), newNumForIndex);
                    break;
                }
            }
            newNumConst.setNumberForIndex(new MutableNumber(newNumForIndex));
        }
        return newNumConst;
    }

    // javadoc inherited from CspGenericNumConstant interface
    public CspGenericNumConstant subtractFrom(Number num){
        MutableNumber newConsts[] = new MutableNumber[vals.length];
        for (int i=0; i< vals.length; i++) {
            MutableNumber n = new MutableNumber();
            NumberMath.subtractNoInvalid(num,vals[i], this.getNumberType(), n);
            newConsts[i] = n;
        }
        return generateNumConstant(num+"-"+name,indices,newConsts);
    }
    
    // javadoc inherited from CspGenericNumConstant interface
    public CspGenericNumConstant subtractFrom(CspGenericNumConstant num){
        return operate(num,NumConstants.SUBTRACT_FROM);
    }
    
    // javadoc inherited from CspGenericNumConstant interface
    public CspGenericNumConstant subtract(Number num){
        MutableNumber newConsts[] = new MutableNumber[vals.length];
        for (int i=0; i< vals.length; i++) {
            MutableNumber n = new MutableNumber();
            NumberMath.subtractNoInvalid(vals[i],num, this.getNumberType(), n);
            newConsts[i] = n;
        }
        return generateNumConstant(name+"-"+num,indices,newConsts);
    }
    
    // javadoc inherited from CspGenericNumConstant interface
    public CspGenericNumConstant subtract(CspGenericNumConstant num){
        return operate(num,NumConstants.SUBTRACT);
    }
    
    // javadoc inherited from CspGenericNumConstant interface
    public CspGenericNumConstant multiply(Number num){
        MutableNumber newConsts[] = new MutableNumber[vals.length];
        for (int i=0; i< vals.length; i++) {
            MutableNumber n = new MutableNumber();
            NumberMath.multiplyNoInvalid(vals[i],num, this.getNumberType(), n);
            newConsts[i] = n;
        }
        return generateNumConstant(name+"*"+num,indices,newConsts);
    }
    
    // javadoc inherited from CspGenericNumConstant interface
    public CspGenericNumConstant multiply(CspGenericNumConstant num){
        return operate(num,NumConstants.MULTIPLY);
    }

    // javadoc inherited from CspGenericNumConstant interface
    public CspGenericNumConstant divideByCeil(Number num){
        MutableNumber newConsts[] = new MutableNumber[vals.length];
        for (int i=0; i< vals.length; i++) {
            MutableNumber n = new MutableNumber();
            NumberMath.divideCeil(vals[i],num, this.getNumberType(), n);
            newConsts[i] = n;
        }
        return generateNumConstant(name+"/"+num,indices,newConsts);
    }
    

    // javadoc inherited from CspGenericNumConstant interface
    public CspGenericNumConstant divideByCeil(CspGenericNumConstant num){
        return operate(num,NumConstants.DIVIDE_BY_CEIL);
    }
    

    // javadoc inherited from CspGenericNumConstant interface
    public CspGenericNumConstant divideByFloor(Number num){
        MutableNumber newConsts[] = new MutableNumber[vals.length];
        for (int i=0; i< vals.length; i++) {
            MutableNumber n = new MutableNumber();
            NumberMath.divideFloor(vals[i],num, this.getNumberType(), n);
            newConsts[i] = n;
        }
        return generateNumConstant(name+"/"+num,indices,newConsts);
    }
    

    // javadoc inherited from CspGenericNumConstant interface
    public CspGenericNumConstant divideByFloor(CspGenericNumConstant num){
        return operate(num,NumConstants.DIVIDE_BY_FLOOR);
    }
    
    // javadoc inherited from CspGenericNumConstant interface
    public CspGenericNumConstant divideBy(Number num){
        MutableNumber newConsts[] = new MutableNumber[vals.length];
        for (int i=0; i< vals.length; i++) {
            MutableNumber n = new MutableNumber();
            NumberMath.divideNoInvalid(vals[i],num, this.getNumberType(), n);
            newConsts[i] = n;
        }
        return generateNumConstant(name+"/"+num,indices,newConsts);
    }
    
    // javadoc inherited from CspGenericNumConstant interface
    public CspGenericNumConstant divideBy(CspGenericNumConstant num){
        return operate(num,NumConstants.DIVIDE_BY);
    }
    
    // javadoc inherited from CspGenericNumConstant interface
    public CspGenericNumConstant divide(Number num){
        MutableNumber newConsts[] = new MutableNumber[vals.length];
        for (int i=0; i< vals.length; i++) {
            MutableNumber n = new MutableNumber();
            NumberMath.divideNoInvalid(num,vals[i], this.getNumberType(), n);
            newConsts[i] = n;
        }
        return generateNumConstant(num+"/"+num,indices,newConsts);
    }
    
    // javadoc inherited from CspGenericNumConstant interface
    public CspGenericNumConstant divide(CspGenericNumConstant num){
        return operate(num,NumConstants.DIVIDE);
    }
    
    // javadoc inherited from CspGenericNumConstant interface
    public CspGenericNumConstant divideCeil(Number num){
        MutableNumber newConsts[] = new MutableNumber[vals.length];
        for (int i=0; i< vals.length; i++) {
            MutableNumber n = new MutableNumber();
            NumberMath.divideCeil(num,vals[i], this.getNumberType(), n);
            newConsts[i] = n;
        }
        return generateNumConstant(num+"/"+num,indices,newConsts);
    }
    
    // javadoc inherited from CspGenericNumConstant interface
    public CspGenericNumConstant divideCeil(CspGenericNumConstant num){
        return operate(num,NumConstants.DIVIDE_CEIL);
    }
    
    // javadoc inherited from CspGenericNumConstant interface
    public CspGenericNumConstant divideFloor(Number num){
        MutableNumber newConsts[] = new MutableNumber[vals.length];
        for (int i=0; i< vals.length; i++) {
            MutableNumber n = new MutableNumber();
            NumberMath.divideFloor(num,vals[i], this.getNumberType(), n);
            newConsts[i] = n;
        }
        return generateNumConstant(num+"/"+num,indices,newConsts);
    }
    
    // javadoc inherited from CspGenericNumConstant interface
    public CspGenericNumConstant divideFloor(CspGenericNumConstant num){
        return operate(num,NumConstants.DIVIDE_FLOOR);
    }
    
    /**
     * Displays name of node along with indices
     */
    public abstract String toString(); 

}
