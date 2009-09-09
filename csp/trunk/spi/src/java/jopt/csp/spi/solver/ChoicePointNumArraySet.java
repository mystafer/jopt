/*
 * ChoicePointIntArray.java
 * 
 * Created on Nov 18, 2005
 */
package jopt.csp.spi.solver;

import java.util.ArrayList;

import org.apache.commons.collections.primitives.ArrayIntList;

/**
 * <p>
 * <code>ChoicePointNumArraySet</code> maintains a set of numeric stacks that
 * will push and rollback together.
 * </p>
 * This class is different from {@link ChoicePointDataMap} in that it
 * only stores certain types of numeric data.  The <code>ChoicePointDataMap</code>
 * is similar but can store any type of data as long as it is an object.
 * <p>
 * This class is not thread safe.
 * </p>
 * 
 * @author Nick Coleman
 * @version $Revision: 1.3 $ 
 */
public class ChoicePointNumArraySet implements ChoicePointEntry {
	private ArrayList stacks;
    private ChoicePointEntryCloseListener closeListener;
    private Integer entryID;
    private ChoicePointEntryListener listener;
    private ArrayIntList deltaIdxWorkingList;
    private ArrayList deltaWorkingList;
    private boolean pushing;
    private boolean popping;
    
    /**
     * Creates a new <code>ChoicePointNumArraySet</code>
     *  
     * @param closeListener listener for element closing events
     * @param entryID       ID for callback to close listener
     */
    public ChoicePointNumArraySet(ChoicePointEntryCloseListener closeListener, Integer entryID) {
        this.closeListener = closeListener;
        this.entryID = entryID;
        this.stacks = new ArrayList();
    }
    
    /**
     * Creates a new <code>ChoicePointIntList</code> associated with this set. 
     * 
     * @return <code>ChoicePointIntList</code> associated with this set
     */
    public ChoicePointIntArray newIntList() {
        ChoicePointIntArray list = new ChoicePointIntArray();
        stacks.add(list);
        return list;
    }
    
    /**
     * Creates a new <code>ChoicePointMultiIntArray</code> associated with this set. 
     * 
     * @param listCnt   Number of lists to maintain
     * @return <code>ChoicePointIntList</code> associated with this et
     */
    public ChoicePointMultiIntArray newMultiIntList(int listCnt) {
        ChoicePointMultiIntArray list = new ChoicePointMultiIntArray(listCnt);
        stacks.add(list);
        return list;
    }
    
    /**
     * Creates a new <code>ChoicePointLongArray</code> associated with this set. 
     * 
     * @return <code>ChoicePointLongArray</code> associated with this set
     */
    public ChoicePointLongArray newLongList() {
        ChoicePointLongArray list = new ChoicePointLongArray();
        stacks.add(list);
        return list;
    }
    
    /**
     * Creates a new <code>ChoicePointMultiLongArray</code> associated with this set. 
     * 
     * @param listCnt   Number of lists to maintain
     * @return <code>ChoicePointMultiLongArray</code> associated with this et
     */
    public ChoicePointMultiLongArray newMultiLongList(int listCnt) {
        ChoicePointMultiLongArray list = new ChoicePointMultiLongArray(listCnt);
        stacks.add(list);
        return list;
    }
    
    /**
     * Creates a new <code>ChoicePointFloatArray</code> associated with this set. 
     * 
     * @return <code>ChoicePointFloatArray</code> associated with this set
     */
    public ChoicePointFloatArray newFloatList() {
        ChoicePointFloatArray list = new ChoicePointFloatArray();
        stacks.add(list);
        return list;
    }
    
    /**
     * Creates a new <code>ChoicePointMultiFloatArray</code> associated with this set. 
     * 
     * @param listCnt   Number of lists to maintain
     * @return <code>ChoicePointMultiFloatArray</code> associated with this et
     */
    public ChoicePointMultiFloatArray newMultiFloatList(int listCnt) {
        ChoicePointMultiFloatArray list = new ChoicePointMultiFloatArray(listCnt);
        stacks.add(list);
        return list;
    }
    
    /**
     * Creates a new <code>ChoicePointDoubleArray</code> associated with this set. 
     * 
     * @return <code>ChoicePointDoubleArray</code> associated with this set
     */
    public ChoicePointDoubleArray newDoubleList() {
        ChoicePointDoubleArray list = new ChoicePointDoubleArray();
        stacks.add(list);
        return list;
    }
    
