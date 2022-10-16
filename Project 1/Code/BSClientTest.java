import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class BSClientTest {
    public static void main(String[] args) throws IOException {
        int[][] coordinates;
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Enter server name:");
        String serverName = keyboard.next();
        BattleShipClient client = new BattleShipClient(serverName);
        client.startConnection();
        System.out.println("Connection to server successful");
        int[] arr = (int[]) client.receive();
        System.out.println("From server: " + Arrays.toString(arr));

        System.out.println("From server: " + client.receive());
        coordinates = (int[][]) client.receive();
        System.out.println(Arrays.toString(coordinates));
        keyboard.close();
        client.closeConnection();
    }

}
