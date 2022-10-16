/* TargetGrid is part of the Battle Ship project.  It is an implementation of the GameBoard superclass.  It is used to 
 * represent the target grid (your view of your opponents grid) in a Battle Ship game.
 * Authors: Ryan Collins, John Schmidt
 * Updated: 10/15/2022
 */

public class TargetGrid extends GameBoard {

	//constructor
	public TargetGrid(int rows,int cols) {
		super(rows, cols);
	}

	// updates grid with new value; -1 = no ship no shot, 0 = no ship with shot, 1 = ship with hit
	public boolean processShot() {
		int row = this.getCurrentShot()[0];
		int col = this.getCurrentShot()[1];

		if (this.getGridLocationValue(row, col) < 1) { // miss
			this.isMiss(row, col);
			return false;

		} else { // hit
			this.isHit(row, col);
			return true;
		}
	}

	// updates grid with a 0 (miss) at given location
	public void isMiss(int row, int col) {
		this.updateGrid(row, col, 0);
	}

	// updates grid with 1 (ship hit) at given location
	public void isHit(int row, int col) {
		this.updateGrid(row, col, 1);
	}
}
