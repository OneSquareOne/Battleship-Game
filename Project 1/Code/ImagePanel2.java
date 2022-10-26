import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JPanel;

public class ImagePanel2 extends javax.swing.JFrame {
    Image backgroundImage = Toolkit.getDefaultToolkit().getImage("./Image/Other/bg1.jpg");

    public ImagePanel2() {
        this.setContentPane(new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, null);
            }
        });

        pack();
        setSize(1500, 900);
        setVisible(true);
    }
}
