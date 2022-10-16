/* GameBoard is part of the Battleship Project.  The GameBoard class is the abstract class for implementation of the TargetGrid
 * and OceanGrid classes. The processShot() method is abstract and must be implemented in these subclasses.
 * Authors: Ryan Collins, John Schmidt
 * Updated: 10/15/2022
 */

public abstract class GameBoard {
	private int[][] gridArray;
	private int[] currentShot; // 2 element array, shot location coordinates [row,col]
	protected int totalRows;
	protected int totalCols;

	// constructor; initializes array to -1
	public GameBoard(int rows, int cols) {
		totalRows = rows;
		totalCols = cols;
		gridArray = new int[totalRows][totalCols];
		for (int i = 0; i < totalRows; i++) {
			for (int j = 0; j < totalCols; j++)
				gridArray[i][j] = -1;
		}
		currentShot = new int[] { -1, -1 };
	}

	// clears the grid
	public void clearGrid() {
		for (int i = 0; i < totalRows; i++) {
			for (int j = 0; j < totalCols; j++)
				gridArray[i][j] = -1;
		}
	}

	// updates grid with new value; -1 = no ship, 0 = missed shot (for
	// visualization), 1 or greater: ship (hit or not hit)
	public void updateGrid(int row, int col, int newVal) {
		gridArray[row][col] = newVal;
	}

	// returns grid value; -1 = no ship, 0 = missed shot (for visualization), 1 or
	// greater: ship (hit or not hit)
	public int getGridLocationValue(int row, int col) {
		return gridArray[row][col];
	}

	//returns the grid array
	public int[][] getGridArray(){
		return gridArray;
	}

	// updates current shot
	public void setCurrentShot(int row, int col) {
		currentShot[0] = row;
		currentShot[1] = col;
	}

	// returns 2 element array with row, col coordinates
	public int[] getCurrentShot() {
		int[] shotArr = { currentShot[0], currentShot[1] };
		return shotArr;
	}

	//copies right hand side's game board.  May be useful if GameBoard is changed to a serialized 
	//class for sending through I/O streams
	public GameBoard copy(GameBoard rightHandSide){
		for (int i = 0; i < totalRows; i++) {
			for (int j = 0; j < totalCols; j++)
				this.gridArray[i][j] = rightHandSide.gridArray[i][j];
		}
		return this;
	}

	// process shot received or shot made; returns boolean to indicate true if hit
	public abstract boolean processShot();
}
