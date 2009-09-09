/*
 * GenericIndexManager.java
 * 
 * Created on Apr 13, 2005
 */
package jopt.csp.spi.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import jopt.csp.spi.arcalgorithm.variable.VarFactory;
import jopt.csp.variable.CspGenericIndex;

/**
 * Utility for creating iterators that can be used by arcs when processing generic nodes
 */
public class GenericIndexManager {
    private boolean xIndexRestricted;
    private boolean yIndexRestricted;
    private boolean zIndexRestricted;
    
    private boolean xzCommonIndex;
    private boolean xyCommonIndex;
    private boolean yzCommonIndex;
    
    private List xControlledIndices;
    private List yControlledIndices;
    private List zControlledIndices;
    private List allIndices;
    private List restrictedIndices;
    
    private IndexIterator xIterator;
    private IndexIterator yIterator;
    private IndexIterator zIterator;
    private IndexIterator allIterator;
    private IndexIterator restrictedIterator;
    
    private boolean disableCombinedIndices;
    
    /**
     * Internal constructor for extending manager
     */
    protected GenericIndexManager() {}
    
    /**
     * Creates a new index manager that manages indices for source and target
     * nodes where X and Y are source nodes and Z is a target such as
     * X + Y = Z and X and Y indices have restricted ranges.
     * 
     * @param   xIndices            X source indices for the equation
     * @param   yIndices            Y source indices for the equation
     * @param   zIndices            Z source indices for the equation
     * @param   restrictedIndices   array if indices that have restricted ranges
     * @param   restrictZ           True if Z node should be restricted
     */
    public GenericIndexManager(CspGenericIndex xIndices[], CspGenericIndex yIndices[], CspGenericIndex zIndices[], 
            CspGenericIndex restrictedIndices[], boolean restrictZ) 
    {
    	init(VarFactory.toGenericIndex(xIndices), VarFactory.toGenericIndex(yIndices), VarFactory.toGenericIndex(zIndices), VarFactory.toGenericIndex(restrictedIndices), restrictZ);
    }
    
    /**
     * Initializes internal index manager during constructor calls
     */
    protected void init(GenericIndex xIndices[], GenericIndex yIndices[], GenericIndex zIndices[], 
            GenericIndex restrictedIndices[], boolean restrictZ) 
    {
        // build set of excluded indices
        HashSet restrictedSet = new HashSet();
        if (restrictedIndices!=null) restrictedSet.addAll(Arrays.asList(restrictedIndices));
        
        // build set of unique z indices
        HashSet zIndiceSet = new HashSet();
        if (zIndices!=null) {
            for (int i=0; i<zIndices.length; i++) {
                GenericIndex idx = zIndices[i];
                
                // determine if z index is restricted and excluded from normal control
                if (restrictZ && restrictedSet.contains(idx)) {
                    zIndexRestricted = true;
                }
                else {
                	zIndiceSet.add(zIndices[i]);
                }
            }
        }

        // remove z indices from restricted set
        restrictedSet.removeAll(zIndiceSet);
        this.restrictedIndices = new ArrayList(restrictedSet);
        
        // build set of unique x indices
        HashSet xIndiceSet = new HashSet();
        if (xIndices!=null) {
            for (int i=0; i<xIndices.length; i++) {
                GenericIndex idx = xIndices[i];
                
                // check for index that is excluded from normal control
                if (restrictedSet.contains(idx)) {
                    xIndexRestricted = true;
                }
                else {
                    xIndiceSet.add(idx);
                    if (zIndiceSet.contains(idx)) xzCommonIndex = true;
                }
            }
        }

        // build set of unique y indices
        HashSet yIndiceSet = new HashSet();
        if (yIndices!=null) {
            for (int i=0; i<yIndices.length; i++) {
                GenericIndex idx = yIndices[i];
                
                // check for index that is excluded from normal control
                if (restrictedSet.contains(idx)) {
                    yIndexRestricted = true;
                }
                else {
                    yIndiceSet.add(idx);
                    if (xIndiceSet.contains(idx)) xyCommonIndex = true;
                    if (zIndiceSet.contains(idx)) yzCommonIndex = true;
                }
            }
        }
        
        //if target is null and any source is restricted, target indicies is composed
        // of leftover indices on restricted variables
        if(zIndices == null) {
            if(xIndexRestricted) {
                zIndiceSet.addAll(xIndiceSet);
            }
            if(yIndexRestricted) {
                zIndiceSet.addAll(yIndiceSet);
            }
        }
        
        // if no common indices, no need for iteration
        if (xzCommonIndex || xyCommonIndex || yzCommonIndex || xIndexRestricted || yIndexRestricted || zIndexRestricted) {
            // build control set for all indices
            HashSet s = new HashSet(zIndiceSet);
            if (xzCommonIndex || xyCommonIndex) s.addAll(xIndiceSet);
            if (yzCommonIndex || xyCommonIndex) s.addAll(yIndiceSet);
            zControlledIndices = new ArrayList(s);
            
            // build X controlled set where x indices should not be looped
            s.removeAll(xIndiceSet);
            xControlledIndices = new ArrayList(s);
            
            // build Y controlled set where y indices should not be looped
            if (xzCommonIndex || xyCommonIndex) s.addAll(xIndiceSet);
            s.removeAll(yIndiceSet);
            yControlledIndices = new ArrayList(s);
        }
        
        // build complete set of indices
        HashSet s = new HashSet(zIndiceSet);
        s.addAll(xIndiceSet);
        s.addAll(yIndiceSet);
        allIndices = new ArrayList(s);
    }
    
