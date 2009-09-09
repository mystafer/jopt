package jopt.csp.spi.search;

import jopt.csp.search.SearchAction;
import jopt.csp.search.SearchLimit;
import jopt.csp.search.SearchLimits;
import jopt.csp.spi.search.limit.ChoiceLimit;
import jopt.csp.spi.search.limit.CombinedLimit;
import jopt.csp.spi.search.limit.DeadlineLimit;
import jopt.csp.spi.search.limit.FailLimit;
import jopt.csp.spi.search.limit.TimeLimit;
import jopt.csp.spi.search.tree.SearchTechniqueChange;

/**
 * Creates and returns common search limits that can be used
 * to control searching for solutions to CSP problems
 */
public class SearchLimitsImpl implements SearchLimits {
    public SearchLimitsImpl() {
    }
    
    // javadoc inherited
	public SearchLimit timeLimit(long timeLimitMs) {
        return new TimeLimit(timeLimitMs);
    }
    
    // javadoc inherited
	public SearchLimit failLimit(int maxFailures) {
		return new FailLimit(maxFailures);
	}
    
    // javadoc inherited
	public SearchLimit choiceLimit(int maxChoices) {
		return new ChoiceLimit(maxChoices);
	}
    
    // javadoc inherited
	public SearchLimit deadlineLimit(long endTime) {
		return new DeadlineLimit(endTime);
	}
    
    // javadoc inherited
	public SearchLimit combinedLimit(SearchLimit limits[]) {
		return new CombinedLimit(limits);
	}
    
    // javadoc inherited
    public SearchAction limitSearch(SearchLimit limit, SearchAction action) {
    	return new SearchTechniqueChange(limit, action);
    }
}