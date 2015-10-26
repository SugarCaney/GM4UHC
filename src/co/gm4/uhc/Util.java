package co.gm4.uhc;

/**
 * Several utility methods.
 * 
 * @author MrSugarCaney
 */
public class Util {

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
