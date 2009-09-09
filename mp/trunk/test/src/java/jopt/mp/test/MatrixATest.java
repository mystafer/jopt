package jopt.mp.test;

import jopt.mp.spi.MatrixA;
import junit.framework.TestCase;

/**
 * Simple tests on Matrix A to ensure that it grows and store data correctly
 */
public class MatrixATest extends TestCase {
	public void testColumnGrowth() {
		// create simple matrix with row growth: 2, col growth: 2, nzero growth: 4
		MatrixA a = new MatrixA(2, 2, 4);
		assertEquals("column count", 0, a.getColCount());
		assertEquals("row count", 0, a.getRowCount());
		assertEquals("non-zero count", 0, a.getNonZeroCount());
		assertEquals("column storage space", 2, a.getColStorageSize());
		assertEquals("row storage space", 2, a.getRowStorageSize());
		assertEquals("non-zero storage space", 4, a.getNonZeroStorageSize());
		
		// create two empty columns 
		a.createColumns(0, 2, new String[]{"c1", "c2"}, null, null, null, null);
		assertEquals("column count", 2, a.getColCount());
		assertEquals("row count", 0, a.getRowCount());
		assertEquals("non-zero count", 0, a.getNonZeroCount());
		assertEquals("column storage space", 2, a.getColStorageSize());
		assertEquals("row storage space", 2, a.getRowStorageSize());
		assertEquals("non-zero storage space", 4, a.getNonZeroStorageSize());

		// verify location of columns in matrix
		int cidx1 = a.indexOfColumn("c1");
		int cidx2 = a.indexOfColumn("c2");
		assertEquals("c1", 0, cidx1);
		assertEquals("c2", 1, cidx2);

		// create one more empty column 
		a.createColumns(0, 1, new String[]{"c3"}, null, null, null, null);
		assertEquals("column count", 3, a.getColCount());
		assertEquals("row count", 0, a.getRowCount());
		assertEquals("non-zero count", 0, a.getNonZeroCount());
		assertEquals("column storage space", 4, a.getColStorageSize());
		assertEquals("row storage space", 2, a.getRowStorageSize());
		assertEquals("non-zero storage space", 4, a.getNonZeroStorageSize());

		// verify location of columns in matrix
		cidx1 = a.indexOfColumn("c1");
		cidx2 = a.indexOfColumn("c2");
		int cidx3 = a.indexOfColumn("c3");
		assertEquals("c1", 0, cidx1);
		assertEquals("c2", 1, cidx2);
		assertEquals("c3", 2, cidx3);
	}

	public void testRowGrowth() {
		// create simple matrix with row growth: 2, col growth: 2, nzero growth: 4
		MatrixA a = new MatrixA(2, 2, 4);
		assertEquals("column count", 0, a.getColCount());
		assertEquals("row count", 0, a.getRowCount());
		assertEquals("non-zero count", 0, a.getNonZeroCount());
		assertEquals("column storage space", 2, a.getColStorageSize());
		assertEquals("row storage space", 2, a.getRowStorageSize());
		assertEquals("non-zero storage space", 4, a.getNonZeroStorageSize());
		
		// create two empty rows
		a.createRows(0, 2, new String[]{"r1", "r2"}, null, null, null);
		assertEquals("column count", 0, a.getColCount());
		assertEquals("row count", 2, a.getRowCount());
		assertEquals("non-zero count", 0, a.getNonZeroCount());
		assertEquals("column storage space", 2, a.getColStorageSize());
		assertEquals("row storage space", 2, a.getRowStorageSize());
		assertEquals("non-zero storage space", 4, a.getNonZeroStorageSize());

		// verify location of row in matrix
		int ridx1 = a.indexOfRow("r1");
		int ridx2 = a.indexOfRow("r2");
		assertEquals("r1", 0, ridx1);
		assertEquals("r2", 1, ridx2);

		// create one more empty row 
		a.createRows(0, 1, new String[]{"r3"}, null, null, null);
		assertEquals("column count", 0, a.getColCount());
		assertEquals("row count", 3, a.getRowCount());
		assertEquals("non-zero count", 0, a.getNonZeroCount());
		assertEquals("column storage space", 2, a.getColStorageSize());
		assertEquals("row storage space", 4, a.getRowStorageSize());
		assertEquals("non-zero storage space", 4, a.getNonZeroStorageSize());

		// verify location of row in matrix
		ridx1 = a.indexOfRow("r1");
		ridx2 = a.indexOfRow("r2");
		int ridx3 = a.indexOfRow("r3");
		assertEquals("r1", 0, ridx1);
		assertEquals("r2", 1, ridx2);
		assertEquals("r2", 2, ridx3);
	}

