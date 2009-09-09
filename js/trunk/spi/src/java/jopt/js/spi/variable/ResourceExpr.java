package jopt.js.spi.variable;

import jopt.csp.spi.arcalgorithm.graph.NodeArcGraph;
import jopt.csp.spi.arcalgorithm.variable.IntExpr;
import jopt.csp.spi.arcalgorithm.variable.VariableChangeBase;
import jopt.csp.spi.solver.ChoicePointDataSource;
import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.util.IntIntervalSet;
import jopt.csp.variable.CspIntExpr;
import jopt.js.api.variable.Resource;
import jopt.js.api.variable.SchedulerExpression;
import jopt.js.spi.domain.resource.ResourceDomain;
import jopt.js.spi.graph.node.ResourceNode;
import jopt.js.spi.util.IDStore;

/**
 * Expression used to hold pertinent resource information
 * 
 * @author James Boerkoel
 */
public class ResourceExpr extends VariableChangeBase implements Resource, SchedulerExpression {

    protected ResourceNode resNode;
    protected String name;
    protected int id;
    protected ResourceDomain resDom;
    
    protected CspIntExpr numOps;
    protected CspIntExpr beginTime;
    protected CspIntExpr completeTime;
    protected CspIntExpr makeSpan;
    
    /**
     * Creates a resource expression with the given name and domain
     * @param name the name of this resource
     * @param domain the domain for this resource expression
     */
    public ResourceExpr(String name, ResourceDomain domain) {
    	this.resDom = domain;
    	this.name = name;
    	this.id = IDStore.generateUniqueID();
    }
    
	// javadoc is inherited
	public CspIntExpr getNumOperationsExpr() {
		if (numOps == null) {
			numOps = new IntExpr(name+" num ops",resDom.getNumOps());
		}
		return numOps;
	}
    
    // javadoc is inherited
    public CspIntExpr getBeginTimeExpr() {
    	if (beginTime == null) {
    		beginTime = new IntExpr(name+" begin time",resDom.getBeginTime());
		}
		return beginTime;
    }
    
    // javadoc is inherited
    public CspIntExpr getCompletionTimeExpr() {
    	if (completeTime == null) {
    		completeTime = new IntExpr(name+" completion time",resDom.getCompleteTime());
		}
		return completeTime;
    }
    
    // javadoc is inherited
    public CspIntExpr getMakeSpanExpr() {
    	if (makeSpan == null) {
    		makeSpan = new IntExpr(name+" make span",resDom.getMakeSpan());
		}
		return makeSpan;
    }
    
    // javadoc is inherited
    public String getName() {
    	if (name==null) {
    		return "res "+id;
    	}
    	return name;
    }

    // javadoc is inherited
	public boolean isBuilt() {
		return resDom.isBuilt();
	}
    
    // javadoc is inherited
	public void setBuilt(boolean built) {
		resDom.setBuilt(built);
	}
    
    // javadoc is inherited
	public int getType() {
		return resDom.getType();
	}
    
    /**
     * Returns the node of this resource
     * @return Node of this resource
     */
    public ResourceNode getNode() {
    	if (resNode==null) {
    		resNode = new ResourceNode(name, resDom, id);
    	}
    	return resNode;
    }
    
    // javadoc is inherited
    public int getID() {
    	return id;
    }
    
    // javadoc is inherited
    public int getResourceStart() {
    	return resDom.getResourceStart();
    }
    
    // javadoc is inherited
    public int getResourceEnd() {
    	return resDom.getResourceEnd();
    }
    
    // javadoc is inherited
	public IntIntervalSet findAvailIntervals(int start, int end, int quantity) {
		return resDom.findAvailIntervals(start,end,quantity);
	}
	
    // javadoc is inherited
	public boolean isResourceAvailable(int operationID,int start, int end, int quantity) {
		return resDom.isResourceAvailable(operationID,start,end,quantity);
	}
	
    // javadoc is inherited
	public int maxAvailableResource(int start, int end) {
		return resDom.maxAvailableResource(start,end);
	}

    // javadoc is inherited
    public boolean isBound() {
    	return resDom.isBound();
    }
    
    /**
     * Returns true if any of the resource has been allocated to be used by any operations
     */
    public boolean isUsed() {
    	return resDom.isUsed();
    }

	// javadoc is inherited
    public void updateGraph(NodeArcGraph graph) {
        if (!graph.containsNode(getNode())) {
            graph.addNode(getNode());
        }
    }
    
    // javadoc is inherited
    public void setChoicePointStack(ChoicePointStack cps) {
    	((ChoicePointDataSource) resNode).setChoicePointStack(cps);
    }
    
    // javadoc is inherited
    public boolean choicePointStackSet() {
        return ((ChoicePointDataSource) resNode).choicePointStackSet();
    }

    public String toString() {
    	if (name!=null) {
    		return name + resDom.toString();
    	}
    	return "Resource"+id+resDom.toString();
    }

}