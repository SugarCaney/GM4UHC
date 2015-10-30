package co.gm4.uhc.game;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.gm4.uhc.GM4UHC;
import co.gm4.uhc.chat.Broadcast;
import co.gm4.uhc.team.Team;

/**
 * Gives a player a warning. When a player has a set amount of warnings they
 * will get auto banned.
 * <p>
 * Usage: /warn [player] [reason]
 * 
 * @author MrSugarCaney
 */
public class WarnCommand implements CommandExecutor {

	/**
	 * Main instance of the plugin.
	 */
	private GM4UHC plugin;

	public WarnCommand(GM4UHC plugin) {
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length < 2) {
			sender.sendMessage(Broadcast.ERROR_PREFIX + "Usage: /warn <player> <reason>");
			return true;
		}

		// Check if player is online.
		Player player = Bukkit.getPlayer(args[0]);
		if (player == null) {
			sender.sendMessage(Broadcast.ERROR_PREFIX + args[0] + " doesn't exist!");
			return true;
		}

		Team team = plugin.getTeamManager().getTeamByPlayer(player);
		if (team == null) {
			sender.sendMessage(Broadcast.ERROR_PREFIX + args[0] + " doesn't participate!");
			return true;
		}

		// Make message
		StringBuilder sb = new StringBuilder(" ");
		for (int i = 1; i < args.length; i++) {
			sb.append(args[i]).append(" ");
		}
		String reason = sb.toString().trim();
		reason = reason.replaceAll("&", ChatColor.COLOR_CHAR + "");

		// Update warnings'
		UUID id = player.getUniqueId();
		Map<UUID, Integer> warns = plugin.getMatch().getWarnings();
		int allowedWarnings = plugin.getConfig().getInt("warnings");

		if (!warns.containsKey(id)) {
			warns.put(id, 0);
		}
		warns.put(id, warns.get(id) + 1);

		player.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "WARNING " + warns.get(id)
				+ "/" + allowedWarnings + ChatColor.RED + " You have been warned by "
				+ sender.getName() + ": " + reason);
		sender.sendMessage(Broadcast.SUCCESS_PREFIX + player.getName() + " now has " + warns.get(id)
				+ " warnings.");

		plugin.getLogger().info(player.getName() + " has received warning #" + warns.get(id)
				+ " from " + sender.getName());

		if (warns.get(id) == allowedWarnings) {
			team.die(player.getUniqueId(), plugin);
			Bukkit.broadcastMessage(team.getChatColours() + player.getName() + ChatColor.YELLOW
					+ " was executed due to too many warnings.");
			player.setGameMode(GameMode.SPECTATOR);
		}

		return true;
	}

}
