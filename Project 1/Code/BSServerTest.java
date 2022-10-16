import java.io.IOException;
import java.util.Scanner;

public class BSServerTest {
    public static void main(String[] args) throws IOException {
        String data = "This is some string data";
        int [] coordinates = {45, 16, 28, 9};
        Player player1 = new Player("John");
        int [][] grid = player1.getOceanGrid().getGridArray();
        
        player1.getOceanGrid().autoPlaceShips();
        Scanner keyboard = new Scanner(System.in);
        BattleShipServer server = new BattleShipServer();
        System.out.println(server.getServerName());
        server.startServer();
        System.out.println("Server connected!");
        server.send(coordinates);
        System.out.println("Sent integer array data");
        data = "Data2";
        server.send(data);
        System.out.println("Sent string data");
        server.send(grid);
        System.out.println("Sent 2d integer array data");
        server.closeConnection();
        keyboard.close();
        
    }
}
