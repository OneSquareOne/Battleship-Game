
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

public class Viewer implements Subject, Observer {

	private Controller gameController;
	private Observer controllerObserver;
	private ImageIcon water = new ImageIcon("./images/Ocean1_1.jpg");
	private ImageIcon miss = new ImageIcon("./images/Ocean2_7.jpg");
	private ImageIcon hit = new ImageIcon("./images/Ocean3_2.jpg");
	private JFrame frame = new JFrame();
	private JPanel buttonGrid = new JPanel();
	private JButton buttonArray[][];
	private JPanel buttonGrid2 = new JPanel();
	private JPanel boats = new JPanel();

	// constructor
	public Viewer() {
		buttonArray = new JButton[10][10];
		frame.setBackground(Color.LIGHT_GRAY);
		buttonGrid.setLayout(new GridLayout(11, 11));
		createTargetGrid();
		createOceanGrid();
		createBoatArea();
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
					newButton = new JButton(water); // current image on button
					buttonArray[i-1][j-1] = newButton; // add button to array for accessing later to change images to hit
													// with ship (all buttons changed at the same time)
					newButton.setBorder(new LineBorder(Color.black));
					newButton.addActionListener(new targetGridListener(i-1, j-1)); // send coordinates to controller using
																				// listener
					buttonGrid.add(newButton);
				}
			}
		}
		buttonGrid.setBounds(150, 100, 650, 650);
		frame.add(buttonGrid);
	}

	private void createOceanGrid() {
		String str;
		buttonGrid2.setLayout(new GridLayout(10, 10));

		buttonGrid2.setSize(25, 25);
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				char c = (char) ('A' + i);
				str = c + " " + (j + 1);
				buttonGrid2.add(new JButton(str));
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

	//this will update the corresponding 
	private void shipSunk(){

	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void registerObserver(Observer o) {
		controllerObserver = o;
	}

	@Override
	public void removeObserver(Observer o) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyObservers() {
		// TODO Auto-generated method stub

	}

	public class targetGridListener implements ActionListener {
		private int row;
		private int col;

		// constructor
		targetGridListener(int row, int col) {
			this.row = row;
			this.col = col;
		}

		//action performed; send shot to controller
		public void actionPerformed(ActionEvent e) {
			buttonArray[row][col].setIcon(hit);
		}
	}

	// listens to vertical placement button
	public class verticalListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// TODO: fill out with updates to controller for vertical placement
		}
	}

	public class newGameListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// TODO: fill out with new game communication to controller
		}
	}
}
