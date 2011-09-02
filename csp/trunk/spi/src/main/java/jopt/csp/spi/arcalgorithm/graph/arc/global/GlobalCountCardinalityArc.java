/*
 * GenericNumAllDiffArc.java
 * 
 * Created on Jul 28, 2005
 */
package jopt.csp.spi.arcalgorithm.graph.arc.global;

import java.util.Arrays;

import jopt.csp.spi.arcalgorithm.graph.arc.NumArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericArc;
import jopt.csp.spi.arcalgorithm.graph.node.IntNode;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.util.DomainChangeType;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.variable.PropagationFailureException;

/**
 * Generic arc that ensures all ensures that the count variables accurately reflect the number of occurrences
 * of the corresponding value.
 * 
 * @author Jim Boerkoel
 * @version $Revision $
 */
public class GlobalCountCardinalityArc extends GenericArc implements NumArc {

    //This variable is kept to see if propagation really needs to occur since last time
//    private int size;    
    //These are the variables that count how many occurrences of various values there among the sources
    private IntNode[] count;
    //The values which we would like to count
    private Number vals[];
    
    /**
     * Generates a global count cardinality arc, counting the number of times that each val in the val array occurs in sources
     * @param sources -  the collection of variables whose values are to be counted
     * @param vals - the values of whose presence among sources we would like to count
     * @param count - the node representing the possible number of times that the value will show up
     */
    public GlobalCountCardinalityArc(NumNode sources[], Number vals[], IntNode[] count) {
        super(sources, count);        
        // initialize source dependencies to nodes
        sourceDependencies = new int[sources.length];
        for (int i=0; i<sources.length; i++){
            sourceDependencies[i] = DomainChangeType.DOMAIN;
        }
//        size = sources.length * sources.length;
        
        this.vals = (Number[])vals.clone();
        this.count= new IntNode[count.length];
        
        Arrays.sort(this.vals);
        
        //We will leave all arrays sorted by the vals
        for (int i=0; i< vals.length; i++) {
            Number num = vals[i];
            int index = Arrays.binarySearch(this.vals,num);
            this.count[index] = count[i];
        }
                
        
    }
    //Utility to convert node[] to numnode[]
    private static NumNode[] toNumNodeArray(Node[] sources) {
        NumNode[] numSources = new NumNode[sources.length];
        for (int i=0; i< sources.length; i++) {
            numSources[i] = (NumNode)sources[i];
        }
        return numSources;
    }
    
    
    private void propagateNumNode(NumNode[] sources) throws PropagationFailureException {
        int[] minCount = new int[count.length];
        int[] maxCount = new int[count.length];
        //Count the number of times each value occures
        for (int i=0; i<sources.length; i++) {
            //If it is bound, only then can we adjust the min
            if (sources[i].isBound()) {
                Number boundVal = sources[i].getMax();
                if (boundVal instanceof MutableNumber) {
                    boundVal = ((MutableNumber)boundVal).toConst();
                }
                int index = Arrays.binarySearch(vals,boundVal);
                if (index>=0)
                    minCount[index]++;
            }
            Number val = sources[i].getMin();
            for (int j=0; j<sources[i].getSize();j++) {
                if (val instanceof MutableNumber) {
                    val = ((MutableNumber)val).toConst();
                }
                int index = Arrays.binarySearch(vals,val);
                if (index>=0)
                    maxCount[index]++;
                val = sources[i].getNextHigher(val);
            }
        }
        
        for(int i=0; i<count.length; i++) {
            count[i].setRange(new Integer(minCount[i]),new Integer(maxCount[i]));
        }
        
    }
    
    // javadoc inherited from Arc
    public void propagate(Node src) throws PropagationFailureException { 
        propagate();
    }
    
    // javadoc inherited from Arc
    public void propagate() throws PropagationFailureException {
        propagateNumNode(toNumNodeArray(sources));
    }
}
