import java.util.Scanner;
public class BSClientTest {
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        String serverName = keyboard.next();
        BattleShipClient client = new BattleShipClient(serverName);
        client.runClient();
        System.out.println("Connection to server successful");
        keyboard.close();
    }
    
}