    /**
     * True if the entire generic X node should be used, False is specific node should
     * be used for each index
     */
    public boolean useCombinedX() {
    	return !(disableCombinedIndices || xzCommonIndex || xyCommonIndex || xIndexRestricted);
    }
    
    /**
     * True if the entire generic Y node should be used, False is specific node should
     * be used for each index
     */
    public boolean useCombinedY() {
        return !(disableCombinedIndices || yzCommonIndex || xyCommonIndex || yIndexRestricted);
    }
    
    /**
     * True if the entire generic Z node should be used, False is specific node should
     * be used for each index
     */
    public boolean useCombinedZ() {
        return !(disableCombinedIndices || xzCommonIndex || yzCommonIndex || zIndexRestricted);
    }
    
    /**
     * True if an X index is excluded from the common iterators
     */
    public boolean xIndexIsRestricted() {
        return xIndexRestricted;
    }
    
    /**
     * True if an Y index is excluded from the common iterators
     */
    public boolean yIndexIsRestricted() {
        return yIndexRestricted;
    }
    
    /**
     * True if an Z index is excluded from the common iterators
     */
    public boolean zIndexIsRestricted() {
        return zIndexRestricted;
    }
    
    /**
     * Creates an iterator designed to loop over all indices in the target and source
     * nodes
     */
    public IndexIterator iterator() {
        if (zControlledIndices==null || zControlledIndices.size()==0) return null;
        if (zIterator==null)
            zIterator = new IndexIterator(zControlledIndices);
        else
        	zIterator.reset();
        return zIterator;
    }

    /**
     * Creates an iterator designed to loop over all indices not a part of the X node
     */
    public IndexIterator xControlledIterator() {
        if (xControlledIndices==null || xControlledIndices.size()==0) return null;
        if (xIterator==null)
        	xIterator = new IndexIterator(xControlledIndices);
        else
        	xIterator.reset();
        return xIterator;
    }

    /**
     * Creates an iterator designed to loop over all indices not a part of the Y node
     */
    public IndexIterator yControlledIterator() {
        if (yControlledIndices==null || yControlledIndices.size()==0) return null;
        if (yIterator==null)
            yIterator = new IndexIterator(yControlledIndices);
        else
        	yIterator.reset();
        return yIterator;
    }

    /**
     * Creates an iterator designed to loop over all indices in all nodes
     */
    public IndexIterator allIterator() {
        if (allIndices==null || allIndices.size()==0) return null;
        if (allIterator==null)
            allIterator = new IndexIterator(allIndices);
        else
            allIterator.reset();
        return allIterator;
    }

    /**
     * Creates an iterator designed to loop over indices that were restricted
     * from normal control
     */
    public IndexIterator restrictedIterator() {
        if (restrictedIndices==null || restrictedIndices.size()==0) return null;
        if (restrictedIterator==null)
            restrictedIterator = new IndexIterator(restrictedIndices);
        else
            restrictedIterator.reset();
        return restrictedIterator;
    }
    
    /**
     * Sets flag that that will cause all the useCombined methods to return false
     * if set to true
     */
    public void setDisableCombinedIndices(boolean disable) {
    	this.disableCombinedIndices = disable;
    }
    
    /**
     * Returns flag that indicating if useCombined methods are allowed to 
     * return true
     */
    public boolean getDisableCombinedIndices() {
        return this.disableCombinedIndices;
    }
}
