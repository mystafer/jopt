package jopt.mp.spi;

import java.util.TreeMap;

public class MatrixA {
//	private final static int DEFAULT_COLUMN_GROWTH 	= 100;
//	private final static int DEFAULT_ROW_GROWTH 	= 100;
//	private final static int DEFAULT_NZERO_GROWTH 	= 500;
	
	public final static char COLUMN_TYPE_CONTINUOUS 		= 'C';
	public final static char COLUMN_TYPE_BINARY  			= 'B';
	public final static char COLUMN_TYPE_INTEGER 			= 'I';
	public final static char COLUMN_TYPE_SEMI_CONTINUOUS	= 'S';
	public final static char COLUMN_TYPE_SEMI_INTEGER 		= 'N';
	
	public final static char ROW_TYPE_EQ	= 'E';
	public final static char ROW_TYPE_LEQ	= 'L';
	public final static char ROW_TYPE_GEQ	= 'G';
	public final static char ROW_TYPE_RANGE	= 'R';
	
	// values that control the rate of growth of the column, row and nzero arrays
	private int columnGrowth;
	private int rowGrowth;
	private int nzeroGrowth;
	
	// values that hold the number of rows and columns that represent the size
	// of the matrix
	private int colCount;
	private int rowCount;
	private int nzeroCount;
	
	// arrays that hold pointers to first values for a given column and row
	private int colHead[];
	private int rowHead[];
	
	// arrays holding actual values for a column and index of row the value
	// belongs to within the column
	private double nzeros[];
	private int nzeroColIdx[];
	private int nzeroRowIdx[];
	
	// arrays for holding next value in a column and in a row
	private int nextColVal[];
	private int prevColVal[];
	private int nextRowVal[];
	private int prevRowVal[];
	
	// pointer to first column, row and non-zero that has been deleted from matrix
	private int delColHead;
	private int delRowHead;
	private int delNzeroHead;
	
	// arrays holding bounds, types, names, etc for a column
	private int colId[];
	private int colExtId[];
	private String colName[];
	private TreeMap colNameMap;
	private double colLowerBound[];
	private double colUpperBound[];
	private double colObjective[];
	private char colType[];
	private int colNzeroCount[];
	
	// arrays holding rhs, types, etc for a row
	private int rowId[];
	private int rowExtId[];
	private String rowName[];
	private TreeMap rowNameMap;
	private double rowRhs[];
	private double rowSlack[];
	private char rowType[];
	private int rowNzeroCount[];
	
	/**
	 * Creates a new sparse matrix and initializes the values that control memory growth
	 * 
	 * @param columnGrowth	Number of columns that should be added when current number has been exceeded
	 * @param rowGrowth		Number of rows that should be added when current number has been exceeded
	 * @param nzeroGrowth	Number of non-zeros that should be added when current number has been exceeded
	 */
	public MatrixA(int columnGrowth, int rowGrowth, int nzeroGrowth) {
		// initialize growth values
		this.columnGrowth = columnGrowth;
		this.rowGrowth = rowGrowth;
		this.nzeroGrowth = nzeroGrowth;
		
		// initialize arrays for recording column information
		this.colHead = new int[columnGrowth];
		this.colId = new int[columnGrowth];
		this.colExtId = new int[columnGrowth];
		this.colName = new String[columnGrowth];
		this.colLowerBound = new double[columnGrowth];
		this.colUpperBound = new double[columnGrowth];
		this.colObjective = new double[columnGrowth];
		this.colType = new char[columnGrowth];
		this.colNzeroCount = new int[columnGrowth];
		this.colNameMap = new TreeMap();
		
		// initialize arrays for recording row information
		this.rowHead = new int[rowGrowth];
		this.rowId = new int[rowGrowth];
		this.rowExtId = new int[rowGrowth];
		this.rowName = new String[rowGrowth];
		this.rowRhs = new double[rowGrowth];
		this.rowSlack = new double[rowGrowth];
		this.rowType = new char[rowGrowth];
		this.rowNzeroCount = new int[rowGrowth];
		this.rowNameMap = new TreeMap();
		
		// initialize arrays for recording non-zeros
		this.nzeroColIdx = new int[nzeroGrowth];
		this.nzeroRowIdx = new int[nzeroGrowth];
		this.nzeros = new double[nzeroGrowth];
		this.nextColVal = new int[nzeroGrowth];
		this.prevColVal = new int[nzeroGrowth];
		this.nextRowVal = new int[nzeroGrowth];
		this.prevRowVal = new int[nzeroGrowth];
		
		// intialize rows and column counts to zero
		this.colCount = 0;
		this.rowCount = 0;
		this.nzeroCount = 0;
		
		// point deleted column header to invalid location to indicate
		// no values have been deleted yet
		this.delColHead = -1;
		this.delRowHead = -1;
		this.delNzeroHead = -1;
	}
	
	/**
	 * Returns the amount column of storage space matrix has allocated
	 */
	public int getColStorageSize() {
		return this.colHead.length;
	}
	
	/**
	 * Returns the amount row of storage space matrix has allocated
	 */
	public int getRowStorageSize() {
		return this.rowHead.length;
	}
	
	/**
	 * Returns the amount non-zero of storage space matrix has allocated
	 */
	public int getNonZeroStorageSize() {
		return this.nzeros.length;
	}
	
	/**
	 * Returns the total number of columns in the matrix
	 */
	public int getColCount() {
		return this.colCount;
	}
	
