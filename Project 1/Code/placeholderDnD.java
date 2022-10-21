package dragdrop;

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
public class placeholdDnD extends JPanel implements ActionListener {
	//
  //
  //  it needs a main to run but you should be able to just copy and past this code and replace the right grid with it
  //
  //
	boolean hor;
	int shipnum;
	public placeholdDnD () {
		
	JFrame frame = new JFrame();
    frame.setBackground(Color.LIGHT_GRAY);	
		
    hor=false;
    shipnum=0;
	
	JButton Horez=new JButton("Horezontal");
	Horez.setBounds(950, 25, 100, 50);
	
	
	Horez.addActionListener(new ActionListener() {
		
        @Override
        public void actionPerformed(ActionEvent e) {
        	if(hor==false) {
        		hor=true;
        	}else {
        		hor=false;
        	}
        	JOptionPane.showMessageDialog(frame,hor);
            
            
        }
    });
		
	frame.add(Horez);
	
	JPanel buttonGrid2 = new JPanel();
    buttonGrid2.setLayout(new GridLayout(10,10));
    
    buttonGrid2.setSize(25,25);
    
    for(int i = 0; i < 10; i++)
    {
    	for(int j = 0; j < 10; j++)
    	{
    		char c = (char) ('A' + i);
    		String str= c + " "+(j+1); 		
    		JButton temp=new JButton(str);
    		temp.addActionListener(new ActionListener() {

    			public void actionPerformed(ActionEvent e) {
					JOptionPane.showMessageDialog(frame,hor+" "+shipnum+" ["+str+"]" );
					//send to check function, if check return true .setEnabled(false); to the shipnum button and set shipnum =0
				}
                
            });
    		buttonGrid2.add(temp);
    		
    	}
    }
    buttonGrid2.setBounds(950, 300, 450, 450);
    frame.add(buttonGrid2);

    JPanel boats = new JPanel();
    boats.setLayout(new GridLayout(2,2));
    
    
    JButton ship1=new JButton("1");
    ship1.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            shipnum=1;
        }
    }); boats.add(ship1);
    
    JButton ship2=new JButton("2");
    ship2.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            shipnum=2;
        }
    }); boats.add(ship2);
    
    JButton ship3=new JButton("3");
    ship3.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            shipnum=3;
        }
    }); boats.add(ship3);
    
    JButton ship4=new JButton("4");
    ship4.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            shipnum=4;
        }
    }); boats.add(ship4);
    
    
    boats.setBounds(950, 100, 450, 160);
    frame.add(boats);
    
      
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
