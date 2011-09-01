/*
 * ArrayMutableNumberList.java
 * 
 * Created on Nov 20, 2005
 */
package jopt.csp.spi.util;

import java.util.ArrayList;

import jopt.csp.spi.pool.WeakObjectPool;

import org.apache.commons.pool.BasePoolableObjectFactory;

/**
 * List for maintaining mutable numbers
 */
public class NumberList {
    public static final Pool pool = new Pool();
    
	private ArrayList<MutableNumber> list;
    private ArrayList<MutableNumber> free;
    
    /**
     * Creates a new list
     */
    public NumberList() {
    	this.list = new ArrayList<MutableNumber>();
        this.free = new ArrayList<MutableNumber>();
    }
    
    private MutableNumber newNumber() {
    	if (free.size()>0)
            return (MutableNumber) free.remove(free.size()-1);
        else
            return new MutableNumber();
    }
    
    public int size() {
    	return list.size();
    }
    
    public Number get(int idx) {
        return (Number) list.get(idx);
    }
    
    public void add(int v) {
    	MutableNumber n = newNumber();
        n.setIntValue(v);
        list.add(n);
    }
    
    public void add(long v) {
        MutableNumber n = newNumber();
        n.setLongValue(v);
        list.add(n);
    }
    
    public void add(float v) {
        MutableNumber n = newNumber();
        n.setFloatValue(v);
        list.add(n);
    }
    
    public void add(double v) {
        MutableNumber n = newNumber();
        n.setDoubleValue(v);
        list.add(n);
    }
    
    public void add(Number n) {
        switch(NumberMath.numberType(n)) {
            case NumConstants.INTEGER:
                add(n.intValue());
                break;
            
            case NumConstants.LONG:
                add(n.longValue());
                break;
            
            case NumConstants.FLOAT:
                add(n.floatValue());
                break;
            
            default:
                add(n.doubleValue());
        }
    }
    
    public void add(int idx, int v) {
        MutableNumber n = newNumber();
        n.setIntValue(v);
        list.add(idx, n);
    }
    
    public void add(int idx, long v) {
        MutableNumber n = newNumber();
        n.setLongValue(v);
        list.add(idx, n);
    }
    
    public void add(int idx, float v) {
        MutableNumber n = newNumber();
        n.setFloatValue(v);
        list.add(idx, n);
    }
    
    public void add(int idx, double v) {
        MutableNumber n = newNumber();
        n.setDoubleValue(v);
        list.add(idx, n);
    }
    
    public void add(int idx, Number n) {
        switch(NumberMath.numberType(n)) {
            case NumConstants.INTEGER:
                add(idx, n.intValue());
                break;
            
            case NumConstants.LONG:
                add(idx, n.longValue());
                break;
            
            case NumConstants.FLOAT:
                add(idx, n.floatValue());
                break;
            
            default:
                add(idx, n.doubleValue());
        }
    }
    
    public void set(int idx, int v) {
        MutableNumber n = newNumber();
        n.setIntValue(v);
        list.set(idx, n);
    }
    
    public void set(int idx, long v) {
        MutableNumber n = newNumber();
        n.setLongValue(v);
        list.set(idx, n);
    }
    
    public void set(int idx, float v) {
        MutableNumber n = newNumber();
        n.setFloatValue(v);
        list.set(idx, n);
    }
    
    public void set(int idx, double v) {
        MutableNumber n = newNumber();
        n.setDoubleValue(v);
        list.set(idx, n);
    }
    
    public void set(int idx, Number n) {
        switch(NumberMath.numberType(n)) {
            case NumConstants.INTEGER:
                set(idx, n.intValue());
                break;
            
            case NumConstants.LONG:
                set(idx, n.longValue());
                break;
            
            case NumConstants.FLOAT:
                set(idx, n.floatValue());
                break;
            
            default:
                set(idx, n.doubleValue());
        }
    }
    
    public Number remove(int idx) {
    	MutableNumber n = list.remove(idx);
    	free.add(n);
        return n;
    }
    
    public void clear() {
    	while (list.size()>0) {
    		free.add(list.remove(list.size()-1));
        }
    }

    /**
     * Pool that creates pooled intervals that can be released
     */
    public static class Pool {
        private WeakObjectPool p;
        
        private Pool() {
            this.p = new WeakObjectPool(new PoolObjectFactory());
        }
        
        public NumberList borrowList() {
        	return (NumberList) p.borrowObject();
        }
        
        public void returnList(NumberList list) {
            list.clear();
            p.returnObject(list);
        }
    }

    /**
     * Creates intervals for use during pooling
     */
    private static class PoolObjectFactory extends BasePoolableObjectFactory {
        public Object makeObject() {
            return new NumberList();
        }
    }
}
