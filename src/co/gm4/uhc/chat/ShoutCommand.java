package co.gm4.uhc.chat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import co.gm4.uhc.Util;

/**
 * Show a stand out broadcast message to all players.
 * 
 * @author MrSugarCaney
 */
public class ShoutCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "" + ChatColor.ITALIC
					+ "!! " + ChatColor.RED + "Can't send an empty message!");
			return false;
		}

		StringBuilder sb = new StringBuilder(" ");
		for (String word : args) {
			sb.append(word).append(" ");
		}
		String message = sb.toString();

		String header = ChatColor.BLUE + Util.fill('=', 62);

		Bukkit.broadcastMessage(header);
		Bukkit.broadcastMessage(ChatColor.AQUA + message);
		Bukkit.broadcastMessage(header);

		return true;
	}

}
