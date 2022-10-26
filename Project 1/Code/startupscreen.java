import java.awt.Color;
import java.awt.event.*;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.w3c.dom.events.MouseEvent;

public class startupscreen extends JPanel implements ActionListener {
	public static void main(String[] args) {
		new startupscreen();

	}

	boolean amserver = true;
	String name;
	String servername;

	public startupscreen() {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setSize(750, 750);
		frame.setVisible(true);

		JTextField namefield = new JTextField();
		namefield.setText("Enter Player Name here");
		namefield.setBounds(100, 400, 100, 50);
		frame.add(namefield);

		JTextField serverfield = new JTextField();
		serverfield.setBackground(Color.LIGHT_GRAY);
		serverfield.setText("Enter server ID here");
		serverfield.setBounds(250, 400, 100, 50);
		frame.add(serverfield);
		serverfield.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text = serverfield.getText();
				System.out.println(text);

			}
		});

		serverfield.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				if (serverfield.getText().equals("Enter server name:"))
					serverfield.setText("");
			}

			public void focusLost(FocusEvent e) {
				if (serverfield.getText().isBlank())
					serverfield.setText("Enter server name:");
			}
		});

		JButton server = new JButton("I'll play as the server");
		server.setBounds(100, 25, 300, 50);

		frame.add(server);
		server.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				amserver = true;
				serverfield.setEditable(false);
				serverfield.setText("");
				serverfield.setBackground(Color.DARK_GRAY);
			}
		});

		JButton client = new JButton("I'll play as the client");
		client.setBounds(100, 150, 300, 50);

		frame.add(client);
		client.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				amserver = false;
				serverfield.setEditable(true);
				serverfield.setText("Enter server ID here");
				serverfield.setBackground(Color.LIGHT_GRAY);
			}
		});

		/*
		 * JButton Enter=new JButton("Enter");
		 * Enter.setBounds(0, 25, 100, 50);
		 * 
		 * frame.add(Enter);
		 * Enter.addActionListener(new ActionListener() {
		 * 
		 * @Override
		 * public void actionPerformed(ActionEvent e) {
		 * 
		 * frame.getContentPane().removeAll();
		 * name=namefield.getText();
		 * servername=serverfield.getText();
		 * //submit all information
		 * JOptionPane.showMessageDialog(frame,name+"  "+servername+"  "+amserver);
		 * new notificationfield(frame);
		 * 
		 * }
		 * });
		 */
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}
}
