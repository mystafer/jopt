/*
 * Presolver.java
 * 
 * Created on Feb 24, 2006
 */
package jopt.mp.spi;

public class Presolver {
	private MatrixA a;
	private double feasibilityPrecision;
	
	/**
	 * Creates a new presolver
	 * 
	 * @param a						Matrix presolver operates upon
	 * @param feasibilityPrecision	amount by which a variable may violate its bounds and still be considered feasible
	 */
	public Presolver(MatrixA a, double feasibilityPrecision) {
		this.a = a;
		this.feasibilityPrecision = feasibilityPrecision;
	}
	
	/**
	 * Returns amount by which a variable may violate its bounds and still be considered feasible
	 */
	public double getFeasibilityPrecision() {
		return feasibilityPrecision;
	}
	
	/**
	 * Sets amount by which a variable may violate its bounds and still be considered feasible
	 */
	public void setFeasibilityPrecision(double feasibilityPrecision) {
		this.feasibilityPrecision = feasibilityPrecision;
	}
	
	public void checkBoundsAndRowRedundancy(double reducedLower[], double reducedUpper[], boolean rowRedundant[])
		throws MatrixInvalidException, InfeasibleException, UnboundedException
	{
		// Review chapter 7 - LP Preprocessing
		// 7.1 - Presolve
		// 7.1.1 - Contradicting individual bounds (handled as part of checks while building Matrix A)
		// 7.1.2 - Empty rows
		// 7.1.3 - Empty columns 
		// 7.1.4 - Singleton rows
		// 7.1.5 - ?
		// 7.1.6 - Redundant and forcing constraints
		// 7.1.7 - Tightening individual bounds
		// 7.1.8 - ?
		// 7.1.9 - ?
		// 7.1.10 - ?
		// 7.1.11 - ?
		
		// validate input lengths of arrays
		if (reducedLower != null && reducedLower.length != a.getColCount())
			throw new IllegalArgumentException("reduced lower bound array must be same length as column count in matrix A");
		if (reducedUpper != null && reducedUpper.length != a.getColCount())
			throw new IllegalArgumentException("reduced upper bound array must be same length as column count in matrix A");
		if (rowRedundant != null && rowRedundant.length != a.getRowCount())
			throw new IllegalArgumentException("row status array must be same length as row count in matrix A");
		
		// make sure matrix has rows and columns
		if (a.getRowCount() == 0)
			throw new MatrixInvalidException("Matrix A has no rows");
		if (a.getColCount() == 0)
			throw new MatrixInvalidException("Matrix A has no columns");

		// retrieve bounds for all the columns
		for (int i=0; i<a.getColCount(); i++) {
			
			// 7.1.4 - Empty columns
			// check for empty columns
			int colNzeros = a.getColNzeroCount(i);
			if (colNzeros == 0) {
				// if objective is zero, fix bounds a lowest value
				double obj = a.getObjective(i);
				if (DoubleUtil.isEqual(obj, 0, feasibilityPrecision)) {
					double val = a.getLowerBound(i);
					reducedLower[i] = val;
					reducedUpper[i] = val;
				}
				
				// if objective is positive, fix at lower bound
				else if (obj>0) {
					double val = a.getLowerBound(i);
					reducedLower[i] = val;
					reducedUpper[i] = val;
					
					// check for unboundedness
					if (val == -DoubleUtil.INFINITY)
						throw new UnboundedException("column " + i + " is unbounded");
				}
				
				// if objective is negative, fix a upper bound
				else {
					double val = a.getUpperBound(i);
					reducedLower[i] = val;
					reducedUpper[i] = val;
					
					// check for unboundedness
					if (val == DoubleUtil.INFINITY)
						throw new UnboundedException("column " + i + " is unbounded");
				}
			}
			
			// simply copy current upper and lower for column
			else {
				reducedLower[i] = a.getLowerBound(i);
				reducedUpper[i] = a.getUpperBound(i);
			}
		}
		
		// check for empty and singleton rows
		int nzeroStart[] = new int[]{0};
		int nzeroCols[] = new int[a.getColCount()];
		double nzeroVals[] = new double[a.getColCount()];
		int totalSize[] = new int[a.getColCount()];
		for (int i=0; i<a.getRowCount(); i++) {
			int rowNzeros = a.getRowNzeroCount(i);
			
			// 7.1.2 - Empty rows
			// make sure non-zeros exist for the row
			if (rowNzeros == 0)
				throw new MatrixInvalidException("row " + i + " has no non-zero values");
			
			// 7.1.4 - Singleton rows
			// if one non-zero, row is a singleton
			if (rowNzeros == 1) {
				int col = nzeroCols[0];
				double lower = reducedLower[col];
				double upper = reducedUpper[col];
				
				// retrieve the first value in the row and calculate the value
				// that will be used as a new bound for the column
				a.getRowNzeros(i, i, nzeroStart, nzeroCols, nzeroVals, totalSize);
				double rhs = a.getRHS(i);
				double a_ij = nzeroVals[0];
				double newBound = rhs / nzeroVals[0];
				
				// check if a_ij is less than or equal to RHS
				double newLower = lower;
				double newUpper = upper;
				int cmp = DoubleUtil.compare(a_ij, rhs, feasibilityPrecision);
				if (cmp <= 0) {
					if (a_ij > 0) newUpper = newBound;
					else newLower = newBound;
				}
				
				// check if a_ij is greater than or equal to RHS
				if (cmp >= 0) {
					if (a_ij > 0) newLower = newBound;
					else newUpper = newBound;
				}
				
				// check if bound is feasible
				if (DoubleUtil.compare(newLower, lower, feasibilityPrecision) < 0 || DoubleUtil.compare(newUpper, upper, feasibilityPrecision) > 0)
					throw new InfeasibleException("column " + col + " has infeasible bounds");
				
				// update bounds of column
				reducedLower[col] = newLower;
				reducedUpper[col] = newUpper;
				
				// mark row as being redundant
				rowRedundant[i] = true;
			}
			
			// 7.1.6 - Redundant and forcing constraints
			// check for redundant and forcing constraints
			double rhs = a.getRHS(i);
			
			// calculate smallest and largest values of b
			double smallestB = 0;
			double largestB = 0;
			for (int j=0; j<rowNzeros; j++) {
				int col = nzeroCols[j];
				double a_ij = nzeroVals[j];
				
				if (a_ij > 0) {
					smallestB += a_ij * reducedLower[col];
					largestB += a_ij * reducedUpper[col];
				}
				else {
					smallestB += a_ij * reducedUpper[col];
					largestB += a_ij * reducedLower[col];
				}
			}
			
			// check for infinity overflow
			if (smallestB>DoubleUtil.INFINITY) smallestB = DoubleUtil.INFINITY;
			else if (smallestB<-DoubleUtil.INFINITY) smallestB = -DoubleUtil.INFINITY;
			if (largestB>DoubleUtil.INFINITY) largestB = DoubleUtil.INFINITY;
			else if (largestB<-DoubleUtil.INFINITY) largestB = -DoubleUtil.INFINITY;
			
			// flip rhs and b values if >= constraint to <= equivalent so all checks are consistent
			char rowType = a.getRowType(i);
			if (rowType == MatrixA.ROW_TYPE_GEQ) {
				rhs = -rhs;
				double tmp = smallestB;
				smallestB = -largestB;
				largestB = -tmp;
			}
			
			// compare rhs with smallest possible value
			int cmp = DoubleUtil.compare(rhs, smallestB, feasibilityPrecision);

			// check for rhs < lowest possible value
			if (cmp < 0)
				throw new InfeasibleException("row " + i + " is infeasible");
			
			// check for rhs equal to smallest
			if (cmp == 0) {
				// this constraint will force each variable to either a lower or upper bound
				for (int j = 0; j<rowNzeros; j++) {
					int col = nzeroCols[j];
					double a_ij = nzeroVals[j];
					
					// positive values are forced to lower bounds
					if (a_ij > 0)
						reducedUpper[col] = reducedLower[col];
					
					// negative constraints are forced to upper bounds
					else
						reducedLower[col] = reducedUpper[col];
				}
				
				// row is marked as redundant and can be removed
				rowRedundant[i] = true;
			}
			
			// if rhs >= largest, row is redundant
			else if (largestB > smallestB && DoubleUtil.compare(rhs, largestB, feasibilityPrecision) >= 0) {
				rowRedundant[i] = true;
			}
			
			// 7.1.7 - Tightening individual bounds
			// in order to tighten bounds on individual columns we need to make sure that
			// the bounds are not infinite on the columns that make up the row.  In the
			// case of one infinite column, a special situation can be dealt with as well
			// to reduce bounds on that single variable
			int infColCount = 0;
			int infCol = -1;
			double infColVal = 0d;
			for (int j=0; j<rowNzeros; j++) {
				int col = nzeroCols[j];
				
				// check if variable is at an infinite bound
				boolean infLower = reducedLower[col] == -DoubleUtil.INFINITY;
				boolean infUpper = reducedUpper[col] == DoubleUtil.INFINITY;
				if (infLower || infUpper) {
					// increment counter of number of variables with infinite bounds
					infColCount++;
					
					// no need to continue if more than 1 infinite variable determined
					if (infColCount > 1) break;
					
					// if lower bound = -INF and non-zero is positive, or
					// if upper bound = INF and non-zero is negative
					// a new bound can be calculated for the column
					double a_ij = nzeroVals[j];
					if ((infLower && a_ij > 0) || (infUpper && a_ij < 0)) {
						// capture inf column number and value for use in next section
						infCol = col;
						infColVal = a_ij;
					}
				}
			}
			
			// rows with having 0 infintie variables can check each column for possibility
			// of tighter bounds
			if (infColCount == 0 && smallestB > -DoubleUtil.INFINITY && smallestB < DoubleUtil.INFINITY) {
				double rhsDelta = rhs - smallestB;
				
				// attempt to reduced bounds on all variables in current row
				for (int j=0; j<rowNzeros; j++) {
					int col = nzeroCols[j];
					
					// retrieve current non-zero value
					double a_ij = nzeroVals[j];
					
					// calculate a new upper bound for positive variables
					if (a_ij > 0) {
						double lower = reducedLower[col];
						double upper = reducedUpper[col];
						double newBound = lower + (rhsDelta / a_ij);
						
						// check if new bound is better than current upper
						// before replacing it
						if (newBound < upper) {
							// check if new bound is feasible
							if (newBound < lower)
								throw new InfeasibleException("column " + infCol + " has infeasible bounds");
							
							reducedUpper[infCol] = newBound;
						}
					}
					
					// calculate a new lower bound for negative variables
					else {
						double lower = reducedLower[col];
						double upper = reducedUpper[col];
						double newBound = upper + (rhsDelta / a_ij);
						
						// check if new bound is better than current lower
						// before replacing it
						if (newBound > lower) {
							// check if new bound is feasible
							if (newBound > upper)
								throw new InfeasibleException("column " + infCol + " has infeasible bounds");
							
							reducedLower[infCol] = newBound;
						}
					}
				}
			}
			
			// handle situation with 1 infinite column with either
			// a_ij > 0 and l_j = -INF 
			// or a_ij < 0 and u_j = INF
			else if (infColCount == 1 && infCol >= 0) {
				// calculate new bounds for inf column
				double newBound = rhs;
				for (int j = 0; j<rowNzeros; j++) {
					int col = nzeroCols[j];
					
					if (j!=infCol) {
						double a_ij = nzeroVals[j];
						if (a_ij > 0) newBound -= a_ij * reducedLower[col];
						else newBound -= a_ij * reducedUpper[col];
					}
				}
				newBound /= infColVal;
				
				// check for infinity overflow
				if (newBound > -DoubleUtil.INFINITY && newBound < DoubleUtil.INFINITY) {
					// determine new upper bound if non-zero is positive
					if (infColVal > 0) {
						// check that new bound is feasible
						double lower = reducedLower[infCol];
						if (newBound < lower)
							throw new InfeasibleException("column " + infCol + " has infeasible bounds");
						
						// replace bound on column
						reducedUpper[infCol] = newBound;
					}
					
					// determine new lower bound sinze non-zero is negative
					else {
						// check that new bound is feasible
						double upper = reducedUpper[infCol];
						if (newBound > upper)
							throw new InfeasibleException("column " + infCol + " has infeasible bounds");
						
						// replace bound on column
						reducedLower[infCol] = newBound;
					}
				}
			}
		}
	}
}
