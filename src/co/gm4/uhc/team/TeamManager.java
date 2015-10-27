package co.gm4.uhc.team;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

/**
 * Class managing the teams.
 * 
 * @author MrSugarCaney
 */
public class TeamManager {

	/**
	 * List of all teams.
	 */
	private List<Team> teams = new ArrayList<>();

	/**
	 * Removes the given player from their team.
	 * <p>
	 * This method will also clean up all the teams that have no members left.
	 * 
	 * @param player
	 *            The player to remove from their team.
	 */
	public void removeFromTeam(Player player) {
		Team team = getTeamByPlayer(player);

		if (team == null) {
			return;
		}

		team.getPlayers().remove(player.getUniqueId());
		removeEmptyTeams();
	}

	/**
	 * Get all the players that are currently not in a team.
	 * 
	 * @return A list of all players that don't have a team.
	 */
	public List<Player> getTeamlessPlayers() {
		List<Player> list = new ArrayList<>();

		for (Player player : Bukkit.getOnlinePlayers()) {
			boolean contains = false;

			for (Team team : teams) {
				if (team.getPlayers().contains(player.getUniqueId())) {
					contains = true;
					break;
				}
			}

			if (!contains) {
				list.add(player);
			}
		}

		return list;
	}

	/**
	 * Reads all the teams from a FileConfiguration.
	 * <p>
	 * Teams only get added when all the players of the team are online.
	 * 
	 * @param config
	 *            The fileconfiguration where the teams are stored.
	 * @return The amount of teams that were added.
	 */
	@SuppressWarnings("deprecation")
	public int parseTeams(FileConfiguration config) {
		int teamCount = 0;
		ConfigurationSection section = config.getConfigurationSection("teams");
		Set<String> keys = section.getKeys(false);

		for (String key : keys) {
			List<String> players = section.getStringList(key + ".members");
			ChatColor colour = ChatColor.valueOf(section.getString(key + ".color"));

			ChatColor accentInput = null;
			if (section.isSet(key + ".accent")) {
				accentInput = ChatColor.valueOf(section.getString(key + ".accent"));
			}
			String accent = (accentInput == null ? "" : accentInput + "");

			List<UUID> playerIds = new ArrayList<>();
			for (String playerName : players) {
				Player player = Bukkit.getPlayer(playerName);

				if (player == null) {
					continue;
				}

				if (!player.isOnline()) {
					playerIds.clear();
					break;
				}

				playerIds.add(player.getUniqueId());
			}

			if (playerIds.size() > 0) {
				Team team = new Team();
				team.getPlayers().addAll(playerIds);
				team.setColour(colour);
				team.setAccent(accent);
				teams.add(team);
				teamCount++;
			}
		}

		return teamCount;
	}

	/**
	 * Looks for the team the given player is in.
	 * 
	 * @param player
	 *            The player to check for.
	 * @return The Team the given player is in, or <i>null</i> if the player has
	 *         no team.
	 */
	public Team getTeamByPlayer(Player player) {
		for (Team team : teams) {
			if (team.getPlayers().contains(player.getUniqueId())) {
				return team;
			}
		}

		return null;
	}

	/**
	 * Looks for the team the given player is in.
	 * <p>
	 * Synchronized
	 * 
	 * @param player
	 *            The player to check for.
	 * @return The Team the given player is in, or <i>null</i> if the player has
	 *         no team.
	 */
	public synchronized Team getAsyncTeamByPlayer(Player player) {
		for (Team team : teams) {
			if (team.getAsyncPlayers().contains(player.getUniqueId())) {
				return team;
			}
		}

		return null;
	}

	/**
	 * Return a list of all teams that match a given predicate.
	 * 
	 * @param predicate
	 *            The predicate the teams should match.
	 * @return A list of all teams that match the predicate.
	 */
	public List<Team> filterTeams(Predicate<Team> predicate) {
		return teams.stream().filter(predicate).collect(Collectors.toList());
	}

	/**
	 * Creates a new team with the given members.
	 * <p>
	 * This will also remove all the players from their previous teams and
	 * delete the teams if they become empty because of this.
	 * 
	 * @param players
	 *            An array of all players to put together in a team.
	 * @return The freshly generated team.
	 */
	public Team newTeam(Player... players) {
		for (Player player : players) {
			for (Team team : teams) {
				if (team.getPlayers().contains(player.getUniqueId())) {
					team.getPlayers().remove(player.getUniqueId());
				}
			}
		}

		Team team = new Team();
		for (Player player : players) {
			team.getPlayers().add(player.getUniqueId());
		}
		teams.add(team);

		removeEmptyTeams();

		return team;
	}

	/**
	 * Creates a new team with the given members.
	 * <p>
	 * This will also remove all the players from their previous teams and
	 * delete the teams if they become empty because of this.
	 * 
	 * @param players
	 *            A collection of all players to put together in a team.
	 * @return The freshly generated team.
	 */
	public Team newTeam(Collection<Player> players) {
		for (Player player : players) {
			for (Team team : teams) {
				if (team.getPlayers().contains(player.getUniqueId())) {
					team.getPlayers().remove(player.getUniqueId());
				}
			}
		}

		Team team = new Team();
		for (Player player : players) {
			team.getPlayers().add(player.getUniqueId());
		}
		teams.add(team);

		removeEmptyTeams();

		return team;
	}

	/**
	 * Unregisters all the teams that have no members.
	 */
	public void removeEmptyTeams() {
		for (Iterator<Team> it = teams.iterator(); it.hasNext();) {
			if (it.next().getPlayers().isEmpty()) {
				it.remove();
			}
		}
	}

	public List<Team> getTeams() {
		return teams;
	}

}