	public void testColumnDelete() {
		// create simple matrix with row growth: 2, col growth: 2, nzero growth: 4
		MatrixA a = new MatrixA(2, 2, 4);
		assertEquals("column count", 0, a.getColCount());
		assertEquals("row count", 0, a.getRowCount());
		assertEquals("non-zero count", 0, a.getNonZeroCount());
		assertEquals("column storage space", 2, a.getColStorageSize());
		assertEquals("row storage space", 2, a.getRowStorageSize());
		assertEquals("non-zero storage space", 4, a.getNonZeroStorageSize());
		
		// create two empty columns 
		a.createColumns(0, 3, new String[]{"c1", "c2", "c3"}, null, null, null, null);
		assertEquals("column count", 3, a.getColCount());
		assertEquals("row count", 0, a.getRowCount());
		assertEquals("non-zero count", 0, a.getNonZeroCount());
		assertEquals("column storage space", 4, a.getColStorageSize());
		assertEquals("row storage space", 2, a.getRowStorageSize());
		assertEquals("non-zero storage space", 4, a.getNonZeroStorageSize());

		// verify location of columns in matrix
		int cidx1 = a.indexOfColumn("c1");
		int cidx2 = a.indexOfColumn("c2");
		int cidx3 = a.indexOfColumn("c3");
		assertEquals("c1", 0, cidx1);
		assertEquals("c2", 1, cidx2);
		assertEquals("c3", 2, cidx3);
		
		// attempt to delete invalid column from matrix
		try {
			a.deleteColumns(5, 5);
			fail("should not have been able to delte column with invalid index");
		}
		catch(IllegalArgumentException e) {}
		cidx1 = a.indexOfColumn("c1");
		cidx2 = a.indexOfColumn("c2");
		cidx3 = a.indexOfColumn("c3");
		assertEquals("c1", 0, cidx1);
		assertEquals("c2", 1, cidx2);
		assertEquals("c3", 2, cidx3);
		assertEquals("column count", 3, a.getColCount());
		assertEquals("row count", 0, a.getRowCount());
		assertEquals("non-zero count", 0, a.getNonZeroCount());
		assertEquals("column storage space", 4, a.getColStorageSize());
		assertEquals("row storage space", 2, a.getRowStorageSize());
		assertEquals("non-zero storage space", 4, a.getNonZeroStorageSize());
		
		// delete column from middle of matrix
		a.deleteColumns(1, 1);
		cidx1 = a.indexOfColumn("c1");
		cidx2 = a.indexOfColumn("c2");
		cidx3 = a.indexOfColumn("c3");
		assertEquals("c1", 0, cidx1);
		assertEquals("c2", -1, cidx2);
		assertEquals("c3", 1, cidx3);
		assertEquals("column count", 2, a.getColCount());
		assertEquals("row count", 0, a.getRowCount());
		assertEquals("non-zero count", 0, a.getNonZeroCount());
		assertEquals("column storage space", 4, a.getColStorageSize());
		assertEquals("row storage space", 2, a.getRowStorageSize());
		assertEquals("non-zero storage space", 4, a.getNonZeroStorageSize());
		
		// delete column from end of matrix
		a.deleteColumns(1, 1);
		cidx1 = a.indexOfColumn("c1");
		cidx2 = a.indexOfColumn("c2");
		cidx3 = a.indexOfColumn("c3");
		assertEquals("c1", 0, cidx1);
		assertEquals("c2", -1, cidx2);
		assertEquals("c3", -1, cidx3);
		assertEquals("column count", 1, a.getColCount());
		assertEquals("row count", 0, a.getRowCount());
		assertEquals("non-zero count", 0, a.getNonZeroCount());
		assertEquals("column storage space", 4, a.getColStorageSize());
		assertEquals("row storage space", 2, a.getRowStorageSize());
		assertEquals("non-zero storage space", 4, a.getNonZeroStorageSize());
		
		// delete last column from matrix
		a.deleteColumns(0, 0);
		cidx1 = a.indexOfColumn("c1");
		cidx2 = a.indexOfColumn("c2");
		cidx3 = a.indexOfColumn("c3");
		assertEquals("c1", -1, cidx1);
		assertEquals("c2", -1, cidx2);
		assertEquals("c3", -1, cidx3);
		assertEquals("column count", 0, a.getColCount());
		assertEquals("row count", 0, a.getRowCount());
		assertEquals("non-zero count", 0, a.getNonZeroCount());
		assertEquals("column storage space", 4, a.getColStorageSize());
		assertEquals("row storage space", 2, a.getRowStorageSize());
		assertEquals("non-zero storage space", 4, a.getNonZeroStorageSize());

		// reset matrix and delete columns c1, c2
		a = new MatrixA(2, 2, 4);
		a.createColumns(0, 3, new String[]{"c1", "c2", "c3"}, null, null, null, null);
		a.deleteColumns(0, 1);
		assertEquals("c1", -1, a.indexOfColumn("c1"));
		assertEquals("c2", -1, a.indexOfColumn("c2"));
		assertEquals("c3", 0, a.indexOfColumn("c3"));
		assertEquals("column count", 1, a.getColCount());
		assertEquals("row count", 0, a.getRowCount());
		assertEquals("non-zero count", 0, a.getNonZeroCount());
		assertEquals("column storage space", 4, a.getColStorageSize());
		assertEquals("row storage space", 2, a.getRowStorageSize());
		assertEquals("non-zero storage space", 4, a.getNonZeroStorageSize());

		// reset matrix and delete columns c2, c3
		a = new MatrixA(2, 2, 4);
		a.createColumns(0, 3, new String[]{"c1", "c2", "c3"}, null, null, null, null);
		a.deleteColumns(1, 2);
		assertEquals("c1", 0, a.indexOfColumn("c1"));
		assertEquals("c2", -1, a.indexOfColumn("c2"));
		assertEquals("c3", -1, a.indexOfColumn("c3"));
		assertEquals("column count", 1, a.getColCount());
		assertEquals("row count", 0, a.getRowCount());
		assertEquals("non-zero count", 0, a.getNonZeroCount());
		assertEquals("column storage space", 4, a.getColStorageSize());
		assertEquals("row storage space", 2, a.getRowStorageSize());
		assertEquals("non-zero storage space", 4, a.getNonZeroStorageSize());

		// reset matrix and delete columns c1, c3
		a = new MatrixA(2, 2, 4);
		a.createColumns(0, 3, new String[]{"c1", "c2", "c3"}, null, null, null, null);
		a.deleteColumns(0, 0);
		a.deleteColumns(1, 1);
		assertEquals("c1", -1, a.indexOfColumn("c1"));
		assertEquals("c2", 0, a.indexOfColumn("c2"));
		assertEquals("c3", -1, a.indexOfColumn("c3"));
		assertEquals("column count", 1, a.getColCount());
		assertEquals("row count", 0, a.getRowCount());
		assertEquals("non-zero count", 0, a.getNonZeroCount());
		assertEquals("column storage space", 4, a.getColStorageSize());
		assertEquals("row storage space", 2, a.getRowStorageSize());
		assertEquals("non-zero storage space", 4, a.getNonZeroStorageSize());

		// reset matrix and delete all columns
		a = new MatrixA(2, 2, 4);
		a.createColumns(0, 3, new String[]{"c1", "c2", "c3"}, null, null, null, null);
		a.deleteColumns(0, 2);
		assertEquals("c1", -1, a.indexOfColumn("c1"));
		assertEquals("c2", -1, a.indexOfColumn("c2"));
		assertEquals("c3", -1, a.indexOfColumn("c3"));
		assertEquals("column count", 0, a.getColCount());
		assertEquals("row count", 0, a.getRowCount());
		assertEquals("non-zero count", 0, a.getNonZeroCount());
		assertEquals("column storage space", 4, a.getColStorageSize());
		assertEquals("row storage space", 2, a.getRowStorageSize());
		assertEquals("non-zero storage space", 4, a.getNonZeroStorageSize());
	}


