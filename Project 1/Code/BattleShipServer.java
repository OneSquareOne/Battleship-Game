import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class BattleShipServer {
    private ObjectOutputStream output; // output stream to client
    private ObjectInputStream input; // input stream from client
    private ServerSocket server; // server socket
    private Socket connection; // connection to client
    private int counter = 1; // counter of number of connections
    private Scanner keyboard = new Scanner(System.in);

    //constructor
    public BattleShipServer(){

    }

    // set up and run server
    public void runServer() {
        try // set up server to receive connections; process connections
        {
            server = new ServerSocket(12345, 100); // create ServerSocket
            
           
                try {
                    waitForConnection(); // wait for a connection
                    getStreams(); // get input & output streams
                    processConnection(); // process connection
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
        System.out.println("Connection " + counter + " received from: " +
                connection.getInetAddress().getHostName());
    } // end method waitForConnection

    // get streams to send and receive data
    private void getStreams() throws IOException {
        // set up output stream for objects
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush(); // flush output buffer to send header information

        // set up input stream for objects
        input = new ObjectInputStream(connection.getInputStream());

        System.out.println("\nGot I/O streams\n");
    } // end method getStreams
      // process connection with client

    private void processConnection() throws IOException {
        String message = "-1";
        sendData(message); // send connection successful message

        do // process messages sent from client
        {
            try // read message and display it
            {
            	message = keyboard.nextLine();
            	sendData(message);
                message = (String) input.readObject(); // read new message
                System.out.println("\n" + message); // display message
            } // end try
            catch (ClassNotFoundException classNotFoundException) {
                System.out.println("\nUnknown object type received");
            } // end catch

        } while (!message.equals("CLIENT>>> TERMINATE"));
    } // end method processConnection

    // close streams and socket
    private void closeConnection() {
        try {
            output.close(); // close output stream
            input.close(); // close input stream
            connection.close(); // close socket
        } // end try
        catch (IOException ioException) {
            ioException.printStackTrace();
        } // end catch
    } // end method closeConnection

    // send message to client
    private void sendData(String message) {
        try // send object to client
        {
            output.writeObject("SERVER>>> " + message);
            output.flush(); // flush output to client
            System.out.println("\nSERVER>>> " + message);
        } // end try
        catch (IOException ioException) {
            System.out.println("\nError writing object");
        } // end catch
    } // end method sendData
}
