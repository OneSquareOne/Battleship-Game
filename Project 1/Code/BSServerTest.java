
public class BSServerTest {
    public static void main(String[] args) {
        String data = "This is some string data";
        int [] coordinates = {45, 16, 28, 9};
        BattleShipServer server = new BattleShipServer();
        System.out.println(server.getServerName());
        server.startServer();
        System.out.println("Server connected!");
        server.sendStringData(data);
        System.out.println("Sent String data");
        server.sendIntArrayData(coordinates);
        System.out.println("Sent integer data");
    }
}
