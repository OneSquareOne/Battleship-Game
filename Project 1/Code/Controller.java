/* Controller is part of the MVC design pattern and is used as a point of communication between the model and view.  It is 
 * also responsible for initializing the game environments and connecting to another player.
 * Authors: Ryan Collins, John Schmidt
 * Last Update: 10/15/2022
 */

import java.io.IOException;
import java.util.Scanner;

public class Controller {
	static Scanner keyboard = new Scanner(System.in);
	final int BOARD_ROWS = 10; // can be changed for larger board size
	final int BOARD_COLS = 10;

	public static void main(String args[]) throws IOException {
		// data members

		String name; // for passing current player's name
		Role thisPlayerRole; // applications role (server or client)
		int currentWinner = -1; // for passing along current winner
		int rowColArray[]; // for passing along current shot

		// **game initialization**

		System.out.print("Enter your name: "); // TODO: move this to the GUI
		name = keyboard.nextLine(); // TODO: once in the GUI, the View will pass name here

		Player thisPlayer = new Player(name);

		// Shadow is for maintaining a copy of the opponents grid so less communication
		// is needed between players. After current shot is sent to opponent, all game
		// logic is applied to the shadow, and the opponent does the same. Shadow should
		// always be an exact copy of the second player. This also minimizes the need to
		// rewrite code from the original version where both players were stored on the
		// same console
		Player opponentShadow = new Player("Opponent");

		System.out.print("Select a role: 1=Server  2=Client:  "); // TODO:Replace with input from GUI
		int choice = keyboard.nextInt();
		keyboard.nextLine();

		if (choice == 1) {
			thisPlayerRole = new BattleShipServer();
			System.out.print("You are the " + thisPlayerRole.getRole() + ". Your device name is: "
					+ ((BattleShipServer) thisPlayerRole).getServerName());
			System.out.println();

		} else {
			System.out.print("Enter Server device name: ");
			name = keyboard.nextLine();
			thisPlayerRole = new BattleShipClient(name);
		}

		// sets up the client side or server side of the connection, based on the
		// application's role
		thisPlayerRole.startConnection();

		// Players swap names, sever sends first
		if (thisPlayerRole instanceof BattleShipServer) {
			thisPlayerRole.send(thisPlayer.getName()); // send this player's name to opponent
			opponentShadow.setName((String) thisPlayerRole.receive()); // receive opponents name

		} else if (thisPlayerRole instanceof BattleShipClient) {
			opponentShadow.setName((String) thisPlayerRole.receive()); // receive opponents name
			thisPlayerRole.send(thisPlayer.getName()); // send this player's name to opponent
		}

		// place ships
		placeShips(thisPlayer);
		System.out.println("Local Ships placed\n"); // DEBUG for testing

		// Players swap ocean grids, sever sends first
		if (thisPlayerRole instanceof BattleShipServer) {
			thisPlayerRole.send(thisPlayer.getOceanGrid().getGridArray()); // send to client
			System.out.println("Local Ships sent\n"); // DEBUG for testing
			opponentShadow.getOceanGrid().copyGridArray((int[][]) thisPlayerRole.receive());
			System.out.println("Opponent Ships copied\n"); // DEBUG for testing

		} else if (thisPlayerRole instanceof BattleShipClient) {
			opponentShadow.getOceanGrid().copyGridArray((int[][]) thisPlayerRole.receive());
			System.out.println("Opponent Ships copied\n"); // DEBUG for testing
			thisPlayerRole.send(thisPlayer.getOceanGrid().getGridArray()); // send to server
			System.out.println("Local Ships sent\n"); // DEBUG for testing
		}

		System.out.println("Ships placement completed\n"); // DEBUG for testing

		displayBothGrids(thisPlayer);

		displayBothGrids(opponentShadow); // DEBUG for testing

		// the next if/else preserves play order; it has the server shoot first
		if (thisPlayerRole instanceof BattleShipServer) {
			System.out.println(thisPlayer.getName() + "'s the server.  The server fires first! \n");
			rowColArray = shotPrompt(thisPlayer); // player1's turn
			System.out.println("Sending current shot.\n"); // DEBUG for testing
			thisPlayerRole.send(rowColArray);
			System.out.println("Current shot sent.\n"); // DEBUG for testing
			bombardPlayer(rowColArray[0], rowColArray[1], thisPlayer, opponentShadow);
			displayBothGrids(thisPlayer); // TODO: move to GUI
			currentWinner = checkForWinner(thisPlayer, opponentShadow);

		} else if (thisPlayerRole instanceof BattleShipClient) {
			System.out.println(thisPlayer.getName() + "'s the client.  The server fires first!\n");
			// TODO: move to GUI
		}

		while (currentWinner == -1) { // main play loop

			System.out.println("Awaiting incoming volley!\n");
			rowColArray = (int[]) thisPlayerRole.receive(); // get shot from opponent
			bombardPlayer(rowColArray[0], rowColArray[1], opponentShadow, thisPlayer);
			displayBothGrids(thisPlayer); // TODO: Move to GUI
			currentWinner = checkForWinner(thisPlayer, opponentShadow);

			if (currentWinner == -1) {
				rowColArray = shotPrompt(thisPlayer); // this player's turn
				System.out.println("Sending current shot.\n"); // DEBUG for testing
				thisPlayerRole.send(rowColArray);
				System.out.println("Current shot sent.\n"); // DEBUG for testing
				bombardPlayer(rowColArray[0], rowColArray[1], thisPlayer, opponentShadow);
				displayBothGrids(thisPlayer); // TODO: Move to GUI
				currentWinner = checkForWinner(thisPlayer, opponentShadow);
			}
		}

		// declare winners
		// TODO: move all this stuff to the GUI
		if (currentWinner == 1)
			System.out.println("We have a winner!  " + thisPlayer.getName() + " is this round's winnner.");
		else
			System.out.println("We have a winner!  " + opponentShadow.getName() + " is this round's winnner.");
			
		System.out.println(thisPlayer.getName() + ": " + thisPlayer.getWins() + " wins");
		System.out.println(opponentShadow.getName() + ": " + opponentShadow.getWins() + " wins");
		keyboard.close();
		thisPlayerRole.closeConnection();

		// TODO: Encapsulate in a loop if desired
	}

