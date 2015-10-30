package co.gm4.uhc.setup;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.gm4.uhc.GM4UHC;
import co.gm4.uhc.chat.Broadcast;

/**
 * Sets the spawn point of the lobby.
 * <p>
 * Usage: /setlobby
 * 
 * @author MrSugarCaney
 */
public class SetLobbyCommand implements CommandExecutor {

	/**
	 * Main plugin instance.
	 */
	private GM4UHC plugin;

	public SetLobbyCommand(GM4UHC plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Broadcast.ERROR_PREFIX + "Only players can perform this command!");
			return true;
		}
		
		Location location = ((Player)sender).getLocation();
		plugin.setLobby(location);
		plugin.getConfig().set("lobby-location", location);
		plugin.saveConfig();

		return true;
	}

}
