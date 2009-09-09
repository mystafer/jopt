package jopt.csp.example.api;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

import jopt.csp.CspSolver;
import jopt.csp.example.api.util.SudokuBoard;
import jopt.csp.solution.SolutionScope;
import jopt.csp.solution.SolverSolution;
import jopt.csp.variable.CspIntVariable;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;

/**
 * Uses the CSP solver API to provide unique solutions to Sudoku puzzle problems.
 * This example has a GUI interface which allows the user to provide the puzzle and
 * choose if the solver should solve it automatically or if they will attempt to solve 
 * the puzzle manually.
 * This example uses the same logic as Sudoku.java and the supporting SudokuBoard.java 
 * but with a GUI interface.
 * 
 * This example makes use of the solution storing features of the CSP solver API
 * to allow puzzle resets and to display the solution when solved automatically.
 * 
 * @author Kevin Formsma
 */
public class SudokuGUI implements ActionListener {
	
	//GUI Components
    static JComponent combos[][] = new JComponent[9][9];
    static JFrame frame = new JFrame("Sudoku");
    static JPanel panel = new JPanel();
	
	//Class to represent a sudoku puzzle board
	private SudokuBoard board;
      
    //CSP solver objects
    private CspSolver solver;
    
    //CSP state/Solution solving objects
    //A Scope defines what variables to store
    //SolverSolutions actually store the backups
    private SolutionScope scope;
    private SolverSolution initialProblem;
    private SolverSolution solution;
    
