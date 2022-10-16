/* BattleShipClient is the client side extension for the server and client roles.
 * Author: Ryan Collins, John Schmidt
 * Last Update: 10/15/2022
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
        } // end try
        catch (EOFException eofException) {
            System.out.println("\nClient terminated connection");
        } // end catch
        catch (IOException ioException) {
            ioException.printStackTrace();
        } // end catch
    } // end method runClient

    // connect to server
    private void connectToServer() throws IOException {
        // create Socket to make connection to server
        client = new Socket(playServer, 5000);

        // display connection information
        System.out.println("Connected to: " +
                client.getInetAddress().getHostName());// TODO: written to in-game text box
    } // end method connectToServer

    // get streams to send and receive data
    private void getStreams() throws IOException {
        // set up output stream for objects
        output = new ObjectOutputStream(client.getOutputStream());
        output.flush(); // flush output buffer to send header information

        // set up input stream for objects
        input = new ObjectInputStream(client.getInputStream());

        System.out.println("\nGot I/O streams\n");
    } // end method getStreams

    public Object receive() throws IOException {
        Object obj = null;
        try {
            obj = input.readObject();
        } // end try
        catch (ClassNotFoundException classNotFoundException) {
            System.out.println("\nUnknown object type received");
        } // end catch
        return obj;
    } // end method sendObject

    // send int to client
    public void send(Object obj) {
        try {
            output.writeObject(obj);
            output.flush(); // flush output to client
        } // end try
        catch (IOException ioException) {
            System.out.println("\nError writing object");
        } // end catch
    } // end send

    // close streams and socket
    public void closeConnection() {
        System.out.println("\nClosing connection");

        try {
            output.close(); // close output stream
            input.close(); // close input stream
            client.close(); // close socket
        } // end try
        catch (IOException ioException) {
            ioException.printStackTrace();
        } // end catch
    } // end method closeConnection

    public String getRole() {
        return "Client";
    }
}