	public void testRowDelete() {
		// create simple matrix with row growth: 2, col growth: 2, nzero growth: 4
		MatrixA a = new MatrixA(2, 2, 4);
		assertEquals("column count", 0, a.getColCount());
		assertEquals("row count", 0, a.getRowCount());
		assertEquals("non-zero count", 0, a.getNonZeroCount());
		assertEquals("column storage space", 2, a.getColStorageSize());
		assertEquals("row storage space", 2, a.getRowStorageSize());
		assertEquals("non-zero storage space", 4, a.getNonZeroStorageSize());
		
		// create two empty rows 
		a.createRows(0, 3, new String[]{"r1", "r2", "r3"}, null, null, null);
		assertEquals("column count", 0, a.getColCount());
		assertEquals("row count", 3, a.getRowCount());
		assertEquals("non-zero count", 0, a.getNonZeroCount());
		assertEquals("column storage space", 2, a.getColStorageSize());
		assertEquals("row storage space", 4, a.getRowStorageSize());
		assertEquals("non-zero storage space", 4, a.getNonZeroStorageSize());

		// verify location of rows in matrix
		int ridx1 = a.indexOfRow("r1");
		int ridx2 = a.indexOfRow("r2");
		int ridx3 = a.indexOfRow("r3");
		assertEquals("r1", 0, ridx1);
		assertEquals("r2", 1, ridx2);
		assertEquals("r3", 2, ridx3);
		
		// attempt to delete invalid row from matrix
		try {
			a.deleteRows(5, 5);
			fail("should not have been able to delte row with invalid index");
		}
		catch(IllegalArgumentException e) {}
		ridx1 = a.indexOfRow("r1");
		ridx2 = a.indexOfRow("r2");
		ridx3 = a.indexOfRow("r3");
		assertEquals("r1", 0, ridx1);
		assertEquals("r2", 1, ridx2);
		assertEquals("r3", 2, ridx3);
		assertEquals("column count", 0, a.getColCount());
		assertEquals("row count", 3, a.getRowCount());
		assertEquals("non-zero count", 0, a.getNonZeroCount());
		assertEquals("column storage space", 2, a.getColStorageSize());
		assertEquals("row storage space", 4, a.getRowStorageSize());
		assertEquals("non-zero storage space", 4, a.getNonZeroStorageSize());
		
		// delete row from middle of matrix
		a.deleteRows(1, 1);
		ridx1 = a.indexOfRow("r1");
		ridx2 = a.indexOfRow("r2");
		ridx3 = a.indexOfRow("r3");
		assertEquals("r1", 0, ridx1);
		assertEquals("r2", -1, ridx2);
		assertEquals("r3", 1, ridx3);
		assertEquals("column count", 0, a.getColCount());
		assertEquals("row count", 2, a.getRowCount());
		assertEquals("non-zero count", 0, a.getNonZeroCount());
		assertEquals("column storage space", 2, a.getColStorageSize());
		assertEquals("row storage space", 4, a.getRowStorageSize());
		assertEquals("non-zero storage space", 4, a.getNonZeroStorageSize());
		
		// delete row from end of matrix
		a.deleteRows(1, 1);
		ridx1 = a.indexOfRow("r1");
		ridx2 = a.indexOfRow("r2");
		ridx3 = a.indexOfRow("r3");
		assertEquals("r1", 0, ridx1);
		assertEquals("r2", -1, ridx2);
		assertEquals("r3", -1, ridx3);
		assertEquals("column count", 0, a.getColCount());
		assertEquals("row count", 1, a.getRowCount());
		assertEquals("non-zero count", 0, a.getNonZeroCount());
		assertEquals("column storage space", 2, a.getColStorageSize());
		assertEquals("row storage space", 4, a.getRowStorageSize());
		assertEquals("non-zero storage space", 4, a.getNonZeroStorageSize());
		
		// delete last row from matrix
		a.deleteRows(0, 0);
		ridx1 = a.indexOfRow("r1");
		ridx2 = a.indexOfRow("r2");
		ridx3 = a.indexOfRow("r3");
		assertEquals("r1", -1, ridx1);
		assertEquals("r2", -1, ridx2);
		assertEquals("r3", -1, ridx3);
		assertEquals("column count", 0, a.getColCount());
		assertEquals("row count", 0, a.getRowCount());
		assertEquals("non-zero count", 0, a.getNonZeroCount());
		assertEquals("column storage space", 2, a.getColStorageSize());
		assertEquals("row storage space", 4, a.getRowStorageSize());
		assertEquals("non-zero storage space", 4, a.getNonZeroStorageSize());

		// reset matrix and delete rows r1, r2
		a = new MatrixA(2, 2, 4);
		a.createRows(0, 3, new String[]{"r1", "r2", "r3"}, null, null, null);
		a.deleteRows(0, 1);
		assertEquals("r1", -1, a.indexOfRow("r1"));
		assertEquals("r2", -1, a.indexOfRow("r2"));
		assertEquals("r3", 0, a.indexOfRow("r3"));
		assertEquals("column count", 0, a.getColCount());
		assertEquals("row count", 1, a.getRowCount());
		assertEquals("non-zero count", 0, a.getNonZeroCount());
		assertEquals("column storage space", 2, a.getColStorageSize());
		assertEquals("row storage space", 4, a.getRowStorageSize());
		assertEquals("non-zero storage space", 4, a.getNonZeroStorageSize());

		// reset matrix and delete rows r2, r3
		a = new MatrixA(2, 2, 4);
		a.createRows(0, 3, new String[]{"r1", "r2", "r3"}, null, null, null);
		a.deleteRows(1, 2);
		assertEquals("r1", 0, a.indexOfRow("r1"));
		assertEquals("r2", -1, a.indexOfRow("r2"));
		assertEquals("r3", -1, a.indexOfRow("r3"));
		assertEquals("column count", 0, a.getColCount());
		assertEquals("row count", 1, a.getRowCount());
		assertEquals("non-zero count", 0, a.getNonZeroCount());
		assertEquals("column storage space", 2, a.getColStorageSize());
		assertEquals("row storage space", 4, a.getRowStorageSize());
		assertEquals("non-zero storage space", 4, a.getNonZeroStorageSize());

		// reset matrix and delete row r1, r3
		a = new MatrixA(2, 2, 4);
		a.createRows(0, 3, new String[]{"r1", "r2", "r3"}, null, null, null);
		a.deleteRows(0, 0);
		a.deleteRows(1, 1);
		assertEquals("r1", -1, a.indexOfRow("r1"));
		assertEquals("r2", 0, a.indexOfRow("r2"));
		assertEquals("r3", -1, a.indexOfRow("r3"));
		assertEquals("column count", 0, a.getColCount());
		assertEquals("row count", 1, a.getRowCount());
		assertEquals("non-zero count", 0, a.getNonZeroCount());
		assertEquals("column storage space", 2, a.getColStorageSize());
		assertEquals("row storage space", 4, a.getRowStorageSize());
		assertEquals("non-zero storage space", 4, a.getNonZeroStorageSize());

		// reset matrix and delete all rows
		a = new MatrixA(2, 2, 4);
		a.createRows(0, 3, new String[]{"r1", "r2", "r3"}, null, null, null);
		a.deleteRows(0, 2);
		assertEquals("r1", -1, a.indexOfRow("r1"));
		assertEquals("r2", -1, a.indexOfRow("r2"));
		assertEquals("r3", -1, a.indexOfRow("r3"));
		assertEquals("column count", 0, a.getColCount());
		assertEquals("row count", 0, a.getRowCount());
		assertEquals("non-zero count", 0, a.getNonZeroCount());
		assertEquals("column storage space", 2, a.getColStorageSize());
		assertEquals("row storage space", 4, a.getRowStorageSize());
		assertEquals("non-zero storage space", 4, a.getNonZeroStorageSize());
	}
	
