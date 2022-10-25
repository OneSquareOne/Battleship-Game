/* Viewer is the GUI and view part of the MVC pattern used for the Battleship game. It takes input
 * from the user and sends it to the controller via action listeners.
 * Authors: Ryan Collins, John Schmidt
 * Last Updated: 10/22/2022
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;

public class Viewer {

	private Controller gameController; // so the viewer be accessed by the controller
	private JFrame frame;
	private JButton buttonTargetGridArray[][]; // used for updating target grid button images
	private JButton buttonOceanGridArray[][]; // used for updating ocean grid button images
	private JButton buttonShipArray[]; // used for updating ship boat area
	private JButton serverButton; // buttons are created so they are accessible to other action listeners
	private JButton clientButton;
	private JTextField textArea;
	private JButton horizontalButton;
	private JButton autoPlaceButton;
	protected static JTextArea notificationArea;
	private String playerName;
	private boolean horizontal = true;
	private ImageIcon startup;
	private int shipID = 0; // saves ship selected for ship placement

	// constructor
	public Viewer(Controller controller) throws BadLocationException {
		gameController = controller; // for registering viewer as an observer of the controller
		gameController.registerViewer(this);// register viewer
		frame = new JFrame("BattleShip v1.0 - Ryan Collins, John Schmidt");
		startup = new ImageIcon("./Images/Other/blankOcean.jpg");
		buttonTargetGridArray = new JButton[10][10];
		buttonOceanGridArray = new JButton[10][10];
		buttonShipArray = new JButton[5];
		createNameEntryArea();
		createNotificationArea();
		createTargetGrid();
		createOceanGrid();
		createBoatArea();
		createHorizontalButton();
		createAutoPlaceShipsButton();
		createServerButton();
		createClientButton();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setSize(1500, 1000);
		frame.setVisible(true);
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

	private void createBoatArea() {
		JPanel boats = new JPanel();
		// ImageIcon boatArea = new ImageIcon("./Images/Other/Ocean.jpg"); //DEBUG for
		// background
		boats.setBounds(740, 160, 610, 145);
		// boats.setBackground(Color.blue); //DEBUG need to remove for looks

		ImageIcon carrier = new ImageIcon(
				"./Images/AircraftCarrier/AircraftCarrier1-2.png");
		buttonShipArray[0] = new JButton(carrier); // first "button" is just a blank space
		buttonShipArray[0].addActionListener(new selectedShipListener(1));
		buttonShipArray[0].setFont(new Font(Font.DIALOG, Font.BOLD, 30));
		buttonShipArray[0].setOpaque(false);
		buttonShipArray[0].setContentAreaFilled(false);
		buttonShipArray[0].setBorderPainted(false);

		ImageIcon battleship = new ImageIcon("./Images/Battleship/Battleship 4-1.png");
		buttonShipArray[1] = new JButton(battleship); // first "button" is just a blank space
		buttonShipArray[1].addActionListener(new selectedShipListener(2));
		buttonShipArray[1].setFont(new Font(Font.DIALOG, Font.BOLD, 30));
		buttonShipArray[1].setOpaque(false);
		buttonShipArray[1].setContentAreaFilled(false);
		buttonShipArray[1].setBorderPainted(false);

		ImageIcon cruiser = new ImageIcon("./Images/Cruiser/Cru1.png");
		buttonShipArray[2] = new JButton(cruiser); // first "button" is just a blank space
		buttonShipArray[2].addActionListener(new selectedShipListener(3));
		buttonShipArray[2].setFont(new Font(Font.DIALOG, Font.BOLD, 30));
		buttonShipArray[2].setOpaque(false);
		buttonShipArray[2].setContentAreaFilled(false);
		buttonShipArray[2].setBorderPainted(false);

		ImageIcon submarine = new ImageIcon("./Images/Submarine/Sub1.png");
		buttonShipArray[3] = new JButton(submarine); // first "button" is just a blank space
		buttonShipArray[3].addActionListener(new selectedShipListener(4));
		buttonShipArray[3].setFont(new Font(Font.DIALOG, Font.BOLD, 30));
		buttonShipArray[3].setOpaque(false);
		buttonShipArray[3].setContentAreaFilled(false);
		buttonShipArray[3].setBorderPainted(false);

		ImageIcon destroyer = new ImageIcon("./Images/Destroyer/dest1.png");
		buttonShipArray[4] = new JButton(destroyer); // first "button" is just a blank space
		buttonShipArray[4].addActionListener(new selectedShipListener(5));
		buttonShipArray[4].setFont(new Font(Font.DIALOG, Font.BOLD, 30));
		buttonShipArray[4].setOpaque(false);
		buttonShipArray[4].setContentAreaFilled(false);
		buttonShipArray[4].setBorderPainted(false);

		boats.add(buttonShipArray[0]);
		boats.add(buttonShipArray[1]);
		boats.add(buttonShipArray[2]);
		boats.add(buttonShipArray[3]);
		boats.add(buttonShipArray[4]);

		// buttonShipArray[4].setEnabled(false); //disables and grays out ship button
		// buttonShipArray[4].setVisible(false); //removes button entirely from view,
		// reorganizes other buttons

		frame.add(boats);
	}

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
					nameArea.setEnabled(false); // text box not longer enabled
					nameArea.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
					nameArea.setDisabledTextColor(Color.black); // font color
					nameArea.setBorder(javax.swing.BorderFactory.createEmptyBorder()); // no border
					nameArea.setOpaque(false); // background transparent
					prompt.setVisible(false); // gets rid of prompt
					nameArea.setBounds(100, 50, 250, 50);// resize to prompt area
					nameArea.setText("Player:  " + playerName);
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

	private void createNotificationArea() throws BadLocationException {
		notificationArea = new MyTextArea();
		notificationArea.setBounds(84, 110, 591, 130);
		notificationArea.setFont(new Font(Font.DIALOG, Font.PLAIN, 24));
		notificationArea.setForeground(Color.black);
		notificationArea.setBackground(new Color(1, 1, 1, (float) 0.01));
		frame.add(notificationArea);
		notificationArea.setEditable(false);
	}

	private void createHorizontalButton() {
		horizontalButton = new JButton("Horizontal");
		horizontalButton.setBounds(800, 110, 200, 50);
		horizontalButton.addActionListener(new horizontalListener());
		horizontalButton.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
		frame.add(horizontalButton);
		horizontalButton.setVisible(false);

	}

	private void createAutoPlaceShipsButton() {
		autoPlaceButton = new JButton("Automatic Placement");
		autoPlaceButton.setBounds(1010, 110, 240, 50);
		autoPlaceButton.addActionListener(new automaticPlacementListener());
		autoPlaceButton.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
		frame.add(autoPlaceButton);
		autoPlaceButton.setVisible(false); // keep disabled until needed
	}

	private void createServerButton() {
		serverButton = new JButton("Server");
		serverButton.setBounds(1050, 110, 290, 50);
		serverButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (gameController.getCurrentState() == State.SELECTING_HOST) {
					serverButton.setFont(new Font(Font.DIALOG, Font.ITALIC, 18));
					serverButton.setText("You are: Server");
					clientButton.setText("");
					serverButton.setEnabled(false);
					clientButton.setEnabled(false);
					gameController.selectServerRole();
					horizontalButton.setEnabled(true); // horizontal button ready for input
					autoPlaceButton.setEnabled(true); // autoPlace button ready for input
				}
			}
		});

		serverButton.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
		serverButton.setVisible(false); // keep invisible until name is entered
		frame.add(serverButton);
	}

	private void createClientButton() {

		clientButton = new JButton("Client"); // creates client button
		clientButton.setBounds(750, 110, 290, 50);
		clientButton.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
		clientButton.setVisible(false); // keep invisible until name is entered

		clientButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addNotification("Client role selected.");
				addNotification(playerName + ", enter server machine's name.");
				clientButton.setFont(new Font(Font.DIALOG, Font.ITALIC, 30));
				clientButton.setEnabled(false);
				serverButton.setVisible(false); // replace server button with text entry box
				textArea.setVisible(true);
			}
		});

		// creates text area after client button selection
		textArea = new JTextField("Enter server name:");
		textArea.setFont(new Font(Font.DIALOG, Font.ITALIC, 24));
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
					clientButton.setVisible(false);
					textArea.setVisible(false);
					horizontalButton.setVisible(true); // horizontal button ready for input
					autoPlaceButton.setVisible(true); // autoPlace button ready for input
				}
			}
		});
		textArea.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) { // clicked on text box
				if (textArea.getText().equals("Enter server name:")) {
					textArea.setText("");
					textArea.setFont(new Font(Font.DIALOG, Font.PLAIN, 24));
					textArea.setForeground(Color.BLACK);
				}
			}

			public void focusLost(FocusEvent e) { // clicked away from text box
				if (textArea.getText().isBlank()) {
					textArea.setFont(new Font(Font.DIALOG, Font.ITALIC, 24));
					textArea.setForeground(Color.lightGray);
					textArea.setText("Enter server name:");
				}
			}
		});

		frame.add(textArea);
		frame.add(clientButton);

	}

	// private void create

	public void updateTargetGrid(int row, int col, String imageFilePath) {
		ImageIcon newImage = new ImageIcon(imageFilePath);
		buttonOceanGridArray[row][col].setIcon(newImage);
	}

	public void updateOceanGrid(int row, int col, String imageFilePath) {
		ImageIcon newImage = new ImageIcon(imageFilePath);
		buttonOceanGridArray[row][col].setIcon(newImage);
	}

	// adds text to the notification area
	public void addNotification(String newMessage) {
		try {
			notificationArea.getDocument().insertString(0, " " + newMessage + "\n", null);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
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
				buttonShipArray[shipID - 1].setEnabled(false); // sets ship button to disabled
				shipID = -1; // resets shipID so player needs to click another ship
				autoPlaceButton.setEnabled(false);
			}
		}
	}

	// selectedShipListener will act as the ship selection so that the ocean grid
	// knows which
	// ship needs to be placed; may need to be modified once drag and drop is
	// implemented
	public class selectedShipListener implements ActionListener {

		int buttonID;

		public selectedShipListener(int ID) {
			buttonID = ID;
		}

		public void actionPerformed(ActionEvent e) {
			shipID = buttonID;
		}
	}

	// listens to horizontal placement button
	public class horizontalListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (horizontal) { // button was horizontal, switch to vertical
				horizontal = false;
				horizontalButton.setText("Vertical");
			} else { // button was vertical, switch to horizontal
				horizontal = true;
				horizontalButton.setText("Horizontal");
			}
		}
	}

	// listens to the automatic placement button
	public class automaticPlacementListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			gameController.autoPlaceShips();
			autoPlaceButton.setFont(new Font(Font.DIALOG, Font.ITALIC, 12));
			autoPlaceButton.setText("Ships placed automatically");
			autoPlaceButton.setEnabled(false);
			horizontalButton.setEnabled(false);
			horizontalButton.setBorderPainted(false);
		}
	}

	public class newGameListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// TODO: fill out with new game communication to controller
		}
	}
}
