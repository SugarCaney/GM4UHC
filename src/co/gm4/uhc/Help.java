package co.gm4.uhc;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Executes all help commands.
 * 
 * @author MrSugarCaney
 */
public class Help implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// COLOURS
		if (label.toLowerCase().matches("(colours|colors|accents)")) {
			StringBuilder sb = new StringBuilder();
			for (ChatColor color : ChatColor.values()) {
				if (color.name().equalsIgnoreCase("MAGIC")
						|| color.name().equalsIgnoreCase("RESET")) {
					continue;
				}

				sb.append(color.name());
				sb.append(", ");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.deleteCharAt(sb.length() - 1);

			sender.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD
					+ "List of colours and accents: " + ChatColor.WHITE + sb.toString());

			return true;
		}

		// UHC
		else if (label.equalsIgnoreCase("uhc")) {
			sender.sendMessage(
					ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "GM4UHC Plugin by MrSugarCaney "
							+ ChatColor.GREEN + "Made with love for the community <3");
			return true;
		}

		// GM4
		else if (label.equalsIgnoreCase("gm4")) {
			sender.sendMessage(
					ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Gamemode 4 by AccidentalGames "
							+ ChatColor.GREEN + "Enhancing the vanilla gameplay experience");
			return true;
		}

		return false;
	}

}
