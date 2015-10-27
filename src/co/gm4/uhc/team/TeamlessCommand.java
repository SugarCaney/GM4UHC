package co.gm4.uhc.team;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.gm4.uhc.GM4UHC;

/**
 * Shows all the people who haven't got a team yet.
 * <p>
 * Usage: /teamless
 * 
 * @author MrSugarCaney
 */
public class TeamlessCommand implements CommandExecutor {

	/**
	 * Main instance of the plugin.
	 */
	private GM4UHC plugin;

	public TeamlessCommand(GM4UHC plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Set<Player> teamless = new HashSet<>();
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (plugin.getTeamManager().getTeamByPlayer(player) == null) {
				teamless.add(player);
			}
		}
		
		if (teamless.size() == 0) {
			sender.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "All players are in a team! :)");
			return true;
		}

		StringBuilder message = new StringBuilder();
		message.append(ChatColor.DARK_GREEN);
		message.append(ChatColor.BOLD);
		message.append("Overview of all teamless players:\n");
		message.append(ChatColor.RESET);

		for (Player player : teamless) {
			message.append(">> ");
			message.append(player.getName());
			message.append("\n");
		}
		
		sender.sendMessage(message.toString().trim());

		return true;
	}

}