	/**
	 * Returns the total number of rows in the matrix
	 */
	public int getRowCount() {
		return this.rowCount;
	}
	
	/**
	 * Returns the total number of non-zeros in the matrix
	 */
	public int getNonZeroCount() {
		return this.nzeroCount;
	}
	
	/**
	 * Performs work to allocate memory to hold row entries if necessary
	 *  
	 * @param totalRows	Minimum number of rows that should be supported by matrix
	 */
	private void ensureRowCapacity(int totalRows) {
		//TODO: use disk access if necessary when expansion would cause memory issues
		
		// check if new size has exceeded available memory in nzero row sets
		int m = rowHead.length;
		if (totalRows > m) {
			// calculate new size based on row growth
			int newM = (totalRows / rowGrowth);
			if ((totalRows % rowGrowth) > 0) newM++;
			newM *= rowGrowth;
			
			// expand row header pointers
			int tmp[] = new int[newM];
			System.arraycopy(rowHead, 0, tmp, 0, m);
			this.rowHead = tmp;

			// expand row id pointers
			tmp = new int[newM];
			System.arraycopy(rowId, 0, tmp, 0, m);
			this.rowId = tmp;

			// expand external row id pointers
			tmp = new int[newM];
			System.arraycopy(rowExtId, 0, tmp, 0, m);
			this.rowExtId = tmp;

			// expand row name pointers
			String strTmp[] = new String[newM];
			System.arraycopy(rowName, 0, strTmp, 0, m);
			this.rowName = strTmp;

			// expand row rhs values
			double dblTmp[] = new double[newM];
			System.arraycopy(rowRhs, 0, dblTmp, 0, m);
			this.rowRhs = dblTmp;

			// expand range row pointers
			dblTmp = new double[newM];
			System.arraycopy(rowSlack, 0, dblTmp, 0, m);
			this.rowSlack = dblTmp;

			// expand row types
			char charTmp[] = new char[newM];
			System.arraycopy(rowType, 0, charTmp, 0, m);
			this.rowType = charTmp;

			// expand non-zero counter array 
			tmp = new int[newM];
			System.arraycopy(rowNzeroCount, 0, tmp, 0, m);
			this.rowNzeroCount = tmp;
		}
	}
	
	/**
	 * Performs work to allocate memory to hold column entries if necessary
	 *  
	 * @param totalColumns Minimum number of columns that should be supported by matrix
	 */
	private void ensureColumnCapacity(int totalColumns) {
		//TODO: use disk access if necessary when expansion would cause memory issues
		
		// check if new size has exceeded available memory in column arrays
		int n = colHead.length;
		if (totalColumns > n) {
			// calculate new size based on column growth
			int newN = (totalColumns / columnGrowth);
			if ((totalColumns % columnGrowth) > 0) newN++;
			newN *= columnGrowth;
			
			// expand column head pointers
			int tmp[] = new int[newN];
			System.arraycopy(colHead, 0, tmp, 0, n);
			this.colHead = tmp;
			
			// expand column id pointers
			tmp = new int[newN];
			System.arraycopy(colId, 0, tmp, 0, n);
			this.colId = tmp;

			// expand external column id pointers
			tmp = new int[newN];
			System.arraycopy(colExtId, 0, tmp, 0, n);
			this.colExtId = tmp;

			// expand column objectives
			double dblTmp[] = new double[newN];
			System.arraycopy(colObjective, 0, dblTmp, 0, n);
			this.colObjective = dblTmp;

			// expand column lower bounds
			dblTmp = new double[newN];
			System.arraycopy(colLowerBound, 0, dblTmp, 0, n);
			this.colLowerBound = dblTmp;

			// expand column upper bounds
			dblTmp = new double[newN];
			System.arraycopy(colUpperBound, 0, dblTmp, 0, n);
			this.colUpperBound = dblTmp;

			// expand column types
			char charTmp[] = new char[newN];
			System.arraycopy(colType, 0, charTmp, 0, n);
			this.colType = charTmp;

			// expand column names
			String strTmp[] = new String[newN];
			System.arraycopy(colName, 0, strTmp, 0, n);
			this.colName = strTmp;

			// expand column nzero counters
			tmp = new int[newN];
			System.arraycopy(colNzeroCount, 0, tmp, 0, n);
			this.colNzeroCount = tmp;
		}
	}
	
	/**
	 * Performs work to allocate memory to hold non-zero entries if necessary
	 *  
	 * @param totalEntries	Minimum number of entries that should be supported by matrix
	 */
	private void ensureNonZeroCapacity(int totalEntries) {
		//TODO: use disk access if necessary when expansion would cause memory issues
		
		// check if new size has exceeded available memory in column arrays
		int size = nzeros.length;
		if (totalEntries > size) {
			// calculate new size based on column growth
			int newSize = (totalEntries / nzeroGrowth);
			if ((totalEntries % nzeroGrowth) > 0) newSize++;
			newSize *= nzeroGrowth;
			
			// expand row index pointers
			int tmp[] = new int[newSize];
			System.arraycopy(this.nzeroRowIdx, 0, tmp, 0, size);
			this.nzeroRowIdx = tmp;
			
			// expand col index pointers
			tmp = new int[newSize];
			System.arraycopy(this.nzeroColIdx, 0, tmp, 0, size);
			this.nzeroColIdx = tmp;
			
			// expand next row value pointers
			tmp = new int[newSize];
			System.arraycopy(this.nextRowVal, 0, tmp, 0, size);
			this.nextRowVal = tmp;
			
			// expand previous row value pointers
			tmp = new int[newSize];
			System.arraycopy(this.prevRowVal, 0, tmp, 0, size);
			this.prevRowVal = tmp;
			
			// expand next col value pointers
			tmp = new int[newSize];
			System.arraycopy(this.nextColVal, 0, tmp, 0, size);
			this.nextColVal = tmp;
			
			// expand previous col value pointers
			tmp = new int[newSize];
			System.arraycopy(this.prevColVal, 0, tmp, 0, size);
			this.prevColVal = tmp;
			
			// expand non-zero entries array
			double dblTmp[] = new double[newSize];
			System.arraycopy(this.nzeros, 0, dblTmp, 0, size);
			this.nzeros = dblTmp;
		}
	}
	
