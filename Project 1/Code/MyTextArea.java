/* MyTextArea allows for a custom background to be added to a JTextArea
 * Authors: Ryan Collins, John Schmidt
 * Last Update: 10/30/2022
 */

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JTextArea;

public class MyTextArea extends JTextArea {

    private Image img;

    public MyTextArea() {
        super();
        try {
            img = ImageIO.read(new File("./Images/Other/bg1-2.jpg"));
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    //for item repainting
    protected void paintComponent(Graphics g) {
        g.drawImage(img, 0, 0, null);
        super.paintComponent(g);
    }
}
