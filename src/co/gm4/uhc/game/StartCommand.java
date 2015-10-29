package co.gm4.uhc.game;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import co.gm4.uhc.GM4UHC;

/**
 * This command will start the countdown.
 * <p>
 * Usage: /start
 * 
 * @author MrSugarCaney
 */
public class StartCommand implements CommandExecutor {

	/**
	 * Main instance of the plugin.
	 */
	private GM4UHC plugin;

	public StartCommand(GM4UHC plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		plugin.getMatch().startCountdown();
		
		return true;
	}

}
