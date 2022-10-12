
public class BSServerTest {
    public static void main(String[] args) {
     
        BattleShipServer server = new BattleShipServer();
        System.out.println(server.getServerName());
        server.startServer();
        System.out.println("Server connected!");
    }
}