	// method modifies an incoming OceanGrid to targetGrid formatting. Both players
	// store a copy of
	// their own ocean grid and the target grid representing their opponent
	public void copyOpponentsOceanGrid(OceanGrid copiedOceanGrid, Player opponentShadow) {
		for (int i = 0; i < BOARD_ROWS; i++) {
			for (int j = 0; j < BOARD_COLS; j++) {
				int value = copiedOceanGrid.getGridLocationValue(i, j); // get value from reference
				opponentShadow.getOceanGrid().updateGrid(i, j, value); // copy to shadow
			}
		}
	}

	public static void placeShips(Player currentPlayer) {
		System.out.print("Place ships for " + currentPlayer.getName() + " automatically (Yes or No?)"); // TODO: change
																										// to GUI button
		String choice = keyboard.next();

		if (choice.equalsIgnoreCase("y") || choice.equalsIgnoreCase("yes")) { // place automatically
			currentPlayer.getOceanGrid().autoPlaceShips();
		} else {
			displayOceanGrid(currentPlayer); // print grid for reference
			for (int i = 0; i < currentPlayer.getOceanGrid().getNumberOfShips(); i++) { // place manually

				Ship tempShip = currentPlayer.getOceanGrid().getShipWithID(i + 1); // get ship with the ID i+1
				boolean validPlacement = false;
				while (!validPlacement) { // loop for validating correct placement

					int row, col;
					boolean horizontal = false; // for passing to placeShip(), assumed vertical
					System.out.print("Place the " + tempShip.getName() + "(" + tempShip.getSize()
							+ " spaces) horizontally (H) or vertically (V)?: ");
					String direction = keyboard.next();

					if (direction.equalsIgnoreCase("h")) // user selected horizontal
						horizontal = true;

					System.out.print("Enter a row and column number separated by a space to place the "
							+ tempShip.getName() + ":");
					row = keyboard.nextInt();
					col = keyboard.nextInt();
					System.out.println("You entered Row: " + row + "  Column: " + col);

					validPlacement = currentPlayer.getOceanGrid().placeShip(row, col, tempShip, horizontal);
					if (!validPlacement) {
						System.out.println("Not a valid placement. Try again.");
					}
					displayOceanGrid(currentPlayer);
				}
			}
		}
	}

