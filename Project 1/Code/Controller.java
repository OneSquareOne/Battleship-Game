/* Controller is part of the MVC design pattern and is used as a point of communication between the model and view.  It is 
 * also responsible for initializing the game environments and connecting to another player.
 * Authors: Ryan Collins, John Schmidt
 * Last Update: 10/20/2022
 */

import java.io.IOException;

public class Controller {

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
	private Viewer gameViewer;
	private Model gameModel;

	// constructor
	public Controller() {
		thisPlayerState = new State();
		rowColArray = new int[2];
	}

	public void playGame() throws IOException, InterruptedException {

		gameViewer.addNotification("Player, enter your name. ");

		// wait for setup to complete
		while (thisPlayerState.currentState == State.SETUP) {
		}

		gameViewer.addNotification(name + ", select server or client.");

		thisPlayer = new Player(name); // TODO: move to model
		opponentShadow = new Player("Opponent"); // TODO: move to model

		// wait for host selection
		while (thisPlayerState.currentState == State.SELECTING_SERVER) {
		}

		if (thisPlayerState.currentState == State.CONNECT_TO_SERVER) { // prompt for client
			gameViewer.addNotification(name + ", enter the server's device name.");
		}

		// wait for entering of server name from client, skipped if server
		while (thisPlayerState.currentState == State.CONNECT_TO_SERVER) {
		}

		// sets up the client side or server side of the connection, based on the
		// application's role; this is a BLOCKING method call (client and server
		// synchronized here)
		thisPlayerRole.startConnection();

		gameViewer.activateShipPlacement(); // activates ship buttons on viewer

		thisPlayerState.currentState = State.SHIP_PLACEMENT; // connection successful

		// Players swap names, sever sends first; BLOCKING method call
		swapNames();
		gameViewer.addNotification("Connection successful.  Opponent: " + opponentShadow.getName());

		
		// MAIN PLAY WHILE LOOP
		while (thisPlayerState.currentState == State.SHIP_PLACEMENT) {

			gameViewer.addNotification(thisPlayer.getName() + ", place your ships.");

			// waiting for ship placement in Viewer
			while (thisPlayerState.currentState == State.SHIP_PLACEMENT) {
			}

			gameViewer.shipPlacementComplete();
			gameViewer.addNotification("All ships placed successfully.");
			gameViewer.addNotification("Local ships placed, waiting for " + opponentShadow.getName() + ".");

			// Players swap ocean grids, sever sends first; BLOCKING method call
			swapGrids();

			gameViewer.addNotification("Ships placement completed for both players.");

			// the next if/else preserves play order; it has the server shoot first
			if (thisPlayerRole instanceof BattleShipServer) {
				gameViewer.addNotification("The server fires first. It's your turn!");
				thisPlayerState.currentState = State.SELECTING_VOLLEY;
			} else if (thisPlayerRole instanceof BattleShipClient) {
				gameViewer.addNotification("The server fires first. It's " + opponentShadow.getName() + "'s turn");
				thisPlayerState.currentState = State.AWAITING_INCOMING_VOLLEY;
			}

			while (currentWinner == -1) { // main play loop

				// this player's turn to shoot
				if (thisPlayerState.currentState == State.SELECTING_VOLLEY) {
					gameViewer.setTurnLabelPlayersTurn();
					while (thisPlayerState.currentState == State.SELECTING_VOLLEY) {
					} // wait for volley to be selected from GUI

					bombardPlayer(thisPlayer, opponentShadow); // process shot from current player
					thisPlayerRole.send(rowColArray); // send shot to real opponent
					updateViewerTargetGridLocation(rowColArray[0], rowColArray[1]);
					currentWinner = checkForWinner(thisPlayer, opponentShadow); // changes state if winner
				}

				// if game isn't over, opponent's turn to shoot
				if (thisPlayerState.currentState == State.AWAITING_INCOMING_VOLLEY) {
					gameViewer.setTurnLabelOpponentsTurn();
					rowColArray = (int[]) thisPlayerRole.receive(); // get shot from opponent
					shotFromOpponent(rowColArray[0], rowColArray[1]); // sets current shot and changes state
					bombardPlayer(opponentShadow, thisPlayer);// process shot from shadow opponent
					updateViewerOceanGridLocation(rowColArray[0], rowColArray[1]);
					thisPlayerState.currentState = State.SELECTING_VOLLEY;
					currentWinner = checkForWinner(thisPlayer, opponentShadow);// changes state if winner
				}
			}

			// declare winners
			if (currentWinner == 1)
				gameViewer.winCondition();
			else
				gameViewer.loseCondition();

			// wait for player end game choice; play again sets state to ship placement and restarts
			// loop. End game sets state to setup so loop is not repeated and connection is closed
			while (thisPlayerState.currentState == State.END_GAME) {
			}
		}//end of main play (while) loop

		thisPlayerRole.closeConnection();
	}// end of playGame

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