	/**
	 * Adds new columns in matrix and assigns non-zero values within columns to rows. 
	 * Rows must have been previously created using createRows before this method 
	 * is called to ensure row indexs are valid.  
	 * 
	 * @param columnCount	Number of new columns being added to matrix
	 * @param columnNames	Optional names to assign to columns.  This argument may be null;
	 * @param columnTypes	Type of variable column represents such as continuous, integer, etc.  
	 * 						If argument is null, all columns are assigned a continous type.
	 * @param objectives	Objective for column variable. If this argument is null, 0 will be used as the objective
	 * @param lowerBounds	Lower bound for the column. If this argument is null, 0 will be used as the lower bound
	 * @param upperBounds	Upper bound for the column. If this argument is null, INFINITY will be used as the upper bound
	 * @param nzeroCount	Number of non-zeros being added to matrix
	 * @param nzeroStart	Start of nonzero entries in nzeroVals array
	 * @param nzeroRow	    Row to which value is assigned
	 * @parma nzeroVals		Non-zero values that are to assigned to the columns
	 */
	public void createColumns(int columnCount, String columnNames[], char columnTypes[], double objectives[], 
			double lowerBounds[], double upperBounds[], int nzeroCount, int nzeroStart[], int nzeroRow[], double nzeroVals[]) 
	{
		// check correct number of start positions supplied
		if (nzeroStart==null || nzeroStart.length != columnCount)
			throw new IllegalArgumentException("invalid number of non-zero entry starting points supplied");
		
		// check for non-zero entry arguments
		for (int i=0; i<columnCount; i++) {
			int s = nzeroStart[i];
			int p  = (i>0) ? nzeroStart[i-1] : -1;
			
			if (s<=p || s>=nzeroCount)
				throw new IllegalArgumentException("start of non-zero entries for column " + i + " is invalid");
		}
		
		// check correct number of non-zeros supplied
		if (nzeroRow==null || nzeroRow.length != nzeroCount)
			throw new IllegalArgumentException("invalid number of non-zero row indexes supplied");
		if (nzeroVals==null || nzeroVals.length != nzeroCount)
			throw new IllegalArgumentException("invalid number of non-zero values supplied");
		
		// make sure the row indexes are valid
		for (int i=0; i<nzeroRow.length; i++) {
			int row = nzeroRow[i];
			
			if (row < 0 || row >= this.rowCount) {
				throw new IllegalArgumentException("illegal row assignment: " + row);
			}
		}
		
		// ensure non-zero capacity is available
		ensureNonZeroCapacity(this.nzeroCount + nzeroCount);
		
		// create the empty columns
		int firstColumn = this.colCount;
		createColumns(0, columnCount, columnNames, columnTypes, objectives, lowerBounds, upperBounds);
	
		// assign values to rows of column
		for (int i=0; i<columnCount; i++) {
			int col = firstColumn + i;
			int start = nzeroStart[i];
			int end = (i<columnCount-1) ? nzeroStart[i+1] : nzeroCount;

			// add all non-zeros in column
			for (int j=start; j<end; j++) {
				int row = nzeroRow[j];
				double val = nzeroVals[j];
				
				// record value in column
				setValue(col, row, val);
			}
		}
	}
	
	/**
	 * Adds new columns in matrix that can be assigned to rows.  Columns will be assigned indexes from current number of 
	 * columns n in the matrix through n + columnCount
	 * 
	 * @param columnCount	Number of new columns being added to matrix
	 * @param columnNames	Optional names to assign to columns.  This argument may be null;
	 * @param columnTypes	Type of variable column represents such as continuous, integer, etc.  
	 * 						If argument is null, all columns are assigned a continous type.
	 * @param objectives	Objective for column variable. If this argument is null, 0 will be used as the objective
	 * @param lowerBounds	Lower bound for the column. If this argument is null, 0 will be used as the lower bound
	 * @param upperBounds	Upper bound for the column. If this argument is null, INFINITY will be used as the upper bound
	 */
	public void createColumns(int columnCount, String columnNames[], char columnTypes[], 
			double objectives[], double lowerBounds[], double upperBounds[])
	{
		createColumns(0, columnCount, columnNames, columnTypes, objectives, lowerBounds, upperBounds);
	}
	
