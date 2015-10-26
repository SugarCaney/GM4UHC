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

}
