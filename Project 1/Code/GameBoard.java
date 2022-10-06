/* GameBoard is part of the Battleship Project.  The GameBoard class is the abstract class for implementation of the TargetGrid
 * and OceanGrid classes. The processShot() method is abstract and must be implemented in these subclasses.
 * Authors: Ryan Collins, John Schmidt
 * Updated: 9/20/22
 */

public abstract class GameBoard {
	private int[][] gridArray;
	private int[] currentShot; // 2 element array, shot location coordinates [row,col]

	// constructor; initializes array to -1
	public GameBoard() {
		gridArray = new int[10][10];
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++)
				gridArray[i][j] = -1;
		}
		currentShot = new int[]{-1,-1};
	}

	// clears the grid
	public void clearGrid() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++)
				gridArray[i][j] = -1;
		}
	}

	// updates grid with new value; -1 = no ship, 0 = missed shot (for visualization), 1 or greater: ship (hit or not hit)
	public void updateGrid(int row, int col, int newVal) {
		gridArray[row][col] = newVal;
	}

	// returns grid value; -1 = no ship, 0 = missed shot (for visualization), 1 or greater: ship (hit or not hit)
	public int getGridLocationValue(int row, int col) {
		return gridArray[row][col];
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
}
