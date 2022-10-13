/* Role is the interface for the server and client roles.
 * Author: Ryan Collins, John Schmidt
 * Last Update: 10/12/2022
 */

public abstract class  Role {
    public abstract String getRole();
    public abstract void send(String message);
    public abstract void send(int []array);
}