	/**
	 * Adds new columns in matrix that can be assigned to rows.  Columns will be assigned indexes from current number of 
	 * columns n in the matrix through n + columnCount
	 * 
	 * @param offset		Beginning offset that were data starts in arrays
	 * @param columnCount	Number of new columns being added to matrix
	 * @param columnNames	Optional names to assign to columns.  This argument may be null;
	 * @param columnTypes	Type of variable column represents such as continuous, integer, etc.  
	 * 						If argument is null, all columns are assigned a continous type.
	 * @param objectives	Objective for column variable. If this argument is null, 0 will be used as the objective
	 * @param lowerBounds	Lower bound for the column. If this argument is null, 0 will be used as the lower bound
	 * @param upperBounds	Upper bound for the column. If this argument is null, INFINITY will be used as the upper bound
	 */
	public void createColumns(int offset, int columnCount, String columnNames[], char columnTypes[], 
			double objectives[], double lowerBounds[], double upperBounds[]) 
	{
		// check for valid arguments
		int minLength = offset + columnCount;
		if (columnNames!=null && columnNames.length < minLength)
			throw new IllegalArgumentException("invalid number of column names supplied");
		if (columnTypes!=null && columnTypes.length < minLength)
			throw new IllegalArgumentException("invalid number of column types supplied");
		if (objectives!=null && objectives.length < minLength)
			throw new IllegalArgumentException("invalid number of objectives supplied");
		if (lowerBounds!=null && lowerBounds.length < minLength)
			throw new IllegalArgumentException("invalid number of lower bounds supplied");
		if (upperBounds!=null && upperBounds.length < minLength)
			throw new IllegalArgumentException("invalid number of objectives supplied");
		
		// check for valid bounds
		if (lowerBounds != null || upperBounds != null) {
			for (int i=0; i<columnCount; i++) {
				int dataIdx = offset + i;
				double lb = (lowerBounds!=null) ? lowerBounds[dataIdx] : 0d;
				double ub = (upperBounds!=null) ? upperBounds[dataIdx] : DoubleUtil.INFINITY;
				
				if (lb > ub) 
					throw new IllegalArgumentException("lower bound is greater than upper bound for column: " + i);
			}
		}
		
		// make sure enough room exists for columns
		ensureColumnCapacity(this.colCount + columnCount);
		
		// add columns to array
		for (int i=0; i<columnCount; i++) {
			int dataIdx = offset + i;
			String name = (columnNames!=null) ? columnNames[dataIdx] : null;
			char type = (columnTypes!=null) ? columnTypes[dataIdx] : COLUMN_TYPE_CONTINUOUS;
			double obj = (objectives!=null) ? objectives[dataIdx] : 0d;
			double lb = (lowerBounds!=null) ? lowerBounds[dataIdx] : 0d;
			double ub = (upperBounds!=null) ? upperBounds[dataIdx] : DoubleUtil.INFINITY;
			
			// verify column type is valid
			if (type != COLUMN_TYPE_CONTINUOUS &&
			    type != COLUMN_TYPE_BINARY &&
			    type != COLUMN_TYPE_INTEGER &&
			    type != COLUMN_TYPE_SEMI_CONTINUOUS &&
			    type != COLUMN_TYPE_SEMI_INTEGER) 
			{
				type = COLUMN_TYPE_CONTINUOUS;
			}
			
			// double check bounds if they exceed infinity
			if (lb < -DoubleUtil.INFINITY) lb = -DoubleUtil.INFINITY;
			if (ub > DoubleUtil.INFINITY) ub = DoubleUtil.INFINITY;
			
			// add column information to arrays
			addNewColumn(name, type, obj, lb, ub);
		}
	}
	
	/**
	 * Helper function to create a new column entry in matrix
	 * 
	 * @param name	Optional name to assign to column
	 * @param type	Type of column to create
	 * @param obj	Objective value for column
	 * @param lb	Lower bound for column
	 * @param ub	Upper bound for column
	 * @return		Internal index of newly created column
	 */
	private int addNewColumn(String name, char type, double obj, double lb, double ub) {
		// determine location for inserting column name in search lists
		if (colNameMap.containsKey(name))
			throw new IllegalArgumentException("column with name '" + name + "' already exists");
		
		// check if previously deleted column exists that can hold new column
		int col = this.delColHead;
		if (col >= 0)
			this.delColHead = this.colHead[col];
		else
			col = this.colCount;
		
		// add column information to arrays
		int colId = this.colCount++;
		this.colId[colId] = col;
		this.colExtId[col] = colId;
		this.colName[col] = name;
		this.colHead[col] = -1;
		this.colType[col] = type;
		this.colObjective[col] = obj;
		this.colLowerBound[col] = lb;
		this.colUpperBound[col] = ub;

		// add column name to search list
		if (name != null)
			colNameMap.put(name, new Integer(col));
	
		return col;
	}
	
	/**
	 * Removes specified columns from the matrix. All columns after index end
	 * are decreased by the number of columns deleted
	 * 
	 * @param start	Index of first column to be deleted
	 * @param end	Index of last column to be deleted
	 */
	public void deleteColumns(int start, int end) {
		// validate paramters
		if (start < 0 || start >= this.colCount)
			throw new IllegalArgumentException("starting index is invalid");
		if (end < 0 || end >= this.colCount)
			throw new IllegalArgumentException("ending index is invalid");
		if (start > end)
			throw new IllegalArgumentException("start index is greater than end index");
		
		// loop through columns and delete them from the matrix
		for (int i=start; i<=end; i++) {
			int col = this.colId[i];
			
			// remove from name list
			String name = this.colName[col];
			if (name!=null) {
				colNameMap.remove(name);
				this.colName[col] = null;
			}
			
			// remove column from rows
			int nzeroIdx = this.colHead[col];
			while (nzeroIdx >= 0) {
				this.removeInternalValue(col, this.nzeroRowIdx[nzeroIdx], nzeroIdx);
				nzeroIdx = this.colHead[col];
			}
			
			// insert id of column at beginning of deleted list
			this.colHead[col] = delColHead;
			this.delColHead = col;
		}
		
		// reduce column ids following end
		int numToMove = this.colCount - end - 1;
		int numRemoved = end - start + 1;
		if (numToMove > 0) {
			// decrement internal to external id pointers
			for (int i=end+1; i<this.colCount; i++) {
				int col = this.colId[i];
				this.colExtId[col] -= numRemoved; 
			}
		
			// shift pointer array from external to internal accordingly
			System.arraycopy(this.colId, end+1, this.colId, start, numToMove);
		}
		
		// reduce column count
		this.colCount -= numRemoved;
	}
	
