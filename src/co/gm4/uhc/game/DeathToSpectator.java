package co.gm4.uhc.game;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import co.gm4.uhc.GM4UHC;
import co.gm4.uhc.team.Team;

/**
 * Will automatically convert players who die to spectator mode when the game
 * has started. It also modifies the death message.
 * 
 * @author MrSugarCaney
 */
public class DeathToSpectator implements Listener {

	/**
	 * The main plugin instance.
	 */
	private GM4UHC plugin;

	public DeathToSpectator(GM4UHC plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerDie(PlayerDeathEvent event) {
		if (plugin.getMatch().getState() != MatchState.RUNNING) {
			return;
		}

		Player player = (Player)event.getEntity();
		player.setGameMode(GameMode.SPECTATOR);
		player.setHealth(20);

		String message = event.getDeathMessage();

		for (UUID playerId : plugin.getMatch().getPlayers()) {
			Player p = Bukkit.getPlayer(playerId);
			Team t = plugin.getTeamManager().getTeamByPlayer(p);
			String name = t.getChatColours() + p.getName() + ChatColor.YELLOW;

			message = message.replace(p.getName(), name);
		}

		event.setDeathMessage(message);
	}

}
