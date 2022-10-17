package grid;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;  
import javax.swing.JFrame;  
import javax.swing.JLabel;  
import javax.swing.JPanel;  
public class JFrameExample {  
    public static void main(String s[]) {  
        JFrame frame = new JFrame();
 
          
        
        JPanel buttonGrid = new JPanel();
        buttonGrid.setLayout(new GridLayout(10,10));
        
        
        /*-
        JPanel testpan = new JPanel();
        testpan.setBackground(Color.red);
        testpan.setBounds(0, 0, 250, 250);
        frame.add(testpan);
        -*/
        
        for(int i = 0; i < 10; i++)
        {
        	for(int j = 0; j < 10; j++)
        	{
        		char c = (char) ('A' + i);
        		String str = c + " "+(j+1);
        		buttonGrid.add(new JButton(str));
        	}
        }
        buttonGrid.setBounds(950, 300, 450, 450);
        frame.add(buttonGrid);
        
        
        JPanel buttonGrid2 = new JPanel();
        buttonGrid2.setLayout(new GridLayout(10,10));
        
        buttonGrid2.setSize(25,25);
        for(int i = 0; i < 10; i++)
        {
        	for(int j = 0; j < 10; j++)
        	{
        		char c = (char) ('A' + i);
        		String str = c + " "+(j+1);
        		buttonGrid2.add(new JButton(str));
        	}
        }
        buttonGrid2.setBounds(150, 100, 650, 650);
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
}  