	public void testColDeleteAdd() {
		// if c1 is deleted, make sure c4 replaces it internally
		MatrixA a = new MatrixA(2, 2, 4);
		a.createColumns(0, 3, new String[]{"c1", "c2", "c3"}, null, null, null, null);
		a.deleteColumns(0, 0);
		a.createColumns(0, 1, new String[]{"c4"}, null, null, null, null);
		assertEquals("c1", -1, a.indexOfColumn("c1"));
		assertEquals("c2", 0, a.indexOfColumn("c2"));
		assertEquals("c3", 1, a.indexOfColumn("c3"));
		assertEquals("c4", 2, a.indexOfColumn("c4"));
		assertEquals("internal col 0", 1, a.internalColIndex(0));
		assertEquals("internal col 1", 2, a.internalColIndex(1));
		assertEquals("internal col 2", 0, a.internalColIndex(2));
		assertEquals("column count", 3, a.getColCount());
		assertEquals("row count", 0, a.getRowCount());
		assertEquals("non-zero count", 0, a.getNonZeroCount());
		assertEquals("column storage space", 4, a.getColStorageSize());
		assertEquals("row storage space", 2, a.getRowStorageSize());
		assertEquals("non-zero storage space", 4, a.getNonZeroStorageSize());
		
		// delete c1 & c2
		// then add 4 columns and see where they are added in internal arrays
		// c5 should end up in c2's old space
		// c6 should end up in c1's old space
		// c7 & c8 should be in the next consequtive spaces
		a.deleteColumns(0, 1);
		a.createColumns(0, 4, new String[]{"c5", "c6", "c7", "c8"}, null, null, null, null);
		assertEquals("c1", -1, a.indexOfColumn("c1"));
		assertEquals("c2", -1, a.indexOfColumn("c2"));
		assertEquals("c3", -1, a.indexOfColumn("c3"));
		assertEquals("c4", 0, a.indexOfColumn("c4"));
		assertEquals("c5", 1, a.indexOfColumn("c5"));
		assertEquals("c6", 2, a.indexOfColumn("c6"));
		assertEquals("c7", 3, a.indexOfColumn("c7"));
		assertEquals("c8", 4, a.indexOfColumn("c8"));
		assertEquals("internal col 0", 0, a.internalColIndex(0));
		assertEquals("internal col 1", 2, a.internalColIndex(1));
		assertEquals("internal col 2", 1, a.internalColIndex(2));
		assertEquals("internal col 2", 3, a.internalColIndex(3));
		assertEquals("internal col 2", 4, a.internalColIndex(4));
		assertEquals("column count", 5, a.getColCount());
		assertEquals("row count", 0, a.getRowCount());
		assertEquals("non-zero count", 0, a.getNonZeroCount());
		assertEquals("column storage space", 6, a.getColStorageSize());
		assertEquals("row storage space", 2, a.getRowStorageSize());
		assertEquals("non-zero storage space", 4, a.getNonZeroStorageSize());
	}
	
	public void testRowDeleteAdd() {
		// if r1 is deleted, make sure r4 replaces it internally
		MatrixA a = new MatrixA(2, 2, 4);
		a.createRows(0, 3, new String[]{"r1", "r2", "r3"}, null, null, null);
		a.deleteRows(0, 0);
		a.createRows(0, 1, new String[]{"r4"}, null, null, null);
		assertEquals("r1", -1, a.indexOfRow("r1"));
		assertEquals("r2", 0, a.indexOfRow("r2"));
		assertEquals("r3", 1, a.indexOfRow("r3"));
		assertEquals("r4", 2, a.indexOfRow("r4"));
		assertEquals("internal row 0", 1, a.internalRowIndex(0));
		assertEquals("internal row 1", 2, a.internalRowIndex(1));
		assertEquals("internal row 2", 0, a.internalRowIndex(2));
		assertEquals("column count", 0, a.getColCount());
		assertEquals("row count", 3, a.getRowCount());
		assertEquals("non-zero count", 0, a.getNonZeroCount());
		assertEquals("column storage space", 2, a.getColStorageSize());
		assertEquals("row storage space", 4, a.getRowStorageSize());
		assertEquals("non-zero storage space", 4, a.getNonZeroStorageSize());
		
		// delete r1 & r2
		// then add 4 rows and see where they are added in internal arrays
		// r5 should end up in r2's old space
		// r6 should end up in r1's old space
		// r7 & r8 should be in the next consequtive spaces
		a.deleteRows(0, 1);
		a.createRows(0, 4, new String[]{"r5", "r6", "r7", "r8"}, null, null, null);
		assertEquals("r1", -1, a.indexOfRow("r1"));
		assertEquals("r2", -1, a.indexOfRow("r2"));
		assertEquals("r3", -1, a.indexOfRow("r3"));
		assertEquals("r4", 0, a.indexOfRow("r4"));
		assertEquals("r5", 1, a.indexOfRow("r5"));
		assertEquals("r6", 2, a.indexOfRow("r6"));
		assertEquals("r7", 3, a.indexOfRow("r7"));
		assertEquals("r8", 4, a.indexOfRow("r8"));
		assertEquals("internal row 0", 0, a.internalRowIndex(0));
		assertEquals("internal row 1", 2, a.internalRowIndex(1));
		assertEquals("internal row 2", 1, a.internalRowIndex(2));
		assertEquals("internal row 2", 3, a.internalRowIndex(3));
		assertEquals("internal row 2", 4, a.internalRowIndex(4));
		assertEquals("column count", 0, a.getColCount());
		assertEquals("row count", 5, a.getRowCount());
		assertEquals("non-zero count", 0, a.getNonZeroCount());
		assertEquals("column storage space", 2, a.getColStorageSize());
		assertEquals("row storage space", 6, a.getRowStorageSize());
		assertEquals("non-zero storage space", 4, a.getNonZeroStorageSize());
	}
	