    /**
     * Default Constructor for Sudoku Class
     * Setup the solver objects
     * 
     */
    public SudokuGUI(){
    	//Initialize solvers with autoPropagation
        init(true);   
    }
    /**
     * Initialize the solver and solution storing objects
     * 
     * @param autoPropagate		boolean to set solver's autoPropagate property
     */
    private void init(boolean autoPropagate)
    {
    	//Generate the solver
    	solver = CspSolver.createSolver();
    	solver.setAutoPropagate(autoPropagate);
    	
    	//These are used to save states of the variables in the solver
    	scope = new SolutionScope();
    	initialProblem = new SolverSolution();
    	solution = new SolverSolution();
    }
    /**
     * Setup the SudokuBoard object which represents the 
     * sudoku puzzle cells
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
			for (int i=0; i<3*board.getNumSquares(); i++) {
				//Get Row and add constraint
				CspIntVariable[] row = board.getRow(i);
				allDiff(row);
				
				//We are also going to add variables to scope to record solutions/restore the problem
				scope.add(row);
				
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
		//For each element in set
		for(int i=0;i<set.length;i++)
		{
			//Make it not equal the other elements
			for(int j=i;j<set.length;j++)
			{
				//Don't add the constraint to itself
				if(j != i)
				{
					solver.addConstraint(set[i].neq(set[j]));
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
		//on the constraints and initial values
        boolean solved = solver.solve(board.getUnboundedCells(), false);
		
        //If we have a solution(s) print them out!
		if (solved)
		{
			//Store the first solution to display
			solution = solver.storeSolution(scope);
			board.dumpVerbose();
			while (solver.nextSolution())
				board.dumpVerbose();
             
			System.out.println("All solutions were found");
			//Restore the state of first solution to the display
			try {
				solver.restoreSolution(solution);
			} catch (PropagationFailureException e) {
				
				e.printStackTrace();
			}
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
        
        SudokuGUI sdk = new SudokuGUI();
        //Setup the board with hard-coded values
        sdk.initializePuzzle(3);
        
        //Add the constraints to the solver
        sdk.setupConstraints();
        
        //Start the GUI Version
        sdk.createAndShowGUI(true);

    }
    
    /**
     * This method is the listener for interface events on the GUI
     * It responds to button clicks and number selections
     * 
     */
    public void actionPerformed(ActionEvent e) {
    	//If a number was chosen lets update the puzzle
    	if(e.getSource() instanceof JComboBox )
    	{
    		JComboBox cb = (JComboBox)e.getSource();
    		String number = (String)cb.getSelectedItem();
    		//If they selected the blank 'number' do nothing
    		if (number.equals(" "))
    		{
    			return;
    		}
    		for (int i=0; i<combos.length;i++)
    		{
    			for (int j=0; j<combos[i].length;j++)
    			{
    				//If this is the updated combo box
    				if (combos[i][j]==cb)
    				{
    					//Try to set the value
    					try
    					{
    						//Store a backup incase things go bad
    						SolverSolution backup = solver.storeSolution(scope);
    						try
    						{
    							//set the value
    							board.setValue(i, j, Integer.parseInt(number));
    						}
    						catch(PropagationFailureException pfe)
    						{
    							//restore backup in event of an error
    							JOptionPane.showMessageDialog(null, "The last selection led to an invalid puzzle state", "Failure", JOptionPane.ERROR_MESSAGE);
    							solver.restoreSolution(backup,true);
    							try
    							{
    								//Remove that possible value from the domain of the cell
    								board.getValue(i, j).removeValue(Integer.parseInt(number));
    							}
    							catch(PropagationFailureException ex)
    							{                            	
    								JOptionPane.showMessageDialog(null, "The problem has reached an unstable state, please start over.", "Failure", JOptionPane.ERROR_MESSAGE);
    								//Reset the puzzle since it is not solvable with selected numbers
    								solver.reset();
    								solver.restoreSolution(initialProblem);
    							}
    						}

    					}
    					catch(PropagationFailureException pfe) {
    						JOptionPane.showMessageDialog(null, "The last selection led to an invalid puzzle state", "PropationFailureException", JOptionPane.ERROR_MESSAGE);
    						pfe.printStackTrace();
    					}
    					//Redraw the interface
    					createAndShowGUI(false);
    					return;
    				}
    			}
    		}
    	}
    	//If the pressed a button do the following
    	if(e.getSource() instanceof JButton)
    	{
    		JButton buttonPressed = (JButton) e.getSource();

    		//If they pressed this button, have the computer solve and display the solution
    		if(buttonPressed.getText().equals("Solve Automatically"))
    		{
    			initialProblem = solver.storeSolution(scope);
    			this.solvePuzzle();
    		}
    		//If they are solving manually, lets store the 
    		//initial state and let them select numbers
    		if(buttonPressed.getText().equals("Solve Manually"))
    		{
    			initialProblem = solver.storeSolution(scope);
    		}
    		//If they pressed the reset button, lets restore original puzzle
    		if(buttonPressed.getText().equals("Reset Puzzle"))
    		{
    			try {
    				solver.reset();
					solver.restoreSolution(initialProblem);
				} catch (PropagationFailureException e1) {
					e1.printStackTrace();
				}
				//remove the initial problem so they can
				//select their solve method again
				initialProblem = new SolverSolution();
    		}
    		createAndShowGUI(false);
    	}
    }
    /**
     * Creates and displays the GUI interface
     * for solving sudoku puzzles.
     * 
     * @param initialCall		controls display messages
     */
    private void createAndShowGUI(boolean initialCall) {
        panel.invalidate();
        
        JFrame.setDefaultLookAndFeelDecorated(true);
        panel.removeAll();
        
        //Create and set up the window    
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JLabel emptyLabel = new JLabel("");
        emptyLabel.setPreferredSize(new Dimension(175, 100));
        frame.getContentPane().add(emptyLabel, BorderLayout.CENTER);
        
        //Setup the buttons for the bottom of the pane
        JButton solveAuto = new JButton("Solve Automatically");
        JButton solveManually = new JButton("Solve Manually");
        JButton resetPuzzle = new JButton("Reset Puzzle");
        
        //Add the actionListeners
        solveAuto.addActionListener(this);
        solveManually.addActionListener(this);
        resetPuzzle.addActionListener(this);
        
        //Lets setup the grid with the sudoku numbers
        //If it returns true, then the puzzle is solved.
        if (setupSudokuGrid()) {
            JOptionPane.showMessageDialog(null, "The Sudoku puzzle is fully solved", "Puzzle Solved", JOptionPane.INFORMATION_MESSAGE);
        }
       
        
        
        //If the puzzle is initialized, show a reset button
        if(initialProblem.variables().size() != 0)
        	panel.add(resetPuzzle);
        else
        {
        	//Add the buttons to the bottom of the panel
        	//These choose how the puzzle is solved and mark inital state
            panel.add(solveAuto);
            panel.add(solveManually);
        }
        
        if(initialCall)
        {
        	JOptionPane.showMessageDialog(null, "Enter the initial values of the puzzle. Then choose solution method.","Intialize Problem",JOptionPane.INFORMATION_MESSAGE);
        }
        
        frame.getContentPane().add(panel);
        panel.repaint();
        panel.setVisible(true);
        panel.validate();
        
        //Display the window.
        frame.validate();
        
        frame.getContentPane().repaint();
        frame.setVisible(true);
        frame.setSize(500,500);
        frame.setResizable(false);
        
    }
    /**
     * Adds the sudoku grid with known numbers and
     * selection boxes for unbound sudoku cells to the GUI
     * 
     * @return		returns true if all cells are bound
     */
    public boolean setupSudokuGrid()
    {
    	panel.setLayout(new GridLayout(4,3));
        JPanel[][] panelGrid = new JPanel[3][3];
        for (int i=0;i<3;i++){
            for (int j=0;j<3;j++){
                panelGrid[i][j]=new JPanel();
                panelGrid[i][j].setLayout(new GridLayout(3,3));
            }
        }
        
        boolean allBound=true;
        for (int i=0; i<board.getNumSquares()*3; i++) {
            for (int j=0; j<board.getNumSquares()*3;j++){
            	CspIntVariable currentCell = board.getValue(i, j);
                if (currentCell.isBound()) {
                    JLabel label =new JLabel(""+currentCell.getMin(),SwingConstants.CENTER); 
                    label.setBorder(new LineBorder(Color.GRAY));
                    combos[i][j]=label;
                    panelGrid[i/3][j/3].add(label);
                }
                else {
                    allBound=false;
                    String[] nums = new String[currentCell.getSize()+1];
                    nums[0]=" ";
                    int num = currentCell.getMin();
                    for (int k=1;k<currentCell.getSize()+1;k++) {
                        nums[k]=""+num;
                        num = currentCell.getNextHigher(num);
                    }
                    JComboBox numList = new JComboBox(nums);
                    numList.addActionListener(this);
                    combos[i][j]=numList;
                    panelGrid[i/3][j/3].add(numList);
                }
            }
        }
        
        for (int i=0;i<3;i++){
            for (int j=0;j<3;j++){
                panelGrid[i][j].setBorder(new BevelBorder(BevelBorder.RAISED));
                panel.add(panelGrid[i][j]);
            }
        }
        return allBound;
    }
}