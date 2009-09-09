package jopt.csp.function;

/**
 * Defines a piecewise step function that may be used with some constraints.
 * 
 * @author Nick Coleman
 * @version $Revision: 1.8 $
 */
public class PiecewiseStepFunction implements PiecewiseFunction {
    private static final int INIT_SIZE = 8;

	private int segmentCount;
	private double minX;
	private double maxX;
    private double[] pointX;
    private double[] pointY;
	
	public PiecewiseStepFunction() {
		this(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0);
	}
	public PiecewiseStepFunction(double xmin, double xmax, double val) {
		this.pointX = new double[INIT_SIZE];
		this.pointY = new double[INIT_SIZE];
		
		this.minX = xmin;
		this.maxX = xmax;
		
		this.segmentCount = 1;
		this.pointX[0] = minX;
		this.pointX[1] = maxX;
		this.pointY[0] = val;
	}

    /** 
     * Increases my capacity, if necessary, to ensure that I can hold at 
     * least the number of elements specified by the minimum capacity 
     * argument without growing.
     */
    private void ensureCapacity(int mincap) {
        if(mincap > pointX.length) {
            int newcap = Math.max((pointX.length * 3)/2 + 1, mincap);
            int oldcap = pointX.length;
            
            // expand pointX array
            double[] olddata = pointX;
            pointX = new double[newcap];
            System.arraycopy(olddata, 0, pointX, 0, oldcap);
            
            // expand pointY array
            olddata = pointY;
            pointY = new double[newcap];
            System.arraycopy(olddata, 0, pointY, 0, oldcap);
        }
    }
	
	/* (non-Javadoc)
	 * @see jopt.csp.function.PiecewiseFunction#getIntervalMinX()
	 */
	public double getIntervalMinX() {
		return minX;
	}
	
	/* (non-Javadoc)
	 * @see jopt.csp.function.PiecewiseFunction#getIntervalMaxX()
	 */
	public double getIntervalMaxX() {
		return maxX;
	}
	
	/**
	 * Returns the index of the segment containing a given point X
	 */
	private int indexOfSegment(double x) {
		if (x < minX || x >= maxX)
			throw new IndexOutOfBoundsException("X(" + x + ") is not valid for function");
		
        // initialize range of indices that might contain
        // segment to entire list
        int lowIdx = 0;
        int highIdx = segmentCount-1;
        
        // loop through indices and locate segment
        while (lowIdx <= highIdx) {
            int idx = (lowIdx+highIdx)/2;
            
            // if x is before start of segment, reduce high index to be index below current median
            if (x < pointX[idx]) highIdx = idx - 1;
            
            // check if x is contained in segment
            else if (x < pointX[idx+1]) return idx;
            
            // otherwise value must be above
            else lowIdx = idx + 1;
        }
        
		throw new IllegalStateException("Point " + x + " was not located for function");
	}
	
	/**
	 * Returns the index of the segment containing or ending just before a given point X
	 */
	private int indexOfSegmentEnd(int startIdx, double x) {
		int endIdx = (x==maxX) ? segmentCount-1 : indexOfSegment(x);
		if (endIdx > startIdx && x == pointX[endIdx]) endIdx--;
		return endIdx;
	}
	
	/* (non-Javadoc)
	 * @see jopt.csp.function.PiecewiseFunction#getY(double)
	 */
	public double getY(double x) {
		int idx = indexOfSegment(x);
		return pointY[idx];
	}
	
	/* (non-Javadoc)
	 * @see jopt.csp.function.PiecewiseFunction#getMinY(double, double)
	 */
	public double getMinY(double x1, double x2) {
		if (x1 > x2)
			throw new IllegalArgumentException("X values are out of order");
		if (x2 >= maxX)
			throw new IndexOutOfBoundsException("X(" + x2 + ")is not valid for function");
		if (x1 < minX)
			throw new IndexOutOfBoundsException("X(" + x1 + ") is not valid for function");
		if (x1 == x2)
			return getY(x1);
			
		// loop over all affected segments
		int idx = indexOfSegment(x1);
		double min = Double.POSITIVE_INFINITY;
		do {
			// check if new minimum is found
			double yMin = pointY[idx];
			if (yMin < min)
				min = yMin;
			
			idx++;
		} while (idx < segmentCount && idx <= indexOfSegmentEnd(idx, x2));
		
		return min;
	}
	
