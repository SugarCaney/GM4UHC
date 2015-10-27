package co.gm4.uhc.team;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import co.gm4.uhc.GM4UHC;
import co.gm4.uhc.chat.Broadcast;

/**
 * This command will read the teams from the config.
 * <p>
 * Usage: /parseteams
 * 
 * @author MrSugarCaney
 */
public class ParseCommand implements CommandExecutor {

	/**
	 * Main instance of the plugin.
	 */
	private GM4UHC plugin;

	public ParseCommand(GM4UHC plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		int amount = plugin.getTeamManager().parseTeams(plugin.getConfig());

		sender.sendMessage(Broadcast.SUCCESS_PREFIX + amount + " team"
				+ (amount == 1 ? " has" : "s have") + " been loaded from the configuration file.");

		return true;
	}

}
