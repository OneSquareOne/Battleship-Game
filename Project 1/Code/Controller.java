/* Controller is part of the MVC design pattern and is used as a point of communication between the model and view.  It is 
 * also responsible for initializing the game environments an connecting to another player.
 * Authors: Ryan Collins, John Schmidt
 * Last Update: 10/4/2022
 */

import java.util.Scanner;

public class Controller implements Observer, Subject {

	final int BOARD_ROWS = 10; // can be changed for larger board size
	final int BOARD_COLS = 10;
	public static void main (String args[]) {
		// data members
		Scanner keyboard = new Scanner(System.in);
		String name;
		Role thisPlayerRole;

		// **game initialization**

		System.out.print("Enter your name: "); //TODO: move this to the GUI
		name = keyboard.nextLine();	//TODO: once in the GUI, the View will pass name here
		
		Player thisPlayer = new Player(name);
		
		//shadow is for maintaining a copy of the opponents grid so less communication is needed
		//between players. After current shot is sent to opponent, all game logic is applied to 
		//the shadow, and the opponent does the same.  Shadow should always be an exact copy of the 
		//second player this also minimizes the need to rewrite code from the original version where
		//both players were stored on the same console
		Player opponentShadow = new Player("Opponent"); //TODO: if it's possible, we can get the
		//name from the opponent once the connection is established.  Until then, it can be default

		System.out.print("Select a role: 1=Server  2=Client"); //TODO:Replace with input from GUI
		int choice = keyboard.nextInt();
		
		
		if(choice == 1){
			thisPlayerRole = new BattleShipServer();
			System.out.print("You are the " + thisPlayerRole.getRole() + ". Your device name is: " + ((BattleShipServer)thisPlayerRole).getServerName());
		
		}else {
			System.out.print("Enter Server device name: ");
			name = keyboard.nextLine();
			thisPlayerRole = new BattleShipClient(name);
		}
		//TODO: Set up communication between client and server


		//TODO: Players place ships
	
		//TODO: Players swap ocean grids
		
	
		

		keyboard.close();

	}

	// method modifies an incoming OceanGrid to targetGrid formatting. Both players store a copy of
	// their own ocean grid and the target grid representing their opponent
	public void copyOpponentsOceanGrid(OceanGrid copiedOceanGrid, Player opponentShadow){
		for(int i = 0; i < BOARD_ROWS; i++){
			for(int j = 0; j < BOARD_COLS; j++){
				int value = copiedOceanGrid.getGridLocationValue(i, j); //get value from reference
				opponentShadow.getOceanGrid().updateGrid(i, j, value); //copy to shadow
			}
		}
	}

	@Override
	public void registerObserver(Observer o) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeObserver(Observer o) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyObservers() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

}
