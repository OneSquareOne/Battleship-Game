/* OceanGrid is part of the Battle Ship project.  The OceanGrid class is an implementation of the GameBoard superclass.  It is used to 
 * represent the ocean grid (your view of your own grid and ships) in a Battle Ship game.
 * Authors: Ryan Collins, John Schmidt
 * Updated: 9/20/22
 */

import java.util.Random; //for autoPlaceShip()

public class OceanGrid extends GameBoard {

	private Ship[] shipArray;
	private final int NUM_SHIPS = 5; // open for expansion

	// constructor
	public OceanGrid() {
		super();
		shipArray = new Ship[NUM_SHIPS];
		shipArray[0] = new Ship("Carrier", 1, 5, true);
		shipArray[1] = new Ship("Battleship", 2, 4, true);
		shipArray[2] = new Ship("Cruiser", 3, 3, true);
		shipArray[3] = new Ship("Submarine", 4, 3, true);
		shipArray[4] = new Ship("Destroyer", 5, 2, true);
	}

	// place ship; checks for valid placement
	public boolean placeShip(int row, int col, Ship ship1, boolean horizontal) {

		// set whether ship is horizontal or not
		ship1.setHorizontal(horizontal);

		// check for invalid placement out of bounds
		if (row < 0 || row > 9 || col < 0 || col > 9)
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
			for (int i = 0; i < height; i++) {
				if ((row + i) > 9 || this.getGridLocationValue(row + i, col) > 0) { // check out of bounds and check
																					// grid (greater than 0 means that a
																					// ship is there already)
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

				int row = rand.nextInt(10); // generate random row between 0-10
				int col = rand.nextInt(10); // generate random row between 0-10
				boolean horziontal = rand.nextBoolean(); // generate random T/F

				validPlacement = this.placeShip(row, col, ship1, horziontal); // try to place ship

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

	// get ships remaining unsunk
	public int shipsLeft() {
		int remainingShips = NUM_SHIPS;

		for (int i = 0; i < NUM_SHIPS; i++) { // remove sunk ships from count
			if (shipArray[i].isSunk())
				remainingShips--;
		}
		return remainingShips;
	}

}