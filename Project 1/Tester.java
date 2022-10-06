/* Tester is part of the Battle Ship project.  Tester contains the main program and is uses for testing other classes.
 * The current version will play the game in the console window using no graphics or sound. 
 * Authors: Ryan Collins, John Schmidt
 * Updated: 9/20/22
 */

import java.util.Scanner;

public class Tester {

	private static final Scanner keyboard = new Scanner(System.in);

	public static void main(String[] args) {

		int[] rowColArray = new int[2]; // stores coordinates

		// test player creation
		Player player1 = new Player("Player 1");
		Player player2 = new Player("Player 2");

		placeShips(player1); //place ships
		placeShips(player2);

		displayTargetGrid(player1); //display grids for player 1
		displayOceanGrid(player1);

		displayTargetGrid(player2);  //display grids for player 2
		displayOceanGrid(player2);

		int winner = -1;
		while (winner == -1) { // main play loop
			
			rowColArray = shotPrompt(player1); //player1's turn
			bombardPlayer(rowColArray[0], rowColArray[1], player1, player2);
			winner = checkForWinner(player1, player2);

			rowColArray = shotPrompt(player2);//player2's turn
			bombardPlayer(rowColArray[0], rowColArray[1], player2, player1);
			winner = checkForWinner(player1, player2); 
			
			displayTargetGrid(player1); //display grids for player 1
			displayOceanGrid(player1);

			displayTargetGrid(player2); //display grids for player 2
			displayOceanGrid(player2);		
		}
		
		keyboard.close();
	}

	// prints empty board
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

	// prompts user for shot, then returns array with [row,col]
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
			if(shipHit.isSunk()) {
				System.out.println("The "+ shipHit.getName() + " was sunk!");
			}
			
		} else {
			System.out.println("Miss!\n");
			shootingPlayer.getTargetGrid().isMiss(row, col);
		}
		System.out.println();
	}

	// places all ships for currentPlayer, either automatically or manually
	public static void placeShips(Player currentPlayer) {
		System.out.print("Place ships for " + currentPlayer.getName() + " automatically (Yes or No?)");
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
