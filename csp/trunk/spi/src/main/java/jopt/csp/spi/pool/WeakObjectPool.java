package jopt.csp.spi.pool;

import org.apache.commons.pool.BaseObjectPool;
import org.apache.commons.pool.PoolableObjectFactory;

/**
 * Pool implementation that releases idle objects during garbage collection
 */
public class WeakObjectPool extends BaseObjectPool {
    private PoolableObjectFactory factory = null;
    private WeakObjectQueue queue = null;
    
    /**
     * Creates new pool
     * 
     * @param factory   Factory used to create new objects when needed by pool
     */
    public WeakObjectPool(PoolableObjectFactory factory) {
    	this.factory = factory;
        this.queue = new WeakObjectQueue();
    }

	public synchronized Object borrowObject() {
        try {
            Object obj = queue.pop();
            if (obj==null) obj = factory.makeObject();
            return obj;
        }
        catch(Exception e) {
            throw new RuntimeException("error creating object for pool", e);
        }
	}

	public synchronized void returnObject(Object obj) {
	    //NOTE: Commenting out the following line of code effectively disables pooling
        queue.push(obj);
	}

	public void invalidateObject(Object obj) throws Exception {
        queue.remove(obj);
	}

	public void addObject() throws Exception {
        queue.push(factory.makeObject());
	}

}
