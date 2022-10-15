import java.io.IOException;
import java.util.Scanner;

public class BSServerTest {
    public static void main(String[] args) throws IOException {
        String data = "This is some string data";
        //int [] coordinates = {45, 16, 28, 9};
        Scanner keyboard = new Scanner(System.in);
        BattleShipServer server = new BattleShipServer();
        System.out.println(server.getServerName());
        server.startServer();
        System.out.println("Server connected!");
        server.send(data);
        System.out.println("Sent String data");
        data = "Data2";
        server.send(data);
        data = keyboard.nextLine();
        server.send(data);
        server.closeConnection();
        keyboard.close();
        
    }
}
