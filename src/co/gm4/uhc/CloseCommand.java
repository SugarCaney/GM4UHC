package co.gm4.uhc;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * This command will close the server. All new players will become spectators.
 * <p>
 * Usage: /close
 * 
 * @author MrSugarCaney
 */
public class CloseCommand implements CommandExecutor {

	/**
	 * Main instance of the plugin.
	 */
	private GM4UHC plugin;

	public CloseCommand(GM4UHC plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		plugin.setOpen(!plugin.isOpen());
		
		StringBuilder message = new StringBuilder();
		message.append(ChatColor.DARK_GREEN);
		message.append(ChatColor.BOLD);
		message.append(plugin.isOpen() ? "OPENED " : "CLOSED ");
		message.append(ChatColor.GREEN);
		message.append("Joining has been " + (plugin.isOpen() ? "enabled" : "disabled") + "!");
		sender.sendMessage(message.toString());
		
		return true;
	}

}
