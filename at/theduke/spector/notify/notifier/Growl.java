/**
 * 
 */
package at.theduke.spector.notify.notifier;

import java.io.IOException;
import java.util.ArrayList;

import at.theduke.spector.notify.Helper;

import com.mongodb.DBObject;

/**
 * @author theduke
 *
 */
public class Growl implements Notifier {

	/* (non-Javadoc)
	 * @see at.theduke.spector.notify.notifier.Notifier#notify(com.mongodb.DBObject)
	 */
	@Override
	public void notify(DBObject entry) {
		String severity = Helper.mapSeverity(Integer.parseInt(entry.get("severity").toString()));
		
		send(severity, entry.get("message").toString());
	}
	
	protected void send(String title, String message) {
		ArrayList<String> args = new ArrayList<String>();
		
		args.add("/usr/local/bin/growlnotify");
		args.add("-m");
		args.add(message);
		args.add("-t");
		args.add(title);
		
		try {
			Process exec = Runtime.getRuntime().exec( args.toArray(new String[] {}) );
		} catch (IOException e) {
		}
	}

	/* (non-Javadoc)
	 * @see at.theduke.spector.notify.notifier.Notifier#test()
	 */
	@Override
	public void test() {
		send("Title", "Message");
	}

}