	/**
	 * Adds new rows in matrix and assigns non-zero values within rows to columns. 
	 * Columns must have been previously created using createCols before this method 
	 * is called to ensure column indexes are valid.  
	 * 
	 * @param rowCount  	Number of new rows being added to matrix
	 * @param rowNames		Optional names to assign to columns.  This argument may be null;
	 * @param rowTypes  	Type of row such as <=, >=, range, etc.  If argument is null, = is used.
	 * @param rhsVals		RHS value for row. If argument is null, 0 is used.
	 * @param rangeValues	Range value for column. If this argument is null, 0 will be used.
	 * @param nzeroCount	Number of non-zeros being added to matrix
	 * @param nzeroStart	Start of nonzero entries in nzeroVals array
	 * @param nzeroCol	    Column to which value is assigned
	 * @parma nzeroVals		Non-zero values that are to assigned to the columns
	 */
	public void createRows(int rowCount, String rowNames[], char rowTypes[], double rhsVals[], double rangeValues[],
			int nzeroCount, int nzeroStart[], int nzeroCol[], double nzeroVals[]) 
	{
		// check correct number of start positions supplied
		if (nzeroStart==null || nzeroStart.length != rowCount)
			throw new IllegalArgumentException("invalid number of non-zero entry starting points supplied");
		
		// check for non-zero entry arguments
		for (int i=0; i<rowCount; i++) {
			int s = nzeroStart[i];
			int p  = (i>0) ? nzeroStart[i-1] : -1;
			
			if (s<=p || s>=nzeroCount)
				throw new IllegalArgumentException("start of non-zero entries for row " + i + " is invalid");
		}
		
		// check correct number of non-zeros supplied
		if (nzeroCol==null || nzeroCol.length != nzeroCount)
			throw new IllegalArgumentException("invalid number of non-zero row indexes supplied");
		if (nzeroVals==null || nzeroVals.length != nzeroCount)
			throw new IllegalArgumentException("invalid number of non-zero values supplied");
		
		// make sure the column indexes are valid
		for (int i=0; i<nzeroCol.length; i++) {
			int col = nzeroCol[i];
			
			if (col < 0 || col >= this.colCount) {
				throw new IllegalArgumentException("illegal column assignment: " + col);
			}
		}
		
		// ensure non-zero capacity is available
		ensureNonZeroCapacity(this.nzeroCount + nzeroCount);
		
		// create the empty rows
		int firstRow = this.rowCount;
		createRows(0, rowCount, rowNames, rowTypes, rhsVals, rangeValues);
	
		// assign values to columns of row
		for (int i=0; i<rowCount; i++) {
			int row = firstRow + i;
			int start = nzeroStart[i];
			int end = (i<rowCount-1) ? nzeroStart[i+1] : nzeroCount;

			// add all non-zeros in row
			for (int j=start; j<end; j++) {
				int col = nzeroCol[j];
				double val = nzeroVals[j];
				
				// record value in column
				setValue(col, row, val);
			}
		}
	}
	
	/**
	 * Adds new rows in matrix that can be assigned to columns.  Rows will be assigned indexes from current number of 
	 * rows m in the matrix through m + rowCount
	 * 
	 * @param rowCount  	Number of new rows being added to matrix
	 * @param rowNames		Optional names to assign to columns.  This argument may be null;
	 * @param rowTypes  	Type of row such as <=, >=, range, etc.  If argument is null, = is used.
	 * @param rhsVals		RHS value for row. If argument is null, 0 is used.
	 * @param rangeValues	Range value for column. If this argument is null, 0 will be used.
	 */
	public void createRows(int rowCount, String rowNames[], char rowTypes[], double rhsVals[], double rangeValues[]) {
		createRows(0, rowCount, rowNames, rowTypes, rhsVals, rangeValues);
	}
	
