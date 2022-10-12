import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class BattleShipClient {
    private ObjectOutputStream output; // output stream to server
    private ObjectInputStream input; // input stream from server
    private String playServer; // host server for this application
    private Socket client; // socket to communicate with server

    public BattleShipClient(String serverName) {
        playServer = serverName; // set playServer to initiate connection correctly
    }

    public void runClient() {
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

    /*
     * Previous code
     * // process connection with server
     * private void processConnection() throws IOException {
     * do // process messages sent from server
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
     * } while (!message.equals("SERVER>>> TERMINATE"));
     * } // end method processConnection
     */

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

    // send message to server
    public void sendStringData(String message) {
        try {
            output.writeObject("CLIENT>>> " + message);
            output.flush(); // flush data to output
        } // end try
        catch (IOException ioException) {
            System.out.println("\nError writing object");
        } // end catch
    } // end method sendData

    // send int[] to server
    public void sendIntArrayData(int[] arr) {
        try {
            output.writeObject(arr);
            output.flush(); // flush output to server
        } // end try
        catch (IOException ioException) {
            System.out.println("\nError writing object");
        } // end catch
    } // end sendIntArrayData

    public String readStringObject() throws IOException {
        String message = null;
        do {
            try {
                message = (String) input.readObject(); // read new message
            } catch (ClassNotFoundException classNotFoundException) {
                System.out.println("\nUnknown object type received");
            } // end catch
        } while (message == null);
        return message;
    }

    public int[] readIntArrayObject() throws IOException {
        int[] intArr = null;
        do {
            try {
                intArr = (int[]) input.readObject(); // read new message
                System.out.println("Integer array received"); // display message
            } catch (ClassNotFoundException classNotFoundException) {
                System.out.println("\nUnknown object type received");
            } // end catch
        } while (intArr == null);
        return intArr;
    }
}
