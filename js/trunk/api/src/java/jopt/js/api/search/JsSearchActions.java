package jopt.js.api.search;

import jopt.csp.search.SearchAction;
import jopt.csp.search.SearchActions;
import jopt.js.api.variable.Activity;

/**
 * An interface that is used to characterize any commonalities between local searches implemented for the Job Scheduler.
 * @author James Boerkoel
 *
 */
public interface JsSearchActions extends SearchActions {
	
    /**
     * Creates an action that will "instantiate" an array of activities.
     * The generator will instantiate each activity (in order) by selecting a resource 
     * and then selecting a start time.
     * 
     * @param activities      Array of activites to instantiate
     */
    public SearchAction generateResourceAndStartTimes(Activity[] activities);
    
    /**
     * Creates an action that will "instantiate" an array of activities in the order
     * specified by the {@link ActivitySelector}.
     * The generator will instantiate each activity by selecting a resource and then 
     * selecting a start time.
     * 
     * @param activities		Array of activites to instantiate
     * @param actSelector		Selects the order in which activities are instantiated
     */
    public SearchAction generateResourceAndStartTimes(Activity[] activities, ActivitySelector actSelector);
    
    /**
     * Creates an action that will "instantiate" an array of activities.
     * The generator will instantiate each activity (in order) by selecting a resource
     * in the order specified by the {@link ResourceSelector} and then selecting a start time.
     * 
     * @param activities     	Array of activites to instantiate
     * @param resSelector		Selects the order in which resources are assigned
     */
    public SearchAction generateResourceAndStartTimes(Activity[] activities, ResourceSelector resSelector);
    
    /**
     * Creates an action that will "instantiate" an array of activities in the order
     * specified by the {@link ActivitySelector} using the {@link ResourceSelector} to select
     * a resource for each one and then selecting a start time.
     * 
     * @param activities      	Array of activites to instantiate
     * @param actSelector		Selects the order in which activities are instantiated
     * @param resSelector		Selects the order in which resources are assigned
     */
    public SearchAction generateResourceAndStartTimes(Activity[] activities, ActivitySelector actSelector, ResourceSelector resSelector);
    
    /**
     * Creates an action that will "instantiate" an array of activities.
     * The generator will instantiate each activity by selecting a resource 
     * and then selecting a start time. The {@link ResourceSetSelector} is
     * used to determine the order in which alternative resource sets are selected.
     * 
     * @param activities      	Array of activites to instantiate
     * @param resSelector		Selects the order in which resources are assigned from alternative resource sets
     * @param resSetSelector	Determines the order in which alternative resource sets should be selected
     */
    public SearchAction generateResourceAndStartTimes(Activity[] activities, ResourceSelector resSelector, ResourceSetSelector resSetSelector);
    
    /**
     * Creates an action that will "instantiate" an array of activities.
     * The generator will instantiate each activity by selecting a resource 
     * and then selecting a start time.
     * 
     * @param activities      	Array of activites to instantiate
     * @param actSelector		Selects the order in which activities are instantiated
     * @param resSelector		Selects the order in which resources are assigned from alternative resource sets
     * @param resSetSelector    Determines the order in which alternative resource sets should be selected
     */
    public SearchAction generateResourceAndStartTimes(Activity[] activities, ActivitySelector actSelector, ResourceSelector resSelector, ResourceSetSelector resSetSelector);
    
    /**
     * Creates an action that will "instantiate" an array of activities.
     * The generator will instantiate each activity by selecting a resource 
     * and then selecting a start time. The {@link StartTimeSelector} is
     * used to determine the order in which potential start times are selected. 
     * 
     * @param activities      Array of activites to instantiate
     * @param startTimeSelector		Determines the order in which the start times are assigned to activities
     */
    public SearchAction generateResourceAndStartTimes(Activity[] activities, StartTimeSelector startTimeSelector);
    
    /**
     * Creates an action that will "instantiate" an array of activities.
     * The generator will instantiate each activity by selecting a resource 
     * and then selecting a start time.
     * 
     * @param activities		Array of activites to instantiate
     * @param actSelector		Selects the order in which activities are instantiated
     * @param startTimeSelector		Determines the order in which the start times are assigned to activities
     */
    public SearchAction generateResourceAndStartTimes(Activity[] activities, ActivitySelector actSelector, StartTimeSelector startTimeSelector);
    