	public void testSetNzero() {
		// create simple matrix
		MatrixA a = new MatrixA(2, 2, 2);
		a.createColumns(0, 2, new String[]{"c1", "c2"}, null, null, null, null);
		a.createRows(0, 2, new String[]{"r1", "r2"}, null, null, null);
		
		// assert matrix is empty
		assertEquals("non-zero count", 0, a.getNonZeroCount());
		assertEquals("non-zero storage", 2, a.getNonZeroStorageSize());
		assertEquals("(0,0)", 0d, a.getValue(0, 0), 0d);
		assertEquals("(0,1)", 0d, a.getValue(0, 1), 0d);
		assertEquals("(1,0)", 0d, a.getValue(1, 0), 0d);
		assertEquals("(1,1)", 0d, a.getValue(1, 1), 0d);
		
		// set first value in matrix
		a.setValue(0, 0, 1);
		assertEquals("non-zero count", 1, a.getNonZeroCount());
		assertEquals("non-zero storage", 2, a.getNonZeroStorageSize());
		assertEquals("(0,0)", 1d, a.getValue(0, 0), 0d);
		assertEquals("(0,1)", 0d, a.getValue(0, 1), 0d);
		assertEquals("(1,0)", 0d, a.getValue(1, 0), 0d);
		assertEquals("(1,1)", 0d, a.getValue(1, 1), 0d);
		
		// clear value and assert matrix is empty
		a.setValue(0, 0, 0);
		assertEquals("non-zero count", 0, a.getNonZeroCount());
		assertEquals("non-zero storage", 2, a.getNonZeroStorageSize());
		assertEquals("(0,0)", 0d, a.getValue(0, 0), 0d);
		assertEquals("(0,1)", 0d, a.getValue(0, 1), 0d);
		assertEquals("(1,0)", 0d, a.getValue(1, 0), 0d);
		assertEquals("(1,1)", 0d, a.getValue(1, 1), 0d);
		
		// set second value in matrix
		a.setValue(0, 1, 2);
		assertEquals("non-zero count", 1, a.getNonZeroCount());
		assertEquals("non-zero storage", 2, a.getNonZeroStorageSize());
		assertEquals("(0,0)", 0d, a.getValue(0, 0), 0d);
		assertEquals("(0,1)", 2d, a.getValue(0, 1), 0d);
		assertEquals("(1,0)", 0d, a.getValue(1, 0), 0d);
		assertEquals("(1,1)", 0d, a.getValue(1, 1), 0d);
		
		// clear value and assert matrix is empty
		a.setValue(0, 1, 0);
		assertEquals("non-zero count", 0, a.getNonZeroCount());
		assertEquals("non-zero storage", 2, a.getNonZeroStorageSize());
		assertEquals("(0,0)", 0d, a.getValue(0, 0), 0d);
		assertEquals("(0,1)", 0d, a.getValue(0, 1), 0d);
		assertEquals("(1,0)", 0d, a.getValue(1, 0), 0d);
		assertEquals("(1,1)", 0d, a.getValue(1, 1), 0d);
		
		// set third value in matrix
		a.setValue(1, 0, 3);
		assertEquals("non-zero count", 1, a.getNonZeroCount());
		assertEquals("non-zero storage", 2, a.getNonZeroStorageSize());
		assertEquals("(0,0)", 0d, a.getValue(0, 0), 0d);
		assertEquals("(0,1)", 0d, a.getValue(0, 1), 0d);
		assertEquals("(1,0)", 3d, a.getValue(1, 0), 0d);
		assertEquals("(1,1)", 0d, a.getValue(1, 1), 0d);
		
		// clear value and assert matrix is empty
		a.setValue(1, 0, 0);
		assertEquals("non-zero count", 0, a.getNonZeroCount());
		assertEquals("non-zero storage", 2, a.getNonZeroStorageSize());
		assertEquals("(0,0)", 0d, a.getValue(0, 0), 0d);
		assertEquals("(0,1)", 0d, a.getValue(0, 1), 0d);
		assertEquals("(1,0)", 0d, a.getValue(1, 0), 0d);
		assertEquals("(1,1)", 0d, a.getValue(1, 1), 0d);
		
		// set fourth value in matrix
		a.setValue(1, 1, 4);
		assertEquals("non-zero count", 1, a.getNonZeroCount());
		assertEquals("non-zero storage", 2, a.getNonZeroStorageSize());
		assertEquals("(0,0)", 0d, a.getValue(0, 0), 0d);
		assertEquals("(0,1)", 0d, a.getValue(0, 1), 0d);
		assertEquals("(1,0)", 0d, a.getValue(1, 0), 0d);
		assertEquals("(1,1)", 4d, a.getValue(1, 1), 0d);
		
		// clear value and assert matrix is empty
		a.setValue(1, 1, 0);
		assertEquals("non-zero count", 0, a.getNonZeroCount());
		assertEquals("non-zero storage", 2, a.getNonZeroStorageSize());
		assertEquals("(0,0)", 0d, a.getValue(0, 0), 0d);
		assertEquals("(0,1)", 0d, a.getValue(0, 1), 0d);
		assertEquals("(1,0)", 0d, a.getValue(1, 0), 0d);
		assertEquals("(1,1)", 0d, a.getValue(1, 1), 0d);
		
		// set first & second value in matrix
		a.setValue(0, 0, 1);
		a.setValue(0, 1, 2);
		assertEquals("non-zero count", 2, a.getNonZeroCount());
		assertEquals("non-zero storage", 2, a.getNonZeroStorageSize());
		assertEquals("(0,0)", 1d, a.getValue(0, 0), 0d);
		assertEquals("(0,1)", 2d, a.getValue(0, 1), 0d);
		assertEquals("(1,0)", 0d, a.getValue(1, 0), 0d);
		assertEquals("(1,1)", 0d, a.getValue(1, 1), 0d);
		
		// clear value and assert matrix is empty
		a.setValue(0, 0, 0);
		a.setValue(0, 1, 0);
		assertEquals("non-zero count", 0, a.getNonZeroCount());
		assertEquals("non-zero storage", 2, a.getNonZeroStorageSize());
		assertEquals("(0,0)", 0d, a.getValue(0, 0), 0d);
		assertEquals("(0,1)", 0d, a.getValue(0, 1), 0d);
		assertEquals("(1,0)", 0d, a.getValue(1, 0), 0d);
		assertEquals("(1,1)", 0d, a.getValue(1, 1), 0d);
		
		// set 2 & 3 value in matrix
		a.setValue(0, 1, 2);
		a.setValue(1, 0, 3);
		assertEquals("non-zero count", 2, a.getNonZeroCount());
		assertEquals("non-zero storage", 2, a.getNonZeroStorageSize());
		assertEquals("(0,0)", 0d, a.getValue(0, 0), 0d);
		assertEquals("(0,1)", 2d, a.getValue(0, 1), 0d);
		assertEquals("(1,0)", 3d, a.getValue(1, 0), 0d);
		assertEquals("(1,1)", 0d, a.getValue(1, 1), 0d);
		
		// clear value and assert matrix is empty
		a.setValue(0, 1, 0);
		a.setValue(1, 0, 0);
		assertEquals("non-zero count", 0, a.getNonZeroCount());
		assertEquals("non-zero storage", 2, a.getNonZeroStorageSize());
		assertEquals("(0,0)", 0d, a.getValue(0, 0), 0d);
		assertEquals("(0,1)", 0d, a.getValue(0, 1), 0d);
		assertEquals("(1,0)", 0d, a.getValue(1, 0), 0d);
		assertEquals("(1,1)", 0d, a.getValue(1, 1), 0d);
		
		// set 3 & 4 value in matrix
		a.setValue(1, 0, 3);
		a.setValue(1, 1, 4);
		assertEquals("non-zero count", 2, a.getNonZeroCount());
		assertEquals("non-zero storage", 2, a.getNonZeroStorageSize());
		assertEquals("(0,0)", 0d, a.getValue(0, 0), 0d);
		assertEquals("(0,1)", 0d, a.getValue(0, 1), 0d);
		assertEquals("(1,0)", 3d, a.getValue(1, 0), 0d);
		assertEquals("(1,1)", 4d, a.getValue(1, 1), 0d);
		
		// clear value and assert matrix is empty
		a.setValue(1, 0, 0);
		a.setValue(1, 1, 0);
		assertEquals("non-zero count", 0, a.getNonZeroCount());
		assertEquals("non-zero storage", 2, a.getNonZeroStorageSize());
		assertEquals("(0,0)", 0d, a.getValue(0, 0), 0d);
		assertEquals("(0,1)", 0d, a.getValue(0, 1), 0d);
		assertEquals("(1,0)", 0d, a.getValue(1, 0), 0d);
		assertEquals("(1,1)", 0d, a.getValue(1, 1), 0d);
		
		// set 1 & 3 value in matrix
		a.setValue(0, 0, 1);
		a.setValue(1, 0, 3);
		assertEquals("non-zero count", 2, a.getNonZeroCount());
		assertEquals("non-zero storage", 2, a.getNonZeroStorageSize());
		assertEquals("(0,0)", 1d, a.getValue(0, 0), 0d);
		assertEquals("(0,1)", 0d, a.getValue(0, 1), 0d);
		assertEquals("(1,0)", 3d, a.getValue(1, 0), 0d);
		assertEquals("(1,1)", 0d, a.getValue(1, 1), 0d);
		
		// clear value and assert matrix is empty
		a.setValue(0, 0, 0);
		a.setValue(1, 0, 0);
		assertEquals("non-zero count", 0, a.getNonZeroCount());
		assertEquals("non-zero storage", 2, a.getNonZeroStorageSize());
		assertEquals("(0,0)", 0d, a.getValue(0, 0), 0d);
		assertEquals("(0,1)", 0d, a.getValue(0, 1), 0d);
		assertEquals("(1,0)", 0d, a.getValue(1, 0), 0d);
		assertEquals("(1,1)", 0d, a.getValue(1, 1), 0d);
		
		// set 1 & 4 value in matrix
		a.setValue(0, 0, 1);
		a.setValue(1, 1, 4);
		assertEquals("non-zero count", 2, a.getNonZeroCount());
		assertEquals("non-zero storage", 2, a.getNonZeroStorageSize());
		assertEquals("(0,0)", 1d, a.getValue(0, 0), 0d);
		assertEquals("(0,1)", 0d, a.getValue(0, 1), 0d);
		assertEquals("(1,0)", 0d, a.getValue(1, 0), 0d);
		assertEquals("(1,1)", 4d, a.getValue(1, 1), 0d);
		
		// clear value and assert matrix is empty
		a.setValue(0, 0, 0);
		a.setValue(1, 1, 0);
		assertEquals("non-zero count", 0, a.getNonZeroCount());
		assertEquals("non-zero storage", 2, a.getNonZeroStorageSize());
		assertEquals("(0,0)", 0d, a.getValue(0, 0), 0d);
		assertEquals("(0,1)", 0d, a.getValue(0, 1), 0d);
		assertEquals("(1,0)", 0d, a.getValue(1, 0), 0d);
		assertEquals("(1,1)", 0d, a.getValue(1, 1), 0d);
		
		// set 2 & 4 value in matrix
		a.setValue(0, 1, 2);
		a.setValue(1, 1, 4);
		assertEquals("non-zero count", 2, a.getNonZeroCount());
		assertEquals("non-zero storage", 2, a.getNonZeroStorageSize());
		assertEquals("(0,0)", 0d, a.getValue(0, 0), 0d);
		assertEquals("(0,1)", 2d, a.getValue(0, 1), 0d);
		assertEquals("(1,0)", 0d, a.getValue(1, 0), 0d);
		assertEquals("(1,1)", 4d, a.getValue(1, 1), 0d);
		
		// clear value and assert matrix is empty
		a.setValue(0, 1, 0);
		a.setValue(1, 1, 0);
		assertEquals("non-zero count", 0, a.getNonZeroCount());
		assertEquals("non-zero storage", 2, a.getNonZeroStorageSize());
		assertEquals("(0,0)", 0d, a.getValue(0, 0), 0d);
		assertEquals("(0,1)", 0d, a.getValue(0, 1), 0d);
		assertEquals("(1,0)", 0d, a.getValue(1, 0), 0d);
		assertEquals("(1,1)", 0d, a.getValue(1, 1), 0d);
		
		// set zero on cell that is already zero
		a.setValue(0, 0, 0);
		a.setValue(0, 1, 0);
		a.setValue(1, 0, 0);
		a.setValue(1, 1, 0);
		assertEquals("non-zero count", 0, a.getNonZeroCount());
		assertEquals("non-zero storage", 2, a.getNonZeroStorageSize());
		assertEquals("(0,0)", 0d, a.getValue(0, 0), 0d);
		assertEquals("(0,1)", 0d, a.getValue(0, 1), 0d);
		assertEquals("(1,0)", 0d, a.getValue(1, 0), 0d);
		assertEquals("(1,1)", 0d, a.getValue(1, 1), 0d);
	}
	
