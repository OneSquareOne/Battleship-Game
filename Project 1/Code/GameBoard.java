/* GameBoard is part of the Battleship Project.  The GameBoard class is the abstract class for 
 * implementation of the TargetGrid and OceanGrid classes.
 * Authors: Ryan Collins, John Schmidt
 * Updated: 10/29/2022
 */

public abstract class GameBoard {
	private int[][] gridArray;
	private int[] currentShot; // 2 element array, shot location coordinates [row,col]
	protected int totalRows;
	protected int totalCols;
	protected final String IMAGE_DIRECTORY = "./Images"; // directory of all images
	protected final String IMAGE_EXTENSION = ".jpg"; // extension used for ship images
	protected final String AIRCRAFT_CARRIER_IMG_SUBDIRECTORY = "/AircraftCarrier";
	protected final String BATTLESHIP_IMG_SUBDIRECTORY = "/Battleship";
	protected final String CRUISER_IMG_SUBDIRECTORY = "/Cruiser";
	protected final String SUBMARINE_IMG_SUBDIRECTORY = "/Submarine";
	protected final String DESTROYER_IMG_SUBDIRECTORY = "/Destroyer";
	protected final String OTHER_IMG_SUBDIRECTORY = "/Other";
	
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

	// copies right hand side's GameBoard grid array
	public int[][] copyGridArray(int[][] rightHandSide) {
		for (int i = 0; i < totalRows; i++) {
			for (int j = 0; j < totalCols; j++)
				this.gridArray[i][j] = rightHandSide[i][j];
		}
		return this.gridArray;
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

	// process shot received or shot made; returns boolean to indicate true if hit
	public abstract boolean processShot();

	//returns file path for image at game board location
	public abstract String getImagePath(int row, int col);
}