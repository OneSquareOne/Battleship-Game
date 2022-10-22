/* Viewer is the GUI and view part of the MVC pattern used for the Battleship game. It takes input
 * from the user and sends it to the controller via action listeners.
 * Authors: Ryan Collins, John Schmidt
 * Last Updated: 10/22/2022
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class Viewer {

	private Controller gameController; // so the viewer be accessed by the controller
	private JFrame frame = new JFrame();
	private JPanel buttonGrid = new JPanel();
	private JPanel buttonGrid2 = new JPanel();
	private JPanel boats = new JPanel();
	private JButton buttonTargetGridArray[][]; // used for updating target grid button images
	private JButton buttonOceanGridArray[][]; // used for updating ocean grid button images
	private ImageIcon startup;
	private int shipID = 0; // saves ship selected for ship placement
	private boolean horizontal = true;

	// constructor
	public Viewer() {

		//gameController = controller; // for registering viewer as an observer of the controller
		//gameController.registerViewer(this);// register viewer
		startup = new ImageIcon("./Images/Other/blankOcean.jpg");
		buttonTargetGridArray = new JButton[10][10];
		buttonOceanGridArray = new JButton[10][10];
		frame.setBackground(Color.LIGHT_GRAY);
		buttonGrid.setLayout(new GridLayout(11, 11));
		createTargetGrid();
		createOceanGrid();
		createBoatArea();
		createHorizontalButton();
		createAutoPlaceShipsButton();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setSize(1500, 900);
		
		frame.setVisible(true);
	}

	// creates the target grid area filled with a button grid for selecting a shot
	private void createTargetGrid() {
		char char1 = '@';
		String str;
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
		buttonGrid.setBounds(150, 100, 650, 650);
		frame.add(buttonGrid);
	}

	private void createOceanGrid() {
		buttonGrid2.setLayout(new GridLayout(10, 10));

		buttonGrid2.setSize(25, 25);
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				startup = new ImageIcon("./Images/Other/blankOcean.jpg");
				JButton newButton = new JButton(startup);
				newButton.setBorder(new LineBorder(Color.black));
				buttonOceanGridArray[i][j] = newButton;
				newButton.addActionListener(new oceanGridListener(i, j));
				buttonGrid2.add(newButton);
			}
		}
		buttonGrid2.getComponent(0);
		buttonGrid2.setBounds(950, 300, 450, 450);
		frame.add(buttonGrid2);
	}

	private void createBoatArea() {
		boats.setBackground(Color.red);
		boats.setBounds(950, 100, 450, 160);
		frame.add(boats);
	}

	private void createHorizontalButton() {
		JButton newButton = new JButton("Horizontal");
		newButton.setBounds(950, 25, 200, 50);
		newButton.addActionListener(new horizontalListener(newButton));
		frame.add(newButton);
	}

	private void createAutoPlaceShipsButton(){
		JButton newButton = new JButton("Automatic Placement");
		newButton.setBounds(1200, 25, 200, 50);
		newButton.addActionListener(new automaticPlacementListener());
		frame.add(newButton);
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
				shipID = -1; // resets shipID so player needs to click another ship
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
		JButton callingButton;

		// constructor captures the button to assign the new text to so you don't have
		// to keep track of the button in the data members
		public horizontalListener(JButton button) {
			callingButton = button;
		}

		public void actionPerformed(ActionEvent e) {
			if (horizontal) { // button was horizontal, switch to vertical
				horizontal = false;
				callingButton.setText("Vertical");
			} else { // button was vertical, switch to horizontal
				horizontal = true;
				callingButton.setText("Horizontal");
			}
		}
	}

	//listens to the automatic placement button
	public class automaticPlacementListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			gameController.autoPlaceShips();
		}
	}


	public class newGameListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// TODO: fill out with new game communication to controller
		}
	}
}
