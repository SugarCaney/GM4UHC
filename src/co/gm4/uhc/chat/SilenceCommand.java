package co.gm4.uhc.chat;

import java.util.List;
import java.util.UUID;
import java.util.Vector;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Prevents all non-mod messages to show up.
 * <p>
 * Usage: <i>/silence</i>
 * 
 * @author MrSugarCaney
 */
public class SilenceCommand implements CommandExecutor {

	/**
	 * List of all players that have the chat silenced.
	 */
	private List<UUID> silenced = new Vector<>();
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}
		
		Player player = (Player)sender;
		
		if (silenced.contains(player.getUniqueId())) {
			silenced.remove(player.getUniqueId());
			player.sendMessage(Broadcast.NOTIFICATION + "The chat is no longer silenced.");
		}
		else {
			silenced.add(player.getUniqueId());
			player.sendMessage(Broadcast.NOTIFICATION + "Your chat has been silenced.");
		}

		return true;
	}
	
	public boolean isSilenced(Player player) {
		return silenced.contains(player.getUniqueId());
	}

}
