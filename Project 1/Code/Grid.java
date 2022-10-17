package grid;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;  
import javax.swing.JFrame;  
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;  
public class JFrameExample implements ActionListener{  
    public static void main(String s[]) { 
    	
    	
        JFrame frame = new JFrame();
        frame.setBackground(Color.LIGHT_GRAY);
          
        
        JPanel buttonGrid = new JPanel();
        buttonGrid.setLayout(new GridLayout(11,11));
        
        
        /*-
        JPanel testpan = new JPanel();
        testpan.setBackground(Color.red);
        testpan.setBounds(0, 0, 250, 250);
        frame.add(testpan);
        -*/
        String str;
		JButton temp;
		
        for(int i = 0; i < 11; i++)
        {
        	switch (i) {
            case 0:  
            	str = "0 0";
	    		temp=new JButton(str);
	    		temp.setBackground(Color.LIGHT_GRAY);
	    		buttonGrid.add(temp);
                break;
            case 1:  
            	str = "A";
	    		temp=new JButton(str);
	    		temp.setBackground(Color.LIGHT_GRAY);
	    		buttonGrid.add(temp);
                break;
            case 2:  
            	str = "B";
	    		temp=new JButton(str);
	    		temp.setBackground(Color.LIGHT_GRAY);
	    		buttonGrid.add(temp);
                break;
            case 3:  
            	str = "C";
	    		temp=new JButton(str);
	    		temp.setBackground(Color.LIGHT_GRAY);
	    		buttonGrid.add(temp);
                break;
            case 4:  
            	str = "D";
	    		temp=new JButton(str);
	    		temp.setBackground(Color.LIGHT_GRAY);
	    		buttonGrid.add(temp);
                break;
            case 5:  
            	str = "E";
	    		temp=new JButton(str);
	    		temp.setBackground(Color.LIGHT_GRAY);
	    		buttonGrid.add(temp);
                break;
            case 6:  
            	str = "F";
	    		temp=new JButton(str);
	    		temp.setBackground(Color.LIGHT_GRAY);
	    		buttonGrid.add(temp);
                break;
            case 7:  
            	str = "G";
	    		temp=new JButton(str);
	    		temp.setBackground(Color.LIGHT_GRAY);
	    		buttonGrid.add(temp);
                break;
            case 8:  
            	str = "H";
	    		temp=new JButton(str);
	    		temp.setBackground(Color.LIGHT_GRAY);
	    		buttonGrid.add(temp);
                break;
            case 9:  
            	str = "I";
	    		temp=new JButton(str);
	    		temp.setBackground(Color.LIGHT_GRAY);
	    		buttonGrid.add(temp);
                break;
            case 10:  
            	str = "J";
	    		temp=new JButton(str);
	    		temp.setBackground(Color.LIGHT_GRAY);
	    		buttonGrid.add(temp);
                break;
        	}
        	for(int j = 1; j < 11; j++)
        	{
        		if(i==0) {
        			str =""+j;
            		temp=new JButton(str);
            		temp.setBackground(Color.LIGHT_GRAY);
            		buttonGrid.add(temp);
        		}else {
	        		char c = (char) ('A' + (i-1));
	        		String str1 = c + " "+(j);
	        		temp=new JButton(str1);
	        		temp.addActionListener(new ActionListener() {
	                    @Override
	                    public void actionPerformed(ActionEvent e) {
	                        JOptionPane.showMessageDialog(frame,str1 );
	                    }
	                });
	        		buttonGrid.add(temp);
        		}
        	}
        }
        buttonGrid.setBounds(150, 100, 650, 650);
        frame.add(buttonGrid);
        
        
        JPanel buttonGrid2 = new JPanel();
        buttonGrid2.setLayout(new GridLayout(10,10));
        
        buttonGrid2.setSize(25,25);
        for(int i = 0; i < 10; i++)
        {
        	for(int j = 0; j < 10; j++)
        	{
        		char c = (char) ('A' + i);
        		str = c + " "+(j+1);
        		buttonGrid2.add(new JButton(str));
        	}
        }
        buttonGrid2.setBounds(950, 300, 450, 450);
        frame.add(buttonGrid2);
        
        
        JPanel boats = new JPanel();
        //boats.setLayout(new GridLayout(3,3));
        boats.setBackground(Color.red);
        
        boats.setBounds(950, 100, 450, 160);
        frame.add(boats);
        
          
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        frame.setLayout(null);
        frame.setSize(750, 750);  
        frame.setVisible(true);
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
		
	}  
}  
