/* Controller is part of the MVC design pattern and is used as a point of communication between the 
 * model and view.  It is also responsible for initializing the game environments and connecting to 
 * another player.
 * Authors: Ryan Collins, John Schmidt
 * Last Update: 10/29/2022
 */

import java.io.IOException;

public class Controller {

	private String name; // for passing current player's name
	private int currentWinner; // for passing along current winner
	private int rowColArray[]; // for passing along current shot
	private Viewer gameViewer;
	private Model gameModel;
	private Sound soundEffect;

	// constructor
	public Controller() {
		rowColArray = new int[2];
		soundEffect = new Sound();
		currentWinner = -1;
	}

	// initiates the main game sequence, starting with name and role selection
	public void playGame() throws IOException, InterruptedException {

		soundEffect.mainPlayLoop();
		gameViewer.addNotification("Player, enter your name. ");

		// wait for setup to complete
		while (gameModel.getState() == State.SETUP) {
		}

		gameViewer.addNotification(name + ", select server or client.");

		gameModel.getThisPlayer().setName(name); // set this player's name

		// wait for host selection
		while (gameModel.getState() == State.SELECTING_SERVER) {
		}

		if (gameModel.getState() == State.CONNECT_TO_SERVER) { // prompt for client
			gameViewer.addNotification(name + ", enter the server's device name.");
		}

		// wait for entering of server name from client, skipped if server
		while (gameModel.getState() == State.CONNECT_TO_SERVER) {
		}

		// sets up the client side or server side of the connection, based on the
		// application's role; this is a BLOCKING method call (client and server
		// synchronized here)
		gameModel.getRole().startConnection();

		gameViewer.activateShipPlacement(); // activates ship buttons on viewer

		gameModel.setState(State.SHIP_PLACEMENT); // connection successful

		// Players swap names, sever sends first; BLOCKING method call
		swapNames();
		gameViewer.addNotification("Connection successful.  Opponent: " + gameModel.getOpponentShadow().getName());

		// MAIN PLAY WHILE LOOP
		while (gameModel.getState() == State.SHIP_PLACEMENT) {

			gameViewer.addNotification(gameModel.getThisPlayer().getName() + ", place your ships.");

			// waiting for ship placement in Viewer
			while (gameModel.getState() == State.SHIP_PLACEMENT) {
			}

			gameViewer.shipPlacementComplete();
			gameViewer.addNotification("All ships placed successfully.");
			gameViewer.addNotification(
					"Local ships placed, waiting for " + gameModel.getOpponentShadow().getName() + ".");

			// Players swap ocean grids, sever sends first; BLOCKING method call
			swapGrids();

			gameViewer.addNotification("Ships placement completed for both players.");

			// the next if/else preserves play order; it has the server shoot first
			if (gameModel.getRole() instanceof BattleShipServer) {
				gameViewer.addNotification("The server fires first. It's your turn!");
				gameModel.setState(State.SELECTING_VOLLEY);
			} else if (gameModel.getRole() instanceof BattleShipClient) {
				gameViewer.addNotification(
						"The server fires first. It's " + gameModel.getOpponentShadow().getName() + "'s turn");
				gameModel.setState(State.AWAITING_INCOMING_VOLLEY);
			}

			while (currentWinner == -1) { // main play loop

				// this player's turn to shoot
				if (gameModel.getState() == State.SELECTING_VOLLEY) {
					gameViewer.setTurnLabelPlayersTurn();
					while (gameModel.getState() == State.SELECTING_VOLLEY) {
					} // wait for volley to be selected from GUI

					bombardPlayer(gameModel.getThisPlayer(), gameModel.getOpponentShadow());
					gameModel.getRole().send(rowColArray); // send shot to real opponent
					updateViewerTargetGridLocation(rowColArray[0], rowColArray[1]);
					currentWinner = checkForWinner(gameModel.getThisPlayer(), gameModel.getOpponentShadow());
				}

				// if game isn't over, opponent's turn to shoot
				if (gameModel.getState() == State.AWAITING_INCOMING_VOLLEY) {
					gameViewer.setTurnLabelOpponentsTurn();
					rowColArray = (int[]) gameModel.getRole().receive(); // get shot from opponent
					shotFromOpponent(rowColArray[0], rowColArray[1]); // sets current shot and changes state
					bombardPlayer(gameModel.getOpponentShadow(), gameModel.getThisPlayer());
					updateViewerOceanGridLocation(rowColArray[0], rowColArray[1]);

					// faster paced music when down to one ship
					if (gameModel.getThisPlayer().getRemainingShips() == 1) {
						soundEffect.stopAll();
						soundEffect.lastShipLoop();
					}

					gameModel.setState(State.SELECTING_VOLLEY);
					currentWinner = checkForWinner(gameModel.getThisPlayer(), gameModel.getOpponentShadow());
				}
			}

			// declare winners
			if (currentWinner == 1) {
				gameViewer.winCondition();
				soundEffect.stopAll();
				soundEffect.playVictory();
			} else {
				gameViewer.loseCondition();
				soundEffect.playLoss();
			}

			// wait for player end game choice; play again sets state to ship placement and
			// restarts loop. End game sets state to setup so loop is not repeated and
			// connection is closed
			while (gameModel.getState() == State.END_GAME) {
			}
		} // end of main play (while) loop

		gameModel.getRole().closeConnection();
	}// end of playGame

