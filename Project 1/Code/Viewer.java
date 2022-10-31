/* Viewer is the GUI and view part of the MVC pattern used for the Battleship game. It takes input
 * from the user and sends it to the controller via action listeners.
 * Authors: Ryan Collins, John Schmidt
 * Last Updated: 10/29/2022
 */

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;

public class Viewer {

	private Controller gameController; // so the viewer be accessed by the controller
	private JFrame frame; // main window
	private JPanel boats;
	private JButton buttonTargetGridArray[][]; // used for updating target grid button images
	private JButton buttonOceanGridArray[][]; // used for updating ocean grid button images
	private JButton buttonShipArray[]; // used for updating ship boat area
	private JButton serverButton; // buttons are created so they are accessible to other action listeners
	private JButton clientButton;
	private JTextField textArea;
	private JButton horizontalButton;
	private JButton autoPlaceButton;
	private JLabel turnLabel;
	private JLabel playerNameLabel;
	private JLabel opponentNameLabel;
	protected static JTextArea notificationArea;
	private String playerName;
	private String opponentName;
	private boolean horizontal;
	private ImageIcon startup;
	private ImageIcon playerTurn;
	private ImageIcon opponentTurn;
	private int shipID; // saves ship selected for ship placement
	private Sound sound;

	// constructor
	public Viewer(Controller controller) throws BadLocationException, IOException {
		gameController = controller; // for registering viewer as an "observer" of the controller
		gameController.registerViewer(this);// register viewer
		frame = new JFrame("BattleShip v1.0 - Ryan Collins, John Schmidt");
		startup = new ImageIcon("./Images/Other/blankOcean.jpg");
		playerTurn = new ImageIcon("./Images/Other/yourTurn1.png");
		opponentTurn = new ImageIcon("./Images/Other/opponentsTurn.png");
		buttonTargetGridArray = new JButton[10][10];
		buttonOceanGridArray = new JButton[10][10];
		buttonShipArray = new JButton[5];
		horizontal = true;
		shipID = 0;
		createAllElements();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setSize(1500, 1000);
		frame.setVisible(true);
		sound = new Sound();
	}

	// creates and adds all frame elements
	private void createAllElements() throws BadLocationException, IOException {
		createNameEntryArea();
		createNotificationArea();
		createTargetGrid();
		createOceanGrid();
		createBoatArea();
		createHorizontalButton();
		createAutoPlaceShipsButton();
		createServerButton();
		createClientButton();
		createTurnLabel();
		createOpponentLabel();
		createPlayerLabel();
	}

	// creates the target grid area filled with a button grid for selecting a shot
	private void createTargetGrid() {
		char char1 = '@';
		String str;
		JPanel buttonGrid = new JPanel();
		buttonGrid.setLayout(new GridLayout(11, 11));
		JButton newButton;

		// create row labels
		for (int i = 0; i < 11; i++) {
			if (i == 0) {
				newButton = new JButton(); // first "button" is just a blank space
				newButton.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
				newButton.setOpaque(false);
				newButton.setContentAreaFilled(false);
				newButton.setBorderPainted(false);
				buttonGrid.add(newButton);
			} else { // every other "button" will have row letter label
				newButton = new JButton(Character.toString(char1 + i));
				newButton.setMargin(new Insets(0, 0, 0, 0));
				newButton.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
				newButton.setOpaque(false);
				newButton.setContentAreaFilled(false);
				newButton.setBorderPainted(false);
				newButton.setFocusPainted(false);
				buttonGrid.add(newButton);
			}

			// create column labels
			for (int j = 1; j < 11; j++) {
				if (i == 0) {
					str = "" + j;
					newButton = new JButton(str);
					newButton.setMargin(new Insets(0, 0, 0, 0));
					newButton.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
					newButton.setOpaque(false);
					newButton.setContentAreaFilled(false);
					newButton.setBorderPainted(false);
					newButton.setFocusPainted(false);
					buttonGrid.add(newButton);
				} else { // create the buttons for selecting shot
					newButton = new JButton(startup); // current image on button
					buttonTargetGridArray[i - 1][j - 1] = newButton; // add button to array for accessing later to
																		// change images
					// to hit
					// with ship (all buttons changed at the same time)
					newButton.setBorder(new LineBorder(Color.black));
					newButton.addActionListener(new targetGridListener(i - 1, j - 1)); // send coordinates to controller
																						// using
																						// listener
					buttonGrid.add(newButton);
				}
			}
		}
		buttonGrid.setBounds(25, 250, 650, 650);

		JLabel targetArea = new JLabel("Target Area");
		targetArea.setBounds(300, 900, 200, 50);
		targetArea.setFont(new Font(Font.DIALOG, Font.BOLD, 30));

		frame.add(targetArea);
		frame.add(buttonGrid);
	}

