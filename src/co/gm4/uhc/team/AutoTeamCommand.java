package co.gm4.uhc.team;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.gm4.uhc.GM4UHC;
import co.gm4.uhc.chat.Broadcast;

/**
 * Adds a given player to the first empty spot in a non-full team.
 * <p>
 * Usage: /autoteam [player]
 * 
 * @author MrSugarCaney
 */
public class AutoTeamCommand implements CommandExecutor {

	/**
	 * Main instance of the plugin.
	 */
	private GM4UHC plugin;

	public AutoTeamCommand(GM4UHC plugin) {
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(Broadcast.ERROR_PREFIX + "Usage: /autoteam <player>");
			return false;
		}

		// Check if player is online.
		Player player = Bukkit.getPlayer(args[0]);
		if (player == null) {
			sender.sendMessage(Broadcast.ERROR_PREFIX + args[0] + " doesn't exist!");
			return false;
		}

		TeamManager tm = plugin.getTeamManager();
		int teamSize = plugin.getConfig().getInt("team-size");
		Team notEnough = null;

		// Look for the first full team.
		for (Team team : tm.getTeams()) {
			if (team.size() < teamSize) {
				notEnough = team;
				break;
			}
		}

		// If all teams are full, show message.
		if (notEnough == null) {
			sender.sendMessage(Broadcast.ERROR_PREFIX + "There are no available spots left.");
			return false;
		}

		// Also no spots left when the last team already contains the player.
		if (notEnough.contains(player)) {
			sender.sendMessage(Broadcast.ERROR_PREFIX + "There are no available spots left.");
			return false;
		}

		notEnough.add(player);

		sender.sendMessage(Broadcast.SUCCESS_PREFIX + player.getName() + " has been put in team #"
				+ notEnough.getId());

		player.sendMessage(
				Broadcast.NOTIFICATION + "You have been placed in team #" + notEnough.getId());

		return true;
	}

}
