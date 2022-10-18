import javax.swing.JPanel;

import java.util.jar.JarFile;

import javax.imageio.plugins.tiff.ExifGPSTagSet;
import javax.swing.JFrame;
import java.awt.GridLayout;

public class FrameTest extends JFrame{
    JPanel p = new JPanel();
    XOButton buttons[] = new XOButton[9];
    public static void main(String args[]){

    }

    public FrameTest(){
        super("Tester");
        setSize(400, 400);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        p.setLayout(new GridLayout(3, 3));
        for(int i=0; i<9; i++){
            buttons[i] = new XOButton();
        }
        add(p);
        
        setVisible(true);
    }
}