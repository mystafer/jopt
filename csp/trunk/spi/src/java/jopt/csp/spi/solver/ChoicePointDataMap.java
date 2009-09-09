package jopt.csp.spi.solver;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * Map that stores data for an object that needs to record changes in a
 * {@link ChoicePointStack} so the changes can be pushed onto the stack and
 * later rolled back when the stack is popped.
 * <p>
 * Objects that need to act upon the data stored in this map in order to
 * undo the changes recorded in the map need to implement the
 * {@link ChoicePointEntryListener} interface. Objects implementing this
 * interface can listen for events that indicate the data in the 
 * map is about to be rolled back and should be acted upon.
 * <p>
 * This class is different from {@link ChoicePointNumArraySet} in that it
 * will store any sort of data--any object that is given it.  The <code>
 * ChoicePointNumArraySet</code> is similar but only handles certain types
 * of numeric data.
 * <p>
 * This map wraps internal <code>HashMap</code> and <code>Stack</code>
 * objects that handle performing the functionality required. A new internal
 * <code>HashMap</code> is created whenever a map is pushed on the 
 * internal <code>Stack</code>.
 * <p>
 * This class is not thread safe.
 *  
 * @author Nick Coleman
 * @version %I% 
 */
public class ChoicePointDataMap implements Map, ChoicePointEntry {
    private ChoicePointEntryCloseListener closeListener;
    private Integer entryID;
    // Stack of currentData maps that have been pushed
    private Stack dataStack;
    // Map containing data about any number of objects
    private Map currentData;
    private ChoicePointEntryListener listener;
    
    /**
     * Creates a new choicepoint data map
     * 
     * @param closeListener Object listening for entry closed events
     * @param entryID       Unique ID assigned by choicepoint stack
     */
    public ChoicePointDataMap(ChoicePointEntryCloseListener closeListener, Integer entryID) {
        this.closeListener = closeListener;
        this.entryID = entryID;
        this.dataStack = null;
        this.currentData = new HashMap();
    }
    
    /**
     * Disconnects the map from the choicepoint stack
     */
    public void close() {
        // remove all data in the map
        if (dataStack!=null)
        	while (dataStack.size()>0) pop();
        
        // notify listener that map has closed
        closeListener.entryClosedEvent(entryID);
    }
    
    // javadoc inherited
    public void push() {
        pushDelta(null);
    }
    
    /**
     * Handles pushing values in the current map onto an internal stack
     * that can be restored when the pop method is called
     */
    public void pushDelta(Object data) {
        // Notify listener
        if (listener!=null)
            listener.beforeChoicePointPushEvent();
        
        if (dataStack==null) dataStack = new Stack();
        dataStack.push(currentData);
        
        if (data==null)
        	currentData = new HashMap();
        else
            currentData = (Map) data;
        
        // Notify listener
        if (listener!=null)
            listener.afterChoicePointPushEvent();
    }
    
    // javadoc inherited
    public void pop() {
    	popDelta();
    }
    
    /**
     * Restores values in the current map from data recorded in the internal
     * stack. Any listeners attached to this map are notified that the values
     * in the map are about to be rolled back before the action takes place
     * allowing listeners to act accordingly.
     * 
     * @return  map of changes that was rolled back in map
     */
    public Object popDelta() {
        // Notify listener
        if (listener!=null)
            listener.beforeChoicePointPopEvent();
        
        // capture changes that have occurred to map since last push
        Map changes = currentData;
        
        // Pop data from stack
        if (dataStack==null || dataStack.size()==0)
            currentData = new HashMap();
        else
            currentData = (Map) dataStack.pop();
        
        // Notify listener
        if (listener!=null)
            listener.afterChoicePointPopEvent();
        
        return changes;
    }
    
    /**
     * Sets listener for this datamap
     */
    public void setListener(ChoicePointEntryListener listener) {
        this.listener = listener;
    }
    
    /**
     * Returns unique ID of map assigned to choice point stack
     */
    public Integer getEntryID() {
    	return entryID;
    }
    
    // Map wrapper functions
    public void clear() {
        currentData.clear();
    }
    
    public boolean containsKey(Object obj) {
        return currentData.containsKey(obj);
    }
    
    public boolean containsValue(Object obj) {
        return currentData.containsValue(obj);
    }
    
    public Set entrySet() {
        return currentData.entrySet();
    }
    
    public Object get(Object obj) {
        return currentData.get(obj);
    }
    
    public boolean isEmpty() {
        return currentData.isEmpty();
    }
    
    public Set keySet() {
        return currentData.keySet();
    }
    
    public Object put(Object obj, Object obj1) {
        return currentData.put(obj, obj1);
    }
    
    public void putAll(Map map) {
        currentData.putAll(map);
    }
    
    public Object remove(Object obj) {
        return currentData.remove(obj);
    }
    
    public int size() {
        return currentData.size();
    }
    
    public Collection values() {
        return currentData.values();
    }
    
    public String toString() {
        StringBuffer buf = new StringBuffer("--- Data Map for : ");
        buf.append(listener);
        buf.append(" ---\ncurrent: ");
        buf.append(currentData);
        buf.append('\n');
        
        if (dataStack!=null) {
            for (int i=dataStack.size()-1; i>=0; i--) {
            	Map pm = (Map) dataStack.get(i);
                buf.append("[");
                buf.append(Integer.toString(i));
                buf.append("]: ");
                buf.append(pm);
                if (i>0) buf.append('\n');
            }
        }
        
        return buf.toString();
    }
}