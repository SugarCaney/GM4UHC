package co.gm4.uhc.team;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import co.gm4.uhc.GM4UHC;
import co.gm4.uhc.Util;

/**
 * This command will list all teams and members.
 * <p>
 * Usage: /teamlist
 * 
 * @author MrSugarCaney
 */
public class TeamListCommand implements CommandExecutor {

	/**
	 * Main instance of the plugin.
	 */
	private GM4UHC plugin;

	public TeamListCommand(GM4UHC plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		List<Team> teams = plugin.getTeamManager().getTeams();

		StringBuilder message = new StringBuilder();
		message.append(ChatColor.DARK_GREEN);
		message.append(ChatColor.BOLD);
		message.append("Overview of all ");
		message.append(teams.size());
		message.append(" team");
		message.append(teams.size() == 1 ? "" : "s");
		message.append(":\n");
		message.append(ChatColor.RESET);

		for (Team team : teams) {
			StringBuilder line = new StringBuilder();
			line.append(">> Team #");
			line.append(team.getId());
			line.append(": ");
			line.append(Util.toString(team.getPlayers()));
			line.append(" ");
			line.append(team.getChatColours());
			line.append("DISPLAY\n");
			message.append(line);
		}
		
		sender.sendMessage(message.toString().trim());

		return true;
	}

}