    /**
     * Creates an action that will "instantiate" an array of activities.
     * The generator will instantiate each activity by selecting a resource 
     * and then selecting a start time.
     * 
     * @param activities     	Array of activites to instantiate
     * @param startTimeSelector		Determines the order in which the start times are assigned to activities
     * @param resSelector       Selects the order in which resources are assigned from alternative resource sets
     */
    public SearchAction generateResourceAndStartTimes(Activity[] activities, StartTimeSelector startTimeSelector, ResourceSelector resSelector);
    
    /**
     * Creates an action that will "instantiate" an array of activities.
     * The generator will instantiate each activity by selecting a resource 
     * and then selecting a start time.
     * 
     * @param activities      	Array of activites to instantiate
     * @param actSelector		Selects the order in which activities are instantiated
     * @param startTimeSelector		Determines the order in which the start times are assigned to activities
     * @param resSelector       Selects the order in which resources are assigned from alternative resource sets
     */
    public SearchAction generateResourceAndStartTimes(Activity[] activities, ActivitySelector actSelector, StartTimeSelector startTimeSelector, ResourceSelector resSelector);
    
    /**
     * Creates an action that will "instantiate" an array of activities.
     * The generator will instantiate each activity by selecting a resource 
     * and then selecting a start time.
     * 
     * @param activities      	Array of activites to instantiate
     * @param startTimeSelector		Determines the order in which the start times are assigned to activities
     * @param resSelector       Selects the order in which resources are assigned from alternative resource sets
     * @param resSetSelector	Determines the order in which alternative resource sets should be selected 
     */
    public SearchAction generateResourceAndStartTimes(Activity[] activities, StartTimeSelector startTimeSelector, ResourceSelector resSelector, ResourceSetSelector resSetSelector);
    
    /**
     * Creates an action that will "instantiate" an array of activities.
     * The generator will instantiate each activity by selecting a resource 
     * and then selecting a start time.
     * 
     * @param activities      	Array of activites to instantiate
     * @param actSelector		Selects the order in which activities are instantiated
     * @param startTimeSelector		Determines the order in which the start times are assigned to activities
     * @param resSelector       Selects the order in which resources are assigned from alternative resource sets
     * @param resSetSelector	Determines the order in which alternative resource sets should be selected
     */
    public SearchAction generateResourceAndStartTimes(Activity[] activities, ActivitySelector actSelector, StartTimeSelector startTimeSelector, ResourceSelector resSelector, ResourceSetSelector resSetSelector);
	
    /**
     * Creates an action that will "instantiate" an array of activities.
     * The generator will instantiate each activity by selecting a start time
     * and then selecting resource.
     * 
     * @param activities      Array of activites to instantiate
     */
    public SearchAction generateStartTimeAndResources(Activity[] activities);
    
    /**
     * Creates an action that will "instantiate" an array of activities.
     * The generator will instantiate each activity by selecting a start time
     * and then selecting resource.
     * 
     * @param activities		Array of activites to instantiate
     * @param actSelector		Selects the order in which activities are instantiated
     */
    public SearchAction generateStartTimeAndResources(Activity[] activities, ActivitySelector actSelector);
    
    /**
     * Creates an action that will "instantiate" an array of activities.
     * The generator will instantiate each activity by selecting a start time
     * and then selecting resource.
     * 
     * @param activities     	Array of activites to instantiate
     * @param resSelector       Selects the order in which resources are assigned from alternative resource sets
     */
    public SearchAction generateStartTimeAndResources(Activity[] activities, ResourceSelector resSelector);
    
    /**
     * Creates an action that will "instantiate" an array of activities.
     * The generator will instantiate each activity by selecting a start time
     * and then selecting resource.
     * 
     * @param activities      	Array of activites to instantiate
     * @param actSelector		Selects the order in which activities are instantiated
     * @param resSelector		Selects the order in which resources are assigned from alternative resource sets
     */
    public SearchAction generateStartTimeAndResources(Activity[] activities, ActivitySelector actSelector, ResourceSelector resSelector);
    