	public boolean tryPlaceShip(int row, int col, int shipID, boolean horizontal) {

		boolean shipPlaced = false;
		if (thisPlayerState.currentState == State.SHIP_PLACEMENT) {

			if (shipID < 1) { // invalid ship ID
				return shipPlaced;
			}

			Ship tempShip = thisPlayer.getOceanGrid().getShipWithID(shipID); // get correct ship

			// if bow position is [-1,-1], then ship isn't placed yet, so try to place it
			if (tempShip.getBowPosition()[0] == -1) {
				shipPlaced = thisPlayer.getOceanGrid().placeShip(row, col, tempShip, horizontal);
			}

			if (shipPlaced) { // ship was placed; update state if that was the last one
				gameViewer.addNotification(tempShip.getName() + " placed successfully.");
				updateViewerOceanGridWithShip(tempShip);

				if (thisPlayer.getShipsToBePlaced() == 0) { // all ships placed
					thisPlayerState.currentState = State.SELECTING_VOLLEY;
				}
			}
		}
		return shipPlaced;
	}

	// updates the viewer's target grid at given coordinates
	public void updateViewerTargetGridLocation(int row, int col) {
		String imageFilePath = thisPlayer.getTargetGrid().getImagePath(row, col);
		gameViewer.updateTargetGrid(row, col, imageFilePath);
	}

	// updates the viewer's entire target grid
	public void updateViewerEntireTargetGrid() {
		for (int row = 0; row < BOARD_ROWS; row++) {
			for (int col = 0; col < BOARD_COLS; col++) {
				String imageFilePath = thisPlayer.getTargetGrid().getImagePath(row, col);
				gameViewer.updateTargetGrid(row, col, imageFilePath);
			}
		}
	}

	// updates the viewer's ocean grid at given coordinates
	public void updateViewerOceanGridLocation(int row, int col) {
		String imageFilePath = thisPlayer.getOceanGrid().getImagePath(row, col);
		gameViewer.updateOceanGrid(row, col, imageFilePath);
	}

	// updates the viewer's entire ocean grid
	public void updateViewerEntireOceanGrid() {
		for (int row = 0; row < BOARD_ROWS; row++) {
			for (int col = 0; col < BOARD_COLS; col++) {
				String imageFilePath = thisPlayer.getOceanGrid().getImagePath(row, col);
				gameViewer.updateOceanGrid(row, col, imageFilePath);
			}
		}
	}

	// updates only the viewer's ocean grid coordinates where ship is placed
	public void updateViewerOceanGridWithShip(Ship ship) {
		for (int i = 0; i < ship.getSize(); i++) {
			if (ship.isHorizontal()) {
				int row = ship.getBowPosition()[0]; // horizontal, row doesn't change
				int col = ship.getBowPosition()[1] + i; // go through each ship spot
				updateViewerOceanGridLocation(row, col); // update that spot
			} else {
				int row = ship.getBowPosition()[0] + i; // go through each ship spot
				int col = ship.getBowPosition()[1]; // vertical, col doesn't change
				updateViewerOceanGridLocation(row, col); // update that spot
			}
		}
	}

	// processes a shot; precondition: current shot is set for shooting player
	public void bombardPlayer(Player shootingPlayer, Player receivingPlayer) {
		int row = shootingPlayer.getOceanGrid().getCurrentShot()[0];
		int col = shootingPlayer.getOceanGrid().getCurrentShot()[1];
		boolean hit = receivingPlayer.getOceanGrid().processShot(); // process shot against receivingPlayer

		if (hit) {
			Ship shipHit = receivingPlayer.getOceanGrid().getShipAt(row, col); // fetch ship hit
			gameViewer.addNotification(receivingPlayer.getName() + "'s " + shipHit.getName() + " was hit!");
			shootingPlayer.getTargetGrid().isHit(row, col);
			if (shipHit.isSunk()) {
				gameViewer.addNotification("The " + shipHit.getName() + " was sunk!");
				if (shootingPlayer == thisPlayer) {
					gameViewer.enemyShipSunk(shipHit.getID()); // changes ship image to sunk
				}
			}
		} else {
			shootingPlayer.getTargetGrid().isMiss(row, col);
			gameViewer.addNotification(shootingPlayer.getName() + "'s volley missed!");
		}
	}

