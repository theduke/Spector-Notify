/**
 * 
 */
package at.theduke.spector.notify.notifier;

import com.mongodb.DBObject;

/**
 * @author theduke
 *
 */
public interface Notifier {
	public void notify(DBObject entry);
	public void test();
}
