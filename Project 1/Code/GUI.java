import javax.management.ImmutableDescriptor;
import javax.swing.JFrame;

public class GUI extends JFrame{
    GUI(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(getPreferredSize());
        this.setTitle("BattleShip V0.9");
        this.setLocationRelativeTo(null);

        ImagePanel image_panel = new ImagePanel();
        this.add(image_panel);

        this.setVisible(true);
    }
}
