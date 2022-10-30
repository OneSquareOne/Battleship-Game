/* Player is part of the Battle Ship project.  Player represents the data for each individual player
 * in a game of Battle Ship. 
 * Authors: Ryan Collins, John Schmidt
 * Updated: 10/20/2022
 */

public class Player {
	
	private TargetGrid target;
	private OceanGrid ocean;
	private int remainingShips;
	private int wins;
	private int losses;
	private String playerName;
	private int boardRows;
	private int boardCols;
	
	//constructor
	public Player(String name, int rows, int cols) {
		boardRows = rows;
		boardCols = cols;
		
		target = new TargetGrid(boardRows, boardCols);
		ocean = new OceanGrid(boardRows, boardCols);
		remainingShips = ocean.getNumberOfShips();
		wins = 0;
		losses = 0;
		playerName = name;
	}

	//resets players boards by creating new target and ocean grids
	public void reinitializeBoard() {
		target = new TargetGrid(boardRows, boardCols);
		ocean = new OceanGrid(boardRows, boardCols);
		remainingShips = ocean.getNumberOfShips();
	}
	
	//returns the number of ships that still need to be placed on ocean grid
	public int getShipsToBePlaced(){
		int shipsRemaining = ocean.getNumberOfShips();
		Ship tempShip;
		for(int ID = 1; ID <= ocean.getNumberOfShips(); ID++){
			tempShip = ocean.getShipWithID(ID); //iterate over each ship
			
			if(tempShip.getBowPosition()[1] != -1){ // bow position column is not -1, then ship
				shipsRemaining--;
			}
		}
		return shipsRemaining;
	}

	//updates and returns the number of remaining ships
	public int getRemainingShips() {
		remainingShips = ocean.shipsLeft();
		return remainingShips;
	}
	
	//adds win
	public void addWin() {
		wins++;
	}
	
	//adds loss
	public void addLoss() {
		losses++;
	}
	
	//returns Target Grid
	public TargetGrid getTargetGrid() {
		return target;
	}
	
	//returns Ocean Grid
	public OceanGrid getOceanGrid() {
		return ocean;
	}
	
	//returns Wins
	public int getWins() {
		return wins;
	}
	
	//returns Losses
	public int getLosses() {
		return losses;
	}
	
	//sets Player name
	public void setName(String name) {
		playerName = name;
	}
	
	//returns Player Name
	public String getName() {
		return playerName;
	}
}