    /**
     * Creates a new <code>ChoicePointMultiDoubleArray</code> associated with this set. 
     * 
     * @param listCnt   Number of lists to maintain
     * @return <code>ChoicePointMultiDoubleArray</code> associated with this et
     */
    public ChoicePointMultiDoubleArray newMultiDoubleList(int listCnt) {
        ChoicePointMultiDoubleArray list = new ChoicePointMultiDoubleArray(listCnt);
        stacks.add(list);
        return list;
    }
    
    /**
     * Returns unique ID of element assigned to choice point stack
     */
    public Integer getEntryID() {
        return entryID;
    }
    
    /**
     * Disconnects the list from the choicepoint stack
     */
    public void close() {
        // notify listener that map has closed
        closeListener.entryClosedEvent(entryID);
    }
    
    // javadoc inherited
    public void push() {
    	pushing = true;
    	
        // Notify listener
        if (listener!=null)
            listener.beforeChoicePointPushEvent();
        
        // push empty values onto stack
        for (int i=0; i<stacks.size(); i++) {
            StackList list = (StackList) stacks.get(i);
            list.push();
        }
        
        // Notify listener
        if (listener!=null)
            listener.afterChoicePointPushEvent();
        
    	pushing = false;
    }
    
    // javadoc inherited
    public void pushDelta(Object delta) {
        // check if any delta data was pushed
        if (delta!=null) {
        	pushing = true;
        	
            // Notify listener
            if (listener!=null)
                listener.beforeChoicePointPushEvent();
            
            // retrieve indices and data of values that were pushed
            Object setData[] = (Object[]) delta;
            int indices[] = (int[]) setData[0];
            Object deltaArray[] = (Object[]) setData[1];
            
            // push data back onto array
            for (int i=0; i<stacks.size(); i++) {
                // retrieve delta data corresponding to array
                Object data = null;
            	for (int j=0; j<indices.length; j++) {
            		if (j==i) {
            			data = deltaArray[j];
                        break;
                    }
                }
                
                // push data back onto array
                StackList list = (StackList) stacks.get(i);
                if (data!=null)
                    list.pushDelta(data);
                else
                    list.push();
            }
            
            // Notify listener
            if (listener!=null)
                listener.afterChoicePointPushEvent();
            
            pushing = false;
        }
        else {
            push();
        }
    }
    
    // javadoc inherited
    public void pop() {
    	popping = true;
    	
        // Notify listener
        if (listener!=null)
            listener.beforeChoicePointPopEvent();
        
        for (int i=0; i<stacks.size(); i++) {
            StackList list = (StackList) stacks.get(i);
            list.pop();
        }
        
        // Notify listener
        if (listener!=null)
            listener.afterChoicePointPopEvent();
        
        popping = false;
    }
    
    // javadoc inherited
    public Object popDelta() {
    	popping = true;
    	
        // Notify listener
        if (listener!=null)
            listener.beforeChoicePointPopEvent();
        
        // initialize delta working lists
        if (deltaIdxWorkingList==null) {
        	deltaIdxWorkingList = new ArrayIntList();
            deltaWorkingList = new ArrayList();
        }
        else {
        	deltaIdxWorkingList.clear();
        	deltaWorkingList.clear();
        }
        
        // pop delta information for each list
        for (int i=0; i<stacks.size(); i++) {
            StackList list = (StackList) stacks.get(i);
            Object delta = list.popDelta();
            
            if (delta!=null) {
            	deltaIdxWorkingList.add(i);
            	deltaWorkingList.add(delta);
            }
        }

        // Notify listener
        if (listener!=null)
            listener.afterChoicePointPopEvent();
        
        popping = false;
        
        // return delta data
        if (deltaIdxWorkingList.size()>0) {
        	return new Object[]{deltaIdxWorkingList.toArray(), deltaWorkingList.toArray()};
        }
        else {
        	return null;
        }
    }
    
    /**
     * Returns true if set is in middle of a push operation
     */
    public boolean isPushing() {
    	return pushing;
    }
    
    /**
     * Returns true if set is in middle of a pop operation
     */
    public boolean isPopping() {
    	return popping;
    }
    
    /**
     * Returns true if set is in middle of a push or pop operation
     */
    public boolean isWorking() {
    	return pushing || popping;
    }
    
    /**
     * Sets listener for this entry
     */
    public void setListener(ChoicePointEntryListener listener) {
    	this.listener = listener;
    }
    
    public static interface StackList {
        public void push();
        public void pushDelta(Object delta);
        public void pop();
        public Object popDelta();
    }
}
