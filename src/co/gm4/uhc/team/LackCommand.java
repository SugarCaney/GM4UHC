package co.gm4.uhc.team;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.gm4.uhc.GM4UHC;
import co.gm4.uhc.Util;

/**
 * This command will list all teams that are not complete, or have offline
 * members that makes it incomplete.
 * <p>
 * Usage: /lack
 * 
 * @author MrSugarCaney
 */
public class LackCommand implements CommandExecutor {

	/**
	 * Main instance of the plugin.
	 */
	private GM4UHC plugin;

	public LackCommand(GM4UHC plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		List<Team> teams = plugin.getTeamManager().getTeams();
		Set<Team> lacking = new HashSet<>();
		Set<UUID> offline = new HashSet<>();
		int size = plugin.getConfig().getInt("team-size");

		// Check all the teams.
		for (Team team : teams) {
			if (team.size() < size) {
				lacking.add(team);
			}

			for (UUID id : team.getPlayers()) {
				Player player = Bukkit.getPlayer(id);
				if (player == null) {
					lacking.add(team);
					offline.add(id);
					System.out.println("added " + id + " to offline");
				}
			}
		}

		// When everybody is well sorted.
		if (lacking.isEmpty()) {
			StringBuilder message = new StringBuilder();
			message.append(ChatColor.DARK_GREEN);
			message.append(ChatColor.BOLD);
			message.append("All teams are complete!");
			sender.sendMessage(message.toString());
			return true;
		}

		// Otherwise, list all the incomplete teams.
		StringBuilder message = new StringBuilder();
		message.append(ChatColor.DARK_GREEN);
		message.append(ChatColor.BOLD);
		message.append("Overview of all ");
		message.append(teams.size());
		message.append(" incomplete team");
		message.append(teams.size() == 1 ? "" : "s");
		message.append(":\n");
		message.append(ChatColor.RESET);

		for (Team team : lacking) {
			StringBuilder line = new StringBuilder();
			line.append(">> Team #");
			line.append(team.getId());
			line.append(": ");
			line.append(Util.toString(team.getPlayers()));
			line.append(" ");
			line.append(team.getChatColours());
			line.append("DISPLAY ");
			line.append(ChatColor.WHITE);
			List<UUID> offlinePlayers = team.getPlayers().stream().filter(p -> offline.contains(p))
					.collect(Collectors.toList());
			line.append(ChatColor.RED);
			line.append(Util.toString(offlinePlayers));
			line.append("\n");
			line.append(ChatColor.RESET);
			message.append(line);
		}

		sender.sendMessage(message.toString().trim());

		return true;
	}

}
