
public class Ship {

	private String name;
	private int shipID;
	private int size;
	private int hits;
	private int [] bowPosition; //the position of the front of the ship
	private boolean isHorizontal;
	private boolean isSunk;

	//constructor
	public Ship(String shipName, int iD, int shipSize, boolean horizontal) {
		name = shipName;
		shipID = iD;
		size = shipSize;
		hits = 0;
		bowPosition = new int[] {-1,-1};
		isHorizontal = horizontal;
		isSunk = false;
	}

	//return ship name
	public String getName() {
		return name;
	}
	
	//returns shipID
	public int getID() {
		return shipID;
	}
	
	//return ship size
	public int getSize() {
		return size;
	}

	//update if ship is facing horizontal
	public void setHorizontal(boolean bool) {
		isHorizontal = bool;
	}

	//returns true if the ship is horizontal; bow is always facing at top or left
	public boolean isHorizontal() {
		return isHorizontal;
	}

	//add a hit to the ship, changes isSunk if the ship has been fully hit
	public void isHit() {
		hits++;
		if (hits == size) {
			isSunk = true;
		}
	}

	//returns if the ship is sunk
	public boolean isSunk() {
		return isSunk;
	}
	
	//returns position of bow of ship; bow is always facing top or left
	public int[] getBowPosition() {
		return bowPosition;
	}
	
	public void setBowPosition(int row, int col) {
		bowPosition[0] = row;
		bowPosition[1] = col;
	}
}