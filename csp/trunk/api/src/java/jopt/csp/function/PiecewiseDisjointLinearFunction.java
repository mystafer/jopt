package jopt.csp.function;


/**
 * Defines a piecewise linear function that may be used with some constraints.
 * 
 * @author Nick Coleman
 * @version $Revision: 1.2 $
 */
public class PiecewiseDisjointLinearFunction implements PiecewiseFunction {
    private static final int INIT_SIZE = 8;

	private int segmentCount;
	private double minX;
	private double maxX;
    private double[] pointX;
    private double[] pointY;
    private double[] pointSlope;
	
	public PiecewiseDisjointLinearFunction() {
		this(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0);
	}
	public PiecewiseDisjointLinearFunction(double xmin, double xmax, double val) {
		this.pointX = new double[INIT_SIZE];
		this.pointY = new double[INIT_SIZE];
		this.pointSlope = new double[INIT_SIZE];
		
		this.minX = xmin;
		this.maxX = xmax;
		
		this.segmentCount = 1;
		this.pointX[0] = minX;
		this.pointX[1] = maxX;
		this.pointY[0] = val;
		this.pointSlope[0] = 0;
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
            
            // expand pointSlope array
            olddata = pointSlope;
            pointSlope = new double[newcap];
            System.arraycopy(olddata, 0, pointSlope, 0, oldcap);
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
	
	/**
	 * Calculates the value of an intermediate Y
	 * for x in [x1, x2), y = y1 + slope * x
	 */
	private double calculateY(double y1, double slope, double x) {
		if (slope != 0)
			return y1 + slope * x;
		else
			return y1;
	}
	
	/* (non-Javadoc)
	 * @see jopt.csp.function.PiecewiseFunction#getY(double)
	 */
	public double getY(double x) {
		int idx = indexOfSegment(x);
		return calculateY(pointY[idx], pointSlope[idx], x);
	}
	
	/* (non-Javadoc)
	 * @see jopt.csp.function.PiecewiseFunction#getMinY(double, double)
	 */
	public double getMinY(double x1, double x2) {
		if (x1 > x2)
			throw new IllegalArgumentException("X values are out of order");
		if (x2 >= maxX)
			throw new IndexOutOfBoundsException("X(" + x2 + ")is not valid for function");
		if (x1 == x2)
			return getY(x1);
			
		// loop over all affected segments
		int idx = indexOfSegment(x1);
		double min = Double.POSITIVE_INFINITY;
		do {
			double yMin = pointY[idx];
			
			// check if slope is specified
			double slope = pointSlope[idx];
			if (slope != 0) {
				double xs = pointX[idx];
				
				// if slope is negative, find smallest Y at largest valid X
				if (slope < 0) {
					double xe = pointX[idx+1];
					double lastX = Math.min(x2, xe);
					yMin = calculateY(yMin, slope, lastX);
				}
				
				// check if segment begins after start of specified range
				// use earliest possible x in range that is valid
				else if (x1 > xs) {
					yMin = calculateY(yMin, slope, x1);
				}
			}
			
			// check if new minimum is found
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
		if (x1 == x2)
			return getY(x1);
			
		// loop over all affected segments
		int idx = indexOfSegment(x1);
		double max = Double.NEGATIVE_INFINITY;
		do {
			double yMax = pointY[idx];
			
			// check if slope is specified
			double slope = pointSlope[idx];
			if (slope != 0) {
				double xs = pointX[idx];
				
				// if slope is positive, find largest Y at end of segment
				if (slope > 0) {
					double xe = pointX[idx+1];
					double lastX = Math.min(x2, xe);
					yMax = calculateY(yMax, slope, lastX);
				}
				
				// check if segment begins after start of specified range
				// use earliest possible x in range that is valid
				else if (x1 > xs) {
					yMax = calculateY(yMax, slope, x1);
				}
			}
			
			// check if new maximum is found
			if (yMax > max)
				max = yMax;
			
			idx++;
		} while (idx < segmentCount && idx <= indexOfSegmentEnd(idx, x2));
		
		return max;
	}
	
	/**
	 * Sets Y to a constant over an interval of the function:
	 * 
	 * for x in [x1, x2), y = b
	 */
	public void setY(double x1, double x2, double b) {
		setSlope(x1, x2, b, 0);
	}
	
	/**
	 * Sets the value to a slope over an interval of the function:
	 * 
	 * for x in [x1, x2), y = y1 + slope * x
	 */
	public void setSlope(double x1, double x2, double y1, double slope) {
		if (x1 > x2)
			throw new IllegalArgumentException("X values are out of order");
		if (x1 == x2)
			throw new IllegalArgumentException("Segments in function cannot be defined as single point");
		
		// find segments that contain range
		int startIdx = indexOfSegment(x1);
		int endIdx = indexOfSegmentEnd(startIdx, x2);
		
		// check if new segment should combine with previous or next segment
		if (startIdx > 0 && pointSlope[startIdx-1] == slope) {
			double prevX = pointX[startIdx-1];
			double prevY = pointY[startIdx-1];
			if (calculateY(prevY, slope, x1) == y1) {
				x1 = prevX;
				y1 = prevY;
				startIdx--;
			}
		}
		if (endIdx < segmentCount-1 && pointSlope[endIdx+1] == slope) {
			double nextX = pointX[endIdx+1];
			double nextY = pointY[endIdx+1];
			if (calculateY(x2, slope, nextX) == nextY) {
				x2 = nextX;
				endIdx++;
			}
		}
		
		// store prev and next segment information to restore if necessary
		boolean keepStart = (pointX[startIdx] == x1) || (pointY[startIdx] == y1 && pointSlope[startIdx] == slope);
		double nextX = pointX[endIdx+1];
		double nextY = pointY[endIdx];
		double nextSlope = pointSlope[endIdx];
		boolean keepEnd = (pointX[endIdx+1] == x2) || (pointY[endIdx] == y1 && pointSlope[endIdx] == slope);
		
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
				System.arraycopy(pointSlope, srcOffset, pointSlope, dstOffset, numToMove);
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
			System.arraycopy(pointSlope, srcOffset, pointSlope, dstOffset, numToMove);
			segmentCount -= entriesToDelete;
		}
		
		// record updated data
		int idx = startIdx;
		if (pointX[idx] != x1) {
			idx++;
		}
		
		pointX[idx] = x1;
		pointX[idx+1] = x2;
		pointY[idx] = y1;
		pointSlope[idx] = slope;
		
		if (nextX != x2) {
			if (!keepEnd) {
				idx++;
				pointX[idx] = x2;
				pointX[idx+1] = nextX;
			}
			
			pointY[idx] = nextY;
			pointSlope[idx] = nextSlope;
		}
	}
	
	/**
	 * Sets the function to be the maximum between the current value and the value of 
	 * another defined <code>PiecewiseLinearFunction</code>.  The min and max X for
	 * both functions must be the same. 
	 */
	public void setMax(PiecewiseDisjointLinearFunction f) {
		if (f.minX != minX || f.maxX != maxX)
			throw new IllegalArgumentException("Functions must have same min and max X intervals");
		
		for (int i=0; i<f.segmentCount; i++)
			setMaxSlope(f.pointX[i], f.pointY[i], f.pointX[i+1], f.pointSlope[i]);
	}
	
	/**
	 * Sets the function to be equal to that maximum between the current value and the value
	 * y everywhere on the interval [x1, x2)
	 */
	public void setMax(double x1, double x2, double y) {
		// make sure interval falls within range of this function
		if (x1<x2 && x2>minX && x1<maxX) {
			setMaxSlope(x1, y, 0, x2);
		}
	}
	
	/**
	 * Sets the function to be equal to that maximum between the current value and the value
	 * of the line between the points (x1,y1) and (x2,y2) on the interval [x1, x2)
	 */
	public void setMax(double x1, double y1, double x2, double y2) {
		// make sure interval falls within range of this function
		if (x1<x2 && x2>minX && x1<maxX) {
			// calculate slope for maximal line
			double slope = (y2 - y1) / (x2 - x1);
			setMaxSlope(x1, y1, slope, x2);
		}
	}
	
	/**
	 * Sets the function to be equal to that maximum between the current value and the value
	 * of the line starting at (x1,y1) with given slope and ending at x2
	 * on the interval [x1, x2)
	 */
	private void setMaxSlope(double x1, double y1, double slope, double x2) {
		// find segments that contain range
		int startIdx = indexOfSegment(x1);
		int endIdx = indexOfSegmentEnd(startIdx, x2);
		
		// work from right to left updating segments
		for (int i=endIdx; i>=startIdx; i--)
			setLinearMax(i, x1, y1, slope, x2);
	}
	
	/**
	 * Alters a segment if necessary to ensure that no value exceeds a maximum defined line
	 */
	private void setLinearMax(int idx, double Xn, double Yn, double Mn, double Xe) {
		// find start point for segment index
		double Xs = pointX[idx];
		double Ys = pointY[idx];
		double Ms = pointSlope[idx];
		
		// calculate point within segment to use as start
		// if segment beings before start of new max interval
		double Xp = Math.max(Xs, Xn);
		// Yp = calculateY(Xs, Ys, Ms, Xn);
		
		// determine last X to alter if affected interval is beyond end of segment or interval
		// ends within segment
		Xe = Math.min(Xe, pointX[idx+1]);
		
		// determine value of Y for maximal and segment lines when they cross origin X=0
		//
		// since x in [x1, x2), y = y1 + slope * (x-x1)
		// y = y1 + slope*x - slope*x1
		//   = slope*x + (y1 - slope*x1)
		//
		// if x = 0 (ie the origin) then
		// y = 0 * (y1 - slope*x1)
		//   = y1 - slope*x1;
		double Bn = Yn - Mn * Xn;
		double Bs = Ys - Ms * Xs;
		
		// check if lines are parallel
		if (Ms == Mn) {
			// use maximal line if it has larger Y at origin when parallel
			// to current segment line
			if (Bn > Bs) {
				// need to determine value of Y for maximal line when at start point
				double Yi = calculateY(Yn, Mn, Xp);
				setSlope(Xp, Xe, Yi, Mn);
			}
		}
		
		// lines intersect
		else {
			// determine X of intersection
			// Yn = Mn*X + Bn
			// Ys = Ms*X + Bs
			//
			// Ys = Yc  -> Mn*X + Bn = Ms*X + Bs
			// 			-> Mn*X - Ms*X = Bs - Bn
			// 			-> (Mn - Ms)*X = Bs - Bn
			// 			-> X = (Bs - Bn) / (Mn - Ms)
			double Xi = (Bs - Bn) / (Mn - Ms);
			
			// if lines intersect before start point, entire range is affected
			if (Xi < Xp) {
				
				// if new slope is greater, new line will be higher than current line
				// in the range [Xp, Xe) so use the new slope
				if (Mn > Ms) {
					// need to determine value of Y for maximal line when at start point
					double Yi = calculateY(Yn, Mn, Xp);
					setSlope(Xp, Xe, Yi, Mn);
				}
			}
			
			// if lines intersect after end point, entire range is affected
			else if (Xi >= Xe) {
				
				// if new slope is less, new line will be higher than current line
				// in the range [Xp, Xe) so use the new slope
				if (Mn < Ms) {
					// need to determine value of Y for maximal line when at start point
					double Yi = calculateY(Yn, Mn, Xp);
					setSlope(Xp, Xe, Yi, Mn);
				}
			}
			
			// lines intersect within the range [Xp, Xe)
			else {
				// if new slope is greater, new line will be lower before
				// intesection and higher after so use new slope after intersection
				if (Mn > Ms) {
					// need to determine value of Y for maximal line when at intersection
					double Yi = calculateY(Yn, Mn, Xi);
					setSlope(Xi, Xe, Yi, Mn);
				}
				
				// if new slope is less, new line will be lower after
				// intesection and higher before so use new slope before intersection
				else {
					// need to determine value of Y for maximal line when at start point
					double Yi = calculateY(Yn, Mn, Xp);
					setSlope(Xp, Xi, Yi, Mn);
				}
			}
		}
	}
	
	/**
	 * Sets the function to be the minimum between the current value and the value of 
	 * another defined <code>PiecewiseLinearFunction</code>.  The min and max X for
	 * both functions must be the same. 
	 */
	public void setMin(PiecewiseDisjointLinearFunction f) {
		if (f.minX != minX || f.maxX != maxX)
			throw new IllegalArgumentException("Functions must have same min and max X intervals");
		
		for (int i=0; i<f.segmentCount; i++)
			setMinSlope(f.pointX[i], f.pointY[i], f.pointX[i+1], f.pointSlope[i]);
	}
	
	/**
	 * Sets the function to be equal to that minimum between the current value and the value
	 * Y everywhere on the interval [x1, x2)
	 */
	public void setMin(double x1, double x2, double y) {
		// make sure interval falls within range of this function
		if (x1<x2 && x2>minX && x1<maxX) {
			setMinSlope(x1, y, 0, x2);
		}
	}
	
	/**
	 * Sets the function to be equal to that minimum between the current value and the value
	 * of the line between the points (x1,y1) and (x2,y2) on the interval [x1, x2)
	 */
	public void setMin(double x1, double y1, double x2, double y2) {
		// make sure interval falls within range of this function
		if (x1<x2 && x2>minX && x1<maxX) {
			// calculate slope for minimal line
			double slope = (y2 - y1) / (x2 - x1);
			setMinSlope(x1, y1, slope, x2);
		}
	}
	
	/**
	 * Sets the function to be equal to that minimum between the current value and the value
	 * of the line starting at (x1,y1) with given slope and ending at x2
	 * on the interval [x1, x2)
	 */
	private void setMinSlope(double x1, double y1, double slope, double x2) {
		// find segments that contain range
		int startIdx = indexOfSegment(x1);
		int endIdx = indexOfSegmentEnd(startIdx, x2);
		
		// work from right to left updating segments
		for (int i=endIdx; i>=startIdx; i--)
			setLinearMin(i, x1, y1, slope, x2);
	}
	
	/**
	 * Alters a segment if necessary to ensure that no value exceeds a minimum defined line
	 */
	private void setLinearMin(int idx, double Xn, double Yn, double Mn, double Xe) {
		// find start point for segment index
		double Xs = pointX[idx];
		double Ys = pointY[idx];
		double Ms = pointSlope[idx];
		
		// calculate point within segment to use as start
		// if segment beings before start of new max interval
		double Xp = Math.max(Xs, Xn);
		// Yp = calculateY(Xs, Ys, Ms, Xn);
		
		// determine last X to alter if affected interval is beyond end of segment or interval
		// ends within segment
		Xe = Math.min(Xe, pointX[idx+1]);
		
		// determine value of Y for minimal and segment lines when they cross origin X=0
		//
		// since x in [x1, x2), y = y1 + slope * (x-x1)
		// y = y1 + slope*x - slope*x1
		//   = slope*x + (y1 - slope*x1)
		//
		// if x = 0 (ie the origin) then
		// y = 0 * (y1 - slope*x1)
		//   = y1 - slope*x1;
		double Bn = Yn - Mn * Xn;
		double Bs = Ys - Ms * Xs;
		
		// check if lines are parallel
		if (Ms == Mn) {
			// use minimal line if it has smaller Y at origin when parallel
			// to current segment line
			if (Bn < Bs) {
				// need to determine value of Y for minimal line when at start point
				double Yi = calculateY(Yn, Mn, Xp);
				setSlope(Xp, Xe, Yi, Mn);
			}
		}
		
		// lines intersect
		else {
			// determine X of intersection
			// Yn = Mn*X + Bn
			// Ys = Ms*X + Bs
			//
			// Ys = Yc  -> Mn*X + Bn = Ms*X + Bs
			// 			-> Mn*X - Ms*X = Bs - Bn
			// 			-> (Mn - Ms)*X = Bs - Bn
			// 			-> X = (Bs - Bn) / (Mn - Ms)
			double Xi = (Bs - Bn) / (Mn - Ms);
			
			// if lines intersect before start point, entire range is affected
			if (Xi < Xp) {
				
				// if new slope is less, new line will be lower than current line
				// in the range [Xp, Xe) so use the new slope
				if (Mn < Ms) {
					// need to determine value of Y for minimal line when at start point
					double Yi = calculateY(Yn, Mn, Xp);
					setSlope(Xp, Xe, Yi, Mn);
				}
			}
			
			// if lines intersect after end point, entire range is affected
			else if (Xi >= Xe) {
				
				// if new slope is greater, new line will be lower than current line
				// in the range [Xp, Xe) so use the new slope
				if (Mn > Ms) {
					// need to determine value of Y for minimal line when at start point
					double Yi = calculateY(Yn, Mn, Xp);
					setSlope(Xp, Xe, Yi, Mn);
				}
			}
			
			// lines intersect within the range [Xp, Xe)
			else {
				// if new slope is less, new line will be higher before
				// intesection and lower after so use new slope after intersection
				if (Mn < Ms) {
					// need to determine value of Y for minimal line when at intersection
					double Yi = calculateY(Yn, Mn, Xi);
					setSlope(Xi, Xe, Yi, Mn);
				}
				
				// if new slope is greater, new line will be higher after
				// intesection and lower before so use new slope before intersection
				else {
					// need to determine value of Y for minimal line when at start point
					double Yi = calculateY(Yn, Mn, Xp);
					setSlope(Xp, Xi, Yi, Mn);
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
			buf.append(pointSlope[i]);
			buf.append(" * x + ");
			buf.append(pointY[i]);
			buf.append("\n");
		}
		return buf.toString();
	}
}
