import java.io.IOException;
import java.util.Scanner;

public class BSClientTest {
    public static void main(String[] args) throws IOException {
        // int[] coordinates;
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Enter server name:");
        String serverName = keyboard.next();
        BattleShipClient client = new BattleShipClient(serverName);
        client.runClient();
        System.out.println("Connection to server successful");
        System.out.println("From server: " + client.receive());
        System.out.println("From server: " + client.receive());
        System.out.println("From server: " + client.receive());
        keyboard.close();
        client.closeConnection();
    }

}
