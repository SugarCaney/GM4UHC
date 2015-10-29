package co.gm4.uhc.game;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import co.gm4.uhc.GM4UHC;
import co.gm4.uhc.Util;
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
		team.die(player);
		plugin.getTeamManager().removeEmptyTeams();

		// Remove player from the match.
		plugin.getMatch().remove(player);

		// Add player count.
		int playersLeft = plugin.getMatch().count();
		int teamsLeft = plugin.getTeamManager().getTeams().size();

		// Announce winner!
		if (teamsLeft == 1) {
			Team teamLeft = plugin.getTeamManager().getTeams().get(0);
			List<UUID> wonPlayers = new ArrayList<>();
			wonPlayers.addAll(teamLeft.getPlayers());
			wonPlayers.addAll(teamLeft.getDeaths());
			Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "WIN "
					+ teamLeft.getChatColours() + "Team #" + teamLeft.getId()
					+ " containing the players " + Util.toString(wonPlayers) + " has won!");
		}
		// Announce teams and players left.
		else {
			Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "DEATH "
					+ ChatColor.GREEN + "There are " + playersLeft + " players and " + teamsLeft
					+ " teams left.");
		}
	}

}
