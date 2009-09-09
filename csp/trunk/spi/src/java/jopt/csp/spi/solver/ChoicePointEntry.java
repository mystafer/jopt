/*
 * ChoicePointEntry.java
 * 
 * Created on Nov 18, 2005
 */
package jopt.csp.spi.solver;



/**
 * Interface for objects that are referenced by a choice point stack and store
 * information about problem state at a previous point in the search tree.
 * Examples are the {@link ChoicePointDataMap} and {@link ChoicePointNumArraySet}.
 * Note that the information about problem state is not store in the {@link
 * ChoicePointStack} as the "stack" name might imply.  Information is stored in
 * ChoicePointEntry objects (which are mini-stacks themselves); these objects 
 * are referenced by the ChoicePointStack and are told to push and pop when the
 * ChoicePointStack gets pushed and popped itself. 
 */
public interface ChoicePointEntry {
    /**
     * <p>
     * Handles pushing values in the current entry onto an internal stack
     * that can be restored when the pop method is called
     * </p>
     * <p>
     * This method is more efficient to the <code>pushDelta</code> method and is encouraged
     * for use if delta information is not necessary
     * </p>
     */
    public void push();
    
    /**
     * Handles pushing values in the current entry onto an internal stack
     * that can be restored when the pop method is called
     */
    public void pushDelta(Object delta);
    
    /**
     * <p>
     * Restores values in the current entry from data recorded in the internal
     * stack. Any listeners attached to this entry are notified that the values
     * in the entry are about to be rolled back before the action takes place
     * allowing listeners to act accordingly.
     * </p>
     * <p>
     * This method is more efficient to the <code>popDelta</code> method and is encouraged
     * for use if delta information is not necessary
     * </p>
     */
    public void pop();
    
	/**
	 * Restores values in the current entry from data recorded in the internal
	 * stack. Any listeners attached to this entry are notified that the values
	 * in the entry are about to be rolled back before the action takes place
	 * allowing listeners to act accordingly.
	 * 
	 * @return  object containing changes that were rolled back
	 */
	public Object popDelta();

	/**
	 * Returns unique ID of map assigned to choice point stack
	 */
	public Integer getEntryID();

    /**
     * Sets listener for this entry
     */
    public void setListener(ChoicePointEntryListener listener);
}