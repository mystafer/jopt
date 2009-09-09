package jopt.csp.spi.search.limit;

import jopt.csp.search.SearchLimit;
import jopt.csp.search.SearchNode;

/**
 * Search limit that will stop activating search nodes after a configurable
 * number of choicepoints have been activated
 */
public class ChoiceLimit implements SearchLimit {

	private int maxChoices;
	private int totalNodes;
	
	/**
	 * Creates a new choicepoint limit search limit
	 * 
	 * @param maxChoices	Maximum numbere of choicepoints allowed
	 */
	public ChoiceLimit(int maxChoices) {
		this.maxChoices = maxChoices;
	}
	
	// javadoc inherited
	public void init(SearchNode node) {
		this.totalNodes = 0;
	}
	
	// javadoc inherited
	public boolean isOkToContinue(SearchNode node) {
		totalNodes++;
		return totalNodes < maxChoices;
	}
	
	public Object clone() {
		return new ChoiceLimit(maxChoices);
	}

}
