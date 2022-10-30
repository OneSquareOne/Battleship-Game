/* OceanGrid is part of the Battle Ship project.  The OceanGrid class is an implementation of the 
 * GameBoard superclass.  It is used to represent the ocean grid (your view of your own grid and
 * ships) in a Battle Ship game.
 * Authors: Ryan Collins, John Schmidt
 * Updated: 10/30/2022
 */

import java.util.Random; //for autoPlaceShip()

public class OceanGrid extends GameBoard {

	private Ship[] shipArray;
	private final int NUM_SHIPS = 5; // open for expansion
	

	// constructor
	public OceanGrid(int rows, int cols) {
		super(rows, cols);
		shipArray = new Ship[NUM_SHIPS];
		shipArray[0] = new Ship("Carrier", 1, 5, true, AIRCRAFT_CARRIER_IMG_SUBDIRECTORY);
		shipArray[1] = new Ship("Battleship", 2, 4, true, BATTLESHIP_IMG_SUBDIRECTORY);
		shipArray[2] = new Ship("Cruiser", 3, 3, true, CRUISER_IMG_SUBDIRECTORY);
		shipArray[3] = new Ship("Submarine", 4, 3, true, SUBMARINE_IMG_SUBDIRECTORY);
		shipArray[4] = new Ship("Destroyer", 5, 2, true, DESTROYER_IMG_SUBDIRECTORY);
	}

	// place ship; checks for valid placement
	public boolean placeShip(int row, int col, Ship ship1, boolean horizontal) {

		// set whether ship is horizontal or not
		ship1.setHorizontal(horizontal);

		// check for invalid placement out of bounds
		if (row < 0 || row > totalRows - 1 || col < 0 || col > totalCols - 1)
			return false;

		int length = 0; // ship length if it is horizontal
		int height = 0; // ship height if it is vertical

		// ship is horizontal
		if (ship1.isHorizontal()) {
			length = ship1.getSize();
			for (int i = 0; i < length; i++) {
				if ((col + i) > 9 || this.getGridLocationValue(row, col + i) > 0) { // check out of bounds and check
																					// grid (greater than 0 means that a
																					// ship is there already)
					return false;
				}
			}

			// valid placement established if past previous for loop; change grid values for
			// appropriate ship
			ship1.setBowPosition(row, col);
			for (int i = 0; i < length; i++) {
				this.updateGrid(row, col + i, ship1.getID());
			}

		} else { // ship is vertical
			height = ship1.getSize();
			for (int i = 0; i < height; i++) { //check out of bounds and spot already filled
				if ((row + i) > totalRows - 1 || this.getGridLocationValue(row + i, col) > 0) { 
					return false;
				}
			}

			// valid placement established if past previous for loop; change grid values for
			// appropriate ship
			ship1.setBowPosition(row, col);
			for (int i = 0; i < height; i++) {
				this.updateGrid(row + i, col, ship1.getID());
			}
		}

		return true;
	}

	// returns Ship with shipID; precondition: Ship with shipID must exist
	public Ship getShipWithID(int shipID) {

		Ship tempShip = shipArray[0];
		for (int i = 0; i < NUM_SHIPS; i++) { // for each ship in the ship array
			if (shipID == shipArray[i].getID()) { // check shipID against that ship's ID
				tempShip = shipArray[i]; // save under tempShip to return
			}
		}
		return tempShip;
	}

	// returns Ship at current grid location
	public Ship getShipAt(int row, int col) {
		int shipID = this.getGridLocationValue(row, col); // find shipID at location given
		Ship ship1 = this.getShipWithID(shipID);
		return ship1;
	}

	// place all ships automatically (and randomly)
	public void autoPlaceShips() {

		Random rand = new Random(); // for generating random numbers

		// place all NUM_SHIPS (number of ships)
		for (int i = 0; i < NUM_SHIPS; i++) {
			Ship ship1 = shipArray[i]; // select current ship

			boolean validPlacement = false;
			while (!validPlacement) {

				int row = rand.nextInt(totalRows); // generate random row between 0-totalRows
				int col = rand.nextInt(totalCols); // generate random row between 0-totalCols
				boolean horizontal = rand.nextBoolean(); // generate random T/F

				validPlacement = this.placeShip(row, col, ship1, horizontal); // try to place ship

			}
		}
	}

	// updates grid gridArray value and sends hit data to individual Ships
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

	// updates ship with a hit at given location
	public void isHit(int row, int col) {
		Ship ship1 = this.getShipAt(row, col); // find which ship is hit
		ship1.addHit(row, col); // update with hit
	}

	// get number of ships in ocean grid
	public int getNumberOfShips() {
		return NUM_SHIPS;
	}

	// get ships remaining afloat
	public int shipsLeft() {
		int remainingShips = NUM_SHIPS;

		for (int i = 0; i < NUM_SHIPS; i++) { // remove sunk ships from count
			if (shipArray[i].isSunk())
				remainingShips--;
		}
		return remainingShips;
	}

	// returns file path for image at game board location
	public String getImagePath(int row, int col) {
		String fileNameString = IMAGE_DIRECTORY; // add directory
		int shipID = getGridLocationValue(row, col); // get ship or ocean value

		if (shipID == -1) // build extension for open ocean image
			fileNameString = fileNameString + OTHER_IMG_SUBDIRECTORY + "/blankOcean";
		else if (shipID == 0) // build extension for open ocean with miss
			fileNameString = fileNameString + OTHER_IMG_SUBDIRECTORY + "/miss";
		else { // build extension for correct ship with correct orientation and hit/miss
			Ship ship = getShipWithID(shipID);
			fileNameString = fileNameString + ship.getImagePathAtCoordinates(row, col);
		}
		return fileNameString + IMAGE_EXTENSION;
	}

}