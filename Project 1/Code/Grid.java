
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Grid implements ActionListener {
	public static void main(String s[]) {
		// ImageIcon water

		JFrame frame = new JFrame();
		frame.setBackground(Color.LIGHT_GRAY);

		JPanel buttonGrid = new JPanel();
		buttonGrid.setLayout(new GridLayout(11, 11));

		/*-
		JPanel testpan = new JPanel();
		testpan.setBackground(Color.red);
		testpan.setBounds(0, 0, 250, 250);
		frame.add(testpan);
		-*/
		char char1 = '@';
		String str;
		JButton newButton;

		//create row labels
		for (int i = 0; i < 11; i++) {
			if (i == 0) {
				newButton = new JButton();
				newButton.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
				newButton.setOpaque(false);
				newButton.setContentAreaFilled(false);
				newButton.setBorderPainted(false);
				buttonGrid.add(newButton);
			} else {
				newButton = new JButton(Character.toString(char1 + i));
				newButton.setMargin(new Insets(0, 0, 0, 0));
				newButton.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
				newButton.setOpaque(false);
				newButton.setContentAreaFilled(false);
				newButton.setBorderPainted(false);
				buttonGrid.add(newButton);
			}

			//create column labels
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
				} else { //and create buttons TODO: remove labels once painted
					char c = (char) ('A' + (i - 1));
					String str1 = c + " " + (j);
					newButton = new JButton(str1);
					newButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							JOptionPane.showMessageDialog(frame, str1);
						}
					});
					buttonGrid.add(newButton);
				}
			}
		}
		buttonGrid.setBounds(150, 100, 650, 650);
		frame.add(buttonGrid);

		JPanel buttonGrid2 = new JPanel();
		buttonGrid2.setLayout(new GridLayout(10, 10));

		buttonGrid2.setSize(25, 25);
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				char c = (char) ('A' + i);
				str = c + " " + (j + 1);
				buttonGrid2.add(new JButton(str));
			}
		}
		buttonGrid2.setBounds(950, 300, 450, 450);
		frame.add(buttonGrid2);

		JPanel boats = new JPanel();
		// boats.setLayout(new GridLayout(3,3));
		boats.setBackground(Color.red);

		boats.setBounds(950, 100, 450, 160);
		frame.add(boats);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setSize(1500, 900);
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}
}
