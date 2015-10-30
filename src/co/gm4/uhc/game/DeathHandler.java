package co.gm4.uhc.game;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import co.gm4.uhc.GM4UHC;
import co.gm4.uhc.team.Team;

/**
 * Handles all administrative things needed to happen when somebody dies during
 * a game.
 * 
 * @author MrSugarCaney
 */
public class DeathHandler implements Listener {

	/**
	 * The main plugin instance.
	 */
	private GM4UHC plugin;

	public DeathHandler(GM4UHC plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		if (plugin.getMatch().getState() != MatchState.RUNNING) {
			return;
		}
		
		Player player = event.getEntity();
		Team team = plugin.getTeamManager().getTeamByPlayer(player);

		// Remove player from team.
		team.die(player.getUniqueId(), plugin);
	}

}
