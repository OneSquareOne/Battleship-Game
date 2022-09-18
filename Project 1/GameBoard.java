
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
		currentShot[0] = -1;
		currentShot[1] = -1;
	}

	// clears the grid
	public void clearGrid() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++)
				gridArray[i][j] = -1;
		}
	}

	// updates grid with new value
	public void updateGrid(int row, int col, int newVal) {
		gridArray[row][col] = newVal;
	}

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

	// process shot received or shot made; returns value to indicate shot results
	public abstract int processShot();
}