	// prints empty board //TODO: will need to be removed or deactivated once GUI is
	// in place
	public static void displayOceanGrid(Player currentPlayer) {
		int rows = 10; // can be modified to make the game board larger
		int cols = 10;

		System.out.println("\t" + currentPlayer.getName() + "'s Ocean Grid");
		System.out.print("  C");
		for (int i = 0; i < rows; i++) // prints column labels
			System.out.print("  " + i);

		System.out.println();

		for (int i = 0; i < rows + 2; i++) { // start printing grid
			if (i == 0)
				System.out.print("R ");
			if (i == 11)
				System.out.print("  "); // for indenting bottom line
			if (i % 11 == 0)
				System.out.println("---------------------------------");
			else {

				System.out.print((i - 1) + " |"); // prints row labels and leading border

				for (int j = 0; j < cols; j++) { // print each grid value after formatting

					int gridValue = currentPlayer.getOceanGrid().getGridLocationValue(i - 1, j);
					String formattedPrint = String.valueOf(gridValue);

					if (gridValue == -1) { // no ship in grid
						formattedPrint = "~";
					} else if (gridValue == 0) { // miss in grid
						formattedPrint = "@";
					} else if (currentPlayer.getOceanGrid().getShipWithID(gridValue).checkForHit(i - 1, j)) {
						formattedPrint = "X"; // check for hit on ship
					}
					System.out.printf("%3s", formattedPrint); // print each formatted grid value with spacing
				}
				System.out.println(" |"); // trailing border
			}
		}
		System.out.println();
	}

	// display target grid, properly formatted; target
	public static void displayTargetGrid(Player currentPlayer) {
		int rows = 10; // can be modified to make the game board larger
		int cols = 10;

		System.out.println("\t" + currentPlayer.getName() + "'s Target Grid");
		System.out.print("  C");
		for (int i = 0; i < rows; i++) // prints column labels
			System.out.print("  " + i);

		System.out.println();

		for (int i = 0; i < rows + 2; i++) { // start printing grid
			if (i == 0)
				System.out.print("R ");
			if (i == 11)
				System.out.print("  "); // for indenting bottom line
			if (i % 11 == 0)
				System.out.println("---------------------------------");
			else {

				System.out.print((i - 1) + " |"); // prints row labels

				for (int j = 0; j < cols; j++) { // print each grid value after formatting

					int gridValue = currentPlayer.getTargetGrid().getGridLocationValue(i - 1, j);
					String formattedPrint = String.valueOf(gridValue);
					if (gridValue == -1) {
						formattedPrint = "~";
					} else if (gridValue == 0) {
						formattedPrint = "@";
					} else {
						formattedPrint = "X";
					}
					System.out.printf("%3s", formattedPrint);
				}
				System.out.println(" |");
			}
		}
		System.out.println();
	}

	// just displays both grids; formatted for text output
	public static void displayBothGrids(Player currentPlayer) {
		displayTargetGrid(currentPlayer);
		displayOceanGrid(currentPlayer);
	}

	// prompts user for shot, then returns array with [row,col] //TODO: replace with
	// GUI input
	public static int[] shotPrompt(Player currentPlayer) {

		System.out.println(currentPlayer.getName() + "'s turn");
		int[] coordinates = new int[2];
		System.out.print("Enter row: ");
		coordinates[0] = keyboard.nextInt();
		System.out.print("Enter column: ");
		coordinates[1] = keyboard.nextInt();
		System.out.println();
		return coordinates;
	}

	// processes a shot
	public static void bombardPlayer(int row, int col, Player shootingPlayer, Player receivingPlayer) {
		receivingPlayer.getOceanGrid().setCurrentShot(row, col); // set shot
		shootingPlayer.getTargetGrid().setCurrentShot(row, col);
		boolean hit = receivingPlayer.getOceanGrid().processShot(); // process shot against receivingPlayer

		if (hit) {
			Ship shipHit = receivingPlayer.getOceanGrid().getShipAt(row, col); // fetch ship hit
			System.out.println(receivingPlayer.getName() + "'s " + shipHit.getName() + " was hit!");
			shootingPlayer.getTargetGrid().isHit(row, col);
			if (shipHit.isSunk()) {
				System.out.println("The " + shipHit.getName() + " was sunk!");
			}

		} else {
			System.out.println("Miss!\n");
			shootingPlayer.getTargetGrid().isMiss(row, col);
		}
		System.out.println();
	}

	// check for victory condition; returns winner # or -1 if no winner yet
	public static int checkForWinner(Player player1, Player player2) {
		if (player1.getRemainingShips() == 0) {
			player1.addLoss();
			player2.addWin();
			return 2;
		} else if (player2.getRemainingShips() == 0) {
			player2.addLoss();
			player1.addWin();
			return 1;
		}
		return -1;
	}

}
