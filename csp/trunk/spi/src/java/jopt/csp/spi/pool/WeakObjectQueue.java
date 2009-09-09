package jopt.csp.spi.pool;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Pool implementation that releases idle objects during garbage collection
 */
public class WeakObjectQueue {
    private WeakReference ref = null;
    
    /**
     * Pops an object from the queue or returns null if no objects
     * are in the queue
     */
	public synchronized Object pop() {
        // check for stored references
        if (ref==null) return null;
        
        // retrieve reference list
        ArrayList list = (ArrayList) ref.get();
        if (list==null) return null;
        
        // check if value exists in list
        if (list.size()==0) return null;
        
        return list.remove(0);
	}

    /**
     * Pushes an object onto the queue
     */
	public void push(Object obj) {
        try {
            // retrieve list to store reference
            ArrayList list = (ref != null) ? (ArrayList) ref.get() : null;
            
            // check if list has been initialized
            if (list==null) {
                list = new ArrayList();
                ref = new WeakReference(list);
            }
            
            // add object to list
            list.add(obj);
        }
        catch(Exception e) {
            throw new RuntimeException("error adding object to queue", e);
        }
	}

    /**
     * Removes an object from the queue
     */
	public void remove(Object obj) {
        // check for stored references
        if (ref==null) return;
        
        // retrieve reference list
        ArrayList list = (ArrayList) ref.get();
        if (list==null) return;
        
        // check if value exists in list
        if (list.size()==0) return;
        
        // check if object is in list
        list.remove(obj);
	}
    
    /**
     * Clears queue data
     */
    public void clear() {
    	ref = null;
    }
}
