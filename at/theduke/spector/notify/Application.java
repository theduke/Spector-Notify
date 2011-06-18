/**
 * 
 */
package at.theduke.spector.notify;

import java.net.UnknownHostException;

import at.theduke.spector.notify.notifier.Growl;
import at.theduke.spector.notify.notifier.Notifier;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

/**
 * @author theduke
 *
 */
public class Application {

	private ConfigData config;
	private boolean notificationsEnabled = false;
	
	private Fetcher fetcher;
	
	private Notifier notifier;
	
	private Swt swtApp;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Application app = new Application();
		app.run();
	}
	
	public Application() {
		config = Configuration.readConfiguration();
		
		notifier = new Growl();
	}
	
	private void run() {
		swtApp = new Swt();
		swtApp.run(this);
	}
	
	public void initializeMongo(ConfigData config) {
		System.out.println("Initializing Mongo connection");
		
		if (config != null && config.isValid()) {
			System.out.println("Config validated successfully");
			
			try {
				fetcher = new Fetcher(config);
				notificationsEnabled = true;
				
				swtApp.showToolTip("Connected to database.");
			} catch (UnknownHostException e) {
				swtApp.showAlert("Spector Notify", "The specified Mongo Host could not be found. Check settings.");
			} catch (MongoException e) {
				swtApp.showAlert("Spector Notify", "Could not connect to the MongoDB server. Check settings and connection.");
			} catch (Exception e) {
				swtApp.showAlert("Spector Notify", e.getMessage());
			}
		} else {
			notificationsEnabled = false;
			
			System.out.println("Config is invalid.");
		}
	}
	
	public void doNotify() {
		if (!(notificationsEnabled && (config != null) && config.isValid())) return;
		
		DBCursor entries = fetcher.doFetch();
		
		boolean idSet = false;
		
		for (DBObject entry : entries) {
			notifier.notify(entry);
			
			if (!idSet) {
				config.lastId = entry.get("_id").toString();
				idSet = true;
			}
		}
		
		Configuration.writeConfiguration(config);
	}

	/**
	 * @return boolean the notificationsEnabled
	 */
	public boolean getNotificationsEnabled() {
		return notificationsEnabled;
	}

	/**
	 * @param boolean notificationsEnabled the notificationsEnabled to set
	 */
	public void setNotificationsEnabled(boolean notificationsEnabled) {
		this.notificationsEnabled = notificationsEnabled;
	}

	/**
	 * @return the config
	 */
	public ConfigData getConfig() {
		return config;
	}

	/**
	 * @param config the config to set
	 */
	public void setConfig(ConfigData config) {
		this.config = config;
	}

	/**
	 * @return the fetcher
	 */
	public Fetcher getFetcher() {
		return fetcher;
	}

	/**
	 * @param fetcher the fetcher to set
	 */
	public void setFetcher(Fetcher fetcher) {
		this.fetcher = fetcher;
	}
}
