import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;

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
public class notificationfield extends JPanel implements ActionListener {
	
	
	public notificationfield (JFrame frame) {
		
		JTextField notifications=new JTextField();
		notifications.setText("Game information can be displayed here");
		notifications.setBounds(100, 200, 400, 50);
		frame.add(notifications);
		notifications.setEditable(false);		
		frame.add(notifications);
		
		
		JButton proofofconsept=new JButton("proof of consept");
		proofofconsept.setBounds(100, 25, 300, 50);
		
		frame.add(proofofconsept);
		proofofconsept.addActionListener(new ActionListener() {
			
	        @Override
	        public void actionPerformed(ActionEvent e) {     	
	        	notifications.setText("The text field has been changed");
	        }
	    });
	    
	      
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
	    frame.setLayout(null);
	    frame.setSize(750, 750);  
	    frame.setVisible(true);
}
	


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