	// Players swap names, sever sends first
	private void swapNames() throws IOException {
		if (gameModel.getRole() instanceof BattleShipServer) {
			gameModel.getRole().send(gameModel.getThisPlayer().getName()); // send this player's name to opponent
			gameModel.getOpponentShadow().setName((String) gameModel.getRole().receive()); // receive opponents name

		} else if (gameModel.getRole() instanceof BattleShipClient) {
			gameModel.getOpponentShadow().setName((String) gameModel.getRole().receive()); // receive opponents name
			gameModel.getRole().send(gameModel.getThisPlayer().getName()); // send this player's name to opponent
		}
	}

	// Players swap ocean grids, sever sends first
	private void swapGrids() throws IOException {
		if (gameModel.getRole() instanceof BattleShipServer) {
			gameModel.getRole().send(gameModel.getThisPlayer().getOceanGrid().getGridArray()); // send to client
			gameModel.getOpponentShadow().getOceanGrid().copyGridArray((int[][]) gameModel.getRole().receive());

		} else if (gameModel.getRole() instanceof BattleShipClient) {
			gameModel.getOpponentShadow().getOceanGrid().copyGridArray((int[][]) gameModel.getRole().receive());
			gameModel.getRole().send(gameModel.getThisPlayer().getOceanGrid().getGridArray()); // send to server
		}
	}

	// method modifies an incoming OceanGrid to targetGrid formatting. Both players
	// store a copy of their own ocean grid and the target grid representing their
	// opponent
	public void copyOpponentsOceanGrid(OceanGrid copiedOceanGrid, Player opponentShadow) {
		for (int i = 0; i < Model.BOARD_ROWS; i++) {
			for (int j = 0; j < Model.BOARD_COLS; j++) {
				int value = copiedOceanGrid.getGridLocationValue(i, j); // get value from reference
				gameModel.getOpponentShadow().getOceanGrid().updateGrid(i, j, value); // copy to shadow
			}
		}
	}

	//attempt to place a ship, return false if unsuccessful
	public boolean tryPlaceShip(int row, int col, int shipID, boolean horizontal) {

		boolean shipPlaced = false;
		if (gameModel.getState() == State.SHIP_PLACEMENT) {

			if (shipID < 1) { // invalid ship ID
				return shipPlaced;
			}

			Ship tempShip = gameModel.getThisPlayer().getOceanGrid().getShipWithID(shipID); // get correct ship

			// if bow position is [-1,-1], then ship isn't placed yet, so try to place it
			if (tempShip.getBowPosition()[0] == -1) {
				shipPlaced = gameModel.getThisPlayer().getOceanGrid().placeShip(row, col, tempShip, horizontal);
			}

			if (shipPlaced) { // ship was placed; update state if that was the last one
				gameViewer.addNotification(tempShip.getName() + " placed successfully.");
				updateViewerOceanGridWithShip(tempShip);

				if (gameModel.getThisPlayer().getShipsToBePlaced() == 0) { // all ships placed
					gameModel.setState(State.SELECTING_VOLLEY);
				}
			}
		}
		return shipPlaced;
	}

	// updates the viewer's target grid at given coordinates
	public void updateViewerTargetGridLocation(int row, int col) {
		String imageFilePath = gameModel.getThisPlayer().getTargetGrid().getImagePath(row, col);
		gameViewer.updateTargetGrid(row, col, imageFilePath);
	}

	// updates the viewer's entire target grid
	public void updateViewerEntireTargetGrid() {
		for (int row = 0; row < Model.BOARD_ROWS; row++) {
			for (int col = 0; col < Model.BOARD_COLS; col++) {
				String imageFilePath = gameModel.getThisPlayer().getTargetGrid().getImagePath(row, col);
				gameViewer.updateTargetGrid(row, col, imageFilePath);
			}
		}
	}

	// updates the viewer's ocean grid at given coordinates
	public void updateViewerOceanGridLocation(int row, int col) {
		String imageFilePath = gameModel.getThisPlayer().getOceanGrid().getImagePath(row, col);
		gameViewer.updateOceanGrid(row, col, imageFilePath);
	}