    /**
     * Creates an action that will "instantiate" an array of activities.
     * The generator will instantiate each activity by selecting a start time
     * and then selecting resource.
     * 
     * @param activities      	Array of activites to instantiate
     * @param resSelector		Selects the order in which resources are assigned from alternative resource sets
     * @param resSetSelector	Determines the order in which alternative resource sets should be selected 
     */
    public SearchAction generateStartTimeAndResources(Activity[] activities, ResourceSelector resSelector, ResourceSetSelector resSetSelector);
    
    /**
     * Creates an action that will "instantiate" an array of activities.
     * The generator will instantiate each activity by selecting a start time
     * and then selecting resource.
     * 
     * @param activities      	Array of activites to instantiate
     * @param actSelector		Selects the order in which activities are instantiated
     * @param resSelector		Selects the order in which resources are assigned from alternative resource sets
     * @param resSetSelector	Determines the order in which alternative resource sets should be selected
     */
    public SearchAction generateStartTimeAndResources(Activity[] activities, ActivitySelector actSelector, ResourceSelector resSelector, ResourceSetSelector resSetSelector);
	
    /**
     * Creates an action that will "instantiate" an array of activities.
     * The generator will instantiate each activity by selecting a start time
     * and then selecting resource.
     * 
     * @param activities      		Array of activites to instantiate
     * @param startTimeSelector		Determines the order in which the start times are assigned to activities
     */
    public SearchAction generateStartTimeAndResources(Activity[] activities,StartTimeSelector startTimeSelector);
    
    /**
     * Creates an action that will "instantiate" an array of activities.
     * The generator will instantiate each activity by selecting a start time
     * and then selecting resource.
     * 
     * @param activities		Array of activites to instantiate
     * @param actSelector		Selects the order in which activities are instantiated
     * @param startTimeSelector		Determines the order in which the start times are assigned to activities
     */
    public SearchAction generateStartTimeAndResources(Activity[] activities, ActivitySelector actSelector, StartTimeSelector startTimeSelector);
    
    /**
     * Creates an action that will "instantiate" an array of activities.
     * The generator will instantiate each activity by selecting a start time
     * and then selecting resource.
     * 
     * @param activities     	Array of activites to instantiate
     * @param startTimeSelector		Determines the order in which the start times are assigned to activities
     * @param resSelector		Selects the order in which resources are assigned from alternative resource sets
     */
    public SearchAction generateStartTimeAndResources(Activity[] activities, StartTimeSelector startTimeSelector, ResourceSelector resSelector);
    
    /**
     * Creates an action that will "instantiate" an array of activities.
     * The generator will instantiate each activity by selecting a start time
     * and then selecting resource.
     * 
     * @param activities      	Array of activites to instantiate
     * @param actSelector		Selects the order in which activities are instantiated
     * @param startTimeSelector		Determines the order in which the start times are assigned to activities
     * @param resSelector		Selects the order in which resources are assigned from alternative resource sets
     */
    public SearchAction generateStartTimeAndResources(Activity[] activities, ActivitySelector actSelector, StartTimeSelector startTimeSelector, ResourceSelector resSelector);
    
    /**
     * Creates an action that will "instantiate" an array of activities.
     * The generator will instantiate each activity by selecting a start time
     * and then selecting resource.
     * 
     * @param activities      	Array of activites to instantiate
     * @param startTimeSelector		Determines the order in which the start times are assigned to activities
     * @param resSelector		Selects the order in which resources are assigned from alternative resource sets
     * @param resSetSelector	Determines the order in which alternative resource sets should be selected 
     */
    public SearchAction generateStartTimeAndResources(Activity[] activities, StartTimeSelector startTimeSelector, ResourceSelector resSelector, ResourceSetSelector resSetSelector);
    
    /**
     * Creates an action that will "instantiate" an array of activities.
     * The generator will instantiate each activity by selecting a start time
     * and then selecting resource.
     * 
     * @param activities      	Array of activites to instantiate
     * @param actSelector		Selects the order in which activities are instantiated
     * @param startTimeSelector		Determines the order in which the start times are assigned to activities
     * @param resSelector		Selects the order in which resources are assigned from alternative resource sets
     * @param resSetSelector	Determines the order in which alternative resource sets should be selected
     */
    public SearchAction generateStartTimeAndResources(Activity[] activities, ActivitySelector actSelector, StartTimeSelector startTimeSelector, ResourceSelector resSelector, ResourceSetSelector resSetSelector);
	
