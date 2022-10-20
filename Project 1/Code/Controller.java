/* Controller is part of the MVC design pattern and is used as a point of communication between the model and view.  It is 
 * also responsible for initializing the game environments and connecting to another player.
 * Authors: Ryan Collins, John Schmidt
 * Last Update: 10/19/2022
 */

import java.io.IOException;
import java.util.Scanner;

public class Controller implements Subject, Observer {
	private Viewer gameViewer = null; // this is so the controller can observe the viewer
	private Observer viewObserver = null; // this is so the viewer can observe the controller

	final int BOARD_ROWS = 10; // can be changed for larger board size
	final int BOARD_COLS = 10;
	private String name; // for passing current player's name
	private String serverName;
	private Role thisPlayerRole = null; // applications role (server or client)
	private int currentWinner = -1; // for passing along current winner
	private int rowColArray[]; // for passing along current shot
	private Player thisPlayer; // TODO: Move to model
	private Player opponentShadow; // TODO:Move to model
	private State thisPlayerState;

	// constructor
	public Controller() {
		thisPlayerState = new State();
	}

	public void playGame() throws IOException {

		while (thisPlayerState.currentState == State.SETUP) {
		} // wait for setup to complete

		System.out.print("Enter your name: "); // TODO: move this to the GUI
		thisPlayer = new Player(name); // move to model

		// Shadow is for maintaining a copy of the opponents grid so less communication
		// is needed between players. After current shot is sent to opponent, all game
		// logic is applied to the shadow, and the opponent does the same. Shadow should
		// always be an exact copy of the second player. This also minimizes the need to
		// rewrite code from the original version where both players were stored on the
		// same console
		opponentShadow = new Player("Opponent"); // move to model

		while (thisPlayerState.currentState == State.SELECTING_HOST) {
		} // wait for host selection

		System.out.print("Enter Server device name: "); // TODO: move to gui

		while (thisPlayerState.currentState == State.CONNECT_TO_HOST) {
		} // wait for entering of server name from client, skipped if server

		// if selection was client, thisPlayerRole will still be null
		if (thisPlayerRole == null) {
			thisPlayerRole = new BattleShipClient(serverName);
			thisPlayerState.currentState = State.SHIP_PLACEMENT;
		}

		// sets up the client side or server side of the connection, based on the
		// application's role; this is a BLOCKING method call (client and server
		// synchronized here)
		thisPlayerRole.startConnection();

		// Players swap names, sever sends first
		swapNames();

		while (thisPlayerState.currentState == State.SHIP_PLACEMENT) {
		}

		// place ships
		placeShips(thisPlayer);//TODO: implement in gui
		System.out.println("Local Ships placed\n"); // TODO: move to GUI

		// Players swap ocean grids, sever sends first
		swapGrids();

		System.out.println("Ships placement completed\n"); //TODO: move to gui

		// the next if/else preserves play order; it has the server shoot first
		if (thisPlayerRole instanceof BattleShipServer) {
			System.out.println(thisPlayer.getName() + "'s the server.  The server fires first! \n");
			rowColArray = shotPrompt(thisPlayer); // player1's turn
			System.out.println("Sending current shot.\n"); // DEBUG for testing
			thisPlayerRole.send(rowColArray);
			System.out.println("Current shot sent.\n"); // DEBUG for testing
			// bombardPlayer(rowColArray[0], rowColArray[1], thisPlayer, opponentShadow);
			displayBothGrids(thisPlayer); // TODO: move to GUI
			currentWinner = checkForWinner(thisPlayer, opponentShadow);

		} else if (thisPlayerRole instanceof BattleShipClient) {
			System.out.println(thisPlayer.getName() + "'s the client.  The server fires first!\n");
			// TODO: move to GUI
		}

		/*
		 * while (currentWinner == -1) { // main play loop
		 * 
		 * System.out.println("Awaiting incoming volley!\n");
		 * rowColArray = (int[]) thisPlayerRole.receive(); // get shot from opponent
		 * bombardPlayer(rowColArray[0], rowColArray[1], opponentShadow, thisPlayer);
		 * displayBothGrids(thisPlayer); // TODO: Move to GUI
		 * currentWinner = checkForWinner(thisPlayer, opponentShadow);
		 * 
		 * if (currentWinner == -1) {
		 * 
		 * rowColArray = shotPrompt(thisPlayer); // this player's turn
		 * System.out.println("Sending current shot.\n"); // DEBUG for testing
		 * thisPlayerRole.send(rowColArray);
		 * System.out.println("Current shot sent.\n"); // DEBUG for testing
		 * bombardPlayer(rowColArray[0], rowColArray[1], thisPlayer, opponentShadow);
		 * displayBothGrids(thisPlayer); // TODO: Move to GUI
		 * currentWinner = checkForWinner(thisPlayer, opponentShadow);
		 * }
		 * }
		 */

		while (thisPlayerState.currentState == State.SELECTING_VOLLEY) {
		} // wait for volley to be selected; currentState is changed when target button
			// pressed

		bombardPlayer(thisPlayer, opponentShadow);
		thisPlayerRole.send(rowColArray);
		currentWinner = checkForWinner(thisPlayer, opponentShadow);
		if (currentWinner != -1) {
			thisPlayerState.currentState = State.END_GAME; // move currentState to end game if winner declared
		}

		// declare winners
		// TODO: move all this stuff to the GUI
		if (currentWinner == 1)
			System.out.println("We have a winner!  " + thisPlayer.getName() + " is this round's winnner.");
		else
			System.out.println("We have a winner!  " + opponentShadow.getName() + " is this round's winnner.");

		System.out.println(thisPlayer.getName() + ": " + thisPlayer.getWins() + " wins");
		System.out.println(opponentShadow.getName() + ": " + opponentShadow.getWins() + " wins");
		thisPlayerRole.closeConnection();

		// TODO: Encapsulate in a loop if desired
	}