	/**
	 * Adds new rows in matrix that can be assigned to columns.  Rows will be assigned indexes from current number of 
	 * rows m in the matrix through m + rowCount
	 * 
	 * @param offset		Beginning offset that were data starts in arrays
	 * @param rowCount  	Number of new rows being added to matrix
	 * @param rowNames		Optional names to assign to columns.  This argument may be null;
	 * @param rowTypes  	Type of row such as <=, >=, range, etc.  If argument is null, = is used.
	 * @param rhsVals		RHS value for row. If argument is null, 0 is used.
	 * @param rangeValues	Range value for column. If this argument is null, 0 will be used.
	 */
	public void createRows(int offset, int rowCount, String rowNames[], char rowTypes[], double rhsVals[], double rangeValues[]) {
		// check for valid arguments
		int minLength = offset + rowCount;
		if (rowNames!=null && rowNames.length < minLength)
			throw new IllegalArgumentException("invalid number of row names supplied");
		if (rowTypes!=null && rowTypes.length < minLength)
			throw new IllegalArgumentException("invalid number of row types supplied");
		if (rhsVals!=null && rhsVals.length < minLength)
			throw new IllegalArgumentException("invalid number of rhs values supplied");
		if (rangeValues!=null && rangeValues.length < minLength)
			throw new IllegalArgumentException("invalid number of range values supplied");
		
		// make sure range values are valid
		if (rangeValues != null) {
			for (int i=0; i<rowCount; i++) {
				int dataIdx = offset + i;
				double val = rangeValues[dataIdx];
				
				if (val < 0)
					throw new IllegalArgumentException("range value must be greater than zero");
			}
		}
		
		// make sure enough room exists for rows
		ensureRowCapacity(this.rowCount + rowCount);
		
		// add columns to array
		for (int i=0; i<rowCount; i++) {
			int dataIdx = offset + i;
			String name = (rowNames!=null) ? rowNames[dataIdx] : null;
			char type = (rowTypes!=null) ? rowTypes[dataIdx] : ROW_TYPE_EQ;
			double rhs = (rhsVals!=null) ? rhsVals[dataIdx] : 0d;
			
			// verify row type is valid
			if (type != ROW_TYPE_EQ && type != ROW_TYPE_LEQ && type != ROW_TYPE_GEQ && type != ROW_TYPE_RANGE)
				type = ROW_TYPE_EQ;
			
			// determine id for inserting row name in search lists
			if (rowNameMap.containsKey(name))
				throw new IllegalArgumentException("row with name '" + name + "' already exists");
			
			// check if previously deleted row exists that can hold new row
			int row = this.delRowHead;
			if (row >= 0)
				this.delRowHead = this.rowHead[row];
			else
				row = this.rowCount;
			
			// add column information to arrays
			int rowId = this.rowCount++;
			this.rowId[rowId] = row;
			this.rowExtId[row] = rowId;
			this.rowHead[row] = -1;
			this.rowName[row] = name;
			this.rowType[row] = type;
			this.rowRhs[row] = rhs;
			
			// check if slack value should be added
			if (type != ROW_TYPE_EQ) {
				// determine coeff of slack and upper bound
				if (type == ROW_TYPE_GEQ) {
					this.rowSlack[row] = -1d;
				}
				else if (type == ROW_TYPE_LEQ) {
					this.rowSlack[row] = 1d;
				}
				else {
					this.rowSlack[row] = (rangeValues!=null) ? rangeValues[dataIdx] : 0d;
				}
			}
			else {
				this.rowSlack[row] = 0d;
			}

			// add row name to search list
			if (name != null)
				rowNameMap.put(name, new Integer(row));
		}
	}
	
	/**
	 * Removes specified rows from the matrix. All rows after index end
	 * are decreased by the number of rows deleted
	 * 
	 * @param start	Index of first rpw to be deleted
	 * @param end	Index of last row to be deleted
	 */
	public void deleteRows(int start, int end) {
		// validate paramters
		if (start < 0 || start >= this.rowCount)
			throw new IllegalArgumentException("starting index is invalid");
		if (end < 0 || end >= this.rowCount)
			throw new IllegalArgumentException("ending index is invalid");
		if (start > end)
			throw new IllegalArgumentException("start index is greater than end index");
		
		// loop through rows and delete them from the matrix
		for (int i=start; i<=end; i++) {
			int row = this.rowId[i];
			
			// remove from name list
			String name = this.rowName[row];
			if (name!=null) {
				rowNameMap.remove(name);
				this.rowName[row] = null;
			}
			
			// remove row from columns
			int nzeroIdx = this.rowHead[row];
			while (nzeroIdx >= 0) {
				this.removeInternalValue(this.nzeroColIdx[nzeroIdx], row, nzeroIdx);
				nzeroIdx = this.rowHead[row];
			}
			
			// insert id of row at beginning of deleted list
			this.rowHead[row] = delRowHead;
			this.delRowHead = row;
		}
		
		// reduce row ids following end
		int numToMove = this.rowCount - end - 1;
		int numRemoved = end - start + 1;
		if (numToMove > 0) {
			// decrement internal to external id pointers
			for (int i=end+1; i<this.rowCount; i++) {
				int row = this.rowId[i];
				this.rowExtId[row] -= numRemoved; 
			}
		
			// shift pointer array from external to internal accordingly
			System.arraycopy(this.rowId, end+1, this.rowId, start, numToMove);
		}
		
		// reduce row count
		this.rowCount -= numRemoved;
	}
	
	/**
	 * Adds a non-zero to the matrix
	 *  
	 * @param col	Internal column index to add new value
	 * @param row	Internal row index to add new value
	 * @param val	New value to store at location
	 * @return Index of new non-zero entry in nzeros array
	 */
	private int addNonZeroValue(int col, int row, double val) {
		// grow the matrix if necessary
		ensureNonZeroCapacity(this.nzeroCount+1);
		
		// check if previously deleted column exists that can hold new column
		int nzeroIdx = this.delNzeroHead;
		if (nzeroIdx >= 0)
			this.delNzeroHead = this.nzeroRowIdx[nzeroIdx];
		else
			nzeroIdx = this.nzeroCount;
		
		// add non-zero to matrix
		this.nzeroColIdx[nzeroIdx] = col;
		this.nzeroRowIdx[nzeroIdx] = row;
		this.nzeros[nzeroIdx] = val;
		this.nzeroCount++;
		this.rowNzeroCount[row]++;
		this.colNzeroCount[col]++;
		
		// insert value at front of column
		int oldHead = colHead[col];
		this.colHead[col] = nzeroIdx;
		this.prevColVal[nzeroIdx] = -1;
		this.nextColVal[nzeroIdx] = oldHead;
		if (oldHead >= 0)
			this.prevColVal[oldHead] = nzeroIdx;
		
		// insert value at front of row
		oldHead = rowHead[row];
		this.rowHead[row] = nzeroIdx;
		this.prevRowVal[nzeroIdx] = -1;
		this.nextRowVal[nzeroIdx] = oldHead;
		if (oldHead >= 0)
			this.prevRowVal[oldHead] = nzeroIdx;
		
		return nzeroIdx;
	}
	
