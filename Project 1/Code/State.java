/* The State class is used to maintain which state the current player is in. In it's current
 * implementation, it is "open for modification". If there is time, we will move this class to a 
 * State interface, and follow a more loosely connected approach using the State design pattern
 * Authors: Ryan Collins
 * Last Update: 10/19/2022
 */
public class State {
    final static int SETUP = 0;
    final static int SELECTING_HOST = 1;
    final static int CONNECT_TO_HOST = 2;
    final static int SHIP_PLACEMENT = 3;
    final static int SELECTING_VOLLEY = 4;
    final static int AWAITING_INCOMING_VOLLEY = 5;
    final static int END_GAME = 6;
    public int currentState;

    public State(){
        currentState = SETUP;
    }

}
