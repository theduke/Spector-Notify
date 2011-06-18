/**
 * 
 */
package at.theduke.spector.notify;

/**
 * @author theduke
 *
 */
public class Helper {
	public static String mapSeverity(int severity) {
		String[] map = {
				"Emergency",
				"Alert",
				"Critical",
				"Error",
				"Warning",
				"Notice",
				"Informational",
				"Debug"};
		
		return map[severity];
	}
}
