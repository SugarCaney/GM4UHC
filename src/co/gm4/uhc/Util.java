package co.gm4.uhc;

import java.util.Collection;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Several utility methods.
 * 
 * @author MrSugarCaney
 */
public class Util {

	/**
	 * Saves a location to the configuration file.
	 * 
	 * @param loc
	 *            The location to save.
	 * @param node
	 *            Where to save the location.
	 * @param plugin
	 *            The main plugin instance.
	 */
	public static void saveLocation(Location loc, String node, GM4UHC plugin) {
		FileConfiguration config = plugin.getConfig();
		config.set(node + ".x", loc.getX());
		config.set(node + ".y", loc.getY());
		config.set(node + ".z", loc.getZ());
		config.set(node + ".pi", loc.getPitch());
		config.set(node + ".yw", loc.getYaw());
		config.set(node + ".w", loc.getWorld().getName());
		plugin.saveConfig();
	}

	/**
	 * Deserialises a location from the config file.
	 * 
	 * @param node
	 *            The location of the location in the configuration.
	 * @param plugin
	 *            The main plugin instance.
	 * @return The parsed location. Or <i>null</i> if there was any problem.
	 */
	public static Location loadLocation(String node, GM4UHC plugin) {
		// #YayForDirtyHacksThatWorkRegardlessly
		try {
			FileConfiguration config = plugin.getConfig();
			double x = config.getDouble(node + ".x");
			double y = config.getDouble(node + ".y");
			double z = config.getDouble(node + ".z");
			float pitch = (float)config.getDouble(node + ".pi");
			float yaw = (float)config.getDouble(node + ".yw");
			String w = config.getString(node + ".w");
			World world = plugin.getServer().getWorld(w);

			Location loc = new Location(world, x, y, z);
			loc.setYaw(yaw);
			loc.setPitch(pitch);
			return loc;
		}
		catch (Exception e) {
			return null;
		}
	}

	/**
	 * Calculates the absolute distance between two locations.
	 * 
	 * @param loc1
	 *            First location.
	 * @param loc2
	 *            Second location.
	 * @return The distance between the points in meters.
	 */
	public static double distance(Location loc1, Location loc2) {
		double dx2 = Math.pow(Math.abs(loc1.getX() - loc2.getX()), 2);
		double dy2 = Math.pow(Math.abs(loc1.getY() - loc2.getY()), 2);
		double dz2 = Math.pow(Math.abs(loc1.getZ() - loc2.getZ()), 2);
		return Math.sqrt(dx2 + dy2 + dz2);
	}

	/**
	 * Gives a new integer between lower and upper.
	 * 
	 * @param lower
	 *            The lower bound.
	 * @param upper
	 *            The upper bound.
	 * @param ran
	 *            Random object to use.
	 * @return A random number n with lower<=n<=upper.
	 */
	public static int randomRange(int lower, int upper, Random ran) {
		return lower + ran.nextInt(upper - lower + 1);
	}

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
	public static String toCountdownString(long seconds) {
		long sec = seconds % 60;

		if (seconds >= 60) {
			long min = seconds / 60;

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
