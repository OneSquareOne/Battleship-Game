import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class BattleShipClient {
    private ObjectOutputStream output; // output stream to server
    private ObjectInputStream input; // input stream from server
    private String message = ""; // message from server
    private String playServer; // host server for this application
    private Socket client; // socket to communicate with server
    private Scanner keyboard = new Scanner(System.in);

    public BattleShipClient(String host) {
    	playServer = host;
    }
    
    public void runClient() {
        try // connect to server, get streams, process connection
        {
            connectToServer(); // create a Socket to make connection
            getStreams(); // get the input and output streams
            processConnection(); // process connection
        } // end try
        catch (EOFException eofException) {
            System.out.println("\nClient terminated connection");
        } // end catch
        catch (IOException ioException) {
            ioException.printStackTrace();
        } // end catch
        finally {
            closeConnection(); // close connection
        } // end finally
    } // end method runClient
    
 // connect to server
    private void connectToServer() throws IOException {
    	System.out.println("Attempting connection\n");

        // create Socket to make connection to server
        client = new Socket(InetAddress.getByName(playServer), 12345);

        // display connection information
        System.out.println("Connected to: " +
                client.getInetAddress().getHostName());
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
    
    // process connection with server
    private void processConnection() throws IOException {
        do // process messages sent from server
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

        } while (!message.equals("SERVER>>> TERMINATE"));
    } // end method processConnection
    
    // close streams and socket
    private void closeConnection() {
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
    
    // send message to server
    private void sendData(String message) {
        try // send object to server
        {
            output.writeObject("CLIENT>>> " + message);
            output.flush(); // flush data to output
            System.out.println("\nCLIENT>>> " + message);
        } // end try
        catch (IOException ioException) {
        	System.out.println("\nError writing object");
        } // end catch
    } // end method sendData
}
