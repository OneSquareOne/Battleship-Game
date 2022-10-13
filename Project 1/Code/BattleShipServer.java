import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.net.InetAddress;

public class BattleShipServer extends Role{
    private ObjectOutputStream output; // output stream to client
    private ObjectInputStream input; // input stream from client
    private ServerSocket server; // server socket
    private Socket connection; // connection to client
    private final int SERVER_PORT = 5000;
    private String serverName;

    // constructor
    public BattleShipServer() {
        super();
        try { // returns the server name
            final String SERVER_NAME = InetAddress.getLocalHost().getHostName();
            serverName = SERVER_NAME; // SERVER_NAME has to be in try/catch so you can't have it
                                      // with other variables
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }// end constructor

    // set up and run server
    public void startServer() {
        try // set up server to receive connections; process connections
        {
            server = new ServerSocket(SERVER_PORT); // create ServerSocket

            try {
                waitForConnection(); // wait for a connection
                getStreams(); // get input & output streams
            } // end try
            catch (EOFException eofException) {
                System.out.println("\nServer terminated connection");
            } // end catch
        } // end try
        catch (IOException ioException) {
            ioException.printStackTrace();
        } // end catch
    } // end method runServer

    // wait for connection to arrive, then display connection info
    private void waitForConnection() throws IOException {
        connection = server.accept(); // allow server to accept connection
        System.out.println("Connection received from: " +
                connection.getInetAddress().getHostName()); // TODO: written to in-game text box
    } // end method waitForConnection

    // get streams to send and receive data
    private void getStreams() throws IOException {
        // set up output stream for objects
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush(); // flush output buffer to send header information

        // set up input stream for objects
        input = new ObjectInputStream(connection.getInputStream());
    } // end method getStreams

    /*
     * FROM ORIGINAL CODE
     * private void processConnection() throws IOException {
     * String message = "-1";
     * sendData(message); // send connection successful message
     * 
     * do // process messages sent from client
     * {
     * try // read message and display it
     * {
     * message = keyboard.nextLine();
     * sendData(message);
     * message = (String) input.readObject(); // read new message
     * System.out.println("\n" + message); // display message
     * } // end try
     * catch (ClassNotFoundException classNotFoundException) {
     * System.out.println("\nUnknown object type received");
     * } // end catch
     * 
     * } while (!message.equals("CLIENT>>> TERMINATE"));
     * } // end method processConnection
     */

    // close streams and socket
    public void closeConnection() {
        try {
            output.close(); // close output stream
            input.close(); // close input stream
            connection.close(); // close socket
        } // end try
        catch (IOException ioException) {
            ioException.printStackTrace();
        } // end catch
    } // end method closeConnection

    // send String to client
    public void send(String message) {
        boolean finished = false;
        Object unknownType;
        do{
        try {
            unknownType = input.readObject();
            output.writeObject("SERVER>>> " + message);
            output.flush(); // flush output to client
        } // end try
        catch (IOException ioException) {
            System.out.println("\nError writing object");
        } // end catch
    }while (finished);
    } // end sendStringData

    // send int[] to client
    public void send(int[] arr) {
        try {
            output.writeObject(arr);
            output.flush(); // flush output to client
        } // end try
        catch (IOException ioException) {
            System.out.println("\nError writing object");
        } // end catch
    } // end sendIntArrayData

    // returns current server name
    public String getServerName() {
        return serverName;
    }

    @Override
    public String getRole() {
        return "Server";
    }
}
