/* Subject is the interface class for the Subject/Observer design pattern.  It is used for one-way communication between
 * the subject and observer class.  It follows the publisher/subscription model
 * Authors: Ryan Collins, John Schmidt
 * Last Update: 10/4/2022
 */

public interface Subject {
	public void registerObserver(Observer o);
	public void removeObserver(Observer o);
	public void notifyObservers();
}
