package co.gm4.uhc;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import co.gm4.uhc.chat.Broadcast;
import co.gm4.uhc.game.MatchState;
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
			if (team.getDeaths().contains(player.getUniqueId())) {
				player.setGameMode(GameMode.SPECTATOR);
			}

			String name = team.getChatColours() + player.getName() + ChatColor.YELLOW;
			event.setJoinMessage(event.getJoinMessage().replace(player.getName(), name));

			if (!plugin.getMatch().getTeleported().contains(player.getUniqueId())) {
				plugin.getMatch().getTeleported().add(player.getUniqueId());

				for (UUID id : team.getPlayers()) {
					Player teamMate = Bukkit.getPlayer(id);
					if (teamMate == null) {
						continue;
					}

					if (!teamMate.equals(player)) {
						player.teleport(teamMate);
						break;
					}
				}
			}
		}
		else if (!plugin.isOpen() || plugin.getMatch().getState() == MatchState.RUNNING) {
			player.setGameMode(GameMode.SPECTATOR);
			player.sendMessage(Broadcast.NOTIFICATION + "You are now spectating.");

			World world = Bukkit.getWorld(plugin.getConfig().getString("world-name"));
			Location spawn = world.getSpawnLocation();
			player.teleport(spawn);
		}

		// Teleport to lobby.
		if (plugin.getMatch().getState() == MatchState.LOBBY) {
			Location lobby = plugin.getLobby();
			if (lobby == null) {
				lobby = plugin.getServer().getWorld(plugin.getConfig().getString("lobby-name"))
						.getSpawnLocation();
			}
			player.teleport(lobby);
			player.getInventory().clear();
			player.setGameMode(GameMode.SURVIVAL);
			player.setHealth(20);
			player.setFoodLevel(20);
			player.setExp(0);
			player.setLevel(0);
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

			if (!plugin.getMatch().getOffline().containsKey(player.getUniqueId())) {
				plugin.getMatch().getOffline().put(player.getUniqueId(), 0);
			}
		}
	}

}
