package jopt.csp.spi.search.limit;

import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;

import jopt.csp.search.SearchLimit;
import jopt.csp.search.SearchNode;
import jopt.csp.spi.search.tree.TreeNode;

/**
 * Search limit that will stop activating search nodes after a configurable
 * number of backtracking failures
 */
public class FailLimit implements SearchLimit {

	private int maxFailures;
	private int depth;
	private List<Integer> path;
	private BitSet binaryPath;
	private BitSet workBits;
	
	/**
	 * Creates a new fail limit search limiter
	 * 
	 * @param maxFailures	Max number of failures allowed before stopping search
	 */
	public FailLimit(int maxFailures) {
		this.maxFailures = maxFailures;
		this.workBits = new BitSet();
	}
	
	// javadoc inherited
	public void init(SearchNode node) {
		TreeNode n = (TreeNode) node;
		
		this.depth = n.getDepth();
		if (n.isBinary())
			this.path = n.getPath();
		else
			this.binaryPath = n.getBinaryPath();
	}
	
	// javadoc inherited
	public boolean isOkToContinue(SearchNode node) {
		TreeNode n = (TreeNode) node;
		
		// calculate failures
		int failures = (this.binaryPath != null && n.isBinary()) ?
				binaryFailures(n) : nonBinaryFailures(n);
				
		return failures < maxFailures;
	}
	
	public Object clone() {
		return new FailLimit(maxFailures);
	}

    /**
     * Calculates the number of failures from base node to a given node
     * when either node is non-binary
     */
    private int nonBinaryFailures(TreeNode n) {
        // determine maximum possible common depth that
    	List<Integer> currentPath = null;
    	if (this.path != null)
    		currentPath = this.path;
    	else
    		currentPath = convertToNonBinary(this.binaryPath, this.depth);
        List<Integer> nPath = n.getPath();
        int commonDepth = Math.min(currentPath.size(), nPath.size());
        
        // determine depth of common parent
        int commonParentDepth = 0;
        for (int i=0; i<commonDepth; i++) {
            Integer currentMove = (Integer) currentPath.get(i);
        	Integer nChildMove = (Integer) nPath.get(i);
            
            // exit when movements are different
            if (!currentMove.equals(nChildMove))
                break;
            
            commonParentDepth++;
        }
        
        // determine number of non-left movements
        int failures = 0;
        for (int i=commonParentDepth; i<nPath.size()-1; i++) {
            Integer childNum = (Integer) nPath.get(i);
            if (childNum.intValue() > 0)
            	failures++;
        }
        
        return failures;
    }

    /**
     * Calculates the number of failures from base node to a given node
     * when both nodes use binary paths
     */
    private int binaryFailures(TreeNode n) {
        BitSet nPath = n.getBinaryPath();
        
        // paths are identical
        if (this.binaryPath.equals(nPath)) {
            return 0;
        }
        
        // move backwards in list until common parent is located
        // then recompute back to child
        else {
            int maxBits = Math.max(this.depth, n.getDepth());
            
            // initialize work bits
            if (workBits==null || workBits.size()<maxBits) 
                workBits = new BitSet(maxBits*2);
            else
                workBits.clear();
            workBits.or(this.binaryPath);
            workBits.xor(nPath);
            
            // determine depth of common parent
            int commonParentDepth = workBits.nextSetBit(0);
            if (commonParentDepth<0) commonParentDepth = this.depth-1;
            
            // recompute moves to new node
            int failures = 0;
            for (int i=commonParentDepth; i<n.getDepth(); i++) {
                
                // if bit is set, move is to right and is a failure
                if (nPath.get(i))
                	failures++;
            }
            
            return failures;
        }
    }
    
    /**
     * Helper function to convert a binary path to a non-binary path
     */
    private List<Integer> convertToNonBinary(BitSet path, int depth) {
        LinkedList<Integer> list = new LinkedList<Integer>();
        for (int i=0; i<depth; i++) {
            if (path.get(i))
            	list.addLast(new Integer(1));
            else
                list.addLast(new Integer(0));
        }
    	return list;
    }
}
