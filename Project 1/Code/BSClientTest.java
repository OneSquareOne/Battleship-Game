import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
public class BSClientTest {
    public static void main(String[] args) throws IOException {
        String message;
        int[] coordinates;
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Enter server name:");
        String serverName = keyboard.next();
        BattleShipClient client = new BattleShipClient(serverName);
        client.runClient();
        System.out.println("Connection to server successful");
        message = client.readStringObject();
        System.out.println("From server: " + message);
        coordinates = client.readIntArrayObject();
        System.out.print (Arrays.toString(coordinates));
        keyboard.close();
    }
    
}