	public void testNzeroGrowth() {
		// create simple 2x2 matrix
		MatrixA a = new MatrixA(2, 2, 2);
		a.createColumns(0, 2, new String[]{"c1", "c2"}, null, null, null, null);
		a.createRows(0, 2, new String[]{"r1", "r2"}, null, null, null);
		
		// set 2 values on matrix
		a.setValue(0, 0, 1d);
		a.setValue(0, 1, 2d);
		assertEquals("non-zero count", 2, a.getNonZeroCount());
		assertEquals("non-zero storage", 2, a.getNonZeroStorageSize());
		assertEquals("(0,0)", 1d, a.getValue(0, 0), 0d);
		assertEquals("(0,1)", 2d, a.getValue(0, 1), 0d);
		assertEquals("(1,0)", 0d, a.getValue(1, 0), 0d);
		assertEquals("(1,1)", 0d, a.getValue(1, 1), 0d);
		
		// grow matrix by adding 1 valud
		a.setValue(1, 0, 3d);
		assertEquals("non-zero count", 3, a.getNonZeroCount());
		assertEquals("non-zero storage", 4, a.getNonZeroStorageSize());
		assertEquals("(0,0)", 1d, a.getValue(0, 0), 0d);
		assertEquals("(0,1)", 2d, a.getValue(0, 1), 0d);
		assertEquals("(1,0)", 3d, a.getValue(1, 0), 0d);
		assertEquals("(1,1)", 0d, a.getValue(1, 1), 0d);
		
		// matrix should not grow again
		a.setValue(1, 1, 4d);
		assertEquals("non-zero count", 4, a.getNonZeroCount());
		assertEquals("non-zero storage", 4, a.getNonZeroStorageSize());
		assertEquals("(0,0)", 1d, a.getValue(0, 0), 0d);
		assertEquals("(0,1)", 2d, a.getValue(0, 1), 0d);
		assertEquals("(1,0)", 3d, a.getValue(1, 0), 0d);
		assertEquals("(1,1)", 4d, a.getValue(1, 1), 0d);
		
		// matrix nzero storage should remain same size with each removal
		a.setValue(0, 0, 0d);
		assertEquals("non-zero count", 3, a.getNonZeroCount());
		assertEquals("non-zero storage", 4, a.getNonZeroStorageSize());
		assertEquals("(0,0)", 0d, a.getValue(0, 0), 0d);
		assertEquals("(0,1)", 2d, a.getValue(0, 1), 0d);
		assertEquals("(1,0)", 3d, a.getValue(1, 0), 0d);
		assertEquals("(1,1)", 4d, a.getValue(1, 1), 0d);
		
		// matrix nzero storage should remain same size with each removal
		a.setValue(1, 0, 0d);
		assertEquals("non-zero count", 2, a.getNonZeroCount());
		assertEquals("non-zero storage", 4, a.getNonZeroStorageSize());
		assertEquals("(0,0)", 0d, a.getValue(0, 0), 0d);
		assertEquals("(0,1)", 2d, a.getValue(0, 1), 0d);
		assertEquals("(1,0)", 0d, a.getValue(1, 0), 0d);
		assertEquals("(1,1)", 4d, a.getValue(1, 1), 0d);
		
		// matrix nzero storage should remain same size with each removal
		a.setValue(0, 1, 0d);
		assertEquals("non-zero count", 1, a.getNonZeroCount());
		assertEquals("non-zero storage", 4, a.getNonZeroStorageSize());
		assertEquals("(0,0)", 0d, a.getValue(0, 0), 0d);
		assertEquals("(0,1)", 0d, a.getValue(0, 1), 0d);
		assertEquals("(1,0)", 0d, a.getValue(1, 0), 0d);
		assertEquals("(1,1)", 4d, a.getValue(1, 1), 0d);
		
		// matrix nzero storage should remain same size with each removal
		a.setValue(1, 1, 0d);
		assertEquals("non-zero count", 0, a.getNonZeroCount());
		assertEquals("non-zero storage", 4, a.getNonZeroStorageSize());
		assertEquals("(0,0)", 0d, a.getValue(0, 0), 0d);
		assertEquals("(0,1)", 0d, a.getValue(0, 1), 0d);
		assertEquals("(1,0)", 0d, a.getValue(1, 0), 0d);
		assertEquals("(1,1)", 0d, a.getValue(1, 1), 0d);
	}
	