	/* (non-Javadoc)
	 * @see jopt.csp.function.PiecewiseFunction#getMaxY(double, double)
	 */
	public double getMaxY(double x1, double x2) {
		if (x1 > x2)
			throw new IllegalArgumentException("X values are out of order");
		if (x2 >= maxX)
			throw new IndexOutOfBoundsException("X(" + x2 + ") is not valid for function");
		if (x1 < minX)
			throw new IndexOutOfBoundsException("X(" + x1 + ") is not valid for function");
		if (x1 == x2)
			return getY(x1);
			
		// loop over all affected segments
		int idx = indexOfSegment(x1);
		double max = Double.NEGATIVE_INFINITY;
		do {
			// check if new maximum is found
			double yMax = pointY[idx];
			if (yMax > max)
				max = yMax;
			
			idx++;
		} while (idx < segmentCount && idx <= indexOfSegmentEnd(idx, x2));
		
		return max;
	}
	
	/**
	 * Sets function to a constant over an interval [x1, x2)
	 * 
	 * for x in [x1, x2), y = v
	 */
	public void setValue(double x1, double x2, double v) {
		// find segments that contain range
		int startIdx = indexOfSegment(x1);
		int endIdx = indexOfSegmentEnd(startIdx, x2);
		
        // check if range is redundant
        if (startIdx == endIdx && pointY[startIdx] == v) return;
        
		// check if new segment should combine with previous or next segment
		if (startIdx > 0 && pointY[startIdx-1] == v) {
			x1 = pointX[startIdx-1];
			startIdx--;
		}
		if (endIdx < segmentCount-1 && pointY[endIdx+1] == v) {
			x2 = pointX[endIdx+2];
			endIdx++;
		}
		
		// store prev and next segment information to restore if necessary
		boolean keepStart =  pointX[startIdx] == x1 || pointY[startIdx] == v;
		double nextX = pointX[endIdx+1];
		double nextY = pointY[endIdx];
		boolean keepEnd = (pointX[endIdx+1] == x2) || pointY[endIdx] == v;
		
		// determine number of entries affected and needed
		int entriesAffected = endIdx - startIdx + 1;
		int entriesNeeded = 3;
		if (keepStart) entriesNeeded--;
		if (keepEnd) entriesNeeded--;
		
		// check if correct number of entries need to be inserted
		if (entriesNeeded > entriesAffected) {
			int entriesToInsert = entriesNeeded - entriesAffected;
			ensureCapacity(segmentCount + entriesToInsert + 1);
			
			// copy entries
			int srcOffset = startIdx + 1;
			if (srcOffset < segmentCount) {
				int dstOffset = srcOffset + entriesToInsert;
				int numToMove = segmentCount - srcOffset + 1;
				System.arraycopy(pointX, srcOffset, pointX, dstOffset, numToMove);
				System.arraycopy(pointY, srcOffset, pointY, dstOffset, numToMove);
			}
			segmentCount += entriesToInsert;
		}
		
		else if (entriesNeeded < entriesAffected) {
			int entriesToDelete = entriesAffected - entriesNeeded;
			
			// copy entries
			int dstOffset = startIdx + 1;
			int srcOffset = dstOffset + entriesToDelete;
			int numToMove = segmentCount - srcOffset + 1;
			System.arraycopy(pointX, srcOffset, pointX, dstOffset, numToMove);
			System.arraycopy(pointY, srcOffset, pointY, dstOffset, numToMove);
			segmentCount -= entriesToDelete;
		}
		
		// record updated data
		int idx = startIdx;
		if (pointX[idx] != x1) {
			idx++;
		}
		
		pointX[idx] = x1;
		pointX[idx+1] = x2;
		pointY[idx] = v;
		
		if (nextX != x2) {
			if (!keepEnd) {
				idx++;
				pointX[idx] = x2;
				pointX[idx+1] = nextX;
			}
			
			pointY[idx] = nextY;
		}
	}
	
