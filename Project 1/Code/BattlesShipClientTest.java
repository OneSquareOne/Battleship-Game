
public class BattlesShipClientTest {
	public static void main(String args[]) {
        BattleShipClient application; // declare client application

        // if no command line args
        if (args.length == 0)
            application = new BattleShipClient("192.168.4.87"); // connect to localhost
        else
            application = new BattleShipClient(args[0]); // use args to connect

        application.runClient(); // run client application
    } // end main
} // end class ClientTest

