package co.gm4.uhc.team;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.gm4.uhc.GM4UHC;
import co.gm4.uhc.chat.Broadcast;

/**
 * This command will remove a player from their team.
 * <p>
 * Usage: /remove [player]
 * 
 * @author MrSugarCaney
 */
public class RemoveCommand implements CommandExecutor {

	/**
	 * Main instance of the plugin.
	 */
	private GM4UHC plugin;

	public RemoveCommand(GM4UHC plugin) {
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(Broadcast.ERROR_PREFIX + "Usage: /remove <player>");
			return false;
		}

		// Parse Player
		Player player = Bukkit.getPlayer(args[0]);
		if (player == null) {
			sender.sendMessage(Broadcast.ERROR_PREFIX + "Player " + args[0] + " doesn't exist!");
			return false;
		}

		// Remove from team.
		plugin.getTeamManager().removeFromTeam(player);

		sender.sendMessage(Broadcast.SUCCESS_PREFIX + "Player " + args[0] + " is now teamless!");

		return true;
	}

}
