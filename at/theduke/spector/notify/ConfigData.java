/**
 * 
 */
package at.theduke.spector.notify;

/**
 * @author theduke
 *
 */
public class ConfigData {
	public String mongoHost = "localhost";
	public int mongoPort = 27017;
	public String mongoDatabase = "spector";
	
	public String mongoUser;
	public String mongoPassword;
	
	public String lastId;
	
	public boolean isValid() {
		if ((mongoHost.length() > 0) && (mongoPort != 0) && (mongoDatabase.length() > 0)) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean authRequired() {
		return ((mongoUser.length() > 0) && (mongoPassword.length() > 0));
	}
}
