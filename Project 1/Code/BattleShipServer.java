/* BattleShipServer is the server side extension for the server and client roles.
 * Author: Ryan Collins, John Schmidt
 * Last Update: 10/30/2022
 */

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.net.InetAddress;

public class BattleShipServer extends Role {
    private ObjectOutputStream output; // output stream to client
    private ObjectInputStream input; // input stream from client
    private ServerSocket server; // server socket
    private Socket connection; // connection to client
    private final int SERVER_PORT = 5000;
    private String serverName;

    // constructor
    public BattleShipServer() {
        try { // returns the server name
            final String SERVER_NAME = InetAddress.getLocalHost().getHostName();
            serverName = SERVER_NAME; // SERVER_NAME has to be in try/catch so you can't have it
                                      // with other variables
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    //for polymorphic use
    public void startConnection(){
        startServer();
    }

    // set up and run server
    private void startServer() {
        try 
        {
            server = new ServerSocket(SERVER_PORT); // create ServerSocket

            try {
                waitForConnection(); // wait for a connection
                getStreams(); // get input & output streams
            }
            catch (EOFException eofException) {
                System.out.println("\nServer terminated connection");
            } 
        } 
        catch (IOException ioException) {
            ioException.printStackTrace();
        } 
    } 

    // wait for connection to arrive, then display connection info
    private void waitForConnection() throws IOException {
        connection = server.accept(); // allow server to accept connection
    } 

    // get streams to send and receive data
    private void getStreams() throws IOException {
        // set up output stream for objects
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush(); // flush output buffer to send header information

        // set up input stream for objects
        input = new ObjectInputStream(connection.getInputStream());
    } 

    //read in a native Object type; blocking call
    public Object receive() throws IOException {
        Object obj = null;
        try {
            obj = input.readObject();
        } 
        catch (ClassNotFoundException classNotFoundException) {
            System.out.println("\nUnknown object type received");
        }
        return obj;
    } 

    // send native Object type to client; blocking call
    public void send(Object obj) {
        try {
            output.writeObject(obj);
            output.flush(); // flush output to client
        } 
        catch (IOException ioException) {
            System.out.println("\nError writing object");
        } 
    } 

    public String getServerName() {
        return serverName;
    }

    // close streams and socket
    public void closeConnection() {
        try {
            output.close();
            input.close(); 
            connection.close(); 
        } 
        catch (IOException ioException) {
            ioException.printStackTrace();
        } 
    }

    public String getRole() {
        return "Server";
    }
}
