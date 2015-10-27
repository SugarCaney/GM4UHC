package co.gm4.uhc.chat;

import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author MrSugarCaney
 */
public class ChatFilter {

	/**
	 * List of all banned words.
	 */
	private List<String> banned;

	public ChatFilter(FileConfiguration config) {
		banned = config.getStringList("banned-words");
		banned.forEach(String::toLowerCase);
	}

	/**
	 * Checks if a message contains foul language i.e. words that are present in
	 * the banned word list.
	 * <p>
	 * It will ignore spaces and replaces common replaced characters like 4 & @
	 * by a.
	 * 
	 * @param msg
	 *            The message to check.
	 * @return <i>true</i> if foul language has been detected, <i>false</i> if
	 *         not.
	 */
	public boolean containsFoulLanguage(String msg) {
		String string = msg.toLowerCase();
		string = string.replace(" ", "");
		string = string.replace("_", "");
		string = string.replace("-", "");
		string = string.replace("@", "a");
		string = string.replace("4", "a");
		string = string.replace("0", "o");
		string = string.replace("!", "i");
		string = string.replace(":", "i");
		string = string.replace(";", "i");
		string = string.replace("\\", "l");
		string = string.replace("/", "l");
		string = string.replace("|", "l");
		string = string.replace("1", "l");
		string = string.replace("$", "s");
		string = string.replace("5", "s");
		string = string.replace("€", "e");
		string = string.replace("&", "e");
		string = string.replace("3", "e");
		string = string.replace("+", "t");
		string = string.replace("(", "c");
		string = string.replace("2", "z");
		string = string.replace("8", "b");
		string = string.replace("9", "g");
		string = string.replace("*", "");
		string = string.replace("7", "t");
		string = string.replace("á", "a");
		string = string.replace("à", "a");
		string = string.replace("â", "a");
		string = string.replace("ä", "a");
		string = string.replace("ó", "o");
		string = string.replace("ò", "o");
		string = string.replace("ô", "o");
		string = string.replace("ö", "o");
		string = string.replace("í", "i");
		string = string.replace("ì", "i");
		string = string.replace("î", "i");
		string = string.replace("ï", "i");
		string = string.replace("è", "e");
		string = string.replace("é", "e");
		string = string.replace("ë", "e");
		string = string.replace("ê", "e");
		string = string.replace("û", "u");
		string = string.replace("ü", "u");
		string = string.replace("ù", "u");
		string = string.replace("ú", "u");
		string = string.replace("ý", "y");
		string = string.replace("ÿ", "y");
		string = string.replace("ç", "c");
		string = string.replace("ñ", "n");
		string = string.replace("ß", "b");
		string = string.replaceAll("(ooo*)", "oo");

		boolean contains = false;
		for (String ban : banned) {
			if (string.contains(ban)) {
				contains = true;
				break;
			}
		}

		if (!contains) {
			if (string.contains("v")) {
				contains = containsFoulLanguage(string.replaceAll("v", "u"));
			}
		}

		return contains;
	}

}
