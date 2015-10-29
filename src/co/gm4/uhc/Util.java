package co.gm4.uhc;

import java.util.Collection;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

/**
 * Several utility methods.
 * 
 * @author MrSugarCaney
 */
public class Util {

	/**
	 * Turns an amount of seconds into a countdown string.
	 * <p>
	 * ex1. 60 -> 1 minute <br>
	 * ex2. 30 -> 30 seconds <br>
	 * ex3. 10 -> 10 seconds <br>
	 * ex4. 1 -> 1 second
	 * 
	 * @param seconds
	 *            The amount of seconds left of the countdown.
	 * @return The nicely formatted countdown string without starting and
	 *         trailing spaces.
	 */
	public static String toCountdownString(int seconds) {
		int sec = seconds % 60;

		if (seconds >= 60) {
			int min = seconds / 60;

			String string = min + " minute" + (min == 1 ? "" : "s");
			if (sec > 0) {
				string += " and " + sec + " second" + (sec == 1 ? "" : "s");
			}
			return string;
		}
		else {
			return sec + " second" + (sec == 1 ? "" : "s");
		}
	}

	/**
	 * Converts a collection of players to a readable string.
	 * <p>
	 * Each name is seperated by commas.
	 * <p>
	 * Example: <i>MrSugarCaney, PenguinJ2</i>
	 * 
	 * @param players
	 *            Collection of player UUIDs.
	 * @return Formatted string.
	 */
	public static String toString(Collection<UUID> players) {
		StringBuilder sb = new StringBuilder();

		for (UUID playerId : players) {
			OfflinePlayer player = Bukkit.getOfflinePlayer(playerId);
			sb.append(player.getName());
			sb.append(", ");
		}

		sb.deleteCharAt(sb.length() - 1);
		sb.deleteCharAt(sb.length() - 1);

		return sb.toString();
	}

	/**
	 * Creates a new string with <i>length</i> times the character <i>c</i>.
	 * 
	 * @param c
	 *            The character to repeat.
	 * @param length
	 *            The amount of characters to put in the string.
	 * @return A string containing <i>length</i> times the character <i>c</i>.
	 */
	public static String fill(char c, int length) {
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			sb.append(c);
		}
		return sb.toString();
	}

	/**
	 * Checks how many capital letters a given string contains and calculates
	 * the percentage.
	 * <p>
	 * Ignoring spaces.
	 * 
	 * @param string
	 *            The string to analyse.
	 * @return A value between <i>0</i> (no caps) and <i>1</i> (all caps).
	 */
	public static float analyseCaps(String string) {
		String trimmed = string.replace(" ", "");
		float length = trimmed.length();
		float caps = 0;

		for (char c : trimmed.toCharArray()) {
			String s = c + "";
			if (s.matches("[A-Z]")) {
				caps++;
			}
		}

		return caps / length;
	}

}
