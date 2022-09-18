import java.util.Random; //for autoPlaceShip()

public class OceanGrid extends GameBoard {

	Ship[] shipArray;

	// constructor
	public OceanGrid() {
		super();
		shipArray = new Ship[5];
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

	public void autoPlaceShips() {
		
		Random rand = new Random(); //for generating random numbers
		
		// place all 5 ships
		for (int i = 0; i < 5; i++) {
			Ship ship1 = shipArray[i]; // select current ship

			boolean validPlacement = false;
			while (!validPlacement) {
				
				int row = rand.nextInt(10); //generate random row between 0-10
				int col = rand.nextInt(10); //generate random row between 0-
				boolean horziontal = rand.nextBoolean(); //generate random T/F
				
				validPlacement = this.placeShip(row, col, ship1, horziontal); //try to place ship
				
			}
		}
	}
}
