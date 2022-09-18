
public class TargetGrid extends GameBoard {

	public TargetGrid() {
		super();
	}

	// -1: No ship, no miss yet  0: No ship in square with miss; 1-5: ship is in square; returns -1 if a miss, else
	// returns 1-5 indicating which ship occupies that square
	public int processShot() {
		int row = this.getCurrentShot()[0];
		int col = this.getCurrentShot()[1];

		// miss
		if (this.getGridLocationValue(row, col) < 1) {
			this.isMiss(row, col);
			return -1;

			// hit
		} else {
			return this.getGridLocationValue(row, col);
		}
	}

	// updates grid with a 0 (miss) at given location
	public void isMiss(int row, int col) {
		this.updateGrid(row, col, 0);
	}
}