	/**
	 * Returns the position in the nzeros array of a value for a given
	 * row and column
	 * 
	 * @param col	Internal index of column to assign a value
	 * @param row	Internal index of row to assign a value
	 * @return Position in nzero array corresponding to row and col values
	 */
	private int getNzeroIndex(int col, int row) {
		int nzeroIdx = this.colHead[col];
		while (nzeroIdx >= 0 && this.nzeroRowIdx[nzeroIdx] != row) {
			nzeroIdx = this.nextColVal[nzeroIdx];
		}
		return nzeroIdx;
	}
	
	/**
	 * Removes a non-zero value from the matrix
	 *  
	 * @param col		Internal column index value is being removed from
	 * @param row		Internal row index value is being removed from
	 * @param nzeroIdx	Index in non-zero array of value being removed
	 */
	private void removeInternalValue(int col, int row, int nzeroIdx) {
		// remove from prev and next column linked lists
		int prev = this.prevColVal[nzeroIdx];
		int next = this.nextColVal[nzeroIdx];
		if (prev >= 0) this.nextColVal[prev] = next;
		if (next >= 0) this.prevColVal[next] = prev;
		if (this.colHead[col] == nzeroIdx) 
			this.colHead[col] = next;
		
		// remove from prev and next row linked lists
		prev = this.prevRowVal[nzeroIdx];
		next = this.nextRowVal[nzeroIdx];
		if (prev >= 0) this.nextRowVal[prev] = next;
		if (next >= 0) this.prevRowVal[next] = prev;
		if (this.rowHead[row] == nzeroIdx) 
			this.rowHead[row] = next;
		
		// record position as deleted in deleted head
		this.nzeroRowIdx[nzeroIdx] = this.delNzeroHead;
		this.delNzeroHead = nzeroIdx;
		
		// decrement non-zero counter
		this.nzeroCount--;
		this.rowNzeroCount[row]--;
		this.colNzeroCount[col]--;
	}
	
	/**
	 * Changes a value at a corresponding internal row and index
	 * 
	 * @param col	Internal index of column to assign a value
	 * @param row	Internal index of row to assign a value
	 * @param val	Value to assign to column and row
	 */
	private void setInternalValue(int col, int row, double val) {
		// search for current entry in matrix for requested position
		int nzeroIdx = getNzeroIndex(col, row);
		
		// handle case where position exists
		if (nzeroIdx >= 0) {
			// handle when value is zero and should be deleted
			if (val == 0) {
				removeInternalValue(col, row, nzeroIdx);
			}
			else {
				this.nzeros[nzeroIdx] = val;
			}
		}
		
		// if value is not zero, add it to matrix
		else if (val != 0) {
			addNonZeroValue(col, row, val);
		}
	}
	
	/**
	 * Changes a value at a corresponding row and index
	 * 
	 * @param col	Index of column to assign a value
	 * @param row	Index of row to assign a value
	 * @param val	Value to assign to column and row
	 */
	public void setValue(int col, int row, double val) {
		if (col < 0 || col >= this.colCount)
			throw new IllegalArgumentException("column index is out of range");
		if (row < 0 || row >= this.rowCount)
			throw new IllegalArgumentException("row index is out of range");
		
		int cidx = this.colId[col];
		int ridx = this.rowId[row];
		setInternalValue(cidx, ridx, val);
	}
	
	/**
	 * Returns a value stored corresponding row and index
	 * 
	 * @param col	Index of column containing value
	 * @param row	Index of row containing value
	 * @return value assigned to column and row
	 */
	public double getValue(int col, int row) {
		if (col < 0 || col >= this.colCount)
			throw new IllegalArgumentException("column index is out of range");
		if (row < 0 || row >= this.rowCount)
			throw new IllegalArgumentException("row index is out of range");
		
		int cidx = this.colId[col];
		int ridx = this.rowId[row];
		int nidx = this.getNzeroIndex(cidx, ridx);
		
		// if entry is not in matrix, it is a zero value
		if (nidx < 0)
			return 0d;
		else
			return this.nzeros[nidx];
	}
	
	/**
	 * Returns the number of non-zeros stored in a given column
	 * 
	 * @param col	Index of column to retrieve non-zero count
	 * @return Number of non-zeros in the column
	 */
	public int getColNzeroCount(int col) {
		if (col < 0 || col >= this.colCount)
			throw new IllegalArgumentException("column index is out of range");
		
		int cidx = this.colId[col];
		return this.colNzeroCount[cidx];
	}
	
