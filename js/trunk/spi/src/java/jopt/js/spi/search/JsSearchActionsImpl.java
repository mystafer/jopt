package jopt.js.spi.search;

import jopt.csp.search.SearchAction;
import jopt.csp.spi.search.SearchActionsImpl;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.js.api.search.ActivitySelector;
import jopt.js.api.search.JsSearchActions;
import jopt.js.api.search.ResourceSelector;
import jopt.js.api.search.ResourceSetSelector;
import jopt.js.api.search.StartTimeSelector;
import jopt.js.api.variable.Activity;
import jopt.js.spi.search.actions.GenerateActivityResourceAction;
import jopt.js.spi.search.actions.GenerateActivityResourceThenStartTimeAction;
import jopt.js.spi.search.actions.GenerateActivityStartTimeAction;
import jopt.js.spi.search.actions.GenerateActivityStartTimeThenResourceAction;

public class JsSearchActionsImpl extends SearchActionsImpl implements JsSearchActions {

    public JsSearchActionsImpl(ConstraintStore store) {
        super(store);
    }
    
    // javadoc is inherited
    public SearchAction generateResourceAndStartTimes(Activity[] activities) {
    	return new GenerateActivityResourceThenStartTimeAction(activities);
    }

    // javadoc is inherited
	public SearchAction generateResourceAndStartTimes(Activity[] activities, ActivitySelector actSelector, ResourceSelector resSelector, ResourceSetSelector resSetSelector) {
		return new GenerateActivityResourceThenStartTimeAction(activities,actSelector, resSelector, resSetSelector);
	}

    // javadoc is inherited
	public SearchAction generateResourceAndStartTimes(Activity[] activities, ActivitySelector actSelector, ResourceSelector resSelector) {
		return new GenerateActivityResourceThenStartTimeAction(activities,actSelector, resSelector);
	}

    // javadoc is inherited
	public SearchAction generateResourceAndStartTimes(Activity[] activities, ActivitySelector actSelector, StartTimeSelector startTimeSelector, ResourceSelector resSelector, ResourceSetSelector resSetSelector) {
		return new GenerateActivityResourceThenStartTimeAction(activities, actSelector, startTimeSelector, resSelector, resSetSelector);
	}

    // javadoc is inherited
	public SearchAction generateResourceAndStartTimes(Activity[] activities, ActivitySelector actSelector, StartTimeSelector startTimeSelector, ResourceSelector resSelector) {
		return new GenerateActivityResourceThenStartTimeAction(activities, actSelector, startTimeSelector, resSelector);
	}

    // javadoc is inherited
	public SearchAction generateResourceAndStartTimes(Activity[] activities, ActivitySelector actSelector, StartTimeSelector startTimeSelector) {
		return new GenerateActivityResourceThenStartTimeAction(activities, actSelector, startTimeSelector);
	}

    // javadoc is inherited
	public SearchAction generateResourceAndStartTimes(Activity[] activities, ActivitySelector actSelector) {
		return new GenerateActivityResourceThenStartTimeAction(activities, actSelector);
	}

    // javadoc is inherited
	public SearchAction generateResourceAndStartTimes(Activity[] activities, ResourceSelector resSelector, ResourceSetSelector resSetSelector) {
		return new GenerateActivityResourceThenStartTimeAction(activities, resSelector, resSetSelector);
	}

    // javadoc is inherited
	public SearchAction generateResourceAndStartTimes(Activity[] activities, ResourceSelector resSelector) {
		return new GenerateActivityResourceThenStartTimeAction(activities, resSelector);
	}

    // javadoc is inherited
	public SearchAction generateResourceAndStartTimes(Activity[] activities, StartTimeSelector startTimeSelector, ResourceSelector resSelector, ResourceSetSelector resSetSelector) {
		return new GenerateActivityResourceThenStartTimeAction(activities, startTimeSelector, resSelector, resSetSelector);
	}

    // javadoc is inherited
	public SearchAction generateResourceAndStartTimes(Activity[] activities, StartTimeSelector startTimeSelector, ResourceSelector resSelector) {
		return new GenerateActivityResourceThenStartTimeAction(activities, startTimeSelector, resSelector);
	}

    // javadoc is inherited
	public SearchAction generateResourceAndStartTimes(Activity[] activities, StartTimeSelector startTimeSelector) {
		return new GenerateActivityResourceThenStartTimeAction(activities, startTimeSelector);
	}

    // javadoc is inherited
	public SearchAction generateResources(Activity[] activities, ActivitySelector actSelector, ResourceSelector resSelector, ResourceSetSelector resSetSelector) {
		return new GenerateActivityResourceAction(activities, actSelector, resSelector, resSetSelector);
	}

    // javadoc is inherited
	public SearchAction generateResources(Activity[] activities, ActivitySelector actSelector, ResourceSelector resSelector) {
		return new GenerateActivityResourceAction(activities, actSelector, resSelector);
	}

