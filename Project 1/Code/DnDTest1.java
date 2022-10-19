/* DnDTest1.java tests the drag and drop functionality using JFrame.  Most of the code referenced an instructional
 * video hosted on Youtube.com by creator Coding River.
 * Authors: Ryan Collins, John Schmidt
 * Date: 10/05/2022
 */

import javax.swing.*;

public class DnDTest1 extends JFrame {

    DnDTest1() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);
        this.setTitle("DnDTest1");
        this.setLocationRelativeTo(null);

        ImagePanel image_panel = new ImagePanel();

        this.add(image_panel);

        this.setVisible(true);
    }
}
