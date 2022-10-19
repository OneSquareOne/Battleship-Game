/* The state class is used to maintain which state the current player is in. In it's current
 * implementation, it is "open for modification". If there is time, we will move this class to a 
 * State interface, and follow a more loosely connected approach using the State design pattern
 * Authors: Ryan Collins
 * Last Update: 10/19/2022
 */
public class State {
    final static int SETUP = 0;
    final static int ENTER_HOST_NAME = 1;
    final static int SHIP_PLACEMENT = 2;
    final static int SELECTING_VOLLEY = 3;
    final static int AWAITING_INCOMING_VOLLEY = 4;
    final static int END_GAME = 5;

}
