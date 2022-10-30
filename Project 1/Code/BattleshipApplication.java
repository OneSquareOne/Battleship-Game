/* BattleshipApplication contains the main() to start the Battleship game.
 * Author: Ryan Collins, John Schmidt
 * Last Update: 10/18/2022
 */

import java.io.IOException;
import javax.swing.text.BadLocationException;

public class BattleshipApplication {
    public static void main(String[] args) throws IOException, InterruptedException, BadLocationException {
        Controller mainController = new Controller();
        new Viewer(mainController);
        new Model(mainController);
        mainController.playGame();
    }
}