    // javadoc is inherited
	public SearchAction generateResources(Activity[] activities, ActivitySelector actSelector) {
		return new GenerateActivityResourceAction(activities, actSelector);
	}

    // javadoc is inherited
	public SearchAction generateResources(Activity[] activities, ResourceSelector resSelector, ResourceSetSelector resSetSelector) {
		return new GenerateActivityResourceAction(activities, resSelector, resSetSelector);
	}

    // javadoc is inherited
	public SearchAction generateResources(Activity[] activities, ResourceSelector resSelector) {
		return new GenerateActivityResourceAction(activities, resSelector);
	}

    // javadoc is inherited
	public SearchAction generateResources(Activity[] activities) {
		return new GenerateActivityResourceAction(activities);
	}

    // javadoc is inherited
	public SearchAction generateStartTimeAndResources(Activity[] activities, ActivitySelector actSelector, ResourceSelector resSelector, ResourceSetSelector resSetSelector) {
		return new GenerateActivityStartTimeThenResourceAction(activities, actSelector, resSelector, resSetSelector);
	}

    // javadoc is inherited
	public SearchAction generateStartTimeAndResources(Activity[] activities, ActivitySelector actSelector, ResourceSelector resSelector) {
		return new GenerateActivityStartTimeThenResourceAction(activities, actSelector, resSelector);
	}

    // javadoc is inherited
	public SearchAction generateStartTimeAndResources(Activity[] activities, ActivitySelector actSelector, StartTimeSelector startTimeSelector, ResourceSelector resSelector, ResourceSetSelector resSetSelector) {
		return new GenerateActivityStartTimeThenResourceAction(activities, actSelector, startTimeSelector, resSelector, resSetSelector);
	}

    // javadoc is inherited
	public SearchAction generateStartTimeAndResources(Activity[] activities, ActivitySelector actSelector, StartTimeSelector startTimeSelector, ResourceSelector resSelector) {
		return new GenerateActivityStartTimeThenResourceAction(activities, actSelector, startTimeSelector, resSelector);
	}

    // javadoc is inherited
	public SearchAction generateStartTimeAndResources(Activity[] activities, ActivitySelector actSelector, StartTimeSelector startTimeSelector) {
		return new GenerateActivityStartTimeThenResourceAction(activities, actSelector, startTimeSelector);
	}

    // javadoc is inherited
	public SearchAction generateStartTimeAndResources(Activity[] activities, ActivitySelector actSelector) {
		return new GenerateActivityStartTimeThenResourceAction(activities, actSelector);
	}

    // javadoc is inherited
	public SearchAction generateStartTimeAndResources(Activity[] activities, ResourceSelector resSelector, ResourceSetSelector resSetSelector) {
		return new GenerateActivityStartTimeThenResourceAction(activities, resSelector, resSetSelector);
	}

    // javadoc is inherited
	public SearchAction generateStartTimeAndResources(Activity[] activities, ResourceSelector resSelector) {
		return new GenerateActivityStartTimeThenResourceAction(activities, resSelector);
	}

    // javadoc is inherited
	public SearchAction generateStartTimeAndResources(Activity[] activities, StartTimeSelector startTimeSelector, ResourceSelector resSelector, ResourceSetSelector resSetSelector) {
		return new GenerateActivityStartTimeThenResourceAction(activities, startTimeSelector, resSelector, resSetSelector);
	}

    // javadoc is inherited
	public SearchAction generateStartTimeAndResources(Activity[] activities, StartTimeSelector startTimeSelector, ResourceSelector resSelector) {
		return new GenerateActivityStartTimeThenResourceAction(activities, startTimeSelector, resSelector);
	}

    // javadoc is inherited
	public SearchAction generateStartTimeAndResources(Activity[] activities, StartTimeSelector startTimeSelector) {
		return new GenerateActivityStartTimeThenResourceAction(activities, startTimeSelector);
	}

    // javadoc is inherited
	public SearchAction generateStartTimeAndResources(Activity[] activities) {
		return new GenerateActivityStartTimeThenResourceAction(activities);
	}

    // javadoc is inherited
	public SearchAction generateStartTimes(Activity[] activities, ActivitySelector actSelector, StartTimeSelector startTimeSelector) {
		return new GenerateActivityStartTimeAction(activities, actSelector, startTimeSelector);
	}

    // javadoc is inherited
	public SearchAction generateStartTimes(Activity[] activities, ActivitySelector actSelector) {
		return new GenerateActivityStartTimeAction(activities, actSelector);
	}

    // javadoc is inherited
	public SearchAction generateStartTimes(Activity[] activities, StartTimeSelector startTimeSelector) {
		return new GenerateActivityStartTimeAction(activities, startTimeSelector);
	}

    // javadoc is inherited
	public SearchAction generateStartTimes(Activity[] activities) {
		return new GenerateActivityStartTimeAction(activities);
	}
    
}