    /**
     * Creates an action that will "instantiate" the start times of an array of activities.
     * The generator only selects a start time for each activity; no resource selection is made.
     * 
     * @param activities      Array of activites to instantiate
     */
    public SearchAction generateStartTimes(Activity[] activities);
    
    /**
     * Creates an action that will "instantiate" the start times of an array of activities.
     * The generator only selects a start time for each activity; no resource selection is made.
     * 
     * @param activities		Array of activites to instantiate
     * @param actSelector		Selects the order in which activities are instantiated
     */
    public SearchAction generateStartTimes(Activity[] activities, ActivitySelector actSelector);
    
    /**
     * Creates an action that will "instantiate" the start times of an array of activities.
     * The generator only selects a start time for each activity; no resource selection is made. 
     * 
     * @param activities      	Array of activites to instantiate
     * @param startTimeSelector Selects which start time to attempt to assign to a given activity
     */
    public SearchAction generateStartTimes(Activity[] activities, StartTimeSelector startTimeSelector);
    
    /**
     * Creates an action that will "instantiate" the start times of an array of activities.
     * The generator only selects a start time for each activity; no resource selection is made.
     * 
     * @param activities		Array of activites to instantiate
     * @param actSelector		Selects the order in which activities are instantiated
     * @param startTimeSelector Selects which start time to attempt to assign to a given activity
     */
    public SearchAction generateStartTimes(Activity[] activities, ActivitySelector actSelector, StartTimeSelector startTimeSelector);
    
    /**
     * Creates an action that will instantiate the resource distribution of activities in order.
     * The generator only selects a resource for each activity; no start time selection is made.
     * 
     * @param activities      Array of activites to instantiate
     */
    public SearchAction generateResources(Activity[] activities);
    
    /**
     * Creates an action that will instantiate the resource distribution of activities in order.
     * The generator only selects a resource for each activity; no start time selection is made.  
     * 
     * @param activities     	Array of activites to instantiate
     * @param resSelector		Selects the order in which resources are assigned from alternative resource sets
     */
    public SearchAction generateResources(Activity[] activities, ResourceSelector resSelector);
    
    /**
     * Creates an action that will instantiate the resource distribution of activities in order.
     * The generator only selects a resource for each activity; no start time selection is made.  
     * 
     * @param activities      	Array of activites to instantiate
     * @param resSelector		Selects the order in which resources are assigned from alternative resource sets
     * @param resSetSelector	Determines the order in which alternative resource sets should be selected 
     */
    public SearchAction generateResources(Activity[] activities, ResourceSelector resSelector, ResourceSetSelector resSetSelector);	
    
    /**
     * Creates an action that will instantiate the resource distribution of activities in order.
     * The generator only selects a resource for each activity; no start time selection is made.  
     * 
     * @param activities      Array of activites to instantiate
     * @param actSelector		Selects the order in which the activities are instantiated
     */
    public SearchAction generateResources(Activity[] activities, ActivitySelector actSelector);
    
    /**
     * Creates an action that will instantiate the resource distribution of activities in order.
     * The generator only selects a resource for each activity; no start time selection is made.  
     * 
     * @param activities     	Array of activites to instantiate
     * @param actSelector		Selects the order in which the activities are instantiated
     * @param resSelector		Selects the order in which resources are assigned from alternative resource sets
     */
    public SearchAction generateResources(Activity[] activities, ActivitySelector actSelector,  ResourceSelector resSelector);
    
    /**
     * Creates an action that will instantiate the resource distribution of activities in order.
     * The generator only selects a resource for each activity; no start time selection is made.  
     * 
     * @param activities      	Array of activites to instantiate
     * @param actSelector		Selects the order in which the activities are instantiated
     * @param resSelector		Selects the order in which resources are assigned from alternative resource sets
     * @param resSetSelector	Determines the order in which alternative resource sets should be selected 
     */
    public SearchAction generateResources(Activity[] activities, ActivitySelector actSelector, ResourceSelector resSelector, ResourceSetSelector resSetSelector);
}
