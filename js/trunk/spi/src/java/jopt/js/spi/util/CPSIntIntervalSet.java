package jopt.js.spi.util;

import jopt.csp.spi.solver.ChoicePointDataSource;
import jopt.csp.spi.solver.ChoicePointEntryListener;
import jopt.csp.spi.solver.ChoicePointMultiIntArray;
import jopt.csp.spi.solver.ChoicePointNumArraySet;
import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.util.IntIntervalSet;
import jopt.csp.util.NumSet;

/**
 * A "choice point-aware" extension of IntIntervalSet allowing "automatic" back-tracking
 */
public class CPSIntIntervalSet extends IntIntervalSet implements ChoicePointDataSource,ChoicePointEntryListener{
	
    protected ChoicePointNumArraySet cpdata;
	protected IntIntervalSet iisDelta;
    protected boolean cpsEvent;
    protected int cpsEventStart = Integer.MIN_VALUE;
    protected int cpsEventEnd = Integer.MIN_VALUE;
	//monitors changes for choice point events
    protected ChoicePointMultiIntArray valChanges;
	
    // javadoc is inherited
	public void notifyIntervalAddition(int start, int end) {
		if((!cpsEvent)&&(valChanges!=null)) {
			System.out.println("val Changes changed occured in notify Interval Addition");
			int index = valChanges.add(0,start);
			valChanges.set(1,index, end);
			valChanges.set(2,index, 1);
		}
		if (iisDelta!=null) {
			iisDelta.add(start,end);
		}
		super.notifyIntervalAddition(start,end);
	}
	
    // javadoc is inherited
	public void notifyIntervalRemoval(int start, int end) {
		if((!cpsEvent)&&(valChanges!=null)) {
			int index = valChanges.add(0,start);
			valChanges.set(1,index, end);
			valChanges.set(2,index, -1);
		}
		if (iisDelta!=null) {
			iisDelta.add(start,end);
		}
		super.notifyIntervalRemoval(start,end);
	}
	
	// javadoc is inherited
	public boolean choicePointStackSet() {
		if(cpdata!=null)
			return true;
		return false;
	}
	
	// javadoc is inherited
	public void setChoicePointStack(ChoicePointStack cps) {
		if (cps == null) return;
		
		if (cps!=null) {
			this.cpdata = cps.newNumStackSet(this);
			this.valChanges = cpdata.newMultiIntList(3);
		}
	}
	
	// javadoc is inherited
	public void beforeChoicePointPopEvent() {
			//restore altered data
			for (int i=0; i<valChanges.size(); i++) {
				if (valChanges.get(0,i)==Integer.MIN_VALUE) {
					//We can skip this iteration 
				}
				else if (valChanges.get(2,i)>0){
					this.remove(valChanges.get(0,i),valChanges.get(1,i),false);
				}
				else {			
					this.add(valChanges.get(0,i),valChanges.get(1,i),false);
				}
			}
			if (valChanges.size()<=0) {
				valChanges.add(0,Integer.MIN_VALUE);
			}
	}
	
	// javadoc is inherited
	public void afterChoicePointPopEvent() {
		
	}
	
	// javadoc is inherited
	public void beforeChoicePointPushEvent() {
		
	}
	
	// javadoc is inherited
	public void afterChoicePointPushEvent() {
		cpsEvent = true;
			//    	 restore altered data
			if (!(valChanges.size()==1) || valChanges.get(0,0)!=Integer.MIN_VALUE) {
				for (int i=0; i<valChanges.size(); i++) {
					if (valChanges.get(2,i)>0) {
						this.add(valChanges.get(0,i),valChanges.get(1,i),false);
					}
					else {
						this.remove(valChanges.get(0,i),valChanges.get(1,i),false);
					}
				}
			}
		cpsEvent = false;
	}
	
	public void setMin(int min) {
		removeEndingBefore(min);
	}
	
	public void setMax(int max) {
		removeStartingAfter(max);
	}
	
	/**
	 * Removes the specified interval and records all changes that actually occur
	 * @param start start of interval
	 * @param end end of interval
	 * @return the intervals that have actually been removed
	 */
	public IntIntervalSet removeGetDelta(int start, int end) {
		iisDelta = new IntIntervalSet();
		remove(start, end);
		return iisDelta;
	}
	
	/**
	 * Removes the specified set of values and records all changes that actually occur
	 * @param set set to be removed
	 * @return the intervals that have actually been removed
	 */
	public IntIntervalSet removeAllGetDelta(NumSet set) {
		iisDelta = new IntIntervalSet();
		removeAll (set);
		return iisDelta;
	}
	
	/**
	 * Removes anything ending before the specified val and records all changes that actually occur
	 * @param val new minimum value
	 * @return the intervals that have actually been removed
	 */
	public IntIntervalSet removeEndingBeforeGetDelta(int val) {
		iisDelta = new IntIntervalSet();
		removeEndingBefore(val);
		return iisDelta;
	}
	
	/**
	 * Removes anything starting after the specified val and records all changes that actually occur
     * @param val new maximum value
     * @return the intervals that have actually been removed
	 */
	public IntIntervalSet removeStartingAfterGetDelta(int val) {
		iisDelta = new IntIntervalSet();
		removeStartingAfter(val);
		return iisDelta;
	}
	
	/**
	 * Adds specified interval and records all changes that actually occur
	 * @param start start of interval to be added
	 * @param end end of interval to be added
	 * @return a set of values that were actually added
	 */
	public IntIntervalSet addGetDelta(int start, int end) {
		iisDelta = new IntIntervalSet();
		add(start,end);
		return iisDelta;
	}
}
