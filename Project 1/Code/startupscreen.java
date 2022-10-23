package dragdrop;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;  
import javax.swing.JFrame;  
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;  
import javax.swing.JButton;
import javax.swing.JFrame;


//////////////////////////////this is the needed main///////////////////////////
public class main {

	public static void main(String[] args) {
		new startupscreen();

	}

}
////////////////////////////////////////////////////////////////////////////////




public class startupscreen extends JPanel implements ActionListener{
	
	boolean amserver=true;
	String name;
	String servername;
	
	public startupscreen () {
	JFrame frame = new JFrame();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
    frame.setLayout(null);
    frame.setSize(750, 750);  
    frame.setVisible(true);
    ///////////////
    JTextField namefield=new JTextField();
	namefield.setText("Enter Player Name here");
	namefield.setBounds(100, 400, 100, 50);
	frame.add(namefield);
	
	JTextField serverfield=new JTextField();
	serverfield.setBackground(Color.LIGHT_GRAY);
	serverfield.setText("Enter server ID here");
	serverfield.setBounds(250, 400, 100, 50);
	frame.add(serverfield);
	/////////////////////////
	
	////////////////////////////
	JButton server=new JButton("I'll play as the server");
	server.setBounds(100, 25, 300, 50);
	
	frame.add(server);
	server.addActionListener(new ActionListener() {
		
        @Override
        public void actionPerformed(ActionEvent e) {     	
        		amserver=true;  
        		serverfield.setEditable(false);
        		serverfield.setText("");
        		serverfield.setBackground(Color.DARK_GRAY);
        }
    });
	///////////////////////////////
	
	////////////////////////
	JButton client=new JButton("I'll play as the client");
	client.setBounds(100, 150, 300, 50);
	
	frame.add(client);
	client.addActionListener(new ActionListener() {
		
        @Override
        public void actionPerformed(ActionEvent e) {     	
        		amserver=false; 
        		serverfield.setEditable(true);
        		serverfield.setText("Enter server ID here");
        		serverfield.setBackground(Color.LIGHT_GRAY);
        }
    });
	///////////////////////////
	
	
	////////////////////////
	JButton Enter=new JButton("Enter");
	Enter.setBounds(0, 25, 100, 50);
	
	frame.add(Enter);
	Enter.addActionListener(new ActionListener() {
		
        @Override
        public void actionPerformed(ActionEvent e) {
        	
        		frame.getContentPane().removeAll();
        		name=namefield.getText();
        		servername=serverfield.getText();
        		//submit all information
        		JOptionPane.showMessageDialog(frame,name+"  "+servername+"  "+amserver);
        		new notificationfield(frame);
            
        }
    });
	////////////////////////////////
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}	
}