	// Players swap names, sever sends first
	private void swapNames() throws IOException {
		if (thisPlayerRole instanceof BattleShipServer) {
			thisPlayerRole.send(thisPlayer.getName()); // send this player's name to opponent
			opponentShadow.setName((String) thisPlayerRole.receive()); // receive opponents name

		} else if (thisPlayerRole instanceof BattleShipClient) {
			opponentShadow.setName((String) thisPlayerRole.receive()); // receive opponents name
			thisPlayerRole.send(thisPlayer.getName()); // send this player's name to opponent
		}
	}

	// Players swap ocean grids, sever sends first
	private void swapGrids() throws IOException {
		if (thisPlayerRole instanceof BattleShipServer) {
			thisPlayerRole.send(thisPlayer.getOceanGrid().getGridArray()); // send to client
			opponentShadow.getOceanGrid().copyGridArray((int[][]) thisPlayerRole.receive());

		} else if (thisPlayerRole instanceof BattleShipClient) {
			opponentShadow.getOceanGrid().copyGridArray((int[][]) thisPlayerRole.receive());
			thisPlayerRole.send(thisPlayer.getOceanGrid().getGridArray()); // send to server
		}
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

	// processes a shot; precondition: current shot is set for shooting player
	public static void bombardPlayer(Player shootingPlayer, Player receivingPlayer) {
		int row = shootingPlayer.getOceanGrid().getCurrentShot()[0];
		int col = shootingPlayer.getOceanGrid().getCurrentShot()[0];
		boolean hit = receivingPlayer.getOceanGrid().processShot(); // process shot against receivingPlayer

		if (hit) {
			Ship shipHit = receivingPlayer.getOceanGrid().getShipAt(row, col); // fetch ship hit
			System.out.println(receivingPlayer.getName() + "'s " + shipHit.getName() + " was hit!"); // TODO: move to
																										// gui
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

	@Override
	public void update() {

	}

	// this update is for updating the name of this console's player; TODO: tie to
	// name box
	public void update(String playerOrServerName) {
		if (thisPlayerState.currentState == State.SETUP) {
			name = playerOrServerName;
			thisPlayerState.currentState = State.SELECTING_HOST; // moves to next state
		} else if (thisPlayerState.currentState == State.CONNECT_TO_HOST) {
			serverName = playerOrServerName;
		}
	}

	// this update is for updating which player is the server
	public void update(boolean isServer) {
		if (thisPlayerState.currentState == State.SELECTING_HOST) { // in server setup state
			if (isServer) {
				thisPlayerRole = new BattleShipServer();
				System.out.print("You are the " + thisPlayerRole.getRole() + ". Your device name is: "
						+ ((BattleShipServer) thisPlayerRole).getServerName()); // TODO: move to gui
				thisPlayerState.currentState = State.SHIP_PLACEMENT; // skip host name entry
			} else {
				thisPlayerState.currentState = State.CONNECT_TO_HOST; // move to receive host name
			}
		}
	}

	// this update is for incoming shot information from this player's Viewer
	public void update(int row, int col) {
		if (thisPlayerState.currentState == State.SELECTING_VOLLEY) {
			thisPlayer.getOceanGrid().setCurrentShot(row, col);
			opponentShadow.getOceanGrid().setCurrentShot(row, col);
			thisPlayerState.currentState = State.AWAITING_INCOMING_VOLLEY;
		}
	}

	// this update is for placing ships automatically
	public void updateAutoPlaceShips() {
		thisPlayer.getOceanGrid().autoPlaceShips();
		if (thisPlayerRole instanceof BattleShipServer) {
			thisPlayerState.currentState = State.SELECTING_VOLLEY;
		} else {
			thisPlayerState.currentState = State.AWAITING_INCOMING_VOLLEY;
		}
	}

	// registers the Viewer and Model controller; Precondition: the viewer must be
	// registered first
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

}