	public void testCreateRowWithData() {
		// create simple 2x2 matrix
		MatrixA a = new MatrixA(2, 2, 2);
		a.createColumns(2, new String[]{"c1", "c2"}, null, null, null, null);
		a.createRows(2, new String[]{"r1", "r2"}, null, null, null, 4, new int[]{0,2}, new int[]{0,1,0,1}, new double[]{1,3,2,4});
		assertEquals("non-zero count", 4, a.getNonZeroCount());
		assertEquals("non-zero storage", 4, a.getNonZeroStorageSize());
		assertEquals("(0,0)", 1d, a.getValue(0, 0), 0d);
		assertEquals("(0,1)", 2d, a.getValue(0, 1), 0d);
		assertEquals("(1,0)", 3d, a.getValue(1, 0), 0d);
		assertEquals("(1,1)", 4d, a.getValue(1, 1), 0d);
	}
	
	public void testCreateColWithData() {
		// create simple 2x2 matrix
		MatrixA a = new MatrixA(2, 2, 2);
		a.createRows(2, new String[]{"r1", "r2"}, null, null, null);
		a.createColumns(2, new String[]{"c1", "c2"}, null, null, null, null, 4, new int[]{0,2}, new int[]{0,1,0,1}, new double[]{1,2,3,4});
		assertEquals("non-zero count", 4, a.getNonZeroCount());
		assertEquals("non-zero storage", 4, a.getNonZeroStorageSize());
		assertEquals("(0,0)", 1d, a.getValue(0, 0), 0d);
		assertEquals("(0,1)", 2d, a.getValue(0, 1), 0d);
		assertEquals("(1,0)", 3d, a.getValue(1, 0), 0d);
		assertEquals("(1,1)", 4d, a.getValue(1, 1), 0d);
	}
	
	public void testDeleteColWithData() {
		// create simple 2x2 matrix
		MatrixA a = new MatrixA(2, 2, 2);
		a.createRows(2, new String[]{"r1", "r2"}, null, null, null);
		a.createColumns(2, new String[]{"c1", "c2"}, null, null, null, null, 4, new int[]{0,2}, new int[]{0,1,0,1}, new double[]{1,2,3,4});
		assertEquals("non-zero count", 4, a.getNonZeroCount());
		assertEquals("non-zero storage", 4, a.getNonZeroStorageSize());
		assertEquals("(0,0)", 1d, a.getValue(0, 0), 0d);
		assertEquals("(0,1)", 2d, a.getValue(0, 1), 0d);
		assertEquals("(1,0)", 3d, a.getValue(1, 0), 0d);
		assertEquals("(1,1)", 4d, a.getValue(1, 1), 0d);
		assertEquals("c1", 0, a.indexOfColumn("c1"));
		assertEquals("c2", 1, a.indexOfColumn("c2"));
		assertEquals("r1", 0, a.indexOfRow("r1"));
		assertEquals("r2", 1, a.indexOfRow("r2"));
		assertEquals("column count", 2, a.getColCount());
		assertEquals("row count", 2, a.getRowCount());
		assertEquals("column storage space", 2, a.getColStorageSize());
		assertEquals("row storage space", 2, a.getRowStorageSize());
		
		// delete column c2
		a.deleteColumns(1, 1);
		assertEquals("non-zero count", 2, a.getNonZeroCount());
		assertEquals("non-zero storage", 4, a.getNonZeroStorageSize());
		assertEquals("(0,0)", 1d, a.getValue(0, 0), 0d);
		assertEquals("(0,1)", 2d, a.getValue(0, 1), 0d);
		assertEquals("c1", 0, a.indexOfColumn("c1"));
		assertEquals("c2", -1, a.indexOfColumn("c2"));
		assertEquals("r1", 0, a.indexOfRow("r1"));
		assertEquals("r2", 1, a.indexOfRow("r2"));
		assertEquals("column count", 1, a.getColCount());
		assertEquals("row count", 2, a.getRowCount());
		assertEquals("column storage space", 2, a.getColStorageSize());
		assertEquals("row storage space", 2, a.getRowStorageSize());
		
		// delete column c1
		a.deleteColumns(0, 0);
		assertEquals("non-zero count", 0, a.getNonZeroCount());
		assertEquals("non-zero storage", 4, a.getNonZeroStorageSize());
		assertEquals("c1", -1, a.indexOfColumn("c1"));
		assertEquals("c2", -1, a.indexOfColumn("c2"));
		assertEquals("r1", 0, a.indexOfRow("r1"));
		assertEquals("r2", 1, a.indexOfRow("r2"));
		assertEquals("column count", 0, a.getColCount());
		assertEquals("row count", 2, a.getRowCount());
		assertEquals("column storage space", 2, a.getColStorageSize());
		assertEquals("row storage space", 2, a.getRowStorageSize());

		// create simple 2x2 matrix
		a = new MatrixA(2, 2, 2);
		a.createRows(2, new String[]{"r1", "r2"}, null, null, null);
		a.createColumns(2, new String[]{"c1", "c2"}, null, null, null, null, 4, new int[]{0,2}, new int[]{0,1,0,1}, new double[]{1,2,3,4});
		assertEquals("non-zero count", 4, a.getNonZeroCount());
		assertEquals("non-zero storage", 4, a.getNonZeroStorageSize());
		assertEquals("(0,0)", 1d, a.getValue(0, 0), 0d);
		assertEquals("(0,1)", 2d, a.getValue(0, 1), 0d);
		assertEquals("(1,0)", 3d, a.getValue(1, 0), 0d);
		assertEquals("(1,1)", 4d, a.getValue(1, 1), 0d);
		assertEquals("c1", 0, a.indexOfColumn("c1"));
		assertEquals("c2", 1, a.indexOfColumn("c2"));
		assertEquals("r1", 0, a.indexOfRow("r1"));
		assertEquals("r2", 1, a.indexOfRow("r2"));
		assertEquals("column count", 2, a.getColCount());
		assertEquals("row count", 2, a.getRowCount());
		assertEquals("column storage space", 2, a.getColStorageSize());
		assertEquals("row storage space", 2, a.getRowStorageSize());
		
		// delete column c1
		a.deleteColumns(0, 0);
		assertEquals("non-zero count", 2, a.getNonZeroCount());
		assertEquals("non-zero storage", 4, a.getNonZeroStorageSize());
		assertEquals("(0,0)", 3d, a.getValue(0, 0), 0d);
		assertEquals("(0,1)", 4d, a.getValue(0, 1), 0d);
		assertEquals("c1", -1, a.indexOfColumn("c1"));
		assertEquals("c2", 0, a.indexOfColumn("c2"));
		assertEquals("r1", 0, a.indexOfRow("r1"));
		assertEquals("r2", 1, a.indexOfRow("r2"));
		assertEquals("column count", 1, a.getColCount());
		assertEquals("row count", 2, a.getRowCount());
		assertEquals("column storage space", 2, a.getColStorageSize());
		assertEquals("row storage space", 2, a.getRowStorageSize());
		
		// delete column c2
		a.deleteColumns(0, 0);
		assertEquals("non-zero count", 0, a.getNonZeroCount());
		assertEquals("non-zero storage", 4, a.getNonZeroStorageSize());
		assertEquals("c1", -1, a.indexOfColumn("c1"));
		assertEquals("c2", -1, a.indexOfColumn("c2"));
		assertEquals("r1", 0, a.indexOfRow("r1"));
		assertEquals("r2", 1, a.indexOfRow("r2"));
		assertEquals("column count", 0, a.getColCount());
		assertEquals("row count", 2, a.getRowCount());
		assertEquals("column storage space", 2, a.getColStorageSize());
		assertEquals("row storage space", 2, a.getRowStorageSize());
	}
	
