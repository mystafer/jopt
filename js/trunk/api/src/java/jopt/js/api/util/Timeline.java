package jopt.js.api.util;

import jopt.csp.util.IntIntervalSet;

/**
 * Represents a timeline.  Essentially consists of one or more intervals starting and ending
 * at int values.
 * 
 * @author Chris Johnson
 */
public class Timeline extends IntIntervalSet {

    public Timeline (int start, int end) {
        super();
        this.add(start, end);
    }
}
