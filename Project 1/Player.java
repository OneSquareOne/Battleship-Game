/* Player is part of the Battle Ship project.  Player represents the data for each individual player in a game of Battle Ship. 
 * Authors: Ryan Collins, John Schmidt
 * Updated: 9/20/22
 */

public class Player {
	
	private TargetGrid target;
	private OceanGrid ocean;
	private int remainingShips;
	private int wins;
	private int losses;
	private String playerName;
	
	//constructor
	public Player(String name) {
		target = new TargetGrid();
		ocean = new OceanGrid();
		remainingShips = ocean.getNumberOfShips();
		wins = 0;
		losses = 0;
		playerName = name;
	}

	//resets players boards by creating new target and ocean grids
	public void reintializeBoard() {
		target = new TargetGrid();
		ocean = new OceanGrid();
		remainingShips = ocean.getNumberOfShips();
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