	/**
	 * <p>
	 * Resets function to a piecewise set of steps defined by X and Y within the defined interval
	 * [minX, maxX). If array X is length n, array Y must be length n+1.
	 * </p>
	 * 
	 * Creates steps as follows:<br>
	 * v[0] for [minX, x[0])<br>
     * v[i] for [x[i], x[i+1]) for all i in[0, n-2]
	 * v[n] for [x[n-1], maxX)
	 * 
	 * @param x		Array of points within [minX, maxX)
	 * @param y		Value of Y at each point X given
	 */
	public void setSteps(double x[], double v[]) {
		// ensure arrays are proper length
		if (x.length+1 != v.length)
			throw new IllegalArgumentException("Number of points in array V must be one more than values in array X");
		
		// make sure all points fall within proper range
		if (x[0] <= minX)
			throw new IllegalArgumentException("All X points must be greater than defined minimum for function");
		
		if (x[x.length-1] >= maxX)
			throw new IllegalArgumentException("All X points must be less than defined maximum for function");
		
		// verify points are increasing
		for (int i=0; i<x.length-1; i++) {
			double x1 = x[i];
			double x2 = x[i+1];
			if (x1 >= x2)
				throw new IllegalArgumentException("Points in array X must be strictly increasing");
		}
		
		// make sure minimum number of entries exist
		ensureCapacity(x.length + 2);
		
		// v[0] for [minX, x[0])
		setValue(minX, x[0], v[0]);
		
		// v[i] for [x[i], x[i+1])
		for (int i=0; i<x.length-1; i++) {
			double x1 = x[i];
			double x2 = x[i+1];
			double vi = v[i+1]; 
			setValue(x1, x2, vi);
		}
		
		// v[n] for [x[n-1], maxX)
		setValue(x[x.length-1], maxX, v[v.length-1]);
	}
	
	/**
	 * Sets the function to be the maximum between the current value and the value of 
	 * another defined <code>PiecewiseStepFunction</code>.  The min and max X for
	 * both functions must be the same. 
	 */
	public void setMax(PiecewiseStepFunction f) {
		if (f.minX != minX || f.maxX != maxX)
			throw new IllegalArgumentException("Functions must have same min and max X intervals");
		
		for (int i=0; i<f.segmentCount; i++)
			setMax(f.pointX[i], f.pointX[i+1], f.pointY[i]);
	}
	
	/**
	 * Sets the function to be equal to that maximum between the current value and the value
	 * v everywhere on the interval [x1, x2)
	 */
	public void setMax(double x1, double x2, double v) {
		// make sure interval falls within range of this function
		if (x1<x2 && x2>minX && x1<maxX) {
			// find segments that contain range
			int startIdx = indexOfSegment(x1);
			int endIdx = indexOfSegmentEnd(startIdx, x2);
			
			// work from right to left updating segments
			for (int i=endIdx; i>=startIdx; i--) {
				// use maximal value if it has larger Y
				if (v > pointY[i]) {
					
					// determine last X to alter if affected interval is beyond end of segment or interval
					// ends within segment
					double Xe = Math.min(x2, pointX[i+1]);
					
					// determine earliest segment that can be combined
					while (i>startIdx && v > pointY[i-1])
						i--;
					
					// calculate point within segment to use as start
					// if segment beings before start of new max interval
					double Xs = Math.max(pointX[i], x1);
					
					setValue(Xs, Xe, v);
				}
			}
		}
	}
	
	/**
	 * Sets the function to be the minimum between the current value and the value of 
	 * another defined <code>PiecewiseStepFunction</code>.  The min and max X for
	 * both functions must be the same. 
	 */
	public void setMin(PiecewiseStepFunction f) {
		if (f.minX != minX || f.maxX != maxX)
			throw new IllegalArgumentException("Functions must have same min and max X intervals");
		
		for (int i=0; i<f.segmentCount; i++)
			setMin(f.pointX[i], f.pointX[i+1], f.pointY[i]);
	}
	
	/**
	 * Sets the function to be equal to that minimum between the current value and the value
	 * v everywhere on the interval [x1, x2)
	 */
	public void setMin(double x1, double x2, double v) {
		// make sure interval falls within range of this function
		if (x1<x2 && x2>minX && x1<maxX) {
			// find segments that contain range
			int startIdx = indexOfSegment(x1);
			int endIdx = indexOfSegmentEnd(startIdx, x2);
			
			// work from right to left updating segments
			for (int i=endIdx; i>=startIdx; i--) {
				// use minimal value if it has smaller Y
				if (v < pointY[i]) {
					
					
					// determine last X to alter if affected interval is beyond end of segment or interval
					// ends within segment
					double Xe = Math.min(x2, pointX[i+1]);
					
					// determine earliest segment that can be combined
					while (i>startIdx && v < pointY[i-1])
						i--;
					
					// calculate point within segment to use as start
					// if segment beings before start of new min interval
					double Xs = Math.max(pointX[i], x1);
					
					setValue(Xs, Xe, v);
				}
			}
		}
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		for (int i=0; i<segmentCount; i++) {
			buf.append("x in [");
			buf.append(pointX[i]);
			buf.append(", ");
			buf.append(pointX[i+1]);
			buf.append(") -> f(x) = ");
			buf.append(pointY[i]);
			buf.append("\n");
		}
		return buf.toString();
	}
}
