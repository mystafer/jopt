package jopt.csp.spi.search.limit;

import jopt.csp.search.SearchLimit;
import jopt.csp.search.SearchNode;

/**
 * Search limit that will fail if any of the limits it combines fails
 */
public class CombinedLimit implements SearchLimit {

	private SearchLimit limits[];
	
	/**
	 * Creates a new combined limit search limit
	 * 
	 * @param limits	Search limits to combine into single limit
	 */
	public CombinedLimit(SearchLimit limits[]) {
		this.limits = limits;
	}
	
	// javadoc inherited
	public void init(SearchNode node) {
		for (int i=0; i<limits.length; i++)
			limits[i].init(node);
	}
	
	// javadoc inherited
	public boolean isOkToContinue(SearchNode node) {
		for (int i=0; i<limits.length; i++)
			if (!limits[i].isOkToContinue(node))
				return false;
		
		return true;
	}
	
	public Object clone() {
		SearchLimit limits[] = new SearchLimit[this.limits.length];
		for (int i=0; i<limits.length; i++)
			limits[i] = (SearchLimit) this.limits[i].clone();
		
		return new CombinedLimit(limits);
	}

}
