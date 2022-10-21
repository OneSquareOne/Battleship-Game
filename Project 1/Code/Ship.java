/* Ship is part of the Battle Ship project.  The Ship class contains all of the data members necessary to 
 * represent a ship in the Battle Ship game.  It is constructed such that if extension of the game rules is
 * desired (additional ships, altered number of shots like the Salvo variant, etc...), the code can be easily modified.
 * Authors: Ryan Collins, John Schmidt
 * Updated: 10/20/22
 */

public class Ship {

	private String name;
	private int shipID; // identifies ship on a grid
	private int size;
	private int hits;
	private int[] bowPosition; // the position of the front of the ship
	private int[][] hitPositions; // positions ship has been hit
	private boolean isHorizontal;
	private boolean isSunk;
	private String imagePathName; // for building the rest of image fil

	// constructor
	public Ship(String shipName, int iD, int shipSize, boolean horizontal, String fileName) {
		name = shipName;
		shipID = iD;
		size = shipSize;
		hits = 0;
		hitPositions = new int[shipSize][2];
		for (int i = 0; i < shipSize; i++) { // initialize all hit positions to (-1,-1)
			for (int j = 0; j < 2; j++)
				hitPositions[i][j] = -1;
		}
		bowPosition = new int[] { -1, -1 };
		isHorizontal = horizontal;
		isSunk = false;
		imagePathName = fileName;
	}

	// return ship name
	public String getName() {
		return name;
	}

	// returns shipID
	public int getID() {
		return shipID;
	}

	// return ship size
	public int getSize() {
		return size;
	}

	// returns the appropriate image file path for the ship at given row and column;
	// Precondition:
	// ship must exist at location
	public String getImagePathAtCoordinates(int row, int col) {

		if (checkForHit(row, col)) // check it its an image with a hit (explosion)
			imagePathName = imagePathName + "/2-";
		else
			imagePathName = imagePathName + "/1-";

		for (int i = 0; i < getSize(); i++) { // add correct ship slice
			if (isHorizontal) {
				if ((bowPosition[1] + i) == col) { // since ship is horizontal, only check columns
					imagePathName = imagePathName + (i + 1) + "h";
				}
			} else {
				if ((bowPosition[0] + i) == row) { // since ship is vertical, only check rows
					imagePathName = imagePathName + (i + 1) + "v";
				}
			}
		}
		return imagePathName; //still needs extension (.jpg) after this return
	}

	// update if ship is facing horizontal
	public void setHorizontal(boolean bool) {
		isHorizontal = bool;
	}

	// returns true if the ship is horizontal; bow is always facing at top or left
	public boolean isHorizontal() {
		return isHorizontal;
	}

	// add a hit to the ship, changes isSunk if the ship has been fully hit; updates
	// hitPositions
	public void addHit(int row, int col) {

		for (int i = 0; i < size; i++) { // go through hit positions
			if ((hitPositions[i][0] == row) && (hitPositions[i][1] == col)) { // hit is duplicate
				return;
			} else if (hitPositions[i][0] == -1) { // got to end of list; add hit location
				hits++;
				if (hits == size) { // update sunk condition
					isSunk = true;
				}
				hitPositions[i][0] = row;
				hitPositions[i][1] = col;
				return;
			}
		}
	}

	// returns a 2D int array of coordinates where the ship was hit, in the order
	// they were hit
	public int[][] getHitPositions() {
		return hitPositions;
	}

	// returns if the ship is sunk
	public boolean isSunk() {
		return isSunk;
	}

	// returns position of bow of ship; bow is always facing top or left
	public int[] getBowPosition() {
		return bowPosition;
	}

	// sets new bow position
	public void setBowPosition(int row, int col) {
		bowPosition[0] = row;
		bowPosition[1] = col;
	}

	// returns true if ship has a hit at given coordinates
	public boolean checkForHit(int row, int col) {
		for (int i = 0; i < size; i++) {
			if (row == hitPositions[i][0] && col == hitPositions[i][1]) {
				return true;
			}
		}
		return false;
	}
}