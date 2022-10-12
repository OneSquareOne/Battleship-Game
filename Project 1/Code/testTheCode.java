import java.net.InetAddress;
import java.net.UnknownHostException;

public class testTheCode {
    public static void main(String[] args) {
        try { //returns the server name
            final String serverName = InetAddress.getLocalHost().getHostName();
            System.out.println(serverName);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
