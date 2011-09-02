package jopt.csp.example.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import jopt.csp.CspSolver;
import jopt.csp.example.api.util.SudokuBoard;
import jopt.csp.spi.arcalgorithm.variable.IntExpr;
import jopt.csp.variable.CspIntVariable;
import jopt.csp.variable.CspMath;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;

/**
 * Uses the CSP solver API to provide unique solutions to Sudoku puzzle problems.
 * It solves a puzzles initialized values. This example uses more 
 * complex constraints than StartHere.java to solve the puzzle. 
 * 
 * A user can specify the size of the puzzle. The default is 9x9 cell sudoku. 
 * The user is allowed to input the intial values using spaces to delimit cells.
 * 
 * @author Kevin Formsma
 */
public class Sudoku {
	
	//Class to represent a sudoku puzzle board
	private SudokuBoard board;
      
    //CSP solver objects
    private CspSolver solver;
    
    //Determine what constraints to use
    private boolean useGlobalCardinality;
    
    /**
     * Default Constructor for Sudoku Class
     * Creates initial object and solver
     * 
     */
    public Sudoku(boolean useAllDiffConstraint){
    	//Initialize solvers without autoPropagation
        init(true);   
        
        //Set if we want to use complex constraints
        //If false neq constraints are used
        useGlobalCardinality = useAllDiffConstraint;
    }
    /**
     * Initialize the solver objects
     * 
     * @param autoPropagate		boolean to set solver's autoPropagate property
     */
    private void init(boolean autoPropagate)
    {
    	//Generate the solver
    	solver = CspSolver.createSolver();
    	solver.setAutoPropagate(autoPropagate);   	
    }
    /**
     * Setup the puzzle board
     * 
     * @param boardSize		size of the board
     */
    private void initializePuzzle(int boardSize) {
    
    	//Retrieve the varFactory to create our ints to represent the puzzle
    	CspVariableFactory varFactory = solver.getVarFactory();
    	
    	//Create board of specified size
    	board = new SudokuBoard(boardSize,varFactory);
    	
    }

    /**
     * Setup the constraints for the solver
     * that match sudoku rules
     */
	private void setupConstraints() {
		try{
			
			//Row & Column Constraints - All numbers in a row/column must be unique
			//See allDiff() for how it adds the constraints
			for (int i=0; i<board.getNumSquares()*board.getNumSquares(); i++) {
				//Get Row and add constraint
				CspIntVariable[] row = board.getRow(i);
				allDiff(row);
				
				//Get Column and add constraint
				CspIntVariable[] col = board.getCol(i);
				allDiff(col);
			}
			
			//Square Constraints - All numbers in a sudoku Square must be unique
			//See allDiff() for how it adds the constraints
			for(int i=0;i<board.getNumSquares();i++)
			{
				for(int j=0;j<board.getNumSquares();j++)
				{
					allDiff(board.getSquare(i, j));
				}
			}	
		} catch (PropagationFailureException e) {
			System.out.println("Error setting up constraints:"+e.getMessage());
			e.printStackTrace();
		}
	}
	/**
	 * This method adds constraints to the solver that all CspIntVariables
	 * in the specified array must be unique.
	 * 
	 * @param set	CspIntVariable array
	 * @throws PropagationFailureException		Constraint conflicts with inputs
	 * 											or other constraints
	 */
	private void allDiff(CspIntVariable[] set) throws PropagationFailureException {
		if(!useGlobalCardinality)
		{
			for(int i=0;i<set.length;i++)
			{
				for(int j=i;j<set.length;j++)
				{
					if(j != i)
					{
						solver.addConstraint(set[i].neq(set[j]));
					}
				}
			}
		}
		else
		{
			//Get the CspMath object to use complex constraints
			CspMath math = solver.getVarFactory().getMath();
			//Change to IntExpr type for the CspMath operation
			IntExpr[] setExp = new IntExpr[set.length];
	        for (int i=0; i<set.length; i++) {
	            setExp[i] = (IntExpr)set[i];
	        }
	        //Add allDifferent constraint
			solver.addConstraint(math.allDifferent(setExp));
		}

	}
	
	/**
	 * Adds the user specified default puzzle values to the board
	 * 
	 */
	private void setupInitValues()
	{
		//Number of rows to read & cells per row
		int numRows = board.getNumSquares()*board.getNumSquares();
		int numCells = numRows;
		//Input stream
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		//For each row
		for(int i=0; i<numRows; i++)
		{
			//Read row values
			System.out.print("Row "+(i+1)+":");
			String currentRow = "";
			try {
				currentRow = input.readLine();
			} catch (IOException e) {
				System.out.println("Error reading input line...");
				e.printStackTrace();
			}
			String[] cells = currentRow.split(" ");
			//Check that we have enough input
			if(cells.length < numCells)
			{
				System.out.println("Not enough input...retry");
				i--;
				continue;
			}
			//For each cell in the row
			for(int c = 0;c<numCells;c++)
			{
				//If input wasn't x, set the value
				if(!cells[c].equals("x"))
				{
					try{
						board.setValue(i,c, Integer.valueOf(cells[c]).intValue());
					}
					catch(PropagationFailureException exp)
					{
						System.out.println("Failed to setup intial values");
						exp.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * Tells the CSP solver to propagate the constraints
	 * and find all solutions to the puzzle
	 * 
	 * @return		true if puzzle has solutions
	 */
	private boolean solvePuzzle()
	{
		//Propagate the constraints manually if we aren't doing so automatically
		if(!solver.getAutoPropagate())
			solver.propagate();

		//Solve the solution for the cells that have not been bounded based
		// on the constraints and initial values
        boolean solved = solver.solve(board.getUnboundedCells(), false);
		
        //If we have a solution(s) print them out!
		if (solved)
		{
			board.dumpVerbose();
			while (solver.nextSolution())
				board.dumpVerbose();
             
			System.out.println("All solutions were found");
			return true;
		}
		else
		{
			System.out.println("No solution was found!");
			return false;
		}
	}
	
	/**
     * Method that gets the sudoku solver started
     * 
     * @param args
     */
    public static void main(String args[]) throws Exception {
        
    	//Change argument to true to enable 
    	//global cardinality constraints instead of simple neq constraints
        Sudoku sdk = new Sudoku(false);
        
        //Prompt for puzzle square size
        System.out.print("Enter number of sudoku cells per row(default is 9):");
        //Read the square size, then take the square root and round to find good value
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        String line = input.readLine();
        double sqrt = 0;
        try{
        	sqrt = Math.sqrt(Integer.valueOf(line).intValue());
        } catch(NumberFormatException nfe)//Invalid input
        {
        	System.out.println("Invalid input...uses default size of 9x9 puzzle");
        }
    
        int	size = (int)Math.floor(sqrt);
        if(size==0)
        	size=3;
        //Setup the board
        sdk.initializePuzzle(size);
        
        //Add the constraints to the solver
        sdk.setupConstraints();
        
        //Print instructions for entering default values
        System.out.println("Enter each row of the puzzle to solve.");
        System.out.println("Example row 9x9 cell sudoku('x' represents no value,space to delimit cells):");
        System.out.println("x x 4 x x 3 x x 2");
         
        //Initialize puzzle values
        sdk.setupInitValues();
        
                
        //Solve the puzzle automatically
        sdk.solvePuzzle();
    }
}