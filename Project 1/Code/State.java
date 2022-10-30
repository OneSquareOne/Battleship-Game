/* The State class is used to maintain which state the current player is in. In it's current
 * implementation, it is "open for modification". If there is time, we will move this class to a 
 * State interface, and follow a more loosely connected approach using the State design pattern
 * Authors: Ryan Collins
 * Last Update: 10/29/2022
 */

public class State {
    static volatile int SETUP = 0;
    static volatile int SELECTING_SERVER = 1;
    static volatile int CONNECT_TO_SERVER = 2;
    static volatile int AWAITING_CLIENT_CONNECTION = 3;
    static volatile int SHIP_PLACEMENT = 4;
    static volatile int SELECTING_VOLLEY = 5;
    static volatile int AWAITING_INCOMING_VOLLEY = 6;
    static volatile int END_GAME = 7;
    public volatile int currentState;

    //constructor
    public State(){
        currentState = SETUP;
    }

}