	// updates the viewer's entire ocean grid
	public void updateViewerEntireOceanGrid() {
		for (int row = 0; row < Model.BOARD_ROWS; row++) {
			for (int col = 0; col < Model.BOARD_COLS; col++) {
				String imageFilePath = gameModel.getThisPlayer().getOceanGrid().getImagePath(row, col);
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
			gameViewer.addNotification(
					shootingPlayer.getName() + " hit " + receivingPlayer.getName() + "'s " + shipHit.getName() + "!");
			shootingPlayer.getTargetGrid().isHit(row, col);

			if (shootingPlayer == gameModel.getThisPlayer()) {
				soundEffect.playHitO();
			} else {
				soundEffect.playHitP();
			}

			if (shipHit.isSunk()) {
				gameViewer.addNotification("The " + shipHit.getName() + " was sunk!");
				if (shootingPlayer == gameModel.getThisPlayer()) {
					gameViewer.enemyShipSunk(shipHit.getID()); // changes ship image to sunk
				}
			}
		} else {
			shootingPlayer.getTargetGrid().isMiss(row, col);
			gameViewer.addNotification(shootingPlayer.getName() + "'s volley missed!");

			if (shootingPlayer == gameModel.getThisPlayer()) {
				soundEffect.playMissO();
			} else {
				soundEffect.playMissP();
			}
		}
	}

	// check for victory condition; returns winner # or -1 if no winner yet
	public int checkForWinner(Player player1, Player player2) {
		if (player1.getRemainingShips() == 0) {
			player1.addLoss();
			player2.addWin();
			gameModel.setState(State.END_GAME);
			return 2;
		} else if (player2.getRemainingShips() == 0) {
			player2.addLoss();
			player1.addWin();
			gameModel.setState(State.END_GAME);
			return 1;
		}
		return -1;
	}

	// incoming shot information from the opponent's target grid
	public void shotFromOpponent(int row, int col) {
		if (gameModel.getState() == State.AWAITING_INCOMING_VOLLEY) {
			rowColArray[0] = row;
			rowColArray[1] = col;
			gameModel.getThisPlayer().getOceanGrid().setCurrentShot(row, col);
			gameModel.getOpponentShadow().getOceanGrid().setCurrentShot(row, col);
			gameModel.setState(State.SELECTING_VOLLEY);
		}
	}

	// incoming shot information from this player's Viewer
	public void shotFromViewer(int row, int col) {
		if (gameModel.getState() == State.SELECTING_VOLLEY) {
			rowColArray[0] = row;
			rowColArray[1] = col;
			gameModel.getThisPlayer().getOceanGrid().setCurrentShot(row, col);
			gameModel.getOpponentShadow().getOceanGrid().setCurrentShot(row, col);
			gameModel.setState(State.AWAITING_INCOMING_VOLLEY);
		}
	}

	// this update is for placing ships automatically
	public boolean autoPlaceShips() {
		boolean stateChanged = false;
		if (gameModel.getState() == State.SHIP_PLACEMENT) {
			gameModel.getThisPlayer().getOceanGrid().autoPlaceShips();
			if (gameModel.getRole() instanceof BattleShipServer) {
				gameModel.setState(State.SELECTING_VOLLEY);
			} else {
				gameModel.setState(State.AWAITING_INCOMING_VOLLEY);
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
		if (gameModel.getState() == State.SELECTING_SERVER) {
			gameViewer.addNotification("Server role selected.");
			gameModel.setServerRole();
			gameModel.setState(State.AWAITING_CLIENT_CONNECTION);
			gameViewer.addNotification(
					"Give device name to client: " + ((BattleShipServer) gameModel.getRole()).getServerName());
			stateChanged = true;
		}
		return stateChanged;
	}

	// selects the client role for this machine
	public boolean selectClientRole(String serverName) {
		boolean stateChanged = false;
		if (gameModel.getState() == State.SELECTING_SERVER) {
			gameModel.setClientRole(serverName);
			gameModel.setState(State.SHIP_PLACEMENT);
			stateChanged = true;
		}
		return stateChanged;
	}

	// sets this player's name, changes state
	public boolean setPlayerName(String newName) {
		boolean stateChanged = false;
		if (gameModel.getState() == State.SETUP) {
			name = newName;
			gameModel.setState(State.SELECTING_SERVER);
			stateChanged = true;
		}
		return stateChanged;
	}

	// return opponents name (players name is already known by user entry)
	public String getOpponentName() {
		return gameModel.getOpponentShadow().getName();
	}

	// returns game to play loop if new game button is selected
	public void startNewGame(boolean newGame) {
		if (newGame && (gameModel.getState() == State.END_GAME)) {
			gameModel.setState(State.SHIP_PLACEMENT);
		} else if (!newGame && (gameModel.getState() == State.END_GAME)) {
			gameModel.setState(State.SETUP);
		}
	}
}