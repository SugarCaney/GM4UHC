package co.gm4.uhc.chat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import co.gm4.uhc.GM4UHC;
import co.gm4.uhc.Permission;
import co.gm4.uhc.team.Team;

/**
 * @author MrSugarCaney
 */
public class ChatListener implements Listener {

	/**
	 * Main plugin instance.
	 */
	private GM4UHC plugin;

	public ChatListener(GM4UHC plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void manageChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		boolean mod = player.hasPermission(Permission.MODERATOR);
		String message = event.getMessage();

		// Check for mute
		if (plugin.getMute().isMuted(player)) {
			event.setCancelled(true);
			player.sendMessage(Broadcast.NOTIFICATION + "You can't talk. You have been muted.");
			return;
		}

		// Determine player colour.
		Team team = plugin.getTeamManager().getAsyncTeamByPlayer(player);
		ChatColor color = null;
		if (team == null) {
			String colorName = plugin.getConfig().getString("colours." + player.getName());
			color = ChatColor.valueOf(colorName);
		}
		else {
			color = team.getAsyncColour();
		}

		// Change chat message
		event.setCancelled(true);

		String newMessage = "";
		if (mod) {
			StringBuilder sb = new StringBuilder("");
			sb.append(color);
			sb.append(ChatColor.ITALIC);
			sb.append(ChatColor.BOLD);
			sb.append("MOD");
			sb.append(color);
			sb.append(" ");
			sb.append(player.getName());
			sb.append(ChatColor.WHITE);
			sb.append(": ");
			sb.append(message);
			newMessage = sb.toString();
		}
		else {
			newMessage = color + player.getName() + ChatColor.GRAY + ": " + message;
		}

		Bukkit.broadcastMessage(newMessage);
	}

}
