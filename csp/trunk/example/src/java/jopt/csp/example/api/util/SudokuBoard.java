package jopt.csp.example.api.util;

import java.util.ArrayList;

import jopt.csp.variable.CspIntVariable;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;

/**
 * This Class holds the variables of a sudoku puzzle board
 * It supports variable size sudoku boards and is intended for use
 * with the JOpt CSP solver API. See Sudoku.java for a CSP implementation
 * for solving sudoku problems. 
 * 
 * @author Kevin Formsma
 *
 */
public class SudokuBoard {
	
	private CspIntVariable cells[][];
	private int numSquares;
	/**
	 * Default constructor for SudokuBoard. Creates board of
	 * 3x3 sudoku squares.
	 * 
	 * @param varFactory		A CspVariableFactory object to create
	 * 							the CspIntVariables
	 */
	public SudokuBoard(CspVariableFactory varFactory)
	{
		numSquares = 3;
		//Create an double array of size 9x9 to hold the default
		// 3 square SudokuBoard
		cells = new CspIntVariable[this.numSquares*this.numSquares][];
		for(int i =0; i<9;i++)
		{
			cells[i] = new CspIntVariable[this.numSquares*this.numSquares];
		}
		//Setup the domain of possible values for each cell
		intializeDomains(varFactory);
	}

	/**
	 * Constructor to create variable size sudoku puzzle boards.
	 * 
	 * @param numSquares		Number of 3x3 cell sudoku squares.
	 * @param varFactory		A CspVariableFactory object to create
	 * 							the CspIntVariables 						
	 */
	public SudokuBoard(int numSquares,CspVariableFactory varFactory)
	{
		this.numSquares = numSquares;
		
		//Create a double array of specified size to hold
		// the SudokuBoard
		cells = new CspIntVariable[this.numSquares*this.numSquares][];
		for(int i =0; i<this.numSquares*this.numSquares;i++)
		{
			cells[i] = new CspIntVariable[this.numSquares*this.numSquares];
		}
		
		//Setup the domain of possible values for each cell
		intializeDomains(varFactory);
	}
	/**
	 * Initialize the domain size of each cell
	 * in the SudokuBoard
	 * 
	 * @param varFactory		A CspVariableFactory object to create
	 * 							the CspIntVariables 
	 */
	private void intializeDomains(CspVariableFactory varFactory) {
       
		for (int i=0; i<numSquares*numSquares; i++)
            for (int j=0; j<numSquares*numSquares; j++)
            {
                cells[i][j] = varFactory.intVar("cell [" + i + ", " + j + "]", 1, numSquares*numSquares);
            }
	}
	/**
	 * Get the square size of the board
	 * 
	 * @return		number of sudoku squares in board
	 */
	public int getNumSquares()
	{
		return numSquares;
	}
	/**
	 * Set a individual sudoku cell value
	 * 
	 * @param row		Row index of cell
	 * @param col		Column index of cell
	 * @param val		New cell value
	 * @throws PropagationFailureException	Failed to update value
	 * 										due to a constraint propagation
	 */
	public void setValue(int row,int col,int val) throws PropagationFailureException
	{
		cells[row][col].setValue(val);
	}
	/**
	 * Get the CspIntVariable in a specific cell
	 * 
	 * @param row		Row index of cell
	 * @param col		Column index of cell
	 * @return			A CspIntVariable representing the cell data
	 */
	public CspIntVariable getValue(int row,int col)
	{
		return cells[row][col];
	}
	
	/**
	 * Return a CspIntVariable array representing a row
	 * 
	 * @param row		Row index to return
	 * @return			The CspIntVariable array of the row
	 */
	public CspIntVariable[] getRow(int row)
	{
		return cells[row];
	}
	/**
	 * Return a CspIntVariable array representing a column
	 * 
	 * @param col		Column index to return
	 * @return			The CspIntVariable array of the column
	 */
	public CspIntVariable[] getCol(int col)
	{
		CspIntVariable[] column = new CspIntVariable[numSquares*numSquares];
		//Get the cell in each row at specified column
		for(int i=0; i<numSquares*numSquares; i++)
		{
			column[i] = cells[i][col];
		}
		return column;
	}
	/**
	 * Returns the cells in a given sudoku square. The square is specified
	 * using its grid coordinates. Cells in array list are returned order by row
	 * 
	 * @param rowIndex		row index of Square
	 * @param colIndex		column index of Square
	 * @return				CspIntVariable array of the cells
	 * 						in specified square
	 */
	public CspIntVariable[] getSquare(int rowIndex, int colIndex)
	{
		CspIntVariable[] square = new CspIntVariable[numSquares*numSquares];
		//For each of the rows in square
		for(int j=0;j<numSquares;j++)
		{
			//Get the cells and add them to the array
			for(int i=0; i<numSquares;i++)
			{
				square[j*numSquares+i] = cells[rowIndex*numSquares+j][colIndex*numSquares+i];
			}
		}
		return square;
	}
	/**
	 * This function gets all cells in the puzzle whose domain
	 * has not been restricted to a single value
	 * 
	 * @return	CspIntVariable array of unbounded cells
	 */
	public CspIntVariable[] getUnboundedCells()
	{
		ArrayList unbound = new ArrayList();
		for (int i=0; i<numSquares*numSquares; i++) 
		{
			for (int j=0; j<numSquares*numSquares; j++)
			{
				if (!cells[i][j].isBound())
				{
					unbound.add(cells[i][j]);    
				}
			}
		}
        CspIntVariable[] unboundedCells = (CspIntVariable[]) unbound.toArray(new CspIntVariable[unbound.size()]);
        return unboundedCells;
	}
	
    /**
     * Prints out via System.out the status of all cells
     * in the puzzle
     * 
     * @param cells		A double array of CspIntVariables
     */
    public void dumpVerbose() {
       
    	for (int i=0; i<numSquares*numSquares; i++) {
            for (int j=0; j<numSquares*numSquares; j++) {
                System.out.print(cells[i][j].getMin());
                System.out.print(" ");
            }
            System.out.println();
        }      
        System.out.println();
    }

}
