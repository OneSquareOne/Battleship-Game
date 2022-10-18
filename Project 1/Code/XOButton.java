import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class XOButton extends JButton implements ActionListener{
    ImageIcon X,O;
    byte value =0;

    public XOButton(){
        X=new ImageIcon(this.getClass().getResource("null"));
        O=new ImageIcon(this.getClass().getResource("null"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        
    }
}