	// check for victory condition; returns winner # or -1 if no winner yet
	public int checkForWinner(Player player1, Player player2) {
		if (player1.getRemainingShips() == 0) {
			player1.addLoss();
			player2.addWin();
			thisPlayerState.currentState = State.END_GAME;
			return 2;
		} else if (player2.getRemainingShips() == 0) {
			player2.addLoss();
			player1.addWin();
			thisPlayerState.currentState = State.END_GAME;
			return 1;
		}
		return -1;
	}

	// incoming shot information from the opponent's target grid
	public void shotFromOpponent(int row, int col) {
		if (thisPlayerState.currentState == State.AWAITING_INCOMING_VOLLEY) {
			rowColArray[0] = row;
			rowColArray[1] = col;
			thisPlayer.getOceanGrid().setCurrentShot(row, col);
			opponentShadow.getOceanGrid().setCurrentShot(row, col);
			thisPlayerState.currentState = State.SELECTING_VOLLEY;
		}
	}

	// incoming shot information from this player's Viewer
	public void shotFromViewer(int row, int col) {
		if (thisPlayerState.currentState == State.SELECTING_VOLLEY) {
			rowColArray[0] = row;
			rowColArray[1] = col;
			thisPlayer.getOceanGrid().setCurrentShot(row, col);
			opponentShadow.getOceanGrid().setCurrentShot(row, col);
			thisPlayerState.currentState = State.AWAITING_INCOMING_VOLLEY;
			gameViewer.addNotification(thisPlayer.getName() + " shoots at (" + row + ", " + col + ").");
		}
	}

	// this update is for placing ships automatically
	public boolean autoPlaceShips() {
		boolean stateChanged = false;
		if (thisPlayerState.currentState == State.SHIP_PLACEMENT) {
			thisPlayer.getOceanGrid().autoPlaceShips();
			if (thisPlayerRole instanceof BattleShipServer) {
				thisPlayerState.currentState = State.SELECTING_VOLLEY;
			} else {
				thisPlayerState.currentState = State.AWAITING_INCOMING_VOLLEY;
			}
			updateViewerEntireOceanGrid();
			stateChanged = true;
		}
		return stateChanged;
	}

	// allows the controller to access the model
	public void registerModel(Model model) {
		gameModel = model;
	}

	// allows the controller to access the model
	public void registerViewer(Viewer view) {
		gameViewer = view;
	}

	// chooses the server role for this machine
	public boolean selectServerRole() {
		boolean stateChanged = false;
		if (thisPlayerState.currentState == State.SELECTING_SERVER) {
			gameViewer.addNotification("Server role selected.");
			thisPlayerRole = new BattleShipServer();
			thisPlayerState.currentState = State.AWAITING_CLIENT_CONNECTION;
			gameViewer.addNotification(
					"Give device name to client: " + ((BattleShipServer) thisPlayerRole).getServerName());
			stateChanged = true;
		}
		return stateChanged;
	}

	// selects the client role for this machine
	public boolean selectClientRole(String name) {
		boolean stateChanged = false;
		if (thisPlayerState.currentState == State.SELECTING_SERVER) {
			serverName = name;
			thisPlayerRole = new BattleShipClient(serverName);
			thisPlayerState.currentState = State.SHIP_PLACEMENT;
			stateChanged = true;
		}
		return stateChanged;
	}

	// returns the current player's state
	public int getCurrentState() {
		return thisPlayerState.currentState;
	}

	// sets this player's name, changes state
	public boolean setPlayerName(String newName) {
		boolean stateChanged = false;
		if (thisPlayerState.currentState == State.SETUP) {
			name = newName;
			thisPlayerState.currentState = State.SELECTING_SERVER;
			stateChanged = true;
		}
		return stateChanged;
	}

	// return opponents name (players name is already known by user entry)
	public String getOpponentName() {
		return opponentShadow.getName();
	}

	// returns game to play loop if new game button is selected
	public void startNewGame(boolean newGame) {
		if (newGame && (thisPlayerState.currentState == State.END_GAME)) {
			thisPlayerState.currentState = State.SHIP_PLACEMENT;
		} else if (!newGame && (thisPlayerState.currentState == State.END_GAME)) {
			thisPlayerState.currentState = State.SETUP;
		}
	}
}