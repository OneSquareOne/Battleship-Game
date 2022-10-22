/* Viewer is the GUI and view part of the MVC pattern used for the Battleship game. It takes input
 * from the user and sends it to the controller via action listeners.
 * Authors: Ryan Collins, John Schmidt
 * Last Updated: 10/22/2022
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class Viewer {

	private Controller gameController; // so the viewer be accessed by the controller
	private JFrame frame;
	private JButton buttonTargetGridArray[][]; // used for updating target grid button images
	private JButton buttonOceanGridArray[][]; // used for updating ocean grid button images
	private JButton buttonShipArray[]; //used for updating ship boat area
	private JButton server; //buttons are created so they are accessible to other action listeners
	private JButton client;
	private JButton horizontalButton;
	private JButton autoPlaceButton;
	private boolean horizontal = true;
	private ImageIcon startup;
	private int shipID = 0; // saves ship selected for ship placement
	

	// constructor
	public Viewer(Controller controller) {

		gameController = controller; // for registering viewer as an observer of the controller
		gameController.registerViewer(this);// register viewer
		frame = new JFrame("BattleShip v1.0 - Ryan Collins, John Schmidt");
		startup = new ImageIcon("./Images/Other/blankOcean.jpg");
		buttonTargetGridArray = new JButton[10][10];
		buttonOceanGridArray = new JButton[10][10];
		buttonShipArray = new JButton[5];
		frame.setBackground(Color.LIGHT_GRAY);
		createTargetGrid();
		createOceanGrid();
		createBoatArea();
		createHorizontalButton();
		createAutoPlaceShipsButton();
		createServerButton();
		createClientButton();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setSize(1500, 900);

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
		buttonGrid.setBounds(50, 150, 650, 650);
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
		buttonGrid2.setBounds(800, 350, 450, 450);
		frame.add(buttonGrid2);
	}

	private void createBoatArea() {
		JPanel boats = new JPanel();
		ImageIcon boatArea = new ImageIcon("./Images/Other/Ocean.jpg");
		boats.setBackground(Color.red);
		boats.setBounds(800, 175, 450, 160);

		ImageIcon carrier = new ImageIcon("./Images/AircraftCarrier/AircraftCarrier1-2.png");
		buttonShipArray[0] = new JButton(carrier); // first "button" is just a blank space
		buttonShipArray[0].addActionListener(new selectedShipListener(1));
		buttonShipArray[0].setFont(new Font(Font.DIALOG, Font.BOLD, 30));
		buttonShipArray[0].setOpaque(false);
		buttonShipArray[0].setContentAreaFilled(false);
		buttonShipArray[0].setBorderPainted(false);
		boats.add(buttonShipArray[0]);
		
		frame.add(boats);
	}

	private void createHorizontalButton() {
		horizontalButton = new JButton("Horizontal");
		horizontalButton.setBounds(800, 110, 200, 50);
		horizontalButton.addActionListener(new horizontalListener());
		horizontalButton.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
		frame.add(horizontalButton);
	}

	private void createAutoPlaceShipsButton() {
		autoPlaceButton = new JButton("Automatic Placement");
		autoPlaceButton.setBounds(1010, 110, 240, 50);
		autoPlaceButton.addActionListener(new automaticPlacementListener());
		autoPlaceButton.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
		frame.add(autoPlaceButton);
	}

	private void createServerButton() {
		server = new JButton("Server");
		server.setBounds(800, 50, 200, 50);
		server.addActionListener(new serverListener());
		server.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
		frame.add(server);
	}

	private void createClientButton() {
		client = new JButton("Client");
		client.setBounds(1010, 50, 200, 50);
		client.addActionListener(new clientListener());
		client.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
		frame.add(client);
	}

	public void updateTargetGrid(int row, int col, String imageFilePath) {
		ImageIcon newImage = new ImageIcon(imageFilePath);
		buttonOceanGridArray[row][col].setIcon(newImage);
	}

	public void updateOceanGrid(int row, int col, String imageFilePath) {
		ImageIcon newImage = new ImageIcon(imageFilePath);
		buttonOceanGridArray[row][col].setIcon(newImage);
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
				buttonShipArray[shipID-1].setEnabled(false); //sets ship button to disabled
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

		int buttonID = -1;

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
		}
	}

	public class serverListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JButton thisButton = (JButton) e.getSource();
			thisButton.setFont(new Font(Font.DIALOG, Font.ITALIC, 18));
			thisButton.setText("You are: Server");
			client.setText("");
			thisButton.setEnabled(false);
			client.setEnabled(false);
		}
	}

	public class clientListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JButton thisButton = (JButton) e.getSource();
			thisButton.setFont(new Font(Font.DIALOG, Font.ITALIC, 18));
			thisButton.setText("You are: Client");
			server.setText("");
			thisButton.setEnabled(false);
			server.setEnabled(false);
		}
	}

	public class newGameListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// TODO: fill out with new game communication to controller
		}
	}
}
