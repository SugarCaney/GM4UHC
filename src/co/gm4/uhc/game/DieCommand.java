package co.gm4.uhc.game;

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
 * Removes a player from the game.
 * <p>
 * Usage: /die [player]
 * 
 * @author MrSugarCaney
 */
public class DieCommand implements CommandExecutor {

	/**
	 * Main instance of the plugin.
	 */
	private GM4UHC plugin;

	public DieCommand(GM4UHC plugin) {
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length < 2) {
			sender.sendMessage(Broadcast.ERROR_PREFIX + "Usage: /die <player> <reason>");
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

		player.setGameMode(GameMode.SPECTATOR);

		team.die(player.getUniqueId(), plugin);
		Bukkit.broadcastMessage(team.getChatColours() + player.getName() + ChatColor.YELLOW
				+ " was executed by MOD " + sender.getName() + " (Reason: " + reason + ")");

		return true;
	}

}