	/**
	 * Returns the number of non-zeros stored in a given row
	 * 
	 * @param row	Index of row to retrieve non-zero count
	 * @return Number of non-zeros in the row
	 */
	public int getRowNzeroCount(int row) {
		if (row < 0 || row >= this.rowCount)
			throw new IllegalArgumentException("row index is out of range");
		
		int ridx = this.rowId[row];
		return this.rowNzeroCount[ridx];
	}
	
	
	/**
	 * Returns the non-zeros for an array of rows
	 */
	public int getRowNzeros(int start, int end, int nzeroStart[], int nzeroCols[], double nzeroVals[], int totalSize[]) {
		// check correct number of non-zeros supplied
		if (end > start)
			throw new IllegalArgumentException("start and end values are invalid");
		if (start < 0 || start >= this.rowCount)
			throw new IllegalArgumentException("starting index is invalid");
		if (end < 0 || end >= this.rowCount)
			throw new IllegalArgumentException("ending index is invalid");
		
		int numRows = end-start+1;
		if (nzeroStart==null || nzeroStart.length < numRows)
			throw new IllegalArgumentException("number of start positions will not allow all rows to fit");
		if (totalSize==null || totalSize.length < numRows)
			throw new IllegalArgumentException("number of entries to store size of each row will not allow all rows to fit");
			
		if (nzeroCols==null)
			throw new IllegalArgumentException("invalid number of non-zero row indexes supplied");
		if (nzeroVals==null || nzeroVals.length != nzeroCols.length)
			throw new IllegalArgumentException("invalid number of non-zero values supplied");
		
		int nzeroCnt = 0;
		int totalNzero = 0;
		for (int i=start; i<=end && nzeroCnt < nzeroVals.length; i++) {
			int row = this.rowId[i];
			int size = this.rowNzeroCount[row];		
			totalNzero += size;
			
			if (size > 0) {
				nzeroStart[i] = -1;
				totalSize[i] = 0;
			}
			else {
				nzeroStart[i] = nzeroCnt;
				totalSize[i] = this.rowNzeroCount[row];
			
				int col = this.rowHead[i];
				while (col >= 0 && nzeroCnt < nzeroVals.length) {
					nzeroCols[nzeroCnt] = this.colExtId[this.nzeroColIdx[col]];
					nzeroVals[nzeroCnt++] = this.nzeros[col];
					col = this.nextRowVal[col];
				}
			}
		}
		
		return totalNzero;
	}
	
	/**
	 * Returns the lower bound for a column
	 * 
	 * @param idx	Index of column to retrieve lower bound
	 * @return	Lower bound of the column
	 */
	public double getLowerBound(int idx) {
		if (idx < 0 || idx >= this.colCount)
			throw new IllegalArgumentException("column index is out of range");
		
		int cidx = this.colId[idx];
		return this.colLowerBound[cidx];
	}
	
	/**
	 * Returns the upper bound for a column
	 * 
	 * @param idx	Index of column to retrieve upper bound
	 * @return	Upper bound of the column
	 */
	public double getUpperBound(int idx) {
		if (idx < 0 || idx >= this.colCount)
			throw new IllegalArgumentException("column index is out of range");
		
		int cidx = this.colId[idx];
		return this.colUpperBound[cidx];
	}
	
	/**
	 * Returns the objective value for a column
	 * 
	 * @param idx	Index of column to retrieve objective
	 * @return	Objective of a column
	 */
	public double getObjective(int idx) {
		if (idx < 0 || idx >= this.colCount)
			throw new IllegalArgumentException("column index is out of range");
		
		int cidx = this.colId[idx];
		return this.colObjective[cidx];
	}
	
	/**
	 * Returns the RHS value for a row
	 * 
	 * @param idx	Index of row to retrieve RHS
	 * @return	RHS of a row
	 */
	public double getRHS(int idx) {
		if (idx < 0 || idx >= this.rowCount)
			throw new IllegalArgumentException("row index is out of range");
		
		int ridx = this.rowId[idx];
		return this.rowRhs[ridx];
	}
	
	/**
	 * Returns the range value for a row
	 * 
	 * @param idx	Index of row to retrieve range value
	 * @return	range value of a row
	 */
	public double getRangeValue(int idx) {
		if (idx < 0 || idx >= this.rowCount)
			throw new IllegalArgumentException("row index is out of range");
		
		int ridx = this.rowId[idx];
		
		if (this.rowType[ridx] == ROW_TYPE_RANGE)
			return this.rowSlack[ridx];
		else
			return 0;
	}
	
	/**
	 * Returns a row's type
	 * 
	 * @param idx	Index of row to retrieve type
	 * @return	type of a row
	 */
	public char getRowType(int idx) {
		if (idx < 0 || idx >= this.rowCount)
			throw new IllegalArgumentException("row index is out of range");
		
		int ridx = this.rowId[idx];
		return this.rowType[ridx];
	}
	
	/**
	 * Returns the index of a column corresponding to a column name
	 */
	public int indexOfColumn(String columnName) {
		Integer entry = (Integer) colNameMap.get(columnName);
		if (entry==null)
			return -1;
		else
			return this.colExtId[entry.intValue()];
	}
	
	/**
	 * Returns the index of a row corresponding to a row name
	 */
	public int indexOfRow(String rowName) {
		Integer entry = (Integer) rowNameMap.get(rowName);
		if (entry==null)
			return -1;
		else
			return this.rowExtId[entry.intValue()];
	}
	
	/**
	 * Method used in testing matrix
	 */
	public int internalColIndex(int idx) {
		if (idx < 0 || idx >= this.colCount)
			throw new IllegalArgumentException("column index is out of range");
		return this.colId[idx];
	}
	
	/**
	 * Method used in testing matrix
	 */
	public int internalRowIndex(int idx) {
		if (idx < 0 || idx >= this.rowCount)
			throw new IllegalArgumentException("row index is out of range");
		return this.rowId[idx];
	}
}
