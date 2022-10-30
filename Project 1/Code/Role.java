/* Role is the interface for the server and client roles. It is mostly self explanatory.
 * Author: Ryan Collins, John Schmidt
 * Last Update: 10/30/2022
 */

import java.io.IOException;
public abstract class Role {

    public abstract void startConnection() throws IOException;

    public abstract Object receive() throws IOException;

    public abstract void send(Object obj);

    public abstract void closeConnection();

    public abstract String getRole();
}
