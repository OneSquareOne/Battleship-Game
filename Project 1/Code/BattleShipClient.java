/* BattleShipClient is the client side extension for the server and client roles.
 * Author: Ryan Collins, John Schmidt
 * Last Update: 10/30/2022
 */

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class BattleShipClient extends Role {
    private ObjectOutputStream output; // output stream to server
    private ObjectInputStream input; // input stream from server
    private String playServer; // host server for this application
    private Socket client; // socket to communicate with server

    public BattleShipClient(String serverName) {
        playServer = serverName; // set playServer to initiate connection correctly
    }

    // for polymorphic use
    public void startConnection() throws IOException {
        runClient();
    }

    private void runClient() {
        try {
            connectToServer(); // create a Socket to make connection
            getStreams(); // get the input and output streams
        } catch (EOFException eofException) {
            System.out.println("\nClient terminated connection");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    // connect to server
    private void connectToServer() throws IOException {
        // create Socket to make connection to server
        client = new Socket(playServer, 5000);
    }

    // get streams to send and receive data
    private void getStreams() throws IOException {
        // set up output stream for objects
        output = new ObjectOutputStream(client.getOutputStream());
        output.flush(); // flush output buffer to send header information

        // set up input stream for objects
        input = new ObjectInputStream(client.getInputStream());
    }

    // read in a native Object type; blocking call
    public Object receive() throws IOException {
        Object obj = null;
        try {
            obj = input.readObject();
        } catch (ClassNotFoundException classNotFoundException) {
            System.out.println("\nUnknown object type received");
        }
        return obj;
    }

    // send native Object type to client; blocking call
    public void send(Object obj) {
        try {
            output.writeObject(obj);
            output.flush(); // flush output to client
        } catch (IOException ioException) {
            System.out.println("\nError writing object");
        }
    }

    // close streams and socket
    public void closeConnection() {
        System.out.println("\nClosing connection");

        try {
            output.close();
            input.close();
            client.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public String getRole() {
        return "Client";
    }
}