	// creates ocean grid and sets up ocean grid buttons
	private void createOceanGrid() {
		JPanel buttonGrid2 = new JPanel();
		buttonGrid2.setLayout(new GridLayout(10, 10));
		buttonGrid2.setSize(25, 25);
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				JButton newButton = new JButton(startup);
				newButton.setBorder(new LineBorder(Color.black));
				buttonOceanGridArray[i][j] = newButton;
				newButton.addActionListener(new oceanGridListener(i, j));
				buttonGrid2.add(newButton);
			}
		}
		buttonGrid2.getComponent(0);
		buttonGrid2.setBounds(750, 309, 591, 591);

		JLabel oceanGrid = new JLabel("Player's Grid");
		oceanGrid.setBounds(950, 900, 200, 50);
		oceanGrid.setFont(new Font(Font.DIALOG, Font.BOLD, 30));

		frame.add(oceanGrid);
		frame.add(buttonGrid2);
	}

	// creates the ship area to be used, first for selection, then for keeping track
	// of kills
	private void createBoatArea() {
		boats = new JPanel();
		// ImageIcon boatArea = new ImageIcon("./Images/Other/Ocean.jpg"); //DEBUG for
		// background
		boats.setBounds(740, 160, 610, 145);
		// boats.setBackground(Color.blue); //DEBUG need to remove for looks

		ImageIcon carrier = new ImageIcon(
				"./Images/AircraftCarrier/AircraftCarrier1-2.png");
		buttonShipArray[0] = new JButton(carrier); // create Carrier

		ImageIcon battleship = new ImageIcon("./Images/Battleship/Battleship 4-1.png");
		buttonShipArray[1] = new JButton(battleship); // create Battleship

		ImageIcon cruiser = new ImageIcon("./Images/Cruiser/Cru1.png");
		buttonShipArray[2] = new JButton(cruiser); // create Cruiser

		ImageIcon submarine = new ImageIcon("./Images/Submarine/Sub1.png");
		buttonShipArray[3] = new JButton(submarine); // create Submarine

		ImageIcon destroyer = new ImageIcon("./Images/Destroyer/dest1.png");
		buttonShipArray[4] = new JButton(destroyer); // create Destroyer

		// add attributes to each ship
		for (int i = 0; i < 5; i++) {
			final int id = i;
			buttonShipArray[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					shipID = id + 1;
					sound.playShipChime();
				}
			});
			buttonShipArray[i].setFont(new Font(Font.DIALOG, Font.BOLD, 30));
			buttonShipArray[i].setOpaque(false);
			buttonShipArray[i].setContentAreaFilled(false);
			buttonShipArray[i].setBorderPainted(false);
			buttonShipArray[i].setEnabled(false);
			boats.add(buttonShipArray[i]);
		}

		frame.add(boats);
	}

	// creates the name entry area to take in player name
	private void createNameEntryArea() {

		JTextField prompt = new JTextField("Player Name:"); // player name label
		prompt.setBounds(100, 50, 150, 50);
		prompt.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
		prompt.setForeground(Color.BLACK); // font color
		prompt.setOpaque(false); // background transparent
		prompt.setBorder(javax.swing.BorderFactory.createEmptyBorder()); // make border invisible

		// text box to enter name
		JTextField nameArea = new JTextField();
		nameArea.setText("Enter your name:");
		nameArea.setFont(new Font(Font.DIALOG, Font.ITALIC, 24));
		nameArea.setForeground(Color.lightGray);
		nameArea.setBounds(260, 50, 250, 50);

		// this action listener takes effect when enter is hit on keyboard with text box
		// selected
		nameArea.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				playerName = nameArea.getText(); // gets text from field
				boolean stateChanged = gameController.setPlayerName(playerName); // checks if successful
				if (stateChanged) {
					nameArea.setVisible(false); // text box not longer enabled
					prompt.setVisible(false); // gets rid of prompt
					playerNameLabel.setText("Player:  " + playerName);
					playerNameLabel.setVisible(true);
					serverButton.setVisible(true);
					clientButton.setVisible(true);
				}
			}
		});

		// focus listener allows text to go away when the box is focused on for keyboard
		// entry
		nameArea.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) { // clicked on text box
				if (nameArea.getText().equals("Enter your name:")) {
					nameArea.setText("");
					nameArea.setFont(new Font(Font.DIALOG, Font.PLAIN, 24));
					nameArea.setForeground(Color.BLACK);
				}
			}

			public void focusLost(FocusEvent e) { // clicked away from text box
				if (nameArea.getText().isBlank()) {
					nameArea.setFont(new Font(Font.DIALOG, Font.ITALIC, 24));
					nameArea.setForeground(Color.lightGray);
					nameArea.setText("Enter your name:");
				}
			}
		});
		frame.add(prompt);
		frame.add(nameArea);
	}

	// creates the notification area on the left side of the screen
	private void createNotificationArea() throws BadLocationException {
		notificationArea = new MyTextArea();
		notificationArea.setBounds(84, 110, 591, 130);
		notificationArea.setFont(new Font(Font.DIALOG, Font.PLAIN, 24));
		notificationArea.setForeground(Color.black);
		notificationArea.setBackground(new Color(1, 1, 1, (float) 0.01));
		frame.add(notificationArea);
		notificationArea.setEditable(false);
	}

	// creates the horizontal/vertical selection button
	private void createHorizontalButton() {
		horizontalButton = new JButton("Horizontal");
		horizontalButton.setBounds(750, 110, 290, 50);
		horizontalButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sound.playClick();
				if (horizontal) { // button was horizontal, switch to vertical
					horizontal = false;
					horizontalButton.setText("Vertical");
				} else { // button was vertical, switch to horizontal
					horizontal = true;
					horizontalButton.setText("Horizontal");
				}
			}
		});
		horizontalButton.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
		frame.add(horizontalButton);
		horizontalButton.setVisible(false);

	}

	// creates the automatic placement button
	private void createAutoPlaceShipsButton() {
		autoPlaceButton = new JButton("Automatic Placement");
		autoPlaceButton.setBounds(1050, 110, 290, 50);
		autoPlaceButton.setFont(new Font(Font.DIALOG, Font.BOLD, 24));

		autoPlaceButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sound.playClick();
				if (gameController.autoPlaceShips()) // if controller successfully places ships
					shipPlacementComplete();
			}
		});

		frame.add(autoPlaceButton);
		autoPlaceButton.setVisible(false); // keep disabled until needed
	}

	private void createServerButton() {
		serverButton = new JButton("Server");
		serverButton.setBounds(1050, 110, 290, 50);
		serverButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sound.playClick();
				if (gameController.selectServerRole()) { // try to select server role
					serverButton.setFont(new Font(Font.DIALOG, Font.ITALIC, 30));
					serverButton.setEnabled(false);
					clientButton.setVisible(false);
					addNotification("Awaiting client connection...");
				}
			}
		});

		serverButton.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
		serverButton.setVisible(false); // keep invisible until name is entered
		frame.add(serverButton);
	}

	// creates the client button on the right side of the screen
	private void createClientButton() {
		clientButton = new JButton("Client"); // creates client button
		clientButton.setBounds(750, 110, 290, 50);
		clientButton.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
		clientButton.setVisible(false); // keep invisible until name is entered

		clientButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sound.playClick();
				addNotification("Client role selected.");
				addNotification(playerName + ", enter server's device name.");
				clientButton.setFont(new Font(Font.DIALOG, Font.ITALIC, 30));
				clientButton.setEnabled(false);
				serverButton.setVisible(false); // replace server button with text entry box
				textArea.setVisible(true);
			}
		});

		// creates text area after client button selection
		textArea = new JTextField("Enter server's device name:");
		textArea.setFont(new Font(Font.DIALOG, Font.ITALIC, 18));
		textArea.setForeground(Color.lightGray);
		textArea.setBounds(1050, 110, 290, 50);
		textArea.setVisible(false);

		textArea.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text = textArea.getText(); // gets text from field
				if (text.isEmpty()) {
					addNotification("Server name empty. Local connection initiated.");
				} else {
					addNotification("Connecting to " + text + ".");
				}

				boolean stateChanged = gameController.selectClientRole(text); // checks if successful
				if (stateChanged) {
					textArea.setVisible(false);
				}
			}
		});

		textArea.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) { // clicked on text box
				if (textArea.getText().equals("Enter server's device name:")) {
					textArea.setText("");
					textArea.setFont(new Font(Font.DIALOG, Font.PLAIN, 18));
					textArea.setForeground(Color.BLACK);
				}
			}

			public void focusLost(FocusEvent e) { // clicked away from text box
				if (textArea.getText().isBlank()) {
					textArea.setFont(new Font(Font.DIALOG, Font.ITALIC, 18));
					textArea.setForeground(Color.lightGray);
					textArea.setText("Enter server's device name:");
				}
			}
		});
		frame.add(textArea);
		frame.add(clientButton);
	}

	// creates label indicating whose turn to fire it is
	private void createTurnLabel() {
		turnLabel = new JLabel();
		turnLabel.setBounds(750, 205, 590, 90);
		turnLabel.setVisible(false);
		frame.add(turnLabel);
	}

	// creates the player name label for the right side of the screen
	private void createPlayerLabel() {
		playerNameLabel = new JLabel();
		playerNameLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
		playerNameLabel.setBounds(240, 50, 350, 50);
		playerNameLabel.setVisible(false);
		frame.add(playerNameLabel);
	}

	// creates the opponent name label for the left side of the screen
	private void createOpponentLabel() {
		opponentNameLabel = new JLabel();
		opponentNameLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
		opponentNameLabel.setBounds(240, 20, 350, 50);
		opponentNameLabel.setVisible(false);
		frame.add(opponentNameLabel);

	}

	// game was won, call end game window
	public void winCondition() throws IOException {
		endGameWindow(true);
	}

	// game was lost, call end game window
	public void loseCondition() throws IOException {
		endGameWindow(false);
	}

	// sets up the end game window
	private void endGameWindow(boolean won) throws IOException {

		JFrame winFrame = new JFrame(); // new popup frame contains label and buttons
		ImageIcon icon;

		if (won) { // set correct background
			icon = new ImageIcon("./Images/Other/win.jpg");

		} else {
			icon = new ImageIcon("./Images/Other/lose.jpg");
		}

		JLabel picLabel = new JLabel(icon); // background picture
		picLabel.setBounds(0, 0, 585, 395);

		winFrame.setLocation(frame.getX() + 450, frame.getY() + 350);
		winFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		winFrame.setLayout(null);
		winFrame.setSize(600, 395);
		winFrame.setVisible(true);
		winFrame.setAlwaysOnTop(true);

		// play again button
		ImageIcon playAgain = new ImageIcon("./Images/Other/playAgain.png");
		JButton playAgainButton = new JButton(playAgain);
		playAgainButton.setBounds(10, 295, 270, 50);
		playAgainButton.setContentAreaFilled(false);
		playAgainButton.setBorderPainted(false);
		playAgainButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sound.playClick();
				gameController.startNewGame(true);
				resetGameBoard();
				winFrame.dispose();
			}
		});

		// end button
		ImageIcon end = new ImageIcon("./Images/Other/quit.png");
		JButton endButton = new JButton(end);
		endButton.setBounds(300, 295, 270, 50);
		endButton.setContentAreaFilled(false);
		endButton.setBorderPainted(false);
		endButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sound.playClick();
				gameController.startNewGame(false);
				winFrame.dispose();
				frame.dispose();
			}
		});

		winFrame.add(endButton);
		winFrame.add(playAgainButton);
		winFrame.add(picLabel);

		winFrame.repaint();
	}

	// turns on ship buttons in preparation for placement
	public void activateShipPlacement() {
		serverButton.setVisible(false);
		clientButton.setVisible(false);
		turnLabel.setVisible(false);
		autoPlaceButton.setVisible(true);
		horizontalButton.setVisible(true);

		for (int i = 0; i < 5; i++) { // turn on ship icons
			buttonShipArray[i].setEnabled(true);
		}
	}

	// moves notification area and ship area, turns on turn indicator button
	public void shipPlacementComplete() {
		autoPlaceButton.setVisible(false);
		horizontalButton.setVisible(false);

		for (int i = 0; i < 5; i++) { // turn on ship icons
			buttonShipArray[i].setEnabled(true);
			buttonShipArray[i].setFocusPainted(false); // stops highlighting box around ship
			for (ActionListener a1 : buttonShipArray[i].getActionListeners()) {
				buttonShipArray[i].removeActionListener(a1);
			}
		}

		notificationArea.setBounds(750, 70, 591, 130);
		boats.setBounds(74, 110, 610, 145);
		turnLabel.setVisible(true);
		playerNameLabel.setBounds(920, 20, 350, 50);
		opponentName = gameController.getOpponentName();
		opponentNameLabel.setText("Opponent: " + opponentName);
		opponentNameLabel.setVisible(true);

		JLabel opponentShips = new JLabel("Ships left to destroy:");
		opponentShips.setFont(new Font(Font.DIALOG, Font.PLAIN, 24));
		opponentShips.setBounds(250, 60, 350, 50);
		frame.add(opponentShips);
	}

	// updates target grid at location with parameter image path
	public void updateTargetGrid(int row, int col, String imageFilePath) {
		ImageIcon newImage = new ImageIcon(imageFilePath);
		buttonTargetGridArray[row][col].setIcon(newImage);
	}

	// updates ocean grid at location with parameter image path
	public void updateOceanGrid(int row, int col, String imageFilePath) {
		ImageIcon newImage = new ImageIcon(imageFilePath);
		buttonOceanGridArray[row][col].setIcon(newImage);
	}

	// updates the enemy ship image to indicate that it is sunk
	public void enemyShipSunk(int shipID) {
		if (shipID == 1) {
			ImageIcon sunk = new ImageIcon("./Images/AircraftCarrier/AircraftCarrier2-2.png");
			buttonShipArray[0].setIcon(sunk);
		} else if (shipID == 2) {
			ImageIcon sunk = new ImageIcon("./Images/Battleship/Battleship1-2.png");
			buttonShipArray[1].setIcon(sunk);
		} else if (shipID == 3) {
			ImageIcon sunk = new ImageIcon("./Images/Cruiser/Cruiser1-2.png");
			buttonShipArray[2].setIcon(sunk);
		} else if (shipID == 4) {
			ImageIcon sunk = new ImageIcon("./Images/Submarine/Submarine1-2.png");
			buttonShipArray[3].setIcon(sunk);
		} else if (shipID == 5) {
			ImageIcon sunk = new ImageIcon("./Images/Destroyer/Destroyer1-2.png");
			buttonShipArray[4].setIcon(sunk);
		}
	}

	// sets turn label to "Your Turn"
	public void setTurnLabelPlayersTurn() {
		turnLabel.setIcon(playerTurn);
	}

	// sets turn label to "Incoming"
	public void setTurnLabelOpponentsTurn() {
		turnLabel.setIcon(opponentTurn);
	}

	// adds text to the notification area
	public void addNotification(String newMessage) {
		try {
			notificationArea.getDocument().insertString(0, " " + newMessage + "\n", null);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	// resets game board
	private void resetGameBoard() {
		notificationArea.setBounds(84, 110, 591, 130);
		frame.remove(boats);
		frame.remove(autoPlaceButton);
		frame.remove(turnLabel);
		createBoatArea();
		createAutoPlaceShipsButton();
		createTurnLabel();
		activateShipPlacement();
		frame.revalidate();
	}

	// targetGridListener will listen for buttons on the target grid to be pressed
	// and send which button to the controller as a potential shot
	public class targetGridListener implements ActionListener {
		private int row;
		private int col;

		// constructor
		targetGridListener(int row, int col) {
			this.row = row;
			this.col = col;
		}

		// action performed; send shot to controller
		public void actionPerformed(ActionEvent e) {
			gameController.shotFromViewer(row, col);
		}
	}

	// oceanGridListener will listen for buttons on the ocean grid to be pressed to
	// send ship placement to the controller
	public class oceanGridListener implements ActionListener {
		private int row;
		private int col;

		// constructor
		public oceanGridListener(int row, int col) {
			this.row = row;
			this.col = col;
		}

		// action performed; send ship placement to controller
		public void actionPerformed(ActionEvent e) {

			boolean successful = gameController.tryPlaceShip(row, col, shipID, horizontal);
			if (successful) {
				sound.playEmbark();
				buttonShipArray[shipID - 1].setEnabled(false); // sets ship button to disabled
				shipID = -1; // resets shipID so player needs to click another ship
				autoPlaceButton.setEnabled(false);
				autoPlaceButton.setFont(new Font(Font.DIALOG, Font.ITALIC, 24));
				autoPlaceButton.setText("Ships Placed Manually");
			}
		}
	}
}
