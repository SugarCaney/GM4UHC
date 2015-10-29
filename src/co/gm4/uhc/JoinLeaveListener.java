package co.gm4.uhc;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import co.gm4.uhc.team.Team;

/**
 * Handles players joining and leaving the server.
 * 
 * @author MrSugarCaney
 */
public class JoinLeaveListener implements Listener {

	/**
	 * The main plugin instance.
	 */
	private GM4UHC plugin;

	public JoinLeaveListener(GM4UHC plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		Team team = plugin.getTeamManager().getTeamByPlayer(player);

		// Add colours to join message.
		if (team != null) {
			String name = team.getChatColours() + player.getName() + ChatColor.YELLOW;
			event.setJoinMessage(event.getJoinMessage().replace(player.getName(), name));
		}
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		Team team = plugin.getTeamManager().getTeamByPlayer(player);

		// Add colours to join message.
		if (team != null) {
			String name = team.getChatColours() + player.getName() + ChatColor.YELLOW;
			event.setQuitMessage(event.getQuitMessage().replace(player.getName(), name));
		}
	}

}
