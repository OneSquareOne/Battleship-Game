/* Controller is part of the MVC design pattern and is used as a point of communication between the model and view.  It is 
 * also responsible for initializing the game environments an connecting to another player.
 * Authors: Ryan Collins, John Schmidt
 * Last Update: 10/4/2022
 */

import java.util.Scanner;

import javax.lang.model.util.ElementScanner14;

public class Controller implements Observer, Subject {

	public static void main (String args[]) {
		// data members
		Scanner keyboard = new Scanner(System.in);
		String name;
		Role thisPlayerRole;
		// **game initialization**

		System.out.print("Enter your name: "); //TODO: move this to the GUI
		name = keyboard.nextLine();	//TODO: once in the GUI, the View will pass name here
		
		Player thisPlayer = new Player(name);
		
		Player opponent = new Player("Opponent"); //TODO: if it's possible, we can get the
		//name from the opponent once the connection is established.  Until then, it can be default

		System.out.print("Select a role: 1=Server  2=Client"); //TODO:Replace with input from GUI
		int choice = keyboard.nextInt();
		
		BattleShipServer newone = new BattleShipServer();
		newone.getServerName();
		
		
		if(choice == 1){
			thisPlayerRole = new BattleShipServer();
			System.out.print("You are the " + thisPlayerRole.getRole() + ". Your device name is: ");
		
		}else 
			thisPlayerRole = new BattleShipClient(name)
		//methods for communication between view and model
		
		keyboard.close();
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