	public void testDeleteRowWithData() {
		// create simple 2x2 matrix
		MatrixA a = new MatrixA(2, 2, 2);
		a.createColumns(2, new String[]{"c1", "c2"}, null, null, null, null);
		a.createRows(2, new String[]{"r1", "r2"}, null, null, null, 4, new int[]{0,2}, new int[]{0,1,0,1}, new double[]{1,3,2,4});
		assertEquals("non-zero count", 4, a.getNonZeroCount());
		assertEquals("non-zero storage", 4, a.getNonZeroStorageSize());
		assertEquals("(0,0)", 1d, a.getValue(0, 0), 0d);
		assertEquals("(0,1)", 2d, a.getValue(0, 1), 0d);
		assertEquals("(1,0)", 3d, a.getValue(1, 0), 0d);
		assertEquals("(1,1)", 4d, a.getValue(1, 1), 0d);
		assertEquals("c1", 0, a.indexOfColumn("c1"));
		assertEquals("c2", 1, a.indexOfColumn("c2"));
		assertEquals("r1", 0, a.indexOfRow("r1"));
		assertEquals("r2", 1, a.indexOfRow("r2"));
		assertEquals("column count", 2, a.getColCount());
		assertEquals("row count", 2, a.getRowCount());
		assertEquals("column storage space", 2, a.getColStorageSize());
		assertEquals("row storage space", 2, a.getRowStorageSize());
		
		// delete row r2
		a.deleteRows(1, 1);
		assertEquals("non-zero count", 2, a.getNonZeroCount());
		assertEquals("non-zero storage", 4, a.getNonZeroStorageSize());
		assertEquals("(0,0)", 1d, a.getValue(0, 0), 0d);
		assertEquals("(1,0)", 3d, a.getValue(1, 0), 0d);
		assertEquals("c1", 0, a.indexOfColumn("c1"));
		assertEquals("c2", 1, a.indexOfColumn("c2"));
		assertEquals("r1", 0, a.indexOfRow("r1"));
		assertEquals("r2", -1, a.indexOfRow("r2"));
		assertEquals("column count", 2, a.getColCount());
		assertEquals("row count", 1, a.getRowCount());
		assertEquals("column storage space", 2, a.getColStorageSize());
		assertEquals("row storage space", 2, a.getRowStorageSize());
		
		// delete row r1
		a.deleteRows(0, 0);
		assertEquals("non-zero count", 0, a.getNonZeroCount());
		assertEquals("non-zero storage", 4, a.getNonZeroStorageSize());
		assertEquals("c1", 0, a.indexOfColumn("c1"));
		assertEquals("c2", 1, a.indexOfColumn("c2"));
		assertEquals("r1", -1, a.indexOfRow("r1"));
		assertEquals("r2", -1, a.indexOfRow("r2"));
		assertEquals("column count", 2, a.getColCount());
		assertEquals("row count", 0, a.getRowCount());
		assertEquals("column storage space", 2, a.getColStorageSize());
		assertEquals("row storage space", 2, a.getRowStorageSize());
		
		// create simple 2x2 matrix
		a = new MatrixA(2, 2, 2);
		a.createColumns(2, new String[]{"c1", "c2"}, null, null, null, null);
		a.createRows(2, new String[]{"r1", "r2"}, null, null, null, 4, new int[]{0,2}, new int[]{0,1,0,1}, new double[]{1,3,2,4});
		assertEquals("non-zero count", 4, a.getNonZeroCount());
		assertEquals("non-zero storage", 4, a.getNonZeroStorageSize());
		assertEquals("(0,0)", 1d, a.getValue(0, 0), 0d);
		assertEquals("(0,1)", 2d, a.getValue(0, 1), 0d);
		assertEquals("(1,0)", 3d, a.getValue(1, 0), 0d);
		assertEquals("(1,1)", 4d, a.getValue(1, 1), 0d);
		assertEquals("c1", 0, a.indexOfColumn("c1"));
		assertEquals("c2", 1, a.indexOfColumn("c2"));
		assertEquals("r1", 0, a.indexOfRow("r1"));
		assertEquals("r2", 1, a.indexOfRow("r2"));
		assertEquals("column count", 2, a.getColCount());
		assertEquals("row count", 2, a.getRowCount());
		assertEquals("column storage space", 2, a.getColStorageSize());
		assertEquals("row storage space", 2, a.getRowStorageSize());
		
		// delete row r1
		a.deleteRows(0, 0);
		assertEquals("non-zero count", 2, a.getNonZeroCount());
		assertEquals("non-zero storage", 4, a.getNonZeroStorageSize());
		assertEquals("(0,0)", 2d, a.getValue(0, 0), 0d);
		assertEquals("(1,0)", 4d, a.getValue(1, 0), 0d);
		assertEquals("c1", 0, a.indexOfColumn("c1"));
		assertEquals("c2", 1, a.indexOfColumn("c2"));
		assertEquals("r1", -1, a.indexOfRow("r1"));
		assertEquals("r2", 0, a.indexOfRow("r2"));
		assertEquals("column count", 2, a.getColCount());
		assertEquals("row count", 1, a.getRowCount());
		assertEquals("column storage space", 2, a.getColStorageSize());
		assertEquals("row storage space", 2, a.getRowStorageSize());
		
		// delete row r2
		a.deleteRows(0, 0);
		assertEquals("non-zero count", 0, a.getNonZeroCount());
		assertEquals("non-zero storage", 4, a.getNonZeroStorageSize());
		assertEquals("c1", 0, a.indexOfColumn("c1"));
		assertEquals("c2", 1, a.indexOfColumn("c2"));
		assertEquals("r1", -1, a.indexOfRow("r1"));
		assertEquals("r2", -1, a.indexOfRow("r2"));
		assertEquals("column count", 2, a.getColCount());
		assertEquals("row count", 0, a.getRowCount());
		assertEquals("column storage space", 2, a.getColStorageSize());
		assertEquals("row storage space", 2, a.getRowStorageSize());
	}
}