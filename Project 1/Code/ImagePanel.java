/* ImagePanel.java is used to test the drag and drop functionality using JFrame.  Most of the code referenced an instructional
 * video hosted on Youtube.com by creator Coding River.
 * Authors: Ryan Collins, John Schmidt
 * Date: 10/05/2022
 */

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.Graphics;
import java.awt.Point;

public class ImagePanel extends JPanel {

    // insert correct file path here to have test work on your system.
    ImageIcon image = new ImageIcon("./images/AircraftCarrier1.1.png");

    final int IMG_WIDTH = image.getIconWidth();
    final int IMG_HEIGHT = image.getIconHeight();
    Point image_corner;
    Point previousPoint;

    // constructor
    ImagePanel() {
        image_corner = new Point(0, 0);

        ClickListener clickListener = new ClickListener();
        this.addMouseListener(clickListener);

        DragListener dragListener = new DragListener();
        this.addMouseMotionListener(dragListener);

    }

    // used to "paint" the component in the Jframe
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        image.paintIcon(this, g, (int) image_corner.getX(), (int) image_corner.getY());
    }

    // sets previous point for click listener
    private class ClickListener extends MouseAdapter {
        public void mousePressed(MouseEvent evt) {
            previousPoint = evt.getPoint();
        }
    }

    // sets current point for drag listener
    private class DragListener extends MouseMotionAdapter {
        public void mouseDragged(MouseEvent evt) {
            Point currentPoint = evt.getPoint();

            image_corner.translate((int) (currentPoint.getX() - previousPoint.getX()),
                    (int) (currentPoint.getY() - previousPoint.getY()));

            previousPoint = currentPoint;

            repaint();
        }
    }
}
